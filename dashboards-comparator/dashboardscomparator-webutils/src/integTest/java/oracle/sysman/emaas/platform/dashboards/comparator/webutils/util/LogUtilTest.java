package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class LogUtilTest {

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
