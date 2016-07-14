package oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.comparator.rows.entities.TableRowsEntity;

public class DashboardRowsComparatorTest
{
	private static final String DASHBOARD1_NAME = "D1";
	private static final String DASHBOARD1_OPTIONS = "options1";

	// @formatter:off
	private static final String JSON_RESPONSE_DATA_TABLE="{"
			+ "\"EMS_DASHBOARD\": [{"
			+ 		"\"DASHBOARD_ID\":1,"
			+ 		"\"NAME\":\"" + DASHBOARD1_NAME + "\","
			+ 		"\"TYPE\":1,"
			+ 		"\"DESCRIPTION\":\"desc1\","
			+ 		"\"OWNER\":\"emcsadmin\","
			+ 		"\"IS_SYSTEM\":1,"
			+ 		"\"APPLICATION_TYPE\":1,"
			+ 		"\"ENABLE_TIME_RANGE\":1,"
			+ 		"\"DELETED\":1,"
			+ 		"\"TENANT_ID\":1,"
			+ 		"\"ENABLE_REFRESH\":1,"
			+ 		"\"SHARE_PUBLIC\":0,"
			+ 		"\"ENABLE_ENTITY_FILTER\":0,"
			+ 		"\"ENABLE_DESCRIPTION\":1,"
			+ 		"\"EXTENDED_OPTIONS\":\"" + DASHBOARD1_OPTIONS + "\""
			+ 	"}],"
			+ "\"EMS_DASHBOARD_FAVORITE\": [{"
			+ 			"\"USER_NAME\":\"emcsadmin\","
			+ 			"\"DASHBOARD_ID\":1,"
			+ 			"\"TENANT_ID\":1"
			+ 		"},{"
			+ 			"\"USER_NAME\":\"emcsadmin\","
			+ 			"\"DASHBOARD_ID\":2,"
			+ 			"\"TENANT_ID\":1"
			+ 		"}"
			+ "],"
			+ "\"EMS_DASHBOARD_LAST_ACCESS\": [{"
			+ 		"\"DASHBOARD_ID\":1,"
			+ 		"\"ACCESSED_BY\": 0,"
			+ 		"\"TENANT_ID\": 1"
			+ "}],"
			+ "\"EMS_DASHBOARD_SET\": [{"
			+ 		"\"DASHBOARD_SET_ID\": 12, "
			+ 		"\"TENANT_ID\":1, "
			+ 		"\"SUB_DASHBOARD_ID\":1,"
			+ 		"\"POSITION\": 0"
			+ "}],"
			+ "\"EMS_DASHBOARD_TILE\": [{"
			+ 		"\"TILE_ID\": 12, "
			+ 		"\"DASHBOARD_ID\":1,"
			+ 		"\"OWNER\":\"emcsadmin\","
			+ 		"\"TITLE\":\"tile1 title\","
			+ 		"\"HEIGHT\":2,"
			+ 		"\"WIDTH\":6,"
			+ 		"\"IS_MAXIMIZED\":1,"
			+ 		"\"TENANT_ID\":1,"
			+ 		"\"POSITION\": 0"
			+ "}],"
			+ "\"EMS_DASHBOARD_TILE_PARAMS\": [{"
			+ 		"\"TILE_ID\":12,"
			+ 		"\"PARAM_NAME\":\"Test name\","
			+ 		"\"TENANT_ID\":1,"
			+ 		"\"IS_SYSTEM\":1,"
			+ 		"\"PARAM_TYPE\":1,"
			+ 		"\"PARAM_VALUE_STR\":\"test value\"}],"
			+ "\"EMS_DASHBOARD_USER_OPTIONS\": [{"
			+ 		"\"USER_NAME\":\"emcsadmin\","
			+ 		"\"TENANT_ID\":1,"
			+ 		"\"DASHBOARD_ID\":1,"
			+ 		"\"AUTO_REFRESH_INTERVAL\":1,"
			+ 		"\"IS_FAVORITE\":1,"
			+ 		"\"EXTENDED_OPTIONS\":\"options1\"}],"
			+ "\"EMS_PREFERENCE\": [{"
			+ 		"\"USER_NAME\":\"emcsadmin1\","
			+ 		"\"PREF_KEY\":\"Dashboards.showWelcomeDialog\","
			+ 		"\"PREF_VALUE\":\"false\","
			+ 		"\"TENANT_ID\":1"
			+ 	"},{"
			+ 		"\"USER_NAME\":\"emcsadmin2\","
			+ 		"\"PREF_KEY\":\"Dashboards.showWelcomeDialog\","
			+ 		"\"PREF_VALUE\":\"true\","
			+ 		"\"TENANT_ID\":1"
			+ 	"}]"
			+ "}";
	// @formatter:on

	@Test(groups = { "s1" })
	public void testRetrieveRowsEntityFromJsonForSingleInstance() throws IOException
	{
		DashboardRowsComparator drc = new DashboardRowsComparator();
		TableRowsEntity tre = drc.retrieveRowsEntityFromJsonForSingleInstance(JSON_RESPONSE_DATA_TABLE);
		Assert.assertNotNull(tre);
		Assert.assertEquals(tre.getEmsDashboard().get(0).getName(), DASHBOARD1_NAME);
		Assert.assertEquals(tre.getEmsDashboardFavorite().size(), 2);
		Assert.assertEquals(tre.getEmsDashboardFavorite().get(0).getDashboardId().longValue(), 1);
		Assert.assertEquals(tre.getEmsDashboardFavorite().get(1).getDashboardId().longValue(), 2);
		Assert.assertEquals(tre.getEmsDashboardLastAccess().size(), 1);
		Assert.assertEquals(tre.getEmsDashboardLastAccess().get(0).getDashboardId().longValue(), 1);
		Assert.assertEquals(tre.getEmsDashboardSet().size(), 1);
		Assert.assertEquals(tre.getEmsDashboardSet().get(0).getDashboardSetId().longValue(), 12);
		Assert.assertEquals(tre.getEmsDashboardTile().size(), 1);
		Assert.assertEquals(tre.getEmsDashboardTile().get(0).getTileId().longValue(), 12);
		Assert.assertEquals(tre.getEmsDashboardTile().get(0).getHeight().intValue(), 2);
		Assert.assertEquals(tre.getEmsDashboardTileParams().size(), 1);
		Assert.assertEquals(tre.getEmsDashboardTileParams().get(0).getTileId().longValue(), 12);
		Assert.assertEquals(tre.getEmsDashboardTileParams().size(), 1);
		Assert.assertEquals(tre.getEmsDashboardUserOptions().get(0).getIsFavorite().intValue(), 1);
		Assert.assertEquals(tre.getEmsPreference().size(), 2);
		Assert.assertEquals(tre.getEmsPreference().get(0).getPrefValue(), "false");
		Assert.assertEquals(tre.getEmsPreference().get(1).getPrefValue(), "true");
	}
}
