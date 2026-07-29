/**
 * Types for the `tallyInterfacing` screen (legacy:
 * `resources/assets/js/controller/master/unit/tallyInterfacingController.js`,
 * view `views/master/UnitMaster/tally-interfacing.html`; backend
 * `legacy/Global/src/main/java/com/param/global/controller/
 * TallyIntegrationController.java` -> `TallyIntegrationServiceImpl` ->
 * `TallyIntegrationDaoImpl`).
 */

/** `/unit/org/{organizationId}` list item -- same shape used by other
 * screens' Clinical Unit dropdown (see web-appointment-booking.models.ts). */
export interface TallyUnitOption {
  unitId: number;
  unitName: string;
}

/**
 * One accounting-ledger row, as returned by `getIpLedger`/`getOpLedger`/
 * `getInventoryLedger`/`getDoctorShareLedger` (all four use the identical
 * Hibernate `Transformers.aliasToBean(TallyIntegrationDto.class)` SQL alias
 * set in `TallyIntegrationDaoImpl.java`) and sent back unmodified to
 * `sendTallyData`/`exportTallyData`.
 *
 * NOTE (see docs/ANGULAR_PENDING_FINDINGS.md,
 * PENDING-tallyinterfacing-billamount-instrumentno-blank-fieldname-mismatch):
 * legacy's own view template binds two columns to properties that don't
 * exist on this real response shape (`billAmount` instead of the real
 * `billingAmount`, `instrumentNo` instead of the real `instrumentNumber`),
 * so those two columns always render blank on legacy, on every tab. This
 * type is deliberately named after the REAL API fields (matching the SQL
 * aliases) for correctness; the display template intentionally does not
 * bind `billingAmount`/`instrumentNumber` to those two columns, to
 * faithfully reproduce legacy's actual (blank) rendered output.
 */
export interface TallyLedgerRow {
  ledger: string;
  debit: number;
  credit: number;
  voucherNarration: string;
  voucherNo: string;
  reference: string;
  voucherType: string;
  billingAmount: number;
  instrumentNumber: string;
  instrumentDate: string;
  voucherDate: string;
  uniqueNumber: string;
  bankName: string;
  refNo: string;
}

/** `getAllLedgersNameFromHis` row -- `Select distinct tti.ledger as "ledger" ...`. */
export interface HisLedgerName {
  ledger: string;
}

/** `getTallyInterfaceCommunicationKeys` response object
 * (`t_unit_application_billing_configuration` columns). `isTallyLocalHost`
 * is nullable in the DB (this dev DB's only config row has it NULL) --
 * legacy defaults to direct-post ('Y') whenever it isn't exactly "N". */
export interface TallyCommKeys {
  tallyIp?: string | null;
  tallyPortNo?: string | null;
  isTallyLocalHost?: string | null;
}

/** OPD=1, IPD=2, Inventory=3, Doctor's Share=4 -- matches legacy's
 * `setSecondTabLevel`/`isSetSecondTabLevel` numbering exactly. */
export type TallyTabId = 1 | 2 | 3 | 4;

/** Result of one attempt to reach the Tally application itself (either a
 * direct browser POST or a backend-relayed POST) -- see
 * `sendToHost`/`sendRequstToTally`/`sendCreateLedgerRequest`/
 * `sendRefreshTallyLedgerNameList` in the legacy controller, all four of
 * which share this exact 3-outcome shape. */
export type TallyCommResult =
  | { kind: 'success'; data: string }
  | { kind: 'notRunning' }
  | { kind: 'serverError'; message: string };
