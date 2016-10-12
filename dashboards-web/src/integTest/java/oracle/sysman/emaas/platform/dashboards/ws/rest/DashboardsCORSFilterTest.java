package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ws.MockHttpServletRequest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

/**
 * @author jishshi
 * @since 1/21/2016.
 */
@Test(groups = {"s2"})
public class DashboardsCORSFilterTest {
    DashboardsCORSFilter dashboardsCORSFilter;

    @BeforeMethod
    public void setUp()  {
        dashboardsCORSFilter = new DashboardsCORSFilter();
    }

    @Test
    public void testDestroy() {
        dashboardsCORSFilter.destroy();
    }

    @Test
    public void testInit(@Mocked final  FilterConfig filterConfig) {
        try {
			dashboardsCORSFilter.init(null);
		}
		catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			dashboardsCORSFilter.init(filterConfig);
		}
		catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test
    public void testDoFilter(@Mocked final HttpServletRequest httpServletRequest, @Mocked final HttpServletResponse httpServletResponse, @Mocked final FilterChain filterChain) {
        //do nothing return
        try {
			dashboardsCORSFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
		}
		catch (IOException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    @Test
    public void testDoFilterInnerClass(@SuppressWarnings("unused")@Mocked final HttpServletRequest httpServletRequest, @SuppressWarnings("unused")@Mocked final HttpServletResponse httpServletResponse, @SuppressWarnings("unused")@Mocked final FilterChain filterChain) {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setHeader("OAM_REMOTE_USER", "tenant01.emcsadmin");

       @SuppressWarnings("unused")
        Object oamHttpRequestWrapperInst = Deencapsulation.newInstance(
                "oracle.sysman.emaas.platform.dashboards.ws.rest.DashboardsCORSFilter$OAMHttpRequestWrapper",
                mockHttpServletRequest);
    }

    @Test
    public void testDoFilter1(@Mocked final File file, @Mocked final HttpServletRequest httpServletRequest, @Mocked final HttpServletResponse httpServletResponse, @Mocked final FilterChain filterChain) {
        new Expectations() {
            {
                httpServletRequest.getHeader("Origin");
                returns(null,anyString);

                //noinspection ResultOfMethodCallIgnored
                file.exists();
                result = true;
            }
        };
        try {
			dashboardsCORSFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
		}
		catch (IOException | ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        //if header with "Origin"
        try {
			dashboardsCORSFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

}