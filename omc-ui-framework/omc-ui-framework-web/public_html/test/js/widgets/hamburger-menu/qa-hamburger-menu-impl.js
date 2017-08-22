define(['ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/widgets/hamburger-menu/hamburger-menu-impl'
],function(oj, ko, $, hamburgerMenuViewModel, usertenantUtil){
    return {
        run: function(){
            QUnit.module("hamburgerMenuTest");
            QUnit.test("testHamburgerMenuLoadedWhenNoSubscribe", function(assert){
                window.DEV_MODE ={ 
                   "tenant": "fake",
                   "user": "fake",                              
                   "wlsAuth": "fake",                                  
                   "registryUrl": "fake",   
                   "dfRestApiEndPoint": "fake",                         
                   "ssfRestApiEndPoint": "fake",                             
                   "dashboardFrameworkAPIEndPoint": "fake",
                   "savedSearchEndPoint": "fake"
               };
               var params = {"userName":"userName", "tenantName":"tenantName"};
               var hamburger = new hamburgerMenuViewModel(params);
               hamburger.subscribedApps = ["OMC"]; 
               assert.ok(hamburger.serviceMenuData);   
            });
        }
    }
});

