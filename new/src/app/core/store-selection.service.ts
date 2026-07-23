import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from './api-config';
import { SessionService } from './session.service';
import { NotificationService } from './notification.service';
import { AssetStore } from './store-selection.models';

/**
 * Rebuild-side equivalent of legacy's global `$rootScope.storePopup()`/
 * `setStore()`/the `#pharmacyPopupBeforLeftexpand` modal (app.js:2607-2653,
 * markup in index.html:243-267) -- a single, shared, app-shell-level modal
 * (not per-screen), used by every screen that needs a "which store" context
 * before proceeding (viewAssetScheduler and 3 sibling Asset Management
 * screens in this Phase 4 batch alone; `selectedTopStore` is referenced in
 * ~88 files repo-wide per docs/ANGULAR_PENDING_FINDINGS.md entry 16).
 * Wired once at the root shell (app.html), matching legacy's index.html
 * placement.
 *
 * Call-site contract, matching legacy's own controllers exactly (e.g.
 * viewAssetSchedulerController.js:17-23) -- NOT bundled into one method,
 * because legacy doesn't bundle it either:
 *   if (storeSelection.getSelectedStoreId() == null) {
 *     storeSelection.checkAndFetchStores();
 *     storeSelection.show();
 *   } else {
 *     this.storeId = storeSelection.getSelectedStoreId();
 *   }
 *
 * KNOWN BUG, faithfully replicated (docs/ANGULAR_PENDING_FINDINGS.md entry
 * 37): if the fetch resolves with exactly one store, it's auto-selected
 * silently -- but the modal, already shown synchronously by the caller
 * before this async call resolves, is never hidden (legacy's own
 * single-store branch never calls setStore()/modal('hide'), only the
 * user clicking Ok in the multi-store path does). A single-store user's
 * first visit this session leaves an empty, seemingly-stuck modal open
 * even though selection succeeded correctly underneath it. This
 * "caller shows synchronously" call-site contract is legacy-faithful
 * ONLY for the ~25 controllers that themselves call
 * `.modal('show')` right after `storePopup()` (e.g.
 * viewAssetSchedulerController.js:19-20) -- NOT universal. `home`/
 * `doctorDashboard`'s own legacy controllers
 * (nurseDashboardMasterController.js, doctorDashboardController.js) have
 * no such synchronous call of their own; they rely solely on the
 * `stores.length > 1` gate below. Those two callers must NOT call
 * `show()` themselves -- see this method's own `next` handler.
 */
@Injectable({ providedIn: 'root' })
export class StoreSelectionService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  storeList = signal<AssetStore[]>([]);
  visible = signal(false);
  selected = signal<AssetStore | undefined>(undefined);

  checkAndFetchStores(): void {
    const userId = this.session.userId ?? 0;
    const unitId = this.session.unitId ?? 0;
    const organizationId = this.session.organizationId ?? 0;

    this.http
      .get<{ stores: AssetStore[] }>(`${this.base}/api/global/store/getStoreListById/${userId}/${unitId}`)
      .subscribe({
        next: (response) => {
          const stores = response.stores ?? [];
          if (stores.length > 1) {
            this.storeList.set(stores);
            // Matches storePopup()'s own internal `.modal('show')`
            // (app.js:2620-2622) -- needed by callers (home, doctorDashboard)
            // that don't also show() synchronously themselves. A no-op for
            // callers that already called show() before this resolved.
            this.show();
          } else if (stores.length === 1) {
            sessionStorage.setItem('selectedTopStore', JSON.stringify(stores[0]));
            this.session.updateStoreId(stores[0].storeId);
          }
        },
      });

    // Unconditional side effect, matching storePopup() exactly -- unrelated
    // to store selection itself, just piggybacks on the same trigger.
    this.http
      .get<{ status: string; listObject: unknown[] }>(
        `${this.base}/api/global/getWardListForER/${organizationId}/${unitId}`,
      )
      .subscribe({
        next: (response) => {
          if (response.status === 'success') {
            sessionStorage.setItem('nursingStationsWardsListforEr', JSON.stringify(response.listObject));
          }
        },
      });
  }

  show(): void {
    this.visible.set(true);
  }

  /** Equivalent of $rootScope.setStore(storeName) (app.js:2641-2653). */
  confirmSelection(): void {
    const store = this.selected();
    if (store) {
      sessionStorage.setItem('selectedTopStore', JSON.stringify(store));
      this.session.updateStoreId(store.storeId);
      this.visible.set(false);
    } else {
      this.notify.warning('Please select store..!!');
    }
  }

  /** Equivalent of the sessionStorage.getItem('selectedTopStore') check
   * every screen using this modal does itself before deciding whether to
   * call checkAndFetchStores()/show(). */
  getSelectedStoreId(): number | undefined {
    const raw = sessionStorage.getItem('selectedTopStore');
    if (!raw) return undefined;
    try {
      return (JSON.parse(raw) as AssetStore).storeId;
    } catch {
      return undefined;
    }
  }
}
