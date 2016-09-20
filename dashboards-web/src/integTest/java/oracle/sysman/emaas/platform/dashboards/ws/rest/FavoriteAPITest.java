package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertNotNull;

/**
 * @author Troy
 * @since 2016/1/21.
 */
@Test(groups={"s2"})
public class FavoriteAPITest {

    private FavoriteAPI favoriteAPI = new FavoriteAPI();

    @Test
    public void testAddOneFavoriteDashboard(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm) throws Exception {
        final Long  anyTenantId = 10L;
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                returns(anyTenantId);
                anyDm.addFavoriteDashboard(anyLong, anyLong);
                returns(null);
            }
        };
        assertNotNull(favoriteAPI.addOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer", 10L));
    }

    @Test
    public void test2AddOneFavoriteDashboard(@Mocked final APIBase anyAPIBase, @SuppressWarnings("unused")@Mocked final DashboardManager anyDm) throws Exception{
        new Expectations() {
            {
                anyAPIBase.getTenantId(anyString);
                result = new DashboardNotFoundException();
            }
        };
        assertNotNull(favoriteAPI.addOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }

    @Test
    public void test3AddOneFavoriteDashboard(@Mocked final APIBase anyAPIBase, @SuppressWarnings("unused")@Mocked final DashboardManager anyDm) throws Exception{
        new Expectations() {
            {
                anyAPIBase.getTenantId(anyString);
                result = new BasicServiceMalfunctionException("something wrong","serviceName");
            }
        };
        assertNotNull(favoriteAPI.addOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }

    @Test
    public void testDeleteOneFavoriteDashboard(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm) throws Exception {
        final Long tenantId = 10L;
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result = tenantId;
                anyDm.removeFavoriteDashboard(anyLong, anyLong);
                result = null;
            }
        };
        assertNotNull(favoriteAPI.deleteOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }
    @Test
    public void test2DeleteOneFavoriteDashboard(@Mocked final APIBase anyAPIBase,@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception {
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result = new DashboardNotFoundException();
            }
        };
        assertNotNull(favoriteAPI.deleteOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }
    @Test
    public void test3DeleteOneFavoriteDashboard(@Mocked final APIBase anyAPIBase,@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception {
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result = new BasicServiceMalfunctionException("something wrong", "service name");
            }
        };
        assertNotNull(favoriteAPI.deleteOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }

    @Test
    public void testGetAllFavoriteDashboards(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm) throws Exception {
        final List<Dashboard> list = new ArrayList<>();
        for(int i=0;i<=3;i++) 
        	list.add(new Dashboard());
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result = 10L;
                anyDm.getFavoriteDashboards(anyLong);
                result = list;
            }
        };
        assertNotNull(favoriteAPI.getAllFavoriteDashboards("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer"));
    }


    @Test
    public void test2GetAllFavoriteDashboards(@Mocked final APIBase anyAPIBase, @SuppressWarnings("unused")@Mocked final DashboardManager anyDm) throws Exception {
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result = new DashboardNotFoundException();
            }
        };
        assertNotNull(favoriteAPI.getAllFavoriteDashboards("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer"));
    }

    @Test
    public void test3GetAllFavoriteDashboards(@Mocked final APIBase anyAPIBase, @SuppressWarnings("unused")@Mocked final DashboardManager anyDm) throws Exception {
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result = new BasicServiceMalfunctionException("something wrong","service name");
            }
        };
        assertNotNull(favoriteAPI.getAllFavoriteDashboards("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer"));
    }

    @Test
    public void testIsFavoriteDashboard(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm) throws Exception {
        final Long anyTenantId = 10L;
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result = anyTenantId;
                anyDm.getDashboardById(anyLong, anyLong);
                result = new Dashboard();
                anyDm.isDashboardFavorite(anyLong,anyLong);
                result = true;
            }
        };
        assertNotNull(favoriteAPI.isFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }

    @Test
    public void test2IsFavoriteDashboard(@Mocked final APIBase anyAPIBase, @SuppressWarnings("unused")@Mocked final DashboardManager anyDm) throws Exception {
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result =  new DashboardNotFoundException();
            }
        };
        assertNotNull(favoriteAPI.isFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }

    @Test
    public void test3IsFavoriteDashboard(@Mocked final APIBase anyAPIBase, @SuppressWarnings("unused")@Mocked final DashboardManager anyDm) throws Exception {
        new Expectations(){
            {
                anyAPIBase.getTenantId(anyString);
                result =  new BasicServiceMalfunctionException("something wrong","service name");
            }
        };
        assertNotNull(favoriteAPI.isFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER","Referer",10L));
    }

}