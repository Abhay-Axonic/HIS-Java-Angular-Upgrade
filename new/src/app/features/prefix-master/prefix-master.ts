import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PrefixMasterService } from './prefix-master.service';
import { GenderOption, PrefixForm, PrefixRow, emptyPrefixForm } from './prefix-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/GlobalMaster/prefix-master.html +
 * resources/assets/js/controller/master/prefixMasterController.js
 * (`prefixMasterController`, classic `$scope`, no `controllerAs`). State
 * `prefixMaster` self-registered at app.js:3949-3971 (`templateUrl:
 * 'views/master/GlobalMaster/prefix-master.html'`, `controller:
 * "prefixMasterController"`), moduleId 398 (resolve block, app.js:3958-3966,
 * confirmed live). Script-loaded at index.html:525, not orphaned.
 *
 * NOT a duplicate of this codebase's already-rebuilt `prefix-configuration`
 * feature: that screen ports `prefixConfigurationMasterController.js`, an
 * entirely different unit-level document-numbering-sequence generator
 * (screen/reset/format dropdowns, running-number/length config). This
 * screen is a plain Mr./Mrs./Dr.-style name-prefix + gender master with a
 * "show at patient registration" flag -- confirmed genuinely distinct by
 * reading both controllers in full: no shared bindings, no shared
 * endpoints, no shared DTOs.
 *
 * Backend confirmed real and live (not the entry-67/77 dead-backend
 * pattern): navigated the running dev Tomcat instance directly and captured
 * real 200 responses for `/adt/getActiveGenderList`, `/adt/getPrefixMasterList`,
 * and `/adt/getPrefixCount`, with one real seeded row rendering
 * (`PRE_01`/"Mr."/Male).
 *
 * Confirmed real, working CRUD: save (ctrl:200-259), update (ctrl:334-382),
 * edit-populate `showUpdateBtn` (ctrl:262-291), status toggle `updateStatus`
 * (ctrl:294-332) -- all wired to real endpoints, see prefix-master.service.ts.
 *
 * **Deliberate quirks/bugs preserved, not fixed:**
 * - The confirm modal (shared `<modal>` directive, prefix-master.html:92,
 *   wired via legacy's `PopupOkBtn`/`popUpFlag`) only gates CREATE
 *   (ctrl:219-222); `updatePrefix()` (ctrl:334-382) saves immediately with
 *   no confirm step -- same asymmetry as this codebase's amenity-master/
 *   ward-master. Reproduced via `save()`/`confirmSave()` vs. `update()`.
 * - **Copy-paste error-message bug, faithfully reproduced**: both
 *   `savePrefix()` (ctrl:245-252) and `updatePrefix()` (ctrl:370-377) show
 *   the SAME hardcoded `growl.error('Prefix already exits...!!')` on ANY
 *   non-success response, regardless of the real failure reason returned by
 *   the server -- same pattern as this codebase's `subSpecialityMaster`
 *   (entry 72) and `QMSkioskMaster` (entry 57) copy-paste message bugs.
 *   Reproduced verbatim rather than surfacing `res.message`.
 * - **Native `alert()` instead of growl, faithfully reproduced**:
 *   `showUpdateBtn`'s failure branch (ctrl:286-288) calls a blocking
 *   `alert("Error!!")` -- unlike every other error path in this same
 *   controller, which uses `growl.error`. Reproduced via `window.alert(...)`
 *   in `edit()`'s failure branch, not silently normalized to a toast.
 * - **Missing status guard on the edit action, a real sibling divergence**:
 *   unlike `gradeCreationMaster` (and this codebase's `amenity-master`),
 *   whose edit pencil is visually disabled and click-guarded for Inactive
 *   rows, `prefixMaster`'s edit icon (prefix-master.html:189) carries NO
 *   `ng-class="disabled"`/status check at all -- an Inactive prefix can
 *   still be opened for edit. Reproduced by NOT gating `edit()` on
 *   `row.status`, unlike the sibling screen rebuilt in this same batch.
 * - The confirm-modal's close button is wired to an ordinary working
 *   `closeConfirmModal()`, per docs/ANGULAR_PENDING_FINDINGS.md entry 55
 *   (standing policy) -- `prefixMasterController.js` never defines
 *   `$scope.cancel`, which the shared `<modal>` directive's template
 *   hardcodes a call to, so legacy's own close button crashes today. Not
 *   re-litigated here; a working close is the accepted standing
 *   improvement, not a parity gap.
 * - `isShowPatientRegistration` (view:60-64) carries a redundant, inert
 *   `ng-checked="isShowPatientRegistration"` alongside `ng-model` on the
 *   same checkbox -- copy-paste residue with no behavioral effect (`ng-model`
 *   is what actually governs the control). Modeled as a plain two-way-bound
 *   boolean here, no special handling needed.
 * - Legacy's own field-naming quirk: the description text field's `$scope`
 *   binding is named `prefix.prefixName` (view:32) though it holds the
 *   *description*, mapped to `prefixDesc` only when building the save/update
 *   payload -- see prefix-master.models.ts's `PrefixForm` doc comment.
 * - **No missing-`$state`-DI crash class bug** (docs/ANGULAR_PENDING_FINDINGS.md
 *   entries 8/10): `$state` IS in this controller's DI list (ctrl:21/25) and
 *   IS used correctly in the session guard (ctrl:30-34) -- confirmed clean,
 *   nothing to note beyond this comment per the playbook's instruction not
 *   to reproduce that bug class here.
 * - The search box (prefix-master.html:146-148, `ng-model="searchKeyword"`
 *   feeding `filter:searchKeyword` on the `ng-repeat`, view:179) is LIVE,
 *   client-side filtering -- unlike several recent sibling masters'
 *   confirmed-dead search boxes (entry 71 `mealPreferenceMaster`, the
 *   `serachTestmaster`-class copy-paste crash in `amenity-master`/
 *   `degree-master`/`qualification-master`). Reproduced as a genuinely
 *   working client-side filter here, not a dead/crashing stub.
 */
@Component({
  selector: 'app-prefix-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './prefix-master.html',
  styleUrl: './prefix-master.scss',
})
export class PrefixMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PrefixMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.prefix + $scope.isShowPatientRegistration) ----
  form = signal<PrefixForm>(emptyPrefixForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  genderList = signal<GenderOption[]>([]);

  // ---- list + server-side pagination (mirrors initPrefixMasterList/getPrefixMasterList/PagerService) ----
  list = signal<PrefixRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ctrl:179 -- `filter:searchKeyword` client-side filter, live (see class doc comment).
  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((row) =>
      [row.prefixCode, row.prefixDesc, row.genderName].some((v) => v?.toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadGenderList();
    this.loadList();
    this.loadCount();
  }

  private loadGenderList(): void {
    this.api.getActiveGenderList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.genderList.set(res.listObject ?? []);
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
  }

  private loadCount(): void {
    this.api.getCount(this.organizationId).subscribe({
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

  updateField<K extends keyof PrefixForm>(field: K, value: PrefixForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors prefixform.$valid via the 3
  // `required` inputs -- prefixCode/prefixName/genderId, view:17/32/46) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.prefixCode.trim() && f.prefixName.trim() && f.genderId.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().prefixCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().prefixName.trim();
  }

  isGenderInvalid(): boolean {
    return this.formSubmitted() && !this.form().genderId.trim();
  }

  // ---- save / confirm-modal (mirrors savePrefix/PopupOkBtn, ctrl:200-259) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:219 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        prefixCode: f.prefixCode,
        prefixDesc: f.prefixName,
        id: f.genderId,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
        isShowPatientRegistration: f.isShowPatientRegistration,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Prefix added successfully...!!');
            this.resetForm();
          } else {
            // ctrl:245-252 -- hardcoded message on ANY failure, faithfully
            // reproduced (see class doc comment), not res.message.
            this.notify.error('Prefix already exits...!!');
          }
          // ctrl:253 -- unlike some sibling masters, legacy refetches the
          // list on BOTH branches here, not only on success.
          this.loadList();
          this.loadCount();
        },
      });
  }

  // ---- update (mirrors updatePrefix, ctrl:334-382) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:351 guard (no else branch)
    const f = this.form();
    this.api
      .update({
        prefixCode: f.prefixCode,
        prefixDesc: f.prefixName,
        prefixId: f.prefixId as number,
        id: f.genderId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        isShowPatientRegistration: f.isShowPatientRegistration,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Prefix updated successfully...!!');
            this.resetForm();
          } else {
            // ctrl:370-377 -- same hardcoded message as save's failure
            // branch, faithfully reproduced (see class doc comment).
            this.notify.error('Prefix already exits...!!');
          }
          this.loadList();
          this.loadCount();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyPrefixForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    // ctrl:244/369 -- legacy calls $scope.init() (which re-fetches the
    // gender dropdown) on save/update success, in addition to
    // initPrefixMasterList(); reproduced by re-fetching here too.
    this.loadGenderList();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:262-291) ----
  // NOTE: unlike gradeCreationMaster (this same batch) and this codebase's
  // amenity-master, legacy places NO status guard on this action at all --
  // see class doc comment. Deliberately not gated on row.status here either.
  edit(prefixId: number): void {
    this.saveMode.set(false);
    this.api.getById(prefixId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          // ctrl:286-288 -- a blocking native alert, not growl.error, unlike
          // every other failure path in this controller. Faithfully
          // reproduced, see class doc comment.
          window.alert('Error!!');
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          prefixId: d.prefixId,
          prefixCode: d.prefixCode,
          prefixName: d.prefixDesc,
          genderId: String(d.id),
          isShowPatientRegistration: d.isShowPatientRegistration,
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:294-332) ----
  toggleStatus(row: PrefixRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // ctrl:294-332 -- growl only, no re-fetch of list/count on success, and
    // no revert on failure (the view's checkbox is two-way bound via
    // ng-model, so it has already visually flipped regardless of the
    // response); same pattern as this codebase's amenity-master.
    this.list.update((rows) => rows.map((r) => (r.prefixId === row.prefixId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        prefixId: row.prefixId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Prefix Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:201-203/296-298/337-339. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
