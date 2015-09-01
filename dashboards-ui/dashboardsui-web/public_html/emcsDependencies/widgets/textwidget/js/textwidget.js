define(["require", "knockout", "jquery", "ojs/ojcore"],
        function (localrequire, ko, $, oj) {
            function textWidgetViewModel(params) {
                var self = this;
                var textEditor;
                var textEditorId;
                self.randomId = new Date().getTime();
                if(params.content && params.content()) {
                   self.content = params.content;
                }else{
                   self.content = ko.observable("Double click here to edit text");
                }
                

                self.showEditor = function () {
                    var flag = false;
                    $("#textWidget_" + self.randomId + " #textEditorWrapper").toggle();
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
                        textEditor = CKEDITOR.replace(textEditorId, {
                            language: 'en',
                            toolbar: [
                                {name: 'styles', items: ['Font', 'FontSize']},
                                {name: 'basicStyles', items: ['Bold', 'Italic', 'Underline']},
                                {name: 'colors', items: ['TextColor']},
                                {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']},
                                {name: 'insert', items: ['Image', 'Flash']}
                            ],
                            removePlugins: 'resize, elementspath'
                        });
                    }
                    if($("#textWidget_" + self.randomId + " #textEditorWrapper").is(":hidden")) {
                        var textInfo = textEditor.document ? (textEditor.document.getBody().getHtml()?textEditor.document.getBody().getHtml() : "Double click here to edit text"):"";
                        $("#textWidget_" + self.randomId + " #textInfo").html(textInfo); 
                        console.log(textInfo);
                        self.content(textInfo);
                    }else {
                        $("#textEditor_"+self.randomId).html(self.content());
                    }
                     
                    if(params.callbackAfterDblClick) {
                        params.callbackAfterDblClick();
                    }
                }
            }
            return textWidgetViewModel;
        });