package com.oracle.platform.emaas.cache.api;

import com.oracle.platform.emaas.cache.Keys;
import com.oracle.platform.emaas.cache.Tenant;

/**
 * Created by chehao on 2016/12/11.
 */
public interface KeyGenerator
{
    /**
     * Generate a key for the given tenant and key list.
     *
     * @param tenant
     *            the tenant
     * @param params
     *            the key list
     * @return a generated key
     */
    Object generate(Tenant tenant, Keys keys);
}
