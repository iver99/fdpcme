package oracle.sysman.emaas.platform.dashboards.test.preference;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class PreferenceCRUD
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
		tenantid_2 = ct.getTenantid_2();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void multiTenant_headerCheck()
	{
		try {
			//System.out.println("------------------------------------------");
			//List All
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when().get("/preferences");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 500);
			//			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			//list with key
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("/preferences/Test");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 500);
			//			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			//delete all
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.delete("/preferences");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 500);
			//			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			//delete with key
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.delete("/preferences/Test");
			//System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 500);
			//			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

			//put
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).body(jsonString).when()
					.put("/preferences/TestPreference");
			//System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 500);
			//			Assert.assertEquals(res5.jsonPath().get("errorCode"), 30000);
			//			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
			//					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void preference_create()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Verfy the preference which want to create is not exsited");

			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 404) {
				Assert.assertEquals(res.jsonPath().get("errorCode"), 20001);
				Assert.assertEquals(res.jsonPath().get("errorMessage"), "Specified preference key is not found");
			}
			else {
				Assert.fail("The preference you want to create exists!");
			}

			//System.out.println("Create a preference");
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference");
			//System.out.println(res1.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			//System.out.println("Status code is: " + res.getStatusCode());
			if (res.getStatusCode() == 200) {
				//System.out.println("Delete the preference which created");
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/TestPreference");
				//System.out.println("Status code is: " + res1.getStatusCode());
				Assert.assertTrue(res1.getStatusCode() == 204);
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
	}

	@Test
	public void preference_create_longKey()
	{
		String str_prefname = "defghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij";
		String str_prefname_new = "efghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghijabcdefghij";
		try {

			//System.out.println("Create a preference which the key has 257 characters");
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/" + str_prefname);
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 400);
			Assert.assertEquals(res.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(
					res.jsonPath().get("errorMessage"),
					"\"Key\" is required, which should only contain characters 'a' to 'Z', '_', '.', and '-'. The length of key should be less than 256 characters.");

			//System.out.println("Create a prefernce which the key has 256 characters");

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/" + str_prefname_new);
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), str_prefname_new);
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			//System.out.println("Status code is: " + res.getStatusCode());
			if (res.getStatusCode() == 200) {
				//System.out.println("Delete the preference which created");
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/" + str_prefname_new);
				//System.out.println("Status code is: " + res1.getStatusCode());
				Assert.assertTrue(res1.getStatusCode() == 204);
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");

		}

	}

	@Test
	public void preference_delete_invalidKey()
	{
		try {
			//System.out.println("------------------------------------------");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/preferences/abc#");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 20001);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"), "Specified preference key is not found");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void preference_deleteAll()
	{
		try {
			//System.out.println("------------------------------------------");

			//System.out.println("Create preferences");
			String jsonString = "{ \"value\":\"test preference1\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference1");
			//System.out.println(res1.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference1");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference1");

			//System.out.println("Create a preference");
			String jsonString1 = "{ \"value\":\"test preference2\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference2");
			//System.out.println(res2.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference2");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference2");

			//System.out.println("Delete All Preferences");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/preferences");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 204);

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			for (int i = 1; i < 3; i++) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().get("/preferences/TestPreference" + i);
				//System.out.println("Status code is: " + res.getStatusCode());
				if (res.getStatusCode() == 200) {
					//System.out.println("Delete the preference which created");
					Response res1 = RestAssured
							.given()
							.contentType(ContentType.JSON)
							.log()
							.everything()
							.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
									"Authorization", authToken).when().delete("/preferences/TestPreference" + i);
					//System.out.println("Status code is: " + res1.getStatusCode());
					Assert.assertTrue(res1.getStatusCode() == 204);
				}
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");

		}

	}

	@Test
	public void preference_listAll()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Verfy the preference which want to create is not exsited");

			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 404) {
				Assert.assertEquals(res.jsonPath().get("errorCode"), 20001);
				Assert.assertEquals(res.jsonPath().get("errorMessage"), "Specified preference key is not found");
			}
			else {
				Assert.fail("The preference you want to create exists!");
			}

			//System.out.println("Create preferences");
			String jsonString = "{ \"value\":\"test preference1\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference1");
			//System.out.println(res1.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference1");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference1");

			//System.out.println("Create a preference");
			String jsonString1 = "{ \"value\":\"test preference2\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference2");
			//System.out.println(res2.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference2");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference2");

			//System.out.println("List All Preferences");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences");
			//System.out.println("Status code is: " + res3.getStatusCode());
			List<String> lst_preferences = res3.jsonPath().get("key");
			//System.out.println(lst_preferences.size());
			Assert.assertNotNull(res3.jsonPath().getBoolean("href[0]"));
			Assert.assertEquals(res3.jsonPath().get("key[0]"), "TestPreference1");
			Assert.assertEquals(res3.jsonPath().get("value[0]"), "test preference1");
			Assert.assertNotNull(res3.jsonPath().getBoolean("href[1]"));
			Assert.assertEquals(res3.jsonPath().get("key[1]"), "TestPreference2");
			Assert.assertEquals(res3.jsonPath().get("value[1]"), "test preference2");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			for (int i = 1; i < 3; i++) {
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().get("/preferences/TestPreference" + i);
				//System.out.println("Status code is: " + res.getStatusCode());
				if (res.getStatusCode() == 200) {
					//System.out.println("Delete the preference which created");
					Response res1 = RestAssured
							.given()
							.contentType(ContentType.JSON)
							.log()
							.everything()
							.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
									"Authorization", authToken).when().delete("/preferences/TestPreference" + i);
					//System.out.println("Status code is: " + res1.getStatusCode());
					Assert.assertTrue(res1.getStatusCode() == 204);
				}
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");

		}
	}

	@Test
	public void preference_multiTenant_CRUD()
	{
		try {
			//System.out.println("------------------------------------------");
			//create a preference
			//System.out.println("Create a preferences");
			String jsonString = "{ \"value\":\"test preference multi tenant\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference_multiTenant");
			//System.out.println(res1.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference_multiTenant");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference multi tenant");

			//System.out.println("Get the prefenerce with other tenant");
			Response res2_1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			//System.out.println(res2_1.asString());
			//System.out.println("Status code is:  " + res2_1.getStatusCode());
			Assert.assertTrue(res2_1.getStatusCode() == 500);
			//			Assert.assertEquals(res2_1.jsonPath().getString("errorCode"), "30000");
			//			Assert.assertEquals(res2_1.jsonPath().getString("errorMessage"), "Tenant Name is not recognized: errortenant");

			//			Response res2_2 = RestAssured
			//					.given()
			//					.contentType(ContentType.JSON)
			//					.log()
			//					.everything()
			//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid_2, "X-REMOTE-USER", tenantid_2 + "." + remoteuser,
			//							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			//			//System.out.println(res2_2.asString());
			//			//System.out.println("Status code is:  " + res2_2.getStatusCode());
			//			Assert.assertTrue(res2_2.getStatusCode() == 404);
			//			Assert.assertEquals(res2_2.jsonPath().getString("errorCode"), "20001");
			//			Assert.assertEquals(res2_2.jsonPath().getString("errorMessage"), "Specified preference key is not found");

			//System.out.println("Delete the prefenerce with other tenant");
			Response res3_1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			//System.out.println(res3_1.asString());
			//System.out.println("Status code is:  " + res3_1.getStatusCode());
			Assert.assertTrue(res3_1.getStatusCode() == 500);
			//			Assert.assertEquals(res3_1.jsonPath().getString("errorCode"), "30000");
			//			Assert.assertEquals(res3_1.jsonPath().getString("errorMessage"), "Tenant Name is not recognized: errortenant");

			//			Response res3_2 = RestAssured
			//					.given()
			//					.contentType(ContentType.JSON)
			//					.log()
			//					.everything()
			//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid_2, "X-REMOTE-USER", tenantid_2 + "." + remoteuser,
			//							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			//			//System.out.println(res3_2.asString());
			//			//System.out.println("Status code is:  " + res3_2.getStatusCode());
			//			Assert.assertTrue(res3_2.getStatusCode() == 404);
			//			Assert.assertEquals(res3_2.jsonPath().getString("errorCode"), "20001");
			//			Assert.assertEquals(res3_2.jsonPath().getString("errorMessage"), "Specified preference key is not found");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference_multiTenant");
			//System.out.println("Status code is: " + res.getStatusCode());
			if (res.getStatusCode() == 200) {
				//System.out.println("Delete the preference which created");
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/TestPreference_multiTenant");
				//System.out.println("Status code is: " + res1.getStatusCode());
				Assert.assertTrue(res1.getStatusCode() == 204);
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
	}

	@Test
	public void preference_update()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Verfy the preference which want to create is not exsited");

			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			if (res.getStatusCode() == 404) {
				Assert.assertEquals(res.jsonPath().get("errorCode"), 20001);
				Assert.assertEquals(res.jsonPath().get("errorMessage"), "Specified preference key is not found");
			}
			else {
				Assert.fail("The preference you want to create exists!");
			}

			//System.out.println("Create preferences");
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().put("/preferences/TestPreference");
			//System.out.println(res1.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

			//System.out.println("update the preference");
			String jsonString1 = "{ \"value\":\"test preference update\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference");
			//System.out.println(res2.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference update");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/preferences/TestPreference");
			//System.out.println("Status code is: " + res.getStatusCode());
			if (res.getStatusCode() == 200) {
				//System.out.println("Delete the preference which created");
				Response res1 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/preferences/TestPreference");
				//System.out.println("Status code is: " + res1.getStatusCode());
				Assert.assertTrue(res1.getStatusCode() == 204);
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
	}

	@Test
	public void prefernece_create_invalidKey()
	{
		String[] str_prefname = new String[] { "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "+", "=", "~", "`", "{", "}",
				"[", "]", "|", "\\", ";", ":", "\"", "'", ",", "<", ">", "test name" };
		try {

			for (String element : str_prefname) {
				String jsonString = "{ \"value\":\"test preference\"}";
				Response res = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).body(jsonString).when().put("/preferences/" + element);
				//System.out.println("Status code is: " + res.getStatusCode());
				Assert.assertTrue(res.getStatusCode() == 400);
				Assert.assertEquals(res.jsonPath().get("errorCode"), 10000);
				Assert.assertEquals(
						res.jsonPath().get("errorMessage"),
						"\"Key\" is required, which should only contain characters 'a' to 'Z', '_', '.', and '-'. The length of key should be less than 256 characters.");

			}
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void remoteUser_headerCheck()
	{
		try {
			//System.out.println("------------------------------------------");
			//List All
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get("/preferences");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//list with key
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().get("/preferences/Test");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//delete all
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when().delete("/preferences");
			//System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//delete with key
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.delete("/preferences/Test");
			//System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 403);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

			//put
			String jsonString = "{ \"value\":\"test preference\"}";
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).body(jsonString).when()
					.put("/preferences/TestPreference");
			//System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 403);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

}
