package oracle.sysman.emaas.platform.dashboards.test.ui;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.BrandingBarUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.GlobalContextUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.TimeSelectorUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeRange;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.ITimeSelectorUtil.TimeUnit;

public class TestDashBoard_BuilderPage extends LoginAndLogout
{
	private String dbName_favorite = "";
	private String dbName_timepicker = "";
	private String dbName_columncheck = "";
//	private String dbName_ITADashboard = "";
//	private String dbName_LADashboard = "";
	private String dbName_testMaximizeRestore = "";
//	private String dbName_CustomWidget = "";
	private String dbName_Test = "";
	private String dbDesc_Test = "";
//	private String dbName_duplicate = "";
//	private String dbName_duplicateOOB = "";
//	private String dbName_saveConfirmation = "";
	private String dbName_textWidget = "";
//	private String dbName_textWidget_toolbar = "";
//	private String dbName_longName = "dashboardNamedashboardNamedashboardNamedashboardNamedashboardNam";
//	private String dbName_textWidget_image = "";
//	private String dbName_textWidget_link = "";
//	private String dbName_textWidget_multiLink = "";
//	private String dbName_textWidget_order = "";
//	private String dbName_textWidget_empty = "";

	private String dbRespectGCTimeRangeName = "";
	private String dbRespectGCTimeRangeName_URLCheck = "";
//	private final String dbRespectGCEntityName = "";

//	private final String widgetName_LA = "Database Errors Trend";
	private final String widgetName_UDE = "Area Chart";
//	private final String customWidgetName = "Execution Details";
//	private final String OOBName = "Middleware Operations";
//	private final String OOBDesc = "Displays the current health of your Oracle middleware ecosystem";

	
	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

	}

	@BeforeClass
	public void createTestDashboard()
	{
		dbName_Test = "Dashboard_withWidget-" + DashBoardUtils.generateTimeStamp();
		dbDesc_Test = "test dashboard";

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
		DashboardBuilderUtil.saveDashboard(webd);
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
		DashBoardUtils.deleteDashboard(webd, dbName_timepicker);
	//	DashBoardUtils.deleteDashboard(webd, dbName_saveConfirmation);
		DashBoardUtils.deleteDashboard(webd, dbName_columncheck);
	//	DashBoardUtils.deleteDashboard(webd, dbName_ITADashboard);
	//	DashBoardUtils.deleteDashboard(webd, dbName_LADashboard);
		DashBoardUtils.deleteDashboard(webd, dbName_testMaximizeRestore);
	//	DashBoardUtils.deleteDashboard(webd, dbName_CustomWidget);
		DashBoardUtils.deleteDashboard(webd, dbName_Test);
	//	DashBoardUtils.deleteDashboard(webd, dbName_duplicate);
	//	DashBoardUtils.deleteDashboard(webd, dbName_duplicateOOB);
		DashBoardUtils.deleteDashboard(webd, dbName_textWidget);
	//	DashBoardUtils.deleteDashboard(webd, dbName_longName);
	//	DashBoardUtils.deleteDashboard(webd, dbName_textWidget_toolbar);
	//	DashBoardUtils.deleteDashboard(webd, dbName_textWidget_link);
	//	DashBoardUtils.deleteDashboard(webd, dbName_textWidget_multiLink);
	//	DashBoardUtils.deleteDashboard(webd, dbName_textWidget_order);
		
		DashBoardUtils.deleteDashboard(webd, dbRespectGCTimeRangeName);
		DashBoardUtils.deleteDashboard(webd, dbRespectGCTimeRangeName_URLCheck);
		
		webd.getLogger().info("All test data have been removed");

		LoginAndLogout.logoutMethod();
	}
	
	@Test(alwaysRun=true)
	public void testDashboardWith12Columns()
	{
		dbName_columncheck = "DashboardWith12Columns-" + DashBoardUtils.generateTimeStamp();
		String desc = "Description for " + dbName_columncheck;

		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testDashboardWith12Columns");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//create dashboard
		webd.getLogger().info("Start to create dashboard");
		DashboardHomeUtil.createDashboard(webd, dbName_columncheck, desc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_columncheck, desc, true),
				"Failed to verify the created dashboard. Probably the creation failed, or name/description is wrongly specified!");

		String widgetName = "Database Errors Trend";
		//add widget
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName);
		webd.getLogger().info("Add widget finished");

		webd.getLogger().info("Get narrower widgets");
		for (int i = 1; i <= 4; i++) {
			DashboardBuilderUtil.resizeWidget(webd, widgetName, DashboardBuilderUtil.TILE_NARROWER);


		}
		webd.getLogger().info("Finished to get narrower widgets");

		webd.getLogger().info("Get wider widgets");
		for (int i = 1; i <= 10; i++) {
			DashboardBuilderUtil.resizeWidget(webd, widgetName, DashboardBuilderUtil.TILE_WIDER);

		}
		webd.getLogger().info("Finished to get wider widgets");

		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);
	}
	
	@Test(alwaysRun=true)
	public void testFavorite()
	{
		dbName_favorite = "favoriteDashboard-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "favorite_testDashboard desc";

		//Initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testFavorite");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_favorite, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to favorite
		webd.getLogger().info("Set the dashboard to favorite");
		Assert.assertTrue(DashboardBuilderUtil.favoriteOption(webd));

		//verify the dashboard is favorite
		webd.getLogger().info("Visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);

		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");

		webd.getLogger().info("Verfiy the dashboard is favorite");
		//DashboardHomeUtil.search(webd, dbName_favorite);
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite), "Can not find the dashboard");

		webd.getLogger().info("Open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_favorite);
		webd.getLogger().info("Verify the dashboard in builder page");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_favorite, dbDesc, true), "Create dashboard failed!");

		//set it to not favorite
		webd.getLogger().info("set the dashboard to not favorite");
		Assert.assertFalse(DashboardBuilderUtil.favoriteOption(webd), "Set to not my favorite dashboard failed!");

		//verify the dashboard is not favoite
		webd.getLogger().info("visit my favorite page");
		BrandingBarUtil.visitMyFavorites(webd);
		webd.getLogger().info("Verfiy the favortie checkbox is checked");
		Assert.assertTrue(DashboardHomeUtil.isFilterOptionSelected(webd, "favorites"), "My Favorites option is NOT checked");

		webd.getLogger().info("Verfiy the dashboard is not favorite");
		Assert.assertFalse(DashboardHomeUtil.isDashboardExisted(webd, dbName_favorite),
				"The dashboard is still my favorite dashboard");

		//delete the dashboard
		webd.getLogger().info("start to delete the dashboard");

		WebElement el = webd.getWebDriver().findElement(By.id(DashBoardPageId.FAVORITE_BOXID));
		if (el.isSelected()) {
			el.click();
		}
		DashboardHomeUtil.deleteDashboard(webd, dbName_favorite, DashboardHomeUtil.DASHBOARDS_GRID_VIEW);
		webd.getLogger().info("the dashboard has been deleted");
	}
	@Test
	public void testHideEntityFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHideEntityFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//hide entity filter in dashboard
		webd.getLogger().info("hide entity filter");
		Assert.assertFalse(DashboardBuilderUtil.showEntityFilter(webd, false), "hide entity filter failed");

		//verify the target selector not displayed in the page
		webd.getLogger().info("Verify the target selecotr not diplayed in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.TARGETSELECTOR_CSS), "The target selector is in the page");
	}
	
	@Test
	public void testHideTimeRangeFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testHideTimeRangeFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//hide time range filter in dashboard
		webd.getLogger().info("hide time range filter");
		Assert.assertFalse(DashboardBuilderUtil.showTimeRangeFilter(webd, false), "hide time range filter failed");

		//verify the time range filter not displayed in the page
		webd.getLogger().info("Verify the time range filter not diplayed in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is in the page");
	}
	
	@Test
	public void testMaximizeRestoreWidgetSelfDashboard()
	{
		dbName_testMaximizeRestore = "selfDb-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testMaximize/RestoreWidget");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbName_testMaximizeRestore, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_testMaximizeRestore, null, true),
				"Create dashboard failed!");

		//Add two widgets
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Access Log Error Status Codes");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "All Logs Trend");
		webd.getLogger().info("Add widget finished");

		//verify if the widget added successfully
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "Access Log Error Status Codes"),
				"Widget 'Access Log Error Status Codes' not found");
		Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, "All Logs Trend"), "Widget 'All Logs Trend' not found");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//widget operation
		webd.getLogger().info("maximize/restore the widget");
		DashboardBuilderUtil.maximizeWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertFalse(webd.isDisplayed("css=" + ".dbd-widget[data-tile-name=\"All Logs Trend\"]"),
				"Widget 'All Logs Trend' is still displayed");

		DashboardBuilderUtil.restoreWidget(webd, "Access Log Error Status Codes", 0);
		Assert.assertTrue(webd.isDisplayed("css=" + ".dbd-widget[data-tile-name=\"All Logs Trend\"]"),
				"Widget 'All Logs Trend' is not displayed");

		//verify the edit/add button displayed in the page
		WebElement addButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Add Content']"));
		Assert.assertTrue(addButton.isDisplayed(), "Add button isn't displayed in system dashboard set");

		WebElement editButton = webd.getWebDriver().findElement(By.xpath("//button[@title='Edit Settings']"));
		Assert.assertTrue(editButton.isDisplayed(), "Edit button isn't displayed in system dashboard set");
	}
	
	@Test
	public void testShareDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShareDashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//sharing dashbaord
		webd.getLogger().info("Share the dashboard");
		Assert.assertTrue(DashboardBuilderUtil.toggleShareDashboard(webd), "Share dashboard failed!");

	}

	@Test
	public void testShowEntityFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShowEntityFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//show entity filter in dashboard
		webd.getLogger().info("show entity filter");
		Assert.assertTrue(DashboardBuilderUtil.showEntityFilter(webd, true), "show entity filter failed");

		//verify the target selector not displayed in the page
		webd.getLogger().info("Verify the target selecotr diplayed in the page");
		Assert.assertTrue(webd.isDisplayed("css=" + PageId.TARGETSELECTOR_CSS), "The target selector is NOT in the page");
	}

	@Test
	public void testShowTimeRangeFilter()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testShowTimeRangeFilter");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//show time range filter in dashboard
		webd.getLogger().info("Show time range filter");
		Assert.assertTrue(DashboardBuilderUtil.showTimeRangeFilter(webd, true), "Show time range filter failed");

		//verify the time range filter displayed in the page
		webd.getLogger().info("Verify the time range filter diplayed in the page");
		Assert.assertTrue(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is NOT in the page");

		//add test case for EMCPDF-1412

		webd.getLogger().info("change time range filter");
		Assert.assertFalse(DashboardBuilderUtil.showTimeRangeFilter(webd, false), "Show time range filter failed");

		//verify the time range filter not displayed in the page
		webd.getLogger().info("Verify the time range filter is invisible in the page");
		Assert.assertFalse(webd.isDisplayed("css=" + PageId.DATETIMEPICK_CSS), "The time range filter is displayed in the page");

	}

	@Test(dependsOnMethods = { "testShareDashboard" })
	public void testStopShareDashboard()
	{
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testStopShareDashboard");

		DashboardHomeUtil.gridView(webd);

		//search the dashboard and open it in builder page
		webd.getLogger().info("search the dashboard");
		DashboardHomeUtil.search(webd, dbName_Test);
		webd.getLogger().info("verify the dashboard is existed");
		Assert.assertTrue(DashboardHomeUtil.isDashboardExisted(webd, dbName_Test), "Dashboard NOT found!");
		webd.getLogger().info("open the dashboard");
		DashboardHomeUtil.selectDashboard(webd, dbName_Test);

		//stop sharing dashbaord
		webd.getLogger().info("Stop share the dashboard");
		Assert.assertFalse(DashboardBuilderUtil.toggleShareDashboard(webd), "Stop sharing the dashboard failed!");
	}

	@Test
	public void testTimePicker()
	{
		dbName_timepicker = "TestDashboardTimeselector-" + DashBoardUtils.generateTimeStamp();
		String dbDesc = "Test Dashboard timeselector description";

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in testTimePicker");

		//reset the home page
		webd.getLogger().info("Reset all filter options in the home page");
		DashboardHomeUtil.resetFilterOptions(webd);

		//switch to Grid View
		webd.getLogger().info("Switch to grid view");
		DashboardHomeUtil.gridView(webd);

		//create dashboard
		webd.getLogger().info("Create a dashboard: with description, time refresh");
		DashboardHomeUtil.createDashboard(webd, dbName_timepicker, dbDesc, DashboardHomeUtil.DASHBOARD);

		//verify dashboard in builder page
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, true), "Create dashboard failed!");

		//disable time selector
		DashboardBuilderUtil.showTimeRangeFilter(webd, false);
		//verify the dashboard, the time selector no display
		webd.getLogger().info("Verify the time selector is not diplayed in dashboard");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, false),
				"Time selector component is still displayed!");
		//enable time selector
		DashboardBuilderUtil.showTimeRangeFilter(webd, true);
		//verify the dashboard, the time selector is displayed
		webd.getLogger().info("Verify the time selector is diplayed in dashboard");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_timepicker, dbDesc, true),
				"Time selector component is not displayed!");

		//Add the widget to the dashboard
		webd.getLogger().info("Start to add Widget into the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, "Database Errors Trend");
		webd.getLogger().info("Add widget finished");

		//save dashboard
		webd.getLogger().info("save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//set time range for time picker
		webd.getLogger().info("Set the time range");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last15Mins);
		String returnTimeRange = TimeSelectorUtil.setCustomTime(webd, "04/06/2015 07:22 PM", "04/07/2016 09:22 PM");

		//convert date to timestamp
		SimpleDateFormat fmt = new SimpleDateFormat("MMM d, yyyy h:mm a");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date dTmpStart = new Date();
		Date dTmpEnd = new Date();

		String tmpReturnDate = returnTimeRange.substring(7);

		String[] tmpDate = tmpReturnDate.split("-");

		String tmpStartDate = tmpDate[0].trim();
		String tmpEndDate = tmpDate[1].trim();

		try {
			dTmpStart = fmt.parse(tmpStartDate);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dTmpEnd = fmt.parse(tmpEndDate);
		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String tmpStartDateNew = df.format(dTmpStart);
		String tmpEndDateNew = df.format(dTmpEnd);
		webd.getLogger().info(tmpStartDateNew);
		webd.getLogger().info(tmpEndDateNew);

		long StartTimeStamp = Timestamp.valueOf(tmpStartDateNew).getTime();
		long EndTimeStamp = Timestamp.valueOf(tmpEndDateNew).getTime();

		webd.getLogger().info(String.valueOf(StartTimeStamp));
		webd.getLogger().info(String.valueOf(EndTimeStamp));

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, "Database Errors Trend");

		//verify the url
		webd.switchToWindow();
		//WaitUtil.waitForPageFullyLoaded(webd);
		webd.getLogger().info("Wait for the widget loading....");
		WebDriverWait wait1 = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait1.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='srchSrch']")));

		String url = webd.getWebDriver().getCurrentUrl();
		webd.getLogger().info("url = " + url);
		if (!url.substring(url.indexOf("emsaasui") + 9).contains(
				"startTime%3D" + String.valueOf(StartTimeStamp) + "%26endTime%3D" + String.valueOf(EndTimeStamp))) {
			Assert.fail("not open the correct widget");
		}
	}
	
	@Test
	public void testTextWidget()
	{
		dbName_textWidget = "Dashboard_textWidget-" + DashBoardUtils.generateTimeStamp();
		
		String dbDesc = "Add text widget into dashboard";
		String content = "This is the dashboard which is used to test the new feature of adding text widget";
		//String content_Hyperlink = "";
		
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in testTextWidget");

		DashboardHomeUtil.gridView(webd);

		webd.getLogger().info("Create the dashboard, then to add text widget");
		DashboardHomeUtil.createDashboard(webd, dbName_textWidget, dbDesc, DashboardHomeUtil.DASHBOARD);
		
		webd.getLogger().info("Verify the dashboard created Successfully");
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbName_textWidget, dbDesc, true), "Create dashboard failed!");		
		
		DashboardBuilderUtil.addTextWidgetToDashboard(webd);
		
		//Assert.assertTrue(DashboardBuilderUtil.verifyWidget(webd, widgetName), "text widget isn't added into the dashboard successfully");
		DashboardBuilderUtil.editTextWidgetAddContent(webd, 1, content);
		
		DashboardBuilderUtil.saveDashboard(webd);
		
		//Verify the content is added successfully
		webd.getLogger().info("Verify the content is added successfully");
		
		WebElement textContent = webd.getWebDriver().findElement(By.cssSelector(DashBoardPageId.TEXTCONTENTCSS));
		Assert.assertEquals(textContent.getText(), content);
	}
	
	@Test(alwaysRun = true)
	public void testRespectGCTimeRange() throws InterruptedException
	{
		dbRespectGCTimeRangeName = "respectGC TimeRange-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger()
				.info("start to test testRespectGCTimeRange");

		//Create dashboard
		DashboardHomeUtil.createDashboard(webd, dbRespectGCTimeRangeName, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCTimeRangeName, null, true),
				"Create dashboard failed!");

		//add widget to the dashboard
		webd.getLogger().info("Add widget to the dashboard");
		DashboardBuilderUtil.addWidgetToDashboard(webd, widgetName_UDE);

		//respect GC for TimeRange
		webd.getLogger().info("Respect GC for Time Range");
		DashboardBuilderUtil.respectGCForTimeRange(webd);

		//save the dashboard
		webd.getLogger().info("Save the dashboard");
		DashboardBuilderUtil.saveDashboard(webd);

		//change the time range, verify the url will be changed
		webd.getLogger().info("Change the time range, verify the url will be changed");
		GlobalContextUtil.setTimeRange(webd, TimeRange.NewLast7Days);

		String currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_7_DAY"), true);

		//open the widget
		webd.getLogger().info("Open the widget");
		DashboardBuilderUtil.openWidget(webd, widgetName_UDE);

		WaitUtil.waitForPageFullyLoaded(webd);

		Assert.assertEquals(GlobalContextUtil.getTimeRangeLabel(webd).contains("Last 7 days"), true);
	}
	
	@Test(alwaysRun = true)
	public void testRespectGCTimeRange_URLCheck()
	{
		dbRespectGCTimeRangeName_URLCheck = "respectGC TimeRange URL-" + DashBoardUtils.generateTimeStamp();

		//initialize the test
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger()
				.info("start to test testRespectGCTimeRange_URLCheck");

		//Create dashboard
		webd.getLogger().info("Create dashboard");
		DashboardHomeUtil.createDashboard(webd, dbRespectGCTimeRangeName_URLCheck, null, DashboardHomeUtil.DASHBOARD);
		Assert.assertTrue(DashboardBuilderUtil.verifyDashboard(webd, dbRespectGCTimeRangeName_URLCheck, null, true),
				"Create dashboard failed!");
		
		webd.getLogger().info("Respect GC");
		Assert.assertTrue(DashboardBuilderUtil.respectGCForTimeRange(webd), "Fail to configure respect GC");
		
		webd.getLogger().info("Start to set time range then check the URL change as well");
		
		webd.getLogger().info("Set Last 15 mins");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last15Mins);
		String currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_15_MINUTE"), true);

		webd.getLogger().info("Set Last 30 mins");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last30Mins);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_30_MINUTE"), true);
		
		webd.getLogger().info("Set Last 60 mins");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.NewLast60Mins);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_60_MINUTE"), true);
		
		webd.getLogger().info("Set Last 8 hours");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last8Hours);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_8_HOUR"), true);
		
		webd.getLogger().info("Set Last 24 hours");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last24Hours);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_24_HOUR"), true);
		
		webd.getLogger().info("Set Last 7 days");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.NewLast7Days);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_7_DAY"), true);
		
		webd.getLogger().info("Set Last 14 days");
		TimeSelectorUtil.setTimeRange(webd, TimeRange.Last14Days);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_14_DAY"), true);
		
		webd.getLogger().info("Set custom Last 8 mins");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 8, TimeUnit.Minute);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_8_MINUTE"), true);
		
		webd.getLogger().info("Set custom Last 14 hours");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 14, TimeUnit.Hour);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_14_HOUR"), true);
		
		webd.getLogger().info("Set custom Last 5 days");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 5, TimeUnit.Day);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_5_DAY"), true);
		
		webd.getLogger().info("Set custom Last 3 weeks");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 3, TimeUnit.Week);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_3_WEEK"), true);
		
		webd.getLogger().info("Set custom Last 6 months");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 6, TimeUnit.Month);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_6_MONTH"), true);
		
		webd.getLogger().info("Set custom Last 2 years");
		TimeSelectorUtil.setFlexibleRelativeTimeRange(webd, 2, TimeUnit.Year);
		currentURL = webd.getWebDriver().getCurrentUrl();
		Assert.assertEquals(currentURL.contains("timePeriod%3DLAST_2_YEAR"), true);		
	}
}
