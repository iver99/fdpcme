package oracle.sysman.emaas.platform.dashboards.core.model.subscription2;

import java.util.List;

/**
 * Created by chehao on 3/30/17.
 */
public class ServiceComponent {
    private String component_id;

    private List<Component> component;

    public List<Component> getComponent() {
        return component;
    }

    public void setComponent(List<Component> component) {
        this.component = component;
    }

    public String getComponent_id() {
        return component_id;
    }

    public void setComponent_id(String component_id) {
        this.component_id = component_id;
    }
}
