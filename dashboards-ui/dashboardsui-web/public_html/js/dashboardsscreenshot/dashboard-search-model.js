/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'ojs/ojcore',
        'jquery',
        'jqueryui',
        'ojs/ojknockout',
        'ojs/ojmenu'
    ],
    
    function(ko)
    {
        function SearchObject(name, description, url, chartType) {
            var self = this;
            self.name = ko.observable(name);
            self.description = ko.observable(description);
            self.url = ko.observable(url);
            self.chartType = ko.observable(chartType);
        }

        function DashboardSearchesViewModel() {
            var self = this;
            self.viewWidth = ko.observable(2);

            self.searches = ko.observableArray([
                new SearchObject("New Search (Bar)", "Create new tile from search (bar chart)", "http://localhost:8383/emcpssf/dataVisualization.html", "bar"),
                new SearchObject("New Search (Line)", "Create new tile from search (line chart)", "http://localhost:8383/emcpssf/dataVisualization.html", "line")
            ]);
            
            self.viewClass = ko.computed(function() {
                if (self.viewWidth() > 0) {
                    return "oj-md-" + self.viewWidth();
                } else {
                    return "dbd-col-0";
                }
            });
            
            self.toggleViewWidth = function() {
                if (self.viewWidth() === 2)
                    self.viewWidth(0);
                else
                    self.viewWidth(2);
            };
            
            self.getJson = function(node, fn)       // get local json
            {
                    var data = [
                        {
                            "title": "All searches",
                            "attr": {"id": "searches"},
                            "children": [
                                {"title": "New Search (Bar)",
                                 "attr": {"id": "bar", "name": "New Search (Bar)", "url": "http://localhost:8383/emcpssf/dataVisualization.html", "chartType": "bar"}
                                },
                                {"title": "New Search (Line)",
                                 "attr": {"id": "line", "name": "New Search (Line)", "url": "http://localhost:8383/emcpssf/dataVisualization.html", "chartType": "line"}
                                }
                            ]
                        }
                    ];

                    fn(data);  // pass to ojTree using supplied function
            };
        }
        
        return {"DashboardSearchesViewModel": DashboardSearchesViewModel};
    }
);
