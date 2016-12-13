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
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

/**
 * @author shangwan
 */
public class TestDashboardSet_DashboardRelated_CreatedInsideSet extends LoginAndLogout
{
	private String dbsetName_Test1 = "";
	private String dbName_InSet = "";
	private String dbName_OutSet = "";
	private String dbName_DuplicateOOB = "";

	private static String OOBAddToSet = "Database Operations";

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
		DashBoardUtils.deleteDashboard(webd, dbsetName_Test1);
		DashBoardUtils.deleteDashboard(webd, dbName_InSet);
		DashBoardUtils.deleteDashboard(webd, dbName_OutSet);
		DashBoardUtils.deleteDashboard(webd, dbName_OutSet + "-duplicate");
		DashBoardUtils.deleteDashboard(webd, dbName_DuplicateOOB);

		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}

	@Test
	public void testCreateDashboardInSet()
	{
		//create dashboard set
		dbsetName_Test1 = "DashboardSet_For_Test_DashboardInsideSet-" + generateTimeStamp();
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

	@Test(dependsOnMethods = { "testModifyDashboardInSet_dbCreatedInsideSet" })
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

	@Test(dependsOnMethods = { "testDeleteDashboardInSet" })
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

	@Test(dependsOnMethods = { "testDuplicateDashboardAddToSet" })
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

	@Test(dependsOnMethods = { "testDuplicateDashboardNotAddToSet" })
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

	@Test(dependsOnMethods = { "testDuplicateOOBAddToSet" })
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

	@Test(dependsOnMethods = { "testSearchDashboardInSet" })
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
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_InSet), "Expected dashboard '" + dbName_InSet
				+ "' is found in dashboard home page");

	}

	@Test(dependsOnMethods = { "testDuplicateOOBNotAddToSet" })
	public void testRemoveDashboardSetWithDashboardCreatedInsideSet()
	{
		//init the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test case: testRemoveDashboardSetWithDashboardCreatedInsideSet");

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

	@Test(dependsOnMethods = { "testCreateDashboardInSet" })
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

	@Test(dependsOnMethods = { "testSearchDashboardCreatedInSetFromHome" })
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

	private String generateTimeStamp()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
