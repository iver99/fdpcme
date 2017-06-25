/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;

/**
 * @author shangwan
 *
 */
public class TestDashBoard_WidgetLink extends LoginAndLogout
{
	private String dbName_Test = "";
	private String dbName2_Test = "";
	private String dbName3_Test = "";
	private String dbDesc_Test = "";
	
	private final String widgetName = "Top Hosts by Log Entries";
	private final String OOBName = "Middleware Operations";
	
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}
	
	@BeforeClass
	public void createTestDashboard()
	{
		dbName_Test = "Test Widget Link-" + DashBoardUtils.generateTimeStamp();
		dbDesc_Test = "test widget link";
		
		dbName2_Test = "link dashboard - " + DashBoardUtils.generateTimeStamp();

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_Test, dbDesc_Test, true), "Create dashboard failed!");
		
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName),	"Widget '" + widgetName + "' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
		
		//back to home page and create another dashboard
		webd.getLogger().info("Back to home page and create another dashboard");
		BrandingBarUtil.visitDashboardHome(webd);
		
		DashboardHomeUtil.createDashboard(webd, dbName2_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName2_Test, dbDesc_Test, true), "Create dashboard failed!");	
		
		LoginAndLogout.logoutMethod();
	}
	
	@AfterClass
	public void RemoveDashboard()
	{
		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to remove test data");

		//delete dashboard
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Start to remove the test data...");
		DashBoardUtils.deleteDashboard(webd, dbName_Test);
		DashBoardUtils.deleteDashboard(webd, dbName2_Test);
		DashBoardUtils.deleteDashboard(webd, dbName3_Test);		

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}
	
	@Test
	public void testAddWidgetLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//add a link to the widget
		webd.getLogger().info("Add Widget Link");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, OOBName);
		
		//once EMCPDF-4281 fixed, save action should be removed
		//save the dashboard
		webd.getLogger().info("Save dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
		
		//click the widget link
		webd.getLogger().info("Click the link added to the widget");
		Assert.assertTrue(DashboardBuilderUtil.verifyLinkOnWidgetTitle(webd, widgetName, OOBName));		
	}
	
	@Test(dependsOnMethods = {"testAddWidgetLink"})
	public void testEditWidgetLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//edit the link in the widget
		webd.getLogger().info("Add Widget Link");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, dbName2_Test);
				
		//once EMCPDF-4281 fixed, save action should be removed
		//save the dashboard
		webd.getLogger().info("Save dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
				
		//click the widget link
		webd.getLogger().info("Click the link added to the widget");
		DashboardBuilderUtil.clickLinkOnWidgetTitle(webd, widgetName);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName2_Test, dbDesc_Test, true), "Not open the correct dashboard");
	}
	
	@Test(dependsOnMethods = {"testEditWidgetLink"})
	public void testRemoveWidgetLink()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//edit the link in the widget
		webd.getLogger().info("Add Widget Link");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, "");
				
		//once EMCPDF-4281 fixed, save action should be removed
		//save the dashboard
		webd.getLogger().info("Save dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
				
		//click the widget link
		webd.getLogger().info("Verify the link in the widget has been removed");
		Assert.assertFalse(DashboardBuilderUtil.hasWidgetLink(webd, widgetName));		
	}
	
	@Test(dependsOnMethods = {"testRemoveWidgetLink"})
	public void testWidgetTitleHide()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testAddWidgetLink");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);
		
		//open a dashboard
		webd.getLogger().info("Open a dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);
		
		//verify the widget
		webd.getLogger().info("Verify the widget");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName), "Not have widget '" + widgetName + "'");
		
		//hide the title
		webd.getLogger().info("Hide the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, false);
		
		//verify that Add link is disabled
		webd.getLogger().info("Verify that the link can't be added");
		DashBoardUtils.verifyAddLinkButton(webd, widgetName, 0);
		
		//show the title
		webd.getLogger().info("Show the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, true);
		
		//add the widget link
		webd.getLogger().info("Add the link to the widget");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, 0, dbName2_Test);
		
		//verify the link has been added
		webd.getLogger().info("Verify the link has been removed");
		Assert.assertTrue(DashboardBuilderUtil.hasWidgetLink(webd, widgetName, 0), "Widget has link");
		
		//hide the title
		webd.getLogger().info("Hide the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, false);
		
		//remove the link
		webd.getLogger().info("Remove the link");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, 0, "");
		
		//show the title
		webd.getLogger().info("Show the widget title");
		DashboardBuilderUtil.showWidgetTitle(webd, widgetName, true);
		
		//verify the link has been removed
		webd.getLogger().info("Verify the link has been removed");
		Assert.assertFalse(DashboardBuilderUtil.hasWidgetLink(webd, widgetName, 0), "Widget has link");		
	}
	
	@Test
	public void testMultipleWidget()
	{
		dbName3_Test = "test multiple widget - " + DashBoardUtils.generateTimeStamp();
		dbDesc_Test = "test widget link";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in createTestDashboard");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard in grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardHomeUtil.createDashboard(webd, dbName3_Test, dbDesc_Test, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName3_Test, dbDesc_Test, true), "Create dashboard failed!");
		
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");
		
		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
		
		//add link to the second widget
		webd.getLogger().info("Add link to the second widget");
		DashboardBuilderUtil.addLinkToWidgetTitle(webd, widgetName, 1, dbName2_Test);
		
		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
		
		//verify the widget
		webd.getLogger().info("Verify the widgets");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName));
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName, 1));
		
		//click the link in widget
		webd.getLogger().info("Click the widget link");
		DashboardBuilderUtil.clickLinkOnWidgetTitle(webd, widgetName, 0);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName2_Test, dbDesc_Test, true), "Not open the correct dashboard");
	}
}
