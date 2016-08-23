/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.webutils.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.AbstractApplicationLifecycleService;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;

import org.testng.Assert;
import org.testng.annotations.Test;

import weblogic.application.ApplicationLifecycleEvent;

/**
 * @author aduan
 */
public class DashboardApiLifecycleServiceTest
{
	private final DashboardApiLifecycleService apils = new DashboardApiLifecycleService();
	private static final Logger logger = LogManager.getLogger(DashboardApiLifecycleServiceTest.class);

	@SuppressWarnings("unchecked")
	@Test(groups = { "s2" })
	public void testNewLifecycleInstance()
	{
		List<ApplicationServiceManager> registeredServices = (List<ApplicationServiceManager>) Deencapsulation.getField(apils,
				"registeredServices");
		Assert.assertNotNull(registeredServices);
		Assert.assertTrue(registeredServices.size() == 4);

		Assert.assertEquals(AbstractApplicationLifecycleService.APPLICATION_LOGGER_SUBSYSTEM,
				"oracle.sysman.emaas.platform.dashboards");
		AbstractApplicationLifecycleService aals = new AbstractApplicationLifecycleService(registeredServices.get(0),
				registeredServices.get(1));
		List<ApplicationServiceManager> absRegisteredServices = (List<ApplicationServiceManager>) Deencapsulation.getField(aals,
				"registeredServices");
		Assert.assertNotNull(absRegisteredServices);
		Assert.assertTrue(absRegisteredServices.size() == 2);
	}

	@Test(groups = { "s2" })
	public void testPostStart(@Mocked final AvailabilityServiceManager asm, @Mocked final LoggingServiceManager lsm,
			@Mocked final EMTargetInitializer emti, @Mocked final RegistryServiceManager rsm,
			@Mocked final ApplicationLifecycleEvent evt)
	{
		try {
			new Expectations() {
				{
					asm.postStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					lsm.postStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					emti.postStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					rsm.postStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
				}
			};
			apils.postStart(evt);

			new Expectations() {
				{
					rsm.postStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			apils.postStart(evt);
		}
		catch (Exception e) {
			logger.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testPostStop(@Mocked final AvailabilityServiceManager asm, @Mocked final LoggingServiceManager lsm,
			@Mocked final EMTargetInitializer emti, @Mocked final RegistryServiceManager rsm,
			@Mocked final ApplicationLifecycleEvent evt)
	{
		try {
			new Expectations() {
				{
					asm.postStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					lsm.postStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					emti.postStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					rsm.postStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
				}
			};
			apils.postStop(evt);

			new Expectations() {
				{
					rsm.postStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			apils.postStop(evt);
		}
		catch (Exception e) {
			logger.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testPreStart(@Mocked final AvailabilityServiceManager asm, @Mocked final LoggingServiceManager lsm,
			@Mocked final EMTargetInitializer emti, @Mocked final RegistryServiceManager rsm,
			@Mocked final ApplicationLifecycleEvent evt)
	{
		try {
			new Expectations() {
				{
					asm.preStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					lsm.preStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					emti.preStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					rsm.preStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
				}
			};
			apils.preStart(evt);

			new Expectations() {
				{
					rsm.preStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			apils.preStart(evt);
		}
		catch (Exception e) {
			logger.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testPreStop(@Mocked final AvailabilityServiceManager asm, @Mocked final LoggingServiceManager lsm,
			@Mocked final EMTargetInitializer emti, @Mocked final RegistryServiceManager rsm,
			@Mocked final ApplicationLifecycleEvent evt, @Mocked final PersistenceManager pm)
	{
		try {
			new Expectations() {
				{
					asm.preStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					lsm.preStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					emti.preStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					rsm.preStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = null;
					PersistenceManager.getInstance().closeEntityManagerFactory();
					result = null;
				}
			};
			apils.preStop(evt);

			new Expectations() {
				{
					rsm.preStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			apils.preStop(evt);
		}
		catch (Exception e) {
			logger.info("context",e);
		}
	}
}
