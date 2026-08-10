/**
 * frequency-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/AssetManagenent/Master/frequency-master.html
 *   (menu node 478 'Frequency Master', parent 476 Clinical Workstation ->
 *   369 Global Master, actionLink #/frequencyMaster).
 *
 * THE DANGLE SHAPE (menu'd-unrouted — the drug-recall class, THIRD instance
 * after 479 and 477): the node's actionLink points at #/frequencyMaster, but
 * NO `.state('frequencyMaster')` is registered anywhere in app.js (grep = 0).
 * The controller IS loaded (index.html:1009), the template exists, and the
 * backend is LIVE — so the legacy menu navigates to a non-existent state
 * (NG04002). Per the amended reachability rule (RULES.md four-way
 * classification) a DANGLE screen with a real backend is BUILDABLE: the
 * rebuild RESTORES a menu-advertised screen the legacy cannot open.
 */
export interface FrequencyItem {
  id?: number;
  administrationTime: string;
  preBufferAdministrationTime?: string | null;
  postBufferAdministrationTime?: string | null;
}

export interface FrequencyRow {
  id: number;
  code: string;
  description: string;
  noOfAdministrationTime: number;
  status: boolean;
  items?: FrequencyItem[];
}

export interface FrequencySavePayload {
  code: string;
  description: string;
  noOfAdministrationTime: number;
  items: FrequencyItem[];
  addedLanguageList: unknown[];
  status?: boolean;
  organizationId: number;
  unitId: number;
  userId: number;
}
