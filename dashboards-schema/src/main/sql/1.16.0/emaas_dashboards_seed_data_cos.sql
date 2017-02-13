Rem
Rem emaas_dashboards_seed_data_cos.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016, 2017 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data_cos.sql
Rem
Rem    DESCRIPTION
Rem      Seed data for COSUPDATE
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    xiadai      02/09/17 - update data for Orchestration
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON
DEFINE TENANT_ID ='&1'
DECLARE
    V_TENANT_ID         NUMBER(38,0);
    V_COUNT             NUMBER;
    V_WIDGET_OLD_NAME   VARCHAR2(64 BYTE)   := 'Workflow Submission Details';
    V_WIDGET_NEW_NAME   VARCHAR2(64 BYTE)   := 'Execution Details';
    V_TID               NUMBER(38,0)        := '&TENANT_ID';
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

        SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE
            WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_OLD_NAME AND WIDGET_NAME = V_WIDGET_OLD_NAME;
        IF V_COUNT <1 THEN
            DBMS_OUTPUT.PUT_LINE('TILE Workflow Submission Details NOT USED NO NEED TO UPDATE FOR TENANT: ' ||V_TENANT_ID);
        ELSE
            UPDATE EMS_DASHBOARD_TILE SET TITLE = V_WIDGET_NEW_NAME, WIDGET_NAME = V_WIDGET_NEW_NAME
                WHERE TENANT_ID = V_TENANT_ID AND TITLE = V_WIDGET_OLD_NAME AND WIDGET_NAME = V_WIDGET_OLD_NAME;
            DBMS_OUTPUT.PUT_LINE('TILE Workflow Submission Details UPDATED SUCCESSFULLY FOR TENANT: ' ||V_TENANT_ID);
        END IF;

     IF (V_TID<>-1) THEN
        EXIT;
     END IF;
  END LOOP;
  CLOSE TENANT_CURSOR;
  COMMIT;

EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update Screenshot of some ITA OOB widget according to EMCPDF-2904 due to '||SQLERRM);
  RAISE;  
END;
/