package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.DashboardSameNameException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.AppContext;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavorite;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavoritePK;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccess;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccessPK;

public class DashboardManager
{
	private static DashboardManager instance;

	static {
		instance = new DashboardManager();
	}

	/**
	 * Returns the singleton instance for dashboard manager
	 *
	 * @return
	 */
	public static DashboardManager getInstance()
	{
		return instance;
	}

	private DashboardManager()
	{
	}

	/**
	 * Adds a dashboard as favorite
	 *
	 * @param dashboardId
	 * @param tenantId
	 */
	public void addFavoriteDashboard(Long dashboardId, String tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
				return;
			}
			em = dsf.getEntityManager();
			String currentUser = AppContext.getInstance().getCurrentUser();
			EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(currentUser, dashboardId);
			EmsDashboardFavorite edf = em.find(EmsDashboardFavorite.class, edfpk);
			if (edf == null) {
				edf = new EmsDashboardFavorite(new Date(), ed, currentUser);
				dsf.persistEmsDashboardFavorite(edf);
			}
			else {
				edf.setCreationDate(new Date());
				dsf.mergeEmsDashboardFavorite(edf);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
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
	 * Delete a dashboard specified by dashboard id for given tenant.
	 *
	 * @param dashboardId
	 *            id for the dashboard
	 * @param permanent
	 *            delete permanently or not
	 */
	public void deleteDashboard(Long dashboardId, boolean permanent, String tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null) {
			return;
		}

		removeFavoriteDashboard(dashboardId, tenantId);
		if (!permanent) {
			ed.setDeleted(dashboardId);
			dsf.mergeEmsDashboard(ed);
		}
		else {
			EmsDashboardLastAccess edla = getLastAccess(dashboardId, tenantId);
			if (edla != null) {
				dsf.removeEmsDashboardLastAccess(edla);
			}
			dsf.removeEmsDashboard(ed);
		}
	}

	/**
	 * Delete a dashboard specified by dashboard id for given tenant. Soft deletion is supported
	 *
	 * @param dashboardId
	 * @param tenantId
	 */
	public void deleteDashboard(Long dashboardId, String tenantId)
	{
		deleteDashboard(dashboardId, false, tenantId);
	}

	/**
	 * Returns dashboard instance by specifying the id
	 *
	 * @param dashboardId
	 * @return
	 */
	public Dashboard getDashboardById(Long dashboardId, String tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return null;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null) {
			return null;
		}
		Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted() > 0;
		if (isDeleted != null && isDeleted.booleanValue()) {
			return null;
		}
		updateLastAccessDate(dashboardId, tenantId);
		return Dashboard.valueOf(ed);
	}

	/**
	 * Returns dashboard instance specified by name for current user Please note that same user under single tenant can't have
	 * more than one dashboards with same name, so this method return single dashboard instance
	 */
	public Dashboard getDashboardByName(String name, String tenantId)
	{
		if (name == null || "".equals(name)) {
			return null;
		}
		String currentUser = AppContext.getInstance().getCurrentUser();
		String jpql = "select d from EmsDashboard d where d.name = ?1 and d.owner = ?2 and d.deleted = ?3";
		Object[] params = new Object[] { name, currentUser, new Integer(0) };
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			Query query = em.createQuery(jpql);
			for (int i = 1; i <= params.length; i++) {
				query.setParameter(i, params[i - 1]);
			}
			EmsDashboard ed = (EmsDashboard) query.getSingleResult();
			return Dashboard.valueOf(ed);
		}
		catch (NoResultException e) {
			return null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns a list of all favorite dashboards for current user
	 *
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> getFavoriteDashboards(String tenantId)
	{
		String currentUser = AppContext.getInstance().getCurrentUser();
		String hql = "select d from EmsDashboard d join EmsDashboardFavorite f on d.dashboardId = f.dashboard.dashboardId and f.userName = '"
				+ currentUser + "'";
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			Query query = em.createQuery(hql);
			@SuppressWarnings("unchecked")
			List<EmsDashboard> edList = query.getResultList();
			List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
			for (EmsDashboard ed : edList) {
				dbdList.add(Dashboard.valueOf(ed));
			}
			return dbdList;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves last access for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 */
	public EmsDashboardLastAccess getLastAccess(Long dashboardId, String tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
				return null;
			}
			em = dsf.getEntityManager();
			String currentUser = AppContext.getInstance().getCurrentUser();
			EmsDashboardLastAccessPK edlapk = new EmsDashboardLastAccessPK(currentUser, dashboardId);
			EmsDashboardLastAccess edla = em.find(EmsDashboardLastAccess.class, edlapk);
			return edla;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Retrieves last access date for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 */
	public Date getLastAccessDate(Long dashboardId, String tenantId)
	{
		EmsDashboardLastAccess edla = getLastAccess(dashboardId, tenantId);
		if (edla != null) {
			return edla.getAccessDate();
		}
		return null;
	}

	/**
	 * Returns all dashboards
	 *
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> listAllDashboards(String tenantId)
	{
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		List<EmsDashboard> edList = dsf.getEmsDashboardFindAll();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
		for (EmsDashboard ed : edList) {
			dbdList.add(Dashboard.valueOf(ed));
		}
		return dbdList;
	}

	/**
	 * Returns dashboards for specified page with given row offset & page size
	 *
	 * @param offset
	 *            number to indicate row offset, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 * @throws DashboardException
	 */
	public PaginatedDashboards listDashboards(Integer offset, Integer pageSize, String tenantId, boolean ic)
			throws DashboardException
	{
		return listDashboards(null, offset, pageSize, tenantId, ic);
	}

	/**
	 * Returns dashboards for specified query string, by providing page number and page size
	 *
	 * @param queryString
	 * @param offset
	 *            number to indicate row index, started from 0
	 * @param pageSize
	 * @param tenantId
	 * @param ic
	 *            ignore case or not
	 * @return
	 */
	public PaginatedDashboards listDashboards(String queryString, final Integer offset, Integer pageSize, String tenantId,
			boolean ic) throws DashboardException
	{
		if (offset != null && offset < 0) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_QUERY_INVALID_OFFSET));
		}
		int firstResult = 0;
		if (offset != null) {
			firstResult = offset.intValue();
		}

		if (pageSize != null && pageSize <= 0) {
			throw new CommonFunctionalException(
					MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_QUERY_INVALID_LIMIT));
		}
		int maxResults = DashboardConstants.DASHBOARD_QUERY_DEFAULT_LIMIT;
		if (pageSize != null) {
			maxResults = pageSize.intValue();
		}

		StringBuilder sb = new StringBuilder(
				" from EmsDashboard p left join EmsDashboardLastAccess lae on p.dashboardId=lae.dashboardId and lae.accessedBy=:accessBy ");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String currentUser = AppContext.getInstance().getCurrentUser();
		paramMap.put("accessBy", currentUser);
		if (queryString != null && !"".equals(queryString)) {
			Locale locale = AppContext.getInstance().getLocale();
			if (!ic) {
				sb.append(" where (p.name LIKE :name");
				paramMap.put("name", "%" + queryString + "%");
			}
			else {
				sb.append(" where (lower(p.name) LIKE :name");
				paramMap.put("name", "%" + queryString.toLowerCase(locale) + "%");
			}

			if (!ic) {
				sb.append(" or p.description like :description");
				paramMap.put("description", "%" + queryString + "%");
			}
			else {
				sb.append(" or lower(p.description) like :description");
				paramMap.put("description", "%" + queryString.toLowerCase(locale) + "%");
			}

			if (!ic) {
				sb.append(" or p in (select t.dashboard from EmsDashboardTile t where t.title like :tileTitle ) ");
				paramMap.put("tileTitle", "%" + queryString + "%");
			}
			else {
				sb.append(" or p in (select t.dashboard from EmsDashboardTile t where lower(t.title) like :tileTitle ) ");
				paramMap.put("tileTitle", "%" + queryString.toLowerCase(locale) + "%");
			}
			sb.append(" or lower(p.owner) = :owner)");
			paramMap.put("owner", queryString.toLowerCase(locale));
			sb.append(" and p.deleted = 0 ");
		}
		sb.append(" order by CASE WHEN lae.accessDate IS NULL THEN 1 ELSE 0 END, lae.accessDate DESC, p.dashboardId DESC");
		StringBuilder sbQuery = new StringBuilder(sb);
		sbQuery.insert(0, "select p ");
		String jpqlQuery = sbQuery.toString();
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EntityManager em = dsf.getEntityManager();
		Query listQuery = em.createQuery(jpqlQuery);
		initializeQueryParams(listQuery, paramMap);
		listQuery.setFirstResult(firstResult);
		listQuery.setMaxResults(maxResults);
		@SuppressWarnings("unchecked")
		List<EmsDashboard> edList = listQuery.getResultList();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
		for (EmsDashboard ed : edList) {
			dbdList.add(Dashboard.valueOf(ed));
		}

		StringBuilder sbCount = new StringBuilder(sb);
		sbCount.insert(0, "select count(p) ");
		Query countQuery = em.createQuery(sbCount.toString());
		initializeQueryParams(countQuery, paramMap);
		Long totalResults = (Long) countQuery.getSingleResult();
		PaginatedDashboards pd = new PaginatedDashboards(totalResults, firstResult, dbdList == null ? 0 : dbdList.size(),
				maxResults, dbdList);
		return pd;
	}

	/**
	 * Removes a dashboard from favorite list
	 *
	 * @param dashboardId
	 * @param tenantId
	 */
	public void removeFavoriteDashboard(Long dashboardId, String tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
				return;
			}
			em = dsf.getEntityManager();
			String currentUser = AppContext.getInstance().getCurrentUser();
			EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(currentUser, dashboardId);
			EmsDashboardFavorite edf = em.find(EmsDashboardFavorite.class, edfpk);
			if (edf != null) {
				dsf.removeEmsDashboardFavorite(edf);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Save a newly created dashboard for given tenant
	 *
	 * @param dbd
	 * @param tenantId
	 * @return the dashboard saved
	 */
	public Dashboard saveNewDashboard(Dashboard dbd, String tenantId) throws DashboardException
	{
		if (dbd == null) {
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = AppContext.getInstance().getCurrentUser();
			//			if (dbd.getDashboardId() != null) {
			//				Dashboard sameId = getDashboardById(dbd.getDashboardId(), tenantId);
			//				if (sameId != null) {
			//					throw new CommonFunctionalException(
			//							MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_CREATE_SAME_ID_ERROR));
			//				}
			//			}
			//check dashboard name
			if (dbd.getName() == null || dbd.getName().trim() == "" || dbd.getName().length() > 64) {
				throw new CommonFunctionalException(MessageUtils.getDefaultBundleString("DASHBOARD_CREATE_INVALID_NAME_ERROR"));
			}
			Dashboard sameName = getDashboardByName(dbd.getName(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = new Date();
			if (dbd.getCreationDate() == null) {
				dbd.setCreationDate(created);
			}
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			if (dbd.getTileList() != null) {
				for (Tile tile : dbd.getTileList()) {
					if (tile.getCreationDate() == null) {
						tile.setCreationDate(created);
					}
					if (tile.getOwner() == null) {
						tile.setOwner(currentUser);
					}
				}
			}
			EmsDashboard ed = dbd.getPersistenceEntity(null);
			ed.setCreationDate(dbd.getCreationDate());
			ed.setOwner(currentUser);
			dsf.persistEmsDashboard(ed);
			updateLastAccessDate(ed.getDashboardId(), tenantId);
			return Dashboard.valueOf(ed, dbd);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Enables or disables the 'include time control' settings for specified dashboard
	 *
	 * @param dashboardId
	 * @param enable
	 * @param tenantId
	 */
	public void setDashboardIncludeTimeControl(Long dashboardId, boolean enable, String tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null) {
			return;
		}
		ed.setEnableTimeRange(DataFormatUtils.boolean2Integer(enable));
		dsf.mergeEmsDashboard(ed);
	}

	/**
	 * Update an existing dashboard for given tenant
	 *
	 * @param dbd
	 * @param tenantId
	 * @return the dashboard saved or updated
	 */
	public Dashboard updateDashboard(Dashboard dbd, String tenantId) throws DashboardException
	{
		if (dbd == null) {
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = AppContext.getInstance().getCurrentUser();
			Dashboard sameName = getDashboardByName(dbd.getName(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = new Date();
			if (dbd.getCreationDate() == null) {
				dbd.setCreationDate(created);
			}
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			if (dbd.getTileList() != null) {
				for (Tile tile : dbd.getTileList()) {
					if (tile.getCreationDate() == null) {
						tile.setCreationDate(created);
					}
					if (tile.getOwner() == null) {
						tile.setOwner(currentUser);
					}
				}
			}

			EmsDashboard ed = dsf.getEmsDashboardById(dbd.getDashboardId());
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			dbd.getPersistenceEntity(ed);
			ed.setLastModificationDate(new Date());
			ed.setLastModifiedBy(currentUser);
			if (dbd.getOwner() != null) {
				ed.setOwner(dbd.getOwner());
			}
			dsf.mergeEntity(ed);
			dsf.commitTransaction();
			return Dashboard.valueOf(ed, dbd);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Updates last access date for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 */
	public void updateLastAccessDate(Long dashboardId, String tenantId)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
				return;
			}
			em = dsf.getEntityManager();
			String currentUser = AppContext.getInstance().getCurrentUser();
			EmsDashboardLastAccessPK edlapk = new EmsDashboardLastAccessPK(currentUser, dashboardId);
			EmsDashboardLastAccess edla = em.find(EmsDashboardLastAccess.class, edlapk);
			if (edla == null) {
				edla = new EmsDashboardLastAccess(new Date(), currentUser, dashboardId);
				dsf.persistEmsDashboardLastAccess(edla);
			}
			else {
				edla.setAccessDate(new Date());
				dsf.mergeEmsDashboardLastAccess(edla);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	private void initializeQueryParams(Query query, Map<String, Object> paramMap)
	{
		if (query == null || paramMap == null) {
			return;
		}
		Iterator<String> paramKeySet = paramMap.keySet().iterator();
		for (; paramKeySet.hasNext();) {
			String paramKey = paramKeySet.next();
			Object value = paramMap.get(paramKey);
			query.setParameter(paramKey, value);
		}
	}
}
