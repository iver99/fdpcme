package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.core.cache.screenshot.ScreenshotData;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.exception.functional.DashboardSameNameException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.TenantWithoutSubscriptionException;
import oracle.sysman.emaas.platform.dashboards.core.exception.security.CommonSecurityException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.DashboardApplicationType;
import oracle.sysman.emaas.platform.dashboards.core.model.PaginatedDashboards;
import oracle.sysman.emaas.platform.dashboards.core.model.Tile;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.util.AppContext;
import oracle.sysman.emaas.platform.dashboards.core.util.DataFormatUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.DateUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.IdGenerator;
import oracle.sysman.emaas.platform.dashboards.core.util.MessageUtils;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.core.util.ZDTContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

public class DashboardManager
{
	private static final Logger logger = LogManager.getLogger(DashboardManager.class);

	private static DashboardManager instance;

	static {
		instance = new DashboardManager();
	}

	public static final String BLANK_SCREENSHOT = "data:image/png;base64,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AEFIhjEBbGkpyX9EeoKlxVwvUjtiTYqnp98ivhBnAMDXwenpqfbp6a+1iNgHAHS/n/PlMyGNeHNmgKWVXejlAybZ4F/iL3HrBB73ywzvAAABnWlUWHRYTUw6Y29tLmFkb2JlLnhtcAAAAAAAPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS40LjAiPgogICA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPgogICAgICA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIgogICAgICAgICAgICB4bWxuczpleGlmPSJodHRwOi8vbnMuYWRvYmUuY29tL2V4aWYvMS4wLyI+CiAgICAgICAgIDxleGlmOlBpeGVsWERpbWVuc2lvbj4zNDk8L2V4aWY6UGl4ZWxYRGltZW5zaW9uPgogICAgICAgICA8ZXhpZjpQaXhlbFlEaW1lbnNpb24+MTg3PC9leGlmOlBpeGVsWURpbWVuc2lvbj4KICAgICAgPC9yZGY6RGVzY3JpcHRpb24+CiAgIDwvcmRmOlJERj4KPC94OnhtcG1ldGE+CppjahgAAAX4SURBVHgB7dTBCQAgDARBtf+eo1jEviYNHAxh97xbjgABAgQSgZOsGCFAgACBLyC6HoEAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqGA6IbYpggQICC6foAAAQKhgOiG2KYIECAgun6AAAECoYDohtimCBAgILp+gAABAqHABWHCBXJKFjVxAAAAAElFTkSuQmCC";

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

	public static final String SCREENSHOT_BASE64_PNG_PREFIX = "data:image/png;base64,";
	public static final String SCREENSHOT_BASE64_JPG_PREFIX = "data:image/jpeg;base64,";

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
	 * @throws DashboardNotFoundException
	 */
	public void addFavoriteDashboard(BigInteger dashboardId, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
				logger.debug("Dashboard with id {} and tenant id {} is not found, or deleted already", dashboardId, tenantId);
				throw new DashboardNotFoundException();
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {// system dashboard
				logger.debug(
						"Dashboard with id {} and tenant id {} is a system dashboard and cannot be accessed by current tenant",
						dashboardId, tenantId);
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions edf = dsf.getEmsUserOptions(currentUser, dashboardId);
			if (edf == null) {
				edf = new EmsUserOptions();
				edf.setUserName(currentUser);
				edf.setDashboardId(dashboardId);
				edf.setIsFavorite(1);
				dsf.persistEmsUserOptions(edf);
			}
			else {
				edf.setIsFavorite(1);
				dsf.mergeEmsUserOptions(edf);
			}
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
	public void deleteDashboard(BigInteger dashboardId, boolean permanent, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			return;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			if (permanent == false && ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
				throw new DashboardNotFoundException();
			}
			if (!permanent && DataFormatUtils.integer2Boolean(ed.getIsSystem())) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_DELETE_SYSTEM_DASHBOARD_ERROR));
			}
			String currentUser = UserContext.getCurrentUser();
			// user can access owned or system dashboard
			if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
				throw new DashboardNotFoundException();
			}
			//			if (ed.getDeleted() == null || ed.getDeleted() == 0) {
			//				removeFavoriteDashboard(dashboardId, tenantId);
			//			}
			if (!permanent) {
				ed.setDeleted(dashboardId);
				dsf.mergeEmsDashboard(ed);
				dsf.removeEmsSubDashboardBySubId(dashboardId);
				dsf.removeEmsSubDashboardBySetId(dashboardId);
			}
			else {
				dsf.removeAllEmsUserOptions(dashboardId);
				dsf.removeEmsSubDashboardBySubId(dashboardId);
				dsf.removeEmsSubDashboardBySetId(dashboardId);
				dsf.removeEmsDashboard(ed);
			}
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Delete a dashboard specified by dashboard id for given tenant. Soft deletion is supported
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @throws DashboardNotFoundException
	 */
	public void deleteDashboard(BigInteger dashboardId, Long tenantId) throws DashboardException
	{
		deleteDashboard(dashboardId, false, tenantId);
	}

	public ScreenshotData getDashboardBase64ScreenShotById(BigInteger dashboardId, Long tenantId) throws DashboardException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().compareTo(BigInteger.ZERO) > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			if (ed.getSharePublic().intValue() == 0) {
				if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
					throw new DashboardNotFoundException();
				}
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {
				throw new DashboardNotFoundException();
			}
			if (ed.getScreenShot() == null) {
				logger.debug("Retrieved null screenshot base64 data from persistence layer, we use a blank screenshot then");
				return new ScreenshotData(BLANK_SCREENSHOT, ed.getCreationDate(), ed.getLastModificationDate());
			}
			else if (!ed.getScreenShot().startsWith(SCREENSHOT_BASE64_PNG_PREFIX)
					&& !ed.getScreenShot().startsWith(SCREENSHOT_BASE64_JPG_PREFIX)) {
				logger.error("Retrieved an invalid screenshot base64 data that is not started with specified prefix, we use a blank screenshot then");
				logger.debug("Th screenshot string with an invalid base64 previs is: {}", ed.getScreenShot());
				return new ScreenshotData(BLANK_SCREENSHOT, ed.getCreationDate(), ed.getLastModificationDate());
			}
			return new ScreenshotData(ed.getScreenShot(), ed.getCreationDate(), ed.getLastModificationDate());
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
	public Dashboard getDashboardById(BigInteger dashboardId, Long tenantId) throws DashboardException
	{
		EntityManager em = null;
		try {
			if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
				logger.debug("Dashboard not found for id {} is invalid", dashboardId);
				throw new DashboardNotFoundException();
			}
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				logger.debug("Dashboard not found with the specified id {}", dashboardId);
				throw new DashboardNotFoundException();
			}
			Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().compareTo(BigInteger.ZERO) > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				logger.debug("Dashboard with id {} is not found for it's deleted already", dashboardId);
				throw new DashboardNotFoundException();
			}
			String currentUser = UserContext.getCurrentUser();
			// user can access owned or system dashboard
			if (ed.getSharePublic().intValue() == 0) {
				if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
					logger.debug(
							"Dashboard with id {} is not found for it's a non-OOB dashboard and not owned by current user {}",
							dashboardId, currentUser);
					throw new DashboardNotFoundException();
				}
			}
			if (!isDashboardAccessbyCurrentTenant(ed)) {
				logger.debug("Dashboard with id {} is not found for it can't be accessed by current tenant", dashboardId);
				throw new DashboardNotFoundException();
			}
			updateLastAccessDate(dashboardId, tenantId, dsf);
			return Dashboard.valueOf(ed, null, true, true, true);
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public Dashboard getDashboardSetsBySubId(BigInteger dashboardId, Long tenantId) throws DashboardException{
		EntityManager em = null;
		try {
            if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
                logger.debug("Dashboard not found for id {} is invalid", dashboardId);
                throw new DashboardNotFoundException();
            }
            DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
            em = dsf.getEntityManager();
            EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
            if (ed == null) {
                logger.debug("Dashboard not found with the specified id {}", dashboardId);
                throw new DashboardNotFoundException();
            }
            Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().longValue() > 0;
            if (isDeleted != null && isDeleted.booleanValue()) {
                logger.debug("Dashboard with id {} is not found for it's deleted already", dashboardId);
                throw new DashboardNotFoundException();
            }
            String currentUser = UserContext.getCurrentUser();
            // user can access owned or system dashboard
            if (ed.getSharePublic().intValue() == 0) {
                if (!currentUser.equals(ed.getOwner()) && ed.getIsSystem() != 1) {
                    logger.debug(
                            "Dashboard with id {} is not found for it's a non-OOB dashboard and not owned by current user {}",
                            dashboardId, currentUser);
                    throw new DashboardNotFoundException();
                }
            }
            if (!isDashboardAccessbyCurrentTenant(ed)) {
                logger.debug("Dashboard with id {} is not found for it can't be accessed by current tenant", dashboardId);
                throw new DashboardNotFoundException();
            }

            updateLastAccessDate(dashboardId, tenantId, dsf);
            Dashboard dashboard = Dashboard.valueOf(ed, null, false, false, false);

            List<EmsDashboard> emsDashboards = dsf.getEmsDashboardsBySubId(dashboardId);
			if (null == emsDashboards) {
				logger.debug("Dashboard not found with the specified sub dashboard id {}", dashboardId);
				throw new DashboardNotFoundException();
			}

            List<Dashboard> dashboardList = new ArrayList<>();
            for (EmsDashboard emsDashboard : emsDashboards) {
                dashboardList.add(Dashboard.valueOf(emsDashboard,null,false,false,false));
            }

            dashboard.setDashboardSets(dashboardList);

			return dashboard;
		} finally {
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
			logger.debug("Dashboard not found for name \"{}\" is invalid", name);
			return null;
		}
		String currentUser = UserContext.getCurrentUser();
		//		String jpql = "select d from EmsDashboard d where d.name = ?1 and (d.owner = ?2 or d.sharePublic = 1) and d.deleted = ?3";
		//		Object[] params = new Object[] { StringEscapeUtils.escapeHtml4(name), currentUser, new Integer(0) };
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			//			Query query = em.createQuery(jpql);
			//			for (int i = 1; i <= params.length; i++) {
			//				query.setParameter(i, params[i - 1]);
			//			}
			//			EmsDashboard ed = (EmsDashboard) query.getSingleResult();
			EmsDashboard ed = dsf.getEmsDashboardByName(name, currentUser);
			return Dashboard.valueOf(ed);
		}
		catch (NoResultException e) {
			logger.debug("Dashboard not found for name \"{}\" because NoResultException is caught", name);
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
		//		String hql = "select d from EmsDashboard d join EmsDashboardFavorite f on d.dashboardId = f.dashboard.dashboardId and f.userName = '"
		//				+ currentUser + "' and d.deleted = ?1";
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			//			Query query = em.createQuery(hql);
			//			query.setParameter(1, new Integer(0));
			//			@SuppressWarnings("unchecked")
			//			List<EmsDashboard> edList = query.getResultList();
			List<EmsDashboard> edList = dsf.getFavoriteEmsDashboards(currentUser);
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
	 * Retrieves last access date for specified dashboard
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 */
	public Date getLastAccessDate(BigInteger dashboardId, Long tenantId)
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			logger.debug("Last access for dashboard not found for dashboard id {} is invalid", dashboardId);
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null) {
				logger.debug("Last access is not found for dashboard with id {} is not found", dashboardId);
				return null;
			}
			if (ed.getDeleted() != null && ed.getDeleted().equals(1)) {
				logger.debug("Last access is not found for dashboard with id {} is deleted", dashboardId);
				return null;
			}
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions options = dsf.getEmsUserOptions(currentUser, dashboardId);
			if (options != null) {
				return options.getAccessDate();
			}
			return null;
		}
		finally {
			if (em != null) {
				em.close();
			}
		}
	}

	/**
	 * Check if the dashboard with spacified id is favorite dashboard or not
	 *
	 * @param dashboardId
	 * @param tenantId
	 * @return
	 * @throws DashboardException
	 */
	public boolean isDashboardFavorite(BigInteger dashboardId, Long tenantId) throws DashboardException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions edf = dsf.getEmsUserOptions(currentUser, dashboardId);
			return edf != null && edf.getIsFavorite() != null && edf.getIsFavorite().intValue() > 0;
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
			dbdList.add(Dashboard.valueOf(ed, null, false, false, false));
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
		return listDashboards(queryString, offset, pageSize, tenantId, ic, null, null);
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
			boolean ic, String orderBy, DashboardsFilter filter) throws DashboardException
	{
		logger.debug(
				"Listing dashboards with parameters: queryString={}, offset={}, pageSize={}, tenantId={}, ic={}, orderBy={}, filter={}",
				queryString, offset, pageSize, tenantId, ic, orderBy, filter);
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

		List<DashboardApplicationType> apps = getTenantApplications();
		if (apps == null || apps.isEmpty()) {
			throw new TenantWithoutSubscriptionException();
		}
		/*
		if (filter != null) {
			if (filter.getIncludedApplicationTypes() != null && !filter.getIncludedApplicationTypes().isEmpty()) {
				List<DashboardApplicationType> filteredTypes = new ArrayList<DashboardApplicationType>();
				for (DashboardApplicationType type : apps) {
					if (!filter.getIncludedApplicationTypes().contains(type)) {
						filteredTypes.add(type);
					}
				}
		
				for (DashboardApplicationType type : filteredTypes) {
					apps.remove(type);
				}
			}
		}*/

		StringBuilder sb = null;
		int index = 1;
		String currentUser = UserContext.getCurrentUser();
		List<Object> paramList = new ArrayList<Object>();

		sb = new StringBuilder(" from Ems_Dashboard p  ");
		boolean joinOptions = false;
		if (getListDashboardsOrderBy(orderBy).toLowerCase().contains("access_date")) {
			joinOptions = true;
		}
		if (filter != null && filter.getIncludedFavorites() != null && filter.getIncludedFavorites().booleanValue() == true) {
			joinOptions = true;
		}
		if (joinOptions) {
			sb.append("left join Ems_Dashboard_User_Options le on (p.dashboard_Id =le.dashboard_Id and le.user_name = ?" + index++
					+ " and p.tenant_Id = le.tenant_Id) ");
			paramList.add(currentUser);
		}

		if (apps.isEmpty()) {
			// no subscribe apps
			//			sb = new StringBuilder(
			//					" from Ems_Dashboard p left join Ems_Dashboard_Last_Access le on (p.dashboard_Id =le.dashboard_Id and le.accessed_By = ?1 and p.tenant_Id = le.tenant_Id) "
			//							+ "where p.deleted = 0 and p.tenant_Id = ?2 and (p.share_public = 1 or p.owner = ?3) ");
			//			index = 4;
			sb.append("where p.deleted = 0 and p.tenant_Id = ?" + index++ + " and (p.share_public = 1 or p.owner = ?" + index++
					+ ") ");
			paramList.add(tenantId);
			paramList.add(currentUser);
		}
		else {
			StringBuilder sbApps = new StringBuilder();
			for (int i = 0; i < apps.size(); i++) {
				DashboardApplicationType app = apps.get(i);
				if (i != 0) {
					sbApps.append(",");
				}
				sbApps.append(String.valueOf(app.getValue()));
			}

			sb.append("where p.deleted = 0 and p.tenant_Id = ?" + index++ + " and (p.share_public = 1 or p.owner = ?" + index++
					+ " or (p.is_system = 1 and p.application_type in (" + sbApps.toString() + "))) ");
			paramList.add(tenantId);
			paramList.add(currentUser);
		}

		if (filter != null) {
			if (filter.getIncludedFavorites() != null && filter.getIncludedFavorites().booleanValue() == true) {
				//sb.append(" and df.user_name is not null ");
				sb.append(" and le.is_favorite > 0 ");
			}

			if (filter.getIncludedTypeIntegers() != null && !filter.getIncludedTypeIntegers().isEmpty()) {
				sb.append(" and ( ");
				for (int i = 0; i < filter.getIncludedTypeIntegers().size(); i++) {
					if (i != 0) {
						sb.append(" or ");
					}
					sb.append(" p.type = ?" + index++);
					paramList.add(filter.getIncludedTypeIntegers().get(i));
				}
				sb.append(" ) ");
			}

			if (filter.getIncludedApplicationTypes() != null && !filter.getIncludedApplicationTypes().isEmpty()) {
				sb.append(" and ( ");
				for (int i = 0; i < filter.getIncludedApplicationTypes().size(); i++) {
					if (i != 0) {
						sb.append(" or ");
					}
					sb.append(" p.application_type = " + filter.getIncludedApplicationTypes().get(i).getValue() + " ");
					//paramList.add(filter.getIncludedApplicationTypes().get(i).getValue());
				}
				//sb.append(" or p.is_system < 1 ");
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.PROVIDER_NAME in ("
						+ filter.getIncludedWidgetProvidersString() + " )) ");
				sb.append(" ) ");
			}

			if (filter.getIncludedOwners() != null && !filter.getIncludedOwners().isEmpty()) {
				sb.append(" and ( ");
				for (int i = 0; i < filter.getIncludedOwners().size(); i++) {
					if (i != 0) {
						sb.append(" or ");
					}
					if (filter.getIncludedOwners().get(i).equals("Oracle")) {
						sb.append(" p.owner = ?" + index++);
						paramList.add("Oracle");
					}
					if (filter.getIncludedOwners().get(i).equals("Others")) {
						sb.append(" p.owner != ?" + index++);
						paramList.add("Oracle");
					}
					if (filter.getIncludedOwners().get(i).equals("Me")) {
						sb.append(" p.owner = ?" + index++);
						paramList.add(UserContext.getCurrentUser());
					}
					if (filter.getIncludedOwners().get(i).equals("Share")) {
						sb.append(" p.owner != ?" + index++ + " and p.share_public > 0");
						paramList.add(UserContext.getCurrentUser());
					}
				}

				sb.append(" ) ");
			}
		}

		if (queryString != null && !"".equals(queryString)) {
			Locale locale = AppContext.getInstance().getLocale();
			if (!ic) {
				sb.append(" and (p.name LIKE ?" + index++);
				paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString) + "%");
			}
			else {
				sb.append(" and (lower(p.name) LIKE ?" + index++);
				paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString.toLowerCase(locale)) + "%");
			}

			if (!ic) {
				sb.append(" or p.description like ?" + index++);
				paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString) + "%");
			}
			else {
				sb.append(" or lower(p.description) like ?" + index++);
				paramList.add("%" + StringEscapeUtils.escapeHtml4(queryString.toLowerCase(locale)) + "%");
			}

			if (!ic) {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.type <> 1 and t.title like ?"
						+ index++ + " )) ");
				paramList.add("%" + queryString + "%");
			}
			else {
				sb.append(" or p.dashboard_Id in (select t.dashboard_Id from Ems_Dashboard_Tile t where t.type <> 1 and lower(t.title) like ?"
						+ index++ + " )) ");
				paramList.add("%" + queryString.toLowerCase(locale) + "%");
			}
		}

		// order by
		sb.append(getListDashboardsOrderBy(orderBy));

		//query
		StringBuilder sbQuery = new StringBuilder(sb);
		sbQuery.insert(0,
				"select p.DASHBOARD_ID,p.DELETED,p.DESCRIPTION,p.ENABLE_TIME_RANGE,p.ENABLE_REFRESH,p.IS_SYSTEM,p.SHARE_PUBLIC,"
						+ "p.APPLICATION_TYPE,p.CREATION_DATE,p.LAST_MODIFICATION_DATE,p.NAME,p.OWNER,p.TENANT_ID,p.TYPE ");
		String jpqlQuery = sbQuery.toString();
		//System.out.println("sql: " + jpqlQuery);
		logger.debug(jpqlQuery);
		DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
		EntityManager em = dsf.getEntityManager();
		Query listQuery = em.createNativeQuery(jpqlQuery, EmsDashboard.class);
		initializeQueryParams(listQuery, paramList);
		listQuery.setFirstResult(firstResult);
		listQuery.setMaxResults(maxResults);
		@SuppressWarnings("unchecked")
		List<EmsDashboard> edList = listQuery.getResultList();
		List<Dashboard> dbdList = new ArrayList<Dashboard>(edList.size());

		if (edList != null && !edList.isEmpty()) {
			for (int i = 0; i < edList.size(); i++) {
				dbdList.add(Dashboard.valueOf(edList.get(i), null, false, false, false));
			}
		}
		//		Long totalResults = edList == null ? 0L : Long.valueOf(edList.size());
		//		PaginatedDashboards pd = new PaginatedDashboards(totalResults, firstResult, dbdList == null ? 0 : dbdList.size(),
		//				maxResults, dbdList);
		//		return pd;

		StringBuilder sbCount = new StringBuilder(sb);
		sbCount.insert(0, "select count(*) ");
		String jpqlCount = sbCount.toString();
		logger.debug(jpqlCount);
		Query countQuery = em.createNativeQuery(jpqlCount);
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
	public void removeFavoriteDashboard(BigInteger dashboardId, Long tenantId) throws DashboardNotFoundException
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			throw new DashboardNotFoundException();
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
			if (ed == null || ed.getDeleted() != null && ed.getDeleted().compareTo(BigInteger.ZERO) > 0) {
				logger.debug("Dashboard with id {} is not found for it does not exists or is deleted already", dashboardId);
				throw new DashboardNotFoundException();
			}
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			EmsUserOptions edf = dsf.getEmsUserOptions(currentUser, dashboardId);
			if (edf == null) {
				edf = new EmsUserOptions();
				edf.setUserName(currentUser);
				edf.setDashboardId(dashboardId);
				edf.setIsFavorite(0);
				dsf.persistEmsUserOptions(edf);
			}
			else {
				edf.setIsFavorite(0);
				dsf.mergeEmsUserOptions(edf);
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
			logger.debug("Dashboard is not saved: it's impossible to save null dashboard");
			return null;
		}
		EntityManager em = null;
		try {
			DashboardServiceFacade dsf = new DashboardServiceFacade(tenantId);
			em = dsf.getEntityManager();
			String currentUser = UserContext.getCurrentUser();
			if (dbd.getDashboardId() != null) {
				EmsDashboard sameId = dsf.getEmsDashboardById(dbd.getDashboardId());
				if (sameId != null && sameId.getDeleted().compareTo(BigInteger.ZERO) <= 0) {
					throw new CommonFunctionalException(
							MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_CREATE_SAME_ID_ERROR));
				}
			}
			else {
				// initialize id
				dbd.setDashboardId(IdGenerator.getDashboardId(ZDTContext.getRequestId()));
			}

			//check dashboard name
			if (dbd.getName() == null || "".equals(dbd.getName().trim()) || dbd.getName().length() > 64) {
				throw new CommonFunctionalException(
						MessageUtils.getDefaultBundleString(CommonFunctionalException.DASHBOARD_INVALID_NAME_ERROR));
			}
			Dashboard sameName = getDashboardByName(dbd.getName(), tenantId);
			if (sameName != null && !sameName.getDashboardId().equals(dbd.getDashboardId())) {
				throw new DashboardSameNameException();
			}
			// init creation date, owner to prevent null insertion
			Date created = DateUtil.getGatewayTime();
			if (dbd.getCreationDate() == null) {
				dbd.setCreationDate(created);
			}
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			dbd.setLastModificationDate(created);
			if (dbd.getType().equals(Dashboard.DASHBOARD_TYPE_SET)) {
				//				dbd.setEnableTimeRange(null);
			}
			else {
				if (dbd.getTileList() != null && !dbd.getTileList().isEmpty()) {
					List<Tile> tiles = dbd.getTileList();
					for (int i = 0; i < tiles.size(); i++) {
						Tile tile = tiles.get(i);
						tile.setTileId(IdGenerator.getTileId(ZDTContext.getRequestId(), i));
						if (tile.getCreationDate() == null) {
							tile.setCreationDate(created);
						}
						if (tile.getOwner() == null) {
							tile.setOwner(currentUser);
						}
						tile.setLastModificationDate(created);
					}
				}
			}

			EmsDashboard ed = dbd.getPersistenceEntity(null);
			ed.setOwner(currentUser);
			dsf.persistEmsDashboard(ed);
			updateLastAccessDate(ed.getDashboardId(), tenantId);
			return Dashboard.valueOf(ed, dbd, true, true, true);
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
	public void setDashboardIncludeTimeControl(BigInteger dashboardId, boolean enable, Long tenantId)
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
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
			logger.debug("Dashboard is not updated: it's impossible to update null dashboard");
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
			Date created = DateUtil.getGatewayTime();
			if (dbd.getOwner() == null) {
				dbd.setOwner(currentUser);
			}
			if (dbd.getType().equals(Dashboard.DASHBOARD_TYPE_SET)) {

			}
			else {
				if (dbd.getTileList() != null && !dbd.getTileList().isEmpty()) {
					List<Tile> tiles = dbd.getTileList();
					for (int i = 0; i < tiles.size(); i++) {
						Tile tile = tiles.get(i);
						if (tile.getTileId() == null) {
							tile.setTileId(IdGenerator.getTileId(ZDTContext.getRequestId(), i));
						}
						if (tile.getCreationDate() == null) {
							tile.setCreationDate(created);
						}
						if (tile.getOwner() == null) {
							tile.setOwner(currentUser);
						}
						tile.setLastModificationDate(created);
					}
				}
			}

			ed = dsf.getEmsDashboardById(dbd.getDashboardId());
			if (ed == null) {
				throw new DashboardNotFoundException();
			}

            Boolean isDeleted = ed.getDeleted() == null ? null : ed.getDeleted().longValue() > 0;
			if (isDeleted != null && isDeleted.booleanValue()) {
				throw new DashboardNotFoundException();
			}

			if (DataFormatUtils.integer2Boolean(ed.getIsSystem())) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.NOT_SUPPORT_UPDATE_SYSTEM_DASHBOARD_ERROR));
			}
			if (!currentUser.equals(ed.getOwner())) {
				throw new CommonSecurityException(
						MessageUtils.getDefaultBundleString(CommonSecurityException.DASHBOARD_ACTION_REQUIRE_OWNER));
			}
			ed = dbd.getPersistenceEntity(ed);
			ed.setLastModifiedBy(currentUser);
			if (dbd.getOwner() != null) {
				ed.setOwner(dbd.getOwner());
			}
			dsf.mergeEmsDashboard(ed);
			return Dashboard.valueOf(ed, dbd, true, true, true);
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
	public void updateLastAccessDate(BigInteger dashboardId, Long tenantId)
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

	public void updateLastAccessDate(BigInteger dashboardId, Long tenantId, DashboardServiceFacade dsf)
	{
		if (dashboardId == null || dashboardId.compareTo(BigInteger.ZERO) <= 0) {
			logger.debug("Last access date for dashboard is not updated: dashboard id with value {} is invalid", dashboardId);
			return;
		}
		//EntityManager em = null;
		EmsDashboard ed = dsf.getEmsDashboardById(dashboardId);
		if (ed == null || ed.getDeleted() != null && ed.getDeleted().equals(1)) {
			return;
		}
		//em = dsf.getEntityManager();
		String currentUser = UserContext.getCurrentUser();
		EmsUserOptions edla = dsf.getEmsUserOptions(currentUser, dashboardId);
		if (edla == null) {
			edla = new EmsUserOptions();
			edla.setUserName(currentUser);
			edla.setDashboardId(dashboardId);
			edla.setAccessDate(DateUtil.getGatewayTime());
			dsf.persistEmsUserOptions(edla);
		}
		else {
			edla.setAccessDate(DateUtil.getGatewayTime());
			dsf.mergeEmsUserOptions(edla);
		}
	}

	private String getListDashboardsOrderBy(String orderBy)
	{
		if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_NAME.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_NAME_ASC.equals(orderBy)) {
			return " order by lower(p.name), p.name, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_NAME_DSC.equals(orderBy)) {
			return " order by lower(p.name) DESC, p.name DESC, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_DSC.equals(orderBy)) {
			return " order by CASE WHEN p.creation_Date IS NULL THEN 0 ELSE 1 END DESC, p.creation_Date DESC, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_CREATE_TIME_ASC.equals(orderBy)) {
			return " order by CASE WHEN p.creation_Date IS NULL THEN 0 ELSE 1 END, p.creation_Date, p.dashboard_Id";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_DSC.equals(orderBy)) {
			return " order by CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END DESC, le.access_Date DESC, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_ACCESS_TIME_ASC.equals(orderBy)) {
			return " order by CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END, le.access_Date, p.dashboard_Id";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_DSC.equals(orderBy)) {
			return " order by CASE WHEN p.last_modification_Date IS NULL THEN p.creation_Date ELSE p.last_modification_Date END DESC, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_LAST_MODIFEID_ASC.equals(orderBy)) {
			return " order by CASE WHEN p.last_modification_Date IS NULL THEN p.creation_Date ELSE p.last_modification_Date END, p.dashboard_Id";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_OWNER.equals(orderBy)
				|| DashboardConstants.DASHBOARD_QUERY_ORDER_BY_OWNER_ASC.equals(orderBy)) {
			return " order by lower(p.owner), p.owner, lower(p.name), p.name, p.dashboard_Id DESC";
		}
		else if (DashboardConstants.DASHBOARD_QUERY_ORDER_BY_OWNER_DSC.equals(orderBy)) {
			return " order by lower(p.owner) DESC, p.owner DESC, lower(p.name), p.name, p.dashboard_Id DESC";
		}
		else {
			//default order by
			return " order by p.application_Type, p.type DESC, lower(p.name), p.name, CASE WHEN le.access_Date IS NULL THEN 0 ELSE 1 END DESC, le.access_Date DESC";
		}
	}

	private List<DashboardApplicationType> getTenantApplications()
	{
		String opcTenantId = TenantContext.getCurrentTenant();
		if (opcTenantId == null || "".equals(opcTenantId)) {
			logger.warn("When trying to retrieve subscribed application, it's found the tenant context is not set (TenantContext.getCurrentTenant() == null)");
			return null;
		}
		List<String> appNames = TenantSubscriptionUtil.getTenantSubscribedServices(opcTenantId);
		if (appNames == null || appNames.isEmpty()) {
			return null;
		}
		List<DashboardApplicationType> apps = new ArrayList<DashboardApplicationType>();
		for (String appName : appNames) {
			DashboardApplicationType dat = DashboardApplicationType.fromJsonValue(appName);
			apps.add(dat);
		}
		return apps;
	}

	private void initializeQueryParams(Query query, List<Object> paramList)
	{
		if (query == null || paramList == null) {
			return;
		}
		for (int i = 0; i < paramList.size(); i++) {
			Object value = paramList.get(i);
			query.setParameter(i + 1, value);
			logger.debug("binding parameter [{}] as [{}]", i + 1, value);
		}
	}

	private boolean isDashboardAccessbyCurrentTenant(EmsDashboard ed) throws TenantWithoutSubscriptionException
	{
		if (ed == null) {
			logger.debug("null dashboard is not accessed by current tenant");
			return false;
		}
		List<DashboardApplicationType> datList = getTenantApplications();
		if (datList == null || datList.isEmpty()) { // accessible app list is empty
			throw new TenantWithoutSubscriptionException();
		}
		Boolean isSystem = DataFormatUtils.integer2Boolean(ed.getIsSystem());
		if (!isSystem) { // check system dashboard only
			logger.debug("dashboard with id {} is accessed by current tenant", ed.getDashboardId());
			return true;
		}
		Integer at = ed.getApplicationType();
		if (at == null) { // should be always available for system dashboard
			logger.error("Unexpected: application type for system dashboard with id {} is null", ed.getDashboardId());
			return false;
		}
		DashboardApplicationType app = DashboardApplicationType.fromValue(at.intValue());
		if (app == null) {
			logger.debug("Failed to retrieve a valid DashboardApplicationType from given application type internal value {}", at);
			return false;
		}
		for (DashboardApplicationType dat : datList) {
			if (dat.equals(app)) {
				return true;
			}
		}
		logger.debug("dashboard can't be accessed by current tenant as it's application type isn't in the subscribed application list");
		return false;
	}
}
