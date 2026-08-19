import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { ShiftClosingService } from './shift-closing.service';
import { ShiftClosingRefundRow } from './shift-closing.models';

/**
 * Parity rebuild of the legacy `shiftClosing` state (600) — template
 * views/master/UnitMaster/billingMaster/shift-closing.html, controller
 * billing/cashDeskManagement/global/shiftClosingController.js.
 *
 * CLASSIFICATION (live-verified 2026-08-12, backend localhost:8080, headers
 * Authorization Bearer tools/.token_cache.json + userId:1 unitId:1
 * organizationId:1): write-path cash-entry form — NOT a master list. The
 * refund feed is a POST endpoint with a complex payload:
 * /api/global/getRefundList (hardcoded URI, shiftClosingController.js:278 —
 * NOT a url.config constant), payload {organizationId, unitId, createdBy,
 * openingDate, currentDate, offset, noOfRecordsPerPage} -> HTTP 400
 * "Invalid parameters" with the probe (payload incomplete). The DAO
 * (ShiftClosingMasterDaoImpl.getRefundList, :529-563) needs {openingDate,
 * currentDate, organizationId, unitId, createdBy} and additionally carries
 * a stray-quote SQL syntax bug at :541-542 (`AND :currentDate ' and
 * prm.orgnisation_id …`), so the feed is not consumable as a list even
 * with a complete payload. Deferred to the transaction methodology; this
 * rebuild renders the honest state (see template).
 */
@Component({
  selector: 'app-shift-closing',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './shift-closing.html',
  styleUrl: './shift-closing.scss',
})
export class ShiftClosing {
  private readonly service = inject(ShiftClosingService);
  private readonly session = inject(SessionService);

  readonly rows = signal<ShiftClosingRefundRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly probed = signal(false);
  readonly probeStatus = signal('');
  readonly probeMessage = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service
      .getRefundList({
        organizationId: this.session.organizationId ?? 1,
        unitId: this.session.unitId ?? 1,
        createdBy: this.session.userId ?? 1,
        openingDate: '2026-08-12 00:00:00',
        currentDate: '2026-08-12 23:59:59',
        offset: 0,
        noOfRecordsPerPage: 10,
      })
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          this.probed.set(true);
          this.probeStatus.set(res.status ?? '');
          this.probeMessage.set(res.message ?? '');
          this.rows.set(res.listObject ?? []);
        },
        error: (err) => {
          this.loading.set(false);
          this.probed.set(true);
          this.probeStatus.set(`http-${err?.status ?? 'unknown'}`);
          this.probeMessage.set(`${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: ShiftClosingRefundRow): string[] {
    return [
      String(row.voucherNo ?? '—'),
      String(row.createdDate ?? '—'),
      String(row.pName ?? '—'),
      String(row.uhidNumber ?? '—'),
      String(row.amount ?? '—'),
    ];
  }

  // ---- shift-close WRITE (BUILDABLE, DQ-059) ---------------------------
  readonly form = signal<{ difference: number | null; refundAmount: number | null }>({
    difference: null,
    refundAmount: null,
  });
  readonly saving = signal(false);
  readonly saveMessage = signal('');
  readonly saveError = signal('');
  readonly savedId = signal<number | null>(null);

  /** Today as 'dd-M-yyyy' — the mapper's parse format (time suffixes mangle). */
  today(): string {
    const d = new Date();
    return `${d.getDate()}-${d.getMonth() + 1}-${d.getFullYear()}`;
  }

  save(): void {
    this.saving.set(true);
    this.saveMessage.set('');
    this.saveError.set('');
    this.savedId.set(null);
    const date = this.today();
    this.service
      .saveShiftCloseBalance({
        organizationId: this.session.organizationId ?? 1,
        unitId: this.session.unitId ?? 1,
        createdBy: this.session.userId ?? 1,
        updatedBy: this.session.userId ?? 1,
        createdDate: date,
        updatedDate: date,
        status: 'A',
        difference: this.form().difference ?? undefined,
        refundAmount: this.form().refundAmount ?? undefined,
        listOfIds: [],
      })
      .subscribe({
        next: (res) => {
          this.saving.set(false);
          if (res.status === 'success') {
            this.savedId.set(res.object?.id ?? null);
            this.saveMessage.set(
              `Shift-closing balance saved.${res.object?.id ? ` balanceId: ${res.object.id}` : ''}`,
            );
          } else {
            this.saveError.set(`Backend rejected: ${res.message ?? 'unknown'}`);
          }
        },
        error: (err) => {
          this.saving.set(false);
          this.saveError.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });


  }
  /** Signal-aware field setter — rerouted from the in-place `form().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.form.update((f) => ({...f, [key]: value}));
  }

}
