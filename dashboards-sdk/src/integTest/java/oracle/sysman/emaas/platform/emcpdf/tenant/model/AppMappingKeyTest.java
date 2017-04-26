package oracle.sysman.emaas.platform.emcpdf.tenant.model;

import org.testng.annotations.Test;

import java.util.ArrayList;

@Test(groups = {"s1"} )
public class AppMappingKeyTest {
    private AppMappingEntity.AppMappingKey appMappingKey = new AppMappingEntity.AppMappingKey();
    private AppMappingEntity.AppMappingValue appMappingValue = new AppMappingEntity.AppMappingValue();
    private AppMappingEntity appMappingEntity = new AppMappingEntity();
    @Test
    public void testGetName() throws Exception {
        appMappingKey.setName("name");
        appMappingKey.getName();
    }

    @Test
    public void testGetValue() throws Exception {
        appMappingKey.setValue("value");
        appMappingKey.getValue();
    }

    @Test
    public void testSetName() throws Exception {
        appMappingValue.setApplicationNames("names");
        appMappingValue.getApplicationNames();
    }

    @Test
    public void testSetValue() throws Exception {
        appMappingValue.setOpcTenantId("Id");
        appMappingValue.getOpcTenantId();
    }

    @Test
    public void testGetCanonicalUrl(){
        appMappingEntity.setCanonicalUrl("url");
        appMappingEntity.getCanonicalUrl();
    }
    @Test
    public void testGetDomainName(){
        appMappingEntity.setDomainName("domainName");
        appMappingEntity.getDomainName();
    }
    @Test
    public void testGetHash(){
        appMappingEntity.setHash(1L);
        appMappingEntity.getHash();
    }
    @Test
    public void testGetKeys(){
        appMappingEntity.setKeys(new ArrayList<AppMappingEntity.AppMappingKey>());
        appMappingEntity.getKeys();
    }

    @Test
    public void testGetUUid(){
        appMappingEntity.setUuid("uuid");
        appMappingEntity.getUuid();
    }

    @Test
    public void testGetDomainUUid(){
        appMappingEntity.setDomainUuid("uuid");
        appMappingEntity.getDomainUuid();
    }

    @Test
    public void testGetValues(){
        appMappingEntity.setValues(new ArrayList<AppMappingEntity.AppMappingValue>());
        appMappingEntity.getValues();
    }

}