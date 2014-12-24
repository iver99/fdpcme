package oracle.sysman.emaas.platform.dashboards.core;

import java.util.List;

import javax.persistence.EntityManager;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
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
	 * @param dashboard
	 * @param tenantId
	 * @return the dashboard saved or updated
	 */
	public Dashboard saveOrUpdateDashboard(Dashboard dashboard, String tenantId) throws DashboardException {
		if (dashboard == null)
			return null;
		EntityManager em = PersistenceManager.getInstance().createEntityManager(tenantId);
		try {
			if (dashboard.getDashboardId() == null) {
				em.getTransaction().begin();
				EmsDashboard ed = dashboard.getPersistenceEntity();
				em.persist(ed);
				em.getTransaction().commit();
				em.refresh(ed);
				return Dashboard.valueOf(ed, dashboard);
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
	 * @return
	 */
	public List<Dashboard> listDashboards(int page, int pageSize, String tenantId) {
		return null;
	}
	
	/**
	 * Returns dashboards for specified query string, by providing page number and page size
	 * @param queryString
	 * @param page number to indicate page index, started from 1
	 * @param pageSize
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> listDashboards(String queryString, int page, int pageSize, String tenantId) {
		return null;
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
