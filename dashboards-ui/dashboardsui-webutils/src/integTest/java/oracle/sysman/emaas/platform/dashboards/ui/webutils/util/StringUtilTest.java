package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import org.testng.Assert;
import org.testng.annotations.Test;

public class StringUtilTest
{
	@Test(groups = { "s2" })
	public void testIsEmpty()
	{
		Assert.assertFalse(StringUtil.isEmpty("AnyString"));
		Assert.assertFalse(StringUtil.isEmpty("  AnyString  "));
		Assert.assertTrue(StringUtil.isEmpty(null));
		Assert.assertTrue(StringUtil.isEmpty(""));
		Assert.assertTrue(StringUtil.isEmpty("  "));
	}
}
