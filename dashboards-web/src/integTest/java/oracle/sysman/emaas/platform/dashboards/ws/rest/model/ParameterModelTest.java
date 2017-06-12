package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import org.testng.annotations.Test;

import java.util.Date;

/**
 * Created by chehao on 6/12/2017 13:22.
 */
@Test(groups = { "s2" })
public class ParameterModelTest {
    @Test
    public void testParameterModel(){
        ParameterModel pm = new ParameterModel();
        pm.setName("test");
        pm.setType("test");
        pm.setValue("test");
        pm.getName();
        pm.getType();
        pm.getValue();
    }
    @Test
    public void testCategoryModel() {
        CategoryModel cm = new CategoryModel();
        cm.setName("test");
        cm.setProviderAssetRoot("test");
        cm.setProviderName("test");
        cm.setProviderVersion("test");
        cm.getName();
        cm.getProviderAssetRoot();
        cm.getProviderName();
        cm.getProviderVersion();
    }
    public void testSearchModel(){
        SearchModel sm = new SearchModel();
        sm.setName("test");
        sm.setCategory(null);
        sm.setCreationDate("test Date");
        sm.setDescription("test desc");
        sm.setId(null);
        sm.setId(null);
        sm.setOwner("test");
        sm.setParameters(null);
        sm.getCategory();
        sm.getParameters();
        sm.getName();
        sm.getCreationDate();
        sm.getDescription();
        sm.getId();
        sm.getOwner();

        SearchModel.InnerCategory innerCategory = new SearchModel().new InnerCategory();
        innerCategory.setHref("test");
        innerCategory.setId("test");
        innerCategory.getId();
        innerCategory.getHref();


    }
}
