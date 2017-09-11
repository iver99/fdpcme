define(['ojs/ojcore',
    'knockout',
    'jquery',
    'uifwk/@version@/js/util/ajax-util-impl'
],function(oj, ko, $, ajaxUtilModel){
    return {
        run: function(){
            QUnit.module("ajaxUtilTest");
            QUnit.test("getAjaxOptionsTest", function(assert){
               var ajaxUtil = new ajaxUtilModel();
               var argsWithLenOneString = ["/emsaasui/emcpdfui/builder.html"];
               var retryOptions = ajaxUtil.getAjaxOptions(argsWithLenOneString);
               assert.equal(argsWithLenOneString[0],retryOptions.url);
               
               var argsWithLenOneObject = [{"url":"/emsaasui/emcpdfui/builder.html"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenOneObject);
               assert.equal(argsWithLenOneObject[0], retryOptions);
               
               var argsWithLenTwoObject = ["/emsaasui/emcpdfui/builder.html", {"name":"dashboard"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenTwoObject);
               assert.equal("dashboard", retryOptions.name);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);

               var argsWithFunction = function(){return true;}; 
               var argsWithLenTwoFunction = ["/emsaasui/emcpdfui/builder.html", argsWithFunction];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenTwoFunction);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal(argsWithFunction, retryOptions.success);
               
               var argsWithLenTwoNull = ["/emsaasui/emcpdfui/builder.html", null];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenTwoNull);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               
               var argsWithLenThree = ["/emsaasui/emcpdfui/builder.html", argsWithFunction,{"name":"dashboard"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThree);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal(argsWithFunction, retryOptions.success);
               assert.equal("dashboard", retryOptions.name);
               
               var argsWithLenThreeNullObject = ["/emsaasui/emcpdfui/builder.html", argsWithFunction, null];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThree);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal(argsWithFunction, retryOptions.success);
               
               var argsWithLenThreeNullFunction = ["/emsaasui/emcpdfui/builder.html", null, {"name":"dashboard"}];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThreeNullFunction);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal("dashboard", retryOptions.name);
               
               var argsWithLenThreeNullBoth = ["/emsaasui/emcpdfui/builder.html", null, null];
               retryOptions = ajaxUtil.getAjaxOptions(argsWithLenThreeNullFunction);
               assert.equal("/emsaasui/emcpdfui/builder.html", retryOptions.url);
               assert.equal("dashboard", retryOptions.name);
            });
        }
    }
});