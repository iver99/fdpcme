

define(['dfutil', 'ojs/ojcore', 'knockout', 'jquery', 'ojs/ojknockout', 'ojs/ojmodel'], 
function(dfu, oj, ko, $)
{
/**
 * @preserve Copyright (c) 2015, Oracle and/or its affiliates.
 * All rights reserved.
 */


    DashboardModel = function(attrs, options) {
        var self = this, _attrs = attrs, _options = options || {};
        this.screenShot = undefined;
        
        //_options['idAttribute'] = "id";
        
        var _customURL = function(_operation, _col, _opt) {
            var __url =  self.get('href');
            if (!__url) {
                __url = self.url();
            }
            if (!__url) {
                __url = null;
            }
            //console.log("[DashboardModel] operation: "+ _operation +"  "+__url + " \n      Header: " + JSON.stringify(dfu.getDashboardsRequestHeader())); //return __url;
            return {
                    url: __url,
                    headers: dfu.getDashboardsRequestHeader()//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()}//Pass the required header information
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
        // super
        var self = this, widgets = self.get('widgets') || self.get('tiles');
        if ("onePage"===self.type){
                if (widgets instanceof Array && widgets.length===1 && 
                        widgets[0].WIDGET_KOC_NAME &&
                        widgets[0].WIDGET_VIEWMODEL &&
                        widgets[0].WIDGET_TEMPLATE &&
                        widgets[0].PROVIDER_NAME &&
                        widgets[0].PROVIDER_VERSION &&
                        widgets[0].PROVIDER_ASSET_ROOT
                        ){
                    window.open(self.getLink());
                }else{
                    $( "#dbs_comingsoonDialog" ).ojDialog( "open" );
                }              
        }else{
                window.open(self.getNavLink());
        }
    };
    
    DashboardModel.prototype.getNavLink = function()
    {
        var _id = this.get('id');
        if (!_id) return null;
        return document.location.protocol + '//' + document.location.host + '/emcpdfui/builder.html?dashboardId=' + _id;
    };

    return {'DashboardModel': DashboardModel};
});

