package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Troy on 2016/1/20.
 */
@Test(groups={"s1"})
public class EmsPreferencePKTest {
    private EmsPreference emsPreference = new EmsPreference("elephant","elephant","elephant");
    private EmsPreferencePK emsPreferencePK = new EmsPreferencePK("elephant","dolphine");
    @Test
    public void testEquals() throws Exception {
        EmsPreferencePK emsPreferencePK1 = new EmsPreferencePK("elephant","dolphine");
        EmsPreferencePK emsPreferencePK2 = new EmsPreferencePK("elephant","kitten");
        EmsPreferencePK emsPreferencePK3 = new EmsPreferencePK("kitten","dolphine");
        assertFalse(emsPreferencePK.equals(new Integer(10)));
        assertTrue(emsPreferencePK.equals(emsPreferencePK1));
        assertFalse(emsPreferencePK.equals(emsPreferencePK2));
        assertFalse(emsPreferencePK.equals(emsPreferencePK3));
    }

    @Test
    public void testGetPrefKey() throws Exception {
        emsPreferencePK.setPrefKey("elephant");
        assertEquals(emsPreferencePK.getPrefKey(),"elephant");
    }

    @Test
    public void testGetUserName() throws Exception {
        emsPreferencePK = new EmsPreferencePK();
        emsPreferencePK.setUserName("dolphine");
        assertEquals(emsPreferencePK.getUserName(),"dolphine");

    }

    @Test
    public void testHashCode() throws Exception {
        assertNotNull(emsPreferencePK.hashCode());

    }

    @Test
    public void testSetPrefKey() throws Exception {

    }

    @Test
    public void testSetUserName() throws Exception {

    }
}