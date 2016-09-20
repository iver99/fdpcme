package oracle.sysman.emaas.platform.dashboards.core.cache;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;

public class CachedLinkTest
{
	private Link link;
	private final static String testHref = "test href";
	private final static String testRel = "test rel";

	@BeforeMethod
	public void beforeMethod()
	{
		link = new Link();
		link.withHref(testHref);
		link.withRel(testRel);
	}

	@Test
	public void testCachedLink()
	{
		CachedLink cl = new CachedLink(link);
		Assert.assertEquals(cl.getHref(), testHref);
		Assert.assertEquals(cl.getRel(), testRel);

		Link testLink = cl.getLink();
		Assert.assertEquals(testLink.getHref(), link.getHref());
		Assert.assertEquals(testLink.getRel(), link.getRel());
	}
}
