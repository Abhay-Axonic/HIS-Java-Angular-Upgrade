import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ChecklistItemMasterService } from './checklist-item-master.service';
import { ChecklistItemForm, ChecklistItemRow, emptyChecklistItemForm } from './checklist-item-master.models';

/**
 * Parity rebuild of views/master/GlobalMaster/OTIMS/checklist_master.html +
 * resources/assets/js/controller/otims/global/checklistItemMasterController.js
 * (`checklistItemMasterController`, classic `$scope`, no `controllerAs`).
 * Legacy state `checklistItem` (app.js:12511-12535, resolve block confirmed
 * live directly -- note the state name is `checklistItem`, NOT
 * `checklistItemMaster`, and its real template is `checklist_master.html`
 * with an underscore; a sibling file `checklist-item-master.html` exists on
 * disk but is unused/orphaned -- confirmed by reading the state config
 * directly, not guessed from the filename), moduleId 525.
 *
 * **Confirmed real, fully-functional CRUD, no known findings.** This screen
 * was independently pre-verified during this project's systemic
 * duplicate-check sweep (docs/ANGULAR_PENDING_FINDINGS.md) as one of the
 * best-built duplicate-checks found in that entire sweep -- confirmed again
 * here via direct DAO read (`ChecklistItemMasterDaoImpl.saveChecklistItemMaster`):
 * a real, self-excluding-on-update duplicate-check covers BOTH code and
 * description, on BOTH create and update (four distinct named queries,
 * correctly gated by whether `checklistItemMasterId` is present). Live
 * round trip confirmed 2026-07-28 against real pre-existing seed data
 * (`CHK1`/`CHK2`, left over from the duplicate-check sweep itself): create,
 * edit-then-resave (a real upsert -- one form, one button, one function,
 * same shape as this batch's `checklistEquipmentMaster` sibling: no
 * separate update endpoint exists at all), and status-toggle, all HTTP 200.
 *
 * **One real, confirmed structural difference from this batch's other 3
 * screens:** the status-toggle success branch DOES trigger a real list
 * reload (`$scope.getChecklistItemList()`, ctrl:152) -- unlike
 * `checklistEquipmentMaster`/`checklistEquipmentMapper`/`autoclaveMaster`,
 * which all rely solely on the checkbox's own optimistic two-way binding.
 * Reproduced here as a real `loadList()` call after a successful toggle,
 * not just an in-place signal update.
 *
 * No confirm-modal of any kind (confirmed by full-file read of both
 * controller and view -- zero `popUpFlag`/`showModal` references). Real,
 * working client-side validation (empty code/description blocked with
 * `growl.warning`, matching legacy's exact messages).
 */
@Component({
  selector: 'app-checklist-item-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './checklist-item-master.html',
  styleUrl: './checklist-item-master.scss',
})
export class ChecklistItemMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ChecklistItemMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<ChecklistItemForm>(emptyChecklistItemForm());
  searchKeyword = signal('');

  rows = signal<ChecklistItemRow[]>([]);

  displayedRows = () => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  };

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getList(this.organizationId, 0, 10, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    
        error: (err) => {
          console.error('checklist-item-master error', err);
          this.notify.error('Something went wrong !!');
        },
      });
  }

  updateField<K extends keyof ChecklistItemForm>(field: K, value: ChecklistItemForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // mirrors saveCheckListItem (ctrl:80-134) -- real client-side validation,
  // single upsert call.
  onSaveClick(): void {
    const f = this.form();
    if (!f.code.trim()) {
      this.notify.warning('please enter checklist Item code');
      return;
    }
    if (!f.description.trim()) {
      this.notify.warning('please enter checklist Item description');
      return;
    }
    this.api
      .save({ ...f, organizationId: this.organizationId, unitId: this.unitId, userId: this.userId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
            this.form.set(emptyChecklistItemForm());
          } else {
            this.notify.error(res.message ?? 'Error');
          }
          this.loadList();
        },
      
        error: (err) => {
          console.error('checklist-item-master error', err);
          this.notify.error('Something went wrong !!');
        },
      });
  }

  // mirrors $scope.update (ctrl:173-182).
  edit(row: ChecklistItemRow): void {
    this.form.set({
      checklistItemMasterId: row.checklistItemMasterId,
      code: row.code,
      description: row.description,
    });
  }

  // mirrors updateStatus (ctrl:136-171) -- unlike this batch's other 3
  // screens, a successful toggle DOES trigger a real list reload (see
  // class doc comment).
  toggleStatus(row: ChecklistItemRow): void {
    const nextStatus: ChecklistItemRow['status'] = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) =>
      rows.map((r) => (r.checklistItemMasterId === row.checklistItemMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        checklistItemMasterId: row.checklistItemMasterId,
        status: nextStatus,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
            this.loadList(); // ctrl:152 -- real reload on success, unlike this batch's other 3 screens.
          } else {
            this.notify.error(res.message ?? 'Error');
          }
        },
      
        error: (err) => {
          console.error('checklist-item-master error', err);
          this.notify.error('Something went wrong !!');
        },
      });
  }
}
