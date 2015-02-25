define(['require','knockout', 'jquery', '../../../js/util/df-util'],
        function (localrequire, ko, $, dfumodel) {
            function BrandingBarViewModel(params) {
                var self = this;
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                self.registryUrl = $.isFunction(params.registryUrl) ? params.registryUrl() : params.registryUrl;
                self.authToken = $.isFunction(params.authToken) ? params.authToken() : params.authToken;
                self.productName = "Enterprise Manager";
                self.cloudName = "Cloud Service";
                self.appName = $.isFunction(params.appName) ? params.appName() : params.appName;
                self.userTenantName = self.userName && self.tenantName ? self.userName + " (" + self.tenantName + ")" : "emaas.user@oracle.com";
                self.globalNavItems = [
                    {"label": "preferences",
                        "url": "#",
                        "onclick": ""
                    },
                    {"label": "help",
                        "url": "#",
                        "onclick": ""
                    },
                    {"label": "about",
                        "url": "#",
                        "onclick": ""
                    },
                    {"label": "sign out",
                        "url": "#",
                        "onclick": self.handleSignout
                    }
                ];
                
                var dfu = new dfumodel(self.userName, self.tenantName);
                var ssoLogoutEndUrl =dfu.discoverDFHomeUrl(self.registryUrl, self.authToken);
                var templatePath = getFilePath(localrequire, '../../navlinks/navigation-links.html');
                var vmPath = getFilePath(localrequire, '../../navlinks/js/navigation-links.js');
                var cssFile = getFilePath(localrequire, '../../../css/dashboards-common-alta.css');
                self.brandingbarCss = cssFile && cssFile.indexOf('../')===0 ? cssFile.substring(3) : cssFile;
                
                //Register a Knockout component for navigation links
                if (!ko.components.isRegistered('df-oracle-nav-links')) {
                    ko.components.register("df-oracle-nav-links",{
                        viewModel:{require:vmPath.substring(0, vmPath.length-3)},
                        template:{require:'text!'+templatePath}
                    });
                }
                
                /**
                * SSO logout handler
                */
                self.handleSignout = function() {
                    window.location.href = dfu.discoverLogoutUrl(registryUrl, authToken) + "?endUrl=" + ssoLogoutEndUrl;
                };

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
                    var pathArray;
                    pathArray = requireContext.toUrl(relPath).split('/');
                    pathArray.shift();
                    return pathArray.join('/');
                };
            }
            
            return BrandingBarViewModel;
        });

