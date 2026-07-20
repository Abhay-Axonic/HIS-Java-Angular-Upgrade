import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { PeriodicityServiceApi } from './periodicity-service.service';
import {
  AdmissionDetail,
  PatientSearchResult,
  PeriodicityServOrderEntry,
  PeriodicityServOrderListRow,
  PeriodicityServiceSearchResult,
} from './periodicity-service.models';

function emptyOrderEntry(): PeriodicityServOrderEntry {
  return {
    serviceName: '',
    fromDate: null,
    fromTime: '',
    toDate: null,
    toTime: '',
    visitTypeId: null,
  };
}

/**
 * Parity rebuild of views/Nursing/periodicity-Service.html +
 * resources/assets/js/controller/services/unit/periodicityServiceOrdersController.js.
 * Second pilot batch replacement for `SurgicalSiteInfection` (the
 * originally-picked clinical-only screen, found to be a non-functional
 * copy-pasted mockup during audit -- see the pending-findings doc). Only
 * the live "Periodicity Charges" tab is rebuilt; the "Consumable Charges"
 * tab is entirely commented out in legacy and never rendered.
 *
 * **Deliberate simplifications/deviations, not bugs in this rebuild:**
 * - The shared patient-search partial (`views/common/common-patient-search-opd.html`)
 *   is not rebuilt in full -- same precedent as the first pilot's
 *   `inputOutputCharts`, which used "a minimal inline implementation rather
 *   than the full shared search partials, explicitly out of scope for
 *   Phase 2's per-screen characterization." This rebuild wires the real
 *   `searchPatient` endpoint (POST /adt/patientSearchByMultipleCriteriaForIPD)
 *   with just UHID + Name fields rather than the partial's full 6-field form.
 * - The view's OWN second, duplicate patient-search typeahead
 *   (`vm.model.searchPatient`/`vm.model.selectPatient`) is not reproduced --
 *   confirmed dead in legacy (the field is permanently `ng-disabled="true"`,
 *   and `selectPatient` doesn't even exist as a live function, only a
 *   commented-out stub). Nothing of value is lost by omitting it.
 * - The Equipment sub-block and `getItemList` are not reproduced -- in
 *   legacy `equipmentHideShow` is never set `true` by any reachable code
 *   path, so that whole sub-feature is unreachable dead UI today.
 * - The `updateStatus` status-toggle action is not reproduced -- its only
 *   UI trigger (a checkbox) is commented out in the legacy view.
 * - `getActiveGenderList`/`getActiveIdentificationList` are still fired on
 *   load (matching legacy's real on-load network activity for golden-trace
 *   parity) even though this rebuild's minimal patient-search form has no
 *   Gender/Identification-Type fields to populate from them -- in legacy
 *   these two calls feed the full shared partial's dropdowns; here their
 *   results are intentionally unused, the same "real call, discarded
 *   result" shape already documented for `assigningPickerDispenser`'s
 *   `CurrencyService.GetCurrency()` call in this same pilot batch.
 * - `orderPeriodicityService`'s legacy per-row payload uses a global
 *   `Object.prototype.multidelete` patch (installed by an unrelated
 *   sibling controller file, loaded first purely by script-tag order) to
 *   strip UI-only fields before POSTing. Reproduced here as a local
 *   payload-building function instead, which is Safe Refactoring per
 *   RULES.md (the resulting request body is provably identical), not a
 *   behavior change -- the global-prototype-patch mechanism itself was
 *   explicitly flagged in the audit as something the rebuild should not
 *   reproduce architecturally.
 * - The masked `status` vs `tempPeriodicityServ.status` bug in legacy's
 *   `editPeriodicityServOrder` (assigns from an undeclared bare `status`
 *   global, silently corrected moments later when Add unconditionally
 *   re-sets status to 'A') has no observable effect in the normal flow, so
 *   nothing to preserve -- documented in the pending-findings doc for the
 *   record, not reproduced as a bug here since doing so would require
 *   deliberately reading `window.status`, which is not "parity," it's
 *   pointless fragility.
 */
@Component({
  selector: 'app-periodicity-service',
  imports: [FormsModule],
  templateUrl: './periodicity-service.html',
  styleUrl: './periodicity-service.scss',
})
export class PeriodicityService {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PeriodicityServiceApi);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  private readonly wardList: unknown[] = this.readWardList();

  // ---- patient search (minimal inline form, see class comment) ----
  uhidSearch = signal('');
  nameSearch = signal('');
  patientResults = signal<PatientSearchResult[]>([]);

  // ---- selected patient / admission (mirrors periodicityServ.*) ----
  selectedPatientName = signal('');
  selectedUhid = signal('');
  selectedWardName = signal('');
  selectedBedNumber = signal('');
  selectedAdmissionNumber = signal('');
  visitTypeId = signal<number | null>(null);
  private admissionId: number | undefined;

  // ---- order-entry form (mirrors self.model.periodicityServ) ----
  entry = signal<PeriodicityServOrderEntry>(emptyOrderEntry());
  editFlag = signal(false);
  private editIndex: number | null = null;

  serviceSearchTerm = signal('');
  serviceResults = signal<PeriodicityServiceSearchResult[]>([]);
  private readonly serviceSearch$ = new Subject<string>();

  // ---- order list (mirrors periodicityServOrdersList) ----
  orders = signal<PeriodicityServOrderListRow[]>([]);
  visibleOrders = computed(() => this.orders());

  constructor() {
    this.api.getActiveGenderList().subscribe({ next: () => {} });
    this.api.getActiveIdentificationList().subscribe({ next: () => {} });

    this.serviceSearch$
      .pipe(
        debounceTime(250),
        switchMap((term) => this.api.searchServices(this.visitTypeId(), this.unitId, term)),
      )
      .subscribe({ next: (res) => this.serviceResults.set(res.listObject ?? []) });
  }

  private readWardList(): unknown[] {
    try {
      const raw = sessionStorage.getItem('nursingStationsWardsList');
      const parsed = raw ? JSON.parse(raw) : [];
      if (!Array.isArray(parsed) || parsed.length === 0) {
        this.notify.warning('No ward mapped with selected nursing station !!');
      }
      return Array.isArray(parsed) ? parsed : [];
    } catch {
      return [];
    }
  }

  // ---- patient search (mirrors searchPatient) ----
  searchPatient(): void {
    if (this.wardList.length === 0) return;
    if (this.nameSearch().trim().length > 0 && this.nameSearch().trim().length < 3) {
      this.notify.warning('Please enter atleast 3 characters..!');
      return;
    }
    this.api
      .searchPatient({
        uhIdNumber: this.uhidSearch(),
        patientName: this.nameSearch(),
        patientType: '1',
        unitId: this.unitId,
        organizationId: this.organizationId,
        visitTypeId: null,
        wardList: this.wardList,
      })
      .subscribe({
        next: (res) => {
          if (!res.listObject) {
            this.notify.warning(res.message ?? 'No patients found');
            this.patientResults.set([]);
            return;
          }
          this.patientResults.set(res.listObject);
        },
        error: () => this.notify.error('Patient search failed'),
      });
  }

  // ---- patient selection (mirrors getPatientDetailsByIndex) ----
  selectPatient(patient: PatientSearchResult): void {
    this.selectedPatientName.set(patient.patientName);
    this.selectedUhid.set(patient.uhIdNumber);
    this.admissionId = patient.admissionId;
    this.orders.set([]);
    this.patientResults.set([]);

    if (!this.admissionId) {
      this.notify.warning('No Record Found..!!');
      return;
    }

    this.api.getAdmissionDetail(this.admissionId).subscribe({
      next: (res) => {
        const detail = res.object as AdmissionDetail | undefined;
        if (detail?.isFinalBill === 'Y') {
          this.notify.warning('Final bill is already generated for this admission..!!');
          this.clearAdmission();
          this.searchPatient();
          return;
        }
        if (detail) {
          this.selectedBedNumber.set(detail.bedNumber ?? '');
          this.selectedWardName.set(detail.wardName ?? '');
          this.selectedAdmissionNumber.set(detail.admissionNumber ?? '');
        }
      },
    });
    this.loadOrders();
  }

  private clearAdmission(): void {
    this.admissionId = undefined;
    this.selectedBedNumber.set('');
    this.selectedWardName.set('');
    this.selectedAdmissionNumber.set('');
  }

  private loadOrders(): void {
    if (this.admissionId == null) return;
    this.api.getPeriodicityServicesByAdmissionId(this.admissionId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error(String(res.status));
          return;
        }
        const rows = (res.listObject as Record<string, unknown>[] | undefined) ?? [];
        this.orders.set(rows.map((ele) => this.toOrderRow(ele)));
      },
    });
  }

  private toOrderRow(ele: Record<string, unknown>): PeriodicityServOrderListRow {
    const parseLegacyDate = (s: unknown): Date | null => {
      if (typeof s !== 'string' || !s) return null;
      const [d, m, y] = s.split('-');
      return new Date(`${y}-${m}-${d}`);
    };
    const fromDate = parseLegacyDate(ele['fromDate']);
    const toDate = parseLegacyDate(ele['toDate']);
    return {
      orderNo: ele['orderNo'] as number,
      orderDetailId: ele['orderDetailId'] as number,
      periodicityServiceOrderId: (ele['periodicityServiceOrderId'] as number) ?? null,
      serviceId: ele['serviceId'] as number,
      serviceName: ele['serviceName'] as string,
      equipmentName: (ele['equipmentName'] as string) ?? '',
      fromDate,
      fromDateString: fromDate ? formatDMY(fromDate) : '-',
      fromTime: (ele['fromTime'] as string) ?? '',
      toDate,
      toDateString: toDate ? formatDMY(toDate) : '-',
      toTime: (ele['toTime'] as string) ?? null,
      tatTimeName: (ele['tatTimeName'] as string) ?? '',
      serviceRendered: (ele['serviceRendered'] as number) ?? 0,
      status: (ele['status'] as string) ?? 'A',
      equipmentId: ele['equipmentId'] as number | undefined,
      productCategoryId: ele['productCategoryId'] as number | undefined,
      assetCategoryId: ele['assetCategoryId'] as number | undefined,
      wardId: ele['wardId'] as number | undefined,
      bedId: ele['bedId'] as number | undefined,
      admissionId: ele['admissionId'] as number | undefined,
    };
  }

  // ---- service typeahead (mirrors searchPServices/selectPService) ----
  onServiceSearchChange(term: string): void {
    this.serviceSearchTerm.set(term);
    this.entry.update((e) => ({ ...e, serviceId: undefined }));
    if (term.length >= 1) this.serviceSearch$.next(term);
  }

  selectService(item: PeriodicityServiceSearchResult): void {
    this.serviceSearchTerm.set(item.displayName);
    this.serviceResults.set([]);
    this.api.getRate(item.serviceId, this.organizationId, this.unitId, this.visitTypeId(), item.periodicityServiceConfigId).subscribe({
      next: (res) => {
        if (res.object == null) {
          this.notify.error('Configuration for service not available please add');
          this.entry.update((e) => ({ ...e, serviceName: '', serviceId: undefined }));
          this.serviceSearchTerm.set('');
          return;
        }
        this.entry.update((e) => ({
          ...e,
          serviceName: item.displayName,
          serviceId: item.serviceId,
          equipmentId: item.equipmentId,
          equipmentName: item.equipmentName,
          productCategoryId: item.productCategoryId,
          assetCategoryId: item.assetCategoryId,
          perServConfig: res.object,
          toDate: null,
          toTime: '',
        }));
      },
    });
  }

  // ---- date/time ----
  onFromDateChange(value: string): void {
    this.entry.update((e) => ({ ...e, fromDate: value ? new Date(value) : null }));
  }

  onToDateChange(value: string): void {
    this.entry.update((e) => ({ ...e, toDate: value ? new Date(value) : null }));
  }

  // ---- add/edit/remove order rows (mirrors addPeriodicityServOrder etc.) ----
  addOrUpdateEntry(): void {
    const e = this.entry();
    if (!e.serviceId) {
      this.notify.error('Please select service');
      return;
    }
    if (!e.fromDate) {
      this.notify.error('Please select from time');
      return;
    }
    if (e.toDate && !e.toTime) {
      this.notify.error('Please select to time');
      return;
    }
    if (e.toDate) {
      const sdt = `${formatYMD(e.fromDate)} ${e.fromTime}`;
      const tdt = `${formatYMD(e.toDate)} ${e.toTime}`;
      if (sdt > tdt) {
        this.notify.warning('To date and time should be greater then start date and time.');
        return;
      }
    }

    const row: PeriodicityServOrderListRow = {
      orderNo: this.orders().length + 1,
      orderDetailId: 0,
      periodicityServiceOrderId: null,
      serviceId: e.serviceId,
      serviceName: e.serviceName,
      equipmentName: e.equipmentName ?? '',
      fromDate: e.fromDate,
      fromDateString: formatDMY(e.fromDate),
      fromTime: e.fromTime,
      toDate: e.toDate,
      toDateString: e.toDate ? formatDMY(e.toDate) : '-',
      toTime: e.toDate ? e.toTime : null,
      tatTimeName: '',
      serviceRendered: 0,
      status: 'A',
      equipmentId: e.equipmentId,
      productCategoryId: e.productCategoryId,
      assetCategoryId: e.assetCategoryId,
    };

    if (this.editIndex != null) {
      this.orders.update((list) => list.map((r, i) => (i === this.editIndex ? row : r)));
    } else {
      this.orders.update((list) => [...list, row]);
    }
    this.resetEntry();
  }

  private resetEntry(): void {
    this.entry.set(emptyOrderEntry());
    this.serviceSearchTerm.set('');
    this.editFlag.set(false);
    this.editIndex = null;
  }

  editOrder(index: number): void {
    const row = this.orders()[index];
    if (!row) return;
    this.entry.set({
      serviceName: row.serviceName,
      serviceId: row.serviceId,
      equipmentId: row.equipmentId,
      equipmentName: row.equipmentName,
      productCategoryId: row.productCategoryId,
      assetCategoryId: row.assetCategoryId,
      fromDate: row.fromDate,
      fromTime: row.fromTime,
      toDate: row.toDate,
      toTime: row.toTime ?? '',
      visitTypeId: this.visitTypeId(),
    });
    this.serviceSearchTerm.set(row.serviceName);
    this.editFlag.set(true);
    this.editIndex = index;
  }

  removeOrder(index: number): void {
    this.orders.update((list) => list.filter((_, i) => i !== index));
  }

  // ---- save (mirrors orderPeriodicityService) ----
  save(): void {
    const pending = this.orders().filter(
      (r) => (r.serviceRendered === 0 || r.periodicityServiceOrderId == null) && r.status === 'A',
    );
    if (!pending.length) {
      this.notify.warning('Please add at least one order.');
      return;
    }
    const payload = pending.map((r) => ({
      serviceId: r.serviceId,
      fromDate: r.fromDate ? formatDMY(r.fromDate) : '',
      fromTime: r.fromTime,
      toDate: r.toDate ? formatDMY(r.toDate) : undefined,
      toTime: r.toDate ? r.toTime : undefined,
      equipmentId: r.equipmentId,
      productCategoryId: r.productCategoryId,
      assetCategoryId: r.assetCategoryId,
      admissionId: this.admissionId,
      unitId: this.unitId,
      organizationId: this.organizationId,
      noOfUnit: 0,
    }));

    this.api.orderPeriodicityService(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Saved successfully');
          this.loadOrders();
        }
      },
      error: () => this.notify.error('Save failed'),
    });
  }
}

function formatDMY(d: Date): string {
  return `${String(d.getDate()).padStart(2, '0')}-${String(d.getMonth() + 1).padStart(2, '0')}-${d.getFullYear()}`;
}

function formatYMD(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}
