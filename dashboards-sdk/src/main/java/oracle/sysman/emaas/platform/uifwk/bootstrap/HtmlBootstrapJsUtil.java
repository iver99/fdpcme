/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ashkak
 */
package oracle.sysman.emaas.platform.uifwk.bootstrap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.sysman.emSDK.emaas.authz.bean.LoginDataStore;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import org.apache.logging.log4j.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

public class HtmlBootstrapJsUtil {
    private static final String SDK_FILE = "versionLookupSDK";
    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(HtmlBootstrapJsUtil.class.getName());

    public static String getAllBootstrapJS() {
        return getSDKVersionJS();
    }

    public static String getLoggedInUser() {
        String currentUser = LoginDataStore.getUserName();
        String userName = "\\{currentUser:\"" + currentUser + "\"\\}";
        return userName;
    }
    
    /**
     * Returns javascript that is to be added. This script contains 1. map for
     * SDK version files - "window.sdkFilePath" 2. function to get the SDK
     * version files - "window.getSDKVersionFile()"
     *
     * "https://den01fjq.us.oracle.com:7005/". Use this to form correct after
     * context URL for the MAP to be returned (i.e remove host:port information
     * from the value received from registry manager) RegistryManager returns -
     * https://den01fjq.us.oracle.com:7005/emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition
     * remove host:port to create value as-
     * "emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition" As this is
     * what require uses in javascript to load modules
     *
     * @return String
     *
     */
    public static String getSDKVersionJS() {
        List<Link> sdkFileLinks = lookupLinksWithRelPrefix(SDK_FILE, HTTPS);
        Map sdkFilesMap = getSdkFilesMap(sdkFileLinks);
        String mapResponse = JsonWriteUtil.writeValueAsString(sdkFilesMap);

        String sdkVersionDefinitionJS = "if(!window.sdkFilePath){window.sdkFilePath={};}window.sdkFilePath=" + mapResponse + ";";
        String getSDKVersionFunctionJS = "window.getSDKVersionFile=function(nonCacheableVersion){console.log(\"getSDKVersionFile() for: \"+nonCacheableVersion);var versionFile=nonCacheableVersion;if(window.sdkFilePath){versionFile=window.sdkFilePath[nonCacheableVersion];}if(!versionFile){versionFile=nonCacheableVersion;}console.log(\"getSDKVersionFile(), found version: \"+versionFile);return versionFile;}";

        String injectableJS = sdkVersionDefinitionJS + getSDKVersionFunctionJS;
        LOGGER.debug("VersionFilesSDK(), injectableJS: " + injectableJS);
        return injectableJS;
    }
    /**
     * private Returns List of Links that have a particular linkPrefix which in
     * our case is "sdkVersionFile" if protocol is not specified, it fetches
     * both HTTP and HTTPS links, otherwise only those protocol links are
     * fetched
     *
     * @param linkPrefix : like "sdkVersionFile"
     * @param protocol: HTTP or HTTPS or both (""/null)
     *
     * @return List<Link>
     *
     */
    private static List<Link> lookupLinksWithRelPrefix(String linkPrefix, String protocol) {
        LOGGER.info("lookupLinksWithRelPrefix(" + linkPrefix + "), protocol: " + protocol);
        List<Link> linkList = new ArrayList<>();

        LookupClient lookUpClient = LookupManager.getInstance().getLookupClient();
        List<InstanceInfo> instanceList;
        if (protocol == null || protocol.isEmpty()) {
            instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix);
        } else {
            instanceList = lookUpClient.getInstancesWithLinkRelPrefix(linkPrefix, protocol);
        }
        // TODO: Maybe check for app subbscriptions??
        if (instanceList != null && !instanceList.isEmpty()) {
            for (InstanceInfo internalInstance : instanceList) {
                LOGGER.info("lookupLinksWithRelPrefix for Service: " + internalInstance.getServiceName() + ", protocol: " + protocol);
                List<Link> links;
                if (protocol == null || protocol.isEmpty()) {
                    links = internalInstance.getLinksWithRelPrefix(linkPrefix);
                } else {
                    links = internalInstance.getLinksWithRelPrefixWithProtocol(linkPrefix, protocol);
                }
                if (links != null && !links.isEmpty()) {
                    linkList.addAll(links);
                }
            }
        }
        LOGGER.debug("lookupLinksWithRelPrefix(" + linkPrefix + "): ", linkList.toString());
        return linkList;
    }

    /**
     * private Retrieves list of all links that have "sdkVersionFile". Remove
     * "sdkVersionFile/" from "rel" to get the KEY VALUE is "href" without the
     * host:port info
     *
     * LINK 1 rel:
     * sdkVersionFile/emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil href:
     * https://den01fjq.us.oracle.com:7005/emsaasui/emcta/ta/1.15.0-201620012335/js/entitycard/EntityCardRegistryImpl
     *
     * LINK 2 rel: sdkVersionFile/emsaasui/uifwk href:
     * https://den01fjq.us.oracle.com:7005/emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition
     *
     * Return Map like this for above KEY-
     * emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil; VALUE:
     * emsaasui/emcta/ta/1.15.0-201620012335/js/entitycard/EntityCardRegistryImpl
     * KEY- emsaasui/uifwk; VALUE:
     * emsaasui/uifwk/1.15.0-170118.012237/js/uifwk-impl-partition
     *
     * @param relLinks : list of all links that have "sdkVersionFile" registered
     * with serviceregistry
     *
     * @return Map<K,V>
     *
     */
    private static Map<String, String> getSdkFilesMap(List<Link> relLinks) {
        Map<String, String> sdkVersionFilesMap = new HashMap<>();
        if (relLinks != null && !relLinks.isEmpty()) {
            for (Link link : relLinks) {
                try {
                    URL url = new URL(link.getHref());
                    String path = url.getPath();
                    String key = link.getRel().substring(SDK_FILE.length() + 1);    // plus one to accomodate "/"
                    LOGGER.info("getSdkFilesMap() key: " + key);
                    String value = path.substring(1);   // to neglect the preceeding "/"
                    if (!sdkVersionFilesMap.containsKey(key)) {
                        sdkVersionFilesMap.put(key, value);
                        LOGGER.info("getSdkFilesMap() value: " + value);
                    } else {
                        LOGGER.info("getSdkFilesMap() key-value is present already: " + sdkVersionFilesMap.get(key));
                    }
                } catch (MalformedURLException me) {
                    LOGGER.error("Malformed URL getSdkFilesMap(" + relLinks.toString() + "): ", me);
                }
            }
        }
        LOGGER.debug("getSdkFilesMap(" + relLinks.toString() + ") for: ", sdkVersionFilesMap.toString());
        return sdkVersionFilesMap;
    }

    /**
     * Utility to write map values as JSON
     */
    public static class JsonWriteUtil {

        private static final ObjectMapper mapper = new ObjectMapper();

        public static String writeValueAsString(Object value) {
            try {
                return mapper.writeValueAsString(value);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
