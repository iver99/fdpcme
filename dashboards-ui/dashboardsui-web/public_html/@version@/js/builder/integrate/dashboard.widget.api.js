define(['ojs/ojcore', 'knockout', 'jquery',
//    'emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils',
    'builder/builder.core'
],
    function (oj, ko, $/*, TargetSelectorUtils*/)
    {
        function DashboardWidgetAPI() {
            var self = this;
            self.tselContextChanged = false;
            self.tselContext = {};
            self.targetList = [];

            self.setTargetSelectionContext = function (targetSelectionContext) {
                self.tselContext = targetSelectionContext;
                self.tselContextChanged = true;
            };

            self.getTargetInstancesList = function (callback) {
                    if (self.tselContextChanged) {
                        var versionedTargetSelectorUtils = window.getSDKVersionFile ? 
                            window.getSDKVersionFile('emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils') : null;
                        var targetSelectorUtilsModel = versionedTargetSelectorUtils ? versionedTargetSelectorUtils : 
                                'emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils';
                        require([targetSelectorUtilsModel], function(TargetSelectorUtils) {
                            TargetSelectorUtils.getSelection(TargetSelectorUtils.MEID_FORCE_QUERY, self.tselContext,
                                function (targets) {
                                    self.tselContextChanged = false;
                                    self.targetList = targets;
                                    executeCallback(callback);
                                }
                            );
                        });
                    } else {
                        executeCallback(callback);
                    }
            };

            function executeCallback(callback) {
                if (callback) {
                    callback(self.targetList);
                }
            }

        }

        window.DashboardWidgetAPI = new DashboardWidgetAPI();

        return DashboardWidgetAPI;
    });