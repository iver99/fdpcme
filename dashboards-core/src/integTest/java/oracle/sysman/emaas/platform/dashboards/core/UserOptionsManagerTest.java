package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigInteger;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 2016/3/8.
 */
@Test
public class UserOptionsManagerTest extends BaseTest {
    @BeforeMethod
    public void beforeMethod()
    {
        TenantContext.setCurrentTenant("TenantOPC1");
    }

    @Test
    public void testGetInstance() {
        Assert.assertNotNull(UserOptionsManager.getInstance());
    }

    @Test
    public void testGetOptionsById() {
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
			Assert.assertNotNull(userOptionsManager.getOptionsById(BigInteger.valueOf(1001L),1L));
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testGetOptionsByIdWithNullId() {
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
			userOptionsManager.getOptionsById(null, 1L);
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testGetOptionsByIdWithInvalidId() {
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
			userOptionsManager.getOptionsById(BigInteger.valueOf(1001L),1L);
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test(expectedExceptions = UserOptionsNotFoundException.class)
    public void testGetOptionsByIdWithInvalidId2() {
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
			userOptionsManager.getOptionsById(BigInteger.valueOf(1001L),1L);
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test
    public void testSaveOrUpdateUserOptions() {
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
			userOptionsManager.saveOrUpdateUserOptions(null,1001L);
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        UserOptions userOptions = new UserOptions();
        userOptions.setDashboardId("1001L");
        userOptions.setAutoRefreshInterval(3000L);
        try {
			userOptionsManager.saveOrUpdateUserOptions(userOptions,1001L);
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testSaveOrUpdateUserOptionsWithNullId() {

        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        try {
			userOptionsManager.saveOrUpdateUserOptions(new UserOptions(),1001L);
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @Test(expectedExceptions = DashboardNotFoundException.class)
    public void testSaveOrUpdateUserOptionsWithInvalidId() {
        UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
        UserOptions userOptions = new UserOptions();
        userOptions.setDashboardId("1001L");
        userOptions.setAutoRefreshInterval(3000L);
        try {
			userOptionsManager.saveOrUpdateUserOptions(userOptions,1001L);
		}
		catch (DashboardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}