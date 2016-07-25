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
import org.eclipse.persistence.queries.QueryRedirector;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;

/**
 * @author guochen
 */
public class EmsDashboardTileRedirector implements QueryRedirector
{
	private static final long serialVersionUID = 2647497341914181486L;

	/* (non-Javadoc)
	 * @see org.eclipse.persistence.queries.QueryRedirector#invokeQuery(org.eclipse.persistence.queries.DatabaseQuery, org.eclipse.persistence.sessions.Record, org.eclipse.persistence.sessions.Session)
	 */
	@Override
	public Object invokeQuery(DatabaseQuery query, Record arguments, Session session)
	{
		ClassDescriptor cd = session.getDescriptor(query.getReferenceClass());
		if (query.isDeleteObjectQuery()) {// soft deletion
			DeleteObjectQuery doq = (DeleteObjectQuery) query;
			EmsDashboardTile edt = (EmsDashboardTile) doq.getObject();
			edt.setDeleted(true);
			UpdateObjectQuery uoq = new UpdateObjectQuery(edt);
			cd.addDirectQueryKey("deleted", "DELETED");
			uoq.setDescriptor(cd);
			doq.setDescriptor(uoq.getDescriptor());
			return uoq.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
		else {
			return query.execute((AbstractSession) session, (AbstractRecord) arguments);
		}
	}
}
