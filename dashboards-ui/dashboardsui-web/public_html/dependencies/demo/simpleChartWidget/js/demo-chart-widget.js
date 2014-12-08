/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'knockout', 
    'jquery',
    'dfutil'
],
    function(ko, $, dfu)
    {
        function VisualizationModel(params) {
            var self = this;
            var randomInt = dfu.getRandomInt(1,2);
            var chartType = 'bar';
            if (randomInt===2){
                chartType = "line";
            }

            self.parameters={};
            self.seriesValue = ko.observableArray();
            self.groupsValue = ko.observableArray();
            self.chartTypeValue=ko.observableArray();
            self.stackValue = ko.observable('off');
            self.orientationValue = ko.observable('vertical');
//            self.plotBackground = ko.observable('#FAF0E6');
            self.plotBackground = ko.observable('#FFFFFF');
            var plotAreaInfo = {backgroundColor: ko.toJS(self.plotBackground)};
            self.plotAreaData = ko.observable(plotAreaInfo);

            if (params.tile.chartType){
                chartType=params.tile.chartType();
            }
            var startTime = new Date(); 
            var endTime = null;
            params.tile.onDashboardItemChangeEvent = function(dashboardItemChangeEvent){
                if (dashboardItemChangeEvent){
                    if (dashboardItemChangeEvent.timeRangeChange){
                        startTime = dashboardItemChangeEvent.timeRangeChange.viewStartTime;//();
                        endTime = dashboardItemChangeEvent.timeRangeChange.viewEndTime;//();
                        refresh0(chartType,startTime,endTime);
                    }
                }
            }
            

            
            self.parameters.chartType=chartType;
            self.parameters.startTime=startTime;
            self.parameters.endTime=endTime;
            refresh();
            
            self.refreshData = function() {
                refresh();
            };
            
       
            function refresh(){
                refresh0(self.parameters.chartType, self.parameters.startTime, self.parameters.endTime);
            }
            function refresh0(chartType, startTime, endTime) {
                if(self.chartTypeValue().length<=0)
                    self.chartTypeValue([chartType]);
                
                var size = 50;
                if (endTime !== null) {
                    var diff = endTime.getTime()-startTime.getTime();
                    var min = diff/(60*1000*60*24);
                    size = min/5;
                    //without at least 2 points, DVT will throw exception
                    if (size<=1){
                        size=0;
                    }
                }

                var tempDate = startTime;
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
                series.push({name: "Demo Series", items: seriesItems,color:'#3CB371',bordercolor:'#FFF'});
                
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


