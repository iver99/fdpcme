package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emaas.platform.dashboards.core.zdt.DataManager;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2017/1/9.
 */

@Test(groups = { "s2" })
public class ZDTAPITest {
    private ZDTAPI zdtapi = new ZDTAPI();
    @Mocked
    DataManager dataManager;
    @Mocked
    Throwable throwable;
    @Test
    public void testGetAllTableData() throws Exception {
        final List<Map<String, Object>> list = new ArrayList<>();
        new Expectations(){
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.getDashboardSetTableData();
                result = list;
            }
        };
        zdtapi.getAllTableData();
    }

    @Test
    public void testGetAllTableDataException() throws Exception {
        final List<Map<String, Object>> list = new ArrayList<>();
        new Expectations(){
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.getDashboardSetTableData();
                result = new JSONException(throwable);
            }
        };
        zdtapi.getAllTableData();
    }

    @Test
    public void testGetEntitiesCoung() throws Exception {
        new Expectations(){
            {
                DataManager.getInstance();
                result = dataManager;
                dataManager.getAllDashboardsCount();
                result = 1;
                dataManager.getAllFavoriteCount();
                result = 1;
                dataManager.getAllPreferencessCount();
                result = 1;
            }
        };
        zdtapi.getEntitiesCoung();
    }

    @Test
    public void testSync() throws Exception {
        zdtapi.sync(new JSONObject());
    }

}