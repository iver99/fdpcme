package oracle.sysman.emaas.platform.dashboards.core;

import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Performance test cases created for dashbaord manager
 *
 * @author guobaochen
 */
public class DashboardManagerPerfTest
{
	private static final long NUM_DASHBOARDS_FOR_PERF_TEST = 1000L;

	private final String tenantId = "tenant1";
	private Long dashboard1stId;
	private final DashboardManager dm = DashboardManager.getInstance();
	private final List<Dashboard> dashboards = new ArrayList<Dashboard>();

	static {
		PersistenceManager.setTestEnv(true);
	}

	@AfterClass
	public void afterClass()
	{
		dm.deleteDashboard(dashboard1stId, true, tenantId);
	}

	@BeforeClass
	public void beforeClass() throws DashboardException
	{
		Dashboard dbd1 = new Dashboard();
		String name = "1st dashboard in beforeClass() for perf test " + System.currentTimeMillis();
		dbd1.setName(name);
		dbd1 = dm.saveNewDashboard(dbd1, tenantId);
		dashboard1stId = dbd1.getDashboardId();
	}

	@Test
	public void test1stCreateDashboard() throws DashboardException
	{
		long start = System.currentTimeMillis();
		for (int i = 0; i < NUM_DASHBOARDS_FOR_PERF_TEST; i++) {
			Dashboard dbd = new Dashboard();
			String name = "dashboards created for perf test " + System.currentTimeMillis();
			dbd.setName(name);
			dbd = dm.saveNewDashboard(dbd, tenantId);
			dashboards.add(dbd);
		}
		System.out.println("Time to create " + NUM_DASHBOARDS_FOR_PERF_TEST + " is " + (System.currentTimeMillis() - start));
	}

	@Test
	public void test2stQueryDashboardById() throws DashboardException
	{
		long start = System.currentTimeMillis();
		for (int i = 0; i < dashboards.size(); i++) {
			dm.getDashboardById(dashboards.get(i).getDashboardId(), tenantId);
		}
		System.out.println("Time to query " + NUM_DASHBOARDS_FOR_PERF_TEST + " is " + (System.currentTimeMillis() - start));
	}

	@Test
	public void test2stUpdateDashboard() throws DashboardException
	{
		for (int i = 0; i < dashboards.size(); i++) {
			Dashboard dbd = dashboards.get(i);
			dbd.setName("dashboard updated for perf test " + System.currentTimeMillis());
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < dashboards.size(); i++) {
			dm.updateDashboard(dashboards.get(i), tenantId);
		}
		System.out.println("Time to update " + NUM_DASHBOARDS_FOR_PERF_TEST + " is " + (System.currentTimeMillis() - start));
	}

	@Test
	public void test3rdQueryDashboardByString() throws DashboardException
	{
		long start = System.currentTimeMillis();
		dm.getDashboardByName("dashboard updated", tenantId);
		System.out.println("Time to query " + NUM_DASHBOARDS_FOR_PERF_TEST + " is " + (System.currentTimeMillis() - start));
	}

	/*
	 * Use this method to see how much time is needed to query a search from 1 million searches.
	 * Several manual steps are needed before run this method
	 * Note: (!!!!!!!!IMPORTANT!!!!!!!!)
	 * 1. use testCreateSearchPerformance() to create 1 million searches before running into this method
	 * 2. query your unit test database and change the searchId, searchName and folderId in the method, as
	 * it's found the JPA cache (or database cache?) have extreme impact on the result
	 * 3. un-comment the @Test and run with JUnit
	 */
	//@Test
	public void test4thSimgleQueryPerformance() throws DashboardException
	{
		Long dashboardId = 999L;
		String dashboardName = "dashboard updated";
		long start = System.currentTimeMillis();
		//sm.getSearch(search.getId());
		dm.getDashboardById(dashboardId, tenantId);
		long end = System.currentTimeMillis();
		System.out.println("Time spent to query one dashboard by ID from " + NUM_DASHBOARDS_FOR_PERF_TEST + " searches: "
				+ (end - start) + "ms");

		start = System.currentTimeMillis();
		dm.getDashboardByName(dashboardName, tenantId);
		end = System.currentTimeMillis();
		System.out.println("Time spent to query one dashboard by name from " + NUM_DASHBOARDS_FOR_PERF_TEST + " searches: "
				+ (end - start) + "ms");
	}

	/*
	 * Use this method to see how much time is needed to query a search from 1 million searches.
	 * Several manual steps are needed before run this method
	 * Note: (!!!!!!!!IMPORTANT!!!!!!!!)
	 * 1. use testCreateSearchPerformance() to create 1 million searches before running into this method
	 * 2. query your unit test database and change the searchId, searchName and folderId in the method, as
	 * it's found the JPA cache (or database cache?) have extreme impact on the result
	 * 3. un-comment the @Test and run with JUnit
	 */
	//@Test
	public void test5thDeletePerformance() throws DashboardException
	{
		long start = System.currentTimeMillis();
		int size = dashboards.size();
		for (int i = 0; i < size; i++) {
			dm.deleteDashboard(dashboards.get(i).getDashboardId(), tenantId);
		}
		System.out.println("Time to delete " + size + " is " + (System.currentTimeMillis() - start) + "ms");
		System.out.println("Time to delete one dashboard averagely is " + (System.currentTimeMillis() - start)
				/ Double.valueOf(size) + "ms");
	}
}
