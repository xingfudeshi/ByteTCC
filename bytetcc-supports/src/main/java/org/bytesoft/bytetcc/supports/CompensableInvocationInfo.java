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
package org.bytesoft.bytetcc.supports;

import java.io.ObjectStreamException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompensableInvocationInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	static final Logger logger = LoggerFactory.getLogger(CompensableInvocationInfo.class);

	private String declaringClass;
	private String methodName;
	private String[] parameterTypeArray;
	private Object[] args;
	private String confirmableKey;
	private String cancellableKey;
	private Object identifier;

	private boolean simplified;

	protected Object readResolve() throws ObjectStreamException {
		CompensableInvocationImpl that = new CompensableInvocationImpl();

		that.setArgs(this.args);
		that.setConfirmableKey(this.confirmableKey);
		that.setCancellableKey(this.cancellableKey);
		that.setIdentifier(this.identifier);
		that.setSimplified(this.simplified);

		that.setDeclaringClass(this.declaringClass);
		that.setMethodName(this.methodName);
		that.setParameterTypeArray(this.parameterTypeArray);

		return that;
	}

	public String getDeclaringClass() {
		return declaringClass;
	}

	public void setDeclaringClass(String declaringClass) {
		this.declaringClass = declaringClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String[] getParameterTypeArray() {
		return parameterTypeArray;
	}

	public void setParameterTypeArray(String[] parameterTypeArray) {
		this.parameterTypeArray = parameterTypeArray;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getConfirmableKey() {
		return confirmableKey;
	}

	public void setConfirmableKey(String confirmableKey) {
		this.confirmableKey = confirmableKey;
	}

	public String getCancellableKey() {
		return cancellableKey;
	}

	public void setCancellableKey(String cancellableKey) {
		this.cancellableKey = cancellableKey;
	}

	public Object getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Object identifier) {
		this.identifier = identifier;
	}

	public boolean isSimplified() {
		return simplified;
	}

	public void setSimplified(boolean simplified) {
		this.simplified = simplified;
	}

}
