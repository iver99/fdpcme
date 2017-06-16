package oracle.sysman.emaas.platform.dashboards.comparator.timer;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import oracle.sysman.emaas.platform.dashboards.comparator.ws.rest.ZDTAPI;

public class DashboardComparatorHandlerNotification implements NotificationListener{
	
	private ZDTAPI api;
	private static String tenant = "";
	private static String user = "";
	
	private final static Logger LOGGER = LogManager.getLogger(DashboardComparatorHandlerNotification.class);


	@Override
	public void handleNotification(Notification arg0, Object arg1) {
	//	api.compareRows("", "", "compareType", 5);
		LOGGER.info("****************DashboardComparatorHandlerNotification**************");
	}

}
