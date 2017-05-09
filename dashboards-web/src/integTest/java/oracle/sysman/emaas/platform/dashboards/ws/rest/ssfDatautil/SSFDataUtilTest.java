package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfDatautil;

import mockit.Mocked;
import oracle.sysman.emaas.platform.emcpdf.rc.RestClient;

import org.testng.annotations.Test;

import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;

public class SSFDataUtilTest {
	
	@Test
	public void testGetSSFData() {
		SSFDataUtil.getSSFData("userTenant", "[\"123456\"]");
	}
	
	@Test
	public void testSaveSSFData() {
		SSFDataUtil.saveSSFData("userTenant", "[\"123456\"]", true);
		SSFDataUtil.saveSSFData("userTenant", "[\"123456\"]", false);
	}
	
	@Test
	public void testGetSSFData(@Mocked final RestClient client, @Mocked final RegistryLookupUtil lookupUtil) {
		SSFDataUtil.getSSFData("userTenant", "[\"123456\"]");
	}
	
	@Test
	public void testSaveSSFData(@Mocked final RestClient client, @Mocked final RegistryLookupUtil lookupUtil) {
		SSFDataUtil.saveSSFData("userTenant", "[\"123456\"]", true);
		SSFDataUtil.saveSSFData("userTenant", "[\"123456\"]", false);
	}

}
