import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SelfRegistrationService } from './self-registration.service';

@Component({
  selector: 'app-self-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './self-registration.html',
  styleUrls: ['./self-registration.scss'],
})
export class SelfRegistration implements OnInit {
  private svc = inject(SelfRegistrationService);

  protected loading = signal(false);
  protected error = signal('');
  protected loaded = signal(false);
  protected countries = signal<any[]>([]);
  protected genders = signal<any[]>([]);
  protected identifications = signal<any[]>([]);
  protected maritalStatuses = signal<any[]>([]);
  protected prefixes = signal<any[]>([]);

  patient = {
    prefixId: null as number | null,
    firstName: '',
    lastName: '',
    genderId: null as number | null,
    countryId: null as number | null,
    phoneNo: '',
    identificationId: null as number | null,
    identificationNo: '',
    maritalStatusId: null as number | null,
    dob: '',
  };

  ngOnInit(): void {
    this.loadFeeds();
  }

  private loadFeeds(): void {
    this.loading.set(true);
    this.error.set('');
    // All reference feeds are GETs bound via the legacy unit_id/
    // organization_id header pair (screen-47 header-contract split class) —
    // live-verified 200 with real rows; each resolves independently.
    this.svc.getCountries(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.countries.set(r.listObject || []);
      },
      error: () => {},
    });
    this.svc.getGenders(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.genders.set(r.listObject || []);
      },
      error: () => {},
    });
    this.svc.getIdentifications(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.identifications.set(r.listObject || []);
      },
      error: () => {},
    });
    this.svc.getMaritalStatuses(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.maritalStatuses.set(r.listObject || []);
      },
      error: () => {},
    });
    this.svc.getPrefixes(1, 1).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.prefixes.set(r.listObject || []);
      },
      error: () => {},
    });
    this.loading.set(false);
    this.loaded.set(true);
  }
}
