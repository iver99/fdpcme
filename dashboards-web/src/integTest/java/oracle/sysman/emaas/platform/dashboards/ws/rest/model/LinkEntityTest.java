package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 1/18/2016.
 */

@Test(groups={"s1"})
public class LinkEntityTest {
    LinkEntity linkEntity ;

    @BeforeMethod
    public void setUp() throws Exception {
        linkEntity = new LinkEntity(null,null,null,null);
    }

    @Test
    public void testGetHref() throws Exception {
        Assert.assertNull(linkEntity.getHref());
        linkEntity.setHref("href");
        Assert.assertEquals(linkEntity.getHref(),"href");
    }

    @Test
    public void testGetName() throws Exception {
        Assert.assertNull(linkEntity.getName());

        linkEntity.setName("name");
        Assert.assertEquals(linkEntity.getName(),"name");
    }

    @Test
    public void testGetServiceName() throws Exception {
        Assert.assertNull(linkEntity.getServiceName());
        linkEntity.setServiceName("ServiceName");
        Assert.assertEquals(linkEntity.getServiceName(),"ServiceName");

    }

    @Test
    public void testGetVersion() throws Exception {
        Assert.assertNull(linkEntity.getVersion());
        linkEntity.setVersion("version");
        Assert.assertEquals(linkEntity.getVersion(),"version");
    }
}