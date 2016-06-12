
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
            self.descriptionValue = ko.observable(dsb.enableDescription ? (dsb.enableDescription()==="TRUE"?["ON"]:["OFF"]) : ["OFF"]);
            self.entityFilterValue = ko.observable(dsb.enableEntityFilter ? (dsb.enableEntityFilter()==="TRUE"?["ON"]:["OFF"]) : ["OFF"]);//ko.observable(["OFF"]);
            self.timeRangeFilterValue = ko.observable(dsb.enableTimeRange ? (dsb.enableTimeRange()==="TRUE"?["ON"]:["OFF"]) : ["ON"]); //ko.observable(["ON"]);//for now ON always and hide option in UI
//            self.targetFilterValue = ko.observable(["OFF"]);
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
                        data: JSON.stringify({name: self.name(), description: self.description(), enableDescription: self.isEditDsbOptionEnabled(self.descriptionValue) ? "TRUE" : "FALSE", enableEntityFilter: (self.isEditDsbOptionEnabled(self.entityFilterValue) ? "TRUE" : "FALSE"), enableTimeRange: (self.isEditDsbOptionEnabled(self.timeRangeFilterValue) ? "TRUE" : "FALSE")}),
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
                            self.dashboard.enableDescription(self.isEditDsbOptionEnabled(self.descriptionValue) ? "TRUE" : "FALSE");
                            self.tbModel.dashboardDescriptionEnabled(self.isEditDsbOptionEnabled(self.descriptionValue) ? "TRUE" : "FALSE");
                            self.dashboard.enableEntityFilter(self.isEditDsbOptionEnabled(self.entityFilterValue) ? "TRUE" : "FALSE");
                            self.dashboard.enableTimeRange(self.isEditDsbOptionEnabled(self.timeRangeFilterValue) ? "TRUE" : "FALSE");
                            $('#edit-dashboard').ojDialog("close"); 
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            dfu.showMessage({type: 'error', summary: getNlsString('DBS_BUILDER_MSG_ERROR_IN_SAVING'), detail: '', removeDelayTime: 5000});
                        }
                    });
            };
            
            self.cancel = function() {
               $("#edit-dashboard").ojDialog("close"); 
            }

            self.clear = function() {
                self.name(self.dashboard.name());
                self.description(self.dashboard.description ? self.dashboard.description() : undefined);
            };

            self.isEditDsbOptionEnabled = function(optionVal) {
                if(optionVal() === "ON" ||
                        optionVal()[0] === "ON") {
                    return true;
                }
                return false;
            };
        }
        
        return {"EditDashboardDialogModel": EditDashboardDialogModel};
    }
);
