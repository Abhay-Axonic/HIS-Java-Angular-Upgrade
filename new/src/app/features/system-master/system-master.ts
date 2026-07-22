import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { SystemMasterService } from './system-master.service';
import {
  EXAMINATION_TYPES,
  SystemGenderDetailRow,
  SystemMasterFormRecord,
  SystemMasterListRow,
  emptySystemMaster,
} from './system-master.models';

/**
 * Parity rebuild of views/master/GlobalMaster/system-master.html +
 * resources/assets/js/controller/opd/global/systemMasterController.js.
 * Route `/systemMaster`, moduleId 484 (app.js:14978-15004, no
 * `requireSuperUser` check in the resolve block). Confirmed real: `$state`
 * IS in this controller's DI list, and the full backend chain exists for
 * all 7 endpoints this screen calls (SystemMasterController.java,
 * `@RequestMapping("api/global")` -> SystemMasterServiceImpl ->
 * SystemMasterDaoImpl; the gender list additionally goes through
 * GenderMasterController, `@RequestMapping("/adt")`).
 *
 * **SEVERITY-1 FINDING, faithfully reproduced, not fixed (see
 * docs/ANGULAR_PENDING_FINDINGS.md, this batch): `saveSystem`/
 * `updateSystem`/`updateStatus` (ctrl:160-211/253-289/322-365) each call
 * `$rootScope.startLoader()` before their POST, but only call
 * `$rootScope.stopLoader()` INSIDE the `if (response.data.status ==
 * "success")` branch -- there is no `else` branch at all in any of the
 * three. On any non-success response (a real backend rejection, not just
 * a network error), the loader is never stopped, no error growl fires, and
 * `#preloader` (app.js:429-437, a full-page `show()`/`hide()` overlay
 * blocking the WHOLE app, not scoped to this screen) is left visible
 * indefinitely with no recovery path except a manual page reload. This is
 * a distinct, screen-specific instance of the same "silently swallow
 * every failure" shape already documented at the shared-service level in
 * entries 38/41 -- but here it recurs 3 separate times inside one
 * controller's own code, not just inside the generic `GenericService`
 * wrapper, so fixing entries 38/41 alone would NOT fix this screen.
 * Reproduced via `LoaderService.start()`/`.stop()` with `.stop()` called
 * ONLY in the success branch of `save()`/`update()`/`toggleStatus()` below
 * -- no `notify.error()` anywhere in those 3 flows, matching legacy's
 * total silence on failure.**
 *
 * **Other deliberate quirks preserved, not bugs in this rebuild:**
 * - Per entry 55's standing policy: legacy's shared `<modal>` directive
 *   (view:73, the Save/Update confirm dialog) has an x-close button
 *   calling a bare `cancel()` this controller's `$scope` never defines --
 *   a live, disclosed legacy crash. `systemMaster` is one more confirmed
 *   instance. This rebuild uses a plain working `closeConfirmModal()`
 *   instead of reproducing the crash, per the standing policy.
 * - Status-vocabulary drift vs. this batch's OTHER screen,
 *   `rejectionMaster`: that screen's active/inactive flag is a real
 *   boolean (`isActive`), this screen's is the string `'A'`/`'I'`
 *   (`status`, `char` on the Java side) -- same "Status(n)" concept,
 *   different wire representation, confirmed by reading each screen's own
 *   DTO independently (Step 1's sibling-divergence check).
 * - No server-side pagination at all (unlike `rejectionMaster`): the view
 *   has no "Show N entries" control, no pager, and `getListOfSystemMaster`
 *   takes no offset/page-size params -- the full active+inactive list
 *   loads in one call every time (ctrl:114-127), sorted/filtered entirely
 *   client-side (`systemList|orderBy:column:reverse|filter:searchKeyword`,
 *   view:138). `column`/`reverse` are never assigned anywhere in the
 *   controller or view (no `ng-click` on any header) -- dead, decorative,
 *   same as `rejectionMaster`'s dead sort carets. Reproduced by NOT adding
 *   pagination UI and filtering the full, unpaginated list client-side.
 * - The Gender multiselect's "required" marker (view:47-50) is fully
 *   decorative: it's a `<label ng-model="systemObj.genderIdList"
 *   required>`, not a real registered Angular form control, so
 *   `systemMasterForm.genderList` is always `undefined` and its error
 *   message can never display, nor does an empty gender selection block
 *   `systemMasterForm.$valid`. Reproduced by leaving gender selection out
 *   of `requiredFieldsValid()` entirely (only Code/Name/Type are
 *   enforced).
 * - `getActiveGenderList`'s success-status check (ctrl:88-93) is a
 *   missing-braces no-op: `if (status == "success") console.log(...)` is
 *   the ENTIRE if-body once comments are stripped, so the very next
 *   statement (`angular.forEach(response.data.listObject, ...)`, which
 *   actually builds `genderList`) runs UNCONDITIONALLY regardless of
 *   response status. Reproduced by always mapping the response's
 *   `listObject` into `genderOptions`, never gating it on `res.status`.
 * - `updateSystem`'s success branch calls `$scope.clearData()`
 *   (ctrl:361), a function this controller never defines -- throws inside
 *   the `.then()` callback, silently swallowed by `$q` (no `.catch`
 *   anywhere in the chain) since it fires AFTER `growl.success`/
 *   `$scope.init()` have already run. Zero observable effect either way;
 *   not reproduced (there is nothing for a TypeScript equivalent to do --
 *   an undefined-function call has no analogue once TypeScript's own
 *   compile-time checking is in play, and the crash it produces in
 *   legacy has no visible symptom to preserve).
 * - `saveSystem`/`updateSystem` both unconditionally set
 *   `systemObj.genderIdList = selectedGenderList` (ctrl:161/324) just
 *   before building the payload -- reproduced via
 *   `genderIdList: this.selectedGenderIds().map((id) => ({ id }))`,
 *   matching `SystemMasterDaoImpl`'s own `genderListObj.getId()`-only
 *   usage (the `label`/`genderDesc` fields are round-tripped in legacy but
 *   never read server-side either way).
 * - `showUpdateBtn`'s failure branch uses a raw `alert("Error!!")`
 *   (ctrl:246), not `growl` -- same convention already established
 *   elsewhere in this codebase. Reproduced verbatim via `window.alert`.
 */
@Component({
  selector: 'app-system-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './system-master.html',
  styleUrl: './system-master.scss',
})
export class SystemMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(SystemMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  examinationTypes = EXAMINATION_TYPES;

  // ---- form state (mirrors $scope.systemObj, ctrl:58-69) ----
  system = signal<SystemMasterFormRecord>(emptySystemMaster());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- gender multiselect (mirrors genderList/selectedGenderList, ctrl:72-101) ----
  genderOptions = signal<{ id: number; label: string }[]>([]);
  selectedGenderIds = signal<number[]>([]);

  // ---- read-only "Gender Details" popup (mirrors showFieldList, ctrl:292-320) ----
  genderDetailRows = signal<SystemGenderDetailRow[]>([]);
  showGenderDetailsModal = signal(false);

  // ---- list, no server pagination (mirrors systemList, ctrl:126) ----
  list = signal<SystemMasterListRow[]>([]);
  searchKeyword = signal('');

  // Full-dataset client-side filter (no pagination on this screen -- see class comment).
  filteredList = computed(() => {
    const kw = this.searchKeyword().trim().toLowerCase();
    if (!kw) return this.list();
    return this.list().filter(
      (row) => row.systemCode.toLowerCase().includes(kw) || row.systemName.toLowerCase().includes(kw),
    );
  });

  constructor() {
    this.loadGenderOptions();
    this.loadList();
  }

  private loadGenderOptions(): void {
    this.api.getActiveGenderList().subscribe({
      next: (res) => {
        // ctrl:88-93 missing-braces quirk -- always populate, not gated on res.status. See class comment.
        this.genderOptions.set((res.listObject ?? []).map((g) => ({ id: g.id, label: g.desc })));
      },
    });
  }

  private loadList(): void {
    this.api.getList(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  updateField<K extends keyof SystemMasterFormRecord>(field: K, value: SystemMasterFormRecord[K]): void {
    this.system.update((s) => ({ ...s, [field]: value }));
  }

  onGenderSelectionChange(select: HTMLSelectElement): void {
    this.selectedGenderIds.set(Array.from(select.selectedOptions).map((o) => Number(o.value)));
  }

  // ---- required-field validation (mirrors systemMasterForm.$valid via the
  // 3 real form controls -- systemCode, systemName, examination/typeId,
  // view:15-70; Gender's own marker is decorative, see class comment) ----
  private requiredFieldsValid(): boolean {
    const s = this.system();
    return !!(s.systemCode.trim() && s.systemName.trim() && s.typeId !== null);
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.system().systemCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.system().systemName.trim();
  }

  isTypeInvalid(): boolean {
    return this.formSubmitted() && this.system().typeId === null;
  }

  // ---- save / confirm-modal (mirrors saveSystem/PopupOkBtn, ctrl:154-211) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:172 guard
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const s = this.system();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .create(
        {
          systemCode: s.systemCode,
          systemName: s.systemName,
          typeId: s.typeId,
          status: 'A',
          createdBy: this.userId,
          updatedBy: this.userId,
          createdDate: now,
          updatedDate: now,
          genderIdList: this.selectedGenderIds().map((id) => ({ id })),
        },
        this.unitId,
        this.organizationId,
      )
      .subscribe({
        next: (res) => {
          // No else branch -- see class comment's severity-1 finding:
          // legacy never stops the loader or shows feedback on failure.
          if (res.status === 'success') {
            this.notify.success('System Master Added Successfully');
            this.loader.stop();
            this.resetForm();
            this.loadGenderOptions();
            this.loadList();
          }
        },
      });
  }

  // ---- update (mirrors updateSystem, ctrl:322-365) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const s = this.system();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .update(
        {
          systemId: s.systemId,
          systemCode: s.systemCode,
          systemName: s.systemName,
          typeId: s.typeId,
          updatedDate: now,
          genderIdList: this.selectedGenderIds().map((id) => ({ id })),
        },
        this.unitId,
        this.organizationId,
      )
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.loader.stop();
            this.notify.success('System master updated successfully');
            this.resetForm();
            this.loadGenderOptions();
            this.loadList();
          }
        },
      });
  }

  private resetForm(): void {
    this.system.set(emptySystemMaster());
    this.selectedGenderIds.set([]);
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:214-250) ----
  edit(systemId: number): void {
    this.api.getById(systemId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          const d = res.object;
          this.system.set({ systemId: d.systemId, systemCode: d.systemCode, systemName: d.systemName, typeId: d.typeId });
          this.selectedGenderIds.set((res.listObject ?? []).map((g) => g.id));
          this.saveMode.set(false);
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:253-289) -- no
  // revert-on-failure branch at all, see class comment ----
  toggleStatus(row: SystemMasterListRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.systemId === row.systemId ? { ...r, status: nextStatus } : r)));
    this.loader.start();
    this.api
      .updateStatus({
        systemId: row.systemId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('System Master Status Updated Successfully');
            this.loader.stop();
            // ctrl:286 -- legacy calls $scope.init() on success, which
            // fully resets the form AND reloads both gender options and
            // the system list fresh from the server (same init() every
            // write flow on this screen calls). Reproduced identically.
            this.resetForm();
            this.loadGenderOptions();
            this.loadList();
          }
        },
      });
  }

  // ---- read-only gender-details popup (mirrors showFieldList, ctrl:292-320) ----
  showFieldList(systemId: number): void {
    this.api.getGenderDetailsById(systemId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.genderDetailRows.set(res.listObject ?? []);
          this.showGenderDetailsModal.set(true);
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  closeGenderDetailsModal(): void {
    this.showGenderDetailsModal.set(false);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:179-181/257-259/336-338. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
