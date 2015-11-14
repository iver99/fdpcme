package oracle.sysman.emaas.platform.uifwk.ui.webutils.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class StringUtilTest
{
	@Test
	public void testIsEmpty()
	{
		Assert.assertTrue(StringUtil.isEmpty(null));
		Assert.assertTrue(StringUtil.isEmpty(""));
		Assert.assertTrue(StringUtil.isEmpty("   "));
		Assert.assertFalse(StringUtil.isEmpty("TestStringNotEmpty"));
	}
}
