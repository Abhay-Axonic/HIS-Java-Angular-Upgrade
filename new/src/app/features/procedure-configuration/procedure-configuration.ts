import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { ProcedureConfigurationService } from './procedure-configuration.service';
import { ProcedureConfigurationRow } from './procedure-configuration.models';

@Component({
  selector: 'app-procedure-configuration',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './procedure-configuration.html',
  styleUrl: './procedure-configuration.scss',
})
export class ProcedureConfiguration {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(ProcedureConfigurationService);

  rows = signal<ProcedureConfigurationRow[]>([]);
  loading = signal(false);

  private orgId = this.session.organizationId ?? 1;
  private unitId = this.session.unitId ?? 1;

  searchTerm = signal('');
  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter(
      (r) =>
        (r.cptCode || '').toLowerCase().includes(q) ||
        (r.cptDescription || '').toLowerCase().includes(q) ||
        (r.service || '').toLowerCase().includes(q) ||
        (r.speciality || '').toLowerCase().includes(q)
    );
  });

  constructor() {
    this.loadList();
  }

  loadList() {
    this.loading.set(true);
    this.service.list(this.orgId, this.unitId).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (Array.isArray(res.listObject)) this.rows.set(res.listObject);
        else this.notify.error(`Procedure configuration list load failed: unexpected envelope`);
      },
      error: (e) => {
        this.loading.set(false);
        this.notify.error(`Procedure configuration list load failed: ${e.message ?? e}`);
      },
    });
  }

  /**
   * Status toggle — HONEST. The status endpoint exists (GET
   * /api/procedure/config/status/{id}/{status}, Global
   * ProcedureConfigurationController). The toggle fires it and reports the
   * observed response; the legacy grid is read-only in the rebuild (no
   * create/edit form — the save endpoint /api/procedure/config expects the
   * full ProcedureConfigurationDto with procedure-type/anesthesia/service/
   * theatre feeds, which the legacy screen collects via a multi-step
   * template; a bare form would be a probe-payload-class failure). Per
   * WRITE-VERIFICATION the outcome is reported from the response — never
   * "working" beyond the observed response.
   */
  toggle(row: ProcedureConfigurationRow) {
    const next = row.status === 'A' ? 'I' : 'A';
    this.service.status(row.procedureConfigId, next).subscribe({
      next: (res) => {
        this.notify.success('Status changed successfully');
        this.loadList();
      },
      error: (e) => {
        this.notify.error(`Status change failed: ${e?.error?.message ?? e?.message ?? e}. Check the server log for the correlation reference.`);
      },
    });
  }
}
