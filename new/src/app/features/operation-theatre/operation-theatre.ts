import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { OperationTheatreService } from './operation-theatre.service';
import { OperationTheatreRow } from './operation-theatre.models';

@Component({
  selector: 'app-operation-theatre',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './operation-theatre.html',
  styleUrl: './operation-theatre.scss',
})
export class OperationTheatre {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(OperationTheatreService);

  rows = signal<OperationTheatreRow[]>([]);
  loading = signal(false);

  private orgId = this.session.organizationId ?? 1;
  private unitId = this.session.unitId ?? 1;

  searchTerm = signal('');
  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter(
      (r) =>
        (r.theatreName || '').toLowerCase().includes(q) ||
        (r.theatreCode || '').toLowerCase().includes(q) ||
        (r.theatreDesc || '').toLowerCase().includes(q)
    );
  });

  constructor() {
    this.loadList();
  }

  loadList() {
    this.loading.set(true);
    this.service.list({ orgId: this.orgId, unitId: this.unitId, offset: 0, noOfRecordsPerPage: 50 }).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (Array.isArray(res.listObject)) this.rows.set(res.listObject);
        else {
          const detail = (res as any)?.message ?? 'unexpected envelope';
          this.notify.error(
            `Operation theatre list load failed: the server reported "${detail}".`
          );
        }
      },
      error: (e) => {
        this.loading.set(false);
        this.notify.error(
          `Operation theatre list load failed: ${e.message ?? e}. Check the server log for the correlation reference.`
        );
      },
    });
  }
}
