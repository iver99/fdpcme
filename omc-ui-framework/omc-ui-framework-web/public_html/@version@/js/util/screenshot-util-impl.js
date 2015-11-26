/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery',
        'ojs/ojcore',
        'uifwk/libs/@version@/js/html2canvas/html2canvas',
        'uifwk/libs/@version@/js/canvg/rgbcolor',
        'uifwk/libs/@version@/js/canvg/StackBlur',
        'uifwk/libs/@version@/js/canvg/canvg'],
    function($, oj)
    {
        function ScreenShotUtils() {
            var self = this;
            
            self.screenShot = function(elem_id, width, quality, callback) {
                console.debug("Element width is: " + $(elem_id).width());
                console.debug("Element height is: " + $(elem_id).height());
                var nodesToRecover = [];
                var nodesToRemove = [];
                var elems = $(elem_id).find('svg');
                elems.each(function(index, node) {
                    var parentNode = node.parentNode;
                    var width = $(node).width();
                    var height = $(node).height();
                    var svg = '<svg width="' + width + 'px" height="' + height + 'px">' + node.innerHTML + '</svg>';
                    var canvas = document.createElement('canvas');
                    try {
                        canvg(canvas, svg);
                    } catch (e) {
                        oj.Logger.error(e);
                    }
                    nodesToRecover.push({
                        parent: parentNode,
                        child: node
                    });
                    parentNode.removeChild(node);
                    nodesToRemove.push({
                        parent: parentNode,
                        child: canvas
                    });
                    parentNode.appendChild(canvas);
                });
                $("#tiles-col-container").css("overflow", "visible");
                $("body").css("overflow", "visible");
                $("html").css("overflow", "visible");
                var elemWidth = $(elem_id).width, elemHeight=$(elem_id).height;
                html2canvas($(elem_id), {
                    background: "#fff",
                    onrendered: function(canvas) {
                        try {
                            var resize_canvas = document.createElement('canvas');
                            resize_canvas.width = width;
                            resize_canvas.height = (elemHeight * resize_canvas.width) / elemWidth;
                            var resize_ctx = resize_canvas.getContext('2d');
                            resize_ctx.drawImage(canvas, 0, 0, resize_canvas.width, resize_canvas.height);
                            var data = resize_canvas.toDataURL("image/jpeg", quality);
                            nodesToRemove.forEach(function(pair) {
                            	pair.parent.removeChild(pair.child);
                            });
                            nodesToRecover.forEach(function(pair) {
                                pair.parent.appendChild(pair.child);
                            });
                            callback(data);
                        } catch (e) {
                            oj.Logger.error(e);
                        }
                    }
                });
                $("body").css("overflow", "hidden");
                $("html").css("overflow", "hidden");
                $("#tiles-col-container").css("overflow-x", "hidden");
                $("#tiles-col-container").css("overflow-y", "auto");
            };
        }
        
        return new ScreenShotUtils;
    }
);


