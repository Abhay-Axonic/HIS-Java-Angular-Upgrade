/**
 * vaccination-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/vaccination-master.html
 *   (state `vaccinationMaster`, module id 482, parent 476 Clinical
 *   Workstation -> 369 Global Master; controller: the generic
 *   MasterController — no screen-specific logic).
 *
 * THE DECOY-ROUTED SHAPE (the headline of this screen): node 482's
 * actionLink `#/vaccinationMaster` reaches the SHELL state at app.js:8427
 * (MasterController, zero ng-bindings, zero API calls, hardcoded grid row,
 * inert Save/Search, Diseases add-row as raw DOM manipulation). The REAL
 * 21-field CRUD screen is registered separately as `vaccinationMAster`
 * (app.js:17050, capital A, dedicated vaccinationMasterController.js,
 * views/clinicalWorkStation/vaccination-master.html) — but NO privilege node
 * carries an actionLink for it (verified: the only vaccination node in the
 * role-1 tree is 482 -> #/vaccinationMaster). Real functionality exists
 * under a different name; the menu points at the shell. This is the
 * decoy-routed legacy defect class (logged as a finding, queued).
 *
 * The static-shell rule (owner-approved, screens 4-5/9) applies: the rebuild
 * IMPLEMENTS the shell with the endpoints that demonstrably exist, and every
 * missing endpoint is reported honestly in the UI.
 *
 * BACKEND REALITY (this screen: endpoints MAPPED, NOT live-verified — the
 * live calls were skipped by owner choice on 2026-08-09; the README states
 * "mapped, not live-verified"): the full CRUD exists in the Billing module's
 * VaccinationMasterController (/api/billing/*) and is consumed by the
 * ORPHANED real screen — constants lis.url.config.js:527-533:
 *   save/update/updateStatus/byId/list/count/active
 * plus GET_ROUTE_OF_ADMINISTRATION_LIST (a named NATIVE query on
 * m_route_of_administration, org+unit scoped). Entity lab.m_vaccination_master
 * with clean column mapping (see README scoped backend check).
 */

/** One vaccination-master list row (VaccinationMasterDto shape). */
export interface VaccinationRow {
  vaccinationId: number;
  vaccinationCode?: number | null;
  name?: string | null;
  abbreviation?: string | null;
  status?: string | null;
  age?: number | null;
  day?: string | null;
  series?: number | null;
  [key: string]: unknown;
}

/** The generic HIS response envelope. */
export interface VaccinationApiEnvelope<T> {
  status?: string | null;
  code?: string | null;
  message?: string | null;
  listObject?: T | null;
  object?: unknown;
  [key: string]: unknown;
}

/** The Diseases add-row entries (client-side only — no backend for them). */
export interface DiseaseRow {
  disease: string;
}

export const emptyDiseaseRow: DiseaseRow = { disease: '' };
