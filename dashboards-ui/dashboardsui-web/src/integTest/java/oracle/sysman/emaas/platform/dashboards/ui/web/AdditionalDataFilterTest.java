package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.math.BigInteger;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata.HtmlFragmentCache;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;

import oracle.sysman.emaas.platform.uifwk.nls.filter.NLSFilter;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by guochen on 11/29/16.
 */
public class AdditionalDataFilterTest {
    @Test(groups = { "s2" })
    public void testDoFilter(@Mocked final FilterChain chain, @Mocked final HttpServletRequest httpReq, @Mocked final HttpServletResponse response,
                             @Mocked final ByteArrayOutputStream anyByteArrayOutputStream, @Mocked final NLSFilter nlsf,
                             @Mocked final DashboardDataAccessUtil dashboardDataAccessUtil, @Mocked final HtmlFragmentCache htmlFragmentCache,
                             @Mocked final HtmlFragmentCache.CachedHtml cachedHtml) throws Exception
    {
        final AdditionalDataFilter filter = new AdditionalDataFilter();
        new Expectations() {
            {
                httpReq.getHeader(anyString);
                result = "tenant.user";
                httpReq.getParameter(anyString);
                result = "1";
                httpReq.getRequestURI();
                result = AdditionalDataFilter.BUILDER_URI;
                anyByteArrayOutputStream.toString();
                result = "<BEFORE_PART lang=\"en-US\">INTER_PART////ADDITIONALDATA////END_PART";
                NLSFilter.getLangAttr((HttpServletRequest)any);
                result = "lang=\"en\"";
                DashboardDataAccessUtil.getDashboardData(anyString, anyString, anyString, (BigInteger) any);
                result = "{value: 123abc??$^-[]|(&!~@#%^&*+{}<>\\_,.;`':\"}";
                DashboardDataAccessUtil.getRegistrationData(anyString, anyString, anyString, anyString);
                result = "{registration data}";
                DashboardDataAccessUtil.getUserTenantInfo(anyString, anyString, anyString, anyString);
                result = "{user tenant data}";
                HtmlFragmentCache.getInstance().getCachedElementsForRequest(anyString);
                result = cachedHtml;
                cachedHtml.getBeforeLangAttrPart();
                result = "<BEFORE_PART ";
                cachedHtml.getBeforeAdditionalDataPart();
                result = ">INTER_PART";
                cachedHtml.getAfterAdditionalDataPart();
                result = "END_PART";
            }
        };
        filter.doFilter(httpReq, response, chain);
    }

    /*@Test(groups = { "s2" })
    public void testFormatJsonString() {
        AdditionalDataFilter filter = new AdditionalDataFilter();
        String json = "{field1: value, field2: \"value2\", field3: \\\"value3\\\"}";
        String expected = "{field1: value, field2: \\\"value2\\\", field3: \\\\\\\"value3\\\\\\\"}";
        Assert.assertEquals(filter.formatJsonString(json), expected);
    }*/
}

