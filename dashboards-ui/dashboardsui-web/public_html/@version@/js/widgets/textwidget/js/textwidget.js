define(["require", "knockout", "jquery", "ojs/ojcore"],
        function (localrequire, ko, $) {
            function getGuid() {
                function securedRandom(){
                    var arr = new Uint32Array(1);
                    var crypto = window.crypto || window.msCrypto;
                    crypto.getRandomValues(arr);
                    var result = arr[0] * Math.pow(2,-32);
                    return result;
                }
                function S4() {
                    return parseInt(((1 + securedRandom()) * 0x10000)).toString(16).substring(1);
                }
                return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
            }

            function textWidgetViewModel(params) {
                var self = this;
                var TEXT_WIDGET_CONTENT_MAX_LENGTH = 4000;
                var editor;
                var defaultContent = '<p><span style="font-family:arial,helvetica,sans-serif"><span style="font-size: 18px"><strong>' +
                        getNlsString("DBS_BUILDER_TEXT_WIDGET_SAMPLE") + '</strong></span></span></p>';
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
                if(params.deleteTextCallback) {
                    self.deleteTextCallback = params.deleteTextCallback;
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

                var lang;
                try {
                    lang = requirejs.s.contexts._.config.config.i18n.locale;
                }catch(err) {
                    lang = $("html").attr("lang") ? $("html").attr("lang") : window.navigator.language;
                }
                var configOptions = {
                    language: lang,
                    toolbar: [
                        {name: 'styles', items: ['Font', 'FontSize']},
                        {name: 'basicStyles', items: ['Bold', 'Italic', 'Underline']},
                        {name: 'colors', items: ['TextColor']},
                        {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']},
                        {name: 'links', items: ['Link']}
                    ],
                    removePlugins: 'resize, elementspath',
                    startupFocus: false,
                    uiColor: "#FFFFFF",
                    linkShowAdvancedTab: false,
                    linkShowTargetTab: false

                };
                var x,y;
                self.textMouseDown = function(data, e) {
                    x = e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft;
                    y = e.clientY+document.body.scrollTop+document.documentElement.scrollTop;
                };
                self.textMouseUp = function(data, e) {
                    if(e.which !== 1) {
                        return;
                    }
                    //Do not open ckeditor when single click on hyperlink
                    var elem = $(e.target);
                    while(elem.attr("id") !== "textContentWrapper_"+self.randomId) {
                       if(elem.is("a")) {
                           return;
                       }
                       elem = elem.parent();
                    }

                    var tmpX  = e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft;
                    var tmpY = e.clientY+document.body.scrollTop+document.documentElement.scrollTop;
                    if(x!==tmpX || y!==tmpY) {
                        return;
                    }else {
                        self.textClicked(data, e);
                    }
                };



                var delay = 300;
                var clicks = 0;
                var timerClickType = null, timerSetCaret = null;

                function insertBreakAtPoint(e) {
                    var range, caretPosition;
                    var textNode;
                    var offset;

                    if (document.caretPositionFromPoint) {
                        caretPosition = document.caretPositionFromPoint(e.clientX, e.clientY);
                        textNode = caretPosition.offsetNode;
                        offset = caretPosition.offset;
                        range = document.createRange();
                    } else if (document.caretRangeFromPoint) {
                        range = document.caretRangeFromPoint(e.clientX, e.clientY);
                        textNode = range.startContainer;
                        offset = range.startOffset;
                    }
                    var sel = window.getSelection();
                    range.setStart(textNode, offset);
                    range.collapse(true);
                    sel.removeAllRanges();
                    sel.addRange(range);
                }

                self.textClicked = function(data, e) {
                    clicks++;
                    if(clicks === 1) {
                        timerClickType = setTimeout(function() {
                            clicks = 0;
                            self.showTextEditor();
                            timerSetCaret = setTimeout(function() {insertBreakAtPoint(e);}, 400);
                        }, delay);
                    }else {
                        clearTimeout(timerSetCaret);
                        clearTimeout(timerClickType);
                        clicks = 0;
                        self.editTextEditor();
                    }
                };

                self.editTextEditor = function() {
                    self.showTextEditor();
                    editor.execCommand("selectAll");
                };

                self.showTextEditor = function () {
                    $("#textContentWrapper_" + self.randomId).hide();
                    $("#textEditorWrapper_" + self.randomId).show();
                    $("#textEditor_" + self.randomId).focus();
                };

                CKEDITOR.on('dialogDefinition', function(ev) {
                    // Take the dialog name and its definition from the event data.
                    var dialogName = ev.data.name;
                    var dialogDefinition = ev.data.definition;

                    dialogDefinition.height = "150px";
                    dialogDefinition.resizable = CKEDITOR.DIALOG_RESIZE_NONE;
                    if (dialogName === 'link') {
                        //change dialog title to "Add Hyperlink"
                        dialogDefinition.title = getNlsString("DBS_BUILDER_TEXT_WIDGET_LINK_DIALOG_TITLE");
                        //remove link type and set its option as "URL"
                        var infoTab = dialogDefinition.getContents("info");
                        infoTab.get("linkType").style = "display: none;";
                        infoTab.get("anchorOptions").style = "display: none";
                        infoTab.get("emailOptions").style = "display: none";

                        //reset ok button in link dialog
                        dialogDefinition.onShow = function() {
                            var dialog = CKEDITOR.dialog.getCurrent();
                            var okButtonId = dialog.getButton("ok").domId;

                            $("#"+okButtonId).css("background", "linear-gradient(to bottom, #0470c9 0%, #0571cd 50%, #0479d8 100%)");
                            $("#"+okButtonId).css("background-image", "none");
                            $("#"+okButtonId).css("background-color", "#0572ce");
                            $("#"+okButtonId).css("color", "#ffffff");
                            $("#"+okButtonId).css("border-color", "#0476d3");
                            $("#"+okButtonId).css("border-shadow", "none");
                            if($("#"+okButtonId).hasClass("cke_dialog_ui_button_ok")) {
                                $("#"+okButtonId).hover(
                                    function() {
                                        $("#"+okButtonId).css("background-color", "#3a9aea");
                                        $("#"+okButtonId).css("border", "1px solid #3a9aea");
                                    },
                                    function() {
                                        $("#"+okButtonId).css("background-color", "#0572ce");
                                        $("#"+okButtonId).css("border", "1px solid #0476d3");
                                    }
                                );
                            }
                        };
                    }
                });

                self.loadEditor = function (data, event) {
                    $("#textEditor").attr("id", "textEditor_" + self.randomId);
                    $("#textEditor_" + self.randomId).attr("contenteditable", "true");

                    editor = CKEDITOR.inline("textEditor_" + self.randomId, configOptions);

                    editor.on("instanceReady", function () {
                        this.dataProcessor.htmlFilter.addRules({
                            elements: {
                                a: function(element) {
                                    element.attributes.target = "_blank";
                                }
                            }
                        });
                        //reset textcolor icon

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
                            preHeight = curHeight;
                            self.show && self.show();
                            self.builder && self.builder.triggerEvent(self.builder.EVENT_TEXT_START_EDITING, null, null);
                        }
                    });
                };

                self.deleteEditor = function() {
                    $("#textWidget_"+self.randomId).remove();
                    self.tiles && self.tiles.remove(self.tile);
                    self.deleteTextCallback && self.deleteTextCallback(self.tile);
                    self.show && self.show();
                };

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
                            var thisChildLeft = ($(children[i]).position()).left + $(children[i]).width();
                            rowWidth = Math.max(rowWidth, thisChildLeft);
                            textMaxWidth = Math.max(textMaxWidth, rowWidth);
                        }
                    });
                    if($b.findEl(".widget-area") && (textMaxWidth+78)>widgetContainerWidth) {
                        textMaxWidth = widgetContainerWidth-78;
                    }
                    $("#textContentWrapper_"+self.randomId+" #textWidgetEditBtns_"+self.randomId).css("left", textMaxWidth);
                };

                self.loadEditor();
            }
            return textWidgetViewModel;
        });