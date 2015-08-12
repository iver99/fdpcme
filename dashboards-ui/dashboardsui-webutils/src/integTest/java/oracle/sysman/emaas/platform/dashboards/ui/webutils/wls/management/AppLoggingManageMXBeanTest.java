package oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.management;

import java.net.URISyntaxException;
import java.net.URL;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.services.LoggingServiceManager;

import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class AppLoggingManageMXBeanTest
{
	@BeforeMethod
	public void beforeMethod() throws URISyntaxException
	{
		URL url = LoggingServiceManager.class.getResource("/log4j2_dsbui.xml");
		Configurator.initialize("root", LoggingServiceManager.class.getClassLoader(), url.toURI());
	}

	@Test
	public void testGetLogLevels()
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.ui\":\"INFO\""));
	}

	@Test
	public void testSetLogLevels()
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.ui", "DEBUG");
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.ui\":\"DEBUG\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.ui", "INFO");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.ui\":\"INFO\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.ui", "WARN");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.ui\":\"WARN\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.ui", "ERROR");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.ui\":\"ERROR\""));

		almmxb.setLogLevel("oracle.sysman.emaas.platform.dashboards.ui", "FATAL");
		levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.ui\":\"FATAL\""));
	}
}
