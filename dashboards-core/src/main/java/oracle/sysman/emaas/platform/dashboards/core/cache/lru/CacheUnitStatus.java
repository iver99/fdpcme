package oracle.sysman.emaas.platform.dashboards.core.cache.lru;


import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by chehao on 2016/10/26.
 */
public class CacheUnitStatus {

    private static final Logger LOGGER=LogManager.getLogger(CacheUnitStatus.class);
    private Long requestCount=0L;
    private Long hitCount=0L;

    private Integer capacity;
    private Integer usage=0;

    private Long evictionCount =0L;
//    private Long eliminateExpirationCount=0L;
//    private Long eliminateCapacityCount=0L;

    public CacheUnitStatus(Integer capacity) {
        this.capacity = capacity;
    }

    public CacheUnitStatus() {
    }

    public Long getEvictionCount() {
        return evictionCount;
    }

    public void setEvictionCount(Long evictionCount) {
        this.evictionCount = evictionCount;
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public Long getHitCount() {
        return hitCount;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public void setHitCount(Long hitCount) {
        this.hitCount = hitCount;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    /**
     * get cache groups hit rate
     * @return
     */
    public String getHitRate(){
    	if(hitCount==0 || requestCount ==0){
    		return "0.0%";
    	}
    	if(hitCount<0 ){
    		LOGGER.error("Cache hit count cannot below 0!");
    		this.setHitCount(0L);
    	}
    	if(requestCount<0){
    		LOGGER.error("Cache request count cannot below 0!");
    		this.setRequestCount(0L);
    	}
    	DecimalFormat df=new DecimalFormat(".##");
		String st=df.format(1.0*hitCount/requestCount);
		double result=Double.valueOf(st)*100;
        return result+"%";
    }

    /**
     * get cache groups usage rate
     * @return
     */
    public String getUsageRate(){
    	if(usage==0 || capacity ==0){
    		return "0.0%";
    	}
    	if(usage<0){
    		LOGGER.error("Cache usage count cannot below 0!");
    		this.setUsage(0);
    	}
    	DecimalFormat df=new DecimalFormat(".##");
		String st=df.format(1.0*usage/capacity);
		double result=Double.valueOf(st)*100;
        return result+"%";
    }

}
