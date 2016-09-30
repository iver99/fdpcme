Rem
Rem    MODIFIED   (MM/DD/YY)
Rem    QIQIA      08/31/2016 - created
Rem

--update OOB Category, since OOB category can be referenced by both normal and OOB search, don't use delete from ...

--update OOB Folder, since OOB folder can be referenced by search and category, don't use delete from ...

@&EMSAAS_SQL_ROOT/1.11.0/emaas_dashboards_seed_data_ta.sql -1

COMMIT;

