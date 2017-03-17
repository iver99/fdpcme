/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.tests.ui.impl;

import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.DashBoardPageId;
import oracle.sysman.emaas.platform.dashboards.tests.ui.util.WaitUtil;
import oracle.sysman.qatool.uifwk.webdriver.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author cawei
 */
public class EntitySelectorUtil_1170 extends EntitySelectorUtil_1160
{
        //TODO: keep using this value or use WaitUtil.WAIT_TIMEOUT?
        private static final int UNTIL_TIMEOUT = 900;
        
        @Override
        public void clearContext(WebDriver driver, Logger logger)
        {
                logger.log(Level.INFO, "Remove every pill to clear context.");
                //wait until previous processes are completed
                WaitUtil.waitForPageFullyLoaded(driver);
        
                //Remove all pills from global context bar
                WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), UNTIL_TIMEOUT);
                WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.id(DashBoardPageId.ENTSEL_CLEARALL_BUTTONID)));
                logger.log(Level.INFO, "Click button 'Clear All'");
                element.click();
                //Wait until all the pills are removed
                final WebDriver finalDriver = driver;
                final Logger finalLogger = logger;
                logger.log(Level.INFO, "Waiting until all the pills are removed...");
                wait.until(new ExpectedCondition<Boolean>() {
                    @Override
                    public Boolean apply(org.openqa.selenium.WebDriver webdriver) {
                        return getNumberOfPills(finalDriver, finalLogger) == 0;
                    }
                });
                
                logger.log(Level.INFO, "Global context has been cleared, current pill count: {0}", new Object[] { getNumberOfPills(driver, logger) });
        }
        
        @Override
        public void verifyCompositePillContent(WebDriver driver, Logger logger, String text)
        {
                logger.log(Level.INFO, "Verify Global Context bar contains a composite entity pill with text \"{0}\".", new Object[]{ text });
                driver.waitForElementVisible("xpath=" + MessageFormat.format(DashBoardPageId.ENTSEL_COMPOSITE_PILL_BYTEXT, text));
                logger.log(Level.INFO, "A composite entity pill matching text \"{0}\" was found in Global Context bar.", new Object[]{ text });
        }
        
        @Override
        public void verifyEntityPillContent(WebDriver driver, Logger logger, String text)
        {
                logger.log(Level.INFO, "Verify Global Context bar contains an entity pill with text \"{0}\".", new Object[]{ text });
                driver.waitForElementVisible("xpath=" + MessageFormat.format(DashBoardPageId.ENTSEL_ENTITY_PILL_BYTEXT, text));
                logger.log(Level.INFO, "An entity pill matching text \"{0}\" was found in Global Context bar.", new Object[]{ text });
        }
        
        @Override
        public void verifyPillContains(WebDriver driver, Logger logger, String text)
        {
                logger.log(Level.INFO, "Verify Global Context bar has a pill containing text \"{0}\".", new Object[]{ text });
                driver.waitForElementVisible("xpath=" + MessageFormat.format(DashBoardPageId.ENTSEL_PILL_BYCONTAINSTEXT, text));
                logger.log(Level.INFO, "A pill containing text \"{0}\" was found in Global Context bar.", new Object[]{ text });
        }
        
        @Override
        public void verifyPillContentByIndex(WebDriver driver, Logger logger, int pillIndex, String text)
        {
                logger.log(Level.INFO, "Verify the pill at index [{0}] in the Global Context bar contains text: \"{1}\".", new Object[]{ pillIndex, text });
                //Take in consideration that XPath uses 1-based indexing
                pillIndex++;
                driver.waitForElementVisible("xpath=" + MessageFormat.format(DashBoardPageId.ENTSEL_PILL_CONTAINSTEXT_BYINDEX, pillIndex, text));
                logger.log(Level.INFO, "A pill containing text \"{0}\" was found in Global Context bar at position [{1}].", new Object[]{ text, --pillIndex });
        }
}