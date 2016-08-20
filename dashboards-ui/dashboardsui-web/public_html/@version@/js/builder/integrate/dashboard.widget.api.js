define(['ojs/ojcore', 'knockout', 'jquery',
    'builder/builder.core'
],
    function (oj, ko, $)
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
                 require(['emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils'], function(TargetSelectorUtils){
                    if (self.tselContextChanged) {
                        TargetSelectorUtils.getSelection(TargetSelectorUtils.MEID_FORCE_QUERY, self.tselContext,
                            function (targets) {
                                self.tselContextChanged = false;
                                self.targetList = targets;
                                executeCallback(callback);
                            }
                        );
                    } else {
                        executeCallback(callback);
                    }
                });;
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