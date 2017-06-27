define(["require", "knockout", "jquery", "ojs/ojcore", "ckeditor"],
        function (localrequire, ko, $) {
            function textWidgetViewModel(params) {
                var self = this;
                self.content = params.tile.content;
                self.emptyContent = ko.computed(function() {
                    var content = $("<div/>").html(self.content()).text().trim();
                    if(content) {
                        return false;
                    }else {
                        self.content(content);
                        return true;
                    }
                });
                self.textWidgetId = params.tile.tileId ? ko.unwrap(params.tile.tileId) : params.tile.clientGuid;
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
                        {name: 'basicStyles', items: ['Bold', 'Italic']},
                        {name: 'colors', items: ['TextColor']},
                        {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']},
                        {name: 'links', items: ['Link']}
                    ],
                    removePlugins: 'elementspath',
                    startupFocus: false,
                    uiColor: "#FFFFFF",
                    linkShowAdvancedTab: false,
                    linkShowTargetTab: false,
                    enterMode: CKEDITOR.ENTER_BR

                };

                var editor;
                self.loadTextEditor = function () {
                    $("#textEditor").attr("id", "textEditor_" + self.textWidgetId);
                    $("#textEditor_" + self.textWidgetId).attr("contenteditable", "true");
                    editor = CKEDITOR.inline("textEditor_" + self.textWidgetId, configOptions);
                        
                    editor.on("instanceReady", function () {
                        this.setData(self.content());
                        $('#textEditorWrapper_'+self.textWidgetId).hide();
                        $("#textEditor_" + self.textWidgetId).focus();
                    });

                    editor.on("blur", function () {
//                        self.content(this.getData());
                        $('#textContentWrapper_'+self.textWidgetId).show();
                        $('#textEditorWrapper_'+self.textWidgetId).hide();
                    });
                    
                    editor.on("focus", function() {
                       this.setData(self.content()); 
                    });
                    editor.on("change", function() {
                        self.content(this.getData());
                    });
                };
                
                self.showTextEditor = function() {
                    $('#textContentWrapper_'+self.textWidgetId).hide();
                    $('#textEditorWrapper_'+self.textWidgetId).show();
                    $("#textEditor_" + self.textWidgetId).focus();
                };
                
                self.loadTextEditor();
            }
            return textWidgetViewModel;
        });