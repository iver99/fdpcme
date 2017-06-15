package oracle.sysman.emaas.platform.emcpdf.tenant.model;

import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by xiadai on 2016/11/1.
 */
@Test(groups = {"s1"})
public class AppMappingCollectionTest {
    private AppMappingCollection appMappingCollection = new AppMappingCollection();
    @Test
    public void testGetCount() throws Exception {
        appMappingCollection.setCount(10);
        appMappingCollection.getCount();
    }

    @Test
    public void testGetItems() throws Exception {
        appMappingCollection.setItems(new ArrayList<AppMappingEntity>());
        appMappingCollection.getItems();
    }

    @Test
    public void testGetTotal() throws Exception {
        appMappingCollection.getTotal();
        appMappingCollection.setTotal(10);
    }


}