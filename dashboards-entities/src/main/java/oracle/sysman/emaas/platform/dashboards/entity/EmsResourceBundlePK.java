/*
 * Copyright (C) 2017 Oracle
 * All rights reserved.
 *
 * $$File: $$
 * $$DateTime: $$
 * $$Author: $$
 * $$Revision: $$
 */
 
package oracle.sysman.emaas.platform.dashboards.entity;

import java.io.Serializable;

/**
 * @author reliang
 *
 */
public class EmsResourceBundlePK implements Serializable {
    private static final long serialVersionUID = 4421017446909354425L;
    private String languageCode;
    private String countryCode;
    private String serviceName;
    
    @Override
    public boolean equals(Object other)
    {
        if (other instanceof EmsResourceBundlePK) {
            final EmsResourceBundlePK otherEmsResourceBundlePK = (EmsResourceBundlePK) other;
            final boolean areEqual = otherEmsResourceBundlePK.languageCode.equals(languageCode)
                    && otherEmsResourceBundlePK.countryCode.equals(countryCode)
                    && otherEmsResourceBundlePK.serviceName.equals(serviceName);
            return areEqual;
        }
        return false;
    }
    
    /**
     * 
     * @return the languageCode
     */
    public String getLanguageCode()
    {
        return languageCode;
    }
    /**
     * @param languageCode the languageCode to set
     */
    public void setLanguageCode(String languageCode)
    {
        this.languageCode = languageCode;
    }
    /**
     * @return the countryCode
     */
    public String getCountryCode()
    {
        return countryCode;
    }
    /**
     * @param countryCode the countryCode to set
     */
    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }
    /**
     * @return the serviceName
     */
    public String getServiceName()
    {
        return serviceName;
    }
    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

}
