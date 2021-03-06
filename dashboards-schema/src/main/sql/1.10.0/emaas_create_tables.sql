Rem
Rem
Rem emaas_create_tables.sql
Rem
Rem
Rem    NAME
Rem      emaas_create_tables.sql
Rem
Rem    DESCRIPTION
Rem      create the two tables ems_dashboards_favorite and ems_dashboards_last_access at the beginning of onboarding 
Rem		as the old versions need them;
Rem
Rem    NOTES
Rem      None
Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    pamela   8/15/2016  Created

SET FEEDBACK ON
SET SERVEROUTPUT ON

DECLARE
  v_count       NUMBER;
BEGIN
	--create EMS_DASHBOARD_FAVORITE 
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_FAVORITE';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE TABLE EMS_DASHBOARD_FAVORITE (
    USER_NAME          VARCHAR2(128) NOT NULL,
    DASHBOARD_ID       NUMBER(*,0) NOT NULL,
    CREATION_DATE      TIMESTAMP NOT NULL,
    TENANT_ID          NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_DASHBOARD_FAVORITE_PK PRIMARY KEY (USER_NAME, DASHBOARD_ID, TENANT_ID) USING INDEX,
    CONSTRAINT EMS_DASHBOARD_FAVORITE_FK1 FOREIGN KEY (DASHBOARD_ID, TENANT_ID) REFERENCES EMS_DASHBOARD (DASHBOARD_ID, TENANT_ID)
  )';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_FAVORITE exists');      
  END IF;
  
  --create EMS_DASHBOARD_LAST_ACCESS
  SELECT COUNT(*) INTO v_count FROM user_tab_columns WHERE table_name='EMS_DASHBOARD_LAST_ACCESS';
  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'CREATE TABLE EMS_DASHBOARD_LAST_ACCESS (
    DASHBOARD_ID   NUMBER(*,0) NOT NULL,
    ACCESSED_BY VARCHAR2(128) NOT NULL, 
    ACCESS_DATE TIMESTAMP NOT NULL,
    TENANT_ID     NUMBER(*,0) NOT NULL,
    CONSTRAINT EMS_DASHBOARD_LAST_ACCESS_PK PRIMARY KEY (DASHBOARD_ID, ACCESSED_BY,TENANT_ID) USING INDEX
   )';
  ELSE
    DBMS_OUTPUT.PUT_LINE('Schema object: EMS_DASHBOARD_LAST_ACCESS exists');      
  END IF;  
END;
/