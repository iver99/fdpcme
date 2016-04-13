package oracle.sysman.emaas.platform.dashboards.test.ui.util;

import oracle.sysman.emaas.platform.dashboards.tests.ui.DashboardHomeUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class DashBoardUtils
{

	public static void addWidget() throws Exception
	{
		driver.getLogger().info("Select a widget");
		driver.getLogger().info("click Tab key 1");
		WebElement webe = driver.getWebDriver().findElement(By.id(PageId.WidgetSearchInputID));
		if (driver.getWebDriver().switchTo().activeElement().equals(webe)) {
			webe.sendKeys(Keys.TAB);
		}
		driver.takeScreenShot();
		driver.getLogger().info("click Tab key 2");
		WebElement webe1 = driver.getWebDriver().findElement(By.id(PageId.WidgetSearchBtnID));
		if (driver.getWebDriver().switchTo().activeElement().equals(webe1)) {
			webe1.sendKeys(Keys.TAB);
		}
		driver.takeScreenShot();
		driver.getLogger().info("click Enter");

		driver.getWebDriver().switchTo().activeElement().sendKeys(Keys.ENTER);
		driver.takeScreenShot();
		driver.getLogger().info("add the widget into the dashboard");
	}

	public static void APM_OOB_GridView() throws Exception
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Application Performance Monitoring");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Application Performance Monitoring"));
	}

	public static void checkBrandingBarLink() throws Exception
	{
		DashBoardUtils.clickNavigatorLink();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		driver.takeScreenShot();
		//Home link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.HomeLinkID)).getText(), "Home");
		//IT Analytics link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.ITALinkID)).getText(), "IT Analytics");
		//Log Analytics link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.LALinkID)).getText(), "Log Analytics");
		//APM link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.APMLinkID)).getText(), "APM");
		//Log link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.LOGLinkID)).getText(), "Log");
		//AWR Analytics link
		driver.takeScreenShot();
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.AnalyzeLinkID)).getText(), "Analyze");
		//		//Flex link
		//		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(DashBoardPageId.FlexLinkID)).getText(),"AWR");
		//Target link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.SearchLinkID)).getText(), "Search");
		//Customer Software link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.AgentsLinkID)).getText(), "Agents");
		//IT Analytics Administration link
		Assert.assertEquals(driver.getWebDriver().findElement(By.xpath(PageId.ITA_Admin_LinkID)).getText(),
				"IT Analytics Administration");
	}

	public static void clickAddButton() throws Exception
	{
		driver.click(PageId.AddBtn);
	}

	public static void clickAddButton_final() throws Exception
	{
		driver.click(PageId.AddBtn);
	}

	public static void clickCheckBox(String selector) throws Exception
	{
		WebElement el = driver.getWebDriver().findElement(By.id(selector));
		if (!el.isSelected()) {
			el.click();
		}
	}

	public static void clickCloseButton() throws Exception
	{
		driver.click(PageId.closeBtnID);
	}

	public static void clickDashBoard() throws Exception
	{
		driver.click(PageId.DashBoardID);
	}

	public static void clickDashBoardName() throws Exception
	{
		driver.click(PageId.DashBoardName);
	}

	public static void clickDBOnTable(String dbID) throws Exception
	{
		driver.click("//div[@aria-dashboard=" + dbID + "]");
	}

	public static void clickDelete() throws Exception
	{
		driver.getLogger().info("before clicking Delete icon");
		driver.click(PageId.OptionsID);
		driver.click(PageId.DashboardDelete);
		driver.getLogger().info("after clicking Delete icon");
		driver.takeScreenShot();
	}

	public static void clickDeleteButton() throws Exception
	{
		//add verify if we are into deleting dialog
		//Assert.assertEquals("//div/p[text()=Do you want to delete the selected dashboard 'test'?", "Do you want to delete the selected dashboard 'test'?","we are not in delete dialog");
		driver.click(PageId.DeleteBtnID_Dialog);
	}

	public static void clickExploreDataButton() throws Exception
	{
		driver.click(PageId.ExploreDataBtnID);
	}

	public static void clickExploreDataMenuItem(String id) throws Exception
	{
		driver.click(id);
	}

	public static void clickFavorite() throws Exception
	{
		driver.getLogger().info("before clicking set favoirte button");
		driver.click(PageId.OptionsID);
		driver.click(PageId.DashboardFavorite);
		driver.getLogger().info("after clicking set favoirte button");
		driver.takeScreenShot();
	}

	public static void clickGVButton() throws Exception
	{
		driver.click(PageId.GridViewID);
	}

	public static void clickLVButton() throws Exception
	{
		driver.click(PageId.ListViewID);
	}

	public static void clickLVDashBoard() throws Exception
	{
		driver.click(PageId.DashBoardListViewDashBoardID);
	}

	public static void clickLVDeleteButton() throws Exception
	{
		//add verify if we are into deleting dialog
		//Assert.assertEquals("//div/p[text()=Do you want to delete the selected dashboard 'test'?", "Do you want to delete the selected dashboard 'test'?","we are not in delete dialog");
		driver.click(PageId.LV_DeleteBtnID_Dialog);
	}

	public static void clickNavigatorLink() throws Exception
	{
		driver.click(PageId.LinkID);
	}

	public static void clickOKButton() throws Exception
	{
		driver.click(PageId.DashOKButtonID);
	}

	public static void clickRefreshItem() throws Exception
	{
		WebElement Box = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();

		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);

		driver.takeScreenShot();
		WebElement DivisionList1 = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshBy_15_Secs_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList1.click();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		Box = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		WebElement DivisionList2 = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshBy_30_Secs_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList2.click();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		Box = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		WebElement DivisionList3 = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshBy_1_Min_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList3.click();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		Box = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		WebElement DivisionList4 = driver.getWebDriver().findElement(By.xpath(PageId.AutoRefreshBy_15_Mins_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList4.click();

	}

	public static void clickSaveButton() throws Exception
	{
		boolean exist = driver.isElementPresent(PageId.DashBoardSaveID);
		if (!exist) {
			return;
		}
		driver.click(PageId.DashBoardSaveID);
	}

	public static void clickSetHome() throws Exception
	{
		driver.getLogger().info("before clicking set home button");
		driver.click(PageId.OptionsID);
		driver.click(PageId.DashboardHome);
		driver.getLogger().info("after clicking set home button");
		driver.takeScreenShot();
	}

	public static void clickTimePicker() throws Exception
	{
		WebElement Box = driver.getWebDriver().findElement(By.xpath(PageId.TimePickerID));//*[@id='oj-listbox-drop']"));//));
		Box.click();
		driver.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.click(PageId.CustomDateTimeID);
		driver.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.click(PageId.DateID1);
		driver.takeScreenShot();
		//driver.click(DashBoardPageId.DateID2);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.click(PageId.ApplyBtnID);
		driver.takeScreenShot();
	}

	//	public static  void addWidget(int i,String parentHandle,String dbname,String dbdesc) throws Exception
	//	{
	//		WidgetAddPage widgetAddPage;
	//		String widgetName;
	//
	//		driver.getLogger().info("start to test in addWidget");
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		//driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
	//
	//		//driver.getLogger().info("add widget button is found");
	//		//driver.takeScreenShot();
	//		//waitForMilliSeconds(3*DashBoardPageId.Delaytime_long);
	//
	//		driver.getLogger().info("verify dashboard title and description");
	//		driver.getLogger().info(getText(DashBoardPageId.DashboardNameID));
	//		driver.getLogger().info(getText(DashBoardPageId.DashboardDescID));
	//		//verify title and desc of dashboard
	//		/*if( getText(DashBoardPageId.DashboardNameID) == null)
	//		{
	//			Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
	//			Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
	//		}
	//		else*/{
	//			Assert.assertEquals(getText(DashBoardPageId.DashboardNameID),dbname);//"AAA_testDashboard");
	//			Assert.assertEquals(getText(DashBoardPageId.DashboardDescID),dbdesc);//"AAA_testDashBoard desc");
	//		}
	//		driver.getLogger().info("start clicking add widget button");
	//
	//		driver.click(DashBoardPageId.OptionsID);
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		driver.takeScreenShot();
	//		driver.click(DashBoardPageId.WidgetAddButton);
	//		driver.takeScreenShot();
	//
	//		driver.getLogger().info("finish clicking add widget button");
	//		driver.takeScreenShot();
	//		widgetName = WidgetPageId.widgetName;
	//
	//		widgetAddPage = new WidgetAddPage(driver);
	//
	//		//search widget
	//		widgetAddPage.searchWidget(widgetName);
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//
	//		driver.getLogger().info("before clicking widget button");
	//		driver.takeScreenShot();
	//		//select widget
	//		widgetAddPage.clickWidgetOnTable(widgetName);
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		driver.takeScreenShot();
	//		clickAddButton();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		clickCloseButton();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		driver.getLogger().info("before clicking save widget button");
	//		driver.takeScreenShot();
	//		//save dashboard
	//		clickSaveButton();
	//		driver.getLogger().info("after clicking save widget button");
	//		driver.takeScreenShot();
	//
	//	}
	//
	//	public static  void addWidget(int i,String parentHandle,String widgetName,String dbname,String dbdesc) throws Exception
	//	{
	//		WidgetAddPage widgetAddPage,widgetAddPage2;
	//
	//		driver.getLogger().info("start to test in addWidget");
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		//driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
	//
	//		driver.getLogger().info("add widget button is found");
	//		driver.takeScreenShot();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		//verify title and desc of dashboard
	//		/*if( getText(DashBoardPageId.DashboardNameID) == null)
	//		{
	//			Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
	//			Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
	//		}
	//		else{
	//			Assert.assertEquals(getText(DashBoardPageId.DashboardNameID),"AAA_testDashboard");
	//			Assert.assertEquals(getText(DashBoardPageId.DashboardDescID),"AAA_testDashBoard desc");
	//		}*/
	//		//modify name and desc
	//		modifyDashboardInfo(dbname,dbdesc);
	//
	//		driver.takeScreenShot();
	//		driver.click(DashBoardPageId.OptionsID);
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		driver.takeScreenShot();
	//		driver.click(DashBoardPageId.WidgetAddButton);
	//		driver.takeScreenShot();
	//
	//
	//		driver.takeScreenShot();
	//
	//		widgetAddPage = new WidgetAddPage(driver);
	//
	//		//search widget
	//		widgetAddPage.searchWidget(widgetName);
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//
	//
	//		driver.takeScreenShot();
	//		//select widget
	//		widgetAddPage.clickWidgetOnTable(widgetName);
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		driver.takeScreenShot();
	//		clickAddButton();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//
	//		widgetAddPage2 = new WidgetAddPage(driver);
	//		//search widget
	//		widgetAddPage2.searchWidget("Top 10 Listeners by Load");//Database Top Errors");//");
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//
	//
	//		driver.takeScreenShot();
	//		//select widget
	//		widgetAddPage2.clickWidgetOnTable("Top 10 Listeners by Load");//Database Top Errors");//Top 10 Listeners by Load");
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		driver.takeScreenShot();
	//		clickAddButton();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//
	//		clickCloseButton();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//
	//		driver.takeScreenShot();
	//		//save dashboard
	//		clickSaveButton();
	//
	//		driver.takeScreenShot();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		//add autorefresh
	//		//clickRefreshItem();
	//		waitForMilliSeconds(DashBoardPageId.Delaytime_long);
	//		//add time selector
	//		//clickTimePicker();
	//
	//		TileManager tg = new TileManager(driver);
	//
	//	    tg.tileOpen();
	//		tg.tileDelete();
	//		//save dashboard
	//		clickSaveButton();
	//		tg.tileOpen();
	//		tg.tileMaximize();
	//		tg.tileOpen();
	//		tg.tileRestore();
	//		tg.tileOpen();
	//		tg.tileWider();
	//		tg.tileOpen();
	//		tg.tileNarrower();
	//		tg.tileOpen();
	//		tg.tileTaller();
	//		tg.tileOpen();
	//		tg.tileShorter();
	//	}

	public static void clickToSortByLastAccessed() throws Exception
	{
		WebElement Box = driver.getWebDriver().findElement(By.xpath(PageId.SortDropListID));//*[@id='oj-listbox-drop']"));//));
		Box.click();

		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);

		driver.takeScreenShot();
		WebElement DivisionList = driver.getWebDriver().findElement(By.xpath(PageId.Access_Date_ID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		DivisionList.click();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);

		driver.takeScreenShot();
	}

	public static void closeOverviewPage() throws Exception
	{
		driver.getLogger().info("before clicking overview button");
		driver.click(PageId.OverviewCloseID);
		driver.getLogger().info("after clicking overview button");
	}

	public static boolean doesWebElementExist(String selector) throws Exception
	{
		if (driver.isElementPresent(selector) && driver.isDisplayed(selector)) {
			return true;
		}
		else {
			return false;
		}
		//		WebElement el = driver.getWebDriver().findElement(By.id(selector));
		//		//boolean b = driver.isElementPresent(selector);
		//
		//		if (el.isDisplayed()) {
		//			driver.getLogger().info("can get element");
		//			return true;
		//		}
		//		else {
		//			driver.getLogger().info("can not get element");
		//			return false;
		//		}
	}

	public static boolean doesWebElementExistByXPath(String xpath) throws Exception
	{

		WebElement el = driver.getWebDriver().findElement(By.xpath(xpath));

		if (el.isDisplayed()) {
			driver.getLogger().info("xpath:can get element");
			return true;
		}
		else {

			driver.getLogger().info("xpath:can not get element");
			return false;
		}

	}

	public static String getText(String id)
	{
		WebElement we = driver.getWebDriver().findElement(By.xpath(id));
		return we.getText();
	}

	public static String getTextByID(String id)
	{
		WebElement we = driver.getWebDriver().findElement(By.id(id));
		return we.getText();
	}

	public static void inputDashBoardInfo(String dbName, String dbDesc) throws Exception
	{

		driver.sendKeys(PageId.DashBoardNameBoxID, dbName);
		driver.sendKeys(PageId.DashBoardDescBoxID, dbDesc);
	}

	public static void ITA_OOB_GridView() throws Exception
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Health Summary");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Database Health Summary"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Host Health Summary"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Performance Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Performance Analytics: Middleware"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Resource Analytics: Database"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Resource Analytics: Middleware"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "WebLogic Health Summary"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Database Configuration and Storage By Version"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Enterprise Overview"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Host Inventory By Platform"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Top 25 Databases by Resource Consumption"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Top 25 WebLogic Servers by Heap Usage"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Top 25 WebLogic Servers by Load"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "WebLogic Servers by JDK Version"));
	}

	public static void LA_OOB_GridView() throws Exception
	{
		DashboardHomeUtil.gridView(driver);
		DashboardHomeUtil.waitForDashboardPresent(driver, "Database Operations");

		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Database Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Host Operations"));
		Assert.assertTrue(DashboardHomeUtil.isDashboardExists(driver, "Middleware Operations"));
	}

	public static void loadWebDriver(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
		DashBoardUtils.waitForMilliSeconds(10000);
		if (DashBoardUtils.doesWebElementExist(PageId.OverviewCloseID)) {
			DashBoardUtils.closeOverviewPage();
		}
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		Assert.assertFalse(DashBoardUtils.doesWebElementExist(PageId.OverviewCloseID));
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.takeScreenShot();
	}

	public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}

	public static void modifyDashboardInfo(String dbName, String dbDesc) throws Exception
	{

		/*WebElement mainelement = driver.getElement(DashBoardPageId.DashboardNameID);
		WebElement editNamebutton = driver.getElement(DashBoardPageId.NameEditID);
		Actions builder = new Actions(driver.getWebDriver());
		builder.moveToElement(mainelement).moveToElement(editNamebutton).click().perform();
		driver.getElement(DashBoardPageId.NameInputID).clear();
		driver.sendKeys(DashBoardPageId.NameInputID, dbname);//"DBA_Name_Modify");
		driver.click(DashBoardPageId.NameEditOKID);

		mainelement = driver.getElement(DashBoardPageId.DashboardDescID);
		WebElement editDescbutton = driver.getElement(DashBoardPageId.DescEditID);
		builder = new Actions(driver.getWebDriver());
		builder.moveToElement(mainelement).moveToElement(editDescbutton).click().perform();
		driver.getElement(DashBoardPageId.DescInputID).clear();
		driver.sendKeys(DashBoardPageId.DescInputID, dbdesc);//"DBA_DESC_MODIFY");
		driver.click(DashBoardPageId.DescEditOKID);*/

		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.takeScreenShot();
		driver.click(PageId.OptionsID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.takeScreenShot();
		driver.click(PageId.DashboardEdit);
		driver.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.getElement(PageId.DashBoardNameBoxID).clear();
		driver.click(PageId.DashBoardNameBoxID);
		driver.sendKeys(PageId.DashBoardNameBoxID, dbName);
		driver.getElement(PageId.DashBoardDescBoxID).clear();
		driver.click(PageId.DashBoardDescBoxID);
		driver.sendKeys(PageId.DashBoardDescBoxID, dbDesc);
		driver.takeScreenShot();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.click(PageId.DashSaveButtonID);
		driver.takeScreenShot();

	}

	public static void navigateWidget(String parentHandle) throws Exception
	{
		WidgetAddPage widgetAddPage;
		String widgetName;

		driver.getLogger().info("start to test in navigateWidget");
		DashBoardUtils.waitForMilliSeconds(2 * PageId.Delaytime_long);

		//verify title and desc of dashboard
		/*if( getText(DashBoardPageId.DashboardNameID) == null)
		{
			Assert.assertEquals(getText(DashBoardPageId.MDashboardNameID),"AAA_testDashboard");
			Assert.assertEquals(getText(DashBoardPageId.MDashboardDescID),"AAA_testDashBoard desc");
		}
		else*/{
			Assert.assertEquals(DashBoardUtils.getText(PageId.DashboardNameID), "AAA_testDashboard");
			Assert.assertEquals(DashBoardUtils.getText(PageId.DashboardDescID), "AAA_testDashBoard desc modify");
		}

		//driver.waitForElementPresent(DashBoardPageId.WidgetAddButtonID);
		driver.click(PageId.OptionsID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_long);
		driver.takeScreenShot();
		driver.click(PageId.WidgetAddButton);
		driver.takeScreenShot();

		driver.getLogger().info("before select");
		WebElement Box = driver.getWebDriver().findElement(By.xpath(WidgetPageId.dropListID));//*[@id='oj-listbox-drop']"));//));
		Box.click();

		DashBoardUtils.waitForMilliSeconds(2 * PageId.Delaytime_long);

		driver.takeScreenShot();
		//WebElement DivisionList = driver.getWebDriver().findElement(By.xpath(WidgetPageId.LAListID));//*[contains(@id,'oj-listbox-result-label')]")); //and contains(text(),'Last Accessed')]"));
		//DivisionList.click();
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		//get text and grab number,then determine how many pages we should navigate
		WebElement leftButton = driver.getWebDriver().findElement(By.xpath(WidgetPageId.leftNavigatorBtnID));
		WebElement rightButton = driver.getWebDriver().findElement(By.xpath(WidgetPageId.rightNavigatorBtnID));
		driver.getLogger().info("after select");

		while (rightButton.isEnabled()) {
			rightButton.click();
		}
		driver.getLogger().info("after right btn");
		Assert.assertFalse(rightButton.isEnabled());
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		Assert.assertTrue(leftButton.isEnabled());
		driver.getLogger().info("after enabled 1");
		//navigate to left
		while (leftButton.isEnabled()) {
			leftButton.click();
		}
		driver.getLogger().info("after left btn");
		Assert.assertFalse(leftButton.isEnabled());
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		Assert.assertTrue(rightButton.isEnabled());
		driver.getLogger().info("after enabled 2");
		DashBoardUtils.clickCloseButton();
		DashBoardUtils.waitForMilliSeconds(2 * PageId.Delaytime_long);
	}

	public static void noOOBCheck_GridView() throws Exception
	{
		//verify all the oob dashboard not exsit
		driver.getLogger().info("verify all the oob dashboard not exsit");
		Assert.assertFalse(driver.isElementPresent(PageId.Application_Performance_Monitoring_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Health_Summary_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Host_Health_Summary_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Performance_Analytics_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Middleware_Performance_Analytics_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Resource_Analytics_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Middleware_Resource_Analytics_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.WebLogic_Health_Summary_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Configuration_and_Storage_By_Version_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Enterprise_OverView_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Host_Inventory_By_Platform_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Top_25_Databases_by_Resource_Consumption_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Top_25_WebLogic_Servers_by_Heap_Usage_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Top_25_WebLogic_Servers_by_Load_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.WebLogic_Servers_by_JDK_Version_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Database_Operations_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Host_Operations_ID));
		Assert.assertFalse(driver.isElementPresent(PageId.Middleware_Operations_ID));
	}

	public static void openDBCreatePage() throws Exception
	{
		//TODO: Add page check

		if (driver == null) {
			throw new Exception("The WebDriver variable has not been initialized,please initialize it first");
		}
		driver.click(PageId.CreateDSButtonID);
	}

	public static void resetCheckBox(String selector) throws Exception
	{
		WebElement el = driver.getWebDriver().findElement(By.id(selector));
		if (el.isSelected()) {
			el.click();
		}
	}

	public static void saveWidget() throws Exception
	{
		DashBoardUtils.clickSaveButton();
	}

	public static void searchDashBoard(WebDriver wdriver, String board) throws Exception
	{
		wdriver.takeScreenShot();
		wdriver.getLogger().info("go into search DashBoard");
		wdriver.getElement(PageId.SearchDSBoxID).clear();
		wdriver.click(PageId.SearchDSBoxID);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		wdriver.getLogger().info("search dashboard: " + board);
		wdriver.sendKeys(PageId.SearchDSBoxID, board);
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		wdriver.click("/html/body/div[*]/div/div[1]/div/div/div[2]/div[1]/span[1]/button[2]");
		DashBoardUtils.waitForMilliSeconds(PageId.Delaytime_short);
		wdriver.takeScreenShot();

	}

	public static void searchWidget(String widgetName) throws Exception
	{
		driver.getLogger().info("Search a widget");
		driver.getElement(PageId.WidgetSearchInputID).clear();
		driver.click(PageId.WidgetSearchInputID);
		driver.sendKeys(PageId.WidgetSearchInputID, widgetName);
		DashBoardUtils.waitForMilliSeconds(2 * PageId.Delaytime_short);
		driver.takeScreenShot();
	}

	//Sharing and stopping dashbaord
	public static void sharedashboard() throws Exception
	{
		driver.click(PageId.option);
		driver.click(PageId.dashboardshare);
	}

	public static void sharestopping() throws Exception
	{
		driver.click(PageId.option);
		driver.click(PageId.stopshare_btn);
	}

	public static void waitForMilliSeconds(long millisSec) throws Exception
	{
		Thread.sleep(millisSec);
	}

	private static WebDriver driver;

}
