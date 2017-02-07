package oracle.sysman.emaas.platform.dashboards.ws.rest.model;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chehao on 2017/1/12.
 */
@Test(groups = { "s2" })
public class RoleNamesEntityTest {

    @Test
    public void testRoleNamesEntity(){
        List<String> roleNames=new ArrayList<>();
        RoleNamesEntity re=new RoleNamesEntity();
        re.setRoleNames(roleNames);
        re.getRoleNames();
    }
}
