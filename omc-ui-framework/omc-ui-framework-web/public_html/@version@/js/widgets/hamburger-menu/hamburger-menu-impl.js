define([
    'jquery', 
    'ojs/ojcore', 
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg',
    'ojs/ojnavigationlist',
    'ojs/ojjsontreedatasource'],
        function ($, oj, nls) {
            function HamburgerMenuViewModel(params) {
                var self = this;
                self.hamburgerRootMenuLabel = nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_LABEL;
                var rootMenuData = [
                    {'id': 'omc_root_home', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_HOME_LABEL, 'href': '#'},
                    {'id': 'omc_root_alerts', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_ALERTS_LABEL, 'href': '#'},
                    {'id': 'omc_root_applications', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_APPS_LABEL, 'href': '#'},
                    {'id': 'omc_root_dashboards', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_DASHBOARDS_LABEL, 'href': '#'},
                    {'id': 'omc_root_savedsearches', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ROOT_SAVEDSEARCH_LABEL, 'href': '#'},
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
                            {'id': 'omc_admin_alertrules', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ALERTRULES_LABEL, 'href': '#'},
                            {'id': 'omc_admin_agents', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_AGENTS_LABEL, 'href': '#'},
                            {'id': 'omc_admin_entitiesconfig', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_ADMIN_ENTITIESCONFIG_LABEL, 'href': '#'},
                            {'id': 'omc_admin_divider', type: 'divider', 'name': '', 'href': '#'},
                            {'id': 'omc_admin_grp_APM', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_APM_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_admin_grp_Monitoring', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_MONITORING_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_admin_grp_LogAnalytics', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_LOG_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_admin_grp_SecurityAnalytics', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_SECURITY_ADMIN_LABEL, 'href': '#'},
                            {'id': 'omc_admin_grp_Compliance', type: 'menu', 'name': nls.BRANDING_BAR_HAMBURGER_MENU_COMPLIANCE_ADMIN_LABEL, 'href': '#'}
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
                
                self.dataSource =  new oj.JsonTreeDataSource(omcMenus);
                self.selectionHandler = function(data, event) {
                    if (event.type === 'click' && data.id.indexOf('root_') !== 0) {
                        var item = null;
                        for (var j = 0; j < rootMenuData.length; j++) {
                            var found = findItem(rootMenuData[j], data.id);
                            if (found) {
                                item = found;
                                break;
                            }
                        }
                        if (item && !item.children) {
                            fireMenuSelectionEvent(data);
                        }

                    }
                };

                function fireMenuSelectionEvent(data) {
                    var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SELECTION'};
                    message.data = data;
                    window.postMessage(message, window.location.href);
                }
            }
            return HamburgerMenuViewModel;
        });

