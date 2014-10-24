/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */


define([
    'ojs/ojcore', 
    'knockout', 
    'jquery', 
    'ojs/ojknockout'
],
function(oj, ko, $)
{
    function DashboardTabModel(tabId, dashboardId) {
        var self = this;
        self.tabId = tabId;
        self.dashboardId = dashboardId;
        
        DashboardTabModel.superclass.constructor.call(this);
    }
    
    oj.Object.createSubclass(DashboardTabModel, oj.Object, 'DashboardTabModel');

    /**
     * Initializes the data source.
     * @export
     */
    DashboardTabModel.prototype.Init = function()
    {
        // super
        DashboardTabModel.superclass.Init.call(this);
    };
    
    DashboardTabModel.prototype.addEeventHandler = function(eventName, handler)
    {
        
    };
    
    DashboardTabModel.prototype.fireEevent = function(eventName, data)
    {
        
    };
    
    DashboardTabModel.prototype.afterModelBinding = function()
    {
         console.log("[dashboards.DashboardTabModel] afterModelBinding is called. "/*+ this.options['test']*/);
    };
    
    DashboardTabModel.prototype.afterTabRemoved = function()
    {
        
    };
            
    return {'DashboardTabModel': DashboardTabModel};
});
