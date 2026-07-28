import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AutoclaveMasterService } from './autoclave-master.service';
import {
  AutoclaveForm,
  AutoclaveRow,
  SterilizationColorOption,
  SterilizationTypeOption,
  emptyAutoclaveForm,
} from './autoclave-master.models';

/**
 * Parity rebuild of views/master/UnitMaster/CSSD/autoclave-master.html +
 * resources/assets/js/controller/CSSD/unit/autoClaveMasterController.js
 * (`autoClaveMasterController`, classic `$scope`, no `controllerAs`).
 * Legacy state `autoclaveMaster` (app.js:13873-13895, resolve block
 * confirmed live directly), moduleId 553.
 *
 * **Confirmed real, functional CRUD once a real seed-data gap was closed.**
 * This dev DB's `m_sterilization_color_indicator` table (org/unit-unscoped
 * -- a genuinely global reference table per its own query, not seeded per
 * screen) was completely empty, blocking the required Sterilization Color
 * dropdown. One row was seeded for live verification and cleaned up in the
 * same pass -- not a schema/code defect, same class as this session's other
 * empty-reference-table gaps. Live round trip confirmed 2026-07-28: create
 * (confirm-modal-gated), edit-then-update (real PUT, no gate), and
 * status-toggle, all HTTP 200.
 *
 * **Real, confirmed findings reproduced below (see
 * docs/ANGULAR_PENDING_FINDINGS.md, PENDING-batch24-step2-autoclavemaster-goldentrace
 * for the full writeup):**
 * - **No duplicate-check of any kind, client or server.** Confirmed live:
 *   created 2 real rows with the identical Machine Name, both succeeded.
 *   Already known (`docs/DECISION_NEEDED_DuplicateCheckSweepTier3Findings.md`,
 *   awaiting owner decision) -- not fixed here.
 * - **The Save button never becomes "Update," even while genuinely
 *   editing.** `editAutoClave` (ctrl:188-200) never sets `updateBtnFlag`
 *   true anywhere in the controller (confirmed via grep -- it's assigned
 *   exactly once, `false`, in `init()`); the view gates Save's visibility
 *   on `popUpFlag` (view:149), not `saveBtnFlag`/an edit-mode flag, so Save
 *   stays visible and labeled "Save" throughout editing. Functionally
 *   harmless: `saveAutoClave()` checks `onEditAutoClave` first and
 *   correctly delegates to the real `updateAutoClave()` PUT regardless of
 *   what the button says -- confirmed live (edited a real row via the
 *   still-"Save"-labeled control, got a real 200, row updated in place).
 *   Reproduced here as a single `onSaveClick()` whose button label never
 *   changes based on edit state (only the underlying call target does).
 * - **Copy-paste typo in the Create success toast**: legacy's
 *   `saveAutoClave` success message reads "MasterClave added
 *   Successfully...!!" (not "AutoClave") -- confirmed live, verbatim.
 *   Update's own toast is correctly worded. Reproduced verbatim, both toasts.
 * - **Confirm-modal on Create only** (`updateAutoClave` has no gate at
 *   all, matching this codebase's `district-master`-style asymmetry) --
 *   and unlike `vitalQualifierMaster`'s sibling finding, this one DOES
 *   auto-close on a successful save (`init()`'s success-path reset
 *   includes `showModal = false`, confirmed live).
 * - **Real, working Angular form validation** (`autoclaveform.$valid`,
 *   `show-errors-check-validity` broadcast) gates both create and update --
 *   reproduced as a `formSubmitted` signal driving per-field error text,
 *   same pattern as this codebase's other real-validation siblings.
 * - Status toggle has no revert-on-failure and does not reload the list
 *   (ctrl:240-269, no `init()`/list-reload call in either branch) -- just
 *   a growl either way.
 * - No CSSD Store selection is required for Create/Update in this dev
 *   environment -- `$scope.storeId` (sourced from a separate, unrelated
 *   app-wide store-selection popup, `cssdStorePopupFromController`,
 *   ctrl:273-277, never wired to any control in this specific view) stayed
 *   `undefined` throughout live testing and every create/update still
 *   succeeded; not modeled here as a blocking dependency.
 */
@Component({
  selector: 'app-autoclave-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './autoclave-master.html',
  styleUrl: './autoclave-master.scss',
})
export class AutoclaveMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AutoclaveMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  sterilizationTypes = signal<SterilizationTypeOption[]>([]);
  sterilizationColors = signal<SterilizationColorOption[]>([]);

  form = signal<AutoclaveForm>(emptyAutoclaveForm());
  formSubmitted = signal(false);
  isEditing = signal(false);
  showConfirmModal = signal(false);
  searchKeyword = signal('');

  rows = signal<AutoclaveRow[]>([]);

  displayedRows = () => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  };

  constructor() {
    this.loadOptions();
    this.loadList();
  }

  private loadOptions(): void {
    this.api.getSterilizationTypes(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.sterilizationTypes.set(res.listObject ?? []);
      },
    });
    this.api.getSterilizationColors().subscribe({
      next: (res) => {
        if (res.status === 'success') this.sterilizationColors.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    this.api.getList(this.organizationId, this.unitId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe();
  }

  updateField<K extends keyof AutoclaveForm>(field: K, value: AutoclaveForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(
      f.machine_name.trim() &&
      f.machine_description.trim() &&
      f.sterilization_type_id !== '' &&
      f.sterilization_color_id !== '' &&
      f.sterilization_temperature.trim() &&
      f.sterilization_pressure.trim() &&
      f.biological_indicator_day.trim()
    );
  }

  isFieldInvalid(field: keyof AutoclaveForm): boolean {
    if (!this.formSubmitted()) return false;
    const v = this.form()[field];
    return v === '' || (typeof v === 'string' && !v.trim());
  }

  // mirrors saveAutoClave (ctrl:203-235) -- delegates to update when
  // editing, matching legacy's own onEditAutoClave branch, regardless of
  // the button's label (see class doc comment).
  onSaveClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    if (this.isEditing()) {
      this.confirmUpdate();
      return;
    }
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn -> saveAutoClave's real-persist branch. Auto-closes
  // on success, matching legacy (init()'s success-path reset includes
  // showModal = false) -- unlike vitalQualifierMaster's sibling finding.
  confirmFromModal(): void {
    const f = this.form();
    this.api
      .create({ ...f, organizationId: this.organizationId, unitId: this.unitId, userId: this.userId })
      .subscribe({
        next: (res) => {
          this.showConfirmModal.set(false);
          if (res.status === 'success') {
            this.notify.success('MasterClave added Successfully...!!'); // ctrl:219 -- verbatim copy-paste typo, see class doc comment
            this.form.set(emptyAutoclaveForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('MasterClave already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // mirrors updateAutoClave (ctrl:280-300) -- no confirm-modal gate.
  private confirmUpdate(): void {
    const f = this.form();
    this.api.update({ ...f, userId: this.userId }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('AutoClave updated Successfully...!!');
          this.form.set(emptyAutoclaveForm());
          this.formSubmitted.set(false);
          this.isEditing.set(false);
        } else {
          this.notify.error('Update AutoClave already exits...!!');
        }
        this.loadList();
      },
    });
  }

  // mirrors editAutoClave (ctrl:188-200).
  edit(row: AutoclaveRow): void {
    this.form.set({
      id: row.id,
      machine_name: row.machine_name,
      machine_description: row.machine_description,
      sterilization_type_id: row.sterilization_type_id,
      sterilization_color_id: row.sterilization_color_id,
      sterilization_temperature: String(row.sterilization_temperature),
      sterilization_pressure: String(row.sterilization_pressure),
      biological_indicator_day: String(row.biological_indicator_day),
    });
    this.isEditing.set(true);
  }

  // mirrors updateStatus (ctrl:240-269) -- no revert-on-failure, no list
  // reload after.
  toggleStatus(row: AutoclaveRow): void {
    const nextStatus: AutoclaveRow['status'] = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus({ id: row.id, status: nextStatus, updatedBy: this.userId }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('AutoClave Updated Successfully...!!');
        } else {
          this.notify.error('Something went wrong...!!');
        }
      },
    });
  }
}
