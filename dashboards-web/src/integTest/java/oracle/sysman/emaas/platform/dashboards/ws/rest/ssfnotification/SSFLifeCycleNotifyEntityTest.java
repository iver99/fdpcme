package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfnotification;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by guochen on 3/24/17.
 */
@Test(groups = { "s2" })
public class SSFLifeCycleNotifyEntityTest {
	@Test
	public void testSSFLifeCycleNotifyEntity() {
		SSFLifeCycleNotifyEntity lcne = new SSFLifeCycleNotifyEntity();
		Assert.assertNull(lcne.getType());

		lcne.setType(SSFLifeCycleNotifyEntity.SSFNotificationType.UP);
		Assert.assertEquals(lcne.getType(), SSFLifeCycleNotifyEntity.SSFNotificationType.UP);
	}

	@Test
	public void testSSFNotificationType() {
		SSFLifeCycleNotifyEntity.SSFNotificationType type = SSFLifeCycleNotifyEntity.SSFNotificationType.fromName("DOWN");
		Assert.assertEquals(type, SSFLifeCycleNotifyEntity.SSFNotificationType.DOWN);
		Assert.assertEquals(type.getType(), "DOWN");

		type = SSFLifeCycleNotifyEntity.SSFNotificationType.fromName("UP");
		Assert.assertEquals(type, SSFLifeCycleNotifyEntity.SSFNotificationType.UP);
		Assert.assertEquals(type.getType(), "UP");

		type = SSFLifeCycleNotifyEntity.SSFNotificationType.fromName(null);
		Assert.assertNull(type);

		type = SSFLifeCycleNotifyEntity.SSFNotificationType.fromName("UNKNOWN");
		Assert.assertNull(type);
	}
}
