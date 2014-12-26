package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavorite;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavoritePK;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccess;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccessPK;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParamsPK;

public class DashboardServiceFacade {
    private final EntityManager em;

    public DashboardServiceFacade(String tenantId) {
        final EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();
        em = emf.createEntityManager();
        em.setProperty("tenant.id", tenantId);
    }
    
    public EntityManager getEntityManager() {
            return em;
    }

    /**
     * All changes that have been made to the managed entities in the
     * persistence context are applied to the database and committed.
     */
    public void commitTransaction() {
        final EntityTransaction entityTransaction = em.getTransaction();
        if (!entityTransaction.isActive()) {
            entityTransaction.begin();
        }
        entityTransaction.commit();
    }

    public List queryByRange(String jpqlStmt, Class resultClass, Map<String, Object> params, int firstResult, int maxResults) {
        Query query = (resultClass == null ? em.createQuery(jpqlStmt) : em.createQuery(jpqlStmt, resultClass));
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, Object> p : params.entrySet()) {
                query.setParameter(p.getKey(), p.getValue());
            }
        }
        if (firstResult > 0) {
            query = query.setFirstResult(firstResult);
        }
        if (maxResults > 0) {
            query = query.setMaxResults(maxResults);
        }
        return query.getResultList();
    }

    public <T> T persistEntity(T entity) {
        em.persist(entity);
        commitTransaction();
        return entity;
    }

    public <T> T mergeEntity(T entity) {
        entity = em.merge(entity);
        commitTransaction();
        return entity;
    }

    public void removeEmsDashboardTile(EmsDashboardTile emsDashboardTile) {
        emsDashboardTile = em.find(EmsDashboardTile.class, emsDashboardTile.getTileId());
        em.remove(emsDashboardTile);
        commitTransaction();
    }

    /** <code>select o from EmsDashboardTile o</code> */
    public List<EmsDashboardTile> getEmsDashboardTileFindAll() {
        return em.createNamedQuery("EmsDashboardTile.findAll", EmsDashboardTile.class).getResultList();
    }

    public EmsDashboard persistEmsDashboard(EmsDashboard emsDashboard) {
        em.persist(emsDashboard);
        commitTransaction();
        return emsDashboard;
    }

    public EmsDashboard mergeEmsDashboard(EmsDashboard emsDashboard) {
        EmsDashboard entity = null;
        entity = em.merge(emsDashboard);
        commitTransaction();
        return entity;
    }

    public void removeEmsDashboard(EmsDashboard emsDashboard) {
        emsDashboard = em.find(EmsDashboard.class, emsDashboard.getDashboardId());
        em.remove(emsDashboard);
        commitTransaction();
    }
    
    public EmsDashboard getEmsDashboardById(Long dashboardId) {
        return em.find(EmsDashboard.class, dashboardId);
    }

    /** <code>select o from EmsDashboard o</code> */
    public List<EmsDashboard> getEmsDashboardFindAll() {
        return em.createNamedQuery("EmsDashboard.findAll", EmsDashboard.class).getResultList();
    }

    public EmsDashboardLastAccess persistEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess) {
        em.persist(emsDashboardLastAccess);
        commitTransaction();
        return emsDashboardLastAccess;
    }

    public EmsDashboardLastAccess mergeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess) {
        EmsDashboardLastAccess entity = null;
        entity = em.merge(emsDashboardLastAccess);
        commitTransaction();
        return entity;
    }

    public void removeEmsDashboardLastAccess(EmsDashboardLastAccess emsDashboardLastAccess) {
        emsDashboardLastAccess =
            em.find(EmsDashboardLastAccess.class,
                    new EmsDashboardLastAccessPK(emsDashboardLastAccess.getAccessedBy(),
                                                 emsDashboardLastAccess.getDashboardId()));
        em.remove(emsDashboardLastAccess);
        commitTransaction();
    }

    /** <code>select o from EmsDashboardLastAccess o</code> */
    public List<EmsDashboardLastAccess> getEmsDashboardLastAccessFindAll() {
        return em.createNamedQuery("EmsDashboardLastAccess.findAll", EmsDashboardLastAccess.class).getResultList();
    }

    public void removeEmsDashboardTileParams(EmsDashboardTileParams emsDashboardTileParams) {
        emsDashboardTileParams =
            em.find(EmsDashboardTileParams.class,
                    new EmsDashboardTileParamsPK(emsDashboardTileParams.getParamName(),
                                                 emsDashboardTileParams.getDashboardTile().getTileId()));
        em.remove(emsDashboardTileParams);
        commitTransaction();
    }

    /** <code>select o from EmsDashboardTileParams o</code> */
    public List<EmsDashboardTileParams> getEmsDashboardTileParamsFindAll() {
        return em.createNamedQuery("EmsDashboardTileParams.findAll", EmsDashboardTileParams.class).getResultList();
    }

    public EmsDashboardFavorite persistEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite) {
        em.persist(emsDashboardFavorite);
        commitTransaction();
        return emsDashboardFavorite;
    }

    public EmsDashboardFavorite mergeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite) {
        EmsDashboardFavorite entity = null;
        entity = em.merge(emsDashboardFavorite);
        commitTransaction();
        return entity;
    }

    public void removeEmsDashboardFavorite(EmsDashboardFavorite emsDashboardFavorite) {
        emsDashboardFavorite =
            em.find(EmsDashboardFavorite.class,
                    new EmsDashboardFavoritePK(emsDashboardFavorite.getUserName(),
                                               emsDashboardFavorite.getDashboard().getDashboardId()));
        em.remove(emsDashboardFavorite);
        commitTransaction();
    }

    /** <code>select o from EmsDashboardFavorite o</code> */
    public List<EmsDashboardFavorite> getEmsDashboardFavoriteFindAll() {
        return em.createNamedQuery("EmsDashboardFavorite.findAll", EmsDashboardFavorite.class).getResultList();
    }
}
