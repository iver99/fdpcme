Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data.sql 
Rem
Rem    DESCRIPTION
Rem      Seed data for TAUPDATE
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    Jie/Miao     09/27/16
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON
DEFINE TENANT_ID ='&1'

DECLARE
  V_TENANT NUMBER(38,0) := '&TENANT_ID';
  V_TENANT_ID NUMBER(38,0);
  V_TID NUMBER(38,0) := '&TENANT_ID';
  V_SHOW_INHOME NUMBER(38,0);
  v_count NUMBER(38,0);

  CURSOR TENANT_CURSOR IS
    SELECT DISTINCT TENANT_ID FROM EMS_DASHBOARD ORDER BY TENANT_ID;

BEGIN

  LOOP
    IF (V_TID<>-1) THEN
      V_TENANT_ID:=V_TID;
    ELSE
      IF NOT TENANT_CURSOR%ISOPEN THEN
        OPEN TENANT_CURSOR;
       END IF;
       FETCH TENANT_CURSOR INTO V_TENANT_ID;
      EXIT WHEN TENANT_CURSOR%NOTFOUND;     
    END IF;  
    
    V_SHOW_INHOME:=1;
    SELECT count(*) into v_count from EMS_DASHBOARD 
    WHERE DASHBOARD_ID in (32,33,34,35,36,29,30,25,26,27) AND SHOW_INHOME=V_SHOW_INHOME AND TENANT_ID=V_TENANT_ID;
    IF v_count=0 THEN
       DBMS_OUTPUT.PUT_LINE('No need to update again. SHOW_INHOME have been updated before according to EMCPDF-1871 for tenant: '||V_TENANT_ID);
    ELSE
       V_SHOW_INHOME:=0;
       UPDATE EMS_DASHBOARD SET SHOW_INHOME=V_SHOW_INHOME 
       WHERE DASHBOARD_ID in (32,33,34,35,36,29,30,25,26,27) and TENANT_ID=V_TENANT_ID;
       DBMS_OUTPUT.PUT_LINE('SHOW_INHOME have been updated according to EMCPDF-1871 for tenant: '||V_TENANT_ID);
    END IF;
    IF (V_TID<>-1) THEN
      EXIT;
    END IF;
  END LOOP;
  IF TENANT_CURSOR%ISOPEN THEN
    CLOSE TENANT_CURSOR;
  END IF;

  commit;
  DBMS_OUTPUT.PUT_LINE('SHOW_INHOME have been updated and change is committed');
  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to update the sql due to '||SQLERRM);
    RAISE;
END;
/