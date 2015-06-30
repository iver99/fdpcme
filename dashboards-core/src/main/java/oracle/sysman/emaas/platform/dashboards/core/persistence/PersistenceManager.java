package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import oracle.sysman.emaas.platform.dashboards.core.util.SchemaUtil;
import oracle.sysman.qatool.uifwk.utils.Utils; 
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
	private static final String SERVICE_MANAGER_URL = "SERVICE_MANAGER_URL";
	private static final String DEPLOY_URL = "/instances?servicename=LifecycleInventoryService";
	private static final String SERVICE_NAME = "dashboardService-api";
	 private static final String DEPLY_SCHEMA = "/schemaDeployments?softwareName=dashboardService-api";

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
		if (emf == null) {
			initialize();
		}
		return emf;
	}

	
	/*public static void main(String args[])
	{
		SchemaUtil rct = new SchemaUtil();				
		String url = "http://slc08twq.us.oracle.com:7004/registry/servicemanager/registry/v1" + DEPLOY_URL;
		String data = rct.get(url);		
		List<String> urlList = rct.getDeploymentUrl(data );
		data = rct.get(urlList.get(0)+DEPLY_SCHEMA);
		String schemaName = rct.getSchemaUserBySoftwareName(data, SERVICE_NAME);
		System.out.println(schemaName);
	}*/
	
	
	
	private void initialize()
	{
		if (IS_TEST_ENV) {
			// testng local properties
			Properties props = loadProperties(CONNECTION_PROPS_FILE);
			// lrg env only
			if (System.getenv("T_WORK") != null) {
				
				
				String url = "jdbc:oracle:thin:@"+Utils.getProperty("ODS_HOSTNAME")+":"+Utils.getProperty("ODS_PORT")+":"+Utils.getProperty("ODS_SERVICE");
				props.put("javax.persistence.jdbc.url", url);
				SchemaUtil rct = new SchemaUtil();				
				String data = Utils.getProperty(SERVICE_MANAGER_URL) + DEPLOY_URL;
				 data = rct.get(url);		
				List<String> urlList = rct.getDeploymentUrl(data );
				String schemaName = null;
				for(String tmp :urlList)					
				{
					data = rct.get(tmp+DEPLY_SCHEMA);				
					List<String> urls=rct.getDeploymentUrl(data);				
					 schemaName = rct.getSchemaUserBySoftwareName(data, SERVICE_NAME);	
					if(schemaName!=null)
						break;
				}		
				
				props.put("javax.persistence.jdbc.user", schemaName);
				String password = "welcome1";
				props.put("javax.persistence.jdbc.password", password);
		
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
			input = PersistenceManager.class.getResourceAsStream("/"+testPropsFile);//new FileInputStream(testPropsFile);
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
