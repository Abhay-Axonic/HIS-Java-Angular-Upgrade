import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import {
  KeyIngredientOption,
  KeyIngredientRow,
  KeyIngredientsMasterService,
  NutritionLine,
} from './key-ingredients-master.service';

@Component({
  selector: 'app-key-ingredients-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './key-ingredients-master.html',
  styleUrl: './key-ingredients-master.scss',
})
export class KeyIngredientsMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(KeyIngredientsMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  rows = signal<KeyIngredientRow[]>([]);
  units = signal<KeyIngredientOption[]>([]);
  nutritions = signal<KeyIngredientOption[]>([]);
  search = signal('');
  showConfirmModal = signal(false);
  showDetailsModal = signal(false);
  detailRows = signal<NutritionLine[]>([]);
  editing = signal(false);

  model = signal<KeyIngredientRow>({
    ingredientCode: '',
    ingredientDescription: '',
    ingredientValue: '',
    uomId: '',
    status: 'A',
    nutriciansList: [],
  });
  nutritionRows = signal<NutritionLine[]>([{ nutritionId: '', value: '', uomId: '', ingredientNutritionId: '' }]);

  displayedRows = computed(() => {
    const term = this.search().trim().toLowerCase();
    if (!term) return this.rows();
    return this.rows().filter((r) =>
      [r.ingredientCode, r.ingredientDescription, r.ingredientValue, r.uomName].some((v) => String(v ?? '').toLowerCase().includes(term)),
    );
  });

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.loadAll();
  }

  loadAll(): void {
    this.api.list(this.organizationId, this.unitId).subscribe({ next: (r) => this.rows.set(r.listObject ?? []) });
    this.api.units(this.organizationId, this.unitId).subscribe({ next: (r) => this.units.set(r.listObject ?? []) });
    this.api.nutritions(this.organizationId, this.unitId).subscribe({ next: (r) => this.nutritions.set(r.listObject ?? []) });
  }

  setField(field: keyof KeyIngredientRow, value: string): void {
    this.model.update((m) => ({ ...m, [field]: value }));
  }

  addNutritionRow(): void {
    this.nutritionRows.update((rows) => [...rows, { nutritionId: '', value: '', uomId: '', ingredientNutritionId: '' }]);
  }

  removeNutritionRow(index: number): void {
    this.nutritionRows.update((rows) => rows.filter((_, i) => i !== index));
  }

  edit(row: KeyIngredientRow): void {
    this.editing.set(true);
    this.model.set({ ...row, organizationId: this.organizationId, unitId: this.unitId, updatedBy: this.userId } as KeyIngredientRow);
    this.nutritionRows.set(row.nutriciansList?.length ? row.nutriciansList.map((n) => ({ ...n })) : [{ nutritionId: '', value: '', uomId: '', ingredientNutritionId: '' }]);
  }

  openDetails(row: KeyIngredientRow): void {
    this.detailRows.set(row.nutriciansList ?? []);
    this.showDetailsModal.set(true);
  }

  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  save(): void {
    this.showConfirmModal.set(false);
    const m = this.model();
    if (!m.ingredientCode || !m.ingredientDescription || !m.ingredientValue || !m.uomId) {
      this.notify.error('Please fill required fields');
      return;
    }
    const payload = {
      ...m,
      organizationId: this.organizationId,
      unitId: this.unitId,
      createdBy: this.userId,
      updatedBy: this.userId,
      nutriciansList: this.nutritionRows().map((n) => ({ ...n })),
    };
    const call = this.editing() ? this.api.update(payload) : this.api.create(payload);
    call.subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(this.editing() ? 'Key Ingredient Updated Successfully...!!' : 'Key Ingrediants Added Successfully...!!');
          this.clear();
          this.loadAll();
        } else {
          this.notify.error(this.editing() ? 'Key ingredient already exits...!!' : 'Nutrition already exits...!!');
        }
      },
    });
  }

  toggleStatus(row: KeyIngredientRow): void {
    const next = row.status === 'A' ? 'I' : 'A';
    row.status = next;
    this.api.updateStatus({ keyIngredientId: row.keyIngredientId, status: next, updatedBy: this.userId }).subscribe({
      next: (res) => res.status === 'success' ? this.notify.success('Key Ingredients Updated Successfully...!!') : this.notify.error('Something went wrong...!!'),
      error: () => this.notify.error('Something went wrong...!!'),
    });
  }

  clear(): void {
    this.editing.set(false);
    this.model.set({ ingredientCode: '', ingredientDescription: '', ingredientValue: '', uomId: '', status: 'A', nutriciansList: [] });
    this.nutritionRows.set([{ nutritionId: '', value: '', uomId: '', ingredientNutritionId: '' }]);
  }
}
