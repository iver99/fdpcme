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
  v_sql LONG;
BEGIN
    --create new table 'EMS_DASHBOARD_USER_OPTIONS' if not exists
  SELECT count(*) into v_count FROM user_tables where table_name = 'EMS_DASHBOARD_USER_OPTIONS';
  IF (v_count <= 0)  THEN
    v_sql := 'CREATE TABLE EMS_DASHBOARD_USER_OPTIONS
    (
    USER_NAME       VARCHAR2(128) NOT NULL,
    TENANT_ID       NUMBER(*,0) NOT NULL,
    DASHBOARD_ID    NUMBER(*,0) NOT NULL,
    AUTO_REFRESH_INTERVAL NUMBER(*,0) DEFAULT(0) NOT NULL,
    CONSTRAINT    EMS_DASHBOARD_USER_OPTIONS_PK PRIMARY KEY (USER_NAME,TENANT_ID,DASHBOARD_ID) USING INDEX
    )';
    EXECUTE IMMEDIATE v_sql;
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_USER_OPTIONS table created successfully');
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_USER_OPTIONS table exists already');
  END IF;

END;
/

DECLARE
  v_count     INTEGER;
BEGIN

  --add new column 'EMS_DASHBOARD_USER_OPTIONS.ACCESS_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_USER_OPTIONS' AND column_name='ACCESS_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_USER_OPTIONS ADD "ACCESS_DATE" TIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_USER_OPTIONS.ACCESS_DATE exists already, no change is needed');      
  END IF;
  
END;
/

DECLARE
  v_count     INTEGER;
BEGIN

  --add new column 'EMS_DASHBOARD_USER_OPTIONS.IS_FAVORITE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_USER_OPTIONS' AND column_name='IS_FAVORITE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_USER_OPTIONS ADD "IS_FAVORITE" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_USER_OPTIONS.IS_FAVORITE exists already, no change is needed');      
  END IF;

END;
/




