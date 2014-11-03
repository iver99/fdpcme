/**
 * @preserve Copyright (c) 2014, Oracle and/or its affiliates.
 * All rights reserved.
 */

/**
 * @preserve Copyright 2013 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */

define([
    'text!/emcpdfui/builderTemplate.html',
    'dbs/dashboard-tab-model',
    'ojs/ojcore', 
    'knockout', 
    'jquery', 
    'ojs/ojknockout', 
    'ojs/ojpagingcontrol',
    'ojs/ojpagingcontrol-model'
],
function(temp, tabmodel, oj, ko, $)
{
    
    function ViewModel() {
        
        var self = this;
        
        var _dbsArray = [];
        for (var _i = 0; _i < 1000; _i++)
        {
            var _db = {id: _i, name: 'Dashboard'+_i, tiles: [{name: 'Demo Tile'}, {name: 'Demo Tile'}, {name: 'Demo Tile'}]};
            _dbsArray.push(_db);
        }
        
        self.pagingDatasource = new oj.ArrayPagingDataSource(_dbsArray);
        self.dashboards = self.pagingDatasource.getWindowObservable();
        
       /*
       self.serviceURL = 'http://slc04wjl.us.oracle.com:7101/emlacore/resources/dashboards';
       self.parseDashboard = function(response) {
            return {
                //id: response['id'],
                name: response['name'],
                tiles: response['tiles']
            };
        };
        self.DashboardModel = oj.Model.extend({
                urlRoot: self.serviceURL,
                parse: self.parseDashboard,
                idAttribute: 'name'
        });
        
        var _customPagingOptions = function(response){
          var _ret = {
              totalResults: response['totalResults'],
              limit: response['limit'],
              count: response['count'],
              offset:response['offset'],
              hasMore:response['hasMore'],
              //items: response
          };
          return _ret;
        };

        //self.myDept = new self.Department();
        self.DashboardCollection = oj.Collection.extend({
                url: self.serviceURL,
                model: self.DashboardModel,
                fetchSize: 50,
                modelLimit:50,
                //customPagingOptions: _customPagingOptions
        });
        
        var _col = new self.DashboardCollection();
        //var o = _col.where({name: 'dash'});
        self.pagingDatasource = new oj.CollectionPagingDataSource(_col);
        self.dashboards = self.pagingDatasource.getWindowObservable(); //ko.observableArray([]);
        */
       
        var _count = 1;
        //console.log(temp);
        self.addTab =  function()
        {
            var _tabId = "dbstab_" + _count;
            var _contentId = _tabId + "_ct";
            _count++;
            var title = "Tab: " + _tabId;
            
            var _newTab = "<div id=\""+_tabId+"\" class=\"dbs-tab-container\"><span>" + title + "</span><div id=\""+_contentId+"\"></span>"+temp+"</div></div>";
            var _newTabEle = $(_newTab);
            //$("#dtabs").ojTabs("addTab", _newTabEle);
            $("#dtabs").dbsTabs("addTab", _newTabEle);
            var _dmodel = new tabmodel.DashboardTabModel(_tabId);
            ko.applyBindings(_dmodel, document.getElementById(_contentId));
            _dmodel.afterModelBinding();
            //$("#dtabs").ojTabs( "option", "selected", _tabId);
            $("#dtabs").dbsTabs( "option", "selected", _tabId);
        };
        
        self.removeTab =  function()
        {
            var _tabId = "dbstab_1";
            $("#dtabs").dbsTabs("removeTab", _tabId);
        };
        
        self.createDashboardClicked = function()
        {
            $( "#cDsbDialog" ).ojDialog( "open" );
        };
        
        self.confirmDashboardCreate = function()
        {
            //self.addTab();
            $( "#cDsbDialog" ).ojDialog( "close" );
            if (self.timeRangeFilterValue()=="ON"){
                window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html?includeTimeRangeFilter=true');
            }else{
                window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html');
            }
        };
        
        self.cancelDashboardCreate = function()
        {
            $( "#cDsbDialog" ).ojDialog( "close" );
        };
        
        self.timeRangeFilterValue = ko.observable(["OFF"]);
        self.targetFilterValue = ko.observable(["OFF"]);
    };
            
    return {'ViewModel': ViewModel};
});
