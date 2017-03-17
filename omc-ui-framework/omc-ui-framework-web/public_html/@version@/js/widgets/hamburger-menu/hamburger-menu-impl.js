define('uifwk/@version@/js/widgets/hamburger-menu/hamburger-menu-impl', [
    'jquery', 
    'ojs/ojcore', 
    'knockout',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/preference-util-impl', 
    'uifwk/@version@/js/sdk/context-util-impl',
    'uifwk/@version@/js/sdk/menu-util-impl',
    'uifwk/@version@/js/sdk/SessionCacheUtil',
    'ojs/ojnavigationlist',
    'ojs/ojjsontreedatasource'],
        function ($, oj, ko, nls, dfumodel, pfumodel, ctxmodel, menumodel, sessionCacheModel) {
            function HamburgerMenuViewModel(params) {
                var self = this;
                var userName = params.userName;
                var tenantName = params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                var ctxUtil = new ctxmodel();
                var prefUtil = new pfumodel(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
                var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
                var isSetAsHomeChecked = false;
                var omcHomeUrl = null;
                var menuUtil = new menumodel();
                var rootCompositeMenuid = 'omc_root_composite';
                var menuSessionCacheName = '_uifwk_servicemenucache';
                var sessionCacheAllMenusKey = 'omc_hamburger_menu';
                var sessionCacheOmcMenusDataKey = 'omc_menus';
                var sessionCacheServiceMenuDataKey = 'service_menu_data';
                var sessionCacheOmcMenusServiceLinksKey = 'service_links';
                var sessionCacheOmcMenusPrivilegeKey = 'privilege_list';
                var sessionCacheOmcMenusSubscribedAppsKey = 'subscribed_apps';
                var sessionCacheBaseVanityUrlsKey = 'base_vanity_urls';
                var omcMenuSeparatorId = 'omc_service_menu_separator';
                
                var userName = params.userName;
                var tenantName = params.tenantName;
                var serviceAppId = params.appId;
                self.selectedItem = ko.observable();
                self.expanded = ko.observableArray([]);
                
                //
                // sessionStorage cache
                //
                var sessionCaches = [];
                var sessionCacheNames = [menuSessionCacheName];
                for (var i = 0; i < sessionCacheNames.length; i++) {
                    sessionCaches.push(new sessionCacheModel(sessionCacheNames[i], 1));
                }
                if (window.performance) {
                    //We should only clear the cache once during a page refresh, otherwise
                    //it may cause cached data lost though service menus already fetched
                    if (window.performance.navigation.type === 1 && !window._uifwk.isOmcServiceMenuCacheCleared) {
                        for (var i = 0; i < sessionCaches.length; i++) {
                            sessionCaches[i].clearCache();
                        }
                        window._uifwk.isOmcServiceMenuCacheCleared = true;
                    }
                }
                
                //Clear old composite menu items before show up a new composite menu
                function clearCompositeMenuItems() {
                    var size = omcMenus.length;
                    if (omcMenus[size-1] && omcMenus[size-1].attr.id === rootCompositeMenuid) {
                        omcMenus.pop();
                    }
                    if (self.serviceMenuData[size-1] && self.serviceMenuData[size-1].id === rootCompositeMenuid) {
                        self.serviceMenuData.pop();
                    }
                    currentCompositeParentId = null;
                }
                
                var currentCompositeParentId = null;
                //Show up a composite menu
                function jumpToCompositeMenu(parentMenuId, rootMenuLabel, menuJson) {
                    if (menuJson && menuJson.serviceCompositeMenus) {
                        clearCompositeMenuItems();
                        currentCompositeParentId = parentMenuId;
                        if (menuJson.serviceMenuMsgBundle) {
                            var url = menuJson.serviceMenuMsgBundle;
                            if (dfu.isDevMode()) {
                                var dfBaseUrl = dfu.getDevData().dfRestApiEndPoint;
                                url = dfBaseUrl.substring(0, dfBaseUrl.indexOf('/emcpdf/')) + url;
                            }
                            else {
                                url = url.substring(url.indexOf('/emsaasui/') + 1, url.length - 3);
                            }
//                            //testing code
//                            url = menuJson.serviceMenuMsgBundle.substring(menuJson.serviceMenuMsgBundle.indexOf('/emsaasui/') + 1, menuJson.serviceMenuMsgBundle.length - 3); 
//                            //testing code
                            
                            require(['ojL10n!' + url], function (_nls) {
                                var rootCompositMenuItem = {'id': rootCompositeMenuid, 
                                                    'labelKey': rootMenuLabel, 
                                                    'externalUrl': '#', 
                                                    'children': menuJson.serviceCompositeMenus};
                                rootCompositMenuItem = applyNlsOnMenu(rootCompositMenuItem, _nls);
                                self.serviceMenuData.push(rootCompositMenuItem);
                                var compositeMenu = getMenuItem(rootCompositMenuItem);
                                omcMenus.push(compositeMenu);
                                self.expanded([rootCompositeMenuid]);
                                self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                                $("#omcMenuNavList").ojNavigationList("refresh");
                            });
                        }
                    }                  
                }
                menuUtil.subscribeCompositeMenuDisplayEvent(jumpToCompositeMenu);
                
                //Register service menus at runtime when UIFWK registerServiceMenus() API is called
                function registerServiceMenus(menuJson) {
                    if (menuJson) {
                        var serviceItem = {};
                        serviceItem.appId = serviceAppId;
                        serviceItem.serviceMenus = menuJson.serviceMenus;
                        serviceItem.serviceAdminMenus = menuJson.serviceAdminMenus;
                        var msgBundleUrl = menuJson.serviceMenuMsgBundle;
                        if (msgBundleUrl) {
                            msgBundleUrl = msgBundleUrl.substring(msgBundleUrl.indexOf('/emsaasui/') + 1, msgBundleUrl.length - 3);
                        }
                        //Load resource bundle file for NLSed strings
                        require(['ojL10n!' + msgBundleUrl], function (_nls) {
                            serviceItem.serviceMenuMsgBundle = _nls;
                            serviceItem.serviceMenus = applyNlsOnMenu(serviceItem.serviceMenus, _nls);
                            serviceItem.serviceAdminMenus = applyNlsOnMenu(serviceItem.serviceAdminMenus, _nls);
                            
                            var menuId = findAppItemIndex(rootMenuData, 'omc_root_' + serviceAppId);
                            if (self.serviceMenuData[menuId]) {
                                if (serviceItem.serviceMenus) {
                                    self.serviceMenuData[menuId].children = serviceItem.serviceMenus;
                                    var menuItem = getMenuItem(self.serviceMenuData[menuId]);
                                    omcMenus[menuId] = menuItem;
                                }
                                if (serviceItem.serviceAdminMenus && serviceItem.serviceAdminMenus.children) {
                                    if (serviceItem.serviceMenus && serviceItem.serviceMenus.length > 0) {
                                        var lastServiceMenuItem = serviceItem.serviceMenus[serviceItem.serviceMenus.length - 1];
                                        if (lastServiceMenuItem.type !== 'divider') {
                                            var dividerItem = generateDividerItem('omc_' + serviceItem.appId);
                                            self.serviceMenuData[menuId].children.push(dividerItem);
                                            omcMenus[menuId].children.push(getMenuItem(dividerItem));
                                        }
                                    }
                                    self.serviceMenuData[menuId].children.push(serviceItem.serviceAdminMenus);
                                    omcMenus[menuId].children.push(getMenuItem(serviceItem.serviceAdminMenus));
                                    var adminMenuId = findAppItemIndex(self.serviceMenuData,'omc_root_admin');
                                    var adminSubMenuId = findAppItemIndex(self.serviceMenuData[adminMenuId].children, 'omc_root_admin_grp_'+serviceItem.appId);
                                    if (adminSubMenuId > -1) {
                                        self.serviceMenuData[adminMenuId].children[adminSubMenuId].children = serviceItem.serviceAdminMenus.children;
                                        omcMenus[adminMenuId].children[adminSubMenuId].attr['disabled'] = false;
                                        var serviceAdminItem = getMenuItem(serviceItem.serviceAdminMenus)
                                        omcMenus[adminMenuId].children[adminSubMenuId].children = serviceAdminItem.children;
                                    }
                                    else {
                                        if (!self.serviceMenuData[adminMenuId].children) {
                                            self.serviceMenuData[adminMenuId].children = [];
                                        }
                                        self.serviceMenuData[adminMenuId].children.push($.extend(true, {}, serviceItem.serviceAdminMenus));
                                        self.serviceMenuData[adminMenuId].children[self.serviceMenuData[adminMenuId].children.length].id = 'omc_root_admin_grp_'+serviceItem.appId;
                                        omcMenus[adminMenuId].children.push(getMenuItem(self.serviceMenuData[adminMenuId].children[self.serviceMenuData[adminMenuId].children.length]));
                                    }
                                }
                                
                                self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                            }
                            
                        });                        
                    }
                }
                
                menuUtil.subscribeServiceMenuRegisterEvent(registerServiceMenus);
                
                self.hamburgerRootMenuLabel = nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LABEL;
                var rootMenuData = [
                    {'id': 'omc_root_home', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_HOME_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_alerts', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ALERTS_LABEL, 'externalUrl': '#'},
//                    {'id': 'omc_root_applications', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APPS_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_dashboards', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DASHBOARDS_LABEL, 'externalUrl': '#'},
//                    {'id': 'omc_root_savedsearches', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SAVEDSEARCH_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_dataexplorer', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DATAEXPLORER_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_divider', type: 'divider', 'labelKey': '', 'externalUrl': '#'},
                    {'id': 'omc_root_APM', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APM_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Monitoring', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_MONITORING_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_LogAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LA_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_ITAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ITA_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Orchestration', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ORCHESTRATION_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_SecurityAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SECURITY_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_Compliance', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_COMPLIANCE_LABEL, 'externalUrl': '#'},
                    {'id': 'omc_root_divider1', type: 'divider', 'labelKey': '', 'externalUrl': '#'},
                    {'id': 'omc_root_admin', type: 'menu_group', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ADMIN_LABEL, 'externalUrl': '#', children: [
                            {'id': 'omc_root_admin_alertrules', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ALERTRULES_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_agents', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_AGENTS_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_entitiesconfig', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ENTITIESCONFIG_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_divider', type: 'divider', 'labelKey': '', 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_APM', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_APM_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_Monitoring', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_MONITORING_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_LogAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_LOG_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_SecurityAnalytics', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_SECURITY_ADMIN_LABEL, 'externalUrl': '#'},
                            {'id': 'omc_root_admin_grp_Compliance', type: 'menu_item', 'labelKey': nls.BRANDING_BAR_HAMBURGER_MENU_COMPLIANCE_ADMIN_LABEL, 'externalUrl': '#'}
                    ]}
                ];
                
                var defaultMenuIds = ['omc_root_home',
                    'omc_root_alerts',
                    'omc_root_dashboards',
                    'omc_root_admin', 
                    'omc_root_admin_alertrules', 
                    'omc_root_admin_agents',
                    'omc_root_admin_entitiesconfig',
                    rootCompositeMenuid
                ];

                self.privilegeList = [];
                self.subscribedApps = [];
                self.serviceMenuData = [];
                self.baseVanityUrls = null;
                self.dataSource = ko.observable();
                var omcMenus = [];
                var globalMenuIdHrefMapping = null;
                
                //Get vanity base URLs for all subscribed services
                function fetchBaseVanityUrls() {
                    var dfdFetchBaseVanityUrls = $.Deferred();
                    var host = window.location.host;
                    if (host.indexOf(tenantName + '.') === 0) {
                        menuUtil.getServiceBaseVanityUrls(function(data){
                            self.baseVanityUrls = data;
                            dfdFetchBaseVanityUrls.resolve();
                        });
                    }
                    else {
                        dfdFetchBaseVanityUrls.resolve();
                    }
                    return dfdFetchBaseVanityUrls;
                }
                
                //Load service menus from service registry JSON files
                function loadServiceMenus() {
                    var dfdLoadServiceMenus = $.Deferred();
                        dfu.getRegistrations(function(data){
                            self.registration = data;
                            fetchGlobalMenuLinks(self.registration);
                            self.serviceLinks = data.serviceMenus;
//                            //testing code start
//                            self.serviceLinks = [
//                                {
//                                    'appId': 'APM',
//                                    'serviceName': 'ApmUI',
//                                    'version': '1.0',
//                                    'metaDataHref': 'http://localhost:8383/emsaasui/uifwk/@version@/data/serviceMenus.json'
//                                }
//                            ];
//                            //testing code end
                            if (self.serviceLinks && self.serviceLinks.length > 0) {
                                loadServiceData(dfdLoadServiceMenus, self.serviceLinks);
                            }
                            else {
                                dfdLoadServiceMenus.resolve();
                            }
                        }, true, function(){
                            dfdLoadServiceMenus.reject();
                        });
                    return dfdLoadServiceMenus;
                }
                
                //Get all privileges that have been granted to current user
                function getUserGrants() {
                    var dfdGetUserGrants = $.Deferred();
                    if(!self.privilegeList || self.privilegeList.length < 1){
                        var url = "/sso.static/getUserGrants";
                        if (dfu.isDevMode()) {
                            var odsUrl = dfu.getDevData().odsRestApiEndPoint;
                            var odsBaseUrl = odsUrl.substring(0, odsUrl.indexOf('/ods-query'));
                            url = odsBaseUrl + "/authorization/ws/api/v1/priv/grants/getUserGrants";
//                            //testing
//                            url = '/emsaasui/uifwk/test.html';
//                            //testing
                        }
                        url = url + '?granteeUser=' + tenantName + '.' + userName;
                        var header = dfu.getDefaultHeader();
                        dfu.ajaxWithRetry(url, {
                                type: 'get',
                                headers: header,
                                success: function (data) {
//                                    //testing
//                                    data = 'ADMINISTER_LOG_TYPE,RUN_AWR_VIEWER_APP,USE_TARGET_ANALYTICS,ADMIN_ITA_WAREHOUSE,ADMINISTER_ROLE,RUN_MW_PERF_APP,MANAGE_CREDENTIALS,FULL_ANY_TARGET,RUN_DB_PERF_APP,DELETE_ANY_LOG,VIEW_SOURCE_DETAILS,OVERRIDE_OOTB_RULE_SETTINGS,CREATE_UPLOAD,RUN_DB_RESOURCE_APP,ADMIN_ITA_TARGET,RUN_FLEX_ANALYZER_APP,ASSOCIATE_RULE_TO_TARGET,VIEW_APMAAS_MONITORING_DATA,VIEW_LOG_TYPE_DETAILS,SET_RULE_TO_AUTO_ASSOCIATE,RUN_RESOURCE_ANALYTICS_APP,MANAGE_ALERT_SETUP,MANAGE_APMAAS_AGENT,VIEW_ANY_TARGET,ADMINISTER_SOURCE,SEARCH_LOGS';
//                                    //testing
                                    self.privilegeList = data;
                                    dfdGetUserGrants.resolve();
                                },
                                error: function (xhr, textStatus, errorThrown) {
                                    dfdGetUserGrants.reject();
                                    oj.Logger.error("Failed to get UserGrants due to error: " + textStatus);
                                }
                            });
                    }
                    return dfdGetUserGrants;
                }
                
                //Fetch all subscribed application names for current tenant user
                function getSubscribedApps() {
                    var dfdGetSubscribedApps = $.Deferred();
                    if (!self.subscribedApps || self.subscribedApps.length < 1) {
                        function subscribedAppsCallback(data) {
                            if (!data) {
                                dfdGetSubscribedApps.reject();
                            }
                            else {
                                self.subscribedApps = data;
                                dfdGetSubscribedApps.resolve();
                            }
                        }
                        dfu.checkSubscribedApplications(subscribedAppsCallback);
                    }
                    return dfdGetSubscribedApps;
                }
                
                //Load service menus meta-data from registered JSON files
                function loadServiceData(dfd, serviceLinks) {
                    if(!self.allServiceData || self.allServiceData.length < 1){
                        self.allServiceData = [];
                        self.loadedServiceCnt = ko.observable(0);
                        self.loadedServiceCnt.subscribe(function(cnt) {
                            if (cnt === serviceLinks.length) {
                                dfd.resolve();
                            }
                        });
                        //Load json files
                        $.each(serviceLinks, function(idx, linkItem){
                            var serviceItem = {};
                            serviceItem.appId = linkItem.appId;
                            serviceItem.serviceName = linkItem.serviceName;
                            serviceItem.version = linkItem.version;
                            var header = dfu.getDefaultHeader();

                            var url = linkItem.metaDataHref;
                            if (dfu.isDevMode()) {
                                url = url.replace("https://", "http://").replace("4443", "7019");
                            }
                            dfu.ajaxWithRetry(url, {
                                type: 'get',
                                dataType: 'json',
                                contentType: "application/json",
                                headers: header,
                                success: function (data) {
                                    serviceItem.serviceMenus = data.serviceMenus;
                                    serviceItem.serviceAdminMenus = data.serviceAdminMenus;
                                    url = data.serviceMenuMsgBundle;
                                    if (!url){
                                        self.loadedServiceCnt(self.loadedServiceCnt() + 1);
                                        return;
                                    }
                                    if (dfu.isDevMode()) {
                                        var dfBaseUrl = dfu.getDevData().dfRestApiEndPoint;
                                        url = dfBaseUrl.substring(0, dfBaseUrl.indexOf('/emcpdf/')) + url;
                                    }
                                    else {
                                        url = url.substring(url.indexOf('/emsaasui/') + 1, url.length - 3);
                                    }
//                                    //testing code
//                                    url = data.serviceMenuMsgBundle.substring(data.serviceMenuMsgBundle.indexOf('/emsaasui/') + 1, data.serviceMenuMsgBundle.length - 3); 
//                                    //testing code
                                    
                                    //Load resource bundle files
                                    require(['ojL10n!' + url], function (_nls) {
                                        serviceItem.serviceMenuMsgBundle = _nls;
                                        serviceItem.serviceMenus = applyNlsOnMenu(serviceItem.serviceMenus, _nls);
                                        serviceItem.serviceAdminMenus = applyNlsOnMenu(serviceItem.serviceAdminMenus, _nls);
                                        self.allServiceData.push(serviceItem);
                                        self.loadedServiceCnt(self.loadedServiceCnt() + 1);
                                    });
                                },
                                error: function (xhr, textStatus, errorThrown) {
                                    self.loadedServiceCnt(self.loadedServiceCnt() + 1);
                                    oj.Logger.error("Failed to load service menus due to error: " + textStatus);
                                }
                            });
                        });
                    }
                }
                
                //Generate a menu separator item
                function generateDividerItem(prefix) {
                    var dividerId = prefix + '_' + [].toString.apply(window.crypto&&window.crypto.getRandomValues(new Uint32Array(1))||window.msCrypto&&window.msCrypto.getRandomValues(new Uint32Array(1)));
                    return {'id': dividerId, type: 'divider', 'labelKey': '', 'externalUrl': '#'};
                };
                
                //If omc service menus have been cached in window session storage, get it directly from cache
                var cachedMenus = sessionCaches[0].retrieveDataFromCache(sessionCacheAllMenusKey);
                if (cachedMenus && cachedMenus[sessionCacheOmcMenusDataKey] && cachedMenus[sessionCacheOmcMenusServiceLinksKey] && cachedMenus[sessionCacheOmcMenusPrivilegeKey] && cachedMenus[sessionCacheOmcMenusSubscribedAppsKey]) {
                    omcMenus = cachedMenus[sessionCacheOmcMenusDataKey];
                    self.subscribedApps = cachedMenus[sessionCacheOmcMenusSubscribedAppsKey];
                    self.serviceLinks = cachedMenus[sessionCacheOmcMenusServiceLinksKey];
                    self.privilegeList = cachedMenus[sessionCacheOmcMenusPrivilegeKey];
                    self.serviceMenuData = cachedMenus[sessionCacheServiceMenuDataKey];
                    self.baseVanityUrls = cachedMenus[sessionCacheBaseVanityUrlsKey];
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    menuUtil.fireServiceMenuLoadedEvent();
                    var selectedMenuId = params.omcCurrentMenuId;
                    if (selectedMenuId) {
                        menuUtil.setCurrentMenuItem(selectedMenuId);
                    }
                }
                //otherwise, get all service menus from service registries
                else {
                    $.when(loadServiceMenus(), getUserGrants(), getSubscribedApps(), fetchBaseVanityUrls()).done(function() {
                        for (var k = 0; k < rootMenuData.length; ++k) {
                            rootMenuData[k].externalUrl = globalMenuIdHrefMapping[rootMenuData[k].id] ? globalMenuIdHrefMapping[rootMenuData[k].id] : '#';
                            self.serviceMenuData.push($.extend(true, {}, rootMenuData[k]));
                        }
                        self.allServiceData && $.each(self.allServiceData, function (idx, singleServiceData) {
                            //Find index for parent root menu item
                            var menuId = findAppItemIndex(rootMenuData, 'omc_root_' + singleServiceData.appId);
                            if (self.serviceMenuData[menuId]) {
                                if (singleServiceData.serviceMenus) {
                                    self.serviceMenuData[menuId].children = singleServiceData.serviceMenus;
                                }
                                //Load service admin menu items
                                if (singleServiceData.serviceAdminMenus && singleServiceData.serviceAdminMenus.children) {
                                    //Add a separator between service menus and admin menus if there is no one defined in JSON
                                    if (singleServiceData.serviceMenus && singleServiceData.serviceMenus.length > 0) {
                                        var lastServiceMenuItem = singleServiceData.serviceMenus[singleServiceData.serviceMenus.length - 1];
                                        if (lastServiceMenuItem.type !== 'divider') {
                                            var dividerItem = generateDividerItem('omc_' + singleServiceData.appId);
                                            self.serviceMenuData[menuId].children.push(dividerItem);
                                        }
                                    }
                                    //Append service admin menus to global service admin menus
                                    self.serviceMenuData[menuId].children.push(singleServiceData.serviceAdminMenus);
                                    var adminMenuId = findAppItemIndex(self.serviceMenuData,'omc_root_admin');
                                    var adminSubMenuId = findAppItemIndex(self.serviceMenuData[adminMenuId].children, 'omc_root_admin_grp_'+singleServiceData.appId);
                                    if (adminSubMenuId > -1) {
                                        self.serviceMenuData[adminMenuId].children[adminSubMenuId].children = singleServiceData.serviceAdminMenus.children;
                                    }
                                    else {
                                        if (!self.serviceMenuData[adminMenuId].children) {
                                            self.serviceMenuData[adminMenuId].children = [];
                                        }
                                        self.serviceMenuData[adminMenuId].children.push($.extend(true, {}, singleServiceData.serviceAdminMenus));
                                        self.serviceMenuData[adminMenuId].children[rootMenuData[adminMenuId].children.length].id = 'omc_root_admin_grp_'+singleServiceData.appId;
                                    }
                                }
                            }
                        });
                        //Construct menu items as data source for jet ojNavigationList component
                        for (var j = 0; j < self.serviceMenuData.length; j++) {
                            var item = self.serviceMenuData[j];
                            var menuItem = getMenuItem(item);
                            omcMenus.push(menuItem);
                        }
                        //Cache the entire OMC menus and related data into window session storage
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusSubscribedAppsKey, self.subscribedApps);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusServiceLinksKey, self.serviceLinks);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusPrivilegeKey, self.privilegeList);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusDataKey, omcMenus);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheServiceMenuDataKey, self.serviceMenuData);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheBaseVanityUrlsKey, self.baseVanityUrls);
                        self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                        menuUtil.fireServiceMenuLoadedEvent();
                        //Set current menu item
                        setTimeout(function(){
                            var selectedMenuId = params.omcCurrentMenuId;
                            if (selectedMenuId) {
                                menuUtil.setCurrentMenuItem(selectedMenuId);
                            }
                        }, 10);
                    });
                }
                
                //Do privilege check
                function checkPrivilege(requiredPrivilege) {
                    if (!requiredPrivilege) {
                        return true;
                    }

                    if (Array.isArray(requiredPrivilege)) {
                        for (var _idx = 0; _idx < requiredPrivilege.length; ++_idx) {
                            if (self.privilegeList.indexOf(requiredPrivilege[_idx]) < 0) {
                                return false;
                            }
                        }
                        return true;
                    } 
                    else {
                        if (self.privilegeList.indexOf(requiredPrivilege) < 0) {
                                return false;
                        }
                        else {
                            return true;
                        }
                    }
                }
                
                //Check whether a application is subscribed by current tenant
                function isAppSubscribed(appId) {
                    if (self.subscribedApps) {
                        for (var i = 0; i < self.subscribedApps.length; i++) {
                            if (self.subscribedApps[i] === appId) {
                                return true;
                            }
                        }
                        return false;
                    }
                    else {
                        return false;
                    }
                }
                
                //Do privilege check and subscription check to disable/hide menu items
                function filterAuthorizedMenuItem(rawMenuObj){
                    if (!Array.isArray(rawMenuObj)) {
                        var menuItem = rawMenuObj;
                        //Some root menus should be always be there, like Home, Dashboards, Alerts etc.
                        if (defaultMenuIds.indexOf(menuItem.id) > -1) {
                            return menuItem;
                        }
                        
                        //Subscription check
                        if (menuItem.id.indexOf("omc_root_") > -1) {
                            var appId = null;
                            //Do subscription check for Data Explorer, which is included in ITAnalytics service
                            if (menuItem.id === 'omc_root_dataexplorer') {
                                appId = 'ITAnalytics';
                            }
                            else {
                                //If no service admin menus, disable the root admin menu item for that service
                                if (menuItem.id.indexOf('omc_root_admin_grp_') > -1) {
                                    if (!menuItem.children || menuItem.children.length === 0) {
                                        menuItem.disabled = true;
                                        return menuItem;
                                    }
                                    appId = menuItem.id.substring(19);
                                }
                                else {
                                    appId = menuItem.id.substring(9);
                                }
                            }
                            
                            //Disable menu item if service is not subscribed
                            if (!isAppSubscribed(appId)){
                                menuItem.disabled = true;
                                if (menuItem.children) {
                                    delete menuItem.children;
                                }
                                return menuItem;
                            }
                        }
                        
                        //Privilege check and disable/hide according to disableMode specified in JSON meta-data
                        if (menuItem && menuItem.id && menuItem.requiredPrivileges) {
                            if (!checkPrivilege(menuItem.requiredPrivileges)) {
                                if (menuItem.id.indexOf('omc_root') < 0 && menuItem.disableMode === "hidden"){
                                    return false;
                                }
                                //By default, disableMode will be disabled
                                else {
                                    menuItem.disabled = true;
                                    //if parent menu is disabled,remove sub menus
                                    if (menuItem.children) {
                                        delete menuItem.children;
                                    }
                                    return menuItem;
                                }
                            }
                        }
                        //Check child menu items
                        if (menuItem.children) {
                            for (var _idx = 0; _idx < menuItem.children.length; ++_idx) {
                                var childItem = menuItem.children[_idx];
                                if (!checkPrivilege(childItem.requiredPrivileges)) {
                                    if (childItem.disableMode === "hidden") {
                                        menuItem.children.splice(_idx, 1);
                                    }
                                    else {
                                        menuItem.children[_idx].disabled = true;
                                    }
                                }
                            }
                            if (menuItem.children.length === 0) {
                                delete menuItem.children;
                            }
                        }
                        return menuItem;
                    }
//                    else {
//                        var menuItemList = [];
//                        for(_idx = 0; _idx < rawMenuObj.length; ++_idx){
//                            var menuItem = $.extend(true,{},rawMenuObj[_idx]);
//                            if(checkPrivilege(menuItem.children[_idx].id, menuItem.children[_idx].requiredPrivileges)){
//                                menuItemList.push(menuItem);
//                            }else if(menuItem.children[_idx].disableMode && menuItem.children[_idx].disableMode === "disabled"){
//                                menuItem.disabled = true;
//                                menuItemList.push(menuItem);
//                            }
//                        }
//                        return menuItemList;
//                    }
                }
                
                //Get NLSed string for menu item label and tooltips
                function applyNlsOnMenu(rawMenuObj, nlsObj){
                    var _idx;
                    if(rawMenuObj && !Array.isArray(rawMenuObj)){
                        var menuItem = $.extend(true,{},rawMenuObj);
                        if (menuItem.id === omcMenuSeparatorId) {
                            menuItem.type = 'divider';
                            return menuItem;
                        }
                        else {
                            menuItem.type = 'menu_item';
                        }
                        if(menuItem && menuItem.labelKey){
                            var _labelKey = menuItem.labelKey;
                            menuItem.labelKey = (nlsObj&&nlsObj[_labelKey])?nlsObj[_labelKey]:_labelKey;
                        }
                        if(menuItem && menuItem.tooltipKey){
                            var _tooltipKey = menuItem.tooltipKey;
                            menuItem.tooltipKey = (nlsObj&&nlsObj[_tooltipKey])?nlsObj[_tooltipKey]:_tooltipKey;
                        }
                        if(menuItem.children){
                            for(_idx = 0; _idx < menuItem.children.length; ++_idx){
                                menuItem.children[_idx] = applyNlsOnMenu(menuItem.children[_idx],nlsObj);
                            }
                        }
                        return menuItem;
                    } else if (rawMenuObj) {
                        var menuItemList = [];
                        for(_idx = 0; _idx < rawMenuObj.length; ++_idx){
                            var menuItem = $.extend(true,{},rawMenuObj[_idx]);
                            menuItemList.push(applyNlsOnMenu(menuItem,nlsObj));
                        }
                        return menuItemList;
                    }
                }
                
                function findAppItemIndex(items, id) {
                    if (id && items && items.length > 0) {
                        for (var i = 0; i < items.length; i++) {
                            if (id === items[i].id) {
                                return i;
                                break;
                            }
                        }
                    }
                    return -1;
                }
                
//                function findMenuTreeItemIndex(items, id) {
//                    if (id && items && items.length > 0) {
//                        for (var i = 0; i < items.length; i++) {
//                            if (id === items[i].attr['id']) {
//                                return i;
//                                break;
//                            }
//                        }
//                    }
//                    return -1;
//                }
                
                //Construct a valid menu item structure as data source for ojNavigationList
                function getMenuItem(item) {
                    if (item) {
                        if (item.type && item.type !== 'divider') {
                            item = filterAuthorizedMenuItem(item);
                            //If vanity URL is required, construct the external URL to an vanity URL
                            if (item && item.externalUrl && item.serviceNameForVanityUrl) {
                                if (self.baseVanityUrls) {
                                    item.externalUrl = menuUtil.generateVanityUrl(item.externalUrl, self.baseVanityUrls[item.serviceNameForVanityUrl]);
                                }
                            }
                        }
                        var menuItem = {'attr': {'id': item.id, 'type': item.type, 'label': item.labelKey, 'externalUrl': item.externalUrl, 
                                'disabled': item.disabled, 'selfHandleMenuSelection': item.selfHandleMenuSelection, 
                                'tooltip': item.tooltipKey, 'serviceNameForVanityUrl': item.serviceNameForVanityUrl}};
                        if (item && item.children && item.children.length > 0) {
                            menuItem.children = [];
                            for (var i = 0; i < item.children.length; i++) {
                                var itemWithChildren = getMenuItem(item.children[i]);
                                if (itemWithChildren) {
                                    menuItem.children.push(itemWithChildren);
                                }
                            }
                        }
                        return menuItem;
                    }
                    return null;
                }

                function findItem(item, menuId) {
                    if (item && item.id === menuId) {
                        return item;
                    }   
                    else if (item && item.children) {
                        var foundItem = null;
                        for (var i = 0; i < item.children.length; i++) {
                            foundItem = findItem(item.children[i], menuId);
                            if (foundItem) return foundItem;
                        }
                        return foundItem;
                    }
                }
                
                function findItemTrack(item, menuId) {
                    var itemStack = [];
                    function _findItem(_item){
                        if (_item && _item.id === menuId) {
                            itemStack.push(_item);
                            return true;
                        }
                        else if (_item && _item.children) {
                            for (var i = 0; i < _item.children.length; i++) {
                                itemStack.push(_item);
                                var foundItem = _findItem(_item.children[i], menuId);
                                if (foundItem){
                                    break;
                                }else{
                                    itemStack.pop();
                                }
                            }
                        }
                    }
                    _findItem(item, menuId);
                    return itemStack;
                }
                
                //Menu selection handler when a menu is clicked
                self.selectionHandler = function(data, event) {
                    self.selectedItem(data.id);
                    if (event.type === 'click' && (data.id.indexOf('omc_root_') !== -1 || data.selfHandleMenuSelection === 'false')) {
                        handleMenuSelection(true, data);
                    }
                    else {
                        handleMenuSelection(false, data);
                    }
                };
                
                //Event handler before a menu item is collapsed
                self.beforeCollapse = function(event, ui) {
                    if (ui.key === rootCompositeMenuid) {
//                        currentCompositeParentId && self.selectedItem(currentCompositeParentId);
                        clearCompositeMenuItems();
                        self.expanded([]);
                        self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                        $("#omcMenuNavList").ojNavigationList("refresh");
                    }
                    return true;
                    
                };
                
                //Check user preferences to determine whether a dashboard has been set as Home
                if (!isSetAsHomeChecked) {
                    checkDashboardAsHomeSettings();
                }
                
                function handleMenuSelection(uifwkControlled, data) {
                    var item = null;
                    for (var j = 0; j < self.serviceMenuData.length; j++) {
                        var found = findItem(self.serviceMenuData[j], data.id);
                        if (found) {
                            item = found;
                            break;
                        }
                    }
                    if (item && !item.children) {
                        if (uifwkControlled) {
                            var linkHref = item.externalUrl; //globalMenuIdHrefMapping[data.id];
                            if (linkHref && linkHref !== '#') {
                                window.location.href = ctxUtil.appendOMCContext(linkHref, true, true, true);
                            }
                        }
                        else {
                            fireMenuSelectionEvent(data);
                        }
                    }
                }
                
                //Build mapping from menu item id and service links fetched from dashborad registration data
                function fetchGlobalMenuLinks(data) {
                    globalMenuIdHrefMapping = {};
                    globalMenuIdHrefMapping['omc_root_home'] = omcHomeUrl ? omcHomeUrl : '/emsaasui/emcpdfui/welcome.html';
                    globalMenuIdHrefMapping['omc_root_alerts'] = fetchLinkFromRegistrationData(data, 'homeLinks', 'EventUI');
                    globalMenuIdHrefMapping['omc_root_dashboards'] = '/emsaasui/emcpdfui/home.html';
                    globalMenuIdHrefMapping['omc_root_dataexplorer'] = fetchLinkFromRegistrationData(data, 'visualAnalyzers', 'TargetAnalytics');
                    globalMenuIdHrefMapping['omc_root_APM'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'ApmUI');
                    globalMenuIdHrefMapping['omc_root_Monitoring'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'MonitoringServiceUI');
                    globalMenuIdHrefMapping['omc_root_LogAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'LogAnalyticsUI');
                    globalMenuIdHrefMapping['omc_root_ITAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'emcitas-ui-apps');
                    globalMenuIdHrefMapping['omc_root_Orchestration'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'CosServiceUI');
                    globalMenuIdHrefMapping['omc_root_SecurityAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'SecurityAnalyticsUI');
                    globalMenuIdHrefMapping['omc_root_Compliance'] = fetchLinkFromRegistrationData(data, 'homeLinks', 'ComplianceUIService');
                    globalMenuIdHrefMapping['omc_root_admin_alertrules'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'EventUI');
                    globalMenuIdHrefMapping['omc_root_admin_agents'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'TenantManagementUI');
                    globalMenuIdHrefMapping['omc_root_admin_entitiesconfig'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'AdminConsoleSaaSUi');
                }
                
                function fetchLinkFromRegistrationData(data, linkType, serviceName) {
                    var links = data[linkType];
                    if (links && links.length > 0) {
                        for (var i = 0; i < links.length; i++) {
                            if (links[i].serviceName === serviceName) {
                                return links[i].href;
                            }
                        }
                    }
                    return null;
                }
                
                //fire menu selection event to integrators
                function fireMenuSelectionEvent(data) {
                    var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SELECTION'};
                    message.data = data;
                    window.postMessage(message, window.location.href);
                }
                
                function checkDashboardAsHomeSettings() {
                    function succCallback(data) {
                        var homeDashboardId = prefUtil.getPreferenceValue(data, prefKeyHomeDashboardId);
                        if (homeDashboardId) {
                            omcHomeUrl = "/emsaasui/emcpdfui/builder.html?dashboardId=" + homeDashboardId;
                        }
                        else {
                            omcHomeUrl = null;
                        }
                        isSetAsHomeChecked = true;
                    }
                    function errorCallback(jqXHR, textStatus, errorThrown) {
                        omcHomeUrl = null;
                    }
                    var options = {
                        success: succCallback,
                        error: errorCallback
                    };
                    prefUtil.getAllPreferences(options);
                }
                
                //Set current menu item
                function listenToSetCurrentMenuItem() {
                    var messageTag = 'EMAAS_OMC_GLOBAL_MENU_SET_CURRENT_ITEM';
                    function onSetCurrentMenuItem(event) {
                        if (event.origin !== window.location.protocol + '//' + window.location.host) {
                            return;
                        }
                        var eventData = event.data;
                        //Only handle received message for global menu selection
                        if (eventData && eventData.tag && eventData.tag === messageTag) {
                            if(eventData.menuItemId){
                                var itemTrack;
                                for (var j = 0; j < self.serviceMenuData.length; j++) {
                                    itemTrack = findItemTrack(self.serviceMenuData[j], eventData.menuItemId);
                                    if (itemTrack.length>0) {
                                        break;
                                    }else{
                                        itemTrack = null;
                                    }
                                }
                                if(itemTrack){
                                    var expandedIdList = $("#omcMenuNavList").ojNavigationList("getExpanded");
                                    var trackIdList = [];
                                    while(itemTrack.length>0){
                                        trackIdList.push(itemTrack.shift().id);
                                    }
                                    while(expandedIdList.length>0 && trackIdList.length > 0 && expandedIdList[0] === trackIdList[0]){
                                        expandedIdList.shift();
                                        trackIdList.shift();
                                    }
                                    $.each(expandedIdList,function(idx, expandedItemId){
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("collapse",expandedItemId, true);
                                    });
                                    setTimeout(function(){
                                    while(trackIdList.length>1){
                                        var parentItemId = trackIdList.shift();
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand",parentItemId, true);
                                    }
                                    $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "selection", eventData.menuItemId);
                                    $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "currentItem", eventData.menuItemId);
                                    },0);
                                }
                            }
                        }
                    }
                    window.addEventListener("message", onSetCurrentMenuItem, false);
                }
                
                listenToSetCurrentMenuItem();
            }
            return HamburgerMenuViewModel;
        });

