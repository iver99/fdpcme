package oracle.sysman.emaas.platform.dashboards.core.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Deencapsulation;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Mocked;

public class LogUtilTest
{
	@Test(groups = { "s2" })
	public void testGetLoggerUpdateTime_S2(@Mocked final LogManager anyLogManager, @Mocked final LoggerContext anyLoggerContext,
			@Mocked final Configuration anyConfiguration, @Mocked final LoggerConfig anyLoggerConfig)
	{
		final String testCfgName = "test config name";
		final Long testTime = System.currentTimeMillis();
		final Map<String, String> cfgMap = new HashMap<String, String>();
		String LOGGER_PROP_UPDATE_TIME = (String) Deencapsulation.getField(LogUtil.class, "LOGGER_PROP_UPDATE_TIME");
		cfgMap.put(LOGGER_PROP_UPDATE_TIME + testCfgName, testTime.toString());
		new Expectations() {
			{
				LogManager.getContext(withEqual(false));
				result = anyLoggerContext;
				anyLoggerContext.getConfiguration();
				result = anyConfiguration;
				anyConfiguration.getProperties();
				result = cfgMap;
				anyLoggerConfig.getName();
				result = testCfgName;
			}
		};
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		Configuration cfg = loggerContext.getConfiguration();
		Long time = LogUtil.getLoggerUpdateTime(cfg, new LoggerConfig());
		Assert.assertEquals(time, testTime);
	}

	@Test(groups = { "s2" })
	public void testInitializeLoggersUpdateTime_S2(@Mocked final LogManager anyLogManager,
			@Mocked final LoggerContext anyLoggerContext, @Mocked final Configuration anyConfiguration,
			@Mocked final LoggerConfig anyLoggerConfig)
	{
		new Expectations(LogUtil.class) {
			{
				LogManager.getContext(false);
				anyLoggerContext.getConfiguration();
				result = anyConfiguration;
				anyConfiguration.getLoggers();
				result = new Delegate<Map<String, LoggerConfig>>() {
					@SuppressWarnings("unused")
					Map<String, LoggerConfig> getLoggers()
					{
						Map<String, LoggerConfig> map = new HashMap<String, LoggerConfig>();
						map.put("test logger 1", anyLoggerConfig);
						map.put("test logger 2", anyLoggerConfig);
						return map;
					}
				};
				LogUtil.setLoggerUpdateTime(anyConfiguration, anyLoggerConfig, anyLong);
				times = 2;
				anyLoggerContext.updateLoggers();
				times = 2;
			}
		};
		LogUtil.initializeLoggersUpdateTime();
	}
}
