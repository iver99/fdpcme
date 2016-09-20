package oracle.sysman.emaas.platform.dashboards.core.cache.lru;


import java.util.logging.Logger;

import org.testng.Assert;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;
/**
 * @author chendonghao
 *
 */
@SuppressWarnings("all")
public class CacheUnitTest {
	
	public static final Logger LOGGER=Logger.getLogger(CacheUnitTest.class.getName());

	/**
	 * test put action begins
	 */

	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testPutCache(CacheUnit cacheUnit){
		cacheUnit.put("1", new Element("1","one"));
		Assert.assertNotNull(cacheUnit.get("1"));
	}
	
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testPutWhenEmpty(CacheUnit cacheUnit){
		cacheUnit.put("1", new Element("1","One"));
	}
	
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testPutWhenNotEmpty(CacheUnit cacheUnit){
		cacheUnit.put("1", new Element("1","second one"));
	}
	
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testPutIfAbsent(CacheUnit cacheUnit){
		cacheUnit.put("4", new Element("4","four"));
	}
	
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	@ExpectedExceptions(value = { IllegalArgumentException.class })
	public void testPutNullKey(CacheUnit cacheUnit){
		cacheUnit.put(null, new Element("1","one"));
	}
	@ExpectedExceptions(value = { IllegalArgumentException.class })
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testPutNullValue(CacheUnit cacheUnit){
		cacheUnit.put("1", null);
	}
	
	/**
	 * test get action begins 
	 */
	
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testGetCache(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.get("2"), "two");
		Assert.assertEquals(cacheUnit.get("1"), "one");
		Assert.assertEquals(cacheUnit.get("3"), "three");
		Assert.assertEquals(cacheUnit.get("4"), null);
	}
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testGetWhenEmpty(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.isEmpty(), true);
		Assert.assertEquals(cacheUnit.get("1"), null);
	}
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testGetWhenNotEmpty(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.isEmpty(), false);
		Assert.assertNotEquals(cacheUnit.get("1"),null);
	}
	
	/**
	 *	test remove action begins 
	 */

	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testRemoveCache(CacheUnit cacheUnit){
		Assert.assertNotEquals(cacheUnit.get("2"), null);
		cacheUnit.remove("2");
		Assert.assertEquals(cacheUnit.get("2"), null);
		Assert.assertNotEquals(cacheUnit.get("1"), null);
	}
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testRemoveWhenEmpty(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.isEmpty(), true);
		Assert.assertEquals(cacheUnit.remove("1"), false);
	}
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testRemoveWhenNotEmpty(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.isEmpty(), false);
		Assert.assertEquals(cacheUnit.remove("1"), true);
	}
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testRemoveWhenExists(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.remove("1"), true);
	}
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testRemoveWhenNotExists(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.remove("4"), false);
	}
	/**
	 * 	test clear action begins 
	 */
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testClearWhenEmpty(CacheUnit cacheUnit){
		cacheUnit.clearCache();
	}
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testClearWhenNotEmpty(CacheUnit cacheUnit){
		cacheUnit.clearCache();
	}
	
	/**
	 *	test get capacity actions begins 
	 */
	
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testCacheUnitCapacity(CacheUnit cacheUnit){
		Assert.assertEquals(cacheUnit.getCacheCapacity(), CacheUnit.DEFAULT_CACHE_CAPACITY);
	}
	@Test(dataProvider="default_cache_unit",dataProviderClass=DataProviderClass.class)
	public void testCapacityWhenEmpty(CacheUnit cacheUnit){
		cacheUnit.getCacheCapacity();
	}
	@Test(dataProvider="default_cache_unit_three_elements",dataProviderClass=DataProviderClass.class)
	public void testCapacityWhenNotEmpty(CacheUnit cacheUnit){
		cacheUnit.getCacheCapacity();
	}
	
	@Test(dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testGetBeforeExpiration(CacheUnit cacheUnit){
		Assert.assertNotNull(cacheUnit.get("1"));
	}
	@Test(dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testGetAfterExpiration(CacheUnit cacheUnit) {
		try {
			Thread.currentThread().sleep(2100);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	}
	@Test(dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testPutBeforeExpiration(CacheUnit cacheUnit) {
		cacheUnit.put("4", new Element("4","four"));
	}
	@Test(dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testPutAfterExpiration(CacheUnit cacheUnit) {
		Assert.assertNotNull(cacheUnit.get("1"));
		try {
			Thread.currentThread().sleep(2100);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		Assert.assertNull(cacheUnit.get("1"));
	}
	@Test(dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testRemoveBeforeExpiration(CacheUnit cacheUnit) {
		cacheUnit.remove("1");
	}
	@Test(dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testClearBeforeExpiration(CacheUnit cacheUnit) {
		cacheUnit.clearCache();
	}
	@Test(dataProvider="cache_unit_2_elements_2sec",dataProviderClass=DataProviderClass.class)
	public void testClearAfterExpiration(CacheUnit cacheUnit) {
		try {
			Thread.currentThread().sleep(2100);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		cacheUnit.clearCache();
	}
	
	
}
