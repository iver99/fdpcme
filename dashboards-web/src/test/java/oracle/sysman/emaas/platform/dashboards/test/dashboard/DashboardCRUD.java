package oracle.sysman.emaas.platform.dashboards.test.dashboard;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.test.common.CommonTest;

import org.codehaus.jettison.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

public class DashboardCRUD
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
	public void dashboard_create_emptyTilePara()
	{

	}

	@Test
	public void dashboard_create_simple()
	{

		/* create a dashboard with required filed
		 *
		 */
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString1 = "{ \"name\":\"Test_Dashboard\"}";
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString1).when()
					.post("/dashboards");
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
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards/" + dashboard_id);
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
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString2).when()
					.post("/dashboards");
			System.out.println(res2.asString());
			System.out.println("Status code is:  " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().getString("errorCode"), "10000");
			Assert.assertEquals(res2.jsonPath().getString("errorMessage"),
					"Can not create a new dashboard, dashboard with specified ID exists already");
			System.out.println("											");

			System.out.println("Verify that creating dashbaord with existed name won't be successful");
			String jsonString3 = "{ \"name\":\"Test_Dashboard\"}";
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString3).when()
					.post("/dashboards");
			System.out.println(res3.asString());
			System.out.println("Status code is:  " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().getString("errorCode"), "10001");
			Assert.assertEquals(res3.jsonPath().getString("errorMessage"), "Dashboard with the same name exists already");
			System.out.println("											");

			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + dashboard_id);
			System.out.println(res5.asString());
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 204);

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_create_withTile()
	{
		String dashboard_id = "";
		try {
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString1 = "{\"name\": \"test_dashboard_tile\",\"tiles\":[{\"title\":\"A sample tile for search\",\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\",\"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\", \"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEW_MODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\", \"PROVIDER_NAME\": \"X Analytics\", \"PROVIDER_VERSION\": \"0.1\",\"PROVIDER_ASSET_ROOT\": \"asset\"}]}";
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString1).when()
					.post("/dashboards");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 201);

			dashboard_id = res1.jsonPath().getString("id");
			//System.out.println("Verfify whether the timestamp of create&modify is same or not");
			//Assert.assertEquals(res1.jsonPath().get("createdOn"), res1.jsonPath().get("lastModifiedOn"));
			System.out.println("											");

			System.out.println("Get the details of the created dashboard...");
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards/" + dashboard_id);
			System.out.println(res2.asString());
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertEquals(res2.jsonPath().get("tiles.title[0]"), "A sample tile for search");

			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + dashboard_id);
			System.out.println(res5.asString());
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 204);

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {

		}

	}

	@Test
	public void dashboard_delete_invalidId()
	{
		try {
			System.out.println("");
			System.out.println("");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboard/99999999");
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
			System.out.println("");
			System.out.println("");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().when().delete("/dashboard/1");
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

	@Test
	public void dashboard_query_multi()
	{
		int totalResults = 0;
		int offset = 0;
		int count = 0;
		int limit = 0;
		try {
			//get all dashboard, defalut is to display 20
			System.out.println("List all dashboard...");
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().when().get("/dashboards/");
			Assert.assertTrue(res1.getStatusCode() == 200);
			List list_dashboards = res1.jsonPath().get("dashboards.name");
			totalResults = res1.jsonPath().getInt("totalResults");
			limit = res1.jsonPath().getInt("limit");
			count = res1.jsonPath().getInt("count");
			Assert.assertEquals(count, list_dashboards.size());

			String jsonString1 = "{ \"name\":\"Test_Dashboard_QueryAll\"}";
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString1).when()
					.post("/dashboards");
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 201);

			String dashboard_id = res2.jsonPath().getString("id");

			System.out.println("List the dashboards with ...");
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards?queryString=Test_Dashboard_QueryAll&offset=0&limit=5");
			Assert.assertTrue(res3.getStatusCode() == 200);
			list_dashboards = res3.jsonPath().get("dashboards.name");
			totalResults = res3.jsonPath().getInt("totalResults");
			limit = res3.jsonPath().getInt("limit");
			count = res3.jsonPath().getInt("count");
			Assert.assertEquals(count, list_dashboards.size());
			Assert.assertEquals(count, 1);
			Assert.assertEquals(totalResults, 1);
			Assert.assertEquals(limit, 5);
			Assert.assertEquals(res3.jsonPath().getInt("offset"), 0);

			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + dashboard_id);
			System.out.println(res5.asString());
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 204);

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_query_multi_invalid_para()
	{
		try {
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards?limit=0");
			Assert.assertTrue(res1.getStatusCode() == 400);
			Assert.assertEquals(res1.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res1.jsonPath().get("errorMessage"),
					"Invalid parameter 'limit', a number greater than 0 is expected");

			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards?limit=-1");
			Assert.assertTrue(res2.getStatusCode() == 400);
			Assert.assertEquals(res2.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res2.jsonPath().get("errorMessage"),
					"Invalid parameter 'limit', a number greater than 0 is expected");

			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.get("/dashboards?limit=-1");
			Assert.assertTrue(res3.getStatusCode() == 400);
			Assert.assertEquals(res3.jsonPath().get("errorCode"), 10000);
			Assert.assertEquals(res3.jsonPath().get("errorMessage"),
					"Invalid parameter 'offset', a number greater than 0 is expected");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_update()
	{
		try {
			//create a new dashboard
			System.out.println("------------------------------------------");
			System.out.println("POST method is in-progress to create a new dashboard");

			String jsonString = "{ \"name\":\"Test_Update_Dashboard\"}";
			Response res = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString).when()
					.post("/dashboards");
			System.out.println(res.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 201);

			String dashboard_id = res.jsonPath().getString("id");

			//update the simple dashboard
			System.out.println("											");
			System.out.println("Update the created dashboard...");

			String jsonString1 = "{\"name\": \"Test_Update_Dashboard_Edit\"}";
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString1).when()
					.put("/dashboards/" + dashboard_id);
			System.out.println(res1.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertEquals(res1.jsonPath().get("name"), "Test_Update_Dashboard_Edit");
			Assert.assertNotEquals(res1.jsonPath().get("createOn"), res1.jsonPath().get("lastModifiedOn"));

			//update the dashboard with tile
			System.out.println("											");
			System.out.println("Update the dashboard with tile...");
			String jsonString2 = "{\"name\":\"Test_Update_Dashboard_Edit_NewTile\",\"tiles\":[{\"title\":\"Another tile for search\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEW_MODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"0.1\",\"PROVIDER_ASSET_ROOT\": \"asset\"}]}";
			Response res2 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString2).when()
					.put("/dashboards/" + dashboard_id);
			System.out.println(res2.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			String tile_id = res2.jsonPath().get("tiles.tileId[0]").toString();
			Assert.assertEquals(res2.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertNotEquals(res2.jsonPath().get("createOn"), res2.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res2.jsonPath().get("tiles.title[0]"), "Another tile for search");

			//update the existed tile in dashboard
			System.out.println("											");
			System.out.println("Update the exsited tile in dashboard");
			String jsonString3 = "{\"name\":\"Test_Update_Dashboard_Edit_ExistTile\",\"tiles\":[{\"tileId\": "
					+ tile_id
					+ ",\"title\":\"Another tile for search_edit\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEW_MODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"0.1\",\"PROVIDER_ASSET_ROOT\": \"asset\",\"tileParameters\": [{\"name\": \"WIDGET_CREATED_BY\",\"systemParameter\":false, \"type\": \"STRING\",\"value\":\"SYSMAN\"}]}]}";
			Response res3 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString3).when()
					.put("/dashboards/" + dashboard_id);
			System.out.println(res3.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 200);
			Assert.assertEquals(res3.jsonPath().get("name"), "Test_Update_Dashboard_Edit_ExistTile");
			Assert.assertNotEquals(res3.jsonPath().get("createOn"), res3.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res3.jsonPath().get("tiles.title[0]"), "Another tile for search_edit");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].name[0]"), "WIDGET_CREATED_BY");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].type[0]"), "STRING");
			Assert.assertEquals(res3.jsonPath().get("tiles.tileParameters[0].systemParameter[0]"), false);

			//update the dashboard with new tile
			System.out.println("											");
			System.out.println("Add a new tile in dashboard");
			String jsonString4 = "{\"name\":\"Test_Update_Dashboard_Edit_NewTile\",\"tiles\":[{\"title\":\"A sample tile for search\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEW_MODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"0.1\",\"PROVIDER_ASSET_ROOT\": \"asset\",\"tileParameters\": [{\"name\": \"WIDGET_CREATED_BY\",\"systemParameter\":false, \"type\":\"STRING\",\"value\":\"SYSMAN\"}]}]}";
			Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString4).when()
					.put("/dashboards/" + dashboard_id);
			System.out.println(res4.asString());
			System.out.println("==PUT operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res4.getStatusCode());
			Assert.assertTrue(res4.getStatusCode() == 200);
			String tile_newid = res4.jsonPath().get("tiles.tileId[0]").toString();
			System.out.println(tile_newid);
			Assert.assertEquals(res4.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
			Assert.assertNotEquals(res4.jsonPath().get("createOn"), res4.jsonPath().get("lastModifiedOn"));
			Assert.assertEquals(res4.jsonPath().get("tiles.title[0]"), "A sample tile for search");
			Assert.assertNotEquals(tile_newid, tile_id);
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].name[0]"), "WIDGET_CREATED_BY");
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].type[0]"), "STRING");
			Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].systemParameter[0]"), false);

			//clean up the created dashboard
			System.out.println("											");
			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + dashboard_id);
			System.out.println(res5.asString());
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 204);

			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_update_invalidId()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("This test is to validate update a non-existed dashboard");
			String jsonString = "{ \"name\":\"Custom_Dashboard_Edit\" }";
			Response res = RestAssured.given().log().everything().body(jsonString).when().put("/dashboards/999999999");

			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			System.out.println(res.asString());
			Assert.assertEquals(res.jsonPath().getString("errorCode"), "20001");
			Assert.assertEquals(res.jsonPath().getString("errorMessage"), "Specified dashboard is not found");
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void dashboard_update_systemDashboard()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("This test is to validate update a system dashboard");
			String jsonString = "{ \"name\":\"Custom_Dashboard_Edit\" }";
			Response res = RestAssured.given().log().everything().body(jsonString).when().put("/dashboards/1");

			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);
			System.out.println(res.asString());
			Assert.assertEquals(res.jsonPath().getString("errorCode"), "20000");
			Assert.assertEquals(res.jsonPath().getString("errorMessage"), "Not allow to update system Dashboard");
			System.out.println("											");

			System.out.println("This test is to validate update a custom dashboard to the system dashboard");
			System.out.println("Create a new dashboard...");
			String jsonString1 = "{ \"name\":\"Test_Custom_Dashboard\"}";
			Response res1 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString1).when()
					.post("/dashboards");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			Assert.assertTrue(res1.getStatusCode() == 201);
			String id = res1.jsonPath().getString("id");
			System.out.println("Update the dashboard to system dashboard...");
			String jsonString2 = "{ \"name\":\"Custom_Dashboard_Edit\" }";
			Response res2 = RestAssured.given().log().everything().body(jsonString2).when().put("/dashboards/" + id);
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 404);

			Assert.assertEquals(res.jsonPath().getString("errorCode"), "20000");
			Assert.assertEquals(res.jsonPath().getString("errorMessage"), "Not support to update field \"systemDashboard\"");

			System.out.println("											");

			System.out.println("cleaning up the dashboard that is created above using DELETE method");
			Response res5 = RestAssured.given().contentType(ContentType.JSON).log().everything().when()
					.delete("/dashboards/" + id);
			System.out.println(res5.asString());
			System.out.println("Status code is: " + res5.getStatusCode());
			Assert.assertTrue(res5.getStatusCode() == 204);
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");

		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void ping_test() throws JSONException
	{
		String jsonString4 = "{\"name\":\"Test_Update_Dashboard_Edit_NewTile\",\"tiles\":[{\"title\":\"A sample tile for search\", \"isMaximized\": false,\"width\": 2,\"height\": 220,\"WIDGET_UNIQUE_ID\": \"1001\", \"WIDGET_NAME\": \"iFrame\",\"WIDGET_DESCRIPTION\": \"A widget which can show another page inside a iFrame\",\"WIDGET_GROUP_NAME\": \"Built-in Widgets\",\"WIDGET_ICON\": \"dependencies/built-in/iFrame/images/icon.png\",\"WIDGET_HISTOGRAM\": \"dependencies/built-in/iFrame/images/histogram.png\", \"WIDGET_OWNER\": \"SYSMAN\",\"WIDGET_CREATION_TIME\": \"2014-11-11T19:20:30.45Z\",\"WIDGET_SOURCE\": 0,\"WIDGET_KOC_NAME\": \"DF_V1_WIDGET_IFRAME\",\"WIDGET_VIEW_MODEL\": \"dependencies/built-in/iFrame/js/iFrameViewModel.js\",\"WIDGET_TEMPLATE\": \"dependencies/built-in/iFrame/iFrame.html\",\"PROVIDER_NAME\": \"X Analytics\",\"PROVIDER_VERSION\": \"0.1\",\"PROVIDER_ASSET_ROOT\": \"asset\",\"tileParameters\": [{\"name\": \"WIDGET_CREATED_BY\",\"systemParameter\":false, \"type\":\"STRING\",\"value\":\"SYSMAN\"}]}]}";
		Response res4 = RestAssured.given().contentType(ContentType.JSON).log().everything().body(jsonString4).when()
				.put("/dashboards/57");
		System.out.println(res4.asString());
		System.out.println("==PUT operation is done");
		System.out.println("											");
		System.out.println("Status code is: " + res4.getStatusCode());
		Assert.assertTrue(res4.getStatusCode() == 200);
		//Assert.assertEquals(res4.jsonPath().get("name"), "Test_Update_Dashboard_Edit_NewTile");
		//Assert.assertNotEquals(res4.jsonPath().get("createOn"), res4.jsonPath().get("lastModifiedOn"));
		Assert.assertEquals(res4.jsonPath().get("tiles.title[0]"), "A sample tile for search");
		Assert.assertNotEquals(res4.jsonPath().get("tiles.tileId[0]").toString(), "15");
		System.out.println("11111");
		Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].name[0]"), "WIDGET_CREATED_BY");
		Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].type[0]"), "STRING");
		Assert.assertEquals(res4.jsonPath().get("tiles.tileParameters[0].systemParameter[0]"), false);

	}
}