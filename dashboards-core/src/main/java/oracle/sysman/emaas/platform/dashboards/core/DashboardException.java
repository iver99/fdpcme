package oracle.sysman.emaas.platform.dashboards.core;

public class DashboardException extends Exception {
	private static final long serialVersionUID = 7545894221209380053L;

	public DashboardException(Throwable t) {
		super(t);
	}
	
	public DashboardException(String message, Throwable t) {
		super(message, t);
	}
	
	public DashboardException(String message) {
		super(message);
	}
}
