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

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_count     INTEGER;
BEGIN

  --add new column 'EMS_DASHBOARD.SHARE_PUBLIC'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='SHARE_PUBLIC';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "SHARE_PUBLIC" NUMBER(1,0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.SHARE_PUBLIC exists already, no change is needed');      
  END IF;
END;
/

