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
import org.eclipse.persistence.queries.SQLCall;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.UnitOfWork;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

/**
 * @author guochen
 */
public class EmsDashboardTileParamsRedirector implements QueryRedirector
{
	private static final long serialVersionUID = -4080358817498970850L;

	/* (non-Javadoc)
	 * @see org.eclipse.persistence.queries.QueryRedirector#invokeQuery(org.eclipse.persistence.queries.DatabaseQuery, org.eclipse.persistence.sessions.Record, org.eclipse.persistence.sessions.Session)
	 */
	@Override
	public Object invokeQuery(DatabaseQuery query, Record arguments, Session session)
	{
		ClassDescriptor cd = session.getDescriptor(query.getReferenceClass());
		if (query.isDeleteObjectQuery()) {// soft deletion
			DeleteObjectQuery doq = (DeleteObjectQuery) query;
			EmsDashboardTileParams edtp = (EmsDashboardTileParams) doq.getObject();
			edtp.setDeleted(true);
			UpdateObjectQuery uoq = new UpdateObjectQuery(edtp);
			cd.addDirectQueryKey("deleted", "DELETED");
			uoq.setDescriptor(cd);
			doq.setDescriptor(uoq.getDescriptor());
			Object rtn = uoq.execute((AbstractSession) session, (AbstractRecord) arguments);
			return rtn;
		}
		else if (query.isInsertObjectQuery()) {// remove the soft deleted object before insertion
			InsertObjectQuery ioq = (InsertObjectQuery) query;
			EmsDashboardTileParams edtp = (EmsDashboardTileParams) ioq.getObject();

			UnitOfWork uow = session.acquireUnitOfWork();
			String delSql = "DELETE FROM EMS_DASHBOARD_TILE_PARAMS WHERE TILE_ID='" + edtp.getDashboardTile().getTileId()
					+ "' AND PARAM_NAME='" + edtp.getParamName() + "' AND TENANT_ID="
					+ session.getActiveSession().getProperty("tenant.id") + " AND DELETED=1";
			uow.executeNonSelectingCall(new SQLCall(delSql));
			uow.commit();

			ioq = new InsertObjectQuery(edtp);
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
