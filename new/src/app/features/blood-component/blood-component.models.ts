/**
 * blood-component — parity rebuild of the legacy
 *   views/master/GlobalMaster/blood-component.html
 *   (state `bloodComponent`, module id 481, parent 476 Clinical
 *   Workstation -> 369 Global Master; controller: the generic
 *   MasterController — no screen-specific logic).
 *
 * THE DECOY-ROUTED SHAPE (same class as screen 10's vaccinationMaster):
 * node 481's actionLink `#/bloodComponent` reaches the SHELL state at
 * app.js:8421 (MasterController, zero ng-bindings, zero API calls,
 * hardcoded grid row with a Status switch + pencil, inert Save/Search).
 * The REAL full-CRUD screen is registered separately as
 * `bloodComponentMaster` (app.js:16591, dedicated bloodComponentMaster-
 * Controller.js, views/master/UnitMaster/bloodBank/blood-component-blood-
 * bank.html, resolve gate item.id == 736) — but NO privilege node carries
 * an actionLink for it (verified: the only blood* nodes in the role-1 tree
 * are 481 -> #/bloodComponent and 251 Blood Request -> #/bloodRequest).
 * Real functionality exists under a different name; the menu points at the
 * shell. Legacy defect, logged, queued (Q32).
 *
 * BACKEND REALITY (verified by CALLING with the cached token — 2026-08-10):
 * the full CRUD exists in the BloodBank module — BloodComponentMaster-
 * Controller, @RequestMapping("/bloodBank") — and the constants
 * (bloodBank.url.config.js:97-103) carry NO /api prefix (the fragment
 * class, same as screen 8's /adt):
 *   POST /bloodBank/getBloodComponentMasterList   -> 200 success (1 row dev)
 *   POST /bloodBank/getBloodComponentMasterCount  -> 200 success (object 1)
 *   GET  /bloodBank/getActiveBloodComponentMaster/1/1 -> 200 success (1 row)
 *   GET  /bloodBank/getBloodComponentMasterById/1/1/1 -> 200 success ([] — id 1 absent; the dev row is id 5)
 * Write endpoints (save/update/status) are MAPPED; their success is
 * UNVERIFIED per the WRITE-VERIFICATION rule (never "working").
 *
 * The rebuild implements the SHELL per the static-shell rule: grid wired to
 * the live list feed, form per the shell's two fields (Code + Name), Save
 * and the Status switch reported honestly, Diseases-less (this shell has no
 * disease section — unlike vaccinationMaster).
 */

/** One blood-component row (BloodComponentMasterDto shape). */
export interface BloodComponentRow {
  bloodComponentId: number;
  bloodComponentCode?: string | null;
  bloodComponentDesc?: string | null;
  bloodComponentShortDesc?: string | null;
  expiry?: number | null;
  quantity?: number | null;
  byCrossChecking?: string | null;
  status?: string | null;
  unitId?: number | null;
  organizationId?: number | null;
  [key: string]: unknown;
}

/** The generic HIS response envelope. */
export interface BloodComponentApiEnvelope<T> {
  status?: string | null;
  code?: string | null;
  message?: string | null;
  listObject?: T | null;
  object?: unknown;
  [key: string]: unknown;
}
