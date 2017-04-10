package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import mockit.*;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.AppMappingCollection;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.AppMappingEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.DomainEntity;
import oracle.sysman.emaas.platform.dashboards.comparator.webutils.json.DomainsEntity;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 2017/1/11.
 */
@Test(groups = {"s2"})
public class TenantSubscriptionUtilTest {

    private static final String ENTITY_NAMING_DOMAIN = "{"
            + "\"total\": 12,"
            + "\"items\": ["
            + "        {"
            + "            \"uuid\": \"58bfe535-7134-4cae-be4a-d6d3dcfdb4d8\","
            + "            \"domainName\": \"TenantApplicationMapping\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/58bfe535-7134-4cae-be4a-d6d3dcfdb4d8\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"opcTenantId\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"4136781e-d91c-4d69-976e-d7c298c4aca2\","
            + "            \"domainName\": \"EmlacoreTenantDBLogin\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/4136781e-d91c-4d69-976e-d7c298c4aca2\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"tenantid\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"9c3a576f-736e-4d23-b106-e26d4bce8adf\","
            + "            \"domainName\": \"TenantSchemaMapping\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/9c3a576f-736e-4d23-b106-e26d4bce8adf\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"tenantID\""
            + "                },"
            + "                {"
            + "                    \"name\": \"serviceName\""
            + "                },"
            + "                {"
            + "                    \"name\": \"serviceVersion\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"f4de97e5-47b4-4d43-ad33-322429897f1b\","
            + "            \"domainName\": \"CloudDatabaseResources\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/f4de97e5-47b4-4d43-ad33-322429897f1b\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"id\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"915c5cad-a175-4303-9c00-f47bd670cd78\","
            + "            \"domainName\": \"LogAnalyticsSolrOSSResources\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/915c5cad-a175-4303-9c00-f47bd670cd78\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"credentials\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"98d39323-cf21-42e3-ada5-bd1e6247e587\","
            + "            \"domainName\": \"DataServiceDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/98d39323-cf21-42e3-ada5-bd1e6247e587\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"PropertyBag\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"82af926b-6502-49f7-a12d-3d917d6320cc\","
            + "            \"domainName\": \"HAHDFSDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/82af926b-6502-49f7-a12d-3d917d6320cc\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"NameNode\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"9423b075-9a12-4293-bccb-85541df80652\","
            + "            \"domainName\": \"ITADatabaseTenantMapping\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/9423b075-9a12-4293-bccb-85541df80652\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"tenantid\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"7d10280e-c174-48cf-b911-acce1c539daa\","
            + "            \"domainName\": \"zkBackupUtilityDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/7d10280e-c174-48cf-b911-acce1c539daa\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"zookeeperCluster\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"86a7a003-ce0a-4b1a-8fc4-aa2496b6189f\","
            + "            \"domainName\": \"InternalTenantIdMap\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/86a7a003-ce0a-4b1a-8fc4-aa2496b6189f\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"opcTenantId\""
            + "                },"
            + "                {"
            + "                    \"name\": \"internalTenantId\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"2fbb32ea-aece-4fe3-b6df-430bd16a877f\","
            + "            \"domainName\": \"ApplicationShardTenantMapping\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/2fbb32ea-aece-4fe3-b6df-430bd16a877f\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"applicationShard\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"b1f390b3-c2e1-4c1b-9977-5cf133e44a47\","
            + "            \"domainName\": \"HAHDFSClusterDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/b1f390b3-c2e1-4c1b-9977-5cf133e44a47\","
            + "            \"keys\": [" + "                {" + "                    \"name\": \"NodeFqhn\""
            + "                }" + "            ]" + "        }" + "    ]," + "    \"count\": 12" + "}";

    // @formatter:off
    private final static String TENANT_LOOKUP_RESULT = "{"
            + "    \"total\": 1,"
            + "    \"items\": ["
            + "        {"
            + "            \"domainUuid\": \"58bfe535-7134-4cae-be4a-d6d3dcfdb4d8\","
            + "            \"domainName\": \"TenantApplicationMapping\","
            + "            \"uuid\": \"ea395485-3981-4fc3-94c0-900523f3ebd0\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/58bfe535-7134-4cae-be4a-d6d3dcfdb4d8/lookups/ea395485-3981-4fc3-94c0-900523f3ebd0\","
            + "            \"keys\": [" + "                {" + "                    \"name\": \"opcTenantId\","
            + "                    \"value\": \"emaastesttenant1\"" + "                }" + "            ],"
            + "            \"values\": [" + "                {" + "                    \"opcTenantId\": \"emaastesttenant1\","
            + "                    \"applicationNames\": \"APM,LogAnalytics,ITAnalytics\"" + "                }"
            + "            ]," + "            \"hash\": -2083815310" + "        }" + "    ]," + "    \"count\": 1" + "}";
    // @formatter:on
    // @formatter:off
    private static final String ENTITY_NAMING_DOMAIN_EMPTY_TENANT_APP_URL = "{"
            + "\"total\": 12,"
            + "\"items\": ["
            + "        {"
            + "            \"uuid\": \"58bfe535-7134-4cae-be4a-d6d3dcfdb4d8\","
            + "            \"domainName\": \"TenantApplicationMapping\","
            + "            \"canonicalUrl\": \"\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"opcTenantId\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"4136781e-d91c-4d69-976e-d7c298c4aca2\","
            + "            \"domainName\": \"EmlacoreTenantDBLogin\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/4136781e-d91c-4d69-976e-d7c298c4aca2\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"tenantid\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"9c3a576f-736e-4d23-b106-e26d4bce8adf\","
            + "            \"domainName\": \"TenantSchemaMapping\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/9c3a576f-736e-4d23-b106-e26d4bce8adf\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"tenantID\""
            + "                },"
            + "                {"
            + "                    \"name\": \"serviceName\""
            + "                },"
            + "                {"
            + "                    \"name\": \"serviceVersion\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"f4de97e5-47b4-4d43-ad33-322429897f1b\","
            + "            \"domainName\": \"CloudDatabaseResources\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/f4de97e5-47b4-4d43-ad33-322429897f1b\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"id\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"915c5cad-a175-4303-9c00-f47bd670cd78\","
            + "            \"domainName\": \"LogAnalyticsSolrOSSResources\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/915c5cad-a175-4303-9c00-f47bd670cd78\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"credentials\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"98d39323-cf21-42e3-ada5-bd1e6247e587\","
            + "            \"domainName\": \"DataServiceDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/98d39323-cf21-42e3-ada5-bd1e6247e587\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"PropertyBag\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"82af926b-6502-49f7-a12d-3d917d6320cc\","
            + "            \"domainName\": \"HAHDFSDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/82af926b-6502-49f7-a12d-3d917d6320cc\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"NameNode\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"9423b075-9a12-4293-bccb-85541df80652\","
            + "            \"domainName\": \"ITADatabaseTenantMapping\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/9423b075-9a12-4293-bccb-85541df80652\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"tenantid\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"7d10280e-c174-48cf-b911-acce1c539daa\","
            + "            \"domainName\": \"zkBackupUtilityDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/7d10280e-c174-48cf-b911-acce1c539daa\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"zookeeperCluster\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"86a7a003-ce0a-4b1a-8fc4-aa2496b6189f\","
            + "            \"domainName\": \"InternalTenantIdMap\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/86a7a003-ce0a-4b1a-8fc4-aa2496b6189f\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"opcTenantId\""
            + "                },"
            + "                {"
            + "                    \"name\": \"internalTenantId\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"2fbb32ea-aece-4fe3-b6df-430bd16a877f\","
            + "            \"domainName\": \"ApplicationShardTenantMapping\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/2fbb32ea-aece-4fe3-b6df-430bd16a877f\","
            + "            \"keys\": ["
            + "                {"
            + "                    \"name\": \"applicationShard\""
            + "                }"
            + "            ]"
            + "        },"
            + "        {"
            + "            \"uuid\": \"b1f390b3-c2e1-4c1b-9977-5cf133e44a47\","
            + "            \"domainName\": \"HAHDFSClusterDomain\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/b1f390b3-c2e1-4c1b-9977-5cf133e44a47\","
            + "            \"keys\": [" + "                {" + "                    \"name\": \"NodeFqhn\""
            + "                }" + "            ]" + "        }" + "    ]," + "    \"count\": 12" + "}";

    private final static String TENANT_LOOKUP_RESULT_EMPTY_APP_MAPPINGS = "{" + "    \"total\": 0," + "    \"items\": ["
            + "    ]," + "    \"count\": 0" + "}";

    private final static String TENANT_LOOKUP_RESULT_EMPTY_APP_MAPPING_ENTITY = "{"
            + "    \"total\": 1,"
            + "    \"items\": ["
            + "        {"
            + "            \"domainUuid\": \"58bfe535-7134-4cae-be4a-d6d3dcfdb4d8\","
            + "            \"domainName\": \"TenantApplicationMapping\","
            + "            \"uuid\": \"ea395485-3981-4fc3-94c0-900523f3ebd0\","
            + "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/58bfe535-7134-4cae-be4a-d6d3dcfdb4d8/lookups/ea395485-3981-4fc3-94c0-900523f3ebd0\","
            + "            \"keys\": [" + "                {" + "                    \"name\": \"opcTenantId\","
            + "                    \"value\": \"emaastesttenant1\"" + "                }" + "            ],"
            + "            \"values\": [" + "            ]," + "            \"hash\": -2083815310" + "        }" + "    ],"
            + "    \"count\": 1" + "}";

    private final static String SUBSCRIBED_APPS = "{" +
            "    \"applications\": [" +
            "        \"APM\"," +
            "        \"LogAnalytics\"," +
            "        \"ITAnalytics\"" +
            "    ]" +
            "}";

    @Test
    public void testIsAPMService() {
        List<String> services = new ArrayList<>();
        services.add("APM");
        Assert.assertTrue(TenantSubscriptionUtil.isAPMServiceOnly(services));
        services.add("ITA");
        Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(services));
    }

    @Test
    public void testIsMonitoringService() {
        List<String> services = new ArrayList<>();
        services.add("Monitoring");
        Assert.assertTrue(TenantSubscriptionUtil.isMonitoringServiceOnly(services));
        services.add("ITA");
        Assert.assertFalse(TenantSubscriptionUtil.isMonitoringServiceOnly(services));
    }

   /* @Test(groups = {"s2"})
    public void testRestClientGetNull() {
        String res = new RestClient().get(null, null, null);
        org.testng.Assert.assertNull(res);
    }

    @Test(groups = {"s2"})
    public void testRestClientGetNullAuthS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                            @Mocked final RegistrationManager anyRegistrationManager, @Mocked final URI anyUri,
                                            @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                            @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder, @Mocked final StringUtil anyStringUtil)
            throws Exception {
        new NonStrictExpectations() {
            {
                new DefaultClientConfig();
                Client.create(anyClientConfig);
                StringUtil.isEmpty(anyString);
                result = false;
            }
        };
        new RestClient().get("http://test.link.com", "emaastesttenant1", null);
        new Verifications() {
            {
                RegistrationManager.getInstance().getAuthorizationToken();
                UriBuilder.fromUri(anyString).build();
                anyClient.resource(anyUri).header(anyString, any);
                anyBuilder.get(String.class);
            }
        };
    }*/

    /*@Test(groups = {"s2"})
    public void testRestClientGetS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
                                    @Mocked final RegistrationManager anyRegistrationManager, @Mocked final URI anyUri,
                                    @Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
                                    @Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder) throws Exception {
        new NonStrictExpectations() {
            {
                new DefaultClientConfig();
                Client.create(anyClientConfig);
            }
        };
        new RestClient().get("http://test.link.com", "emaastesttenant1", null);
        new Verifications() {
            {
                RegistrationManager.getInstance().getAuthorizationToken();
                UriBuilder.fromUri(anyString).build();
                anyClient.resource(anyUri).header(anyString, any);
                anyBuilder.get(String.class);
            }
        };
    }*/

    @Mocked
    private RegistryLookupUtil registryLookupUtil;
    @Test
    public void testGetTenantSubscribedServices(@Mocked final DefaultClientConfig anyClientConfig,final @Mocked JsonUtil jsonUtil,
                                                final @Mocked InstanceInfo instanceInfo,final @Mocked RestClient rs) throws Exception {
        final Link link = new Link();

        link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
        link.withRel("");
        List<DomainEntity> list=new ArrayList<>();
        final DomainsEntity de=new DomainsEntity();
        DomainEntity entity=new DomainEntity();
        entity.setDomainName("TenantApplicationMapping");
        entity.setCanonicalUrl("url");
        list.add(entity);
        de.setItems(list);
        final AppMappingCollection ac=new AppMappingCollection();
        AppMappingEntity appMappingEntity=new AppMappingEntity();
//        appMappingEntity.setDomainName();
        appMappingEntity.setCanonicalUrl("url");
        final String appMappingJson="appMappingJson";

        List<AppMappingEntity> appMappingEntityList=new ArrayList<>();
        appMappingEntityList.add(appMappingEntity);
        ac.setItems(appMappingEntityList);
        List<AppMappingEntity.AppMappingValue> appMappingValueList=new ArrayList<>();
        appMappingEntity.setValues(appMappingValueList);
        AppMappingEntity.AppMappingValue value=new AppMappingEntity.AppMappingValue();
        value.setOpcTenantId("emaastesttenant1");
        appMappingValueList.add(value);

        new Expectations() {
            {
                registryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, "emaastesttenant1");
                result = link;
                jsonUtil.fromJson(anyString,DomainsEntity.class);
                result=de;
                rs.get(anyString, "emaastesttenant1", anyString);
                result =appMappingJson;
                jsonUtil.fromJson(anyString, AppMappingCollection.class);
                result=ac;


            }
        };
        TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
    }

    @Mocked
    private RegistrationManager registrationManager;
    @Mocked
    private WebResource.Builder builder;

    @Mocked
    private Client client;
    @Mocked
    private URI uri;
    @Mocked
    private WebResource webResource;
    @Mocked
    private UriBuilder uriBuilder;


}

