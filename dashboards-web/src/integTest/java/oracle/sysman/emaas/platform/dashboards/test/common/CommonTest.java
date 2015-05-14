/*
 * Copyright (C) 2015 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.LogConfig;
import oracle.sysman.qatool.uifwk.utils.Utils;
public class CommonTest
{

	private final String HOSTNAME;
	private final String portno;
	private final String serveruri;
	private final String authToken;
	private final String tenantid;
	private final String remoteuser;
	private final String tenantid_2;

	/**
	 * Sets up RESTAssured defaults before executing test cases Enables logging Reading the inputs from the testenv.properties
	 * file
	 */

	public CommonTest()
	{
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("testenv.properties");
			prop.load(input);
			System.out.println("---------------------------------------------------------------------");
			System.out.println("The property values - Hostname: " + prop.getProperty("hostname") + " and Port: "
					+ prop.getProperty("port"));
			System.out.println("---------------------------------------------------------------------");
			System.out.println("											");
		}
		catch (IOException e) {

			e.printStackTrace();
		}
           
		//HOSTNAME = prop.getProperty("hostname");
		HOSTNAME=Utils.getProperty("EMCS_NODE1_HOSTNAME");
	//portno = prop.getProperty("port");
	portno = "7001";
	
	//	authToken = prop.getProperty("authToken");
	authToken = Utils.getProperty("SAAS_AUTH_TOKEN");
	//tenantid = prop.getProperty("tenantid");
	tenantid = Utils.getProperty("TENANT_ID");
	tenantid_2 = prop.getProperty("tenantid_2");
	//remoteuser = prop.getProperty("RemoteUser");
	remoteuser = Utils.getProperty("SSO_USERNAME");
	serveruri = "http://" + HOSTNAME + ":" + portno;
	RestAssured.useRelaxedHTTPSValidation();
	RestAssured.baseURI = serveruri;
	RestAssured.basePath = "/emcpdf/api/v1";
	RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().enablePrettyPrinting(false));

	}

	public String getAuthToken()
	{
		return authToken;
	}

	public String getHOSTNAME()
	{
		return HOSTNAME;
	}

	public String getPortno()
	{
		return portno;
	}

	public String getRemoteUser()
	{
		return remoteuser;
	}

	public String getServeruri()
	{
		return serveruri;
	}

	public String getTenantid()
	{
		return tenantid;
	}

	public String getTenantid_2()
	{
		return tenantid_2;
	}
}
