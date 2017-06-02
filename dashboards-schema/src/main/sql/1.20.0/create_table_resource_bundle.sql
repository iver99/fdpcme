Rem This table stores resource bundle files of EMaaS OOB Dashboards
Rem
Rem KEYS
Rem
Rem The primary key is LANGUAGE_CODE, COUNTRY_CODE, SERVICE_NAME
Rem
Rem COLUMNS
Rem
Rem LANGUAGE_CODE - language code in LOCAL
Rem COUNTRY_CODE - country code in LOCAL
Rem SERVICE_NAME - like APM/ITAnalytics/LogAnalytics/Monitoring/SecurityAnalytics/Orchestration/Compliance/UDE
Rem SERVICE_VERSION - for future extend
Rem PROPERTIES_FILE - key-value mapping
Rem LAST_MODIFICATION_DATE - last modification date

CREATE TABLE EMS_DASHBOARD_RESOURCE_BUNDLE
  (
    LANGUAGE_CODE                VARCHAR(2) NOT NULL,
    COUNTRY_CODE                 VARCHAR(4) NOT NULL,
    SERVICE_NAME                 VARCHAR(255) NOT NULL,
    SERVICE_VERSION              VARCHAR(255),
    PROPERTIES_FILE              NCLOB,
    LAST_MODIFICATION_DATE       TIMESTAMP NOT NULL,
    CONSTRAINT EMS_DASHBOARD_RESOURCE_B_PK PRIMARY KEY (LANGUAGE_CODE, COUNTRY_CODE, SERVICE_NAME) USING INDEX
  );