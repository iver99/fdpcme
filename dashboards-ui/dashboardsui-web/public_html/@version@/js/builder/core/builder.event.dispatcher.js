/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define([], function() {
    function Dispatcher() {
        var self = this;
        self.queue = [];

        self.registerEventHandler = function(event, handler) {
            if (!event || !handler){
                return;
            }
            if (!self.queue[event]){
                self.queue[event] = [];
            }
            if (self.queue[event].indexOf(handler) !== -1){
                return;
            }
            self.queue[event].push(handler);
            window.DEV_MODE && console.debug('Dashboard builder event registration. [Event]' + event + ' [Handler]' + handler);
        };

        self.triggerEvent = function(event, p1, p2, p3, p4) {
            if (!event || !self.queue[event]){
                return;
            }
            for (var i = 0; i < self.queue[event].length; i++) {
                self.queue[event][i](p1, p2, p3, p4);
            }
        };
    }

    return {"Dispatcher": Dispatcher};
});
