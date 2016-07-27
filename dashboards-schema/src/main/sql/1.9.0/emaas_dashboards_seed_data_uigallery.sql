Rem
Rem emaas_dashboards_seed_data_uigallery.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    NAME
Rem      emaas_dashboards_seed_data_uigallery.sql 
Rem
Rem    DESCRIPTION
Rem      Seed data for UI Gallery
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    MIAYU       07/21/16 - Add OOB dashborads for UI Gallery
Rem

DEFINE TENANT_ID ='&1'
SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  V_COUNT                                NUMBER;
  V_DASHBOARD_ID                         NUMBER(38,0);
  V_NAME                                 VARCHAR2(320 BYTE);
  V_TYPE                                 NUMBER(38,0);
  V_DESCRIPTION                          VARCHAR2(1280 BYTE);
  V_CREATION_DATE                        TIMESTAMP(6);
  V_LAST_MODIFICATION_DATE               TIMESTAMP(6);
  V_LAST_MODIFIED_BY                     VARCHAR2(128 BYTE);
  V_OWNER                                VARCHAR2(128 BYTE);
  V_IS_SYSTEM                            NUMBER(1,0);
  V_APPLICATION_TYPE                     NUMBER(4,0);
  V_ENABLE_TIME_RANGE                    NUMBER(2,0);
  V_ENABLE_REFRESH                       NUMBER(1,0);
  V_ENABLE_ENTITY_FILTER                 NUMBER(1,0);
  V_ENABLE_DESCRIPTION                   NUMBER(1,0);
  V_SHARE_PUBLIC                         NUMBER(1,0);
  V_DELETED                              NUMBER(38,0);
  V_TENANT_ID                            NUMBER(38,0) := '&TENANT_ID';
  V_SCREEN_SHOT                          CLOB;

  V_DASHBOARD_SET_ID                     NUMBER(38,0);
  V_SUB_DASHBOARD_ID                     NUMBER(38,0);
  V_SUB_DSB_POSITION                     NUMBER(38,0);

  V_TILE_ID                     NUMBER(38,0);
  V_TITLE                       VARCHAR2(64 BYTE);
  V_HEIGHT                      NUMBER(38,0);
  V_WIDTH                       NUMBER(38,0);
  V_IS_MAXIMIZED                NUMBER(1,0);
  V_POSITION                    NUMBER(38,0);
  V_WIDGET_UNIQUE_ID            VARCHAR2(64 BYTE);
  V_WIDGET_NAME                 VARCHAR2(64 BYTE);
  V_WIDGET_DESCRIPTION          VARCHAR2(256 BYTE);
  V_WIDGET_GROUP_NAME           VARCHAR2(64 BYTE);
  V_WIDGET_ICON                 VARCHAR2(1024 BYTE);
  V_WIDGET_HISTOGRAM            VARCHAR2(1024 BYTE);
  V_WIDGET_OWNER                VARCHAR2(128 BYTE);
  V_WIDGET_CREATION_TIME        VARCHAR2(32 BYTE);
  V_WIDGET_SOURCE               NUMBER(38,0);
  V_WIDGET_KOC_NAME             VARCHAR2(256 BYTE);
  V_WIDGET_VIEWMODE             VARCHAR2(1024 BYTE);
  V_WIDGET_TEMPLATE             VARCHAR2(1024 BYTE);
  V_PROVIDER_NAME               VARCHAR2(64 BYTE);
  V_PROVIDER_VERSION            VARCHAR2(64 BYTE);
  V_PROVIDER_ASSET_ROOT         VARCHAR2(64 BYTE);
  V_TILE_ROW                    NUMBER(38,0);
  V_TILE_COLUMN                 NUMBER(38,0);
  V_TILE_TYPE                   NUMBER(38,0);
  V_TILE_SUPPORT_TIMECONTROL    NUMBER(1,0);
  V_TILE_LINKED_DASHBOARD       NUMBER(38,0);

  V_SUB_DASHBOARD_ID_1      NUMBER(38,0);
  V_SUB_DASHBOARD_ID_1_POS   NUMBER(38,0);
  V_SUB_DASHBOARD_ID_2       NUMBER(38,0);
  V_SUB_DASHBOARD_ID_2_POS   NUMBER(38,0);      
  V_SUB_DASHBOARD_ID_3       NUMBER(38,0);
  V_SUB_DASHBOARD_ID_3_POS   NUMBER(38,0);  
   V_PARAM_NAME EMS_DASHBOARD_TILE_PARAMS.PARAM_NAME%TYPE;
   V_PARAM_TYPE EMS_DASHBOARD_TILE_PARAMS.PARAM_TYPE%TYPE;
   V_PARAM_VALUE_STR EMS_DASHBOARD_TILE_PARAMS.PARAM_VALUE_STR%TYPE;
   V_PARAM_VALUE_NUM EMS_DASHBOARD_TILE_PARAMS.PARAM_VALUE_NUM%TYPE;
   V_PARAM_VALUE_TIMESTAMP EMS_DASHBOARD_TILE_PARAMS.PARAM_VALUE_TIMESTAMP%TYPE;
   
  CONST_NULL                       CONSTANT    CLOB:=null;
  CONST_ORACLE                     CONSTANT    VARCHAR2(128 BYTE):='Oracle';
  CONST_IS_SYSTEM                  CONSTANT    NUMBER:=1;
  CONST_OOB_DSB_AUTO_REFRESH       CONSTANT    EMS_DASHBOARD_USER_OPTIONS.AUTO_REFRESH_INTERVAL%TYPE:=0;
  CONST_OOB_DSB_ACCESS_DATE        CONSTANT    EMS_DASHBOARD_USER_OPTIONS.ACCESS_DATE%TYPE:=SYS_EXTRACT_UTC(SYSTIMESTAMP);
  CONST_OOB_DSB_IS_FAVORITE        CONSTANT    EMS_DASHBOARD_USER_OPTIONS.IS_FAVORITE%TYPE:=0;
  
  CONST_DASHBOARD_ID_UI_GALLERY    CONSTANT    EMS_DASHBOARD.DASHBOARD_ID%type:=24; 
  
  CONST_DASHBOARD_ID_TIMESERIES    CONSTANT    EMS_DASHBOARD.DASHBOARD_ID%type:=25; 
  CONST_TIMESERIES_TILE_ID_1       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=39; 
  CONST_TIMESERIES_TILE_ID_2       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=40; 
  CONST_TIMESERIES_TILE_ID_3       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=41; 
  CONST_TIMESERIES_TILE_ID_4       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=42; 
  CONST_TIMESERIES_TILE_ID_5       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=43; 
  CONST_TIMESERIES_TILE_ID_6       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=44; 
  CONST_TIMESERIES_TILE_ID_7       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=45; 
  CONST_TIMESERIES_TILE_ID_8       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=46; 
  CONST_TIMESERIES_TILE_ID_9       CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=47; 
  CONST_TIMESERIES_TILE_ID_10      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=48; 
  CONST_TIMESERIES_TILE_ID_11      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=49; 
  CONST_TIMESERIES_TILE_ID_12      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=50; 
  
  CONST_DASHBOARD_ID_CATEGORICAL   CONSTANT    EMS_DASHBOARD.DASHBOARD_ID%type:=26; 
  CONST_CATEGORICAL_TILE_ID_1      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=51; 
  CONST_CATEGORICAL_TILE_ID_2      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=52; 
  CONST_CATEGORICAL_TILE_ID_3      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=53; 
  CONST_CATEGORICAL_TILE_ID_4      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=54; 
  CONST_CATEGORICAL_TILE_ID_5      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=55; 
  CONST_CATEGORICAL_TILE_ID_6      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=56; 
  CONST_CATEGORICAL_TILE_ID_7      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=57; 
  CONST_CATEGORICAL_TILE_ID_8      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=58; 
  CONST_CATEGORICAL_TILE_ID_9      CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=59; 
  CONST_CATEGORICAL_TILE_ID_10     CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=60; 
  
  CONST_DASHBOARD_ID_OTHERS        CONSTANT    EMS_DASHBOARD.DASHBOARD_ID%type:=27;
  CONST_OTHERS_TILE_ID_1           CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=61;   
  CONST_OTHERS_TILE_ID_2           CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=62; 
  CONST_OTHERS_TILE_ID_3           CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=63; 
  CONST_OTHERS_TILE_ID_4           CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=64; 
  CONST_OTHERS_TILE_ID_5           CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=65; 
  CONST_OTHERS_TILE_ID_6           CONSTANT    EMS_DASHBOARD_TILE.TILE_ID%type:=66;   
BEGIN
  SELECT COUNT(DASHBOARD_ID) INTO V_COUNT FROM EMS_DASHBOARD WHERE DASHBOARD_ID=CONST_DASHBOARD_ID_UI_GALLERY AND TENANT_ID=V_TENANT_ID;
  IF (V_COUNT<1) THEN
      V_DASHBOARD_ID              := CONST_DASHBOARD_ID_UI_GALLERY;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
      V_NAME                      := 'UI Gallery';                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
      V_TYPE                      := 2;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_DESCRIPTION               := null;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
      V_CREATION_DATE             := SYS_EXTRACT_UTC(SYSTIMESTAMP);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
      V_LAST_MODIFICATION_DATE    := SYS_EXTRACT_UTC(SYSTIMESTAMP);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
      V_LAST_MODIFIED_BY          := CONST_ORACLE;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
      V_OWNER                     := CONST_ORACLE;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  
      V_IS_SYSTEM                 := 1;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_APPLICATION_TYPE          := 2;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_ENABLE_TIME_RANGE         := 0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_ENABLE_REFRESH            := 1;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_ENABLE_ENTITY_FILTER      := 0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         

      V_ENABLE_DESCRIPTION        := 0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_SHARE_PUBLIC              := 0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_DELETED                   := 0;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      V_SCREEN_SHOT               :=
'data:image/png;base64,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';
      
      INSERT INTO EMS_DASHBOARD (DASHBOARD_ID,NAME,TYPE,DESCRIPTION,CREATION_DATE,LAST_MODIFICATION_DATE,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
                                 LAST_MODIFIED_BY,OWNER,IS_SYSTEM,APPLICATION_TYPE,ENABLE_TIME_RANGE,SCREEN_SHOT,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               
                                 DELETED,TENANT_ID,ENABLE_REFRESH,SHARE_PUBLIC,ENABLE_ENTITY_FILTER,ENABLE_DESCRIPTION)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
      VALUES(V_DASHBOARD_ID,V_NAME,V_TYPE,V_DESCRIPTION,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
             V_OWNER,V_IS_SYSTEM,V_APPLICATION_TYPE,V_ENABLE_TIME_RANGE,V_SCREEN_SHOT,V_DELETED,V_TENANT_ID,
             V_ENABLE_REFRESH,V_SHARE_PUBLIC,V_ENABLE_ENTITY_FILTER,V_ENABLE_DESCRIPTION); 

      Insert into EMS_DASHBOARD_USER_OPTIONS (USER_NAME,TENANT_ID,DASHBOARD_ID,AUTO_REFRESH_INTERVAL,ACCESS_DATE,IS_FAVORITE,EXTENDED_OPTIONS)
      values (CONST_ORACLE,V_TENANT_ID,V_DASHBOARD_ID,CONST_OOB_DSB_AUTO_REFRESH,CONST_OOB_DSB_ACCESS_DATE,CONST_OOB_DSB_IS_FAVORITE,CONST_NULL);

      V_DASHBOARD_ID              := CONST_DASHBOARD_ID_TIMESERIES;
      V_NAME                      := 'Timeseries';
      V_TYPE                      := 0;
      V_DESCRIPTION               := null;
      V_CREATION_DATE             := SYS_EXTRACT_UTC(SYSTIMESTAMP);
      V_LAST_MODIFICATION_DATE    := SYS_EXTRACT_UTC(SYSTIMESTAMP);
      V_LAST_MODIFIED_BY          := CONST_ORACLE;
      V_OWNER                     := CONST_ORACLE;
      V_IS_SYSTEM                 := 1;
      V_APPLICATION_TYPE          := 2;
      V_ENABLE_TIME_RANGE         := 1;
      V_ENABLE_REFRESH            := 1;
      V_ENABLE_ENTITY_FILTER      := 0;
      V_ENABLE_DESCRIPTION        := 0;
      V_SHARE_PUBLIC              := 0;
      V_DELETED                   := 0;
      V_SCREEN_SHOT               :=
'data:image/png;base64,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';

      INSERT INTO EMS_DASHBOARD (DASHBOARD_ID,NAME,TYPE,DESCRIPTION,CREATION_DATE,LAST_MODIFICATION_DATE,
                                 LAST_MODIFIED_BY,OWNER,IS_SYSTEM,APPLICATION_TYPE,ENABLE_TIME_RANGE,SCREEN_SHOT,
                                 DELETED,TENANT_ID,ENABLE_REFRESH,SHARE_PUBLIC,ENABLE_ENTITY_FILTER,ENABLE_DESCRIPTION) 
      VALUES(V_DASHBOARD_ID,V_NAME,V_TYPE,V_DESCRIPTION,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,
             V_OWNER,V_IS_SYSTEM,V_APPLICATION_TYPE,V_ENABLE_TIME_RANGE,V_SCREEN_SHOT,V_DELETED,V_TENANT_ID,
             V_ENABLE_REFRESH,V_SHARE_PUBLIC,V_ENABLE_ENTITY_FILTER,V_ENABLE_DESCRIPTION);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_1;
      V_TITLE                       := 'Area Chart';
      V_HEIGHT                      := 1;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3055;
      V_WIDGET_NAME                 := 'Area Chart';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_2;
      V_TITLE                       := 'Line Chart';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3058;
      V_WIDGET_NAME                 := 'Line Chart';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 6;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_3;
      V_TITLE                       := 'Line Chart with Color';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3060;
      V_WIDGET_NAME                 := 'Line Chart with Color';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 4;
      V_TILE_COLUMN                 := 6;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_4;
      V_TITLE                       := 'Line Chart with Shared Y-axis';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3059;
      V_WIDGET_NAME                 := 'Line Chart with Shared Y-axis';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 2;
      V_TILE_COLUMN                 := 6;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_5;
      V_TITLE                       := 'Line Chart with Trend and Forecasting';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3062;
      V_WIDGET_NAME                 := 'Line Chart with Trend and Forecasting';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 6;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_6;
      V_TITLE                       := 'Stacked Area Chart';
      V_HEIGHT                      := 3;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3056;
      V_WIDGET_NAME                 := 'Stacked Area Chart';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 1;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_7;
      V_TITLE                       := 'Stacked Area Chart with Group By';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3057;
      V_WIDGET_NAME                 := 'Stacked Area Chart with Group By';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 4;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_8;
      V_TITLE                       := 'Analytics Line with Trend and Forecasting';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3064;
      V_WIDGET_NAME                 := 'Analytics Line with Trend and Forecasting';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 10;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_9;
      V_TITLE                       := 'Analytics Line';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3063;
      V_WIDGET_NAME                 := 'Analytics Line';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 8;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_10;
      V_TITLE                       := 'Line Chart with Reference Line';
      V_HEIGHT                      := 1;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3066;
      V_WIDGET_NAME                 := 'Line Chart with Reference Line';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 11;
      V_TILE_COLUMN                 := 6;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_11;
      V_TITLE                       := 'Stacked Line Chart with Group By';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3061;
      V_WIDGET_NAME                 := 'Stacked Line Chart with Group By';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 6;
      V_TILE_COLUMN                 := 6;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_TIMESERIES_TILE_ID_12;
      V_TITLE                       := 'Stacked Line with Color and Group by';
      V_HEIGHT                      := 3;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3065;
      V_WIDGET_NAME                 := 'Stacked Line with Color and Group by';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 8;
      V_TILE_COLUMN                 := 6;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);


      V_DASHBOARD_ID              := CONST_DASHBOARD_ID_CATEGORICAL;
      V_NAME                      := 'Categorical';
      V_TYPE                      := 0;
      V_DESCRIPTION               := null;
      V_CREATION_DATE             := SYS_EXTRACT_UTC(SYSTIMESTAMP);
      V_LAST_MODIFICATION_DATE    := SYS_EXTRACT_UTC(SYSTIMESTAMP);
      V_LAST_MODIFIED_BY          := CONST_ORACLE;
      V_OWNER                     := CONST_ORACLE;
      V_IS_SYSTEM                 := 1;
      V_APPLICATION_TYPE          := 2;
      V_ENABLE_TIME_RANGE         := 1;
      V_ENABLE_REFRESH            := 1;
      V_ENABLE_ENTITY_FILTER      := 0;
      V_ENABLE_DESCRIPTION        := 0;
      V_SHARE_PUBLIC              := 0;
      V_DELETED                   := 0;
      V_SCREEN_SHOT               :=
'data:image/png;base64,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';
      
      INSERT INTO EMS_DASHBOARD (DASHBOARD_ID,NAME,TYPE,DESCRIPTION,CREATION_DATE,LAST_MODIFICATION_DATE,
                                 LAST_MODIFIED_BY,OWNER,IS_SYSTEM,APPLICATION_TYPE,ENABLE_TIME_RANGE,SCREEN_SHOT,
                                 DELETED,TENANT_ID,ENABLE_REFRESH,SHARE_PUBLIC,ENABLE_ENTITY_FILTER,ENABLE_DESCRIPTION) 
      VALUES(V_DASHBOARD_ID,V_NAME,V_TYPE,V_DESCRIPTION,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,
             V_OWNER,V_IS_SYSTEM,V_APPLICATION_TYPE,V_ENABLE_TIME_RANGE,V_SCREEN_SHOT,V_DELETED,V_TENANT_ID,
             V_ENABLE_REFRESH,V_SHARE_PUBLIC,V_ENABLE_ENTITY_FILTER,V_ENABLE_DESCRIPTION);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_1;
      V_TITLE                       := 'Analytics Line - Categorical';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 5;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3047;
      V_WIDGET_NAME                 := 'Analytics Line - Categorical';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 3;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_2;
      V_TITLE                       := 'Bar Chart';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 5;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3050;
      V_WIDGET_NAME                 := 'Bar Chart';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 2;
      V_TILE_COLUMN                 := 4;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_3;
      V_TITLE                       := 'Bar Chart with Color and Group by option';
      V_HEIGHT                      := 3;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3052;
      V_WIDGET_NAME                 := 'Bar Chart with Color and Group by option';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 6;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_4;
      V_TITLE                       := 'Bar Chart with Color option';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 7;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3053;
      V_WIDGET_NAME                 := 'Bar Chart with Color option';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 4;
      V_TILE_COLUMN                 := 5;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_5;
      V_TITLE                       := 'Donut';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 3;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3046;
      V_WIDGET_NAME                 := 'Donut';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_6;
      V_TITLE                       := 'Histogram';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 4;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3049;
      V_WIDGET_NAME                 := 'Histogram';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 8;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_7;
      V_TITLE                       := 'Stacked Bar Chart';
      V_HEIGHT                      := 3;
      V_WIDTH                       := 6;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3051;
      V_WIDGET_NAME                 := 'Stacked Bar Chart';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 6;
      V_TILE_COLUMN                 := 6;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_8;
      V_TITLE                       := 'Treemap';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 4;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3048;
      V_WIDGET_NAME                 := 'Treemap';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 2;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_9;
      V_TITLE                       := 'Bar Chart with Top N';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 5;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3054;
      V_WIDGET_NAME                 := 'Bar Chart with Top N';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 4;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_CATEGORICAL_TILE_ID_10;
      V_TITLE                       := 'Pareto Chart';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 3;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3067;
      V_WIDGET_NAME                 := 'Pareto Chart';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 2;
      V_TILE_COLUMN                 := 9;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);

      V_DASHBOARD_ID              := CONST_DASHBOARD_ID_OTHERS;
      V_NAME                      := 'Others';
      V_TYPE                      := 0;
      V_DESCRIPTION               := null;
      V_CREATION_DATE             := SYS_EXTRACT_UTC(SYSTIMESTAMP);
      V_LAST_MODIFICATION_DATE    := SYS_EXTRACT_UTC(SYSTIMESTAMP);
      V_LAST_MODIFIED_BY          := CONST_ORACLE;
      V_OWNER                     := CONST_ORACLE;
      V_IS_SYSTEM                 := 1;
      V_APPLICATION_TYPE          := 2;
      V_ENABLE_TIME_RANGE         := 1;
      V_ENABLE_REFRESH            := 1;
      V_ENABLE_ENTITY_FILTER      := 0;
      V_ENABLE_DESCRIPTION        := 0;
      V_SHARE_PUBLIC              := 0;
      V_DELETED                   := 0;
      V_SCREEN_SHOT               :=
'data:image/png;base64,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';
      
      INSERT INTO EMS_DASHBOARD (DASHBOARD_ID,NAME,TYPE,DESCRIPTION,CREATION_DATE,LAST_MODIFICATION_DATE,
                                 LAST_MODIFIED_BY,OWNER,IS_SYSTEM,APPLICATION_TYPE,ENABLE_TIME_RANGE,SCREEN_SHOT,
                                 DELETED,TENANT_ID,ENABLE_REFRESH,SHARE_PUBLIC,ENABLE_ENTITY_FILTER,ENABLE_DESCRIPTION) 
      VALUES(V_DASHBOARD_ID,V_NAME,V_TYPE,V_DESCRIPTION,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,
             V_OWNER,V_IS_SYSTEM,V_APPLICATION_TYPE,V_ENABLE_TIME_RANGE,V_SCREEN_SHOT,V_DELETED,V_TENANT_ID,
             V_ENABLE_REFRESH,V_SHARE_PUBLIC,V_ENABLE_ENTITY_FILTER,V_ENABLE_DESCRIPTION);
      V_TILE_ID                     := CONST_OTHERS_TILE_ID_1;
      V_TITLE                       := 'Circular Gauge';
      V_HEIGHT                      := 1;
      V_WIDTH                       := 2;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3043;
      V_WIDGET_NAME                 := 'Circular Gauge';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 3;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_PARAM_NAME                  := 'DF_HIDE_TITLE';
      V_PARAM_TYPE                  := 1;
      V_PARAM_VALUE_STR             := 'true';
      V_PARAM_VALUE_NUM             := null;
      V_PARAM_VALUE_TIMESTAMP       := null;
      Insert into EMS_DASHBOARD_TILE_PARAMS (TILE_ID,PARAM_NAME,TENANT_ID,IS_SYSTEM,PARAM_TYPE,PARAM_VALUE_STR,PARAM_VALUE_NUM,PARAM_VALUE_TIMESTAMP) 
      values (V_TILE_ID,V_PARAM_NAME,V_TENANT_ID,CONST_IS_SYSTEM,V_PARAM_TYPE,V_PARAM_VALUE_STR,V_PARAM_VALUE_NUM,V_PARAM_VALUE_TIMESTAMP);


      V_TILE_ID                     := CONST_OTHERS_TILE_ID_2;
      V_TITLE                       := 'Horizontal Gauge';
      V_HEIGHT                      := 1;
      V_WIDTH                       := 3;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3041;
      V_WIDGET_NAME                 := 'Horizontal Gauge';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 1;
      V_TILE_COLUMN                 := 2;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_PARAM_NAME                  := 'DF_HIDE_TITLE';
      V_PARAM_TYPE                  := 1;
      V_PARAM_VALUE_STR             := 'true';
      V_PARAM_VALUE_NUM             := null;
      V_PARAM_VALUE_TIMESTAMP       := null;
      Insert into EMS_DASHBOARD_TILE_PARAMS (TILE_ID,PARAM_NAME,TENANT_ID,IS_SYSTEM,PARAM_TYPE,PARAM_VALUE_STR,PARAM_VALUE_NUM,PARAM_VALUE_TIMESTAMP) 
      values (V_TILE_ID,V_PARAM_NAME,V_TENANT_ID,CONST_IS_SYSTEM,V_PARAM_TYPE,V_PARAM_VALUE_STR,V_PARAM_VALUE_NUM,V_PARAM_VALUE_TIMESTAMP);


      V_TILE_ID                     := CONST_OTHERS_TILE_ID_3;
      V_TITLE                       := 'Label';
      V_HEIGHT                      := 1;
      V_WIDTH                       := 3;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3040;
      V_WIDGET_NAME                 := 'Label';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);

      V_PARAM_NAME                  := 'DF_HIDE_TITLE';
      V_PARAM_TYPE                  := 1;
      V_PARAM_VALUE_STR             := 'true';
      V_PARAM_VALUE_NUM             := null;
      V_PARAM_VALUE_TIMESTAMP       := null;
      Insert into EMS_DASHBOARD_TILE_PARAMS (TILE_ID,PARAM_NAME,TENANT_ID,IS_SYSTEM,PARAM_TYPE,PARAM_VALUE_STR,PARAM_VALUE_NUM,PARAM_VALUE_TIMESTAMP) 
      values (V_TILE_ID,V_PARAM_NAME,V_TENANT_ID,CONST_IS_SYSTEM,V_PARAM_TYPE,V_PARAM_VALUE_STR,V_PARAM_VALUE_NUM,V_PARAM_VALUE_TIMESTAMP);


      V_TILE_ID                     := CONST_OTHERS_TILE_ID_4;
      V_TITLE                       := 'Vertical Gauge';
      V_HEIGHT                      := 1;
      V_WIDTH                       := 2;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3042;
      V_WIDGET_NAME                 := 'Vertical Gauge';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 1;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);

      V_PARAM_NAME                  := 'DF_HIDE_TITLE';
      V_PARAM_TYPE                  := 1;
      V_PARAM_VALUE_STR             := 'true';
      V_PARAM_VALUE_NUM             := null;
      V_PARAM_VALUE_TIMESTAMP       := null;
      Insert into EMS_DASHBOARD_TILE_PARAMS (TILE_ID,PARAM_NAME,TENANT_ID,IS_SYSTEM,PARAM_TYPE,PARAM_VALUE_STR,PARAM_VALUE_NUM,PARAM_VALUE_TIMESTAMP) 
      values (V_TILE_ID,V_PARAM_NAME,V_TENANT_ID,CONST_IS_SYSTEM,V_PARAM_TYPE,V_PARAM_VALUE_STR,V_PARAM_VALUE_NUM,V_PARAM_VALUE_TIMESTAMP);

      V_TILE_ID                     := CONST_OTHERS_TILE_ID_5;
      V_TITLE                       := 'Scatter Chart';
      V_HEIGHT                      := 2;
      V_WIDTH                       := 5;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3044;
      V_WIDGET_NAME                 := 'Scatter Chart';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 2;
      V_TILE_COLUMN                 := 0;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);
      V_TILE_ID                     := CONST_OTHERS_TILE_ID_6;
      V_TITLE                       := 'Table';
      V_HEIGHT                      := 4;
      V_WIDTH                       := 7;
      V_IS_MAXIMIZED                := 0;
      V_POSITION                    := 0;
      V_WIDGET_UNIQUE_ID            := 3045;
      V_WIDGET_NAME                 := 'Table';
      V_WIDGET_DESCRIPTION          := null;
      V_WIDGET_GROUP_NAME           := 'Data Explorer';
      V_WIDGET_ICON                 := '/../images/func_horibargraph_24_ena.png;';
      V_WIDGET_HISTOGRAM            := null;
      V_WIDGET_OWNER                := 'ORACLE';
      V_WIDGET_CREATION_TIME        := to_char(SYS_EXTRACT_UTC(SYSTIMESTAMP),'YYYY-MM-DD"T"HH24:MI:SS.ff3"Z"');
      V_WIDGET_SOURCE               := 1;
      V_WIDGET_KOC_NAME             := 'emcta-visualization';
      V_WIDGET_VIEWMODE             := '/widget/visualizationWidget/js/VisualizationWidget.js';
      V_WIDGET_TEMPLATE             := '/widget/visualizationWidget/visualizationWidget.html';
      V_PROVIDER_NAME               := 'TargetAnalytics';
      V_PROVIDER_VERSION            := '1.1';
      V_PROVIDER_ASSET_ROOT         := 'assetRoot';
      V_TILE_ROW                    := 0;
      V_TILE_COLUMN                 := 5;
      V_TILE_TYPE                   := 0;
      V_TILE_SUPPORT_TIMECONTROL    := 1;
      V_TILE_LINKED_DASHBOARD       := null;
      INSERT INTO EMS_DASHBOARD_TILE(TILE_ID,DASHBOARD_ID,CREATION_DATE,LAST_MODIFICATION_DATE,LAST_MODIFIED_BY,OWNER,TITLE,HEIGHT,WIDTH,IS_MAXIMIZED,POSITION,TENANT_ID,
                                     WIDGET_UNIQUE_ID,WIDGET_NAME,WIDGET_DESCRIPTION,WIDGET_GROUP_NAME,WIDGET_ICON,WIDGET_HISTOGRAM,WIDGET_OWNER,WIDGET_CREATION_TIME,
                                     WIDGET_SOURCE,WIDGET_KOC_NAME,WIDGET_VIEWMODE,WIDGET_TEMPLATE,PROVIDER_NAME,PROVIDER_VERSION,PROVIDER_ASSET_ROOT,TILE_ROW,TILE_COLUMN,
                                     TYPE,WIDGET_SUPPORT_TIME_CONTROL,WIDGET_LINKED_DASHBOARD)
      VALUES(V_TILE_ID,V_DASHBOARD_ID,V_CREATION_DATE,V_LAST_MODIFICATION_DATE,V_LAST_MODIFIED_BY,V_OWNER,V_TITLE,V_HEIGHT,V_WIDTH,V_IS_MAXIMIZED,V_POSITION,V_TENANT_ID,
             V_WIDGET_UNIQUE_ID,V_WIDGET_NAME,V_WIDGET_DESCRIPTION,V_WIDGET_GROUP_NAME,V_WIDGET_ICON,V_WIDGET_HISTOGRAM,V_WIDGET_OWNER,V_WIDGET_CREATION_TIME,V_WIDGET_SOURCE,V_WIDGET_KOC_NAME,
             V_WIDGET_VIEWMODE,V_WIDGET_TEMPLATE,V_PROVIDER_NAME,V_PROVIDER_VERSION,V_PROVIDER_ASSET_ROOT,V_TILE_ROW,V_TILE_COLUMN,V_TILE_TYPE,V_TILE_SUPPORT_TIMECONTROL,V_TILE_LINKED_DASHBOARD);


      V_DASHBOARD_SET_ID       := CONST_DASHBOARD_ID_UI_GALLERY;
      V_SUB_DASHBOARD_ID_1       := CONST_DASHBOARD_ID_TIMESERIES;
      V_SUB_DASHBOARD_ID_1_POS       := 0;
      V_SUB_DASHBOARD_ID_2       := CONST_DASHBOARD_ID_CATEGORICAL;
      V_SUB_DASHBOARD_ID_2_POS       := 1;      
      V_SUB_DASHBOARD_ID_3       := CONST_DASHBOARD_ID_OTHERS;
      V_SUB_DASHBOARD_ID_3_POS       := 2;      
      INSERT INTO EMS_DASHBOARD_SET(DASHBOARD_SET_ID,TENANT_ID,SUB_DASHBOARD_ID,POSITION) VALUES(V_DASHBOARD_SET_ID,V_TENANT_ID,V_SUB_DASHBOARD_ID_1,V_SUB_DASHBOARD_ID_1_POS);
      INSERT INTO EMS_DASHBOARD_SET(DASHBOARD_SET_ID,TENANT_ID,SUB_DASHBOARD_ID,POSITION) VALUES(V_DASHBOARD_SET_ID,V_TENANT_ID,V_SUB_DASHBOARD_ID_2,V_SUB_DASHBOARD_ID_2_POS);
      INSERT INTO EMS_DASHBOARD_SET(DASHBOARD_SET_ID,TENANT_ID,SUB_DASHBOARD_ID,POSITION) VALUES(V_DASHBOARD_SET_ID,V_TENANT_ID,V_SUB_DASHBOARD_ID_3,V_SUB_DASHBOARD_ID_3_POS);      

      
      COMMIT;
  DBMS_OUTPUT.PUT_LINE('Out of box dashboards for UI Gallery have been created successfully!');
ELSE
  DBMS_OUTPUT.PUT_LINE('Out of box dashboards for UI Gallery had been created before, no need to create again.');
END IF;

EXCEPTION
  WHEN OTHERS THEN
    ROLLBACK;
    DBMS_OUTPUT.PUT_LINE('Failed to create out of box dashboards for UI Gallery due to error '||SQLERRM);
    RAISE;
END;
/