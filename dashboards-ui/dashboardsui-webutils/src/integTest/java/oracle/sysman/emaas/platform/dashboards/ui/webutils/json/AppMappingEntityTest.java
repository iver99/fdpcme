package oracle.sysman.emaas.platform.dashboards.ui.webutils.json;

import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2016/12/1.
 */
@Test(groups = {"s1"})
public class AppMappingEntityTest {
    private AppMappingEntity appMappingEntity = new AppMappingEntity();
    private AppMappingEntity.AppMappingKey appMappingKey = new AppMappingEntity.AppMappingKey();
    private AppMappingEntity.AppMappingValue appMappingValue = new AppMappingEntity.AppMappingValue();

    public void testAppMappingEntitySetters() throws Exception {
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

    private AppMappingCollection appMappingCollection = new AppMappingCollection();
    public void testAppMappingCollectionSetters() throws Exception {
        appMappingCollection.setCount(1);
        appMappingCollection.getCount();
        appMappingCollection.setItems(new ArrayList<AppMappingEntity>());
        appMappingCollection.getItems();
        appMappingCollection.setTotal(1);
        appMappingCollection.getTotal();
    }

    private DomainsEntity domainsEntity = new DomainsEntity();
    public void testDomainsEntitySetters(){
        domainsEntity.setCount(1);
        domainsEntity.getCount();
        domainsEntity.setItems(new ArrayList<DomainEntity>());
        domainsEntity.getItems();
        domainsEntity.setTotal(1);
        domainsEntity.getTotal();
    }

}
