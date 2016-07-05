/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.testsdk;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/**
 * @author cawei
 */
public class DashboardOrderWriteCURD
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
	public void dashboard_create_emptyPara()
	{
		try {
			System.out.println("------------------------------------------");
			String jsonString1 = "{ \"name\":\"\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 400);

			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Invalid dashboard name. Name can not be empty and must be less than 64 characters");
			System.out.println("											");

			String jsonString2 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"\"}]}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().post("/dashboards");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"), "Title for tile is required");

			String jsonString3 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"\"}]}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().post("/dashboards");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"), "\"WIDGET_NAME\" is required for tile(s) of the dashboard");

			String jsonString4 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"\"}]}";
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString4).when().post("/dashboards");
			System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 400);
			Assert.assertEquals(res4.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res4.jsonPath().get("errorMessage"),
					"\"WIDGET_UNIQUE_ID\" is required for tile(s) of the dashboard");

			String jsonString5 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"\"}]}";
			Response res5 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString5).when().post("/dashboards");
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 400);
			Assert.assertEquals(res5.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res5.jsonPath().get("errorMessage"), "\"WIDGET_OWNER\" is required for tile(s) of the dashboard");

			String jsonString6 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"\"}]}";
			Response res6 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString6).when().post("/dashboards");
			System.out.println("Status code is: " + res6.getStatusCode());
			Assert.assertTrue(res6.getStatusCode() == 400);
			Assert.assertEquals(res6.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res6.jsonPath().get("errorMessage"),
					"\"WIDGET_CREATION_TIME\" is required for tile(s) of the dashboard");

			String jsonString7 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": \"\"}]}";
			Response res7 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString7).when().post("/dashboards");
			System.out.println("Status code is: " + res7.getStatusCode());
			Assert.assertTrue(res7.getStatusCode() == 400);
			Assert.assertEquals(res7.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res7.jsonPath().get("errorMessage"), "\"WIDGET_SOURCE\" is required for tile(s) of the dashboard");

			String jsonString8 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"\"}]}";
			Response res8 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString8).when().post("/dashboards");
			System.out.println("Status code is: " + res8.getStatusCode());
			Assert.assertTrue(res8.getStatusCode() == 400);
			Assert.assertEquals(res8.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res8.jsonPath().get("errorMessage"),
					"\"WIDGET_KOC_NAME\" is required for tile(s) of the dashboard");

			String jsonString9 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"\"}]}";
			Response res9 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString9).when().post("/dashboards");
			System.out.println("Status code is: " + res9.getStatusCode());
			Assert.assertTrue(res9.getStatusCode() == 400);
			Assert.assertEquals(res9.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res9.jsonPath().get("errorMessage"),
					"\"WIDGET_VIEWMODEL\" is required for tile(s) of the dashboard");

			String jsonString10 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"\"}]}";
			Response res10 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString10).when().post("/dashboards");
			System.out.println("Status code is: " + res10.getStatusCode());
			Assert.assertTrue(res10.getStatusCode() == 400);
			Assert.assertEquals(res10.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res10.jsonPath().get("errorMessage"),
					"\"WIDGET_TEMPLATE\" is required for tile(s) of the dashboard");

			String jsonString11 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"\"}]}";
			Response res11 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString11).when().post("/dashboards");
			System.out.println("Status code is: " + res11.getStatusCode());
			Assert.assertTrue(res11.getStatusCode() == 400);
			Assert.assertEquals(res11.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res11.jsonPath().get("errorMessage"),
					"\"PROVIDER_NAME\" is required for tile(s) of the dashboard");

			String jsonString12 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"\"}]}";
			Response res12 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString12).when().post("/dashboards");
			System.out.println("Status code is: " + res12.getStatusCode());
			Assert.assertTrue(res12.getStatusCode() == 400);
			Assert.assertEquals(res12.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res12.jsonPath().get("errorMessage"),
					"\"PROVIDER_VERSION\" is required for tile(s) of the dashboard");

			String jsonString13 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"\"}]}";
			Response res13 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString13).when().post("/dashboards");
			System.out.println("Status code is: " + res13.getStatusCode());
			Assert.assertTrue(res13.getStatusCode() == 400);
			Assert.assertEquals(res13.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res13.jsonPath().get("errorMessage"),
					"\"PROVIDER_ASSET_ROOT\" is required for tile(s) of the dashboard");

			String jsonString14 = "{ \"name\":\"test_emptyPara\", \"tiles\":[{\"title\":\"test1\", \"WIDGET_NAME\": \"test_widget\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\":\"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\":\"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\":\"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"assetRoot\",\"tileParameters\": [{\"name\": \"\"}]}]}";
			Response res14 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString14).when().post("/dashboards");
			System.out.println("Status code is: " + res14.getStatusCode());
			Assert.assertTrue(res14.getStatusCode() == 400);
			Assert.assertEquals(res14.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res14.jsonPath().get("errorMessage"), "\"Name\" is required for tile parameter(s) of the tile");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_create_longNameDesc()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("Create a dashboard which name length is more than 64 characters");
			String jsonString1 = "{ \"name\":\"long name long name long name long name long name long name long name longg\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 400);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Invalid dashboard name. Name can not be empty and must be less than 64 characters");
			System.out.println("											");

			System.out.println("Create a dashboard which description length is more than 256 characters");
			String jsonString2 = "{ \"name\":\"long description\", \"description\":\"long description long description long description long description long description long description long description long description long description long description long description long description long description long description long description lo\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().post("/dashboards");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Invalid dashboard description. Description must be less than 256 characters");

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_create_simple()
	{

		/* create a dashboard with required field
		 *
		 */
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString1 = "{ \"name\":\"Test_Dashboard\"}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 201);
			System.out.println("Verfify whether the timestamp of create&modify is same or not");
			//Assert.assertEquals(res1.jsonPath().get("createdOn"), res1.jsonPath().get("lastModifiedOn"));
			System.out.println("											");

			dashboard_id = res1.jsonPath().getString("id");

			System.out.println("Verify that the created dashboard will be queried...");
			Response res4 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboard_id);
			System.out.println(res4.asString());
			System.out.println("Status code is:  " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 200);
			Assert.assertEquals(res4.jsonPath().getString("name"), "Test_Dashboard");
			Assert.assertEquals(res4.jsonPath().getString("type"), "NORMAL");
			Assert.assertEquals(res4.jsonPath().getString("id"), dashboard_id);
			Assert.assertEquals(res4.jsonPath().getBoolean("systemDashboard"), false);
			Assert.assertEquals(res4.jsonPath().getString("tiles"), "[]");
			System.out.println("											");

			System.out.println("Verify that creating dashbaord with existed Id won't be successful");
			String jsonString2 = "{ \"id\":" + dashboard_id + ",\"name\": \"Dashboard_with_existed_ID\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString2).when().post("/dashboards");
			System.out.println(res2.asString());
			System.out.println("Status code is:  " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "10000");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"),
					"Failed to create a dashboard by specifying an ID exists already");
			System.out.println("											");

			System.out.println("Verify that creating dashbaord with existed name won't be successful");
			String jsonString3 = "{ \"name\":\"Test_Dashboard\"}";
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString3).when().post("/dashboards");
			System.out.println(res3.asString());
			System.out.println("Status code is:  " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().getString("errorCode"), "10001");
			Assert.assertEquals(res3.jsonPath().getString("errorMessage"), "Dashboard with the same name exists already");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				System.out.println(res5.asString());
				System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
	}

	@Test
	public void dashboard_create_withTile()
	{
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString1 = "{\"name\": \"test_dashboard_tile\",\"tiles\":[{\"title\":\"A sample tile for search\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\",\"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\", \"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEWMODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\", \"PROVIDER_NAME\": \"X Analytics\", \"PROVIDER_VERSION\": \"1.0\",\"PROVIDER_ASSET_ROOT\": \"asset\"}]}";
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().post("/dashboards");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 201);

			dashboard_id = res1.jsonPath().getString("id");
			System.out.println("											");

			System.out.println("Get the details of the created dashboard...");
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("/dashboards/" + dashboard_id);
			System.out.println(res2.asString());
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("tiles.title[0]"), "A sample tile for search");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
		finally {
			if (!dashboard_id.equals("")) {
				System.out.println("cleaning up the dashboard that is created above using DELETE method");
				Response res5 = RestAssured
						.given()
						.contentType(ContentType.JSON)
						.log()
						.everything()
						.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
								"Authorization", authToken).when().delete("/dashboards/" + dashboard_id);
				System.out.println(res5.asString());
				System.out.println("Status code is: " + res5.getStatusCode());
				Assert.assertTrue(res5.getStatusCode() == 204);
			}
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
	}

	@Test
	public void dashboard_delete_invalidId()
	{
		try {
			System.out.println("------------------------------------------");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/99999999");
			System.out.println("Status code:" + res1.getStatusCode());
			System.out.println(res1.asString());
			Assert.assertTrue(res1.getStatusCode() == 404);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void dashboard_delete_sytemDashboard()
	{
		try {
			System.out.println("------------------------------------------");
			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/dashboards/1");
			System.out.println("Status code:" + res1.getStatusCode());
			System.out.println(res1.asString());
			Assert.assertTrue(res1.getStatusCode() == 403);
			Assert.assertEquals(res1.jsonPath().getString("errorCode"), "30001");
			Assert.assertEquals(res1.jsonPath().getString("errorMessage"), "Not support to delete system dashboard");
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

}
