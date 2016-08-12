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
        
        self.dataSource={};
        
        self.loadDashboardUserOptionsData = function (dashboardId, successCallback, errorCallback) {
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].userOptions) {
                Builder.fetchDashboardOptions(dashboardId,
                        function (data) {
                            self.dataSource[dashboardId].userOptions = data;
                            self.dataSource[dashboardId].hasUserOptionInDB = true;
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            self[dashboardId].hasUserOptionInDB = false;
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            } else {
                successCallback && successCallback(self.dataSource[dashboardId].userOptions);
            }
        };
        
        self.saveDashboardUserOptions = function (optionsJson, successCallback, errorCallback) {
            if (!self.dataSource[optionsJson.dashboardId]) {
                self.dataSource[optionsJson.dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[optionsJson.dashboardId]) || !self.dataSource[optionsJson.dashboardId].hasUserOptionInDB) {
                Builder.saveDashboardOptions(optionsJson,
                        function (data) {
                            self.dataSource[optionsJson.dashboardId] = {
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
                            self.dataSource[optionsJson.dashboardId] = {
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
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].dashboard) {
                Builder.loadDashboard(dashboardId,
                        function (data) {
                            self.dataSource[dashboardId].dashboard = data;
                            successCallback && successCallback(data);
                        },
                        errorCallback);
            } else {
                successCallback && successCallback(self.dataSource[dashboardId].dashbaord);
            }
        };
        
        self.updateDashboardData = function(dashboardId,successCallback,errorCallback){
             if(!self.dataSource[dashboardId]){
                 self.dataSource[dashboardId] = {};
             }
             Builder.updateDashboard(dashboardId, function (data) {
                 self.dataSource[dashboardId].dashboard=data;
                 successCallback && successCallback(data);
            }, errorCallback);          
        };
        
        self.fetchScreenshotData = function(dashboardId, successCallback, errorCallback){
            if(!self.dataSource[dashboardId]){
                 self.dataSource[dashboardId] = {};
             }
             if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].screenshot) {
                Builder.fetchDashboardScreenshot(dashboardId,
                        function (data) {
                            self.dataSource[dashboardId].screenshot = data;
                            successCallback && successCallback(data);
                        },
                        errorCallback);
            } else {
                successCallback && successCallback(self.dataSource[dashboardId].screenshot);
            }           
        };
        
        self.fetchFavoriteData =function(dashboardId, successCallback, errorCallback){
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].favorite) {
                Builder.checkDashboardFavorites(dashboardId,
                        function (data) {
                            self.dataSource[dashboardId].favorite = data;
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            } else {
                successCallback && successCallback(self.dataSource[dashboardId].favorite);
            } 
        };
                      
        function isEmptyObject(obj) {
            var index;
            for (index in obj)
                return !1;
            return !0;
        };  
        
        DashboardDataSource.instance = self;
    }
    
    Builder.registerModule(DashboardDataSource, "DashboardDataSource");
});
