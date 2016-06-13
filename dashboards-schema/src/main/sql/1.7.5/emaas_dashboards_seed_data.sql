Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, 2015 Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data.sql 
Rem
Rem    DESCRIPTION
Rem      Dashboard Framework seed data sql file.
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    jishshi  1/27/16 - Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  V_COUNT number;
BEGIN

SELECT COUNT(1) INTO V_COUNT from EMS_DASHBOARD_TILE where TENANT_ID='&TENANT_ID' AND PROVIDER_NAME='EmcitasApplications';
IF (V_COUNT>0) THEN
  UPDATE EMS_DASHBOARD_TILE SET PROVIDER_NAME='emcitas-ui-apps' WHERE TENANT_ID='&TENANT_ID' AND PROVIDER_NAME='EmcitasApplications';
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Provider name of EmcitasApplications has been upgrade to emcitas-ui-apps for tenant: &TENANT_ID successfully! Upgraded records: '||V_COUNT);
ELSE
  DBMS_OUTPUT.PUT_LINE('Provider name of EmcitasApplications has been upgrade to emcitas-ui-apps for tenant: &TENANT_ID before, no need to upgrade again');
END IF;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to upgrade provider name of EmcitasApplications to emcitas-ui-apps for tenant: &TENANT_ID due to '||SQLERRM);
    RAISE;
END;
/

DECLARE
  V_COUNT number;
BEGIN

SELECT COUNT(1) INTO V_COUNT from EMS_DASHBOARD WHERE TENANT_ID='&TENANT_ID' AND DASHBOARD_ID IN (11, 12, 13);
IF (V_COUNT>0) THEN
  DELETE FROM EMS_DASHBOARD_USER_OPTIONS WHERE TENANT_ID='&TENANT_ID' AND DASHBOARD_ID IN (11, 12, 13);
  DELETE FROM EMS_DASHBOARD_FAVORITE WHERE TENANT_ID='&TENANT_ID' AND DASHBOARD_ID IN (11, 12, 13);
  DELETE FROM EMS_DASHBOARD_LAST_ACCESS WHERE TENANT_ID='&TENANT_ID' AND DASHBOARD_ID IN (11, 12, 13);
  DELETE FROM EMS_DASHBOARD_TILE WHERE TENANT_ID='&TENANT_ID' AND DASHBOARD_ID IN (11, 12, 13);
  DELETE FROM EMS_DASHBOARD WHERE TENANT_ID='&TENANT_ID' AND DASHBOARD_ID IN (11, 12, 13);
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('OOB dashboard <Database Health Summary/Host Health Summary/WebLogic Health Summary> for tenant: &TENANT_ID are removed successfully! Upgraded records: '||V_COUNT);
ELSE
  DBMS_OUTPUT.PUT_LINE('OOB dashboard <Database Health Summary/Host Health Summary/WebLogic Health Summary> for tenant: &TENANT_ID are removed before, no need to upgrade again');
END IF;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to OOB dashboard <Database Health Summary/Host Health Summary/WebLogic Health Summary> for tenant: &TENANT_ID due to '||SQLERRM);
    RAISE;
END;
/




