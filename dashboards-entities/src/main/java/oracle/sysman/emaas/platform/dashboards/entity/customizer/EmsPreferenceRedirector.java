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

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.DeleteObjectQuery;
import org.eclipse.persistence.queries.InsertObjectQuery;
import org.eclipse.persistence.queries.QueryRedirector;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;

import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;

/**
 * @author guochen
 */
public class EmsPreferenceRedirector implements QueryRedirector
{
	private static final long serialVersionUID = 8558823239804439205L;

	/* (non-Javadoc)
	 * @see org.eclipse.persistence.queries.QueryRedirector#invokeQuery(org.eclipse.persistence.queries.DatabaseQuery, org.eclipse.persistence.sessions.Record, org.eclipse.persistence.sessions.Session)
	 */
	@Override
	public Object invokeQuery(DatabaseQuery query, Record arguments, Session session)
	{
		ClassDescriptor cd = session.getDescriptor(query.getReferenceClass());
		if (query.isDeleteObjectQuery()) {// soft deletion
			DeleteObjectQuery doq = (DeleteObjectQuery) query;
			EmsPreference pre = (EmsPreference) doq.getObject();
			pre.setDeleted(true);
			UpdateObjectQuery uoq = new UpdateObjectQuery(pre);
			cd.addDirectQueryKey("deleted", "DELETED");
			uoq.setDescriptor(cd);
			doq.setDescriptor(uoq.getDescriptor());
			return uoq.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
		else if (query.isInsertObjectQuery()) {// remove the soft deleted object before insertion
			InsertObjectQuery ioq = (InsertObjectQuery) query;
			EmsPreference pre = (EmsPreference) ioq.getObject();

			// try to get the soft deleted obj
			//			session.executeNonSelectingSQL("DELETE FROM EMS_PREFERENCE WHERE USER_NAME='" + pre.getUserName() + "' AND PREF_KEY='"
			//					+ pre.getPrefKey() + "' AND TENANT_ID=" + session.getProperty("tenant.id") + " AND DELETED=1");
			//			EmsPreference del = new EmsPreference();
			//			del.setDeleted(Boolean.TRUE);
			//			del.setPrefKey(pre.getPrefKey());
			//			del.setUserName(pre.getUserName());
			//			DeleteObjectQuery doq = new DeleteObjectQuery(del);
			//			doq.setDoNotRedirect(true);
			//			String delSql = "DELETE FROM EMS_PREFERENCE WHERE USER_NAME='" + pre.getUserName() + "' AND PREF_KEY='"
			//					+ pre.getPrefKey() + "' AND TENANT_ID=" + session.getProperty("tenant.id") + " AND DELETED=1";
			//			doq.setSQLString(delSql);
			//			DeleteObjectQuery oldDoq = cd.getQueryManager().getDeleteQuery();
			//			cd.getQueryManager().setDeleteQuery(doq);
			//			//			session.executeQuery(doq);
			//			//			Object rtn = doq.execute((AbstractSession) session, (AbstractRecord) arguments);
			//			((AbstractSession) session).deleteObject(del);
			//			//			session.getActiveUnitOfWork().commitAndResume();
			//			cd.getQueryManager().setDeleteQuery(oldDoq);

			// do the normal insertion
			InsertObjectQuery ioq2 = new InsertObjectQuery(pre);
			ioq2.setDoNotRedirect(true);
			InsertObjectQuery old = cd.getQueryManager().getInsertQuery();
			cd.getQueryManager().setInsertQuery(ioq2);
			Object rtn2 = ioq2.execute((AbstractSession) session, (AbstractRecord) arguments);
			//			((AbstractSession) session).insertObject(rtn2);
			cd.getQueryManager().setInsertQuery(old);
			return rtn2;
		}
		else {
			return query.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
	}

}
