package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager
{
	/**
	 * For the whole JVM life cycle, IS_TEST_ENV can only be set once
	 */
	private static Boolean IS_TEST_ENV = null;

	private static final String PERSISTENCE_UNIT = "DashboardsPU";

	private static final String TEST_PERSISTENCE_UNIT = "DashboardsTestPU";
	private static final String CONNECTION_PROPS_FILE = "TestNG.properties";
	private static PersistenceManager singleton;
	private static Object lock = new Object();

	public static PersistenceManager getInstance()
	{
		if (singleton == null) {
			synchronized (lock) {
				if (singleton == null) {
					singleton = new PersistenceManager();
				}
			}
		}
		return singleton;
	}

	public static void setTestEnv(boolean value)
	{
		synchronized (lock) {
			if (IS_TEST_ENV == null) {
				IS_TEST_ENV = true;
			}
		}
	}

	private EntityManagerFactory emf;

	private PersistenceManager()
	{
		if (IS_TEST_ENV == null) {
			IS_TEST_ENV = false;
		}

		initialize();
	}

	public synchronized void closeEntityManagerFactory()
	{
		if (emf != null) {
			emf.close();
			emf = null;
		}
	}

	public EntityManager createEntityManager(Long tenantId)
	{
		if (emf == null) {
			initialize();
		}
		EntityManager em = emf.createEntityManager();
		em.setProperty("tenant.id", tenantId);
		return em;
	}

	public EntityManagerFactory getEntityManagerFactory()
	{
		return emf;
	}

	private void initialize()
	{
		if (IS_TEST_ENV) {
			Properties props = loadProperties(CONNECTION_PROPS_FILE);
			createEntityManagerFactory(TEST_PERSISTENCE_UNIT, props);
		}
		else {
			createEntityManagerFactory(PERSISTENCE_UNIT, null);
		}
	}

	private Properties loadProperties(String testPropsFile)
	{
		Properties connectionProps = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(testPropsFile);
			connectionProps.load(input);
			return connectionProps;
		}
		catch (Exception ex) {
			ex.printStackTrace();

		}
		return connectionProps;

	}

	protected synchronized void createEntityManagerFactory(String puName, Properties props)
	{
		emf = Persistence.createEntityManagerFactory(puName, props);
	}
}
