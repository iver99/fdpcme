package oracle.sysman.emaas.platform.dashboards.core.model.subscription2;

import java.util.List;

/**
 * Created by chehao on 2017/3/31 10:50.
 */
public class AppsInfo {

    private String id;//application name
    private String licVersion;//application lic version
    private List<String> editions;//application editions

    public AppsInfo(String id, String licVersion) {
        this.id = id;
        this.licVersion = licVersion;
    }

    public AppsInfo(String id, String licVersion, List<String> editions) {

        this.id = id;
        this.licVersion = licVersion;
        this.editions = editions;
    }



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

    /*public void addEdition(String edition){
        editions.add(edition);
    }*/

    /*public void addAllEdition(List editions){
        editions.addAll(editions);
    }*/

}
