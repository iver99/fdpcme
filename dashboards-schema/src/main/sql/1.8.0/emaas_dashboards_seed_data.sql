Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016 Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data.sql 
Rem
Rem    DESCRIPTION
Rem      Dashboard Framework seed data sql file.
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    guochen  5/27/16 - Created
Rem

DEFINE TENANT_ID = '&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  CURSOR DSBS_CUR IS
      SELECT DASHBOARD_ID FROM EMS_DASHBOARD WHERE DELETED=0 AND TENANT_ID='&TENANT_ID';
  DSB DSBS_CUR%ROWTYPE;

  CURSOR TILES_CUR(DSB_ID NUMBER) IS
      SELECT * FROM EMS_DASHBOARD_TILE WHERE DASHBOARD_ID=DSB_ID AND TENANT_ID='&TENANT_ID' ORDER BY TILE_ROW, TILE_COLUMN;
  TILE TILES_CUR%ROWTYPE;
  V_MAX_WIDTH NUMBER;
  UPDATED_COLUMNS NUMBER := 12;
  TILE_NEW_COL NUMBER;
  TILE_NEW_WIDTH NUMBER;
BEGIN
  SELECT MAX(WIDTH) INTO V_MAX_WIDTH FROM EMS_DASHBOARD_TILE WHERE TENANT_ID='&TENANT_ID';
  IF (V_MAX_WIDTH=UPDATED_COLUMNS) THEN
    DBMS_OUTPUT.PUT_LINE('Data in Schema object: EMS_DASHBOARD_TILE have been updated to support dashboard layout upgrade from 8 columns to 12 columns for tenant: &TENANT_ID, no need to update again'); 
    RETURN;
  END IF;
  OPEN DSBS_CUR;
  LOOP
    FETCH DSBS_CUR INTO DSB;
    EXIT WHEN DSBS_CUR%NOTFOUND;
    --DBMS_OUTPUT.PUT_LINE('Updating dashboard with id=' || DSB.DASHBOARD_ID);
    TILE_NEW_COL := 0;
    TILE_NEW_WIDTH := 0;
      OPEN TILES_CUR(DSB.DASHBOARD_ID);
      LOOP
        FETCH TILES_CUR INTO TILE;
        EXIT WHEN TILES_CUR%NOTFOUND;
        TILE_NEW_COL := CEIL(TILE.TILE_COLUMN * 1.5);
        TILE_NEW_WIDTH := TILE.WIDTH * 1.5 - (MOD(TILE.TILE_COLUMN, 2) - 0.5) * MOD(TILE.WIDTH, 2);
        UPDATE EMS_DASHBOARD_TILE SET TILE_COLUMN=TILE_NEW_COL, WIDTH=TILE_NEW_WIDTH WHERE TILE_ID=TILE.TILE_ID AND TENANT_ID='&TENANT_ID';
        --DBMS_OUTPUT.PUT_LINE('         Upgrade to 12 columns for tile with id= ' || TILE.TILE_ID || ' for dashboard with id=' || DSB.DASHBOARD_ID || '. Old left column is ' || TILE.TILE_COLUMN || ', old width is ' || TILE.WIDTH || '. After upgrade, left column is ' || TILE_NEW_COL || ', width is ' || TILE_NEW_WIDTH);
      END LOOP;
      CLOSE TILES_CUR;
  END LOOP;
  CLOSE DSBS_CUR;

  COMMIT;
  DBMS_OUTPUT.PUT_LINE('Data in Schema object: EMS_DASHBOARD_TILE have been updated to support dashboard layout upgrade from 8 columns to 12 columns successfully for tenant: &TENANT_ID');    
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to update data in Schema object: EMS_DASHBOARD_TILE to support dashboard layout upgrade from 8 columns to 12 columns for tenant: &TENANT_ID to due to '||SQLERRM); 
    RAISE;
END;
/
