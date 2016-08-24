package oracle.sysman.emaas.platform.dashboards.entity;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


@Test(groups={"s1"})
public class EmsPreferencePKTest {
    private EmsPreference emsPreference = new EmsPreference("elephant","elephant","elephant");
    private EmsPreferencePK emsPreferencePK = new EmsPreferencePK("elephant","dolphine");
    @Test
    public void testEquals() {
        EmsPreferencePK emsPreferencePK1 = new EmsPreferencePK("elephant","dolphine");
        EmsPreferencePK emsPreferencePK2 = new EmsPreferencePK("elephant","kitten");
        EmsPreferencePK emsPreferencePK3 = new EmsPreferencePK("kitten","dolphine");
        assertFalse(emsPreferencePK.equals(new Integer(10)));
        assertTrue(emsPreferencePK.equals(emsPreferencePK1));
        assertFalse(emsPreferencePK.equals(emsPreferencePK2));
        assertFalse(emsPreferencePK.equals(emsPreferencePK3));
    }

    @Test
    public void testGetPrefKey() {
        emsPreferencePK.setPrefKey("elephant");
        assertEquals(emsPreferencePK.getPrefKey(),"elephant");
    }

    @Test
    public void testGetUserName() {
        emsPreferencePK = new EmsPreferencePK();
        emsPreferencePK.setUserName("dolphine");
        assertEquals(emsPreferencePK.getUserName(),"dolphine");

    }

    @Test
    public void testHashCode() {
        assertNotNull(emsPreferencePK.hashCode());

    }

}