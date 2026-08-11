import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  CommonMasterService,
  CommonMasterEntry,
  COMMON_MASTER_DISPATCH,
} from './common-master.service';

/**
 * CommonMasterComponent — the rebuilt commonMaster dispatcher.
 *
 * Legacy shape (see docs/DECISIONS_QUEUE.md 'COMMONMASTER DISPATCH MECHANISM'):
 * one state at /commonMaster/:url whose resolve.getController switches on the
 * url param (43 branches) and injects the per-master controller via
 * controllerProvider into the single shared template common-master-screen.html.
 * The backend discriminates by endpoint path, not a discriminator field.
 *
 * This rebuild mirrors that: the route is commonMaster/:url, the component
 * reads the param, the service dispatches to the per-master endpoint, and one
 * generic grid renders the rows. A hardcoded single master would be
 * five-sixths broken and pass every gate — the param IS the mechanism.
 *
 * Slice scope (owner decision, option b): the 4 Unit Master branches
 * (phlebotomyMaster, headerMaster, departmentMaster, biochemicalTest) plus one
 * ADT real-data branch (relationmaster) to prove the data path. The remaining
 * branches are added in family batches.
 *
 * House pattern: signal() state + @if/@for blocks only (C9).
 */
@Component({
  selector: 'app-common-master',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './common-master.html',
  styleUrls: ['./common-master.scss'],
})
export class CommonMasterComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private service = inject(CommonMasterService);

  /** The :url dispatch value (e.g. 'phlebotomyMaster'). */
  readonly masterKey = signal('');
  /** Human label derived from the key (title case). */
  readonly masterTitle = signal('');
  readonly rows = signal<CommonMasterEntry[]>([]);
  readonly loading = signal(true);
  readonly error = signal('');
  /** True when the key has no configured endpoint yet (batch-pending branch). */
  readonly unsupported = signal(false);

  ngOnInit(): void {
    const key = this.route.snapshot.paramMap.get('url') ?? '';
    this.masterKey.set(key);
    this.masterTitle.set(this.titleFromKey(key));
    this.load(key);
  }

  private titleFromKey(key: string): string {
    if (!key) {
      return 'Common Master';
    }
    return key
      .replace(/([a-z0-9])([A-Z])/g, '$1 $2')
      .replace(/\b\w/g, (c) => c.toUpperCase());
  }

  private load(key: string): void {
    if (!key) {
      this.loading.set(false);
      this.error.set('No master selected.');
      return;
    }
    if (!this.service.hasMaster(key)) {
      this.loading.set(false);
      this.unsupported.set(true);
      return;
    }
    this.service.list(key, 1, 1, 0, 50).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
          this.error.set('');
        } else {
          // The 500 dev-empty envelope is the honest state for unseeded
          // masters ("Phlebotomy Records not found." etc.) — surface the
          // message rather than masking it.
          this.rows.set([]);
          this.error.set(res.message ?? `No records for '${key}'.`);
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.rows.set([]);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  /** Column values for a row: prefer id/desc/code, fall back to first keys. */
  rowCells(row: CommonMasterEntry): string[] {
    const picked = [row['desc'], row['code'], row['status']];
    return picked.map((v) => (v === null || v === undefined ? '—' : String(v)));
  }
}
