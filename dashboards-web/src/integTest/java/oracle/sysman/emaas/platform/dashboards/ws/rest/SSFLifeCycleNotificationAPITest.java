package oracle.sysman.emaas.platform.dashboards.ws.rest;

import mockit.Expectations;
import mockit.Mocked;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * Created by guochen on 3/24/17.
 */
@Test(groups = {"s2"})
public class SSFLifeCycleNotificationAPITest {
	@Test
	public void testNotifySsfLifecycle() throws JSONException {
		SSFLifeCycleNotificationAPI lcn = new SSFLifeCycleNotificationAPI();

		//Test 403 with invalid input
		Assert.assertEquals(lcn.notifySsfLifecycle(new JSONObject()).getStatus(), 403);

		JSONObject json = new JSONObject();
		json.put("type", "UP");
		Assert.assertEquals(lcn.notifySsfLifecycle(json).getStatus(), 204);
	}

	@Test
	public void testNotifySsfLifecycleIOException(@Mocked final JSONObject anyJSONObject) {
		new Expectations() {
			{
				anyJSONObject.toString();
				result = new IOException("Test IO exception");
			}
		};
		Assert.assertEquals(new SSFLifeCycleNotificationAPI().notifySsfLifecycle(new JSONObject()).getStatus(), 400);
	}
}
