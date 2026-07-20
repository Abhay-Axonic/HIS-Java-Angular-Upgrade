import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CorrectiveMaintenanceHandoffService } from '../../core/corrective-maintenance-handoff.service';
import { DetailsCorrectiveMaintenanceService } from './details-corrective-maintenance.service';
import { DcmAccessoryItem, DcmDetail, DcmInsurance, DcmManufacturer, DcmSerialOption, DcmVendor } from './details-corrective-maintenance.models';

/**
 * Rebuild of the legacy `detailsCorrectiveMaintenance` state -- see
 * detailsCorrectiveMaintenanceController.js (193 lines) +
 * details-corrective-maintenance.html. No data.moduleId (no resolve
 * block, app.js:10692-10699), real cookieObject guard only.
 *
 * Row data arrives via `CorrectiveMaintenanceHandoffService.takeObject()`,
 * the rebuild's equivalent of legacy's `$stateParams.detailsObj`
 * (ctrl:30) -- this state is reached only via ui-router `params`, never a
 * URL/route param, so there is nothing to read from ActivatedRoute here.
 *
 * Judgment calls / findings, cited by file:line:
 * - `editflag` (ctrl:38-41): read-only unless `detailsObj.editflag === 0`.
 *   Mirrors legacy's default-to-locked behavior exactly (`editflag: true`
 *   unless explicitly `0`).
 * - Known legacy gap, faithfully reproduced, NOT fixed: `resolutionExpectedTime`
 *   is bound via ng-model (view:155) but never included in the payload
 *   built by `saveCorectiveMaintenance()` (ctrl:111-143) -- its value is
 *   silently dropped on save. Reproduced below by keeping
 *   `resolutionExpectedTime` as its own signal that the save payload never
 *   reads.
 * - Dead branch bug in `changedowndate()` (ctrl:96-109), found while
 *   auditing: `ng-true-value="true"`/`ng-false-value="false"` on the "Is
 *   Operative" checkbox (view:62) store the STRING "true"/"false" on
 *   `detailsObj.isDown`, not a boolean. `changedowndate()` then checks
 *   `detailsObj.isDown == false` -- a string-vs-boolean `==` that is
 *   ALWAYS false in JS (`Number("false")` is `NaN`, and `NaN == 0` is
 *   false), so the "clear the down date/time" branch never runs; the
 *   `else` branch always fires instead, re-copying the current down
 *   date/time back onto themselves. Net observable effect: toggling "Is
 *   Operative" never clears down date/time, only `isDown` itself changes.
 *   Reproduced faithfully as a no-op in `onIsDownChange()` below (only
 *   `isDown` is updated) rather than "fixing" the toggle to actually
 *   clear the fields.
 * - Date handling: legacy parses/formats `downDateTime`/
 *   `resolutionExpectedDateTime` with moment.js between `DD-MM-YYYY`
 *   display format and an internal Date object that always carries a
 *   midnight time-of-day (ctrl:43-46 parse with a date-only format
 *   string; ctrl:105-106's `changedowndate()` copies that same
 *   already-midnight value back). The real time-of-day for "down" lives
 *   separately in the free-text `downTime` field (clockpicker), never in
 *   `downDateTime` itself. Reproduced below with plain `yyyy-MM-dd`
 *   strings bound to `<input type="date">` (per this project's known
 *   Date-object-vs-date-input footgun) and a `DD-MM-YYYY 00:00:00` legacy
 *   format on save.
 * - `saveCorectiveMaintenance()`'s own `downDateTime` guard (ctrl:115:
 *   `!= undefined || != null || != ""`) is an OR-chain that is always
 *   true for any value including `undefined` (`undefined != null` is
 *   `false`, but `undefined != ""` is `true`, so the OR is true
 *   regardless) -- so the conversion always runs in practice. Reproduced
 *   by always converting rather than porting the (functionally inert)
 *   guard.
 */
@Component({
  selector: 'app-details-corrective-maintenance',
  imports: [FormsModule, RouterLink],
  templateUrl: './details-corrective-maintenance.html',
  styleUrl: './details-corrective-maintenance.scss',
})
export class DetailsCorrectiveMaintenance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notification = inject(NotificationService);
  private readonly handoff = inject(CorrectiveMaintenanceHandoffService);
  private readonly api = inject(DetailsCorrectiveMaintenanceService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  editflag = true;

  detail = signal<DcmDetail>({});
  serialNoObj = signal<DcmSerialOption>({});
  serialNumberList = signal<DcmSerialOption[]>([]);

  downDateIso = signal('');
  downTime = signal('');
  isDown = signal<'true' | 'false'>('false');
  resolutionExpectedDateIso = signal('');
  resolutionExpectedTime = signal('');

  showPmsModal = signal(false);
  activePmsTab = signal(1);
  manufacturerList = signal<DcmManufacturer[]>([]);
  vendorList = signal<DcmVendor[]>([]);
  insuranceList = signal<DcmInsurance[]>([]);
  itemDataList = signal<DcmAccessoryItem[]>([]);

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    const obj = this.handoff.takeObject();
    this.detail.set(obj as DcmDetail);
    this.editflag = obj.editflag !== 0;

    this.serialNoObj.set({ serialNumber: obj.serialNumber, batchId: obj.batchId });
    this.downDateIso.set(parseLegacyDateToIso(obj.downDateTime));
    this.downTime.set(obj.downTime ?? '');
    this.resolutionExpectedDateIso.set(parseLegacyDateToIso(obj.resolutionExpectedDateTime));
    this.resolutionExpectedTime.set(obj.resolutionExpectedTime ?? '');
    const rawIsDown = obj.isDown as unknown;
    this.isDown.set(rawIsDown === true || rawIsDown === 'true' ? 'true' : 'false');

    this.getSerialNumbers();
  }

  private getSerialNumbers(): void {
    this.loader.start();
    this.api.getSerialNoByItemId(this.detail().itemId ?? '', this.unitId, this.organizationId).subscribe({
      next: (res) => {
        const list = res.listObject ?? [];
        this.serialNumberList.set(list);
        // Legacy's `x as x.serialNumber for x in serialNumberList track by
        // x.batchId` (view:46) matches the pre-selected option by batchId
        // value, not object identity. Angular's [ngModel] on a <select>
        // bound via [ngValue] matches by reference, so re-point
        // serialNoObj at the actual list entry once it's loaded --
        // otherwise the dropdown would render unselected even though a
        // serial number is already saved.
        const current = this.serialNoObj();
        const match = list.find((opt) => opt.batchId === current.batchId);
        if (match) {
          this.serialNoObj.set(match);
        }
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  updateField(key: 'requestDetails' | 'remark', value: string): void {
    this.detail.update((d) => ({ ...d, [key]: value }));
  }

  /** See class doc comment: faithfully a no-op beyond storing the toggle's own value. */
  onIsDownChange(checked: boolean): void {
    this.isDown.set(checked ? 'true' : 'false');
  }

  getPmsDetails(): void {
    const batchId = this.serialNoObj().batchId;
    if (batchId === null || batchId === undefined || (batchId as unknown) === '') {
      this.notification.warning('Please select Serial Number');
      return;
    }

    const payload = {
      itemId: this.detail().itemId,
      unitId: this.unitId,
      organizationId: this.organizationId,
      batchId,
    };

    this.loader.start();
    this.api.getPmsDetails(payload).subscribe({
      next: (res) => {
        const obj = res.object ?? {};
        this.manufacturerList.set(obj.manufacturerList ?? []);
        this.vendorList.set(obj.vendorList ?? []);
        this.insuranceList.set(obj.insuranceList ?? []);
        this.itemDataList.set(
          (obj.accessoryList ?? []).map((a) => ({
            itemCode: a.assetCode,
            itemName: a.assetName,
            quantity: a.quantity,
            serialNumber: a.serialNumber,
          })),
        );
        this.loader.stop();
        this.activePmsTab.set(1);
        this.showPmsModal.set(true);
      },
      error: () => this.loader.stop(),
    });
  }

  saveCorrectiveMaintenance(): void {
    const current = this.detail();
    const payload: Record<string, unknown> = {
      ...current,
      serialNumber: this.serialNoObj().serialNumber,
      batchId: this.serialNoObj().batchId,
      downDateTime: isoToLegacyDateTime(this.downDateIso()),
      downTime: this.downTime(),
      isDown: this.isDown(),
      resolutionExpectedDateTime: isoToLegacyDateTime(this.resolutionExpectedDateIso()),
      updatedDate: formatLegacyNow(),
      updatedBy: this.userId,
    };
    // Faithful gap (ctrl:111-143): resolutionExpectedTime is bound in the
    // form but never sent -- see class doc comment. It's tracked as its
    // own signal (never merged into `detail`), and explicitly stripped
    // here too in case the handed-off row already carried a stale value
    // for that key.
    delete payload['resolutionExpectedTime'];

    this.loader.start();
    this.api.saveCorrectiveMaintenance(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.router.navigate(['/correctiveMaintenance']);
          this.loader.stop();
          this.notification.success(res.message ?? '');
        } else if (res.status === 'error') {
          this.loader.stop();
          this.notification.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}

/**
 * Legacy parses `downDateTime`/`resolutionExpectedDateTime` with
 * `moment(value, "DD-MM-YYYY")` (ctrl:43,46) -- lenient parsing that
 * reads only the leading `DD-MM-YYYY` portion even when `value` also
 * carries a trailing time (e.g. the `DD-MM-YYYY HH:mm:ss` shape
 * `saveCorectiveMaintenance` writes back). Ported here as a plain
 * `yyyy-MM-dd` string for `<input type="date">` binding.
 */
function parseLegacyDateToIso(value: string | undefined | null): string {
  if (!value) return '';
  const match = /^(\d{2})-(\d{2})-(\d{4})/.exec(value);
  if (!match) return '';
  const [, dd, mm, yyyy] = match;
  return `${yyyy}-${mm}-${dd}`;
}

/**
 * Legacy's save-time format, `moment(dateObj).format('DD-MM-YYYY HH:mm:ss')`
 * (ctrl:116,118) -- `dateObj` always carries a midnight time-of-day (see
 * class doc comment), so the time component here is always `00:00:00`.
 */
function isoToLegacyDateTime(iso: string): string {
  if (!iso) return '';
  const [yyyy, mm, dd] = iso.split('-');
  if (!yyyy || !mm || !dd) return '';
  return `${dd}-${mm}-${yyyy} 00:00:00`;
}

/** Legacy: `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')` (ctrl:119). */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
