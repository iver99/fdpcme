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
Rem 	MODIFIED   (MM/DD/YY)
Rem     jnan        4/20/2016 - add column "ENABLE_ENTITY_FILTER" to EMS_DASHBOARD
Rem

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_count INTEGER;
BEGIN
  --add new column 'ENABLE_ENTITY_FILTER'
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_ENTITY_FILTER';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "ENABLE_ENTITY_FILTER" NUMBER(1, 0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_ENTITY_FILTER exists already, no change is needed');
  END IF;

END;
/
