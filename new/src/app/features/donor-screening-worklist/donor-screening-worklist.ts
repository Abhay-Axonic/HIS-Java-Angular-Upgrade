import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {BloodBankService} from './donor-screening-worklist.service';
import type {DonorRow, DonorSearch} from './donor-screening-worklist.models';

/** Donor Screening Worklist — matches the legacy worklist
 * (donorScreeningWorklistController.js): one search endpoint, a donor search
 * form (donorNumber/donorName/uhidNumber/fromDate/toDate), and a row action
 * that navigates to the donorScreening form (the save's owner). No save on
 * this screen — the legacy has none. DATE CONVENTION: String dates via
 * formatLegacyNow — the registration/prescription convention. */
@Component({
  selector: 'app-donor-screening-worklist',
  imports: [FormsModule],
  templateUrl: './donor-screening-worklist.html',
  styleUrl: './donor-screening-worklist.scss',
})
export class BloodBankComponent {
  private api = inject(BloodBankService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private router = inject(Router);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  readonly rows = signal<DonorRow[]>([]);
  readonly loading = signal(false);
  readonly loaded = signal(false);
  readonly donorNumber = signal('');
  readonly donorName = signal('');
  readonly uhidNumber = signal('');
  readonly fromDate = signal('');
  readonly toDate = signal('');

  constructor() {
    this.search();
  }

  search(): void {
    this.loading.set(true);
    const criteria: DonorSearch = {
      donorNumber: this.donorNumber() || undefined,
      donorName: this.donorName() || undefined,
      uhidNumber: this.uhidNumber() || undefined,
      fromDate: this.fromDate() || undefined,
      toDate: this.toDate() || undefined,
      unitId: this.unitId,
      organizationId: this.orgId,
    };
    this.api.search(criteria).subscribe({
      next: (res) => {
        const list = (res as {listObject?: DonorRow[]}).listObject ?? [];
        this.rows.set(list);
        this.loading.set(false);
        this.loaded.set(true);
      },
      error: () => {
        this.loading.set(false);
        this.loaded.set(true);
        this.notify.error('Search failed');
      },
    });
  }

  /** The legacy's row action: navigate to the donorScreening form with the
   * selected donor (donorScreeningWorklistController.js:123-131). The form
   * owns the save. */
  openDetails(r: DonorRow): void {
    this.router.navigate(['/donorScreening'], {state: {donor: r}});
  }
}
