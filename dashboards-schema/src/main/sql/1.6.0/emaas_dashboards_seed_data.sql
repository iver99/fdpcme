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


