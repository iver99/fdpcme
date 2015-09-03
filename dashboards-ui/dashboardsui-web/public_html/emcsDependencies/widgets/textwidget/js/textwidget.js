define(["require", "knockout", "jquery", "ojs/ojcore"],
        function (localrequire, ko, $, oj) {
            function textWidgetViewModel(params) {
                var self = this;
                var textEditor;
                var textEditorId;
                var defaultContent = '<span style="font-size: 16px;">' + getNlsString("DBS_BUILDER_TEXT_WIDGET_EDIT") + '<span>';
                var validator, callbackAfterDblClick;
                if(params.validator){
                    validator = params.validator;
                }
                if(params.callbackAfterDblClick) {
                    callbackAfterDblClick = params.callbackAfterDblClick;
                }
                self.showErrorMsg = ko.observable("none");
                self.randomId = new Date().getTime();
                if(params.tile.content) {
                   self.content = params.tile.content;
                }
                if(!self.content()){
                    self.content = ko.observable(defaultContent);
                    params.tile.content(self.content());
                }
                
                self.showEditor = function () {
                    var flag = false;
                    textEditorId = "textEditor_"+self.randomId;
                    if(CKEDITOR.instances){
                        for(var i in CKEDITOR.instances) {
                            if(i === textEditorId) {
                                textEditor = CKEDITOR.instances[i];
                                flag = true;
                                break;
                            }
                        }
                    }
                    if(!flag){
                        self.initializeCKEditor(textEditorId);
                    }
                    if($("#textWidget_" + self.randomId + " #textEditorWrapper").is(":visible")) {                        
                        var textInfo = textEditor.document ? (textEditor.document.getBody().getHtml()?textEditor.document.getBody().getHtml() : defaultContent):"";
//                        console.log(textInfo);
                        if(!validator(textInfo)) {
                            self.showErrorMsg("block");                            
                        }else{                            
                            $("#textWidget_" + self.randomId + " #textEditorWrapper").toggle();
                            if($("#textWidget_"+self.randomId).hasClass("editing")) {
                                $("#textWidget_"+self.randomId).removeClass("editing");
                            }
                            self.content(textInfo);
                            params.tile.content(self.content());
                            self.showErrorMsg("none");
                        }
                    }else {                        
                        $("#textWidget_" + self.randomId + " #textEditorWrapper").toggle();
                        $("#textWidget_"+self.randomId).addClass("editing");
                        $("#textEditor_"+self.randomId).html(self.content());
                        textEditor.setData(self.content());
                    }
                     
                    callbackAfterDblClick();
                }
                
                self.initializeCKEditor = function(id) {
                    textEditor = CKEDITOR.replace(id, {
                            language: 'en',
                            toolbar: [
                                {name: 'styles', items: ['Font', 'FontSize']},
                                {name: 'basicStyles', items: ['Bold', 'Italic', 'Underline']},
                                {name: 'colors', items: ['TextColor']},
                                {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']}
//                                {name: 'insert', items: ['Image', 'Flash']}
                            ],
                            removePlugins: 'resize, elementspath',
                            startupFocus: true
                        });
                }
                
                self.cancelEdit = function() {
                    if($("#textWidget_"+self.randomId).hasClass("editing")) {
                        $("#textWidget_"+self.randomId).removeClass("editing");
                    }
                    
                    self.showErrorMsg("none");
                    $("#textWidget_" + self.randomId + " #textEditorWrapper").toggle();
                    
                    callbackAfterDblClick();
                }
                
                self.deleteEditor = function() {
                    $("#textWidget_"+self.randomId).remove();
                    if(params.tiles) {
                        params.tiles.remove(params.tile);
                    }
                    callbackAfterDblClick();
                }
                
                self.toggleEditIcons = function() {
                    callbackAfterDblClick();
                }
            }
            return textWidgetViewModel;
        });