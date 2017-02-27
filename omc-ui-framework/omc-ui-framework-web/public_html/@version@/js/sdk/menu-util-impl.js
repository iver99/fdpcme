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
            
            self.subscribeMenuSelectionEvent = function (callback) {
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
                name: nls.BRANDING_BAR_HAMBURGER_MENU_COMMON_ALERTS_LABEL};
            self.OMC_COMMON_ADMIN_MENU_ALERTS = {id: 'omc_common_admin_alertrules', 
                name: nls.BRANDING_BAR_HAMBURGER_MENU_COMMON_ADMIN_ALERT_RULES_LABEL};
        }
        return UIFWKGlobalMenuUtil;
    }
);

