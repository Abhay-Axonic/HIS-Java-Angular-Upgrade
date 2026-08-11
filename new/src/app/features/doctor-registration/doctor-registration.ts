import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { DoctorRegistrationService } from './doctor-registration.service';
import { DoctorRegistrationRow } from './doctor-registration.models';

/**
 * Parity rebuild of the legacy `doctorRegistration` screen
 * (master/unit/doctorRegistrationController.js +
 * views/AdmissionDischarge/doctor-master.html).
 * Backend: POST /api/global/doctorDetailsUniversalSearch (body is the
 * doctorSearchObj: doctorName:'', specialityId:'0', docDesignationId:'0',
 * docCategoryId:'0', organizationId, unitId, typeId:1,
 * identificationTypeId:0, offset, noOfRecordsPerPage). Module id 651.
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty grid ("No records."), no error banner.
 */
@Component({
  selector: 'app-doctor-registration',
  standalone: true,
  imports: [],
  templateUrl: './doctor-registration.html',
  styleUrl: './doctor-registration.scss',
})
export class DoctorRegistration {
  private readonly service = inject(DoctorRegistrationService);
  private readonly session = inject(SessionService);

  readonly rows = signal<DoctorRegistrationRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }
  private unitId(): number {
    return this.session.unitId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), this.unitId()).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load doctors.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: DoctorRegistrationRow): string[] {
    return [
      String(row.docCode ?? '—'),
      `${row.firstName ?? ''} ${row.lastName ?? ''}`.trim() || '—',
      String(row.docCategoryDescription ?? '—'),
      String(row.docDesignationDescription ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
