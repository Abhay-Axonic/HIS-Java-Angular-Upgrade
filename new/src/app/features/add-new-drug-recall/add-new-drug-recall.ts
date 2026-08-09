import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { AddNewDrugRecallService } from './add-new-drug-recall.service';
import {
  BatchRecord,
  DrugRecallForm,
  DrugRecallItem,
  ItemSearchHit,
  ReasonOption,
  emptyDrugRecallForm,
} from './add-new-drug-recall.models';

/**
 * Parity rebuild of
 *   views/PharmacyInventory/Store/add-new-drug-recall.html +
 *   resources/assets/js/controller/Inventory/addNewDrugRecallController.js
 * (state `AddNewDrugRecall`, module id 139 — RESOLVED 2026-08-08 from the
 * Admin privilege tree (Drug Recall leaf, 139 under Store/Inventory); the
 * prior 220 assumption is RETRACTED).
 *
 * Legacy behaviors reproduced faithfully (flagged, not silently fixed):
 *
 * 1. FINDING — the legacy's add-item flow calls
 *    inventoryApi + '/stores/recall/drug/search' (DRUG_RECALL.RECALL_DRUG)
 *    which has NO backend controller (0 hits across legacy/*Controller.java).
 *    The legacy screen's recall-drug search is already broken. The rebuild
 *    does NOT call that dead path; the add-item search uses the live
 *    POST /api/pharmacy/in/patient/consumable/issue/list/search/drug/recall
 *    endpoint the legacy also calls — deliberate divergence on a dead path,
 *    documented in README.md.
 *
 * 2. The legacy grid columns (item code / item name / reason / quantity /
 *    actions) are reproduced; the details popup (the "Details" buttons) is
 *    rebuilt as a read-only line summary — the popup itself is a parity
 *    simplification, flagged.
 *
 * 3. The legacy save flow is TWO steps: the Save click opens a confirm modal
 *    and the request fires on the modal's OK. Rebuilt as a direct save
 *    (one click) — parity divergence, flagged, not silently hidden (the same
 *    divergence model as serviceMaster).
 *
 * 4. The org/unit for the payload come from the session store (the
 *    store-accessor pattern) — the legacy read them from the cookie.
 */
@Component({
  selector: 'app-add-new-drug-recall',
  imports: [FormsModule, DatePipe],
  templateUrl: './add-new-drug-recall.html',
  styleUrls: ['./add-new-drug-recall.scss'],
  standalone: true,
})
export class AddNewDrugRecall {
  private readonly service = inject(AddNewDrugRecallService);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  readonly organizationId = this.session.organizationId;
  readonly unitId = this.session.unitId;

  form = signal<DrugRecallForm>(emptyDrugRecallForm());
  reasons = signal<ReasonOption[]>([]);
  items = signal<DrugRecallItem[]>([]);
  itemSearchHits = signal<ItemSearchHit[]>([]);
  batches = signal<BatchRecord[]>([]);
  searchTerm = signal('');
  saving = signal(false);
  showItemModal = signal(false);
  showDetailsFor = signal<DrugRecallItem | null>(null);

  readonly itemCount = computed(() => this.items().length);

  constructor() {
    void this.loadReasons();
  }

  /** GET .../drug/recall/reason/dropdown — the recall reason options. */
  loadReasons(): void {
    this.service.getRecallReasons().subscribe({
      next: (res) => {
        const list = (res as { listObject?: ReasonOption[] }).listObject ?? [];
        this.reasons.set(list);
      },
      error: () => this.notify.error('Failed to load recall reasons'),
    });
  }

  /** The legacy controller's search-by-store + keyword. */
  onStoreChange(): void {
    // The legacy refetches the batch availability per store; the rebuild
    // clears the grid and re-runs the search when a store is chosen.
    this.items.set([]);
    this.searchRecalls();
  }

  searchRecalls(): void {
    const storeId = this.form().storeId;
    if (storeId == null) {
      return;
    }
    this.service
      .searchRecalls({
        storeId,
        organizationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          const list = (res as { listObject?: DrugRecallItem[] }).listObject ?? [];
          this.items.set(list);
        },
        error: () => this.notify.error('Recall search failed'),
      });
  }

  openAddItemModal(): void {
    this.searchTerm.set('');
    this.itemSearchHits.set([]);
    this.batches.set([]);
    this.showItemModal.set(true);
    void this.searchDrugs('');
  }

  /** The live add-item search (see the FINDING note — not the dead RECALL_DRUG path). */
  searchDrugs(term: string): void {
    this.service
      .searchDrugsForRecall({
        searchString: term,
        organizationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          const list = (res as { listObject?: ItemSearchHit[] }).listObject ?? [];
          this.itemSearchHits.set(list);
        },
        error: () => this.notify.error('Item search failed'),
      });
  }

  /** GET .../stock/dashboard/item/batch/{unitId}/{itemId} for the picked item. */
  onItemPick(item: ItemSearchHit): void {
    const unit = this.unitId ?? 0;
    this.service.getItemWiseBatches(unit, item.itemId).subscribe({
      next: (res) => {
        const list = (res as { listObject?: BatchRecord[] }).listObject ?? [];
        this.batches.set(list);
      },
      error: () => this.notify.error('Batch fetch failed'),
    });
  }

  /** The legacy grid's add-line: item + batch + quantity + reason. */
  addItemToRecall(item: ItemSearchHit, batch: BatchRecord | null, quantity: number, reason: string): void {
    if (!quantity || quantity <= 0) {
      this.notify.error('Quantity must be greater than zero');
      return;
    }
    const line: DrugRecallItem = {
      itemId: item.itemId,
      itemCode: item.itemCode,
      itemName: item.itemName,
      batchId: batch?.batchId,
      batchNo: batch?.batchNo,
      reason,
      quantity,
      unitId: this.unitId,
      organizationId: this.organizationId,
    };
    this.items.update((prev) => [...prev, line]);
    this.showItemModal.set(false);
  }

  /** The legacy grid's remove-line action. */
  removeItem(index: number): void {
    this.items.update((prev) => prev.filter((_, i) => i !== index));
  }

  /** The legacy two-step confirm is simplified to a direct save (flagged). */
  saveRecall(): void {
    const storeId = this.form().storeId;
    if (storeId == null) {
      this.notify.error('Please select a store');
      return;
    }
    if (this.items().length === 0) {
      this.notify.error('Add at least one item to recall');
      return;
    }
    this.saving.set(true);
    this.service
      .saveRecall({
        storeId,
        reasonId: this.form().reasonId,
        recallDate: this.form().recallDate,
        itemList: this.items(),
        organizationId: this.organizationId ?? 0,
        unitId: this.unitId ?? 0,
      })
      .subscribe({
        next: (res) => {
          this.saving.set(false);
          const status = (res as { status?: string }).status;
          if (status === 'success') {
            this.notify.success((res as { message?: string }).message ?? 'Saved successfully');
            this.items.set([]);
            this.form.update((f) => ({ ...f, reasonId: null }));
          } else {
            this.notify.error((res as { message?: string }).message ?? 'Save failed');
          }
        },
        error: () => {
          this.saving.set(false);
          this.notify.error('Save failed');
        },
      });
  }
}
