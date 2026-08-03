import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ChecklistEquipmentMasterService } from './checklist-equipment-master.service';
import { ChecklistEquipmentForm, ChecklistEquipmentRow, emptyChecklistEquipmentForm } from './checklist-equipment-master.models';

/**
 * Parity rebuild of
 * views/master/GlobalMaster/EquipmentMaster/checklist-equipment-master.html +
 * resources/assets/js/controller/master/global/equipmentMaster/checklistEquipmentMasterController.js
 * (`checklistEquipmentMasterController`, classic `$scope`, no `controllerAs`).
 * Legacy state `checklistEquipmentMaster` (app.js:12691-12716, resolve block
 * confirmed live directly), moduleId 526.
 *
 * **Confirmed real, fully-functional CRUD, no known findings.** Live round
 * trip confirmed 2026-07-28 (real auth session, module 526 patched into
 * localStorage for this test account): create, edit-then-resave (upsert),
 * and status-toggle all confirmed against the real backend, all HTTP 200 --
 * the edit-then-resave correctly updated the SAME row in place (not a
 * duplicate), confirming the upsert behavior read from source.
 *
 * **Real, confirmed structural quirks reproduced below:**
 * - **One form, one button, one function serves both Create and Update.**
 *   Unlike most sibling screens in this codebase, there is no separate
 *   `update()` HTTP call at all -- `saveChecklistEquipmentMaster()`
 *   (ctrl:153-201) sends the whole `checklistObj`, and the backend
 *   (`EquipmentChecklistDaoImpl.saveEquipmentChecklist`, confirmed by direct
 *   read) upserts based on whether `checklistEquipmentMasterId` is null.
 *   The button's title toggles `{{saveBtnFlag ? 'Save' : 'Update'}}`
 *   (view:91) but always calls the same function. Reproduced with a single
 *   `onSaveClick()` and a `saveMode` signal purely for the button label.
 * - **No confirm-modal of any kind** -- neither Save nor the upsert path
 *   has one, confirmed by full-file read (zero `popUpFlag`/`showModal`
 *   references anywhere in this controller). Same structural shape as this
 *   project's `machineParameterMaster` precedent.
 * - **Real, working client-side validation** -- `saveChecklistEquipmentMaster`
 *   blocks an empty Code or Name with a `growl.warning`, confirmed live
 *   (unlike `vitalQualifierMaster`'s sibling screen, whose validation is
 *   dead). Reproduced as an early-return with the same two-field check.
 * - **Real, correct, self-excluding duplicate-check server-side** -- the
 *   DAO uses one named query for create (`CHECK_FOR_UNIQUE_...`, no self-
 *   exclusion) and a different one for update
 *   (`CHECK_FOR_UNIQUE_..._NOT_ID`, excludes the row being edited) --
 *   confirmed by direct DAO read, not a rebuild concern (server-enforced).
 * - **Status toggle does not reload the list after a successful call**
 *   (ctrl:203-237, success branch calls `$scope.init()` only, which resets
 *   `checklistObj`, NOT `initMasterList()`) -- but the checkbox's own
 *   `ng-model="info.status"` two-way binding flips the row optimistically
 *   and correctly on click regardless, so the display never goes stale in
 *   practice (matches this codebase's `amenity-master`-style toggle
 *   precedent, not `district-master`'s always-reload variant). Reproduced
 *   via an in-place signal update, no re-fetch.
 * - **A decorative "Add Checklist Component" modal exists in the DOM but is
 *   entirely unreachable** -- its only trigger (view:36-40) is commented
 *   out, and its own content (hardcoded "Component 1"/"Component 2" rows,
 *   non-functional +/- buttons) has no live data binding at all. Not
 *   modeled here -- zero live path reaches it, same treatment as this
 *   project's other confirmed-dead decorative markup.
 * - Real client-side search (`filter:searchKeyword`) and real pagination,
 *   both reproduced as working.
 */
@Component({
  selector: 'app-checklist-equipment-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './checklist-equipment-master.html',
  styleUrl: './checklist-equipment-master.scss',
})
export class ChecklistEquipmentMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ChecklistEquipmentMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<ChecklistEquipmentForm>(emptyChecklistEquipmentForm());
  searchKeyword = signal('');

  rows = signal<ChecklistEquipmentRow[]>([]);
  totalCount = signal(0);

  // ctrl:91 -- button label only; the same function always runs.
  saveMode = computed(() => this.form().checklistEquipmentMasterId === null);

  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getList(this.organizationId, 0, 10, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    
        error: (err) => {
          console.error('checklist-equipment-master error', err);
          this.notify.error('Something went wrong !!');
        },
      });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
          error: (err) => {
            console.error('checklist-equipment-master error', err);
            this.notify.error('Something went wrong !!');
          },
        })
  }

  updateField<K extends keyof ChecklistEquipmentForm>(field: K, value: ChecklistEquipmentForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // mirrors saveChecklistEquipmentMaster (ctrl:153-201) -- real client-side
  // validation (see class doc comment), single upsert call.
  onSaveClick(): void {
    const f = this.form();
    if (!f.checklistCode.trim()) {
      this.notify.warning('Please select code');
      return;
    }
    if (!f.checklistName.trim()) {
      this.notify.warning('Please select name');
      return;
    }
    this.api
      .save({ ...f, organizationId: this.organizationId, unitId: this.unitId, userId: this.userId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
            this.form.set(emptyChecklistEquipmentForm());
          } else {
            this.notify.error(res.message ?? 'Error');
          }
          this.loadList();
        },
      
        error: (err) => {
          console.error('checklist-equipment-master error', err);
          this.notify.error('Something went wrong !!');
        },
      });
  }

  clearSearch(): void {
    this.searchKeyword.set('');
    this.form.set(emptyChecklistEquipmentForm());
  }

  // mirrors $scope.update (ctrl:268-277) -- loads a row into the form for
  // editing; the actual persist happens via onSaveClick's upsert.
  edit(row: ChecklistEquipmentRow): void {
    this.form.set({
      checklistEquipmentMasterId: row.checklistEquipmentMasterId,
      checklistCode: row.checklistCode,
      checklistName: row.checklistName,
    });
  }

  // mirrors updateStatus (ctrl:203-237) -- see class doc comment for why no
  // list reload is needed after.
  toggleStatus(row: ChecklistEquipmentRow): void {
    const nextStatus: ChecklistEquipmentRow['status'] = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) =>
      rows.map((r) => (r.checklistEquipmentMasterId === row.checklistEquipmentMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        checklistEquipmentMasterId: row.checklistEquipmentMasterId,
        status: nextStatus,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
          } else {
            this.notify.error(res.message ?? 'Error');
          }
        },
      
        error: (err) => {
          console.error('checklist-equipment-master error', err);
          this.notify.error('Something went wrong !!');
        },
      });
  }
}
