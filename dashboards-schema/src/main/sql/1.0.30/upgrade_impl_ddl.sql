Rem --DDL change during upgrade

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_length     INTEGER;
BEGIN
  
  --increase column 'NAME' length to 320
  SELECT DATA_LENGTH INTO v_length FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='NAME';
  IF v_length  < 320 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD MODIFY ( NAME  VARCHAR2(320) )';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.NAME length is over 320, no need to change.');      
  END IF;
  
  --increase column 'DESCRIPTION' length to 1280
  SELECT DATA_LENGTH INTO v_length FROM user_tab_columns WHERE table_name='EMS_DASHBOARD' AND column_name='DESCRIPTION';
  IF v_length  < 1280 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD MODIFY ( DESCRIPTION  VARCHAR2(1280) )';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.DESCRIPTION length is over 320, no need to change.');      
  END IF;
END;
/

