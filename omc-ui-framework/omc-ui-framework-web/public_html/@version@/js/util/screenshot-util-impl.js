/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery',
        'uifwk/libs/@version@/js/oraclejet/js/libs/oj/v1.1.2/min/ojcore',
        'uifwk/libs/@version@/js/html2canvas/html2canvas',
        'uifwk/libs/@version@/js/canvg/rgbcolor',
        'uifwk/libs/@version@/js/canvg/StackBlur',
        'uifwk/libs/@version@/js/canvg/canvg'],
    function($, oj)
    {
        function ScreenShotUtils() {
            this.getBase64ScreenShot = function(elem_id, width, quality, callback) {
                var nodesToRecover = [], nodesToRemove = [], overflowElems = [], parents = $(elem_id).parents();
                parents && parents.each(function() {
                    if ($(this).css("overflow") && $(this).css("overflow") !== "visible") {
                        overflowElems.push({element: $(this), field: "overflow", value: $(this).css("overflow")});
                        $(this).css("overflow", "visible");
                    }
                    if ($(this).css("overflow-x") && $(this).css("overflow-x") !== "visible") {
                        overflowElems.push({element: $(this), field: "overflow-x", value: $(this).css("overflow-x")});
                        $(this).css("overflow-x", "visible");
                    }
                    if ($(this).css("overflow-y") && $(this).css("overflow-y") !== "visible") {
                        overflowElems.push({element: $(this), field: "overflow-y", value: $(this).css("overflow-y")});
                        $(this).css("overflow-y", "visible");
                    }
                });
                $(elem_id).find('svg').each(function(idx, node) {
                    var parentNode = node.parentNode, nodeWidth = $(node).width(), nodeHeight = $(node).height();
                    var svg = '<svg width="' + nodeWidth + 'px" height="' + nodeHeight + 'px">' + node.innerHTML + '</svg>';
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
                html2canvas($(elem_id), {
                    background: "#fff",
                    onrendered: function(canvas) {
                        try {
                            var resize_canvas = document.createElement('canvas');
                            resize_canvas.width = width;
                            resize_canvas.height = (canvas.height * resize_canvas.width) / canvas.width;
                            var resize_ctx = resize_canvas.getContext('2d');
                            resize_ctx.drawImage(canvas, 0, 0, resize_canvas.width, resize_canvas.height);
                            var data = resize_canvas.toDataURL("image/jpeg", quality);
                            nodesToRemove.forEach(function(pair) {
                            	pair.parent.removeChild(pair.child);
                            });
                            nodesToRecover.forEach(function(pair) {
                                pair.parent.appendChild(pair.child);
                            });
                            overflowElems.forEach(function(elem) {
                                elem.element.css(elem.field, elem.value);
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


