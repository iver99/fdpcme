package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

public class EmsPreferencePK implements Serializable
{
	/**
	 *
	 */
	private static final long serialVersionUID = 6993351647079180076L;
	private String prefKey;
	private String userName;

	public EmsPreferencePK()
	{
	}

	public EmsPreferencePK(String prefKey, String userName)
	{
		this.prefKey = prefKey;
		this.userName = userName;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof EmsPreferencePK) {
			final EmsPreferencePK otherEmsPreferencePK = (EmsPreferencePK) other;
			final boolean areEqual = otherEmsPreferencePK.prefKey.equals(prefKey)
					&& otherEmsPreferencePK.userName.equals(userName);
			return areEqual;
		}
		return false;
	}

	public String getPrefKey()
	{
		return prefKey;
	}

	public String getUserName()
	{
		return userName;
	}

	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	public void setPrefKey(String prefKey)
	{
		this.prefKey = prefKey;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
}
