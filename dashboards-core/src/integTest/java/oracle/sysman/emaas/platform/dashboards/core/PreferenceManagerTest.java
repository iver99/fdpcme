package oracle.sysman.emaas.platform.dashboards.core;

import java.util.List;

import oracle.sysman.emaas.platform.dashboards.core.exception.DashboardException;
import oracle.sysman.emaas.platform.dashboards.core.model.Preference;
import oracle.sysman.emaas.platform.dashboards.core.util.UserContext;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PreferenceManagerTest extends BaseTest
{

	private static final long TENANT_ID = 0L;
	private static final String TEST_USER = "PREF_TEST";

	private static PreferenceManager pm;

	@AfterClass
	public void afterClass()
	{
		if (pm != null) {
			pm.removeAllPreferences(TENANT_ID);
			pm = null;
		}
	}

	@BeforeClass
	public void beforeClass() throws DashboardException
	{
		UserContext.setCurrentUser(TEST_USER);
		pm = PreferenceManager.getInstance();
		Preference p1 = new Preference();
		p1.setKey("p1");
		p1.setValue("p1");
		pm.savePreference(p1, TENANT_ID);
		Preference p2 = new Preference();
		p2.setKey("p2");
		p2.setValue("p2");
		pm.savePreference(p2, TENANT_ID);
	}

	@Test
	public void testGetPreference() throws DashboardException
	{
		Preference p = pm.getPreferenceByKey("p2", TENANT_ID);
		Assert.assertEquals(p.getKey(), "p2");
	}

	@Test
	public void testListPreferences()
	{
		List<Preference> ps = pm.listPreferences(TENANT_ID);
		Assert.assertEquals(ps.size() >= 1, true);
	}

	@Test
	public void testRemovePreference() throws DashboardException
	{
		pm.removePreference("p1", TENANT_ID);
		List<Preference> ps = pm.listPreferences(TENANT_ID);
		Assert.assertEquals(ps.size() == 1, true);
	}

	@Test
	public void testUpdatePreference() throws DashboardException
	{
		Preference p = pm.getPreferenceByKey("p2", TENANT_ID);
		p.setValue("p2_update");
		pm.savePreference(p, TENANT_ID);
		p = pm.getPreferenceByKey("p2", TENANT_ID);
		Assert.assertEquals(p.getValue(), "p2_update");
	}

}
