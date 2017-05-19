package oracle.sysman.emaas.platform.dashboards.ui.web.additionaldata;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ui.web.AdditionalDataFilter;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.DashboardDataAccessUtil;
import oracle.sysman.emaas.platform.uifwk.bootstrap.HtmlBootstrapJsUtil;
import oracle.sysman.emaas.platform.uifwk.util.DataAccessUtil;

import org.testng.annotations.Test;

/**
 * Created by guochen on 2/13/17.
 */
public class AdditionalDataProviderTest
{

    private AdditionalDataProvider additionalDataProvider = new AdditionalDataProvider();
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
                result = "!/emsaasui/emcpdfui/builder.html";
                HtmlBootstrapJsUtil.getAllBootstrapJS(httpServletRequest);
                result = "bootstrapjs";
            }
        };

        AdditionalDataProvider.getPreloadDataForRequest(httpServletRequest);
	}

}
