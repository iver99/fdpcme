/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.test.Configuration;

import oracle.sysman.emaas.platform.dashboards.ui.test.common.CommonTest;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

public class GetConfiguration
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
	public void getServiceManagerProp()
	{
		try {
			System.out.println("------------------------------------------");
			System.out.println("GET details of service manager properties");
			System.out.println("											");

			Response res = RestAssured.given().log().everything().header("Authorization", authToken).when()
					.get("/configurations/registration");

			System.out.println("											");
			System.out.println("Status code is: " + res.getStatusCode());
			Assert.assertTrue(res.getStatusCode() == 200);
			System.out.println(res.asString());
			Assert.assertNotNull(res.jsonPath().get("registryUrls"));
			//Assert.assertNotNull(res.jsonPath().get("ssfServiceName"));
			//Assert.assertNotNull(res.jsonPath().get("ssfVersion"));
			System.out.println("											");
			System.out.println("------------------------------------------");
			System.out.println("											");
		}
		catch (Exception e) {
			Assert.fail(e.getLocalizedMessage());
		}
	}

}
