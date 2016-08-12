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
    
    function DashboardDataSource() {
        var self = this;
        
        if(typeof DashboardDataSource.instance === 'object') {
            return DashboardDataSource.instance;
        }
        
        self.instance={};
        
        self.loadDashboardUserOptionsData = function (dashboardId, successCallback, errorCallback) {
            if (!self.instance[dashboardId]) {
                self.instance[dashboardId] = {};
            }
            if (isEmptyObject(self.instance[dashboardId]) || !self.instance[dashboardId].userOptions) {
                Builder.fetchDashboardOptions(dashboardId,
                        function (data) {
                            self[dashboardId].userOptions = data;
                            self[dashboardId].hasUserOptionInDB = true;
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            self[dashboardId].hasUserOptionInDB = false;
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            } else {
                successCallback && successCallback(self.instance[dashboardId].userOptions);
            }
        };
        
        self.saveDashboardUserOptions = function (optionsJson, successCallback, errorCallback) {
            if (!self.instance[optionsJson.dashboardId]) {
                self.instance[optionsJson.dashboardId] = {};
            }
            if (isEmptyObject(self.instance[optionsJson.dashboardId]) || !self.instance[optionsJson.dashboardId].hasUserOptionInDB) {
                Builder.saveDashboardOptions(optionsJson,
                        function (data) {
                            self[optionsJson.dashboardId] = {
                                "userOptions": data
                            };
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            } else {
                Builder.updateDashboardOptions(optionsJson,
                        function (data) {
                            self[optionsJson.dashboardId] = {
                                "userOptions": data
                            };
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            }
        };
        
        self.loadDashboardData = function (dashboardId, successCallback, errorCallback) {
            if (!self.instance[dashboardId]) {
                self.instance[dashboardId] = {};
            }
            if (isEmptyObject(self.instance[dashboardId]) || !self.instance[dashboardId].dashboard) {
                Builder.loadDashboard(dashboardId,
                        function (data) {
                            self.instance[dashboardId].dashboard = data;
                            successCallback && successCallback(data);
                        },
                        errorCallback);
            } else {
                successCallback && successCallback(self.instance[dashboardId].dashbaord);
            }
        };
        
        function isEmptyObject(obj) {
            var index;
            for (index in obj)
                return !1;
            return !0;
        }  
        
        DashboardDataSource.instance = self;
    }
    
    Builder.registerModule(DashboardDataSource, "DashboardDataSource");
});
