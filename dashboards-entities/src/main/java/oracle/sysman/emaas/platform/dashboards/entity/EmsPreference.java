package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.MultitenantType;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@Entity
@NamedQueries({
		@NamedQuery(name = "EmsPreference.findAll", query = "select o from EmsPreference o where o.userName = :username"),
		@NamedQuery(name = "EmsPreference.removeAll", query = "delete from EmsPreference o where o.userName = :username") })
@Table(name = "EMS_PREFERENCE")
@IdClass(EmsPreferencePK.class)
@Multitenant(MultitenantType.SINGLE_TABLE)
@TenantDiscriminatorColumn(name = "TENANT_ID", contextProperty = "tenant.id", length = 32, primaryKey = true)
public class EmsPreference extends EmBaseEntity implements Serializable
{
	private static final long serialVersionUID = 5177176379267126865L;
	@Id
	@Column(name = "PREF_KEY", nullable = false, length = 256)
	private String prefKey;
	@Column(name = "PREF_VALUE", nullable = false, length = 256)
	private String prefValue;
	//    @Id
	//    @Column(name = "TENANT_ID", nullable = false)
	//    private Long tenantId;
	@Id
	@Column(name = "USER_NAME", nullable = false, length = 128)
	private String userName;

	public EmsPreference()
	{
	}

	public EmsPreference(String prefKey, String prefValue, String userName)
	{
		this.prefKey = prefKey;
		this.prefValue = prefValue;
		this.userName = userName;
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
}
