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

/**
 * @author reliang
 *
 */
public class EmsDashboardTilePK implements Serializable{
    private static final long serialVersionUID = -8052165914140211820L;
    
    private Long tenantId;
    private String tileId;
    
    public EmsDashboardTilePK(String tileId, Long tenantId) {
        this.tenantId = tenantId;
        this.tileId = tileId;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if (other instanceof EmsDashboardTilePK) {
            final EmsDashboardTilePK otherEmsDashboardTilePK = (EmsDashboardTilePK) other;
            final boolean isEqual = otherEmsDashboardTilePK.tileId.equals(tileId)
                    && otherEmsDashboardTilePK.tenantId.equals(tenantId);
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
     * @return the tileId
     */
    public String getTileId()
    {
        return tileId;
    }
    /**
     * @param tileId the tileId to set
     */
    public void setTileId(String tileId)
    {
        this.tileId = tileId;
    }

}
