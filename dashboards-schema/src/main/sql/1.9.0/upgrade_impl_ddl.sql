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
  v_count INTEGER;
BEGIN
 --add new column 'DELETED' for EMS_DASHBOARD_SET
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_DASHBOARD_SET' AND column_name='DELETED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_SET ADD "DELETED" NUMBER(1, 0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_SET.DELETED exists already, no change is needed');
  END IF;
  
 --add new column 'DELETED' for EMS_DASHBOARD_TILE
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='DELETED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "DELETED" NUMBER(1, 0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.DELETED exists already, no change is needed');
  END IF;
  
 --add new column 'DELETED' for EMS_DASHBOARD_TILE_PARAMS
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE_PARAMS' AND column_name='DELETED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE_PARAMS ADD "DELETED" NUMBER(1, 0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE_PARAMS.DELETED exists already, no change is needed');
  END IF;
  
 --add new column 'DELETED' for EMS_PREFERENCE
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_PREFERENCE' AND column_name='DELETED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_PREFERENCE ADD "DELETED" NUMBER(1, 0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_PREFERENCE.DELETED exists already, no change is needed');
  END IF;
END;
/
