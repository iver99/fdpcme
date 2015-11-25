/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery',
        'ojs/ojcore',
        'uifwk/emcsDependencies/html2canvas/html2canvas',
        'uifwk/emcsDependencies/canvg/rgbcolor',
        'uifwk/emcsDependencies/canvg/StackBlur',
        'uifwk/emcsDependencies/canvg/canvg'],
    function($, oj)
    {
        function ScreenShotUtils() {
            var self = this;
            
            self.screenShot = function(elem_id, width, callback) {
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
                self.setAncestorsOverflowVisible();
                html2canvas($(elem_id), {
                    background: "#fff",
                    onrendered: function(canvas) {
                        try {
                            var resize_canvas = document.createElement('canvas');
                            resize_canvas.width = width;
                            resize_canvas.height = (canvas.height * resize_canvas.width) / canvas.width;
                            var resize_ctx = resize_canvas.getContext('2d');
                            resize_ctx.drawImage(canvas, 0, 0, resize_canvas.width, resize_canvas.height);
                            var data = resize_canvas.toDataURL("image/jpeg", 0.8);
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
            };
        }
        
        return new ScreenShotUtils;
    }
);


