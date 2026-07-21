import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { InPatientFeedbackService } from './in-patient-feedback.service';
import { IpfFeedbackItem, IpfSelectedItem } from './in-patient-feedback.models';

interface RatingRow {
  key: number;
  label: string;
}

/**
 * Rebuild of the legacy `inPatientFeedback` state -- see
 * inPatientFeedbackController.js (398 lines), same shape as
 * outPatientFeedback (see that component's doc comment for the shared
 * disclosed simplifications: dropped `/api` route prefix, ActivatedRoute
 * params instead of window.location.href splitting, consolidated per-row
 * handler functions into one `rate(key, value)`, dead onclick="selectStar"
 * not reproduced -- confirmed present here too, same unreachable
 * `$scope.selectStar` pattern, logged under the same finding).
 *
 * Unlike outPatientFeedback, there is no "section of hospital" textarea --
 * all 18 rating rows (keys 0-17) are standard 0-4 tables; only the
 * suggestions textarea (key 20) is required on submit, matching
 * `submitForm()`'s single `isSuggestionsFilled` check (key 0 has no
 * equivalent required-field check here).
 *
 * NOT reproduced (disclosed, zero observable effect): legacy's
 * `setInPatientFeedbackList` inconsistently leaves keys 0 and 13-19 as
 * raw strings while Number()-wrapping keys 1-12 -- irrelevant in practice
 * because this only runs when populating a previously-submitted response,
 * which is exactly when `isAlreadyFilledUp` hides the form those values
 * would render into (same "harmless in practice" class as entry 32).
 */
@Component({
  selector: 'app-in-patient-feedback',
  imports: [FormsModule],
  templateUrl: './in-patient-feedback.html',
  styleUrl: './in-patient-feedback.scss',
})
export class InPatientFeedback {
  private readonly route = inject(ActivatedRoute);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(InPatientFeedbackService);

  readonly frontOfficeRows: RatingRow[] = [
    { key: 0, label: 'Polite, Friendly and helpful' },
    { key: 1, label: 'Explanation of admission, Room, Facility and Tarrif' },
  ];
  readonly nursingRows: RatingRow[] = [
    { key: 2, label: 'Receiving on admission' },
    { key: 3, label: 'Response / attentiveness to your queries' },
    { key: 4, label: 'Timely Medication' },
    { key: 5, label: 'Clarity on communication' },
  ];
  readonly houseKeepingRows: RatingRow[] = [
    { key: 6, label: 'Cleanliness of Room / Toilet' },
    { key: 7, label: 'Prompt service delivery' },
    { key: 8, label: 'Polite, Friendly and helpful' },
  ];
  readonly doctorsRows: RatingRow[] = [
    { key: 9, label: 'Proper explanation of condition and treatment' },
    { key: 10, label: 'Courteous and concerned' },
    { key: 11, label: 'Counselling on approximate cost of treatment' },
  ];
  readonly dietaryRows: RatingRow[] = [
    { key: 12, label: 'Food quality' },
    { key: 13, label: 'On time service' },
    { key: 14, label: 'Diet Counselling' },
  ];
  readonly dischargeBillingRows: RatingRow[] = [
    { key: 15, label: 'Clear instructions for medications' },
    { key: 16, label: 'Explanation and clarification on billing' },
    { key: 17, label: 'Time taken for completing billing' },
  ];
  readonly scaleValues = [0, 1, 2, 3, 4];

  private organizationId = 0;
  private unitId = 0;
  private patientId = 0;
  private visitType = '';
  private visitTypeId = '';
  private admissionId = 0;

  isAlreadyFilledUp = signal(false);
  suggestions = signal('');
  cleanlinessRating = signal<number | undefined>(undefined);
  experienceRating = signal<number | undefined>(undefined);
  ratingValues = signal<Record<number, number>>({});

  private feedbackSelectedList: IpfSelectedItem[] = [];

  constructor() {
    const params = this.route.snapshot.paramMap;
    this.organizationId = Number(params.get('organizationId'));
    this.unitId = Number(params.get('unitId'));
    this.patientId = Number(params.get('patientId'));
    this.visitType = params.get('visitType') ?? '';
    this.visitTypeId = params.get('visitTypeId') ?? '';
    this.admissionId = Number(params.get('admissionId'));

    this.getInPatientFeedback();
  }

  rate(key: number, value: number): void {
    this.ratingValues.update((v) => ({ ...v, [key]: value }));
    this.updateOrPushObject({ key, value });
  }

  onSuggestionsChange(value: string): void {
    this.suggestions.set(value);
    this.updateOrPushObject({ key: 20, value });
  }

  private updateOrPushObject(obj: IpfSelectedItem): void {
    const foundIndex = this.feedbackSelectedList.findIndex((item) => item.key === obj.key);
    if (foundIndex !== -1) {
      this.feedbackSelectedList[foundIndex] = obj;
    } else {
      this.feedbackSelectedList.push(obj);
    }
  }

  submitForm(): void {
    this.loader.start();
    const isSuggestionsFilled = this.feedbackSelectedList.some((item) => item.key === 20);

    if (!isSuggestionsFilled) {
      this.notify.warning('Please enter any suggestions...!');
      this.loader.stop();
      return;
    }

    const now = formatDateTime(new Date());
    this.api
      .saveInPatientFeedback({
        admissionId: this.admissionId,
        patientId: this.patientId,
        visitType: this.visitType,
        visitTypeId: this.visitTypeId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        feedbackSelectedList: this.feedbackSelectedList,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (response) => {
          this.loader.stop();
          if (response.status === 'success') {
            this.getInPatientFeedback();
            this.notify.success(response.message ?? '');
          } else if (response.status === 'error') {
            this.notify.error(response.message ?? '');
          }
        },
      });
  }

  private getInPatientFeedback(): void {
    this.loader.start();
    this.api.getInPatientFeedback(this.unitId, this.organizationId, this.patientId, this.visitType, this.visitTypeId, this.admissionId).subscribe({
      next: (response) => {
        if (response.status === 'success') {
          this.loader.stop();
          const feedbackList = response.listObject ?? [];
          this.admissionId = response.object ?? this.admissionId;
          if (feedbackList.length > 0) {
            this.isAlreadyFilledUp.set(true);
            feedbackList.forEach((item) => this.setInPatientFeedbackList(item));
          }
        } else if (response.status === 'error') {
          this.loader.stop();
          this.notify.error(response.message ?? '');
        }
      },
    });
  }

  private setInPatientFeedbackList(value: IpfFeedbackItem): void {
    if (value.feedbackKey === 20) {
      this.suggestions.set(value.feedbackValue);
    } else if (value.feedbackKey === 18) {
      this.cleanlinessRating.set(Number(value.feedbackValue));
      this.ratingValues.update((v) => ({ ...v, 18: Number(value.feedbackValue) }));
    } else if (value.feedbackKey === 19) {
      this.experienceRating.set(Number(value.feedbackValue));
      this.ratingValues.update((v) => ({ ...v, 19: Number(value.feedbackValue) }));
    } else {
      this.ratingValues.update((v) => ({ ...v, [value.feedbackKey]: Number(value.feedbackValue) }));
    }
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
