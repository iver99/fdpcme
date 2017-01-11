package oracle.sysman.emaas.platform.dashboards.comparator.webutils.json;

import org.testng.annotations.Test;

import java.util.ArrayList;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class AppMappingCollectionTest {
    private AppMappingCollection appMappingCollection = new AppMappingCollection();
    @Test
    public void testAppMappingCollection(){
        appMappingCollection.setCount(1);
        appMappingCollection.setTotal(1);
        appMappingCollection.setItems(new ArrayList<AppMappingEntity>());

        appMappingCollection.getCount();
        appMappingCollection.getItems();
        appMappingCollection.getTotal();
    }

    private AppMappingEntity appMappingEntity = new AppMappingEntity();
    private AppMappingEntity.AppMappingKey appMappingKey = new AppMappingEntity.AppMappingKey();
    private AppMappingEntity.AppMappingValue appMappingValue = new AppMappingEntity.AppMappingValue();
    @Test
    public void testAppMappingEntitySetters(){
        appMappingEntity.setCanonicalUrl("url");
        appMappingEntity.getCanonicalUrl();
        appMappingEntity.setDomainName("domainName");
        appMappingEntity.getDomainName();
        appMappingEntity.setDomainUuid("uuid");
        appMappingEntity.getDomainUuid();
        appMappingEntity.setHash(1L);
        appMappingEntity.getHash();
        appMappingEntity.setKeys(new ArrayList<AppMappingEntity.AppMappingKey>());
        appMappingEntity.getKeys();
        appMappingEntity.setUuid("uuid");
        appMappingEntity.getUuid();
        appMappingEntity.setValues(new ArrayList<AppMappingEntity.AppMappingValue>());
        appMappingEntity.getValues();
        appMappingKey.setName("name");
        appMappingKey.getName();
        appMappingKey.setValue("value");
        appMappingKey.getValue();
        appMappingValue.setApplicationNames("applicationname");
        appMappingValue.getApplicationNames();
        appMappingValue.setOpcTenantId("tenantId");
        appMappingValue.getOpcTenantId();
    }

    private DomainEntity domainEntity = new DomainEntity();
    private DomainEntity.DomainKeyEntity domainKeyEntity = new DomainEntity.DomainKeyEntity();
    @Test
    public void testDomainEntitySetters(){
        domainEntity.setDomainName("domianName");
        domainEntity.getDomainName();
        domainEntity.setCanonicalUrl("url");
        domainEntity.getCanonicalUrl();
        domainEntity.setKeys(new ArrayList<DomainEntity.DomainKeyEntity>());
        domainEntity.getKeys();
        domainEntity.setUuid("uuid");
        domainEntity.getUuid();
        domainKeyEntity.setName("name");
        domainKeyEntity.getName();

    }
    private DomainsEntity domainsEntity = new DomainsEntity();
    @Test
    public void testDomainsEntitySetters(){
        domainsEntity.setCount(1);
        domainsEntity.getCount();
        domainsEntity.setItems(new ArrayList<DomainEntity>());
        domainsEntity.getItems();
        domainsEntity.setTotal(1);
        domainsEntity.getTotal();
    }
}
