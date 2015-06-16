/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define([
    'ojs/ojcore',
    'knockout',
    'jquery',
    'dfutil',
    'ojs/ojknockout',
    'ojs/ojselectcombobox'
],
        function (oj, ko, $, dfu)
        {
            function OnePageModel(params) {
                var self = this;
                var tile = params.tile;
                //Get unique id from WIDGET_UNIQUE_ID

                var providerName = tile.PROVIDER_NAME();
                var providerVersion = tile.PROVIDER_VERSION();
                var providerAssetRoot = tile.PROVIDER_ASSET_ROOT();

                if (providerName && providerVersion && providerAssetRoot) {
                    self.fullUrl = ko.observable(dfu.df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot, false));
                } else { 
                    self.fullUrl = ko.observable("about:blank");
                }
                if (self.fullUrl() && self.fullUrl()!=="about:blank"){
                    var url = self.fullUrl();
                     if (url.length>"displayMode=dashboard".length){
                        if (url.indexOf("displayMode=dashboard")<0){
                            if (url.indexOf("?")>0 || url.indexOf("&")>0){
                                self.fullUrl(url+"&displayMode=dashboard"); 
                            }else{
                                self.fullUrl(url+"?displayMode=dashboard"); 
                            }
                        } 
                     }else{
                        self.fullUrl(url+"?displayMode=dashboard"); 
                     }
                }
            }
            return OnePageModel;
        });


