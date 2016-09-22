define(['ojs/ojcore', 'jquery', 'knockout', 'ojs/ojdatacollection-common'],
       function(oj, $, ko)
{

/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */

var DashboardArrayTableSource = function(data, options)
{
    DashboardArrayTableSource.superclass.constructor.call(this, data, options);
};

// Subclass from oj.PagingDataSource
oj.Object.createSubclass(DashboardArrayTableSource, oj.ArrayTableDataSource, "DashboardArrayTableSource");

DashboardArrayTableSource.prototype.Init = function()
{
  DashboardArrayTableSource.superclass.Init.call(this);
};

DashboardArrayTableSource.prototype.sort = function(criteria)
{
    return Promise.resolve();
};

DashboardArrayTableSource.prototype.handleEvent = function(event, data)
{
    DashboardArrayTableSource.superclass.handleEvent.call(this, event, data);
};

return {'DashboardArrayTableSource': DashboardArrayTableSource};

});