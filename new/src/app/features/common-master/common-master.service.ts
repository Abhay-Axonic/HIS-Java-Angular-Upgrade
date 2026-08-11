import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';

/**
 * CommonMasterService — per-master endpoint dispatch for the commonMaster
 * family (legacy state 'commonMaster' at /commonMaster/:url, a 43-way
 * controllerProvider switch over the url param into one shared template —
 * see docs/DECISIONS_QUEUE.md 'COMMONMASTER DISPATCH MECHANISM').
 *
 * The backend discriminates by ENDPOINT PATH, not by a discriminator field:
 * each master has its own list endpoint under its family's root
 * (/LIS/global, /LIS/unit, /adt, ...). This service maps a dispatch key
 * (the :url param value) to the endpoint group that master uses.
 *
 * Auth: interceptor-only (core/auth.interceptor.ts owns the JWT). No manual
 * HttpHeaders. These endpoints bind nothing via @RequestHeader beyond what
 * the interceptor supplies (verified live: LIS/ADT both respond with the
 * standard header set).
 *
 * Slice scope (option b): the 4 Unit Master branches + 1 ADT real-data
 * branch (relation). The remaining 38 branches are added in family batches
 * per the owner's decision.
 */
export interface CommonMasterEntry {
  id: number | null;
  desc?: string | null;
  code?: string | null;
  status?: string | null;
  [key: string]: unknown;
}

export interface CommonMasterResponse {
  status?: string;
  code?: string | number | null;
  message?: string | null;
  listObject?: CommonMasterEntry[] | null;
}

/** Dispatch table: url param value -> endpoint definition. */
interface MasterEndpoint {
  method: 'GET' | 'POST';
  /** Full relative path (no base). */
  path: string;
  /** GET: path segments after the base, e.g. ['1', '0', '10'] for orgId/offset/recs. */
  getSegments?: (orgId: number, unitId: number, offset: number, size: number) => string[];
  /** POST: body builder. */
  body?: (orgId: number, unitId: number, offset: number, size: number) => Record<string, unknown>;
}

const DISPATCH: Record<string, MasterEndpoint> = {
  phlebotomyMaster: {
    method: 'GET',
    path: '/LIS/global/listPhlebotomyMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  headerMaster: {
    method: 'GET',
    path: '/LIS/global/listHeaderMaster',
    getSegments: (o, u, off, sz) => [String(o), String(u), String(off), String(sz)],
  },
  departmentMaster: {
    method: 'GET',
    path: '/LIS/global/listDepartmentMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  biochemicalTest: {
    method: 'POST',
    path: '/LIS/unit/getMicrobiologyTestMasterList',
    body: (o, u, off, sz) => ({ orgId: o, orgUnitId: u, offset: off, noOfRecordsPerPage: sz }),
  },
  relationmaster: {
    method: 'POST',
    path: '/adt/getRelationMasterList',
    body: (o, _u, off, sz) => ({ organizationId: o, offset: off, noOfRecordsPerPage: sz }),
  },
  // --- ADT family batch (root 380; owner order 2026-08-12: ADT 7 first) ---
  // All POST /adt/<ep> with {organizationId, offset, noOfRecordsPerPage},
  // live-verified 2026-08-12: gender 3 rows, marital 3 (Widowed), religion 2,
  // occupation 2, patientSource 6, referralType 5 — ALL real rows.
  sexmaster: {
    method: 'POST',
    path: '/adt/getGenderMasterList',
    body: (o, _u, off, sz) => ({ organizationId: o, offset: off, noOfRecordsPerPage: sz }),
  },
  maritalstatus: {
    method: 'POST',
    path: '/adt/getMaritalStatusMasterList',
    body: (o, _u, off, sz) => ({ organizationId: o, offset: off, noOfRecordsPerPage: sz }),
  },
  religion: {
    method: 'POST',
    path: '/adt/getReligionMasterList',
    body: (o, _u, off, sz) => ({ organizationId: o, offset: off, noOfRecordsPerPage: sz }),
  },
  occupation: {
    method: 'POST',
    path: '/adt/getOccupationMasterList',
    body: (o, _u, off, sz) => ({ organizationId: o, offset: off, noOfRecordsPerPage: sz }),
  },
  patientsource: {
    method: 'POST',
    path: '/adt/getPatientSourceMasterList',
    body: (o, _u, off, sz) => ({ organizationId: o, offset: off, noOfRecordsPerPage: sz }),
  },
  referraltype: {
    method: 'POST',
    path: '/adt/getReferralTypeMasterList',
    body: (o, _u, off, sz) => ({ organizationId: o, offset: off, noOfRecordsPerPage: sz }),
  },
  // --- LIS family batch (owner order 2026-08-12; DQ-2026-08-12-003) ---
  // 26 branches. All GET /LIS/global/<list>/{orgId}/{offset}/{size} EXCEPT
  // languageMaster + serologyTestFindingMaster (4 segments: orgId/unitId/
  // offset/size). 10 are descriptive dev-empty ("X Records not found."); 16
  // return the GENERIC "Internal Server Error" envelope — the unreachable-
  // empty-branch fall-through in the legacy service layer (the size()>0 guard
  // makes the inner isEmpty() branch dead, so empty lists fall to
  // ERR_500_MESSAGE). The 16 get an AMBER-permanent banner from the
  // component; live-verified 2026-08-12.
  sampleMaster: {
    method: 'GET', path: '/LIS/global/listSampleMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  containerMaster: {
    method: 'GET', path: '/LIS/global/listContainerMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  techinqueMaster: {
    method: 'GET', path: '/LIS/global/listTechinqueMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  prerequisitesMaster: {
    method: 'GET', path: '/LIS/global/listPrerequisitesMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  unitMaster: {
    method: 'GET', path: '/LIS/global/listUnitMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  reagentMaster: {
    method: 'GET', path: '/LIS/global/listReagentMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  reportTypeMaster: {
    method: 'GET', path: '/LIS/global/listReportType',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  bacteriaShapMaster: {
    method: 'GET', path: '/LIS/global/listBacteria',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  wtifilmStudyMasterMaster: {
    method: 'GET', path: '/LIS/global/listWtfilmStudy',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  mediaMaster: {
    method: 'GET', path: '/LIS/global/listMediaMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  colonyMaster: {
    method: 'GET', path: '/LIS/global/listColony',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  organismMaster: {
    method: 'GET', path: '/LIS/global/listOrganismMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  organismGroupMaster: {
    method: 'GET', path: '/LIS/global/listOrganisumGroup',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  stainigMaster: {
    method: 'GET', path: '/LIS/global/listStainigMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  microbiologyStainMaster: {
    method: 'GET', path: '/LIS/global/listMicrobStainigMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  antibioticMaster: {
    method: 'GET', path: '/LIS/global/listAntibiotics',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  antibioticsMaster: {
    method: 'GET', path: '/LIS/global/listAntibiotic',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  incubationTestMaster: {
    method: 'GET', path: '/LIS/global/listIncubationTest',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  antibioticsGroupMaster: {
    method: 'GET', path: '/LIS/global/listAntibioticsGroupMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  sensitivityTypeResultMaster: {
    method: 'GET', path: '/LIS/global/listSensitivityTestResult',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  slideProcedureMaster: {
    method: 'GET', path: '/LIS/global/listSlideProcedure',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  bacteriaClassificationMaster: {
    method: 'GET', path: '/LIS/global/listBacteriaClassfication',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  blockMaster: {
    method: 'GET', path: '/LIS/global/listBlock',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  serologyTestFindingMaster: {
    method: 'GET', path: '/LIS/global/listSerologyMaster',
    getSegments: (o, u, off, sz) => [String(o), String(u), String(off), String(sz)],
  },
  storageLocationMaster: {
    method: 'GET', path: '/LIS/global/listStorageLocationMaster',
    getSegments: (o, _u, off, sz) => [String(o), String(off), String(sz)],
  },
  languageMaster: {
    method: 'GET', path: '/LIS/global/listLanguageMaster',
    getSegments: (o, u, off, sz) => [String(o), String(u), String(off), String(sz)],
  },
};

export const COMMON_MASTER_DISPATCH = DISPATCH;

@Injectable({ providedIn: 'root' })
export class CommonMasterService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  /** True when the dispatch key has a configured endpoint. */
  hasMaster(key: string): boolean {
    return key in DISPATCH;
  }

  /** Keys known to the dispatcher (for the header/title). */
  knownKeys(): string[] {
    return Object.keys(DISPATCH);
  }

  /**
   * Fetch the master's rows. Returns the raw listObject; the component maps
   * the generic entry shape (id/desc/code/status) onto the grid.
   */
  list(key: string, orgId: number, unitId: number, offset = 0, size = 50): Observable<CommonMasterResponse> {
    const ep = DISPATCH[key];
    if (!ep) {
      throw new Error(`commonMaster: no endpoint configured for '${key}'`);
    }
    const base = `${this.base}${ep.path}`;
    if (ep.method === 'GET' && ep.getSegments) {
      const segs = ep.getSegments(orgId, unitId, offset, size);
      return this.http.get<CommonMasterResponse>(`${base}/${segs.join('/')}`);
    }
    const body = ep.body ? ep.body(orgId, unitId, offset, size) : {};
    return this.http.post<CommonMasterResponse>(base, body);
  }
}
