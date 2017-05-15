Rem ----------------------------------------------------------------
Rem 05/04/2017	REX	Created file
Rem
Rem ----------------------------------------------------------------

@&EMSAAS_SQL_ROOT/1.19.0/cleanup_oob.sql
@&EMSAAS_SQL_ROOT/1.19.0/oob_tenant_onboarding.sql -11

COMMIT;

