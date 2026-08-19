import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {BloodBankService} from './blood-bag-collection-worklist.service';
import type {CollectionRow} from './blood-bag-collection-worklist.models';

/** Blood Bag Collection Worklist — matches the legacy worklist
 * (bloodBagCollectionWorklistController.js): one search endpoint, a from/to
 * date search, and a row action navigating to the bloodBagCollection form
 * (the save's owner, :121). No save on this screen. */
@Component({
  selector: 'app-blood-bag-collection-worklist',
  imports: [FormsModule],
  templateUrl: './blood-bag-collection-worklist.html',
  styleUrl: './blood-bag-collection-worklist.scss',
})
export class BloodBankComponent {
  private api = inject(BloodBankService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private router = inject(Router);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;

  readonly rows = signal<CollectionRow[]>([]);
  readonly loading = signal(false);
  readonly loaded = signal(false);
  readonly fromDate = signal('');
  readonly toDate = signal('');

  constructor() {
    this.search();
  }

  search(): void {
    this.loading.set(true);
    this.api.search({
      organizationId: this.orgId,
      unitId: this.unitId,
      fromDate: this.fromDate() || undefined,
      toDate: this.toDate() || undefined,
    }).subscribe({
      next: (res) => {
        this.rows.set((res as {listObject?: CollectionRow[]}).listObject ?? []);
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

  /** The legacy's row action: navigate to the collection form with the donor
   * (bloodBagCollectionWorklistController.js:121). */
  openDetails(r: CollectionRow): void {
    this.router.navigate(['/bloodBagCollection'], {state: {donor: r}});
  }
}
