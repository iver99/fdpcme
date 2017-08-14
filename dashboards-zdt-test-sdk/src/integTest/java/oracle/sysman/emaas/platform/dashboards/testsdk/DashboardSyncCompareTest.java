/*
 * Copyright (C) 2017 Oracle
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

import java.sql.SQLException;

/**
 * @author cawei
 */
public class DashboardSyncCompareTest {
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
	public static void setUp() {
		CommonTest ct = new CommonTest();
		HOSTNAME = ct.getHOSTNAME();
		portno = ct.getPortno();
		serveruri = ct.getServeruri();
		authToken = ct.getAuthToken();
		tenantid = ct.getTenantid();
		remoteuser = ct.getRemoteUser();
	}

	@Test
	public void preferenceCRUD() {
		try {
			System.out.println("full compare");

			Response res1 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body("").when().put("/preferences/TestPreference");
			System.out.println(res1.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res1.getStatusCode());
			Assert.assertTrue(res1.getStatusCode() == 200);
			Assert.assertNotNull(res1.jsonPath().get("href"));
			Assert.assertEquals(res1.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res1.jsonPath().get("value"), "test preference");

			System.out.println("update the preference");
			String jsonString1 = "{ \"value\":\"test preference update\"}";
			Response res2 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).body(jsonString1).when().put("/preferences/TestPreference");
			System.out.println(res2.asString());
			System.out.println("==POST operation is done");
			System.out.println("											");
			System.out.println("Status code is: " + res2.getStatusCode());
			Assert.assertTrue(res2.getStatusCode() == 200);
			Assert.assertNotNull(res2.jsonPath().get("href"));
			Assert.assertEquals(res2.jsonPath().get("key"), "TestPreference");
			Assert.assertEquals(res2.jsonPath().get("value"), "test preference update");

			System.out.println("Delete the preference which created");
			Response res3 = RestAssured
					.given()
					.contentType(ContentType.JSON)
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().delete("/preferences/TestPreference");
			System.out.println("Status code is: " + res3.getStatusCode());
			Assert.assertTrue(res3.getStatusCode() == 204);
		} catch (Exception ex) {
			Assert.fail(ex.getLocalizedMessage());
		}

	}

	@Test
	public void testFullCompare() throws SQLException {
		DatabaseUtil.Cloud1ExecuteSQL(null);
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/compare?";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("?type=full");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testFullCompareStatus() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/compare/status");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIncrementalCompare() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/compare";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/compare/status");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIncrementalCompare1() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/sync";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIncrementalCompareAfterSync() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/sync";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIncrementalCompareStatus() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/compare/status");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIncrementalCompareStatus1() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/sync";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIncrementalCompareStatusAfterSync() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/sync";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testIncrementalSync() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/sync";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testSync() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testSyncStatus() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/sync";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void testSyncStatus1() {
		try {
			RestAssured.baseURI = "http://" + HOSTNAME + ":" + "8028";
			RestAssured.basePath = "/emcpdfcomparator/api/v1/comparator/sync";
			Response res = RestAssured
					.given()
					.log()
					.everything()
					.headers("X-USER-IDENTITY-DOMAIN-NAME", tenantid, "X-REMOTE-USER", tenantid + "." + remoteuser,
							"Authorization", authToken).when().get("comparator/sync");
			System.out.println("The response is" + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			//			System.out.println("The text"+res.jsonPath().get("items.serviceName"));
			//			System.out.println("The text"+res.jsonPath().get("items[0].links[0].rel"));
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[4].rel"), "serviceapi/dashboards.service");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[9].rel"), "serviceapi/dashboards.subscribedapps");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[12].rel"), "serviceapi/dashboards.subscribedapps2");
			//			Assert.assertEquals(res.jsonPath().get("items[0].links[15].rel"), "serviceapi/dashboards.logging");
		} catch (Exception e) {

			Assert.fail(e.getLocalizedMessage());
		}
	}

}
