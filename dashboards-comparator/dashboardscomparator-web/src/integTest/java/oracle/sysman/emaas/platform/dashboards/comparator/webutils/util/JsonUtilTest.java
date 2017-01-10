package oracle.sysman.emaas.platform.dashboards.comparator.webutils.util;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2017/1/10.
 */
public class JsonUtilTest {
    public static class TestClass<T>
    {
        private int param1;
        private T param2;

        /**
         * @return the param1
         */
        public int getParam1()
        {
            return param1;
        }

        /**
         * @return the param2
         */
        public T getParam2()
        {
            return param2;
        }

        /**
         * @param param1
         *            the param1 to set
         */
        public void setParam1(int param1)
        {
            this.param1 = param1;
        }

        /**
         * @param param2
         *            the param2 to set
         */
        public void setParam2(T param2)
        {
            this.param2 = param2;
        }
    }

    private static final String json = "{\"param1\":100,\"param2\":\"value2\"}";

    @Test (groups = {"s1"})
    public void testFromJsonForJavaType() throws JsonProcessingException, IOException
    {
        JsonUtil ju = JsonUtil.buildNonDefaultMapper();
        ObjectMapper mapper = ju.getMapper();
        JavaType jt = mapper.getTypeFactory().constructParametricType(TestClass.class, String.class);
        TestClass<String> tc = ju.fromJson(json, jt);
        Assert.assertEquals(tc.getParam1(), 100);
        Assert.assertEquals(tc.getParam2(), "value2");

        tc = ju.fromJson(null, jt);
        Assert.assertNull(tc);
    }

    @Test (groups = {"s1"})
    public void testFromJsonForJsonNode() throws JsonProcessingException, IOException
    {
        JsonUtil ju = JsonUtil.buildNonDefaultMapper();
        JsonNode jn = ju.parseNode(json);
        TestClass<String> tc = ju.fromJson(jn, TestClass.class, String.class);
        Assert.assertEquals(tc.getParam1(), 100);
        Assert.assertEquals(tc.getParam2(), "value2");
    }

    @SuppressWarnings("unchecked")
    @Test (groups = {"s1"})
    public void testFromJsonForString() throws JsonProcessingException, IOException
    {
        JsonUtil ju = JsonUtil.buildNonDefaultMapper();
        ju.setDateFormat("yyyy-MM-dd HH:mm:ss");
        ju.setEnumUseToString(true);
        TestClass<String> tc = ju.fromJson(json, TestClass.class);
        Assert.assertEquals(tc.getParam1(), 100);
        Assert.assertEquals(tc.getParam2(), "value2");
        tc = ju.fromJson(null, TestClass.class);
        Assert.assertNull(tc);
    }

    @Test (groups = {"s1"})
    public void testFromJsonForStringParametricType() throws JsonProcessingException, IOException
    {
        JsonUtil ju = JsonUtil.buildNonDefaultMapper();
        TestClass<String> tc = ju.fromJson(json, TestClass.class, String.class);
        Assert.assertEquals(tc.getParam1(), 100);
        Assert.assertEquals(tc.getParam2(), "value2");
    }

    @Test (groups = {"s1"})
    public void testToJson() throws IOException
    {
        JsonUtil ju = JsonUtil.buildNonDefaultMapper();
        TestClass<String> tc = ju.fromJson(json, TestClass.class, String.class);
        String jsonString = ju.toJson(tc);
        Assert.assertEquals(jsonString, json);
    }

}