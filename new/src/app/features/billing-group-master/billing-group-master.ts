import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { BillingGroupMasterService } from './billing-group-master.service';
import {
  BillingGroupMasterRecord,
  BillingGroupSpeciality,
  SpecialityMaster,
  emptyBillingGroupMaster,
} from './billing-group-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/UnitMaster/billingMaster/billing-group-master.html
 * + resources/assets/js/controller/billing/global/BillingGroupMasterController.js.
 * Behavior audited and golden-traced in docs/PHASE2_PILOT_AUDIT.md section 8.
 * No new validation, no new fields, no behavior not present in the legacy
 * screen -- exact parity is the goal for this pilot rebuild.
 */
@Component({
  selector: 'app-billing-group-master',
  imports: [FormsModule],
  templateUrl: './billing-group-master.html',
  styleUrl: './billing-group-master.scss',
})
export class BillingGroupMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(BillingGroupMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  // ---- form state (mirrors $scope.billingGroupMasterObj) ----
  record = signal<BillingGroupMasterRecord>(emptyBillingGroupMaster(this.unitId, this.organizationId));
  saveMode = signal(true); // true = create (saveBtnFlag), false = update
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  // ---- speciality selection state ----
  specialityMasterList = signal<SpecialityMaster[]>([]);
  private checkedSpecialityIds = signal<Set<number>>(new Set());
  tempSelectedSpecialties = signal<BillingGroupSpeciality[]>([]);
  selectedSpecialties = signal<BillingGroupSpeciality[]>([]);

  isSpecialityChecked = (specialityId: number) => this.checkedSpecialityIds().has(specialityId);

  // ---- list + pagination state ----
  list = signal<BillingGroupMasterRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter(
      (row) =>
        row.billingCode.toLowerCase().includes(keyword) ||
        row.billingGroupDesc.toLowerCase().includes(keyword),
    );
  });

  // ---- service-group view modal ----
  showSpecialityModal = signal(false);
  specialityListForModal = signal<SpecialityMaster[]>([]);

  constructor() {
    this.loadSpecialityMasterList();
    this.loadList();
  }

  private loadSpecialityMasterList(): void {
    this.api.getActiveSpecialityList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.specialityMasterList.set(res.listObject ?? []);
        }
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
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

  // ---- speciality selection (mirrors addTempSpecialty / selectSpeciality) ----
  toggleSpeciality(speciality: SpecialityMaster): void {
    const checked = new Set(this.checkedSpecialityIds());
    const already = this.tempSelectedSpecialties().find((s) => s.specialityId === speciality.specialityId);
    if (already) {
      this.tempSelectedSpecialties.update((list) =>
        list.filter((s) => s.specialityId !== speciality.specialityId),
      );
      checked.delete(speciality.specialityId);
    } else {
      this.tempSelectedSpecialties.update((list) => [
        ...list,
        { specialityId: speciality.specialityId, specialityName: speciality.specialityName },
      ]);
      checked.add(speciality.specialityId);
    }
    this.checkedSpecialityIds.set(checked);
  }

  moveToSelected(): void {
    this.selectedSpecialties.set([...this.tempSelectedSpecialties()]);
  }

  private resetSpecialitySelection(): void {
    this.selectedSpecialties.set([]);
    this.tempSelectedSpecialties.set([]);
    this.checkedSpecialityIds.set(new Set());
  }

  // ---- create / update (mirrors createBillingGroupMaster / updateBillingGroupMaster) ----
  isFieldInvalid(value: string): boolean {
    return this.submitted() && value.trim().length === 0;
  }

  save(): void {
    this.submitted.set(true);
    const r = this.record();
    if (!r.billingCode.trim() || !r.billingGroupDesc.trim()) {
      return;
    }

    const payload: BillingGroupMasterRecord = {
      ...r,
      listSpecialityMasterDto: [...this.selectedSpecialties()],
    };

    const request = this.saveMode() ? this.api.create(payload) : this.api.update(payload);
    request.subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Saved successfully');
          this.resetForm();
          this.loadList();
        } else {
          this.notify.error(res.message ?? 'Save failed');
        }
      },
      error: () => this.notify.error('Save failed'),
    });
  }

  private resetForm(): void {
    this.record.set(emptyBillingGroupMaster(this.unitId, this.organizationId));
    this.submitted.set(false);
    this.saveMode.set(true);
    this.resetSpecialitySelection();
  }

  // ---- edit (mirrors getBillingGroupMasterById) ----
  edit(billingGroupId: number): void {
    this.resetSpecialitySelection();
    this.api.getById(this.organizationId, this.unitId, billingGroupId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        this.record.set(res.listObject[0]);
        const selected: BillingGroupSpeciality[] = [];
        const checked = new Set<number>();
        for (const row of res.listObject) {
          const speciality = row as unknown as BillingGroupSpeciality;
          if (speciality.specialityId != null) {
            selected.push({ specialityId: speciality.specialityId, specialityName: speciality.specialityName });
            checked.add(speciality.specialityId);
          }
        }
        this.selectedSpecialties.set(selected);
        this.tempSelectedSpecialties.set(selected);
        this.checkedSpecialityIds.set(checked);
        this.saveMode.set(false);
      },
    });
  }

  // ---- status toggle (mirrors changeBillingGroupStatus) ----
  toggleStatus(row: BillingGroupMasterRecord): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .changeStatus(row.billingGroupId as number, nextStatus, this.organizationId, this.unitId)
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            // Update via the list signal, not a direct mutation of `row` --
            // mutating a plain object nested inside a signal's array doesn't
            // reliably trigger a re-render on its own (docs/
            // ANGULAR_PENDING_FINDINGS.md entry 12); replacing the array via
            // `.update()` does, matching legacy's own full-list-reload
            // pattern (`initBillingGroupMaster()`) in spirit.
            this.list.update((rows) =>
              rows.map((r) => (r.billingGroupId === row.billingGroupId ? { ...r, status: nextStatus } : r)),
            );
            this.notify.success(res.message ?? 'Status updated');
          }
        },
      });
  }

  // ---- view specialities modal (mirrors getSpecialityByGroup) ----
  viewSpecialities(billingGroupId: number): void {
    this.api.getById(this.organizationId, this.unitId, billingGroupId).subscribe({
      next: (res) => {
        this.specialityListForModal.set(res.status === 'success' ? (res.listObject as unknown as SpecialityMaster[]) ?? [] : []);
        this.showSpecialityModal.set(true);
      },
    });
  }

  closeSpecialityModal(): void {
    this.showSpecialityModal.set(false);
  }

  updateField<K extends keyof BillingGroupMasterRecord>(field: K, value: BillingGroupMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }
}
