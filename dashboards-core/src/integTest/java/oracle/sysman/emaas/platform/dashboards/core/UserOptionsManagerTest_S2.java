/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core;

import java.math.BigInteger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.exception.resource.DashboardNotFoundException;
import oracle.sysman.emaas.platform.dashboards.core.model.UserOptions;
import oracle.sysman.emaas.platform.dashboards.core.persistence.DashboardServiceFacade;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.entity.EmsDashboard;
import oracle.sysman.emaas.platform.dashboards.entity.EmsUserOptions;

import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class UserOptionsManagerTest_S2
{
	private static final Logger LOGGER = LogManager.getLogger(UserOptionsManagerTest_S2.class);
	@Test(groups = { "s2" })
	public void testUserOptionsManagerCC(@Mocked final PersistenceManager mockpm, @Mocked final EntityManager mockem,
			@Mocked final EntityManagerFactory mockemf, @Mocked final EntityTransaction mocket,
			@Mocked final DashboardServiceFacade mockdsf) throws DashboardException
	{
		final EmsUserOptions uo = new EmsUserOptions();
		uo.setDashboardId(BigInteger.valueOf(1L));
		uo.setAutoRefreshInterval(0L);
		uo.setUserName("t");
		UserOptions uo1 = new UserOptions();
		uo1.setDashboardId("-1");
		uo1.setAutoRefreshInterval(0L);
		uo1.setUserName("t");

		new NonStrictExpectations() {
			{
				PersistenceManager.getInstance();
				result = mockpm;
				mockpm.getEntityManagerFactory();
				result = mockemf;
				mockemf.createEntityManager();
				result = mockem;
				mockem.getTransaction();
				result = mocket;
				mockem.find((Class<?>) any, any);
				result = null;
				mockdsf.getEmsUserOptions(anyString, BigInteger.valueOf(anyLong));
				result = uo;
				mockdsf.getEmsDashboardById(BigInteger.valueOf(anyLong));
				result = null;
			}
		};

		UserOptionsManager dbcm = UserOptionsManager.getInstance();
		try {
			dbcm.getOptionsById(BigInteger.valueOf(1L), 0L);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context",e);
		}
		try {
			dbcm.getOptionsById(BigInteger.valueOf(-1L), 0L);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context",e);
		}
		try {
			dbcm.saveOrUpdateUserOptions(uo1, 1L);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context",e);
		}
		uo1.setDashboardId("1");
		try {
			dbcm.saveOrUpdateUserOptions(uo1, 1L);
		}
		catch (DashboardNotFoundException e) {
			LOGGER.info("context",e);
		}
		new NonStrictExpectations() {
			{
				mockdsf.getEmsDashboardById(BigInteger.valueOf(anyLong));
				result = new EmsDashboard();
			}
		};
		dbcm.getOptionsById(BigInteger.valueOf(1L), 0L);
		dbcm.saveOrUpdateUserOptions(null, 1L);
		dbcm.saveOrUpdateUserOptions(uo1, 1L);
	}
}
