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

import java.util.Properties;

import oracle.sysman.emaas.platform.dashboards.core.persistence.PersistenceManager;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;
import oracle.sysman.emaas.platform.emcpdf.tenant.TenantSubscriptionUtil;

/**
 * @author reliang
 *
 */
public class BaseTest {
    protected static PersistenceManager pm;
    static {
        PersistenceManager.setTestEnv(true);
        UserContext.setCurrentUser("SYSMAN");
        TenantSubscriptionUtil.setTestEnv();
        
        // set db env to system property for running test cases
        Properties props = QAToolUtil.getDbProperties();
        if (props != null) {
            System.setProperty(PersistenceManager.JDBC_PARAM_URL, props.getProperty(QAToolUtil.JDBC_PARAM_URL));
            System.setProperty(PersistenceManager.JDBC_PARAM_USER, props.getProperty(QAToolUtil.JDBC_PARAM_USER));
            System.setProperty(PersistenceManager.JDBC_PARAM_PASSWORD, props.getProperty(QAToolUtil.JDBC_PARAM_PASSWORD));
            System.setProperty(PersistenceManager.JDBC_PARAM_DRIVER, props.getProperty(QAToolUtil.JDBC_PARAM_DRIVER));
        }
    }
}
