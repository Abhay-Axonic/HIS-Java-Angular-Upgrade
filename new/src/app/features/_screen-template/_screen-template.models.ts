/**
 * TEMPLATE -- replace with the real record shape for the screen being
 * rebuilt, read from the actual backend DTO the legacy controller
 * consumes (not guessed). See the playbook's Step 1 (audit) before
 * assuming field names/types.
 */
export interface ScreenTemplateRecord {
  id: number;
  code: string;
  description: string;
  status: 'A' | 'I';
}

/**
 * Real screens typically embed unitId/organizationId directly on the empty
 * record for create payloads -- see billing-group-master.models.ts's
 * `emptyBillingGroupMaster(unitId, organizationId)` for a real example.
 * Add those fields to `ScreenTemplateRecord` and this factory once the
 * real record shape is known.
 */
export function emptyScreenTemplateRecord(): ScreenTemplateRecord {
  return {
    id: 0,
    code: '',
    description: '',
    status: 'A',
  };
}
