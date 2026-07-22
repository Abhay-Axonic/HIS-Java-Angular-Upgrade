import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { SystemObservationMasterService } from './system-observation-master.service';
import {
  EXAMINATION_TYPES,
  PropertyDetail,
  PropertyDraft,
  RawSpecialityOption,
  SaveSystemObservationPayload,
  SpecialityDetail,
  SystemObservationFlatRow,
  SystemObservationRow,
  SystemOption,
} from './system-observation-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/GlobalMaster/system-observation-master.html +
 * resources/assets/js/controller/opd/global/systemObservationMasterController.js
 * (state `systemObservationMaster`, registered centrally in
 * `resources/assets/js/config/app.js:15006`, module id 485, no
 * `requireSuperUser` check -- confirmed by reading the state's own
 * `resolve` block directly: it filters `permissions.modules` for id 485
 * and calls `$state.go("accessDenied")` on failure, with no `isSuperUser`
 * check anywhere in the block).
 *
 * **Real, confirmed finding: the entire "edit an existing observation" code
 * path is dead, unreachable code in legacy.** `showUpdateBtn()`
 * (ctrl:405-449, populates `$scope.observationObj` from a row and flips
 * `updateBtnFlag`/`saveBtnFlag`) is never called from anywhere in the view
 * -- confirmed by grep across `system-observation-master.html`: no
 * `ng-click="showUpdateBtn(...)"` exists anywhere, so `updateBtnFlag` stays
 * `false` forever and the `ng-if="updateBtnFlag"` Update button, its
 * +/- property buttons (`addUpdate`/`removeUpdate`), and
 * `updateSystemObservation()` itself never render/run for any user. This
 * is fortunate, because `updateSystemObservation()`'s body is also
 * independently broken copy-paste residue from a DIFFERENT screen
 * (`modalityMasterController`, per its own doc comment, ctrl:1-7): it
 * reads/writes `$scope.modalityObj` (never defined anywhere in this
 * controller -- only `$scope.observationObj` is used), POSTs to
 * `UPDATEMODALITYMASTER` (the wrong endpoint -- Modality Master's, not
 * System Observation's), and calls `$scope.initStateMasterList` (also
 * never defined). Calling it would throw
 * `TypeError: Cannot set properties of undefined (reading 'updatedDate')`
 * immediately. **Net effect for parity: this screen has no way to edit an
 * existing observation's code/name/system/speciality once saved** (only
 * the two status toggles below, and the property list's create-time
 * add/remove) -- this rebuild deliberately does not implement an edit
 * flow, matching legacy's real, if surprising, behavior exactly. Not
 * fixed/improved here per the playbook's no-silent-improvement rule.
 *
 * **Shared `<modal>` directive close-button crash (entry 55 policy
 * applies).** The view's `<modal visible="showModal"></modal>` (view:316)
 * is the same shared, prototypal-scope directive from entry 55 --
 * confirmed no `cancel()` is defined anywhere in this controller (grep),
 * so legacy's own × close button throws `cancel is not a function` on
 * click today. Per the standing policy, this rebuild's confirm-dialog
 * close button works correctly by default and is NOT retrofitted to
 * reproduce the crash.
 *
 * **Multi-row-per-observation flattening.** The backing named query
 * (`GET_LIST_SYSTEM_OBSERVATION_PROPERTY`) LEFT OUTER JOINs the property
 * table, so the raw response has one row per (observation, property) pair
 * -- see system-observation-master.models.ts for the full explanation.
 * This rebuild mirrors ctrl:149-161's own dedup-for-the-table /
 * keep-the-flat-list-for-the-popup pattern exactly.
 *
 * **Preserved quirks, not bugs introduced by this rebuild:**
 * - The "Type" dropdown (`examinationList`) is a static, hardcoded 3-option
 *   list (Both/General/Systemic Examination), not fetched from any
 *   backend endpoint (ctrl:93-107) -- it exists only to drive the
 *   `getListOfSystemMasterByType` filter on the "System" dropdown, and is
 *   never itself part of the save payload.
 * - The observation list is filtered by `organizationId` only -- the
 *   `unitId` filter is commented out server-side, so this list is
 *   genuinely org-wide, not unit-scoped (see models.ts).
 * - Speciality selection is required on save (`growl.error` if empty,
 *   ctrl:365-370); Code/Name/System/Type all use HTML5 `required` only
 *   (single validation layer, no duplication to preserve).
 * - Save's confirm-then-save flow (`showModal`/`popUpFlag`, ctrl:338-346)
 *   is reproduced: clicking Save opens a confirm dialog; confirming it
 *   performs the actual POST.
 */
@Component({
  selector: 'app-system-observation-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './system-observation-master.html',
  styleUrl: './system-observation-master.scss',
})
export class SystemObservationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(SystemObservationMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  examinationTypes = EXAMINATION_TYPES;

  // ---- master data ----
  private flatList = signal<SystemObservationFlatRow[]>([]);
  systemList = signal<SystemOption[]>([]);

  // Full, UNFILTERED speciality response -- the "Specialities" info popup
  // (viewSpecialityDetails) looks names up against this, matching
  // legacy's own $scope.specialityMasterList (ctrl:173/259-277). The
  // multiselect dropdown's own options are a separate, clinical-only
  // filtered derivation, matching legacy's separate $scope.specialityList
  // (ctrl:180-187).
  private specialityMasterListRaw = signal<RawSpecialityOption[]>([]);
  specialityDropdownOptions = computed(() =>
    this.specialityMasterListRaw()
      .filter((v) => v.isClinicalSpeciality === 'Y')
      .map((v) => ({ id: v.specialityId, label: v.specialityName })),
  );

  // ---- deduplicated table rows (one per observationId, mirrors
  // ctrl:149-157's systemObservationListTempaorary) ----
  private dedupedRows = computed<SystemObservationRow[]>(() => {
    const seen = new Set<number>();
    const rows: SystemObservationRow[] = [];
    for (const r of this.flatList()) {
      if (seen.has(r.observationId)) continue;
      seen.add(r.observationId);
      rows.push({
        observationId: r.observationId,
        observationName: r.observationName,
        systemId: r.systemId,
        systemName: r.systemName,
        observationCode: r.observationCode,
        isPropertyRequired: r.isPropertyRequired,
        specialityIds: r.specialityIds,
        status: r.status,
      });
    }
    return rows;
  });

  // ---- client-side pagination over the full deduplicated list, matching
  // legacy's own PaginationService (fetch-all + client paginate) ----
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  private filteredRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.dedupedRows();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.systemName?.toLowerCase().includes(keyword) ||
        r.observationCode?.toLowerCase().includes(keyword) ||
        r.observationName?.toLowerCase().includes(keyword),
    );
  });

  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredRows().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  displayedRows = computed(() => {
    const size = this.pageSize();
    const offset = (this.currentPage() - 1) * size;
    return this.filteredRows().slice(offset, offset + size);
  });

  // ---- create-mode form state (mirrors $scope.observationObj) ----
  typeId = signal<number | ''>('');
  systemId = signal<number | ''>('');
  observationCode = signal('');
  observationName = signal('');
  isPropertyRequired = signal<'Y' | 'N'>('N');
  private selectedSpecialityIds = signal<Set<number>>(new Set());
  showSpecialityDropdown = signal(false);
  propertyDrafts = signal<PropertyDraft[]>([{ propertyName: '' }]);
  submitted = signal(false);

  selectedSpecialityLabels = computed(() => {
    const ids = this.selectedSpecialityIds();
    return this.specialityDropdownOptions()
      .filter((s) => ids.has(s.id))
      .map((s) => s.label);
  });
  isSpecialityChecked = (id: number) => this.selectedSpecialityIds().has(id);

  // ---- confirm-before-save modal (mirrors showModal/popUpFlag, ctrl:338-346) ----
  showConfirmModal = signal(false);

  // ---- "Property Details" info popup (mirrors displayNote, ctrl:234-257) ----
  showPropertyModal = signal(false);
  propertyDetailsForModal = signal<PropertyDetail[]>([]);

  // ---- "Specialities" info popup (mirrors displaySpecialities, ctrl:259-277) ----
  showSpecialityModal = signal(false);
  specialityDetailsForModal = signal<SpecialityDetail[]>([]);

  constructor() {
    this.loadObservationList();
    this.loadSpecialityMasterList();
  }

  private loadObservationList(): void {
    this.api.getListOfSystemObservationProperty(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.flatList.set(res.listObject ?? []);
        }
      },
    });
  }

  private loadSpecialityMasterList(): void {
    this.api.getActiveSpecialityList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.specialityMasterListRaw.set(res.listObject ?? []);
        }
      },
    });
  }

  onTypeChange(typeId: number | ''): void {
    this.typeId.set(typeId);
    this.systemId.set('');
    this.systemList.set([]);
    if (typeId === '') return;
    this.api.getSystemListByType(this.organizationId, this.unitId, typeId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.systemList.set(res.listObject ?? []);
        }
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  toggleSpecialityDropdown(): void {
    this.showSpecialityDropdown.update((v) => !v);
  }

  toggleSpeciality(id: number): void {
    const next = new Set(this.selectedSpecialityIds());
    if (next.has(id)) next.delete(id);
    else next.add(id);
    this.selectedSpecialityIds.set(next);
  }

  addPropertyDraft(): void {
    this.propertyDrafts.update((list) => [...list, { propertyName: '' }]);
  }

  removePropertyDraft(index: number): void {
    // Legacy quirk preserved exactly (ctrl:292-306): a no-op when exactly
    // one row remains, so the last property row can never be removed.
    if (this.propertyDrafts().length === 1) return;
    this.propertyDrafts.update((list) => list.filter((_, i) => i !== index));
  }

  updatePropertyDraft(index: number, value: string): void {
    this.propertyDrafts.update((list) => list.map((p, i) => (i === index ? { propertyName: value } : p)));
  }

  /** Mirrors saveSystemObservation()'s first click (ctrl:338-346): opens the
   * confirm dialog rather than saving directly. */
  requestSave(): void {
    this.submitted.set(true);
    if (!this.typeId() || !this.systemId() || !this.observationCode().trim() || !this.observationName().trim()) {
      return;
    }
    this.showConfirmModal.set(true);
  }

  /** Mirrors PopupOkBtn() -> saveSystemObservation()'s second pass
   * (ctrl:196-200/338-399): the actual POST. */
  confirmSave(): void {
    this.showConfirmModal.set(false);

    if (this.selectedSpecialityIds().size === 0) {
      this.notify.error('Please select speciality..!!');
      return;
    }

    const now = formatDmyHms(new Date());
    const payload: SaveSystemObservationPayload = {
      systemId: this.systemId(),
      observationCode: this.observationCode(),
      observationName: this.observationName(),
      isPropertyRequired: this.isPropertyRequired(),
      specialityIds: [...this.selectedSpecialityIds()].join(','),
      organizationId: this.organizationId,
      unitId: this.unitId,
      updatedBy: this.userId,
      createdBy: this.userId,
      status: 'A',
      createdDate: now,
      updatedDate: now,
      listSystemPropertyDto: this.isPropertyRequired() === 'Y' ? this.propertyDrafts() : [],
    };

    this.api.saveSystemObservationProperty(payload, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.resetForm();
          this.loadObservationList();
        }
      },
    });
  }

  cancelConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  private resetForm(): void {
    this.typeId.set('');
    this.systemId.set('');
    this.systemList.set([]);
    this.observationCode.set('');
    this.observationName.set('');
    this.isPropertyRequired.set('N');
    this.selectedSpecialityIds.set(new Set());
    this.showSpecialityDropdown.set(false);
    this.propertyDrafts.set([{ propertyName: '' }]);
    this.submitted.set(false);
  }

  // ---- status toggle: observation-level (mirrors updateStatus, ctrl:452-481) ----
  toggleObservationStatus(row: SystemObservationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateSystemObservationPropertyStatus({
        observationId: row.observationId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.loadObservationList();
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- status toggle: property-level, inside the "Property Details"
  // modal (mirrors updateStatusObservationProperty, ctrl:483-518) ----
  togglePropertyStatus(detail: PropertyDetail): void {
    if (detail.propertyId == null) return;
    const nextStatus: 'A' | 'I' = detail.status === 'A' ? 'I' : 'A';
    this.api
      .updateSystemObservationSinglePropertyStatus({
        observationId: detail.observationId,
        propertyId: detail.propertyId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.propertyDetailsForModal.update((list) =>
              list.map((d) => (d.propertyId === detail.propertyId ? { ...d, status: nextStatus } : d)),
            );
            this.loadObservationList();
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- "Property Details" info popup (mirrors displayNote, ctrl:234-257) ----
  viewPropertyDetails(row: SystemObservationRow): void {
    const details: PropertyDetail[] =
      row.isPropertyRequired === 'Y'
        ? this.flatList()
            .filter((r) => r.observationId === row.observationId)
            .map((r) => ({
              propertyId: r.propertyId,
              observationId: r.observationId,
              propertyName: r.propertyName,
              status: r.propertyStatus,
            }))
        : [];
    this.propertyDetailsForModal.set(details);
    this.showPropertyModal.set(true);
  }

  closePropertyModal(): void {
    this.showPropertyModal.set(false);
  }

  // ---- "Specialities" info popup (mirrors displaySpecialities, ctrl:259-277) ----
  viewSpecialityDetails(row: SystemObservationRow): void {
    const ids = (row.specialityIds ?? '').split(',').filter(Boolean).map((v) => parseInt(v, 10));
    // Looks up against the FULL, unfiltered speciality response -- matches
    // legacy's displaySpecialities() reading $scope.specialityMasterList,
    // not the clinical-only-filtered dropdown options. See class comment.
    const master = this.specialityMasterListRaw();
    const details: SpecialityDetail[] = ids
      .map((id) => {
        const match = master.find((s) => s.specialityId === id);
        return match ? { id: match.specialityId, specialityName: match.specialityName } : null;
      })
      .filter((d): d is SpecialityDetail => d !== null);
    this.specialityDetailsForModal.set(details);
    this.showSpecialityModal.set(true);
  }

  closeSpecialityModal(): void {
    this.showSpecialityModal.set(false);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:349-351. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
