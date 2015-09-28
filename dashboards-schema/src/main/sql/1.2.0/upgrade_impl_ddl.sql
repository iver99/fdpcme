Rem --DDL change during upgrade

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_count     INTEGER;
BEGIN
  --add new column 'ENABLE_REFRESH'
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='ENABLE_REFRESH';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD ADD "ENABLE_REFRESH" NUMBER(1,0) DEFAULT(1) NOT NULL';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.ENABLE_REFRESH exists already, no change is needed');      
  END IF;
END;
/

