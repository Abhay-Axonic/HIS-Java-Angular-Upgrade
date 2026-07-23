import { Injectable, inject, signal } from '@angular/core';
import { SessionService } from './session.service';
import { NotificationService } from './notification.service';
import { CounterListItem, CounterMst } from './counter-selection.models';

/**
 * Rebuild-side equivalent of legacy's global counter-selection trio,
 * `$rootScope.getCounterDetails()` / `selectCounter()` / `confirmCounter()`
 * (app.js:1812-1877) -- the same session-state family as
 * `StoreSelectionService`, gated behind two sessionStorage flags
 * (`isCounterApplicable`/`isUserCounterApplicable`, set at login, outside
 * any single screen's own scope) rather than fetched by this service.
 *
 * First screen to need this is `home` (nurseDashboardMasterController.js,
 * shared with the not-yet-built `nurseDashboard`) -- both screens embed
 * an *identical copy* of the `#counterDetailsModal` markup locally (unlike
 * the store popup, which legacy renders once app-shell-wide), so this
 * service intentionally only owns the state/logic; each consuming
 * screen renders its own modal markup bound to these signals, the same
 * way `home.html`/(future) `nurseDashboard.html` each embed their own
 * copy in legacy.
 *
 * `confirmSelection()` below implements the validate-then-hide behavior
 * that actually executes on click in `home.html` -- `nurseDashboardMasterController`
 * defines its OWN `$scope.confirmCounter` (shadowing `$rootScope.confirmCounter`,
 * which has no validation/hide at all and is therefore dead for any screen
 * using this controller). Faithfully porting the dead `$rootScope` version
 * instead of the one that actually runs would be a real behavior miss, not
 * a faithful one.
 */
@Injectable({ providedIn: 'root' })
export class CounterSelectionService {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);

  counterList = signal<CounterListItem[]>([]);
  visible = signal(false);
  selected = signal<CounterMst | null>(null);

  /** Mirrors the two sessionStorage flag reads every consuming controller
   * does itself before deciding whether counter selection applies at all
   * (nurseDashboardMasterController.js:62-64). */
  isApplicable(): boolean {
    return this.readFlag('isCounterApplicable') === 'Y' && this.readFlag('isUserCounterApplicable') === 'Y';
  }

  /**
   * Mirrors `$rootScope.getCounterDetails()` (app.js:1840-1867). Returns
   * `true` when counter context is already resolved (an existing
   * selection, or exactly one counter available -- auto-selected
   * in-memory only, matching legacy's own asymmetry: that branch does
   * NOT persist to sessionStorage, unlike every other selection path
   * here). Returns `false` and flips `visible` on when the caller must
   * show the modal for a real choice among 2+ counters.
   */
  checkAndPrepare(): boolean {
    const list = this.readCounterList();
    this.counterList.set(list);

    const stored = this.readCounterMstObj();
    if (stored && stored.counterId !== undefined && stored.counterId !== null && stored.counterId !== '') {
      this.selected.set(stored);
      return true;
    }

    if (list.length > 0) {
      if (list.length > 1) {
        this.visible.set(true);
        return false;
      }
      // Single-counter auto-select -- in-memory only, not persisted,
      // faithfully matching app.js:1853-1858.
      this.selected.set({ counterId: list[0].id, counterDesc: list[0].name });
      return true;
    }

    return false;
  }

  /** Mirrors `$rootScope.selectCounter(obj)` (app.js:1820-1838) -- the
   * counter `<select>`'s `ng-change` handler. */
  selectCounter(counter: CounterListItem | null): void {
    if (!counter) {
      this.selected.set(null);
      sessionStorage.setItem('counterMstObj', JSON.stringify(null));
      return;
    }
    const mst: CounterMst = { counterId: counter.id, counterDesc: counter.name };
    this.selected.set(mst);
    sessionStorage.setItem('counterMstObj', JSON.stringify(mst));
    this.session.updateCounterId(counter.id);
  }

  /** Mirrors the LOCAL, actually-executing `$scope.confirmCounter`
   * (nurseDashboardMasterController.js:79-90), not the dead
   * `$rootScope.confirmCounter` it shadows. */
  confirmSelection(): void {
    const stored = this.readCounterMstObj();
    if (!stored || stored.counterId === undefined || stored.counterId === null || stored.counterId === '') {
      this.notify.warning('Please select counter..!!');
      return;
    }
    if (Number(stored.counterId) > 0) {
      this.selected.set(stored);
      this.visible.set(false);
    }
  }

  private readFlag(key: string): string | null {
    try {
      const raw = sessionStorage.getItem(key);
      return raw ? (JSON.parse(raw) as string) : null;
    } catch {
      return null;
    }
  }

  private readCounterMstObj(): CounterMst | null {
    try {
      const raw = sessionStorage.getItem('counterMstObj');
      return raw ? (JSON.parse(raw) as CounterMst) : null;
    } catch {
      return null;
    }
  }

  private readCounterList(): CounterListItem[] {
    try {
      const raw = localStorage.getItem('counterList');
      return raw ? ((JSON.parse(raw) as CounterListItem[]) ?? []) : [];
    } catch {
      return [];
    }
  }
}
