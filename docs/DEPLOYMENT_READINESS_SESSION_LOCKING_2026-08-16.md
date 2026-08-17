# DEPLOYMENT READINESS — SESSION & CONCURRENCY INVESTIGATION (2026-08-16, nothing built)

## PART 1 — MULTI-SESSION PER USER

**Mechanism: a DATABASE FLAG — not a cache, not a token blacklist.**
- The login-log table `public.t_user_login_log` (log_id, user_id, is_login
  char, is_logout char, status char, mac_address, unit_id, organization_id,
  log_time, audit cols) carries one row per login.
- The collision query `CHECK_OTHER_LOGIN_BY_USER_ID`
  (Entity/.../adt/master/global/model/UserLoginLog.java:49-57) finds any
  ACTIVE row: `status='A' AND userId=:userId AND isLogin='Y' AND unitId AND orgId`.
- The login flow (Global/.../service/UserMasterServiceImpl.java ~280-315):
  after the credential check — **same macAddress** → the previous rows are
  closed (`updateUserLog`: status I/C, isLogin N, isLogout Y — :296-309) and
  the login proceeds; **different macAddress with an ACTIVE row** →
  `return new Response(SUCCESS, SUCCESS_CODE, "You are already logged in...!!", null, 1)` (:314)
  — the login is blocked, which is what the frontend's "already logged in"
  dialog renders.
- **The JWT is stateless** (8h TTL, exp/iat claims; no per-request login-log
  check) — the previous session is never token-blacklisted; only the
  login-log row flags change.

**What removing the check touches:**
1. The service branch at UserMasterServiceImpl:311-315 (the blocked login →
   proceed). Same-mac logins already proceed, so multi-TAB (one machine)
   already works; the check only blocks different-machines.
2. The frontend's collision dialog + its Ok-path (loginController.js) — the
   code path becomes dead.
3. The login-log's ACTIVE-row semantics: the logout's `saveUserLogDetails`
   (an isLogin='N' INSERT — it does NOT close the active row),
   `updateUserLog` (the close), the login-history audit, and the other
   login-log queries (e.g. `GET_USER_LIST_OF_SCHEDULING_TO_UPDATE`) — all
   keep working with more rows; none validate a single-session invariant
   per-request.
4. The account-concurrency control: the check doubles as the
   same-account-different-machine detection; removing it deliberately
   permits concurrent sessions (the product requirement).

## PART 2 — PATIENT-LEVEL RECORD LOCKING

**Nothing exists today.**
- No `version` / `locked_by` / `is_locked` column on ANY of the six core
  tables — verified in the live schema: patient.t_patient_registration,
  patient.t_patient_details, opd.t_encounter_master, adt.t_admission,
  pharmacy.t_prescription, billing.m_billing_master — all carry only
  `updated_by` / `updated_date` (the audit stamps). Note: the owner's proposed
  `t_billing_master` does not exist — the real table is
  `billing.m_billing_master`.
- **Zero `@Version` annotations** across the whole Entity module (grep).
- **No DAO-level optimistic check**: the writes are findById + Hibernate
  update by PK (e.g. PatientTypeDaoImpl.updatePatientTypeStatus :88-107 — the
  DQ-128 toggle: the payload's updatedDate is parsed and WRITTEN as the audit
  stamp, never compared to the row's current value). No "modified since
  load" check anywhere.
- **The legacy frontend sends updatedDate on updates** (formatLegacyNow —
  the DQ-128 evidence) but nothing validates it.

**What would change (the owner's proposed mechanism):**
- Add `version` to the six tables + `@Version` in the six entities — Hibernate
  increments and throws StaleObjectStateException on a mismatch — map it to a
  clear "record was modified by another user" message in the global handler.
- The scope is days, not weeks, for the backend — with TWO caveats:
  1. **The legacy 1.5 frontend sends no version** — a strict
     expected-version check would reject every legacy update (version absent
     vs the row's 1+). The check must be version-present-optional (the legacy
     proceeds; the rebuild's optimistic clients enforce), or the legacy's
     update paths must start sending it.
  2. The REBUILD must plumb the version: read it in getById, carry it in the
     edit state, send it on save/update — that is every rebuilt edit() flow
     (the DQ-127 class) + the payload contracts + the error handling for the
     rejection message. The frontend plumbing roughly doubles the effort.
