import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { CancellationMasterService } from './cancellation-master.service';
import { CancelReasonRow } from './cancellation-master.models';

@Component({
  selector: 'app-cancellation-master',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './cancellation-master.html',
  styleUrl: './cancellation-master.scss',
})
export class CancellationMaster {
  private notify = inject(NotificationService);
  private service = inject(CancellationMasterService);

  rows = signal<CancelReasonRow[]>([]);
  loading = signal(false);

  searchTerm = signal('');
  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter(
      (r) =>
        (r.reason || '').toLowerCase().includes(q) ||
        (r.code || '').toLowerCase().includes(q)
    );
  });

  constructor() {
    this.loadList();
  }

  loadList() {
    this.loading.set(true);
    this.service.list().subscribe({
      next: (res) => {
        this.loading.set(false);
        if (Array.isArray(res)) this.rows.set(res);
        else {
          const detail = (res as any)?.message ?? 'unexpected envelope';
          this.notify.error(
            `Cancellation reasons load failed: the server reported "${detail}".`
          );
        }
      },
      error: (e) => {
        this.loading.set(false);
        this.notify.error(
          `Cancellation reasons load failed: ${e.message ?? e}. Check the server log for the correlation reference.`
        );
      },
    });
  }

  save() {
    // HONEST GAP: no save endpoint exists for cancel reasons (verified —
    // the ONLY endpoint for this screen is GET /api/global/cancel/reasons;
    // m_cancel_reason has no save/update/status mapping anywhere; the
    // entity is an FK reference for AssetManagement only). The legacy
    // template is a dead shell with an inert Save. Per the static-shell
    // rule the gap is reported in plain language, never faked.
    this.notify.error(
      'Save not available: there is no save endpoint for cancel reasons in the backend (only the list read exists). This is a backend gap, reported honestly.'
    );
  }
}
