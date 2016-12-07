package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import oracle.sysman.emaas.platform.dashboards.core.util.SessionInfoUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PersistenceManager
{
    /** 
     * mapping to QAToolUtil
     * Picking up db env when running test cases
     */
    public static final String JDBC_PARAM_URL = "javax.persistence.jdbc.url";
    public static final String JDBC_PARAM_USER = "javax.persistence.jdbc.user";
    public static final String JDBC_PARAM_PASSWORD = "javax.persistence.jdbc.password";
    public static final String JDBC_PARAM_DRIVER = "javax.persistence.jdbc.driver";
    
    private static final String MODULE_NAME = "DashboardService"; // application service name
	private final String ACTION_NAME = this.getClass().getSimpleName();//current class name
    
	private static final Logger LOGGER = LogManager.getLogger(PersistenceManager.class);

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
		synchronized (lock) {
			if (singleton == null) {
				singleton = new PersistenceManager();
			}
			return singleton;
		}
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
		try {
			SessionInfoUtil.setModuleAndAction(em, MODULE_NAME, ACTION_NAME);
		} catch (SQLException e) {
			LOGGER.info("setModuleAndAction in PersistenceManager",e);
		}
		em.setProperty("tenant.id", tenantId);
		return em;
	}

	public EntityManagerFactory getEntityManagerFactory()
	{
		if (emf == null) {
			initialize();
		}
		return emf;
	}

	private void initialize()
	{
		if (IS_TEST_ENV) {
			LOGGER.info("Dashboard JPA Persistence Manager is now running in test environment");
			// testng local properties
			Properties props = loadProperties(CONNECTION_PROPS_FILE);
			// lrg env only
			if (System.getenv("T_WORK") != null) {
                props.put(PersistenceManager.JDBC_PARAM_URL, System.getProperty(PersistenceManager.JDBC_PARAM_URL));
                props.put(PersistenceManager.JDBC_PARAM_USER, System.getProperty(PersistenceManager.JDBC_PARAM_USER));
                props.put(PersistenceManager.JDBC_PARAM_PASSWORD, System.getProperty(PersistenceManager.JDBC_PARAM_PASSWORD));
                props.put(PersistenceManager.JDBC_PARAM_DRIVER, System.getProperty(PersistenceManager.JDBC_PARAM_DRIVER));
			}
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
			input = PersistenceManager.class.getResourceAsStream("/" + testPropsFile);//new FileInputStream(testPropsFile);
			connectionProps.load(input);
			return connectionProps;
		}
		catch (Exception ex) {
			LOGGER.error(ex.getLocalizedMessage(), ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			}
		}
		return connectionProps;

	}

	protected synchronized void createEntityManagerFactory(String puName, Properties props)
	{
		emf = Persistence.createEntityManagerFactory(puName, props);
		LOGGER.debug("EntityManagerFactory has been created with properties {}", props == null ? null : props.toString());
	}
}
