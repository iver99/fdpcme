package oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.management;


import mockit.Expectations;
import mockit.Mocked;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author guobaochen
 */
@Test(groups = { "s2" })
public class AppLoggingManageMXBeanTest_S2
{
	private AppLoggingManageMXBean appLoggingManageMXBean = new AppLoggingManageMXBean();
	@Mocked
	LoggerContext loggerContext;
	@Mocked
	LogManager logManager;
	@Mocked
	LoggerConfig loggerConfig;
	@Mocked
	Configuration configuration;
	@Test
	public void testGetLogLevels(){
		final Map<String, LoggerConfig> stringLoggerConfigMap = new HashMap<>();
		stringLoggerConfigMap.put("1", loggerConfig);
		new Expectations(){
			{
				LogManager.getContext(false);
				result = loggerContext;
				configuration.getLoggers();
				result = stringLoggerConfigMap;
			}
		};
		appLoggingManageMXBean.getLogLevels();
	}

	@Test
	public void testSetLogLevel(){
		appLoggingManageMXBean.setLogLevel("web", "DEBUG");
		appLoggingManageMXBean.setLogLevel("web", "INFO");
		appLoggingManageMXBean.setLogLevel("web", "WARN");
		appLoggingManageMXBean.setLogLevel("web", "ERROR");
		appLoggingManageMXBean.setLogLevel("web", "FATAL");
	}
}

