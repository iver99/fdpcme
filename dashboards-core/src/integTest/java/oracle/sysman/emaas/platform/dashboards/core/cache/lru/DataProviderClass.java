package oracle.sysman.emaas.platform.dashboards.core.cache.lru;


import org.testng.annotations.DataProvider;

public class DataProviderClass {
	
	private DataProviderClass() {
	  }


	/**
	 * CacheUnit Test Data
	 * 
	 */
	@DataProvider(name = "default_cache_unit")
	public static Object[][] providesDefaultCacheUnit() {
		return new Object[][] { { new CacheUnit() } };
	}
	@DataProvider(name = "default_cache_unit_three_elements")
	public static Object[][] providesDefaultCacheUnitWith3Elements() {
		CacheUnit cu=new CacheUnit();
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		
		return new Object[][] { { cu } };
	}
	
	
	@DataProvider(name = "default_cache_unit_3sec")
	public static Object[][] providesDefaultCacheUnit3Sec() {
		return new Object[][] { { new CacheUnit(3) } };
	}
	
	@DataProvider(name = "default_cache_unit_5sec")
	public static Object[][] providesDefaultCacheUnit5Sec() {
		return new Object[][] { { new CacheUnit(5) } };
	}
	
	@DataProvider(name = "default_cache_unit_7sec")
	public static Object[][] providesDefaultCacheUnit7Sec() {
		return new Object[][] { { new CacheUnit(7) } };
	}
	
	@DataProvider(name = "cache_unit_2_elements_2sec")
	public static Object[][] provides3ElementsCacheUnit3Sec() {
		CacheUnit cu=new CacheUnit(2);
		cu.put("1", new Element("1","one"));
		cu.put("2", new Element("2", "two"));
		cu.put("3", new Element("3", "three"));
		return new Object[][] { { cu } };
	} 
	/**
	 * Element Test Data
	 */
	@DataProvider(name = "default_element")
	public static Object[][] defaultElement() {
		return new Object[][] { { new CacheUnit(),new Element("","") } };
	}
	
	@DataProvider(name = "default_element_one")
	public static Object[][] elementOne() {
		return new Object[][] { { new CacheUnit(),new Element("1","one") } };
	}
	
	@DataProvider(name = "default_element_two")
	public static Object[][] ElementTwo() {
		return new Object[][] { { new CacheUnit(),new Element("2","two") } };
	}
	
	@DataProvider(name = "default_element_three")
	public static Object[][] elementThree() {
		return new Object[][] { { new CacheUnit(),new Element("3","three") } };
	}
	
	
}
