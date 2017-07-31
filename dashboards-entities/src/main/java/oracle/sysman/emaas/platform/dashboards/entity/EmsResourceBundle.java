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
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author reliang
 *
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "EmsResourceBundle.deleteByServiceName", query = "delete from EmsResourceBundle t where t.serviceName = :serviceName"),
    @NamedQuery(name = "EmsResourceBundle.loadModifyTimeByLangContry", query = "select t.lastModificationDate from EmsResourceBundle t where t.languageCode = :languageCode and t.countryCode = :countryCode and t.serviceName = :serviceName order by t.lastModificationDate desc"),
    @NamedQuery(name = "EmsResourceBundle.loadByLangContry", query = "select t from EmsResourceBundle t where t.languageCode = :languageCode and t.countryCode = :countryCode and t.serviceName = :serviceName order by t.lastModificationDate desc")
})
@IdClass(EmsResourceBundlePK.class)
@Table(name = "EMS_DASHBOARD_RESOURCE_BUNDLE")
public class EmsResourceBundle implements Serializable {
    private static final long serialVersionUID = -3794500021876489604L;
    @Id
    @Column(name = "LANGUAGE_CODE")
    private String languageCode;
    @Id
    @Column(name = "COUNTRY_CODE")
    private String countryCode;
    @Id
    @Column(name = "SERVICE_NAME")
    private String serviceName;
    @Column(name = "SERVICE_VERSION")
    private String serviceVersion;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "PROPERTIES_FILE", columnDefinition = "CLOB NULL")
    private String propertiesFile;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFICATION_DATE")
    private Date lastModificationDate;
    
    /**
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
    /**
     * @return the serviceVersion
     */
    public String getServiceVersion()
    {
        return serviceVersion;
    }
    /**
     * @param serviceVersion the serviceVersion to set
     */
    public void setServiceVersion(String serviceVersion)
    {
        this.serviceVersion = serviceVersion;
    }
    /**
     * @return the propertiesFile
     */
    public String getPropertiesFile()
    {
        return propertiesFile;
    }
    /**
     * @param propertiesFile the propertiesFile to set
     */
    public void setPropertiesFile(String propertiesFile)
    {
        this.propertiesFile = propertiesFile;
    }
    /**
     * @return the lastModificationDate
     */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }
    /**
     * @param lastModificationDate the lastModificationDate to set
     */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }
}
