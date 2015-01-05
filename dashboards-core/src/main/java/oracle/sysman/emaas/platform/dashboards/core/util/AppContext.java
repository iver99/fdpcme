package oracle.sysman.emaas.platform.dashboards.core.util;

import java.util.Locale;

public class AppContext {
	private static AppContext ctx = new AppContext();
	
	private AppContext() {
		
	}
	
	public static AppContext getInstance() {
		return ctx;
	}
	
	public String getCurrentUser() {
		return "SYSMAN";
	}
	
	public Locale getLocale() {
		return Locale.getDefault();
	}
}
