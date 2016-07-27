/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.WelcomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet extends LoginAndLogout
{
	private String dbsetName = "";
	private String dbsetName_Test = "";
	private String dbsetName_setHome = "";
	private String dbsetName_Favorite = "";
	private String dbName = "";
	private String dbsetName_Test1 = "";
	private String dbName_InSet = "";
	private String dbName_OutSet = "";

	@BeforeClass
	public void createTestData() throws Exception
	{
		dbsetName = "DashboardSet_Test-" + generateTimeStamp();
		dbName = "Dashboard_Test-" + generateTimeStamp();
		String dbsetDesc = "Test the dashboard set";

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: createTestData");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName), "Dashboard set NOT found!");

		//back to the home page, continue to create dashboard
		webd.getLogger().info("Go back to the home page, continue to create dashboard fo testing");
		BrandingBarUtil.visitDashboardHome(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Create a new dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName, "", DashboardHomeUtil.DASHBOARD);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, "", true), "Dashboard NOT found!");

	}

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@AfterClass
	public void removeTestData() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: removeTestData");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to the grid view
		webd.getLogger().info("Swtich to the grid view");
		DashboardHomeUtil.gridView(webd);

		//remove the test data
		webd.getLogger().info("Start to delete the dashboard set: " + dbName);
		DashboardHomeUtil.deleteDashboard(webd, dbsetName_Favorite, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("Verify the dashboard set: " + dbName + " has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName), "Delete dashboard " + dbName + " failed!");
		webd.getLogger().info("Delete the dashboard set: " + dbName + " finished");

		webd.getLogger().info("Start to delete the dashboard set: " + dbsetName_setHome);
		DashboardHomeUtil.deleteDashboard(webd, dbsetName_setHome, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("Verify the dashboard set: " + dbsetName_setHome + " has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_setHome), "Delete dashboard set "
				+ dbsetName_setHome + " failed!");
		webd.getLogger().info("Delete the dashboard set: " + dbsetName_setHome + " finished");

		webd.getLogger().info("Start to delete the dashboard set: " + dbsetName_Favorite);
		DashboardHomeUtil.deleteDashboard(webd, dbsetName_Favorite, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("Verify the dashboard set: " + dbsetName_Favorite + " has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Favorite), "Delete dashboard set "
				+ dbsetName_Favorite + " failed!");
		webd.getLogger().info("Delete the dashboard set: " + dbsetName_Favorite + " finished");

		webd.getLogger().info("All test data have been removed");

	}

	@Test(groups = "third run", dependsOnGroups = { "second run" })
	public void testAddDashboardInGridView() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testAddDashboardInGridView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//add a dashboard to the dashboard set
		webd.getLogger().info("Add a dashboard into the dashborad set");
		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName);

		thinkTime(8000L);

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is NOT in the dashboard set");

	}

	@Test(groups = "third run", dependsOnMethods = { "testShareWithoutDashboardInSet" })
	public void testAddDashboardInListView() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testAddDashboardInListView");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//add a dashboard to the dashboard set
		webd.getLogger().info("Add a dashboard into the dashborad set");

		//switch to list view
		webd.getLogger().info("Switch to the list view");
		DashboardHomeUtil.listView(webd);
		DashboardBuilderUtil.addNewDashboardToSet(webd, dbName);

		thinkTime(8000L);

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is NOT in the dashboard set");

	}

	@Test(groups = "forth run", dependsOnGroups = { "third run" })
	public void testCreateDashboardInSet() throws Exception
	{
		//create dashboard set
		dbsetName_Test1 = "DashboardSet_For_Test-" + generateTimeStamp();
		String dbsetDesc = "create the dashboard set to test create dashboard in set";
		dbName_InSet = "DashboardInSet-" + generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Test1, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Test1), "Dashboard set NOT found!");

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//create a dashboard in dashboard set
		webd.getLogger().info("Create a dashboard inside dashboard set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_InSet, null);

		thinkTime(8000L);

		//verify the dashboard is in the dashboard set
		webd.getLogger().info("Verify the created dashboard is in the dashboard set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet), "Dashboard NOT found in the set");

	}

	@Test(groups = "first run")
	public void testCreateDashboardSet() throws Exception
	{
		dbsetName_Test = "DashboardSet-" + generateTimeStamp();
		String dbsetDesc = "Test the dashboard set";

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testCreateDashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Test, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Test), "Dashboard set NOT found!");

	}

	@Test(groups = "forth run", dependsOnMethods = { "testSearchDashboardInSet" })
	public void testDeleteDashboardInSet() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDeleteDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//remove the dashboard from dashboard set
		webd.getLogger().info("Remove the dashboard from the dashboard set");
		DashboardBuilderUtil.removeDashboardFromSet(webd, dbName_InSet);

		thinkTime(8000L);

		//click Add dashboard icon
		//webd.getLogger().info("Click Add Dashboard Icon");
		//webd.click("css=" + PageId.DashboardSetAddDashboardIcon_Css);

		//search the dashboard created in set
		DashboardHomeUtil.search(webd, dbName_InSet);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName_InSet
				+ "' NOT found");

		//delete the dashboard
		DashboardHomeUtil.deleteDashboard(webd, dbName_InSet, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

	}

	@Test(groups = "forth run", dependsOnMethods = { "testDeleteDashboardInSet" })
	public void testDuplicateDashboardAddToSet() throws Exception
	{
		dbName_InSet = "DashboardInSet-" + generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateDashboardAddToSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//create a dashboard in the dashboard set
		webd.getLogger().info("create a dashboard in the set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_InSet, null);

		//verify the dashboard is in the dashboard set
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet);

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_InSet + "-duplicate", null, true);

		webd.getLogger().info("Verify the duplicated dashboard is in the dashboard set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet + "-duplicate"),
				"Duplicated dashboard failed!");

		//delete the dashboard
		webd.getLogger().info("Delete the duplicate dashboard: " + dbName_InSet + "-duplicate");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_InSet + "-duplicate");
		DashboardBuilderUtil.deleteDashboardInsideSet(webd);
		webd.getLogger().info(
				"verify the dashboard: " + dbName_InSet + "-duplicate" + "has been deleted, and not in dashboard set as well");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet + "-duplicate"), "The dashboard:"
				+ dbName_InSet + "-duplicate" + " is still in the dashboard set");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet + "-duplicate"), "Delete dashboard: "
				+ dbName_InSet + "-duplicate" + " failed!");

	}

	@Test(groups = "forth run", dependsOnMethods = { "testDuplicateDashboardAddToSet" })
	public void testDuplicateDashboardNotAddToSet() throws Exception
	{
		dbName_OutSet = "DashboardOutSet-" + generateTimeStamp();
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateDashboardNotAddToSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//create a dashboard in the dashboard set
		webd.getLogger().info("create a dashboard in the set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_OutSet, null);

		//verify the dashboard is in the dashboard set
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_OutSet);

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_OutSet + "-duplicate", null, false);

		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_OutSet + "-duplicate", null, true),
				"Duplicate dashboard not add to set failed!");

		//back to home page
		webd.getLogger().info("Go to Home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboard set
		webd.getLogger().info("Open the dashboard set: " + dbsetName_Test1);
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		//verify the duplicated dashboard
		webd.getLogger().info("Verify the duplicate dashboard: " + dbName_OutSet + " is not in the dashboard set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_OutSet + "-duplicate"),
				"Dashboard has been duplicated and add to dashboard set");
	}

	@Test(groups = "second run", dependsOnGroups = { "first run" })
	public void testFavorite() throws Exception
	{
		dbsetName_Favorite = "DashboardSet_Favorite-" + generateTimeStamp();
		String dbsetDesc = "set the dashboard set as favorite";

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFavorite");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_Favorite, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Favorite), "Dashboard set NOT found!");

		//set the dashboard set as favorite
		webd.getLogger().info("Set the dashboard as favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOptionDashboardSet(webd), "Set the dashboard set as favorite failed!");

		//verify the dashboard set if set as favorite
		webd.getLogger().info("Click my favorite link in the branding bar");
		BrandingBarUtil.visitMyFavorites(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Verify if the dashboard set is favorite");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites options is NOT checked!");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Favorite), "NOT find the dashboard set");

		//open the dashboard set and remove the favorite
		webd.getLogger().info("Open the dashboard set in builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Favorite);
		webd.getLogger().info("Set the dashboard set as not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOptionDashboardSet(webd), "Set the dashboard set as NOT favorite failed!");

		//verify the dashboard set if not as favorite
		webd.getLogger().info("Click my favorite link in the branding bar");
		BrandingBarUtil.visitMyFavorites(webd);
		WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Verify if the dashboard set is not favorite");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites options is NOT checked!");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Favorite), "dashboard set is still favorite");
	}

	@Test(groups = "first run", dependsOnMethods = { "testCreateDashboardSet" })
	public void testModifyDashboardSet() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test);

		//edit the dashboardSet
		webd.getLogger().info("Edit the dashboard set");
		DashboardBuilderUtil.editDashboardSet(webd, dbsetName_Test + "-Modify", "modify the dashboard set");

		//verify the dashboardSet
		webd.getLogger().info("Verify if the dashboard set has been modified in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_Test + "-Modify"), "Dashboard set NOT found!");

	}

	@Test(groups = "second run", dependsOnGroups = { "first run" })
	public void testPrint() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testPrint");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard set in builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		//print the dashboard set
		webd.getLogger().info("Print the dashboard set");
		DashboardBuilderUtil.printDashboardSet(webd);
	}

	@Test(groups = "second run", dependsOnGroups = { "first run" })
	public void testRefresh() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRefresh");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		//verify the refresh status
		webd.getLogger().info("Verify the defaul refresh setting for the dashboard is 5 mins");
		Assert.assertTrue(DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet(webd,
				DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN), "the default setting is not ON(5 mins)");

		//set the refresh setting to 5 min
		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);
		webd.getLogger().info("Verify the refresh setting for the dashboard is OFF");
		Assert.assertTrue(DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet(webd,
				DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF), "the setting is not OFF");

		//set the refresh setting to OFF
		webd.getLogger().info("Set the refresh setting to 5 mins");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN);
		webd.getLogger().info("Verify the refresh setting for the dashboard is 5 mins");
		Assert.assertTrue(DashboardBuilderUtil.isRefreshSettingCheckedForDashbaordSet(webd,
				DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_5MIN), "the setting is not ON(5 mins)");
	}

	@Test(groups = "third run", dependsOnMethods = { "testAddDashboardInGridView" })
	public void testRemoveDashboardFromDashboardSet() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardFromDashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		WaitUtil.waitForPageFullyLoaded(webd);

		//remove a dashboard from the dashboard set
		webd.getLogger().info("Remove a dashboard from the dashborad set");
		DashboardBuilderUtil.removeDashboardFromSet(webd, dbName);

		thinkTime(8000L);

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is in the dashboard set");

	}

	@Test(groups = "first run", dependsOnMethods = { "testCreateDashboardSet", "testModifyDashboardSet" })
	public void testRemoveDashboardSetFromBuilderPage() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardSetFromBuilderPage");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard set in builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test + "-Modify");

		//delete the dashboard set
		webd.getLogger().info("Deleted the dashboard set in builder page");
		DashboardBuilderUtil.deleteDashboardSet(webd);

		//verify if in the home page
		webd.getLogger().info("Verify delete successfully and back to the home page");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(PageId.DashboardDisplayPanelCss)));

		//verify if in the dashboar set has been deleted
		webd.getLogger().info("Verify if the dashboard has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Test + "-Modify"), "Dashboard Set NOT removed");

	}

	@Test(groups = "last run", dependsOnGroups = { "forth run" })
	public void testRemoveDashboardSetFromHomePage() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardSetFromHomePage");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to the grid view
		webd.getLogger().info("Swtich to the grid view");
		DashboardHomeUtil.gridView(webd);

		//remove the test data
		webd.getLogger().info("Start to delete the dashboard set: " + dbsetName);
		DashboardHomeUtil.deleteDashboard(webd, dbsetName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("Verify the dashboard set: " + dbsetName + " has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName), "Delete dashboard " + dbsetName + " failed!");
		webd.getLogger().info("Delete the dashboard set: " + dbsetName + " finished");

	}

	@Test(groups = "last run", dependsOnMethods = { "testSetHome" })
	public void testRemoveHome() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveHome");

		//verify is in the builder page
		webd.getLogger().info("Start the test case: testSetHome");
		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("current url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains("builder.html?dashboardId=")) {
			Assert.fail("not open the builder page");
		}
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_setHome), "Not the correct home page");

		//remove the home page
		DashboardBuilderUtil.toggleHomeDashboardSet(webd);
		//verify the home page
		webd.getLogger().info("Go to home page and verify if the dashboard set is home page");
		BrandingBarUtil.visitMyHome(webd);
		Assert.assertTrue(WelcomeUtil.isServiceExistedInWelcome(webd, WelcomeUtil.SERVICE_NAME_DASHBOARDS),
				"It is NOT the home page!");

	}

	@Test(groups = "forth run", dependsOnMethods = { "testCreateDashboardInSet" })
	public void testSearchDashboardInSet() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testSearchDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		//click Add dashboard icon
		webd.getLogger().info("Click Add Dashboard Icon");
		webd.click("css=" + PageId.DashboardSetAddDashboardIcon_Css);

		//search the dashboard created in set
		DashboardHomeUtil.search(webd, dbName_InSet);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName_InSet
				+ "' NOT found");

		//delete the dashboard
		String InfoBtn_xpath = "//div[contains(@aria-label, 'DashboardInSet')]//button";
		webd.getLogger().info("Verfiy the current dashboard can not be deleted");
		webd.click(InfoBtn_xpath);
		WebElement removeButton = webd.getWebDriver().findElement(By.xpath(DashBoardPageId.RmBtnID));
		Assert.assertFalse(removeButton.isEnabled(), "delete is enabled for current dashboard");

	}

	@Test(groups = "last run", dependsOnGroups = { "forth run" })
	public void testSetHome() throws Exception
	{
		dbsetName_setHome = "DashboardSet_TestSetHome-" + generateTimeStamp();
		String dbsetDesc = "Test the dashboardset set home";

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testSetHome");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_setHome, dbsetDesc, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_setHome), "Dashboard set NOT found!");

		//set the dashboard as home
		webd.getLogger().info("Set the created dashborad set as Home");
		DashboardBuilderUtil.toggleHomeDashboardSet(webd);

		//verify the home page
		webd.getLogger().info("Go to home page and verify if the dashboard set is home page");
		BrandingBarUtil.visitMyHome(webd);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_setHome), "DashboarSet is NOT set to home page");
	}

	@Test(groups = "third run", dependsOnMethods = { "testAddDashboardInListView" })
	public void testShare() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testShare");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		//share the dashboardset
		webd.getLogger().info("Share the dashboard");
		Assert.assertTrue(DashboardBuilderUtil.toggleShareDashboardset(webd), "Share the dashboard set failed!");

	}

	@Test(groups = "third run", dependsOnMethods = { "testRemoveDashboardFromDashboardSet" })
	public void testShareWithoutDashboardInSet() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testShareWithoutDashboardInSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Set the refresh setting to OFF");
		DashboardBuilderUtil.refreshDashboardSet(webd, DashboardBuilderUtil.REFRESH_DASHBOARD_SETTINGS_OFF);

		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the share options are diabled
		Assert.assertTrue(DashBoardUtils.verfiyShareOptionDisabled(), "The options are enabled!");

	}

	@Test(groups = "third run", dependsOnMethods = { "testShare" })
	public void testStopSharing() throws Exception
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testStopSharing");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		//share the dashboardset
		webd.getLogger().info("Share the dashboard");
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboardset(webd), "Stop sharing the dashboard set failed!");

	}

	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}

	private void thinkTime(long waittime) throws InterruptedException
	{
		Thread.sleep(waittime);
	}
}