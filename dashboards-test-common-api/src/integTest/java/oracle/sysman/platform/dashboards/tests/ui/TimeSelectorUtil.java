package oracle.sysman.emaas.platform.dashboards.tests.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.sysman.emsaas.login.LoginUtils;
import oracle.sysman.emsaas.login.PageUtils;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import oracle.sysman.qatool.uifwk.webdriver.WebDriverUtils;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.Keys;

import  oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;

import org.testng.Assert;

public class TimeSelectorUtil {

	private static WebDriver driver;
	
	private static void clickTimePicker(WebDriver webd, int Index) throws Exception
	{
		Thread.sleep(5000);
    	//click the datetimepicker component		
    	webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).get(Index).click();
    	webd.takeScreenShot(); 
	}
	
	private static void clickTimePicker(WebDriver webd) throws Exception
	{
		Thread.sleep(5000);
    	//click the datetimepicker component		
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRangeBtn)).click();
    	webd.takeScreenShot(); 
	}

    public static void loadWebDriverOnly(WebDriver webDriver) throws Exception
	{
		driver = webDriver;
	}
    
    public static String setTimeRange(WebDriver webd, int Index, String options) throws Exception
    {
    	//click the datetimepicker component
    	clickTimePicker(webd, Index);
    	 					
       	//select the option
    	switch(options)
    	{
    		case TimeSelectorTimeRange.LAST_15_MINS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_15Min)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_30_MINS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_30Min)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_60_MINS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_60Min)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_4_HOURS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_4Hour)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_6_HOURS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_6Hour)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_1_DAY:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_1Day)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_7_DAYS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_7Days)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_30_DAYS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_30Days)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_90_DAYS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_90Days)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_1_YEAR:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_1Year)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LATEST:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_Latest)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.CUSTOM:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_Custom)).click();
    			webd.takeScreenShot();
    			break;
    	}
    	
    	return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sDisplayDateTime)).get(Index).getText();    	
    }
    
    public static String setTimeRange(WebDriver webd, String options) throws Exception
    {
    	//click the datetimepicker component
    	clickTimePicker(webd);   	
    	 					
    	//select the option
    	switch(options)
    	{
    		case TimeSelectorTimeRange.LAST_15_MINS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_15Min)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_30_MINS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_30Min)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_60_MINS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_60Min)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_4_HOURS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_4Hour)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_6_HOURS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_6Hour)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_1_DAY:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_1Day)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_7_DAYS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_7Days)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_30_DAYS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_30Days)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_90_DAYS:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_90Days)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LAST_1_YEAR:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_1Year)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.LATEST:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_Latest)).click();
    			webd.takeScreenShot();
    			break;
    		case TimeSelectorTimeRange.CUSTOM:
    			webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeRange_Custom)).click();
    			webd.takeScreenShot();
    			break;
    	}
    	
    	Thread.sleep(5000);
    	
    	return webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sDisplayDateTime)).getText();    	
    }
   
    //Date MM/dd/yyyy
    //Time hh:mm a
    public static void setCustomTime(WebDriver webd, String startDate,String startTime, String endDate,String endTime) throws Exception
    {
    	Thread.sleep(5000);
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sStartDateInput)).clear();
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sStartDateInput)).sendKeys(startDate);
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).clear();
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sStartTimeInput)).sendKeys(startTime);
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sEndDateInput)).clear();
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sEndDateInput)).sendKeys(endDate);
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).clear();
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sEndTimeInput)).sendKeys(endTime);
		webd.takeScreenShot();
    	
    }
    
    public static String setExcludeHour(WebDriver webd, int Index, String hours) throws Exception
    {
     	//click the datetimepicker component
    	clickTimePicker(webd, Index);
    	
    	//select time range as Custom
    	setTimeRange(webd, Index, TimeSelectorTimeRange.CUSTOM);
    	
    	//click time filter icon
    	webd.getLogger().info("Click Time Filter Icon");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
		webd.takeScreenShot();	    	
    	
    	//set exclude hours
		webd.getLogger().info("Enter excluded time");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).clear();
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).sendKeys(hours);
		webd.takeScreenShot();	
		//click apply button
		webd.getLogger().info("Click Apply button");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sApplyBtn)).click();
		webd.takeScreenShot();
		
		return webd.getWebDriver().findElements(By.cssSelector(TimeSelectorUIControls.sFilterInfo)).get(Index).getText();     	
    }
    
    public static String setExcludeHour(WebDriver webd, String hours) throws Exception
    {
     	//click the datetimepicker component
    	clickTimePicker(webd);
    	
    	//select time range as Custom
    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
    	
    	//click time filter icon
    	webd.getLogger().info("Click Time Filter Icon");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
		webd.takeScreenShot();	    	
    	
    	//set exclude hours
		webd.getLogger().info("Enter excluded time");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).clear();
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeExcludedInput)).sendKeys(hours);
		webd.takeScreenShot();	
		//click apply button
		webd.getLogger().info("Click Apply button");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sApplyBtn)).click();
		webd.takeScreenShot();
		
		return webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sFilterInfo)).getText();     	
    }
    
    public static void setExcludeDay(WebDriver webd, String ExcludeDays) throws Exception
    {
    	//click the datetimepicker component
    	clickTimePicker(webd);
    	
    	//select time range as Custom
    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
    	
    	//click time filter icon
    	webd.getLogger().info("Click Time Filter Icon");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
		webd.takeScreenShot();
		
		//select excluded days
		WebElement webe =webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sDayMonday));
		if(webe.isSelected()){
			webe.click();
		};
		webd.takeScreenShot();    	
    }
    
    public static void setExcludeMonth(WebDriver webd, String ExcludeMonths) throws Exception
    {
    	//click the datetimepicker component
    	clickTimePicker(webd);
    	
    	//select time range as Custom
    	setTimeRange(webd, TimeSelectorTimeRange.CUSTOM);
    	
    	//click time filter icon
    	webd.getLogger().info("Click Time Filter Icon");
		webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sTimeFilterIcon)).click();
		webd.takeScreenShot();
		
		//select excluded months
		WebElement webe =webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sDayMonday));
		if(webe.isSelected()){
			webe.click();
		};
		webd.takeScreenShot();    	
    }
    
    public static void clickApplyButton(WebDriver webd)
    {
    	//click Apply button
    	webd.getLogger().info("Click Cancel button");
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sApplyBtn)).click();
    	webd.takeScreenShot();
    }
        
    public static void clickCancleButton(WebDriver webd)
    {
    	//click Cancel button
    	webd.getLogger().info("Click Cancel button");
    	webd.getWebDriver().findElement(By.cssSelector(TimeSelectorUIControls.sCancelBtn)).click();
    	webd.takeScreenShot();
    }
	
}
