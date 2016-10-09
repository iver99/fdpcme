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
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='SHOW_INHOME';
  IF v_count=0 THEN
     DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.SHOW_INHOME not exist');
  ELSE
     EXECUTE IMMEDIATE 'UPDATE EMS_DASHBOARD SET SHOW_INHOME=0 WHERE DASHBOARD_ID in (32,33,34,35,36,29,30,25,26,27)';
  END IF;
END;
/
