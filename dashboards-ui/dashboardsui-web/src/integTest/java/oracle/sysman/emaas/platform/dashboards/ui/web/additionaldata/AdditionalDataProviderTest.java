package oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.uifwk.bootstrap.HtmlBootstrapJsUtil;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by guochen on 2/13/17.
 */
public class AdditionalDataProviderTest
{
    @Mocked
    HttpServletRequest httpServletRequest;
    @Mocked
    HtmlBootstrapJsUtil htmlBootstrapJsUtil;
    @Mocked
    DashboardDataAccessUtil dashboardDataAccessUtil;
    @Test(groups = { "s2" })
    public void testGetPreloadDataForRequest(){
        new Expectations(){
            {
                httpServletRequest.getHeader(anyString);
				result = "tenant.admin";
                httpServletRequest.getRequestURI();
				result = "/emsaasui/emcpdfui/builder.html";
                HtmlBootstrapJsUtil.getSDKVersionJS();
				result = "2.3.0";
            }
        };

        AdditionalDataProvider.getPreloadDataForRequest(httpServletRequest);
    }

    @Test(groups = { "s2" })
    public void testGetPreloadDataForRequestParamNull(){
        new Expectations(){
            {
                httpServletRequest.getHeader(anyString);
                result = "tenant";
            }
        };

        AdditionalDataProvider.getPreloadDataForRequest(httpServletRequest);
    }

    @Test(groups = { "s2" })
    public void testGetPreloadDataForRequest2ND(){
        new Expectations(){
            {
                httpServletRequest.getHeader(anyString);
                result = "tenant.admin";
                httpServletRequest.getRequestURI();
                result = "/emsaasui/emcpdfui/builder.html";
                HtmlBootstrapJsUtil.getSDKVersionJS();
                result = "sdkversionjs";
            }
        };

        AdditionalDataProvider.getPreloadDataForRequest(httpServletRequest);
	}

    @Test(groups = { "s2" })
    public void testGetPostloadDataForRequest(){
        new Expectations(){
            {
                httpServletRequest.getRequestURI();
                result = "/emsaasui/emcpdfui/builder.html";
                httpServletRequest.getHeader(anyString);
                result = "tenant.admin";
                httpServletRequest.getParameter(anyString);
                result = "24";
                DashboardDataAccessUtil.getCombinedData(anyString, anyString, (HttpServletRequest) any, anyString, (BigInteger)any);
            }
        };

        AdditionalDataProvider.getPostloadDataForRequest(httpServletRequest);
    }

    @Test(groups = { "s2" })
    public void testGetPostloadDataForRequest_otherURI(){
        new Expectations(){
            {
                httpServletRequest.getRequestURI();
                result = "/emsaasui/emcpdfui/home.html";
            }
        };

        String data = AdditionalDataProvider.getPostloadDataForRequest(httpServletRequest);
        Assert.assertEquals(data, "</body></html>");
    }

    @Test(groups = { "s2" })
    public void testGetPostloadDataForRequest_nullParam(){
        new Expectations(){
            {
                httpServletRequest.getRequestURI();
                result = "/emsaasui/emcpdfui/builder.html";
                httpServletRequest.getHeader(anyString);
                result = "tenant";
            }
        };

        AdditionalDataProvider.getPostloadDataForRequest(httpServletRequest);
    }

}
