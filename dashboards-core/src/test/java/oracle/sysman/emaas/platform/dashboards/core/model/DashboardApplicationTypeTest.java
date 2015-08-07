package oracle.sysman.emaas.platform.dashboards.core.model;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class DashboardApplicationTypeTest
{
	@Test
	public void testFromJsonValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM, DashboardApplicationType.fromJsonValue("APM"));
		Assert.assertEquals(DashboardApplicationType.ITAnalytics, DashboardApplicationType.fromJsonValue("ITAnalytics"));
		Assert.assertEquals(DashboardApplicationType.LogAnalytics, DashboardApplicationType.fromJsonValue("LogAnalytics"));
		try {
			DashboardApplicationType.fromJsonValue("Not Existing");
			Assert.fail("Fail: trying to get application type from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
		}
	}

	@Test
	public void testFromValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM, DashboardApplicationType.fromValue(1));
		Assert.assertEquals(DashboardApplicationType.ITAnalytics, DashboardApplicationType.fromValue(2));
		Assert.assertEquals(DashboardApplicationType.LogAnalytics, DashboardApplicationType.fromValue(3));
		try {
			DashboardApplicationType.fromValue(Integer.MAX_VALUE);
			Assert.fail("Fail: trying to get application type from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
		}
	}

	@Test
	public void testGetJsonValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM_STRING, DashboardApplicationType.APM.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.ITA_SRING, DashboardApplicationType.ITAnalytics.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.LA_STRING, DashboardApplicationType.LogAnalytics.getJsonValue());
	}
}
