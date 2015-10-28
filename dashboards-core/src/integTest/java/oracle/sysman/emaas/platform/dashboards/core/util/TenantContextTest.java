package oracle.sysman.emaas.platform.dashboards.core.util;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class TenantContextTest
{
	@Test(groups = { "s1" })
	public void testClearCurrentUser()
	{
		String testTenant = "tenant";
		TenantContext.setCurrentTenant(testTenant);
		Assert.assertEquals(TenantContext.getCurrentTenant(), testTenant);

		// test repeatable set operatio
		TenantContext.setCurrentTenant(testTenant);
		Assert.assertEquals(TenantContext.getCurrentTenant(), testTenant);

		TenantContext.clearCurrentUser();
		Assert.assertNull(TenantContext.getCurrentTenant());

		// test repeatable clear operatio
		TenantContext.clearCurrentUser();
		Assert.assertNull(TenantContext.getCurrentTenant());
	}
}
