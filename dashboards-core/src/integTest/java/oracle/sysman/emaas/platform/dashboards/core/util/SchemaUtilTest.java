package oracle.sysman.emaas.platform.dashboards.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class SchemaUtilTest
{
	private static final String schemaDeployments = "[" + "{" + "\"deploymentId\": 1,"
			+ "\"deploymentUUID\": \"617bd35a-ad45-4bac-8ada-090db2e57365\"," + "\"containerName\": \"slc06wfs.us.oracle.com\","
			+ "\"softwareName\": \"DataStorageService\"," + "\"softwareVersion\": \"1.0.0\","
			+ "\"deploymentTs\": 1433849006183," + "\"deploymentType\": \"schema_deployment\","
			+ "\"deploymentStatus\": \"SUCCESS\"," + "\"schemaId\": \"1\"," + "\"schemaUser\": \"SYSEMS_T_1\","
			+ "\"entityDomainName\": \"CloudDatabaseResources\"," + "\"entityDomainKey\": \"id\","
			+ "\"entityDomainValue\": \"-Common-Ingest\"" + "}," + "{" + "\"deploymentId\": 2,"
			+ "\"deploymentUUID\": \"9fef4183-23cf-4718-a794-6d714eb09080\"," + "\"containerName\": \"slc06wfs.us.oracle.com\","
			+ "\"softwareName\": \"emaas-platform-target-schema\"," + "\"softwareVersion\": \"1.0.0\","
			+ "\"deploymentTs\": 1433849157085," + "\"deploymentType\": \"schema_deployment\","
			+ "\"deploymentStatus\": \"SUCCESS\"," + "\"schemaId\": \"2\"," + "\"schemaUser\": \"SYSEMS_T_2\","
			+ "\"entityDomainName\": \"CloudDatabaseResources\"," + "\"entityDomainKey\": \"id\","
			+ "\"entityDomainValue\": \"dev-appshard1-apps-OPS\"" + "}," + "{" + "\"deploymentId\": 3,"
			+ "\"deploymentUUID\": \"1b737320-d588-47db-b468-3d138a11549f\"," + "\"containerName\": \"slc06wfs.us.oracle.com\","
			+ "\"softwareName\": \"emcitas-platform\"," + "\"softwareVersion\": \"1.0.0\"," + "\"deploymentTs\": 1433849450061,"
			+ "\"deploymentType\": \"schema_deployment\"," + "\"deploymentStatus\": \"SUCCESS\"," + "\"schemaId\": \"3\","
			+ "\"schemaUser\": \"SYSEMS_T_3\"," + "\"entityDomainName\": \"CloudDatabaseResources\","
			+ "\"entityDomainKey\": \"id\"," + "\"entityDomainValue\": \"dev-appshard1-apps-ITA\"" + "}," + "{"
			+ "\"deploymentId\": 4," + "\"deploymentUUID\": \"1a9d1075-a9ad-4d75-a41c-3ced01397c3a\","
			+ "\"containerName\": \"slc06wfs.us.oracle.com\"," + "\"softwareName\": \"emcitas-platform\","
			+ "\"softwareVersion\": \"1.0.0\"," + "\"deploymentTs\": 1433849450070,"
			+ "\"deploymentType\": \"schema_deployment\"," + "\"deploymentStatus\": \"SUCCESS\"," + "\"schemaId\": \"4\","
			+ "\"schemaUser\": \"SYSEMS_T_4\"," + "\"entityDomainName\": \"CloudDatabaseResources\","
			+ "\"entityDomainKey\": \"id\"," + "\"entityDomainValue\": \"dev-appshard1-apps-ITA\"" + "}," + "{"
			+ "\"deploymentId\": 5," + "\"deploymentUUID\": \"d2a9390a-2f11-46ce-aec4-d071bd6c22c3\","
			+ "\"containerName\": \"slc06wfs.us.oracle.com\"," + "\"softwareName\": \"emaas-awrwh\","
			+ "\"softwareVersion\": \"1.0.0\"," + "\"deploymentTs\": 1433849611766,"
			+ "\"deploymentType\": \"schema_deployment\"," + "\"deploymentStatus\": \"SUCCESS\"," + "\"schemaId\": \"7\","
			+ "\"schemaUser\": \"SYSEMS_T_7\"," + "\"entityDomainName\": \"CloudDatabaseResources\","
			+ "\"entityDomainKey\": \"id\"," + "\"entityDomainValue\": \"dev-appshard1-apps-ITA\"" + "}," + "{"
			+ "\"deploymentId\": 6," + "\"deploymentUUID\": \"ad32907a-53f7-4651-8229-59faa7fd4028\","
			+ "\"containerName\": \"slc06wfs.us.oracle.com\"," + "\"softwareName\": \"emaas-awrwh\","
			+ "\"softwareVersion\": \"1.0.0\"," + "\"deploymentTs\": 1433849611781,"
			+ "\"deploymentType\": \"schema_deployment\"," + "\"deploymentStatus\": \"SUCCESS\"," + "\"schemaId\": \"5\","
			+ "\"schemaUser\": \"SYSEMS_T_5\"," + "\"entityDomainName\": \"CloudDatabaseResources\","
			+ "\"entityDomainKey\": \"id\"," + "\"entityDomainValue\": \"dev-appshard1-apps-ITA\"" + "}" + "]";

	private static final String urlItems = "{\"items\":[{" + "\"virtualEndpoints\": ["
			+ "\"http://slc07hgf.us.oracle.com:7001/lifecycle-schema-service/LifecycleInvManager\","
			+ "\"https://slc07hgf.us.oracle.com:7002/lifecycle-schema-service/LifecycleInvManager\"" + "],"
			+ "\"canonicalEndpoints\": ["
			+ "\"http://slc07hgf.us.oracle.com:7001/lifecycle-schema-service/LifecycleInvManager\","
			+ "\"https://slc07hgf.us.oracle.com:7002/lifecycle-schema-service/LifecycleInvManager\"" + "]" + "}]}";

	@Test(groups = { "s2" })
	public void testGetDeploymentUrl()
	{
		List<String> urls = SchemaUtil.getDeploymentUrl(urlItems);
		Assert.assertEquals(urls.get(0), "http://slc07hgf.us.oracle.com:7001/lifecycle-schema-service/LifecycleInvManager");
	}

	@Test(groups = { "s2" })
	public void testGetSchemaUserBySoftwareName()
	{
		SchemaUtil su = new SchemaUtil();
		String user = su.getSchemaUserBySoftwareName(schemaDeployments, "emcitas-platform");
		Assert.assertEquals(user, "SYSEMS_T_3");
	}

	@Mocked
	HttpURLConnection httpURLConnection;
	@Mocked
	InputStream inputStream;
	@Test(groups = { "s2" })
	public void testGet() throws IOException {
		SchemaUtil su = new SchemaUtil();
		su.get("http:domainname.com/");
		su.get(null);
	}
}
