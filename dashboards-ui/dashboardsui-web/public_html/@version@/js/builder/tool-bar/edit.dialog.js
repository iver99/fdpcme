/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 
        'jquery',
        'ojs/ojcore'
    ], 
    function(ko, $, oj) {
        function EditDashboardDialogModel(dsb, tbModel) {
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
                $( "#cDsbDialog" ).ojDialog( "open" );    
            };

            self.save = function() {
                if (self.nameValidated() === false) 
                    return;
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
                $( "#cDsbDialog" ).ojDialog( "close" ); 
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
        
//        self.keydown = function (d, e) {
//           if (e.keyCode === 13) {
//              $( "#cDsbDialog" ).ojDialog( "close" );
//           }
//        };
        }
        
        return {"EditDashboardDialogModel": EditDashboardDialogModel};
    }
);
