package oracle.sysman.emaas.platform.dashboards.ws.rest.subappedition;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/18/2016.
 */
@Test(groups = {"s1"})
public class ServiceEntityTest {
    ServiceEntity serviceEntity;

    @BeforeMethod
    public void setUp()  {
        serviceEntity = new ServiceEntity();
    }

    @Test
    public void testGetCanonicalLink() {
        Assert.assertNull(serviceEntity.getCanonicalLink());

        serviceEntity.setCanonicalLink("link");
        Assert.assertEquals(serviceEntity.getCanonicalLink(),"link");

        serviceEntity.setCanonicalLink(null);
        Assert.assertNull(serviceEntity.getCanonicalLink());
    }

    @Test
    public void testGetEdition() {
        Assert.assertNull(serviceEntity.getEdition());

        serviceEntity.setEdition("edition");
        Assert.assertEquals(serviceEntity.getEdition(),"edition");

        serviceEntity.setEdition(null);
        Assert.assertNull(serviceEntity.getEdition());

    }

    @Test
    public void testGetEditionUUID() {
        Assert.assertNull(serviceEntity.getEditionUUID());

        serviceEntity.setEditionUUID("editionUUID");
        Assert.assertEquals(serviceEntity.getEditionUUID(),"editionUUID");

        serviceEntity.setEditionUUID(null);
        Assert.assertNull(serviceEntity.getEditionUUID());
    }

    @Test
    public void testGetServiceId() {
        Assert.assertNull(serviceEntity.getServiceId());

        serviceEntity.setServiceId("serviceId");
        Assert.assertEquals(serviceEntity.getServiceId(),"serviceId");

        serviceEntity.setServiceId(null);
        Assert.assertNull(serviceEntity.getServiceId());
    }

    @Test
    public void testGetServiceName() {
        Assert.assertNull(serviceEntity.getServiceName());

        serviceEntity.setServiceName("serviceName");
        Assert.assertEquals(serviceEntity.getServiceName(),"serviceName");

        serviceEntity.setServiceName(null);
        Assert.assertNull(serviceEntity.getServiceName());
    }

    @Test
    public void testGetServiceType() {
        Assert.assertNull(serviceEntity.getServiceType());

        serviceEntity.setServiceType("serviceType");
        Assert.assertEquals(serviceEntity.getServiceType(),"serviceType");

        serviceEntity.setServiceType(null);
        Assert.assertNull(serviceEntity.getServiceType());
    }

    @Test
    public void testGetStatus() {
        Assert.assertNull(serviceEntity.getStatus());

        serviceEntity.setStatus("status");
        Assert.assertEquals(serviceEntity.getStatus(),"status");

        serviceEntity.setStatus(null);
        Assert.assertNull(serviceEntity.getStatus());
    }
}