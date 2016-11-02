package oracle.sysman.emaas.platform.dashboards.core.restclient;

import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

@Test(groups = {"s2"})
public class DomainEntityTest {
    private DomainEntity.DomainKeyEntity domainKeyEntity = new DomainEntity.DomainKeyEntity();
    private  DomainEntity domainEntity = new DomainEntity();
    @Test
    public void testGetCanonicalUrl() throws Exception {
        domainKeyEntity.setName("keyName");
        domainKeyEntity.getName();
        domainEntity.setCanonicalUrl("url");
        domainEntity.getCanonicalUrl();
    }

    @Test
    public void testGetDomainName() throws Exception {
        domainEntity.setDomainName("name");
        domainEntity.getDomainName();
    }

    @Test
    public void testGetKeys() throws Exception {
        domainEntity.setKeys(new ArrayList<DomainEntity.DomainKeyEntity>());
        domainEntity.getKeys();
    }

    @Test
    public void testGetUuid() throws Exception {
        domainEntity.setUuid("uuid");
        domainEntity.getUuid();
    }

}