package oracle.sysman.emaas.platform.dashboards.core.model.subscription2;


/**
 * Created by chehao on 3/30/17.
 */
public class ServiceRequestCollection {

    private OrderComponents orderComponents;
    private boolean trial;
    private String serviceType;

    public boolean isTrial() {
        return trial;
    }

    public void setTrial(boolean trial) {
        this.trial = trial;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public OrderComponents getOrderComponents() {
        return orderComponents;
    }
    public void setOrderComponents(OrderComponents orderComponents) {
        this.orderComponents = orderComponents;
    }


}
