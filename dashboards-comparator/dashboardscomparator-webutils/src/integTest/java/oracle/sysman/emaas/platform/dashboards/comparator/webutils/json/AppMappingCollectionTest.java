package oracle.sysman.emaas.platform.dashboards.comparator.webutils.json;

import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class AppMappingCollectionTest {
    @Test
    public void testAppMappingCollection(){
        AppMappingCollection app=new AppMappingCollection();
        app.getCount();
        app.getItems();
        app.getTotal();

        app.setCount(1);
        app.setItems(new ArrayList<AppMappingEntity>());
        app.setTotal(1);
    }
}
