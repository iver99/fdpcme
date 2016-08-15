/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['jquery',
//        'uifwk/libs/@version@/js/oraclejet/js/libs/oj/v2.0.2/min/ojcore',
        'uifwk/libs/@version@/js/html2canvas/html2canvas',
        'uifwk/libs/@version@/js/canvg/rgbcolor',
        'uifwk/libs/@version@/js/canvg/StackBlur',
        'uifwk/libs/@version@/js/canvg/canvg'],
    function($)
    {
        function ScreenShotUtils() {
            this.getBase64ScreenShot = function(elem_id, target_width, target_height, quality, callback) {
                // if elem_id is already a jquery object, just take it as $elemInst
                var $elemInst = elem_id instanceof $ ? elem_id : $(elem_id);

                if (isNaN(target_width) || target_width <= 0)
                    throw new RangeError("Invalid target screenshot width");
                if (isNaN(target_height) || target_height <= 0)
                    throw new RangeError("Invalid target screenshot height");
                if (isNaN(quality))
                    throw new RangeError("Invalid target screenshot quality");
                var nodesToRecover = [], nodesToRemove = [], overflowElems = [], parents = $elemInst.parents();
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
                $elemInst.find('svg').each(function(idx, node) {
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

                var optWidth = $elemInst.width(), optHeight = $elemInst.height();
                var ratio = target_width / target_height;
                var optRatio = optWidth / optHeight;
                if (optRatio >= ratio) {
                    optHeight = (optWidth * target_height) / target_width;
                } else {
                    optWidth = (optHeight * target_width) / target_height;
                }

                html2canvas($elemInst, {
                    background: "#fff",
                    logging: true,
                    width: optWidth,
                    height: optHeight,
                    onrendered: function(canvas) {
                        try {
                            var resize_canvas = document.createElement('canvas');
                            resize_canvas.width = target_width;
                            resize_canvas.height = target_height;
                            var ratio = target_width / target_height;
                            var canvasRatio = canvas.width / canvas.height;
                            var  swidth, sheight;
                            if (canvasRatio >= ratio) {
                                sheight = canvas.height;
                                swidth = (sheight * target_width) / target_height;
                            }
                            else {
                                swidth = canvas.width;
                                sheight = (swidth * target_height) / target_width;
                            }
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

            this.getBase64PartialScreenShot = function(elem_id, src_left, src_top, src_width, src_height, resizing_ratio, quality, callback) {
                // if elem_id is already a jquery object, just take it as $elemInst
                var $elemInst = elem_id instanceof $ ? elem_id : $(elem_id);
                if (isNaN(quality) || quality <= 0 || quality > 1)
                    throw new RangeError("Invalid target screenshot quality");
                if (isNaN(resizing_ratio) || resizing_ratio <= 0 || resizing_ratio > 1)
                    throw new RangeError("Invalid resizing ratio");
                var nodesToRecover = [], nodesToRemove = [], overflowElems = [], parents = $elemInst.parents();
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
                $elemInst.find('svg').each(function(idx, node) {
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
                if (isNaN(src_left) || src_left < 0 || src_left >= $elemInst.width())
                    throw new RangeError("Invalid source left position for screenshot capturing");
                if (isNaN(src_top) || src_top < 0 || src_top >= $elemInst.height())
                    throw new RangeError("Invalid source left position for screenshot capturing");
                if (isNaN(src_width) || src_width <= 0 || src_width >= $elemInst.width() - src_left)
                    throw new RangeError("Invalid source width for screenshot capturing");
                if (isNaN(src_height) || src_height <= 0 || src_height >= $elemInst.height() - src_top)
                    throw new RangeError("Invalid source height for screenshot capturing");
                html2canvas($elemInst, {
                    background: "#fff",
                    onrendered: function(canvas) {
                        try {
                            var resize_canvas = document.createElement('canvas');
                            var target_width = src_width * resizing_ratio;
                            var target_height = src_height * resizing_ratio;
                            resize_canvas.setAttribute('height', target_height + 'px');
                            resize_canvas.setAttribute('width', target_width + 'px');
                            var resize_ctx = resize_canvas.getContext('2d');
                            resize_ctx.drawImage(canvas, src_left, src_top, src_width, src_height, 0, 0, target_width, target_height);
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

        return new ScreenShotUtils();
    }
);


