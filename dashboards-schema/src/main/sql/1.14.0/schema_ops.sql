Rem ----------------------------------------------------------------
Rem 12/12/2016	REX	Created file
Rem 
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.14.0/upgrade_impl_ddl.sql

SET HEADING OFF
SET FEEDBACK OFF
SET LINESIZE 2000

@&EMSAAS_SQL_ROOT/1.14.0/upgrade_impl_dml.sql

