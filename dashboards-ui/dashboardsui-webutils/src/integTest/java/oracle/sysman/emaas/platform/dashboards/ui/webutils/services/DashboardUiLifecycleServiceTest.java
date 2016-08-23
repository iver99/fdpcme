/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.ui.webutils.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.AbstractApplicationLifecycleService;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.wls.lifecycle.ApplicationServiceManager;

import org.testng.Assert;
import org.testng.annotations.Test;

import weblogic.application.ApplicationLifecycleEvent;

/**
 * @author aduan
 */
public class DashboardUiLifecycleServiceTest
{
	private final DashboardUiLifecycleService uils = new DashboardUiLifecycleService();
	private static final Logger LOGGER = LogManager.getLogger(DashboardUiLifecycleServiceTest.class);
	@SuppressWarnings("unchecked")
	@Test(groups = { "s2" })
	public void testNewLifecycleInstance()
	{
		List<ApplicationServiceManager> registeredServices = (List<ApplicationServiceManager>) Deencapsulation.getField(uils,
				"registeredServices");
		Assert.assertNotNull(registeredServices);
		Assert.assertTrue(registeredServices.size() == 4);

		Assert.assertEquals(AbstractApplicationLifecycleService.APPLICATION_LOGGER_SUBSYSTEM,
				"oracle.sysman.emaas.platform.dashboards.ui");
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
			uils.postStart(evt);

			new Expectations() {
				{
					rsm.postStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			uils.postStart(evt);
		}
		catch (Exception e) {
			LOGGER.info("context",e);
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
			uils.postStop(evt);

			new Expectations() {
				{
					rsm.postStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			uils.postStop(evt);
		}
		catch (Exception e) {
			LOGGER.info("context",e);
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
			uils.preStart(evt);

			new Expectations() {
				{
					rsm.preStart((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			uils.preStart(evt);
		}
		catch (Exception e) {
			LOGGER.info("context",e);
		}
	}

	@Test(groups = { "s2" })
	public void testPreStop(@Mocked final AvailabilityServiceManager asm, @Mocked final LoggingServiceManager lsm,
			@Mocked final EMTargetInitializer emti, @Mocked final RegistryServiceManager rsm,
			@Mocked final ApplicationLifecycleEvent evt)
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
				}
			};
			uils.preStop(evt);

			new Expectations() {
				{
					rsm.preStop((ApplicationLifecycleEvent) any);
					times = 1;
					result = new Exception();
				}
			};
			uils.preStop(evt);
		}
		catch (Exception e) {
			LOGGER.info("context",e);
		}
	}
}
