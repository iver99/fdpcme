Rem --DDL change during upgrade
Rem
Rem upgrade_impl_ddl.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016 Oracle and/or its affiliates.
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
---add new unique index EMS_DASHBOARD_U2 ON EMS_DASHBOARD(UPPER(NAME), DESCRIPTION, OWNER, TENANT_ID, DELETED)
@&EMSAAS_SQL_ROOT/1.13.0/emaas_dashboards_new_unique_index.sql -1

DECLARE
  v_count     INTEGER;
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_TIME_RANGE';
  IF v_count>0 THEN
    EXECUTE IMMEDIATE 'alter table ems_dashboard modify (ENABLE_TIME_RANGE NUMBER(2,0) DEFAULT(1))';  
	COMMIT;
	DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_TIME_RANGE is change default value to 1!');     
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_TIME_RANGE is not existed! Cannot change column default value!');      
  END IF;
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to change default value for  EMS_DASHBOARD.ENABLE_TIME_RANGE to 1 due to '||SQLERRM);
  RAISE;
END;
/
