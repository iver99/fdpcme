package oracle.sysman.emaas.platform.dashboards.webutils.services;

import oracle.sysman.emaas.platform.dashboards.webutils.ParallelThreadPool;
import oracle.sysman.emaas.platform.dashboards.webutils.timer.ThreadPoolStatusNotification;
import oracle.sysman.emaas.platform.dashboards.webutils.wls.lifecycle.ApplicationServiceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import weblogic.application.ApplicationLifecycleEvent;
import weblogic.management.timer.Timer;

import javax.management.InstanceNotFoundException;
import javax.management.NotificationListener;
import java.util.Date;

/**
 * Created by chehao on 2017/2/17 13:59.
 */
public class ThreadPoolManager implements ApplicationServiceManager {

    private static final Logger LOGGER = LogManager.getLogger(ThreadPoolStatusNotification.class);
    private static final long PERIOD = Timer.ONE_SECOND * 60;
    private Timer timer;
    private Integer notificationId;

    @Override
    public String getName() {
        return "Dashboard-API: Thread Pool Service";
    }

    @Override
    public void postStart(ApplicationLifecycleEvent evt) throws Exception {
        LOGGER.info("Post-starting thread pool.");
        ParallelThreadPool.init();
        timer = new Timer();
        NotificationListener notification = new ThreadPoolStatusNotification();
        timer.addNotificationListener(notification, null, null);
        Date timerTriggerAt = new Date(new Date().getTime() + 10000L);
        notificationId = timer.addNotification("DashboardsServiceThreadPoolTimer", null, notification, timerTriggerAt, PERIOD, 0);
        timer.start();
        LOGGER.info("Timer for dashboard service thread pool checking started. notificationId={}", notificationId);
    }

    @Override
    public void postStop(ApplicationLifecycleEvent evt) throws Exception {
        //do nothing
    }

    @Override
    public void preStart(ApplicationLifecycleEvent evt) throws Exception {
        //do nothing
    }

    @Override
    public void preStop(ApplicationLifecycleEvent evt) throws Exception {
        LOGGER.info("Pre-stopping thread pool service");
        try {
            timer.stop();
            timer.removeNotification(notificationId);
            LOGGER.info("Timer for dashboards api thread pool checking stopped. notificationId={}", notificationId);
        }
        catch (InstanceNotFoundException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
        LOGGER.info("Pre-closing thread pool.");
        ParallelThreadPool.close();
        LOGGER.debug("Pre-stopped thread pool");
    }
}
