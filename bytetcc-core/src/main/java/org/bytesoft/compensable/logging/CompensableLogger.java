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
package org.bytesoft.compensable.logging;

import org.bytesoft.compensable.archive.CompensableArchive;
import org.bytesoft.compensable.archive.TransactionArchive;
import org.bytesoft.transaction.archive.XAResourceArchive;
import org.bytesoft.transaction.recovery.TransactionRecoveryCallback;

public interface CompensableLogger {

	/* transaction */
	public void createTransaction(TransactionArchive archive);

	public void updateTransaction(TransactionArchive archive);

	public void deleteTransaction(TransactionArchive archive);

	/* participant */
	public void createParticipant(XAResourceArchive archive);

	public void updateParticipant(XAResourceArchive archive);

	public void deleteParticipant(XAResourceArchive archive);

	/* compensable */
	public void createCompensable(CompensableArchive archive);

	public void updateCompensable(CompensableArchive archive);

	/* recovery */
	public void recover(TransactionRecoveryCallback callback);

}
