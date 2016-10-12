Rem
Rem
Rem emaas_drop_tables.sql
Rem
Rem
Rem    NAME
Rem      emaas_drop_tables.sql
Rem
Rem    DESCRIPTION
Rem      drop the two tables ems_dashboards_favorite and ems_dashboards_last_access at the end of onboarding as they are useless;
Rem		 For JIRA : https://jira.oraclecorp.com/jira/browse/EMCPDF-1809
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    pamela   8/15/2016  Created

SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  v_count       NUMBER;
BEGIN
  --remove EMS_DASHBOARD_FAVORITE 
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_FAVORITE';
  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'DROP TABLE EMS_DASHBOARD_FAVORITE PURGE';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_FAVORITE does not exist, no change is needed');      
  END IF;
  
  --remove EMS_DASHBOARD_LAST_ACCESS
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_LAST_ACCESS';
  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'DROP TABLE EMS_DASHBOARD_LAST_ACCESS PURGE';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_LAST_ACCESS does not exist, no change is needed');      
  END IF;
END;
/

