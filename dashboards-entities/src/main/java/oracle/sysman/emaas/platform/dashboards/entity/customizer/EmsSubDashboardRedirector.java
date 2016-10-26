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

import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;

/**
 * @author guochen
 */
public class EmsSubDashboardRedirector implements QueryRedirector
{
	private static final long serialVersionUID = 9018162680358428654L;
	
	private static final Logger LOGGER = LogManager.getLogger(EmsSubDashboardRedirector.class);

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
			EmsSubDashboard esd = (EmsSubDashboard) doq.getObject();
			esd.setDeleted(true);
			UpdateObjectQuery uoq = new UpdateObjectQuery(esd);
			LOGGER.info("Soft deletion: instead of deleting sub dashboard with ID={}, it's 'deleted' field is updated", esd.getDashboardSetId());
			cd.addDirectQueryKey("deleted", "DELETED");
			uoq.setDescriptor(cd);
			doq.setDescriptor(uoq.getDescriptor());
			uoq.setDoNotRedirect(true);
			return uoq.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
		else if (query.isInsertObjectQuery()) {// remove the soft deleted object before insertion
			InsertObjectQuery ioq = (InsertObjectQuery) query;
			EmsSubDashboard esd = (EmsSubDashboard) ioq.getObject();
			LOGGER.info("Soft deletion: before inserting sub dashboard with ID={}, ensure previouly soft deleted object is hard deleted", esd.getDashboardSetId());

			UnitOfWork uow = session.acquireUnitOfWork();
			String sql = "DELETE FROM EMS_DASHBOARD_SET p WHERE p.DASHBOARD_SET_ID='" + esd.getDashboardSetId()
					+ "' AND p.SUB_DASHBOARD_ID='" + esd.getSubDashboardId() + "' AND p.TENANT_ID='"
					+ session.getActiveSession().getProperty("tenant.id") + "' AND p.DELETED=1";
			uow.executeNonSelectingCall(new SQLCall(sql));
			uow.commit();

			ioq = new InsertObjectQuery(esd);
			ioq.setDoNotRedirect(true);
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
