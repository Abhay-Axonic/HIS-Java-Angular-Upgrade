import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {SessionService} from '../../core/session.service';
import {ErWorklistService} from './er-mark-for-discharge.service';
import type {ErRow} from './er-mark-for-discharge.models';

/** ER Mark for Hospital Discharge — core scope (read-only grid) per docs/SPEC_ER_MODULE_2026-08-16.md. */
@Component({
  selector: 'app-er-mark-for-discharge',
  imports: [FormsModule],
  templateUrl: './er-mark-for-discharge.html',
  styleUrl: './er-mark-for-discharge.scss',
})
export class ErWorklistComponent {
  private api = inject(ErWorklistService);
  private session = inject(SessionService);
  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly rows = signal<ErRow[]>([]);
  readonly loading = signal(false);
  readonly loaded = signal(false);

  constructor() {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.api.list({organizationId: this.orgId, unitId: this.unitId}).subscribe({
      next: (r) => { this.loading.set(false); this.loaded.set(true);
        if (r.status === 'success') this.rows.set(r.listObject ?? []); },
      error: () => { this.loading.set(false); this.loaded.set(true); },
    });
  }
}
