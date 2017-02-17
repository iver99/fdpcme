package oracle.sysman.emaas.platform.dashboards.webutils.timer;

import oracle.sysman.emaas.platform.dashboards.webutils.ParallelThreadPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.management.Notification;
import javax.management.NotificationListener;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by chehao on 2017/2/17 13:55.
 * This class is for logging thread pool status on Dashboard-API
 */
public class ThreadPoolStatusNotification implements NotificationListener {
    private static final Logger LOGGER = LogManager.getLogger(ThreadPoolStatusNotification.class);

    @Override
    public void handleNotification(Notification notification, Object handback) {
        //check thread pool status
        ThreadPoolExecutor pool = ParallelThreadPool.getThreadPool();
        LOGGER.info("Dashboard-API thread pool status: Active count is {}, " +
                        "core pool size is {}, " +
                        "largest pool size is {}, " +
                        "maximum pool size is {}, " +
                        "pool size is {}, " +
                        "completed task count is {}, " +
                        "queue size is {}",
                        pool.getActiveCount(),
                        pool.getCorePoolSize(),
                        pool.getLargestPoolSize(),
                        pool.getMaximumPoolSize(),
                        pool.getPoolSize(),
                        pool.getCompletedTaskCount(),
                        pool.getQueue().size());

    }
}
