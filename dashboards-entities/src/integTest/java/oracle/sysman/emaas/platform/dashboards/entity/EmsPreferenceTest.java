package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


@Test(groups = {"s1"})
public class EmsPreferenceTest {

    private EmsPreference emsPreference = new EmsPreference("elephant","elephant","elephant");

    @Test
    public void testGetPrefKey() {
        emsPreference.setPrefKey("elephant");
        assertEquals(emsPreference.getPrefKey(), "elephant");
    }

    @Test
    public void testGetPrefValue() {
        emsPreference = new EmsPreference();
        emsPreference.setPrefValue("elephant");
        assertEquals(emsPreference.getPrefValue(),"elephant");
    }

    @Test
    public void testGetUserName() {
        emsPreference.setUserName("elephant");
        assertEquals(emsPreference.getUserName(),"elephant");
    }

    @Test
    public void testSetPrefKey() {
    }

    @Test
    public void testSetPrefValue() {

    }

    @Test
    public void testSetUserName() {

    }
}