package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.emaas.platform.dashboards.core.util.SchemaUtil;
import oracle.sysman.qatool.uifwk.utils.Utils;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class PersistenceManagerTest_S2
{
	@BeforeTest
	public void setUp()
	{
		PersistenceManager.setTestEnv(true);
	}

	@Test(groups = { "s2" })
	public void testPersistenceManager(@Mocked final Properties mockProperties, @Mocked final EntityManagerFactory memf,
			@Mocked final EntityManager mem, @Mocked final SchemaUtil msu, @Mocked final Persistence mp, @Mocked final System ms,
			@Mocked final Utils mu) throws IOException
	{
		final Properties props = getMockProperties();
		props.put("tenant.id", 3);
		new NonStrictExpectations() {
			{
				mockProperties.load((Reader) any);
				result = null;
			}
		};

		new NonStrictExpectations() {
			{
				msu.get(anyString);
				result = "dummy";
				mem.getProperties();
				result = props;
				Persistence.createEntityManagerFactory(anyString, (java.util.Map<?, ?>) any);
				result = memf;
				System.getenv(anyString);
				result = "dummy";
				Utils.getProperty(anyString);
				result = "dummy";
			}
		};
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
		Assert.assertNotNull(em);
		//		Object tenantId = em.getProperties().get("tenant.id");
		//		Assert.assertEquals(tenantId, testTenant);
	}

	private Properties getMockProperties()
	{
		Properties props = new Properties();
		return props;
	}
}
