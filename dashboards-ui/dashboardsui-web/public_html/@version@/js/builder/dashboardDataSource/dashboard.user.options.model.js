/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
    'jquery',
    'ojs/ojcore',
    'builder/builder.core'
], function(ko, $, oj) {
    
    function DashboardUserOptionsDataSource() {
        var self = this;
        
        if(typeof DashboardUserOptionsDataSource.instance === 'object') {
            return DashboardUserOptionsDataSource.instance;
        }
        
        self.userOptions = null;
        self.hasUserOptionInDB = ko.observable(false);
        
        self.loadDashboardUserOptionsData = function(dashboardId, successCallback, errorCallback) {
            if(self.userOptions) {
                successCallback && successCallback(self.userOptions);
            }else {
                Builder.fetchDashboardOptions(dashboardId, 
                function(data) {
                    self.userOptions = data;
                    self.hasUserOptionInDB(true);
                    successCallback && successCallback(data);
                }, 
                function(jqXHR, textStatus, errorThrown) {
                    self.hasUserOptionInDB(false);
                    errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                });
            }
        }
        
        self.saveDashboardUserOptions = function(optionsJson, successCallback, errorCallback) {
            if(self.hasUserOptionInDB()) {
                Builder.updateDashboardOptions(optionsJson,
                function(data) {
                    self.userOptions = data;
                    successCallback && successCallback(data);
                },
                function(jqXHR, textStatus, errorThrown) {
                    errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                });
            }else {
                Builder.saveDashboardOptions(optionsJson,
                function(data) {
                    self.userOptions = data;
                    successCallback && successCallback(data);
                },
                function(jqXHR, textStatus, errorThrown) {
                    errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                });
            }
        }
        
        DashboardUserOptionsDataSource.instance = self;
    }
    
    Builder.registerModule(DashboardUserOptionsDataSource, "DashboardUserOptionsDataSource");
});
