package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.LogUtil.InteractionLogDirection;

public class LogUtilTest
{
	@Test(groups = { "s2" })
	public void testGetInteractionLogger(@Mocked final LogManager anyLogManager)
	{
		new Expectations() {
			{
				String LOGUTIL_INTERACTION_LOG_NAME = Deencapsulation.getField(LogUtil.class, "INTERACTION_LOG_NAME");
				LogManager.getLogger(withEqual(LOGUTIL_INTERACTION_LOG_NAME));
			}
		};
		LogUtil.getInteractionLogger();
	}

	@Test(groups = { "s2" })
	public void testGetLoggerUpdateTimeEmptyPropertiesS2(@Mocked final LogManager anyLogManager,
			@Mocked final LoggerContext anyLoggerContext, @Mocked final Configuration anyConfiguration,
			@Mocked final LoggerConfig anyLoggerConfig)
	{
		new Expectations() {
			{
				LogManager.getContext(withEqual(false));
				result = anyLoggerContext;
				anyLoggerContext.getConfiguration();
				result = anyConfiguration;
				anyConfiguration.getProperties();
				result = null;
			}
		};
		LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		Configuration cfg = loggerContext.getConfiguration();
		Long time = LogUtil.getLoggerUpdateTime(cfg, new LoggerConfig());
		Assert.assertNull(time);
	}

	@Test(groups = { "s2" })
	public void testGetLoggerUpdateTimeEmptyTimeS2(@Mocked final LogManager anyLogManager,
			@Mocked final LoggerContext anyLoggerContext, @Mocked final Configuration anyConfiguration,
			@Mocked final LoggerConfig anyLoggerConfig)
	{
		final String testCfgName = "test config name";
		final Map<String, String> cfgMap = new HashMap<String, String>();
		String LOGGER_PROP_UPDATE_TIME = (String) Deencapsulation.getField(LogUtil.class, "LOGGER_PROP_UPDATE_TIME");
		cfgMap.put(LOGGER_PROP_UPDATE_TIME + testCfgName, null);
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
		Assert.assertNull(time);
	}

	@Test(groups = { "s2" })
	public void testGetLoggerUpdateTimeS2(@Mocked final LogManager anyLogManager, @Mocked final LoggerContext anyLoggerContext,
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
	public void testInteractionLogDirectionFromValue()
	{
		InteractionLogDirection il = InteractionLogDirection.fromValue(null);
		Assert.assertEquals(InteractionLogDirection.NA, il);
		il = InteractionLogDirection.fromValue("OUT");
		Assert.assertEquals(InteractionLogDirection.OUT, il);
	}

	@Test(groups = { "s2" })
	public void testSetLoggerUpdateTimeS2(@Mocked final LoggerContext anyLoggerContext,
			@Mocked final Configuration anyConfiguration, @Mocked final LoggerConfig anyLoggerConfig)
	{
		final String testCfgName = "test config name";
		final Long testTime = System.currentTimeMillis();
		final Map<String, String> cfgMap = new HashMap<String, String>();
		String LOGGER_PROP_UPDATE_TIME = (String) Deencapsulation.getField(LogUtil.class, "LOGGER_PROP_UPDATE_TIME");
		new Expectations() {
			{
				LogManager.getContext(false);
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
		LogUtil.setLoggerUpdateTime(cfg, new LoggerConfig(), testTime);
		Assert.assertEquals(cfgMap.get(LOGGER_PROP_UPDATE_TIME + testCfgName), testTime.toString());

	}

	@Test(groups = { "s2" })
	public void testSetThenClearInteractionLogThreadContextS2()
	{
		String tenantId = "emaastesttenant1";
		String serviceInvoked = "ApmUI";
		InteractionLogDirection direction = InteractionLogDirection.IN;
		LogUtil.setInteractionLogThreadContext(tenantId, serviceInvoked, direction);
		Assert.assertEquals(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_TENANTID), tenantId);
		Assert.assertEquals(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_SERVICE_INVOKED), serviceInvoked);
		Assert.assertEquals(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_DIRECTION), direction.getValue());
		LogUtil.clearInteractionLogContext();
		Assert.assertNull(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_TENANTID));
		Assert.assertNull(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_SERVICE_INVOKED));
		Assert.assertNull(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_DIRECTION));

		LogUtil.setInteractionLogThreadContext(null, null, null);
		Assert.assertNotNull(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_TENANTID));
		Assert.assertNotNull(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_SERVICE_INVOKED));
		Assert.assertNotNull(ThreadContext.get(LogUtil.INTERACTION_LOG_PROP_DIRECTION));
	}
}
