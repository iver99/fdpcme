package oracle.sysman.emaas.platform.dashboards.comparator.webutils.json;

import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class DomainsEntityTest {

    @Test
    public void testDomainsEntity(){
        DomainsEntity dse=new DomainsEntity();
        dse.getTotal();
        dse.getItems();
        dse.getCount();
        dse.setCount(1);
        dse.setItems(new ArrayList<DomainEntity>());
        dse.setTotal(1);
    }
}
