Rem
Rem emaas_dashboards_seed_data.sql
Rem
Rem Copyright (c) 2013, 2014, Oracle and/or its affiliates. 
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
Rem    guochen    09/11/15- Created
Rem

DEFINE TENANT_ID = '&1'

DECLARE
    CURSOR DSBS_CUR IS
        SELECT DASHBOARD_ID FROM EMS_DASHBOARD WHERE DELETED<>1;
    DSB DSBS_CUR%ROWTYPE;
    CURSOR TILES_CUR(DSB_ID NUMBER) IS SELECT * FROM EMS_DASHBOARD_TILE WHERE DASHBOARD_ID=DSB_ID ORDER BY POSITION ASC;
    TILE TILES_CUR%ROWTYPE;
    TILEROW NUMBER;
    TILECOLUMN NUMBER;
    DEFAULT_COLUMNS NUMBER := 8;
BEGIN
    OPEN DSBS_CUR;
    LOOP
        FETCH DSBS_CUR INTO DSB;
        EXIT WHEN DSBS_CUR%NOTFOUND;
        --DBMS_OUTPUT.PUT_LINE('Updating dashboard with id=' || DSB.DASHBOARD_ID);
        TILEROW := 0;
        TILECOLUMN := 0;
        OPEN TILES_CUR(DSB.DASHBOARD_ID);
        LOOP
            FETCH TILES_CUR INTO TILE;
            EXIT WHEN TILES_CUR%NOTFOUND;
            IF TILECOLUMN + TILE.WIDTH * 2 > DEFAULT_COLUMNS
            THEN
                TILEROW := TILEROW + 1;
                TILECOLUMN := 0;
            END IF;
            UPDATE EMS_DASHBOARD_TILE SET TILE_ROW=TILEROW, TILE_COLUMN=TILECOLUMN, "TYPE"=0, WIDTH=TILE.WIDTH * 2, HEIGHT=1 WHERE TILE_ID=TILE.TILE_ID;
            --DBMS_OUTPUT.PUT_LINE('    Handling tile with id=' || TILE.TILE_ID || ' for dashboard with id=' || DSB.DASHBOARD_ID || '. Its width is ' || TILE.WIDTH || '(new width is ' || TILE.WIDTH * 2 || '). Its original position is ' || TILE.POSITION || '. Its new position is (' || TILEROW || ',' || TILECOLUMN || ')');
            TILECOLUMN := TILECOLUMN + TILE.WIDTH * 2;
        END LOOP;
        CLOSE TILES_CUR;
    END LOOP;
    CLOSE DSBS_CUR;
    
    COMMIT;
END;
/




