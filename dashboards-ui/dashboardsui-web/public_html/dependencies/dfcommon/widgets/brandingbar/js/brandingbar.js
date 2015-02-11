/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout', 'jquery', 'dfutil'],
        function (ko, $, dfu) {
            function BrandingBarViewModel(params) {
                dfu.initialize(params.smUrl, params.authToken);
                var ssoLogoutEndUrl =dfu.discoverDFHomeUrl();
                if (!ko.components.isRegistered('nav-links-widget')) {
                    ko.components.register("nav-links-widget",{
                        viewModel:{require:'../dependencies/dfcommon/widgets/navlinks/js/navigation-links'},
                        template:{require:'text!../dependencies/dfcommon/widgets/navlinks/navigation-links.html'}
                    });
                }
                
                var self = this;
            
                self.handleSignout = function() {
                    window.location.href = dfu.discoverLogoutUrl() + "?endUrl=" + ssoLogoutEndUrl;
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
                    ]
                };

                self.appId = appName.id;
                self.appName = appName.name;
                self.cloudName = cloudName;
                self.userName = ko.observable(toolbarData.userName);
                self.toolbarButtons = toolbarData.toolbar_buttons;
                self.globalNavItems = toolbarData.global_nav_dropdown_items;
                self.navLinksNeedRefresh = ko.observable(false);
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

