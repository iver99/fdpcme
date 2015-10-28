package oracle.sysman.emaas.platform.dashboards.test.ui;

import org.testng.annotations.*;

import oracle.sysman.qatool.uifwk.webdriver.WebDriver;

import oracle.sysman.emaas.platform.dashboards.test.ui.util.DashBoardUtils;
import oracle.sysman.emaas.platform.dashboards.test.ui.util.LoginAndLogout;

/**
 *  @version
 *  @author  charles.c.chen
 *  @since   release specific (what release of product did this appear in)
 */

public class TestWidget extends LoginAndLogout{

	public void initTest(String testName) throws Exception
	{
		login(this.getClass().getName()+"."+testName);
		DashBoardUtils.loadWebDriver(webd);
	}
	
	@Test
	public void testCreateWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testCreateWidget");
		
	}
	@Test
	public void testModifyWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testModifyWidget");
		
	}
	@Test
	public void testRemoveWidget() throws Exception
	{
		this.initTest(Thread.currentThread().getStackTrace()[1].getMethodName());
		
		webd.getLogger().info("start to test in testRemoveWidget");
		
	}
}
