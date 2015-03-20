package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
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
	 * Adds a dashboard as favorite
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void addFavoriteDashboard(Long dashboardId, Long tenantId) throws DashboardNotFoundException
	{
		if (dashboardId == null || dashboardId <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted() > 0) {
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(currentUser, dashboardId);
			EmsDashboardFavorite edf = em.find(EmsDashboardFavorite.class, edfpk);
			if (edf == null) {
				edf = new EmsDashboardFavorite(new Date(), ed, currentUser);
				dsf.persistEmsDashboardFavorite(edf);
			}
			//			else {
			//				//				edf.setCreationDate(new Date());
			//				dsf.mergeEmsDashboardFavorite(edf);
			//			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Delete a dashboard specified by dashboard id for given tenant.
	 *
	 * @param dashboardId
	 *            id for the dashboard
	 * @param permanent
	 *            delete permanently or not
	 * @throws DashboardException
	 */
	public void deleteDashboard(Long dashboardId, boolean permanent, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null) {
			throw new DashboardNotFoundException();
		}
		if (permanent == false && ed.getDeleted() != null && ed.getDeleted() > 0) {
			throw new DashboardNotFoundException();
		}
		if (ed.getDeleted() == null || ed.getDeleted() == 0) {
			removeFavoriteDashboard(dashboardId, tenantId);
		}
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
	 * @throws DashboardNotFoundException
	 */
	public void deleteDashboard(Long dashboardId, Long tenantId) throws DashboardException
	{
		deleteDashboard(dashboardId, false, tenantId);
	}

	public String getDashboardBase64ScreenShotById(Long dashboardId, Long tenantId) throws DashboardException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId <= 0) {
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted() > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
				throw new DashboardNotFoundException();
			}
			return ed.getScreenShot();
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns dashboard instance by specifying the id
	 *
	 * @param dashboardId
	 * @return
	 * @throws DashboardException
	 */
	public Dashboard getDashboardById(Long dashboardId, Long tenantId) throws DashboardException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId <= 0) {
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted() > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			// user can access owned or system dashboard
			if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
				throw new DashboardNotFoundException();
			}
			updateLastAccessDate(dashboardId, tenantId, dsf);
			return Dashboard.valueOf(ed);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns dashboard instance specified by name for current user Please note that same user under single tenant can't have
	 * more than one dashboards with same name, so this method return single dashboard instance
	 */
	public Dashboard getDashboardByName(String name, Long tenantId)
	{
		if (name == null || "".equals(name)) {
			return null;
		}
		String currentUser = UserContext.getCurrentUser();
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
	public List<Dashboard> getFavoriteDashboards(Long tenantId)
	{
		String currentUser = UserContext.getCurrentUser();
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
	public EmsDashboardLastAccess getLastAccess(Long dashboardId, Long tenantId)
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
			String currentUser = UserContext.getCurrentUser();
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
	public Date getLastAccessDate(Long dashboardId, Long tenantId)
	{
		EmsDashboardLastAccess edla = getLastAccess(dashboardId, tenantId);
		if (edla != null) {
			return edla.getAccessDate();
		}
		return null;
	}

	/**
	 * Check if the dashboard with spacified id is favorite dashboard or not
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 * @throws DashboardException
	 */
	public boolean isDashboardFavorite(Long dashboardId, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(currentUser, dashboardId);
			EmsDashboardFavorite edf = em.find(EmsDashboardFavorite.class, edfpk);
			return edf != null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Returns all dashboards
	 *
	 * @param tenantId
	 * @return
	 */
	public List<Dashboard> listAllDashboards(Long tenantId)
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
	public PaginatedDashboards listDashboards(Integer offset, Integer pageSize, Long tenantId, boolean ic)
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
	public PaginatedDashboards listDashboards(String queryString, final Integer offset, Integer pageSize, Long tenantId,
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
				" from Ems_Dashboard p left join (select lae.dashboard_Id, lae.access_Date from Ems_Dashboard d, Ems_Dashboard_Last_Access lae "
						+ "where d.dashboard_Id=lae.dashboard_Id and lae.accessed_By=?1 and d.tenant_Id=?2 and lae.tenant_Id=d.tenant_id) le on p.dashboard_Id=le.dashboard_Id "
						+ "where p.deleted = 0 and p.tenant_Id = ?3 and (p.owner = ?4 or p.is_system = ?5) ");
		//		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Object> paramList = new ArrayList<Object>();
		String currentUser = UserContext.getCurrentUser();
		paramList.add(currentUser);
		paramList.add(tenantId);
		paramList.add(tenantId);
		paramList.add(currentUser);
		paramList.add(1);
		if (queryString != null && !"".equals(queryString)) {
			Locale locale = AppContext.getInstance().getLocale();
			if (!ic) {
				sb.append(" and (p.name LIKE ?6");
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" and (lower(p.name) LIKE ?6");
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}

			if (!ic) {
				sb.append(" or p.description like ?7");
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" or lower(p.description) like ?7");
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}

			if (!ic) {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.title like ?8 )) ");
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where lower(t.title) like ?8 )) ");
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}
			//			sb.append(" or lower(p.owner) = :owner)");
			//			paramMap.put("owner", queryString.toLowerCase(locale));
			//			sb.append(" and p.deleted = 0 ");
		}
		sb.append(" order by CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END DESC, le.access_Date DESC, p.dashboard_Id DESC");
		StringBuilder sbQuery = new StringBuilder(sb);
		sbQuery.insert(0, "select p.* ");
		String jpqlQuery = sbQuery.toString();
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EntityManager em = dsf.getEntityManager();
		Query listQuery = em.createNativeQuery(jpqlQuery, EmsDashboard.class);
		initializeQueryParams(listQuery, paramList);
		listQuery.setFirstResult(firstResult);
		listQuery.setMaxResults(maxResults);
		@SuppressWarnings("unchecked")
		List<EmsDashboard> edList = listQuery.getResultList();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());
		for (EmsDashboard ed : edList) {
			dbdList.add(Dashboard.valueOf(ed));
		}

		StringBuilder sbCount = new StringBuilder(sb);
		sbCount.insert(0, "select count(*) ");
		Query countQuery = em.createNativeQuery(sbCount.toString());
		initializeQueryParams(countQuery, paramList);
		Long totalResults = ((BigDecimal) countQuery.getSingleResult()).longValue();
		PaginatedDashboards pd = new PaginatedDashboards(totalResults, firstResult, dbdList == null ? 0 : dbdList.size(),
				maxResults, dbdList);
		return pd;
	}

	/**
	 * Removes a dashboard from favorite list
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void removeFavoriteDashboard(Long dashboardId, Long tenantId) throws DashboardNotFoundException
	{
		if (dashboardId == null || dashboardId <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted() > 0) {
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
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
	public Dashboard saveNewDashboard(Dashboard dbd, Long tenantId) throws DashboardException
	{
		if (dbd == null) {
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			if (dbd.getDashboardId() != null) {
				EmsDashboard sameId = dsf.getEmsDashboardById(dbd.getDashboardId());
				if (sameId != null && sameId.getDeleted() <= 0) {
					throw new CommonFunctionalException(
							MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_CREATE_SAME_ID_ERROR));
				}
			}
			//check dashboard name
			if (dbd.getName() == null || dbd.getName().trim() == "" || dbd.getName().length() > 64) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_NAME_ERROR));
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
	public void setDashboardIncludeTimeControl(Long dashboardId, boolean enable, Long tenantId)
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
	public Dashboard updateDashboard(Dashboard dbd, Long tenantId) throws DashboardException
	{
		if (dbd == null) {
			return null;
		}
		EntityManager em = null;
		EmsDashboard ed = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			Dashboard sameName = getDashboardByName(dbd.getName(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = new Date();
			//			if (dbd.getCreationDate() == null) {
			//				dbd.setCreationDate(created);
			//			}
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

			ed = dsf.getEmsDashboardById(dbd.getDashboardId());
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			ed = dbd.getPersistenceEntity(ed);
			ed.setLastModificationDate(new Date());
			ed.setLastModifiedBy(currentUser);
			if (dbd.getOwner() != null) {
				ed.setOwner(dbd.getOwner());
			}
			dsf.mergeEmsDashboard(ed);
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
	public void updateLastAccessDate(Long dashboardId, Long tenantId)
	{
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			updateLastAccessDate(dashboardId, tenantId, dsf);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void updateLastAccessDate(Long dashboardId, Long tenantId, DashboardServiceFacade dsf)
	{
		if (dashboardId == null || dashboardId <= 0) {
			return;
		}
		EntityManager em = null;
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
			return;
		}
		em = dsf.getEntityManager();
		String currentUser = UserContext.getCurrentUser();
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

	private void initializeQueryParams(Query query, List<Object> paramList)
	{
		if (query == null || paramList == null) {
			return;
		}
		for (int i = 0; i < paramList.size(); i++) {
			Object value = paramList.get(i);
			query.setParameter(i + 1, value);
		}
	}
}
