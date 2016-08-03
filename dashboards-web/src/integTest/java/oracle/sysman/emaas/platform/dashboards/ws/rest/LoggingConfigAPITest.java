package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.*;
import oracle.sysman.emaas.platform.dashboards.core.util.BigIntegerSerializer;
import oracle.sysman.emaas.platform.dashboards.core.util.JsonUtil;
import oracle.sysman.emaas.platform.dashboards.ws.ErrorEntity;
import oracle.sysman.emaas.platform.dashboards.ws.rest.loggingconfig.UpdatedLoggerLevel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = {"s2"})
public class LoggingConfigAPITest {

    @Mocked
    APIBase apiBase;

    LoggingConfigAPI loggingConfigAPI;

    @BeforeMethod
    public void setUp() throws Exception {
        loggingConfigAPI = new LoggingConfigAPI();
        new NonStrictExpectations() {
            {
                apiBase.buildErrorResponse((ErrorEntity) any);
                result = null;
            }
        };
    }

    @Test
    public void testChangeSpecificLoggerLevelwithDashboardException() throws Exception {
        Assert.assertNull(loggingConfigAPI.changeSpecificLoggerLevel("tenantIdParam", "userTenant", "", new JSONObject()));
    }

    @Test
    public void testChangeSpecificLoggerLevelwithIOException() throws Exception {

        new MockUp<JsonUtil>() {
            @Mock
            public <T> T fromJson(String jsonString, Class<T> clazz) throws IOException{
                throw new IOException("jsonUtil - io exception");
            }
        };

        Assert.assertNull(loggingConfigAPI.changeSpecificLoggerLevel("tenantIdParam", "userTenant", "", new JSONObject()));
    }

    @Test
    public void testChangeSpecificLoggerLevel(@Mocked final Level level, @Mocked final DefaultConfiguration configuration) throws Exception {

        final String loggerName = "loggerName";
        new Expectations() {
            {

                UpdatedLoggerLevel updatedLoggerLevel  = new UpdatedLoggerLevel();
                updatedLoggerLevel.setLevel("level");

                apiBase.getJsonUtil().fromJson(anyString, UpdatedLoggerLevel.class);
                result = updatedLoggerLevel;

                Level.getLevel((String)any);
                result = level;


                Map<String,LoggerConfig> loggers = new HashMap<>();
                loggers.put(loggerName,new LoggerConfig(loggerName, level, true));

                configuration.getLoggers();
                result = loggers;

                configuration.getProperties();
                result  = new HashMap<String,String>();

            }
        };
        Assert.assertNotNull(loggingConfigAPI.changeSpecificLoggerLevel("tenantIdParam", "userTenant", loggerName, new JSONObject()));
    }

    @Test
    public void testChangeRootLoggerLevel() throws Exception {
        loggingConfigAPI.changeRootLoggerLevel("tenantIdParam", "userTenant",new JSONObject());
    }


    @Test
    public void testGetAllLoggerLevels(@Mocked final Level level, @Mocked final DefaultConfiguration configuration) throws Exception {
        final String loggerName = "loggerName";
        new Expectations() {
            {

                Map<String,LoggerConfig> loggers = new HashMap<>();
                loggers.put(loggerName,new LoggerConfig(loggerName, level, true));

                configuration.getLoggers();
                result = loggers;

                configuration.getProperties();
                result  = new HashMap<String,String>();

            }
        };
        Assert.assertNotNull(loggingConfigAPI.getAllLoggerLevels("tenantIdParam","userTenant"));

        Assert.assertNotNull(loggingConfigAPI.getAllLoggerLevels(null,"userTenant"));
    }
}