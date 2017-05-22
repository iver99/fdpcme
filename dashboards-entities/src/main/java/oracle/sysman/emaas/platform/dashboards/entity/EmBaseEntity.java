package oracle.sysman.emaas.platform.dashboards.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.AdditionalCriteria;

/**
 * @author reliang
 *
 */
@MappedSuperclass
//@Multitenant(value = MultitenantType.SINGLE_TABLE, includeCriteria = false)
@AdditionalCriteria("this.tenantId = :curTenantId or this.tenantId = -11")
public class EmBaseEntity {
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_DATE")
	private Date creationDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFICATION_DATE")
	private Date lastModificationDate;
	
	@Id
	@Column(name = "TENANT_ID", nullable = false, length = 32/*, insertable = false*/, updatable = false)
	private Long tenantId;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
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
	
}
