package oracle.sysman.emaas.platform.dashboards.ws.rest;

import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification.WidgetNotifyEntity;

public class WidgetNotificationAPITest
{
	private WidgetNotificationAPI api;

	@BeforeMethod
	public void setUp() throws Exception
	{
		api = new WidgetNotificationAPI();
	}

	@Test
	public void testNotifyWidgetChanged(@Mocked final DashboardManager anyDashboardManager,
			@Mocked final TenantIdProcessor anyTenantIdProcessor) throws Exception
	{
		//Test 403 with invalid tenantIdParam DashboardException exception;
		Assert.assertEquals(api.notifyWidgetChanged("", new JSONObject()).getStatus(), 403);
		//Test 403 with invalid tenant
		new Expectations() {
			{
				TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
				result = new BasicServiceMalfunctionException("test", "test", 1);
			}
		};
		Assert.assertEquals(api.notifyWidgetChanged("this is an invalid tenant", new JSONObject()).getStatus(), 403);

		new Expectations() {
			{
				TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
				result = anyLong;
				DashboardManager.getInstance().updateDashboardTilesName(anyLong, anyString, anyLong);
				result = 1;
			}
		};
		WidgetNotifyEntity wne = new WidgetNotifyEntity();
		wne.setUniqueId(1234L);
		wne.setName("test");
		JSONObject jo = new JSONObject();
		jo.put("uniqueId", wne.getUniqueId());
		jo.put("name", wne.getName());
		Assert.assertEquals(api.notifyWidgetChanged("emaastesttenant1", jo).getStatus(), Status.OK.getStatusCode());
	}
}
