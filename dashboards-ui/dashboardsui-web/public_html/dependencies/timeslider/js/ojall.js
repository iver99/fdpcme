/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

define(['ojs/ojcore', 'ojs/ojknockout', 'ojs/ojcomponents',
    'ojs/ojchart', 'ojs/ojselectcombobox', 'ojs/ojdatetimepicker', 'ojs/ojmenu', 'ojs/ojtabs',
    'ojs/ojtable', 'ojs/ojdatagrid', 'ojs/ojdatagrid-model'], function(oj) {

    
    try {
        /**
         * A patch for fixing the problem that time stamp are not available after click on the xAxis
         */
        DvtTimeAxisInfo.prototype._generateLabels = function(context) {
            var labels1 = [];
            var labels2 = [];
            var labelInfos1 = [];
            var coords1 = [];
            var coords2 = [];
            var prevDate = null;
            var c1 = 0;// number of level 1 labels
            var c2 = 0;// number of level 2 labels
            var container = context.getStage(context);
            var isRTL = DvtAgent.isRightToLeft(context);
            var isVert = (this.Position == 'left' || this.Position == 'right');
            var scrollable = this.Options['zoomAndScroll'] != 'off';

            if (scrollable)
              var first = true;

            // Bug #17046187 : On Chrome, creating a gap value to be used for spacing level1 labels and level2 labels
            var levelsGap = 0;
            if (isVert && DvtAgent.isBrowserChrome()) {
              levelsGap = parseInt(this.Options['tickLabel']['style'].getStyle('font-size')) * .2;
            }
            // Find the time positions where labels are located
            var times = [];
            var minSkip = 0;
            if (this._step != null) {
              times = DvtTimeAxisInfo._getLabelPositions(this.MinValue, this.MaxValue, this._step);
            }
            else if (this.Options['timeAxisType'] == 'mixedFrequency') {
              this._step = this._getMixedFrequencyStep();
              times = DvtTimeAxisInfo._getLabelPositions(this.MinValue, this.MaxValue, this._step);
              minSkip = Math.floor(this._averageInterval / this._step) - 1; // to avoid label overcrowding
            }
            else {
              for (var i = 0; i < this._groups.length; i++) {
                if (this._groups[i] >= this.MinValue && this._groups[i] <= this.MaxValue)
                  times.push(this._groups[i]);
              }
              this._step = this._averageInterval;

              // Check the width of the first level1 label. If we expect that we'll have more group labels than we can fit in the
              // available space, then render the time labels at a regular interval (using mixed freq algorithm).
              var firstLabel = new DvtOutputText(context, this._formatAxisLabel(new Date(times[0]))[0], 0, 0);
              var labelWidth = isVert ? DvtTextUtils.guessTextDimensions(firstLabel).h : firstLabel.measureDimensions().w;
              var totalWidth = (labelWidth + this.GetTickLabelGapSize()) * (times.length - 1);
              var availWidth = Math.abs(this._minCoord - this._maxCoord);
              if (totalWidth > availWidth) {
                this._step = this._getMixedFrequencyStep();
                times = DvtTimeAxisInfo._getLabelPositions(this.MinValue, this.MaxValue, this._step);
                minSkip = Math.floor(this._averageInterval / this._step) - 1; // to avoid label overcrowding
              }
            }

            if (times.length == 0)
              times = [this.MinValue]; // render at least one label

            // Create and format the labels
            for (var i = 0; i < times.length; i++) {
              var time = times[i];
              var coord = this.getCoordAt(time);
              if (coord == null)
                continue;

              var date = new Date(time);
              var twoLabels = this._formatAxisLabel(date, prevDate);

              var label1 = twoLabels[0];
              var label2 = twoLabels[1];
              //level 1 label
              if (label1 != null) {
                // If level 2 exists put a levelsGap space between labels. levelsGap is only non-zero on Chrome.
                var lb1Instance = {text: label1, coord: (label2 != null ? coord + levelsGap : coord)};
                // workaround for time drilldown by phoebe.zhou@oracle.com! weave the timestamp info to the label
                //[Start]
                lb1Instance._timestamp = time;
                
                if('rgb(0, 100, 255)'=== this.Options['tickLabel']['style'].getStyle('color')){
                    //lb1Instance.text="_"+lb1Instance.text+"_";
                    this.Options['tickLabel']['style'].setStyle("text-decoration",'underline');
                }
                //[End]
                
                labelInfos1.push(lb1Instance);
                coords1.push(coord);

              }
              else {
                labelInfos1.push(null);
                coords1.push(null);
              }
              // Defer label1 creation for now for performance optimization.
              // Only the labels we expect not to skip will be created in skipLabelsUniform().
              labels1.push(null);

              if (scrollable) {
                if (first) {
                  coord = this.MinValue ? this.getCoordAt(this.MinValue) : coord;
                  first = false;
                }
              }

              //level 2 label
              if (label2 != null) {
                var text = this.CreateLabel(context, label2, label2 != null ? coord - levelsGap : coord);
                coords2.push(coord);
                if (!isVert) //set alignment now in order to determine if the labels will overlap
                  isRTL ? text.alignRight() : text.alignLeft();
                labels2.push(text);
                this._isOneLevel = false;
              }
              else {
                labels2.push(null);
                coords2.push(null);
              }

              prevDate = date;
            }

            // skip level 1 labels every uniform interval
            c1 = this._skipLabelsUniform(labelInfos1, labels1, container, minSkip);
            for (var j = 0; j < labelInfos1.length; j++) {
                if (labelInfos1[j] != null && !labelInfos1[j].skipped) {
                    labels1[j]._timestamp = labelInfos1[j]._timestamp;
                }
            }
            // skip level 2 labels greedily
            c2 = this._skipLabelsGreedy(labels2, this.GetLabelDims(labels2, container));

            if (!scrollable && c2 > 1 && c1 <= 1.5 * c2) {
              // too few level 1 labels
              labels1 = labels2;
              labels2 = null;
              // center align the new level1 labels
              for (var j = 0; j < labels1.length; j++) {
                if (labels1[j] != null)
                  labels1[j].alignCenter();
              }
            }

            if (isVert && labels2 != null)
              this._skipVertLabels(labels1, labels2, container);

            this._level1Labels = labels1;
            this._level2Labels = labels2;
            // Store coordinates of labels for gridline rendering
            this._level1Coords = coords1;
            this._level2Coords = coords2;
          };
;
    } catch(ex) {
        console.warn(ex);
    }
    
    window.oj = oj;
    return oj;
});