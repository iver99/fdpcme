define(["require", "knockout", "jquery", "ojs/ojcore"],
        function (localrequire, ko, $, oj) {
            function getGuid() {
                function S4() {
                    return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
                }
                return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
            }

            function textWidgetViewModel(params) {
                var self = this;
                var TEXT_WIDGET_CONTENT_MAX_LENGTH = 4000;
                var editor;
//                var defaultContent = '<span style="font-size: 1.2em; font-weight: bold;">' + getNlsString("DBS_BUILDER_TEXT_WIDGET_EDIT") + '<span>';
                var defaultContent = '<p><span style="font-size: 18px"><strong>' + getNlsString("DBS_BUILDER_TEXT_WIDGET_EDIT") + '</strong></span></p>';
                var preHeight;
                
                self.showErrorMsg = ko.observable(false);
                self.errorMsgCss = ko.observable("none");
                self.showErrorMsg.subscribe(function (val) {
                    self.errorMsgCss(val ? "block" : "none");
                    self.show && self.show();
                }, self);

                if (params.validator) {
                    self.validator = params.validator;
                }
                if (params.show) {
                    self.show = params.show;
                }
                if(params.tiles) {
                    self.tiles = params.tiles;
                }
                if(params.tile) {
                    self.tile = params.tile;
                }
                if(params.reorder) {
                   self.reorder = params.reorder; 
                }
                if (params.builder) {
                    self.builder = params.builder;
                }
                self.randomId = getGuid();
                if (params.tile.content) {
                    self.content = params.tile.content;
                }
                if (!self.content()) {
                    self.content = ko.observable(defaultContent);
                    params.tile.content(self.content());
                }

                var configOptions = {
                    language: 'en',
                    toolbar: [
                        {name: 'styles', items: ['Font', 'FontSize']},
                        {name: 'basicStyles', items: ['Bold', 'Italic', 'Underline']},
                        {name: 'colors', items: ['TextColor']},
                        {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']},
                        {name: 'links', items: ['Link']}
//                                {name: 'insert', items: ['Image', 'Flash']}
                    ],
                    removePlugins: 'resize, elementspath',
                    startupFocus: false,
                    uiColor: "#FFFFFF"
                }

                self.showTextEditor = function () {
                    $("#textContentWrapper_" + self.randomId).hide();
                    $("#textEditorWrapper_" + self.randomId).show();
                    $("#textEditor_" + self.randomId).focus();
                }

                self.loadEditor = function (data, event) {
                    $("#textEditor").attr("id", "textEditor_" + self.randomId);
                    $("#textEditor_" + self.randomId).attr("contenteditable", "true");

                    editor = CKEDITOR.inline("textEditor_" + self.randomId, configOptions);

                    editor.on("instanceReady", function () {
                        this.setData(self.content());                        
                        $("#textEditorWrapper_" + self.randomId).css("background-color", "white");
                        $("#textEditorWrapper_" + self.randomId).hide();
                        self.show && self.show();
                        self.builder && self.builder.triggerEvent(self.builder.EVENT_TEXT_STOP_EDITING, null, self.showErrorMsg());
                    });

                    editor.on("blur", function () {
                        if(!this.getData()) {
                            this.setData(defaultContent);
                        }
                        if (self.validator && !self.validator(this.getData(), TEXT_WIDGET_CONTENT_MAX_LENGTH)) {
                            self.showErrorMsg(true);
                        } else {
                            self.showErrorMsg(false);
                        }
//                        console.log(this.getData());
                        self.content(this.getData());
                        params.tile.content(self.content());
                        $("#textEditorWrapper_" + self.randomId).hide();
                        $("#textContentWrapper_" + self.randomId).show();
                        self.show && self.show();
                        self.builder && self.builder.triggerEvent(self.builder.EVENT_TEXT_STOP_EDITING, null, self.showErrorMsg());
                    });

                    editor.on("focus", function () {
                        self.show && self.show();
                        self.builder && self.builder.triggerEvent(self.builder.EVENT_TEXT_START_EDITING, null, null);
                        preHeight = $("#textEditorWrapper_"+self.randomId).height();
                    });
                                       
                    editor.on("change", function() {
                        var curHeight = $("#textEditorWrapper_"+self.randomId).height();
                        if(curHeight !== preHeight) {
//                            console.log("editing area height changed!");
                            preHeight = curHeight;
                            self.show && self.show();
                            self.builder && self.builder.triggerEvent(self.builder.EVENT_TEXT_START_EDITING, null, null);
                        }
                    });
                }
                
                self.deleteEditor = function() {
                    $("#textWidget_"+self.randomId).remove();
                    self.tiles && self.tiles.remove(self.tile);
                    self.reorder && self.reorder();
                    self.show && self.show();
                }
                
                self.showEditIcons = function() {
                    var textMaxWidth = 0;
                    var widgetContainerWidth = $('#textContentWrapper_'+self.randomId).width();
                    $("#textContentWrapper_"+self.randomId+" p").each(function() {
                        var rowWidth = 0;
                        var children = $(this).children();
                        if(children.length === 0) {
                            $(this).html("<span>"+$(this).html()+"</span>");
                            children = $(this).children();
                        }
                        for(var i=0; i<children.length; i++) {
                            rowWidth = ($(children[children.length-1]).position()).left+$(children[children.length-1]).width();
                            textMaxWidth = Math.max(textMaxWidth, rowWidth);
                        }                        
                    });
                    if($("#widget-area") && (textMaxWidth+78)>widgetContainerWidth) {
                        textMaxWidth = widgetContainerWidth-78;
                    }
                    $("#textContentWrapper_"+self.randomId+" #textWidgetEditBtns_"+self.randomId).css("left", textMaxWidth);
                }
                
                self.loadEditor();
            }
            return textWidgetViewModel;
        });