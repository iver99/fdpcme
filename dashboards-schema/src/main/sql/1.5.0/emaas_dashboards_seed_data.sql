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
Rem    guochen  12/22/15 - Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  V_COUNT NUMBER;
BEGIN
  SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE WHERE TENANT_ID='&TENANT_ID' AND WIDGET_UNIQUE_ID = '3024' AND WIDGET_LINKED_DASHBOARD = '8';
  IF (V_COUNT>0) THEN
    DBMS_OUTPUT.PUT_LINE('WIDGET_LINKED_DASHBOARD already updated for WIDGET 3024/3026 for: &TENANT_ID, no need to update again');
  ELSE
    UPDATE EMS_DASHBOARD_TILE
    SET WIDGET_LINKED_DASHBOARD  ='8'
    WHERE WIDGET_UNIQUE_ID ='3024'
    AND TENANT_ID='&TENANT_ID';

    UPDATE EMS_DASHBOARD_TILE
    SET WIDGET_LINKED_DASHBOARD  ='10'
    WHERE WIDGET_UNIQUE_ID ='3026'
    AND TENANT_ID='&TENANT_ID';

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Update WIDGET_LINKED_DASHBOARD for WIDGET 3024/3026 in Schema object: EMS_DASHBOARD_TILE successfully for tenant: &TENANT_ID');
  END IF;
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update WIDGET_LINKED_DASHBOARD for WIDGET 3024/3026 in Schema object: EMS_DASHBOARD_TILE for tenant: &TENANT_ID due to '||SQLERRM);
  RAISE;
END;
/


DECLARE
  V_COUNT number;
BEGIN

SELECT COUNT(1) INTO V_COUNT from EMS_DASHBOARD_TILE where TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='0.1';
IF (V_COUNT>0) THEN
  UPDATE EMS_DASHBOARD_TILE SET PROVIDER_VERSION='1.0' WHERE TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='0.1';
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Provider version has been upgrade from 0.1 to 1.0 for tenant: &TENANT_ID successfully! Upgraded records: '||V_COUNT);
ELSE
  DBMS_OUTPUT.PUT_LINE('Provider version has been upgrade from 0.1 to 1.0 for tenant: &TENANT_ID before, no need to upgrade again');
END IF;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to upgrade version from 0.1 to 1.0 for tenant: &TENANT_ID due to '||SQLERRM);
    RAISE;
END;
/

DECLARE
  V_COUNT number;
BEGIN

SELECT COUNT(1) INTO V_COUNT from EMS_DASHBOARD_TILE where TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='1.0.5' AND PROVIDER_NAME='TargetAnalytics';
IF (V_COUNT>0) THEN
  UPDATE EMS_DASHBOARD_TILE SET PROVIDER_VERSION='1.0' WHERE TENANT_ID='&TENANT_ID' AND PROVIDER_VERSION='1.0.5' AND PROVIDER_NAME='TargetAnalytics';
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Provider version of TargetAnalytics has been upgrade from 1.0.5 to 1.0 for tenant: &TENANT_ID successfully! Upgraded records: '||V_COUNT);
ELSE
  DBMS_OUTPUT.PUT_LINE('Provider version of TargetAnalytics has been upgrade from 1.0.5 to 1.0 for tenant: &TENANT_ID before, no need to upgrade again');
END IF;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to upgrade version of TargetAnalytics from 1.0.5 to 1.0 for tenant: &TENANT_ID due to '||SQLERRM);
    RAISE;
END;
/
