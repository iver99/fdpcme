define(['knockout',
'jquery',
'ojs/ojcore',
'dfutil'
],
function (ko, $, oj, dfu) {
    function rightPanelWidget($b, tilesViewModel) {
        var self = this;
        self.widgets = ko.observableArray([]);
        self.keyword = ko.observable('');
        self.clearRightPanelSearch = ko.observable(false);
        self.tilesViewModel = ko.observable(tilesViewModel);

        self.loadWidgets = function (req) {
            var widgetDS = new Builder.WidgetDataSource();

            widgetDS.loadWidgetData(
                    req && (typeof req.term === "string") ? req.term : self.keyword(),
                    function (widgets) {
                        self.widgets([]);
                        if (widgets && widgets.length > 0) {
                            for (var i = 0; i < widgets.length; i++) {
                                if (!widgets[i].WIDGET_DESCRIPTION) {
                                    widgets[i].WIDGET_DESCRIPTION = null;
                                }
                                var wgt = ko.mapping.fromJS(widgets[i]);
                                if (wgt && !wgt.WIDGET_VISUAL) {
                                    wgt.WIDGET_VISUAL = ko.observable('');
                                }
                                if (wgt && !wgt.imgWidth) {
                                    wgt.imgWidth = ko.observable('120px');
                                }
                                if (wgt && !wgt.imgHeight) {
                                    wgt.imgHeight = ko.observable('120px');
                                }
                                self.widgets.push(wgt);
                            }
                        }
                        self.initWidgetDraggable();
                    }
            );
        };

        self.getWidgetScreenshot = function (wgt) {
            var url = null;
            if (wgt.WIDGET_SCREENSHOT_HREF) {
                url = wgt.WIDGET_SCREENSHOT_HREF();
            }
            if (!dfu.isDevMode()) {
                url = dfu.getRelUrlFromFullUrl(url);
            }
            if (wgt && !wgt.WIDGET_VISUAL) {
                wgt.WIDGET_VISUAL = ko.observable('');
            }
            url && wgt.WIDGET_VISUAL(url);
            !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/no-image-available.png'));

            //resize widget screenshot according to aspect ratio
            dfu.getScreenshotSizePerRatio(120, 120, wgt.WIDGET_VISUAL(), function (imgWidth, imgHeight) {
                wgt.imgWidth(imgWidth + "px");
                wgt.imgHeight(imgHeight + "px");
            });
        };

        self.getWidgetBase64Screenshot = function (wgt) {
            var url = '/sso.static/savedsearch.widgets';
            if (dfu.isDevMode()) {
                url = dfu.buildFullUrl(dfu.getDevData().ssfRestApiEndPoint, '/widgets');
            }
            url += '/' + wgt.WIDGET_UNIQUE_ID() + '/screenshot';
            if (wgt && !wgt.WIDGET_VISUAL) {
                wgt.WIDGET_VISUAL = ko.observable('');
            }
            dfu.ajaxWithRetry({
                url: url,
                headers: dfu.getSavedSearchRequestHeader(),
                success: function (data) {
                    data && (wgt.WIDGET_VISUAL(data.screenShot));
                    !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/no-image-available.png'));
                },
                error: function () {
                    oj.Logger.error('Error to get widget screen shot for widget with unique id: ' + wgt.WIDGET_UNIQUE_ID);
                    !wgt.WIDGET_VISUAL() && (wgt.WIDGET_VISUAL('@version@/images/no-image-available.png'));
                },
                async: true
            });
        };

        self.searchWidgetsInputKeypressed = function (e, d) {
            if (d.keyCode === 13) {
                self.searchWidgetsClicked();
                return false;
            }
            return true;
        };
        self.searchWidgetsClicked = function () {
            self.loadWidgets();
        };

        self.autoSearchWidgets = function (req) {
            self.loadWidgets(req);
            if (req.term.length === 0) {
                self.clearRightPanelSearch(false);
            } else {
                self.clearRightPanelSearch(true);
            }
        };

        self.clearWidgetSearchInputClicked = function () {
            if (self.keyword()) {
                self.keyword(null);
                self.searchWidgetsClicked();
                self.clearRightPanelSearch(false);
            }
        };

        self.widgetMouseOverHandler = function (widget, event) {
            if ($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0) {
                return;
            }
            if (!widget.WIDGET_VISUAL()) {
                self.getWidgetScreenshot(widget);
            }
            var widgetItem = $(event.currentTarget).closest('.widget-item-' + widget.WIDGET_UNIQUE_ID());
            var popupContent = $(widgetItem).find('.dbd-left-panel-img-pop');
            $(".dbd-right-panel-build-container i.fa-plus").hide();
            $(".dbd-left-panel-img-pop").ojPopup("close");
            $(widgetItem).find('i').show();
            if (!popupContent.ojPopup("isOpen")) {
                $(popupContent).ojPopup("open", $(widgetItem),
                        {
                            my: "right bottom", at: "start center"
                        });
            }
        };
        self.widgetMouseOutHandler = function (widget, event) {
            var widgetItem = $(event.currentTarget).closest('.widget-item-' + widget.WIDGET_UNIQUE_ID());
            $(widgetItem).find('i').hide();
            if ($('.widget-' + widget.WIDGET_UNIQUE_ID()).ojPopup("isOpen")) {
                $('.widget-' + widget.WIDGET_UNIQUE_ID()).ojPopup("close");
            }
        };

        self.widgetKeyPress = function (widget, event) {
            if (event.keyCode === 13) {
                self.tilesViewModel().appendNewTile(widget.WIDGET_NAME(), "", 4, 2, ko.toJS(widget));
            }
        };

        self.resetFocus = function (widget, event) {
            event.currentTarget.focus();
        };

        self.widgetPlusClicked = function (widget, event) {
            self.tilesViewModel().appendNewTile(widget.WIDGET_NAME(), "", 4, 2, ko.toJS(widget));
        };

        self.widgetShowPlusIcon = function (widget, event) {
            $(".dbd-right-panel-build-container i.fa-plus").hide();
            $(".dbd-left-panel-img-pop").ojPopup("close");
            var widgetItem = $(event.currentTarget).closest('.widget-item-' + widget.WIDGET_UNIQUE_ID());
            $(widgetItem).find('i').show();
            self.widgetMouseOverHandler(widget, event);
        };

        self.widgetHidePlusIcon = function (widget, event) {
            var widgetItem = $(event.currentTarget).closest('.widget-item-' + widget.WIDGET_UNIQUE_ID());
            $(widgetItem).find('i').hide();
        };

        self.containerMouseOverHandler = function () {
            if ($('.ui-draggable-dragging') && $('.ui-draggable-dragging').length > 0) {
                return;
            }
            if (!$('.right-container-pop').ojPopup("isOpen")) {
                $('.right-container-pop').ojPopup("open", $('.dbd-left-panel-footer-contain'),
                        {
                            my: "end bottom", at: "start-25 bottom"
                        });
            }
        };

        self.containerMouseOutHandler = function () {
            if ($('.right-container-pop').ojPopup("isOpen")) {
                $('.right-container-pop').ojPopup("close");
            }
        };


        self.initDraggable = function () {
            self.initWidgetDraggable();
        };

        self.initWidgetDraggable = function () {
            $(".dbd-left-panel-widget-text").draggable({
                helper: "clone",
                scroll: false,
                start: function (e, t) {
                    $b.triggerEvent($b.EVENT_NEW_WIDGET_START_DRAGGING, null, e, t);
                },
                drag: function (e, t) {
                    $b.triggerEvent($b.EVENT_NEW_WIDGET_DRAGGING, null, e, t);
                },
                stop: function (e, t) {
                    $b.triggerEvent($b.EVENT_NEW_WIDGET_STOP_DRAGGING, null, e, t);
                }
            });
        };


        //Lazy loading widget scrrenshots
        var scrollInstantStore = ko.observable();
        var scrollDelay = ko.computed(function () {
            return scrollInstantStore();
        });
        scrollDelay.extend({rateLimit: {method: "notifyWhenChangesStop", timeout: 400}});

        var widgetListHeight = ko.observable($(".dbd-left-panel-widgets").height());
        var $dbdLeftPanelWidgets = $(".dbd-left-panel-widgets");

        self.widgetListScroll = function (data, event) {
            scrollInstantStore(event.target.scrollTop);
        };

        if (typeof window.MutationObserver !== 'undefined') {
            var widgetListHeightChangeObserver = new MutationObserver(function () {
                widgetListHeight($dbdLeftPanelWidgets.height());
            });
            widgetListHeightChangeObserver.observe($dbdLeftPanelWidgets[0], {
                attributes: true,
                attrbuteFilter: ['style']
            });
        } else {
            $b.addBuilderResizeListener(function () {
                widgetListHeight($dbdLeftPanelWidgets.height());
            });
        }

        widgetListHeight.subscribe(function () {
            loadSeeableWidgetScreenshots();
        });

        scrollDelay.subscribe(function (val) {
            loadSeeableWidgetScreenshots(val);
        });


        function loadSeeableWidgetScreenshots(startPosition) {
            var fromWidgetIndex = startPosition ? (Math.floor(startPosition / 30)) : 0;
            var toWidgetIndex = Math.ceil(widgetListHeight() / 30) + fromWidgetIndex;
            if (self.widgets && self.widgets().length > 0) {
                for (var i = fromWidgetIndex; i < toWidgetIndex; i++) {
                    if (self.widgets()[i] && !self.widgets()[i].WIDGET_VISUAL()) {
                        self.getWidgetScreenshot(self.widgets()[i]);
                    }
                }
            }
        };

    }
    return {"rightPanelWidget": rightPanelWidget};
}
);

