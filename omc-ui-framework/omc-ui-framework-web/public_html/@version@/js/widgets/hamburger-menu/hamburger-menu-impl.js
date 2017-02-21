define('uifwk/@version@/js/widgets/hamburger-menu/hamburger-menu-impl', ['knockout', 
    'jquery', 
    'uifwk/@version@/js/util/df-util-impl', 
    'ojs/ojcore', 
    'uifwk/@version@/js/util/preference-util-impl', 
    'uifwk/@version@/js/sdk/context-util-impl',
    'ojs/ojnavigationlist',
    'ojs/ojjsontreedatasource'],
        function (ko, $, dfumodel, oj, pfu, contextModel) {
            function HamburgerMenuViewModel(params) {
                var self = this;
                var cxtUtil = new contextModel();
                var dfHomeUrl = null;
                var dfWelcomeUrl = null;
                var dfDashboardsUrl = null;
                var userName = $.isFunction(params.userName) ? params.userName() : params.userName;
                var tenantName = $.isFunction(params.tenantName) ? params.tenantName() : params.tenantName;
                var dfu = new dfumodel(userName, tenantName);
                var nlsStrings = params.nlsStrings ? params.nlsStrings : {};
                var appMap = params.appMap;
                
                var data = [
        {"attr": {"id": "Home",
			   "name": "Home",
			   "link": "#"
			  }
	 },
        {"attr": {"id": "Alerts",
			   "name": "Alerts",
			   "link": "#"
			  }
	},
        {"attr": {"id": "Applications",
			   "name": "Applications",
			   "link": "#"
			  }
	},
        {"attr": {"id": "Dashboards",
			   "name": "Dashboards",
			   "link": "#"
			  }
	},
        {"attr": {"id": "Saved_Searches",
			   "name": "Saved Searches",
			   "link": "#"
			  }
        },
        {"attr": {"id": "Data_Explorer",
			   "name": "Data Explorer",
			   "link": "#"
			  }
        },
        {"attr": {"id": "randomId_1",
			   "name": "hr",
			   "link": "#"
			  }
        },
        {"attr": {"id": "APM",
			   "name": "Application Performance Monitoring",
			   "link": "#"
			  },
	   "children": [
			{"attr": {"id": "APM_Home",
                            "name": "Home",
                            "link": "#"
                           },
                           "children":[
                                {"attr": {"id": "_APM",
                                            "name": "Application Performance Monitoring",
                                            "link": "#"
                                        },
                                        "children": [
                                                     {"attr": {"id": "_APM_Home",
                                                         "name": "Home",
                                                         "link": "#"
                                                        },
                                                        "children":[
                                                            {"attr": {"id": "_APM_Home2",
                                                                "name": "Home",
                                                                "link": "#"
                                                               }
                                                           }
                                                        ]
                                                     },
                                                     {"attr": {"id": "_APM_Alerts",
                                                             "name": "Alerts",
                                                             "link": "#"
                                                         }
                                                     },
                                                     {"attr": {"id": "_APM_Applications",
                                                             "name": "Applications",
                                                             "link": "#"
                                                         }
                                                     }
                                              ]
                                }
                           ]
                        },
                        {"attr": {"id": "APM_Alerts",
                                "name": "Alerts",
                                "link": "#"
                            }
                        },
                        {"attr": {"id": "APM_Applications",
                                "name": "Applications",
                                "link": "#"
                            }
                        }
		 ]
	 },
         {"attr": {"id": "APM2",
			   "name": "Application Performance Monitoring",
			   "link": "#"
			  },
	   "children": [
			{"attr": {"id": "APM_Home2",
                            "name": "Home",
                            "link": "#"
                           }
                        },
                        {"attr": {"id": "APM_Alerts2",
                                "name": "Alerts",
                                "link": "#"
                            }
                        },
                        {"attr": {"id": "APM_Applications2",
                                "name": "Applications",
                                "link": "#"
                            }
                        }
		 ]
	 },
         {"attr": {"id": "APM3",
			   "name": "Application Performance Monitoring",
			   "link": "#"
			  },
	   "children": [
			{"attr": {"id": "APM_Home3",
                            "name": "Home",
                            "link": "#"
                           }
                        },
                        {"attr": {"id": "APM_Alerts3",
                                "name": "Alerts",
                                "link": "#"
                            }
                        },
                        {"attr": {"id": "APM_Applications3",
                                "name": "Applications",
                                "link": "#"
                            }
                        }
		 ]
	 },
         {"attr": {"id": "APM4",
			   "name": "Application Performance Monitoring",
			   "link": "#"
			  },
	   "children": [
			{"attr": {"id": "APM_Home4",
                            "name": "Home",
                            "link": "#"
                           }
                        },
                        {"attr": {"id": "APM_Alerts4",
                                "name": "Alerts",
                                "link": "#"
                            }
                        },
                        {"attr": {"id": "APM_Applications4",
                                "name": "Applications",
                                "link": "#"
                            }
                        }
		 ]
	 }
];
            self.dataSource =  new oj.JsonTreeDataSource(data);
            
            self.initialize = function(){
                resizeMenuHeight();
                $(window).resize(function() {
                    resizeMenuHeight();
                });
            };
            
            function resizeMenuHeight(){
                var windowH = $(window).height();
                var brandingbarH = 52;
                $("#hamburger-menu").height(windowH - brandingbarH);
                
            }

            self.initialize();
            }
            return HamburgerMenuViewModel;
        });

