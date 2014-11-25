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
    };
    
    function confirmDialogModel(title, okLabel, message, okFunction) {
        var self = this;
        //self.style = ko.observable('min-width: 450px; min-height:150px;');
        self.title = ko.observable(title || '');
        self.okLabel = ko.observable(okLabel || '');
        self.message = ko.observable(message || '');
        
        self.okFunction = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {}; 
        
        self.show = function (title, okLabel, message, okFunction) {
            self.title(title || '');
            self.okLabel(okLabel || '');
            self.message(message || '');
            self.okFunction = function () {
                var _okfunc = (okFunction && $.isFunction(okFunction)) ? okFunction : function() {};
                _okfunc();
                self.close();
            };
            $( "#dbs_cfmDialog" ).ojDialog( "open" );
        };
        
        self.close = function () {
            $( "#dbs_cfmDialog" ).ojDialog( "close" );
        };
    }; 
    
    function DashboardModel(id,name,description,includeTimeRangeFilter,widgets){
        var self = this;
        self.id = id;
        self.name = name;
        self.description = description;
        self.type = 0;
        self.widgets = widgets;
        self.image = undefined; //"http://upload.wikimedia.org/wikipedia/commons/4/4a/Logo_2013_Google.png";
        self.includeTimeRangeFilter = includeTimeRangeFilter===null?false:includeTimeRangeFilter;
        self.currentPageNum = 1;
        self.openDashboard = function(){
            //window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html?name='+encodeURIComponent(self.name)+"&description="+encodeURIComponent(self.description));
            window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html?dashboardId='+self.id);
        }
    };
    
    function ViewModel() {
        
        var self = this;
        self.selectedDashboard = ko.observable(null);
        self.createDashboardModel = new createDashboardDialogModel();
        self.confirmDialogModel = new confirmDialogModel();
        
        self.dbsArray = [];
        self.filterArray = [];
        self.did = 0;
        
       var dsb0 = new DashboardModel(0, 'Application', 
            "Application Dashboard includes widgets: Application Response Time, Security Incidents, Security Histogram.",false, 
            [{"title":"Application Response Time 1","WIDGET_KOC_NAME":"demo-chart-widget","TILE_WIDTH":1},{"title":"Security Incidents 1","WIDGET_KOC_NAME":"demo-chart-widget","TILE_WIDTH":2},{"title":"Security Incidents 2","WIDGET_KOC_NAME":"demo-chart-widget","TILE_WIDTH":1},{"title":"Security Histogram 4","WIDGET_KOC_NAME":"demo-chart-widget","TILE_WIDTH":4}]);
        
        var dsb1 = new DashboardModel(1, 'Refresh Demo', 
        "A dashboard to demonstrate widget refresh by time range change",true,
        [{"title":"Database Diagnostics","WIDGET_KOC_NAME":"demo-chart-widget","TILE_WIDTH":2},{"title":"Security Incidents","WIDGET_KOC_NAME":"demo-chart-widget","TILE_WIDTH":2},{"title":"Top SQL","WIDGET_KOC_NAME":"demo-chart-widget","TILE_WIDTH":4}]);
       
        var dsb2 = new DashboardModel(2, 'Event Demo', 
        'A dashboard demonstrating wiget refresh',false, 
        [{"title":"Publisher","WIDGET_KOC_NAME":"demo-publisher-widget","TILE_WIDTH":2},{"title":"Subscriber 1","WIDGET_KOC_NAME":"demo-subscriber-widget","TILE_WIDTH":1},{"title":"Subscriber 2","WIDGET_KOC_NAME":"demo-subscriber-widget","TILE_WIDTH":1}]);
        
        var dsb3 = new DashboardModel(3, 'LA & TA Demo', 
        "Dashboard includes both LA widgets and TA widgets", true,
        [{"title":"LA Widget","WIDGET_KOC_NAME":"demo-la-widget","TILE_WIDTH":2},{"title":"TA Widget","WIDGET_KOC_NAME":"demo-ta-widget","TILE_WIDTH":2}]);       

        self.dbsArray.push(dsb0);
        self.dbsArray.push(dsb1);
        self.dbsArray.push(dsb2);
        self.dbsArray.push(dsb3);
        self.did=3;
        
        /*
        for (var self.did = 0; self.did < 100000; self.did++)
        {
            var _db = new DashboardModel( self.did, 'Dashboard'+self.did, 'Demo Dashboard'+self.did, [{name: 'Demo Wedgit'}, {name: 'Demo Wedgit'}, {name: 'Demo Wedgit'}]);
            self.dbsArray.push(_db);
        }*/

        self.pageSize = ko.observable(20);
        self.pagingDatasource = ko.observable(new oj.ArrayPagingDataSource(self.dbsArray));
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
            self.selectedDashboard(data.dashboard);
            self.confirmDialogModel.show("Delete Dashboard", "Delete", 
                         "Do you want to delete the selected dashboard '"+data.dashboard.name+"'?",
                         self.confirmDashboardDelete);
        };
        
        self.confirmDashboardDelete = function() {
            
            if ( !self.selectedDashboard() || self.selectedDashboard() === null ) return;
            
            var __id = (self.selectedDashboard())['id'];
            self.dbsArray = $.grep(self.dbsArray, function(o) {
                return o.id !== __id;
            });
            self.filterArray = $.grep(self.filterArray, function(o) {
                return o.id !== __id;
            });
            if (self.filterArray.length > 0)
            {
                self.pagingDatasource(new oj.ArrayPagingDataSource(self.filterArray));
            }
            else
            {
                self.pagingDatasource(new oj.ArrayPagingDataSource(self.dbsArray));
            }
        };
        
        self.createDashboardClicked = function()
        {
            self.createDashboardModel.clear();
            $( "#cDsbDialog" ).ojDialog( "open" );
        };
        
        self.exploreDataClicked = function()
        {
            window.open('http://slc00aeg.us.oracle.com:7201/emlacore/faces/core-logan-observation-search');
        };
        
        self.confirmDashboardCreate = function()
        {
            //self.addTab();
            $( "#cDsbDialog" ).ojDialog( "close" );
            
            ++self.did;
            //var _timeRangeFilter = false;
            //if (self.createDashboardModel.timeRangeFilterValue() == "ON") _timeRangeFilter=true;
            
            var _addeddb = new DashboardModel(self.did, self.createDashboardModel.name(), self.createDashboardModel.description());
            if (self.createDashboardModel.timeRangeFilterValue() == "ON")
            {
                _addeddb.includeTimeRangeFilter = true;
            }
//            self.dbsArray.unshift(_addeddb);
            self.dbsArray.push(_addeddb);
            self.pagingDatasource(new oj.ArrayPagingDataSource(self.dbsArray));
            _addeddb.openDashboard();
            /*
            var _param = "?name="+encodeURIComponent(self.createDashboardModel.name())+"&description="+encodeURIComponent(self.createDashboardModel.description());
            
            if (_timeRangeFilter === true) {
                _param = _param + "&includeTimeRangeFilter=true"
            }
            window.open(document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html'+_param);*/
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
            
            self.filterArray = $.grep(self.dbsArray, function(o) {
                if (!value || value.length <=0) return true; //no filter
                return _contains(o.name, value);
            });
            return self.filterArray;
        };
        
        self.updateDashboard = function (dsb)
        {
            var that = this, _did = dsb['dashboardId'];
            
            for (var _i = 0 ; _i < that.dbsArray.length; _i++)
            {
                if (_did === that.dbsArray[_i].id)
                {
                    that.dbsArray[_i].name = dsb.dashboardName;
                    that.dbsArray[_i].description = dsb.dashboardDescription;
                    that.dbsArray[_i].type = dsb.type;
                    that.dbsArray[_i].includeTimeRangeFilter = dsb.includeTimeRangeFilter;
                    that.dbsArray[_i].image = dsb.screenShot;
                    that.dbsArray[_i].widgets = dsb.widgets;
                    
                }
            }
        };
        
        self.getDashboard = function (id)
        {
            if (id !== 0 && !id) return null;
            for (var _i = 0 ; _i < self.dbsArray.length; _i++)
            {
                if (id === self.dbsArray[_i].id)
                {
                    return self.dbsArray[_i];
                }
            }
        };
        
        self.searchResponse = function (event, data)
        {
            //console.log("searchResponse: "+data.content.length);
            self.pagingDatasource(new oj.ArrayPagingDataSource(data.content));
        };
    };
    
    
    return {'ViewModel': ViewModel};
});
