define([
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
                
                function clearCompositeMenuItems() {
                    var size = omcMenus.length;
                    if (omcMenus[size-1] && omcMenus[size-1].attr.id === rootCompositeMenuid) {
                        omcMenus.pop();
                    }
                    currentCompositeParentId = null;
                }
                
                var currentCompositeParentId = null;
                function jumpToCompositeMenu(parentMenuId, rootMenuLabel, menuJson) {
                    clearCompositeMenuItems();
                    currentCompositeParentId = parentMenuId;
                    var rootCompositMenuItem = {'id': rootCompositeMenuid, type: 'menu_item', 'labelKey': rootMenuLabel, 'externalUrl': '#', children: menuJson};
                    var compositeMenu = getMenuItem(rootCompositMenuItem);
//                    var parentMenuIndex = findTreeItemIndex(omcMenus, parentMenuId);
//                    if (parentMenuIndex > -1) {
//                        if (!omcMenus[parentMenuIndex].children) {
//                            omcMenus[parentMenuIndex].children = [];
//                        }
//                        omcMenus[parentMenuIndex].children.push(compositeMenu);
//                    }
                    
                    omcMenus.push(compositeMenu);
                    self.expanded([rootCompositeMenuid]);
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    $("#omcMenuNavList").ojNavigationList("refresh");
                    
//                    oj.OffcanvasUtils.toggle({
//                                                "edge": "start",
//                                                "displayMode": "push",
//                                //                "content": "#main-container",
//                                                "selector": "#omcHamburgerMenu"
//                                            });
//                    self.selectedItem('omc_composite_m1');
//                    $("omcMenuNavList").ojNavigationList("expand", {'key': rootCompositeMenuid, 'vetoable': true});                    
                }
                menuUtil.subscribeCompositeMenuDisplayEvent(jumpToCompositeMenu);
                
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
//                    //testing
//                    {'id': 'omc_root_test', type: 'menu_item', 'labelKey': 'Test', 'externalUrl': '#'},
//                    //testing
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
//                    //testing
//                    ,'omc_root_test'
                ];

                self.privilegeList = [];
                self.subscribedApps = [];
                self.serviceMenuData = [];
                self.dataSource = ko.observable();
                var omcMenus = [];
                var globalMenuIdHrefMapping = null;
                
                function loadServiceMenus() {
                    var dfdLoadServiceMenus = $.Deferred();
                        dfu.getRegistrations(function(data){
                            fetchGlobalMenuLinks(data);
                            
                            self.serviceLinks = data.serviceMenus;
//                            //testing code start
//                            self.serviceLinks = [
////                                {
////                                    'appId': 'APM',
////                                    'serviceName': 'ApmUI',
////                                    'version': '1.0',
////                                    'metaDataHref': 'http://localhost:8383/emsaasui/uifwk/@version@/data/serviceMenus.json'
////                                },
////                                {
////                                    'appId': 'ITAnalytics',
////                                    'serviceName': 'emcitas-ui-apps',
////                                    'version': '1.0',
////                                    'metaDataHref': 'http://localhost:8383/emsaasui/uifwk/@version@/data/serviceMenus1.json'
////                                },
////                                {
////                                    'appId': 'LogAnalytics',
////                                    'serviceName': 'LogAnalyticsUI',
////                                    'version': '1.0',
////                                    'metaDataHref': 'http://localhost:8383/emsaasui/uifwk/@version@/data/serviceMenus2.json'
////                                }
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
                
                function loadServiceData(dfd, serviceLinks) {
                    if(!self.allServiceData || self.allServiceData.length < 1){
                        self.allServiceData = [];
                        self.loadedServiceCnt = ko.observable(0);
                        self.loadedServiceCnt.subscribe(function(cnt) {
                            if (cnt === serviceLinks.length) {
                                dfd.resolve();
                            }
                        });
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
                
                function generateDividerItem(prefix) {
                    var dividerId = prefix + '_' + [].toString.apply(window.crypto&&window.crypto.getRandomValues(new Uint32Array(1))||window.msCrypto&&window.msCrypto.getRandomValues(new Uint32Array(1)));
                    return {'id': dividerId, type: 'divider', 'labelKey': '', 'externalUrl': '#'};
                };
                
                var cachedMenus = sessionCaches[0].retrieveDataFromCache(sessionCacheAllMenusKey);
                if (cachedMenus && cachedMenus[sessionCacheOmcMenusDataKey]) {
                    omcMenus = cachedMenus[sessionCacheOmcMenusDataKey];
                    self.serviceMenuData = cachedMenus[sessionCacheServiceMenuDataKey];
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    menuUtil.fireServiceMenuLoadedEvent();
                    var selectedMenuId = getOmcMenuUrlParam();
                    if (selectedMenuId) {
                        menuUtil.setCurrentMenuItem(selectedMenuId);
                    }
                }
                else {
                    $.when(loadServiceMenus(), getUserGrants(), getSubscribedApps()).done(function() {
                        for (var k = 0; k < rootMenuData.length; ++k) {
                            rootMenuData[k].externalUrl = globalMenuIdHrefMapping[rootMenuData[k].id] ? globalMenuIdHrefMapping[rootMenuData[k].id] : '#';
                            self.serviceMenuData.push($.extend(true, {}, rootMenuData[k]));
                        }
                        self.allServiceData && $.each(self.allServiceData, function (idx, singleServiceData) {
                            var menuId = findAppItemIndex(rootMenuData, 'omc_root_' + singleServiceData.appId);
                            if (self.serviceMenuData[menuId]) {
                                if (singleServiceData.serviceMenus) {
                                    self.serviceMenuData[menuId].children = singleServiceData.serviceMenus;
                                }
                                if (singleServiceData.serviceAdminMenus && singleServiceData.serviceAdminMenus.children) {
                                    if (singleServiceData.serviceMenus && singleServiceData.serviceMenus.length > 0) {
                                        var lastServiceMenuItem = singleServiceData.serviceMenus[singleServiceData.serviceMenus.length - 1];
                                        if (lastServiceMenuItem.type !== 'divider') {
                                            var dividerItem = generateDividerItem('omc_' + singleServiceData.appId);
                                            self.serviceMenuData[menuId].children.push(dividerItem);
                                        }
                                    }
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
                        for (var j = 0; j < self.serviceMenuData.length; j++) {
                            var item = self.serviceMenuData[j];
                            var menuItem = getMenuItem(item);
                            omcMenus.push(menuItem);
                        }
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheOmcMenusDataKey, omcMenus);
                        sessionCaches[0].updateCacheData(sessionCacheAllMenusKey, sessionCacheServiceMenuDataKey, self.serviceMenuData);
                        self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                        menuUtil.fireServiceMenuLoadedEvent();
                    });
                }
                
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
                
                function filterAuthorizedMenuItem(rawMenuObj){
                    if (!Array.isArray(rawMenuObj)) {
                        var menuItem = rawMenuObj;
                        if (defaultMenuIds.indexOf(menuItem.id) > -1) {
                            return menuItem;
                        }
                        
                        //Subscription check
                        if (menuItem.id.indexOf("omc_root_") > -1) {
                            var appId = null;
                            if (menuItem.id === 'omc_root_dataexplorer') {
                                appId = 'ITAnalytics';
                            }
                            else {
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
                            
                            if (!isAppSubscribed(appId)){
                                menuItem.disabled = true;
                                if (menuItem.children) {
                                    delete menuItem.children;
                                }
                                return menuItem;
                            }
                        }
                        
                        if (menuItem && menuItem.id && menuItem.requiredPrivileges) {
                            if (!checkPrivilege(menuItem.requiredPrivileges)) {
                                if (menuItem.id.indexOf('omc_root') < 0 && menuItem.disableMode === "hidden"){
                                    return false;
                                }
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

                function applyNlsOnMenu(rawMenuObj, nlsObj){
                    var _idx;
                    if(rawMenuObj && !Array.isArray(rawMenuObj)){
                        var menuItem = $.extend(true,{},rawMenuObj);
                        if(menuItem && menuItem.labelKey){
                            var _labelKey = menuItem.labelKey;
                            menuItem.labelKey = (nlsObj&&nlsObj[_labelKey])?nlsObj[_labelKey]:_labelKey;
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

                function setOmcMenuUrlParam(url, paramValue) {
                    var paramName = "omcMenu";
                    paramValue = encodeURI(paramValue);
                    if (paramValue === null) {
                        paramValue = '';
                    }
                    //Handle the case anchor section ('#') exists in the given URL 
                    var anchorIdx = url.indexOf('#');
                    var hash = '';
                    //Retrieve hash string from the URL and append to the end of the URL after appending context string
                    if (anchorIdx !== -1) {
                        hash = url.substring(anchorIdx);
                        url = url.substring(0, anchorIdx);
                    }
                    var pattern = new RegExp('([?&])' + paramName + '=.*?(&|$|#)(.*)', 'i');
                    if (url.match(pattern)) {
                        //If parameter value is not empty, update URL parameter
                        if (paramValue) {
                            return url.replace(pattern, '$1' + paramName + "=" + paramValue + '$2$3') + hash;
                        }
                        //Otherwise, remove the parameter from URL
                        else {
                            return url.replace(pattern, '$1$3').replace(/(&|\?)$/, '') + hash;
                        }
                    }

                    //If value is not empty, append it to the URL
                    if (paramValue) {
                        return url + (url.indexOf('?') > 0 ?
                                //Handle case that an URL ending with a question mark only
                                        (url.lastIndexOf('?') === url.length - 1 ? '' : '&') : '?') + paramName + '=' + paramValue + hash;
                    }
                    //If value is empty, return original URL
                    return url;
                }

                function getOmcMenuUrlParam(){
                    return decodeURI(dfu.getUrlParam("omcMenu"));
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
                
                function findMenuTreeItemIndex(items, id) {
                    if (id && items && items.length > 0) {
                        for (var i = 0; i < items.length; i++) {
                            if (id === items[i].attr['id']) {
                                return i;
                                break;
                            }
                        }
                    }
                    return -1;
                }
                
                
                function getMenuItem(item) {
                    if (item) {
                        if (item.type && item.type !== 'divider') {
                            item = filterAuthorizedMenuItem(item);
                        }
                        var menuItem = {'attr': {'id': item.id, 'type': item.type, 'labelKey': item.labelKey, 'externalUrl': item.externalUrl, 'disabled': item.disabled, 'selfHandleMenuSelection': item.selfHandleMenuSelection}};
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
                
                self.selectionHandler = function(data, event) {
                    self.selectedItem(data.id);
                    if (event.type === 'click' && (data.id.indexOf('omc_root_') !== -1 || data.selfHandleMenuSelection === false)) {
                        handleMenuSelection(true, data);
                    }
                    else {
                        handleMenuSelection(false, data);
                    }
                };
                
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
                                window.location.href = ctxUtil.appendOMCContext(setOmcMenuUrlParam(linkHref, data.id), true, true, true);
                            }
                        }
                        else {
                            fireMenuSelectionEvent(data);
                        }
                    }
                }
                
                function fetchGlobalMenuLinks(data) {
                    globalMenuIdHrefMapping = {};
//                    //testing
//                    globalMenuIdHrefMapping['omc_root_test'] = '/emsaasui/uifwk/test.html?appId=LogAnalytics';
//                    //testing
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
                                    $.each(self.serviceMenuData,function(idx, listItem){
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("collapse",listItem.id, true);
                                    });
                                    while(itemTrack.length>1){
                                        var parentItem = itemTrack.shift();
                                        $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("expand",parentItem.id, true);
                                    }
                                    $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "selection", eventData.menuItemId);
                                    $("#hamburgerMenu #navlistcontainer>div").ojNavigationList("option", "currentItem", eventData.menuItemId);
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

