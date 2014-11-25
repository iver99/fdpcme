/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
define(['knockout',
        'jquery'
    ],
    
    function(ko,$)
    {
        function DashboardFrameworkUtility() {
            var self = this;
            
            /**
            * Returns a random integer between min (inclusive) and max (inclusive)
            * Using Math.round() will give you a non-uniform distribution!
            */
            self.getRandomInt = function(min,max){
                return Math.floor(Math.random() * (max - min + 1)) + min;
            }

            /**
             * Get URL parameter value according to URL parameter name
             * @param {String} name
             * @returns {parameter value}
             */
            self.getUrlParam = function(name){
                var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
                return results === null ? "" : results[1];                
            }
        }
        
        return new DashboardFrameworkUtility();
    }
);

function df_util_widget_lookup_assetRootUrl(providerName, providerVersion, providerAssetRoot){
    //TODO replace below hard coded values
    if (providerName && providerVersion && providerAssetRoot){
        if ("DB Analytics"===providerName){
            return "http://slc03psa.us.oracle.com:7001/db-analytics-war/html/db-analytics-home.html";
        }else if ("Application Performance Manager Cloud Service"===providerName){
            return "http://slc04srr.us.oracle.com:7401/apmUi/";
        }
    }
    return "http://jet.us.oracle.com";
}

