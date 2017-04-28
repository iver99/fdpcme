DEFINE TENANT_ID = '&1'

REM --update OOB data

@&EMSAAS_SQL_ROOT/1.0.25/emaas_dashboards_seed_data_apm.sql '&TENANT_ID'
@&EMSAAS_SQL_ROOT/1.0.25/emaas_dashboards_seed_data_ita.sql '&TENANT_ID'
@&EMSAAS_SQL_ROOT/1.0.25/emaas_dashboards_seed_data_la.sql '&TENANT_ID'

COMMIT;

