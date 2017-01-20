package oracle.sysman.emaas.platform.dashboards.comparator.webutils.services;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import org.testng.annotations.Test;

import javax.management.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by chehao on 2017/1/12.
 */
@Test(groups = {"s2"})
public class RegistryServiceManagerTest {


    /*@Test
    public void testGetApplicationUrl(){

        Deencapsulation.invoke(RegistryServiceManager.class,"");
    }*/

    @Mocked
    private InitialContext context;
    @Mocked
    private MBeanServer server;
    @Mocked
    private ObjectName objectName;
    @Test
    public void testRegisterService() throws NamingException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {
        RegistryServiceManager manager=new RegistryServiceManager();
        final String str="result";
        new Expectations(){
            {
                context.lookup("java:comp/jmx/runtime");
                result=server;
                server.getAttribute((ObjectName)any, "ServerRuntime");
                result=objectName;
                server.invoke(objectName, "getURL", (Object[])any,(String[])any);
                result=str;
            }
        };

        manager.registerService();
    }
}
