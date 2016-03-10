package oracle.sysman.emaas.platform.dashboards.core;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * @author jishshi
 * @since 2016/3/8.
 */
@Test(groups = {"s2"})
public class UserOptionsManagerTest {
    static {
        PersistenceManager.setTestEnv(true);
        UserContext.setCurrentUser("SYSMAN");
        TenantSubscriptionUtil.setTestEnv();
    }


    @BeforeMethod
    public void beforeMethod()
    {
        TenantContext.setCurrentTenant("TenantOPC1");
    }

    @Test
    public void testGetInstance() throws Exception {
        Assert.assertNotNull(UserOptionsManager.getInstance());
    }

    @Test
    public void testGetOptionsById() throws Exception {
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        Assert.assertNotNull(userOptionsManager.getOptionsById(1001L,1L));
    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testGetOptionsByIdWithNullId(@Mocked DashboardServiceFacade dashboardServiceFacade) throws Exception {

        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        userOptionsManager.getOptionsById(null,1l);
    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testGetOptionsByIdWithInvalidId(@Mocked final DashboardServiceFacade dashboardServiceFacade) throws Exception {
        new Expectations(){{
            dashboardServiceFacade.getEmsDashboardById(anyLong);
            result = null;
        }};
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        userOptionsManager.getOptionsById(1001L,1L);
    }

    @Test(expectedExceptions = UserOptionsNotFoundException.class)
    public void testGetOptionsByIdWithInvalidId2(@Mocked final DashboardServiceFacade dashboardServiceFacade) throws Exception {
        new Expectations(){{
            dashboardServiceFacade.getEmsUserOptions(anyString,anyLong);
            result = null;
        }};
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        userOptionsManager.getOptionsById(1001L,1L);
    }

    @Test
    public void testSaveOrUpdateUserOptions(@Mocked final DashboardServiceFacade dashboardServiceFacade) throws Exception {
        new Expectations(){{
            dashboardServiceFacade.mergeEmsUserOptions(withAny(new EmsUserOptions()));
            times =1;
        }};

        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        userOptionsManager.saveOrUpdateUserOptions(null,1001L);

        UserOptions userOptions = new UserOptions();
        userOptions.setDashboardId(100L);
        userOptions.setAutoRefreshInterval(3000L);
        userOptionsManager.saveOrUpdateUserOptions(userOptions,1001L);

    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testSaveOrUpdateUserOptionsWithNullId(@Mocked DashboardServiceFacade dashboardServiceFacade) throws Exception {

        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        userOptionsManager.saveOrUpdateUserOptions(new UserOptions(),1001L);

    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testSaveOrUpdateUserOptionsWithInvalidId(@Mocked final DashboardServiceFacade dashboardServiceFacade) throws Exception {
        new Expectations(){{
            dashboardServiceFacade.getEmsDashboardById(anyLong);
            result = null;
        }};
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        UserOptions userOptions = new UserOptions();
        userOptions.setDashboardId(100L);
        userOptions.setAutoRefreshInterval(3000L);
        userOptionsManager.saveOrUpdateUserOptions(userOptions,1001L);
    }

    @Test
    public void testSaveOrUpdateUserOptionsWithInvalidId2(@Mocked final DashboardServiceFacade dashboardServiceFacade) throws Exception {
        new Expectations(){{
            dashboardServiceFacade.getEmsUserOptions(anyString,anyLong);
            result = null;

            dashboardServiceFacade.persistEmsUserOptions(withAny(new EmsUserOptions()));
            times =1;
        }};
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        UserOptions userOptions = new UserOptions();
        userOptions.setDashboardId(100L);
        userOptions.setAutoRefreshInterval(3000L);
        userOptionsManager.saveOrUpdateUserOptions(userOptions,1001L);
    }
}