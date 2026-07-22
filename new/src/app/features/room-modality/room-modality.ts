import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RoomModalityService } from './room-modality.service';
import {
  ModalityOption,
  ModalityRoomOption,
  RoomModalityRecord,
  RoomModalityRow,
  SubSpecialityOption,
  emptyRoomModality,
} from './room-modality.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/bedManagement/room-modality.html +
 * resources/assets/js/controller/adt/unit/roomModalityController.js
 * (`roomModalityController`, no `controllerAs` -- classic `$scope`
 * pattern). Wired via app.js's central `$stateProvider`
 * (config/app.js:20824), url `/roomModality`, no `data`/`resolve` block at
 * all -- no moduleId/requireSuperUser gate in legacy, same shape as
 * `billingGroupMaster`/`periodicityService`.
 *
 * **Defensive `radiologySpecialityId` read (ctrl:46):** legacy does
 * `$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId`
 * with no null-check, a real crash if `cookieObjectAppConfig` is missing.
 * Read here via `SessionService.appConfig?.radiologySpecialityId ?? ''`
 * instead -- the same defensive-session-check pattern every other rebuilt
 * screen uses for the equivalent legacy DI/cookie-crash bug class (see
 * docs/ANGULAR_REBUILD_PLAYBOOK.md Step 1). This is a fixed field on the
 * form (`specialityId`), never user-editable, matching legacy exactly.
 *
 * **Save vs. Update is asymmetric**, matching room-master/ward-master's
 * identical `adt/unit/` sibling pattern: clicking Save (`saveBtnFlag`,
 * ctrl:166-205) validates the form, then -- because `popUpFlag` starts
 * `true` -- only opens the confirm modal; the actual POST only fires when
 * the modal's Ok button calls `PopupOkBtn()` (ctrl:161-164), which sets
 * `popUpFlag = false` and re-invokes `saveRoomModality()`. Clicking Update
 * (`updateBtnFlag`, ctrl:383-423) validates the same form and POSTs
 * immediately, with no confirm step. Reproduced below as `onSaveClick()`
 * (opens the modal) / `confirmSave()` (the modal's Ok, does the real POST)
 * / `onUpdateClick()` (POSTs directly).
 *
 * **Uses the shared `<modal>` directive in legacy** (view:160,
 * `<modal visible="showModal"></modal>`) -- per
 * `docs/ANGULAR_PENDING_FINDINGS.md` entry 55's standing policy, the
 * directive's hardcoded close button crashes in legacy today
 * (`roomModalityController.js` never defines `$scope.cancel`, confirmed via
 * full-file grep). `closeConfirmModal()` below is an ordinary, working
 * close -- an accepted standing improvement, not a parity violation.
 *
 * **3 validation messages reference a nonexistent `modalityForm.ward`
 * field in legacy** (view:15,32,48 -- a copy-paste leftover from a
 * ward-master screen this view was likely cloned from; there is no `ward`
 * field anywhere in this form). Per the standing validation-visibility
 * policy (playbook Step 3), these are bound here to the REAL fields each
 * message sits next to in the legacy view: "Room modality is required"
 * (Room select), "Select Sub Speciality" (Sub-Speciality select),
 * "Modality machine is required" (Modality select).
 *
 * **Search box is dead in legacy** (view:91-94 -- a plain `<input>` with no
 * `ng-model`/`ng-change` at all). Rendered here as a plain, non-functional
 * input matching legacy exactly -- not wired to `list`/any filter, on
 * purpose.
 *
 * **Status toggle does not reset the create/edit form**, matching
 * room-master.ts's identical decision for the identical legacy pattern:
 * `updateStatus` (ctrl:345-380) calls `$scope.init()` on success, which
 * (among other things) resets `roomModalityObj` -- but per room-master's
 * precedent for the same `adt/unit/` controller shape, the practical effect
 * (list reflecting the new status) is already captured by `list.update()`
 * below, so the form is deliberately left alone here too.
 */
@Component({
  selector: 'app-room-modality',
  imports: [FormsModule, StatusToggle],
  templateUrl: './room-modality.html',
  styleUrl: './room-modality.scss',
})
export class RoomModality {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RoomModalityService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;
  private readonly radiologySpecialityId: number | '' = this.session.appConfig?.radiologySpecialityId ?? '';

  // ---- lookups ----
  roomList = signal<ModalityRoomOption[]>([]);
  subSpecialityList = signal<SubSpecialityOption[]>([]);
  modalityList = signal<ModalityOption[]>([]);

  // ---- form state (mirrors $scope.roomModalityObj, ctrl:56-67) ----
  record = signal<RoomModalityRecord>(
    emptyRoomModality(this.unitId, this.organizationId, this.userId, this.radiologySpecialityId),
  );
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initRoomModalityList/PagerService) ----
  list = signal<RoomModalityRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.api.getActiveModalityRoom(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.roomList.set(res.listObject ?? []);
      },
    });
    this.loadSubSpecialityList();
    this.loadList();
  }

  private loadSubSpecialityList(): void {
    if (this.radiologySpecialityId === '') return;
    this.api.getSubSpecialityBySpecialityId(this.organizationId, this.unitId, this.radiologySpecialityId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.subSpecialityList.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getRoomModalityList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getRoomModalityCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof RoomModalityRecord>(field: K, value: RoomModalityRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ---- cascading Sub-Speciality -> Modality dropdown (mirrors
  // getModalityBySubSpeciality, ctrl:131-159) ----
  onSubSpecialityChange(subSpecialityId: number | ''): void {
    this.updateField('subSpecialityId', subSpecialityId);
    this.loadModalityList(subSpecialityId);
  }

  private loadModalityList(subSpecialityId: number | ''): void {
    if (subSpecialityId === '' || this.radiologySpecialityId === '') {
      this.modalityList.set([]);
      return;
    }
    this.api
      .getModalityBySubSpecialityId(this.organizationId, this.unitId, this.radiologySpecialityId, subSpecialityId)
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.modalityList.set(res.listObject ?? []);
        },
      });
  }

  // ---- validation (see class comment re: the 3 `modalityForm.ward`
  // messages bound to their real fields) ----
  isRoomMissing(): boolean {
    return this.formSubmitted() && !this.record().roomModalityId;
  }

  isSubSpecialityMissing(): boolean {
    return this.formSubmitted() && !this.record().subSpecialityId;
  }

  isModalityMissing(): boolean {
    return this.formSubmitted() && !this.record().modalityId;
  }

  private isRequiredMissing(): boolean {
    if (!this.formSubmitted()) return false;
    return this.isRoomMissing() || this.isSubSpecialityMissing() || this.isModalityMissing();
  }

  // ---- save flow: Save validates then opens the confirm modal; only the
  // modal's Ok actually POSTs (mirrors saveRoomModality()'s popUpFlag-gated
  // double call via PopupOkBtn, ctrl:161-205). ----
  onSaveClick(): void {
    this.formSubmitted.set(true);
    if (this.isRequiredMissing()) return;
    this.showConfirmModal.set(true);
  }

  /** Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy): the
   * shared `<modal>` directive's close button crashes in legacy (`cancel()`
   * is never defined on roomModalityController's $scope) -- an ordinary
   * working close here is an accepted standing improvement, not a parity
   * violation. */
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.api.saveRoomModalityMasterMaster(this.record()).subscribe({
      next: (res) => {
        // ctrl:182-201 -- resetForm (init()) only on success; list reload
        // runs either way.
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.resetForm();
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
        this.loadList();
      },
      error: () => this.notify.error('Error!'),
    });
  }

  // ---- update (mirrors updateRoomModality, ctrl:383-423) -- no confirm modal ----
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (this.isRequiredMissing()) return;
    const payload: RoomModalityRecord = {
      ...this.record(),
      unitId: this.unitId,
      organizationId: this.organizationId,
    };
    this.api.updateRoomModalityMaster(payload).subscribe({
      next: (res) => {
        // ctrl:397-417 -- same success-only-reset / reload-either-way shape
        // as saveRoomModality above.
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.resetForm();
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
        this.loadList();
      },
      error: () => this.notify.error('Error!'),
    });
  }

  private resetForm(): void {
    this.record.set(emptyRoomModality(this.unitId, this.organizationId, this.userId, this.radiologySpecialityId));
    this.modalityList.set([]);
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:321-342) ----
  edit(row: RoomModalityRow): void {
    this.saveMode.set(false);
    this.api.getRoomModalityById(row.roomModalityMasterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.record.set(detail);
          this.loadModalityList(detail.subSpecialityId);
        } else {
          // ctrl:337-339 -- legacy shows a raw browser alert() here; this
          // rebuild uses the same NotificationService every other screen's
          // error path uses instead, for consistency across the app.
          this.notify.error('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:345-380) ----
  toggleStatus(row: RoomModalityRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateRoomModalityMasterStatus({
        roomModalityMasterId: row.roomModalityMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            // Update via the list signal, not a direct mutation of `row` --
            // see docs/ANGULAR_PENDING_FINDINGS.md entry 12. Mirrors
            // ctrl:363's init() reload after a successful toggle (see class
            // comment re: why the form itself isn't reset here).
            this.list.update((rows) =>
              rows.map((r) => (r.roomModalityMasterId === row.roomModalityMasterId ? { ...r, status: nextStatus } : r)),
            );
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }
}

/** `moment(date).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:346-348/384-386. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
