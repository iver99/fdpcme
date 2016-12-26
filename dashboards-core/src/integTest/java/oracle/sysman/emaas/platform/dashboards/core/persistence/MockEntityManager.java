/*
 * Copyright (C) 2016 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */

package oracle.sysman.emaas.platform.dashboards.core.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;

/**
 * @author wenjzhu
 */
public class MockEntityManager implements EntityManager
{
	private Boolean softDeletion = Boolean.FALSE;
	
	@Override
	public void clear()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void close()
	{
		
	}

	@Override
	public boolean contains(Object arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Query createNamedQuery(String arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String arg0, Class<T> arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Query createNativeQuery(String arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Query createNativeQuery(String arg0, @SuppressWarnings("rawtypes") Class arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Query createNativeQuery(String arg0, String arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Query createQuery(String arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> TypedQuery<T> createQuery(String arg0, Class<T> arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void detach(Object arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2, Map<String, Object> arg3)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void flush()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Object getDelegate()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public FlushModeType getFlushMode()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public LockModeType getLockMode(Object arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Metamodel getMetamodel()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public Map<String, Object> getProperties()
	{
		if (this.softDeletion) {
			Map<String, Object> props = new HashMap<String, Object>();
			props.put("soft.deletion.permanent", Boolean.TRUE);
			return props;
		}
		return null;
		//throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> T getReference(Class<T> arg0, Object arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public EntityTransaction getTransaction()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public boolean isOpen()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void joinTransaction()
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void lock(Object arg0, LockModeType arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void lock(Object arg0, LockModeType arg1, Map<String, Object> arg2)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> T merge(T arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void persist(Object arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void refresh(Object arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void refresh(Object arg0, LockModeType arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void refresh(Object arg0, LockModeType arg1, Map<String, Object> arg2)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void refresh(Object arg0, Map<String, Object> arg1)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void remove(Object arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void setFlushMode(FlushModeType arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public void setProperty(String arg0, Object arg1)
	{
		if ("soft.deletion.permanent".equals(arg0)) {
			this.softDeletion = Boolean.TRUE;
			return;
		}
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

	@Override
	public <T> T unwrap(Class<T> arg0)
	{
		throw new UnsupportedOperationException("Unspported mock call. Please change your case.");
	}

}
