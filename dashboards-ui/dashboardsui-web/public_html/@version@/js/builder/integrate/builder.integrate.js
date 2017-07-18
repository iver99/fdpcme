/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery',
        'ojs/ojcore',
        'dfutil',
        'builder/builder.core'
    ],
    function($, oj, dfu) {
        function fireDashboardItemChangeEventTo(tile, dashboardItemChangeEvent) {
            var deferred = $.Deferred();
            setTimeout(function () {
                    /**
                     * A widget needs to define its parent's onDashboardItemChangeEvent() method to resposne to dashboardItemChangeEvent
                     */
                    if (tile.onDashboardItemChangeEvent) {
                        tile.onDashboardItemChangeEvent(dashboardItemChangeEvent);
                        console.log(tile.title());
                        oj.Logger.log(tile.title());
                        deferred.resolve();
                    }
            },0);
            return deferred.promise();
        }
        Builder.registerFunction(fireDashboardItemChangeEventTo, 'fireDashboardItemChangeEventTo');

        function fireDashboardItemChangeEvent(tiles, dashboardItemChangeEvent){
            if (!tiles) {
                return;
            }
            if (dashboardItemChangeEvent){
                var defArray = [];
                for (var i = 0; i < tiles.length; i++) {
                    var aTile = tiles[i];
                    defArray.push(Builder.fireDashboardItemChangeEventTo(aTile,dashboardItemChangeEvent));
                }

                var combinedPromise = $.when.apply($,defArray);
                combinedPromise.done(function(){
                    console.log("All Widgets have completed refresh!");
                    oj.Logger.log("All Widgets have completed refresh!");
                });
                combinedPromise.fail(function(ex){
                    console.log("One or more widgets failed to refresh: "+ex);
                    oj.Logger.log("One or more widgets failed to refresh: "+ex);
                });
            }
        }
        Builder.registerFunction(fireDashboardItemChangeEvent, 'fireDashboardItemChangeEvent');
    }
);

