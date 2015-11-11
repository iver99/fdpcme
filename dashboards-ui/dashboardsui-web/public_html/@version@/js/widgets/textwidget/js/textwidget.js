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
                var defaultContent = '<p><span style="font-family:arial,helvetica,sans-serif"><span style="font-size: 18px"><strong>' 
                        + getNlsString("DBS_BUILDER_TEXT_WIDGET_SAMPLE") + '</strong></span></span></p>';
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
//                if(params.reorder) {
//                   self.reorder = params.reorder; 
//                }
                if(params.deleteTextCallBack) {
                    self.deleteTextCallBack = params.deleteTextCallBack;
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
                    lang = $("html").attr("lang") ? $("html").attr("lang") : navigator.language;
                }
                var configOptions = {
                    language: lang,
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
                    uiColor: "#FFFFFF",
                    linkShowAdvancedTab: false,
                    linkShowTargetTab: false
                    
                }
                var x,y,t;
                self.textMouseDown = function(data, e) {
                    x = e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft;
                    y = e.clientY+document.body.scrollTop+document.documentElement.scrollTop;
                }
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

                    var tmpX  = e.clientX+document.body.scrollLeft+document.documentElement.scrollLeft;;
                    var tmpY = e.clientY+document.body.scrollTop+document.documentElement.scrollTop;
                    if(x!==tmpX || y!==tmpY) {
//                        console.log("dragged");
                        return;
                    }else {
//                        console.log("clicked");
                        self.textClicked(data, e);
                    }
                }
                
                
                
                var delay = 300;
                var clicks = 0;
                var timerClickType = null, timerSetCaret = null, timerHighlight = null;
                
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
//                            console.log("single click");
                            clicks = 0;                            
                            self.showTextEditor();
                            timerSetCaret = setTimeout(function() {insertBreakAtPoint(e)}, 400);
                        }, delay);             
                    }else {
                        clearTimeout(timerSetCaret);
                        clearTimeout(timerClickType);
//                        console.log("double click");
                        clicks = 0;
                        self.editTextEditor();
                    }
                }
                
                self.editTextEditor = function() {
                    self.showTextEditor();
                    editor.execCommand("selectAll");
                }
                
                self.showTextEditor = function () {
                    $("#textContentWrapper_" + self.randomId).hide();
                    $("#textEditorWrapper_" + self.randomId).show();                       
                    $("#textEditor_" + self.randomId).focus();
                }
                
                CKEDITOR.on('dialogDefinition', function(ev) {
                    // Take the dialog name and its definition from the event data.
                    var dialogName = ev.data.name;
                    var dialogDefinition = ev.data.definition;

                    dialogDefinition.height = "150px";
                    dialogDefinition.resizable = CKEDITOR.DIALOG_RESIZE_NONE;
                    if (dialogName == 'link') {
                        //remove link type and set its option as "URL"
                        var infoTab = dialogDefinition.getContents("info");
                        infoTab.get("linkType").style = "display: none;";
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
//                        $($(".cke_button__textcolor span")[0]).removeClass("cke_button__textcolor_icon");
//                        $($(".cke_button__textcolor span")[0]).addClass("cke_textcolor_new_icon");
                        
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
                    self.deleteTextCallBack && self.deleteTextCallBack(self.tile);
//                    self.reorder && self.reorder();
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
                            var thisChildLeft = ($(children[i]).position()).left + $(children[i]).width();
                            rowWidth = Math.max(rowWidth, thisChildLeft);
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