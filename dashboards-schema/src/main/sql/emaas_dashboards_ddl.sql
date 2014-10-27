Rem
Rem Copyright (c) 2014, Oracle and/or its affiliates. 
Rem All rights reserved.
Rem
Rem    DESCRIPTION
Rem      Creates all tables for LAaaS Dashboards in EM SaaS
Rem

Rem This table stores information of EM Analytics Dashboard
Rem
Rem KEYS
Rem
Rem The primary key is DASHBOARD_ID           
Rem
Rem COLUMNS
Rem
Rem DASHBOARD_ID - Id of the dashboard
Rem DASHBOARD_GUID  - Guid of the dashboard (required to support EM privilege model)
Rem NAME - name of the dashboard
Rem OWNER - owner username of the dashboard
Rem DESCRIPTION - description of the dashboard
Rem CREATION_DATE - creation date of the dashboard
Rem LAST_MODIFICATION_DATE - modification date of the dashboard
Rem LAST_MODIFIED_BY - latest user to modify the dashboard
Rem NAME_NLSID  -  name Nlsid to map to messages
Rem DESCRIPTION_NLSID - description Nlsid to map to messages
Rem IS_TEMPLATE - whether the dashboard is template
Rem IS_DELETABLE - whether the dashboard can be deleted or not by owner
prompt EMS_ANALYTICS_DASHBOARD
CREATE TABLE EMS_ANALYTICS_DASHBOARD
  (
    DASHBOARD_ID           NUMBER(*,0) NOT NULL,
    DASHBOARD_GUID         RAW(16) DEFAULT SYS_GUID(),
    NAME 		   VARCHAR2(64) NOT NULL,
    DESCRIPTION 	   VARCHAR2(256),
    CREATION_DATE  	   TIMESTAMP,
    OWNER 		   VARCHAR2(64),
    LAST_MODIFICATION_DATE TIMESTAMP,
    LAST_MODIFIED_BY 	   VARCHAR2(64),
    NAME_NLSID             VARCHAR2(256),
    DESCRIPTION_NLSID      VARCHAR2(256),
    TYPE                   NUMBER(*,0) DEFAULT(0) NOT NULL,
    IS_TEMPLATE            NUMBER(1,0) DEFAULT(0) NOT NULL,
    IS_DELETABLE           NUMBER(1,0) DEFAULT(1) NOT NULL,
    CONSTRAINT EMS_ANALYTICS_DASHBOARDS_PK PRIMARY KEY (DASHBOARD_ID) USING INDEX
  ) ;
  
prompt EMS_ANALYTICS_DASHBOARD_SEQ
CREATE SEQUENCE EMS_ANALYTICS_DASHBOARD_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 ORDER  NOCYCLE;

prompt EMS_ANALYTICS_DASHBOARD_TR
CREATE OR REPLACE TRIGGER  EMS_ANALYTICS_DASHBOARD_TR BEFORE INSERT ON EMS_ANALYTICS_DASHBOARD
FOR EACH ROW
BEGIN
  IF :new.DASHBOARD_ID IS NULL THEN
    SELECT EMS_ANALYTICS_DASHBOARD_SEQ.NEXTVAL INTO :new.DASHBOARD_ID FROM DUAL;
  END IF;
END;
/	

Rem This table stores properties information of EM Analytics Dashboard
Rem
Rem KEYS
Rem
Rem The primary key is DASHBOARD_ID, PROP_NAME
Rem
Rem COLUMNS
Rem
Rem DASHBOARD_ID - Id of the dashboard
Rem PROP_NAME - name of the dashboard property
Rem PROP_TYPE - type of the dashboard property
Rem PROP_VALUE_STR - string value of the dashboard property
prompt EMS_ANALYTICS_DASHBOARD_PARAMS
CREATE TABLE EMS_ANALYTICS_DASHBOARD_PARAMS
  (
    DASHBOARD_ID           NUMBER(*,0) NOT NULL,
    NAME 		   VARCHAR2(64) NOT NULL,
    IS_SYSTEM              NUMBER(1,0) DEFAULT(0) NOT NULL,
    PARAM_TYPE		   NUMBER(*,0) NOT NULL,
    PARAM_VALUE_STR        VARCHAR2(1024),
    CONSTRAINT EMS_ANALYTICS_DASHBOARDS_PP_PK PRIMARY KEY (DASHBOARD_ID,NAME) USING INDEX,
    CONSTRAINT EMS_ANALYTICS_DASHBOARDS_PP_FK FOREIGN KEY (DASHBOARD_ID) REFERENCES EMS_ANALYTICS_DASHBOARD (DASHBOARD_ID) 
  ) ;

Rem This table stores information of EM Analytics Dashboard Tile
Rem
Rem KEYS
Rem
Rem The primary key is TILE_ID          
Rem
Rem COLUMNS
Rem
Rem TILE_ID - Id of the dashboard tile
Rem DASHBOARD_ID - Id of the dashboard which contain this tile
Rem UNIT_ID - Id of savedSearch or dashboard custom unit
Rem UNIT_TYPE - Is a savedSearch or dashboard custom unit
Rem TITLE - title of the dashboard tile
Rem TITLE_NLSID  -  name Nlsid to map to messages
Rem POSITION - position of the dashboard tile
prompt EMS_ANALYTICS_TILE
CREATE TABLE EMS_ANALYTICS_TILE
  (
    TILE_ID                NUMBER(*,0) NOT NULL,
    DASHBOARD_ID           NUMBER(*,0) NOT NULL,
    UNIT_ID                NUMBER(*,0) NOT NULL,
    UNIT_TYPE              NUMBER(*,0) NOT NULL,
    TITLE                  VARCHAR2(64) NOT NULL,
    TITLE_NLSID            VARCHAR2(256),
    POSITION               VARCHAR2(64) NOT NULL,
    CONSTRAINT EMS_ANALYTICS_TILE_PK PRIMARY KEY (TILE_ID) USING INDEX
  ) ;

prompt EMS_ANALYTICS_TILE_SEQ
CREATE SEQUENCE EMS_ANALYTICS_TILE_SEQ  MINVALUE 1 MAXVALUE 999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 ORDER  NOCYCLE;

prompt EMS_ANALYTICS_TILE_TR	
CREATE OR REPLACE TRIGGER  EMS_ANALYTICS_TILE_TR BEFORE INSERT ON EMS_ANALYTICS_TILE
FOR EACH ROW
BEGIN
  IF :new.TILE_ID IS NULL THEN
    SELECT EMS_ANALYTICS_TILE_SEQ.NEXTVAL INTO :new.TILE_ID FROM DUAL;
  END IF;
END;
/

Rem This table stores properties information of EM Analytics Dashboard Tile
Rem
Rem KEYS
Rem
Rem The primary key is TILE_ID, PROP_NAME
Rem
Rem COLUMNS
Rem
Rem TILE_ID - Id of the dashboard tile
Rem PROP_NAME - name of the dashboard tile property
Rem PROP_TYPE - type of the dashboard tile property
Rem PROP_VALUE_STR - string value of the dashboard tile property
prompt EMS_ANALYTICS_TILE_PARAMS
CREATE TABLE EMS_ANALYTICS_TILE_PARAMS
  (
    TILE_ID                NUMBER(*,0) NOT NULL,
    NAME                   VARCHAR2(64) NOT NULL,
    IS_SYSTEM              NUMBER(1,0) DEFAULT(0) NOT NULL,
    PARAM_TYPE             NUMBER(*,0) NOT NULL,
    PARAM_VALUE_STR        VARCHAR2(1024),
    CONSTRAINT EMS_ANALYTICS_TILE_PARAMS_PK PRIMARY KEY (TILE_ID,NAME) USING INDEX,
    CONSTRAINT EMS_ANALYTICS_TILE_PARAMS_FK FOREIGN KEY (TILE_ID) REFERENCES EMS_ANALYTICS_TILE (TILE_ID)
  ) ;


Rem This table stores the last accessed records information for EM analytics objects like dashboards
Rem
Rem KEYS
Rem
Rem The primary keys are OBJECT_ID, ACCESSED_BY and OBJECT_TYPE
Rem
Rem COLUMNS
Rem
Rem OBJECT_ID - Id of objects accessed
Rem ACCESSED_BY - user that accesed the objects
Rem OBJECT_TYPE - type to indicate the objects accessed by user
Rem ACCESS_DATE - access date
Rem 
prompt EMS_ANALYTICS_LAST_ACCESS
CREATE TABLE EMS_ANALYTICS_LAST_ACCESS 
  (
    OBJECT_ID NUMBER(38,0) NOT NULL, 
    ACCESSED_BY VARCHAR2(64) NOT NULL, 
    OBJECT_TYPE NUMBER(38,0) NOT NULL, 
    ACCESS_DATE TIMESTAMP,
    CONSTRAINT EMS_ANALYTICS_LAST_ACCESS_PK PRIMARY KEY (OBJECT_ID,ACCESSED_BY,OBJECT_TYPE) USING INDEX
   );
