define(['knockout',
        'jquery'
    ],
    
    function(ko, $)
    {
        function DashboardFrameworkUtility(userName, tenantName) {
            var self = this;
            self.userName = userName;
            self.tenantName = tenantName;
            
            /**
             * Catenate root and path to build full path
             * e.g.
             * root=http://host:port/root/
             * path=home.html
             * output=http://host:port/root/home.html
             * 
             * root=http://host:port/root
             * path=home.html
             * output=http://host:port/root/home.html
             * 
             * root=http://host:port/root/
             * path=/home.html
             * output=http://host:port/root/home.html
             * 
             * @param {String} root
             * @param {String} path
             * @returns {String}
             */
            self.buildFullUrl=function(root, path){
                if (root===null || root===undefined){
                    console.log("Warning: root is null, path="+path);
                    return path;
                }
                
                if (path===null || path===undefined){
                    console.log("Warning: path is null, root="+root);
                    return root;
                }
                
                if (typeof root==="string" && typeof path==="string"){
                    var expRoot = root;
                    var expPath = path;
                    if (root.charAt(root.length-1)==='/'){
                        expRoot = root.substring(0,root.length-1);
                    }
                    if (path.charAt(0)==='/'){
                        expPath = path.substring(1);                        
                    }
                    return expRoot+"/"+expPath;
                }else{
                    return root+"/"+path;
                }
            };

            /**
             * 
             * Discover URL from Service Manager Registry
             * @param {String} serviceName
             * @param {String} version
             * @param {String} rel
             * @param {String} smUrl
             * @returns {String} result
             */
            self.discoverUrl = function(serviceName, version, rel){
                if (serviceName===null || serviceName===undefined){
                    console.log("Error: Failed to discovery URL, serviceName="+serviceName);
                    return null;
                }
                if (version===null || version===undefined){
                    console.log("Error: Failed to discovery URL, version="+version);
                    return null;
                }

                var result =null;
                var url ="/emsaasui/emcpdfui/api/registry/lookup/endpoint?serviceName="+serviceName+"&version="+version; 
                if (typeof rel==="string"){
                    url = "/emsaasui/emcpdfui/api/registry/lookup/link?serviceName="+serviceName+"&version="+version+"&rel="+rel; 
                }

                $.ajax(url,{
                    success:function(data, textStatus,jqXHR) {
                        result = data;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        console.log("Error: URL not found due to error: "+textStatus);
                    },
                    async:false
                });
                
                if (result){
                    if (typeof rel==="string"){
                        console.log("Link found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                        return result.href;
                    }else{
                        console.log("EndPoint found by serviceName="+serviceName+", version="+version+", rel="+rel);
                        return result.href;
                    }
                }
                console.log("Warning: URL not found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                return null;
            };

            self.discoverLinkWithRelPrefix = function(serviceName, version, rel){
                if (typeof serviceName!=="string"){
                    console.log("Error: Failed to discovery Link (with Rel Prefix), serviceName="+serviceName);
                    return null;
                }
                if (typeof version!=="string"){
                    console.log("Error: Failed to discovery Link (with Rel Prefix), version="+version);
                    return null;
                }

                if (typeof rel!=="string"){
                    console.log("Error: Failed to discovery Link (with Rel Prefix), rel="+rel);
                    return null;                    
                }
                var result =null;
                var url= "/emsaasui/emcpdfui/api/registry/lookup/linkWithRelPrefix?serviceName="+serviceName+"&version="+version+"&rel="+rel; 

                $.ajax(url,{
                    success:function(data, textStatus,jqXHR) {
                        result = data;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        console.log("Error: Link (with Rel Prefix) not found due to error: "+textStatus);
                    },
                    async:false
                });
                
                if (result){
                    console.log("Link (with Rel Prefix) found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                    return result.href;
                }
                console.log("Warning: Link (with Rel Prefix) not found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                return null;
            };
            
            /**
             * Discover SSO logout URL
             * @param {String} smUrl
             * @returns {String}
             */
            self.discoverLogoutUrl = function() {
                return self.discoverUrl('SecurityService', '0.1', 'sso.logout');
            };

            /**
             * Discover dashboard home URL
             * @param {String} smUrl
             * @returns {String} url
             */
            self.discoverDFHomeUrl = function() {
                var url = self.discoverUrl("Dashboard-UI","0.1",'sso.home');
                if (url){
                    return url;
                }else{
                    console.log("Failed to discover Dashboard Home");
                    return null;
                }
            };    
            
            /**
             * Discover available dashboard api service URL
             * @param {String} smUrl
             * @returns {String} url 
             */
            self.discoverDFRestApiUrl = function() {
                var url = self.discoverUrl("Dashboard-API","0.1","sso.endpoint/virtual");
                
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery DF REST API end point");
                    return null;
                }
            };
            
            /**
             * Get default request header for ajax call
             * @returns {Object} 
             */
            self.getDefaultHeader = function() {
                var defHeader = {
                    "X-USER-IDENTITY-DOMAIN-NAME":self.tenantName,
                    "X-REMOTE-USER":self.tenantName+'.'+self.userName};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            /**
             * Get request header for Dashboard API call
             * @returns {Object} 
             */
            self.getDashboardsRequestHeader=function() {
                return self.getDefaultHeader();
            };  
        }
        
        return DashboardFrameworkUtility;
    }
);

