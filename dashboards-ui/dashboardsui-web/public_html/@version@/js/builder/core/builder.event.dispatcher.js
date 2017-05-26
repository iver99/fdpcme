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

        self.delayResizeListenerQueue = [];
        self.triggerEvent = function(event, p1, p2, p3, p4) {
            if (event === Builder.EVENT_BUILDER_RESIZE) {
                var delayedEvent = {
                    event: event,
                    p1: p1,
                    p2: p2,
                    p3: p3,
                    p4: p4,
                    timestamp: performance.now()
                };
                self.delayResizeListenerQueue.push(delayedEvent);
                setTimeout(function() {
                    self.executeDelayedEvent();
                }, 500);
            }
            else
                self.executeListenersImmediately(event, p1, p2, p3, p4);
        };
        
        self.executeDelayedEvent = function() {
            var delayedEvent = self.delayResizeListenerQueue.shift();
            if (delayedEvent) {
                if (self.delayResizeListenerQueue.length <= 0) {// execute the delay event now
                    var delay = performance.now() - delayedEvent.timestamp;
                    console.info("Execute delayed event for builder resize after delay of " + delay + "ms");
                    self.executeListenersImmediately(delayedEvent.event, delayedEvent.p1, delayedEvent.p2, delayedEvent.p3, delayedEvent.p4);
                }
            } else {
                console.warn("Unexpected, after delay the delayevent is missing: " + delayedEvent + ", just ingore the event");
            }
        };
        
        self.executeListenersImmediately = function(event, p1, p2, p3, p4) {
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
