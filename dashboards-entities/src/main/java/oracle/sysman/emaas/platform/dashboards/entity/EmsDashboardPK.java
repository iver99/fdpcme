/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author reliang
 *
 */
public class EmsDashboardPK implements Serializable{
    private static final long serialVersionUID = -8052165914140211820L;
    
    private Long tenantId;
    private BigInteger dashboardId;
    
    public EmsDashboardPK(BigInteger dashboardId, Long tenantId) {
        this.tenantId = tenantId;
        this.dashboardId = dashboardId;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other instanceof EmsDashboardPK) {
            final EmsDashboardPK otherEmsDashboardPK = (EmsDashboardPK) other;
            final boolean isEqual = otherEmsDashboardPK.dashboardId.equals(dashboardId)
                    && otherEmsDashboardPK.tenantId.equals(tenantId);
            return isEqual;
        }
        return false;
    }
    /**
     * @return the tenantId
     */
    public Long getTenantId()
    {
        return tenantId;
    }
    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }
    /**
     * @return the dashboardId
     */
    public BigInteger getDashboardId()
    {
        return dashboardId;
    }
    /**
     * @param dashboardId the dashboardId to set
     */
    public void setDashboardId(BigInteger dashboardId)
    {
        this.dashboardId = dashboardId;
    }

}
