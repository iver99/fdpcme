package oracle.sysman.emaas.platform.dashboards.test.ui;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.PageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardBuilderUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId_190;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class VerifyOOB extends LoginAndLogout
{

	public void initTest(String testName)
	{
		login(this.getClass().getName() + "." + testName);
		DashBoardUtils.loadWebDriver(webd);

		//reset all the checkboxes
		DashboardHomeUtil.resetFilterOptions(webd);
	}

	@Test
	public void verifyAPM_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in verifyAPM_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		verifyAPM();
	}

	@Test
	public void verifyAPM_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start to test in verifyAPM_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		verifyAPM();
	}

	@Test
	public void verifyAPM_withFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAPM_withFilter_GridView");

		//click Filter-APM
		webd.getLogger().info("Click Cloud Services - APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		//verify APM
		verifyAPM();
	}

	@Test
	public void verifyAPM_withFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAPM_withFilter_ListView");

		//click Filter-APM
		webd.getLogger().info("Click Cloud Services - APM");
		DashboardHomeUtil.filterOptions(webd, "apm");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open APM
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Monitoring");

		//verify APM
		verifyAPM();
	}

	@Test
	public void verifyApplicationPerfAnalytics_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		verifyApplicationPerfAnalytics();
	}

	@Test
	public void verifyApplicationPerfAnalytics_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		verifyApplicationPerfAnalytics();
	}

	@Test
	public void verifyApplicationPerfAnalytics_withFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_withFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		verifyApplicationPerfAnalytics();
	}

	@Test
	public void verifyApplicationPerfAnalytics_withFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyApplicationPerfAnalytics_withFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Application Performance Analytics
		webd.getLogger().info("Open the OOB dashboard---Application Performance Analytics");
		DashboardHomeUtil.selectDashboard(webd, "Application Performance Analytics");

		//verify Application PerfAnalytics
		verifyApplicationPerfAnalytics();
	}

	@Test
	public void verifyAvailabilityAnalytics_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		verifyAvailabilityAnalytics();
	}

	@Test
	public void verifyAvailabilityAnalytics_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		verifyAvailabilityAnalytics();
	}

	@Test
	public void verifyAvailabilityAnalytics_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability nalytics
		verifyAvailabilityAnalytics();
	}

	@Test
	public void verifyAvailabilityAnalytics_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyAvailabilityAnalytics_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Availability Analytics
		webd.getLogger().info("Open the OOB dashboard");
		DashboardHomeUtil.selectDashboard(webd, "Availability Analytics");

		//verify Availability Analytics
		verifyAvailabilityAnalytics();
	}

	@Test
	public void verifyDatabaseOperations_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_GridView");

		//lick on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
		verifyDatabaseOperations_Details();
	}

	@Test
	public void verifyDatabaseOperations_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_ListView");

		//lick on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
	}

	@Test
	public void verifyDatabaseOperations_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_WithFilter_GridView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//lick on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
	}

	@Test
	public void verifyDatabaseOperations_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyDatabaseOperations_WithFilter_ListView");

		//click Filter-Application Servers
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//lick on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Database Operations
		webd.getLogger().info("Open the OOB dashboard---Database Operations");
		DashboardHomeUtil.selectDashboard(webd, "Database Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Database Operations
		verifyDatabaseOperations();
	}

	@Test
	public void verifyEnterpriseHealth_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		verifyEnterpriseHealth();
		verifyEnterpriseHealth_Details();
	}

	@Test
	public void verifyEnterpriseHealth_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		verifyEnterpriseHealth();
	}

	@Test
	public void verifyEnterpriseHealth_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		verifyEnterpriseHealth();
	}

	@Test
	public void verifyEnterpriseHealth_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyEnterpriseHealth_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Enterprise Health
		webd.getLogger().info("Open the OOB dashboard---Enterprise Health");
		DashboardHomeUtil.selectDashboard(webd, "Enterprise Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Enterprise Health
		verifyEnterpriseHealth();
	}

	@Test
	public void verifyExadataHealth_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		verifyExadataHealth();
		verifyExadataHealth_Details();
	}

	@Test
	public void verifyExadataHealth_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		verifyExadataHealth();
	}

	@Test
	public void verifyExadataHealth_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		verifyExadataHealth();
	}

	@Test
	public void verifyExadataHealth_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyExadataHealth_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Exadata Health
		webd.getLogger().info("Open the OOB dashboard---Exadata Health");
		DashboardHomeUtil.selectDashboard(webd, "Exadata Health");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Exadata Health
		verifyExadataHealth();
	}

	@Test
	public void verifyHostOperations_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
		verifyHostOperations_Details();
	}

	@Test
	public void verifyHostOperations_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
	}

	@Test
	public void verifyHostOperations_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
	}

	@Test
	public void verifyHostOperations_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyHostOperations_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Host Operations
		webd.getLogger().info("Open the OOB dashboard---Host Operations");
		DashboardHomeUtil.selectDashboard(webd, "Host Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Host Operations
		verifyHostOperations();
	}

	@Test
	public void verifyMiddlewareOperations_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
		verifyMiddlewareOperations_Details();
	}

	@Test
	public void verifyMiddlewareOperations_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
	}

	@Test
	public void verifyMiddlewareOperations_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
	}

	@Test
	public void verifyMiddlewareOperations_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyMiddlewareOperations_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - Log Analytics");
		DashboardHomeUtil.filterOptions(webd, "la");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Middleware Operations
		webd.getLogger().info("Open the OOB dashboard---Middleware Operations");
		DashboardHomeUtil.selectDashboard(webd, "Middleware Operations");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Middleware Operations
		verifyMiddlewareOperations();
	}

	@Test
	public void verifyPerfAnalyticsApplication_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		verifyPerfAnalyticsApplication();
	}

	@Test
	public void verifyPerfAnalyticsApplication_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		verifyPerfAnalyticsApplication();
	}

	@Test
	public void verifyPerfAnalyticsApplication_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		verifyPerfAnalyticsApplication();
	}

	@Test
	public void verifyPerfAnalyticsApplication_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsApplication_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics Application Server");

		//verify Perf Analytics Application
		verifyPerfAnalyticsApplication();
	}

	@Test
	public void verifyPerfAnalyticsDatabase_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		verifyPerfAnalyticsDatabase();
	}

	@Test
	public void verifyPerfAnalyticsDatabase_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		verifyPerfAnalyticsDatabase();
	}

	@Test
	public void verifyPerfAnalyticsDatabase_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		verifyPerfAnalyticsDatabase();
	}

	@Test
	public void verifyPerfAnalyticsDatabase_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyPerfAnalyticsDatabase_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Performance Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Performance Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Performance Analytics: Database");

		//verify Perf Analytics Database
		verifyPerfAnalyticsDatabase();
	}

	@Test
	public void verifyResourceAnalyticsDatabase_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		verifyResourceAnalyticsDatabase();
	}

	@Test
	public void verifyResourceAnalyticsDatabase_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		verifyResourceAnalyticsDatabase();
	}

	@Test
	public void verifyResourceAnalyticsDatabase_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		verifyResourceAnalyticsDatabase();
	}

	@Test
	public void verifyResourceAnalyticsDatabase_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsDatabase_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Database
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Database");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Database");

		//verify Resource Analytics Database
		verifyResourceAnalyticsDatabase();
	}

	@Test
	public void verifyResourceAnalyticsHost_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		verifyResourceAnalyticsHost();
	}

	@Test
	public void verifyResourceAnalyticsHost_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		verifyResourceAnalyticsHost();
	}

	@Test
	public void verifyResourceAnalyticsHost_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		verifyResourceAnalyticsHost();
	}

	@Test
	public void verifyResourceAnalyticsHost_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsHost_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Host
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Host");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Host");

		//verify esource Analytics Host
		verifyResourceAnalyticsHost();
	}

	@Test
	public void verifyResourceAnalyticsMiddleware_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		verifyResourceAnalyticsMiddleware();
	}

	@Test
	public void verifyResourceAnalyticsMiddleware_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		verifyResourceAnalyticsMiddleware();
	}

	@Test
	public void verifyResourceAnalyticsMiddleware_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		verifyResourceAnalyticsMiddleware();
	}

	@Test
	public void verifyResourceAnalyticsMiddleware_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("start to test in verifyResourceAnalyticsMiddleware_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//open Resource Analytics: Middleware
		webd.getLogger().info("Open the OOB dashboard---Resource Analytics: Middleware");
		DashboardHomeUtil.selectDashboard(webd, "Resource Analytics: Middleware");

		//verify Resource Analytics Middleware
		verifyResourceAnalyticsMiddleware();
	}

	@Test
	public void verifyUIGallery_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_GridView");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		verifyUIGallery();
		verifyUIGallery_Details();
	}

	@Test
	public void verifyUIGallery_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_ListView");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		verifyUIGallery();
	}

	@Test
	public void verifyUIGallery_WithFilter_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_GridView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		verifyUIGallery();
	}	

	@Test
	public void verifyUIGallery_WithFilter_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyUIGallery_WithFilter_ListView");

		//click Filter-Application PerfAnalytics
		webd.getLogger().info("Click Cloud Services - IT Analytics");
		DashboardHomeUtil.filterOptions(webd, "ita");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---UI Gallery
		webd.getLogger().info("Open the OOB dashboard---UI Gallery");
		DashboardHomeUtil.selectDashboard(webd, "UI Gallery");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify UI Gallery
		verifyUIGallery();
	}
	
	@Test
	public void verifyOrchestration_GridView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyOrchestration_GridView");

		//click Filter-orchestration
		webd.getLogger().info("Click Cloud Services - orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click on Grid View
		webd.getLogger().info("Click on Grid View icon");
		DashboardHomeUtil.gridView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Orchestration Workflows
		verifyOrchestration();
	}
	@Test
	public void verifyOrchestration_ListView()
	{
		//initTest
		initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		webd.getLogger().info("Start the test in verifyOrchestration_ListView");

		//click Filter-orchestration
		webd.getLogger().info("Click Cloud Services - orchestration");
		DashboardHomeUtil.filterOptions(webd, "orchestration");

		//click on List View
		webd.getLogger().info("Click on List View icon");
		DashboardHomeUtil.listView(webd);

		//Open the OOB dashboard---Orchestration Workflows
		webd.getLogger().info("Open the OOB dashboard---Orchestration Workflows");
		DashboardHomeUtil.selectDashboard(webd, "Orchestration Workflows");

		((org.openqa.selenium.JavascriptExecutor) webd.getWebDriver()).executeScript("window.operationStack = undefined");

		//verify Orchestration Workflows
		verifyOrchestration();
	}
	private void verifyOrchestration()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcpdfui/builder.html?dashboardId=37");
	
		DashboardBuilderUtil.verifyWidget(webd, "Summary");
		DashboardBuilderUtil.verifyWidget(webd, "Workflow Submission Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Submissions by User");
		DashboardBuilderUtil.verifyWidget(webd, "Submissions by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Histogram of Submissions by Average Failed Steps");
		DashboardBuilderUtil.verifyWidget(webd, "Workflow Submission Details");

		webd.getLogger().info("Verification end...");
	}
	private void verifyAPM()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "apmUi/index.html");
		//verify the APM open correctly
		//TODO

		webd.getLogger().info("Verification end...");
	}

	private void verifyApplicationPerfAnalytics()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/ap-analytics-war/html/ap-perf-analytics.html");

		//verify the dashboard title & time picker
		webd.getLogger().info("Verify the dashboard title");
		webd.isTextPresent("Application Performance Analytics", "css=" + PageId.DASHBOARDTITLE_CSS);
		webd.getLogger().info("Verify the time picker");
		webd.isDisplayed("css=" + PageId.DATETIMEPICKER_OOB_CSS);

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verify end...");
	}

	private void verifyApplicationServers()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Application Servers opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Status");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Application Servers: Top 5 by Alert Count");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Performance Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Average Memory Usage");
		DashboardBuilderUtil.verifyWidget(webd, "CPU Utilization by Application Server Type");
		DashboardBuilderUtil.verifyWidget(webd, "Application Servers Grouped by Web Request Rate");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Log Records");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Application Server Status");
		verifyIconInWidget(webd, "Application Server: Fatal Alerts");
		verifyIconInWidget(webd, "Application Server: Critical Alerts");
		verifyIconInWidget(webd, "Application Server: Warning Alerts");
		verifyIconInWidget(webd, "Application Servers: Top 5 by Alert Count");
		verifyIconInWidget(webd, "Application Server Performance Metrics");
		verifyIconInWidget(webd, "Average Memory Usage");
		verifyIconInWidget(webd, "CPU Utilization by Application Server Type");
		verifyIconInWidget(webd, "Application Servers Grouped by Web Request Rate");
		verifyIconInWidget(webd, "Application Server Log Records");

		webd.getLogger().info("Verification end...");

	}

	private void verifyAvailabilityAnalytics()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/avail-analytics-war/html/avail-analytics-home.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	private void verifyCategorical()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Categorical opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Donut");
		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line - Categorical");
		DashboardBuilderUtil.verifyWidget(webd, "Histogram");
		DashboardBuilderUtil.verifyWidget(webd, "Treemap");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Pareto Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Top N");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Color option");
		DashboardBuilderUtil.verifyWidget(webd, "Bar Chart with Color and Group by option");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Bar Chart");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Donut");
		verifyIconInWidget(webd, "Analytics Line - Categorical");
		verifyIconInWidget(webd, "Histogram");
		verifyIconInWidget(webd, "Treemap");
		verifyIconInWidget(webd, "Bar Chart");
		verifyIconInWidget(webd, "Pareto Chart");
		verifyIconInWidget(webd, "Bar Chart with Top N");
		verifyIconInWidget(webd, "Bar Chart with Color option");
		verifyIconInWidget(webd, "Bar Chart with Color and Group by option");
		verifyIconInWidget(webd, "Stacked Bar Chart");

		webd.getLogger().info("Verification end...");
	}

	private void verifyDatabaseOperations()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=15");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Database Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Database Operations", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Database Operations opened finished");
	}

	private void verifyDatabaseOperations_Details()
	{
		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard -- <Database Operations>");
		DashboardBuilderUtil.verifyWidget(webd, "Database Log Trends");
		DashboardBuilderUtil.verifyWidget(webd, "Database Critical Incidents by Target Type");
		DashboardBuilderUtil.verifyWidget(webd, "Top Database Targets with Log Errors");
		DashboardBuilderUtil.verifyWidget(webd, "Database Top Errors");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Database Log Trends");
		verifyIconInWidget(webd, "Database Critical Incidents by Target Type");
		verifyIconInWidget(webd, "Top Database Targets with Log Errors");
		verifyIconInWidget(webd, "Database Top Errors");

		webd.getLogger().info("Verification end...");
	}

	private void verifyDatabases()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Databases opened correctly");

		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Database Status");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Databases: Top 5 by Alert Count");
		DashboardBuilderUtil.verifyWidget(webd, "Database Performance Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Database Log Trends");
		DashboardBuilderUtil.verifyWidget(webd, "Space Used by Database Type");
		DashboardBuilderUtil.verifyWidget(webd, "Databases Grouped by Transactions");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Database Status");
		verifyIconInWidget(webd, "Databases: Fatal Alerts");
		verifyIconInWidget(webd, "Databases: Critical Alerts");
		verifyIconInWidget(webd, "Databases: Warning Alerts");
		verifyIconInWidget(webd, "Databases: Top 5 by Alert Count");
		verifyIconInWidget(webd, "Database Performance Metrics");
		verifyIconInWidget(webd, "Database Log Trends");
		verifyIconInWidget(webd, "Space Used by Database Type");
		verifyIconInWidget(webd, "Databases Grouped by Transactions");

		webd.getLogger().info("Verification end...");
	}

	private void verifyEnterpriseHealth()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=31");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Enterprise Health opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "Enterprise Health");
	}

	private void verifyEnterpriseHealth_Details()
	{
		webd.getLogger().info("Verify the icon in dashboard set -- <Enterprise Health>");
		verifyIconInOobDashboardSet();

		//verify the dashboards in set
		webd.getLogger().info("Verify the dashboards in set");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Summary");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Hosts");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Databases");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Application Servers");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Entities");

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Summary> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Summary");
		verifySummary();

		webd.getLogger().info("Verify Dashboard <Hosts> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Hosts");
		verifyHosts();

		webd.getLogger().info("Verify Dashboard <Databases> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Databases");
		verifyDatabases();

		webd.getLogger().info("Verify Dashboard <Application Servers> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Application Servers");
		verifyApplicationServers();

		webd.getLogger().info("Verify Dashboard <Entities> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Entities");
		verifyEntities();

		webd.getLogger().info("Verification end...");
	}

	private void verifyEntities()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Entities opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Entities");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Entities");

		webd.getLogger().info("Verification end...");
	}

	private void verifyExadataHealth()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=28");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Exadata Health opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "Exadata Health");
	}

	private void verifyExadataHealth_Details()
	{
		webd.getLogger().info("Verify the icon in dashboard set -- <Exadata Health>");
		verifyIconInOobDashboardSet();

		//verify the dashboards in set
		webd.getLogger().info("Verify the dashboards in set");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Overview");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Performance");

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Overview> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Overview");
		verifyOverview();

		webd.getLogger().info("Verify Dashboard <Performance> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Performance");
		verifyPerformance();

		webd.getLogger().info("Verification end...");
	}

	private void verifyHostOperations()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=16");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Host Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Host Operations", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Host Operations opened finished");
	}

	private void verifyHostOperations_Details()
	{
		//verify all the widgets displayed
		webd.getLogger().info("Verify all the widgets in dashboard -- <Host Operations>");
		DashboardBuilderUtil.verifyWidget(webd, "Host Logs Trend");
		DashboardBuilderUtil.verifyWidget(webd, "Top Host Log Sources");
		DashboardBuilderUtil.verifyWidget(webd, "Top Host Log Entries by Service");
		DashboardBuilderUtil.verifyWidget(webd, "Top SUDO Users");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Host Logs Trend");
		verifyIconInWidget(webd, "Top Host Log Sources");
		verifyIconInWidget(webd, "Top Host Log Entries by Service");
		verifyIconInWidget(webd, "Top SUDO Users");

		webd.getLogger().info("Verification end...");
	}

	private void verifyHosts()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Hosts opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Host Status");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts: Top 5 by Alert Count");
		DashboardBuilderUtil.verifyWidget(webd, "Host Performance Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Hosts Grouped by Memory Utilization");
		DashboardBuilderUtil.verifyWidget(webd, "CPU Utilization by Host Type");
		DashboardBuilderUtil.verifyWidget(webd, "Avg Disk I/O Request Rate");
		DashboardBuilderUtil.verifyWidget(webd, "Host Log Trend");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Host Status");
		verifyIconInWidget(webd, "Hosts: Fatal Alerts");
		verifyIconInWidget(webd, "Hosts: Critical Alerts");
		verifyIconInWidget(webd, "Hosts: Warning Alerts");
		verifyIconInWidget(webd, "Hosts: Top 5 by Alert Count");
		verifyIconInWidget(webd, "Host Performance Metrics");
		verifyIconInWidget(webd, "Hosts Grouped by Memory Utilization");
		verifyIconInWidget(webd, "CPU Utilization by Host Type");
		verifyIconInWidget(webd, "Avg Disk I/O Request Rate");
		verifyIconInWidget(webd, "Host Log Trend");

		webd.getLogger().info("Verification end...");
	}

	private void verifyIconInOobDashboard()
	{
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB Dashboard");

		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PageId.DASHBOARDOPTIONS_CSS)));
		webd.click("css=" + PageId.DASHBOARDOPTIONS_CSS);
		webd.takeScreenShot();

		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + DashBoardPageId.BUILDEROPTIONSEDITLOCATORCSS),
				"Edit menu is displayed in OOB Dashboard");
	}

	private void verifyIconInOobDashboardSet()
	{
		//verify the edit menu & save icon are not displayed in OOB
		webd.getLogger().info("Verify the save icon is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css=" + DashBoardPageId.DASHBOARDSAVECSS), "Save icon is displayed in OOB");

		WebDriverWait wait = new WebDriverWait(webd.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(PageId.DASHBOARDSETOPTIONS_CSS)));
		webd.click("css=" + PageId.DASHBOARDSETOPTIONS_CSS);
		webd.takeScreenShot();

		webd.getLogger().info("Verify the edit menu is not displayed in OOB");
		Assert.assertFalse(webd.isDisplayed("css" + PageId.DASHBOARDSETOPTIONSEDIT_CSS), "Edit menu is displayed in OOB");
	}

	private void verifyIconInWidget(WebDriver driver, String widgetname)
	{
		driver.waitForElementPresent(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.click(DashBoardPageId_190.BUILDERTILESEDITAREA);
		driver.takeScreenShot();

		String titleTitlesLocator = String.format(DashBoardPageId.BUILDERTILETITLELOCATOR, widgetname);
		WebElement tileTitle = driver.getWebDriver().findElement(By.xpath(titleTitlesLocator));

		tileTitle.click();
		driver.takeScreenShot();

		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(tileTitle).perform();
		driver.takeScreenShot();

		//verify the config icon not exist
		Assert.assertFalse(driver.isDisplayed(DashBoardPageId.BUILDERTILECONFIGLOCATOR),
				"widiget configuration icon is displayed");
	}

	private void verifyMiddlewareOperations()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=17");

		//verify the Middleware Operations open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Middleware Operations opened correctly");

		webd.getLogger().info("Verify the dashboard titile...");
		DashboardBuilderUtil.verifyDashboard(webd, "Entities", "", true);

		webd.getLogger().info("Verify the OOB Dashboard - Middleware Operations opened finished");
	}

	private void verifyMiddlewareOperations_Details()
	{
		webd.getLogger().info("Verify all the widgets in dashboard  -- <Middleware Operations>");
		DashboardBuilderUtil.verifyWidget(webd, "Middleware Logs Trend");
		DashboardBuilderUtil.verifyWidget(webd, "Top Middleware Error Codes");
		DashboardBuilderUtil.verifyWidget(webd, "Top Middleware Targets with Errors");
		DashboardBuilderUtil.verifyWidget(webd, "Web Server Top Accessed Pages");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Middleware Logs Trend");
		verifyIconInWidget(webd, "Top Middleware Error Codes");
		verifyIconInWidget(webd, "Top Middleware Targets with Errors");
		verifyIconInWidget(webd, "Web Server Top Accessed Pages");

		webd.getLogger().info("Verification end...");
	}

	private void verifyOthers()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Others opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Label");
		DashboardBuilderUtil.verifyWidget(webd, "Circular Gauge");
		DashboardBuilderUtil.verifyWidget(webd, "Table");
		DashboardBuilderUtil.verifyWidget(webd, "Vertical Gauge");
		DashboardBuilderUtil.verifyWidget(webd, "Horizontal Gauge");
		DashboardBuilderUtil.verifyWidget(webd, "Scatter Chart");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Label");
		verifyIconInWidget(webd, "Circular Gauge");
		verifyIconInWidget(webd, "Table");
		verifyIconInWidget(webd, "Vertical Gauge");
		verifyIconInWidget(webd, "Horizontal Gauge");
		verifyIconInWidget(webd, "Scatter Chart");

		webd.getLogger().info("Verification end...");

	}

	private void verifyOverview()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Overview opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Entities by Database Machine");
		DashboardBuilderUtil.verifyWidget(webd, "Exadata Inventory");
		DashboardBuilderUtil.verifyWidget(webd, "Entity Types in Database Machines");
		DashboardBuilderUtil.verifyWidget(webd, "Exadata Capacity by Disk Type");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Entities by Database Machine");
		verifyIconInWidget(webd, "Exadata Inventory");
		verifyIconInWidget(webd, "Entity Types in Database Machines");
		verifyIconInWidget(webd, "Exadata Capacity by Disk Type");

		webd.getLogger().info("Verification end...");
	}

	private void verifyPerfAnalyticsApplication()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-perf-dashboard.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	private void verifyPerfAnalyticsDatabase()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/db-performance-analytics/html/db-performance-analytics.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	private void verifyPerformance()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Performance opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Read Response Time");
		DashboardBuilderUtil.verifyWidget(webd, "Top 5 Databases by Active Sessions");
		DashboardBuilderUtil.verifyWidget(webd, "Host CPU Utilization and Memory Utilization");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Infiniband Network Performance");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Write Response Time");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server Read/Write Response Times");
		DashboardBuilderUtil.verifyWidget(webd, "Oracle Exadata Storage Server I/O Utilization by DB Machine");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Read Response Time");
		verifyIconInWidget(webd, "Top 5 Databases by Active Sessions");
		verifyIconInWidget(webd, "Host CPU Utilization and Memory Utilization");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Infiniband Network Performance");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Write Response Time");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server Read/Write Response Times");
		verifyIconInWidget(webd, "Oracle Exadata Storage Server I/O Utilization by DB Machine");

		webd.getLogger().info("Verification end...");
	}

	private void verifyResourceAnalyticsDatabase()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/db-analytics-war/html/db-analytics-resource-planner.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");
	}

	private void verifyResourceAnalyticsHost()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");

		//verify the url of opened page
		DashBoardUtils.verifyURL(webd, "emcitas/resource-analytics/html/server-resource-analytics.html");

		//verify all the widgets displayed
		//TODO
		webd.getLogger().info("Verification end...");
	}

	private void verifyResourceAnalyticsMiddleware()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL(webd, "emcitas/mw-analytics-war/html/mw-analytics-resource-planner.html");

		//verify all the widgets displayed
		//TODO

		webd.getLogger().info("Verification end...");

	}

	private void verifySummary()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Summary opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Entity Status");
		DashboardBuilderUtil.verifyWidget(webd, "Entity Count");
		DashboardBuilderUtil.verifyWidget(webd, "Entities: Fatal Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Entities: Critical Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Entities: Warning Alerts");
		DashboardBuilderUtil.verifyWidget(webd, "Host Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Host CPU Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Database Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Database I/O Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Application Server Load Metrics");
		DashboardBuilderUtil.verifyWidget(webd, "Load Balancer Status by Type");
		DashboardBuilderUtil.verifyWidget(webd, "Load Balancer Performance Metrics");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Entity Status");
		verifyIconInWidget(webd, "Entity Count");
		verifyIconInWidget(webd, "Entities: Fatal Alerts");
		verifyIconInWidget(webd, "Entities: Critical Alerts");
		verifyIconInWidget(webd, "Entities: Warning Alerts");
		verifyIconInWidget(webd, "Host Status by Type");
		verifyIconInWidget(webd, "Host CPU Metrics");
		verifyIconInWidget(webd, "Database Status by Type");
		verifyIconInWidget(webd, "Database I/O Metrics");
		verifyIconInWidget(webd, "Application Server Status by Type");
		verifyIconInWidget(webd, "Application Server Load Metrics");
		verifyIconInWidget(webd, "Load Balancer Status by Type");
		verifyIconInWidget(webd, "Load Balancer Performance Metrics");

		webd.getLogger().info("Verification end...");
	}

	private void verifyTimeseries()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - Timeseries opened correctly");

		webd.getLogger().info("Verify all the widgets in dashboard");
		DashboardBuilderUtil.verifyWidget(webd, "Area Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Area Chart");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Shared Y-axis");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Area Chart with Group By");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Color");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Trend and Forecasting");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Line Chart with Group By");
		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line");
		DashboardBuilderUtil.verifyWidget(webd, "Stacked Line with Color and Group by");
		DashboardBuilderUtil.verifyWidget(webd, "Analytics Line with Trend and Forecasting");
		DashboardBuilderUtil.verifyWidget(webd, "Line Chart with Reference Line");

		webd.getLogger().info("Verify the icon in OOB");
		verifyIconInOobDashboard();

		webd.getLogger().info("Verify the icon in widget");
		verifyIconInWidget(webd, "Area Chart");
		verifyIconInWidget(webd, "Line Chart");
		verifyIconInWidget(webd, "Stacked Area Chart");
		verifyIconInWidget(webd, "Line Chart with Shared Y-axis");
		verifyIconInWidget(webd, "Stacked Area Chart with Group By");
		verifyIconInWidget(webd, "Line Chart with Color");
		verifyIconInWidget(webd, "Line Chart with Trend and Forecasting");
		verifyIconInWidget(webd, "Stacked Line Chart with Group By");
		verifyIconInWidget(webd, "Analytics Line");
		verifyIconInWidget(webd, "Stacked Line with Color and Group by");
		verifyIconInWidget(webd, "Analytics Line with Trend and Forecasting");
		verifyIconInWidget(webd, "Line Chart with Reference Line");

		webd.getLogger().info("the verification end...");
	}

	private void verifyUIGallery()
	{
		webd.getLogger().info("Start to verify the OOB Dashboard");
		//verify the current url
		webd.getLogger().info("Verify the current url");
		DashBoardUtils.verifyURL_WithPara(webd, "emcpdfui/builder.html?dashboardId=24");

		//verify the dashboard open correctly
		webd.getLogger().info("Start to verify the OOB Dashboard - UI Gallery opened correctly");

		webd.getLogger().info("Verify the dashboard set titile...");
		DashboardBuilderUtil.verifyDashboardSet(webd, "UI Gallery");
	}

	private void verifyUIGallery_Details()
	{
		webd.getLogger().info("Verify the icon in dashboard set --<UIGallery>");
		verifyIconInOobDashboardSet();

		//verify the dashboards in set
		webd.getLogger().info("Verify the dashboards in set");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Timeseries");
		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Categorical");
		//		DashboardBuilderUtil.verifyDashboardInsideSet(webd, "Others");

		//verify each dashboard
		webd.getLogger().info("Verify Dashboard <Timeseries> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Timeseries");
		verifyTimeseries();

		webd.getLogger().info("Verify Dashboard <Categorical> in set");
		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Categorical");
		verifyCategorical();

		//		webd.getLogger().info("Verify Dashboard <Others> in set");
		//		DashboardBuilderUtil.selectDashboardInsideSet(webd, "Others");
		//		verifyOthers();

		webd.getLogger().info("Verification end...");
	}
}
