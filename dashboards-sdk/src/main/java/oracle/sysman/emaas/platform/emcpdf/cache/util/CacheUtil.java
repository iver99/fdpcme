/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.emcpdf.cache.util;

import oracle.sysman.emaas.platform.dashboards.ws.rest.CacheAPI;
import oracle.sysman.emaas.platform.emcpdf.cache.support.AbstractCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.lru.LRUCacheManager;
import oracle.sysman.emaas.platform.emcpdf.cache.support.screenshot.LRUScreenshotCacheManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author chehao
 * This cache util is for other service managing cache
 *
 */
public class CacheUtil
{
	private static final Logger LOGGER = LogManager.getLogger(CacheUtil.class);
	/**
	 *  clear all cache for a specific cache group
	 * @param groupName
	 * @return
	 */
	public static boolean clearCacheGroup(String groupName){
        if(StringUtils.isEmpty(groupName)){
        	LOGGER.warn("Cache group name is not illegal, can not clear cache group!");
            return false;
        }
        if(groupName.equals("screenshotCache")){
        	AbstractCacheManager screenshotCacheManager = LRUScreenshotCacheManager.getInstance();
            screenshotCacheManager.getCache(groupName).clear();
            LOGGER.info("Cache group {} is cleared!", "screenshotCache");
        }else{
        	AbstractCacheManager lruCacheManager = LRUCacheManager.getInstance();
            lruCacheManager.getCache(groupName).clear();
            LOGGER.info("Cache group {} is cleared!", groupName);
        }
		return true;
	}

}
