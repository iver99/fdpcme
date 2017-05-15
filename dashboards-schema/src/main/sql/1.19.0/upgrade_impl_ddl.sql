Rem --DDL change during upgrade
Rem
Rem upgrade_impl_ddl.sql
Rem
Rem Copyright (c) 2013, 2014, 2015, 2016 Oracle and/or its affiliates.
Rem All rights reserved.
Rem
Rem    NAME
Rem      upgrade_impl_ddl.sql
Rem
Rem    DESCRIPTION
Rem      DDL change during upgrade
Rem
Rem    NOTES
Rem      None
Rem



BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_SET DROP CONSTRAINT EMS_DASHBOARD_SET_FK2';
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2443 THEN
         RAISE;
      END IF;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'ALTER TABLE EMS_DASHBOARD_USER_OPTIONS DROP CONSTRAINT EMS_DASHBOARD_USER_OPTIONS_FK1';
  EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2443 THEN
         RAISE;
      END IF;
END;
/
