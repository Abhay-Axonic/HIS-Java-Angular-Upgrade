# Notes Master — screen 24 (module 529, OTIMS)

Parity rebuild of the legacy
`views/master/GlobalMaster/OTIMS/notes-master.html` (menu node 529
'Anaesthesia Note', parent 514 OTIMS -> 369 Global Master, controller
notesMasterController, loaded index.html:1741).

## CLASSIFICATION — DANGLE with a routing mismatch (C7-verified)

- State registration: REGISTERED — app.js:20299 `.state('notesMaster')`,
  url /notesMaster, templateUrl notes-master.html, controller
  notesMasterController, title 'Notes Master'. No resolve gate.
- Menu node: node 529 'Anaesthesia Note' EXISTS under 514, but its
  actionLink is `#/noteMaster` — a state that does NOT exist — while the
  state's url is `/notesMaster`. The one-character legacy routing defect
  (QF-2026-08-10-AG): anyone clicking 'Anaesthesia Note' in the legacy menu
  gets nothing. The rebuild uses the STATE's url (`/notesMaster`) per the
  owner's instruction; the node link stays broken in legacy until the
  legacy fix lands (fixable in the legacy app independently — one character
  in one file).
- Controller loaded: index.html:1741
  (resources/assets/js/controller/otims/global/notesMasterController.js).
- Backend: NotesMasterController (OTIMS module, com.param.otims)
  `@RequestMapping("/global/otims")` — save/update/status/list/
  byNoteType/pagination/count (7+ endpoints).

## PHANTOM-BROKEN LIST — the walk's FOURTH phantom finding

LIVE-VERIFIED BY CALLING (body inspected, ENDPOINT-VERIFICATION STANDARD):
POST /global/otims/getNotesMasterListPagination {organizationId:1,unitId:1,
offset:0,noOfRecordsPerPage:20} -> HTTP 500 body {"status":"error",
"code":"500","message":"Something went wrong. Reference: ERR-e81a54b3"}
(correlation-id envelope — the global handler works, NOT swallowed);
POST /global/otims/getNotesMasterList -> 500 ERR-7f29a04a. Both reads
unconditionally 500 ("could not extract ResultSet" — a SQLGrammarException).

ROOT CAUSE (the four answers, per the owner's ask): (a) the missing columns
are SIX — template_format_id, form_template_id, form_template_keywords,
title, template_type_id, template_id — in otims.m_note_master (the DDL has
exactly 12 columns: note_master_id, code, description, note_type, template,
created_date, created_by, status, updated_by, updated_date, unit_id,
organization_id; information_schema-verified; the entity maps the six at
NotesMaster.java:129-147). (b) The query is GET_NOTELIST_MASTER_LIST
(NotesMaster.java:47-63) selecting all six phantoms; GET_NOTELIST_MASTER_
LIST_BY_NOTE_TYPE_ID (65-90) selects the same six. (c) ALL three read/write
paths break: list + byNoteType 500 on the SELECT (could not extract
ResultSet); saveNotesMaster 500s on the INSERT (could not execute statement;
SQLGrammarException at NotesMasterController.java:34 — verified live with the
correct Integer noteType: ERR-d85de88c). The save's DTO contract also
requires noteType as Integer (a string 400s HIS-400 at Jackson binding — the
probe-payload class). (d) LOUD failure, NOT the screen-17 masked shape: every
path returns HTTP 500 with a correlation-id envelope (ERR-...), not HTTP 200
with a body error. Same class as screen 17 (resourceConfiguration's
visit_type_id phantom) and the walk's t_translated_frequency / allergy cims
phantoms. NOT FIXED (Rule 10) — backend BR candidate (BR-190 class: phantom
columns).

THE REBUILD surfaces this honestly (the screen-17 pattern): it CALLS the
list, detects the error envelope (both the HTTP-200-error and the
correlation-id-500 shapes), and names the phantom defect to the user with
the correlation reference — the screen cannot be green while the backend
500s. The save/update/status endpoints are mapped but NOT wired (the save
contract is a forms-engine integration — templateFormatId/templateId/
templateTypeId — and the same phantom columns would fail the write paths;
BR-190-class dependency).

## Scoped backend check

- Phantom columns: FINDING (above — FOURTH instance, the walk's first on a
  DANGLE screen's list path).
- INNER-JOIN drops: PASS — GET_NOTELIST_MASTER_LIST is single-table (no
  joins).
- Dup-check: the save's CHECK_FOR_UNIQUE_NOTE_MASTER_SAVE / _NOT_ID exist
  but are not live-verified (the save path is unwired — forms-engine
  contract).
- Org-trust: FINDING (module-wide payload-carried class, screen-17 record).
- Swallowed exceptions: PASS — the errors surface as correlation-id 500s
  (global handler), not swallowed generic envelopes.

## Build state

- Route: NEW at the STATE's url `/notesMaster` (moduleId 529).
- Gates: preflight C1-C7 (C7 PASS — genuinely new; C5 PASS with the node
  chain — the node exists even though its actionLink points elsewhere).
  ng build exit 0. Smoke: route serves, the phantom defect banner renders
  honestly with the correlation reference, 0 page errors.
