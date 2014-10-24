/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['ojall', 'knockout', 'jquery', 'itacore',
    'timeslider/time-slider-model',
    'text!/emcpdfui/dependencies/timeslider/time-slider.html',
    'ojs/ojknockout', 'ojs/ojcomponents', 'ojs/ojchart', 'jqueryui192', 'ojs/ojselectcombobox'], function(oj, ko, $, ita, TimeSliderModel,timeSliderTemplate) {

    ita.registerTool(
            {
                name: 'time-slider',
                init: function(element, valueAccessor, allBindings, viewModel, bindingContext) {
                    var vm = valueAccessor().timeSliderModel || new TimeSliderModel();
                    var $container = $(element);
                    $container.append(timeSliderTemplate);
                    $.getJSON('/emcpdfui/dependencies/timeslider/js/time-dimension.json', function(resp) {
                        var hierArray = [];
                        var _timeDim = resp;
                        var _timeHierarchies = _timeDim.hierarchies;
                        var _timeLevels = _timeDim.levels;
                        var levelMap = {};

                        $.each(_timeLevels, function () {
                            levelMap[this.name] = this.displayName;
                        });
                        $.each(_timeHierarchies, function () {
                            var timeHier = {};
                            timeHier['name'] = this.name;
                            timeHier['displayName'] = this.displayName;
                            timeHier['levels'] = [];
                            $.each(this.levels, function () {
                                timeHier.levels.push({name: this.levelName, displayName: levelMap[this.levelName]});
                            });
                            hierArray.push(timeHier);
                        });

                        vm.timeHierarchies(hierArray);
                        vm.timeLevels = ko.computed(function () {
                            var levels = ko.observableArray([]);
                            $.each(vm.timeHierarchies(), function () {
                                if (this.name === vm.timeHierarchy()[0]) {
                                    levels(this.levels);
                                    return false;
                                }
                            });
                            vm.timeLevel([levels()[0].name]);
                            return levels();
                        });
                        renderSlider();
                    });

                    function renderSlider(){
                        ko.applyBindings(vm, $(".time-filter")[0]);

                        var $sliderBar = $container.find(".slider-bar");
                        $sliderBar.slider({
                            range: true,
                            min: vm.totalStart().getTime(),
                            max: vm.totalEnd().getTime(),
                            values: [vm.viewStart().getTime(), vm.viewEnd().getTime()],
                            stop: function(event, ui) {
                                var values = $(this).slider('values');
                                var newStart = values[0];
                                if (newStart !== vm.viewStart().getTime()) {
                                    vm.viewStart(new Date(newStart));
                                }

                                var newEnd = values[1];
                                if (newEnd !== vm.viewEnd().getTime()) {
                                    vm.viewEnd(new Date(newEnd));
                                }
                                vm.timeRangeViewChange(true);
                            },
                            slide: function(event, ui) {
                                var values = $(this).slider("values");
                                vm.scrollStart(new Date(values[0]));
                                vm.scrollEnd(new Date(values[1]));
                            },
                            start: function(event, ui) {
                                // if mousedown on the slider-range ,event canceld
                                if (event.toElement === $sliderBar.find('.ui-slider-range')[0]) {
                                    event.preventDefault();
                                }
                            }
                        });
                        var $zoomInBtn = $('.slider-bar-zoom >.slider-bar-zoom-in');
                        $zoomInBtn.click(function() {
                            var values = $sliderBar.slider("values");
                            var min = $sliderBar.slider("option", "min");
                            var max = $sliderBar.slider("option", "max");
                            var zoomInScale = 24 * 3600 * 1000;
                            var newValues = new Array();
                            newValues[0] = (min < values[0] - zoomInScale ? values[0] - zoomInScale : min);
                            newValues[1] = (max > values[1] + zoomInScale ? values[1] + zoomInScale : max);
                            $sliderBar.slider('values', [newValues[0], newValues[1]]);
                            vm.scrollStart(new Date(newValues[0]));
                            vm.scrollEnd(new Date(newValues[1]));
                            vm.viewStart(new Date(newValues[0]));
                            vm.viewEnd(new Date(newValues[1]));
                            vm.timeRangeViewChange(true);
                        });
                        var $zoomOutBtn = $('.slider-bar-zoom > .slider-bar-zoom-out');
                        $zoomOutBtn.click(function() {
                            var values = $sliderBar.slider("values");
                            var zoomOutScale = 24 * 3600 * 1000;
                            var newValues = new Array();
                            if (values[1] - values[0] <= zoomOutScale) {
                                newValues[0] = values[0];
                                newValues[1] = values[1];
                            } else if (values[1] - values[0] <= 2 * zoomOutScale) {
                                newValues[0] = values[0] + zoomOutScale;
                                newValues[1] = values[1];
                            } else {
                                newValues[0] = values[0] + zoomOutScale;
                                newValues[1] = values[1] - +zoomOutScale;
                            }
                            $sliderBar.slider('values', [newValues[0], newValues[1]]);
                            vm.scrollStart(new Date(newValues[0]));
                            vm.scrollEnd(new Date(newValues[1]));
                            vm.viewStart(new Date(newValues[0]));
                            vm.viewEnd(new Date(newValues[1]));
                            vm.timeRangeViewChange(true);
                        });

                        // constants
                        var MONTH_NAMES = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
                        var HOUR = 60 * 60 * 1000;
                        var DAY = 24 * HOUR;
                        var WEEK = 7 * DAY;
                        var MONTH = 30 * DAY;
                        var YEAR = 365 * DAY;

                        var $handle0 = $('.slider-bar > .ui-slider-handle').eq(0);
                        var $handle1 = $('.slider-bar > .ui-slider-handle').eq(1);

                        $handle0.mouseover(function() {
                            var startDate = vm.scrollStart();
                            $handle0.attr('title', MONTH_NAMES[startDate.getMonth()] + ' ' + startDate.getDate() + ',' + startDate.getFullYear());
                        });
                        $handle1.mouseover(function() {
                            var endDate = vm.scrollEnd();
                            $handle1.attr('title', MONTH_NAMES[endDate.getMonth()] + ' ' + endDate.getDate() + ',' + endDate.getFullYear());
                        });

                        var $range = $('.ui-slider-range');
                        $range.draggable({
                            axis: 'x',
                            containment: $sliderBar,
                            drag: function(event, ui) {
                                var thiz = this;
                                //Compute  handles' new position
                                var newOffsetLeft = ui.position.left;
                                var rangeWidth = $(thiz).width();
                                $handle0.css({left: newOffsetLeft});
                                $handle1.css({left: newOffsetLeft + rangeWidth});
                                var values = $sliderBar.slider("values");

                                //Compute slider's new values;
                                var interval = values[1] - values[0];
                                var min = $sliderBar.slider("option", "min");
                                var max = $sliderBar.slider("option", "max");
                                var weight = (max - min) / $sliderBar.width();
                                values[0] = min + $handle0.position().left * weight;
                                values[1] = values[0] + interval;
                                var newValues = new Array();

                                //in addition to  containment  limitation
                                if (values[1] >= max) {
                                    newValues[1] = max;
                                    newValues[0] = max - interval;
                                } else if (values[0] <= min) {
                                    newValues[0] = min;
                                    newValues[1] = min + interval;
                                } else {
                                    newValues[0] = values[0];
                                    newValues[1] = values[1];
                                }
                                // map slide event in slider 
                                $sliderBar.slider('values', [newValues[0], newValues[1]]);
                                $sliderBar.slider('option', 'slide').call($sliderBar);
                            },
                            stop: function(event, ui) {
                                // map stop event in slider 
                                $sliderBar.slider('option', 'stop').call($sliderBar);
                                vm.timeRangeViewChange(true);
                            }
                        });

                        var $fixedRelativeTime = $('.fixed-relative-time');
                        $fixedRelativeTime.click(function () {
                            var relativeTimeString = $(this).attr('title');
                            var now = new Date();
                            vm.totalEnd(new Date());
                            switch (relativeTimeString) {
                                case 'Last One Year':
                                    vm.totalStart(new Date(now.setYear(now.getFullYear() - 1)));
                                    break;
                                case 'Last Six Month':
                                    vm.totalStart(new Date(now.setMonth(now.getMonth() - 6)));
                                    break;
                                case 'Last Three Month':
                                    vm.totalStart(new Date(now.setMonth(now.getMonth() - 3)));
                                    break;
                                case 'Last Four Week':
                                    vm.totalStart(new Date(now.getFullYear(), now.getMonth(), now.getDate() - 4 * 7));
                                    break;
                                case 'Last One Week':
                                    vm.totalStart(new Date(now.getFullYear(), now.getMonth(), now.getDate() - 1 * 7));
                                    break;
                                    //default:
                            }
                            vm.timeRangeChange(true);

                        });

                        var $toggleOptions = $('.toggleOptions');
                        $toggleOptions.click(function() {
                            if ($(this).text() === '>>') {
                                $('.advanced-options > div').css('display', 'inline-block').show();
                                $('.advanced-options > span').html('Advanced :');
                                $(this).text('<<');
                            } else {
                                $('.advanced-options > div ').css('display', 'none');
                                $('.advanced-options > span').html('Advanced ');
                                $(this).text('>>');
                            }
                        });

                        var $updateButton = $('.update-advanced-options > input').eq(0);
                        $updateButton.click(function() {
                            var now = new Date();
                            var timeValue = vm.relativeTimeValue();
                            var timeUnit = vm.relativeTimeUnit()[0];
                            vm.totalEnd(new Date());
                            switch (timeUnit) {
                                case 'Year':
                                    vm.totalStart(new Date(now.setYear(now.getFullYear() - timeValue)));
                                    break;
                                case 'Month':
                                    vm.totalStart(new Date(now.setMonth(now.getMonth() - timeValue)));
                                    break;
                                case 'Week':
                                    vm.totalStart(new Date(now.getFullYear(), now.getMonth(), now.getDate() - timeValue * 7));
                                    break;
                                case 'Day' :
                                    vm.totalStart(new Date(now.getFullYear(), now.getMonth(), now.getDate() - timeValue));
                                    break;
                                case 'Hour':
                                    vm.totalStart(new Date(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours() - timeValue));
                                    break;
                            }
                            vm.advancedOptionsChange(true);
                        });

                        var $splitterBar = $container.find(".splitter-bar");
                        var $scaleBar = $container.find(".scale-bar");
                        var drawSplitterScale = function() {

                            function getStep(month, day, year) {
                                var step = null;
                                var dayOfMonth = null;
                                switch (month) {
                                    case 1:
                                    case 3:
                                    case 5:
                                    case 7:
                                    case 8:
                                    case 10:
                                    case 12:
                                        dayOfMonth = 31;
                                        break;
                                    case 4:
                                    case 6:
                                    case 9:
                                    case 11:
                                        dayOfMonth = 30;
                                        break;
                                    case 2:
                                        dayOfMonth = ((year % 4 === 0 && year % 100) || (year % 400 === 0)) ? 29 : 28; // year must be provided
                                        break;
                                        // default:
                                }
                                if (!day) {
                                    step = dayOfMonth;
                                } else {
                                    step = dayOfMonth - day + 1;
                                }

                                if (step <= 0) {
                                    throw new Error('Erroe :step value should large than  0 ');
                                }
                                return step;
                            }
                            // Clean the two containers in case a redraw is triggered. 
                            $splitterBar.empty();
                            $scaleBar.empty();

                            // Calculate the start distance and start date align at day.
                            var dVal = vm.totalEnd().getTime() - vm.totalStart().getTime();
                            var splitterDistance = $scaleBar.width() * (DAY / dVal);
                            var startDate = Math.ceil(vm.totalStart().getTime() / DAY) * DAY;
                            var startPos = (startDate - vm.totalStart().getTime()) / DAY * splitterDistance;

                            // Calculate the step in case there will be too many scales to draw.
                            var step = 1;
                            var level = null;

                            if (dVal < 2 * DAY) {
                                if(dVal < 0 ) {
                                    console.log('warning error error !!!');
                                }
                                if (dVal === 0) {
                                    dVal = 1 * DAY;
                                    splitterDistance = $scaleBar.width() * (DAY / dVal);
                                }
                                var splitterDistanceByHour = $scaleBar.width() * (HOUR / dVal);
                                startDate = Math.ceil(vm.totalStart().getTime() / HOUR) * HOUR;
                                startPos = (startDate - vm.totalStart().getTime()) / HOUR * splitterDistanceByHour;
                                level = 'HOUR';
                                step = 1 / 24;

                            } else if (dVal < 4 * WEEK) {
                                level = 'DAY';
                                step = 1;

                            } else if (dVal < 3 * MONTH) {
                                level = 'MONTH_3';
                                step = 4;

                            } else if (dVal < 6 * MONTH) {
                                level = 'MONTH_6';
                                step = 7;

                            } else if (dVal < YEAR) {
                                level = 'YEAR_1';
                                step = 15;

                            } else if (dVal < YEAR * 2) {
                                //step will be computed 
                                level = 'YEAR_2';

                            } else if (dVal < YEAR * 5) {
                                level = 'YEAR_5';

                            } else if (dVal < YEAR * 10) {
                                level = 'YEAR_10';

                            } else {
                                level = 'YEAR_N';
                            }

                            var lastTimeObj = -1;
                            for (var currentPos = startPos, currentTime = startDate;
                                    currentPos <= $scaleBar.width();
                                    currentPos += splitterDistance * step, currentTime += DAY * step) {
                                // Draw the splitter.        
                                var $splitter = $("<span></span>").css("left", currentPos + "px");
                                $splitterBar.append($splitter);
                                var currentTimeObj = new Date(currentTime);

                                // Calculate the scale text. 
                                var scaleText = '';

                                switch (level) {
                                    case 'HOUR' :
                                        if (lastTimeObj === -1 || (currentTimeObj.getDate() !== lastTimeObj.getDate())) {
                                            scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate() + ":" + currentTimeObj.getHours();
                                            lastTimeObj = currentTimeObj;
                                        } else {
                                            scaleText = currentTimeObj.getHours();
                                        }

                                        break;
                                    case 'DAY' :
                                    case 'MONTH_3':
                                    case 'MONTH_6' :
                                    case 'YEAR_1':
                                        // If it is the first time loop into the month,
                                        // draw the date with the month prefix. Like: 'Apr 1'.
                                        if (lastTimeObj === -1 || (currentTimeObj.getMonth() !== lastTimeObj.getMonth())) {
                                            scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate();
                                            lastTimeObj = currentTimeObj;
                                        } else {
                                            scaleText = currentTimeObj.getDate();
                                        }

                                        break;
                                    case 'YEAR_2':
                                        if (lastTimeObj === -1 || (currentTimeObj - lastTimeObj > 31 * DAY)) {
                                            scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate() + "," + currentTimeObj.getFullYear();
                                            lastTimeObj = currentTimeObj;
                                        } else {
                                            scaleText = '';
                                        }

                                        step = getStep(currentTimeObj.getMonth() + 1, currentTimeObj.getDate(), currentTimeObj.getFullYear());

                                        break;
                                    case 'YEAR_5':
                                        if (lastTimeObj === -1 || (currentTimeObj - lastTimeObj > 2 * 31 * DAY)) {
                                            if (lastTimeObj === -1 || lastTimeObj.getFullYear() === currentTimeObj.getFullYear()) {
                                                scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate();
                                            } else {
                                                scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate() + "," + currentTimeObj.getFullYear();
                                            }
                                            lastTimeObj = currentTimeObj;
                                        } else {
                                            scaleText = '';
                                        }

                                        step = getStep(currentTimeObj.getMonth() + 1, currentTimeObj.getDate(), currentTimeObj.getFullYear());

                                        break;
                                    case 'YEAR_10':
                                        if (lastTimeObj === -1 || (currentTimeObj - lastTimeObj > 5 * 31 * DAY)) {
                                            if (lastTimeObj === -1 || lastTimeObj.getFullYear() === currentTimeObj.getFullYear()) {
                                                scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate();
                                            } else {
                                                scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate() + "," + currentTimeObj.getFullYear();
                                            }
                                            lastTimeObj = currentTimeObj;
                                        } else {
                                            scaleText = '';
                                        }

                                        // calculate next step: 3 months later
                                        var month = currentTimeObj.getMonth() + 1;
                                        var year = currentTimeObj.getFullYear();
                                        step = getStep(month, currentTimeObj.getDate(), year);
                                        for (var i = 1; i < 3; i++) {
                                            month = month + 1;
                                            if (month > 12) {
                                                month = month - 12;
                                                year = year + 1;
                                            }
                                            step += getStep(month, 1, year);
                                        }

                                        break;
                                    case 'YEAR_N':
                                        if (lastTimeObj === -1 || (currentTimeObj - lastTimeObj > 11 * 31 * DAY)) {
                                            scaleText = MONTH_NAMES[currentTimeObj.getMonth()] + " " + currentTimeObj.getDate() + "," + currentTimeObj.getFullYear();
                                            lastTimeObj = currentTimeObj;
                                        } else {
                                            scaleText = '';
                                        }

                                        // calculate next step: 6 months later
                                        var month = currentTimeObj.getMonth() + 1;
                                        var year = currentTimeObj.getFullYear();
                                        step = getStep(month, currentTimeObj.getDate(), year);
                                        for (var i = 1; i < 6; i++) {
                                            month = month + 1;
                                            if (month > 12) {
                                                month = month - 12;
                                                year = year + 1;
                                            }
                                            step += getStep(month, 1, year);
                                        }
                                        break;
                                }

                                // Draw the scale.
                                var $scale = $("<span></span>").text(scaleText);
                                $scaleBar.append($scale);
                                // Adjust the scale to align to the middle of the splitter.
                                $scale.css("left", currentPos - $scale.width() / 2 + "px");
                            }
                        };

                        drawSplitterScale();
                        // Redraw the slider when browser window is resized.
                        $(window).resize(function() {
                            drawSplitterScale();
                        });

                        function resetTimeSlider() {
                            $sliderBar.slider("option", "min", vm.totalStart().getTime());
                            $sliderBar.slider("option", "max", vm.totalEnd().getTime());
                            drawSplitterScale();
                            vm.viewStart(vm.totalStart());
                            vm.viewEnd(vm.totalEnd());
                            vm.scrollStart(vm.totalStart());
                            vm.scrollEnd(vm.totalEnd());
                        }
                        vm.viewStart.subscribe(function(newVal) {
                            $sliderBar.slider("values", [newVal.getTime(), vm.viewEnd().getTime()]);
                        });
                        vm.viewEnd.subscribe(function(newVal) {
                            $sliderBar.slider("values", [vm.viewStart().getTime(), newVal.getTime()]);
                        });
                        
                        var $startInputDate = $('.total-start-selector input');
                        $startInputDate.on({
                            //triggers :messagesCustom messagesShown messagesHidden value changes
                            'ojoptionchange': function(event, data) {
                                //if change type is 'value' 
                                if (data['option'] === 'value') {
                                    var newValue = oj.IntlConverterUtils.isoToLocalDate(data.value);
                                    if ((Math.abs(vm.totalStart().getTime() - newValue.getTime()) > HOUR)) {
                                        vm.totalStart(newValue);
                                        vm.timeRangeChange(true);
                                    }
                                }
                            }
                        });

                        var $endInputDate = $('.total-end-selector input');
                        $endInputDate.on({
                            //triggers :messagesCustom messagesShown messagesHidden value changes
                            'ojoptionchange': function(event, data) {
                                //if change type is 'value' 
                                if (data['option'] === 'value') {
                                    var newValue = oj.IntlConverterUtils.isoToLocalDate(data.value);
                                    if ((Math.abs(vm.totalEnd().getTime() - newValue.getTime()) > HOUR)) {
                                        vm.totalEnd(newValue);
                                        vm.timeRangeChange(true);
                                    }
                                }
                            }
                        });
                        
                        vm.timeRangeChange.subscribe(function(changed){
                            if (changed === true) {
                               resetTimeSlider();
                            }
                        });
                        
                        vm.advancedOptionsChange.subscribe(function (changed) {
                            if (changed === true) {
                                resetTimeSlider();
                            }
                        });
                    }

                }
            }
    );
});