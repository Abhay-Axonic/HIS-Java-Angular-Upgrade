import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ModalityMasterService } from './modality-master.service';
import {
  EquipmentOption,
  ModalityFormRecord,
  ModalityListRow,
  SpecialityOption,
  SubSpecialityOption,
  emptyModality,
} from './modality-master.models';

/**
 * Parity rebuild of views/master/UnitMaster/Scheduling/modality-master.html
 * + resources/assets/js/controller/opd/unit/modalityMasterController.js
 * (`modalityMasterController`, `$scope`-style, no `controllerAs`). Route
 * `/modalityMaster`, moduleId 655 (app.js:10858-10883 resolve block,
 * confirmed live), no `requireSuperUser`.
 *
 * **Step 0 backend-existence check (this batch's headline risk, given the
 * sibling `modalityTypeMaster`'s confirmed-dead backend, entry 67):
 * CONFIRMED REAL.** All 9 endpoints this screen calls resolve to a real
 * Spring MVC Controller -> Service -> DAO chain, backed by a real JPA
 * entity/table (`ModalityMasterController.java`/`ModalityMasterServiceImpl.java`/
 * `ModalityMasterDaoImpl.java`, `m_modality_master`) plus 2 cross-module ADT
 * calls (`getActiveSpecialityList`/`getSubSpecialityBySpecialityId`) and one
 * Global-module equipment lookup (`GET /api/opd/equipments/{unitId}`, real
 * native-SQL query against `m_item`). This is the OPPOSITE finding from
 * `modalityTypeMaster` -- see docs/ANGULAR_PENDING_FINDINGS.md for the full
 * per-endpoint Controller/Service/DAO citations.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The stray literal TAB character embedded in legacy's equipments-URL
 *   string (`'\t/api/opd/equipments/'`, ctrl:100-101, confirmed via `od -c`)
 *   has zero effect on the actual network request -- the WHATWG URL
 *   parsing spec (implemented by every modern browser's XHR/fetch) strips
 *   ASCII tab/newline characters during parsing, so the byte that reaches
 *   the wire is identical either way. Not reproduced (there is no
 *   behavioral difference to preserve).
 * - `<modal>` (commonPopUpDirectives.js) is used for the "Are you sure you
 *   want to Save?" confirm dialog on CREATE only (`saveModality`,
 *   ctrl:260-315); `updateModality` (ctrl:397-442) saves immediately with
 *   no confirm step -- same asymmetry already documented for this
 *   codebase's speciality-master/city-master. The directive's own × close
 *   button (`ng-click="cancel();stopLoader();"`) calls `cancel()`, which
 *   `modalityMasterController.js` never defines (confirmed via grep) --
 *   the 13th+ confirmed instance of entry 55's shared-directive pattern.
 *   Per entry 55's standing policy, a working close button here is an
 *   accepted, disclosed standing improvement, not a parity violation.
 * - Both `saveModality` and `updateModality` are SILENT no-ops when the
 *   form is invalid (`$scope.modalityForm.$valid` guards both, ctrl:264,
 *   401 -- no `else` branch in either, confirmed by reading the full
 *   file) -- unlike this codebase's speciality-master, whose `update()`
 *   growls "Fill all mandatory fields." on invalid. Reproduced as two
 *   plain early-returns with no notification.
 * - On a FAILED create (duplicate code), legacy's confirm modal does not
 *   auto-close (`$scope.init()`, which is what resets `showModal`/
 *   `popUpFlag`, only runs in the success branch, ctrl:294-296) -- the
 *   user must explicitly dismiss it before trying again. Reproduced: the
 *   modal only auto-closes on success; failure leaves it open with the
 *   form data intact, alongside the error toast.
 * - Both list-load endpoints (`getModalityMasterList`/`getModalityCount`)
 *   are always called with `noOfRecordsPerPage: 10000` (ctrl:44-45,
 *   `initStateMasterList`) and the view has ZERO pager markup anywhere
 *   (confirmed by grep on modality-master.html) despite the controller
 *   building a full `$scope.pager`/`setPage`/`setNoOfRecords` machinery
 *   (ctrl:149-252) -- a dead pagination scaffold, same class as this
 *   codebase's bankMaster/ambulanceServiceProviderMaster findings (entries
 *   68/75). Reproduced by fetching one large page and rendering no pager
 *   UI at all, matching legacy's real, observable, pagination-free
 *   behavior.
 * - The visible search box's `ng-keyup="serachTestmaster(searchKeyword)"`
 *   (view:112) calls a function that does not exist anywhere in
 *   `modalityMasterController.js` -- it's only ever defined in an
 *   unrelated LIS screen's controller (`singleParamTestController.js`) and
 *   copy-pasted into 19+ other "Master" screens' search boxes across this
 *   codebase. The table's own `ng-repeat="...|filter:searchKeyword"`
 *   (view:197) is what actually performs the search -- independent of the
 *   broken keyup handler -- so the visible search behavior is real and
 *   working, only the redundant dead handler is not. Reproduced via a
 *   plain `computed()` client-side filter; the dead handler itself is not
 *   given a working implementation (same treatment as this codebase's GRN
 *   `PopulateReasonDropdown`, entry 5).
 * - The Edit (pencil) icon's `ng-class="{disabled: info.status=='I'}"`
 *   (view:209) only dims it visually via CSS -- `showUpdateBtn()` is still
 *   wired unconditionally and fires regardless of the row's status.
 *   Reproduced as a CSS-only visual class with the click handler always
 *   live, not a real disabled state.
 * - `$state` IS correctly injected in this controller's DI list (ctrl:21,
 *   26) and used correctly (ctrl:32-36) -- unlike several sibling
 *   OPD/master screens elsewhere in this codebase (entries 8/10/70), this
 *   screen does NOT have the recurring missing-`$state`-DI crash bug.
 *   Noted here as a confirmed negative, not applicable to the rebuild.
 */
@Component({
  selector: 'app-modality-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './modality-master.html',
  styleUrl: './modality-master.scss',
})
export class ModalityMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ModalityMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.modalityObj, ctrl:61-74) ----
  modality = signal<ModalityFormRecord>(emptyModality());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  specialityList = signal<SpecialityOption[]>([]);
  subSpecialityList = signal<SubSpecialityOption[]>([]);
  equipmentList = signal<EquipmentOption[]>([]);

  // Legacy's view only renders subSpeciality rows with isModality == 'Y'
  // (view:63, ng-if) -- the backend response itself is not pre-filtered.
  visibleSubSpecialityList = computed(() => this.subSpecialityList().filter((s) => s.isModality === 'Y'));

  // ---- list (mirrors $scope.modalityList -- no server-side pagination is
  // ever exercised, see class comment) ----
  modalityList = signal<ModalityListRow[]>([]);
  searchKeyword = signal('');

  // Approximates AngularJS's default `filter:searchKeyword` expression
  // filter over modalityList's visible textual columns (view:197-210).
  filteredList = computed(() => {
    const term = this.searchKeyword().trim().toLowerCase();
    if (!term) return this.modalityList();
    return this.modalityList().filter((row) =>
      [row.modalityCode, row.modalityDesc, row.specialityName, row.subSpecialityName, row.equipmentDesc].some(
        (field) => field?.toLowerCase().includes(term),
      ),
    );
  });

  constructor() {
    this.init();
  }

  // ---- mirrors $scope.init(), ctrl:29-114 ----
  private init(): void {
    this.api.getActiveSpecialityList(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.specialityList.set(res.listObject ?? []);
      },
    });
    this.api.getEquipments(this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.equipmentList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private loadList(): void {
    this.api.getModalityMasterList(this.organizationId, this.unitId, 0, 10000).subscribe({
      next: (res) => {
        this.modalityList.set(res.status === 'success' ? (res.listObject ?? []) : []);
      },
    });
    this.api.getModalityCount(this.organizationId, this.unitId).subscribe();
  }

  // ---- mirrors $scope.getSubSpeciality(specialityId), ctrl:119-141 ----
  onSpecialityChange(specialityId: string): void {
    this.updateField('specialityId', specialityId);
    if (!specialityId) {
      this.subSpecialityList.set([]);
      return;
    }
    this.api.getSubSpecialityBySpecialityId(this.unitId, this.organizationId, Number(specialityId)).subscribe({
      next: (res) => {
        if (res.status === 'success') this.subSpecialityList.set(res.listObject ?? []);
      },
    });
  }

  updateField<K extends keyof ModalityFormRecord>(field: K, value: ModalityFormRecord[K]): void {
    this.modality.update((m) => ({ ...m, [field]: value }));
  }

  // ---- required-field validation (mirrors modalityForm.$valid via the 5
  // `required` inputs -- modalityCode, modalityDesc, specialityId,
  // subSpecialityId, equipmentId, view:16/31/46/62/87) ----
  private requiredFieldsValid(): boolean {
    const m = this.modality();
    return !!(m.modalityCode.trim() && m.modalityDesc.trim() && m.specialityId && m.subSpecialityId && m.equipmentId);
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.modality().modalityCode.trim();
  }
  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.modality().modalityDesc.trim();
  }
  isSpecialityInvalid(): boolean {
    return this.formSubmitted() && !this.modality().specialityId;
  }
  isSubSpecialityInvalid(): boolean {
    return this.formSubmitted() && !this.modality().subSpecialityId;
  }
  isEquipmentInvalid(): boolean {
    return this.formSubmitted() && !this.modality().equipmentId;
  }

  // ---- save / confirm-modal (mirrors saveModality/PopupOkBtn, ctrl:260-315) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:264 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    const m = this.modality();
    const now = formatDateTime(new Date());
    this.api
      .saveModality({
        modalityId: 0,
        modalityCode: m.modalityCode,
        modalityDesc: m.modalityDesc,
        equipmentId: m.equipmentId,
        specialityId: m.specialityId,
        subSpecialityId: m.subSpecialityId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        createdBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Modality added Successfully...!!');
            this.showConfirmModal.set(false); // ctrl:294-296 -- init() closes the modal only on success
            this.resetForm();
          } else {
            // ctrl:297-304 -- modal deliberately stays open on failure, see class comment
            this.notify.error('Modality already exits...!!');
          }
          this.loadList(); // ctrl:305-309 -- reloads unconditionally, success or failure
        },
      });
  }

  // ---- update (mirrors updateModality, ctrl:397-442) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:401 guard (no else branch)
    const m = this.modality();
    const now = formatDateTime(new Date());
    this.api
      .updateModalityMaster({
        modalityId: m.modalityId,
        modalityCode: m.modalityCode,
        modalityDesc: m.modalityDesc,
        equipmentId: m.equipmentId,
        specialityId: m.specialityId,
        subSpecialityId: m.subSpecialityId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Modality updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Modality already exits...!!');
          }
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.modality.set(emptyModality());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.subSpecialityList.set([]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:318-351) ----
  edit(modalityId: number): void {
    this.api.getModalityById(modalityId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const d = res.listObject[0];
        this.modality.set({
          modalityId: d.modalityId,
          modalityCode: d.modalityCode,
          modalityDesc: d.modalityDesc,
          specialityId: d.specialityId.toString(),
          subSpecialityId: d.subSpecialityId.toString(),
          equipmentId: d.equipmentId.toString(),
        });
        this.onSpecialityChange(d.specialityId.toString());
        this.saveMode.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:354-394). Legacy's
  // checkbox flips `info.status` immediately via ng-model, BEFORE
  // ng-change/updateStatus() ever fires -- and the failure branch never
  // reverts it (unlike e.g. this codebase's asset-category-master/
  // speciality-master, which at least partially revert). Reproduced as an
  // unconditional optimistic flip with NO revert on failure, matching
  // legacy's real, more permissive behavior exactly. ----
  toggleStatus(row: ModalityListRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.modalityList.update((rows) =>
      rows.map((r) => (r.modalityId === row.modalityId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatusForModality({
        modalityId: row.modalityId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Modality Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!'); // no revert, see comment above
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:271-272/356-358/403-405. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
