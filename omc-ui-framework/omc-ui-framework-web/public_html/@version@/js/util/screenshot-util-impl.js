/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery',
//        'uifwk/libs/@version@/js/oraclejet/js/libs/oj/v1.2.0/min/ojcore',
        'uifwk/libs/@version@/js/html2canvas/html2canvas',
        'uifwk/libs/@version@/js/canvg/rgbcolor',
        'uifwk/libs/@version@/js/canvg/StackBlur',
        'uifwk/libs/@version@/js/canvg/canvg'],
    function($)
    {
        function ScreenShotUtils() {
            this.getBase64ScreenShot = function(elem_id, target_width, target_height, quality, callback) {
                if (isNaN(target_width) || target_width <= 0) 
                    throw new RangeError("Invalid target screenshot width");
                if (isNaN(target_height) || target_height <= 0)
                    throw new RangeError("Invalid target screenshot height");
                if (isNaN(quality))
                    throw new RangeError("Invalid target screenshot quality");
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
                        console.error(e);
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
                            resize_canvas.width = target_width, resize_canvas.height = target_height;
                            var ratio = target_width / target_height;
                            var canvasRatio = canvas.width / canvas.height;
                            var swidth, sheight;
                            if (canvasRatio >= ratio) {
                                sheight = canvas.height;
                                swidth = (sheight * target_width) / target_height;
                            }
                            else {
                                swidth = canvas.width;
                                sheight = (swidth * target_height) / target_width;
                            }
//                            window.DEV_MODE && console.debug("Capturing screenshot. Expecteds size [" + target_width + "x" + target_height + "]. Page size [" + canvas.width + "x" + canvas.height + "] (captured size [" + swidth + "x" + sheight + "]).");
                            var resize_ctx = resize_canvas.getContext('2d');
                            resize_ctx.drawImage(canvas, 0, 0, swidth, sheight, 0, 0, target_width, target_height);
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
                            console.error(e);
                        }
                    }
                });
            };
        }
        
        return new ScreenShotUtils;
    }
);


