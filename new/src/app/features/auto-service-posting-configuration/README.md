# Auto Service Posting Configuration — Unit Master 531 master/unit batch 4 (DQ-2026-08-12-013)

Route `autoServicePostingConfiguration`, module 775.

## Masked-error banner (NOT dev-empty)

The list endpoint GET /config/billing/list/{org}/{unit} 500s with the
correlation envelope **"Something went wrong. Reference: ERR-<uuid8>"** —
the ErrorCorrelationUtil masked envelope (same class as parameterMaster).

Root cause (verified 2026-08-12, information_schema + DAO source): the raw
SQL in AutoPostingConfigDaoImpl.java:92,134 selects
`autopostin0_.is_bed_charges_service` but
`public.t_auto_posting_services_cofiguration` (table name carries the legacy
"cofiguration" typo) has **no `is_bed_charges_service` column** — the second
phantom column found (parameterMaster's technique_id was the first). All
other query columns exist.

PROXY NOTE (4th cause class, DQ-2026-08-12-005): the dev harness initially
showed "Http failure during parsing" (HTTP 200 + SPA index.html) because
new/proxy.conf.json lacked a /config/** route — the backend was fine; the
proxy route was missing. Added /config/** (gitignored, working-tree only).
