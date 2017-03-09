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
                
                function generateDividerItem(prefix) {
                    var dividerId = prefix + '_' + [].toString.apply(window.crypto&&window.crypto.getRandomValues(new Uint32Array(1))||window.msCrypto&&window.msCrypto.getRandomValues(new Uint32Array(1)))
                    return {
                        'id': dividerId, type: 'divider', 'labelKey': '', 'externalUrl': '#'
                    };
                };
                
                function getServiceAdminMenus(serviceMenus) {
                    var adminMenus = [];
                    if (serviceMenus && serviceMenus.length > 0) {
                        for (var i = 0; i < serviceMenus.length; i++) {
                            var menu = serviceMenus[i];
                            if (menu.type === 'admin_menu_group' && menu.children) {
                                adminMenus = menu.children;
                            }
                        }
                    }
                    return adminMenus;
                }
                
                function getServiceLinks(){
                    var serviceName = "??";
                    var version = "1.0";
                    var rel = "registration?";
                    var header = dfu.getDefaultHeader();
                    var result = null;
                    var url = dfu.discoverUrl(serviceName,version,rel);
                    dfu.ajaxWithRetry(url, {
                        type: 'get',
                        dataType: 'json',
                        headers: header,
                        success: function (data) {
                            result = data.serviceMenus;
                        },
                        error: function (xhr, textStatus, errorThrown) {
                            oj.Logger.error("Failed to get subscribed applicatoins due to error: " + textStatus);
                        },
                        async: false
                    });
                    return result;
                }

                function getServiceData(serviceLinks) {
                    self.allServiceData = [{
	"appId":	"APM",
	"serviceName":	"APMName",
	"version":	"1.0",
    "serviceMenus":    [{'id': 'apm_home',type:'menu_item', 'labelKey': 'Home', 'externalUrl': '#'},
                                    {'id': 'apm_Alerts',type:'menu_item', 'labelKey': 'Alerts', 'externalUrl': '#', 'selfHandleMenuSelection': 'false'},
                                    {'id': 'apm_app',type:'menu_item', 'labelKey': 'Applications', 'externalUrl': '#'},
                                    {'id': 'apm_divider',type:'divider', 'labelKey': '', 'externalUrl': '#'},
                                    {'id': 'apm_pages',type:'menu_item', 'labelKey': 'Pages', 'externalUrl': '#'},
                                    {'id': 'apm_ajaxcalls',type:'menu_item', 'labelKey': 'Ajax Calls', 'externalUrl': '#'},
                                    {'id': 'apm_sessions',type:'menu_item', 'labelKey': 'Sessions', 'externalUrl': '#'},
                                    {'id': 'apm_synthetictests',type:'menu_item', 'labelKey': 'Synthetic Tests', 'externalUrl': '#'},
                                    {'id': 'apm_divider1',type:'divider', 'labelKey': '', 'externalUrl': '#'},
                                    {'id': 'apm_mobileclient',type:'menu_item', 'labelKey': 'Mobile Client', 'externalUrl': '#'},
                                    {'id': 'apm_viewcontrollers',type:'menu_item', 'labelKey': 'View Controllers/Activities', 'externalUrl': '#'},
                                    {'id': 'apm_mobilehttp',type:'menu_item', 'labelKey': '(Mobile)HTTP Requests', 'externalUrl': '#'},
                                    {'id': 'apm_divider2',type:'divider', 'labelKey': '', 'externalUrl': '#'},
                                    {'id': 'apm_serverrequests',type:'menu_item', 'labelKey': 'Server Requests', 'externalUrl': '#'},
                                    {'id': 'apm_threadprofiler',type:'menu_item', 'labelKey': 'Thread Profilers', 'externalUrl': '#'},
                                    {'id': 'apm_appservers',type:'menu_item', 'labelKey': 'App Servers', 'externalUrl': '#'}],
	"serviceAdminMenus": 	{'id': 'apm_admin',type:'admin_menu_group', 'labelKey': 'APM Admin', 'externalUrl': '#', children: 
                                        [{'id': 'apm_admin_alertrules',type:'menu_item', 'labelKey': 'Alert Rules', 'externalUrl': '#'},
                                        {'id': 'apm_admin_appdef',type:'menu_item', 'labelKey': 'BRANDING_BAR_HAMBURGER_MENU_ADMIN_ALERTRULES_LABEL', 'externalUrl': '#'},
                                        {'id': 'apm_admin_synthetictests',type:'menu_item', 'labelKey': 'Synthetic Tests', 'externalUrl': '#'},
                                        {'id': 'apm_admin_location',type:'menu_item', 'labelKey': 'Beacon Locations', 'externalUrl': '#'},
                                        {'id': 'apm_admin_metricsettings',type:'menu_item', 'labelKey': 'Metric Settings', 'externalUrl': '#'},
                                        {'id': 'apm_admin_browseragents',type:'menu_item', 'labelKey': 'Browser Agents', 'externalUrl': '#'},
                                        {'id': 'apm_admin_mobileclientregistry',type:'menu_item', 'labelKey': 'Mobile Client Registrations', 'externalUrl': '#'}]},
	"serviceMenuMsgBundle": nls
},
{
	"appId":	"LogAnalytics",
	"serviceName":	"LogAnalyticsName",
	"version":	"1.0",
	"serviceMenus":
                                    [{'id': 'la_home', 'labelKey': 'Log Explorer', 'externalUrl': '#'},
                                    {'id': 'la_Alerts', 'labelKey': 'Alerts', 'externalUrl': '#'}],
	"serviceMenuMsgBundle": nls
}];
                    if(!self.allServiceData || self.allServiceData.length < 1){
                        self.allServiceData = {};
                        $.each(serviceLinks, function(idx, linkItem){
                            var serviceItem = {};
                            serviceItem.appId = linkItem.appId;
                            serviceItem.serviceName = linkItem.serviceName;
                            serviceItem.version = linkItem.version;
                            var header = dfu.getDefaultHeader();

                            var url = linkItem.metaDataHref;
                            dfu.ajaxWithRetry(url, {
                                type: 'get',
                                dataType: 'json',
                                headers: header,
                                success: function (data) {
                                    serviceItem.serviceMenus = data.serviceMenus;
                                    serviceItem.serviceAdminMenus = data.serviceAdminMenus;
                                    url = data.msgBundleHref;
                                    dfu.ajaxWithRetry(url, {
                                        type: 'get',
                                        dataType: 'json',
                                        headers: header,
                                        success: function (data) {
                                            serviceItem.serviceMenuMsgBundle = data;
                                        },
                                        error: function (xhr, textStatus, errorThrown) {
                                            oj.Logger.error("Failed to get subscribed applicatoins due to error: " + textStatus);
                                        },
                                        async: false
                                    });
                                    self.allServiceData.push(serviceItem);
                                        },
                                        error: function (xhr, textStatus, errorThrown) {
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
                    if(!Array.isArray(rawMenuObj)){
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
                    }else{
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
                
//                self.serviceLinks = getServiceLinks();
//                self.serviceLinks && getServiceData(self.serviceLinks);
                getServiceData(self.serviceLinks);
                self.serviceMenuData = [];
                for(var k = 0; k < rootMenuData.length; ++k){
                    self.serviceMenuData.push($.extend(true,{},rootMenuData[k]));
                }
                self.allServiceData && $.each(self.allServiceData, function(idx, singleServiceData){
                    var menuId = findAppItemIndex(rootMenuData, 'omc_root_'+singleServiceData.appId);
                    if(self.serviceMenuData[menuId]){
                        var newServiceMenuItem = applyNlsOnMenu(singleServiceData.serviceMenus, singleServiceData.serviceMenuMsgBundle);
                        self.serviceMenuData[menuId].children = newServiceMenuItem;
                        singleServiceData.serviceAdminMenus && self.serviceMenuData[menuId].children.push(applyNlsOnMenu(singleServiceData.serviceAdminMenus, singleServiceData.serviceMenuMsgBundle));
                    }
                });


                var omcMenus = [];
                function getMenuItem(item) {
                    if (item) {
                        var menuItem = {'attr': {'id': item.id, 'type': item.type, 'labelKey': item.labelKey, 'externalUrl': item.externalUrl}};
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
                
                for (var j = 0; j < self.serviceMenuData.length; j++) {
                    var item = self.serviceMenuData[j];
                    var menuItem = getMenuItem(item);
                    omcMenus.push(menuItem);
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

