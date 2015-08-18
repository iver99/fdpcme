DEFINE TENANT_ID = '&1'

REM --update OOB data

DELETE
FROM ems_dashboard_tile_params
WHERE tile_id<=10000
AND TENANT_ID ='&TENANT_ID';

DELETE
FROM ems_dashboard_tile
WHERE tile_id  <=10000
AND TENANT_ID   ='&TENANT_ID';

DELETE
FROM ems_dashboard_last_access
WHERE dashboard_id<=1000
AND TENANT_ID      ='&TENANT_ID';

DELETE
FROM ems_dashboard_favorite
WHERE dashboard_id<=1000
AND TENANT_ID      ='&TENANT_ID';

DELETE
FROM ems_dashboard
WHERE dashboard_id<=1000
AND TENANT_ID      ='&TENANT_ID';

COMMIT;

@../emaas_dashboards_seed_data.sql '&TENANT_ID';

DECLARE
    CURSOR DSBS_CUR IS
        SELECT DASHBOARD_ID FROM EMS_DASHBOARD WHERE DELETED<>1;
    DSB DSBS_CUR%ROWTYPE;
    CURSOR TILES_CUR(DSB_ID NUMBER) IS SELECT * FROM EMS_DASHBOARD_TILE WHERE DASHBOARD_ID=DSB_ID ORDER BY POSITION ASC;
    TILE TILES_CUR%ROWTYPE;
    TILE_ROW NUMBER;
    TILE_COLUMN NUMBER;
    DEFAULT_COLUMNS NUMBER := 8;
BEGIN
    OPEN DSBS_CUR;
    LOOP
        FETCH DSBS_CUR INTO DSB;
        EXIT WHEN DSBS_CUR%NOTFOUND;
        --DBMS_OUTPUT.PUT_LINE('Updating dashboard with id=' || DSB.DASHBOARD_ID);
        TILE_ROW := 0;
        TILE_COLUMN := 0;
        OPEN TILES_CUR(DSB.DASHBOARD_ID);
        LOOP
            FETCH TILES_CUR INTO TILE;
            EXIT WHEN TILES_CUR%NOTFOUND;
            IF TILE_COLUMN + TILE.WIDTH * 2 > DEFAULT_COLUMNS
            THEN
                TILE_ROW := TILE_ROW + 1;
                TILE_COLUMN := 0;
            END IF;
            UPDATE EMS_DASHBOARD_TILE SET "ROW"=TILE_ROW, "COLUMN"=TILE_COLUMN, "TYPE"=0, WIDTH=TILE.WIDTH * 2, HEIGHT=1 WHERE TILE_ID=TILE.TILE_ID;
            --DBMS_OUTPUT.PUT_LINE('    Handling tile with id=' || TILE.TILE_ID || ' for dashboard with id=' || DSB.DASHBOARD_ID || '. Its width is ' || TILE.WIDTH || '(new width is ' || TILE.WIDTH * 2 || '). Its original position is ' || TILE.POSITION || '. Its new position is (' || TILE_ROW || ',' || TILE_COLUMN || ')');
            TILE_COLUMN := TILE_COLUMN + TILE.WIDTH * 2;
        END LOOP;
        CLOSE TILES_CUR;
    END LOOP;
    CLOSE DSBS_CUR;
END;
/

COMMIT;

