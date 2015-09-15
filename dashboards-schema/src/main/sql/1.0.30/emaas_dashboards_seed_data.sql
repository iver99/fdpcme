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
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
    CURSOR DSBS_CUR IS
        SELECT DASHBOARD_ID FROM EMS_DASHBOARD WHERE DELETED=0;
    DSB DSBS_CUR%ROWTYPE;
    CURSOR TILES_CUR(DSB_ID NUMBER) IS SELECT * FROM EMS_DASHBOARD_TILE WHERE DASHBOARD_ID=DSB_ID ORDER BY POSITION ASC;
    TILE TILES_CUR%ROWTYPE;
    TILEROW NUMBER;
    TILECOLUMN NUMBER;
    DEFAULT_COLUMNS NUMBER := 8;
    V_MAX_WIDTH NUMBER;
BEGIN
    SELECT MAX(WIDTH) INTO V_MAX_WIDTH FROM EMS_DASHBOARD_TILE;
    IF (V_MAX_WIDTH=DEFAULT_COLUMNS) THEN
      DBMS_OUTPUT.PUT_LINE('Data in Schema object: EMS_DASHBOARD_TILE have been updated to support taller widget before, no need to update again'); 
      RETURN;
    END IF;
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
    DBMS_OUTPUT.PUT_LINE('Data in Schema object: EMS_DASHBOARD_TILE have been updated to support taller widget successfully');    
EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;  
    DBMS_OUTPUT.PUT_LINE('Failed to update data in Schema object: EMS_DASHBOARD_TILE to support taller widget due to '||SQLERRM); 
    RAISE;
END;
/

REM Update LA OOB and user created WIDGET METADATA in EMS_DASHBOARD_TILE table according to latest LA side change
DECLARE
  V_COUNT NUMBER;
BEGIN
  SELECT COUNT(1) INTO V_COUNT FROM EMS_DASHBOARD_TILE WHERE PROVIDER_NAME  ='LoganService'  AND WIDGET_KOC_NAME IN ('LA_WIDGET_BAR','LA_WIDGET_PIE','LA_WIDGET_HISTOGRAM');
  IF (V_COUNT=0) THEN
    DBMS_OUTPUT.PUT_LINE('LA WIDGET META DATA in Schema object: EMS_DASHBOARD_TILE has been updated before, no need to update again');  
  ELSE
    UPDATE EMS_DASHBOARD_TILE
    SET WIDGET_KOC_NAME  ='emcla-visualization',
      WIDGET_VIEWMODE    ='/js/viewmodel/search/widget/VisualizationWidget.js',
      WIDGET_TEMPLATE    ='/html/search/widgets/visualizationWidget.html'
    WHERE PROVIDER_NAME  ='LoganService'
    AND WIDGET_KOC_NAME IN ('LA_WIDGET_BAR','LA_WIDGET_PIE','LA_WIDGET_HISTOGRAM');
    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Update LA WIDGET META DATA in Schema object: EMS_DASHBOARD_TILE successfully');  
  END IF;
EXCEPTION
WHEN OTHERS THEN
  ROLLBACK;
  DBMS_OUTPUT.PUT_LINE('Failed to update LA WIDGET META DATA in Schema object: EMS_DASHBOARD_TILE due to '||SQLERRM);   
  RAISE;
END;
/




