package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

/**
 * Created by chehao on 6/5/2017 10:20 AM.
 */
public class CategoryModel {
    private String providerName;
    private String providerVersion;
    private String providerAssetRoot;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderVersion() {
        return providerVersion;
    }

    public void setProviderVersion(String providerVersion) {
        this.providerVersion = providerVersion;
    }

    public String getProviderAssetRoot() {
        return providerAssetRoot;
    }

    public void setProviderAssetRoot(String providerAssetRoot) {
        this.providerAssetRoot = providerAssetRoot;
    }
}
