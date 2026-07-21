import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ModalityTypeMasterService } from './modality-type-master.service';
import { ModalityTypeObj, ModalityTypeRow, SpecialityOption, emptyModalityTypeObj } from './modality-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/Scheduling/modality-type-master.html +
 * resources/assets/js/controller/opd/unit/modalityTypeMasterController.js
 * (`modalityTypeMasterController`, no `controllerAs`). Legacy state
 * `modalityTypeMaster` (app.js:10831-10857), moduleId 654 -- gated at the
 * ROUTE level via `moduleAccessGuard` (`data: { moduleId: 654 }`), not in
 * this component, matching the convention established by
 * ambulance-master/ambulance-type-master.
 *
 * Confirmed real, working CRUD: save (ctrl:222-277), update (ctrl:354-400),
 * edit-populate `showUpdateBtn` (ctrl:280-309), status toggle `updateStatus`
 * (ctrl:312-351) -- all wired to real endpoints, see
 * modality-type-master.service.ts.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - Save opens a real confirm modal ("Are you sure you want to Save?", the
 *   shared `<modal>` directive / `PopupOkBtn`/`popUpFlag` dance, ctrl:222-277)
 *   before the create POST fires; `update()` (ctrl:354-400) posts
 *   immediately, no confirm step -- same asymmetry as every other rebuilt
 *   sibling in this codebase.
 * - Legacy's own `saveBtnFlag`/`updateBtnFlag` are the INVERSE of most
 *   sibling screens (`ng-hide`, not `ng-if` -- `saveBtnFlag=false` means
 *   Save IS shown, view:82/85). The net observable UI state is identical to
 *   every other rebuilt master (Save visible in create mode, Update visible
 *   in edit mode), so this is modeled with the same `saveMode` boolean
 *   convention used elsewhere (`true` = create/Save shown), not with
 *   legacy's inverted flag polarity.
 * - Both `save()`/`update()` only ever check `modalityTypeForm.$valid` with
 *   no `else` branch -- an invalid submit is a silent no-op. Reproduced via
 *   an early `return` in both `save()` and `update()`.
 * - On BOTH success and an app-level error response, save/update
 *   unconditionally reload the list+count (ctrl:267-271/391-395, calling
 *   `initStateMasterList` outside the if/else) -- only the SUCCESS branch
 *   additionally resets the form and re-fetches the specialty dropdown
 *   (`$scope.init()`, ctrl:29-100). Reproduced: `loadListAndCount()` always
 *   runs after the response; `resetFormAndSpecialities()` only runs on
 *   success.
 * - `showUpdateBtn` (ctrl:280-309) only ever overwrites `modalityTypeId`/
 *   `modalityTypeCode`/`modalityTypeDesc`/`specialityId` on the shared
 *   form/payload object -- `organizationId`/`unitId`/`createdBy`/
 *   `updatedBy`/`status` are left exactly as `init()` set them. Net effect,
 *   faithfully reproduced: every `updateModalityType` POST resends the
 *   CURRENT session's `userId` as both `createdBy` and `updatedBy`, not the
 *   row's real original creator (see `ModalityTypeObj`'s doc comment in
 *   modality-type-master.models.ts). Not "fixed" to re-stamp only
 *   `updatedBy`.
 * - `showUpdateBtn`'s failure branch uses a raw `window.alert("Error!!")`,
 *   not `growl` -- same convention already established in
 *   `ambulance-master.ts`'s `edit()`. Reproduced verbatim.
 * - The edit pencil icon has NO disabled-when-inactive guard (unlike
 *   `amenity-master`) -- inactive rows remain editable here, matching
 *   legacy exactly (view:164 has no `ng-class`/disabled anywhere on that
 *   icon).
 * - The per-column sort caret icons (view:97-152, every header) carry no
 *   `ng-click` anywhere in the view or controller -- confirmed dead UI,
 *   same as this codebase's amenity-master. Omitted entirely here, matching
 *   that established convention.
 * - The Specialty dropdown's placeholder option renders here as
 *   `<option value="">Select Specialty</option>`, matching the established
 *   "empty value" placeholder convention used by every other rebuilt FK
 *   dropdown in this codebase (e.g. qms-department-master's Kiosk select).
 *   Note for the record: legacy's own placeholder option
 *   (modality-type-master.html:47, `<option>Select Specialty</option>`) has
 *   NO `value` attribute at all, so a browser defaults its value to its own
 *   label text ("Select Specialty") -- a non-empty string that would pass
 *   AngularJS's `required` validator if a user ever manually re-selected it
 *   after already choosing a real specialty. This is a narrow,
 *   previously-unflagged edge case (not one of this ticket's enumerated
 *   parity decisions, and immaterial for the realistic default-empty-state
 *   flow, which still correctly blocks Save); simplified to the safe
 *   empty-value convention here rather than reproduced. Worth a
 *   docs/ANGULAR_PENDING_FINDINGS.md entry if anyone revisits this screen.
 *
 * **2 confirmed bugs/dead UI, NOT fixed, faithfully reproduced per this
 * ticket's explicit parity decisions:**
 * - The Specialty select's
 *   `ng-change="getDoctorBySpeciality(admissionRequest.specialityId)"`
 *   (view:46) is 100% dead copy-paste residue: `getDoctorBySpeciality` is a
 *   real function used correctly by ~45 OTHER controllers (e.g.
 *   `admissionRequestController.js`), but is never defined anywhere in
 *   `modalityTypeMasterController.js`, and `admissionRequest` is never a
 *   scope variable here either -- confirmed via full-file read. Angular
 *   silently no-ops the broken expression; zero observable effect. Per
 *   this ticket's explicit instruction, OMITTED entirely here -- no
 *   `(change)` handler at all beyond the normal `ngModelChange`
 *   value-capture.
 * - The search input (view:91) has no `ng-model`, no `ng-keyup`, and the
 *   table's `ng-repeat` has no `|filter` applied -- it is not wired to
 *   anything at all in legacy, unlike its sibling
 *   `ambulanceServiceProviderMaster`'s working search box (which DOES
 *   filter). Per this ticket's explicit instruction, reproduced verbatim as
 *   a fully unbound, inert `<input>` (see the template) -- not implemented
 *   as a working filter, and not omitted.
 */
@Component({
  selector: 'app-modality-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './modality-type-master.html',
  styleUrl: './modality-type-master.scss',
})
export class ModalityTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ModalityTypeMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- specialty dropdown (mirrors $scope.specialityList) ----
  specialityOptions = signal<SpecialityOption[]>([]);

  // ---- form state (mirrors $scope.modalityTypeObj, reused verbatim as
  // the save/update POST body -- see ModalityTypeObj's doc comment) ----
  form = signal<ModalityTypeObj>(emptyModalityTypeObj(this.userId, this.organizationId, this.unitId));
  saveMode = signal(true); // true = Save shown (create); false = Update shown (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initStateMasterList/getStateMasterList/PagerService) ----
  list = signal<ModalityTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadSpecialities();
    this.loadListAndCount();
  }

  private loadSpecialities(): void {
    this.api.getActiveSpecialityList(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.specialityOptions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors initStateMasterList (ctrl:116-166): fetches list + count
  // together, and (via setPage(1,false) once the count resolves) always
  // forces the pager back to page 1 -- used for initial load, page-size
  // changes, and after every save/update response.
  private loadListAndCount(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors getStateMasterList (ctrl:168-196) via setPage(page,true):
  // re-fetches just the list at the new offset, no count refetch.
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof ModalityTypeObj>(field: K, value: ModalityTypeObj[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors modalityTypeForm.$valid over
  // the 3 required inputs, view:16/31/46) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.modalityTypeCode.trim() && f.modalityTypeDesc.trim() && f.specialityId.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().modalityTypeCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().modalityTypeDesc.trim();
  }

  isSpecialityInvalid(): boolean {
    return this.formSubmitted() && !this.form().specialityId.trim();
  }

  // ---- save / confirm-modal (mirrors saveModalityType/PopupOkBtn, ctrl:222-277) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:226 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const now = formatDateTime(new Date());
    this.api.save({ ...this.form(), createdDate: now, updatedDate: now }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Modality type added Successfully...!!');
          this.formSubmitted.set(false);
          this.resetFormAndSpecialities();
        } else {
          this.notify.error('Modality type already exits...!!');
        }
        // ctrl:267-271 -- unconditional, runs on both success and failure.
        this.loadListAndCount();
      },
      error: () => this.notify.error('Something Went Wrong'),
    });
  }

  // ---- update (mirrors updateModalityType, ctrl:354-400 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:358 guard (no else branch)
    const now = formatDateTime(new Date());
    this.api.update({ ...this.form(), updatedDate: now }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Modality type updated Successfully...!!');
          this.formSubmitted.set(false);
          this.resetFormAndSpecialities();
        } else {
          this.notify.error('Modality type already exits...!!');
        }
        // ctrl:391-395 -- unconditional, runs on both success and failure.
        this.loadListAndCount();
      },
      error: () => this.notify.error('Something Went Wrong'),
    });
  }

  // mirrors init() (ctrl:29-100) -- resets the form back to create-mode
  // defaults AND re-fetches the specialty dropdown. Only called from the
  // SUCCESS branch of save/update, matching legacy exactly.
  private resetFormAndSpecialities(): void {
    this.form.set(emptyModalityTypeObj(this.userId, this.organizationId, this.unitId));
    this.saveMode.set(true);
    this.loadSpecialities();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:280-309) ----
  edit(modalityTypeId: number): void {
    this.saveMode.set(false);
    this.api.getById(modalityTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:305 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        // ctrl:300-303 -- only these 4 fields are overwritten; organizationId/
        // unitId/createdBy/updatedBy/status are left as-is (see
        // ModalityTypeObj's doc comment for the resend-quirk this causes).
        this.form.update((f) => ({
          ...f,
          modalityTypeId: d.modalityTypeId,
          modalityTypeCode: d.modalityTypeCode,
          modalityTypeDesc: d.modalityTypeDesc,
          specialityId: String(d.specialityId), // ctrl:303's `.toString()`
        }));
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:312-351) ----
  toggleStatus(row: ModalityTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.modalityTypeId === row.modalityTypeId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        modalityTypeId: row.modalityTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Modality type Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:233-235/313-315/360-362. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
