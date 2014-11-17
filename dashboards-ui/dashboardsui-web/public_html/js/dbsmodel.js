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
    
    function DashboardModel(id,name,description,widgets){
        var self = this;
        self.id = id;
        self.name = name;
        self.description = description;
        self.widgets = widgets;
        self.openDashboard = function(){
            //window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html?name='+encodeURIComponent(self.name)+"&description="+encodeURIComponent(self.description));
            window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html');
        }
    }
    
    function ViewModel() {
        
        var self = this;
        self.createDashboardModel = new createDashboardDialogModel();
        
        var _dbsArray = [];
         var _filterArr = [];
        var _did = 0;
        
       /**
        * 
        * Hard coded dashboards for demo, need to delete them when they are not needed in the future
        */
       var dsb0 = new DashboardModel(0, 'Application', 'Demo Dashboard', [{name: 'Response time'}, {name: 'Concurrency'}, {name: 'Error'}]);
       var dsb1 = new DashboardModel(1, 'Database', 'Demo Dashboard', [{name: 'Top SQL'}, {name: 'Ora-XX Error'}, {name: 'SGA'}]);
       var dsb2 = new DashboardModel(2, 'Middleware', 'Demo Dashboard', [{name: 'CPU Usage'}, {name: 'Memory Usage'}, {name: 'JVM Heap Usage'}]);
       var dsb3 = new DashboardModel(3, 'Server Error', 'Demo Dashboard', [{name: 'Server Status'}, {name: 'Server (Down)'}, {name: 'Server Location'}]);       
        _dbsArray.push(dsb0);
        _dbsArray.push(dsb1);
        _dbsArray.push(dsb2);
        _dbsArray.push(dsb3);
        _did=3;
        
        /*
        for (var _did = 0; _did < 100000; _did++)
        {
            var _db = new DashboardModel( _did, 'Dashboard'+_did, 'Demo Dashboard'+_did, [{name: 'Demo Wedgit'}, {name: 'Demo Wedgit'}, {name: 'Demo Wedgit'}]);
            _dbsArray.push(_db);
        }*/

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
      
        var _count = 4;
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
        
        self.handleDashboardClicked = function(event, data) {
            //console.log(data);
            data.dashboard.openDashboard();
        };
        
        self.handleDashboardDeleted = function(event, data) {
            //console.log(data);
            var __id = data.dashboard['id'];
            _dbsArray = $.grep(_dbsArray, function(o) {
                return o.id !== __id;
            });
            _filterArr = $.grep(_filterArr, function(o) {
                return o.id !== __id;
            });
            if (_filterArr.length > 0)
            {
                self.pagingDatasource(new oj.ArrayPagingDataSource(_filterArr));
            }
            else
            {
                self.pagingDatasource(new oj.ArrayPagingDataSource(_dbsArray));
            }
            console.log("ok");
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
            
            var _addeddb = new DashboardModel(_did, self.createDashboardModel.name(), self.createDashboardModel.description());
//            _dbsArray.unshift(_addeddb);
            _dbsArray.push(_addeddb);
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
            
            _filterArr = $.grep(_dbsArray, function(o) {
                if (!value || value.length <=0) return true; //no filter
                return _contains(o.name, value);
            });
            return _filterArr;
        };
        
        self.searchResponse = function (event, data)
        {
            //console.log("searchResponse: "+data.content.length);
            self.pagingDatasource(new oj.ArrayPagingDataSource(data.content));
        };
    };
            
    return {'ViewModel': ViewModel};
});
