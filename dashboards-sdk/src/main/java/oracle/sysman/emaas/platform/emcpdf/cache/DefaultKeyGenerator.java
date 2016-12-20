package oracle.sysman.emaas.platform.emcpdf.cache;

import oracle.sysman.emaas.platform.emcpdf.cache.api.KeyGenerator;

/**
 * Created by chehao on 2016/12/11.
 */
public class DefaultKeyGenerator implements KeyGenerator
{
    /* (non-Javadoc)
     * @see oracle.sysman.emaas.platform.dashboards.core.cache.KeyGenerator#generate(java.lang.String, java.lang.Object[])
     */
    @Override
    public Object generate(Tenant tenant, Keys keys)
    {
        return new DefaultKey(tenant, keys == null ? null : keys.getKeys());
    }
}
