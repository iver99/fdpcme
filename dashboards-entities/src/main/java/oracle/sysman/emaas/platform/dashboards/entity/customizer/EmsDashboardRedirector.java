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

import java.math.BigInteger;

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

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

/**
 * @author guochen
 *
 */
public class EmsDashboardRedirector implements QueryRedirector
{
	private static final long serialVersionUID = 7133740733527045556L;
	
	private static final Logger LOGGER = LogManager.getLogger(EmsDashboardRedirector.class);

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
			EmsDashboard ed = (EmsDashboard) doq.getObject();
			LOGGER.info("Soft deletion: instead of deleting dashboard with id={} and name={}, it's 'deleted' field is updated", ed.getDashboardId(), ed.getName());
			ed.setDeleted(BigInteger.ONE);
			UpdateObjectQuery uoq = new UpdateObjectQuery(ed);
			cd.addDirectQueryKey("deleted", "DELETED");
			uoq.setDescriptor(cd);
			doq.setDescriptor(uoq.getDescriptor());
			Object rtn = uoq.execute((AbstractSession) session, (AbstractRecord) arguments);
			return rtn;
		}
		else if (query.isInsertObjectQuery()) {// remove the soft deleted object before insertion
			InsertObjectQuery ioq = (InsertObjectQuery) query;
			EmsDashboard ed = (EmsDashboard) ioq.getObject();

			UnitOfWork uow = session.acquireUnitOfWork();
			String delSql = "DELETE FROM EMS_DASHBOARD WHERE NAME='" + ed.getName() + "' AND OWNER='" + ed.getOwner()
					+ "' AND TENANT_ID=" + session.getActiveSession().getProperty("tenant.id") + " AND DELETED=1";
			LOGGER.info("Soft deletion: before inserting dashboard with id={} and name={}, ensure previouly soft deleted object is hard deleted. SQL is {}", ed.getDashboardId(), ed.getName(), delSql);
			uow.executeNonSelectingCall(new SQLCall(delSql));
			uow.commit();

			ioq = new InsertObjectQuery(ed);
			ioq.setDoNotRedirect(true);
			InsertObjectQuery old = cd.getQueryManager().getInsertQuery();
			cd.getQueryManager().setInsertQuery(ioq);
			Object rtn = ioq.execute((AbstractSession) session, (AbstractRecord) arguments);
			cd.getQueryManager().setInsertQuery(old);
			return rtn;
		}
		else {
			return query.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
	}

}
