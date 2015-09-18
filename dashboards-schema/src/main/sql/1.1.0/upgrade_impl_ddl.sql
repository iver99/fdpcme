Rem --DDL change during upgrade

SET FEEDBACK ON
SET SERVEROUTPUT ON
DECLARE
  v_count     INTEGER;
  v_length     INTEGER;
BEGIN

  --add new columns 'TILE_ROW', 'TILE_COLUMN', 'TYPE' for 'EMS_DASHBOARD'
  --TILE_ROW: row of the tile
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='TILE_ROW';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "TILE_ROW" NUMBER(*,0)';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.TILE_ROW exists already, no change is needed');      
  END IF;
  --TILE_COLUMN: column of the tile
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='TILE_COLUMN';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "TILE_COLUMN" NUMBER(*,0)';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.TILE_COLUMN exists already, no change is needed');      
  END IF;
  --TYPE: type of the tile, 0: default tile, 1: whole line text tile
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_TILE' AND column_name='TYPE';
  IF v_count=0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE ADD "TYPE" NUMBER(*,0)';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_TILE.TYPE exists already, no change is needed');      
  END IF;
  --update dashboard parameter string value column size
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_TILE_PARAMS MODIFY PARAM_VALUE_STR VARCHAR2(4000)';

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
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD.DESCRIPTION length is over 1280, no need to change.');      
  END IF;
END;
/

