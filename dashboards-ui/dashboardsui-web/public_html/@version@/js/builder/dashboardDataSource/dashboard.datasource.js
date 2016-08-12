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
                successCallback && successCallback(self.dataSource[dashboardId].dashboard);
            }
        };
        
        self.updateDashboardData = function(dashboardId,dashboard,successCallback,errorCallback){
             if(!self.dataSource[dashboardId]){
                 self.dataSource[dashboardId] = {};
             }
             Builder.updateDashboard(dashboardId,dashboard,function (data) {
                 self.dataSource[dashboardId].dashboard=data;
                 successCallback && successCallback(data);
            }, errorCallback);          
        };
        
        self.duplicateDashboard = function(dashboard, successCallback, errorCallback) {
            if(!self.dataSource[ko.unwrap(dashboard.id)]) {
                self.dataSource[ko.unwrap(dashboard.id)] = {};
            }
            
            Builder.duplicateDashboard(dashboard,
                    function(data) {
                        self.dataSource[ko.unwrap(dashboard.id)].dashboard = data;
                        successCallback && successCallback(data);
                    },
                    function(e) {
                        errorCallback && errorCallback(e);
                    });
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
        
        self.checkDashboardFavorites =function(dashboardId, successCallback, errorCallback){
            if (!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            if (isEmptyObject(self.dataSource[dashboardId]) || !self.dataSource[dashboardId].isFavorite) {
                Builder.checkDashboardFavorites(dashboardId,
                        function (data) {
                            self.dataSource[dashboardId].isFavorite = data;
                            successCallback && successCallback(data);
                        },
                        function (jqXHR, textStatus, errorThrown) {
                            errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                        });
            } else {
                successCallback && successCallback(self.dataSource[dashboardId].isFavorite);
            } 
        };
        
        self.addDashboardToFavorites = function(dashboardId, successCallback, errorCallback) {
            if(!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            
            Builder.addDashboardToFavorites(dashboardId,
                    function(data) {
                        self.dataSource[dashboardId].isFavorite = {"isFavorite": true};
                        successCallback && successCallback(data);
                    },
                    function(jqXHR, textStatus, errorThrown) {
                        errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                    });
        };
        
        self.removeDashboardFromFavorites = function(dashboardId, successCallback, errorCallback) {
            if(!self.dataSource[dashboardId]) {
                self.dataSource[dashboardId] = {};
            }
            
            Builder.removeDashboardFromFavorites(dashboardId,
                    function(data) {
                        self.dataSource[dashboardId].isFavorite = {"isFavorite": false};
                        successCallback && successCalback(data);
                    },
                    function(jqXHR, textStatus, errorThrown) {
                        errorCallback && errorCallback(jqXHR, textStatus, errorThrown);
                    });
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
