define(["require", "knockout", "jquery", "ojs/ojcore", "ckeditor"],
        function (localrequire, ko, $) {
            function textWidgetViewModel(params) {
                var self = this;
                self.content = params.tile.content;
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
                        {name: 'basicStyles', items: ['Bold', 'Italic', 'Underline']},
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
//                CKEDITOR.disableAutoInline = true;
                $("#editor1").attr("id", "editor1_" + self.textWidgetId);
                var editor = CKEDITOR.inline("editor1_" + self.textWidgetId, configOptions);
                
                editor.on("instanceReady", function () {
                    this.setData(self.content());
                });
                
                editor.on("blur", function () {
                    self.content(this.getData());
                });
            }
            return textWidgetViewModel;
        });