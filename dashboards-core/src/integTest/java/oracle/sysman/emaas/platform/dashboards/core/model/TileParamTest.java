package oracle.sysman.emaas.platform.dashboards.core.model;

import java.math.BigDecimal;

import oracle.sysman.emaas.platform.dashboards.core.exception.functional.CommonFunctionalException;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class TileParamTest
{

	@Test(groups = { "s2" })
	public void testGetValue()
	{
		//Logger logger = EMTestLogger.getLogger("testGetValue");

		TileParam tp = new TileParam();
		tp.setType(TileParam.PARAM_TYPE_STRING);
		tp.setStringValue("test");
		Assert.assertEquals("test", tp.getValue(), tp.toString());
		//logger.info("Values inside TileParam is: " + tp.toString());

		tp.setType(null);
		BigDecimal bd = new BigDecimal(1024);
		tp.setNumberValue(bd);
		Assert.assertEquals(bd.toString(), tp.getValue(), tp.toString());

		tp.setType(TileParam.PARAM_TYPE_BOOLEAN);
		tp.setLongValue(33L);
		Assert.assertEquals(tp.getValue(), Boolean.TRUE.toString(), tp.toString());
		tp.setNumberValue(new BigDecimal(-33));
		Assert.assertEquals(tp.getValue(), Boolean.FALSE.toString(), tp.toString());
		tp.setNumberValue(null);
		Assert.assertEquals(tp.getValue(), Boolean.FALSE.toString(), tp.toString());

		tp.setType("Unknown type");
		Assert.assertNull(tp.getValue());
	}

	@Test(groups = { "s2" })
	public void testSetValue() throws CommonFunctionalException
	{
		//Logger logger = EMTestLogger.getLogger("testSetValue");

		TileParam tp = new TileParam();
		tp.setType(TileParam.PARAM_TYPE_NUMBER);
		tp.setValue(null);
		Assert.assertEquals(Long.valueOf(0), tp.getLongValue(), tp.toString());

		tp.setValue("3");
		Assert.assertEquals(Long.valueOf(3), tp.getLongValue(), tp.toString());

		try {
			tp.setValue("abcde");
			Assert.fail("Fail: invalid string for number type");
		}
		catch (CommonFunctionalException e) {
			// expected exception
		}

		tp.setType(TileParam.PARAM_TYPE_BOOLEAN);
		tp.setValue("true");
		Assert.assertEquals(tp.getValue(), Boolean.TRUE.toString(), tp.toString());
		tp.setValue("TRUE");
		Assert.assertEquals(tp.getValue(), Boolean.TRUE.toString(), tp.toString());
		tp.setValue(null);
		Assert.assertEquals(tp.getValue(), Boolean.FALSE.toString(), tp.toString());
		tp.setValue("false");
		Assert.assertEquals(tp.getValue(), Boolean.FALSE.toString(), tp.toString());
		tp.setValue("abc");
		Assert.assertEquals(tp.getValue(), Boolean.FALSE.toString(), tp.toString());
	}
}
