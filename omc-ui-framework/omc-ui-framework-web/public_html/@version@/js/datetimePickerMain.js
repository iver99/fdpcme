/**
 * Example of Require.js boostrap javascript
 */


requirejs.config({
    // Path mappings for the logical module names
    paths: {
        'knockout': '../../libs/@version@/js/oraclejet/js/libs/knockout/knockout-3.3.0',
        'jquery': '../../libs/@version@/js/oraclejet/js/libs/jquery/jquery-2.1.3.min',
        'jqueryui-amd': '../../libs/@version@/js/oraclejet/js/libs/jquery/jqueryui-amd-1.11.4.min',
        'promise': '../../libs/@version@/js/oraclejet/js/libs/es6-promise/promise-1.0.0.min',
        'hammerjs': '../../libs/@version@/js/oraclejet/js/libs/hammer/hammer-2.0.4.min',
        'ojs': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.1.2/min',
        'ojL10n': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.1.2/ojL10n',
        'ojtranslations': '../../libs/@version@/js/oraclejet/js/libs/oj/v1.1.2/resources',
        'signals': '../../libs/@version@/js/oraclejet/js/libs/js-signals/signals.min',
        'crossroads': '../../libs/@version@/js/oraclejet/js/libs/crossroads/crossroads.min',
        'history': '../../libs/@version@/js/oraclejet/js/libs/history/history.iegte8.min',
        'text': '../../libs/@version@/js/oraclejet/js/libs/require/text',
	'uifwk': '/emsaasui/uifwk'
    },
    // Shim configurations for modules that do not expose AMD
    shim: {
        'jquery': {
            exports: ['jQuery', '$']
        },
        'crossroads': {
            deps: ['signals'],
            exports: 'crossroads'
        }
    },
    // This section configures the i18n plugin. It is merging the Oracle JET built-in translation 
    // resources with a custom translation file.
    // Any resource file added, must be placed under a directory named "nls". You can use a path mapping or you can define
    // a path that is relative to the location of this main.js file.
    config: {
        ojL10n: {
            merge: {
               // 'ojtranslations/nls/ojtranslations': 'resources/nls/dashboardsMsgBundle'
            }
        }
	,
        text: {
            useXhr: function (url, protocol, hostname, port) {
              // allow cross-domain requests
              // remote server allows CORS
              return true;
            }
          }
    },
    waitSeconds: 60
});




/**
 * A top-level require call executed by the Application.
 * Although 'ojcore' and 'knockout' would be loaded in any case (they are specified as dependencies
 * by the modules themselves), we are listing them explicitly to get the references to the 'oj' and 'ko'
 * objects in the callback
 */
require(['ojs/ojcore',
    'knockout',
    'jquery',
    'ojs/ojknockout',
    'ojs/ojchart'
],
        function (oj, ko, $) // this callback gets executed when all required modules are loaded
        {
            ko.components.register("date-time-picker", {
                viewModel: {require: "/emsaasui/uifwk/js/widgets/datetime-picker/js/datetime-picker.js"},
                template: {require: "text!/emsaasui/uifwk/js/widgets/datetime-picker/html/datetime-picker.html"}
            });

            function MyViewModel() {
                var self = this;
                var start = new Date(new Date() - 24 * 60 * 60 * 1000);
                var end = new Date();
                var dateTimeOption = {formatType: "datetime", dateFormat: "medium"};
                
                self.dateTimeConverter1 = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);
                
                self.start = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(start)));
                self.end = ko.observable(self.dateTimeConverter1.format(oj.IntlConverterUtils.dateToLocalIso(end)));
                self.timeParams1 = {
                    startDateTime: start,
                    endDateTime: end,
//                    hideTimeSelection: true,
                    callbackAfterApply: function (start, end) {
                        console.log(start);
                        console.log(end);
                        var appliedStart = oj.IntlConverterUtils.dateToLocalIso(start);
                        var appliedEnd = oj.IntlConverterUtils.dateToLocalIso(end);
                        self.start(self.dateTimeConverter1.format(appliedStart));
                        self.end(self.dateTimeConverter1.format(appliedEnd));
                        self.generateData(start, end);
                    }
                };
                
                self.lineSeriesValues = ko.observableArray();
                self.lineGroupsValues = ko.observableArray();

                self.generateData = function (start, end) {
                    var lineSeries = [];
                    var lineGroups = [];
                    var timeInterval, dateTimeDiff;
                    var startTmp, endTmp;

                    var dateTimeOption = {formatType: "datetime", dateFormat: "short"};
                    self.dateTimeConverter = oj.Validation.converterFactory("dateTime").createConverter(dateTimeOption);

                    var timeOption = {formatType: "time", timeFormat: "short"};
                    self.timeConverter = oj.Validation.converterFactory("dateTime").createConverter(timeOption);

                    dateTimeDiff = new Date(end).getTime() - new Date(start).getTime();
                    //time range is less than 1 hour
                    if (dateTimeDiff <= 60 * 60 * 1000) {
                        timeInterval = 60 * 1000;  //1 min
                    } else if (dateTimeDiff <= 24 * 60 * 60 * 1000) {
                        timeInterval = 60 * 60 * 1000; //60 min
                    } else {
                        timeInterval = 24 * 60 * 60 * 1000; //1 day
                    }

                    //groups
                    start = new Date(start).getTime();
                    end = new Date(end).getTime();

                    var day, tmp;
                    var n = 0;

                    if (timeInterval === 60 * 1000) {
//                        lineGroups.push(self.dateFormatter(self.startDateISO()).format1 + " " + self.startTime().slice(1));
                        lineGroups.push(self.dateTimeConverter.format(start));
                        n++;
                        while ((start + n * timeInterval) <= end) {
//                            lineGroups.push(oj.IntlConverterUtils.dateToLocalIso(new Date(start + n * timeInterval)).slice(11, 16));
                            lineGroups.push(self.timeConverter.format(start + n * timeInterval));
                            n++;
                        }
                    } else if (timeInterval === 60 * 60 * 1000) {
//                        lineGroups.push(self.dateFormatter(self.startDateISO()).format1 + " " + self.startTime().slice(1));
                        lineGroups.push(self.dateTimeConverter.format(start));
                        n++;
                        day = new Date(start).getDate();
                        while ((start + n * timeInterval) <= end) {
                            tmp = new Date(start + n * timeInterval);
                            if (tmp.getDate() === day) {
//                                lineGroups.push(oj.IntlConverterUtils.dateToLocalIso(tmp).slice(11, 16));
                                lineGroups.push(self.timeConverter.format(oj.IntlConverterUtils.dateToLocalIso(tmp)));
                            } else {
//                                lineGroups.push(self.dateFormatter(tmp).format1 + " " + oj.IntlConverterUtils.dateToLocalIso(tmp).slice(11, 16));
                                lineGroups.push(self.dateTimeConverter.format(oj.IntlConverterUtils.dateToLocalIso(tmp)));
                                day = tmp.getDate();
                            }
                            n++;
                        }
                    } else {
                        while ((start + n * timeInterval) <= end) {
//                            lineGroups.push(self.dateFormatter(start + n * timeInterval).format1);
                            lineGroups.push(self.dateTimeConverter.format(start + n * timeInterval));
                            n++;
                        }
                    }

                    //series
                    var seriesNames = ["p1", "p2", "p3"];
                    var seriesMax = [30, 50, 100];
                    var seriesNumber = seriesNames.length;
                    for (var i = 0; i < seriesNumber; i++) {
                        var max = seriesMax[i];
                        var itemsValues = [];
                        for (var j = 0; j < n; j++) {
                            itemsValues.push(Math.floor(Math.random() * max));
                        }
                        lineSeries.push({name: seriesNames[i], items: itemsValues});
                    }

                    self.lineSeriesValues(lineSeries);
                    self.lineGroupsValues(lineGroups);
                };

                self.generateData(self.timeParams1.startDateTime, self.timeParams1.endDateTime);
                
                self.adjustTime = function(start, end) {
                    var adjustedStart, adjustedEnd;
                    adjustedStart =start - 60*60*1000;
                    adjustedEnd = end.getTime()+ 60*60*1000;
                    return {
                        start: new Date(adjustedStart),
                        end: new Date(adjustedEnd)
                    };
                };
                self.timeParams2 = {
//                    startDateTime: "2015-05-17T00:00:00",
//                    endDateTime: "2015-05-16T13:00:00"
                      startDateTime: new Date(new Date() - 24 * 60 * 60 * 1000),
                      endDateTime: new Date(),
                      timePeriodsNotToShow: ["Last 90 days", "Last 30 days"], //an array of what not to show
                      customWindowLimit: 4*24*60*60*1000-12*60*60*1000, //in custom mode, limit the size of window
                      adjustLastX: self.adjustTime, //used to adjust times when user choose "Last X"
                      customTimeBack: 7*24*60*60*1000, //the max timestamp of how far the user can pick the date from
                      hideMainLabel: true, //hides the main label "Time Range", defaults to "false"
                      hideRangeLabel: true //hides the selected time range, like "Last 30 minutes" inside the time selection, defaults to "false"
//                      hideTimeSelection: true //hides time selection
                };
            }
            ko.applyBindings(new MyViewModel(), document.getElementById("dateTimePicker"));
        }
);
