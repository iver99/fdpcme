

define(['dfutil', 'ojs/ojcore', 'knockout', 'jquery', 'uifwk/js/sdk/context-util', 'ojs/ojknockout', 'ojs/ojmodel'],
function(dfu, oj, ko, $, cxtModel)
{
/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */


    var DashboardModel = function(attrs, options) {
        var self = this, _attrs = attrs, _options = options || {};
        this.screenShot = undefined;

        var _customURL = function(_operation, _col, _opt) {
            var __url =  self.get('href');
            if (!__url) {
                __url = self.url();
            }
            if (!__url) {
                __url = null;
            }
            return {
                    url: __url,
                    headers: dfu.getDashboardsRequestHeader()
               };
        };
        _options['customURL'] = _customURL;
        DashboardModel.superclass.constructor.call(this, _attrs, _options);
    };

    oj.Object.createSubclass(DashboardModel, oj.Model, 'DashboardModel');

    /**
     * Initializes the data source.
     * @export
     */
    DashboardModel.prototype.Init = function()
    {
        // super
        DashboardModel.superclass.Init.call(this);
    };

    DashboardModel.prototype.openDashboardPage = function()
    {
        var self = this;
        var cxtUtil = new cxtModel();
        var url = self.getNavLink();
        if (typeof url==="string"){
            window.location = cxtUtil.appendOMCContext(self.getNavLink());
        }
    };

    DashboardModel.prototype.getNavLink = function()
    {
        var _id = this.get('id');
        if (!_id) {
            return null;
        }
        var _type = this.get('type');
        if ("SINGLEPAGE"===_type){
            var tiles = this.get('tiles');
            if (Array.isArray(tiles) && tiles.length===1){
                var providerName = tiles[0]["PROVIDER_NAME"];
                var version = tiles[0]["PROVIDER_VERSION"];
                var assetRoot = tiles[0]["PROVIDER_ASSET_ROOT"];
                var url = dfu.df_util_widget_lookup_assetRootUrl(providerName,version, assetRoot, false);
                if (dfu.isDevMode()){
                    url = dfu.getRelUrlFromFullUrl(url);
                }
                this.fetch(); //record last access on rest api
                if (typeof url==="string"){
                    oj.Logger.info("Single page URL for dashboard ID=" + this.get("id") + " is: " + url);
                   return url;
                }else{
                   oj.Logger.error("Single Page Dashboard URL is not found by: serviceName="+providerName+", version="+version+", asset root="+assetRoot);
                   return null;
                }
            }else{
                oj.Logger.error("Invalid tiles: "+JSON.stringify(tiles));
            }
        }else{
            return document.location.protocol + '//' + document.location.host + '/emsaasui/emcpdfui/builder.html?dashboardId=' + _id;

        }
    };

    return {'DashboardModel': DashboardModel};
});

