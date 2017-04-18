package oracle.sysman.emaas.platform.emcpdf.tenant;

import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.AppsInfo;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.EditionComponent;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.SubscriptionApps;
import oracle.sysman.emaas.platform.emcpdf.tenant.subscription2.TenantSubscriptionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chehao on 2017/4/18 10:33.
 */
public class SubscriptionAppsUtil {
    private static final Logger LOGGER = LogManager.getLogger(SubscriptionAppsUtil.class);

    /**
     * V1 Basic 7 Services
     */
    private static final String APM_SERVICE_TYPE = "APM";
    private static final String MONITORING_SERVICE_TYPE = "Monitoring";
    private static final String ITANALYTICS_SERVICE_TYPE = "ITAnalytics";
    private static final String LOGANALYTICS_SERVICE_TYPE = "LogAnalytics";
    private static final String ORCHESTRATION_SERVICE_TYPE = "Orchestration";
    private static final String COMPLIANCE_SERVICE_TYPE = "Compliance";
    private static final String SECURITYSERVICE_SERVICE_TYPE = "SecurityAnalytics";

    /**
     * V2 suite
     */
    private static final String OMC_SERVICE_TYPE = "OMC";
    private static final String OSMACC_SERVICE_TYPE = "OSMACC";

    /**
     * V3 suite
     */
    private static final String OMCSE_SERVICE_TYPE = "OMCSE";
    private static final String OMCEE_SERVICE_TYPE = "OMCEE";
    private static final String OMCLOG_SERVICE_TYPE = "OMCLOG";
    private static final String SECSE_SERVICE_TYPE = "SECSE";
    private static final String SECSMA_SERVICE_TYPE = "SECSMA";

    /**
     * Edition info
     */
    //V2 OMC suite edition info
    public static final String SE_EDITION = "Standard Edition";
    public static final String EE_EDITION = "Enterprise Edition";
    public static final String LOG_EDITION = "Log Analytics Edition";
    //V2 OSMACC suite edition info
    public static final String CONFIGURATION_COMPLIANCE_EDITION = "Configuration and Compliance Edition";
    public static final String SECURITY_MONITORING_ANALYTICS_EDITION = "Security Monitoring and Analytics Edition";

    /**
     * Tenant version info
     */
    public static final String V1_TENANT = "V1_MODEL";
    public static final String V2_TENANT = "V2_MODEL";
    public static final String V3_TENANT = "V3_MODEL";


    /**
     * This method will do some mapping work
     *
     * @return
     */
    public static List<String> getSubscribedAppsList(TenantSubscriptionInfo tenantSubscriptionInfo) {
        LOGGER.info("getSubscribedAppsList mapping...");
        if (tenantSubscriptionInfo.getSubscriptionAppsList() == null) {
            LOGGER.error("SubAppList cannot be null or empty!");
            return null;
        }

        List<SubscriptionApps> subscriptionAppsList = tenantSubscriptionInfo.getSubscriptionAppsList();
        List<AppsInfo> appsInfoList = new ArrayList<>();
        Set<String> appSet = new HashSet<>();
        List<String> appList = new ArrayList<>();
        for (SubscriptionApps subscriptionApps : subscriptionAppsList) {
            List editions = getEditions(subscriptionApps.getEditionComponentsList());
            LOGGER.info("editions is {}", editions);
            //V2 handling
            if (OMC_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                LOGGER.info("V2 handling, Service Type is []", OMC_SERVICE_TYPE);
                AppsInfo appsInfo = new AppsInfo(OMC_SERVICE_TYPE, V2_TENANT, editions);
                appsInfoList.add(appsInfo);
                appSet.add(OMC_SERVICE_TYPE);
                if (subscriptionApps.isTrial()) {
                    LOGGER.info("Service suite {} is trial edition", OMC_SERVICE_TYPE);
                    appSet.add(APM_SERVICE_TYPE);
                    AppsInfo appsInfo1 = new AppsInfo(APM_SERVICE_TYPE, V2_TENANT, editions);
                    appsInfoList.add(appsInfo1);

                    appSet.add(LOGANALYTICS_SERVICE_TYPE);
                    AppsInfo appsInfo2 = new AppsInfo(LOGANALYTICS_SERVICE_TYPE, V2_TENANT, editions);
                    appsInfoList.add(appsInfo2);

                    appSet.add(MONITORING_SERVICE_TYPE);
                    AppsInfo appsInfo3 = new AppsInfo(MONITORING_SERVICE_TYPE, V2_TENANT, editions);
                    appsInfoList.add(appsInfo3);

                    appSet.add(ITANALYTICS_SERVICE_TYPE);
                    AppsInfo appsInfo4 = new AppsInfo(ITANALYTICS_SERVICE_TYPE, V2_TENANT, editions);
                    appsInfoList.add(appsInfo4);

                    appSet.add(ORCHESTRATION_SERVICE_TYPE);
                    AppsInfo appsInfo5 = new AppsInfo(ORCHESTRATION_SERVICE_TYPE, V2_TENANT, editions);
                    appsInfoList.add(appsInfo5);
                } else {
                    for (EditionComponent e : subscriptionApps.getEditionComponentsList()) {
                        if (e.getEdition() != null) {
                            if (e.getEdition().contains(SE_EDITION)) {
                                LOGGER.info("Service suite {} is SE edition", OMC_SERVICE_TYPE);
                                if (!checkExist(appsInfoList, APM_SERVICE_TYPE)) {
                                    appSet.add(APM_SERVICE_TYPE);
                                    AppsInfo appsInfo1 = new AppsInfo(APM_SERVICE_TYPE, V2_TENANT, editions);
                                    appsInfoList.add(appsInfo1);
                                }
                                if (!checkExist(appsInfoList, MONITORING_SERVICE_TYPE)) {
                                    appSet.add(MONITORING_SERVICE_TYPE);
                                    AppsInfo appsInfo2 = new AppsInfo(MONITORING_SERVICE_TYPE, V2_TENANT, editions);
                                    appsInfoList.add(appsInfo2);
                                }
                            }
                            if (e.getEdition().contains(EE_EDITION)) {
                                LOGGER.info("Service suite {} is EE edition", OMC_SERVICE_TYPE);
                                if (!checkExist(appsInfoList, APM_SERVICE_TYPE)) {
                                    appSet.add(APM_SERVICE_TYPE);
                                    AppsInfo appsInfo1 = new AppsInfo(APM_SERVICE_TYPE, V2_TENANT, editions);
                                    appsInfoList.add(appsInfo1);
                                }

                                if (!checkExist(appsInfoList, MONITORING_SERVICE_TYPE)) {
                                    appSet.add(MONITORING_SERVICE_TYPE);
                                    AppsInfo appsInfo2 = new AppsInfo(MONITORING_SERVICE_TYPE, V2_TENANT, editions);
                                    appsInfoList.add(appsInfo2);
                                }

                                if (!checkExist(appsInfoList, ITANALYTICS_SERVICE_TYPE)) {
                                    appSet.add(ITANALYTICS_SERVICE_TYPE);
                                    AppsInfo appsInfo3 = new AppsInfo(ITANALYTICS_SERVICE_TYPE, V2_TENANT, editions);
                                    appsInfoList.add(appsInfo3);
                                }

                                if (!checkExist(appsInfoList, ORCHESTRATION_SERVICE_TYPE)) {
                                    appSet.add(ORCHESTRATION_SERVICE_TYPE);
                                    AppsInfo appsInfo4 = new AppsInfo(ORCHESTRATION_SERVICE_TYPE, V2_TENANT, editions);
                                    appsInfoList.add(appsInfo4);
                                }
                            }
                            if (e.getEdition().contains(LOG_EDITION)) {
                                LOGGER.info("Service suite {} is Log analytics edition", OMC_SERVICE_TYPE);
                                if (!checkExist(appsInfoList, LOGANALYTICS_SERVICE_TYPE)) {
                                    appSet.add(LOGANALYTICS_SERVICE_TYPE);
                                    AppsInfo appsInfo1 = new AppsInfo(LOGANALYTICS_SERVICE_TYPE, V2_TENANT, editions);
                                    appsInfoList.add(appsInfo1);
                                }
                            }
                        }
                    }
                }
                LOGGER.info("Service type is {} and subscribed apps is {}", OMC_SERVICE_TYPE, appSet);
                appList.addAll(appSet);
                continue;
            }
            if (OSMACC_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                LOGGER.info("V2 handling Service type is {}", OSMACC_SERVICE_TYPE);
                appSet.add(OSMACC_SERVICE_TYPE);
                AppsInfo appsInfo = new AppsInfo(OSMACC_SERVICE_TYPE, V2_TENANT, editions);
                appsInfoList.add(appsInfo);
                if (subscriptionApps.isTrial()) {
                    LOGGER.info("Service suite {} is trial edition", OSMACC_SERVICE_TYPE);
                    appSet.add(COMPLIANCE_SERVICE_TYPE);
                    AppsInfo appsInfo1 = new AppsInfo(COMPLIANCE_SERVICE_TYPE, V2_TENANT, editions);
                    appsInfoList.add(appsInfo1);

                    appSet.add(SECURITYSERVICE_SERVICE_TYPE);
                    AppsInfo appsInfo2 = new AppsInfo(SECURITYSERVICE_SERVICE_TYPE, V2_TENANT, editions);
                    appsInfoList.add(appsInfo2);
                } else {
                    for (EditionComponent e : subscriptionApps.getEditionComponentsList()) {
                        if (e.getEdition() != null) {
                            if (e.getEdition().contains(CONFIGURATION_COMPLIANCE_EDITION)) {
                                LOGGER.info("Service suite {} is {} edition", OSMACC_SERVICE_TYPE, CONFIGURATION_COMPLIANCE_EDITION);
                                appSet.add(COMPLIANCE_SERVICE_TYPE);
                                AppsInfo appsInfo1 = new AppsInfo(COMPLIANCE_SERVICE_TYPE, V2_TENANT, editions);
                                appsInfoList.add(appsInfo1);
                                continue;
                            }
                            if (e.getEdition().contains(SECURITY_MONITORING_ANALYTICS_EDITION)) {
                                LOGGER.info("Service suite {} is {} edition", OSMACC_SERVICE_TYPE, CONFIGURATION_COMPLIANCE_EDITION);
                                appSet.add(SECURITYSERVICE_SERVICE_TYPE);
                                AppsInfo appsInfo1 = new AppsInfo(SECURITYSERVICE_SERVICE_TYPE, V2_TENANT, editions);
                                appsInfoList.add(appsInfo1);
                                continue;
                            }
                        }
                    }
                }
                LOGGER.info("Service type is {} and after mapping subscribed apps is {}", OSMACC_SERVICE_TYPE, appSet);
                appList.addAll(appSet);
                continue;
            }
            //V3 handling
            if (OMCSE_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                appList.add(OMCSE_SERVICE_TYPE);
                AppsInfo appsInfo = new AppsInfo(OMCSE_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo);

                appList.add(APM_SERVICE_TYPE);
                AppsInfo appsInfo1 = new AppsInfo(APM_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo1);

                appList.add(MONITORING_SERVICE_TYPE);
                AppsInfo appsInfo2 = new AppsInfo(MONITORING_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo2);
                LOGGER.info("Service type is {} and subscribed apps is {}", OMCSE_SERVICE_TYPE, appList);
            }
            if (OMCEE_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                appList.add(OMCEE_SERVICE_TYPE);
                AppsInfo appsInfo = new AppsInfo(OMCEE_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo);

                appList.add(APM_SERVICE_TYPE);
                AppsInfo appsInfo1 = new AppsInfo(APM_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo1);

                appList.add(MONITORING_SERVICE_TYPE);
                AppsInfo appsInfo2 = new AppsInfo(MONITORING_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo2);

                appList.add(ITANALYTICS_SERVICE_TYPE);
                AppsInfo appsInfo3 = new AppsInfo(ITANALYTICS_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo3);

                appList.add(ORCHESTRATION_SERVICE_TYPE);
                AppsInfo appsInfo4 = new AppsInfo(ORCHESTRATION_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo4);
                LOGGER.info("Service type is {} and subscribed apps is {}", OMCEE_SERVICE_TYPE, appList);
            }
            if (OMCLOG_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                appList.add(OMCLOG_SERVICE_TYPE);
                AppsInfo appsInfo = new AppsInfo(OMCLOG_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo);

                appList.add(LOGANALYTICS_SERVICE_TYPE);
                AppsInfo appsInfo1 = new AppsInfo(LOGANALYTICS_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo1);
                LOGGER.info("Service type is {} and subscribed apps is {}", OMCLOG_SERVICE_TYPE, appList);
            }
            if (SECSE_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                appList.add(SECSE_SERVICE_TYPE);
                AppsInfo appsInfo = new AppsInfo(SECSE_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo);

                appList.add(COMPLIANCE_SERVICE_TYPE);
                AppsInfo appsInfo1 = new AppsInfo(COMPLIANCE_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo1);
                LOGGER.info("Service type is {} and subscribed apps is {}", SECSE_SERVICE_TYPE, appList);
            }
            if (SECSMA_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                appList.add(SECSMA_SERVICE_TYPE);
                AppsInfo appsInfo = new AppsInfo(SECSMA_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo);

                appList.add(SECURITYSERVICE_SERVICE_TYPE);
                AppsInfo appsInfo1 = new AppsInfo(SECURITYSERVICE_SERVICE_TYPE, V3_TENANT, editions);
                appsInfoList.add(appsInfo1);
                LOGGER.info("Service type is {} and subscribed apps is {}", SECSMA_SERVICE_TYPE, appList);
            }

            //V1 handling
            if (APM_SERVICE_TYPE.equals(subscriptionApps.getServiceType()) || MONITORING_SERVICE_TYPE.equals(subscriptionApps.getServiceType())
                    || ITANALYTICS_SERVICE_TYPE.equals(subscriptionApps.getServiceType()) || SECURITYSERVICE_SERVICE_TYPE.equals(subscriptionApps.getServiceType())
                    || ORCHESTRATION_SERVICE_TYPE.equals(subscriptionApps.getServiceType()) || COMPLIANCE_SERVICE_TYPE.equals(subscriptionApps.getServiceType())
                    || LOGANALYTICS_SERVICE_TYPE.equals(subscriptionApps.getServiceType())) {
                LOGGER.info("Service Type is {}", subscriptionApps.getServiceType());
                AppsInfo appsInfo = new AppsInfo(subscriptionApps.getServiceType(), V1_TENANT, editions);
                appsInfoList.add(appsInfo);
                appList.add(subscriptionApps.getServiceType());
            }
        }
        LOGGER.debug("After Editions info is {}", appsInfoList);
        tenantSubscriptionInfo.setAppsInfoList(appsInfoList);

        return appList;
    }

    private static List<String> getEditions(List<EditionComponent> list) {
        if (list == null || list.isEmpty()) {
            LOGGER.error("Edition component is null or empty!");
            return null;
        }
        List<String> editionList = new ArrayList<>();
        for (EditionComponent ec : list) {
            editionList.add(ec.getEdition());
            LOGGER.debug("Edition Component info is {}", ec.getEdition());
        }

        return editionList;
    }

    private static boolean checkExist(List<AppsInfo> list, String serviceName) {
        if (serviceName == null) {
            return false;
        }
        if (list != null && !list.isEmpty()) {
            for (AppsInfo appsInfo : list) {
                if (serviceName.equals(appsInfo.getId())) {
                    LOGGER.info("Duplicate app service entry found, will not put into list!");
                    return true;
                }
            }
        }
        return false;

    }
}
