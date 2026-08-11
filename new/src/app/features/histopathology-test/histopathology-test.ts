import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { HistopathologyTestService } from './histopathology-test.service';
import { HistopathologyTestRow } from './histopathology-test.models';

/**
 * Parity rebuild of the legacy `histopathologyTest` screen
 * (lis/unit/histopathologyTestController.js + views/master/LIS/
 * histopathology-test.html). Histopathology test master list (isHistoCyto
 * 'H'), keyword-searchable. Backend:
 * GET /LIS/unit/listTestMaster/{org}/{histo}/{unit}/{off}/{size}/{searchKey}.
 * Module id 559. Dev-empty: the backend answers status 'error' + "Test not
 * found." when the dev DB has no rows -- the message is surfaced plainly in
 * the error state (no masked banner), matching the batch-1 house pattern.
 */
@Component({
  selector: 'app-histopathology-test',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './histopathology-test.html',
  styleUrl: './histopathology-test.scss',
})
export class HistopathologyTest {
  private readonly service = inject(HistopathologyTestService);
  private readonly session = inject(SessionService);

  private static readonly HISTO_CYTO = 'H';
  private static readonly OFFSET = 0;
  private static readonly SIZE = 10;

  readonly rows = signal<HistopathologyTestRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');
  readonly form = signal<{ search: string }>({ search: '' });

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
    const keyword = this.form().search.trim();
    this.service
      .getList(
        this.orgId(),
        HistopathologyTest.HISTO_CYTO,
        this.unitId(),
        HistopathologyTest.OFFSET,
        HistopathologyTest.SIZE,
        keyword || null,
      )
      .subscribe({
        next: (res) => {
          this.loading.set(false);
          if (res.status === 'success') {
            this.rows.set(res.listObject ?? []);
          } else {
            this.error.set(res.message ?? 'Could not load histopathology tests.');
          }
        },
        error: (err) => {
          this.loading.set(false);
          this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
        },
      });
  }

  search(): void {
    this.load();
  }

  rowCells(row: HistopathologyTestRow): string[] {
    return [
      String(row.testCode ?? '—'),
      String(row.testDesc ?? '—'),
      String(row.testAlies ?? '—'),
      String(row.deptName ?? '—'),
      String(row.sampleName ?? '—'),
      String(row.techniqueName ?? '—'),
      String(row.normlTime ?? '—'),
      String(row.urgentTime ?? '—'),
    ];
  }
}
