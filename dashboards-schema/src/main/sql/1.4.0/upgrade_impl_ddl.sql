Rem --DDL change during upgrade
Rem
Rem upgrade_impl_ddl.sql
Rem
Rem Copyright (c) 2013, 2014, Oracle and/or its affiliates. 
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
Rem    MODIFIED   (MM/DD/YY)
Rem    miayu     10/26/2015 - move to 1.4.0 folder
Rem    miayu     10/16/2015 - move to 1.3.0 folder
Rem    Guobao    09/28/2015 - Created

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_count     INTEGER;
  v_precision INTEGER;
BEGIN
  --add new column 'ENABLE_REFRESH'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_REFRESH';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "ENABLE_REFRESH" NUMBER(1,0) DEFAULT(1) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_REFRESH exists already, no change is needed');      
  END IF;
  
  --update column 'EMS_DASHBOARD.ENABLE_TIME_RANGE' precision
  SELECT COALESCE(SUM(data_precision),0) INTO v_precision FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_TIME_RANGE';
  IF v_precision=1 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD MODIFY "ENABLE_TIME_RANGE" NUMBER(2,0) DEFAULT(2)';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_TIME_RANGE has been updated already, no change is needed');      
  END IF;
  
  --add new column 'EMS_DASHBOARD_TILE.WIDGET_SUPPORT_TIME_CONTROL'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='WIDGET_SUPPORT_TIME_CONTROL';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "WIDGET_SUPPORT_TIME_CONTROL" NUMBER(1,0) DEFAULT(1) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.WIDGET_SUPPORT_TIME_CONTROL exists already, no change is needed');      
  END IF;
END;
/

