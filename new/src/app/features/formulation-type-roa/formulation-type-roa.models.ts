/**
 * formulation-type-roa — parity rebuild of the legacy
 *   views/master/GlobalMaster/AssetManagenent/Master/formulation-type-ROA-master.html
 *   (menu node 479 'Formulation Type & ROA Master', parent 476 Clinical
 *   Workstation -> 369 Global Master, actionLink #/formulationTypeROAMaster).
 *
 * THE DANGLE SHAPE (new class, reverse of the decoy): the node's actionLink
 * points at #/formulationTypeROAMaster, but NO `.state('formulationTypeROA
 * Master')` is registered anywhere in app.js (grep over all 1183 states).
 * The controller IS loaded (index.html:1007) and the sidebar + quick-links
 * link to the route — so the legacy menu navigates to a non-existent state
 * (NG04002). The real screen + backend exist but are unreachable from the
 * legacy app's own menu. The rebuild registers the route (moduleId 479) and
 * wires the real Pharmacy-module backend.
 */
export interface FormulationRoaRow {
  id: number;
  formulationType: string;
  routeOfAdministration: string;
  status: boolean;
}

export interface FormulationTypeRow {
  id: number;
  code?: string | null;
  type: string;
  status?: boolean;
}

export interface RoaOption {
  id: number;
  code?: string | null;
  description: string;
}

export interface FormulationRoaSavePayload {
  formulationtypeid: number;
  routeOfAdministrationId: number[];
  status: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}
