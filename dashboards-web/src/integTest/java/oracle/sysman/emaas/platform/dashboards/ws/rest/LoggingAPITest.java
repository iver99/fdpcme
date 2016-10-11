package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jishshi
 * @since 1/20/2016.
 */
@Test(groups = {"s2"})
public class LoggingAPITest {

    LoggingAPI loggingAPI;


    @Mocked
    HttpServletRequest request;

    @BeforeMethod
    public void setUp() {
        loggingAPI = new LoggingAPI();
        new NonStrictExpectations() {
            {
                request.getRemoteAddr();
                result = "addr";
                request.getHeader(anyString);
                result = "header";
                request.getLocale();
                result = null;
            }
        };

        Deencapsulation.setField(loggingAPI, "request", request);

    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testLogMsgWithNull() {
        Assert.assertNull(loggingAPI.logMsg(null));
    }

    @Test
    public void testLogMsg(@Mocked final JSONObject jsonObject) throws JSONException {
        final JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 6; i++) {
            jsonArray.put(i, new JSONObject());
        }

        new Expectations() {
            {
                jsonObject.getJSONObject("logs").getJSONArray("logArray");
                result = jsonArray;

                jsonObject.getString("logLevel");
                returns("1", "2", "3", "4", "0", "other");

                jsonObject.getString("log");
                result = "log";
            }
        };

        Assert.assertNull(loggingAPI.logMsg(jsonObject).getString("currentLogLevel"));

    }

    @Test
    public void testLogMsg1() {

        new MockUp<JSONObject>() {
            @Mock
            public String getString(@SuppressWarnings("unused") String key) throws JSONException {
                throw new JSONException("jsonObject getString");
            }
        };

        loggingAPI.logMsg(new JSONObject());

    }

    @Test
    public void testLogMsg2() {

        new MockUp<JSONObject>() {
            @Mock
            public JSONObject put(String key, Object value) throws JSONException {
                throw new JSONException("jsonObject getString");
            }
            @Mock
            public JSONObject getJSONObject(@SuppressWarnings("unused") String key) throws JSONException {
                return new JSONObject();
            }

        };


        loggingAPI.logMsg(new JSONObject());

    }


}