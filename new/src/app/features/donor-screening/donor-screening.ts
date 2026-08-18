import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {DonorScreeningService} from './donor-screening.service';
import type {DonorScreeningDetail, ScreeningQuestion} from './donor-screening.models';

/** Donor Screening form — receives the donor from the worklist's navigation
 * state, loads the donor's existing screening detail, and saves the vitals +
 * screening result. The form's the save-owner (the worklist navigates here;
 * the legacy: donorScreeningWorklistDetailsController.saveDonorScreeningDetails). */
@Component({
  selector: 'app-donor-screening',
  imports: [FormsModule],
  templateUrl: './donor-screening.html',
  styleUrl: './donor-screening.scss',
})
export class BloodBankComponent {
  private api = inject(DonorScreeningService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private router = inject(Router);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  /** The donor arriving from the worklist (router state) or the history state. */
  readonly donor = signal<any | null>(null);
  readonly donorId = signal<number | null>(null);
  readonly loaded = signal(false);
  readonly saving = signal(false);
  readonly questions = signal<ScreeningQuestion[]>([]);

  readonly form = signal<DonorScreeningDetail>({});

  constructor() {
    const nav = this.router.getCurrentNavigation();
    const donor =
      (nav?.extras.state as {donor?: any} | undefined)?.donor ??
      (history.state as {donor?: any} | undefined)?.donor;
    const id = donor?.donorId ?? donor?.donorId ?? null;
    this.donor.set(donor ?? null);
    this.donorId.set(id);
    if (id) {
      this.load(id);
    } else {
      this.loaded.set(true);
    }
  }

  load(donorId: number): void {
    this.api.getDetail(donorId).subscribe({
      next: (res) => {
        const detail = (res as {listObject?: DonorScreeningDetail[]}).listObject?.[0] ?? {};
        const d = this.donor() ?? {};
        this.form.set({
          donorId,
          donorNumber: detail.donorNumber ?? d.donorNumber,
          donorName: detail.donorName ?? d.donorName,
          ...detail,
          unitId: this.unitId,
          organizationId: this.orgId,
        });
        this.loaded.set(true);
        const genderId = (detail as Record<string, unknown>)['genderId'] ?? (d as Record<string, unknown>)['genderId'];
        if (genderId) {
          this.api.questionsByGender(Number(genderId)).subscribe({
            next: (qr) =>
              this.questions.set((qr as {listObject?: ScreeningQuestion[]}).listObject ?? []),
            error: () => this.questions.set([]),
          });
        }
      },
      error: () => {
        this.loaded.set(true);
        this.notify.error('Could not load donor screening detail');
      },
    });
  }

  /** The legacy's temperature conversion toggle (calculateTemeperature). */
  toggleTempUnit(): void {
    const f = this.form();
    if (f.temperatureType === 'F' && f.donorTemperature != null) {
      f.donorTemperature = Math.round(((f.donorTemperature - 32) * 5) / 9);
      f.temperatureType = 'C';
    } else {
      f.temperatureType = f.temperatureType === 'F' ? 'C' : 'F';
    }
    this.form.set({...f});
  }

  toggleWeightUnit(): void {
    const f = this.form();
    f.weightType = f.weightType === 'lbs' ? 'kg' : 'lbs';
    this.form.set({...f});
  }

  toggleHBUnit(): void {
    const f = this.form();
    f.hBType = f.hBType === 'g/dl' ? 'g%' : 'g/dl';
    this.form.set({...f});
  }

  save(): void {
    if (!this.donorId()) {
      this.notify.error('No donor selected');
      return;
    }
    this.saving.set(true);
    const payload: DonorScreeningDetail = {
      ...this.form(),
      donorId: this.donorId(),
      unitId: this.unitId,
      organizationId: this.orgId,
      createdBy: this.userId,
      updatedBy: this.userId,
    };
    this.api.save(payload).subscribe({
      next: (res) => {
        this.saving.set(false);
        const ok = (res as {status?: string}).status === 'success';
        if (ok) {
          this.notify.success('Donor screening saved');
        } else {
          this.notify.error('Save failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Save failed');
      },
    });
  }
}
