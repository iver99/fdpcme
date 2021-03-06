/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.entity.customizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.DeleteObjectQuery;
import org.eclipse.persistence.queries.InsertObjectQuery;
import org.eclipse.persistence.queries.QueryRedirector;
import org.eclipse.persistence.queries.SQLCall;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;

import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;

/**
 * @author guochen
 */
public class EmsPreferenceRedirector implements QueryRedirector
{
	private static final long serialVersionUID = 8558823239804439205L;
	
	private static final Logger LOGGER = LogManager.getLogger(EmsPreferenceRedirector.class);

	/* (non-Javadoc)
	 * @see org.eclipse.persistence.queries.QueryRedirector#invokeQuery(org.eclipse.persistence.queries.DatabaseQuery, org.eclipse.persistence.sessions.Record, org.eclipse.persistence.sessions.Session)
	 */
	@Override
	public Object invokeQuery(DatabaseQuery query, Record arguments, Session session)
	{
		ClassDescriptor cd = session.getDescriptor(query.getReferenceClass());
		Object permDelete = session.getActiveSession().getProperty("soft.deletion.permanent");
		LOGGER.info("Redirector: permanent deletion parameter is {}", permDelete);
		if (query.isDeleteObjectQuery() && !Boolean.TRUE.equals(permDelete)) {// soft deletion
			DeleteObjectQuery doq = (DeleteObjectQuery) query;
			EmsPreference pre = (EmsPreference) doq.getObject();
			LOGGER.info("Soft deletion: instead of deleting dashboard preference with key={}, it's 'deleted' field is updated", pre.getPrefKey());
			pre.setDeleted(true);
			UpdateObjectQuery uoq = new UpdateObjectQuery(pre);
			cd.addDirectQueryKey("deleted", "DELETED");
			uoq.setDescriptor(cd);
			doq.setDescriptor(uoq.getDescriptor());
			uoq.setDoNotRedirect(true);
			return uoq.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
		else if (query.isInsertObjectQuery()) {// remove the soft deleted object before insertion
			InsertObjectQuery ioq = (InsertObjectQuery) query;
			EmsPreference pre = (EmsPreference) ioq.getObject();
			LOGGER.info("Soft deletion: before inserting dashboard preference with key={}, ensure previouly soft deleted object is hard deleted", pre.getPrefKey());

			UnitOfWork uow = session.acquireUnitOfWork();
			String delSql = "DELETE FROM EMS_PREFERENCE WHERE USER_NAME='" + pre.getUserName() + "' AND PREF_KEY='"
					+ pre.getPrefKey() + "' AND TENANT_ID=" + session.getActiveSession().getProperty("curTenantId")
					+ " AND DELETED=1";
			uow.executeNonSelectingCall(new SQLCall(delSql));
			uow.commit();

			ioq = new InsertObjectQuery(pre);
			ioq.setDoNotRedirect(true);// avoid endless looping
			InsertObjectQuery old = cd.getQueryManager().getInsertQuery();
			cd.getQueryManager().setInsertQuery(ioq);
			Object rtn = ioq.execute((AbstractSession) session, (AbstractRecord) arguments);
			cd.getQueryManager().setInsertQuery(old);
			return rtn;
		} 
		else {
			query.setDoNotRedirect(true);
			return query.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
	}

}
