import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { ResourceConfigurationService } from './resource-configuration.service';
import { ResourceConfigurationRow } from './resource-configuration.models';

@Component({
  selector: 'app-resource-configuration',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './resource-configuration.html',
  styleUrl: './resource-configuration.scss',
})
export class ResourceConfiguration {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(ResourceConfigurationService);

  rows = signal<ResourceConfigurationRow[]>([]);
  loading = signal(false);

  private orgId = this.session.organizationId ?? 1;
  private unitId = this.session.unitId ?? 1;

  searchTerm = signal('');
  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter(
      (r) =>
        (r.procedureName || '').toLowerCase().includes(q) ||
        (r.specialityName || '').toLowerCase().includes(q) ||
        (r.serviceId ? String(r.serviceId).includes(q) : false)
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
        if (Array.isArray(res.listObject)) {
          this.rows.set(res.listObject);
        } else {
          // The backend returns HTTP 200 with body {status:'error', code:500}
          // when the DAO swallows an exception into the generic envelope.
          // Named here, not silent.
          const detail = res && (res as any).message ? (res as any).message : 'no detail';
          this.notify.error(`Resource configuration list load failed: the server reported "${detail}". Backend defect: the list query references a phantom column (otims.t_surgery_resource_config.visit_type_id does not exist — see README).`);
        }
      },
      error: (e) => {
        this.loading.set(false);
        this.notify.error(`Resource configuration list load failed: ${e.message ?? e}. Check the server log for the correlation reference.`);
      },
    });
  }

  /**
   * This screen is read-only in the rebuild. The full create/edit contract
   * (save/update/status + the speciality/tariff/visit-type/payment-entitlement/
   * bed-category feeds of the 840-line legacy controller) is a multi-step
   * form; a bare form sending a partial DTO would be a probe-payload-class
   * failure (the screen-15 lesson). The status toggle (POST
   * /api/otims/updateSurgeryResourceConfigStatus) is MAPPED; not wired here
   * — no row to toggle on (dev-empty table), and firing it blind would
   * report an unverifiable result. Per WRITE-VERIFICATION: never "working"
   * beyond an observed response.
   */
}
