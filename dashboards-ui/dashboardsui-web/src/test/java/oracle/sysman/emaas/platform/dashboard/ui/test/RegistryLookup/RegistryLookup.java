package oracle.sysman.emaas.platform.dashboard.ui.test.RegistryLookup;

import oracle.sysman.emaas.platform.dashboards.ui.test.common.CommonTest;

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

	@BeforeClass
	public static void setUp()
	{
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
	}

	@Test
	public void invalid_url()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("Negative cases for no querystring");
			System.out.println("											");

			System.out.println("Negative cases 1 : endpoint");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/endpoint");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 500);
			System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "Link Not Found: seviceName=, version=0.1, rel=quickLink");

			System.out.println("											");

			System.out.println("Negative cases 2 : link");
			System.out.println("											");

			Response res1 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link");

			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 500);
			System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Link Not Found: seviceName=, version=0.1, rel=quickLink");

			System.out.println("											");

			System.out.println("Negative cases 1 : linkWithRelPrefix");
			System.out.println("											");

			Response res2 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix");

			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 500);
			System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Link Not Found: seviceName=, version=0.1, rel=quickLink");

			System.out.println("											");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupEndPoint()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("GET details of endpoint");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=0.1");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("serviceName"), "Dashboard-UI");
			Assert.assertEquals(res.jsonPath().get("version"), "0.1");
			Assert.assertNotNull(res.jsonPath().get("href"));

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupEndPoint_invalidPara()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("Negative cases for lookup endpoint");
			System.out.println("											");

			System.out.println("Negative cases 1 : no serviceName");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=&version=0.1");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "End Point Not Found: seviceName=, version=0.1");

			System.out.println("											");
			System.out.println("Negative cases 2 : no version");
			System.out.println("											");

			Response res1 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=");

			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "End Point Not Found: seviceName=Dashboard-UI, version=");

			System.out.println("											");
			System.out.println("Negative cases 3 : wrong serviceName");
			System.out.println("											");

			Response res2 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=abc&version=0.1");

			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "End Point Not Found: seviceName=abc, version=0.1");

			System.out.println("											");
			System.out.println("Negative cases 4 : wrong version");
			System.out.println("											");

			Response res3 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/endpoint?serviceName=Dashboard-UI&version=999");

			System.out.println("											");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 404);
			System.out.println(res3.asString());

			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2002);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "End Point Not Found: seviceName=Dashboard-UI, version=999");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupLink()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("GET details of link");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=0.1&rel=sso.home");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("rel"), "sso.home");
			Assert.assertNotNull(res.jsonPath().get("href"));

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void lookupLink_invalidPara()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("Negative cases for lookup link");
			System.out.println("											");

			System.out.println("Negative cases 1 : no serviceName");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=&version=0.1&rel=sso.home");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "Link Not Found: seviceName=, version=0.1, rel=sso.home");

			System.out.println("											");

			System.out.println("Negative cases 2 : no version");
			System.out.println("											");

			Response res1 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=&rel=sso.home");

			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Link Not Found: seviceName=Dashboard-UI, version=, rel=sso.home");

			System.out.println("											");

			System.out.println("Negative cases 3 : no rel");
			System.out.println("											");

			Response res2 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=0.1&rel=");

			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Link Not Found: seviceName=Dashboard-UI, version=0.1, rel=");

			System.out.println("											");

			System.out.println("Negative cases 4 : wrong servicename");
			System.out.println("											");

			Response res3 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=abc&version=0.1&rel=sso.home");

			System.out.println("											");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 404);
			System.out.println(res3.asString());

			Assert.assertEquals(res3.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "Link Not Found: seviceName=abc, version=0.1, rel=sso.home");

			System.out.println("											");

			System.out.println("Negative cases 5 : wrong version");
			System.out.println("											");

			Response res4 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=999&rel=sso.home");

			System.out.println("											");
			System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 404);
			System.out.println(res4.asString());

			Assert.assertEquals(res4.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Link Not Found: seviceName=Dashboard-UI, version=999, rel=sso.home");

			System.out.println("											");

			System.out.println("Negative cases 6 : wrong rel");
			System.out.println("											");

			Response res5 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/link?serviceName=Dashboard-UI&version=0.1&rel=abc");

			System.out.println("											");
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 404);
			System.out.println(res5.asString());

			Assert.assertEquals(res5.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Link Not Found: seviceName=Dashboard-UI, version=0.1, rel=abc");

			System.out.println("											");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void lookupLinkWithRelPrefix()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("GET details for linkWithRelPrefix");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=&version=0.1&rel=quickLink");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("rel"), "quickLink/Dashboard Home");
			Assert.assertNotNull(res.jsonPath().get("href"));

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void lookupLinkWithRelPrefix_invalidPara()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("Negative cases for lookup linkWithRelPrefix");
			System.out.println("											");

			System.out.println("Negative cases 1 : no serviceName");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=&version=0.1&rel=quickLink");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			System.out.println(res.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "Link Not Found: seviceName=, version=0.1, rel=quickLink");

			System.out.println("											");

			System.out.println("Negative cases 2 : no version");
			System.out.println("											");

			Response res1 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=&rel=quickLink");

			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			System.out.println(res1.asString());

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Link Not Found: seviceName=Dashboard-UI, version=, rel=quickLink");

			System.out.println("											");

			System.out.println("Negative cases 3 : no rel");
			System.out.println("											");

			Response res2 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=0.1&rel=");

			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			System.out.println(res2.asString());

			Assert.assertEquals(res2.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Link Not Found: seviceName=Dashboard, version=0.1, rel=");

			System.out.println("											");

			System.out.println("Negative cases 4 : wrong serviceName");
			System.out.println("											");

			Response res3 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=abc&version=0.1&rel=quickLink");

			System.out.println("											");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 404);
			System.out.println(res3.asString());

			Assert.assertEquals(res.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res.jsonPath().get("errorMessage"), "Link Not Found: seviceName=abc, version=0.1, rel=quickLink");

			System.out.println("											");

			System.out.println("Negative cases 5 : wrong version");
			System.out.println("											");

			Response res4 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=999&rel=quickLink");

			System.out.println("											");
			System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 404);
			System.out.println(res4.asString());

			Assert.assertEquals(res4.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Link Not Found: seviceName=Dashboard-UI, version=999, rel=quickLink");

			System.out.println("											");

			System.out.println("Negative cases 6 : wrong rel");
			System.out.println("											");

			Response res5 = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("registry/lookup/linkWithRelPrefix?serviceName=Dashboard-UI&version=0.1&rel=abc");

			System.out.println("											");
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 404);
			System.out.println(res5.asString());

			Assert.assertEquals(res5.jsonPath().get("errorCode"), 2001);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Link Not Found: seviceName=Dashboard-UI, version=0.1, rel=abc");

			System.out.println("											");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}
}