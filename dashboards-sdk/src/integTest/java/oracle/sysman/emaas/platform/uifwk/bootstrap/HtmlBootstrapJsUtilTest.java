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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.InstanceInfo;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.lookup.LookupManager;
import org.testng.Assert;
import org.testng.annotations.Test;
import mockit.Expectations;
import mockit.Mocked;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;

public class HtmlBootstrapJsUtilTest {

    @Test(groups = { "s2" })
    public void testGetSDKVersionJSWithLinks(
                                @Mocked final LookupManager lookupManager,
                                @Mocked final LookupClient lookupClient,
                                @Mocked final InstanceInfo instanceInfo) throws Exception {
        
        new Expectations() {
            {
                lookupClient.getInstancesWithLinkRelPrefix(anyString, anyString);
                result = new ArrayList<>(Arrays.asList(instanceInfo));
                instanceInfo.getServiceName();
                result = "UIFWK";
                instanceInfo.getLinksWithRelPrefixWithProtocol(anyString, anyString);
                result = getLinks();
            }
        };
        String newJs = HtmlBootstrapJsUtil.getAllBootstrapJS();
        String expectedJs = "if(!window.sdkFilePath){window.sdkFilePath={};}window.sdkFilePath={\"emsaasui/emcta/ta/js/sdk/topology/emcta-topology\":\"emsaasui/emcta/ta/1.15.0-170202.111534/js/shared-components/emcta-topology-impl\",\"emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil\":\"emsaasui/emcta/ta/1.15.0-170202.111534/js/entitycard/EntityCardRegistryImpl\"};window.getSDKVersionFile=function(nonCacheableVersion){console.log(\"getSDKVersionFile() for: \"+nonCacheableVersion);var versionFile=nonCacheableVersion;if(window.sdkFilePath){versionFile=window.sdkFilePath[nonCacheableVersion];}if(!versionFile){versionFile=nonCacheableVersion;}console.log(\"getSDKVersionFile(), found version: \"+versionFile);return versionFile;}";
        Assert.assertEquals(newJs, expectedJs);
    }
    
    @Test(groups = { "s2" })
    public void testGetSDKVersionJSToAddNoLinks(
                                @Mocked final LookupManager lookupManager,
                                @Mocked final LookupClient lookupClient,
                                @Mocked final InstanceInfo instanceInfo) throws Exception {
        
        new Expectations() {
            {
                lookupClient.getInstancesWithLinkRelPrefix(anyString, anyString);
                result = new ArrayList<>(Arrays.asList(instanceInfo));
                instanceInfo.getServiceName();
                result = "UIFWK";
                instanceInfo.getLinksWithRelPrefixWithProtocol(anyString, anyString);
                result = new ArrayList<Link>();
            }
        };
        String newJs = HtmlBootstrapJsUtil.getSDKVersionJS();
        String expectedJs = "if(!window.sdkFilePath){window.sdkFilePath={};}window.sdkFilePath={};window.getSDKVersionFile=function(nonCacheableVersion){console.log(\"getSDKVersionFile() for: \"+nonCacheableVersion);var versionFile=nonCacheableVersion;if(window.sdkFilePath){versionFile=window.sdkFilePath[nonCacheableVersion];}if(!versionFile){versionFile=nonCacheableVersion;}console.log(\"getSDKVersionFile(), found version: \"+versionFile);return versionFile;}";
        Assert.assertEquals(newJs, expectedJs);
    }
    
    private List<Link> getLinks(){
        ArrayList<Link> al = new ArrayList<Link>();
        Link link1 = new Link();
        link1.withHref("https://den02dtb.us.oracle.com:7004/emsaasui/emcta/ta/1.15.0-170202.111534/js/shared-components/emcta-topology-impl");
        link1.withRel("versionLookupSDK/emsaasui/emcta/ta/js/sdk/topology/emcta-topology");
        al.add(link1);
        Link link2 = new Link();
        link2.withHref("https://den02dtb.us.oracle.com:7004/emsaasui/emcta/ta/1.15.0-170202.111534/js/entitycard/EntityCardRegistryImpl");
        link2.withRel("versionLookupSDK/emsaasui/emcta/ta/js/sdk/entitycard/EntityCardUtil");
        al.add(link2);
        return al;
    }

}
