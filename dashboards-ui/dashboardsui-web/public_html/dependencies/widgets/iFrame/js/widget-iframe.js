/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'ojs/ojcore',
    'knockout',
    'jquery',
    'dfutil'
],
        function (oj, ko, $,dfu)
        {
            function IFrameModel(params) {
                var PNAME_IFRAME_SRC = "iframeSrc";
                var DEFAULT_URL = "about:blank";
                var self = this;
                var tile = params.tile;
                var param = tile.getParameter(PNAME_IFRAME_SRC);
                if (param===null || param===undefined){
                    url = DEFAULT_URL;
                }else{
                    url = param.value;
                }

                self.clientGuid = dfu.guid();
                /**
                 * Since the same widget can be added to dashboard more than once, 
                 * we need to specify unqiue id for configuation dialog of each widget with the same type.
                 * 
                 */
                var configDialogId = '#iframe_configDialog_'+self.clientGuid;
                self.title = ko.observable(tile.title());
                self.fullUrl=ko.observable(url);
                self.targetUrl=ko.observable(url);
                
                /**
                 * Open dialog to configure widget
                 */
                params.tile.configure = function(){
                    $(configDialogId).ojDialog('open');
                }
                
                /**
                 * Save configuration and close configuration dialog
                 * 
                 */
                self.saveConfiguration = function(){
                   params.tile.title(self.title());
                   self.targetUrl(self.fullUrl()); 
                   params.tile.setParameter(PNAME_IFRAME_SRC,self.fullUrl());
                   $(configDialogId).ojDialog('close');
                }
                
                /**
                 * Discard configuration and close configuration dialog
                 * 
                 */
                self.close = function(){
                    self.title(params.tile.title());
                    self.fullUrl(self.targetUrl()); 
                    $(configDialogId).ojDialog('close');
                }
                
                /**
                 * Open dialog for configuration in async way if criteria to open configuration dialog is met.
                 * Note:
                 * we can't run tile.configure directly here which will break model intialization and template can't run without model initializtion's completion
                 */
                if (DEFAULT_URL===url){
                    setTimeout(tile.configure,1000);
                }
            }
            return IFrameModel;
        });


