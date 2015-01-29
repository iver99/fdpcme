/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['dfutil', 'ojs/ojcore', 'jquery', 'knockout','jqueryui', 'ojs/ojknockout-model'], 
       /*
        * @param {Object} oj 
        * @param {jQuery} $
        */
function(dfu, oj, $, ko)
{
    
(function ()
{ // make sure register is running
    
ko.bindingHandlers.dbsDashboardPanel = {
    init: function(element, valueAccessor) {
        var _value = ko.unwrap(valueAccessor());
        /*
        _value.activated = function(event, data) {
                                console.log('activated');
                            };
        _value.deactivated = function() {
                                console.log('deactivated');
                            };
        _value.deleteClicked = function(event, data) {
                                console.log('delete clicked');
                            };
        _value.navigated = function(event, data) {
                                console.log('nagivate clicked');
                            };*/
        var _data = _value['data'], _dashabord =  _value['data'], _resturl = _dashabord['href'], _dmProperty = 'oj._internalObj', _dProperty = 'attributes';
        
        if (_data[_dmProperty]) //oj.KnockoutUtils.map interanl object
        {
            _dashabord = _data[_dmProperty][_dProperty];
            _resturl = _dashabord['href'];
            _value['dashboard'] = _dashabord;
            _value['baseRestUrl'] = _resturl;
            _value['dashboardModel'] = _data[_dmProperty];
        }
        
        $(element).dbsDashboardPanel(_value);
                
    },
    update: function(element, valueAccessor) {
        /*var value = valueAccessor();
        
        if (ko.unwrap(value))
            element.focus();
        else
            element.blur();*/
    }
};

var TITLE_MAX_LENGTH = 34,
    DESCRIPTION_MAX_LENGTH = 256,
    WIGDET_NAME_MAX_LENGTH = 30,
    DASHBOARD_TYPE_ONE_PAGE = "SINGLEPAGE";


$.widget('dbs.dbsDashboardPanel',
{
        widgetEventPrefix: 'dbs',

        options: {
            activated: null,
            deactivated: null,
            navigated: null,
            deleteClicked: null,
            contentTmplate: null,
            dashboard: null,
            dashboardModel: null,
            data: null,
            baseRestUrl: null
        },
        
        classNames:
        {
            'chover': 'dbs-summary-container-hover',
            'headerTitle': 'dbs-summary-header-title',
            'headerContainer': 'dbs-summary-header-container',
            'headerToolbar': 'dbs-summary-header-toolbar',
            'pages': 'dbs-summary-pages',
            'page': 'dbs-summary-page',
            'pageScroll':' dbs-summary-page-scroll',
            'active': 'active',
            'pageImage': 'dbs-summary-page-image',
            'pageDescription':'dbs-summary-page-description',
            'controlls': 'dbs-summary-controlls',
            'controll': 'dbs-summary-controll'
        },
        
        active: false,
        currentPageNum: undefined,
        
        _activate: function(event, callback) {
            var self = this;
            if (self.active === false)
            {
                var _name = self.name, _dashboard = self.options['dashboard'], _element = self.element, _toolbarEle = self.toolbarElement;
                _element.addClass(self.classNames['active']);
                self.titleElement.addClass(self.classNames['active']);
                _toolbarEle.addClass(self.classNames['active']);
                if (self.options.dashboard.widgets && self.options.dashboard.widgets.length > 4)
                {
                    self.contentPage3Ele.addClass(self.classNames['pageScroll']);
                }
                _element.animate({margin:'-=5px', height:'+=10px', width:'+=10px'},
                {
                    duration: 300,
                    complete: function(){
                     if ($.isFunction(callback)) callback(); 
                    }
                });
                
                
                self.active = true;
                self._trigger('activated', event, _dashboard);
            }
        },
        
        _deactivate: function(event, callback) {
            var self = this;
            if (self.active === true)
            {
                var _element = self.element, _toolbarEle = self.toolbarElement;
                _element.removeClass(self.classNames['active']);
                self.titleElement.removeClass(self.classNames['active']);
                _toolbarEle.removeClass(self.classNames['active']);
                self.contentPage3Ele.removeClass(self.classNames['pageScroll']);
                
                _element.animate({margin:'+=5px', height:'-=10px', width:'-=10px'},
                {
                    duration: 150,
                    complete: function(){
                     if ($.isFunction(callback)) callback(); 
                    }
                });
    
                self.active = false;
                self._trigger('deactivated');
            }
        },
        
        _create: function () {
            var self = this, _element = self.element;
            this._createComponent();
            this._goToPage(this._getCurrentPageNum() || 1);
            this.element.attr("aria-dashboard", this.options['dashboard'].id);
            setTimeout(function() {
                if(_element.is(":hover")) {
                    //_element.css("background", "yellow");
                    //self._activate(null);
                }
                else
                {
                    self._deactivate(null);
                }
            }, 0);
        },

        _createComponent: function () {
            this._createHeader();
            this._createContent();
            //var self = this, _name = self.name, _element = self.element;
            
            var self = this, _name = self.name, _element = self.element;
            self.active = false;
            _element
            .bind('mouseenter.' + _name, function (event) {
                //console.log("mouse enter");
                self._activate(event);
            })
            .bind('mouseleave.' + _name, function (event) {
                self._deactivate(event);
            })
            .bind('click.' + _name, function (event) {
                self._fireNavigated(event);
            });
        },
        
        _truncateString: function(str, length) {
            if (str && length > 0 && str.length > length)
            {
                var _tlocation = str.indexOf(' ', length);
                if ( _tlocation <= 0 ) _tlocation = length;
                return str.substring(0, _tlocation) + "...";
            }
            return str;
        },
        
        _createHeader: function() {
            var self = this, _element = self.element, _name = self.name, 
                     _isSys = self.options.dashboard['systemDashboard']; 
            var _title = (self.options['dashboard']) ? self._truncateString(self.options['dashboard'].name, TITLE_MAX_LENGTH) : '';
            
            self.headerElement = $("<div></div>").addClass(self.classNames['headerContainer']);
            
            // add title
            self.titleElement = $("<div><h2>" + _title + "</h2></div>")
                                  .addClass(self.classNames['headerTitle']);
            self.headerElement.append(self.titleElement); 
            
            // add toolbar
            self.toolbarElement = $("<div></div>").addClass(self.classNames['headerToolbar']);//.attr({'id' : 'toolbar_' + (self.count++)});
            if (_isSys === true)
            {
                self.lockElement = $("<span></span>").attr({"role": "img"})
                        .css({"cursor": "default"})
                        .addClass("dbs-lock-icon-24 dbs-icon oj-sm-float-end")
                        .on('click.'+_name, function(event) {
                            //prevent event bubble
                            event.stopPropagation();
                        });
                self.toolbarElement.append(self.lockElement);
            }
            else
            {
                self.deleteElement = $("<button data-bind=\"ojComponent: { component:'ojButton', display: 'icons', icons: {start:'icon-delete-ena-16 oj-fwk-icon'}}\"></button>")
                    .addClass("oj-button-half-chrome oj-sm-float-end")
                    .on('click.'+_name, function(event) {
                        //prevent event bubble
                        event.stopPropagation();
                        self._fireDeleteClicked(event);
                    }); //$("<span>test</span>")
                self.toolbarElement.append(self.deleteElement);
            }
             
            self.headerElement.append(self.toolbarElement); 
            _element.append(self.headerElement);
            
        },
        
        _createContent: function() {
            this._createContentPages();
            this._createContentControlls();
            var self = this, _element = self.element;
            self.contentElement = 
                    $("<div></div>")
                    .append(self.contentPagesEle)
                    .append(self.contentCtlsEle);
            _element.append(self.contentElement);
        },
        
        _createContentPages: function() {
            var self = this, _dashboard = self.options['dashboard'], _dmodel = self.options.dashboardModel,
                    _wdts = _dashboard['widgets'] || _dashboard['tiles'];
            self.contentPagesEle = $("<div></div>")
                    .addClass(self.classNames['pages']);
            //image page
            self.contentPage1ImgEle = $("<img>").addClass(self.classNames['pageImage']);//.attr('src', _image);
            self.contentPage1Ele = $("<div></div>")//.addClass(self.classNames['active'])
                    .addClass(self.classNames['page']);//.append(self.contentPage1ImgEle);
            self.contentPage1Ele.append(self.contentPage1ImgEle);
            
            
            if (_dmodel['screenShot'])
            {
                var _ss = _dmodel['screenShot'];
                self.contentPage1ImgEle.attr("src", _ss);
            }
            else {
              $.ajax({
                   //This will be a page which will return the base64 encoded string
                   url: self.options['dashboard']['screenShotHref'], 
                   headers: dfu.getDashboardsRequestHeader(),//{"X-USER-IDENTITY-DOMAIN-NAME": getSecurityHeader()},//Pass the required header information
                   success: function(response){
                       var __ss = response;
                       self.contentPage1ImgEle.attr("src", __ss);
                       if (_dmodel && __ss)
                       {
                           //_dmodel.set("screenShot", _ss);
                           _dmodel['screenShot'] = __ss;
                       }
                   },
                   error : function(jqXHR, textStatus, errorThrown) {
                    //console.log("Load image error");
                   }
              });
            }
            self.contentPagesEle.append(self.contentPage1Ele);
            //description page
            var _dtext = self.options.dashboard['description'];
            if (!_dtext) _dtext = '';
            self.contentPage2CntEle = $("<p></p>").addClass(self.classNames['pageDescription']).html(self._truncateString(_dtext, DESCRIPTION_MAX_LENGTH));
            self.contentPage2Ele = $("<div></div>")
                    .addClass(self.classNames['page']).append(self.contentPage2CntEle);
            self.contentPagesEle.append(self.contentPage2Ele);
            //widgets page
            self.contentPage3TlEle = $("<div></div>").text(getNlsString('DBS_HOME_DSB_PANEL_WIDGETS')).css({"text-align": "center", "max-height": "16px", "font-weight":"bold"});//.append("<h6>Widgets</h6>");
            
            self.contentPage3CntEle = $("<ul></ul>").addClass("dbs-summary-rows");
            if (_wdts && _wdts.length > 0)
            {
                $.each(_wdts, function( index, widget ) {
                    self.contentPage3CntEle
                            .append($("<li ></li>")
                            .text(self._truncateString(widget['title'], WIGDET_NAME_MAX_LENGTH)));
                //alert( index + ": " + value ); 
                });
            }
            self.contentPage3Ele = $("<div></div>")
                    .addClass(self.classNames['page'])
                    .append(self.contentPage3TlEle)
                    .append(self.contentPage3CntEle);
            
            self.contentPagesEle.append(self.contentPage3Ele);
        },
        
        _createContentControlls: function() {
            var self = this, _name = self.name;
            self.contentCtlsEle = $("<div></div>")
                    .addClass(self.classNames['controlls']);
            self.contentCtl1Ele = $("<div></div>")
                    .addClass(self.classNames['controll']).append($("<span></span>"))
                    .bind('click.' + _name, function (event) {
                        self._goToPage(1);
                        event.stopPropagation();
                    });
            self.contentCtlsEle.append(self.contentCtl1Ele);
            self.contentCtl2Ele = $("<div></div>")
                    .addClass(self.classNames['controll']).append($("<span></span>"))
                    .bind('click.' + _name, function (event) {
                        self._goToPage(2);
                        event.stopPropagation();
                    });
            self.contentCtlsEle.append(self.contentCtl2Ele);
            self.contentCtl3Ele = $("<div></div>")
                    .addClass(self.classNames['controll']).append($("<span></span>"))
                    .bind('click.' + _name, function (event) {
                        self._goToPage(3);
                        event.stopPropagation();
                    });
            self.contentCtlsEle.append(self.contentCtl3Ele);
        },
        
        _goToPage: function(num) {
            var self = this, _aclass = self.classNames['active'];
            if (num && num > 0 && num <= 3)
            {
                self._removePageFocus();
                switch ( num ) {
                case 1:
                    self.contentPage1Ele.addClass(_aclass);
                    self.contentCtl1Ele.addClass(_aclass);
                    self._setCurrentPageNum(1);
                    break;
                case 2:
                    self.contentPage2Ele.addClass(_aclass);
                    self.contentCtl2Ele.addClass(_aclass);
                    self._setCurrentPageNum(2);
                    break;
                case 3:
                    self.contentPage3Ele.addClass(_aclass);
                    self.contentCtl3Ele.addClass(_aclass);
                    self._setCurrentPageNum(3);
                    break;
                }
            }
        },
        
        _removePageFocus: function() {
            var self = this, _aclass = self.classNames['active'];
            switch ( self.currentPageNum || -1 ) {
                case 1:
                    self.contentPage1Ele.removeClass(_aclass);
                    self.contentCtl1Ele.removeClass(_aclass);
                    //self._setCurrentPageNum(undefined);
                    break;
                case 2:
                    self.contentPage2Ele.removeClass(_aclass);
                    self.contentCtl2Ele.removeClass(_aclass);
                    //self._setCurrentPageNum(undefined);
                    break;
                case 3:
                    self.contentPage3Ele.removeClass(_aclass);
                    self.contentCtl3Ele.removeClass(_aclass);
                    //self._setCurrentPageNum(undefined);
                    break;
            }
        },
        
        _setCurrentPageNum: function(num) {
            this.currentPageNum = num;
            this.options.dashboard['currentPageNum'] = num;
        },
        
        _getCurrentPageNum: function() {
            return this.options.dashboard['currentPageNum'] || this.currentPageNum || undefined; 
        },
        
        _fireDeleteClicked: function(event) {
            this._trigger('deleteClicked', event, {dashboard: this.options['dashboard'], dashboardModel: this.options['dashboardModel']});
        },
        
        _fireNavigated: function(event) {
            var self = this;
            self._trigger('navigated', event, {dashboard: self.options['dashboard'], dashboardModel: this.options['dashboardModel']});
        },
        
        _destroyComponent: function() {
            var self = this;
            if (self.lockElement && self.lockElement.length > 0) self.lockElement.unbind("click." + self.name);
            if (self.deleteElement && self.deleteElement.length > 0) self.deleteElement.unbind("click." + self.name);
            self.element.find("*").removeAttr('style').removeClass().remove();
            self.element.unbind("." + self.name);
            //self.element.unbind("mouseenter." + self.name);
            //self.element.unbind("mouseleave." + self.name);
            //self.element.unbind("click." + self.name);
        },

        _destroy: function () {
            var self = this;
            self._destroyComponent();
           
        },
        
        refresh: function () {
            var self = this;
            if (self.options.dashboardModel)
            {
                self.options.dashboard = self.options.dashboardModel['attributes'];
            }
            this._deactivate(null);
            self._destroyComponent();
            self._createComponent();
            ko.applyBindings({}, self.deleteElement[0]);      
            self._goToPage(self.currentPageNum);
        }
});

}()); // end make sure register is running

});



