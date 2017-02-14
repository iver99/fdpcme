package oracle.sysman.emaas.platform.dashboards.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author guobaochen
 */
public class DashboardApplicationTypeTest
{

	private static final Logger LOGGER = LogManager.getLogger(DashboardApplicationTypeTest.class);
	@Test(groups = { "s2" })
	public void testFromJsonValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM, DashboardApplicationType.fromJsonValue("APM"));
		Assert.assertEquals(DashboardApplicationType.ITAnalytics, DashboardApplicationType.fromJsonValue("ITAnalytics"));
		Assert.assertEquals(DashboardApplicationType.LogAnalytics, DashboardApplicationType.fromJsonValue("LogAnalytics"));
		Assert.assertEquals(DashboardApplicationType.Monitoring, DashboardApplicationType.fromJsonValue("Monitoring"));
		Assert.assertEquals(DashboardApplicationType.SecurityAnalytics,
				DashboardApplicationType.fromJsonValue("SecurityAnalytics"));
		Assert.assertEquals(DashboardApplicationType.Orchestration, DashboardApplicationType.fromJsonValue("Orchestration"));
		Assert.assertEquals(DashboardApplicationType.Compliance, DashboardApplicationType.fromJsonValue("Compliance"));
		Assert.assertEquals(DashboardApplicationType.SECSE, DashboardApplicationType.fromJsonValue("SECSE"));
		Assert.assertEquals(DashboardApplicationType.SECSMA, DashboardApplicationType.fromJsonValue("SECSMA"));
		Assert.assertEquals(DashboardApplicationType.OMCSE, DashboardApplicationType.fromJsonValue("OMCSE"));
		Assert.assertEquals(DashboardApplicationType.OMCEE, DashboardApplicationType.fromJsonValue("OMCEE"));
		Assert.assertEquals(DashboardApplicationType.OMCLOG, DashboardApplicationType.fromJsonValue("OMCLOG"));
		Assert.assertEquals(DashboardApplicationType.OMC, DashboardApplicationType.fromJsonValue("OMC"));
		Assert.assertEquals(DashboardApplicationType.OSMACC, DashboardApplicationType.fromJsonValue("OSMACC"));
		try {
			DashboardApplicationType.fromJsonValue("Not Existing");
			Assert.fail("Fail: trying to get application type from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
			LOGGER.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testFromValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM, DashboardApplicationType.fromValue(1));
		Assert.assertEquals(DashboardApplicationType.ITAnalytics, DashboardApplicationType.fromValue(2));
		Assert.assertEquals(DashboardApplicationType.LogAnalytics, DashboardApplicationType.fromValue(3));
		Assert.assertEquals(DashboardApplicationType.Monitoring, DashboardApplicationType.fromValue(4));
		Assert.assertEquals(DashboardApplicationType.SecurityAnalytics, DashboardApplicationType.fromValue(5));
		Assert.assertEquals(DashboardApplicationType.Orchestration, DashboardApplicationType.fromValue(6));
		Assert.assertEquals(DashboardApplicationType.Compliance, DashboardApplicationType.fromValue(7));
		try {
			DashboardApplicationType.fromValue(Integer.MAX_VALUE);
			Assert.fail("Fail: trying to get application type from invalid value");
		}
		catch (IllegalArgumentException e) {
			// expected exception
			LOGGER.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testGetJsonValue()
	{
		Assert.assertEquals(DashboardApplicationType.APM_STRING, DashboardApplicationType.APM.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.ITA_SRING, DashboardApplicationType.ITAnalytics.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.LA_STRING, DashboardApplicationType.LogAnalytics.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.MONITORING_STRING, DashboardApplicationType.Monitoring.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.SECURITY_ANALYTICS_STRING,
				DashboardApplicationType.SecurityAnalytics.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.ORCHESTRATION_STRING, DashboardApplicationType.Orchestration.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.COMPLIANCE_STRING, DashboardApplicationType.Compliance.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.SECSMA_STRING, DashboardApplicationType.SECSMA.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.SECSE_STRING, DashboardApplicationType.SECSE.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.OMCEE_STRING, DashboardApplicationType.OMCEE.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.OMCLOG_STRING, DashboardApplicationType.OMCLOG.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.OMCSE_STRING, DashboardApplicationType.OMCSE.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.OMC_STRING, DashboardApplicationType.OMC.getJsonValue());
		Assert.assertEquals(DashboardApplicationType.OSMACC_STRING, DashboardApplicationType.OSMACC.getJsonValue());
	}

	@Test(groups = { "s2" })
	public void testGetBasicServices()
	{
		List<DashboardApplicationType> list1 = Arrays.asList(DashboardApplicationType.Compliance, DashboardApplicationType.APM);
		List<DashboardApplicationType> typeList = DashboardApplicationType.getBasicServiceList(list1);
		Assert.assertEquals(typeList.size(), 2);
		Assert.assertTrue(typeList.get(0).equals(DashboardApplicationType.Compliance) || typeList.get(0).equals(DashboardApplicationType.APM));
		Assert.assertTrue(typeList.get(1).equals(DashboardApplicationType.Compliance) || typeList.get(1).equals(DashboardApplicationType.APM));
		Assert.assertFalse(typeList.get(0).equals(typeList.get(1)));

		List<DashboardApplicationType> list2 = Arrays.asList(DashboardApplicationType.OMCEE, DashboardApplicationType.APM);
		typeList = DashboardApplicationType.getBasicServiceList(list2);
		Assert.assertEquals(typeList, DashboardApplicationType.allBasicService);
		
		list2 = Arrays.asList(DashboardApplicationType.OMCLOG, DashboardApplicationType.LogAnalytics);
		typeList = DashboardApplicationType.getBasicServiceList(list2);
		Assert.assertEquals(typeList, DashboardApplicationType.allBasicService);
		
		list2 = Arrays.asList(DashboardApplicationType.OMCSE, DashboardApplicationType.ITAnalytics);
		typeList = DashboardApplicationType.getBasicServiceList(list2);
		Assert.assertEquals(typeList, DashboardApplicationType.allBasicService);
		
		list2 = Arrays.asList(DashboardApplicationType.SECSE);
		typeList = DashboardApplicationType.getBasicServiceList(list2);
		Assert.assertEquals(typeList, DashboardApplicationType.allBasicService);
		
		list2 = Arrays.asList(DashboardApplicationType.SECSMA, DashboardApplicationType.SECSMA);
		typeList = DashboardApplicationType.getBasicServiceList(list2);
		Assert.assertEquals(typeList, DashboardApplicationType.allBasicService);

		list2 = Arrays.asList(DashboardApplicationType.OMC, DashboardApplicationType.OMC);
		typeList = DashboardApplicationType.getBasicServiceList(list2);
		Assert.assertEquals(typeList, DashboardApplicationType.allBasicService);

		list2 = Arrays.asList(DashboardApplicationType.OSMACC, DashboardApplicationType.OSMACC);
		typeList = DashboardApplicationType.getBasicServiceList(list2);
		Assert.assertEquals(typeList, DashboardApplicationType.allBasicService);
		
		typeList = DashboardApplicationType.getBasicServiceList(null);
		Assert.assertNull(typeList);
		
		typeList = DashboardApplicationType.getBasicServiceList(new ArrayList<DashboardApplicationType>());
		Assert.assertTrue(typeList.isEmpty());
	}
}
