Rem ----------------------------------------------------------------
Rem 3/05/2015	MIAYU	Created file
Rem Extract unique teant IDs from tables EMS_DASHBOARD & append that ID next to upgrade implementation file & run that file
Rem ----------------------------------------------------------------

@./upgrade_impl_ddl.sql

SET HEADING OFF
SET FEEDBACK OFF
SPOOL upgrade_impl_dml_tmp.sql
SELECT DISTINCT '@upgrade_impl_dml.sql ' || TENANT_ID  FROM EMS_DASHBOARD ORDER BY '@upgrade_impl_dml.sql ' || TENANT_ID ;
SPOOL OFF

SET HEADING ON
SET FEEDBACK ON
WHENEVER SQLERROR EXIT ROLLBACK

@./upgrade_impl_dml_tmp.sql

