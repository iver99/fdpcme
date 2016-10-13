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

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author wenjzhu
 */
public class DBConnectionManagerTest
{
	@Test(groups = { "s2" })
	public void tesIsDatabaseConnectionAvailableException(@Mocked final PersistenceManager mockpm,
			@Mocked final EntityManager mockem, @Mocked final EntityManagerFactory mockemf,
			@Mocked final EntityTransaction mocket, @Mocked final Query mockquery)
	{
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
				mockem.createNativeQuery(anyString);
				result = mockquery;
				mockquery.getSingleResult();
				result = new Exception();
			}
		};

		DBConnectionManager dbcm = DBConnectionManager.getInstance();
		Assert.assertEquals(dbcm.isDatabaseConnectionAvailable(), false);
	}

	@Test(groups = { "s2" })
	public void testIsDatabaseConnectionAvailable(@Mocked final PersistenceManager mockpm, @Mocked final EntityManager mockem,
			@Mocked final EntityManagerFactory mockemf, @Mocked final EntityTransaction mocket, @Mocked final Query mockquery)
	{
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
				mockem.createNativeQuery(anyString);
				result = mockquery;
				mockquery.getSingleResult();
				result = new BigDecimal(1);
			}
		};

		DBConnectionManager dbcm = DBConnectionManager.getInstance();
		Assert.assertEquals(dbcm.isDatabaseConnectionAvailable(), true);
	}

}
