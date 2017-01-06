package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class DefaultKeyGeneratorTest {
    @Test
    public void testKeyGenerate(){
        Tenant tenant=new Tenant("Tenant");
        DefaultKeyGenerator.getInstance().generate(tenant,new Keys("key"));
    }
}
