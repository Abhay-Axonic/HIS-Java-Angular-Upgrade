-- EMPTY-MASTER SURVEY — run on the corrected DB (HIS-SME).
-- Returns every table whose name matches a master convention across ALL schemas,
-- with an approximate live row count (pg_stat_user_tables.n_live_tup — the
-- autovacuum-updated estimate; exact counts would need a COUNT(*) per table and
-- are slow on the big transaction tables, which this query excludes anyway by
-- the name filter). Schema names are returned explicitly — no schema or
-- plural-name assumption is baked in. Case-insensitive name match.
SELECT schemaname                 AS schema,
       relname                    AS table_name,
       n_live_tup                 AS approx_rows,
       CASE WHEN n_live_tup = 0 THEN 'EMPTY — seed candidate' ELSE 'has rows' END AS status
FROM pg_stat_user_tables
WHERE relname ~* '^(m_|.*_master$|.*_m$)'
   OR relname ILIKE 'm\_%'
ORDER BY n_live_tup ASC, schemaname ASC, relname ASC;

-- The same list with the exact counts, for just the empty candidates (small
-- set — exact COUNT(*) is cheap here). Replace <SCHEMA> per row as needed.
-- SELECT s.schemaname, s.relname, c.reltuples::bigint AS exact_rows
-- FROM pg_stat_user_tables s
-- JOIN pg_class c ON c.oid = s.relid
-- WHERE s.n_live_tup = 0 AND s.relname ~* '^(m_|.*_master$|.*_m$)'
-- ORDER BY s.schemaname, s.relname;
