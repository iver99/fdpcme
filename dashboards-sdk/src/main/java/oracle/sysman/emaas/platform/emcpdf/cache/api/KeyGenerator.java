package oracle.sysman.emaas.platform.emcpdf.cache.api;

import oracle.sysman.emaas.platform.emcpdf.cache.tool.Tenant;
import oracle.sysman.emaas.platform.emcpdf.cache.tool.Keys;

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
