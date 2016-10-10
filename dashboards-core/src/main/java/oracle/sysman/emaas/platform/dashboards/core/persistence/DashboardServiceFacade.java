package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.core.UserOptionsManager;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreference;
import oracle.sysman.emaas.platform.dashboards.entity.EmsPreferencePK;
import oracle.sysman.emaas.platform.dashboards.entity.EmsSubDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptionsPK;

import org.apache.commons.lang3.StringEscapeUtils;

public class DashboardServiceFacade
{
	private final EntityManager em;

	public DashboardServiceFacade(Long tenantId)
	{
		final EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
		em = emf.createEntityManager();
		em.setProperty("tenant.id", tenantId);
	}

	/**
	 * All changes that have been made to the managed entities in the persistence context are applied to the database and
	 * committed.
	 */
	public void commitTransaction()
	{
		final EntityTransaction entityTransaction = em.getTransaction();
		if (!entityTransaction.isActive()) {
			entityTransaction.begin();
		}
		entityTransaction.commit();
	}

	public EmsDashboard getEmsDashboardById(Long dashboardId)
	{
		return em.find(EmsDashboard.class, dashboardId);
	}

	//	/** <code>select o from EmsDashboardFavorite o</code> */
	//	public List<EmsDashboardFavorite> getEmsDashboardFavoriteFindAll()
	//	{
	//		return em.createNamedQuery("EmsDashboardFavorite.findAll", EmsDashboardFavorite.class).getResultList();
	//	}

	public EmsDashboard getEmsDashboardByName(String name, String owner)
	{
		String jpql = "select d from EmsDashboard d where d.name = ?1 and d.owner = ?2 and d.deleted = ?3";
		Object[] params = new Object[] { StringEscapeUtils.escapeHtml4(name), owner, new Integer(0) };
		Query query = em.createQuery(jpql);
		for (int i = 1; i <= params.length; i++) {
			query.setParameter(i, params[i - 1]);
		}
		if(query.getSingleResult()!=null){
			return (EmsDashboard)query.getSingleResult();
		}
		return null;
	}

	//	public EmsDashboardFavorite getEmsDashboardFavoriteByPK(Long dashboardId, String username)
	//	{
	//		EmsDashboardFavoritePK edfpk = new EmsDashboardFavoritePK(username, dashboardId);
	//		return em.find(EmsDashboardFavorite.class, edfpk);
	//	}

	//	/** <code>select o from EmsDashboardLastAccess o</code> */
	//	public List<EmsDashboardLastAccess> getEmsDashboardLastAccessFindAll()
	//	{
	//		return em.createNamedQuery("EmsDashboardLastAccess.findAll", EmsDashboardLastAccess.class).getResultList();
	//	}

	/** <code>select o from EmsDashboard o</code> */
	public List<EmsDashboard> getEmsDashboardFindAll()
	{
		return em.createNamedQuery("EmsDashboard.findAll", EmsDashboard.class).getResultList();
	}

	//	public EmsDashboardLastAccess getEmsDashboardLastAccessByPK(Long dashboardId, String username)
	//	{
	//		EmsDashboardLastAccessPK edlapk = new EmsDashboardLastAccessPK(username, dashboardId);
	//		return em.find(EmsDashboardLastAccess.class, edlapk);
	//	}

	//	/** <code>select o from EmsDashboardLastAccess o</code> */
	//	public List<EmsDashboardLastAccess> getEmsDashboardLastAccessFindAll()
	//	{
	//		return em.createNamedQuery("EmsDashboardLastAccess.findAll", EmsDashboardLastAccess.class).getResultList();
	//	}

	public void removePreferenceByKey(String userName, String key, long tenantId)
	{
		String sql = "select * from ems_preference p where p.user_Name ='"+userName+"' and p.pref_key = '"+key+"' and p.tenant_id="+tenantId;		
		Query query = em.createNativeQuery(sql, EmsPreference.class);
		@SuppressWarnings("unchecked")
		List<EmsPreference> emsPreferenceList = query.getResultList();
		if (emsPreferenceList != null && !emsPreferenceList.isEmpty()) {
			em.remove(emsPreferenceList.get(0));
			commitTransaction();
		}
	}
	
	public EmsPreference getEmsPreference(String username, String prefKey)
	{
		return em.find(EmsPreference.class, new EmsPreferencePK(prefKey, username));
	}

	//	/** <code>select o from EmsDashboardTile o</code> */
	//	public List<EmsDashboardTile> getEmsDashboardTileFindAll()
	//	{
	//		return em.createNamedQuery("EmsDashboardTile.findAll", EmsDashboardTile.class).getResultList();
	//	}

	//	/** <code>select o from EmsDashboardTileParams o</code> */
	//	public List<EmsDashboardTileParams> getEmsDashboardTileParamsFindAll()
	//	{
	//		return em.createNamedQuery("EmsDashboardTileParams.findAll", EmsDashboardTileParams.class).getResultList();
	//	}

	/** <code>select o from EmsPreference o</code> */
	public List<EmsPreference> getEmsPreferenceFindAll(String username)
	{
		return em.createNamedQuery("EmsPreference.findAll", EmsPreference.class).setParameter("username", username)
				.getResultList();
	}

	public EmsUserOptions getEmsUserOptions(String username, Long dashboardId)
	{
		return em.find(EmsUserOptions.class, new EmsUserOptionsPK(username, dashboardId));
	}

	public EntityManager getEntityManager()
	{
		return em;
	}

	@SuppressWarnings("unchecked")
	public List<EmsDashboard> getFavoriteEmsDashboards(String username)
	{
		String hql = "select d from EmsDashboard d join EmsUserOptions f on d.dashboardId = f.dashboardId and f.userName = '"
				+ username + "' and d.deleted = 0 and f.isFavorite > 0";
		Query query = em.createQuery(hql);
		return query.getResultList();
	}
	
	public List<EmsDashboard> getEmsDashboardByIds(List<Long> dashboardIds, Long tenantId)
	{
		if (dashboardIds != null && !dashboardIds.isEmpty()) {
			StringBuilder parameters = new StringBuilder();
			int flag = 0;
			for (Long id : dashboardIds) {
				if (flag++ > 0) {
					parameters.append(",");
				}
				parameters.append(id);
			}
			String sql = "select * from ems_dashboard p where p.tenant_id=? and p.dashboard_id in("
					+ parameters.toString() + ")";
			Query query = em.createNativeQuery(sql, EmsDashboard.class);
			query.setParameter("1", tenantId);
			@SuppressWarnings("unchecked")
			List<EmsDashboard> subDashboards = query.getResultList();
			return subDashboards;
		}
		return Collections.emptyList();

	}
 


	public EmsDashboard mergeEmsDashboard(EmsDashboard emsDashboard)
	{
		EmsDashboard entity = null;
		entity = em.merge(emsDashboard);
		commitTransaction();
		return entity;
	}

	//	public EmsDashboardFavorite mergeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	//	{
	//		EmsDashboardFavorite entity = null;
	//		entity = em.merge(emsDashboardFavorite);
	//		commitTransaction();
	//		return entity;
	//	}

	//	public EmsDashboardLastAccess mergeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	//	{
	//		EmsDashboardLastAccess entity = null;
	//		entity = em.merge(emsDashboardLastAccess);
	//		commitTransaction();
	//		return entity;
	//	}

	public EmsPreference mergeEmsPreference(EmsPreference emsPreference)
	{
		EmsPreference entity = null;
		entity = em.merge(emsPreference);
		commitTransaction();
		return entity;
	}

	public EmsUserOptions mergeEmsUserOptions(EmsUserOptions emsUserOptions)
	{
		EmsUserOptions entity = null;
		entity = em.merge(emsUserOptions);
		commitTransaction();
		return entity;
	}

	public EmsDashboard persistEmsDashboard(EmsDashboard emsDashboard)
	{
		if (emsDashboard.getSharePublic() == null) {
			emsDashboard.setSharePublic(0);
		}
		if (emsDashboard.getEnableEntityFilter() == null) {
			emsDashboard.setEnableEntityFilter(0);
		}
		if (emsDashboard.getEnableTimeRange() == null) {
			emsDashboard.setEnableTimeRange(0);
		}
		if (emsDashboard.getEnableDescription() == null) {
			emsDashboard.setEnableDescription(0);
		}
		if (emsDashboard.getEnableRefresh() == null) {
			emsDashboard.setEnableRefresh(0);
		}

		em.persist(emsDashboard);
		commitTransaction();
		return emsDashboard;
	}

	//	public <T> T mergeEntity(T entity)
	//	{
	//		entity = em.merge(entity);
	//		commitTransaction();
	//		return entity;
	//	}

	//	public EmsDashboardFavorite persistEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	//	{
	//		em.persist(emsDashboardFavorite);
	//		commitTransaction();
	//		return emsDashboardFavorite;
	//	}

	//	public EmsDashboardLastAccess persistEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	//	{
	//		em.persist(emsDashboardLastAccess);
	//		commitTransaction();
	//		return emsDashboardLastAccess;
	//	}

	public EmsPreference persistEmsPreference(EmsPreference emsPreference)
	{
		em.persist(emsPreference);
		commitTransaction();
		return emsPreference;
	}

	public EmsUserOptions persistEmsUserOptions(EmsUserOptions emsUserOptions)
	{
		if (emsUserOptions.getAutoRefreshInterval() == null) {
			emsUserOptions.setAutoRefreshInterval(UserOptionsManager.DEFAULT_REFRESH_INTERVAL);
		}
		if (emsUserOptions.getIsFavorite() == null) {
			emsUserOptions.setIsFavorite(0);
		}
		em.persist(emsUserOptions);
		commitTransaction();
		return emsUserOptions;
	}

	//	public <T> T persistEntity(T entity)
	//	{
	//		em.persist(entity);
	//		commitTransaction();
	//		return entity;
	//	}

	//	@SuppressWarnings("rawtypes")
	//	public List queryByRange(String jpqlStmt, Class resultClass, Map<String, Object> params, int firstResult, int maxResults)
	//	{
	//		@SuppressWarnings("unchecked")
	//		Query query = resultClass == null ? em.createQuery(jpqlStmt) : em.createQuery(jpqlStmt, resultClass);
	//		if (params != null && params.size() > 0) {
	//			for (Map.Entry<String, Object> p : params.entrySet()) {
	//				query.setParameter(p.getKey(), p.getValue());
	//			}
	//		}
	//		if (firstResult > 0) {
	//			query = query.setFirstResult(firstResult);
	//		}
	//		if (maxResults > 0) {
	//			query = query.setMaxResults(maxResults);
	//		}
	//		return query.getResultList();
	//	}

	public void removeAllEmsPreferences(String username)
	{
		getEntityManager().getTransaction().begin();
		em.createNamedQuery("EmsPreference.removeAll").setParameter("username", username).executeUpdate();
		commitTransaction();
	}

	public void removeAllEmsUserOptions(Long dashboardId)
	{
		getEntityManager().getTransaction().begin();
		em.createNamedQuery("EmsUserOptions.removeAll").setParameter("dashboardId", dashboardId).executeUpdate();
		commitTransaction();
	}

	public void removeEmsDashboard(EmsDashboard emsDashboard)
	{
		emsDashboard = em.find(EmsDashboard.class, emsDashboard.getDashboardId());
		em.remove(emsDashboard);
		commitTransaction();
	}

	//	public void removeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	//	{
	//		emsDashboardFavorite = em.find(EmsDashboardFavorite.class, new EmsDashboardFavoritePK(emsDashboardFavorite.getUserName(),
	//				emsDashboardFavorite.getDashboard().getDashboardId()));
	//		em.remove(emsDashboardFavorite);
	//		commitTransaction();
	//	}

	//	public void removeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	//	{
	//		emsDashboardLastAccess = em.find(EmsDashboardLastAccess.class,
	//				new EmsDashboardLastAccessPK(emsDashboardLastAccess.getAccessedBy(), emsDashboardLastAccess.getDashboardId()));
	//		em.remove(emsDashboardLastAccess);
	//		commitTransaction();
	//	}

	//	public void removeEmsDashboardTile(EmsDashboardTile emsDashboardTile)
	//	{
	//		emsDashboardTile = em.find(EmsDashboardTile.class, emsDashboardTile.getTileId());
	//		em.remove(emsDashboardTile);
	//		commitTransaction();
	//	}

	//	public void removeEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams)
	//	{
	//		emsDashboardTileParams = em.find(EmsDashboardTileParams.class,
	//				new EmsDashboardTileParamsPK(emsDashboardTileParams.getParamName(), emsDashboardTileParams.getDashboardTile()
	//						.getTileId()));
	//		em.remove(emsDashboardTileParams);
	//		commitTransaction();
	//	}

	public void removeEmsPreference(EmsPreference emsPreference)
	{
		emsPreference = em
				.find(EmsPreference.class, new EmsPreferencePK(emsPreference.getPrefKey(), emsPreference.getUserName()));
		em.remove(emsPreference);
		commitTransaction();
	}

	public int removeEmsSubDashboardBySetId(long dashboardSetId)
	{
		getEntityManager().getTransaction().begin();
		int deleteCout = em.createNamedQuery("EmsSubDashboard.removeByDashboardSetID").setParameter("p", dashboardSetId)
				.executeUpdate();
		commitTransaction();
		return deleteCout;
	}

	public int removeEmsSubDashboardBySubId(long subDashboardId)
	{
		getEntityManager().getTransaction().begin();
		int deleteCout = em.createNamedQuery("EmsSubDashboard.removeBySubDashboardID").setParameter("p", subDashboardId)
				.executeUpdate();
		commitTransaction();
		return deleteCout;
	}

	public void updateSubDashboardShowInHome(long dashboardId)
	{
		getEntityManager().getTransaction().begin();
		List<EmsSubDashboard> emsSubDashboards = getEmsDashboardById(dashboardId).getSubDashboardList();
		if (emsSubDashboards != null) {
			for (EmsSubDashboard emsSubDashboard : emsSubDashboards) {
				EmsDashboard dashboard = getEmsDashboardById(emsSubDashboard.getSubDashboardId());
				dashboard.setShowInHome(1);
				em.merge(dashboard);
			}
		}
	}

	public List<EmsDashboard> getEmsDashboardsBySubId(long subDashboardId)
	{
		getEntityManager().getTransaction().begin();
		List<EmsDashboard> dashboards = em.createNamedQuery("EmsDashboard.queryBySubDashboardID").setParameter("p", subDashboardId)
				.getResultList();
		commitTransaction();
		return dashboards;
	}

    public int removeUnsharedEmsSubDashboard(long subDashboardId, String owner)
    {
        getEntityManager().getTransaction().begin();
        int deleteCout = em.createNamedQuery("EmsSubDashboard.removeUnshared").setParameter("p1", subDashboardId).setParameter("p2", owner)
                .executeUpdate();
        commitTransaction();
        return deleteCout;
    }

	public void removeEmsUserOptions(EmsUserOptions emsUserOptions)
	{
		emsUserOptions = em.find(EmsUserOptions.class,
				new EmsUserOptionsPK(emsUserOptions.getUserName(), emsUserOptions.getDashboardId()));
		em.remove(emsUserOptions);
		commitTransaction();
	}

}
