package oracle.sysman.emaas.platform.dashboards.core.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import oracle.sysman.emaas.platform.dashboards.core.BaseTest;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class PersistenceManagerTest extends BaseTest
{
	@BeforeTest
	public void setUp()
	{
	}

	@Test
	public void testPersistenceManager()
	{
		long testTenant = 3L;
		PersistenceManager pm = PersistenceManager.getInstance();
		Assert.assertNotNull(pm.getEntityManagerFactory());

		// close emf and create em
		pm.closeEntityManagerFactory();
		EntityManager em = pm.createEntityManager(testTenant);
		Assert.assertNotNull(em);

		// close emf, get emf and create em
		pm.closeEntityManagerFactory();
		EntityManagerFactory emf = pm.getEntityManagerFactory();
		Assert.assertNotNull(emf);
		em = pm.createEntityManager(testTenant);
		Object tenantId = em.getProperties().get("tenant.id");
		Assert.assertEquals(tenantId, testTenant);
	}
}
