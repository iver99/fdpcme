/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.webutils.services.LoggingServiceManager;
import oracle.sysman.emaas.platform.dashboards.webutils.services.RegistryServiceManager;

import org.testng.annotations.Test;

/**
 * @author aduan
 */
public class AbstractApplicationLifecycleServiceTest
{
	@Test(groups = { "s2" })
	public void testPreStart(@Mocked final LoggingServiceManager anyLsm, @Mocked final RegistryServiceManager anyRsm,
			@Mocked final AbstractApplicationLifecycleService anyAals)
	{
		//		new MockUp<weblogic.i18n.logging.MessageLoggerRegistryListener>() {
		//			@Mock
		//			public void $init()
		//			{
		//				System.out.println("Mock constructor called.");
		//			}
		//		};
		//		new MockUp<NonCatalogLogger>() {
		//			@Mock
		//			public void $init(String name)
		//			{
		//				System.out.println("Mock constructor called.");
		//			}
		//		};
		//		lsm = new LoggingServiceManager();
		//		rsm = new RegistryServiceManager();
		//		AbstractApplicationLifecycleService aals = new AbstractApplicationLifecycleService();
		//		@Mocked final LoggingServiceManager anyLsm, @Mocked final RegistryServiceManager anyRsm
		//		Deencapsulation.setField(AbstractApplicationLifecycleService.class, "logger", new Object());
		try {
			new Expectations() {
				{
					//					new NonCatalogLogger(anyString);
					//					result = null;
				}
			};
			//			AbstractApplicationLifecycleService anyAals1 = new AbstractApplicationLifecycleService(anyLsm);
			anyAals.addApplicationServiceManager(anyLsm);
			anyAals.preStart(null);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
