package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.AppContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;

public class DashboardManager {
	private static DashboardManager instance;
	
	static {
		instance = new DashboardManager();
	}
	
	private DashboardManager() {
	}
	
	/**
	 * Returns the singleton instance for dashboard manager
	 * @return
	 */
	public static DashboardManager getInstance() {
		return instance;
	}
	
	/**
	 * Save a newly created dashboard, or update an existing dashboard for given tenant
	 * @param dbd
	 * @param tenantId
	 * @return the dashboard saved or updated
	 */
	public Dashboard saveOrUpdateDashboard(Dashboard dbd, String tenantId) throws DashboardException {
		if (dbd == null)
			return null;
		EntityManager em = PersistenceManager.getInstance().createEntityManager(tenantId);
		try {
			String currentUser = AppContext.getInstance().getCurrentUser();
			if (dbd.getDashboardId() == null) {
				Date creationDate = new Date();
				dbd.setCreationDate(creationDate);
				dbd.setOwner(currentUser);
				EmsDashboard ed = dbd.getPersistenceEntity(null);
				// TODO: persistent to db
//				em.getTransaction().begin();
//				em.persist(ed);
//				em.getTransaction().commit();
//				em.refresh(ed);
				return Dashboard.valueOf(ed, dbd);
			}
			else {
				// TODO: retrieve dashboard from entity manager
				EmsDashboard ed = null;
				ed.setLastModificationDate(new Date());
				ed.setLastModifiedBy(currentUser);
				
				// TODO: a lot to be done..
			}
		} 
		finally {
			if (em!= null)
				em.close();
		}
		return null;
	}
	
	/**
	 * Delete a dashboard specified by dashboard id for given tenant. Soft deletion is supported
	 * 
	 * @param dashboardId
	 * @param tenantId
	 */
	public void deleteDashboard(Long dashboardId, String tenantId) {
		deleteDashboard(dashboardId, false, tenantId);
	}
	
	/**
	 * Delete a dashboard specified by dashboard id for given tenant.
	 * @param dashboardId id for the dashboard
	 * @param permernant delete permernantly or not
	 */
	public void deleteDashboard(Long dashboardId, boolean permernant, String tenantId) {
		// TODO: delete last access
//		EmsDashboardLastAccess edla = 
		// TODO: delete the dashboard
	}
	
	/**
	 * Enables or disables the 'include time control' settings for specified dashboard
	 * @param dashboardId
	 * @param enable
	 * @param tenantId
	 */
	public void setDashboardIncludeTimeControl(Long dashboardId, boolean enable, String tenantId) {
	}
	
	/**
	 * Returns dashboard instance by specifying the id
	 * @param dashboardId
	 * @return
	 */
	public Dashboard getDashboard(Long dashboardId, String tenantId) {
		return null;
	}
	
	/**
	 * Returns all dashboards
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> listAllDashboards(String tenantId) {
		return null;
	}
	
	/**
	 * Returns dashboards for specified page with given page size
	 * @param page number to indicate page number, started from 1
	 * @param pageSize
	 * @param tenantId
	 * @param ic ignore case or not
	 * @return
	 */
	public List<Dashboard> listDashboards(Integer page, Integer pageSize, String tenantId, boolean ic) {
		return listDashboards(null, page, pageSize, tenantId, ic);
	}
	
	/**
	 * Returns dashboards for specified query string, by providing page number and page size
	 * @param queryString
	 * @param page number to indicate page index, started from 1
	 * @param pageSize
	 * @param tenantId
	 * @param ic ignore case or not
	 * @return
	 */
	public List<Dashboard> listDashboards(String queryString, Integer page, Integer pageSize, String tenantId, boolean ic) {
		String jpql = "select p from EmsDashboard p";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (queryString != null && !"".equals(queryString)) {
			StringBuilder sb = new StringBuilder();
			Locale locale = AppContext.getInstance().getLocale();
			if (!ic) {
				sb.append(" p.name LIKE :name");
				paramMap.put("name", "%" + queryString + "%");
			} else {
				sb.append(" lower(p.name) LIKE :name");
				paramMap.put("name", "%" + queryString.toLowerCase(locale) + "%");
			}
			if (!ic) {
				sb.append(" and p.description like :description");
				paramMap.put("description", "%" + queryString + "%");
			} else {
				sb.append(" and lower(p.description) like :description");
				paramMap.put("description", "%" + queryString.toLowerCase(locale) + "%");
			}
			sb.append(" and p.owner = :owner");
			paramMap.put("owner", queryString);
			jpql += sb.toString();
		}
		// TODO: get EntityManager with tenantId
		EntityManager em = null;
		Query query = em.createQuery(jpql);
		Iterator<String> paramKeySet = paramMap.keySet().iterator();
		for (; paramKeySet.hasNext();) {
			String paramKey = paramKeySet.next();
			Object value = paramMap.get(paramKey);
			query.setParameter(paramKey, value);
		}
		if (page != null && page > 0 && pageSize != null && pageSize >= 0) {
			query.setFirstResult(page - 1);
			query.setMaxResults(pageSize);
		}
		List<EmsDashboard> edList = query.getResultList();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
		for (EmsDashboard ed: edList) {
			dbdList.add(Dashboard.valueOf(ed));
		}
		return dbdList;
	}
	
	/**
	 * Updates last access date for specified dashboard
	 * @param dashboardId
	 * @param tenantId
	 */
	public void updateLastAccessDate(Long dashboardId, String tenantId) {
		
	}
	
	/**
	 * Adds a dashboard as favorite 
	 * @param dashboardId
	 * @param tenantId
	 */
	public void addFavoriteDashboard(Long dashboardId, String tenantId) {
		
	}
	
	/**
	 * Removes a dashboard from favorite list
	 * @param dashboardId
	 * @param tenantId
	 */
	public void removeFavoriteDashboard(Long dashboardId, String tenantId) {
		
	}
	
	/**
	 * Returns a list of all favorite dashboards
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> getFavoriteDashboards(String tenantId) {
		return null;
	}
}
