package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by chehao on 2017/1/6.
 */
@Test(groups = { "s2" })
public class DefaultKeyGeneratorTest {
    @Test
    public void testKeyGenerate(){
        Tenant tenant1=new Tenant("Tenant1");
        Tenant tenant1_1=new Tenant("Tenant1");
        Tenant tenant2=new Tenant("Tenant2");
        Object key1=DefaultKeyGenerator.getInstance().generate(tenant1,new Keys("key1"));
        Assert.assertNotNull(key1);
        Object key2=DefaultKeyGenerator.getInstance().generate(tenant1,new Keys("key2"));
        Assert.assertNotEquals(key1,key2);
        Object key3=DefaultKeyGenerator.getInstance().generate(tenant2,new Keys("key1"));
        Assert.assertNotEquals(key1,key3);
        Object key4=DefaultKeyGenerator.getInstance().generate(tenant1_1,new Keys("key1"));
        Assert.assertEquals(key1,key4);
    }
}
