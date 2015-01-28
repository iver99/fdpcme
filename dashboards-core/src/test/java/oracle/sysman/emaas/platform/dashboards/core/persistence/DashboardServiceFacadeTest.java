/**
 *
 */
package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardFavorite;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardLastAccess;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTile;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboardTileParams;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class DashboardServiceFacadeTest
{

	private static void assertEmsDashboard(EmsDashboard emsdashboard)
	{
		Assert.assertNotNull(emsdashboard.getCreationDate());
		Assert.assertNotNull(emsdashboard.getDashboardId());
		Assert.assertNotNull(emsdashboard.getDeleted());
		Assert.assertNotNull(emsdashboard.getDescription());
		Assert.assertNotNull(emsdashboard.getEnableTimeRange());
		Assert.assertNotNull(emsdashboard.getIsSystem());
		Assert.assertNotNull(emsdashboard.getLastModificationDate());
		Assert.assertNotNull(emsdashboard.getLastModifiedBy());
		Assert.assertNotNull(emsdashboard.getName());
		Assert.assertNotNull(emsdashboard.getOwner());
		Assert.assertNotNull(emsdashboard.getScreenShot());
		//Assert.assertNotNull("tenantId = " + emsdashboard.getTenantId());
		Assert.assertNotNull(emsdashboard.getType());

		Assert.assertNotNull(emsdashboard.getDashboardTileList());
	}

	private static void assertEmsDashboardFavorite(EmsDashboardFavorite emsdashboardfavorite)
	{
		Assert.assertNotNull(emsdashboardfavorite.getCreationDate());
		//Assert.assertNotNull("tenantId = " + emsdashboardfavorite.getTenantId());
		Assert.assertNotNull(emsdashboardfavorite.getUserName());
		Assert.assertNotNull(emsdashboardfavorite.getDashboard());
	}

	private static void assertEmsDashboardLastAccess(EmsDashboardLastAccess emsdashboardlastaccess)
	{
		Assert.assertNotNull(emsdashboardlastaccess.getAccessedBy());
		Assert.assertNotNull(emsdashboardlastaccess.getAccessDate());
		Assert.assertNotNull(emsdashboardlastaccess.getDashboardId());
		//Assert.assertNotNull("tenantId = " + emsdashboardlastaccess.getTenantId());
	}

	private static void assertEmsDashboardTile(EmsDashboardTile emsdashboardtile)
	{
		Assert.assertNotNull(emsdashboardtile.getCreationDate());
		Assert.assertNotNull(emsdashboardtile.getHeight());
		Assert.assertNotNull(emsdashboardtile.getIsMaximized());
		Assert.assertNotNull(emsdashboardtile.getLastModificationDate());
		Assert.assertNotNull(emsdashboardtile.getLastModifiedBy());
		Assert.assertNotNull(emsdashboardtile.getOwner());
		Assert.assertNotNull(emsdashboardtile.getPosition());
		Assert.assertNotNull(emsdashboardtile.getProviderAssetRoot());
		Assert.assertNotNull(emsdashboardtile.getProviderName());
		Assert.assertNotNull(emsdashboardtile.getProviderVersion());
		//Assert.assertNotNull("tenantId = " + emsdashboardtile.getTenantId());
		Assert.assertNotNull(emsdashboardtile.getTileId());
		Assert.assertNotNull(emsdashboardtile.getTitle());
		Assert.assertNotNull(emsdashboardtile.getWidgetCreationTime());
		Assert.assertNotNull(emsdashboardtile.getWidgetDescription());
		Assert.assertNotNull(emsdashboardtile.getWidgetGroupName());
		Assert.assertNotNull(emsdashboardtile.getWidgetHistogram());
		Assert.assertNotNull(emsdashboardtile.getWidgetIcon());
		Assert.assertNotNull(emsdashboardtile.getWidgetKocName());
		Assert.assertNotNull(emsdashboardtile.getWidgetName());
		Assert.assertNotNull(emsdashboardtile.getWidgetOwner());
		Assert.assertNotNull(emsdashboardtile.getWidgetSource());
		Assert.assertNotNull(emsdashboardtile.getWidgetTemplate());
		Assert.assertNotNull(emsdashboardtile.getWidgetUniqueId());
		Assert.assertNotNull(emsdashboardtile.getWidgetViewmode());
		Assert.assertNotNull(emsdashboardtile.getWidth());
		Assert.assertNotNull(emsdashboardtile.getDashboard());
		Assert.assertNotNull(emsdashboardtile.getDashboardTileParamsList());
	}

	private static void assertEmsDashboardTileParams(EmsDashboardTileParams emsdashboardtileparams)
	{
		Assert.assertNotNull(emsdashboardtileparams.getIsSystem());
		Assert.assertNotNull(emsdashboardtileparams.getParamName());
		Assert.assertNotNull(emsdashboardtileparams.getParamType());
		Assert.assertNotNull(emsdashboardtileparams.getParamValueNum());
		Assert.assertNotNull(emsdashboardtileparams.getParamValueStr());
		Assert.assertNotNull(emsdashboardtileparams.getParamValueTimestamp());
		Assert.assertNotNull(emsdashboardtileparams.getDashboardTile());
	}

	private static EmsDashboard newDashboard()
	{
		EmsDashboard d = new EmsDashboard();
		d.setName("test");
		d.setCreationDate(new Date());
		d.setDeleted(0L);
		d.setDescription("test");
		d.setEnableTimeRange(1);
		d.setIsSystem(1);
		d.setLastModificationDate(new Date());
		d.setOwner("test");
		d.setLastModifiedBy("test");
		//d.setTenantId("11");
		d.setScreenShot("slslslslsl");
		d.setType(0);
		return d;
	}

	private static EmsDashboardFavorite newFavorite(EmsDashboard d)
	{
		EmsDashboardFavorite f = new EmsDashboardFavorite();
		f.setCreationDate(new Date());
		//f.setTenantId("tenantId");
		f.setUserName("userName");
		f.setDashboard(d);
		return f;
	}

	private static EmsDashboardLastAccess newLastAccess(Long dashboardId)
	{
		EmsDashboardLastAccess a = new EmsDashboardLastAccess();
		a.setAccessDate(new Date());
		a.setDashboardId(dashboardId);
		a.setAccessedBy("accessedBy");
		//a.setTenantId("tenantId");
		return a;
	}

	private static EmsDashboardTile newTile()
	{
		EmsDashboardTile tile = new EmsDashboardTile();
		tile.setCreationDate(new Date());
		tile.setHeight(1);
		tile.setWidth(23);
		tile.setIsMaximized(1);
		tile.setLastModificationDate(new Date());
		tile.setLastModifiedBy("test");
		tile.setOwner("test");
		tile.setPosition(1);
		tile.setProviderAssetRoot("ssss");
		tile.setProviderName("sss");
		tile.setProviderVersion("providerVersion");
		//tile.setTenantId("tenantId");
		tile.setTitle("title");
		tile.setWidgetCreationTime("widgetCreationTime");
		tile.setWidgetDescription("widgetDescription");
		tile.setWidgetGroupName("widgetGroupName");
		tile.setWidgetIcon("widgetIcon");
		tile.setWidgetKocName("widgetKocName");
		tile.setWidgetName("widgetName");
		tile.setWidgetOwner("widgetOwner");
		tile.setWidgetSource(1);
		tile.setWidgetTemplate("widgetTemplate");
		tile.setWidgetUniqueId("widgetUniqueId");
		tile.setWidgetViewmode("widgetViewmode");
		tile.setWidgetHistogram("widgetHistogram");
		return tile;
	}

	private static EmsDashboardTileParams newTileParams()
	{
		EmsDashboardTileParams p = new EmsDashboardTileParams();
		p.setIsSystem(0);
		p.setParamName("paramName");
		p.setParamType(0);
		p.setParamValueNum(1);
		p.setParamValueStr("paramValueStr");
		p.setParamValueTimestamp(new Date());
		return p;
	}

	private DashboardServiceFacade dashboardServiceFacade;

	private EmsDashboard d;

	private EmsDashboardTile t;

	private EmsDashboardTileParams p;

	private EmsDashboardFavorite f;

	private EmsDashboardLastAccess a;

	//	/**
	//	 * @throws java.lang.Exception
	//	 */
	//	@BeforeClass
	//	public static void setUpBeforeClass() throws Exception {
	//		PersistenceManager.setTestEnv(true);
	//	}
	//
	//	/**
	//	 * @throws java.lang.Exception
	//	 */
	//	@AfterClass
	//	public static void tearDownAfterClass() throws Exception {
	//	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeTest
	public void setUp() throws Exception
	{
		PersistenceManager.setTestEnv(true);
		// tenant id updated to number type
		dashboardServiceFacade = new DashboardServiceFacade(111L);
		d = DashboardServiceFacadeTest.newDashboard();
		t = DashboardServiceFacadeTest.newTile();
		p = DashboardServiceFacadeTest.newTileParams();
		t.addEmsDashboardTileParams(p);
		d.addEmsDashboardTile(t);
		f = DashboardServiceFacadeTest.newFavorite(d);

		dashboardServiceFacade.persistEmsDashboard(d);
		dashboardServiceFacade.persistEmsDashboardFavorite(f);
		dashboardServiceFacade.commitTransaction();
		dashboardServiceFacade.getEntityManager().refresh(d);

		a = DashboardServiceFacadeTest.newLastAccess(d.getDashboardId());
		dashboardServiceFacade.persistEmsDashboardLastAccess(a);
		dashboardServiceFacade.commitTransaction();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterTest
	public void tearDown() throws Exception
	{
		if (a != null) {
			dashboardServiceFacade.removeEmsDashboardLastAccess(a);
		}
		if (f != null) {
			dashboardServiceFacade.removeEmsDashboardFavorite(f);
		}
		if (d != null) {
			dashboardServiceFacade.removeEmsDashboard(d);
		}
		dashboardServiceFacade.commitTransaction();
	}

	@Test
	public void testAll()
	{
		testQueryByRange();
		testGetEmsDashboardTileFindAll();
		testMergeEmsDashboard();
		testGetEmsDashboardById();
		testGetEmsDashboardFindAll();
		testGetEmsDashboardLastAccessFindAll();
		testGetEmsDashboardTileParamsFindAll();
		testGetEmsDashboardFavoriteFindAll();
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardById(java.lang.Long)}
	 * .
	 */
	public void testGetEmsDashboardById()
	{
		EmsDashboard rd = dashboardServiceFacade.getEmsDashboardById(d.getDashboardId());
		DashboardServiceFacadeTest.assertEmsDashboard(rd);
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardFavoriteFindAll()}.
	 */
	public void testGetEmsDashboardFavoriteFindAll()
	{
		for (EmsDashboardFavorite emsdashboardfavorite : dashboardServiceFacade.getEmsDashboardFavoriteFindAll()) {
			DashboardServiceFacadeTest.assertEmsDashboardFavorite(emsdashboardfavorite);
		}
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardFindAll()}.
	 */
	public void testGetEmsDashboardFindAll()
	{
		for (EmsDashboard emsdashboard : dashboardServiceFacade.getEmsDashboardFindAll()) {
			DashboardServiceFacadeTest.assertEmsDashboard(emsdashboard);
		}
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardLastAccessFindAll()}.
	 */
	public void testGetEmsDashboardLastAccessFindAll()
	{
		for (EmsDashboardLastAccess emsdashboardlastaccess : dashboardServiceFacade.getEmsDashboardLastAccessFindAll()) {
			DashboardServiceFacadeTest.assertEmsDashboardLastAccess(emsdashboardlastaccess);
		}
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardTileFindAll()}.
	 */
	public void testGetEmsDashboardTileFindAll()
	{
		for (EmsDashboardTile emsdashboardtile : dashboardServiceFacade.getEmsDashboardTileFindAll()) {
			DashboardServiceFacadeTest.assertEmsDashboardTile(emsdashboardtile);
		}
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#getEmsDashboardTileParamsFindAll()}.
	 */
	public void testGetEmsDashboardTileParamsFindAll()
	{
		for (EmsDashboardTileParams emsdashboardtileparams : dashboardServiceFacade.getEmsDashboardTileParamsFindAll()) {
			DashboardServiceFacadeTest.assertEmsDashboardTileParams(emsdashboardtileparams);
		}
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#mergeEmsDashboard(oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard)}
	 * .
	 */
	public void testMergeEmsDashboard()
	{
		d.setLastModificationDate(new Date());
		dashboardServiceFacade.persistEmsDashboard(d);
		dashboardServiceFacade.commitTransaction();
	}

	/**
	 * Test method for
	 * {@link oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade#queryByRange(java.lang.String, java.lang.Class, java.util.Map, int, int)}
	 * .
	 */
	public void testQueryByRange()
	{
		String sql = "select o from EmsDashboard o where o.name = :name";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("name", "test");
		List dqr = dashboardServiceFacade.queryByRange(sql, EmsDashboard.class, params, 0, 0);
		for (EmsDashboard emsdashboard : (List<EmsDashboard>) dqr) {
			DashboardServiceFacadeTest.assertEmsDashboard(emsdashboard);
		}
	}

}
