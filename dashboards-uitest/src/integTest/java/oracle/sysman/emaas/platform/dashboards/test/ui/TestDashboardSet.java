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
	private String dbName_DuplicateOOB = "";
	private String dbsetName_ITA = "";
	private String dbsetName_LA = "";
	private String dbset_testMaximizeRestore = "";
	private String dbName_indbSet = "";
	
	private static String OOBAddToSet = "Database Operations";

	@BeforeClass
	public void createTestData()
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

		//verify the dashboard
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName, "", true), "Dashboard NOT found!");

		LoginAndLogout.logoutMethod();
	}

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);
	}

	@AfterClass
	public void removeTestData()
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
		DashBoardUtils.deleteDashboard(webd, dbName);
		DashBoardUtils.deleteDashboard(webd, dbsetName_setHome);
		DashBoardUtils.deleteDashboard(webd, dbsetName_Favorite);
		DashBoardUtils.deleteDashboard(webd, dbsetName);
		DashBoardUtils.deleteDashboard(webd, dbsetName_Test1);
		DashBoardUtils.deleteDashboard(webd, dbName_InSet);
		DashBoardUtils.deleteDashboard(webd, dbName_OutSet);
		DashBoardUtils.deleteDashboard(webd, dbName_OutSet + "-duplicate");
		DashBoardUtils.deleteDashboard(webd, dbName_DuplicateOOB);
		DashBoardUtils.deleteDashboard(webd, dbsetName_ITA);
		DashBoardUtils.deleteDashboard(webd, dbsetName_LA);
		DashBoardUtils.deleteDashboard(webd, dbset_testMaximizeRestore);
		DashBoardUtils.deleteDashboard(webd,dbName_indbSet);
		
		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test(groups = "third run", dependsOnGroups = { "second run" })
	public void testAddDashboardInGridView()
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

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is NOT in the dashboard set");

	}

	@Test(groups = "third run", dependsOnMethods = { "testShareWithoutDashboardInSet" })
	public void testAddDashboardInListView()
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

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is NOT in the dashboard set");

	}

	@Test(groups = "forth run", dependsOnGroups = { "third run" })
	public void testCreateDashboardInSet()
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

		//verify the dashboard is in the dashboard set
		webd.getLogger().info("Verify the created dashboard is in the dashboard set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet), "Dashboard NOT found in the set");

	}

	@Test(groups = "first run")
	public void testCreateDashboardSet()
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

	@Test(groups = "forth run", dependsOnMethods = { "testModifyDashboardInSet_dbCreatedInsideSet" })
	public void testDeleteDashboardInSet()
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

		//search the dashboard created in set
		DashboardHomeUtil.search(webd, dbName_InSet);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName_InSet
				+ "' NOT found");

		//delete the dashboard
		DashboardHomeUtil.deleteDashboard(webd, dbName_InSet, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

	}

	//this case is for EMCPDF-2801, regression test
	@Test(groups = "third run", dependsOnMethods = { "testStopSharing" })
	public void testDeleteDashboardSelectedBySetFromHomePage()
	{

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDeleteDashboardSelectedBySetFromHomePage");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//remove the dashboard selected by dashboard set in home page
		webd.getLogger().info("Remove the dashboard selected by dashboard set in home page");
		DashboardHomeUtil.deleteDashboard(webd, dbName, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.search(webd, dbsetName);
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		//verify the dashboard set can be opened successfully
		webd.getLogger().info("Verify the dashboard set can be opened successfully");
		DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName);
	}

	@Test(groups = "forth run", dependsOnMethods = { "testDeleteDashboardInSet" })
	public void testDuplicateDashboardAddToSet()
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

		WaitUtil.waitForPageFullyLoaded(webd);

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
	public void testDuplicateDashboardNotAddToSet()
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

		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the dashboard is in the dashboard set
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_OutSet), "Create dashboard in set failed!");

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

	@Test(groups = "forth run", dependsOnMethods = { "testDuplicateDashboardNotAddToSet" })
	public void testDuplicateOOBAddToSet()
	{
		dbName_DuplicateOOB = "OOBDashboard-duplicate-" + generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateOOBAddToSet");

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

		//add an OOB dashboard in the dashboard set
		webd.getLogger().info("Add an OOB dashboard in the set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, OOBAddToSet);

		WaitUtil.waitForPageFullyLoaded(webd);

		//verify the dashboard is in the dashboard set
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, OOBAddToSet),
				"The OOB dashboard is not added into dashboard set");

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_DuplicateOOB, null, true);

		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_DuplicateOOB),
				"Duplicate OOB dashboard failed!");

		//delete the dashboard
		webd.getLogger().info("Delete the duplicate dashboard: " + dbName_DuplicateOOB);
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_DuplicateOOB);
		DashboardBuilderUtil.deleteDashboardInsideSet(webd);
		webd.getLogger().info(
				"verify the dashboard: " + dbName_DuplicateOOB + "has been deleted, and not in dashboard set as well");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_DuplicateOOB), "The dashboard:"
				+ dbName_DuplicateOOB + " is still in the dashboard set");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_DuplicateOOB), "Delete dashboard: "
				+ dbName_DuplicateOOB + " failed!");

	}

	@Test(groups = "forth run", dependsOnMethods = { "testDuplicateOOBAddToSet" })
	public void testDuplicateOOBNotAddToSet()
	{
		dbName_DuplicateOOB = "OOBDashboard-duplicate-" + generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateOOBNotAddToSet");

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

		//select the OOB dashboard in the set
		webd.getLogger().info("Select the OOB dashboard in the set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, OOBAddToSet);

		//verify the edit menu & save icon are not displayed in OOB
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB");
		webd.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PageId.DASHBOARDSETOPTIONS_CSS)));
		WaitUtil.waitForPageFullyLoaded(webd);

		webd.waitForElementPresent("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + PageId.DASHBOARDSETOPTIONSEDIT_CSS), "Edit menu is displayed in OOB");

		webd.getLogger().info("Select the OOB dashboard in the set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, OOBAddToSet);

		//duplicate the dashboard in set
		webd.getLogger().info("duplicate the dashboard in the dashboard set");
		DashboardBuilderUtil.duplicateDashboardInsideSet(webd, dbName_DuplicateOOB, null, false);

		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_DuplicateOOB, null, true),
				"Duplicate OOB dashboard failed!");

		//back to home page
		webd.getLogger().info("Go to Home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//open the dashboard set
		webd.getLogger().info("Open the dashboard set: " + dbsetName_Test1);
		DashboardHomeUtil.selectDashboard(webd, dbsetName_Test1);

		//verify the duplicated dashboard
		webd.getLogger().info("Verify the duplicate dashboard: " + dbName_DuplicateOOB + " is not in the dashboard set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_DuplicateOOB),
				"Dashboard has been duplicated and add to dashboard set");
	}

	@Test(groups = "second run", dependsOnGroups = { "first run" })
	public void testFavorite()
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

	@Test(groups = "fifth run", dependsOnGroups = { "forth run" })
	public void testFilterITADashboardSet()
	{
		dbsetName_ITA = "DashboardSet-ITA-" + generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterITADashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_ITA, null, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_ITA), "Dashboard set NOT found!");

		//add the ITA dashboard into the dashboard set
		webd.getLogger().info("Add a ITA oob dashboard into the set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, "Categorical");

		//back to the home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//set filter option, cloud services="IT Analytics" created by ME
		webd.getLogger().info("set filter option, cloud services='IT Analytics' and Created by ME");
		DashboardHomeUtil.filterOptions(webd, "ita");
		DashboardHomeUtil.filterOptions(webd, "me");
		webd.getLogger().info("Verify the created dashboard exists");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_ITA), "The dashboard NOT exists");

		//reset filter options
		webd.getLogger().info("Reset filter options");

	}

	@Test(groups = "fifth run", dependsOnGroups = { "forth run" })
	public void testFilterLADashboardSet()
	{
		dbsetName_LA = "DashboardSet-LA-" + generateTimeStamp();

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testFilterLADashboardSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboardset
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbsetName_LA, null, DashboardHomeUtil.DASHBOARDSET);

		//verify the dashboardset
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbsetName_LA), "Dashboard set NOT found!");

		//add the ITA dashboard into the dashboard set
		webd.getLogger().info("Add a ITA oob dashboard into the set");
		DashboardBuilderUtil.addNewDashboardToSet(webd, "Database Operations");

		//back to the home page
		webd.getLogger().info("Back to dashboard home page");
		BrandingBarUtil.visitDashboardHome(webd);

		//set filter option, cloud services="IT Analytics" created by ME
		webd.getLogger().info("set filter option, cloud services='Log Analytics' and Created by ME");
		DashboardHomeUtil.filterOptions(webd, "la");
		DashboardHomeUtil.filterOptions(webd, "me");
		webd.getLogger().info("Verify the created dashboard exists");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_LA), "The dashboard NOT exists");

		//reset filter options
		webd.getLogger().info("Reset filter options");
	}

	@Test(groups = "forth run", dependsOnMethods = { "testSearchDashboardInSet" })
	public void testModifyDashboardInSet_dbCreatedInsideSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardInSet_dbCreatedInsideSet");

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

		webd.getLogger().info("Select the created dashboard in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName_InSet);

		dbName_InSet = dbName_InSet + "-Modify";
		webd.getLogger().info("Modify the created dashboard in set");
		DashboardBuilderUtil.editDashboard(webd, dbName_InSet, "Modify the dasbboard in set", true);

		webd.getLogger().info("Verify the modified dashboard in set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_InSet), "Modified dashboard " + dbName_InSet
				+ " not in set");

		//click Add dashboard icon
		webd.getLogger().info("Click Add Dashboard Icon");
		webd.click("css=" + PageId.DASHBOARDSETADDDASHBOARDICON_CSS);

		//search the dashboard modified in set
		DashboardHomeUtil.search(webd, dbName_InSet);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName_InSet
				+ "' NOT found");

		//back to home page and verify the dashboard
		webd.getLogger().info("Back to the dashboard home page and verify the modified dashboard");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName
				+ "' is found in dashboard home page");

	}

	@Test(groups = "third run", dependsOnMethods = { "testAddDashboardInGridView" })
	public void testModifyDashboardInSet_dbCreatedOutsideSet()
	{

		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testModifyDashboardInSet_dbCreatedOutsideSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.selectDashboard(webd, dbsetName);

		webd.getLogger().info("Select the created dashboard in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, dbName);

		dbName = dbName + "-Modify";
		webd.getLogger().info("Modify the created dashboard in set");
		DashboardBuilderUtil.editDashboard(webd, dbName, "Modify the dasbboard in set", true);

		webd.getLogger().info("Verify the modified dashboard in set");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Modified dashboard not in set");

		//click Add dashboard icon
		webd.getLogger().info("Click Add Dashboard Icon");
		webd.click("css=" + PageId.DASHBOARDSETADDDASHBOARDICON_CSS);

		//search the dashboard modified in set
		DashboardHomeUtil.search(webd, dbName);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName), "Expected dashboard '" + dbName + "' NOT found");

		//back to home page and verify the dashboard
		webd.getLogger().info("Back to the dashboard home page and verify the modified dashboard");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName), "Expected dashboard '" + dbName
				+ "' NOT found in dashboard home page");

	}

	@Test(groups = "first run", dependsOnMethods = { "testCreateDashboardSet" })
	public void testModifyDashboardSet()
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
	public void testPrint()
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
	public void testRefresh()
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

	@Test(groups = "third run", dependsOnMethods = { "testModifyDashboardInSet_dbCreatedOutsideSet" })
	public void testRemoveDashboardFromDashboardSet()
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

		//verify the dashboard has been added to the dashboard set
		webd.getLogger().info("Verify if the dashboard exists in the dashborad set");
		Assert.assertFalse(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName), "Dashboard is in the dashboard set");

	}

	@Test(groups = "first run", dependsOnMethods = { "testCreateDashboardSet", "testModifyDashboardSet" })
	public void testRemoveDashboardSetFromBuilderPage()
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
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(PageId.DASHBOARDDISPLAYPANELCSS)));

		//verify if in the dashboar set has been deleted
		webd.getLogger().info("Verify if the dashboard has been deleted");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbsetName_Test + "-Modify"), "Dashboard Set NOT removed");

	}

	@Test(groups = "last run", dependsOnGroups = { "fifth run" })
	public void testRemoveDashboardSetFromHomePage()
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

	@Test(groups = "forth run", dependsOnMethods = { "testDuplicateOOBNotAddToSet" })
	public void testRemoveDashboardSetWithDashboardCreatedInsideSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testDuplicateOOBNotAddToSet");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//open the dashboardset
		webd.getLogger().info("Open the dashboard in the builder page");
		DashboardHomeUtil.deleteDashboard(webd, dbsetName_Test1, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);

		//refresh the home page
		webd.getLogger().info("Refresh the home page");
		BrandingBarUtil.visitDashboardHome(webd);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "The expected dashboard " + dbName_InSet
				+ "NOT found in dashboard home page");

	}

	@Test(groups = "last run", dependsOnMethods = { "testSetHome" })
	public void testRemoveHome()
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
	public void testSearchDashboardCreatedInSetFromHome()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testSearchDashboardCreatedInSetFromHome");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to grid view
		webd.getLogger().info("Switch to the grid view");
		DashboardHomeUtil.gridView(webd);

		//search the dashboard which created in dashboard set in home page
		webd.getLogger().info("Search the dashboard which created in dashboard set in home page");
		DashboardHomeUtil.search(webd, dbName_InSet);
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName_InSet
				+ "' has been found");

	}

	@Test(groups = "forth run", dependsOnMethods = { "testSearchDashboardCreatedInSetFromHome" })
	public void testSearchDashboardInSet()
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
		webd.click("css=" + PageId.DASHBOARDSETADDDASHBOARDICON_CSS);

		//search the dashboard created in set
		DashboardHomeUtil.search(webd, dbName_InSet);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName_InSet
				+ "' NOT found");

		//delete the dashboard
		String InfoBtn_xpath = "//div[contains(@aria-label, 'DashboardInSet')]//button";
		webd.getLogger().info("Verfiy the current dashboard can not be deleted");
		webd.click(InfoBtn_xpath);
		WebElement removeButton = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.RMBTNID));
		Assert.assertFalse(removeButton.isEnabled(), "delete is enabled for current dashboard");

	}

	@Test(groups = "last run", dependsOnGroups = { "fifth run" })
	public void testSetHome()
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
	public void testShare()
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
	public void testShareWithoutDashboardInSet()
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
	public void testStopSharing()
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

	//test maxmize/restore widget in OOB Dashboard Set
	@Test(groups = "sixth run")
	public void testMaximizeRestoreWidget_OOB_DbSet()
	{
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget");

		//open the OOB Dashboard Set, eg: Exadata Health in the dashboard home page
		webd.getLogger().info("open the dashboard set");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.selectDashboardInsideSet(webd,"Overview");
		DashboardBuilderUtil.maximizeWidget(webd, "Entities by Database Machine", 0);
		DashboardBuilderUtil.restoreWidget(webd, "Entities by Database Machine", 0);

		//verify the edit/add button not displayed in the page					
		WebElement addButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Add Content']"));
		Assert.assertFalse(addButton.isDisplayed(), "Add button be displayed in system dashboard set");  

		WebElement editButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertFalse(editButton.isDisplayed(), "Edit button be displayed in system dashboard set");
	}
	
	//test maxmize/restore widget in self created dashboard set, and select a system dashboard to test edit button
	@Test(groups = "sixth run")
	public void testMRWidgetSelfDbSet_sysBb()
	{
		dbset_testMaximizeRestore = "selfDbSet-" + generateTimeStamp();
		
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget in selt create db set");
		
		//Create dashboard set
		webd.getLogger().info("Create a new dashboard set");
		DashboardHomeUtil.createDashboard(webd, dbset_testMaximizeRestore, null, DashboardHomeUtil.DASHBOARDSET);
		
		//verify the dashboard set
		webd.getLogger().info("Verify if the dashboard set existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardSet(webd, dbset_testMaximizeRestore), "Create dashboard set failed!");

		//Select a dashboard and open it
		webd.getLogger().info("select and open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Servers");
		
		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.maximizeWidget(webd, "Application Server Status", 0);
		DashboardBuilderUtil.restoreWidget(webd, "Application Server Status", 0);
		
		//verify the add button not displayed in the page					
		WebElement addButton1 = webd.getWebDriver().findElement(By.xpath("//button[@title='Add Content']"));
		Assert.assertFalse(addButton1.isDisplayed(), "Add button be displayed in system dashboard set");  
		
		//verify the edit button displayed in the page
		WebElement editButton1 = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertTrue(editButton1.isDisplayed(), "Edit button isn't displayed in self dashboard set");
	}
	
	//test maxmize/restore widget in self created dashboard set, and self creating a dashboard to test add/edit button
	@Test(groups = "sixth run", dependsOnMethods = { "testMRWidgetSelfDbSet_sysBb" })
	public void testMRWidgetSelfDbSet_selfBb()
	{
		dbName_indbSet = "selfDb-" + generateTimeStamp();
		
		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget in selt create db set");
		
		//open the created dashboard set
		webd.getLogger().info("select and open the dashboard set");
		DashboardHomeUtil.selectDashboard(webd, dbset_testMaximizeRestore);
		
		//add self created dashboard to the dashboard set
		webd.getLogger().info("Add another dashboard to set: create a new dashboard to this set");
		DashboardBuilderUtil.createDashboardInsideSet(webd, dbName_indbSet, "");
		
		webd.getLogger().info("Verify if the dashboard existed in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboardInsideSet(webd, dbName_indbSet), "Creat dashboard in dashboard set failed!");		
		
		//Add widget 
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Access Log Error Status Codes");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "All Logs Trend");
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "Access Log Error Status Codes"),
				"Widget 'Access Log Error Status Codes' not found");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "All Logs Trend"),
				"Widget 'All Logs Trend' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);	

		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.maximizeWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertFalse(webd.isDisplayed("css="+".dbd-widget[data-tile-name=\"All Logs Trend\"]"), "Widget 'All Logs Trend' is still displayed");
		
		DashboardBuilderUtil.restoreWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertTrue(webd.isDisplayed("css="+".dbd-widget[data-tile-name=\"All Logs Trend\"]"), "Widget 'All Logs Trend' is not displayed");
		
		//verify the edit/add button displayed in the page
		WebElement addButton2 = webd.getWebDriver().findElement(By.xpath("//button[@title='Add Content']"));
		Assert.assertTrue(addButton2.isDisplayed(), "Add button isn't displayed in self dashboard which in self dashboard set");

		WebElement editButton2 = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertTrue(editButton2.isDisplayed(), "Edit button isn't displayed in self dashboard which in self dashboard set");
	}
	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
