package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.core.util.Charsets;
import org.testng.annotations.Test;

public class HomePageFilterTest
{
	@Test(groups = { "s2" })
	public void testDoFilter(@Mocked final FilterChain chain, @Mocked final HttpServletRequest request,
			@Mocked final HttpServletResponse response, @Mocked final LookupManager lookupManager,
			@Mocked final CloseableHttpClient httpClient, @Mocked final RegistryLookupUtil anyUtil) throws Exception
	{
		final HomePageFilter filter = new HomePageFilter();
		final StringBuffer homeUrl = new StringBuffer("https://xxx.us.oracle.com:4443/emsaasui/emcpdfui/home.html");
		final String testString = "xxxvalueid=12345678";
		final String tenant = "tenant01.emcsadmin";
		final String oamPath = "oam/server/auth_cred_submit";
		final Link link = new Link();
		link.withHref("http://xxxx/naming/entitynaming/v1/domains");
		final InputStream is = new ByteArrayInputStream(testString.getBytes(Charsets.UTF_8));

		new NonStrictExpectations() {
			{
				// authorization
				LookupManager.getInstance().getAuthorizationToken();
				result = "authorization".toCharArray();

				// getPreference
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;

				httpClient.execute((HttpUriRequest) any);
				result = new MockCloseableHttpResponse(is);

				request.getRequestURL();
				result = homeUrl;
			}
		};

		new Expectations() {
			{
				String referer = Deencapsulation.getField(filter, "REFERER");
				request.getHeader(referer);
				returns(null, //1
						testString, //2
						oamPath, //3
						oamPath); //4

				// userTenant
				String oamUser = Deencapsulation.getField(filter, "OAM_REMOTE_USER_HEADER");
				request.getHeader(oamUser);
				returns(null, //3
						tenant); //4
			}
		};

		filter.doFilter(request, response, chain); //1
		filter.doFilter(request, response, chain); //2
		filter.doFilter(request, response, chain); //3
		filter.doFilter(request, response, chain); //4

		new Verifications() {
			{
				chain.doFilter(request, response);
				times = 3;
			}
		};
	}
	@Mocked
	ClientProtocolException clientProtocolException;
	@Test(groups = { "s2" }, expectedExceptions = {NullPointerException.class})
	public void testDoFilter_ClientProtocolException(@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response,
													 @Mocked FilterChain chain, @Mocked final CloseableHttpClient httpClient,
													 @Mocked final LookupManager lookupManager,
													 @Mocked HttpClients httpClients,@Mocked final RegistryLookupUtil anyUtil) throws IOException, ServletException {

		final StringBuffer homeUrl = new StringBuffer("https://xxx.us.oracle.com:4443/emsaasui/emcpdfui/home.html");
		final String testString = "xxxvalueid=12345678";
		final String tenant = "tenant01.emcsadmin";
		final String oamPath = "oam/server/auth_cred_submit";
		final Link link = new Link();
		link.withHref("http://xxxx/naming/entitynaming/v1/domains");
		final HomePageFilter filter = new HomePageFilter();
		new Expectations(){
			{
				String referer = Deencapsulation.getField(filter, "REFERER");
				request.getHeader(referer);
				result = oamPath;
				String oamUser = Deencapsulation.getField(filter, "OAM_REMOTE_USER_HEADER");
				request.getHeader(oamUser);
				result= tenant;
				LookupManager.getInstance().getAuthorizationToken();
				result = "authorization".toCharArray();
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;
				httpClient.execute((HttpUriRequest) any);
				result = clientProtocolException;
			}
		};
		filter.doFilter(request, response, chain);
	}
	@Mocked
	IOException ioException;
	@Test(groups = { "s2" }, expectedExceptions = {NullPointerException.class})
	public void testDoFilter_IOException(@Mocked final HttpServletRequest request, @Mocked HttpServletResponse response,
													 @Mocked FilterChain chain, @Mocked final CloseableHttpClient httpClient,
													 @Mocked final LookupManager lookupManager,
													 @Mocked HttpClients httpClients,@Mocked final RegistryLookupUtil anyUtil) throws IOException, ServletException {

		final StringBuffer homeUrl = new StringBuffer("https://xxx.us.oracle.com:4443/emsaasui/emcpdfui/home.html");
		final String testString = "xxxvalueid=12345678";
		final String tenant = "tenant01.emcsadmin";
		final String oamPath = "oam/server/auth_cred_submit";
		final Link link = new Link();
		link.withHref("http://xxxx/naming/entitynaming/v1/domains");
		final HomePageFilter filter = new HomePageFilter();
		new Expectations(){
			{
				String referer = Deencapsulation.getField(filter, "REFERER");
				request.getHeader(referer);
				result = oamPath;
				String oamUser = Deencapsulation.getField(filter, "OAM_REMOTE_USER_HEADER");
				request.getHeader(oamUser);
				result= tenant;
				LookupManager.getInstance().getAuthorizationToken();
				result = "authorization".toCharArray();
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;
				httpClient.execute((HttpUriRequest) any);
				result = ioException;
			}
		};
		filter.doFilter(request, response, chain);
	}
}
