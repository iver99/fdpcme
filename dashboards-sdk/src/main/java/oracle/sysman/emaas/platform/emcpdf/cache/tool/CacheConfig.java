package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 2017/2/21 11:43.
 */
public class CacheConfig {

    private String name;
    private int capacity;
    private long expiry;

    public static List<CacheConfig> cacheConfigList = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getExpiry() {
        return expiry;
    }

    public void setExpiry(long expiry) {
        this.expiry = expiry;
    }
}
