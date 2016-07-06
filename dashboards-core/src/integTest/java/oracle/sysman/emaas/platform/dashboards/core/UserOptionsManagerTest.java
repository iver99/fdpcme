package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigInteger;

import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.UserOptionsNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantContext;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author jishshi
 * @since 2016/3/8.
 */
@Test
public class UserOptionsManagerTest
{
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
	public void testGetInstance() throws Exception
	{
		Assert.assertNotNull(UserOptionsManager.getInstance());
	}

	@Test
	public void testGetOptionsById() throws Exception
	{
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		Assert.assertNotNull(userOptionsManager.getOptionsById(BigInteger.valueOf(1001L), 1L));
	}

	@Test(expectedExceptions = DashboardNotFoundException.class)
	public void testGetOptionsByIdWithInvalidId() throws Exception
	{
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		userOptionsManager.getOptionsById(BigInteger.valueOf(1001L), 1L);
	}

	@Test(expectedExceptions = UserOptionsNotFoundException.class)
	public void testGetOptionsByIdWithInvalidId2() throws Exception
	{
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		userOptionsManager.getOptionsById(BigInteger.valueOf(1001L), 1L);
	}

	@Test(expectedExceptions = DashboardNotFoundException.class)
	public void testGetOptionsByIdWithNullId() throws Exception
	{
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		userOptionsManager.getOptionsById(null, 1L);
	}

	@Test
	public void testSaveOrUpdateUserOptions() throws Exception
	{
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		userOptionsManager.saveOrUpdateUserOptions(null, 1001L);

		UserOptions userOptions = new UserOptions();
		userOptions.setDashboardId(BigInteger.valueOf(1001L));
		userOptions.setAutoRefreshInterval(3000L);
		userOptionsManager.saveOrUpdateUserOptions(userOptions, 1001L);

	}

	@Test(expectedExceptions = DashboardNotFoundException.class)
	public void testSaveOrUpdateUserOptionsWithInvalidId() throws Exception
	{
		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		UserOptions userOptions = new UserOptions();
		userOptions.setDashboardId(BigInteger.valueOf(1001L));
		userOptions.setAutoRefreshInterval(3000L);
		userOptionsManager.saveOrUpdateUserOptions(userOptions, 1001L);
	}

	@Test(expectedExceptions = DashboardNotFoundException.class)
	public void testSaveOrUpdateUserOptionsWithNullId() throws Exception
	{

		UserOptionsManager userOptionsManager = UserOptionsManager.getInstance();
		userOptionsManager.saveOrUpdateUserOptions(new UserOptions(), 1001L);

	}
}