package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.BasicServiceMalfunctionException;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.tenant.TenantIdProcessor;
import oracle.sysman.emaas.platform.dashboards.core.DashboardManager;
import oracle.sysman.emaas.platform.dashboards.core.model.Dashboard;
import oracle.sysman.emaas.platform.dashboards.webutils.dependency.DependencyStatus;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification.WidgetNotificationType;
import oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification.WidgetNotifyEntity;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.Test;

import javax.servlet.annotation.MultipartConfig;

import java.io.IOException;

import static org.testng.Assert.*;

@Test(groups = {"s2"})
public class WidgetNotificationAPITest_S2 {
    private WidgetNotificationAPI widgetNotificationAPI = new WidgetNotificationAPI();
    @Mocked
    DependencyStatus dependencyStatus;
    @Mocked
    TenantIdProcessor tenantIdProcessor;
    @Mocked
    WidgetNotifyEntity widgetNotifyEntity;
    @Test
    public void testNotifyWidgetChangedOrDeleted() throws BasicServiceMalfunctionException {
        JSONObject data = new JSONObject();
        new Expectations(){
            {
                DependencyStatus.getInstance();
                result = dependencyStatus;
                dependencyStatus.isEntityNamingUp();
                result = true;
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = 1L;
                widgetNotifyEntity.getType();
                result = WidgetNotificationType.UPDATE_NAME;
                widgetNotifyEntity.getUniqueId();
                result = 1L;
            }
        };
        widgetNotificationAPI.notifyWidgetChangedOrDeleted("tenantIdParam", data);
    }

    @Mocked
    DashboardManager dashboardManager;
    @Test
    public void testNotifyWidgetChangedOrDeleted2() throws BasicServiceMalfunctionException {
        JSONObject data = new JSONObject();
        new Expectations(){
            {
                DependencyStatus.getInstance();
                result = dependencyStatus;
                dependencyStatus.isEntityNamingUp();
                result = true;
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = 1L;
                widgetNotifyEntity.getType();
                result = WidgetNotificationType.DELETE;
                widgetNotifyEntity.getUniqueId();
                result = 1L;
                DashboardManager.getInstance();
                result = dashboardManager;
                dashboardManager.updateWidgetDeleteForTilesByWidgetId(anyLong, anyLong);
                result = 1;
            }
        };
        widgetNotificationAPI.notifyWidgetChangedOrDeleted("tenantIdParam", data);
    }

    @Test
    public void testNotifyWidgetChangedOrDeleted_IOException() throws BasicServiceMalfunctionException {
        JSONObject data = new JSONObject();
        new Expectations(){
            {
                DependencyStatus.getInstance();
                result = dependencyStatus;
                dependencyStatus.isEntityNamingUp();
                result = true;
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = 1L;
                widgetNotifyEntity.getType();
                result =  new IOException();
            }
        };
        widgetNotificationAPI.notifyWidgetChangedOrDeleted("tenantIdParam", data);
    }
    @Test
    public void testNotifyWidgetChangedOrDeleted_BasicServiceMalfunctionException() throws BasicServiceMalfunctionException {
        JSONObject data = new JSONObject();
        new Expectations(){
            {
                DependencyStatus.getInstance();
                result = dependencyStatus;
                dependencyStatus.isEntityNamingUp();
                result = true;
                TenantIdProcessor.getInternalTenantIdFromOpcTenantId(anyString);
                result = 1L;
                widgetNotifyEntity.getType();
                result =  new BasicServiceMalfunctionException("test"," test");
            }
        };
        widgetNotificationAPI.notifyWidgetChangedOrDeleted("tenantIdParam", data);
    }

}