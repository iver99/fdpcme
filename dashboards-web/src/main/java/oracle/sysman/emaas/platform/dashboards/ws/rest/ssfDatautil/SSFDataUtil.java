package oracle.sysman.emaas.platform.dashboards.ws.rest.ssfDatautil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import oracle.sysman.emaas.platform.dashboards.core.util.RegistryLookupUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil;
import oracle.sysman.emaas.platform.dashboards.core.util.TenantSubscriptionUtil.RestClient;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.info.Link;
import oracle.sysman.emSDK.emaas.platform.servicemanager.registry.registration.RegistrationManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author pingwu
 *
 */
public class SSFDataUtil {
	
	private static final String SERVICE_NAME = "SavedSearch";
	private static final String VERSION = "1.0";
	private static final String PATH = "search";
	private static final String GET_SEARCH_DATA_URI = "all";
	private static final String SAVE_SEARCH_DATA_URI = "import";
	private final static Logger LOGGER = LogManager.getLogger(SSFDataUtil.class);
	
	private static final String USER_IDENTITY_DOMAIN_NAME = "X-USER-IDENTITY-DOMAIN-NAME";
	private static final String REMOTE_USER = "X-REMOTE-USER";
	private static final String AUTHORIZATION = "Authorization";
	
	
	public static String getSSFData(String userTenant, String requestEntity) {
		return accessSSFWebService(userTenant,GET_SEARCH_DATA_URI,requestEntity);
	}
	
	public static String saveSSFData(String userTenant, String requestEntity) {
		return accessSSFWebService(userTenant, SAVE_SEARCH_DATA_URI,requestEntity);
	}
	/*
	private static String accessSSFWebService(String userTenant, String tenantIdParam, 
			String uri, Object requestEntity)
	{
		String ssfData = "";
		CloseableHttpClient client = HttpClients.createDefault();
		Link link = RegistryLookupUtil.getServiceInternalLink(SERVICE_NAME, VERSION, PATH, null);
		if (link == null || StringUtils.isEmpty(link.getHref())) {
            LOGGER.warn("Retrieving ssf data for tenant {}: null/empty ssfLink retrieved from service registry.");
            return null;
        }
        LOGGER.info("SSF REST API href is: " + link.getHref());
        String ssfHref = link.getHref() + "/" + uri;
        TenantSubscriptionUtil.RestClient rc = new TenantSubscriptionUtil.RestClient();
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("X-USER-IDENTITY-DOMAIN-NAME", tenantIdParam);
        headers.put("X-REMOTE-USER", userTenant);
        
        String response = rc.put(ssfHref, headers, requestEntity, tenantIdParam);
        		//.get(ssfHref, tenantIdParam, userTenant);
        LOGGER.info("Retrieved ssf data is: {}", response);
        return response;
       
	}
	*/
	private static String accessSSFWebService(String remoteUser,String uri,String data)
	{
		String value = "";
		CloseableHttpClient client = HttpClients.createDefault();
		Link link = RegistryLookupUtil.getServiceInternalLink(SERVICE_NAME, VERSION, PATH, null);
		if (link == null || StringUtils.isEmpty(link.getHref())) {
            LOGGER.warn("Retrieving ssf data for tenant {}: null/empty ssfLink retrieved from service registry.");
            return null;
        }
        LOGGER.info("SSF REST API href is: " + link.getHref());
        String ssfHref = link.getHref() + "/" + uri;
		if (link != null) {
			HttpPut put = new HttpPut(ssfHref);
			try {
				//data = URLDecoder.decode(data, "UTF-8");
				StringEntity params = new StringEntity(data,"UTF-8");
			    params.setContentType("application/json");
			    put.setEntity(params);
			} catch (UnsupportedEncodingException e) {
				LOGGER.error(e.getLocalizedMessage(), e);
			}
			
		    char[] authToken = RegistrationManager.getInstance().getAuthorizationToken();
			String authorization = String.copyValueOf(authToken);
			String domainName = remoteUser.substring(0, remoteUser.indexOf("."));
			put.addHeader(USER_IDENTITY_DOMAIN_NAME, domainName);
			put.addHeader(AUTHORIZATION, authorization);
			put.addHeader(REMOTE_USER, remoteUser);
			CloseableHttpResponse response = null;
			try {
				response = client.execute(put);
			}
			catch (ClientProtocolException e) {
				LOGGER.error(e.getLocalizedMessage(), e);
			}
			catch (IOException e) {
				LOGGER.error(e.getLocalizedMessage(), e);
			}
			InputStream instream = null;
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					try {
						instream = entity.getContent();
						value = getStrFromInputSteam(instream);
					}
					catch (IllegalStateException e) {
						LOGGER.error(e.getLocalizedMessage(), e);
					}
					catch (IOException e) {
						LOGGER.error(e.getLocalizedMessage(), e);
					}
				}
			}
			finally {
				try {
					if (instream != null) {
						instream.close();
					}
					if (response != null) {
						response.close();
					}
				}
				catch (IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			}
		}
		return value;
	}

	private static String getStrFromInputSteam(InputStream in)
	{
		BufferedReader bf = null;
		StringBuffer buffer = new StringBuffer();
		String line = "";
		try {
			bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while ((line = bf.readLine()) != null) {
				buffer.append(line);
			}
		}
		catch (UnsupportedEncodingException e1) {
			LOGGER.error(e1.getLocalizedMessage(), e1);
		}
		catch (IOException e2) {
			LOGGER.error(e2.getLocalizedMessage(), e2);
		}
		catch (Exception e3) {
			LOGGER.error(e3.getLocalizedMessage(), e3);
		}
		finally {
			if (bf != null) {
				try {
					bf.close();
				}
				catch (IOException e) {
					LOGGER.error(e.getLocalizedMessage(), e);
				}
			}
		}

		return buffer.toString();
	}
}
