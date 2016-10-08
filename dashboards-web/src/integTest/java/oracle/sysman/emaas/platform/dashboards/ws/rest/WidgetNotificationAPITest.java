package oracle.sysman.emaas.platform.dashboards.ws.rest;

import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONException;
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
	public void setUp()
	{
		api = new WidgetNotificationAPI();
	}

	@Test
	public void testNotifyWidgetChanged(@Mocked final DashboardManager anyDashboardManager,
			@Mocked final TenantIdProcessor anyTenantIdProcessor) throws BasicServiceMalfunctionException, JSONException
	{
		//Test 403 with invalid tenantIdParam DashboardException exception;
		Assert.assertEquals(api.notifyWidgetChangedOrDeleted("", new JSONObject()).getStatus(), 403);
		//Test 403 with invalid tenant
		new Expectations() {
			{
				TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
				result = new BasicServiceMalfunctionException("test", "test", 1);
			}
		};
		Assert.assertEquals(api.notifyWidgetChangedOrDeleted("this is an invalid tenant", new JSONObject()).getStatus(), 403);

		// test the 'UPDATE_NAME' branch
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
		jo.put("type", "UPDATE_NAME");
		Assert.assertEquals(api.notifyWidgetChangedOrDeleted("emaastesttenant1", jo).getStatus(), Status.OK.getStatusCode());

		// the 'DELETE' widget branch
		new Expectations() {
			{
				TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
				result = anyLong;
				DashboardManager.getInstance().updateWidgetDeleteForTilesByWidgetId(anyLong, anyLong);
				result = 1;
			}
		};
		jo.put("type", "DELETE");
		Assert.assertEquals(api.notifyWidgetChangedOrDeleted("emaastesttenant1", jo).getStatus(), Status.OK.getStatusCode());
	}
}
