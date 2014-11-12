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
    function createDashboardDialogModel() {
        var self = this;
        self.name = ko.observable('');
        self.description = ko.observable('');
        self.timeRangeFilterValue = ko.observable(["OFF"]);
        self.targetFilterValue = ko.observable(["OFF"]);
        
        self.clear = function() {
            self.name('');
            self.description('');
            self.timeRangeFilterValue(["OFF"]);
            self.targetFilterValue(["OFF"]);
        };
    }
    
    function ViewModel() {
        
        var self = this;
        self.createDashboardModel = new createDashboardDialogModel();
        
        var _dbsArray = [];
        var _did = 0;
        
        for (var _did = 0; _did < 100000; _did++)
        {
            var _db = {id: _did, name: 'Dashboard'+_did, description: 'Demo Dashboard'+_did, tiles: [{name: 'Demo Tile'}, {name: 'Demo Tile'}, {name: 'Demo Tile'}]};
            _dbsArray.push(_db);
        }
        
        self.pageSize = ko.observable(20);
        self.pagingDatasource = ko.observable(new oj.ArrayPagingDataSource(_dbsArray));
        self.dashboards = ko.computed(function() {
            return (self.pagingDatasource().getWindowObservable())();
        });
        self.showPaging = ko.computed(function() {
            return self.pagingDatasource().totalSize() > self.pageSize();
        });
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
        var _pagingds = new oj.CollectionPagingDataSource(_col);
        _pagingds.setPageSize(self.pageSize());
        _pagingds.fetch({'startIndex': 0, 'fetchType': 'init', 'success': function() {
                self.pagingDatasource( _pagingds );
        }} );
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
            self.createDashboardModel.clear();
            $( "#cDsbDialog" ).ojDialog( "open" );
        };
        
        self.confirmDashboardCreate = function()
        {
            //self.addTab();
            $( "#cDsbDialog" ).ojDialog( "close" );
            
            ++_did;
            var _timeRangeFilter = false;
            if (self.createDashboardModel.timeRangeFilterValue() == "ON") _timeRangeFilter=true;
            
            var _addeddb = {id: _did, name: self.createDashboardModel.name(), description: self.createDashboardModel.description(), timeRangeFilter: _timeRangeFilter, tiles: [{name: 'Demo Tile'}, {name: 'Demo Tile'}, {name: 'Demo Tile'}]};
            _dbsArray.unshift(_addeddb);
            self.pagingDatasource(new oj.ArrayPagingDataSource(_dbsArray));
            
            var _param = "?name="+encodeURIComponent(self.createDashboardModel.name())+"&description="+encodeURIComponent(self.createDashboardModel.description());
            
            if (_timeRangeFilter === true) {
                _param = _param + "&includeTimeRangeFilter=true"
            }
            window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html'+_param);
        };
        
        self.cancelDashboardCreate = function()
        {
            $( "#cDsbDialog" ).ojDialog( "close" );
        };
        
        self.searchFilterFunc = function (arr, value)
        {
            var _contains = function(s1, s2)
                        {
                            if (!s1 && !s2)
                                return true;
                            if (s1 && s2)
                            {
                                if (s1.toUpperCase().indexOf(s2.toUpperCase()) > -1)
                                    return true;
                            }
                            return false;
                        };
            //console.log("Arrary length: "+arr.length);
            //console.log("Value: "+value);
            var _filterArr = $.grep(_dbsArray, function(o) {
                if (!value || value.length <=0) return true; //no filter
                return _contains(o.name, value);
            });
            return _filterArr;
        };
        
        self.searchResponse = function (event, data)
        {
            //console.log("searchResponse: "+data.content.length);
            self.pagingDatasource(new oj.ArrayPagingDataSource(data.content));
            //self.dashboards = self.pagingDatasource().getWindowObservable();
        };
    };
            
    return {'ViewModel': ViewModel};
});
