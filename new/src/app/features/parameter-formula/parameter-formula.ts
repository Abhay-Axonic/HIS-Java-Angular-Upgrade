import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ParameterFormulaService } from './parameter-formula.service';
import { FormulaRow } from './parameter-formula.models';

/**
 * Parity rebuild of the legacy `parameterFormulaMaster` screen
 * (lis/unit/parameterFormulaController.js + views/master/LIS/
 * parameter-formula-master.html; rebuilt under the route `parameterFormula`).
 * Parameter formula master list. Backend:
 * GET /LIS/unit/formula/{org}/{unit}/{off}/{size}. Module id 566. Dev-empty:
 * the backend answers status 'error' + "Formula Records Not Found." when the
 * dev DB has no rows -- the message is surfaced plainly in the error state
 * (no masked banner), matching the batch-1 house pattern.
 */
@Component({
  selector: 'app-parameter-formula',
  standalone: true,
  templateUrl: './parameter-formula.html',
  styleUrl: './parameter-formula.scss',
})
export class ParameterFormula {
  private readonly service = inject(ParameterFormulaService);
  private readonly session = inject(SessionService);

  private static readonly OFFSET = 0;
  private static readonly SIZE = 10;

  readonly rows = signal<FormulaRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }
  private unitId(): number {
    return this.session.unitId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), this.unitId(), ParameterFormula.OFFSET, ParameterFormula.SIZE).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load parameter formulas.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: FormulaRow): string[] {
    return [
      String(row.paramDesc ?? '—'),
      String(row.formula ?? '—'),
      String(row.formulaLatex ?? '—'),
    ];
  }
}
