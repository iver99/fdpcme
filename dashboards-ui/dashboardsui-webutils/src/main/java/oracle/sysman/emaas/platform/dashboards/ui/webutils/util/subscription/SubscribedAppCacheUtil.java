package oracle.sysman.emaas.platform.dashboards.ui.webutils.util.subscription;

import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.StringUtil;
import oracle.sysman.emaas.platform.dashboards.ui.webutils.util.TenantSubscriptionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by guochen on 11/18/16.
 */
@Deprecated
public class SubscribedAppCacheUtil {
    private static Logger LOGGER = LogManager.getLogger(TenantSubscriptionUtil.class);

    private static final SubscribedAppCacheUtil INSTANCE = new SubscribedAppCacheUtil();

    // each subscribed app will live untime expiry time is reached
    private static final long EXPIRY_TIME = 60000L;

    // let's set the capacity for the cache. Not that an known issue is, expired cached object may be inside cache still
    private static final int CACHE_CAPACITY = 800;

    private static class CachedObj {
        private long timeStamp;
        private List<String> obj;

        public CachedObj(List<String> obj) {
            timeStamp = System.currentTimeMillis();
            this.obj = obj;
        }

        public List<String> getObject() {
            return obj;
        }

        public void setObject(List<String> obj) {
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

    public static final SubscribedAppCacheUtil getInstance() {
        return INSTANCE;
    }

    private SubscribedAppCacheUtil() {

    }
    @Deprecated
    public List<String> put(String tenant, List<String> value) {
        if (StringUtil.isEmpty(tenant)) {
            LOGGER.error("Failed to put value {} into cache, as specified tenant is empty", value == null? null: value.toString());
            return value;
        }
        if (value == null || value.isEmpty()) {
            LOGGER.warn("Did not put value into cache for tenant {}, as specified value is empty", tenant);
            return value;
        }
        CachedObj co = new CachedObj(value);
        container.put(tenant, co);
        return value;
    }
    @Deprecated
    public List<String> get(String tenant) {
        if (StringUtil.isEmpty(tenant)) {
            LOGGER.error("Failed to getDashboardData value for tenant as it is null or empty: {}", tenant);
            return null;
        }
        CachedObj co = container.get(tenant);
        if (co == null)
            return null;
        long startTime = co.getTimestamp();
        if (!isValidValue(co)) {
            container.remove(tenant);
            return null;
        }
        List<String> value = co.getObject();
        return value;
    }
    @Deprecated
    public List<String> remove(String tenant) {
        if (StringUtil.isEmpty(tenant)) {
            LOGGER.error("Failed to remove value for tenant as tenant is null or empty: {}", tenant);
            return null;
        }
        if (container.containsKey(tenant)) {
            CachedObj co =  container.remove(tenant);
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
