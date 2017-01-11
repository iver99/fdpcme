package oracle.sysman.emaas.platform.dashboards.comparator.webutils.json;

import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class AppMappingEntityTest {

    public void testAppMappingEntity(){
        AppMappingEntity.AppMappingKey obj1=new AppMappingEntity.AppMappingKey();

        AppMappingEntity.AppMappingValue obj2=new AppMappingEntity.AppMappingValue();

        AppMappingEntity app=new AppMappingEntity();

        obj1.getName();
        obj1.getValue();
        obj1.setName("name");
        obj1.setValue("value");

        obj2.getApplicationNames();
        obj2.getOpcTenantId();
        obj2.setApplicationNames("Appname");
        obj2.setOpcTenantId("id");

        app.getCanonicalUrl();
        app.getDomainName();
        app.getDomainUuid();
        app.getHash();
        app.getKeys();
        app.getUuid();
        app.getValues();

        app.setCanonicalUrl("url");
        app.setDomainName("name");
        app.setDomainUuid("uuid");
        app.setHash(1L);
        app.setKeys(new ArrayList<AppMappingEntity.AppMappingKey>());
        app.setValues(new ArrayList<AppMappingEntity.AppMappingValue>());
    }
}
