package oracle.sysman.emaas.platform.dashboards.comparator.timer;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.ZDTAPI;

public class DashboardComparatorHandlerNotification implements NotificationListener{

	private static String TENANT = "emaastesttenant1";
	private static String USER = "emaastesttenant1.emcsadmin";
	private static String TYPE = "incremental";
	private static int SKIPMINS = 30;
	private final static Logger LOGGER = LogManager.getLogger(DashboardComparatorHandlerNotification.class);
	private ZDTAPI api;

	@Override
	public void handleNotification(Notification arg0, Object arg1) {
		api = new ZDTAPI();
		LOGGER.info("******start to handle comparator*******");
		api.compareRows(TENANT, USER, TYPE, SKIPMINS);
	    api.syncOnDF(TENANT, USER, TYPE);
	    LOGGER.info("*****end to compare and sync *********");
	}

}
