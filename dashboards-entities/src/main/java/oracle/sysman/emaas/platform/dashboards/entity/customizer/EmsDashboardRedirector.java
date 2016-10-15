package oracle.sysman.emaas.platform.dashboards.entity.customizer;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.sessions.AbstractRecord;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.queries.DatabaseQuery;
import org.eclipse.persistence.queries.DeleteObjectQuery;
import org.eclipse.persistence.queries.QueryRedirector;
import org.eclipse.persistence.queries.UpdateObjectQuery;
import org.eclipse.persistence.sessions.Record;
import org.eclipse.persistence.sessions.Session;

public class EmsDashboardRedirector implements QueryRedirector {

	@Override
	public Object invokeQuery(DatabaseQuery query, Record arguments,
			Session session) {
		ClassDescriptor cd = session.getDescriptor(query.getReferenceClass());
		if (query.isDeleteObjectQuery()) {// soft deletion
			DeleteObjectQuery doq = (DeleteObjectQuery) query;
			EmsDashboard pre = (EmsDashboard) doq.getObject();
			pre.setDeleted(pre.getDashboardId());
			UpdateObjectQuery uoq = new UpdateObjectQuery(pre);
			cd.addDirectQueryKey("deleted", "DELETED");
			uoq.setDescriptor(cd);
			doq.setDescriptor(uoq.getDescriptor());
			return uoq.execute((AbstractSession) session,
					(AbstractRecord) arguments);
		} else {
			return query.execute((AbstractSession) session,
					(AbstractRecord) arguments);
		}
	}

}
