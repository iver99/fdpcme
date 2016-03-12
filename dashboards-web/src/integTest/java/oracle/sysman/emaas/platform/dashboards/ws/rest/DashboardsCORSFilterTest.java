package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ws.MockHttpServletRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author jishshi
 * @since 1/21/2016.
 */
@Test(groups = {"s2"})
public class DashboardsCORSFilterTest {
    DashboardsCORSFilter dashboardsCORSFilter;

    @BeforeMethod
    public void setUp() throws Exception {
        dashboardsCORSFilter = new DashboardsCORSFilter();
    }

    @Test
    public void testDestroy() throws Exception {
        dashboardsCORSFilter.destroy();
    }

    @Test
    public void testInit(@Mocked final  FilterConfig filterConfig) throws Exception {
        dashboardsCORSFilter.init(null);
        dashboardsCORSFilter.init(filterConfig);
    }

    @Test
    public void testDoFilter(@Mocked final HttpServletRequest httpServletRequest, @Mocked final HttpServletResponse httpServletResponse, @Mocked final FilterChain filterChain) throws Exception {
        //do nothing return
        dashboardsCORSFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);
    }


    @Test
    public void testDoFilterInnerClass(@SuppressWarnings("unused")@Mocked final HttpServletRequest httpServletRequest, @SuppressWarnings("unused")@Mocked final HttpServletResponse httpServletResponse, @SuppressWarnings("unused")@Mocked final FilterChain filterChain) throws Exception {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setHeader("OAM_REMOTE_USER", "tenant01.emcsadmin");

       @SuppressWarnings("unused")
        Object oamHttpRequestWrapperInst = Deencapsulation.newInstance(
                "oracle.sysman.emaas.platform.dashboards.ws.rest.DashboardsCORSFilter$OAMHttpRequestWrapper",
                mockHttpServletRequest);
    }

    @Test
    public void testDoFilter1(@Mocked final File file, @Mocked final HttpServletRequest httpServletRequest, @Mocked final HttpServletResponse httpServletResponse, @Mocked final FilterChain filterChain) throws Exception {
        new Expectations() {
            {
                httpServletRequest.getHeader("Origin");
                returns(null,anyString);

                //noinspection ResultOfMethodCallIgnored
                file.exists();
                result = true;
            }
        };
        dashboardsCORSFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

        //if header with "Origin"
        dashboardsCORSFilter.doFilter(httpServletRequest, httpServletResponse, filterChain);

    }

}