package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Verifications;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.TenantSubscriptionUtil;

import org.apache.logging.log4j.Logger;
import org.testng.annotations.Test;

public class DashboardsUiCORSFilterTest
{
	@SuppressWarnings("unchecked")
	@Test
	public void testDoFilter(@Mocked final FilterChain chain, @Mocked final HttpServletRequest request,
			@Mocked final HttpServletResponse response, @Mocked final TenantSubscriptionUtil tenantUtil, 
			@Mocked final RegistryLookupUtil lookupUtil) throws Exception
	{
		final String test = "TEST";
		final String homePath = "http://xxxx/emsaasui/emcpdfui/home.html";
		final String builderPath = "http://xxxx/emsaasui/emcpdfui/builder.html";
		final String tenant = "tenant01.emcsadmin";
		final List<String> serviceList = Arrays.asList("APM", "LogAnalytics", "ITAnalytics");
		final Link link = new Link();
		link.withHref("http://xxxx/naming/entitynaming/v1/domains");
		
		DashboardsUiCORSFilter filter = new DashboardsUiCORSFilter();
		
		new MockUp<File>() {
			@Mock
			boolean exists()
			{
				return true;
			}
		};

		new Expectations() {
			{
				request.getHeader(anyString);
				returns(test, test, 	//1
						test, tenant, 	//2
						test, tenant, 	//3
						test, tenant);	//4
				
				request.getRequestURI();
				returns(test,									//1 
						homePath, homePath, 					//2
						builderPath, builderPath, builderPath, 	//3
						homePath, homePath);					//4
				
				TenantSubscriptionUtil.getTenantSubscribedServices(anyString);
				returns(new ArrayList<String>(),	//2 
						new ArrayList<String>(), 	//3
						serviceList);				//4
				
				TenantSubscriptionUtil.isAPMServiceOnly((List<String>) any);
				returns(true);//4
				
				RegistryLookupUtil.getServiceExternalLink(anyString, anyString, anyString, anyString);
				returns(link);//4
			}
		};
		
		filter.doFilter(request, response, chain);	//1
		filter.doFilter(request, response, chain);	//2
		filter.doFilter(request, response, chain);	//3
		filter.doFilter(request, response, chain);	//4
		
		new Verifications() {
			{
				chain.doFilter(request, response); times = 1;
			}
		};
	}
}
