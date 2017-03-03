define([
    'ojs/ojcore',
    'knockout',
    'jquery',
    'ojL10n!uifwk/@version@/js/resources/nls/uifwkCommonMsg'
],
    function (oj, ko, $, nls)
    {
        function UIFWKGlobalMenuUtil() {
            var self = this;
            
            function fireCompositeMenuDisplayEvent(objMenuName, menuJson) {
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_COMPOSITE_DISPLAY'};
                message.compositeRootMenuLabel = objMenuName;
                message.compositeMenuJson = menuJson;
                window.postMessage(message, window.location.href);
            }
            
            self.setCurrentMenuItem = function(menuItemId){
                var message = {'tag': 'EMAAS_OMC_GLOBAL_MENU_SET_CURRENT_ITEM'};
                message.menuItemId = menuItemId;
                window.postMessage(message, window.location.href);
            };
            
            self.showCompositeObjectMenu = function(objMenuName, menuJson){
                fireCompositeMenuDisplayEvent(objMenuName, menuJson);
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
                            callback(eventData.compositeRootMenuLabel, eventData.compositeMenuJson);
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
            
            self.OMC_COMMON_MENU_ALERTS = {id: 'omc_common_alerts', 
                labelKey: nls.BRANDING_BAR_HAMBURGER_MENU_COMMON_ALERTS_LABEL};
            self.OMC_COMMON_ADMIN_MENU_ALERTS = {id: 'omc_common_admin_alertrules', 
                labelKey: nls.BRANDING_BAR_HAMBURGER_MENU_COMMON_ADMIN_ALERT_RULES_LABEL};
        }
        return UIFWKGlobalMenuUtil;
    }
);

