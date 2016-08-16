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
            dfu.ajaxWithRetry({url: 'widgetLoading.html',
                tile: tile,
                success: function () {
                    /**
                     * A widget needs to define its parent's onDashboardItemChangeEvent() method to resposne to dashboardItemChangeEvent
                     */
                    if (this.tile.onDashboardItemChangeEvent) {
                        this.tile.onDashboardItemChangeEvent(dashboardItemChangeEvent);
                        console.log(this.tile.title());
                        oj.Logger.log(this.tile.title());
                        deferred.resolve();
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(textStatus);
                    oj.Logger.log(textStatus);
                    deferred.reject(textStatus);
                }
            });
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

