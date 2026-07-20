/**
 * Builds a request-params object containing only the fields the caller
 * actually filled in -- mirrors legacy's own conditional request-building
 * (e.g. `if (patSearchObj.uhIdNumber) {...}`) for multi-field searches
 * where the backend AND-combines whichever fields are present.
 *
 * This is the fix for docs/ANGULAR_PENDING_FINDINGS.md entry 15:
 * inputOutputCharts' rebuild sent `uhIdNumber` and `patientName` as the
 * same typed string on every search. The backend's real query AND-combines
 * every non-empty field, so a patient had to match both simultaneously --
 * something a real UHID and a real name never do -- and the search
 * returned zero results for virtually any real search. Only send the
 * field(s) the user actually intends to filter by.
 *
 * Before wiring this into a new screen, confirm against the actual legacy
 * DAO/query (not assumed) whether the fields are really AND-combined and
 * really meant to be searched independently -- see the playbook's Step 1
 * audit guidance. This helper only handles the "send only what's filled
 * in" half of the fix; it doesn't decide search semantics for you.
 */
export function buildOptionalParams<T extends Record<string, string | undefined | null>>(
  fields: T,
): Partial<Record<keyof T, string>> {
  const result: Partial<Record<keyof T, string>> = {};
  for (const key of Object.keys(fields) as (keyof T)[]) {
    const value = fields[key]?.trim();
    if (value) {
      result[key] = value;
    }
  }
  return result;
}
