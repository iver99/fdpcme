package oracle.sysman.emaas.platform.dashboards.testsdk;

import org.testng.annotations.Test;

import java.sql.SQLException;

/**
 * Created by chehao on 8/14/2017 11:38.
 */
@Test(groups = { "s2" })
public class InsertDBTest {

    @Test
    public void testInsert() throws SQLException {
        DatabaseUtil.Cloud1ExecuteSQL("insert_dashboard.sql");
    }
}
