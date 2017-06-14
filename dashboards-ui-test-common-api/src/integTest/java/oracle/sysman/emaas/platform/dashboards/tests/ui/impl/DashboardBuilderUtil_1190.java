package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import oracle.sysman.emaas.platform.dashboards.tests.ui.util.*;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DashboardBuilderUtil_1190 extends DashboardBuilderUtil_1150
{
	@Override
	public boolean verifyDashboard(WebDriver driver, String dashboardName, String description, boolean showTimeSelector) {
		driver.getLogger().info(
				"DashboardBuilderUtil.verifyDashboard started for name=\"" + dashboardName + "\", description=\"" + description
						+ "\", showTimeSelector=\"" + showTimeSelector + "\"");
		Validator.notEmptyString("dashboardName", dashboardName);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), WaitUtil.WAIT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR)));
		WaitUtil.waitForPageFullyLoaded(driver);

		driver.waitForElementPresent(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		driver.click(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR);
		
		String realName = driver.getElement(DashBoardPageId_190.BUILDERNAMETEXTLOCATOR).getAttribute("title");
		if (!dashboardName.equals(realName)) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected dashboard name is "
							+ dashboardName + ", actual dashboard name is " + realName);
			return false;
		}

		if (driver.isElementPresent(DashBoardPageId_1120.BUILDERDESCRIPTIONTEXTLOCATOR)) {
			String realDesc = driver.getElement(DashBoardPageId_1120.BUILDERDESCRIPTIONTEXTLOCATOR).getAttribute("title");
			if (description == null || "".equals(description)) {
				if (realDesc != null && !"".equals(realDesc.trim())) {
					driver.getLogger().info(
							"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
									+ description + ", actual dashboard description is " + realDesc);
					return false;
				}
			} else {
				if (!description.equals(realDesc)) {
					driver.getLogger().info(
							"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected description is "
									+ description + ", actual dashboard description is " + realDesc);
					return false;
				}
			}
		}else{
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard: description is disabled.");
		}

		boolean actualTimeSelectorShown = driver.isDisplayed(DashBoardPageId_1150.BUILDERDATETIMEPICKERLOCATOR);
		if (actualTimeSelectorShown != showTimeSelector) {
			driver.getLogger().info(
					"DashboardBuilderUtil.verifyDashboard compelted and returns false. Expected showTimeSelector is "
							+ showTimeSelector + ", actual dashboard showTimeSelector is " + actualTimeSelectorShown);
			return false;
		}

		driver.getLogger().info("DashboardBuilderUtil.verifyDashboard compelted and returns true");
		return true;
	}
}
