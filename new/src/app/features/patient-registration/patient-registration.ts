import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { PatientRegistrationService } from './patient-registration.service';

@Component({
  selector: 'app-patient-registration',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './patient-registration.html',
  styleUrl: './patient-registration.scss',
})
export class PatientRegistration {
  private svc = inject(PatientRegistrationService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected searching = signal(false);
  protected results = signal<unknown[]>([]);
  protected searchState = signal<'idle' | 'searched' | 'error'>('idle');
  protected uhid = signal('');
  protected name = signal('');
  protected mobile = signal('');

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }

  protected onSearch(): void {
    this.searching.set(true);
    this.svc.search(this.orgId(), this.unitId(), {
      uhidNumber: this.uhid(),
      patientName: this.name(),
      mobileNumber: this.mobile(),
    }).subscribe({
      next: (r) => {
        this.searching.set(false);
        if (r?.status === 'success') {
          this.results.set((r.listObject as unknown[]) ?? []);
          this.searchState.set('searched');
        } else {
          this.results.set([]);
          this.searchState.set('error');
          this.notify.error(r?.message || 'Search failed (the empty-criteria path returns a 501 error envelope — known backend behaviour)');
        }
      },
      error: () => {
        this.searching.set(false);
        this.searchState.set('error');
        this.notify.error('Search request failed');
      },
    });
  }

  protected clear(): void {
    this.uhid.set('');
    this.name.set('');
    this.mobile.set('');
    this.results.set([]);
    this.searchState.set('idle');
  }
}
