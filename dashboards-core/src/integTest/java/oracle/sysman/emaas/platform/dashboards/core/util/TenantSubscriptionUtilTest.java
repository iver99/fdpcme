package oracle.sysman.emaas.platform.dashboards.core.util;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.oracle.emaas.platform.emcpdf.cache.CacheManager;
import com.oracle.emaas.platform.emcpdf.cache.Tenant;
import com.oracle.emaas.platform.emcpdf.cache.util.CacheConstants;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;
import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;
import oracle.sysman.emaas.platform.dashboards.core.restclient.DomainsEntity;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil.RestClient;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtilTest
{
	private static final String ENTITY_NAMING_DOMAINS_URL = "http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains";
	private static final String TENANT_LOOKUP_URL = "http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/58bfe535-7134-4cae-be4a-d6d3dcfdb4d8/lookups?opcTenantId=emaastesttenant1";

	// @formatter:off
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

	// @formatter:off

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesEmptyAppMappingEntityS2(@Mocked RegistryLookupUtil anyUtil,
			@Mocked final RestClient anyClient) throws IOException
	{
		final Link link = new Link();

		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;

				anyClient.get(anyString, anyString);
				returns(ENTITY_NAMING_DOMAIN, TENANT_LOOKUP_RESULT_EMPTY_APP_MAPPING_ENTITY);
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		Assert.assertTrue(services == null || services.isEmpty());
		Assert.assertEquals(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { "APM" })), true);
	}

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesEmptyAppMappingJsonS2(@Mocked RegistryLookupUtil anyUtil,
			@Mocked final RestClient anyClient) throws IOException
	{
		final Link link = new Link();

		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;

				anyClient.get(anyString, anyString);
				returns(ENTITY_NAMING_DOMAIN, "");
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		//Assert.assertNull(services);
		Assert.assertTrue(services == null || services.isEmpty());
	}

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesEmptyAppMappingsS2(@Mocked RegistryLookupUtil anyUtil,
			@Mocked final RestClient anyClient) throws IOException
	{
		final Link link = new Link();

		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;

				anyClient.get(anyString, anyString);
				returns(ENTITY_NAMING_DOMAIN, TENANT_LOOKUP_RESULT_EMPTY_APP_MAPPINGS);
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		//Assert.assertNull(services);
		Assert.assertTrue(services == null || services.isEmpty());
	}

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesEmptyDomainS2(@Mocked RegistryLookupUtil anyUtil,
			@Mocked final RestClient anyClient)
	{
		final Link link = new Link();

		link.withHref("");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		//Assert.assertNull(services);
		Assert.assertTrue(services == null || services.isEmpty());
	}

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesEmptyDomainsEntityS2(@Mocked RegistryLookupUtil anyUtil,
			@Mocked final RestClient anyClient, @Mocked final JsonUtil anyJsonUtil, @Mocked final DomainsEntity anyDomainsEntity)
			throws IOException
	{
		final Link link = new Link();

		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;
				new RestClient();
				anyClient.get(anyString, anyString);
				JsonUtil.buildNormalMapper();
				result = anyJsonUtil;
				anyJsonUtil.fromJson(anyString, DomainsEntity.class);
				result = anyDomainsEntity;
				anyDomainsEntity.getItems();
				result = null;
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		//Assert.assertNull(services);
		Assert.assertTrue(services == null || services.isEmpty());
	}

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesEmptyTenantAppUrlS2(@Mocked RegistryLookupUtil anyUtil,
			@Mocked final RestClient anyClient) throws IOException
	{
		final Link link = new Link();

		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;

				anyClient.get(anyString, anyString);
				returns(ENTITY_NAMING_DOMAIN_EMPTY_TENANT_APP_URL, TENANT_LOOKUP_RESULT);
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		//Assert.assertNull(services);
		Assert.assertTrue(services == null || services.isEmpty());
	}

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesIOExceptionS2(@Mocked RegistryLookupUtil anyUtil,
			@Mocked final RestClient anyClient, @Mocked final JsonUtil anyJsonUtil) throws IOException
	{
		final Link link = new Link();

		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;
				new RestClient();
				anyClient.get(anyString, anyString);
				JsonUtil.buildNormalMapper();
				result = anyJsonUtil;
				anyJsonUtil.fromJson(anyString, DomainsEntity.class);
				result = new IOException();
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		//Assert.assertNull(services);
		Assert.assertTrue(services == null || services.isEmpty());
	}

	@Test(groups = { "s2" })
	public void testgetTenantSubscribedServicesNullTenantS2()
	{
		List<String> rtn = TenantSubscriptionUtil.getTenantSubscribedServices(null);
		//Assert.assertNull(rtn);
		Assert.assertTrue(rtn == null || rtn.isEmpty());
	}

	@Test(groups = { "s2" })
	public void testGetTenantSubscribedServicesS2(@Mocked RegistryLookupUtil anyUtil, @Mocked final RestClient anyClient)
	{
		final Link link = new Link();

		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
		new Expectations() {
			{
				Deencapsulation.setField(TenantSubscriptionUtil.class, "IS_TEST_ENV", null);

				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;

				anyClient.get(anyString, anyString);
				returns(ENTITY_NAMING_DOMAIN, TENANT_LOOKUP_RESULT);
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		Assert.assertEquals(services, Arrays.asList("APM", "LogAnalytics", "ITAnalytics"));
	}

	@Test(groups = { "s1" })
	public void testIsAPMServiceOnly()
	{
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(null));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList("APM", "ITA")));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { null })));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList("test")));
		Assert.assertTrue(TenantSubscriptionUtil.isAPMServiceOnly(Arrays
				.asList(ApplicationEditionConverter.ApplicationOPCName.APM.toString())));
	}

	@Test(groups = { "s2" })
	public void testRestClientGetNull()
	{
		String res = new TenantSubscriptionUtil.RestClient().get(null, null);
		Assert.assertNull(res);
	}

	@Test(groups = { "s2" })
	public void testRestClientGetNullAuthS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
			@Mocked final RegistrationManager anyRegistrationManager, @Mocked final URI anyUri,
			@Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
			@Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder, @Mocked final StringUtil anyStringUtil)
			throws Exception
	{
		new NonStrictExpectations() {
			{
				new DefaultClientConfig();
				Client.create(anyClientConfig);
				StringUtil.isEmpty(anyString);
				result = false;
			}
		};
		new TenantSubscriptionUtil.RestClient().get("http://test.link.com", "emaastesttenant1");
		new Verifications() {
			{
				RegistrationManager.getInstance().getAuthorizationToken();
				UriBuilder.fromUri(anyString).build();
				anyClient.resource(anyUri).header(anyString, any);
				anyBuilder.get(String.class);
			}
		};
	}

	@Test(groups = { "s2" })
	public void testRestClientGetS2(@Mocked final DefaultClientConfig anyClientConfig, @Mocked final Client anyClient,
			@Mocked final RegistrationManager anyRegistrationManager, @Mocked final URI anyUri,
			@Mocked final UriBuilder anyUriBuilder, @Mocked final MediaType anyMediaType,
			@Mocked final com.sun.jersey.api.client.WebResource.Builder anyBuilder)
	{
		new NonStrictExpectations() {
			{
				new DefaultClientConfig();
				Client.create(anyClientConfig);
			}
		};
		new TenantSubscriptionUtil.RestClient().get("http://test.link.com", "emaastesttenant1");
		new Verifications() {
			{
				RegistrationManager.getInstance().getAuthorizationToken();
				UriBuilder.fromUri(anyString).build();
				anyClient.resource(anyUri).header(anyString, any);
				anyBuilder.get(String.class);
			}
		};
	}
	
	private void cleanCache(){

		CacheManager cm = CacheManager.getInstance();
		Tenant cacheTenant = new Tenant("emaastesttenant1");
		cm.removeCacheable(cacheTenant, CacheConstants.CACHES_SUBSCRIBED_SERVICE_CACHE,
				CacheConstants.LOOKUP_CACHE_KEY_SUBSCRIBED_APPS);
		cm.removeCacheable(cacheTenant, CacheConstants.CACHES_DOMAINS_DATA_CACHE,
				ENTITY_NAMING_DOMAINS_URL);
		cm.removeCacheable(cacheTenant, CacheConstants.CACHES_DOMAINS_DATA_CACHE,
				TENANT_LOOKUP_URL);
	}
	
	@AfterMethod(groups = { "s2" })
	public void afterMethod()
	{
		cleanCache();
	}

	@BeforeMethod(groups = { "s2" })
	public void beforeMethod() throws Exception
	{
		cleanCache();
	}
}
