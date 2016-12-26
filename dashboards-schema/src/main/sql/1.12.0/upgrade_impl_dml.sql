REM --update OOB data

@&EMSAAS_SQL_ROOT/1.12.0/emaas_dashboards_seed_data_ta.sql -1
@&EMSAAS_SQL_ROOT/1.12.0/emaas_dashboards_seed_data.sql -1

COMMIT;


