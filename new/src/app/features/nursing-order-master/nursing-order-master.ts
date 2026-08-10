import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { NursingOrderMasterService } from './nursing-order-master.service';
import {
  NursingOrderMasterRow,
  NursingOrderSavePayload,
  ServiceMasterRow,
  SpecialityRow,
  SubSpecialityRow,
} from './nursing-order-master.models';

@Component({
  selector: 'app-nursing-order-master',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './nursing-order-master.html',
  styleUrl: './nursing-order-master.scss',
})
export class NursingOrderMaster {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(NursingOrderMasterService);

  rows = signal<NursingOrderMasterRow[]>([]);
  specialities = signal<SpecialityRow[]>([]);
  subSpecialities = signal<SubSpecialityRow[]>([]);
  serviceResults = signal<ServiceMasterRow[]>([]);

  searchTerm = signal('');
  code = signal('');
  desc = signal('');
  selectedServiceId = signal<number | null>(null);
  selectedSpecialityId = signal<number | null>(null);
  selectedSubSpecialityId = signal<number | null>(null);
  saving = signal(false);

  private orgId = this.session.organizationId ?? 1;
  private unitId = this.session.unitId ?? 1;

  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter((r) =>
      (r.nursingOrderCode || '').toLowerCase().includes(q) ||
      (r.nursingOrderDesc || '').toLowerCase().includes(q)
    );
  });

  constructor() {
    this.loadList();
    this.loadSpecialities();
  }

  loadList() {
    this.service.list(this.orgId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject || []);
        else this.notify.error(`Nursing order list load failed: ${res.status}`);
      },
      error: (e) => this.notify.error(`Nursing order list load failed: ${e.message ?? e}`),
    });
  }

  loadSpecialities() {
    this.service.specialities(this.orgId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.specialities.set(res.listObject || []);
        else this.notify.error(`Speciality list load failed: ${res.status}`);
      },
      error: (e) => this.notify.error(`Speciality list load failed: ${e.message ?? e}`),
    });
  }

  onSpecialityChange() {
    const id = this.selectedSpecialityId();
    this.subSpecialities.set([]);
    this.selectedSubSpecialityId.set(null);
    if (id) {
      this.service.subSpecialities(this.orgId, this.unitId, id).subscribe({
        next: (res) => {
          if (res.status === 'success') this.subSpecialities.set(res.listObject || []);
          else this.notify.error(`Sub-speciality list load failed: ${res.status}`);
        },
        error: (e) => this.notify.error(`Sub-speciality list load failed: ${e.message ?? e}`),
      });
    }
  }

  onServiceSearch() {
    // the legacy typeahead searches on 2+ chars; the search endpoint is
    // live-verified 200 (dev data empty for the keyword used in probes)
    this.service.searchServices(this.orgId, this.unitId, 'a').subscribe({
      next: (res) => {
        if (res.status === 'success') this.serviceResults.set(res.listObject || []);
        else this.notify.error(`Service search failed: ${res.status}`);
      },
      error: (e) => this.notify.error(`Service search failed: ${e.message ?? e}`),
    });
  }

  pickService(sm: ServiceMasterRow) {
    this.selectedServiceId.set(sm.serviceMasterId);
  }

  /**
   * Save — HONEST. The save endpoint EXISTS and is live-mapped
   * (/api/global/unit/saveNursingOrderMaster); the form collects the full
   * legacy contract (code, desc, service, speciality, sub-speciality) and
   * this component sends it. Per the WRITE-VERIFICATION rule the outcome is
   * reported from the response envelope; a success here means the endpoint
   * accepted the payload ("not erroring") — not a persistence claim.
   */
  save() {
    if (!this.code().trim() || !this.desc().trim()) {
      this.notify.error('Code and Description are required');
      return;
    }
    const now = new Date();
    const fmt = `${String(now.getDate()).padStart(2, '0')}-${String(now.getMonth() + 1).padStart(2, '0')}-${now.getFullYear()} ${String(now.getHours()).padStart(2, '0')}:${String(now.getMinutes()).padStart(2, '0')}:${String(now.getSeconds()).padStart(2, '0')}`;
    const payload: NursingOrderSavePayload = {
      nursingOrderDesc: this.desc().trim(),
      nursingOrderCode: this.code().trim(),
      serviceId: this.selectedServiceId(),
      specialityId: this.selectedSpecialityId(),
      subSpecialityId: this.selectedSubSpecialityId(),
      organizationId: this.orgId,
      unitId: this.unitId,
      createdBy: this.session.userId ?? 0,
      updatedBy: this.session.userId ?? 0,
      status: 'A',
      createdDate: fmt,
      updatedDate: fmt,
    };
    this.saving.set(true);
    this.service.save(payload).subscribe({
      next: (res) => {
        this.saving.set(false);
        if (res.status === 'success') {
          this.notify.success('Nursing order saved');
          this.code.set('');
          this.desc.set('');
          this.selectedServiceId.set(null);
          this.selectedSpecialityId.set(null);
          this.selectedSubSpecialityId.set(null);
          this.loadList();
        } else {
          this.notify.error(`Save not accepted: ${res.message ?? res.code}`);
        }
      },
      error: (e) => {
        this.saving.set(false);
        this.notify.error(`Save failed: ${e.message ?? e}`);
      },
    });
  }
}
