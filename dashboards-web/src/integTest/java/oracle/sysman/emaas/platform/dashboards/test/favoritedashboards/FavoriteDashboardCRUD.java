package oracle.sysman.emaas.platform.dashboards.test.favoritedashboards;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class FavoriteDashboardCRUD
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
		tenantid_2 = ct.getTenantid2();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void favoriteCreateInvalidId()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Verify if the invalid dashboard would be added as favorite dashboard...");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/0");
			//System.out.println("Stauts code is :" + res1.getStatusCode());
			//System.out.println("Output is :" + res1.asString());
			Assert.assertTrue(res1.getStatusCode() == 404);
			//Assert.assertEquals(res1.asString(), "Specified dashboard is not found");

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/9999999999");
			//System.out.println("Stauts code is :" + res2.getStatusCode());
			//System.out.println("Output is :" + res2.asString());
			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void favoriteCRUD()
	{
		String dashboard_id = "";
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			//System.out.println(res.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			//System.out.println("											");

			//System.out.println("Add the newly created dashboard as favorite dashborad...");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 204);
			//System.out.println("											");

			//System.out.println("Verify if the dashboard has been added as favorite dashborad...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().getBoolean("isFavorite"), true);
			//System.out.println("											");

			//System.out.println("Delete the favorite dashborad...");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res3.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 204);
			//System.out.println("											");

			//System.out.println("Verify if the favorite dashboard has been removed...");
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertEquals(res4.jsonPath().getBoolean("isFavorite"), false);
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				//System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				//System.out.println(res5.asString());
				//System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}

	}

	@Test
	public void favoriteDeleteInvalidId()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Verify if the invalid favorite dashboard would be removed...");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/0");
			//System.out.println("Stauts code is :" + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			//Assert.assertEquals(res1.asString(), "Specified dashboard is not found");

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/9999999999");
			//System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void favoriteQueryAll()
	{
		String dashboard_id = "";
		try {
			//System.out.println("------------------------------------------");

			//System.out.println("Check if there is favorite dashboard...");
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites");
			//System.out.println("Stauts code is :" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			List<Integer> origin_id = new ArrayList<Integer>();
			if (res.jsonPath().getString("id") != null && !"".equals(res.jsonPath().getString("id"))) {
				origin_id = res.jsonPath().get("id");
			}

			//System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			//System.out.println(res1.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 201);

			dashboard_id = res1.jsonPath().getString("id");
			//System.out.println("											");

			//System.out.println("Add the newly created dashboard as favorite dashborad...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 204);
			//System.out.println("											");

			//System.out.println("Verify if the dashboard has been added as favorite dashborad...");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites");
			//System.out.println("Stauts code is :" + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 200);
			List<String> name = new ArrayList<String>();
			name = res3.jsonPath().get("name");
			List<Integer> id = new ArrayList<Integer>();
			id = res3.jsonPath().get("id");

			if (id.size() - origin_id.size() == 1) {
				for (int i = 0; i < id.size(); i++) {
					if (name.get(i).equals("Test_Favorite_Dashboard") && id.get(i).toString().equals(dashboard_id)) {
						Assert.assertTrue(true);
					}
				}
			}
			else {
				Assert.fail("Get a list of all favorite dashboards failed");

			}

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				//System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				//System.out.println(res5.asString());
				//System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}

	}

	@Test
	public void favoriteQueryInvalidId()
	{
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("Verify if the invalid favorite dashboard would be queried...");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("/dashboards/favorites/0");
			//System.out.println("Stauts code is :" + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 404);
			//Assert.assertEquals(res1.asString(), "Specified dashboard is not found");

			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"X-REMOTE-USER", tenantid + "." + remoteuser, "Authorization", authToken).when()
					.get("/dashboards/favorites/9999999999");
			//System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 404);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"), "Specified dashboard is not found");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	//	@Test
	//	public void multiTenant_favorite_CRUD()
	//	{
	//		String dashboard_id = "";
	//		try {
	//			//System.out.println("------------------------------------------");
	//			//System.out.println("POST method is in-progress to create a new dashboard");
	//
	//			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard_multitenant\"}";
	//			Response res = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).body(jsonString).when().post("/dashboards");
	//			//System.out.println(res.asString());
	//			//System.out.println("==POST operation is done");
	//			//System.out.println("											");
	//			//System.out.println("Status code is: " + res.getStatusCode());
	//			Assert.assertTrue(res.getStatusCode() == 201);
	//
	//			dashboard_id = res.jsonPath().getString("id");
	//			//System.out.println("											");
	//
	//			//System.out.println("Verfy that the other tenant can't add the created dashboard as favorite dashboard...");
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid_2, "X-REMOTE-USER", tenantid_2 + "." + remoteuser,
	//							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res1.getStatusCode());
	//			Assert.assertTrue(res1.getStatusCode() == 404);
	//			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "20001");
	//			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
	//			//System.out.println("											");
	//
	//			//System.out.println("Add the newly created dashboard as favorite dashborad...");
	//			Response res1_1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res1_1.getStatusCode());
	//			Assert.assertTrue(res1_1.getStatusCode() == 204);
	//			//System.out.println("											");
	//
	//			//System.out.println("Verify if the dashboard has been added as favorite dashborad...");
	//			Response res2 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res2.getStatusCode());
	//			Assert.assertTrue(res2.getStatusCode() == 200);
	//			Assert.assertEquals(res2.jsonPath().getBoolean("isFavorite"), true);
	//			//System.out.println("											");
	//
	//			//System.out
	//					.println("Verify that the other tenant can't query if the dashboard has been added as favorite dashborad...");
	//			Response res3 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid_2, "X-REMOTE-USER", tenantid_2 + "." + remoteuser,
	//							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res3.getStatusCode());
	//			Assert.assertTrue(res3.getStatusCode() == 404);
	//			Assert.assertEquals(res3.jsonPath().getString("errorCode"), "20001");
	//			Assert.assertEquals(res3.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
	//			//System.out.println("											");
	//
	//			//System.out.println("Verify that the other tenant can't delete the favorite dashborad...");
	//			Response res4 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid_2, "X-REMOTE-USER", tenantid_2 + "." + remoteuser,
	//							"Authorization", authToken).when().delete("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res4.getStatusCode());
	//			Assert.assertTrue(res4.getStatusCode() == 404);
	//			Assert.assertEquals(res4.jsonPath().getString("errorCode"), "20001");
	//			Assert.assertEquals(res4.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
	//			//System.out.println("											");
	//		}
	//		catch (Exception e) {
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//		finally {
	//			if (!dashboard_id.equals("")) {
	//				//System.out.println("cleaning up the dashboard that is created above using DELETE method");
	//				Response res5 = RestAssured
	//						.given()
	//						.contentType(ContentType.JSON)
	//						.log()
	//						.everything()
	//						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
	//				//System.out.println(res5.asString());
	//				//System.out.println("Status code is: " + res5.getStatusCode());
	//				Assert.assertTrue(res5.getStatusCode() == 204);
	//			}
	//			//System.out.println("											");
	//			//System.out.println("------------------------------------------");
	//			//System.out.println("											");
	//		}
	//
	//	}

	@Test
	public void multiTenantFavoriteCRUDInvalidTenant()
	{
		String dashboard_id = "";
		try {
			//System.out.println("------------------------------------------");
			//System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard_multitenant\"}";
			Response res = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString).when().post("/dashboards");
			//System.out.println(res.asString());
			//System.out.println("==POST operation is done");
			//System.out.println("											");
			//System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			dashboard_id = res.jsonPath().getString("id");
			//System.out.println("											");

			//System.out.println("Verfy that the other tenant can't add the created dashboard as favorite dashboard...");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", "errortenant." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "30000");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Tenant Name is not recognized: errortenant");
			//System.out.println("											");

			//System.out.println("Add the newly created dashboard as favorite dashborad...");
			Response res1_1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res1_1.getStatusCode());
			Assert.assertTrue(res1_1.getStatusCode() == 204);
			//System.out.println("											");

			//System.out.println("Verify if the dashboard has been added as favorite dashborad...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().getBoolean("isFavorite"), true);
			//System.out.println("											");

			//System.out.println("Verify that the other tenant can't delete the favorite dashborad...");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", "errortenant", "X-REMOTE-USER", "errortenant." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/favorites/" + dashboard_id);
			//System.out.println("Stauts code is :" + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().getString("errorCode"), "30000");
			Assert.assertEquals(res3.jsonPath().getString("errorMessage"), "Tenant Name is not recognized: errortenant");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				//System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				//System.out.println(res5.asString());
				//System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}

	}

	@Test
	public void multiTenantHeaderCheck()
	{
		try {
			//System.out.println("------------------------------------------");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().post("/dashboards/favorites/1");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");
			//System.out.println("											");

			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().delete("/dashboards/favorites/1");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");
			//System.out.println("											");

			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get("/dashboards/favorites/1");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");
			//System.out.println("											");

			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.header("Authorization", authToken).when().get("/dashboards/favorites");
			//System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 403);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"\"X-USER-IDENTITY-DOMAIN-NAME\" is missing in request header");
			//System.out.println("											");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	//	@Test
	//	public void remoteuser_favorite_CRUD()
	//	{
	//		String dashboard_id = "";
	//		try {
	//			//System.out.println("------------------------------------------");
	//			//System.out.println("POST method is in-progress to create a new dashboard");
	//
	//			String jsonString = "{ \"name\":\"Test_Favorite_Dashboard_favorite\"}";
	//			Response res = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).body(jsonString).when().post("/dashboards");
	//			//System.out.println(res.asString());
	//			//System.out.println("==POST operation is done");
	//			//System.out.println("											");
	//			//System.out.println("Status code is: " + res.getStatusCode());
	//			Assert.assertTrue(res.getStatusCode() == 201);
	//
	//			dashboard_id = res.jsonPath().getString("id");
	//			//System.out.println("											");
	//
	//			//System.out.println("Verfy that the other user can't add the created dashboard as favorite dashboard...");
	//			Response res1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + ".userA", "Authorization",
	//							authToken).when().post("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res1.getStatusCode());
	//			Assert.assertTrue(res1.getStatusCode() == 404);
	//			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "20001");
	//			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
	//			//System.out.println("											");
	//
	//			//System.out.println("Add the newly created dashboard as favorite dashborad...");
	//			Response res1_1 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().post("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res1_1.getStatusCode());
	//			Assert.assertTrue(res1_1.getStatusCode() == 204);
	//			//System.out.println("											");
	//
	//			//System.out.println("Verify if the dashboard has been added as favorite dashborad...");
	//			Response res2 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//							"Authorization", authToken).when().get("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res2.getStatusCode());
	//			Assert.assertTrue(res2.getStatusCode() == 200);
	//			Assert.assertEquals(res2.jsonPath().getBoolean("isFavorite"), true);
	//			//System.out.println("											");
	//
	//			//System.out.println("Verify that the other user can't delete the favorite dashborad...");
	//			Response res3 = RestAssured
	//					.given()
	//					.contentType(ContentType.JSON)
	//					.log()
	//					.everything()
	//					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + ".userA", "Authorization",
	//							authToken).when().delete("/dashboards/favorites/" + dashboard_id);
	//			//System.out.println("Stauts code is :" + res3.getStatusCode());
	//			Assert.assertTrue(res3.getStatusCode() == 404);
	//			Assert.assertEquals(res3.jsonPath().getString("errorCode"), "20001");
	//			Assert.assertEquals(res3.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
	//			//System.out.println("											");
	//		}
	//		catch (Exception e) {
	//			Assert.fail(e.getLocalizedMessage());
	//		}
	//		finally {
	//			if (!dashboard_id.equals("")) {
	//				//System.out.println("cleaning up the dashboard that is created above using DELETE method");
	//				Response res5 = RestAssured
	//						.given()
	//						.contentType(ContentType.JSON)
	//						.log()
	//						.everything()
	//						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
	//								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
	//				//System.out.println(res5.asString());
	//				//System.out.println("Status code is: " + res5.getStatusCode());
	//				Assert.assertTrue(res5.getStatusCode() == 204);
	//			}
	//			//System.out.println("											");
	//			//System.out.println("------------------------------------------");
	//			//System.out.println("											");
	//		}
	//
	//	}

	@Test
	public void remoteUserHeaderCheck()
	{
		try {
			//System.out.println("------------------------------------------");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.post("/dashboards/favorites/1");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			//System.out.println("											");

			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.delete("/dashboards/favorites/1");
			//System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 403);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			//System.out.println("											");

			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("/dashboards/favorites/1");
			//System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 403);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			//System.out.println("											");

			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "Authorization", authToken).when()
					.get("/dashboards/favorites");
			//System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 403);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 30000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"Valid header \"X-REMOTE-USER\" in format of <tenant_name>.<user_name> is required");
			//System.out.println("											");

			//System.out.println("											");
			//System.out.println("------------------------------------------");
			//System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}
}
