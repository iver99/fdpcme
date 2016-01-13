package oracle.sysman.emaas.platform.dashboards.core.util;

import java.util.Arrays;

import oracle.sysman.emSDK.emaas.platform.tenantmanager.model.metadata.ApplicationEditionConverter;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class TenantSubscriptionUtilTest
{	// @formatter:off
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
			+ "            \"keys\": [" + "                {"
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
			+ "            \"keys\": [" + "                {"
			+ "                    \"name\": \"NodeFqhn\""
			+ "                }"
			+ "            ]"
			+ "        }"
			+ "    ],"
			+ "    \"count\": 12"
			+ "}";

	// @formatter:off
	private final static String TENANT_LOOKUP_RESULT = "{"
			+ "    \"total\": 1,"
			+ "    \"items\": ["
			+ "        {"
			+ "            \"domainUuid\": \"58bfe535-7134-4cae-be4a-d6d3dcfdb4d8\","
			+ "            \"domainName\": \"TenantApplicationMapping\","
			+ "            \"uuid\": \"ea395485-3981-4fc3-94c0-900523f3ebd0\","
			+ "            \"canonicalUrl\": \"http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains/58bfe535-7134-4cae-be4a-d6d3dcfdb4d8/lookups/ea395485-3981-4fc3-94c0-900523f3ebd0\","
			+ "            \"keys\": ["
			+ "                {"
			+ "                    \"name\": \"opcTenantId\","
			+ "                    \"value\": \"emaastesttenant1\""
			+ "                }" + "            ],"
			+ "            \"values\": ["
			+ "                {"
			+ "                    \"opcTenantId\": \"emaastesttenant1\","
			+ "                    \"applicationNames\": \"APM,LogAnalytics,ITAnalytics\""
			+ "                }"
			+ "            ],"
			+ "            \"hash\": -2083815310"
			+ "        }"
			+ "    ],"
			+ "    \"count\": 1"
			+ "}";
	// @formatter:on

	private static final Link link = new Link();

	static {
		link.withHref("http://den00zyr.us.oracle.com:7007/naming/entitynaming/v1/domains");
		link.withRel("");
	}

	@Test
	public void testGetTenantSubscribedServices(@Mocked RegistryLookupUtil anyUtil, @Mocked final RestClient anyClient)
	{
		new Expectations() {
			{
				RegistryLookupUtil.getServiceInternalLink(anyString, anyString, anyString, anyString);
				result = link;

				anyClient.get(anyString, anyString);
				returns(ENTITY_NAMING_DOMAIN, TENANT_LOOKUP_RESULT);
			}
		};
		List<String> services = TenantSubscriptionUtil.getTenantSubscribedServices("emaastesttenant1");
		Assert.assertEquals(Arrays.asList("APM", "LogAnalytics", "ITAnalytics"), services);
	}
	
	@Test(groups = { "s1" })
	public void testIsAPMServiceOnly()
	{
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(null));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { "APM", "ITA" })));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { null })));
		Assert.assertFalse(TenantSubscriptionUtil.isAPMServiceOnly(Arrays.asList(new String[] { "test" })));
		Assert.assertTrue(TenantSubscriptionUtil.isAPMServiceOnly(Arrays
				.asList(new String[] { ApplicationEditionConverter.ApplicationOPCName.APM.toString() })));
	}
}
