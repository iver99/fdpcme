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
  @&EMSAAS_SQL_ROOT/1.12.0/emaas_dashboards_new_unique_index.sql -1


DECLARE
  v_count     INTEGER;
BEGIN

  --add new column 'EMS_DASHBOARD_TILE.WIDGET_DELETED'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='WIDGET_DELETED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "WIDGET_DELETED" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.WIDGET_DELETED exists already, no change is needed');
  END IF;
  
    --add new column 'EMS_DASHBOARD_TILE.WIDGET_DELETION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='WIDGET_DELETION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "WIDGET_DELETION_DATE" TIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.WIDGET_DELETION_DATE exists already, no change is needed');
  END IF;

  ---update coluum show_inhome value
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='SHOW_INHOME';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "SHOW_INHOME" NUMBER(1,0) DEFAULT(1) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.SHOW_INHOME exists already, no change is needed');
  END IF;

  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to update the sql due to '||SQLERRM);
    RAISE;
END;
/

