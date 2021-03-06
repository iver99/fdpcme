Rem --DDL change during upgrade

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_max_tileId INTEGER;
  v_increaseby INTEGER;
  v_count      INTEGER;
BEGIN
  --upgrade sequence increaseby of EMS_DASHBOARD_TILE_SEQ
  SELECT increment_by
  INTO v_increaseby
  FROM user_sequences
  WHERE sequence_name='EMS_DASHBOARD_TILE_SEQ';
  IF v_increaseby>1 THEN
    SELECT MAX(tile_Id) INTO v_max_tileId FROM EMS_DASHBOARD_TILE;
    EXECUTE IMMEDIATE 'DROP SEQUENCE EMS_DASHBOARD_TILE_SEQ';
    EXECUTE IMMEDIATE 'CREATE SEQUENCE EMS_DASHBOARD_TILE_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH '||(v_max_tileId+20)||' CACHE 20 ORDER  NOCYCLE';
    
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE_SEQ upgraded');
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE_SEQ is good and no change is needed'); 
  END IF;

  --add new table for user preference 
  SELECT COUNT(*) INTO v_count FROM user_tables WHERE table_name='EMS_PREFERENCE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'CREATE TABLE EMS_PREFERENCE(USER_NAME VARCHAR2(128) NOT NULL, PREF_KEY VARCHAR2(256) NOT NULL,PREF_VALUE VARCHAR2(256) NOT NULL,TENANT_ID NUMBER(*,0) NOT NULL,CONSTRAINT EMS_PREFERENCES_PK PRIMARY KEY (USER_NAME, PREF_KEY, TENANT_ID) USING INDEX)'; 
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_PREFERENCE created');    
  ELSE 
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_PREFERENCE exists already, no change is needed');      
  END IF;
  
  --add new column 'APPLICATION_TYPE' for 'EMS_DASHBOARD'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='APPLICATION_TYPE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD APPLICATION_TYPE NUMBER(4,0)';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.APPLICATION_TYPE exists already, no change is needed');      
  END IF;
END;
/

