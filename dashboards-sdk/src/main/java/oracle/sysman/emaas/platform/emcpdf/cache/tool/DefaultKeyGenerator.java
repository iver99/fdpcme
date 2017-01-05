package oracle.sysman.emaas.platform.emcpdf.cache.tool;

import oracle.sysman.emaas.platform.emcpdf.cache.api.IKeyGenerator;

/**
 * Created by chehao on 2016/12/11.
 */
public class DefaultKeyGenerator implements IKeyGenerator
{
    public static DefaultKeyGenerator instance=new DefaultKeyGenerator();

    private DefaultKeyGenerator() {
    }

    @Override
    public Object generate(Tenant tenant, Keys keys)
    {
        return new DefaultKey(tenant, keys == null ? null : keys.getKeys());
    }

    public static DefaultKeyGenerator getInstance(){
        return instance;
    }
}
