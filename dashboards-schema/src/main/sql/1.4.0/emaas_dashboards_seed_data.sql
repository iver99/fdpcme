Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, Oracle and/or its affiliates. 
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
Rem    guochen  10/29/15 - update tiles time control support
Rem    miayu    10/26/15 - move to 1.4.0 folder
Rem    miayu    10/19/15- Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE

BEGIN  

--Rem screenshots for OOB ITA Dashboards
UPDATE EMS_DASHBOARD
SET LAST_MODIFICATION_DATE   =NVL(LAST_MODIFICATION_DATE,CREATION_DATE),
  LAST_MODIFIED_BY='Oracle'
WHERE tenant_id   ='&TENANT_ID'
AND dashboard_id <=1000;

commit;
DBMS_OUTPUT.PUT_LINE('Update OOB Dashboard modification data in Schema object: EMS_DASHBOARD for tenant: &TENANT_ID successfully');

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update OOB Dashboard modification data in Schema object: EMS_DASHBOARD for tenant: &TENANT_ID due to '||SQLERRM);   
  RAISE;
END;
/

DECLARE

BEGIN
  --Rem by default, OOBdashboards support 'auto' showing time control. 
  --UPDATE EMS_DASHBOARD SET ENABLE_TIME_RANGE=2 WHERE TENANT_ID='&TENANT_ID' AND IS_SYSTEM=1;

  --Rem by default, TA tiles support time control, other tile do not support
  UPDATE EMS_DASHBOARD_TILE SET WIDGET_SUPPORT_TIME_CONTROL=0 WHERE TENANT_ID='&TENANT_ID' AND PROVIDER_NAME='TargetAnalytics';
  UPDATE EMS_DASHBOARD_TILE SET WIDGET_SUPPORT_TIME_CONTROL=1 WHERE TENANT_ID='&TENANT_ID' AND PROVIDER_NAME<>'TargetAnalytics';
  
  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Update ENABLE_TIME_RANGE and WIDGET_SUPPORT_TIME_CONTROL in Schema object: EMS_DASHBOARD and EMS_DASHBOARD_TILE for tenant: &TENANT_ID successfully');

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update ENABLE_TIME_RANGE and WIDGET_SUPPORT_TIME_CONTROL in Schema object: EMS_DASHBOARD and EMS_DASHBOARD_TILE for tenant: &TENANT_ID due to '||SQLERRM);   
  RAISE;
END;
/

DECLARE
  SELECTED_TILE_HEIGHT      NUMBER;
  SELECTED_TILE_ROW         NUMBER;
BEGIN
  --Rem As default tile height in px is changed to 141px, previous height (num of rows) should be doubled to keep similar height in px
  SELECT HEIGHT INTO SELECTED_TILE_HEIGHT FROM EMS_DASHBOARD_TILE WHERE TILE_ID=14 AND DASHBOARD_ID=1 AND TENANT_ID='&TENANT_ID';
  SELECT TILE_ROW INTO SELECTED_TILE_ROW FROM EMS_DASHBOARD_TILE WHERE TILE_ID=14 AND DASHBOARD_ID=1 AND TENANT_ID='&TENANT_ID';
  IF SELECTED_TILE_HEIGHT=2 AND SELECTED_TILE_ROW=2 THEN
    DBMS_OUTPUT.PUT_LINE('TILE HEIGHT and TILE_ROW have been updated (doubled) for: &TENANT_ID, no need to update again');  
  ELSE
    UPDATE EMS_DASHBOARD_TILE SET HEIGHT=HEIGHT*2,TILE_ROW=TILE_ROW*2 WHERE TENANT_ID='&TENANT_ID';
  
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Update (double) HEIGHT and TILE_ROW in Schema object: EMS_DASHBOARD_TILE for tenant: &TENANT_ID successfully');
  END IF;

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update (double) HEIGHT and TILE_ROW in Schema object: EMS_DASHBOARD_TILE for tenant: &TENANT_ID due to '||SQLERRM);   
  RAISE;
END;
/
