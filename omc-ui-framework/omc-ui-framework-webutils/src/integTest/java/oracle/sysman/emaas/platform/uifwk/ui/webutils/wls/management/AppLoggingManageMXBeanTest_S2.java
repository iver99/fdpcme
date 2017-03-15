package oracle.sysman.emaas.platform.uifwk.ui.webutils.wls.management;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author guochen
 */
@Test(groups = { "s2" })
public class AppLoggingManageMXBeanTest_S2
{
	private URI oldUri;

	@AfterMethod
	public void afterMethod() throws URISyntaxException
	{
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		context.setConfigLocation(oldUri);
	}

	@BeforeMethod
	public void beforeMethod() throws URISyntaxException
	{
		LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
		oldUri = context.getConfigLocation();
		URL url = AppLoggingManageMXBeanTest_S2.class.getResource(
				"/log4j2-test.xml");
		Configurator.initialize("root", AppLoggingManageMXBeanTest_S2.class.getClassLoader(), url.toURI());
	}

	@Test
	public void testGetLogLevels()
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.uifwk.ui\":\"FATAL\""));
	}

	@Test
	public void testSetLogLevels()
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		almmxb.setLogLevel("oracle.sysman.emaas.platform.uifwk.ui", "DEBUG");
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.uifwk.ui\":\"DEBUG\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.uifwk.ui", "WARN");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.uifwk.ui\":\"WARN\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.uifwk.ui", "ERROR");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.uifwk.ui\":\"ERROR\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.uifwk.ui", "FATAL");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.uifwk.ui\":\"FATAL\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.uifwk.ui", "INFO");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.uifwk.ui\":\"INFO\""));
	}
}