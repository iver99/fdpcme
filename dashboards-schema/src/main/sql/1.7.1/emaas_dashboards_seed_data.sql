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
  CURSOR C_CURSOR IS SELECT PREF_VALUE FROM EMS_PREFERENCE WHERE PREF_KEY = 'Dashboards.dashboardsFilter' AND TENANT_ID = '&TENANT_ID' FOR UPDATE; 
  V_VALUE EMS_PREFERENCE.PREF_VALUE%TYPE; 
  V_TEMP EMS_PREFERENCE.PREF_VALUE%TYPE; 
BEGIN
  OPEN C_CURSOR; 
  LOOP 
     FETCH C_CURSOR INTO V_VALUE; 
     EXIT WHEN C_CURSOR%NOTFOUND; 
     --DBMS_OUTPUT.PUT_LINE('V_VALUE: '||V_VALUE);
     
     IF (TRIM(V_VALUE) LIKE '{%') THEN
          -- old format filter preference, convert
          --DBMS_OUTPUT.PUT_LINE('V_VALUE: '||V_VALUE);
          V_TEMP := NULL;
          IF (V_VALUE LIKE '%APM%') THEN
              V_TEMP := CASE WHEN V_TEMP IS NULL THEN '' ELSE V_TEMP || ',' END || 'apm';
              --DBMS_OUTPUT.PUT_LINE('V_TEMP: '||V_TEMP);
          END IF;
          IF (V_VALUE LIKE '%ITAnalytics%') THEN
              V_TEMP := CASE WHEN V_TEMP IS NULL THEN '' ELSE V_TEMP || ',' END || 'ita';
              --DBMS_OUTPUT.PUT_LINE('V_TEMP: '||V_TEMP);
          END IF;
          IF (V_VALUE LIKE '%LogAnalytics%') THEN
              V_TEMP := CASE WHEN V_TEMP IS NULL THEN '' ELSE V_TEMP || ',' END || 'la';
              --DBMS_OUTPUT.PUT_LINE('V_TEMP: '||V_TEMP);
          END IF;
          IF (V_VALUE LIKE '%true%') THEN
              V_TEMP := CASE WHEN V_TEMP IS NULL THEN '' ELSE V_TEMP || ',' END || 'favorites';
              --DBMS_OUTPUT.PUT_LINE('V_TEMP: '||V_TEMP);
          END IF;
          IF (V_VALUE LIKE '%Oracle%') THEN
              V_TEMP := CASE WHEN V_TEMP IS NULL THEN '' ELSE V_TEMP || ',' END || 'oracle';
              --DBMS_OUTPUT.PUT_LINE('V_TEMP: '||V_TEMP);
          END IF;
          IF (V_VALUE LIKE '%Share%') THEN
              V_TEMP := CASE WHEN V_TEMP IS NULL THEN '' ELSE V_TEMP || ',' END || 'share';
              --DBMS_OUTPUT.PUT_LINE('V_TEMP: '||V_TEMP);
          END IF;
          IF (V_VALUE LIKE '%Me%') THEN
              V_TEMP := CASE WHEN V_TEMP IS NULL THEN '' ELSE V_TEMP || ',' END || 'me';
              --DBMS_OUTPUT.PUT_LINE('V_TEMP: '||V_TEMP);
          END IF;
          
          -- UPDATE
          IF (V_TEMP IS NOT NULL) THEN
             --DBMS_OUTPUT.PUT_LINE('NOT NULL V_TEMP: '||V_TEMP);
             UPDATE EMS_PREFERENCE SET PREF_VALUE = V_TEMP WHERE CURRENT OF C_CURSOR;
          END IF;
     END IF;
   END LOOP;
   CLOSE C_CURSOR; 
   
   COMMIT;
   DBMS_OUTPUT.PUT_LINE('Update EMS_PREFERENCE for PREF_KEY = Dashboards.dashboardsFilter successfully for tenant: &TENANT_ID');
  
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update EMS_PREFERENCE for PREF_KEY = Dashboards.dashboardsFilter for tenant: &TENANT_ID due to '||SQLERRM);
  RAISE;
END;
/



