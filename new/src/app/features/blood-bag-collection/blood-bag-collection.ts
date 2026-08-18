import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {Router} from '@angular/router';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {BloodBankService} from './blood-bag-collection.service';
import type {BagCollectionDetail, Examination, IdNameRow} from './blood-bag-collection.models';

/** Blood Bag Collection form — receives the donor from the worklist's
 * navigation state, loads the existing collection detail + examinations, and
 * saves the bag collection (the save + the examination child rows). Dates are
 * dd-M-yyyy HH:mm:ss strings; absent dates send '' (the backend's
 * GlobalCommonDateUtils NPEs on null). */
@Component({
  selector: 'app-blood-bag-collection',
  imports: [FormsModule],
  templateUrl: './blood-bag-collection.html',
  styleUrl: './blood-bag-collection.scss',
})
export class BloodBankComponent {
  private api = inject(BloodBankService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private router = inject(Router);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  readonly donor = signal<any | null>(null);
  readonly collectionId = signal<number | null>(null);
  readonly loaded = signal(false);
  readonly saving = signal(false);
  readonly camps = signal<IdNameRow[]>([]);
  readonly banks = signal<IdNameRow[]>([]);
  readonly capacities = signal<IdNameRow[]>([]);
  readonly reasons = signal<IdNameRow[]>([]);
  readonly exams = signal<Examination[]>([]);
  readonly form = signal<BagCollectionDetail>({});

  constructor() {
    const nav = this.router.getCurrentNavigation();
    const donor =
      (nav?.extras.state as {donor?: any} | undefined)?.donor ??
      (history.state as {donor?: any} | undefined)?.donor;
    const id = donor?.bloodBagCollectionId ?? donor?.donorId ?? null;
    this.donor.set(donor ?? null);
    this.collectionId.set(id);
    this.loadMasters();
    if (id) {
      this.load(id);
    } else {
      this.loaded.set(true);
    }
  }

  loadMasters(): void {
    this.api.camps().subscribe({next: (r) => this.camps.set((r as {listObject?: IdNameRow[]}).listObject ?? []), error: () => this.camps.set([])});
    this.api.bloodBanks().subscribe({next: (r) => this.banks.set((r as {listObject?: IdNameRow[]}).listObject ?? []), error: () => this.banks.set([])});
    this.api.capacities().subscribe({next: (r) => this.capacities.set((r as {listObject?: IdNameRow[]}).listObject ?? []), error: () => this.capacities.set([])});
    this.api.reasons().subscribe({next: (r) => this.reasons.set((r as {listObject?: IdNameRow[]}).listObject ?? []), error: () => this.reasons.set([])});
  }

  load(id: number): void {
    this.api.getDetail(id).subscribe({
      next: (res) => {
        const detail = (res as {listObject?: BagCollectionDetail[]}).listObject?.[0] ?? {};
        const d = this.donor() ?? {};
        this.form.set({...detail, donorId: detail.donorId ?? d.donorId, unitId: this.unitId, organizationId: this.orgId});
        this.loaded.set(true);
      },
      error: () => {
        this.loaded.set(true);
        this.notify.error('Could not load collection detail');
      },
    });
    this.api.getExaminations(id).subscribe({
      next: (r) => this.exams.set((r as {listObject?: Examination[]}).listObject ?? []),
      error: () => this.exams.set([]),
    });
  }

  addExam(): void {
    this.exams.set([...this.exams(), {examinationDate: '', pulse: null, bpSystolic: null, bpDiastolic: null}]);
  }

  private legacyNow(): string {
    const n = new Date();
    const p = (v: number) => String(v).padStart(2, '0');
    return `${p(n.getDate())}-${p(n.getMonth() + 1)}-${n.getFullYear()} ${p(n.getHours())}:${p(n.getMinutes())}:${p(n.getSeconds())}`;
  }

  private toLegacyDate(date: string): string {
    const [y, m, d] = date.split('-');
    return y && m && d ? `${d}-${m}-${y} 00:00:00` : '';
  }

  save(): void {
    if (!this.form().donorId) {
      this.notify.error('No donor selected');
      return;
    }
    this.saving.set(true);
    const now = this.legacyNow();
    const payload: BagCollectionDetail = {
      ...this.form(),
      collectionDate: this.form().collectionDate ? this.toLegacyDate(this.form().collectionDate as string) : '',
      fromTime: this.form().fromTime || '',
      toTime: this.form().toTime || '',
      examinationList: this.exams().map((e) => ({
        ...e,
        examinationDate: e.examinationDate ? this.toLegacyDate(e.examinationDate) : '',
      })),
      unitId: this.unitId,
      organizationId: this.orgId,
      createdBy: this.userId,
      updatedBy: this.userId,
      createdDate: now,
      updatedDate: now,
    };
    this.api.save(payload).subscribe({
      next: (res) => {
        this.saving.set(false);
        if ((res as {status?: string}).status === 'success') {
          this.notify.success('Bag collection saved');
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
