import { Component, inject, signal } from '@angular/core';
import { StaffRegistrationService } from './staff-registration.service';
import { RelationRow } from './staff-registration.models';

/**
 * Parity rebuild of screen 650 `staffRegistration` — a staff-registration FORM.
 * Legacy state: app.js:14382-14386 (controller staffRegistrationController,
 * template views/AdmissionDischarge/staff-master.html); an older commented-out
 * block (app.js:11229-11251) bound the same state to the generic
 * MasterController with personal/contact/professional/employment sub-views.
 *
 * LIVE-VERIFIED CLASSIFICATION (DQ-2026-08-12-034): NOT a master list — 650 is
 * a registration/entry form (transaction class). The only live feed on its path
 * is the relation master list: GET /adt/getActiveRelationList
 * (GETACTIVERELATIONLIST, adt.url.config.js:343), verified live 2026-08-12:
 * SUCCESS, 2 rows {id, desc, code, createdBy, createdDate, status} — rendered
 * as a real feed table below. The staff-registration form itself is write-path,
 * deferred to the transaction methodology (DQ-025/032/034).
 */
@Component({
  selector: 'app-staff-registration',
  standalone: true,
  imports: [],
  templateUrl: './staff-registration.html',
  styleUrl: './staff-registration.scss',
})
export class StaffRegistration {
  private readonly service = inject(StaffRegistrationService);

  readonly rows = signal<RelationRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getRelations().subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'The relation feed could not be loaded.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: RelationRow): string[] {
    return [
      String(row.code ?? '—'),
      String(row.desc ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
