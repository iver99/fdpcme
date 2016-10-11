package oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition;

import mockit.Mocked;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @author jishshi
 * @since 1/18/2016.
 */

@Test(groups = {"s1"})
public class TenantDetailEntityTest {

    TenantDetailEntity tenantDetailEntity;

    @BeforeClass
    public  void setUp() {
        tenantDetailEntity = new TenantDetailEntity();
    }

    @Test
    public void testGetName() {
        Assert.assertNull(tenantDetailEntity.getName());

        tenantDetailEntity.setName("name");
        Assert.assertEquals(tenantDetailEntity.getName(),"name");

        tenantDetailEntity.setStatus(null);
        Assert.assertNull(tenantDetailEntity.getStatus());
    }

    @Test
    public void testGetServices(@Mocked List<ServiceEntity> serviceEntityList) {
        Assert.assertNull(tenantDetailEntity.getServices());

        tenantDetailEntity.setServices(serviceEntityList);
        Assert.assertSame(tenantDetailEntity.getServices(),serviceEntityList);

        tenantDetailEntity.setServices(null);
        Assert.assertNull(tenantDetailEntity.getServices());
    }

    @Test
    public void testGetStatus() {
        Assert.assertNull(tenantDetailEntity.getStatus());

        tenantDetailEntity.setStatus("status");
        Assert.assertEquals(tenantDetailEntity.getStatus(),"status");

        tenantDetailEntity.setStatus(null);
        Assert.assertNull(tenantDetailEntity.getStatus());
    }

    @Test
    public void testGetTenantId() {
        Assert.assertNull(tenantDetailEntity.getTenantId());

        tenantDetailEntity.setTenantId("tenantId");
        Assert.assertEquals(tenantDetailEntity.getTenantId(),"tenantId");

        tenantDetailEntity.setTenantId(null);
        Assert.assertNull(tenantDetailEntity.getTenantId());
    }
}