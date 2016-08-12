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
        var self= this;
        
        if(typeof DashboardDataSource.instance === 'object') {
            return DashboardDataSource.instance;
        }
        
        self.dashboard = null;
        
        self.loadDashboardData = function(dashboardId, successCallback, errorCallback) {
            if(self.dashboard) {
                successCallback && successCallback(self.dashbaord);
            }else {
                Builder.loadDashboard(dashboardId,
                function(data) {
                    self.dashboard = data;
                    successCallback && successCallback(data);
                },
                errorCallback);
            }
        }
        
        DashboardDataSource.instance = self;
    }
    
    Builder.registerModule(DashboardDataSource, "DashboardDataSource");
});
