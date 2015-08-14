DEFINE TENANT_ID = '&1'

REM --update OOB data

@&EMSAAS_SQL_ROOT/1.0.15/emaas_dashboards_seed_data.sql '&TENANT_ID'

COMMIT;

