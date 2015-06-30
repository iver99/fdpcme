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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.LogConfig;

import oracle.sysman.emaas.platform.dashboards.core.util.SchemaUtil;
import oracle.sysman.qatool.uifwk.utils.Utils;
public class CommonTest
{

	private  String HOSTNAME;
	private  String portno;
	private  String serveruri;
	private  String authToken;
	private  String tenantid;
	private  String remoteuser;
	private  String tenantid_2;
	private static final String SERVICE_NAME = "Dashboard-API";
	private static final String DOMAIN = "www.";
	private static final String DSB_DEPLOY_URL = "/instances?servicename=Dashboard-API";

	/**
	 * Sets up RESTAssured defaults before executing test cases Enables logging Reading the inputs from the testenv.properties
	 * file
	 * @throws URISyntaxException 
	 */
	
	public static void main(String ar[]) throws Exception
	{
		/*String  name = "http://slc07hgf.us.oracle.com:7001/registry/servicemanager/registry/v1";
		name = name + DSB_DEPLOY_URL;
		String data = getData(name);
		SchemaUtil obj = new SchemaUtil();
		List<String>  url=   obj.getDeploymentUrl(data);
		System.out.println(url.get(0));
		
		System.out.println(getDomainName(url.get(0)));
		System.out.println(getPort(url.get(0)));*/
	}
	

	public static String getDomainName(String url) throws URISyntaxException
	{
		URI uri = new URI(url);
		String domain = uri.getHost();
		return domain.startsWith(DOMAIN) ? domain.substring(4) : domain;
	}

	public static int getPort(String url) throws URISyntaxException
	{
		URI uri = new URI(url);
		int port = uri.getPort();
		return port;
	}
	
	public CommonTest()
	{
		
		try
		{
		String  name = getServiceManagerUrl();
		name = name + DSB_DEPLOY_URL;
		String data = getData(name);
		SchemaUtil obj = new SchemaUtil();
		List<String>  url=   obj.getDeploymentUrl(data);
		           
		//HOSTNAME = prop.getProperty("hostname");
		HOSTNAME= getDomainName(url.get(0));
	//portno = prop.getProperty("port");
		portno = getPort(url.get(0))+"";
	
		//	authToken = prop.getProperty("authToken");
		authToken = Utils.getProperty("SAAS_AUTH_TOKEN");
		//tenantid = prop.getProperty("tenantid");
		tenantid = Utils.getProperty("TENANT_ID");
		//tenantid_2 = prop.getProperty("tenantid_2");
		//remoteuser = prop.getProperty("RemoteUser");
		remoteuser = Utils.getProperty("SSO_USERNAME");
		serveruri = "http://" + HOSTNAME + ":" + portno;
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.baseURI = serveruri;
		RestAssured.basePath = "/emcpdf/api/v1";
		RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().enablePrettyPrinting(false));
		}catch(Exception e){
			
		}

	}
	
	
	private static String getServiceManagerUrl()
	{
		return  Utils.getProperty("SERVICE_MANAGER_URL");
	}
	
	private static String getData(String url)
	{
		SchemaUtil obj = new SchemaUtil();
		String data =obj.get(url);
		return data;
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
