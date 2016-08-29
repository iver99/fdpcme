package oracle.sysman.emaas.platform.dashboards.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.core.model.TileParam;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class DataFormatUtilsTest
{
	private static final Logger LOGGER = LogManager.getLogger(DataFormatUtilsTest.class);
	@Test(groups = { "s2" })
	public void testDashboardTypeString2Integer() throws CommonFunctionalException
	{
		Assert.assertEquals(Dashboard.DASHBOARD_TYPE_CODE_NORMAL, DataFormatUtils.dashboardTypeString2Integer(null));

		try {
			String type = "NotExistingType";
			DataFormatUtils.dashboardTypeString2Integer(type);
			Assert.fail("Fail: expecting CommonFunctionalException");
		}
		catch (CommonFunctionalException e) {
			LOGGER.info("context",e);
			// expecting this exception
		}

		Assert.assertEquals(Dashboard.DASHBOARD_TYPE_CODE_SINGLEPAGE,
				DataFormatUtils.dashboardTypeString2Integer(Dashboard.DASHBOARD_TYPE_SINGLEPAGE));
		Assert.assertEquals(Dashboard.DASHBOARD_TYPE_CODE_NORMAL,
				DataFormatUtils.dashboardTypeString2Integer(Dashboard.DASHBOARD_TYPE_NORMAL));
	}

	@Test(groups = { "s2" })
	public void testDate2Timestamp()
	{
		Assert.assertNull(DataFormatUtils.date2Timestamp(null));

		long current = System.currentTimeMillis();
		Date now = new Date(current);
		Timestamp rtn = DataFormatUtils.date2Timestamp(now);
		Assert.assertEquals(current, rtn.getTime());
	}

	@Test(groups = { "s2" })
	public void testInteger2Boolean()
	{
		Assert.assertNull(DataFormatUtils.integer2Boolean(null));

		Assert.assertTrue(DataFormatUtils.integer2Boolean(1));
		Assert.assertTrue(DataFormatUtils.integer2Boolean(1000));
		Assert.assertTrue(DataFormatUtils.integer2Boolean(-30));
		Assert.assertFalse(DataFormatUtils.integer2Boolean(0));
	}

	@Test(groups = { "s2" })
	public void testLong2BigDecimal()
	{
		Assert.assertNull(DataFormatUtils.long2BigDecimal(null));

		Assert.assertEquals(DataFormatUtils.long2BigDecimal(3L), new BigDecimal(3));
		Assert.assertEquals(DataFormatUtils.long2BigDecimal(Long.MAX_VALUE), new BigDecimal(Long.MAX_VALUE));
	}

	@Test(groups = { "s2" })
	public void testTileParamTypeInteger2String()
	{
		Assert.assertEquals(DataFormatUtils.tileParamTypeInteger2String(TileParam.PARAM_TYPE_CODE_NUMBER),
				TileParam.PARAM_TYPE_NUMBER);
		Assert.assertEquals(DataFormatUtils.tileParamTypeInteger2String(TileParam.PARAM_TYPE_CODE_BOOLEAN),
				TileParam.PARAM_TYPE_BOOLEAN);
		Assert.assertEquals(DataFormatUtils.tileParamTypeInteger2String(TileParam.PARAM_TYPE_CODE_STRING),
				TileParam.PARAM_TYPE_STRING);
		// all others: default to string
		Assert.assertEquals(DataFormatUtils.tileParamTypeInteger2String(Integer.MAX_VALUE), TileParam.PARAM_TYPE_STRING);
		Assert.assertEquals(DataFormatUtils.tileParamTypeInteger2String(null), TileParam.PARAM_TYPE_STRING);
	}

	@Test(groups = { "s2" })
	public void testTileParamTypeString2Integer() throws CommonFunctionalException
	{
		Assert.assertEquals(DataFormatUtils.tileParamTypeString2Integer(TileParam.PARAM_TYPE_STRING),
				TileParam.PARAM_TYPE_CODE_STRING);
		Assert.assertEquals(DataFormatUtils.tileParamTypeString2Integer(TileParam.PARAM_TYPE_BOOLEAN),
				TileParam.PARAM_TYPE_CODE_BOOLEAN);
		Assert.assertEquals(DataFormatUtils.tileParamTypeString2Integer(TileParam.PARAM_TYPE_NUMBER),
				TileParam.PARAM_TYPE_CODE_NUMBER);
		Assert.assertEquals(DataFormatUtils.tileParamTypeString2Integer(null), TileParam.PARAM_TYPE_CODE_STRING);
		try {
			Assert.assertEquals(DataFormatUtils.tileParamTypeString2Integer("NotExistingType"), TileParam.PARAM_TYPE_CODE_STRING);
			Assert.fail("Fail: expecting CommonFunctionalException here");
		}
		catch (CommonFunctionalException e) {
			LOGGER.info("context",e);
			// this is the expected exception
		}
	}

	@Test(groups = { "s2" })
	public void testTimestamp2Date()
	{
		long current = System.currentTimeMillis();
		Timestamp tsp = new Timestamp(current);
		Date date = DataFormatUtils.timestamp2Date(tsp);
		Assert.assertEquals(date.getTime(), current);
	}
}
