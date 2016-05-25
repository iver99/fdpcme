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

DECLARE
  v_count     INTEGER;
BEGIN

  --merge rows from  EMS_DASHBOARD_LAST_ACCESS to EMS_DASHBOARD_USER_OPTIONS 
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_LAST_ACCESS';
  IF v_count > 0 THEN
    DBMS_OUTPUT.PUT_LINE('Merge rows from  EMS_DASHBOARD_LAST_ACCESS to EMS_DASHBOARD_USER_OPTIONS for tenant: &TENANT_ID');
    MERGE INTO EMS_DASHBOARD_USER_OPTIONS ops USING (SELECT * FROM EMS_DASHBOARD_LAST_ACCESS WHERE TENANT_ID = '&TENANT_ID') la ON (ops.TENANT_ID = la.TENANT_ID and ops.DASHBOARD_ID = la.DASHBOARD_ID and ops.USER_NAME = la.ACCESSED_BY) WHEN MATCHED THEN UPDATE SET ops.ACCESS_DATE = la.ACCESS_DATE WHEN NOT MATCHED THEN INSERT (TENANT_ID, DASHBOARD_ID, USER_NAME, ACCESS_DATE) VALUES (la.TENANT_ID, la.DASHBOARD_ID, la.ACCESSED_BY, la.ACCESS_DATE);
    COMMIT;
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_LAST_ACCESS not exists, no change is needed');      
  END IF;
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to merge rows from  EMS_DASHBOARD_LAST_ACCESS to EMS_DASHBOARD_USER_OPTIONS for tenant: &TENANT_ID due to '||SQLERRM);
END;
/

DECLARE
  v_count     INTEGER;
BEGIN

  --merge rows from  EMS_DASHBOARD_FAVORITE to EMS_DASHBOARD_USER_OPTIONS
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_FAVORITE';
  IF v_count > 0 THEN
    DBMS_OUTPUT.PUT_LINE('Merge rows from  EMS_DASHBOARD_FAVORITE to EMS_DASHBOARD_USER_OPTIONS for tenant: &TENANT_ID');
    MERGE INTO EMS_DASHBOARD_USER_OPTIONS ops USING (SELECT * FROM EMS_DASHBOARD_FAVORITE WHERE TENANT_ID = '&TENANT_ID') la ON (ops.TENANT_ID = la.TENANT_ID and ops.DASHBOARD_ID = la.DASHBOARD_ID and ops.USER_NAME = la.USER_NAME) WHEN MATCHED THEN UPDATE SET ops.IS_FAVORITE = 1 WHEN NOT MATCHED THEN INSERT (TENANT_ID, DASHBOARD_ID, USER_NAME, IS_FAVORITE) VALUES (la.TENANT_ID, la.DASHBOARD_ID, la.USER_NAME, 1);
    COMMIT;
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_FAVORITE not exists, no change is needed');      
  END IF;
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to merge rows from  EMS_DASHBOARD_FAVORITE to EMS_DASHBOARD_USER_OPTIONS for tenant: &TENANT_ID due to '||SQLERRM);
  RAISE;
END;
/




