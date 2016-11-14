package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification;

import org.testng.annotations.Test;

import java.util.Date;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2016/11/1.
 */
@Test(groups = {"s1"})
public class WidgetNotificationTypeTest {
    private WidgetNotifyEntity widgetNotifyEntity = new WidgetNotifyEntity();
    @Test
    public void testFromName() throws Exception {
        WidgetNotificationType.fromName("type");
        WidgetNotificationType.fromName(null);
    }

    @Test
    public void testGeTAffactedObjects(){
        widgetNotifyEntity.setAffactedObjects(1);
        widgetNotifyEntity.getAffactedObjects();
        widgetNotifyEntity.setName("name");
        widgetNotifyEntity.getName();
        widgetNotifyEntity.setNotifyTime(new Date());
        widgetNotifyEntity.getNotifyTime();
        widgetNotifyEntity.setUniqueId(1L);
        widgetNotifyEntity.setUniqueId(1L);
        widgetNotifyEntity.getUniqueId();
        widgetNotifyEntity.setType(WidgetNotificationType.DELETE);
        widgetNotifyEntity.getType();
    }


}