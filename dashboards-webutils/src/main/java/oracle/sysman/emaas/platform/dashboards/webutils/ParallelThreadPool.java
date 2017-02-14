package oracle.sysman.emaas.platform.dashboards.webutils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.*;

/**
 * Created by chehao on 2017/2/13 22:20.
 */
public class ParallelThreadPool {
    private final static Logger LOGGER = LogManager.getLogger(ParallelThreadPool.class);
    private static ExecutorService pool = null;

    public static  ExecutorService  getThreadPool(){
        if(pool != null){
            return pool;
        }
        init();
        return pool;
    }

    public static void close(){
        LOGGER.info("Dashboards-API: Thread pool with size {} is closing!");
        pool.shutdown();
        if(!pool.isShutdown()){
            LOGGER.error("Error occurred when closing thread pool in Dashboard-API!");
        }
    }

    public static void init(){
        int cpuCore = Runtime.getRuntime().availableProcessors();
        pool = new ThreadPoolExecutor(cpuCore*2,			//Core thread size
                cpuCore*3,									//max thread size
                60,											//keep alived time for idle thread
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10000),	//bounded queue with capacity 10000
                Executors.defaultThreadFactory(),			//default thread factory
                new CustomRejectedExecutionHandler()		//Custom Rejected execution handler
                );
        LOGGER.info("Dashboards-API: Thread pool with core size {} initialized!",cpuCore*2);
    }

    private static class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            LOGGER.fatal("Task queue of thread pool is full! Task is rejected!");
            throw new RejectedExecutionException("Task " + r.toString() +
                    " rejected from " +
                    executor.toString());
        }
    }
}
