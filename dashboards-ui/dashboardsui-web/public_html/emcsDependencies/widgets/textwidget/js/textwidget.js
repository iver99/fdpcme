define(["require", "knockout", "jquery", "ojs/ojcore"],
        function (localrequire, ko, $, oj) {
            function textWidgetViewModel(params) {
                var self = this;
                var textEditor;
                self.randomId = new Date().getTime();
                self.content = params.content;
                console.log(self.content());
                console.log("*******");
                console.log(!self.content());


                self.showEditor = function () {
//                    $("#textWidget_" + self.randomId + " #textEditorWrapper").css("display", "block");
                    $("#textWidget_" + self.randomId + " #textEditorWrapper").toggle();
                    
                    if (!textEditor) {
                        var textEditorId = "textEditor_" + self.randomId;
                        textEditor = CKEDITOR.replace(textEditorId, {
                            language: 'en',
                            toolbar: [
                                {name: 'styles', items: ['Font', 'FontSize']},
                                {name: 'basicStyles', items: ['Bold', 'Italic', 'Underline']},
                                {name: 'colors', items: ['TextColor']},
                                {name: 'paragraph', items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight']},
                                {name: 'insert', items: ['Image', 'Flash']}
                            ]
                        });
                    }
                    if($("#textWidget_" + self.randomId + " #textEditorWrapper").is(":hidden")) {
                        var textInfo = textEditor.document.getBody().getHtml();
                        $("#textWidget_" + self.randomId + " #textInfo").html(textInfo); 
                        self.content(textInfo);
                    }else {                        
                        $("#textEditor_"+self.randomId).html($("#textWidget_" + self.randomId + " #textInfo").html());
                    }
                     
                    if(params.callbackAfterDblClick) {
                        params.callbackAfterDblClick();
                    }
                }

//                self.applyClick = function () {
//                    var textInfo = textEditor.document.getBody().getHtml();
////                    console.log(textInfo);
//                    $("#textWidget_" + self.randomId + " #textInfo").html(textInfo);
//                    $("#textWidget_" + self.randomId + " #textEditorWrapper").css("display", "none");
//                    if(params.callbackAfterDblClick) {
//                        params.callbackAfterDblClick();
//                    }
//                }
//                self.cancelClick = function () {
//                    $("#textWidget_" + self.randomId + " #textEditorWrapper").css("display", "none");
//                    if(params.callbackAfterDblClick) {
//                        params.callbackAfterDblClick();
//                    }
//                }
            }
            return textWidgetViewModel;
        });