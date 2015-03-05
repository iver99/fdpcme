define(['require','knockout', 'jquery', '../../../js/util/df-util'],
        function (localrequire, ko, $, dfumodel) {
            function BrandingBarViewModel(params) {
                var self = this;
                self.userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                self.tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
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
                
                //Open about box
                //aboutbox id
                self.aboutBoxId = 'aboutBox';
                self.openAboutBox = function() {
                    $('#' + self.aboutBoxId).ojDialog('open');
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
                        "onclick": self.openAboutBox
                    },
                    {"label": "Sign Out",
                        "url": "#",
                        "onclick": self.handleSignout
                    }
                ];
                
                var templatePath = getFilePath(localrequire, '../../navlinks/navigation-links.html');
                var vmPath = getFilePath(localrequire, '../../navlinks/js/navigation-links.js');
                var cssFile = getCssFilePath(localrequire, '../../../css/dashboards-common-alta.css'); 

		self.brandingbarCss = cssFile;
                
                //Register a Knockout component for navigation links
                if (!ko.components.isRegistered('df-oracle-nav-links')) {
                    ko.components.register("df-oracle-nav-links",{
                        viewModel:{require:vmPath.substring(0, vmPath.length-3)},
                        template:{require:'text!'+templatePath}
                    });
                }
                
                //Register a Knockout component for about box
                var aboutTemplatePath = getFilePath(localrequire, '../../aboutbox/aboutBox.html');
                var aboutVmPath = getFilePath(localrequire, '../../aboutbox/js/aboutBox.js');
                if (!ko.components.isRegistered('df-oracle-about-box')) {
                    ko.components.register("df-oracle-about-box",{
                        viewModel:{require:aboutVmPath.substring(0, aboutVmPath.length-3)},
                        template:{require:'text!'+aboutTemplatePath}
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
                
                function getCssFilePath(requireContext, relPath) {
                    return requireContext.toUrl(relPath);
                };
            }
            
            return BrandingBarViewModel;
        });

