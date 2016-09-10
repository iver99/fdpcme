Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016 Oracle and/or its affiliates. 
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
Rem    ADUAN  5/27/16 - Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  V_COUNT     NUMBER;
  V_TENANT_ID NUMBER(38,0);
  V_TID       NUMBER(38,0) := '&TENANT_ID';
  CURSOR TENANT_CURSOR IS
    SELECT DISTINCT TENANT_ID FROM EMS_DASHBOARD ORDER BY TENANT_ID;

BEGIN
  OPEN TENANT_CURSOR;
  LOOP
    IF (V_TID<>-1) THEN
      V_TENANT_ID:=V_TID;
    ELSE
      FETCH TENANT_CURSOR INTO V_TENANT_ID;
      EXIT WHEN TENANT_CURSOR%NOTFOUND;     
    END IF;  

    SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE WHERE TENANT_ID=V_TENANT_ID AND PROVIDER_NAME='LoganService';
    IF (V_COUNT>0) THEN
      UPDATE EMS_DASHBOARD_TILE SET PROVIDER_NAME='LogAnalyticsUI' WHERE TENANT_ID=V_TENANT_ID AND PROVIDER_NAME='LoganService';
      COMMIT;
      DBMS_OUTPUT.PUT_LINE('Provider name of LogAnalytics has been upgraded from [LoganService] to [LogAnalyticsUI] successfully for tenant: '||V_TENANT_ID ||'! Upgraded records: '||V_COUNT);
    ELSE
      DBMS_OUTPUT.PUT_LINE('Provider name of LogAnalytics has been upgraded from [LoganService] to [LogAnalyticsUI] for tenant: '||V_TENANT_ID ||' before, no need to upgrade again');
    END IF;

    IF (V_TID<>-1) THEN
      EXIT;
    END IF;
  END LOOP;
  CLOSE TENANT_CURSOR;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to upgrade provider name of LogAnalytics from [LoganService] to [LogAnalyticsUI] for tenant: '||V_TENANT_ID ||' due to '||SQLERRM);
    RAISE;
END;
/
