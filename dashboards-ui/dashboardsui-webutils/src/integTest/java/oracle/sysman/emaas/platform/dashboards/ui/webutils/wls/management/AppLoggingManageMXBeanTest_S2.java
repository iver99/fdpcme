package oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.management;

import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.core.config.Configurator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class AppLoggingManageMXBeanTest_S2
{
	@BeforeClass
	public void beforeClass() throws URISyntaxException
	{
		URL url = AppLoggingManageMXBeanTest_S2.class.getResource(
				"/oracle/sysman/emaas/platform/dashboards/ui/webutils/wls/management/log4j2_dsbui_logging_unittest.xml");
		Configurator.initialize("root", AppLoggingManageMXBeanTest_S2.class.getClassLoader(), url.toURI());
	}

	@Test(groups = { "s2" })
	public void testGetLogLevels()
	{
		AppLoggingManageMXBean almmxb = new AppLoggingManageMXBean();
		String levels = almmxb.getLogLevels();
		Assert.assertTrue(levels.contains("\"oracle.sysman.emaas.platform.dashboards.ui\":\"INFO\""));
	}

	@Test(groups = { "s2" })
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
