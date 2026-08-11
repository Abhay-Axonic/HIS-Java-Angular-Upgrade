import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { FundReconciliationService } from './fund-reconciliation.service';
import { FundInflowRow } from './fund-reconciliation.models';

/**
 * Parity rebuild of the legacy `fundReconciliation` screen
 * (resources/assets/js/controller/billing/global/fundInflowController.js +
 * views/master/UnitMaster/fundManagement/fund-reconciliation.html).
 * State url '/fundReconciliation' (app.js:19919-19946), module id 808
 * (app.js:19932), title from app.js:19923. Module id 808.
 *
 * SCOPED REBUILD: the legacy screen is a large trust/collection surface
 * (trust dropdown, patient-class filter, reconciliation grid, allocation
 * popups — fundInflowController.js is 1,541 lines). The PRIMARY list
 * endpoint is POST /api/billing/getAllfundInflowDetailsByTrust
 * (ctrl:421, getTrustInflowDetailsByTrustId) and that is the only call
 * rebuilt here; the trust dropdown's
 * GET /api/billing/getTrustList/{org}/{unit} (ctrl:397) and the allocation
 * popup calls are out of scope.
 *
 * NOTE on the grid: legacy's own fund-reconciliation.html renders a static
 * empty tbody (7 blank <td>s, never ng-repeat-bound — the controller
 * stores commonTrustInflowlist but the view never iterates it), so the
 * legacy screen itself always showed an empty grid. This rebuild instead
 * displays the real row projection the named query returns
 * (FundInFlowMaster.java:33-42: fundInFlowId, trustId, trustName,
 * totalCollectedAmount, fundtypeId, availableAmount, billConsumeAmount).
 * Live 2026-08-12: the endpoint returns SUCCESS with an empty listObject —
 * honest dev-empty, the @empty branch shows "No records." (no masked-error
 * banner: nothing is lying).
 */
@Component({
  selector: 'app-fund-reconciliation',
  standalone: true,
  imports: [],
  templateUrl: './fund-reconciliation.html',
  styleUrl: './fund-reconciliation.scss',
})
export class FundReconciliation {
  private readonly service = inject(FundReconciliationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<FundInflowRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service
      .getList({
        orgId: this.session.organizationId ?? 1,
        unitId: this.session.unitId ?? 1,
        trustId: 0,
      })
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'Could not load fund inflow details.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  rowCells(row: FundInflowRow): string[] {
    return [
      String(row.fundInFlowId ?? '—'),
      String(row.trustName ?? '—'),
      String(row.totalCollectedAmount ?? '—'),
      String(row.fundtypeId ?? '—'),
      String(row.availableAmount ?? '—'),
      String(row.billConsumeAmount ?? '—'),
    ];
  }
}
