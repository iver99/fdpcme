define([
    'knockout',
    'ojs/ojcore',
    'jquery',
    'builder/right-panel/right.panel.util',
    'uifwk/js/util/screenshot-util',
    'dfutil',    
    'emsaasui/emcta/ta/js/sdk/tgtsel/api/TargetSelectorUtils'
    ], function(ko, oj, $, rpu, ssu, dfu, TargetSelectorUtils) {
        
        function RightPanelFilterModel($b) {
            var self = this;
            
            self.dashboard = $b.dashboard;
            self.rightPanelUtil = new rpu.RightPanelUtil();
            
            var defaultSettings = {
                    tsel:
                        {entitySupport: "byCriteria", entityContext: ""},
                    timeSel:
                        {defaultValue: "last14days", start: "", end: ""},
                    autoRefresh:
                        {defaultValue: 300000}
            };
            self.extendedOptions = self.dashboard.extendedOptions ? JSON.parse(self.dashboard.extendedOptions()) : defaultSettings;

            //set entity support/selectionMode
            self.extendedOptions.tsel.entitySupport && $b.getDashboardTilesViewModel && $b.getDashboardTilesViewModel().selectionMode(self.extendedOptions.tsel.entitySupport);

            self.defaultEntityContext = ko.observable(self.extendedOptions.tsel.entityContext);
            self.defaultTimeRangeValue = ko.observable([self.extendedOptions.timeSel.defaultValue]);
            var endTimeNow = new Date().getTime();
            self.defaultStartTime = ko.observable(parseInt(self.extendedOptions.timeSel.start===""? (""+endTimeNow-14*24*3600*1000):self.extendedOptions.timeSel.start));
            self.defaultEndTime = ko.observable(parseInt(self.extendedOptions.timeSel.end===""? (""+endTimeNow):self.extendedOptions.timeSel.end));
            
            self.enableEntityFilter = ko.observable((self.dashboard.enableEntityFilter() === 'TRUE')?'ON':'OFF');
            self.enableTimeRangeFilter = ko.observable(self.dashboard.enableTimeRange && (self.dashboard.enableTimeRange() === 'TRUE'?'ON':'OFF'));

            self.entitySupport = ko.observable(true);
            if($b.getDashboardTilesViewModel) {
                if($b.getDashboardTilesViewModel().selectionMode()==="byCriteria") {
                    self.entitySupport(true);
                }else {
                    self.entitySupport(false);
                }
            }
            
            self.enableEntityFilter.subscribe(function(val){
                self.dashboard.enableEntityFilter((val==='ON') ? 'TRUE' : 'FALSE');
                $b.getDashboardTilesViewModel && $b.getDashboardTilesViewModel().timeSelectorModel.timeRangeChange(true);
            });
            
            self.enableTimeRangeFilter.subscribe(function(val){
                self.dashboard.enableTimeRange((val==='ON') ? 'TRUE' : 'FALSE');
                $b.getDashboardTilesViewModel && $b.getDashboardTilesViewModel().timeSelectorModel.timeRangeChange(true);
            });
            
            //reset default entity value and entity context when entity support is changed
            self.entitySupport.subscribe(function(val) {
                val = val?"byCriteria":"single";
                self.extendedOptions.tsel.entitySupport = val;
                window.selectedDashboardInst().tilesViewModel.selectionMode(val);

            });

            //default auto-refresh and filter settings displayed in share section
            self.defaultAutoRefreshValue = ko.observable("every5minutes");
            if(self.extendedOptions.autoRefresh) {
                if(parseInt(self.extendedOptions.autoRefresh.defaultValue) === 0) {
                    self.defaultAutoRefreshValue("off");
                }else if(parseInt(self.extendedOptions.autoRefresh.defaultValue) === 300000) {
                    self.defaultAutoRefreshValue("every5minutes");
                }
            }else {
                self.extendedOptions.autoRefresh = {defaultValue: 300000};
            }

            self.defaultAutoRefreshValueText = ko.computed(function() {
                if(self.defaultAutoRefreshValue() === "off") {
                    return getNlsString("DBS_BUILDER_AUTOREFRESH_OFF");
                }else if(self.defaultAutoRefreshValue() === "every5minutes") {
                    return getNlsString("DBS_BUILDER_AUTOREFRESH_ON");
                }
            });
            
            self.defaultTimeRangeValueText = ko.computed(function() {
                if((self.defaultTimeRangeValue()[0] !== "custom") && (self.defaultTimeRangeValue()[0] !== "custom1")) {
                    return self.rightPanelUtil.getDefaultTimeRangeValueText(self.defaultTimeRangeValue()[0]);
                }else {
                    return self.rightPanelUtil.getTimeInfo(self.defaultStartTime(), self.defaultEndTime());
                }
            });

            self.defaultEntityValueText = ko.observable(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
            self.labelInited = false;
            self.defaultEntityValueChanged = ko.computed(function() {
                if(!self.dashboard.sharePublic() || !self.labelInited) {
                    var val = self.defaultEntityContext();

                    if(!val) {
                        self.defaultEntityValueText(getNlsString("DBS_BUILDER_ALL_ENTITIES"));
                        return getNlsString("DBS_BUILDER_ALL_ENTITIES");
                    }

                    var tselId = "tsel_"+self.dashboard.id();
                    var label;
                    var labelIntervalId = setInterval(function() {
                        if(self.labelInited) {
                            clearInterval(labelIntervalId);
                        }
                       if($("#"+tselId).children().get(0) && ko.contextFor($('#' + tselId).children().get(0)).$component.cm.dropdownInitialLabel()) {
                            label =  TargetSelectorUtils.getDropdownLabelForContext(tselId, val);
                            self.labelInited = true;
                        }else {
                            label = getNlsString("DBS_BUILDER_ALL_ENTITIES");
                        }
                        self.defaultEntityValueText(label);
                        return label;
                    }, 500);
                }
            });
            
            self.defaultValueChanged = ko.observable(new Date());
            //handle with auto-saving of filter setting in right drawer
            self.dsbRtDrFiltersSaveDelay = ko.computed(function() {
                return self.enableEntityFilter() + self.entitySupport() + self.defaultEntityContext() +
                        self.enableTimeRangeFilter() + self.defaultTimeRangeValue() + self.defaultValueChanged();
            });

            self.dsbRtDrFiltersSaveDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 800}});

            self.dsbRtDrFiltersSaveDelay.subscribe(function() {
                if(self.dashboard.systemDashboard() || self.dashboard.owner() !== dfu.getUserName()) {
                    console.log("This is an OOB dashboard or the current user is not owner of the dashboard");
                    return;
                }
                var fieldsToUpdate = {
                    "enableEntityFilter": self.dashboard.enableEntityFilter(),
                    "extendedOptions": JSON.stringify(self.extendedOptions),
                    "enableTimeRange": self.dashboard.enableTimeRange()
                };

                if (self.dashboard.tiles() && self.dashboard.tiles().length > 0) {
                    var elem = $(".tiles-wrapper:visible");
                    var clone = Builder.createScreenshotElementClone(elem);
                    ssu.getBase64ScreenShot(clone, 314, 165, 0.8, function(data) {
                        Builder.removeScreenshotElementClone(clone);
                        self.dashboard.screenShot = ko.observable(data);
                        self.handleSaveDsbFilterSettings(fieldsToUpdate);
                    });
                }
                else {
                    self.dashboard.screenShot = ko.observable(null);
                    self.handleSaveDsbFilterSettings(fieldsToUpdate);
                }
            });

            self.handleSaveDsbFilterSettings = function(fieldsToUpdate) {
                self.saveDsbFilterSettings(fieldsToUpdate, function() {
                    if(!self.dashboard.extendedOptions) {
                        self.dashboard.extendedOptions = ko.observable();
                    }
                    self.dashboard.extendedOptions(JSON.stringify(self.extendedOptions));
                },
                function() {
                    console.log("Error occurred when handle saving dashboard filter settings");
                });
            };

            self.saveDsbFilterSettings = function(fieldsToUpdate, succCallback, errorCallback) {
                var newDashboardJs = ko.mapping.toJS(self.dashboard, {
                    'include': ['screenShot', 'description', 'height',
                        'isMaximized', 'title', 'type', 'width',
                        'tileParameters', 'name', 'systemParameter',
                        'tileId', 'value', 'content', 'linkText',
                        'WIDGET_LINKED_DASHBOARD', 'linkUrl'],
                    'ignore': ["createdOn", "href", "owner", "modeWidth", "modeHeight",
                        "modeColumn", "modeRow", "screenShotHref", "systemDashboard",
                        "customParameters", "clientGuid", "dashboard",
                        "fireDashboardItemChangeEvent", "getParameter",
                        "maximizeEnabled", "narrowerEnabled",
                        "onDashboardItemChangeEvent", "restoreEnabled",
                        "setParameter", "shouldHide", "systemParameters",
                        "tileDisplayClass", "widerEnabled", "widget",
                        "WIDGET_DEFAULT_HEIGHT", "WIDGET_DEFAULT_WIDTH"]
                });

                $.extend(newDashboardJs, fieldsToUpdate);
                new Builder.DashboardDataSource().updateDashboardData(self.dashboard.id(), JSON.stringify(newDashboardJs), succCallback, errorCallback);
            };
            
            self.loadRightPanelFilter = function(tilesViewModel) {
                var dashboard = tilesViewModel.dashboard;
                if(!dashboard.extendedOptions) {
                    dashboard.extendedOptions = ko.observable("{\"tsel\": {\"entitySupport\": \"byCriteria\", \"entityContext\": \"\"}, \"timeSel\": {\"defaultValue\": \"last14days\", \"start\": 0, \"end\": 0}}");
                }
                self.dashboard = dashboard;
                var extendedOptions = JSON.parse(dashboard.extendedOptions());
                self.extendedOptions = extendedOptions ? extendedOptions : self.extendedOptions;
                var tsel = extendedOptions ? extendedOptions.tsel : {};
                var timeSel = extendedOptions ? extendedOptions.timeSel : {};
                //1. reset tsel in right drawer
                self.enableEntityFilter((dashboard.enableEntityFilter() === 'TRUE')?'ON':'OFF');
                self.entitySupport(tsel.entitySupport?(tsel.entitySupport==="byCriteria"?true:false):true);
                self.defaultEntityContext(tsel.entityContext ? tsel.entityContext : {});
                tilesViewModel.selectionMode(self.entitySupport()?"byCriteria":"single");
                window.DashboardWidgetAPI && window.DashboardWidgetAPI.setTargetSelectionContext(self.tilesViewModel.targets());
                //2. reset timeSel in right drawer
                self.enableTimeRangeFilter((dashboard.enableTimeRange() === 'TRUE')?'ON':'OFF');
                self.defaultTimeRangeValue([timeSel.defaultValue]);
                self.defaultStartTime(parseInt(timeSel.start));
                self.defaultEndTime(parseInt(timeSel.end));
            };
            
            self.setDefaultValuesWhenSharing = function (creatorExtendedOptions) {
                //creator settings is used as default dashboard settings when the dashboard is shared
                //set what to show for filters/auto-refresh in share area & set what filter/auto_refreh settings to save when this dashboard is shared
                //1. timeSel
                var timeSel = creatorExtendedOptions.timeSel;
                if(!$.isEmptyObject(timeSel)) {
                    self.defaultTimeRangeValue([timeSel.timePeriod]);
                    self.defaultStartTime(timeSel.start);
                    self.defaultEndTime(timeSel.end);
                    
                    self.extendedOptions.timeSel.start = timeSel.start;
                    self.extendedOptions.timeSel.end = timeSel.end;
                    self.extendedOptions.timeSel.defaultValue = timeSel.timePeriod;
                }
                //2. tsel
                var tsel = creatorExtendedOptions.tsel;
                if(!$.isEmptyObject(tsel)) {                    
                    self.defaultEntityContext(tsel.entityContext);
                    
                    self.extendedOptions.tsel.entityContext = tsel.entityContext;
                }
                //3. auto-refresh
                var interval = creatorExtendedOptions.autoRefresh.defaultValue;
                var intervalValue;
                if (interval === 0) {
                    intervalValue = "off";
                } else if (interval === 300000) {
                    intervalValue = "every5minutes";
                }
                self.defaultAutoRefreshValue(intervalValue);
                self.extendedOptions.autoRefresh.defaultValue = interval;

                self.defaultValueChanged(new Date());
            };
        }
        
        return {'RightPanelFilterModel': RightPanelFilterModel};
});