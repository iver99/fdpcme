/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['require','knockout', 'jquery', '../../../js/util/df-util'],
        function (localrequire, ko, $, dfumodel) {
            function BrandingBarViewModel(params) {
                var dfu = new dfumodel();
                var ssoLogoutEndUrl =dfu.discoverDFHomeUrl(params.registryUrl,params.authToken);
                var registryUrl = params.registryUrl;
                var authToken = params.authToken;
                var templatePath = getFilePath(localrequire, '../../navlinks/navigation-links.html');
                var vmPath = getFilePath(localrequire, '../../navlinks/js/navigation-links.js');
                if (!ko.components.isRegistered('nav-links-widget')) {
                    ko.components.register("nav-links-widget",{
//                        viewModel:{require:'../dependencies/dfcommon/widgets/navlinks/js/navigation-links'},
//                        template:{require:'text!../dependencies/dfcommon/widgets/navlinks/navigation-links.html'}
                        viewModel:{require:vmPath.substring(0, vmPath.length-3)},
                        template:{require:'text!'+templatePath}
                    });
                }
                
                function getFilePath(requireContext, relPath) {
                    var pathArray;
                    pathArray = requireContext.toUrl(relPath).split('/');
                    pathArray.shift();
                    return pathArray.join('/');
                };
                
                var self = this;
            
                self.handleSignout = function() {
                    window.location.href = dfu.discoverLogoutUrl(params.smUrl,params.authToken) + "?endUrl=" + ssoLogoutEndUrl;
                };

                // 
                // Dropdown menu states
                // 
                self.selectedMenuItem = ko.observable("(None selected yet)");

                self.menuItemSelect = function(event, ui) {
                    switch (ui.item.attr("id")) {
                        case "open":
                            this.selectedMenuItem(ui.item.children("a").text());
                            break;
                        default:
                            // this.selectedMenuItem(ui.item.children("a").text());
                    }
                };

                // Data for application name
                var appName = {
                    "id": "qs",
                    "name": "Enterprise Manager"
                };

                var cloudName ="Cloud Service";
                // 
                // Toolbar buttons
                // 
                var toolbarData = {
                    // user name in toolbar
                    "userName": function() {
                        var userName = dfu.getUserName();
                        if (userName)
                            return userName + " (" + dfu.getTenantName() + ")";
                        return "emaas.user@oracle.com";
                    }(),
                    "toolbar_buttons": [
                        {
                            "label": "toolbar_button1",
                            "iconClass": "demo-palette-icon-24",
                            "url": "#"
                        },
                        {
                            "label": "toolbar_button2",
                            "iconClass": "demo-gear-icon-16",
                            "url": "#"
                        }
                    ],
                    // Data for global nav dropdown menu embedded in toolbar.
                    "global_nav_dropdown_items": [
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
                    ]
                };

                self.appId = appName.id;
                self.appName = appName.name;
                self.cloudName = cloudName;
                self.userName = ko.observable(toolbarData.userName);
                self.toolbarButtons = toolbarData.toolbar_buttons;
                self.globalNavItems = toolbarData.global_nav_dropdown_items;
                self.navLinksNeedRefresh = ko.observable(false);
                self.registryUrl = ko.observable(registryUrl);
                self.authToken = ko.observable(authToken);
                self.linkMenuHandle = function(event,item){
                    self.navLinksNeedRefresh(true);
                    $("#links_menu").slideToggle('normal');
                    item.stopImmediatePropagation();
                };

                $('body').click(function(){
                    $("#links_menu").slideUp('normal');
                });      
            }
            return BrandingBarViewModel;
        });

