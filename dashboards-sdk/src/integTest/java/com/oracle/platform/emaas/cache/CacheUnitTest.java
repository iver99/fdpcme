package com.oracle.platform.emaas.cache;

import org.testng.Assert;
import org.testng.annotations.ExpectedExceptions;
import org.testng.annotations.Test;

import java.util.logging.Logger;

/**
 * Created by chehao on 2016/12/14.
 */
@SuppressWarnings("all")
public class CacheUnitTest {

    public static final Logger LOGGER=Logger.getLogger(CacheUnitTest.class.getName());

    /**
     * test put action begins
     */

    @Test(groups = { "s2" })
    public void testPutCache(){
        CacheUnit cacheUnit=new CacheUnit();
        cacheUnit.put("1", new Element("1","one"));
        Assert.assertNotNull(cacheUnit.get("1"));
    }

    @Test(groups = { "s2" })
    public void testPutWhenEmpty(){
        CacheUnit cacheUnit=new CacheUnit();
        cacheUnit.put("1", new Element("1","One"));
    }

    @Test(groups = { "s2" })
    public void testPutWhenNotEmpty(){
        CacheUnit cacheUnit=new CacheUnit();
        cacheUnit.put("1", new Element("1","one"));
        cacheUnit.put("2", new Element("2", "two"));
        cacheUnit.put("3", new Element("3", "three"));
        cacheUnit.put("1", new Element("1","second one"));
    }

    @Test(groups = { "s2" })
    public void testPutIfAbsent(){
        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.put("4", new Element("4","four"));
    }

    @Test(groups = { "s2" })
    @ExpectedExceptions(value = { IllegalArgumentException.class })
    public void testPutNullKey(){
        CacheUnit cacheUnit=new CacheUnit();
        cacheUnit.put(null, new Element("1","one"));
    }
    @ExpectedExceptions(value = { IllegalArgumentException.class })
    @Test(groups = { "s2" })
    public void testPutNullValue(){
        CacheUnit cacheUnit=new CacheUnit();
        cacheUnit.put("1", null);
    }

    /**
     * test get action begins
     */

    @Test(groups = { "s2" })
    public void testGetCache(){
        CacheUnit cacheUnit=new CacheUnit();
        cacheUnit.put("1", new Element("1","one"));
        cacheUnit.put("2", new Element("2", "two"));
        cacheUnit.put("3", new Element("3", "three"));
        cacheUnit.get("1");
        cacheUnit.get("2");
        cacheUnit.get("3");
        cacheUnit.get("4");
    }
    @Test(groups = { "s2" })
    public void testGetWhenEmpty(){
        CacheUnit cacheUnit=new CacheUnit();
        Assert.assertEquals(cacheUnit.isEmpty(), true);
        Assert.assertEquals(cacheUnit.get("1"), null);
    }
    @Test(groups = { "s2" })
    public void testGetWhenNotEmpty(){
        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        Assert.assertEquals(cu.isEmpty(), false);
        cu.get("1");
    }

    /**
     *	test remove action begins
     */

    @Test(groups = { "s2" })
    public void testRemoveCache(){
        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.remove("2");
    }
    @Test(groups = { "s2" })
    public void testRemoveWhenEmpty(){
        CacheUnit cacheUnit=new CacheUnit();
        Assert.assertEquals(cacheUnit.isEmpty(), true);
        Assert.assertEquals(cacheUnit.remove("1"), false);
    }
    @Test(groups = { "s2" })
    public void testRemoveWhenNotEmpty(){
        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.remove("1");
    }
    @Test(groups = { "s2" })
    public void testRemoveWhenExists(){
        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.remove("1");
    }
    @Test(groups = { "s2" })
    public void testRemoveWhenNotExists(){

        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        Assert.assertEquals(cu.remove("4"), false);
    }
    /**
     * 	test clear action begins
     */
    @Test(groups = { "s2" })
    public void testClearWhenEmpty(){
        CacheUnit cacheUnit=new CacheUnit();
        cacheUnit.clearCache();
    }
    @Test(groups = { "s2" })
    public void testClearWhenNotEmpty(){
        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.clearCache();
    }

    /**
     *	test get capacity actions begins
     */

    @Test(groups = { "s2" })
    public void testCacheUnitCapacity(){
        CacheUnit cacheUnit =new CacheUnit();
        Assert.assertEquals(cacheUnit.getCacheCapacity(), CacheUnit.DEFAULT_CACHE_CAPACITY);
    }
    @Test(groups = { "s2" })
    public void testCapacityWhenEmpty(){
        CacheUnit cacheUnit =new CacheUnit();
        cacheUnit.getCacheCapacity();
    }
    @Test(groups = { "s2" })
    public void testCapacityWhenNotEmpty(){
        CacheUnit cu=new CacheUnit();
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.getCacheCapacity();
    }

    @Test(groups = { "s2" })
    public void testGetBeforeExpiration(){
        CacheUnit cu=new CacheUnit(20);
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.get("1");
    }
    @Test(groups = { "s2" })
    public void testGetAfterExpiration() {
        CacheUnit cu=new CacheUnit(2);
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        try {
            Thread.currentThread().sleep(2100);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
    }
    @Test(groups = { "s2" })
    public void testPutBeforeExpiration() {
        CacheUnit cu=new CacheUnit(20);
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.put("4", new Element("4","four"));
    }
    @Test(groups = { "s2" })
    public void testPutAfterExpiration() {
        CacheUnit cu=new CacheUnit(2);
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.get("1");
        try {
            Thread.currentThread().sleep(2100);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        cu.get("1");
    }
    @Test(groups = { "s2" })
    public void testRemoveBeforeExpiration() {
        CacheUnit cu=new CacheUnit(2);
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.remove("1");
    }
    @Test(groups = { "s2" })
    public void testClearBeforeExpiration() {
        CacheUnit cu=new CacheUnit(2);
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        cu.clearCache();
    }
    @Test(groups = { "s2" })
    public void testClearAfterExpiration() {
        CacheUnit cu=new CacheUnit(2);
        cu.put("1", new Element("1","one"));
        cu.put("2", new Element("2", "two"));
        cu.put("3", new Element("3", "three"));
        try {
            Thread.currentThread().sleep(2100);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        cu.clearCache();
    }


}

