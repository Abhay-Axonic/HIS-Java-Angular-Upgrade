# frequency-master — parity rebuild of the legacy Frequency Master screen

Legacy source:
- template: `views/master/GlobalMaster/AssetManagenent/Master/frequency-master.html`
- controller: `frequencyMasterController.js` (loaded via index.html:1009)
- menu node: 478 'Frequency Master', parent 476 Clinical Workstation
  -> 369 Global Master, actionLink `#/frequencyMaster`

## THE DANGLE SHAPE (menu'd-unrouted — the drug-recall class, THIRD instance)

Same class as 479 (formulationTypeROAMaster) and 477 (routeAdministration):
the node's actionLink points at `#/frequencyMaster`, but NO
`.state('frequencyMaster')` is registered anywhere in app.js (grep = 0).
The controller IS loaded (index.html:1009), the template exists, and the
backend is LIVE — so the legacy menu navigates to a non-existent state
(NG04002). Per the amended reachability rule (RULES.md four-way
classification, owner directive 2026-08-10), a DANGLE screen with a real
backend is BUILDABLE: the rebuild RESTORES a menu-advertised screen the
legacy cannot open.

## Backend (Pharmacy module) — verified BY CALLING (cached token, 2026-08-10)

- list:  GET /api/globals/frequency/{orgId} -> 200 list len=1 (REAL dev row)
- byId:  GET /api/globals/frequency/{orgId}/{id} -> HTTP 500 HIS-1001
  (root cause below)
- save:  POST /api/globals/frequency/save (MAPPED, not fired — no-seed)
- update: PATCH /api/globals/frequency/{orgId}/{id} (MAPPED, not fired)
- table: opd.m_frequency_master exists (the list's real row)

THE REAL ROW (dev): id/code/description/noOfAdministrationTime/status/items
shape; the list feed is LIVE and populated.

## FINDING (found by calling, not by grep) — phantom table on the byId path

byId 500s with HIS-1001 "Something went wrong. Reference: ERR-eff11174".
Root cause (catalina.out stack): FrequencyDao.getTranslatedFrequencyListForUpdate
(FrequencyDao.java:255) runs
  select ... from public.t_translated_frequency tran
  WHERE tran.status='A' AND tran.frequency_id=:frequencyId AND ...
and public.t_translated_frequency DOES NOT EXIST in the database (psql:
relation does not exist; information_schema has no translated_frequency
table in any schema, though the entity TranslatedFrequencyMaster maps
@Table("t_translated_frequency", schema="public")). The same phantom is
referenced a second time at FrequencyDao.java:222 (and the save path's
translated-frequency insert saveTranslatedFrequency would fail the same
way). byId therefore 500s UNCONDITIONALLY for any id.

WHAT THE USER SEES (merge question, verified by clicking Edit in the probe):
the rebuild CALLS byId on edit and surfaces the failure honestly — the
toast reads "Edit failed to load record: ... Backend defect: byId queries
the missing table public.t_translated_frequency. Editing from the list data
instead." (the HTTP-500 HIS-1001 detail with its correlation reference is
included), and the form still populates from the loaded list row so the
screen stays usable. NOT silent, NOT a crash, NOT generic — the defect is
named. NOT FIXED (Rule 10) — a backend defect (missing table or stale
entity mapping), logged for the owner post-merge.

WHAT ELSE TOUCHES t_translated_frequency (d): FOUR paths — (1) byId via
getTranslatedFrequencyListForUpdate (FrequencyDao:243/255, fired by
FrequencyService.getById:294 — the observed 500); (2) the save path with
translations via saveTranslatedFrequency (FrequencyDao:191, fired by
FrequencyService.saveFrequency:223 when addedLanguageList is non-empty);
(3) updateTranslatedFrequency (FrequencyDao:166, translation edits); (4)
getTranslatedFrequencyList (FrequencyDao:222, the same select). The PLAIN
list (getAll) and a translation-free save do NOT touch it. The rebuild's
save sends no translation list, so save is not phantom-blocked (still
unfired — no-seed).

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: FINDING — the byId translation sub-query hits
  public.t_translated_frequency (absent); the list path (getAll, org-scoped
  HQL) is clean. This is the walk's first PHANTOM-ON-READ finding that
  actually FIRES (the endpoint 500s live, observed).
- Named-query INNER-JOIN null-FK drops: PASS — getAll uses HQL with an
  org/unit predicate; no INNER JOIN on the list path.
- Dup-check operator precedence: NOT PRESENT — saveFrequency inserts with
  no existence check (the absence class). The legacy controller's
  duplicate-code check is client-side only (mirrored in the rebuild).
- Org-trust: FINDING — the WRITE path is payload-carried org: saveFrequency
  sets organizationId/unitId from the request ternary
  (request.getOrganizationId()!=null ? ... : sessionService...) —
  the vaccinationMaster class, NOT the VerifiedIdentityService pattern the
  DANGLE siblings (477/479) use. Reads are org-scoped. NOT FIXED (Rule 10).
- Twin queries: PASS — the translated-frequency queries are the SAME defect
  twice (222/243), not a twin class.
- Swallowed exceptions on write paths: PASS — the controller's
  getAdministationTimes catch rethrows ServiceException (global handler);
  the other endpoints let exceptions propagate to the global handler
  (observed: the byId 500 came back as the HIS-1001 global envelope with
  the correlation id — the handler works).

## Verification state — HONEST

- preflight C1-C6 PASS (C5 flags DEAD-legacy — the dangle, surfaced not
  blocked); `npm run build` exit 0; smoke standard PASS (FINAL-URL
  /frequencyMaster, moduleId 478 present, SERVE-BRANCH matches the branch
  under test); interaction probe PASS (form renders, grid renders with the
  real dev row, 0 page errors).
- WRITE-VERIFICATION (Q28 — the claim was corrected after the owner's
  click-through found a 500): the probe FIRES the real save through the UI
  with the legacy contract (code/description/times/items/addedLanguageList)
  and records the OBSERVED response — success toast "Frequency saved
  successfully" + the PROBE-TIMES row visible in the list feed; the probe
  row is then DELETEd (dev table back to its prior state). Save is reported
  as "the endpoint accepted the payload and the row is visible in the list
  feed" — the observed behavior, never "working" beyond it.
- The save-500 incident (2026-08-10, owner click-through): the first
  rebuild payload OMITTED the items list entirely (500: NPE at
  FrequencyService.saveFrequency:193, request.getItems() null), and the
  first items shape omitted the buffer keys (500: m_frequency_detail
  NOT NULL violation on pre/post_buffer_administration_time — absent keys
  leave the DTO fields null; the legacy always sends them, and its String
  setters map an empty string to the midnight 00:00 default). Both were
  MAKER defects in the probe/rebuild payload, NOT backend defects — the
  save endpoint is healthy (201 with the legacy contract). The rebuild now
  sends the full legacy item shape. The toast names any failure in plain
  language ("Save failed: <server message>. Check the server log for the
  correlation reference.") — never the raw HTTP error.
- Endpoint verification: list live-verified (200, real row); byId
  live-verified FAILING (500, root-caused — the phantom, see the FINDING
  section); save live-verified (201, row created + read back + cleaned).

## Deliberate divergences (flagged, not silent)

1. The legacy's edit path calls byId, which 500s (phantom table); the
   rebuild populates the edit form from the loaded list row and reports any
   byId failure honestly instead of faking a working edit.
2. The legacy's administration-times helper feed (`constants.apiurl +
   "/frequencies"`) references an undefined `constants.apiurl` — a dead
   legacy code path (no definition exists anywhere); the rebuild does not
   reproduce it.
3. The legacy translation-grid (per-language frequency descriptions) writes
   through the same phantom table path; the rebuild omits it (flagged:
   it would fail on save the same way) — reported, not silently dropped.
4. Pagination is client-side search + full list for now (Phase-2/3).
