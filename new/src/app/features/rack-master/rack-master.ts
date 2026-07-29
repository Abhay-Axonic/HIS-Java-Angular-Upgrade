import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RackMasterService } from './rack-master.service';
import { RackOption, RackRow, ShelfRow } from './rack-master.models';

const SELECT_STORE: RackOption = { store: 'Select Store' };
const CODE_RE = /^[a-z0-9]*[-]?[a-z0-9]*$/i;

@Component({
  selector: 'app-rack-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './rack-master.html',
  styleUrl: './rack-master.scss',
})
export class RackMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RackMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  stores = signal<RackOption[]>([SELECT_STORE]);
  racks = signal<RackRow[]>([]);
  search = signal('');
  sortField = signal<keyof RackRow | ''>('');
  sortReverse = signal(false);
  showConfirmModal = signal(false);
  showShelfModal = signal(false);

  id = signal<number | undefined>(undefined);
  activeRackId = signal<number | undefined>(undefined);
  selectedStore = signal<RackOption>(SELECT_STORE);
  oldStore = signal<RackOption>(SELECT_STORE);
  rackCode = signal('');
  rackName = signal('');
  shelfDetails = signal<ShelfRow[]>([{ shelfId: undefined, shelfCode: '', shelfName: '' }]);
  savedShelves = signal<ShelfRow[]>([]);

  displayedRows = computed(() => {
    const term = this.search().trim().toLowerCase();
    let rows = this.racks();
    if (term) {
      rows = rows.filter((r) => [r.storeName, r.rackCode, r.rackName].some((v) => String(v ?? '').toLowerCase().includes(term)));
    }
    const field = this.sortField();
    if (field) {
      const dir = this.sortReverse() ? -1 : 1;
      rows = [...rows].sort((a, b) => (String(a[field] ?? '') > String(b[field] ?? '') ? 1 : -1) * dir);
    }
    return rows;
  });

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.loadStores();
    this.loadRacks();
  }

  private loadStores(): void {
    this.api.getStores(this.unitId, this.userId).subscribe({
      next: (rows) => this.stores.set([SELECT_STORE, ...(rows ?? [])]),
      error: () => this.notify.error('Something went wrong'),
    });
  }

  private loadRacks(): void {
    this.loader.start();
    this.api.getRacks(this.unitId, this.organizationId).subscribe({
      next: (rows) => {
        this.racks.set(rows ?? []);
        this.loader.stop();
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Something went wrong');
      },
    });
  }

  sortBy(field: keyof RackRow): void {
    this.sortField.set(field);
    this.sortReverse.set(!this.sortReverse());
  }

  openShelfModal(): void {
    if (!this.selectedStore().id) return this.notify.error('Please Select Store');
    if (!this.rackCode()) return this.notify.error('Please Enter Rack Code');
    if (!this.rackName()) return this.notify.error('Please Enter Rack Name');
    this.showShelfModal.set(true);
  }

  addShelf(): void {
    this.shelfDetails.update((rows) => [...rows, { shelfId: undefined, shelfCode: '', shelfName: '' }]);
  }

  removeShelf(index: number): void {
    this.shelfDetails.update((rows) => rows.filter((_, i) => i !== index));
  }

  saveShelfDetails(): void {
    if (!this.validateShelves()) return;
    this.savedShelves.set(this.shelfDetails().map((s) => ({ ...s })));
    this.showShelfModal.set(false);
  }

  private validateShelves(): boolean {
    const seen = new Map<string, number>();
    for (const shelf of this.shelfDetails()) {
      if (!shelf.shelfName) return this.notify.error('Fill Shelf Name'), false;
      if (!shelf.shelfCode) return this.notify.error('Fill Shelf Code'), false;
      if (!CODE_RE.test(shelf.shelfCode)) return this.notify.error('Code can only be alpha numeric with single hypen.'), false;
      seen.set(shelf.shelfCode, (seen.get(shelf.shelfCode) ?? 0) + 1);
    }
    const duplicates = [...seen.entries()].filter(([, count]) => count >= 2).map(([code]) => code);
    if (duplicates.length) return this.notify.error(`Duplicate Shelf code - ${duplicates.join(',')}`), false;
    return true;
  }

  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validateRack()) return;
    this.id() == null ? this.create() : this.update();
  }

  private validateRack(): boolean {
    if (!this.selectedStore().id) return this.notify.error('Please Select Store'), false;
    if (!this.rackCode()) return this.notify.error('Please Enter Rack Code'), false;
    if (!CODE_RE.test(this.rackCode())) return this.notify.error('Code can only be alpha numeric with single hypen.'), false;
    if (!this.rackName()) return this.notify.error('Please Enter Rack Name'), false;
    if (!this.savedShelves().length) return this.notify.error('Save Items in Shelf Details'), false;
    const duplicate = this.racks().some((r) => r.rackCode === this.rackCode() && r.id !== this.activeRackId());
    if (duplicate) return this.notify.error('Duplicate rack code'), false;
    return true;
  }

  private payload(): Record<string, unknown> {
    return {
      storeId: this.selectedStore().id,
      rackCode: this.rackCode(),
      rackName: this.rackName(),
      status: true,
      items: this.savedShelves(),
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
    };
  }

  private create(): void {
    this.api.create(this.payload()).subscribe({
      next: (res) => {
        this.notify.success(res.message || 'Record saved successfully');
        this.clearFields();
        this.loadRacks();
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  private update(): void {
    const id = this.id();
    if (id == null) return;
    this.api.update(id, this.payload()).subscribe({
      next: (res) => {
        this.notify.success(res.message || 'Record Updated successfully');
        this.clearFields();
        this.loadRacks();
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  edit(id: number): void {
    this.api.getRack(id).subscribe({
      next: (row) => {
        const store = this.stores().find((s) => s.id === row.storeId) ?? SELECT_STORE;
        this.id.set(row.id);
        this.activeRackId.set(row.id);
        this.selectedStore.set(store);
        this.oldStore.set(store);
        this.rackCode.set(row.rackCode);
        this.rackName.set(row.rackName);
        this.shelfDetails.set((row.items?.length ? row.items : [{ shelfId: undefined, shelfCode: '', shelfName: '' }]).map((s) => ({ ...s })));
        this.savedShelves.set((row.items ?? []).map((s) => ({ ...s })));
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  toggleStatus(row: RackRow): void {
    row.status = !row.status;
    this.api.toggle(row.id, { status: row.status, unitId: this.unitId, organizationId: this.organizationId, userId: this.userId }).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: () => {
        row.status = !row.status;
        this.notify.error('Something went wrong');
      },
    });
  }

  clearFields(): void {
    this.id.set(undefined);
    this.activeRackId.set(undefined);
    this.selectedStore.set(SELECT_STORE);
    this.oldStore.set(SELECT_STORE);
    this.rackCode.set('');
    this.rackName.set('');
    this.shelfDetails.set([{ shelfId: undefined, shelfCode: '', shelfName: '' }]);
    this.savedShelves.set([]);
    this.search.set('');
  }
}
