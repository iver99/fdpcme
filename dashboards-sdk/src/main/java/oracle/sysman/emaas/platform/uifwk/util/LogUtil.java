package oracle.sysman.emaas.platform.uifwk.util;

import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * Created by chehao on 3/26/17.
 */
public class LogUtil {

    private static final String INTERACTION_LOG_NAME = "oracle.sysman.emaas.platform.dashboards.interaction.log";
    private static final Logger LOGGER = LogManager.getLogger(LogUtil.class);

    /**
     * Clear the dashboard interaction log context
     */
    public static void clearInteractionLogContext()
    {
        ThreadContext.remove(INTERACTION_LOG_PROP_TENANTID);
        ThreadContext.remove(INTERACTION_LOG_PROP_SERVICE_INVOKED);
        ThreadContext.remove(INTERACTION_LOG_PROP_DIRECTION);
    }

    public static final String INTERACTION_LOG_PROP_TENANTID = "tenantId";

    public static final String INTERACTION_LOG_VALUE_NA = "N/A";
    public static final String INTERACTION_LOG_PROP_SERVICE_INVOKED = "serviceInvoked";
    public static final String INTERACTION_LOG_PROP_DIRECTION = "direction";
    public static final String INTERACTION_LOG_PROP_ZDT_REQID = "gatewayQuestId";
    public static final String INTERACTION_LOG_PROP_ZDT_REQTIME = "gatewayQuestTime";
    /**
     * Returns the interaction log
     *
     * @return
     */
    public static final Logger getInteractionLogger()
    {
        return LogManager.getLogger(INTERACTION_LOG_NAME);
    }

    public static final Logger getInteractionLogger(String loggerName)
    {
        return LogManager.getLogger(loggerName);
    }


    /**
     * Initialize dashboard interaction log context by specifying the tenant id, service invoked and direction
     *
     * @param serviceInvoked
     * @param direction
     */
    public static InteractionLogContext setInteractionLogThreadContext(String tenantId, String serviceInvoked,
                                                                       InteractionLogDirection direction)
    {
        InteractionLogContext ilc = new InteractionLogContext(ThreadContext.get(INTERACTION_LOG_PROP_TENANTID),
                ThreadContext.get(INTERACTION_LOG_PROP_SERVICE_INVOKED), ThreadContext.get(INTERACTION_LOG_PROP_DIRECTION));
        if (StringUtil.isEmpty(tenantId)) {
            LOGGER.debug("Initialize interaction log context: tenantId is null or empty");
            tenantId = INTERACTION_LOG_VALUE_NA;
        }
        if (StringUtil.isEmpty(serviceInvoked)) {
            LOGGER.debug("Failed to initialize interaction log context: serviceInvoked is null or empty");
            serviceInvoked = "Service invoked: N/A";
        }
        if (direction == null) {
            LOGGER.warn("Initialize interaction log context: direction is null");
            direction = InteractionLogDirection.NA;
        }
        ThreadContext.put(INTERACTION_LOG_PROP_TENANTID, tenantId);
        ThreadContext.put(INTERACTION_LOG_PROP_SERVICE_INVOKED, serviceInvoked);
        ThreadContext.put(INTERACTION_LOG_PROP_DIRECTION, direction.getValue());
        return ilc;
    }

    /**
     * Direction for interaction log
     *
     * @author guobaochen
     */
    public static enum InteractionLogDirection {
        /**
         * for all incoming service requests
         */
        IN("IN"),
        /**
         * for all outbound service request
         */
        OUT("OUT"),
        /**
         * indicate that direction of service request is not available
         */
        NA("N/A");

        public static InteractionLogDirection fromValue(String value) {
            for (InteractionLogDirection ild : InteractionLogDirection.values()) {
                if (ild.getValue().equals(value)) {
                    return ild;
                }
            }
            return NA;
        }

        private final String value;

        private InteractionLogDirection(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Class that represents the interaction log context, containing tenant id, service invoked, and service invocation direction
     *
     * @author guobaochen
     */
    public static class InteractionLogContext
    {
        private String tenantId;
        private String serviceInvoked;
        private InteractionLogDirection direction;

        public InteractionLogContext(String tenantId, String serviceInvoked, String direction)
        {
            this.tenantId = tenantId;
            this.serviceInvoked = serviceInvoked;
            this.direction = InteractionLogDirection.fromValue(direction);
        }

        public InteractionLogDirection getDirection()
        {
            return direction;
        }

        public String getServiceInvoked()
        {
            return serviceInvoked;
        }

        public String getTenantId()
        {
            return tenantId;
        }

        public void setDirection(InteractionLogDirection direction)
        {
            this.direction = direction;
        }

        public void setServiceInvoked(String serviceInvoked)
        {
            this.serviceInvoked = serviceInvoked;
        }

        public void setTenantId(String tenantId)
        {
            this.tenantId = tenantId;
        }
    }


}
