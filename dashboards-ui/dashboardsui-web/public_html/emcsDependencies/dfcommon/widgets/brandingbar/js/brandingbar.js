define(['require','knockout', 'jquery', '../../../js/util/df-util'],
        function (localrequire, ko, $, dfumodel) {
            function BrandingBarViewModel(params) {
                var self = this;
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
//                self.registryUrl = $.isFunction(params.registryUrl) ? params.registryUrl() : params.registryUrl;
                self.productName = "Enterprise Manager";
                self.cloudName = "Cloud Service";
                self.appName = $.isFunction(params.appName) ? params.appName() : params.appName;
                self.userTenantName = self.userName && self.tenantName ? self.userName + " (" + self.tenantName + ")" : "emaas.user@oracle.com";
                var dfu = new dfumodel(self.userName, self.tenantName);
                var ssoLogoutEndUrl =dfu.discoverDFHomeUrl();
                
                //SSO logout handler
                self.handleSignout = function() {
                    window.location.href = dfu.discoverLogoutUrl() + "?endUrl=" + ssoLogoutEndUrl;
                };
                
                self.globalNavItems = [
                    {"label": "Preferences",
                        "url": "#",
                        "onclick": ""
                    },
                    {"label": "Help",
                        "url": "#",
                        "onclick": ""
                    },
                    {"label": "About",
                        "url": "#",
                        "onclick": ""
                    },
                    {"label": "Sign Out",
                        "url": "#",
                        "onclick": self.handleSignout
                    }
                ];
                
                var templatePath = getFilePath(localrequire, '../../navlinks/navigation-links.html');
                var vmPath = getFilePath(localrequire, '../../navlinks/js/navigation-links.js');
                var cssFile = getFilePath(localrequire, '../../../css/dashboards-common-alta.css'); 
                var htmlDepth = getHtmlDepth();
                var jsDepth = getJsDepth(localrequire);
                var depth = jsDepth - htmlDepth;
                if (depth>=0){
                    cssFile = cssFile.substring("../".length*depth);
                }else{
                    for(var i=0;i>depth;i--){
                       cssFile="../"+cssFile; 
                    }                     
                }

		self.brandingbarCss = cssFile;
                
                //Register a Knockout component for navigation links
                if (!ko.components.isRegistered('df-oracle-nav-links')) {
                    ko.components.register("df-oracle-nav-links",{
                        viewModel:{require:vmPath.substring(0, vmPath.length-3)},
                        template:{require:'text!'+templatePath}
                    });
                }
                
                // Dropdown menu states
                self.selectedMenuItem = ko.observable("(None selected yet)");
                
                self.menuItemSelect = function(event, ui) {
                    switch (ui.item.attr("id")) {
                        case "open":
                            this.selectedMenuItem(ui.item.children("a").text());
                            break;
                        default:
                            // todo;
                    }
                };
                
                self.navLinksNeedRefresh = ko.observable(false);
                
                /**
                * Navigation links button click handler
                */
                self.linkMenuHandle = function(event,item){
                    self.navLinksNeedRefresh(true);
                    $("#links_menu").slideToggle('normal');
                    item.stopImmediatePropagation();
                };

                $('body').click(function(){
                    $("#links_menu").slideUp('normal');
                });  
                
                function getFilePath(requireContext, relPath) {
                    var jsRootMain = requireContext.toUrl("");
                    var path = requireContext.toUrl(relPath);
                    path = path.substring(jsRootMain.length);
                    return path;
                };
                
                function getHtmlDepth(){
                    //Limitation: assume context root has style like /emsaasui/xxxx/, e.g. /emsaasui/emcpdfui/ 
                    var htmlPath = window.location.pathname; //e.g. /emsaasui/emcpdfui/folder/page.html
                    var pathArray = htmlPath.split("/");
                    return pathArray.length-4;
                }
                
                function getJsDepth(requireContext){
                    var jsRootMain = requireContext.toUrl("");
                    var doScan = true;
                    while(doScan){
                        if (jsRootMain.indexOf("../")===0){
                            jsRootMain = jsRootMain.substring(3);
                        }else{
                            doScan = false;
                        }
                    }
                    var jsRootMainArray = jsRootMain.split("/");//e.g. js/oracleBrandingBar/oracleBrandingBarMain
                    return jsRootMainArray.length -1;
                }
                
                
            }
            
            return BrandingBarViewModel;
        });

