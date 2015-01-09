package oracle.sysman.emaas.platform.dashboards.core;

public class DashboardException extends Exception
{
	private static final long serialVersionUID = 7545894221209380053L;
	private final Integer errorCode;

	public DashboardException(Integer errorCode, String message)
	{
		super(message);
		this.errorCode = errorCode;
	}

	public DashboardException(Integer errorCode, String message, Throwable t)
	{
		super(message, t);
		this.errorCode = errorCode;
	}

	public DashboardException(Integer errorCode, Throwable t)
	{
		super(t);
		this.errorCode = errorCode;
	}

	public DashboardException(String message)
	{
		super(message);
		errorCode = 0;
	}

	public Integer getErrorCode()
	{
		return errorCode;
	}
}
