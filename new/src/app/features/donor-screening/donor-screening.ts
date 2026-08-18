import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {DonorScreeningService} from './donor-screening.service';
import type {DonorScreeningDetail, DonorScreeningMapper, ScreeningQuestion} from './donor-screening.models';

/** Donor Screening form — receives the donor from the worklist's navigation
 * state, loads the donor's existing screening detail (GET with the three
 * path-vars — DonorScreeningDetailController.java:51), renders the screening
 * questionnaire (temporary + permanent deferral questions, the legacy's
 * temporaryList/permanentList split at donor-screening.html:306/352), and
 * saves the vitals + result + the answers (donorScreeningMapperList — the
 * backend NPEs on null, DonorScreeningDetailController.java:36). */
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

  readonly donor = signal<any | null>(null);
  readonly donorId = signal<number | null>(null);
  readonly loaded = signal(false);
  readonly saving = signal(false);

  /** The questionnaire: temporary + permanent deferral questions, each with a
   * Yes/No answer and an optional per-question deferred-till date. */
  readonly temporaryQuestions = signal<ScreeningQuestion[]>([]);
  readonly permanentQuestions = signal<ScreeningQuestion[]>([]);
  readonly answers = signal<Record<number, {yes: boolean; deferredTill: string}>>({});

  readonly form = signal<DonorScreeningDetail>({});

  constructor() {
    const nav = this.router.getCurrentNavigation();
    const donor =
      (nav?.extras.state as {donor?: any} | undefined)?.donor ??
      (history.state as {donor?: any} | undefined)?.donor;
    const id = donor?.donorId ?? null;
    this.donor.set(donor ?? null);
    this.donorId.set(id);
    if (id) {
      this.load(id);
    } else {
      this.loaded.set(true);
    }
  }

  load(donorId: number): void {
    this.api.getDetail(donorId, this.orgId, this.unitId).subscribe({
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
          this.loadQuestions(Number(genderId));
        }
      },
      error: () => {
        // The GET endpoint 405/404'd with the old POST+single-var call; with the
        // corrected call the backend null-guards the no-record case (the service
        // returns an empty response rather than erroring — DonorRegistrationServiceImpl:200).
        this.loaded.set(true);
        this.notify.error('Could not load donor screening detail');
      },
    });
  }

  loadQuestions(genderId: number): void {
    this.api.questionsByGender(Number(genderId), this.orgId, this.unitId).subscribe({
      next: (qr) => {
        const list = (qr as {listObject?: ScreeningQuestion[]}).listObject ?? [];
        // The legacy split (donorScreeningWorklistDetailsController.js:133-158):
        // deferredTypeId == 1 -> temporary, else -> permanent.
        this.temporaryQuestions.set(list.filter((q) => q.deferredTypeId === 1));
        this.permanentQuestions.set(list.filter((q) => q.deferredTypeId !== 1));
      },
      error: () => {
        this.temporaryQuestions.set([]);
        this.permanentQuestions.set([]);
      },
    });
  }

  setAnswer(q: ScreeningQuestion, yes: boolean): void {
    const a = this.answers();
    const cur = a[q.id ?? -1] ?? {yes: false, deferredTill: ''};
    a[q.id ?? -1] = yes ? {...cur, yes} : {yes, deferredTill: ''};
    this.answers.set({...a});
  }

  setDeferredTill(q: ScreeningQuestion, date: string): void {
    const a = this.answers();
    const cur = a[q.id ?? -1] ?? {yes: true, deferredTill: ''};
    a[q.id ?? -1] = {...cur, deferredTill: date};
    this.answers.set({...a});
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

  /** Build the mapper list from the answered questions (the legacy's
   * save-assembly: each answered question -> one DonorScreeningMapper entry). */
  private buildMapperList(): DonorScreeningMapper[] {
    const donorId = this.donorId();
    const mappers: DonorScreeningMapper[] = [];
    for (const q of [...this.temporaryQuestions(), ...this.permanentQuestions()]) {
      const ans = this.answers()[q.id ?? -1];
      if (ans === undefined) {
        continue;
      }
      mappers.push({
        donorScreeningParameterId: q.id ?? null,
        parameterResultStatus: ans.yes ? 'Y' : 'N',
        donorDeferralTillDate: ans.yes ? ans.deferredTill || undefined : undefined,
        donorId: donorId ?? undefined,
        status: 'A',
        createdBy: this.userId,
        createdDate: new Date().toISOString(),
        unitId: this.unitId,
        organizationId: this.orgId,
      });
    }
    return mappers;
  }

  save(): void {
    if (!this.donorId()) {
      this.notify.error('No donor selected');
      return;
    }
    const mapperList = this.buildMapperList();
    if (!mapperList.length) {
      this.notify.error('Answer at least one screening question');
      return;
    }
    this.saving.set(true);
    const payload: DonorScreeningDetail = {
      ...this.form(),
      donorId: this.donorId(),
      donorScreeningMapperList: mapperList,
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
