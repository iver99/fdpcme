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
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.AppContext;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
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
//		EntityManager em = PersistenceManager.getInstance().createEntityManager(tenantId);
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = AppContext.getInstance().getCurrentUser();
			// init creation date, owner to prevent null insertion
			Date created = new Date();
			if (dbd.getCreationDate() == null)
				dbd.setCreationDate(created);
			if (dbd.getOwner() == null)
				dbd.setOwner(currentUser);
			if (dbd.getTileList() != null) {
				for (Tile tile: dbd.getTileList()) {
					if (tile.getCreationDate() == null)
						tile.setCreationDate(created);
					if (tile.getOwner() == null)
						tile.setOwner(currentUser);
				}
			}
			if (dbd.getDashboardId() == null) {
				Date creationDate = new Date();
				EmsDashboard ed = dbd.getPersistenceEntity(null);
				ed.setCreationDate(creationDate);
				ed.setOwner(currentUser);
				dsf.persistEmsDashboard(ed);
				dsf.commitTransaction();
				return Dashboard.valueOf(ed, dbd);
			}
			else {
				EmsDashboard ed = dsf.getEmsDashboardById(dbd.getDashboardId());
				if (ed == null)
					throw new DashboardException("Specified dashboard doesnot exist");
//				ed.setName(dbd.getName());
//				ed.setDescription(dbd.getDescription());
//				ed.setEnableTimeRange(DataFormatUtils.boolean2Integer(dbd.getEnableTimeRange()));
//				ed.setIsSystem(dbd.);
				dbd.getPersistenceEntity(ed);
//				updateDashboardTiles(dbd.getTileList(), ed);
				ed.setLastModificationDate(new Date());
				ed.setLastModifiedBy(currentUser);
				if (dbd.getOwner() != null)
					ed.setOwner(dbd.getOwner());
				dsf.mergeEntity(ed);
				dsf.commitTransaction();
				return Dashboard.valueOf(ed, dbd);
			}
		}
		finally {
			if (em!= null)
				em.close();
		}
	}
	
//	private Map<Tile, EmsDashboardTile> updateDashboardTiles(List<Tile> tiles, EmsDashboard ed) {
//		Map<Tile, EmsDashboardTile> rows = new HashMap<Tile, EmsDashboardTile>();
//		// remove deleted tile row in dashboard row first
//		List<EmsDashboardTile> edtList = ed.getDashboardTileList();
//		if (edtList != null) {
//			int edtSize = edtList.size();
//			for (int i = edtSize - 1; i >= 0; i--) {
//				EmsDashboardTile edt = edtList.get(i);
//				boolean isDeleted = true;
//				for (Tile tile: tiles) {
//					if (tile.getTileId() != null && tile.getTileId().equals(edt.getTileId())) {
//						isDeleted = false;
//						rows.put(tile, edt);
//						// remove existing props
//						List<EmsDashboardTileParams> edtpList = edt.getDashboardTileParamsList();
//						if (edtpList == null)
//							break;
//						while (!edt.getDashboardTileParamsList().isEmpty()) {
//							EmsDashboardTileParams edtp = edt.getDashboardTileParamsList().get(0);
////							dsf.removeEmsDashboardTileParams(edtp);
//							edt.getDashboardTileParamsList().remove(edtp);
////							edt.removeEmsDashboardTileParams(edtp);
//						}
//						break;
//					}
//				}
//				if (isDeleted) {
////					ed.removeEmsDashboardTile(edt);
//					ed.getDashboardTileList().remove(edt);
//				}
//			}
//		}
//		
//		for (Tile tile: tiles) {
//			EmsDashboardTile edt = null;
//			if (!rows.containsKey(tile)) {
//				edt = tile.getPersistenceEntity(null);
//				ed.addEmsDashboardTile(edt);
//				rows.put(tile, edt);
////				dsf.persistEntity(edt);
//			}
//			else {
//				edt = rows.get(tile);
//			}
//		}
//		return rows;
//	}
	
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
	 * @param permanent delete permanently or not
	 */
	public void deleteDashboard(Long dashboardId, boolean permanent, String tenantId) {
		if (dashboardId == null || dashboardId <= 0)
			return;
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null)
			return;
		if (!permanent) {
			ed.setDeleted(DataFormatUtils.boolean2Integer(Boolean.TRUE));
			dsf.mergeEmsDashboard(ed);
		}
		else
			dsf.removeEmsDashboard(ed);
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
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null)
			return null;
		Boolean isDeleted = DataFormatUtils.integer2Boolean(ed.getDeleted());
		if (isDeleted != null && isDeleted.booleanValue())
			return null;
		return Dashboard.valueOf(ed);
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
				sb.append(" where p.name LIKE :name");
				paramMap.put("name", "%" + queryString + "%");
			} else {
				sb.append(" where lower(p.name) LIKE :name");
				paramMap.put("name", "%" + queryString.toLowerCase(locale) + "%");
			}
			if (!ic) {
				sb.append(" or p.description like :description");
				paramMap.put("description", "%" + queryString + "%");
			} else {
				sb.append(" or lower(p.description) like :description");
				paramMap.put("description", "%" + queryString.toLowerCase(locale) + "%");
			}
			sb.append(" or lower(p.owner) = :owner");
			paramMap.put("owner", queryString.toLowerCase(locale));
			jpql += sb.toString();
		}
		jpql += " order by p.dashboardId asc";
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EntityManager em = dsf.getEntityManager();
		Query query = em.createQuery(jpql);
		Iterator<String> paramKeySet = paramMap.keySet().iterator();
		for (; paramKeySet.hasNext();) {
			String paramKey = paramKeySet.next();
			Object value = paramMap.get(paramKey);
			query.setParameter(paramKey, value);
		}
		if (page != null && page > 0 && pageSize != null && pageSize > 0) {
			int start = (page - 1) * pageSize;
			long startRow = (Long.valueOf(page) - 1) * Long.valueOf(pageSize);
			if (startRow > Integer.MAX_VALUE)
				start = Integer.MAX_VALUE;
			query.setFirstResult(start);
			query.setMaxResults(pageSize);
		}
		@SuppressWarnings("unchecked")
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
