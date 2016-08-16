package oracle.sysman.emaas.platform.dashboards.test.RegistryLookup;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class RegistryLookup
{
	/**
	 * Calling CommonTest.java to Set up RESTAssured defaults & Reading the inputs from the testenv.properties file before
	 * executing test cases
	 */

	static String HOSTNAME;
	static String portno;
	static String serveruri;
	static String authToken;
	static String tenantid;
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
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void invalidUrl()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Negative cases for no querystring");
			//System.out.println("											");

			//System.out.println("Negative cases 1 : endpoint");
			//System.out.println("											");

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			//System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=null, version=null");

			//System.out.println("											");

			//System.out.println("Negative cases 2 : link");
			//System.out.println("											");

			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/link");

			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			//System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Link Not Found: serviceName=null, version=null, rel=null");

			//System.out.println("											");

			//System.out.println("Negative cases 1 : linkWithRelPrefix");
			//System.out.println("											");

			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/linkWithRelPrefix");

			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			//System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=null, version=null, rel prefix=null");

			//System.out.println("											");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupEndPoint()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("GET details of endpoint");
			//System.out.println("											");

			//System.out.println("											");
			//System.out.println("cases 1 : with all parameters");
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=1.0");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("serviceName"), "Dashboard-UI");
			Assert.assertEquals(res.jsonPath().get("version"), "1.0");
			Assert.assertNotNull(res.jsonPath().get("href"));

			//System.out.println("											");
			//System.out.println("cases 2 : no version");
			//System.out.println("											");

			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=");

			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			//System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("serviceName"), "Dashboard-UI");
			Assert.assertEquals(res1.jsonPath().get("version"), "");
			Assert.assertNotNull(res1.jsonPath().get("href"));

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupEndPointInvalidPara()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Negative cases for lookup endpoint");
			//System.out.println("											");

			//System.out.println("Negative cases 1 : no serviceName");
			//System.out.println("											");

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint?serviceName=&version=1.0");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			//System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=, version=1.0");

			//System.out.println("											");
			//System.out.println("Negative cases 2 : wrong serviceName");
			//System.out.println("											");

			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/endpoint?serviceName=abc&version=1.0");

			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			//System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=abc, version=1.0");

			//System.out.println("											");
			//System.out.println("Negative cases 3 : wrong version");
			//System.out.println("											");

			Response res3 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=999");

			//System.out.println("											");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 404);
			//System.out.println(res3.asString());

			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "End Point Not Found: serviceName=Dashboard-UI, version=999");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupLink()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("GET details of link");
			//System.out.println("											");

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=sso.home");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("rel"), "sso.home");
			Assert.assertNotNull(res.jsonPath().get("href"));

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void lookupLinkInvalidPara()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Negative cases for lookup link");
			//System.out.println("											");

			//System.out.println("Negative cases 1 : no serviceName");
			//System.out.println("											");

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("registry/lookup/link?serviceName=&version=1.0&rel=sso.home");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			//System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "Link Not Found: serviceName=, version=1.0, rel=sso.home");

			//System.out.println("											");

			//System.out.println("Negative cases 2 : no version");
			//System.out.println("											");

			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/link?serviceName=Dashboard-UI&version=&rel=sso.home");

			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			//System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=, rel=sso.home");

			//System.out.println("											");

			//System.out.println("Negative cases 3 : no rel");
			//System.out.println("											");

			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=");

			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			//System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=1.0, rel=");

			//System.out.println("											");

			//System.out.println("Negative cases 4 : wrong servicename");
			//System.out.println("											");

			Response res3 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/link?serviceName=abc&version=1.0&rel=sso.home");

			//System.out.println("											");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 404);
			//System.out.println(res3.asString());

			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "Link Not Found: serviceName=abc, version=1.0, rel=sso.home");

			//System.out.println("											");

			//System.out.println("Negative cases 5 : wrong version");
			//System.out.println("											");

			Response res4 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/link?serviceName=Dashboard-UI&version=999&rel=sso.home");

			//System.out.println("											");
			//System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 404);
			//System.out.println(res4.asString());

			Assert.assertEquals(res4.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=999, rel=sso.home");

			//System.out.println("											");

			//System.out.println("Negative cases 6 : wrong rel");
			//System.out.println("											");

			Response res5 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=abc");

			//System.out.println("											");
			//System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 404);
			//System.out.println(res5.asString());

			Assert.assertEquals(res5.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Link Not Found: serviceName=Dashboard-UI, version=1.0, rel=abc");

			//System.out.println("											");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupLinkWithRelPrefix()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("GET details for linkWithRelPrefix");
			//System.out.println("											");

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=quickLink");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("rel"), "quickLink/Dashboard Home");
			Assert.assertNotNull(res.jsonPath().get("href"));

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void lookupLinkWithRelPrefix_invalidPara()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Negative cases for lookup linkWithRelPrefix");
			//System.out.println("											");

			//System.out.println("Negative cases 1 : no serviceName");
			//System.out.println("											");

			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/linkWithRelPrefix?serviceName=&version=1.0&rel=quickLink");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			//System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=, version=1.0, rel prefix=quickLink");

			//System.out.println("											");

			//System.out.println("Negative cases 2 : no version");
			//System.out.println("											");

			Response res1 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=&rel=quickLink");

			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			//System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=, rel prefix=quickLink");

			//System.out.println("											");

			//System.out.println("Negative cases 3 : no rel");
			//System.out.println("											");

			Response res2 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=");

			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			//System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=1.0, rel prefix=");

			//System.out.println("											");

			//System.out.println("Negative cases 4 : wrong serviceName");
			//System.out.println("											");

			Response res3 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/linkWithRelPrefix?serviceName=wrong&version=1.0&rel=quickLink");

			//System.out.println("											");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 404);
			//System.out.println(res3.asString());

			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=wrong, version=1.0, rel prefix=quickLink");

			//System.out.println("											");

			//System.out.println("Negative cases 5 : wrong version");
			//System.out.println("											");

			Response res4 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=999&rel=quickLink");

			//System.out.println("											");
			//System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 404);
			//System.out.println(res4.asString());

			Assert.assertEquals(res4.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=999, rel prefix=quickLink");

			//System.out.println("											");

			//System.out.println("Negative cases 6 : wrong rel");
			//System.out.println("											");

			Response res5 = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when()
							.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=abc");

			//System.out.println("											");
			//System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 404);
			//System.out.println(res5.asString());

			Assert.assertEquals(res5.jsonPath().get("errorCode"), 2003);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Link (with rel Prefix) Not Found: serviceName=Dashboard-UI, version=1.0, rel prefix=abc");

			//System.out.println("											");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void multiTenantHeaderCheck()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Check mutlitenant header of endpoint");
			//System.out.println("											");

			Response res = RestAssured.given().log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=1.0");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 500);
			//			Assert.assertEquals(res.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			//System.out.println("Check multitenant header of link");
			//System.out.println("											");

			Response res1 = RestAssured.given().log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=sso.home");

			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 500);
			//			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			//System.out.println("Check multitenant header for linkWithRelPrefix");
			//System.out.println("											");

			Response res2 = RestAssured.given().log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=quickLink");

			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 500);
			//			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void remoteUserHeaderCheck()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Check remoteuser header of endpoint");
			//System.out.println("											");

			Response res = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=1.0");

			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 403);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//System.out.println("Check remoteuser header of link");
			//System.out.println("											");

			Response res1 = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=1.0&rel=sso.home");

			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//System.out.println("Check remoteuser header for linkWithRelPrefix");
			//System.out.println("											");

			Response res2 = RestAssured.given().log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=1.0&rel=quickLink");

			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}
}
