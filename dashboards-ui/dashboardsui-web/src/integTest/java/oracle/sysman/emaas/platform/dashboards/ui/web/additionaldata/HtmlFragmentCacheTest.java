package oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata;

import oracle.sysman.emaas.platform.dashboards.ui.web.AdditionalDataFilter;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by guochen on 2/13/17.
 */
public class HtmlFragmentCacheTest {
    @Test(groups = { "s2" })
    public void testGetInstance() {
        Assert.assertEquals(HtmlFragmentCache.getInstance(), HtmlFragmentCache.getInstance());
        Assert.assertNotNull(HtmlFragmentCache.getInstance());
    }

    @Test(groups = { "s2" })
    public void testIsHtmlFragmentElementsCached_cacheElementsForRequest() {
        HtmlFragmentCache hfc = HtmlFragmentCache.getInstance();

        Assert.assertFalse(hfc.isHtmlFragmentElementsCached("not_existing_url"));
        Assert.assertFalse(hfc.isHtmlFragmentElementsCached(null));

        Assert.assertFalse(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.BUILDER_URI));
        Assert.assertFalse(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.ERROR_URI));
        Assert.assertFalse(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.HOME_URI));
        Assert.assertFalse(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.WELCOME_URI));

        HtmlFragmentCache.CachedHtml ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.BUILDER_URI);
        Assert.assertFalse(ch.isCached());
        ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.ERROR_URI);
        Assert.assertFalse(ch.isCached());
        ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.HOME_URI);
        Assert.assertFalse(ch.isCached());
        ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.WELCOME_URI);
        Assert.assertFalse(ch.isCached());

        hfc.cacheElementsForRequest(AdditionalDataFilter.BUILDER_URI, "data1", "data2", "data3");
        hfc.cacheElementsForRequest(AdditionalDataFilter.ERROR_URI, "data1", "data2", "data3");
        hfc.cacheElementsForRequest(AdditionalDataFilter.HOME_URI, "data1", "data2", "data3");
        hfc.cacheElementsForRequest(AdditionalDataFilter.WELCOME_URI, "data1", "data2", "data3");

        Assert.assertTrue(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.BUILDER_URI));
        Assert.assertTrue(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.ERROR_URI));
        Assert.assertTrue(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.HOME_URI));
        Assert.assertTrue(hfc.isHtmlFragmentElementsCached(AdditionalDataFilter.WELCOME_URI));

        ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.BUILDER_URI);
        Assert.assertTrue(ch.isCached());
        ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.ERROR_URI);
        Assert.assertTrue(ch.isCached());
        ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.HOME_URI);
        Assert.assertTrue(ch.isCached());
        ch = hfc.getCachedElementsForRequest(AdditionalDataFilter.WELCOME_URI);
        Assert.assertTrue(ch.isCached());
    }
}
