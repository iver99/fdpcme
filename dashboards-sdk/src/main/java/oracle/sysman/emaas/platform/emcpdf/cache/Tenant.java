package oracle.sysman.emaas.platform.emcpdf.cache;

import java.io.Serializable;

/**
 * Created by chehao on 2016/12/11.
 */
public class Tenant implements Serializable
{
    private String tenantName;
    private Long tenantId;

    public Tenant(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    public Tenant(Long tenantId, String tenantName)
    {
        this.tenantId = tenantId;
        this.tenantName = tenantName;
    }

    public Tenant(String tenantName)
    {
        this.tenantName = tenantName;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tenant)) {
            return false;
        }

        Tenant other = (Tenant) obj;
        if (tenantName == null) {
            if (other.tenantName != null) {
                return false;
            }
        }
        else if (!tenantName.equals(other.tenantName)) {
            return false;
        }

        if (tenantId == null) {
            if (other.tenantId != null) {
                return false;
            }
        }
        else if (!tenantId.equals(other.tenantId)) {
            return false;
        }
        return true;
    }

    /**
     * @return the tenantId
     */
    public Long getTenantId()
    {
        return tenantId;
    }

    /**
     * @return the tenantName
     */
    public String getTenantName()
    {
        return tenantName;
    }

    @Override
    public int hashCode()
    {
        int result = 0;
        if (tenantName != null) {
            result += tenantName.hashCode();
        }
        if (tenantId != null) {
            result += result * 31 + tenantId;
        }
        return result;
    }

    /**
     * @param tenantId
     *            the tenantId to set
     */
    public void setTenantId(Long tenantId)
    {
        this.tenantId = tenantId;
    }

    /**
     * @param tenantName
     *            the tenantName to set
     */
    public void setTenantName(String tenantName)
    {
        this.tenantName = tenantName;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        if (tenantName != null) {
            sb.append("  (TenantName)" + tenantName);
        }
        else {
            sb.append("  (TenantName) null");
        }
        if (tenantId != null) {
            sb.append("  (TenantId)" + tenantId);
        }
        else {
            sb.append("  (TenantId) null");
        }
        return sb.toString();
    }
}

