package oracle.sysman.emaas.platform.dashboards.ws.rest.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        pool.shutdown();
    }

    public static void init(){
        int size = Runtime.getRuntime().availableProcessors() * 2;
        LOGGER.info("Dashboards-UI: Thread pool with size {} is initialized!",size);
        //make the number of threads in pool to cpu core * 2

        pool = Executors.newFixedThreadPool(size);
    }
}
