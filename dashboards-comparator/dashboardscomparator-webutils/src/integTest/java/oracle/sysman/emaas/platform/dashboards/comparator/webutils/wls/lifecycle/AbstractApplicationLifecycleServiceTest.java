package oracle.sysman.emaas.platform.dashboards.comparator.webutils.wls.lifecycle;

import mockit.Mocked;
import org.testng.annotations.Test;
import weblogic.application.ApplicationException;
import weblogic.application.ApplicationLifecycleEvent;

/**
 * Created by chehao on 2017/1/11.
 */
@Test(groups = {"s2"})
public class AbstractApplicationLifecycleServiceTest {
    @Test
    public void testAddApplicationServiceManager(final @Mocked ApplicationServiceManager manager, final @Mocked ApplicationLifecycleEvent evt) throws ApplicationException {
        AbstractApplicationLifecycleService service =new AbstractApplicationLifecycleService();

        service.addApplicationServiceManager(manager);

        service.preStart(evt);

        service.postStart(evt);

        service.preStop(evt);

        service.postStop(evt);
    }
}
