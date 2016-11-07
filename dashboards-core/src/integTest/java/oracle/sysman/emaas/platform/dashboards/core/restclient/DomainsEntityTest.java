package oracle.sysman.emaas.platform.dashboards.core.restclient;

import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

@Test(groups = {"s1"})
public class DomainsEntityTest {
    private DomainsEntity domainsEntity = new DomainsEntity();
    @Test
    public void testGetCount() throws Exception {
        domainsEntity.setCount(1);
        domainsEntity.getCount();
    }

    @Test
    public void testGetItems() throws Exception {
        domainsEntity.setItems(new ArrayList<DomainEntity>());
        domainsEntity.getItems();
    }

    @Test
    public void testGetTotal() throws Exception {
        domainsEntity.setTotal(1);
        domainsEntity.getTotal();
    }

}