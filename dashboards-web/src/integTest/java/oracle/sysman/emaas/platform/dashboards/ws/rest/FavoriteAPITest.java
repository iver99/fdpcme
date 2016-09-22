package oracle.sysman.emaas.platform.dashboards.ws.rest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Troy
 * @since 2016/1/21.
 */
@Test(groups = { "s2" })
public class FavoriteAPITest
{

	private final FavoriteAPI favoriteAPI = new FavoriteAPI();

	@Test
	public void test2AddOneFavoriteDashboard(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new DashboardNotFoundException();
			}
		};
		Assert.assertNotNull(favoriteAPI.addOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void test2DeleteOneFavoriteDashboard(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new DashboardNotFoundException();
			}
		};
		Assert.assertNotNull(favoriteAPI.deleteOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void test2GetAllFavoriteDashboards(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new DashboardNotFoundException();
			}
		};
		Assert.assertNotNull(favoriteAPI.getAllFavoriteDashboards("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer"));
	}

	@Test
	public void test2IsFavoriteDashboard(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new DashboardNotFoundException();
			}
		};
		Assert.assertNotNull(favoriteAPI.isFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void test3AddOneFavoriteDashboard(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("something wrong", "serviceName");
			}
		};
		Assert.assertNotNull(favoriteAPI.addOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void test3DeleteOneFavoriteDashboard(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("something wrong", "service name");
			}
		};
		Assert.assertNotNull(favoriteAPI.deleteOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void test3GetAllFavoriteDashboards(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("something wrong", "service name");
			}
		};
		Assert.assertNotNull(favoriteAPI.getAllFavoriteDashboards("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer"));
	}

	@Test
	public void test3IsFavoriteDashboard(@Mocked final APIBase anyAPIBase,
			@SuppressWarnings("unused") @Mocked final DashboardManager anyDm) throws Exception
	{
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = new BasicServiceMalfunctionException("something wrong", "service name");
			}
		};
		Assert.assertNotNull(favoriteAPI.isFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void testAddOneFavoriteDashboard(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm)
			throws Exception
	{
		final Long anyTenantId = 10L;
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				returns(anyTenantId);
				anyDm.addFavoriteDashboard((BigInteger) any, anyLong);
				returns(null);
			}
		};
		Assert.assertNotNull(favoriteAPI.addOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void testDeleteOneFavoriteDashboard(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm)
			throws Exception
	{
		final Long tenantId = 10L;
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = tenantId;
				anyDm.removeFavoriteDashboard((BigInteger) any, anyLong);
				result = null;
			}
		};
		Assert.assertNotNull(favoriteAPI.deleteOneFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

	@Test
	public void testGetAllFavoriteDashboards(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm)
			throws Exception
	{
		final List<Dashboard> list = new ArrayList<>();
		for (int i = 0; i <= 3; i++) {
			list.add(new Dashboard());
		}
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = 10L;
				anyDm.getFavoriteDashboards(anyLong);
				result = list;
			}
		};
		Assert.assertNotNull(favoriteAPI.getAllFavoriteDashboards("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer"));
	}

	@Test
	public void testIsFavoriteDashboard(@Mocked final APIBase anyAPIBase, @Mocked final DashboardManager anyDm) throws Exception
	{
		final Long anyTenantId = 10L;
		new Expectations() {
			{
				anyAPIBase.getTenantId(anyString);
				result = anyTenantId;
				anyDm.getDashboardById((BigInteger) any, anyLong);
				result = new Dashboard();
				anyDm.isDashboardFavorite((BigInteger) any, anyLong);
				result = true;
			}
		};
		Assert.assertNotNull(favoriteAPI.isFavoriteDashboard("X-USER-IDENTITY-DOMAIN-NAME", "X-REMOTE-USER", "Referer",
				BigInteger.valueOf(10L)));
	}

}