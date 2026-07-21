import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { SpecialityMasterService } from './speciality-master.service';
import {
  GeneralLedgerOption,
  SpecialityFormRecord,
  SpecialityListRow,
  emptySpeciality,
} from './speciality-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/department-master-global.html
 * + resources/assets/js/controller/master/specialityMasterController.js.
 * The route/UI title is "Department Master"; the legacy code's internal
 * record/variable name throughout is "speciality" -- both names kept as
 * used in their respective contexts. moduleId 372 (app.js:3204-3230).
 *
 * Confirmed real, full CRUD: search, pagination, save/update/edit/
 * status-toggle are all wired to real endpoints (see speciality-master.service.ts).
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal ("Are you sure you want to Save?",
 *   commonPopUpDirectives.js's `<modal>` directive) only gates CREATE
 *   (`saveSpeciality`, ctrl:224-297); `updateSpeciality` (ctrl:396-466)
 *   saves immediately with no confirm step -- same asymmetry already
 *   documented in this codebase's city-master. Reproduced via
 *   `save()`/`confirmSave()` vs. `update()`.
 * - Unlike city-master's confirm-modal flow, legacy's own `saveSpeciality`
 *   only opens the modal when `Specialityform.$valid` is already true
 *   (ctrl:253) -- if a required field is empty, clicking Save is a silent
 *   no-op (no modal, no error message). `updateSpeciality`, by contrast,
 *   growls "Fill all mandatory fields." when invalid (ctrl:458-465). Both
 *   asymmetries reproduced verbatim below.
 * - Two dead/mis-wired validation messages in the legacy view, reproduced
 *   verbatim rather than "corrected" (a corrected version would itself be
 *   an undocumented behavior change):
 *   - The General Ledger field's help text checks
 *     `Specialityform.generalLedgerId.$error.required` (view:66), but the
 *     input's actual `name` is `generalLedgerName` (view:59) -- there is no
 *     `generalLedgerId` form control, so that expression is always
 *     undefined/falsy and the message can never display, regardless of
 *     validity. The field's own `required` attribute still correctly
 *     blocks the form's overall validity; only its error text is dead.
 *     Reproduced by never rendering that message at all.
 *   - The SAC Code help text checks `Specialityform.specialityName.$error.required`
 *     (view:83) instead of `specialitySAC` -- so "SAC Code is required"
 *     actually appears whenever Description is empty, not when SAC itself
 *     is empty. Reproduced via `isSacMessageShown()`, which is deliberately
 *     wired to `specialityName`, not `specialitySAC`.
 * - The 7 checkbox/number fields under the visible form (`isSurgicalCode`,
 *   `isClinicalSpeciality`, `isRenderingSpeciality`, `deptWiseAppointment`,
 *   `isKioskDept`, `isNursingAssessmentRequired`, `allowedOverbookingSlots`)
 *   have no UI control anywhere in the legacy view (view:91-158, the entire
 *   checkbox block, is commented out) -- carried in `SpecialityFormRecord`
 *   and round-tripped on edit/save/update exactly as legacy does, but never
 *   rendered as inputs. Not a missing feature -- legacy itself has no way
 *   to edit these on this screen.
 * - `getGeneralLedgerList` (`GETGENERALLEDGERMASTERACTIVELIST`) is fetched
 *   unconditionally, unfiltered, on every list load (ctrl:140-156) even
 *   though the `<select>` it would populate is commented out (view:53-57)
 *   -- call-but-don't-render, same pattern as this codebase's
 *   organization-master `getLicenceTypeList`. The general ledger typeahead
 *   itself (`ledgerNameSearch`/`SelectLedgerName`/`clearText`, ctrl:467-495)
 *   is a separate, real, reachable control, rebuilt following this
 *   codebase's insurance-master `ledgerSearchTerm`/`ledgerResults` pattern
 *   (debounced search -- legacy's own `uib-typeahead` has no explicit
 *   debounce, but this codebase already established 250ms as the rebuild
 *   convention for this exact widget).
 * - Status toggle (`updateStatus`, ctrl:342-393): optimistically flips the
 *   row's status client-side first (matching legacy's ng-model-before-
 *   ng-change timing), then only reverts it back on the specific
 *   `code === '409'` ("mapped with unit") response -- confirmed against the
 *   live Java backend that this is a plain 200-OK JSON field, not an HTTP
 *   status (see speciality-master.service.ts doc comment). Any OTHER
 *   failure leaves the optimistic flip in place with just a generic error
 *   growl -- reproduced as-is, including that asymmetric non-revert (same
 *   class of bug this codebase's asset-category-master already documents
 *   for its own 406 case).
 * - Search (`serachDepartmentMaster`, ctrl:516-537) fires a real
 *   server-side search on every keystroke (no debounce in legacy), and an
 *   empty search string falls back to the normal paginated list. Reproduced
 *   without adding a debounce legacy doesn't have.
 * - `loadingMsg` (the empty-list-row text) is never initialized anywhere
 *   outside `serachDepartmentMaster` in legacy (ctrl:516-537) -- so a plain
 *   (non-search) empty list shows a blank cell, not "No record found...".
 *   Reproduced by defaulting the signal to `''`.
 */
@Component({
  selector: 'app-speciality-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './speciality-master.html',
  styleUrl: './speciality-master.scss',
})
export class SpecialityMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(SpecialityMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.speciality, ctrl:48-55) ----
  speciality = signal<SpecialityFormRecord>(emptySpeciality());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- general ledger typeahead (mirrors ledgerNameSearch/SelectLedgerName/clearText, ctrl:467-495) ----
  ledgerResults = signal<GeneralLedgerOption[]>([]);
  private ledgerSearch$ = new Subject<string>();

  // ---- list + server-side pagination (mirrors initSpecialityMasterList/PagerService) ----
  specialityList = signal<SpecialityListRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');
  loadingMsg = signal(''); // see class comment -- only ever set by search()

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.api.getGeneralLedgerList(this.organizationId).subscribe();
    this.loadList();

    this.ledgerSearch$
      .pipe(
        debounceTime(250),
        switchMap((term) => this.api.getGeneralLedgerList(this.organizationId, term)),
      )
      .subscribe({
        next: (res) => this.ledgerResults.set(res.status === 'success' ? (res.listObject ?? []) : []),
      });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.specialityList.set(res.listObject ?? []);
      },
    });
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

  // ---- search (mirrors serachDepartmentMaster, ctrl:516-537) ----
  onSearchInput(keyword: string): void {
    this.searchKeyword.set(keyword);
    if (!keyword) {
      this.loadList();
      return;
    }
    this.specialityList.set([]);
    this.loadingMsg.set('Loading...');
    this.api.search(this.organizationId, keyword).subscribe({
      next: (res) => {
        const rows = res.listObject ?? [];
        this.specialityList.set(rows);
        if (rows.length === 0) this.loadingMsg.set('No record found...');
      },
    });
  }

  updateField<K extends keyof SpecialityFormRecord>(field: K, value: SpecialityFormRecord[K]): void {
    this.speciality.update((s) => ({ ...s, [field]: value }));
  }

  // ---- general ledger typeahead ----
  onLedgerNameInput(value: string): void {
    this.updateField('generalLedgerName', value);
    this.updateField('generalLedgerId', null);
    if (value.length >= 1) this.ledgerSearch$.next(value);
    else this.ledgerResults.set([]);
  }

  selectLedger(option: GeneralLedgerOption): void {
    this.updateField('generalLedgerId', option.generalLedgerId);
    this.updateField('generalLedgerName', option.generalLedgerName);
    this.ledgerResults.set([]);
  }

  // mirrors clearText(), ctrl:489-495.
  clearLedgerIfUnselected(): void {
    if (!this.speciality().generalLedgerId) {
      this.updateField('generalLedgerName', '');
    }
  }

  // ---- required-field validation (mirrors Specialityform.$valid via the
  // 4 `required` inputs -- specialityCode, specialityName,
  // generalLedgerName, specialitySAC, view:20/38/62/81) ----
  private requiredFieldsValid(): boolean {
    const s = this.speciality();
    return !!(s.specialityCode.trim() && s.specialityName.trim() && s.generalLedgerId && s.specialitySAC.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.speciality().specialityCode.trim();
  }

  isDescriptionInvalid(): boolean {
    return this.formSubmitted() && !this.speciality().specialityName.trim();
  }

  isGeneralLedgerInvalid(): boolean {
    return this.formSubmitted() && !this.speciality().generalLedgerId;
  }

  isSacInvalid(): boolean {
    return this.formSubmitted() && !this.speciality().specialitySAC.trim();
  }

  // Legacy quirk (view:83) -- reproduced verbatim, see class comment: tied
  // to Description's emptiness, not SAC's own.
  isSacMessageShown(): boolean {
    return this.formSubmitted() && !this.speciality().specialityName.trim();
  }

  // ---- save / confirm-modal (mirrors saveSpeciality/PopupOkBtn, ctrl:218-297) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:253 guard
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const s = this.speciality();
    const now = formatDateTime(new Date());
    this.api
      .create({
        specialityCode: s.specialityCode,
        specialityName: s.specialityName,
        generalLedgerId: s.generalLedgerId,
        specialitySAC: s.specialitySAC,
        isSurgicalCode: s.isSurgicalCode,
        isClinicalSpeciality: s.isClinicalSpeciality,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdDate: now,
        updatedDate: now,
        deptWiseAppointment: s.deptWiseAppointment,
        isRenderingSpeciality: s.isRenderingSpeciality,
        isKioskDept: s.isKioskDept,
        isNursingAssessmentRequired: s.isNursingAssessmentRequired,
        allowedOverbookingSlots: s.allowedOverbookingSlots,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Department added successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Department already exits...!!');
          }
          // ctrl:289-292 -- reloads the list unconditionally after every
          // save attempt (success or failure), same as this codebase's
          // city-master.
          this.loadList();
        },
      });
  }

  // ---- update (mirrors updateSpeciality, ctrl:396-466) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) {
      this.notify.error('Fill all mandatory fields.');
      return;
    }
    const s = this.speciality();
    const now = formatDateTime(new Date());
    this.api
      .update({
        specialityId: s.specialityId,
        specialityCode: s.specialityCode,
        specialityName: s.specialityName,
        specialitySAC: s.specialitySAC,
        generalLedgerId: s.generalLedgerId,
        isSurgicalCode: s.isSurgicalCode,
        isRenderingSpeciality: s.isRenderingSpeciality,
        isClinicalSpeciality: s.isClinicalSpeciality,
        deptWiseAppointment: s.deptWiseAppointment,
        isKioskDept: s.isKioskDept,
        isNursingAssessmentRequired: s.isNursingAssessmentRequired,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: now,
        allowedOverbookingSlots: s.allowedOverbookingSlots,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Department updated successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Department already exits...!!');
          }
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.speciality.set(emptySpeciality());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.ledgerResults.set([]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:300-339) ----
  edit(specialityId: number): void {
    this.api.getById(specialityId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const d = res.listObject[0];
        this.speciality.set({
          specialityId: d.specialityId,
          specialityCode: d.specialityCode,
          specialityName: d.specialityName,
          generalLedgerId: d.generalLedgerId?.toString() ?? null,
          generalLedgerName: d.generalLedgerName?.toString() ?? '',
          specialitySAC: d.specialitySAC,
          isSurgicalCode: d.isSurgicalCode,
          isClinicalSpeciality: d.isClinicalSpeciality,
          isRenderingSpeciality: d.isRenderingSpeciality,
          deptWiseAppointment: d.deptWiseAppointment,
          isKioskDept: d.isKioskDept,
          isNursingAssessmentRequired: d.isNursingAssessmentRequired,
          allowedOverbookingSlots: d.allowedOverbookingSlots,
        });
        this.saveMode.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:342-393) ----
  toggleStatus(row: SpecialityListRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.specialityList.update((rows) =>
      rows.map((r) => (r.specialityId === row.specialityId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        specialityId: row.specialityId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Department Updated successfully...!!');
            return;
          }
          if (res.code === '409') {
            const reverted: 'A' | 'I' = nextStatus === 'A' ? 'I' : 'A';
            this.notify.warning("Department is mapped with unit, you can't change status.");
            this.specialityList.update((rows) =>
              rows.map((r) => (r.specialityId === row.specialityId ? { ...r, status: reverted } : r)),
            );
          } else {
            // Legacy quirk: no revert here, only the 409 branch above
            // reverts the optimistic flip -- see class comment.
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:226-228/344-346/398-400. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
