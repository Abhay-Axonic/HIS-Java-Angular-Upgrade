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

  /** True when the error is the generic lie-class envelope (empty reported as
   * "Internal Server Error" by the LIS fall-through) — drives the AMBER-
   * permanent banner (DQ-2026-08-12-003, BANNER-TIER RULE). */
  readonly maskedError = signal(false);

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
          // Two dev-empty classes (DQ-2026-08-12-003):
          //  - descriptive per-master message ("Sample Records not found.")
          //    = honest dev-empty; surface the backend's own message.
          //  - the GENERIC "Internal Server Error" envelope = the
          //    unreachable-empty-branch fall-through: the legacy service's
          //    `size() > 0` guard makes its inner isEmpty() branch dead, so an
          //    empty table falls through to ERR_500_MESSAGE. The response
          //    LIES (empty reported as a server error) — the banner must say
          //    WHY it is permanent (BANNER-TIER RULE, amber).
          const msg = res.message ?? `No records for '${key}'.`;
          this.rows.set([]);
          if (this.isGenericEnvelope(msg)) {
            this.maskedError.set(true);
            this.error.set(msg);
          } else {
            this.maskedError.set(false);
            this.error.set(msg);
          }
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.rows.set([]);
        this.maskedError.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  /** True when the message is the generic lie-class envelope (empty reported
   * as "Internal Server Error" by the LIS service fall-through). */
  private isGenericEnvelope(msg: string): boolean {
    const m = msg.toLowerCase();
    return m.includes('internal server error') || m.includes('something went wrong');
  }

  /** Column values for a row: prefer id/desc/code, fall back to first keys. */
  rowCells(row: CommonMasterEntry): string[] {
    const picked = [row['desc'], row['code'], row['status']];
    return picked.map((v) => (v === null || v === undefined ? '—' : String(v)));
  }
}
