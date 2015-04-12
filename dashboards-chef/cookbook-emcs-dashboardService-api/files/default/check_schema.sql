--
-- copyright (c) 2014, Oracle and/or its affiliates. All rights reserved.
--
-- Checks if the saved search schema is present on this db
--

-- Exit with error if expected table is not present
WHENEVER SQLERROR EXIT 1;

-- Check the presence of one of the last tables created by the schema creation scripts
SELECT 'DASHBOARD_SCHEMA_OK=' || TO_CHAR(0*COUNT(*)) FROM EMS_DASHBOARD;

EXIT 0;
