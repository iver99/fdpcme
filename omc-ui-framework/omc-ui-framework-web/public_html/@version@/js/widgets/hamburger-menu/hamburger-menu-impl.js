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
//                    var compositeMenus = [{'id': 'omc_composite_m1',type:'menu', 'name': 'Composite Menu 1', 'href': '#'},
//                            {'id': 'omc_composite_m2',type:'menu', 'name': 'Composite Menu 2', 'href': '#'},
//                            {'id': 'omc_composite_m3',type:'menu', 'name': 'Composite Menu 3', 'href': '#'},
//                            {'id': 'omc_composite_m4',type:'divider', 'name': '', 'href': '#'},
//                            {'id': 'omc_composite_m5',type:'menu', 'name': 'Composite Menu 4', 'href': '#'}
//                            ];
                    clearCompositeMenuItems();
                    var rootCompositMenuItem = {'id': 'omc_root_composite', type: 'menu', 'name': rootMenuLabel, 'href': '#', children: menuJson};
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
                    {'id': 'omc_root_home', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_HOME_LABEL, 'href': '#'},
                    {'id': 'omc_root_alerts', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ALERTS_LABEL, 'href': '#'},
//                    {'id': 'omc_root_applications', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APPS_LABEL, 'href': '#'},
                    {'id': 'omc_root_dashboards', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DASHBOARDS_LABEL, 'href': '#'},
//                    {'id': 'omc_root_savedsearches', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SAVEDSEARCH_LABEL, 'href': '#'},
                    {'id': 'omc_root_dataexplorer', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DATAEXPLORER_LABEL, 'href': '#'},
                    {'id': 'omc_root_divider', type: 'divider', 'name': '', 'href': '#'},
                    {'id': 'omc_root_APM', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APM_LABEL, 'href': '#'},
                    {'id': 'omc_root_Monitoring', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_MONITORING_LABEL, 'href': '#'},
                    {'id': 'omc_root_LogAnalytics', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LA_LABEL, 'href': '#'},
                    {'id': 'omc_root_ITAnalytics', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ITA_LABEL, 'href': '#'},
                    {'id': 'omc_root_Orchestration', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ORCHESTRATION_LABEL, 'href': '#'},
                    {'id': 'omc_root_SecurityAnalytics', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SECURITY_LABEL, 'href': '#'},
                    {'id': 'omc_root_Compliance', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_COMPLIANCE_LABEL, 'href': '#'},
                    {'id': 'omc_root_divider1', type: 'divider', 'name': '', 'href': '#'},
                    {'id': 'omc_root_admin', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ADMIN_LABEL, 'href': '#', children: [
                            {'id': 'omc_root_admin_alertrules', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ALERTRULES_LABEL, 'href': '#'},
                            {'id': 'omc_root_admin_agents', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_AGENTS_LABEL, 'href': '#'},
                            {'id': 'omc_root_admin_entitiesconfig', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ENTITIESCONFIG_LABEL, 'href': '#'},
                            {'id': 'omc_root_admin_divider', type: 'divider', 'name': '', 'href': '#'},
                            {'id': 'omc_root_admin_grp_APM', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_APM_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_root_admin_grp_Monitoring', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_MONITORING_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_root_admin_grp_LogAnalytics', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_LOG_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_root_admin_grp_SecurityAnalytics', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_SECURITY_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_root_admin_grp_Compliance', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_COMPLIANCE_ADMIN_LABEL, 'href': '#'}
                    ]}
                ];
                
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
                
                var serviceMenuItems = params.serviceMenus;
                var appId = params.appId;
                var omcMenus = [];
                if (serviceMenuItems && serviceMenuItems.length > 0) {
                    var index = findAppItemIndex(rootMenuData, 'omc_root_' + appId);
                    if (index >= 0) {
                        rootMenuData[index].children = [];
                        for (var i = 0; i < serviceMenuItems.length; i++) {
                            var item = serviceMenuItems[i];
                            rootMenuData[index].children.push(item);
                        }
                    }
                }
                
                var omcAdminIndex = findAppItemIndex(rootMenuData, 'omc_root_admin');
                if (omcAdminIndex >= 0) {
                    var rootAdminChildren = rootMenuData[omcAdminIndex].children;
                    var appAdminIndex = findAppItemIndex(rootAdminChildren, 'omc_admin_grp_' + appId);
                    if (appAdminIndex >= 0) {
                        var serviceAdminMenuItems = getServiceAdminMenus(serviceMenuItems);
                        rootMenuData[omcAdminIndex].children[appAdminIndex].children = [];
                        for (var i = 0; i < serviceAdminMenuItems.length; i++) {
                            rootMenuData[omcAdminIndex].children[appAdminIndex].children.push(serviceAdminMenuItems[i]);
                        }
                    }
                }
                
                function getMenuItem(item) {
                    if (item) {
                        var menuItem = {'attr': {'id': item.id, 'type': item.type, 'name': item.name, 'href': item.href}};
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
                
                for (var j = 0; j < rootMenuData.length; j++) {
                    var item = rootMenuData[j];
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

