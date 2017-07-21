/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.ws.rest.thread;

/**
 * @author reliang
 *
 */
public abstract class MetadataRefreshRunnable implements Runnable {
    protected String serviceName;
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
