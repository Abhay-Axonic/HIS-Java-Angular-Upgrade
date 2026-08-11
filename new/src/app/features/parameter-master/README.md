# Parameter Master — Unit Master 531 LIS family batch 2 (DQ-2026-08-12-009)

Route `parameterMaster`, module 562.

## Masked-error banner (NOT dev-empty)

The list endpoint GET /LIS/unit/listParameterMaster/{org}/{unit}/{off}/{size}
500s with the correlation envelope **"Something went wrong. Reference:
ERR-<uuid8>"** — produced by `ErrorCorrelationUtil` (Global utility), a NEW
masked envelope distinct from the LIS fall-through's plain "Internal Server
Error" (DQ-2026-08-12-003).

Root cause (verified 2026-08-12, information_schema + entity source): the
`GET_PAGINATED_PARAMETER_MASTER_LIST` named query (ParameterMaster.java:107-140)
selects `parameterMaster.techniqueId` (declared :131, property :289) but
`lab.m_parameter_master` has **no `technique_id` column** — the phantom-column
class (screens 49/52). All other query columns exist.

Per the BANNER-TIER RULE the screen renders an AMBER-permanent banner naming
the column and the envelope. Backend fix NOT made (Rule 10 — behavioral
change; BR candidate: drop techniqueId from the query or add the column).
