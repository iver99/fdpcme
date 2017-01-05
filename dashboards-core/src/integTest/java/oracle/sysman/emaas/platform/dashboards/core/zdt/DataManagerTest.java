package oracle.sysman.emaas.platform.dashboards.core.zdt;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.testng.Assert;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;

@Test(groups = { "s2" })
public class DataManagerTest
{
	private final long count = 1234L;

	@Test
	public void testGetAllDashboardsCount(@Mocked final DashboardServiceFacade anyDashboardServiceFacade,
			@Mocked final EntityManager anyEntityManager, @Mocked final Query anyQuery, @Mocked final Number anyNumber)
	{
		new Expectations() {
			{
				anyDashboardServiceFacade.getEntityManager();
				result = anyEntityManager;
				anyEntityManager.createNativeQuery(anyString);
				result = anyQuery;
				anyQuery.getSingleResult();
				result = anyNumber;
				anyNumber.longValue();
				result = count;
			}
		};
		long dbdCount = DataManager.getInstance().getAllDashboardsCount();
		Assert.assertEquals(dbdCount, count);
	}

	@Test
	public void testGetAllFavoriteCount(@Mocked final DashboardServiceFacade anyDashboardServiceFacade,
			@Mocked final EntityManager anyEntityManager, @Mocked final Query anyQuery, @Mocked final Number anyNumber)
	{
		new Expectations() {
			{
				anyDashboardServiceFacade.getEntityManager();
				result = anyEntityManager;
				anyEntityManager.createNativeQuery(anyString);
				result = anyQuery;
				anyQuery.getSingleResult();
				result = anyNumber;
				anyNumber.longValue();
				result = count;
			}
		};
		long favoriteCount = DataManager.getInstance().getAllFavoriteCount();
		Assert.assertEquals(favoriteCount, count);
	}

	@Test
	public void testGetAllPreferencessCount(@Mocked final DashboardServiceFacade anyDashboardServiceFacade,
			@Mocked final EntityManager anyEntityManager, @Mocked final Query anyQuery, @Mocked final Number anyNumber)
	{
		new Expectations() {
			{
				anyDashboardServiceFacade.getEntityManager();
				result = anyEntityManager;
				anyEntityManager.createNativeQuery(anyString);
				result = anyQuery;
				anyQuery.getSingleResult();
				result = anyNumber;
				anyNumber.longValue();
				result = count;
			}
		};
		long preferenceCount = DataManager.getInstance().getAllPreferencessCount();
		Assert.assertEquals(preferenceCount, count);
	}
}
