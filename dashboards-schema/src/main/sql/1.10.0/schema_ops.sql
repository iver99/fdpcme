Rem ----------------------------------------------------------------
Rem Upgrade from 1.0.0 (this first version with multiple tenancy support) to latest
Rem Note:
Rem   Upgrade from any version below 1.0.0 is not supported any more.
Rem
Rem 07/22/2016	Rex	    created
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.10.0/upgrade_impl_ddl.sql

SET HEADING OFF
SET FEEDBACK OFF
SET LINESIZE 2000

SPOOL &EMSAAS_SQL_ROOT/1.10.0/upgrade_impl_dml_tmp.sql
SELECT DISTINCT '@&EMSAAS_SQL_ROOT/1.10.0/upgrade_impl_dml.sql ' || TENANT_ID  FROM EMS_ANALYTICS_FOLDERS ORDER BY '@&EMSAAS_SQL_ROOT/1.10.0/upgrade_impl_dml.sql ' || TENANT_ID ;
SPOOL OFF
SPOOL ON
SET HEADING ON
SET FEEDBACK ON

WHENEVER SQLERROR EXIT ROLLBACK

@&EMSAAS_SQL_ROOT/1.10.0/upgrade_impl_dml_tmp.sql

