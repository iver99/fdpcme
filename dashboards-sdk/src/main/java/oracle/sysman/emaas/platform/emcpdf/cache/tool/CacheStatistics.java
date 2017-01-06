package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import oracle.sysman.emaas.platform.emcpdf.cache.util.CacheConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;

/**
 * Created by chehao on 2016/12/26.
 */
public class CacheStatistics {

    Logger LOGGER= LogManager.getLogger(CacheStatistics.class);
    private Long requestCount=0L;
    private Long hitCount=0L;
    private Integer capacity=0;
    private Integer usage=0;
    private Long evictionCount=0L;

    public String getUsageRate() {
        if (usage < 0) {
            LOGGER.warn("Cache usage count cannot below 0!");
            this.setUsage(0);
        }
        if (usage == 0 || capacity == 0) {
            return CacheConstants.ZERO_PERCENTAGE;
        }
        if (usage >= capacity) {
            return CacheConstants.ONE_HUNDRED_PERCENTAGE;
        }
        DecimalFormat df = new DecimalFormat(".##");
        String st = df.format(1.0 * usage / capacity);
        double result = Double.valueOf(st) * 100;
        return result + "%";
    }

    public String getHitRate(){
        if(hitCount<0 ){
            LOGGER.warn("Cache hit count cannot below 0!");
            this.setHitCount(0L);
        }
        if(requestCount<0){
            LOGGER.warn("Cache request count cannot below 0!");
            this.setRequestCount(0L);
        }
        if(hitCount==0 || requestCount ==0){
            return CacheConstants.ZERO_PERCENTAGE;
        }
        if(hitCount>=requestCount){
            return CacheConstants.ONE_HUNDRED_PERCENTAGE;
        }
        DecimalFormat df=new DecimalFormat(".##");
        String st=df.format(1.0*hitCount/requestCount);
        double result=Double.valueOf(st)*100;
        return result+"%";
    }

    public Long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(Long requestCount) {
        this.requestCount = requestCount;
    }

    public Long getHitCount() {
        return hitCount;
    }

    public void setHitCount(Long hitCount) {
        this.hitCount = hitCount;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public Long getEvictionCount() {
        return evictionCount;
    }

    public void setEvictionCount(Long evictionCount) {
        this.evictionCount = evictionCount;
    }
}
