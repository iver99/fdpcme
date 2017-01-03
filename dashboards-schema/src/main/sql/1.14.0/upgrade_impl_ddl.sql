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
  v_default   varchar2(4):='(1)';
BEGIN
  --alter column EMS_DASHBOARD_TILE.TILE_ID
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='TILE_ID';
  IF v_count>0 THEN
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE drop constraint EMS_DASHBOARD_TILE_PK cascade drop index';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE rename column TILE_ID to TEMP_ID';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE add TILE_ID varchar2(63)';
    EXECUTE IMMEDIATE 'update EMS_DASHBOARD_TILE set TILE_ID = to_char(TEMP_ID)';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE drop column TEMP_ID';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE add constraint EMS_DASHBOARD_TILE_PK primary key(TILE_ID, TENANT_ID) USING INDEX';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.TILE_ID does not exist, no change is needed');
  END IF;
  
  --alter column EMS_DASHBOARD_TILE_PARAMS.TILE_ID
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE_PARAMS' AND column_name='TILE_ID';
  IF v_count>0 THEN
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE_PARAMS drop constraint EMS_DASHBOARD_TILE_PARAMS_PK cascade drop index';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE_PARAMS rename column TILE_ID to TEMP_ID';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE_PARAMS add TILE_ID varchar2(63)';
    EXECUTE IMMEDIATE 'update EMS_DASHBOARD_TILE_PARAMS set TILE_ID = to_char(TEMP_ID)';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE_PARAMS drop column TEMP_ID';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE_PARAMS add constraint EMS_DASHBOARD_TILE_PARAMS_PK primary key(TILE_ID, TENANT_ID, PARAM_NAME) USING INDEX';
    EXECUTE IMMEDIATE 'alter table EMS_DASHBOARD_TILE_PARAMS add constraint EMS_DASHBOARD_TILE_PARAMS_FK1 FOREIGN KEY (TILE_ID, TENANT_ID) REFERENCES EMS_DASHBOARD_TILE (TILE_ID, TENANT_ID)';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE_PARAMS.TILE_ID does not exist, no change is needed');
  END IF;
  
  --add new column 'EMS_DASHBOARD_SET.CREATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_SET' AND column_name='CREATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_SET ADD CREATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_SET.CREATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'EMS_DASHBOARD_SET.LAST_MODIFICATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_SET' AND column_name='LAST_MODIFICATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_SET ADD LAST_MODIFICATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_SET.LAST_MODIFICATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'EMS_DASHBOARD_TILE_PARAMS.CREATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE_PARAMS' AND column_name='CREATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE_PARAMS ADD CREATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE_PARAMS.CREATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'EMS_DASHBOARD_TILE_PARAMS.LAST_MODIFICATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE_PARAMS' AND column_name='LAST_MODIFICATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE_PARAMS ADD LAST_MODIFICATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE_PARAMS.LAST_MODIFICATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'EMS_DASHBOARD_USER_OPTIONS.CREATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_USER_OPTIONS' AND column_name='CREATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_USER_OPTIONS ADD CREATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_USER_OPTIONS.CREATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'EMS_DASHBOARD_USER_OPTIONS.LAST_MODIFICATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_USER_OPTIONS' AND column_name='LAST_MODIFICATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_USER_OPTIONS ADD LAST_MODIFICATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_USER_OPTIONS.LAST_MODIFICATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'EMS_PREFERENCE.CREATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_PREFERENCE' AND column_name='CREATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_PREFERENCE ADD CREATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_PREFERENCE.CREATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'EMS_PREFERENCE.LAST_MODIFICATION_DATE'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_PREFERENCE' AND column_name='LAST_MODIFICATION_DATE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_PREFERENCE ADD LAST_MODIFICATION_DATE TIMESTAMP(6) DEFAULT LOCALTIMESTAMP';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_PREFERENCE.LAST_MODIFICATION_DATE exists already, no change is needed');
  END IF;
  
  --add new column 'DELETED' for EMS_DASHBOARD_SET
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_DASHBOARD_SET' AND column_name='DELETED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_SET ADD "DELETED" NUMBER(*, 0) DEFAULT(0) NOT NULL';
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
  
  --add new column 'DELETED' for EMS_DASHBOARD_USER_OPTIONS
  SELECT count(*) into v_count from user_tab_columns WHERE table_name='EMS_DASHBOARD_USER_OPTIONS' AND column_name='DELETED';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_USER_OPTIONS ADD "DELETED" NUMBER(1, 0) DEFAULT(0) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_USER_OPTIONS.DELETED exists already, no change is needed');
  END IF;
 
  
  EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to run the sql which is used to support ZDT due to: '||SQLERRM);
    RAISE;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TRIGGER EMS_DASHBOARD_TR DISABLE';
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -4080 THEN
         RAISE;
      END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TRIGGER EMS_DASHBOARD_TILE_TR DISABLE';
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -4080 THEN
         RAISE;
      END IF;
END;
/

DECLARE
  v_count     INTEGER;
  v_default   varchar2(4):='(1)';
BEGIN
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_TIME_RANGE';
  IF v_count>0 THEN
	SELECT data_default into v_default FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_TIME_RANGE';
	if v_default!='(1)' THEN
		EXECUTE IMMEDIATE 'alter table ems_dashboard modify (ENABLE_TIME_RANGE NUMBER(2,0) DEFAULT(1))';
		DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_TIME_RANGE is change default value to 1!');
	ELSE
		DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_TIME_RANGE default value is alreay set to 1, no need to update!');
	END IF;
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


