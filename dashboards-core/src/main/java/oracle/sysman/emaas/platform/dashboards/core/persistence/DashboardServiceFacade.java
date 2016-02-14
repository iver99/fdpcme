package oracle.sysman.emaas.platform.dashboards.core.persistence;

import oracle.sysman.emaas.platform.dashboards.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;

//import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavorite;
//import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavoritePK;

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

	/** <code>select o from EmsDashboardFavorite o</code> */
	public List<EmsDashboardFavorite> getEmsDashboardFavoriteFindAll()
	{
		return em.createNamedQuery("EmsDashboardFavorite.findAll", EmsDashboardFavorite.class).getResultList();
	}

	/** <code>select o from EmsDashboard o</code> */
	public List<EmsDashboard> getEmsDashboardFindAll()
	{
		return em.createNamedQuery("EmsDashboard.findAll", EmsDashboard.class).getResultList();
	}

	/** <code>select o from EmsDashboardLastAccess o</code> */
	public List<EmsDashboardLastAccess> getEmsDashboardLastAccessFindAll()
	{
		return em.createNamedQuery("EmsDashboardLastAccess.findAll", EmsDashboardLastAccess.class).getResultList();
	}

	/** <code>select o from EmsDashboardTile o</code> */
	public List<EmsDashboardTile> getEmsDashboardTileFindAll()
	{
		return em.createNamedQuery("EmsDashboardTile.findAll", EmsDashboardTile.class).getResultList();
	}

	/** <code>select o from EmsDashboardTileParams o</code> */
	public List<EmsDashboardTileParams> getEmsDashboardTileParamsFindAll()
	{
		return em.createNamedQuery("EmsDashboardTileParams.findAll", EmsDashboardTileParams.class).getResultList();
	}

	public EmsPreference getEmsPreference(String username, String prefKey)
	{
		return em.find(EmsPreference.class, new EmsPreferencePK(prefKey, username));
	}

	/** <code>select o from EmsPreference o</code> */
	public List<EmsPreference> getEmsPreferenceFindAll(String username)
	{
		return em.createNamedQuery("EmsPreference.findAll", EmsPreference.class).setParameter("username", username)
				.getResultList();
	}

	public EmsUserOptions getEmsUserOptions(String username, Long dashboardId) {
		return em.find(EmsUserOptions.class, new EmsUserOptionsPK(username,dashboardId));
	}

	public EmsUserOptions persistEmsUserOptions(EmsUserOptions emsUserOptions) {
        em.persist(emsUserOptions);
        commitTransaction();
        return emsUserOptions;
	}

	public EmsUserOptions mergeEmsUserOptions(EmsUserOptions emsUserOptions) {
        EmsUserOptions entity = null;
        entity = em.merge(emsUserOptions);
        commitTransaction();
        return entity;
	}
	public EntityManager getEntityManager()
	{
		return em;
	}

	public EmsDashboard mergeEmsDashboard(EmsDashboard emsDashboard)
	{
		EmsDashboard entity = null;
		entity = em.merge(emsDashboard);
		commitTransaction();
		return entity;
	}

	public EmsDashboardFavorite mergeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	{
		EmsDashboardFavorite entity = null;
		entity = em.merge(emsDashboardFavorite);
		commitTransaction();
		return entity;
	}

	public EmsDashboardLastAccess mergeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	{
		EmsDashboardLastAccess entity = null;
		entity = em.merge(emsDashboardLastAccess);
		commitTransaction();
		return entity;
	}

	public EmsPreference mergeEmsPreference(EmsPreference emsPreference)
	{
		EmsPreference entity = null;
		entity = em.merge(emsPreference);
		commitTransaction();
		return entity;
	}

	//	public <T> T mergeEntity(T entity)
	//	{
	//		entity = em.merge(entity);
	//		commitTransaction();
	//		return entity;
	//	}

	public EmsDashboard persistEmsDashboard(EmsDashboard emsDashboard)
	{
                if (emsDashboard.getSharePublic() == null)
                {
                    emsDashboard.setSharePublic(0);
                }

		em.persist(emsDashboard);
		commitTransaction();
		return emsDashboard;
	}

	public EmsDashboardFavorite persistEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	{
		em.persist(emsDashboardFavorite);
		commitTransaction();
		return emsDashboardFavorite;
	}

	public EmsDashboardLastAccess persistEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	{
		em.persist(emsDashboardLastAccess);
		commitTransaction();
		return emsDashboardLastAccess;
	}

	public EmsPreference persistEmsPreference(EmsPreference emsPreference)
	{
		em.persist(emsPreference);
		commitTransaction();
		return emsPreference;
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

	public void removeEmsDashboard(EmsDashboard emsDashboard)
	{
		emsDashboard = em.find(EmsDashboard.class, emsDashboard.getDashboardId());
		em.remove(emsDashboard);
		commitTransaction();
	}

	public void removeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite)
	{
		emsDashboardFavorite = em.find(EmsDashboardFavorite.class, new EmsDashboardFavoritePK(emsDashboardFavorite.getUserName(),
				emsDashboardFavorite.getDashboard().getDashboardId()));
		em.remove(emsDashboardFavorite);
		commitTransaction();
	}

	public void removeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess)
	{
		emsDashboardLastAccess = em.find(EmsDashboardLastAccess.class,
				new EmsDashboardLastAccessPK(emsDashboardLastAccess.getAccessedBy(), emsDashboardLastAccess.getDashboardId()));
		em.remove(emsDashboardLastAccess);
		commitTransaction();
	}

	public void removeEmsDashboardTile(EmsDashboardTile emsDashboardTile)
	{
		emsDashboardTile = em.find(EmsDashboardTile.class, emsDashboardTile.getTileId());
		em.remove(emsDashboardTile);
		commitTransaction();
	}

	public void removeEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams)
	{
		emsDashboardTileParams = em.find(EmsDashboardTileParams.class,
				new EmsDashboardTileParamsPK(emsDashboardTileParams.getParamName(), emsDashboardTileParams.getDashboardTile()
						.getTileId()));
		em.remove(emsDashboardTileParams);
		commitTransaction();
	}

	public void removeEmsPreference(EmsPreference emsPreference)
	{
		emsPreference = em
				.find(EmsPreference.class, new EmsPreferencePK(emsPreference.getPrefKey(), emsPreference.getUserName()));
		em.remove(emsPreference);
		commitTransaction();
	}
}
