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
  --create new table 'EMS_DASHBOARD_SET' if not exists
  SELECT count(*) into v_count FROM user_tables where table_name = 'EMS_DASHBOARD_SET';
  IF (v_count <= 0)  THEN
    v_sql := 'CREATE TABLE EMS_DASHBOARD_SET
    (
    DASHBOARD_SET_ID   NUMBER(*,0) NOT NULL,
    TENANT_ID          NUMBER(*,0) NOT NULL,
    SUB_DASHBOARD_ID   NUMBER(*,0) NOT NULL,
    POSITION
    CONSTRAINT EMS_DASHBOARD_SET_PK PRIMARY KEY (DASHBOARD_SET_ID,SUB_DASHBOARD_ID,TENANT_ID) USING INDEX,
    CONSTRAINT EMS_DASHBOARD_SET_FK1 FOREIGN KEY (DASHBOARD_SET_ID, TENANT_ID) REFERENCES EMS_DASHBOARD (DASHBOARD_ID, TENANT_ID),
    CONSTRAINT EMS_DASHBOARD_SET_FK2 FOREIGN KEY (SUB_DASHBOARD_ID, TENANT_ID) REFERENCES EMS_DASHBOARD (DASHBOARD_ID, TENANT_ID)
    )';
    EXECUTE IMMEDIATE v_sql;
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_SET table created successfully');
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_SET table exists already');
  END IF;

END;
/

