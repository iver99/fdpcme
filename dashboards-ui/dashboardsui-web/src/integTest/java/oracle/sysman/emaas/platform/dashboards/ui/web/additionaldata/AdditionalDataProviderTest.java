package oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.uifwk.bootstrap.HtmlBootstrapJsUtil;

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
    public void testGetAdditionalDataForRequest(){
        new Expectations(){
            {
                httpServletRequest.getHeader(anyString);
                result = "tenant.admin";
                httpServletRequest.getRequestURI();
                result = "/emsaasui/emcpdfui/builder.html";
                httpServletRequest.getParameter("dashboardId");
                result = "37";
                HtmlBootstrapJsUtil.getSDKVersionJS();
                result = "2.3.0";
                DashboardDataAccessUtil.getCombinedData(anyString, anyString, (HttpServletRequest) any, anyString, (BigInteger) any);
                result = "data";
            }
        };

        AdditionalDataProvider.getAdditionalDataForRequest(httpServletRequest);
    }

    @Test(groups = { "s2" })
    public void testGetAdditionalDataForRequestParamNull(){
        new Expectations(){
            {
                httpServletRequest.getHeader(anyString);
                result = "tenant";
            }
        };

        AdditionalDataProvider.getAdditionalDataForRequest(httpServletRequest);
    }
    @Test(groups = { "s2" })
    public void testGetAdditionalDataForRequest2ND(){
        new Expectations(){
            {
                httpServletRequest.getHeader(anyString);
                result = "tenant.admin";
                httpServletRequest.getRequestURI();
                result = "!/emsaasui/emcpdfui/builder.html";
                HtmlBootstrapJsUtil.getAllBootstrapJS(httpServletRequest);
                result = "bootstrapjs";
            }
        };

        AdditionalDataProvider.getAdditionalDataForRequest(httpServletRequest);
    }

}
