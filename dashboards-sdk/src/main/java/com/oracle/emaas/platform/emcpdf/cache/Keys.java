package com.oracle.emaas.platform.emcpdf.cache;

/**
 * Created by chehao on 2016/12/11.
 */
public class Keys
{
    private final Object[] keys;

    public Keys(Object... keys)
    {
        this.keys = keys;
    }

    /**
     * @return the keys
     */
    public Object[] getKeys()
    {
        return keys;
    }
}

