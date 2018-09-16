/**
 * Copyright 2014-2016 yangming.liu<bytefox@126.com>.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, see <http://www.gnu.org/licenses/>.
 */
package org.bytesoft.bytetcc.supports.spring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bytesoft.compensable.CompensableBeanFactory;
import org.bytesoft.compensable.aware.CompensableBeanFactoryAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CompensableBeanFactoryPostProcessor
		implements BeanFactoryPostProcessor, BeanPostProcessor, SmartInitializingSingleton, ApplicationContextAware {
	static final Logger logger = LoggerFactory.getLogger(CompensableBeanFactoryPostProcessor.class);

	private ApplicationContext applicationContext;

	public void afterSingletonsInstantiated() {
		Map<String, CompensableBeanFactoryAware> beanMap = //
				this.applicationContext.getBeansOfType(CompensableBeanFactoryAware.class);
		Iterator<Map.Entry<String, CompensableBeanFactoryAware>> iterator = //
				(beanMap == null) ? null : beanMap.entrySet().iterator();
		while (iterator != null && iterator.hasNext()) {
			Map.Entry<String, CompensableBeanFactoryAware> entry = iterator.next();
			CompensableBeanFactoryAware bean = entry.getValue();
			this.initializeCompensableBeanFactoryIfNecessary(bean);
		}
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (CompensableBeanFactoryAware.class.isInstance(bean)) {
			this.initializeCompensableBeanFactoryIfNecessary((CompensableBeanFactoryAware) bean);
		} // end-if (CompensableBeanFactoryAware.class.isInstance(bean))

		return bean;
	}

	private void initializeCompensableBeanFactoryIfNecessary(CompensableBeanFactoryAware aware) {
		if (aware.getBeanFactory() == null) {
			CompensableBeanFactory beanFactory = //
					this.applicationContext.getBean(CompensableBeanFactory.class);
			aware.setBeanFactory(beanFactory);
		} // end-if (aware.getBeanFactory() == null)
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		String beanFactoryBeanId = null;
		String interceptorBeanId = null;
		List<BeanDefinition> beanFactoryAwareBeanIdList = new ArrayList<BeanDefinition>();
		String[] beanNameArray = beanFactory.getBeanDefinitionNames();
		for (int i = 0; i < beanNameArray.length; i++) {
			String beanName = beanNameArray[i];
			BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
			String beanClassName = beanDef.getBeanClassName();

			Class<?> beanClass = null;
			try {
				beanClass = cl.loadClass(beanClassName);
			} catch (Exception ex) {
				logger.debug("Cannot load class {}, beanId= {}!", beanClassName, beanName, ex);
				continue;
			}

			if (CompensableBeanFactoryAware.class.isAssignableFrom(beanClass)) {
				beanFactoryAwareBeanIdList.add(beanDef);
			}

			if (CompensableBeanFactory.class.isAssignableFrom(beanClass)) {
				if (beanFactoryBeanId == null) {
					beanFactoryBeanId = beanName;
				} else {
					throw new FatalBeanException("Duplicated org.bytesoft.compensable.CompensableBeanFactory defined.");
				}
			} else if (CompensableMethodInterceptor.class.equals(beanClass)) {
				if (interceptorBeanId == null) {
					interceptorBeanId = beanName;
				} else {
					throw new IllegalStateException(
							"Duplicated org.bytesoft.bytetcc.supports.spring.CompensableMethodInterceptor defined.");
				}
			}

		}

		if (beanFactoryBeanId == null) {
			throw new IllegalStateException(
					"No configuration of class org.bytesoft.compensable.CompensableBeanFactory was found.");
		}
		if (interceptorBeanId == null) {
			throw new IllegalStateException(
					"No configuration of class org.bytesoft.bytetcc.supports.spring.CompensableMethodInterceptor was found.");
		}

		for (int i = 0; i < beanFactoryAwareBeanIdList.size(); i++) {
			BeanDefinition beanDef = beanFactoryAwareBeanIdList.get(i);
			MutablePropertyValues mpv = beanDef.getPropertyValues();
			RuntimeBeanReference beanRef = new RuntimeBeanReference(beanFactoryBeanId);
			mpv.addPropertyValue(CompensableBeanFactoryAware.BEAN_FACTORY_FIELD_NAME, beanRef);
		}

		BeanDefinition beanDef = beanFactory.getBeanDefinition(beanFactoryBeanId);
		MutablePropertyValues mpv = beanDef.getPropertyValues();
		RuntimeBeanReference synchronization = new RuntimeBeanReference(interceptorBeanId);
		mpv.addPropertyValue("compensableSynchronization", synchronization);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
