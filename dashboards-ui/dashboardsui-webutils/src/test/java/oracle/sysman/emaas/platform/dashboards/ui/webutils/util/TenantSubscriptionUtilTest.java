package oracle.sysman.emaas.platform.dashboards.ui.webutils.util;

import java.util.Arrays;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtilTest
{
	@Test
	public void testIsAPMServiceOnly()
	{
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(null));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { "APM", "ITA" })));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { null })));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { "test" })));
		Assert.assertTrue(TenantSubscriptionUtil.isAPMServiceOnly(Arrays
				.asList(new String[] { ApplicationEditionConverter.ApplicationOPCName.APM.toString() })));
	}
}
