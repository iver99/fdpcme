package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/19.
 */
@Test(groups = {"s1"})
public class EmsPreferenceTest {

    private EmsPreference emsPreference = new EmsPreference("elephant","elephant","elephant");

    @Test
    public void testGetPrefKey() throws Exception {
        emsPreference.setPrefKey("elephant");
        assertEquals(emsPreference.getPrefKey(), "elephant");
    }

    @Test
    public void testGetPrefValue() throws Exception {
        emsPreference = new EmsPreference();
        emsPreference.setPrefValue("elephant");
        assertEquals(emsPreference.getPrefValue(),"elephant");
    }

    @Test
    public void testGetUserName() throws Exception {
        emsPreference.setUserName("elephant");
        assertEquals(emsPreference.getUserName(),"elephant");
    }

    @Test
    public void testSetPrefKey() throws Exception {
    }

    @Test
    public void testSetPrefValue() throws Exception {

    }

    @Test
    public void testSetUserName() throws Exception {

    }
}