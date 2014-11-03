/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'ojs/ojcore', 
    'knockout', 
    'jquery', 
    'ojs/ojknockout', 
    'ojs/ojselectcombobox'
],
    function(oj, ko, $)
    {
        function VisualizationModel(params) {
            var self = this;
            var chartType = 'bar';

            self.parameters={};
            self.seriesValue = ko.observableArray();
            self.groupsValue = ko.observableArray();
            self.chartTypeValue=ko.observableArray();
            self.stackValue = ko.observable('off');
            self.orientationValue = ko.observable('vertical');
            self.plotBackground = ko.observable('#FAF0E6');
            var plotAreaInfo = {backgroundColor: ko.toJS(self.plotBackground)};
            self.plotAreaData = ko.observable(plotAreaInfo);
            
//            var param_chartType = getUrlParam('chartType');
//            if (param_chartType) {
//                chartType=param_chartType;
//            }

            if (params.chartType){
                chartType=params.chartType();
            }
            if (params.timeRangeChangeEvent){
                params.timeRangeChangeEvent.subscribe(function (value) {
                    if (value.viewStart){
                        self.parameters.startTime=value.viewStart;
                    }
                    if (value.viewEnd){
                        self.parameters.endTime=value.viewEnd;
                    }
                    refresh();
                });
            }
            
            var startTime = new Date();
            var param_startTime = getUrlParam('startTime');
            if (param_startTime) {
                startTime=param_startTime;
            }
            
            var endTime = null;
            var param_endTime = getUrlParam('endTime');
            if (param_endTime) {
                endTime=param_endTime;
            }
            
            self.parameters.chartType=chartType;
            self.parameters.startTime=startTime;
            self.parameters.endTime=endTime;
            refresh();
            
            self.refreshData = function() {
                refresh();
            };
            
            function getUrlParam(name) {
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];
            };
            
            function refresh() {
                if(self.chartTypeValue().length<=0)
                    self.chartTypeValue([self.parameters.chartType]);
                
                var size = 50;
                if (self.parameters.endTime !== null) {
                    var diff = self.parameters.endTime.getTime()-self.parameters.startTime.getTime();
                    var min = diff/(60*1000*60*24);
                    size = min/5;
                    //without at least 2 points, DVT will throw exception
                    if (size<=1){
                        size=0;
                    }
                }

                var tempDate = self.parameters.startTime;
                var series=[];
                var groups=[];
                var seriesItems = [];
                for (i = 0; i < size; i++) 
                {
                    var value = Math.floor(Math.random()*1000)+1;
                    seriesItems.push(value);
                    var groupvalue = addDate('2', 5, tempDate);
                    groups.push(groupvalue);
                    tempDate=groupvalue;
                }
                series.push({name: "Demo Series", items: seriesItems});
                
                self.seriesValue(series);
                self.groupsValue(groups);
            };
            
            function addDate(type,NumDay,vdate){
                var date=new Date(vdate);
                type = parseInt(type);
                var lIntval = parseInt(NumDay);
                switch(type){
                    case 6 :
                        date.setYear(date.getYear() + lIntval);
                        break;
                    case 7 :
                        date.setMonth(date.getMonth() + (lIntval * 3) );
                        break;
                    case 5 :
                        date.setMonth(date.getMonth() + lIntval);
                        break;
                    case 4 :
                        date.setDate(date.getDate() + lIntval);
                        break;
                    case 3 :
                        date.setHours(date.getHours() + lIntval);
                        break;
                    case 2 :
                        date.setMinutes(date.getMinutes() + lIntval);
                        break;
                    case 1 :
                        date.setSeconds(date.getSeconds() + lIntval);
                        break;
                    default:
                } 
                
                return date;
            }
        };

//        return {'VisualizationModel': VisualizationModel};
        return VisualizationModel;
    });


