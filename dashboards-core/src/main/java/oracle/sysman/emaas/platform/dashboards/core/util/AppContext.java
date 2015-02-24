package oracle.sysman.emaas.platform.dashboards.core.util;

import java.util.Locale;

public class AppContext
{
	private static AppContext ctx = new AppContext();

	public static AppContext getInstance()
	{
		return ctx;
	}

	private AppContext()
	{

	}

	//	public String getCurrentUser()
	//	{
	//		return "SYSMAN";
	//	}

	public Locale getLocale()
	{
		return Locale.getDefault();
	}
}
