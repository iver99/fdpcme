package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import oracle.sysman.emaas.platform.dashboards.entity.customizer.EmsPreferenceRedirector;

import org.eclipse.persistence.annotations.AdditionalCriteria;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.QueryRedirectors;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@NamedQueries({
    @NamedQuery(name = "EmsPreference.findAll", query = "select o from EmsPreference o where o.userName = :username"),
    @NamedQuery(name = "EmsPreference.removeAll", query = "delete from EmsPreference o where o.userName = :username")
})
@Table(name = "EMS_PREFERENCE")
@IdClass(EmsPreferencePK.class)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "curTenantId", length = 32, primaryKey = true)
@AdditionalCriteria("this.deleted = '0'")
@QueryRedirectors(insert = EmsPreferenceRedirector.class, delete = EmsPreferenceRedirector.class)
public class EmsPreference implements Serializable
{
	private static final long serialVersionUID = 5177176379267126865L;
	@Id
	@Column(name = "PREF_KEY", nullable = false, length = 256)
	private String prefKey;
	@Column(name = "PREF_VALUE", nullable = false, length = 256)
	private String prefValue;
	@Id
	@Column(name = "USER_NAME", nullable = false, length = 128)
	private String userName;
	@Column(name = "DELETED", nullable = false, length = 1)
	private Boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE")
    private Date creationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;
    
    @Column(name = "TENANT_ID", nullable = false, length = 32, insertable = false, updatable = false)
    private Long tenantId;

	public EmsPreference()
	{
		deleted = Boolean.FALSE;
	}

	public EmsPreference(String prefKey, String prefValue, String userName)
	{
		this();
		this.prefKey = prefKey;
		this.prefValue = prefValue;
		this.userName = userName;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted()
	{
		return deleted;
	}

	public String getPrefKey()
	{
		return prefKey;
	}

	public String getPrefValue()
	{
		return prefValue;
	}

	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(Boolean deleted)
	{
		this.deleted = deleted;
	}

	public void setPrefKey(String prefKey)
	{
		this.prefKey = prefKey;
	}

	public void setPrefValue(String prefValue)
	{
		this.prefValue = prefValue;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

    /**
     * @return the creationDate
     */
    public Date getCreationDate()
    {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }

    /**
     * @return the lastModificationDate
     */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate the lastModificationDate to set
     */
    public void setLastModificationDate(Date lastModificationDate)
    {
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
