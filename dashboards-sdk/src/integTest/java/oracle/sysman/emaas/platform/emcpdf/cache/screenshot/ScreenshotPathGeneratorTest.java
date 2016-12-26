package oracle.sysman.emaas.platform.emcpdf.cache.screenshot;

import oracle.sysman.emaas.platform.emcpdf.cache.util.ScreenshotPathGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;

public class ScreenshotPathGeneratorTest
{
	@Test
	public void testValidFileName()
	{
		ScreenshotPathGenerator spg = ScreenshotPathGenerator.getInstance();
		Assert.assertTrue(spg.validFileName(BigInteger.valueOf(1L), "123_1.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(BigInteger.valueOf(1L), "123_1.jpg", "11_1.jpg"));
		Assert.assertFalse(spg.validFileName(BigInteger.valueOf(1L), "10_1.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(BigInteger.valueOf(1L), "123_3.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(BigInteger.valueOf(1L), "12s_3.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(BigInteger.valueOf(1L), "3.png", "11_1.png"));
		Assert.assertFalse(spg.validFileName(BigInteger.valueOf(1L), "", "11_1.png"));
		Assert.assertFalse(spg.validFileName(BigInteger.valueOf(1L), null, "11_1.png"));
	}
}
