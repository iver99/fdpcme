define([
    'jquery', 
    'ojs/ojcore', 
    'knockout',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/preference-util-impl', 
    'uifwk/@version@/js/sdk/context-util-impl',
    'uifwk/@version@/js/sdk/menu-util-impl',
    'ojs/ojnavigationlist',
    'ojs/ojjsontreedatasource'],
        function ($, oj, ko, nls, dfumodel, pfumodel, ctxmodel, menumodel) {
            function HamburgerMenuViewModel(params) {
                var self = this;
                var dfu = new dfumodel();
                var ctxUtil = new ctxmodel();
                var prefUtil = new pfumodel(dfu.getPreferencesUrl(), dfu.getDashboardsRequestHeader());
                var prefKeyHomeDashboardId = "Dashboards.homeDashboardId";
                var isSetAsHomeChecked = false;
                var omcHomeUrl = null;
                var menuUtil = new menumodel();
                
                self.selectedItem = ko.observable();
                self.expanded = ko.observableArray([]);
                
                function clearCompositeMenuItems() {
                    var size = omcMenus.length;
                    if (omcMenus[size-1] && omcMenus[size-1].attr.id === 'omc_root_composite') {
                        omcMenus.pop();
                    }
                }
                
                function jumpToCompositeMenu(rootMenuLabel, menuJson) {
//                    var compositeMenus = [{'id': 'omc_composite_m1',type:'menu_item', 'labelKey': 'Composite Menu 1', 'externalUrl': '#'},
//                            {'id': 'omc_composite_m2',type:'menu_item', 'labelKey': 'Composite Menu 2', 'externalUrl': '#'},
//                            {'id': 'omc_composite_m3',type:'menu_item', 'labelKey': 'Composite Menu 3', 'externalUrl': '#'},
//                            {'id': 'omc_composite_m4',type:'divider', 'labelKey': '', 'externalUrl': '#'},
//                            {'id': 'omc_composite_m5',type:'menu_item', 'labelKey': 'Composite Menu 4', 'externalUrl': '#'}
//                            ];
                    clearCompositeMenuItems();
                    var rootCompositMenuItem = {'id': 'omc_root_composite', type: 'menu_item', 'labelKey': rootMenuLabel, 'externalUrl': '#', children: menuJson};
                    var compositeMenu = getMenuItem(rootCompositMenuItem);
                    omcMenus.push(compositeMenu);
                    self.expanded(['omc_root_composite']);
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    $("#omcMenuNavList").ojNavigationList("refresh");
                    
                    oj.OffcanvasUtils.toggle({
                                                "edge": "start",
                                                "displayMode": "push",
                                //                "content": "#main-container",
                                                "selector": "#omcHamburgerMenu"
                                            });
//                    self.selectedItem('omc_composite_m1');
//                    $("omcMenuNavList").ojNavigationList("expand", {'key': 'omc_root_composite', 'vetoable': true});                    
                }
                menuUtil.subscribeCompositeMenuDisplayEvent(jumpToCompositeMenu);
                
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
                
                var defaultMenuIds = ['omc_root_home','omc_root_alerts','omc_root_dashboards','omc_root_admin'];

                self.privilegeList = [];
                self.subscribedApps = [];
                function checkPrivilege(_id, requiredPrivilege){
                    var appId = _id;
//                    self.privilegeList = ['apm11','apm22'];//test#################################
                    if(!self.subscribedApps || self.subscribedApps.length<1){
                        self.subscribedApps = dfu.getSubscribedApplications();
                    }
                    if(!self.privilegeList || self.privilegeList.length<1){
                        var url = "sso.static/getUserGrants";
                        if (dfu.isDevMode()) {
                            url = "/authorization/ws/api/v1/priv/grants/getUserGrants";
                        }
                        var header = dfu.getDefaultHeader();
                        dfu.ajaxWithRetry(url, {
                                type: 'get',
                                dataType: 'json',
                                headers: header,
                                success: function (data) {
                                    self.privilegeList = data;
                                },
                                error: function (xhr, textStatus, errorThrown) {
                                    oj.Logger.error("Failed to get UserGrants due to error: " + textStatus);
                                },
                                async: false
                            });
                    }
                    if(!self.subscribedApps || !self.privilegeList){
                        oj.Logger.error("Failed to get subscribed applications or user granted priviledges in 'CheckPriviledge()'.");
                        return;
                    }
                    if(!appId){
                        oj.Logger.error("Wrong parameter '_id' in 'CheckPriviledge()'.");
                        return;
                    }
                    if(!requiredPrivilege){
                        return true;
                    }

                    if(_id.indexOf("omc_root_")>-1){
                        appId = _id.substring(9);
                        if(self.subscribedApps.indexOf(appId)<0){
                            return false;
                        }
                    }

                    if(Array.isArray(requiredPrivilege)){
                        for(var _idx = 0; _idx < requiredPrivilege.length; ++_idx){
                            if(self.privilegeList.indexOf(requiredPrivilege[_idx])<0){
                                return false;
                            }
                        }
                        return true;
                    }else{
                        if(self.privilegeList.indexOf(requiredPrivilege)<0){
                                return false;
                        }else{
                            return true;
                        }
                    }
                }
                
                function filterAuthorizedMenuItem(rawMenuObj){
                    var _idx;
                    if(!Array.isArray(rawMenuObj)){
                        var menuItem = rawMenuObj;
                        if(defaultMenuIds.indexOf(menuItem.id)>-1){
                            return menuItem;
                        }
                        
//                        if(menuItem.id === 'omc_root_APM') menuItem.requiredPrivileges = 'apm223';//test###########################################
                        if(menuItem && menuItem.id && menuItem.requiredPrivileges){
                            if(!checkPrivilege(menuItem.id, menuItem.requiredPrivileges)){
                                if(menuItem.id.indexOf('omc_root')<0 && !menuItem.disableMode || menuItem.disableMode === "hidden"){
                                    return false;
                                }else{
                                    menuItem.disabled = true;
                                    if(menuItem.children)delete menuItem.children;    //if parent menu is disabled,remove sub menus
                                    return menuItem;
                                }
                            }
                        }
                        if(menuItem.children){
                            for(_idx = 0; _idx < menuItem.children.length; ++_idx){
                                if(!checkPrivilege(menuItem.children[_idx].id, menuItem.children[_idx].requiredPrivileges)){
                                    if(!menuItem.children[_idx].disableMode || menuItem.children[_idx].disableMode === "hidden"){
                                        menuItem.children.remove(_idx);
                                    }else{
                                        menuItem.children[_idx].disabled = true;
                                    }
                                }
                            }
                            if(menuItem.children.length === 0){
                                delete menuItem.children;
                            }
                        }
                        return menuItem;
                    }else{
                        var menuItemList = [];
                        for(_idx = 0; _idx < rawMenuObj.length; ++_idx){
                            var menuItem = $.extend(true,{},rawMenuObj[_idx]);
                            if(checkPrivilege(menuItem.children[_idx].id, menuItem.children[_idx].requiredPrivileges)){
                                menuItemList.push(menuItem);
                            }else if(menuItem.children[_idx].disableMode && menuItem.children[_idx].disableMode === "disabled"){
                                menuItem.disabled = true;
                                menuItemList.push(menuItem);
                            }
                        }
                        return menuItemList;
                    }
                }

                self.menuDataRequestingNum = ko.observable(0);
                function getServiceData(serviceLinks) {
                    if(!self.allServiceData || self.allServiceData.length < 1){
                        self.allServiceData = [];
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
                            self.menuDataRequestingNum(self.menuDataRequestingNum()+1);
                            dfu.ajaxWithRetry(url, {
                                type: 'get',
                                dataType: 'json',
                                contentType: "application/json",
                                headers: header,
                                success: function (data) {
                                    serviceItem.serviceMenus = data.serviceMenus;
                                    serviceItem.serviceAdminMenus = data.serviceAdminMenus;
                                    url = data.serviceMenuMsgBundle;
                                    if(!url){
                                        self.menuDataRequestingNum(self.menuDataRequestingNum()-1);
                                        return;
                                    }
                                    if (dfu.isDevMode()) {
                                        var dfBaseUrl = dfu.getDevData().dfRestApiEndPoint;
                                        url = dfBaseUrl.substring(0, dfBaseUrl.indexOf('/emcpdf/')) + url;
                                    }
                                    else {
                                        url = url.substring(url.indexOf('/emsaasui/') + 1, url.length - 3);
                                    }
                                    
//                                    var re = new RegExp('emsaasui.*\.');
//                                    url = re.exec(url);
                                    
//                                    url = url.subString(0,url.length);
                                    require(['ojL10n!' + url], function (_nls) {
                                        serviceItem.serviceMenuMsgBundle = _nls;
                                        serviceItem.serviceMenus = applyNlsOnMenu(serviceItem.serviceMenus, _nls);
                                        serviceItem.serviceAdminMenus = applyNlsOnMenu(serviceItem.serviceAdminMenus, _nls);
                                        self.allServiceData.push(serviceItem);
                                        self.menuDataRequestingNum(self.menuDataRequestingNum()-1);
                                    });
                                },
                                error: function (xhr, textStatus, errorThrown) {
                                    self.menuDataRequestingNum(self.menuDataRequestingNum()-1);
                                    oj.Logger.error("Failed to get subscribed applicatoins due to error: " + textStatus);
                                },
                                async: false
                            });
                        });
                    }
                    return self.allServiceData;
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
                
                self.serviceMenuData = [];
                var omcMenus = [];
                function getMenuItem(item) {
                    if (item) {
                        item = filterAuthorizedMenuItem(item);
                        var menuItem = {'attr': {'id': item.id, 'type': item.type, 'labelKey': item.labelKey, 'externalUrl': item.externalUrl, 'disabled': item.disabled}};
                        if (item.children && item.children.length > 0) {
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
                
                self.menuDataRequestingNum.subscribe(function(num){
                    if(num!==0)return;
                    for (var k = 0; k < rootMenuData.length; ++k) {
                        self.serviceMenuData.push($.extend(true, {}, rootMenuData[k]));
                    }
                    self.allServiceData && $.each(self.allServiceData, function (idx, singleServiceData) {
                        var menuId = findAppItemIndex(rootMenuData, 'omc_root_' + singleServiceData.appId);
                        if (self.serviceMenuData[menuId]) {
                            self.serviceMenuData[menuId].children = singleServiceData.serviceMenus;
                            singleServiceData.serviceAdminMenus && self.serviceMenuData[menuId].children.push(singleServiceData.serviceAdminMenus);
                            var adminMenuId = findAppItemIndex(rootMenuData,'omc_root_admin');
                            var adminSubMenuId = findAppItemIndex(rootMenuData[adminMenuId].children,'omc_root_admin_grp_'+singleServiceData.appId);
                            if(adminSubMenuId > -1){
                                rootMenuData[adminMenuId].children[adminSubMenuId] = $.extend(true, {}, singleServiceData.serviceAdminMenus);
                                rootMenuData[adminMenuId].children[adminSubMenuId].id = 'omc_root_admin_grp_'+singleServiceData.appId;
                            }else{
                                if(!rootMenuData[adminMenuId].children)rootMenuData['omc_root_admin'].children = [];
                                rootMenuData[adminMenuId].children.push($.extend(true, {}, singleServiceData.serviceAdminMenus));
                                rootMenuData[adminMenuId].children[rootMenuData[adminMenuId].children.length - 1].id = 'omc_root_admin_grp_'+singleServiceData.appId;
                            }
                        }
                    });
                    for (var j = 0; j < self.serviceMenuData.length; j++) {
                        var item = self.serviceMenuData[j];
                        var menuItem = getMenuItem(item);
                        omcMenus.push(menuItem);
                    }
                    self.dataSource(new oj.JsonTreeDataSource(omcMenus));
                    menuUtil.fireServiceMenuLoadedEvent();
                });

                dfu.getRegistrations(function(data){
                    self.serviceLinks = data.serviceMenus;
                    getServiceData(self.serviceLinks);
                }, true);

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
                
                self.dataSource =  ko.observable(new oj.JsonTreeDataSource(omcMenus));
                self.selectionHandler = function(data, event) {
                    if (event.type === 'click' && data.id.indexOf('omc_root_') !== -1) {
                        handleMenuSelection(true, data);
                    }
                    else {
                        handleMenuSelection(false, data);
                    }
                };
                
                self.beforeCollapse = function(event, ui) {
                    if (ui.key === 'omc_root_composite') {
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
                var globalMenuIdHrefMapping = null;
                fetchGlobalMenuLinks();
                
                function handleMenuSelection(uifwkControlled, data) {
                    var item = null;
                    for (var j = 0; j < rootMenuData.length; j++) {
                        var found = findItem(rootMenuData[j], data.id);
                        if (found) {
                            item = found;
                            break;
                        }
                    }
                    if (item && !item.children) {
                        if (uifwkControlled) {
                            var linkHref = globalMenuIdHrefMapping[data.id];
                            if (linkHref) {
                                window.location.href = ctxUtil.appendOMCContext(linkHref, true, true, true);
                            }
                        }
                        else {
                            fireMenuSelectionEvent(data);
                        }
                    }
                }
                
                function fetchGlobalMenuLinks() {
                    globalMenuIdHrefMapping = {};
                    var successCallback = function(data) {
                        globalMenuIdHrefMapping['omc_root_home'] = omcHomeUrl ? omcHomeUrl : '/emsaasui/emcpdfui/welcome.html';
                        globalMenuIdHrefMapping['omc_root_alerts'] = fetchLinkFromRegistrationData(data, 'homeLinks', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_dashboards'] = '/emsaasui/emcpdfui/home.html';
                        globalMenuIdHrefMapping['omc_root_dataexplorer'] = fetchLinkFromRegistrationData(data, 'visualAnalyzers', 'LogAnalyticsUI');
                        globalMenuIdHrefMapping['omc_root_APM'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'ApmUI');
                        globalMenuIdHrefMapping['omc_root_Monitoring'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_LogAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_ITAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'emcitas-ui-apps');
                        globalMenuIdHrefMapping['omc_root_Orchestration'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'CosServiceUI');
                        globalMenuIdHrefMapping['omc_root_SecurityAnalytics'] = fetchLinkFromRegistrationData(data, 'cloudServices', 'SecurityAnalyticsUI');
                        globalMenuIdHrefMapping['omc_root_Compliance'] = fetchLinkFromRegistrationData(data, 'homeLinks', 'ComplianceUIService');
                        globalMenuIdHrefMapping['omc_root_admin_alertrules'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'EventUI');
                        globalMenuIdHrefMapping['omc_root_admin_agents'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'TenantManagementUI');
                        globalMenuIdHrefMapping['omc_root_admin_entitiesconfig'] = fetchLinkFromRegistrationData(data, 'adminLinks', 'AdminConsoleSaaSUi');
                    };
                    dfu.getRegistrations(successCallback, true, null);
                }
                
                function fetchLinkFromRegistrationData(data, linkType, serviceName) {
                    var links = data[linkType];
                    if (links && links.length > 0) {
                        for (var i = 0; i < links.length; i++) {
                            if (links[i].serviceName === serviceName) {
                                return links[i].externalUrl;
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
                                for (var j = 0; j < rootMenuData.length; j++) {
                                    itemTrack = findItemTrack(rootMenuData[j], eventData.menuItemId);
                                    if (itemTrack.length>0) {
                                        break;
                                    }else{
                                        itemTrack = null;
                                    }
                                }
                                if(itemTrack){
                                    $.each(rootMenuData,function(idx, listItem){
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

