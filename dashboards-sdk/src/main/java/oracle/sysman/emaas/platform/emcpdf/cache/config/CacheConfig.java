package oracle.sysman.emaas.platform.emcpdf.cache.config;

import java.util.ResourceBundle;

/**
 * Created by chehao on 2016/12/9.
 */
public class CacheConfig {
    //if you want to run local test, please comment this line of below code.
    public static ResourceBundle conf = ResourceBundle.getBundle("cache_config");
    //all cache group's expiration time
    public static final Long ADMIN_LINK_CACHE_EXPIRE_TIME = Long.valueOf(conf.getString("ADMIN_LINK_CACHE_EXPIRE_TIME"));
    public static final Long CLOUD_SERVICE_LINK_EXPIRE_TIME = Long.valueOf(conf.getString("CLOUD_SERVICE_LINK_EXPIRE_TIME"));
    public static final Long HOME_LINK_EXPIRE_TIME = Long.valueOf(conf.getString("HOME_LINK_EXPIRE_TIME"));
    public static final Long VISUAL_ANALYZER_LINK_EXPIRE_TIME = Long.valueOf(conf.getString("VISUAL_ANALYZER_LINK_EXPIRE_TIME"));
    public static final Long SERVICE_EXTERNAL_LINK_EXPIRE_TIME = Long.valueOf(conf.getString("SERVICE_EXTERNAL_LINK_EXPIRE_TIME"));
    public static final Long SERVICE_INTERNAL_LINK_EXPIRE_TIME = Long.valueOf(conf.getString("SERVICE_INTERNAL_LINK_EXPIRE_TIME"));
    public static final Long VANITY_BASE_URL_EXPIRE_TIME = Long.valueOf(conf.getString("VANITY_BASE_URL_EXPIRE_TIME"));
    public static final Long DOMAINS_DATA_EXPIRE_TIME = Long.valueOf(conf.getString("DOMAINS_DATA_EXPIRE_TIME"));
    public static final Long TENANT_APP_MAPPING_EXPIRE_TIME = Long.valueOf(conf.getString("TENANT_APP_MAPPING_EXPIRE_TIME"));
    public static final Long TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME = Long.valueOf(conf.getString("TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME"));
    public static final Long SCREENSHOT_EXPIRE_TIME = Long.valueOf(conf.getString("SCREENSHOT_EXPIRE_TIME"));
    public static final Long SSO_LOGOUT_EXPIRE_TIME = Long.valueOf(conf.getString("SSO_LOGOUT_URL_EXPIRE_TIME"));
    public static final Long ASSET_ROOT_EXPIRE_TIME = Long.valueOf(conf.getString("ASSET_ROOT_EXPIRE_TIME"));
    public static final Long REGISTRY_EXPIRE_TIME = Long.valueOf(conf.getString("REGISTRY_EXPIRE_TIME"));
    public static final Long TENANT_USER_EXPIRE_TIME = Long.valueOf(conf.getString("TENANT_USER_EXPIRE_TIME"));

//    public static final Long ETERNAL_EXPIRE_TIME = Long.valueOf(conf.getString("DEFAULT_EXPIRE_TIME"));
    public static final Long DEFAULT_EXPIRE_TIME = Long.valueOf(conf.getString("DEFAULT_EXPIRE_TIME"));

    //all cache group's cache capacity
    public static final Integer ADMIN_LINK_CACHE_CAPACITY = Integer.valueOf(conf.getString("ADMIN_LINK_CACHE_CAPACITY"));
    public static final Integer CLOUD_SERVICE_LINK_CAPACITY = Integer.valueOf(conf.getString("CLOUD_SERVICE_LINK_CAPACITY"));
    public static final Integer HOME_LINK_EXPIRE_CAPACITY = Integer.valueOf(conf.getString("HOME_LINK_EXPIRE_CAPACITY"));
    public static final Integer VISUAL_ANALYZER_LINK_CAPACITY = Integer.valueOf(conf.getString("VISUAL_ANALYZER_LINK_CAPACITY"));
    public static final Integer SERVICE_EXTERNAL_LINK_CAPACITY = Integer.valueOf(conf.getString("SERVICE_EXTERNAL_LINK_CAPACITY"));
    public static final Integer SERVICE_INTERNAL_LINK_CAPACITY = Integer.valueOf(conf.getString("SERVICE_INTERNAL_LINK_CAPACITY"));
    public static final Integer VANITY_BASE_URL_CAPACITY = Integer.valueOf(conf.getString("VANITY_BASE_URL_CAPACITY"));
    public static final Integer DOMAINS_DATA_CAPACITY = Integer.valueOf(conf.getString("DOMAINS_DATA_CAPACITY"));
    public static final Integer TENANT_APP_MAPPING_CAPACITY = Integer.valueOf(conf.getString("TENANT_APP_MAPPING_CAPACITY"));
    public static final Integer TENANT_SUBSCRIBED_SERVICES_CAPACITY = Integer.valueOf(conf.getString("TENANT_SUBSCRIBED_SERVICES_CAPACITY"));
    public static final Integer SCREENSHOT_CAPACITY = Integer.valueOf(conf.getString("SCREENSHOT_CAPACITY"));
    public static final Integer SSO_LOGOUT_CAPACITY = Integer.valueOf(conf.getString("SSO_LOGOUT_CAPACITY"));
    public static final Integer ASSET_ROOT_CAPACITY = Integer.valueOf(conf.getString("ASSET_ROOT_CAPACITY"));
    public static final Integer REGISTRY_CAPACITY = Integer.valueOf(conf.getString("REGISTRY_CAPACITY"));
    public static final Integer TENANT_USER_CAPACITY = Integer.valueOf(conf.getString("TENANT_USER_CAPACITY"));

//    public static final Integer ETERNAL_CAPACITY = Integer.valueOf(conf.getString("DEFAULT_CAPACITY"));
    public static final Integer DEFAULT_CAPACITY = Integer.valueOf(conf.getString("DEFAULT_CAPACITY"));

    //log cache status time interval
    public static final Long LOG_INTERVAL = Long.valueOf(conf.getString("LOG_INTERVAL"));
    //refresh cache interval
    public static final Long REFRESH_CACHE_INTERVAL = Long.valueOf(conf.getString("LOG_INTERVAL"));


   /**
     * below code are used when you want to run local test,and before you use , please comment above code.
     */
    /*public static final Long ADMIN_LINK_CACHE_EXPIRE_TIME = 20L;
    public static final Long CLOUD_SERVICE_LINK_EXPIRE_TIME =20L;
    public static final Long HOME_LINK_EXPIRE_TIME = 20L;
    public static final Long VISUAL_ANALYZER_LINK_EXPIRE_TIME = 20L;
    public static final Long SERVICE_EXTERNAL_LINK_EXPIRE_TIME = 20L;
    public static final Long SERVICE_INTERNAL_LINK_EXPIRE_TIME = 20L;
    public static final Long VANITY_BASE_URL_EXPIRE_TIME = 20L;
    public static final Long DOMAINS_DATA_EXPIRE_TIME = 20L;
    public static final Long TENANT_APP_MAPPING_EXPIRE_TIME = 20L;
    public static final Long TENANT_SUBSCRIBED_SERVICES_EXPIRE_TIME = 20L;
    public static final Long SCREENSHOT_EXPIRE_TIME = 20L;
    public static final Long SSO_LOGOUT_EXPIRE_TIME = 20L;
    public static final Long ASSET_ROOT_EXPIRE_TIME = 20L;
    public static final Long ETERNAL_EXPIRE_TIME = 20L;
    public static final Long DEFAULT_EXPIRE_TIME = 20L;
    public static final Long REGISTRY_EXPIRE_TIME = 20L;
    public static final Long TENANT_USER_EXPIRE_TIME = 20L;
    public static final Integer ADMIN_LINK_CACHE_CAPACITY = 20;
    public static final Integer CLOUD_SERVICE_LINK_CAPACITY = 20;
    public static final Integer HOME_LINK_EXPIRE_CAPACITY = 20;
    public static final Integer VISUAL_ANALYZER_LINK_CAPACITY = 20;
    public static final Integer SERVICE_EXTERNAL_LINK_CAPACITY = 20;
    public static final Integer SERVICE_INTERNAL_LINK_CAPACITY = 20;
    public static final Integer VANITY_BASE_URL_CAPACITY = 20;
    public static final Integer DOMAINS_DATA_CAPACITY = 20;
    public static final Integer TENANT_APP_MAPPING_CAPACITY = 20;
    public static final Integer TENANT_SUBSCRIBED_SERVICES_CAPACITY = 20;
    public static final Integer SCREENSHOT_CAPACITY = 20;
    public static final Integer SSO_LOGOUT_CAPACITY = 20;
    public static final Integer ASSET_ROOT_CAPACITY = 20;
    public static final Integer ETERNAL_CAPACITY = 20;
    public static final Integer DEFAULT_CAPACITY = 20;
    public static final Long LOG_INTERVAL = 20L;
    public static final Integer REGISTRY_CAPACITY = 20L;
    public static final Integer TENANT_USER_CAPACITY = 20L;
    public static final Long REFRESH_CACHE_INTERVAL = 20L;*/
}

