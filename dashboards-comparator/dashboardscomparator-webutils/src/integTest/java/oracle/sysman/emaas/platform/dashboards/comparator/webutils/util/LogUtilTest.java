package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class LogUtilTest {

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
        LogUtil.InteractionLogDirection il = LogUtil.InteractionLogDirection.fromValue(null);
        Assert.assertEquals(LogUtil.InteractionLogDirection.NA, il);
        il = LogUtil.InteractionLogDirection.fromValue("OUT");
        Assert.assertEquals(LogUtil.InteractionLogDirection.OUT, il);
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
    public void testSetThenClearInteractionLogThreadContextS2() {
        LogUtil.clearInteractionLogContext();

        LogUtil.setInteractionLogThreadContext(null, null, null);
    }

    @Test
    public void testLogUtil(){
        Assert.assertEquals(LogUtil.InteractionLogDirection.IN,LogUtil.InteractionLogDirection.fromValue("IN"));

        Assert.assertEquals(LogUtil.InteractionLogDirection.OUT,LogUtil.InteractionLogDirection.fromValue("OUT"));

        Assert.assertEquals(LogUtil.InteractionLogDirection.NA,LogUtil.InteractionLogDirection.fromValue("N/A"));

    }

    @Test
    public void testClearInteractionLogContext(){
        LogUtil.clearInteractionLogContext();
    }

    @Test
    public void testGetInteractionLogger(){
        LogUtil.getInteractionLogger();
    }
    @Test
    public void testSetLoggerUpdateTime(){
        LogUtil.setInteractionLogThreadContext("tenantId","Service",LogUtil.InteractionLogDirection.IN);

        LogUtil.setInteractionLogThreadContext("","Service",LogUtil.InteractionLogDirection.IN);

        LogUtil.setInteractionLogThreadContext("tenantId","",LogUtil.InteractionLogDirection.IN);

        LogUtil.setInteractionLogThreadContext("tenantId","Service",null);
    }

    /*@Test
    public void testGetLoggerUpdateTime() throws IOException {
        byte[] data={};
        InputStream in =new FileInputStream("LogUtilTest.java");
        LogUtil.getLoggerUpdateTime(new AbstractConfiguration(new ConfigurationSource(in)) {
            @Override
            public ConfigurationSource getConfigurationSource() {
                return super.getConfigurationSource();
            }
        },new LoggerConfig());
    }*/

}
