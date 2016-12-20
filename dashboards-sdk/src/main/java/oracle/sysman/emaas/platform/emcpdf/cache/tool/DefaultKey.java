package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import oracle.sysman.emaas.platform.emcpdf.cache.util.StringUtil;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by chehao on 2016/12/11.
 */
public class DefaultKey implements Serializable
{
    private final Tenant tenant;
    private final Object[] params;
    private final int hashCode;

    /**
     *
     * @param tenant
     * @param keys
     */
    public DefaultKey(Tenant tenant, Object... keys)
    {
        this.tenant = tenant;
        if (keys == null) {
            throw new IllegalArgumentException("Keys must not be null");
        }
        params = new Object[keys.length];
        System.arraycopy(keys, 0, params, 0, keys.length);
        hashCode = (tenant == null ? 0 : tenant.hashCode() * 31) + Arrays.deepHashCode(params);
    }

    @Override
    public boolean equals(Object obj)
    {
        return this == obj || obj instanceof DefaultKey && (tenant == null ? ((DefaultKey) obj).tenant == null
                : tenant.equals(((DefaultKey) obj).tenant) && Arrays.deepEquals(params, ((DefaultKey) obj).params));
    }

    @Override
    public final int hashCode()
    {
        return hashCode;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " [Tenant - " + tenant + ", Keys - " + StringUtil.arrayToCommaDelimitedString(params)
                + "]";
    }
}

