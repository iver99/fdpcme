package oracle.sysman.emaas.platform.dashboards.ui.webutils.util.registration;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;

/**
 * Created by guochen on 11/18/16.
 */
@Deprecated
public class StringCacheUtil {
    private static Logger LOGGER = LogManager.getLogger(StringCacheUtil.class);

    private static final StringCacheUtil REG_INSTANCE = new StringCacheUtil();
    private static final StringCacheUtil USERINFO_INSTANCE = new StringCacheUtil();

    // each string cache will live untime expiry time is reached
    private static final long EXPIRY_TIME = 180000L;

    // let's set the capacity for the cache. Not that an known issue is, expired cached object may be inside cache still
    private static final int CACHE_CAPACITY = 800;

    private static class CachedObj {
        private long timeStamp;
        private String obj;

        public CachedObj(String obj) {
            timeStamp = System.currentTimeMillis();
            this.obj = obj;
        }

        public String getObject() {
            return obj;
        }

        public void setObject(String obj) {
            this.obj = obj;
        }

        public long getTimestamp() {
            return timeStamp;
        }

        public void setTimestamp(long timestamp) {
            this.timeStamp = timestamp;
        }
    }

    private LinkedHashMap<String,CachedObj> container = new LinkedHashMap<String,CachedObj>(CACHE_CAPACITY);

    public static final StringCacheUtil getUserInfoCacheInstance() {
        return USERINFO_INSTANCE;
    }
    public static final StringCacheUtil getRegistrationCacheInstance() {
        return REG_INSTANCE;
    }

    private StringCacheUtil() {

    }
    @Deprecated
    public String put(String key, String value) {
        if (StringUtil.isEmpty(key)) {
            LOGGER.error("Failed to put value {} into cache, as specified key is empty", value == null? null: value.toString());
            return value;
        }
        if (value == null || "".equals(value)) {
            LOGGER.warn("Did not put value into cache for key {}, as specified value is empty", key);
            return value;
        }
        CachedObj co = new CachedObj(value);
        container.put(key, co);
        return value;
    }
    @Deprecated
    public String get(String key) {
        if (StringUtil.isEmpty(key)) {
            LOGGER.error("Failed to getDashboardData value for key as it is null or empty: {}", key);
            return null;
        }
        CachedObj co = container.get(key);
        if (co == null)
            return null;
        if (!isValidValue(co)) {
            container.remove(key);
            return null;
        }
        String value = co.getObject();
        return value;
    }
    @Deprecated
    public String remove(String key) {
        if (StringUtil.isEmpty(key)) {
            LOGGER.error("Failed to remove value for key as key is null or empty: {}", key);
            return null;
        }
        if (container.containsKey(key)) {
            CachedObj co =  container.remove(key);
            if (isValidValue(co))
                return co.getObject();
            else
                return null;
        }
        return null;
    }
    @Deprecated
    private boolean isValidValue(CachedObj co) {
        if (co == null) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (co.getTimestamp() > now || System.currentTimeMillis() - co.getTimestamp() > EXPIRY_TIME) {
            LOGGER.debug("Invalid cached object: start time is too late, or it expires for timestamp: {}", co.getTimestamp());
            return false;
        }
        else
            return true;
    }
}
