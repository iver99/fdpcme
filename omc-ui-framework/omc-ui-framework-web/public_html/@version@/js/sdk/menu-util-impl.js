define('uifwk/@version@/js/sdk/menu-util-impl', [
    'ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/ajax-util-impl',
    'uifwk/@version@/js/util/df-util-impl',
    'uifwk/@version@/js/util/usertenant-util-impl',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg'
],
    function (oj, ko, $, ajaxUtilModel, dfuModel, userTenantModel, nls)
    {
        function UIFWKGlobalMenuUtil() {
            var self = this;
            var ajaxUtil = new ajaxUtilModel();
            var dfu = new dfuModel();
            var userTenantUtil = new userTenantModel();
            
            self.OMC_GLOBAL_MENU_HOME = 'omc_root_home';
            self.OMC_GLOBAL_MENU_ALERTS = 'omc_root_alerts';
            self.OMC_GLOBAL_MENU_DASHBOARDS = 'omc_root_dashboards';
            self.OMC_GLOBAL_MENU_DATAEXPLORER = 'omc_root_dataexplorer';
            self.OMC_GLOBAL_MENU_APM = 'omc_root_APM';
            self.OMC_GLOBAL_MENU_MONITORING = 'omc_root_Monitoring';
            self.OMC_GLOBAL_MENU_LOGANALYTICS = 'omc_root_LogAnalytics';
            self.OMC_GLOBAL_MENU_ITANALYTICS = 'omc_root_ITAnalytics';
            self.OMC_GLOBAL_MENU_ORCHESTRATION = 'omc_root_Orchestration';
            self.OMC_GLOBAL_MENU_SECURITY = 'omc_root_SecurityAnalytics';
            self.OMC_GLOBAL_MENU_COMPLIANCE = 'omc_root_Compliance';
            self.OMC_GLOBAL_MENU_ADMIN = 'omc_root_admin';
            self.OMC_GLOBAL_MENU_ADMIN_ALERTRULES = 'omc_root_admin_alertrules';
            self.OMC_GLOBAL_MENU_ADMIN_AGENTS = 'omc_root_admin_agents';
            self.OMC_GLOBAL_MENU_ADMIN_ENTITIESCONFIG = 'omc_root_admin_entitiesconfig';
            self.OMC_GLOBAL_MENU_ADMIN_GRP_APM = 'omc_root_admin_grp_APM';
            self.OMC_GLOBAL_MENU_ADMIN_GRP_MONITORING = 'omc_root_admin_grp_Monitoring';
            self.OMC_GLOBAL_MENU_ADMIN_GRP_LOGANALYTICS = 'omc_root_admin_grp_LogAnalytics';
            self.OMC_GLOBAL_MENU_ADMIN_GRP_SECURITY = 'omc_root_admin_grp_SecurityAnalytics';
            self.OMC_GLOBAL_MENU_ADMIN_GRP_COMPLIANCE = 'omc_root_admin_grp_Compliance';
            
            function fireCompositeMenuDisplayEvent(parentMenuId, objMenuName, menuJson) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_COMPOSITE_DISPLAY'};
                message.compositeParentMenuId = parentMenuId;
                message.compositeRootMenuLabel = objMenuName;
                message.compositeMenuJson = menuJson;
                window.postMessage(message, window.location.href);
            }
            
            self.fireServiceMenuLoadedEvent = function(){
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SERVICE_MENU_LOADED'};
                window.postMessage(message, window.location.href);
            };
            
            self.subscribeServiceMenuLoadedEvent = function(callback) {
                function onServiceMenuLoaded(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for global menu selection
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_SERVICE_MENU_LOADED') {
                        if ($.isFunction(callback)) {
                            callback(eventData);
                        }
                    }
                };
                window.addEventListener("message", onServiceMenuLoaded, false);
            };
            
            self.setCurrentMenuItem = function(menuItemId) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SET_CURRENT_ITEM'};
                message.menuItemId = menuItemId;
                window.postMessage(message, window.location.href);
            };
            
            self.registerServiceMenus = function(serviceMenuJson) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_REGISTER_SERVICE_MENU'};
                message.serviceMenuJson = serviceMenuJson;
                window.postMessage(message, window.location.href);
            };
            
            self.subscribeServiceMenuRegisterEvent = function(callback) {
                function onServiceMenuRegister(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for service menu registering
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_REGISTER_SERVICE_MENU') {
                        if ($.isFunction(callback)) {
                            callback(eventData.serviceMenuJson);
                        }
                    }
                };
                window.addEventListener("message", onServiceMenuRegister, false);
            };
            
            self.showCompositeObjectMenu = function(parentMenuId, objMenuName, menuJson){
                fireCompositeMenuDisplayEvent(parentMenuId, objMenuName, menuJson);
            };
            
            self.subscribeCompositeMenuDisplayEvent = function(callback) {
                function onCompositeMenuDisplay(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for composite menu display
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_COMPOSITE_DISPLAY') {
                        if ($.isFunction(callback)) {
                            callback(eventData.compositeParentMenuId, eventData.compositeRootMenuLabel, eventData.compositeMenuJson);
                        }
                    }
                };
                window.addEventListener("message", onCompositeMenuDisplay, false);
            };
            
            self.subscribeMenuSelectionEvent = function(callback) {
                function onMenuSelection(event) {
                    if (event.origin !== window.location.protocol + '//' + window.location.host) {
                        return;
                    }
                    var eventData = event.data;
                    //Only handle received message for global menu selection
                    if (eventData && eventData.tag && eventData.tag === 'EMAAS_OMC_GLOBAL_MENU_SELECTION') {
                        if ($.isFunction(callback)) {
                            callback(eventData.data);
                        }
                    }
                };
                window.addEventListener("message", onMenuSelection, false);
            };
            
            self.setupCustomKOStopBinding = function() {
                ko.bindingHandlers.stopBinding = {
                    init: function() {
                        return {controlsDescendantBindings: true};
                    }
                };
                ko.virtualElements.allowedBindings.stopBinding = true;
            };
            
            self.getServiceBaseVanityUrls = function(callbackForVanityUrls) {
                var dfdGetVanityUrls = $.Deferred();
                if (window._uifwk && window._uifwk.cachedData && window._uifwk.cachedData.baseVanityUrls && 
                        ($.isFunction(window._uifwk.cachedData.baseVanityUrls) ? window._uifwk.cachedData.baseVanityUrls() : true)) {
                    dfdGetVanityUrls.resolve($.isFunction(window._uifwk.cachedData.baseVanityUrls) ? window._uifwk.cachedData.baseVanityUrls() : 
                            window._uifwk.cachedData.baseVanityUrls);
                } else {
                    if (!window._uifwk) {
                        window._uifwk = {};
                    }
                    if (!window._uifwk.cachedData) {
                        window._uifwk.cachedData = {};
                    }
                    if (!window._uifwk.cachedData.isFetchingVanityUrls) {
                        window._uifwk.cachedData.isFetchingVanityUrls = true;
                        if (!window._uifwk.cachedData.baseVanityUrls) {
                            window._uifwk.cachedData.baseVanityUrls = ko.observable();
                        }

                        function doneCallback(data, textStatus, jqXHR) {
                            window._uifwk.cachedData.baseVanityUrls(data);
                            window._uifwk.cachedData.isFetchingVanityUrls = false;
                            dfdGetVanityUrls.resolve(data);
                        }

                        var lookupVanityUrl = '/sso.static/dashboards.registry';
                        if (dfu.isDevMode()) {
                            var dfBaseUrl = dfu.getDevData().dfRestApiEndPoint;
                            lookupVanityUrl = dfBaseUrl.substring(0, dfBaseUrl.indexOf('/emcpdf/')) + '/emcpdf/api/v1/registry';
                        }
                        lookupVanityUrl = lookupVanityUrl + '/lookup/baseVanityUrls';
                        ajaxUtil.ajaxWithRetry({type: 'GET', contentType: 'application/json', url: lookupVanityUrl,
                            dataType: 'json',
                            headers: dfu.getDefaultHeader(),
                            async: true,
                            success: function (data, textStatus, jqXHR) {
                                doneCallback(data, textStatus, jqXHR);
                            },
                            error: function (jqXHR, textStatus, errorThrown) {
                                console.log('Failed to get vanity URLs!');
                                window._uifwk.cachedData.isFetchingVanityUrls = false;
                                dfdGetVanityUrls.resolve(null);
                            }
                        });
                    } else {
                        window._uifwk.cachedData.baseVanityUrls.subscribe(function (data) {
                            dfdGetVanityUrls.resolve(data);
                        });
                    }
                }
                return dfdGetVanityUrls;
            };
            
            self.getVanityUrl = function(originalUrl, serviceName, callback) {
                if (!originalUrl || !serviceName) {
                    oj.Logger.error("Error: Failed to get vanity URL: serviceName=" + serviceName + ', originalUrl=' + originalUrl);
                }
                
                var host = window.location.host;
                var tenantName = userTenantUtil.getTenantName();
                if (host.indexOf(tenantName + '.') === 0) {
                    self.getServiceBaseVanityUrls().done(fetchServiceVanityUrl);
                }
                else {
                    callback(originalUrl);
                }
                
                function fetchServiceVanityUrl(urls) {
                    var targetUrl = originalUrl;
                    if (urls && urls[serviceName]) {
                        var baseVanityUrl = urls[serviceName];
                        if (baseVanityUrl.indexOf('://') > -1) {
                            var urlSplitted = baseVanityUrl.split('://');
                            if (urlSplitted.length === 2) {
                                var idx = originalUrl.indexOf('/emsaasui');
                                if (idx > -1 && idx !== 0) {
                                    originalUrl = originalUrl.substring(idx);
                                }
                                targetUrl = dfu.buildFullUrl(urlSplitted[0] + '://' + tenantName + '.' + urlSplitted[1], originalUrl);
                            }
                        }
                    }
                    callback(targetUrl);
                }
            };
        }
        return UIFWKGlobalMenuUtil;
    }
);

