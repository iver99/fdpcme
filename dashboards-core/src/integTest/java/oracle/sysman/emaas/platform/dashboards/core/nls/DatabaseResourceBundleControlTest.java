/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.core.nls;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import javax.persistence.EntityManager;

import oracle.sysman.emaas.platform.dashboards.core.BaseTest;
import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundle;
import oracle.sysman.emaas.platform.dashboards.entity.EmsResourceBundlePK;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author reliang
 *
 */
public class DatabaseResourceBundleControlTest extends BaseTest{
    private final static String TEST_SERVICE = "TestService";
    private static EntityManager em;
    
    @BeforeClass
    public void setUp() {
        EmsResourceBundle rb = new EmsResourceBundle();
        rb.setLanguageCode(Locale.US.getLanguage());
        rb.setCountryCode(Locale.US.getCountry());
        rb.setServiceName(TEST_SERVICE);
        rb.setLastModificationDate(new Date());
        rb.setPropertiesFile("hello=world");
        
        em = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        em.getTransaction().begin();
        em.persist(rb);
        em.getTransaction().commit();
    }
    
    @Test
    public void testNewBundle() {
        DatabaseResourceBundleControl rb = new DatabaseResourceBundleControl();
        try {
            DatabaseResourceBundle b = (DatabaseResourceBundle) rb.newBundle(TEST_SERVICE, Locale.US,
                    DatabaseResourceBundleControl.FORMAT_DATABASE.get(0),
                    DatabaseResourceBundleControlTest.class.getClassLoader(), true);
            Assert.assertNotNull(b);
            Assert.assertEquals(b.getObject("hello"), "world");
        }
        catch (IllegalAccessException e) {
        }
        catch (InstantiationException e) {
        }
        catch (IOException e) {
        }
        
    }
    
    @AfterClass
    public void tearDown() {
        EmsResourceBundlePK pk = new EmsResourceBundlePK();
        pk.setLanguageCode(Locale.US.getLanguage());
        pk.setCountryCode(Locale.US.getCountry());
        pk.setServiceName(TEST_SERVICE);
        EmsResourceBundle rb = em.find(EmsResourceBundle.class, pk);
        if(rb != null) {
            em.getTransaction().begin();
            em.remove(rb);
            em.getTransaction().commit();
        }
        
        if (em != null) {
            em.close();
        }
    }
}
