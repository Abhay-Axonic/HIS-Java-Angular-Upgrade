/**
 * immunization-schedule — models.
 *
 * The screen's own data model (Age/Vaccine/Sequence/Disease) has NO backend:
 * no immunization-schedule entity, DAO, or controller exists anywhere. The
 * only real feed on this screen's path is the vaccine catalog (the
 * vaccination-master active list, GET /api/billing/getActiveVaccinationMaster
 * — the named query GET_ACTIVE_Vaccination_Master, aliasToBean
 * VaccinationMasterDto). Its rows carry vaccinationId/vaccinationCode/
 * abbreviation/day/age/series/... — the Vaccine select is wired to it; the
 * remaining fields have no source and are reported honestly.
 */

/** One vaccine-catalog row (VaccinationMasterDto shape, the fields the named
 * query aliases). */
export interface VaccineOption {
  vaccinationId: number;
  vaccinationCode?: string | null;
  abbreviation?: string | null;
  day?: string | null;
  age?: number | null;
  series?: string | null;
  status?: string | null;
  [key: string]: unknown;
}

/** The generic HIS response envelope (status "success"/"error", listObject). */
export interface VaccinationApiEnvelope<T> {
  status?: string | null;
  code?: string | null;
  message?: string | null;
  listObject?: T | null;
  object?: unknown;
  [key: string]: unknown;
}
