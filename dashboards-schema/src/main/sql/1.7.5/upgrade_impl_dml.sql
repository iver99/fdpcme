Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    ADUAN      3/15/2016 - Created
Rem

DEFINE TENANT_ID = '&1'

REM --update OOB data

@&EMSAAS_SQL_ROOT/1.7.5/emaas_dashboards_seed_data.sql '&TENANT_ID'

COMMIT;

