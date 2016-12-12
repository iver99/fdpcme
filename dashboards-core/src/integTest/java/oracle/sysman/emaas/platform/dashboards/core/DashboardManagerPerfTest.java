package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Performance test cases created for dashbaord manager
 *
 * @author guobaochen
 */
public class DashboardManagerPerfTest extends BaseTest
{
	private static final long NUM_DASHBOARDS_FOR_PERF_TEST = 10000L;
	private static final Logger LOGGER = LogManager.getLogger(DashboardManagerPerfTest.class);

	private final static Long tenantId = 11L;
	private BigInteger dashboard1stId;
	private final DashboardManager dm = DashboardManager.getInstance();

	private final List<Dashboard> dashboards = new ArrayList<Dashboard>();

	@AfterClass
	public void afterClass() throws DashboardException
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
		try {
			long start = System.currentTimeMillis();
			for (int i = 0; i < NUM_DASHBOARDS_FOR_PERF_TEST; i++) {
				Dashboard dbd = new Dashboard();
				String name = "dashboards created for perf test " + System.currentTimeMillis();
				dbd.setName(name);
				dbd = dm.saveNewDashboard(dbd, tenantId);
				dashboards.add(dbd);
			}
			
		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test2ndQueryDashboardById() throws DashboardException
	{
		try {
			long start = System.currentTimeMillis();
			Dashboard dbd = null;
			for (int i = 0; i < dashboards.size(); i++) {
				dbd = dm.getDashboardById(dashboards.get(i).getDashboardId(), tenantId);
			}
			Assert.assertNotNull(dbd);
			
		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test3rdQueryDashboardByIdMultiThread()
	{
		try {
			DashboardManagerTestMockup.executeRepeatedly(100, 10, new Runnable() {
				@Override
				public void run()
				{
					try {
						dm.getDashboardById(dashboards.get(dashboards.size() - 1).getDashboardId(), tenantId);
					}
					catch (DashboardException e) {
						LOGGER.info("context",e);
						// for dashboard exception, means dashboard not found. just ignore
					}
				}
			}, null, null);
		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test4thQueryDashboardByString() throws DashboardException
	{
		try {
			long start = System.currentTimeMillis();
			Dashboard dbd = dm.getDashboardByName(dashboards.get(dashboards.size() - 1).getName(), tenantId);
			
			Assert.assertNotNull(dbd);
		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test5thSimgleQueryPerformance()
	{
		// better to update the dashboardId in your own environment manually
		BigInteger dashboardId = BigInteger.valueOf(999L);
		String dashboardName = "dashboard updated";
		long start = System.currentTimeMillis();
		try {
			dm.getDashboardById(dashboardId, tenantId);
		}
		catch (DashboardException e) {
			LOGGER.info("context",e);
			// for dashboard exception, means dashboard not found. just ignore
		}
		long end = System.currentTimeMillis();
		

		start = System.currentTimeMillis();
		dm.getDashboardByName(dashboardName, tenantId);
		end = System.currentTimeMillis();
		
	}

	@Test
	public void test6thUpdateDashboard() throws DashboardException, InterruptedException
	{
		try {
			for (int i = 0; i < dashboards.size(); i++) {
				Dashboard dbd = dashboards.get(i);
				Thread.sleep(1);
				dbd.setName("dashboard updated for perf test " + System.currentTimeMillis());
			}
			long start = System.currentTimeMillis();
			Dashboard dbd = null;
			for (int i = 0; i < dashboards.size(); i++) {
				dbd = dm.updateDashboard(dashboards.get(i), tenantId);
			}
			
			Assert.assertNotNull(dbd);
		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void test7thDeletePerformance() throws DashboardException
	{
		try {
			long start = System.currentTimeMillis();
			int size = dashboards.size();
			for (int i = 0; i < size; i++) {
				dm.deleteDashboard(dashboards.get(i).getDashboardId(), tenantId);
			}
			

			// hard deletion
			start = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				dm.deleteDashboard(dashboards.get(i).getDashboardId(), true, tenantId);
			}
			

		}
		catch (Exception e) {
			LOGGER.info("context",e);
			Assert.fail(e.getMessage());
		}
	}
}
