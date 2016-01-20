package oracle.sysman.emaas.platform.dashboards.ui.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.RegistryLookupUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicStatusLine;
import org.apache.logging.log4j.core.util.Charsets;
import org.testng.annotations.Test;

public class HomePageFilterTest {
	@Test
	public void testDoFilter(@Mocked final FilterChain chain, @Mocked final HttpServletRequest request,
			@Mocked final HttpServletResponse response, @Mocked final LookupManager lookupManager, 
			@Mocked final CloseableHttpClient httpClient, @Mocked final RegistryLookupUtil anyUtil) throws Exception {
		final HomePageFilter filter = new HomePageFilter();
		final StringBuffer homeUrl = new StringBuffer("https://xxx.us.oracle.com:4443/emsaasui/emcpdfui/home.html");
		final String testString = "xxxvalueid=12345678";
		final String tenant = "tenant01.emcsadmin";
		final String oamPath = "oam/server/auth_cred_submit";
		final Link link = new Link();
		link.withHref("http://xxxx/naming/entitynaming/v1/domains");
		final InputStream is = new ByteArrayInputStream(testString.getBytes(Charsets.UTF_8));
		
		final CloseableHttpResponse mockResponse = new MockUp<CloseableHttpResponse>() {
			@Mock
			HttpEntity getEntity() {
				BasicHttpEntity entity = new BasicHttpEntity();
				entity.setContent(is);
				return entity;
			}
			
			@Mock
			StatusLine getStatusLine() {
				ProtocolVersion protocolVersion = new ProtocolVersion("HTTP", 1, 1);
			    StatusLine statusline = new BasicStatusLine(protocolVersion, HttpStatus.SC_BAD_REQUEST, "400");
			    return statusline;
			}
		}.getMockInstance();
		
		new NonStrictExpectations() {
			{
				// authorization
				LookupManager.getInstance().getAuthorizationToken();
				result = "authorization".toCharArray();
				
				// getPreference
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;
				
				httpClient.execute((HttpUriRequest) any);
				result = mockResponse;
				
				request.getRequestURL();
				result = homeUrl;
			}
		};
		
		new Expectations() {
			{
				String referer = Deencapsulation.getField(filter, "REFERER");
				request.getHeader(referer);
				returns(null, 		//1
						testString, //2
						oamPath, 	//3
						oamPath); 	//4
				
				// userTenant
				String oamUser = Deencapsulation.getField(filter, "OAM_REMOTE_USER_HEADER");
				request.getHeader(oamUser);
				returns(null, 		//3
						tenant);	//4
			}
		};

		filter.doFilter(request, response, chain); //1
		filter.doFilter(request, response, chain); //2
		filter.doFilter(request, response, chain); //3
		filter.doFilter(request, response, chain); //4

		new Verifications() {
			{
				chain.doFilter(request, response); times = 3;
			}
		};
	}
}
