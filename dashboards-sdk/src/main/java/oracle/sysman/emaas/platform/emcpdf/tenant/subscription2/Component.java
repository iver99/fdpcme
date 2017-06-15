package oracle.sysman.emaas.platform.emcpdf.tenant.subscription2;

import java.util.List;

/**
 * Created by chehao on 3/30/17.
 */
public class Component {

    private String component_id;

    private List<ComponentParameter> component_parameter;

    public List<ComponentParameter> getComponent_parameter() {
        return component_parameter;
    }

    public void setComponent_parameter(List<ComponentParameter> component_parameter) {
        this.component_parameter = component_parameter;
    }

    public String getComponent_id() {
        return component_id;
    }

    public void setComponent_id(String component_id) {
        this.component_id = component_id;
    }
}
