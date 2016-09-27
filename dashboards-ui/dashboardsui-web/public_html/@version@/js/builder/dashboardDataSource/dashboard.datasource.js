/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['knockout',
    'jquery',
    'ojs/ojcore',
    'builder/builder.core',
    'dfutil'
], function(ko, $, oj, dfu) {
    
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
                            var dsb = getKODashboardForUI(data);
                            self.dataSource[dashboardId].dashboard = dsb;
                            successCallback && successCallback(dsb);
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
             Builder.updateDashboard(dashboardId,dashboard,
                function (data) {                    
                    if (data && data['name'] && data['name'] !== null)
                    {
                        data['name'] = $("<div/>").html(data['name']).text();
                    }
                    if (data && data['description'] && data['description'] !== null)
                    {
                        data['description'] = $("<div/>").html(data['description']).text();
                    }
                    self.dataSource[dashboardId].dashboard=data;
                    successCallback && successCallback(data);
               }, 
               errorCallback);          
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
                        successCallback && successCallback(data);
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
        
        //convert dashboard returned from datebase to knockout obaservable for UI use
        function getKODashboardForUI(data) {
            // If dashboad is single page app, success callback will be ignored
            if (data.type === "SINGLEPAGE") {
                try {
                    var tile = data.tiles[0];
                    var url = dfu.df_util_widget_lookup_assetRootUrl(tile["PROVIDER_NAME"], tile["PROVIDER_VERSION"], tile["PROVIDER_ASSET_ROOT"], false);

                    if (dfu.isDevMode()) {
                        url = dfu.getRelUrlFromFullUrl(url);
                    }
                    window.location = url;
                    return;
                } catch (e) {
                    oj.Logger.error(e);
                }
            }


            var mapping = {
                "tiles": {
                    "create": function (options) {
                        return new Builder.TileItem(options.data);
                    }
                }
            };
            if (data && data['name'] && data['name'] !== null)
            {
                data['name'] = $("<div/>").html(data['name']).text();
            }
            if (data && data['description'] && data['description'] !== null)
            {
                data['description'] = $("<div/>").html(data['description']).text();
            }
            var dsb = ko.mapping.fromJS(data, mapping);
            dsb.isDefaultTileExist = function () {
                for (var i in dsb.tiles()) {
                    if (dsb.tiles()[i].type() === "DEFAULT") {
                        return true;
                    }
                }
                return false;
            };
            return dsb;
        }
        
        DashboardDataSource.instance = self;
    }
    
    Builder.registerModule(DashboardDataSource, "DashboardDataSource");
});
