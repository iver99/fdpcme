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
	/*@Test(groups = { "s2" })
	public void testGetAdditionalDataForRequest(@Mocked final HttpServletRequest httpReq,
			@Mocked final HttpServletResponse response, @Mocked final DashboardDataAccessUtil dashboardDataAccessUtil,
			@Mocked final DataAccessUtil dataAccessUtil, @Mocked final HtmlBootstrapJsUtil bootstrapUtil)
	{
		new Expectations() {
			{
				httpReq.getHeader(anyString);
				result = "tenant.user";
				httpReq.getParameter(anyString);
				result = "1";
				httpReq.getRequestURI();
				result = AdditionalDataFilter.BUILDER_URI;
				httpReq.getParameter(anyString);
				result = "1234567";
				DashboardDataAccessUtil.getDashboardData(anyString, anyString, anyString, (BigInteger) any);
				result = "{value: 123abc??$^-[]|(&!~@#%^&*+{}<>\\_,.;`':\"}";
				//				DataAccessUtil.getRegistrationData(anyString, anyString, anyString, anyString);
				//				result = "{registration data}";
				//				DataAccessUtil.getUserTenantInfo(anyString, anyString, anyString, anyString);
				//				result = "{user tenant data}";
				HtmlBootstrapJsUtil.getAllBootstrapJS((HttpServletRequest) any);
				result = "{all bootstrap js codes}";
			}
		};
		AdditionalDataProvider.getAdditionalDataForRequest(httpReq);
	}*/
}
