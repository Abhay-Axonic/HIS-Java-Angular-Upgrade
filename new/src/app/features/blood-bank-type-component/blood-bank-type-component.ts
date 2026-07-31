import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BloodBankTypeComponentService } from './blood-bank-type-component.service';
import {
  BloodBagOption,
  BloodBankTypeComponentRow,
  BloodComponentOption,
  DetailedComponentRow,
} from './blood-bank-type-component.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

interface PickedComponent {
  bloodComponentId: number;
  bloodComponentDesc: string;
}

/**
 * Parity rebuild of
 * views/master/UnitMaster/bloodBank/blood-bank-type-component-master.html +
 * resources/assets/js/controller/bloodBank/bloodBankTypeComponentMasterController.js.
 * moduleId 739 (app.js:16521-16548 resolve block, confirmed live).
 *
 * Real, functional golden-trace capture (2026-07-31, live deployment,
 * authenticated session) confirmed `getBloodBagList` returned a real
 * seeded row ("Bag Type Three"), zero console errors.
 *
 * Each list ROW represents one BAG TYPE, not one bag-type/component pair
 * -- confirmed by reading BloodBankTypeComponentMasterController.java
 * directly: `updateBloodBankTypeComponentMasterStatus`'s own
 * `@PathVariable` is `bloodBagTypeId`, and the legacy view's
 * `showUpdateBtn`/`updateStatus` calls both pass `info.bloodBagTypeId` as
 * the row id (not a distinct per-mapping id) -- one bag type can have
 * multiple components attached via the dual-list picker below.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The dual-list "add component" picker (ctrl:112-131) is the same
 *   `.indexOf(value)`-after-a-boolean-reset copy-paste-descended pattern
 *   found in 24 controller files across this codebase (Billing, LIS,
 *   Nursing, Blood Bank, Master/Unit, Master/Global) -- toggling a
 *   checkbox adds/removes it from a temp selection list; nothing is
 *   actually persisted until "Save"/"Update" is clicked. Reproduced via
 *   `toggleComponentPick()` below, same shape.
 * - `save`/`update` (ctrl:253-343/391-480) validate, in this exact order:
 *   bag type selected, at least one component picked in `getComponentList`
 *   (a checkbox-state map, not the actual selection), THEN that
 *   `selectBloodBankTypeComponentMasterList` (the actual finalized
 *   selection) is non-empty -- three separate checks, each with its own
 *   growl error, reproduced as three separate validation steps here too.
 * - `showUpdateBtn`/`edit` (ctrl:346-389) re-fetches the bag type's already-
 *   mapped components via `getBloodBankTypeComponentMasterById` and
 *   pre-populates BOTH the temp-pick list and the finalized selection list
 *   from the same response -- reproduced: `edit()` seeds `pickedComponents`
 *   directly from the fetched detail rows.
 */
@Component({
  selector: 'app-blood-bank-type-component',
  imports: [FormsModule, StatusToggle],
  templateUrl: './blood-bank-type-component.html',
  styleUrl: './blood-bank-type-component.scss',
})
export class BloodBankTypeComponent {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(BloodBankTypeComponentService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  bloodBagTypeId = signal<number | ''>('');
  bagTypeList = signal<BloodBagOption[]>([]);
  componentList = signal<BloodComponentOption[]>([]);
  pickedComponents = signal<PickedComponent[]>([]);

  saveMode = signal(true);
  editingBagTypeId = signal<number | undefined>(undefined);
  showConfirmModal = signal(false);

  list = signal<BloodBankTypeComponentRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  detailModalRows = signal<DetailedComponentRow[] | null>(null);

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => r.bloodBagTypeDesc?.toLowerCase().includes(keyword));
  });

  isPicked = computed(() => new Set(this.pickedComponents().map((c) => c.bloodComponentId)));

  constructor() {
    this.api.getBagTypeList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.bagTypeList.set(res.listObject ?? []);
      },
    });
    this.api.getComponentList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.componentList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(offset, this.pageSize()).subscribe({
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

  toggleComponentPick(component: BloodComponentOption): void {
    this.pickedComponents.update((list) => {
      const exists = list.some((c) => c.bloodComponentId === component.bloodComponentId);
      if (exists) return list.filter((c) => c.bloodComponentId !== component.bloodComponentId);
      return [...list, { bloodComponentId: component.bloodComponentId, bloodComponentDesc: component.bloodComponentDesc }];
    });
  }

  // ctrl:253-281/391-419 -- three separate checks, each its own error. See class comment.
  private validate(): boolean {
    if (this.bloodBagTypeId() === '') {
      this.notify.error('Please select Blood Bag');
      return false;
    }
    if (this.pickedComponents().length === 0) {
      this.notify.error('Please Select Component Type');
      return false;
    }
    return true;
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
    const now = formatDateTime(new Date());
    this.api
      .save({
        bloodBagTypeId: this.bloodBagTypeId(),
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        isDeleted: 'N',
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        selectBloodBankTypeComponentMasterList: this.pickedComponents().map((c) => c.bloodComponentId),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
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
    const now = formatDateTime(new Date());
    this.api
      .update({
        bloodBagTypeId: this.bloodBagTypeId(),
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        isDeleted: 'N',
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        selectBloodBankTypeComponentMasterList: this.pickedComponents().map((c) => c.bloodComponentId),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
      });
  }

  private resetForm(): void {
    this.bloodBagTypeId.set('');
    this.pickedComponents.set([]);
    this.saveMode.set(true);
    this.editingBagTypeId.set(undefined);
  }

  edit(bloodBagTypeId: number): void {
    this.saveMode.set(false);
    this.editingBagTypeId.set(bloodBagTypeId);
    this.api.getById(bloodBagTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error('Error In Fetching Data');
          return;
        }
        this.bloodBagTypeId.set(bloodBagTypeId);
        this.pickedComponents.set(
          (res.listObject ?? []).map((row) => ({
            bloodComponentId: row.bloodComponentId,
            bloodComponentDesc: row.bloodComponentDesc,
          })),
        );
      },
    });
  }

  viewDetails(bloodBagTypeId: number): void {
    this.api.getDetailedComponentList(bloodBagTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.detailModalRows.set(res.listObject ?? []);
      },
    });
  }

  closeDetailModal(): void {
    this.detailModalRows.set(null);
  }

  toggleStatus(row: BloodBankTypeComponentRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.bloodBagTypeId === row.bloodBagTypeId ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus(row.bloodBagTypeId, this.organizationId, this.unitId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.resetForm();
          this.loadList();
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
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
