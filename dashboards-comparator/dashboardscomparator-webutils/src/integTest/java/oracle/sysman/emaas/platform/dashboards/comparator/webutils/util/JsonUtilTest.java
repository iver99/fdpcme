package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by chehao on 2017/1/10.
 */
@Test(groups = {"s2"})
public class JsonUtilTest {

    @Test
    public void testJsonUtil() throws IOException {
        JsonUtil.buildNonDefaultMapper();
        JsonUtil.buildNonNullMapper();
        JsonUtil.buildNormalMapper();

        JsonUtil json=new JsonUtil(JsonSerialize.Inclusion.ALWAYS);

        Assert.assertFalse(json.isEmptyString("notEmpty"));
        Assert.assertTrue(json.isEmptyString(""));

        json.parseNode("\"applications\":\"LogAnalytics\"");

//        json.toJson(new JsonUtilTest());
    }
}
