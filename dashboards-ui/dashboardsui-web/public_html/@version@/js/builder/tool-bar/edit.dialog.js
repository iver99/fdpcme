
define(['knockout', 
        'jquery',
        'ojs/ojcore',
        'dfutil'
    ], 
    function(ko, $, oj, dfu) {
        function EditDashboardDialogModel($b, tbModel) {
            var dsb = $b.dashboard;
            var self = this;
            self.dashboard = dsb;
            self.tbModel = tbModel;
            self.name = ko.observable(dsb.name());
            self.nameInputed = ko.observable(undefined); //read only input text
            self.description = ko.observable(dsb.description ? dsb.description() : undefined);
            self.timeRangeFilterValue = ko.observable(["ON"]);//for now ON always and hide option in UI
            self.targetFilterValue = ko.observable(["OFF"]);
            self.nameValidated = ko.observable(true);
            self.isDisabled = ko.computed(function() { 
                if (self.nameInputed() && self.nameInputed().length > 0)
                {
                    return false;
                }
                return true;
            });
        
            self.noSameNameValidator = {
                    'validate' : function (value) {
                        self.nameValidated(true);
                        if (self.name() === value)
                            return true;
                        value = value + "";

                        if (value && Builder.isDashboardNameExisting(value)) {
                            //$('#builder-dbd-name-input').focus();
                            self.nameValidated(false);
                            throw new oj.ValidatorError(oj.Translations.getTranslatedString("COMMON_DASHBAORD_SAME_NAME_ERROR"), 
                                             oj.Translations.getTranslatedString("COMMON_DASHBAORD_SAME_NAME_ERROR_DETAIL"));
                        }
                        return true;
                    }
            };

            self.open = function() {
                self.clear();
                $b.findEl(".cDsbDialog").ojDialog( "open" );    
            };

            self.save = function() {
                if (self.nameValidated() === false) 
                    return;
                
                var url = "/sso.static/dashboards.service/";
                if (dfu.isDevMode()) {
                        url = dfu.buildFullUrl(dfu.getDevData().dfRestApiEndPoint, "dashboards/");
                }
                dfu.ajaxWithRetry(url + self.dashboard.id() + "/quickUpdate", {
                        type: 'PUT',
                        dataType: "json",
                        contentType: 'application/json',
                        data: JSON.stringify({name: self.name(), description: self.description()}),
                        headers: dfu.getDashboardsRequestHeader(), //{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},
                        success: function (result) {
                            self.dashboard.name(self.name());
                            self.tbModel.dashboardName(self.name());
                            if (self.dashboard.description)
                            {
                                self.dashboard.description(self.description());
                            }
                            else
                            {
                                self.dashboard.description = ko.observable(self.description());
                            }
                            self.tbModel.dashboardDescription(self.description());
                            $b.findEl(".cDsbDialog").ojDialog("close"); 
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                        }
                    });
            };

            self.clear = function() {
                self.name(self.dashboard.name());
                self.description(self.dashboard.description ? self.dashboard.description() : undefined);
            };

            self.isEnableTimeRange = function() {
                if (self.timeRangeFilterValue()  === "ON" || 
                        self.timeRangeFilterValue()[0] === "ON")
                {
                    return true;
                }
                return false;
            };
        }
        
        return {"EditDashboardDialogModel": EditDashboardDialogModel};
    }
);
