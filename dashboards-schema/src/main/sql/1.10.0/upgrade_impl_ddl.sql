Rem
Rem upgrade_impl_ddl.sql
Rem
Rem All rights reserved.
Rem
Rem    NAME
Rem      upgrade_impl_ddl.sql 
Rem
Rem    DESCRIPTION
Rem      DDL change during upgrade
Rem
Rem    NOTES
Rem      None
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON


DECLARE
  v_count     INTEGER;
BEGIN

  --remove EMS_DASHBOARD_FAVORITE 
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_FAVORITE';
  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'DROP TABLE EMS_DASHBOARD_FAVORITE';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_FAVORITE does not exist, no change is needed');      
  END IF;
  
  --remove EMS_DASHBOARD_LAST_ACCESS
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_LAST_ACCESS';
  IF v_count > 0 THEN
    EXECUTE IMMEDIATE 'DROP TABLE EMS_DASHBOARD_LAST_ACCESS';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_LAST_ACCESS does not exist, no change is needed');      
  END IF;

END;
/


