import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { OperationTableService } from './operation-table.service';
import { OperationTableRecord, OperationTableRow, TableTypeOption, TheatreOption, emptyOperationTable } from './operation-table.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/OTIMS/operation_Table.html +
 * resources/assets/js/controller/otims/global/operationTableMasterController.js.
 * moduleId 522 (app.js:12429-12456 resolve block, confirmed live).
 *
 * Real, functional golden-trace capture (2026-07-31, live deployment,
 * authenticated session) confirmed `getActiveOperationTableTypeList`
 * returned REAL seeded rows ("Table One", "Table Two"), not just an empty
 * envelope -- strong confirmation this screen and its dropdowns are
 * genuinely live and working, zero console errors.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - `save` (ctrl:189-257) validates `tableTypeId`/`operationTheatreId`
 *   individually with their own growl errors BEFORE checking
 *   `$scope.operationTableform.$valid` (which covers tableCode/tableName/
 *   tableDesc via `required`) -- so a genuinely empty table type shows
 *   "Please Select Table Type" even if the other required fields are also
 *   empty, rather than a single generic error. Reproduced: `save()` checks
 *   tableTypeId first, then operationTheatreId, then the remaining
 *   required text fields, each with its own message.
 * - The confirm modal only opens once ALL of the above pass (ctrl:220-222,
 *   same order as `nationality-master`) -- unlike `pharmacy-strength-unit-
 *   master`, where the modal opens unconditionally and validation happens
 *   after.
 * - `showUpdateBtn`/`edit` (ctrl:259-288) explicitly `.toString()`s the
 *   fetched `tableTypeId`/`operationTheatreId` (ctrl:274-275) -- both are
 *   select-bound as strings in legacy's view. Reproduced by coercing both
 *   to number on the Angular side instead (native `<select>` + `[ngModel]`
 *   number coercion), functionally equivalent, not a literal string-typing
 *   port.
 * - `update` (ctrl:291-347) has no confirm modal at all, validates the same
 *   3 checks as `save` and POSTs immediately on success.
 * - `updateStatus` (ctrl:349-382) does not reload the list after a
 *   successful toggle -- the checkbox's own binding already reflects the
 *   new state client-side. Reproduced: `toggleStatus` flips the row
 *   optimistically and does not reload.
 */
@Component({
  selector: 'app-operation-table',
  imports: [FormsModule, StatusToggle],
  templateUrl: './operation-table.html',
  styleUrl: './operation-table.scss',
})
export class OperationTable {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(OperationTableService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  record = signal<OperationTableRecord>(emptyOperationTable());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  tableTypeList = signal<TableTypeOption[]>([]);
  theatreList = signal<TheatreOption[]>([]);

  list = signal<OperationTableRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.tableCode?.toLowerCase().includes(keyword) ||
        r.tableName?.toLowerCase().includes(keyword) ||
        r.tableDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.api.getActiveTableTypeList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.tableTypeList.set(res.listObject ?? []);
      },
    });
    this.api.getActiveTheatreList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.theatreList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
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

  updateField<K extends keyof OperationTableRecord>(field: K, value: OperationTableRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ctrl:189-257 -- tableType/theatre checked first, own messages; see class comment.
  private validate(): boolean {
    const r = this.record();
    if (r.tableTypeId === '') {
      this.notify.error('Please Select Table Type');
      return false;
    }
    if (r.operationTheatreId === '') {
      this.notify.error('Please Select Theatre');
      return false;
    }
    this.formSubmitted.set(true);
    return !!(r.tableCode.trim() && r.tableName.trim() && r.tableDesc.trim());
  }

  isRequiredMissing(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  openConfirmModal(): void {
    if (!this.validate()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const r = this.record();
    const now = formatDateTime(new Date());
    this.api
      .save({
        tableCode: r.tableCode,
        tableName: r.tableName,
        tableDesc: r.tableDesc,
        tableTypeId: r.tableTypeId,
        operationTheatreId: r.operationTheatreId,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
        status: 'A',
        unitId: this.unitId,
        orgId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Added Successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
      });
  }

  update(): void {
    if (!this.validate()) return;
    const r = this.record();
    this.api
      .update({
        operationTableId: r.operationTableId as number,
        tableCode: r.tableCode,
        tableName: r.tableName,
        tableDesc: r.tableDesc,
        tableTypeId: r.tableTypeId,
        operationTheatreId: r.operationTheatreId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        unitId: this.unitId,
        orgId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
      });
  }

  private resetForm(): void {
    this.record.set(emptyOperationTable());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  edit(id: number): void {
    this.saveMode.set(false);
    this.api.getById(id).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error(res.message ?? 'Error!!');
          return;
        }
        const detail = res.listObject[0];
        this.record.set({
          operationTableId: detail.operationTableId,
          tableCode: detail.tableCode,
          tableName: detail.tableName,
          tableDesc: detail.tableDesc,
          tableTypeId: Number(detail.tableTypeId),
          operationTheatreId: Number(detail.operationTheatreId),
        });
      },
    });
  }

  // ctrl:349-382 -- no list reload after toggle, see class comment.
  toggleStatus(row: OperationTableRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.operationTableId === row.operationTableId ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus(row.operationTableId, nextStatus, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') this.notify.success(res.message ?? 'Status updated');
        else this.notify.error(res.message ?? 'Something went wrong');
      },
    });
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
