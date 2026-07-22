import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { IcpcMapperService } from './icpc-mapper.service';
import { GenderOption, IcpcMapperGenderDetail, IcpcMapperRow, IcpcSystemOption } from './icpc-mapper.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/ICPC-mapper.html +
 * resources/assets/js/controller/master/global/iCPCMapperController.js
 * (state `ICPCMapper`, self-registered in that same controller file's own
 * `.config($stateProvider)` block -- NOT in the central app.js -- confirmed
 * no duplicate registration anywhere else in the tree). No `resolve` block
 * at all on the state, so this route has NO module gate in legacy, same as
 * `billingGroupMaster`/`periodicityService` (see docs/
 * ANGULAR_REBUILD_PLAYBOOK.md "Known caveats" #1) -- `data.moduleId` is
 * intentionally omitted at the route level, not an oversight.
 *
 * **CLINICAL-CONTENT VERDICT (owner-requested deep dive, batch 11): this
 * screen is pure org/unit-scoped REFERENCE/CONFIGURATION metadata, NOT
 * per-patient clinical data.** It maps an "ICPC System" (a coding-standard
 * variant, e.g. ICPC-1/ICPC-2 -- ICPC = International Classification of
 * Primary Care) to one or more Gender categories. Every field on every
 * request/response confirmed by full-chain source read (controller -> DTO
 * -> Controller.java -> ServiceImpl -> DaoImpl -> entity): `icpcSystemId`,
 * `genderId`, `icpcSystemMapperId`, `organizationId`, `unitId`, `status`,
 * audit fields. No `patientId`, no `encounterId`/`visitId`, no diagnosis
 * entry tied to any real patient anywhere in this screen's own code path.
 * (Contrast: the codebase does have a genuinely patient-linked ICPC usage
 * -- `com.param.opd.coversheet.model.Icpc`/`m_icpc`, the actual per-code
 * ICPC diagnosis master consumed by the OPD/IPD/nursing cover-sheet family
 * -- but that is a completely separate screen/controller, not touched by
 * either `ICPCMapper` or `icpcCodesConfiguration`.) A dead, unused
 * `$scope.oldDig = $scope.common.diagnosisId` line and an unbound
 * `selectDignosisIdForDescription` function exist in the legacy controller
 * but have no view binding and are never populated with real data -- dead
 * code, not a live diagnosis-linkage path. This confirms Phase 0's "no
 * clinical, no billing" tag for this screen is CORRECT (unlike
 * `doorToNeedle`/entry 66 or the entry 118 cluster).
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - **The shared `<modal visible="showModal">` directive's x close button
 *   crashes in legacy** (`common/commonPopUpDirectives.js:10` hardcodes
 *   `ng-click="cancel();stopLoader();"`, and `iCPCMapperController.js`
 *   never defines `$scope.cancel`) -- a NEW confirmed instance of the entry
 *   55 shared-modal-crash class (not one of the original 12, nor the batch
 *   6/9/10 instances already logged). Per the standing policy, NOT
 *   reproduced: the x button here is an ordinary, working
 *   `closeConfirmModal()`.
 *
 * - **The confirm modal is shown on EVERY Save, not gated behind a
 *   once-per-session flag.** Legacy's `$scope.popUpFlag` (ctrl:13) looks at
 *   first glance like the same "consumed once, never reset" shape as entry
 *   64's `employeeCategoryMaster`/`wingMaster` finding (`PopupOkBtn`,
 *   ctrl:188-191, permanently sets it `false`). Tracing the actual shared
 *   `<modal>` directive (`commonPopUpDirectives.js:38-60`) further shows
 *   its own `link` function resets `scope.$parent.popUpFlag = true`
 *   whenever `showModal` transitions back to `false` -- which happens via
 *   the DOM `hidden.bs.modal` handler firing on ANY modal close (Cancel's
 *   `data-dismiss="modal"`, or the x button's `data-dismiss="modal"`, which
 *   fires via Bootstrap's own listener independently of whether the
 *   `cancel()` expression it's paired with throws). In the normal workflow
 *   (open confirm -> click Save/Ok -> close the leftover dialog via Cancel
 *   or x), `popUpFlag` is back to `true` before the next Save click, so the
 *   modal reappears every time in practice -- it only stays permanently
 *   skipped in the unusual edge case where the dialog is left open,
 *   undismissed, across multiple Save clicks. This refines (does not
 *   overturn) entry 64: the "once-only" framing there was based on each
 *   controller's own code only, without tracing the shared directive's
 *   own reset path. Given the normal-workflow reading, and to stay
 *   consistent with this batch's own `qualificationMaster`/`degreeMaster`-
 *   style precedent (always show), this rebuild shows the confirm modal on
 *   every Save click. Worth a note back to whoever owns entry 64's
 *   eventual consistency decision.
 *
 * - **Validation happens once, before the confirm modal is shown** (not
 *   legacy's literal order, where the modal opens unconditionally on the
 *   first Save click and validation only runs on the second, post-confirm
 *   call to `save()`) -- matching the validate-then-confirm convention
 *   already established by every other master screen in this project with
 *   the same popUpFlag/confirm-modal shape (`employeeCategoryMaster`,
 *   `qualificationMaster`). The end result for a correctly-filled form is
 *   identical either way; this only changes when an invalid form's error
 *   growl appears (before vs. after an otherwise-pointless modal open).
 *
 * - **On edit, the pre-selected genders' labels come from this rebuild's
 *   own gender master-list lookup (by id), not from whatever legacy's
 *   `ng-dropdown-multiselect` third-party widget would have rendered.**
 *   Legacy's `showUpdateBtn` (ctrl:287-325) populates `selectedGenderList`
 *   with `{id: value1.genderId}` ONLY -- `newAr.label = value.genderDesc`
 *   is commented out (ctrl:312), so the widget's own selected-chip label is
 *   never set on edit. This rebuild's own checkbox-list widget always
 *   displays a gender's label by id-lookup against the already-loaded
 *   `genderOptions` list rather than from a per-selection stored label, so
 *   it never reproduces that blank-label artifact -- a minor, disclosed
 *   improvement in the same spirit as entry 55, not a business-logic
 *   change (the actual saved/selected gender IDs are identical either
 *   way).
 *
 * - **Status toggle: only updates the list signal on a successful
 *   response** (standard `<app-status-toggle>` convention, entry 12) --
 *   legacy's raw `ng-model` checkbox flips optimistically and never reverts
 *   on error; this rebuild's equivalent simply never shows the flip at all
 *   until the server confirms it, which is strictly safer and is the same
 *   disclosed, cross-cutting scaffold convention used by every other
 *   rebuilt screen, not a screen-specific decision.
 *
 * - **The FK the backend actually writes for the parent
 *   `t_icpc_code_configuration_mapper` row does not matter to this
 *   screen** -- that referential-integrity quirk lives entirely in
 *   `icpcCodesConfiguration`'s Java service layer (see that screen's class
 *   comment), not this one.
 */
@Component({
  selector: 'app-icpc-mapper',
  imports: [FormsModule, StatusToggle],
  templateUrl: './icpc-mapper.html',
  styleUrl: './icpc-mapper.scss',
})
export class IcpcMapper {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(IcpcMapperService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown/master data ----
  icpcSystemList = signal<IcpcSystemOption[]>([]);
  genderOptions = signal<GenderOption[]>([]);

  // ---- list + pagination state (mirrors initMasterList/setPage) ----
  list = signal<IcpcMapperRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: `ng-repeat="info in commonList | filter:searchKeyword"`
  // only ever filters the CURRENT PAGE's already-fetched rows (same
  // practical interpretation used for the identical pattern elsewhere in
  // this project, e.g. employeeCategoryMaster/asset-category-master).
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => r.icpcSystemDesc?.toLowerCase().includes(keyword));
  });

  // ---- form state (mirrors $scope.common) ----
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false);
  icpcSystemMapperId = signal<number | null>(null);
  selectedIcpcSystemId = signal<number | ''>('');
  private selectedGenderIds = signal<Set<number>>(new Set());

  selectedGenderLabels = computed(() => {
    const ids = this.selectedGenderIds();
    return this.genderOptions()
      .filter((g) => ids.has(g.id))
      .map((g) => g.label);
  });

  isGenderChecked = (id: number) => this.selectedGenderIds().has(id);

  showGenderDropdown = signal(false);

  // ---- confirm-save modal (see class comment: shown on every Save) ----
  showConfirmModal = signal(false);

  // ---- read-only "Gender Details" info popup (hand-rolled bootstrap
  // modal in legacy, `#fieldDetailsModal` -- NOT the shared `<modal>`
  // directive, so entry 55 does not apply to this one) ----
  showGenderDetailsModal = signal(false);
  genderDetailsForModal = signal<IcpcMapperGenderDetail[]>([]);

  constructor() {
    this.loadGenderOptions();
    this.loadIcpcSystemList();
    this.loadList();
  }

  private loadGenderOptions(): void {
    this.api.getActiveGenderList().subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.genderOptions.set((res.listObject ?? []).map((v) => ({ id: v.id, label: v.desc })));
        }
      },
    });
  }

  private loadIcpcSystemList(): void {
    this.api.getIcpcSystemList().subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.icpcSystemList.set(res.listObject ?? []);
        }
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  private reloadFirstPage(): void {
    this.currentPage.set(1);
    this.loadList();
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

  toggleGenderDropdown(): void {
    this.showGenderDropdown.update((v) => !v);
  }

  toggleGender(id: number): void {
    const next = new Set(this.selectedGenderIds());
    if (next.has(id)) next.delete(id);
    else next.add(id);
    this.selectedGenderIds.set(next);
  }

  isFieldInvalid(): boolean {
    return this.submitted() && !this.selectedIcpcSystemId();
  }

  isGenderSelectionInvalid(): boolean {
    return this.submitted() && this.selectedGenderIds().size === 0;
  }

  private validate(): boolean {
    if (!this.selectedIcpcSystemId()) {
      this.notify.error('Please Select ICPC System');
      return false;
    }
    if (this.selectedGenderIds().size === 0) {
      this.notify.error('Please Select Gender');
      return false;
    }
    return true;
  }

  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validate()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    if (this.saveMode()) this.create();
    else this.update();
  }

  private genderListPayload(): { id: number }[] {
    return [...this.selectedGenderIds()].map((id) => ({ id }));
  }

  private create(): void {
    const now = formatDmyHms(new Date());
    this.api
      .save({
        icpcSystemId: this.selectedIcpcSystemId() as number,
        updatedBy: this.userId,
        updatedDate: now,
        createdDate: now,
        createdBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        status: 'A',
        genderList: this.genderListPayload(),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.reloadFirstPage();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private update(): void {
    const id = this.icpcSystemMapperId();
    if (id == null) return;
    this.api
      .update({
        icpcSystemMapperId: id,
        icpcSystemId: this.selectedIcpcSystemId() as number,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
        organizationId: this.organizationId,
        unitId: this.unitId,
        status: 'A',
        genderList: this.genderListPayload(),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            // Legacy hardcodes this exact string on update success
            // (ctrl:373-376), unlike save()'s server-supplied message.
            this.notify.success('Updated Successfully');
            this.resetForm();
            this.reloadFirstPage();
          }
        },
      });
  }

  private resetForm(): void {
    this.saveMode.set(true);
    this.submitted.set(false);
    this.icpcSystemMapperId.set(null);
    this.selectedIcpcSystemId.set('');
    this.selectedGenderIds.set(new Set());
    this.showGenderDropdown.set(false);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:287-325) ----
  edit(row: IcpcMapperRow): void {
    this.saveMode.set(false);
    this.submitted.set(false);
    this.showGenderDropdown.set(false);
    this.api.getById(this.organizationId, row.icpcSystemMapperId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status !== 'success' || !detail) return;
        this.icpcSystemMapperId.set(detail.icpcSystemMapperId);
        this.selectedIcpcSystemId.set(detail.icpcSystemId);
        const ids = new Set((detail.genderList ?? []).map((g) => g.genderId));
        this.selectedGenderIds.set(ids);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:419-457 -- only updates
  // the list on a confirmed success; see class comment) ----
  toggleStatus(row: IcpcMapperRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateStatus({
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
        organizationId: this.organizationId,
        unitId: this.unitId,
        status: nextStatus,
        icpcSystemMapperId: row.icpcSystemMapperId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.update((rows) =>
              rows.map((r) => (r.icpcSystemMapperId === row.icpcSystemMapperId ? { ...r, status: nextStatus } : r)),
            );
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- "Gender Details" read-only info popup (mirrors
  // getGenderListByICPCSystemId, ctrl:390-410) ----
  viewGenderDetails(row: IcpcMapperRow): void {
    this.api.getGenderListByIcpcSystemId(this.organizationId, row.icpcSystemMapperId).subscribe({
      next: (res) => {
        this.genderDetailsForModal.set(res.status === 'success' ? res.listObject ?? [] : []);
        this.showGenderDetailsModal.set(true);
      },
    });
  }

  closeGenderDetailsModal(): void {
    this.showGenderDetailsModal.set(false);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:217-218/349-350/421-422. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
