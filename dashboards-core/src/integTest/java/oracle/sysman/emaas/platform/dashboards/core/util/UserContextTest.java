package oracle.sysman.emaas.platform.dashboards.core.util;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by xiadai on 2016/11/1.
 */
@Test(groups = { "s1" })
public class UserContextTest {
    @Test
    public void testClearCurrentUser() throws Exception {
        UserContext.setCurrentUser("user");
        UserContext.getCurrentUser();
        UserContext.clearCurrentUser();
    }

}