/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'knockout', 
    'jquery'
],
    function(ko, $)
    {
        function DemoTargetAnalytics(params) {
            var self = this;
            var startTime = params.tile.timeRangeStart ? params.tile.timeRangeStart : null; 
            var endTime = params.tile.timeRangeEnd ? params.tile.timeRangeEnd : null; 
            var queryString = {"ast":{"query":"simple",
                        "select":[{"item":{"expr":"function","name":"COUNT",
                                           "args":[{"expr":"column","table":"me","column":"meId"}]}},
                                  {"item":{"expr":"column","table":"met","column":"category"},"alias":null},
                                  {"item":{"expr":"column","table":"me","column":"availabilityStatus"},"alias":null}],
                        "from":[{"table":"virtual","name":"Target","alias":"me"},
                                {"table":"virtual","name":"ManageableEntityType","alias":"met"}],
                        "where":{"cond":"compare","comparator":"EQ",
                                 "lhs":{"expr":"column","table":"me","column":"entityType"},
                                 "rhs":{"expr":"column","table":"met","column":"entityType"}},
                        "groupBy":{"items":[{"expr":"column","table":"met","column":"category"},
                                            {"expr":"column","table":"me","column":"availabilityStatus"}]},
                        "orderBy":{"entries":[{"entry":"expr","item":{"expr":"column","table":"met","column":"category"}}]}
                       }
                };
                
//            var widget = params.tile.widget;
//            if (widget && widget.id) {
//                var ssfUrl = dfu.discoverSavedSearchServiceUrl();
//                if (ssfUrl && ssfUrl !== '') {
//                    var searchUrl = ssfUrl + '/search/'+widget.id;
//                    $.ajax({
//                        url: searchUrl,
//                        success: function(data, textStatus) {
//                            queryString = data.queryStr;
//                        },
//                        error: function(xhr, textStatus, errorThrown){
//                            console.log('Error when querying target analytics saved searches!');
//                        },
//                        async: false
//                    });
//                }
//            }
            
            self.seriesValue = ko.observableArray();
            self.groupsValue = ko.observableArray();
//            var taUrl = 'http://slc08vnv.us.oracle.com:7001/targetmodel/0/query/api/v0.1/query';
//            var taUrl = 'http://slc08upg.us.oracle.com:7001/targetmodel/0/query/api/v0.1/query';
            var taUrl = 'http://slc07ptu.us.oracle.com:7001/targetmodel/0/query/api/v0.1/query';
            
            fetchResults();
            
            params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                if (dashboardItemChangeEvent){
                    if (dashboardItemChangeEvent.timeRangeChange){
                        startTime = dashboardItemChangeEvent.timeRangeChange.viewStartTime;//();
                        endTime = dashboardItemChangeEvent.timeRangeChange.viewEndTime;//();
                        refresh(startTime, endTime);
                    }
                }
            };
            
            function refresh(startTime, endTime) {
                self.seriesValue([]);
                self.groupsValue([]);
                fetchResults();
            };
            
            function fetchResults() {
                $.ajax({type: 'POST', contentType:'application/json',url: taUrl, data: ko.toJSON(queryString),
                    success: function(data, textStatus){
                        querySuccessCallBack(data);
                    },
                    error: function(data, textStatus){
                        var msg = data.statusText+': ' + data.responseText;
                        console.log("Error when fetching data: \n" + msg); 
                    }
                });  
            };
            
            function querySuccessCallBack(data) {
                var results = data.rows;
                var groups = [];
                var status = [];
                var tempGrp = '';
                for (var i=0; i < results.length; i++) {
                    if (results[i][1] !== tempGrp) {
                        groups.push(results[i][1]);
                        tempGrp = results[i][1];
                    }
                    if (!contains(status, results[i][2])) {
                        status.push(results[i][2]);
                    }
                }
                tempGrp = '';  
                var itemSize = status.length;
                var items = [];
                var series = []; 
                var seriesItemName = '';
                for (var j=0; j < results.length; j++) {
                    if (results[j][1] !== tempGrp) {
                        if (j !== 0) {
                            var seriesItem = {name: seriesItemName, values: items};
                            series.push(seriesItem);
                        }
                        items=[]; 
                        seriesItemName = results[j][1];
                        for (var k=0; k < itemSize; k++) {
                            items[k] = 0;
                        }
                        tempGrp=seriesItemName;
                    }
                    items[getItemIndex(status, results[j][2])] = results[j][0];
                }

                series.push({name: seriesItemName, values: items});
                var finalSeries = []; 
                for (var m = 0; m < status.length; m++) {
                    var seriesData = [groups.length];
                    for (var n = 0; n < groups.length; n++) {
                        seriesData[n]=series[n].values[m];
                    }
                    finalSeries.push({name: (status[m] === null ? 'N/A':status[m]), items: seriesData}); 
                } 
                self.seriesValue(finalSeries); 
                self.groupsValue(groups);
            };
            
            function contains(array, item) { 
                var i = array.length; 
                while (i--) { 
                    if (array[i] === item) { 
                        return true; 
                    } 
                } 
                return false; 
            }; 

            function getItemIndex(itemList, item) { 
                //item = (item === null? 'N/A' : item);
                for (var i = 0; i < itemList.length; i++) {
                    if (itemList[i]===item) {
                        return i;
                    }
                }
                return 0;
            }; 
        };
        
        return DemoTargetAnalytics;
    });

