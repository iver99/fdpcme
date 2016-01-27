Rem This table stores the user options and settings  for EMaaS dashboard objects
Rem
Rem KEYS
Rem
Rem The primary keys are USER_NAME ,DASHBOARD_ID and TENANT_ID
Rem
Rem COLUMNS
Rem
Rem USER_NAME - name of user
Rem TENANT_ID - the id of tenant
Rem DASHBOARD_ID - id of dashboard
Rem AUTO_REFRESH_INTERVAL - interval of refresh, auto refresh disabled if set as 0
Rem
prompt EMS_DASHBOARD_USER_OPTIONS
CREATE TABLE EMS_DASHBOARD_USER_OPTIONS
(
USER_NAME       VARCHAR2(128) NOT NULL,
TENANT_ID       NUMBER(*,0) NOT NULL,
DASHBOARD_ID    NUMBER(*,0) NOT NULL,
AUTO_REFRESH_INTERVAL NUMBER(*,0) DEFAULT(0),
CONSTRAINT    EMS_DASHBOARD_USER_OPTIONS_PK PRIMARY KEY (USER_NAME,TENANT_ID,DASHBOARD_ID) USING INDEX
);