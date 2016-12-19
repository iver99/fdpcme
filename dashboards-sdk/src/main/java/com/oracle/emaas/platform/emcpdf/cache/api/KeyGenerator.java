package com.oracle.emaas.platform.emcpdf.cache.api;

import com.oracle.emaas.platform.emcpdf.cache.Tenant;
import com.oracle.emaas.platform.emcpdf.cache.Keys;

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
     * @return a generated key
     */
    Object generate(Tenant tenant, Keys keys);
}
