/**
 * Legacy rootScope lookup helpers — the values come verbatim from the legacy
 * app.js rootScope functions (HIS-WAR/src/main/webapp/resources/assets/js/config/app.js):
 *   getBillStatusById   (app.js:1702): 1='Pending' 2='OnHold' 3='Settled'
 *   getVisitTypeById    (app.js:~1688): 1='OP' 2='IP' 3='Day Care' 4='ER'
 *   getDepositStatusById (app.js, immediately after getBillStatusById): 1='Pending'
 *                         2='Partially Collected' 3='Collected'
 * These are NOT master fetches — they are client-side constants in the legacy app.
 * Bind them in templates by exposing the functions on the component:
 *   readonly getBillStatusById = legacyGetBillStatusById;
 * etc. (the standing helper-exposure pattern, like formatLegacyNow).
 */

export const BILL_STATUS_BY_ID: Record<number, string> = {
  1: 'Pending',
  2: 'OnHold',
  3: 'Settled',
};

export const VISIT_TYPE_BY_ID: Record<number, string> = {
  1: 'OP',
  2: 'IP',
  3: 'Day Care',
  4: 'ER',
};

export const DEPOSIT_STATUS_BY_ID: Record<number, string> = {
  1: 'Pending',
  2: 'Partially Collected',
  3: 'Collected',
};

export function legacyGetBillStatusById(id: number | null | undefined): string {
  return id == null ? '' : (BILL_STATUS_BY_ID[id] ?? '');
}

export function legacyGetVisitTypeById(id: number | null | undefined): string {
  return id == null ? '' : (VISIT_TYPE_BY_ID[id] ?? '');
}

export function legacyGetDepositStatusById(id: number | null | undefined): string {
  return id == null ? '' : (DEPOSIT_STATUS_BY_ID[id] ?? '');
}
