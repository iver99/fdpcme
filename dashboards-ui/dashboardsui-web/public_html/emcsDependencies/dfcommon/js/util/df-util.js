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
             * @param {String} authToken
             * @returns {String} result
             */
            self.discoverUrl = function(serviceName, version, rel, smUrl, authToken){
                if (typeof smUrl!=="string"){
                     console.log("Error: Failed to discovery URL, SM URL="+smUrl);
                    return null;                    
                }

                if (typeof authToken!=="string"){
                    console.log("Error: Failed to discovery URL, authToken="+authToken);
                    return null;
                }
                
                if (serviceName===null || serviceName===undefined){
                    console.log("Error: Failed to discovery URL, serviceName="+serviceName);
                    return null;
                }
                var searchUrl = self.buildFullUrl(smUrl,"instances")+"?serviceName="+serviceName;
                if (typeof version==="string"){
                    searchUrl = searchUrl +"&version="+version;
                }

                var result =null;
                $.ajax(searchUrl,{
                    headers:self.getSMRequestHeader(authToken),
                    success:function(data, textStatus,jqXHR) {
                        result = data;
                    },
                    error:function(xhr, textStatus, errorThrown){
                        console.log("Error: URL not found due to error: "+textStatus);
                    },
                    async:false
                });
                
                if (result && result.total>0){
                    if (typeof rel==="string"){
                        if (Array.isArray(result.items[0].links) && result.items[0].links.length>0){
                            for(var i=0;i<result.items[0].links.length;i++){
                                var link = result.items[0].links[i];
                                if (link.rel===rel){
                                    console.log("URL found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                                    return link.href;
                                }
                            }
                        }
                    }else{
                        var virtualEndpoints = result.items[0].virtualEndpoints;
                        if (Array.isArray(virtualEndpoints) && virtualEndpoints.length>0){
                            console.log("URL found by serviceName="+serviceName+", version="+version); 
                            return virtualEndpoints[0];
                        }else{
                            var canonicalEndpoints = result.items[0].canonicalEndpoints;
                            if (Array.isArray(canonicalEndpoints) && canonicalEndpoints.length>0){
                                console.log("URL found by serviceName="+serviceName+", version="+version); 
                                return canonicalEndpoints[0];
                            }
                        }
                    }
                }
                console.log("Warning: URL not found by serviceName="+serviceName+", version="+version+", rel="+rel); 
                return null;
            };
            
            /**
             * Discover SSO logout URL
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String}
             */
            self.discoverLogoutUrl = function(smUrl, authToken) {
                return self.discoverUrl('SecurityService', '0.1', 'sso.logout', smUrl, authToken);
            };

            /**
             * Discover dashboard home URL
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String} url
             */
            self.discoverDFHomeUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("Dashboard-UI","0.1",'sso.home', smUrl, authToken);
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery Dashboard Home");
                    return null;
                }
            };    
            
            /**
             * Discover available dashboard api service URL
             * @param {String} smUrl
             * @param {String} authToken
             * @returns {String} url 
             */
            self.discoverDFRestApiUrl = function(smUrl, authToken) {
                var url = self.discoverUrl("Dashboard-API","0.1","sso.endpoint/virtual", smUrl, authToken);
                
                if (url){
                    return url;
                }else{
                    console.log("Failed to discovery DF REST API end point");
                    return null;
                }
            };
            
            /**
             * Get request header for query from Service Manager
             * @param {String} authToken
             * @returns {Object} 
             */
            self.getSMRequestHeader = function(authToken) {
                var defHeader = {/*"Authorization": authToken,*/"X-USER-IDENTITY-DOMAIN-NAME":"dummy"};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            /**
             * Get default request header for ajax call
             * @param {String} authToken
             * @returns {Object} 
             */
            self.getDefaultHeader = function(authToken) {
                var defHeader = {/*"Authorization": authToken,*/
                    "X-USER-IDENTITY-DOMAIN-NAME":self.tenantName,
                    "X-REMOTE-USER":self.tenantName+'.'+self.userName};
                console.log("Sent Header: "+JSON.stringify(defHeader));
                return defHeader;
            };
            
            /**
             * Get request header for Dashboard API call
             * @param {String} authToken
             * @returns {Object} 
             */
            self.getDashboardsRequestHeader=function(authToken) {
                return self.getDefaultHeader(authToken);
            };  
        }
        
        return DashboardFrameworkUtility;
    }
);

