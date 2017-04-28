DEFINE TENANT_ID = '&1'

REM --update OOB data

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_seed_data_ita.sql '&TENANT_ID'

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_seed_data_ocs.sql '&TENANT_ID'

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_remove_brownfield.sql '&TENANT_ID'

@&EMSAAS_SQL_ROOT/1.9.0/emaas_dashboards_seed_data_uigallery.sql '&TENANT_ID'

COMMIT;

