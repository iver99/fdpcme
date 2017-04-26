package oracle.sysman.emaas.platform.dashboards.test.TenantSubscriptions;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author shangwan
 */
public class TenantSubscriptions_license
{

	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */
	private static final Logger LOGGER = LogManager.getLogger(TenantSubscriptions.class);
	static String HOSTNAME;
	static String portno;
	static String serveruri;
	static String authToken;
	static String tenantid;
	static String tenantid_2;
	static String remoteuser;

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
		tenantid = ct.getTenantid();
		tenantid_2 = ct.getTenantid2();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void getSubscribedapps_license()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getSubscribedapps_license_OMCEE()
	{
		tenantid = CommonTest.getTenantName("df_omcee");
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	//@Test
	//	public void getSubscribedapps_license_OMCEnterprise()
	//	{
	//		tenantid = "df_omc_enterprise";
	//		try {
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/subscribedapps2");
	//			Assert.assertTrue(res1.getStatusCode() == 200);
	//			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));
	//
	//		}
	//		catch (Exception e) {
	//			LOGGER.info("context", e);
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//	}

	//@Test
	//	public void getSubscribedapps_license_OMCLog()
	//	{
	//		tenantid = "df_omc_log";
	//		try {
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/subscribedapps2");
	//			Assert.assertTrue(res1.getStatusCode() == 200);
	//			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));
	//
	//		}
	//		catch (Exception e) {
	//			LOGGER.info("context", e);
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//	}

	@Test
	public void getSubscribedapps_license_OMCLOG_V3()
	{
		tenantid = CommonTest.getTenantName("df_omclog");
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getSubscribedapps_license_OMCSE()
	{
		tenantid = CommonTest.getTenantName("df_omcse");
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	//@Test
	//	public void getSubscribedapps_license_OMCStandard()
	//	{
	//		tenantid = "df_omc_standard";
	//		try {
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/subscribedapps2");
	//			Assert.assertTrue(res1.getStatusCode() == 200);
	//			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));
	//
	//		}
	//		catch (Exception e) {
	//			LOGGER.info("context", e);
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//	}

	//@Test
	//	public void getSubscribedapps_license_OSMACCCompliance()
	//	{
	//		tenantid = "df_osmacc_compliance";
	//		try {
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/subscribedapps2");
	//			Assert.assertTrue(res1.getStatusCode() == 200);
	//			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));
	//
	//		}
	//		catch (Exception e) {
	//			LOGGER.info("context", e);
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//	}

	//@Test
	//	public void getSubscribedapps_license_OSMACCSecurity()
	//	{
	//		tenantid = "df_osmacc_security";
	//		try {
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/subscribedapps2");
	//			Assert.assertTrue(res1.getStatusCode() == 200);
	//			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
	//			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));
	//
	//		}
	//		catch (Exception e) {
	//			LOGGER.info("context", e);
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//	}

	@Test
	public void getSubscribedapps_license_SECSE()
	{
		tenantid = CommonTest.getTenantName("df_secse");
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getSubscribedapps_license_SECSMA()
	{
		tenantid = CommonTest.getTenantName("df_secsma");
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("applications.id[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.licVersion[0]"));
			Assert.assertNotNull(res1.jsonPath().get("applications.editions[0]"));

		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getSubscribedappsWrongTenant()
	{
		try {
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "wrongtenant", "X-REMOTE-USER", "wrongtenant" + "." + remoteuser,
							"Authorization", authToken).when().get("/subscribedapps2");
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20002);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified tenant does not subscribe to any service");
		}
		catch (Exception e) {
			LOGGER.info("context", e);
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
