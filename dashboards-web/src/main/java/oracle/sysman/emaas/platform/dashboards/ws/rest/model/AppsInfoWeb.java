package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 2017/3/31 10:50.
 */
public class AppsInfoWeb {

    private String id;
    private String licVersion;
    private List<String> editions = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicVersion() {
        return licVersion;
    }

    public void setLicVersion(String licVersion) {
        this.licVersion = licVersion;
    }

    public List<String> getEditions() {
        return editions;
    }

    public void setEditions(List<String> editions) {
        this.editions = editions;
    }

    public void addEdition(String edition){
        editions.add(edition);
    }
}
