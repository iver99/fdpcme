/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


define(['knockout', 'timeslider/ita-util'], function(ko, util) {
    
    var registeredTools = {};

    ko.bindingHandlers.itaComponent = {
        init: function(element, valueAccessor, allBindings, viewModel, bindingContext) {
            // This will be called when the binding is first applied to an element
            // Set up any initial state, event handlers, etc. here
            var val = valueAccessor();
            if (val && val.component) {
                var component = registeredTools[val.component];
                if (component && component.init && typeof component.init === 'function') {
                    component.init(element, valueAccessor, allBindings, viewModel, bindingContext);
                }
            }
            return {controlsDescendantBindings: true};
        },
        update: function(element, valueAccessor, allBindings) {
            // Leave as before
            var val = valueAccessor();
            if (val && val.component) {
                var component = registeredTools[val.component];
                if (component && component.update && typeof component.update === 'function') {
                    component.update(element, valueAccessor, allBindings);
                }
            }
        }
    };

    return {
        
        /**
         * Register the tool. So that html elements configured with the 
         * data-bind attribute can be handled by knockout
         * <pre>
         * data-bind="
         *     itaComponent:{
         *         component:'your-tool-name'
         *     }
         * "
         * </pre>
         * @param {type} toolConfig an object contains 'name'(tool name),
         * 'init'(map the knockout init func),'update'(map the knockout update func)
         * @returns {undefined}
         */
        registerTool: function(toolConfig) {
            if (toolConfig && toolConfig.name) {
                registeredTools[toolConfig.name] = {
                    init: toolConfig.init,
                    update: toolConfig.update
                };
            }
        },
        
        /**
         * quick reference for ita util function
         * @see ita-util.js
         */
        util: util
    };
});


