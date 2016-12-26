package oracle.sysman.emaas.platform.dashboards.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oracle.sysman.qatool.uifwk.utils.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author reliang
 *
 */
public class QAToolUtil {
    public static final String JDBC_PARAM_URL = "javax.persistence.jdbc.url";
    public static final String JDBC_PARAM_USER = "javax.persistence.jdbc.user";
    public static final String JDBC_PARAM_PASSWORD = "javax.persistence.jdbc.password";
    public static final String JDBC_PARAM_DRIVER = "javax.persistence.jdbc.driver";
    
    private static final String AUTHORIZATION = "Authorization";
    private static final String PATTERN = "((https?|www):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
    private static final String LIFE_CYCLE = "LifecycleInvManager";
    private static final String AUTH_STRING = "Basic d2VibG9naWM6d2VsY29tZTE=";//WEBLOGIC_USER + ":" + WEBLOGIC_USER;
    private static final String DEPLOY_URL = "/instances?servicename=LifecycleInventoryService";
    private static final String DEPLY_SCHEMA = "/schemaDeployments?softwareName=dashboardService-api";
    private static final String PASSWORD = "welcome1";
    private static final String JDBC_URL = "jdbc:oracle:thin:@";
    private static final String SCHEMA_USER = "schemaUser";
    private static final String SERVICE_MANAGER_URL = "SERVICE_MANAGER_URL";
    private static final String ODS_HOSTNAME = "ODS_HOSTNAME";
    private static final String ODS_PORT = "ODS_PORT";
    private static final String ODS_SID = "ODS_SID";
    private static final String JDBC_PARAM_DRIVER_VALUE = "oracle.jdbc.OracleDriver";
    private static final String TENANT_USER_NAME = "TENANT_USER_NAME";
    private static final String TENANT_NAME = "TENANT_NAME";
    private static final Logger LOGGER = LogManager.getLogger(QAToolUtil.class);
    
    private QAToolUtil() {
    }
    
    public static Properties getDbProperties()
    {
        Properties props = new Properties();
        try {
            String url = JDBC_URL + Utils.getProperty(ODS_HOSTNAME) + ":" + Utils.getProperty(ODS_PORT) + ":"
                    + Utils.getProperty(ODS_SID);
            props.put(JDBC_PARAM_URL, url);
        } catch (Exception ex) {
            return null;
        }
        String schemaName = QAToolUtil.getSchemaDeploymentDetails();
        props.put(JDBC_PARAM_USER, schemaName);
        props.put(JDBC_PARAM_PASSWORD, PASSWORD);
        props.put(JDBC_PARAM_DRIVER, JDBC_PARAM_DRIVER_VALUE);
        return props;
    }
    
    public static Properties getTenantDetails()
    {
        Properties props = new Properties();
        String tenantIdInternal= "TENANT_ID_INTERNAL_"+Utils.getProperty("TENANT_ID");
        props.put(TENANT_USER_NAME, Utils.getProperty(tenantIdInternal) + "." + Utils.getProperty("SSO_USERNAME"));
        props.put(TENANT_NAME, Utils.getProperty(tenantIdInternal));
        return props;
    }
    
    private static List<String> extractUrls(String value)
    {
        if (value == null) {
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<String>();
        Pattern p = Pattern.compile(PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(value);
        while (m.find()) {
            result.add(value.substring(m.start(0), m.end(0)));
        }
        return result;
    }
    
    private static String getDetaildByUrl(String url)
    {
        BufferedReader in = null;
        InputStreamReader inReader = null;
        StringBuilder response = new StringBuilder();
        try {
            URL schemaDepUrl = new URL(url);
            HttpURLConnection con = (HttpURLConnection) schemaDepUrl.openConnection();
            con.setRequestProperty(AUTHORIZATION, AUTH_STRING);
            //int responseCode = con.getResponseCode();
            inReader = new InputStreamReader(con.getInputStream(), "UTF-8");
            in = new BufferedReader(inReader);
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        catch (IOException e) {
            LOGGER.error("an error occureed while getting details by url" + ":: " + url, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (inReader != null) {
                    inReader.close();
                }
            }
            catch (IOException ioEx) {
                //ignore
            }
        }
        return response.toString();
    }
    
    private static String getSchemaDeploymentDetails()
    {
        String username = null;
        String data = QAToolUtil.getDetaildByUrl(Utils.getProperty(SERVICE_MANAGER_URL) + DEPLOY_URL);
        List<String> urlList = QAToolUtil.extractUrls(data);
        for (String temp : urlList) {
            if (temp.endsWith(LIFE_CYCLE)) {
                data = QAToolUtil.getDetaildByUrl(temp + DEPLY_SCHEMA);
                if (data != null) {
                    username = QAToolUtil.getUsername(data);
                    if (username != null) {
                        return username;
                    }
                }
            }
        }
        return null;
    }
    
    private static String getUsername(String data)
    {
        String[] options = data.split(",");
        if (options.length > 0) {
            for (String tmp : options) {
                if (tmp.contains(SCHEMA_USER)) {
                    String name = tmp.substring(tmp.indexOf(":"));
                    if (name != null) {
                        name = name.replace("\"", "").replace(":", "");
                        return name;
                    }
                }
            }
        }
        return null;
    }
}