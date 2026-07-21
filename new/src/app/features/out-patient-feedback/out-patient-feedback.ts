import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { OutPatientFeedbackService } from './out-patient-feedback.service';
import { OpfFeedbackItem, OpfSelectedItem } from './out-patient-feedback.models';

interface RatingRow {
  key: number;
  label: string;
}

/**
 * Rebuild of the legacy `outPatientFeedback` state -- see
 * outPatientFeedbackController.js (332 lines). No session guard anywhere
 * in the legacy controller (confirmed by full read) -- a genuinely public,
 * patient-facing feedback form, same shape as webAppointmentBooking.
 *
 * Route path simplification, disclosed: legacy's own url is
 * `/api/outPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:encounterId`
 * -- harmless there because ui-router uses hash-based URLs, so the browser's
 * actual HTTP path is always just index.html. This app's Router uses
 * PathLocationStrategy (no withHashLocation()), so keeping a literal `/api`
 * prefix on a frontend route would collide with this dev server's own
 * `/api/**` proxy rule (and likely any real reverse-proxy deployment) on
 * every direct/hard navigation -- the SPA would never even load. The `/api`
 * segment is dropped; all 6 params are kept, in the same order, otherwise
 * unchanged -- not a behavioral change, a structural necessity of
 * path-based routing.
 *
 * Params simplification, disclosed: legacy re-derives all 6 params from
 * `window.location.href.split("/")` instead of using the route's own
 * (mostly broken, see docs/ANGULAR_PENDING_FINDINGS.md entry 32)
 * `resolve` block -- entry 32 itself notes this has "zero observable
 * impact today". Reading the same 6 values via Angular's ActivatedRoute
 * params produces identical results for every real navigation and avoids
 * porting a fragile string-split hack for a bug with no observable effect.
 *
 * `feedbackSelectedList`-building simplification, disclosed: legacy
 * defines 14 separately-named handler functions (easeOfGettingFun,
 * informationRegardingFun, ...) that all do the exact same
 * find-or-push-by-key operation on one array. Consolidated into one
 * `rate(key, value)` method -- same final payload shape, no behavioral
 * difference, 14 identical one-liners collapsed into one.
 *
 * Star-widget simplification, disclosed: legacy's 5-star inputs mix
 * `ng-model="cleanlinessRating"` (which would bind the model to each
 * input's own `value` attribute on check) with an unrelated, separately-
 * numbered `ng-checked="cleanlinessRating === N"` condition -- the two
 * numbering schemes don't agree with each other, which reads as an
 * unresolved authoring quirk in legacy itself, not a deliberate design.
 * This rebuild uses one consistent 0-4 scale matching visual star position
 * (same `rate(11|12, value)` used elsewhere) rather than reconstructing
 * legacy's internally-contradictory exact numbering.
 *
 * NOT reproduced: legacy's star-widget wrapper divs carry
 * `onclick="selectStar(event, 'cleanliness')"`, a plain (non-Angular)
 * onclick calling a global `selectStar` that only exists as `$scope.selectStar`
 * -- unreachable from a bare onclick attribute. Confirmed no global
 * `window.selectStar` exists anywhere in the codebase (grepped). This throws
 * a real `ReferenceError` in the browser console on every star click, but
 * has zero effect on the actual rating (that's captured correctly via the
 * separate, working `ng-model`/`ng-change` bindings on each radio input,
 * which fire first). Logged as a new low-severity finding, not reproduced --
 * there is no equivalent "global scope" in this rebuild to even attach a
 * dead handler to, and reproducing a console-only, zero-impact error serves
 * no parity purpose.
 */
@Component({
  selector: 'app-out-patient-feedback',
  imports: [FormsModule],
  templateUrl: './out-patient-feedback.html',
  styleUrl: './out-patient-feedback.scss',
})
export class OutPatientFeedback {
  private readonly route = inject(ActivatedRoute);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(OutPatientFeedbackService);

  readonly frontOfficeRows: RatingRow[] = [
    { key: 1, label: 'Ease of getting an appointment' },
    { key: 2, label: 'Information regarding doctor' },
    { key: 3, label: 'Warmth and Courtesy at registration desk' },
  ];
  readonly waitingRoomRows: RatingRow[] = [
    { key: 4, label: 'Comfort while waiting for your turn' },
    { key: 5, label: 'Time taken while waiting for your turn' },
    { key: 6, label: 'Information of your position in the queue' },
  ];
  readonly visitRows: RatingRow[] = [
    { key: 7, label: 'Consultant Doctor' },
    { key: 8, label: 'Pharmacy' },
    { key: 9, label: 'Pathology' },
    { key: 10, label: 'Radiology' },
  ];
  readonly scaleValues = [0, 1, 2, 3, 4];

  private organizationId = 0;
  private unitId = 0;
  private patientId = 0;
  private visitType = '';
  private visitTypeId = '';
  private encounterId = 0;

  isAlreadyFilledUp = signal(false);
  sectionOfHospital = signal('');
  suggestions = signal('');
  cleanlinessRating = signal<number | undefined>(undefined);
  experienceRating = signal<number | undefined>(undefined);
  ratingValues = signal<Record<number, number>>({});

  private feedbackSelectedList: OpfSelectedItem[] = [];

  constructor() {
    const params = this.route.snapshot.paramMap;
    this.organizationId = Number(params.get('organizationId'));
    this.unitId = Number(params.get('unitId'));
    this.patientId = Number(params.get('patientId'));
    this.visitType = params.get('visitType') ?? '';
    this.visitTypeId = params.get('visitTypeId') ?? '';
    this.encounterId = Number(params.get('encounterId'));

    this.getOutPatientFeedback();
  }

  rate(key: number, value: number): void {
    this.ratingValues.update((v) => ({ ...v, [key]: value }));
    this.updateOrPushObject({ key, value });
  }

  onSectionOfHospitalChange(value: string): void {
    this.sectionOfHospital.set(value);
    this.updateOrPushObject({ key: 0, value });
  }

  onSuggestionsChange(value: string): void {
    this.suggestions.set(value);
    this.updateOrPushObject({ key: 13, value });
  }

  private updateOrPushObject(obj: OpfSelectedItem): void {
    const foundIndex = this.feedbackSelectedList.findIndex((item) => item.key === obj.key);
    if (foundIndex !== -1) {
      this.feedbackSelectedList[foundIndex] = obj;
    } else {
      this.feedbackSelectedList.push(obj);
    }
  }

  submitForm(): void {
    this.loader.start();
    const isSectionOfHospitalFilled = this.feedbackSelectedList.some((item) => item.key === 0);
    const isSuggestionsFilled = this.feedbackSelectedList.some((item) => item.key === 13);

    if (!isSectionOfHospitalFilled) {
      this.notify.warning('Please enter which section did you visited...!');
      this.loader.stop();
      return;
    }
    if (!isSuggestionsFilled) {
      this.notify.warning('Please enter any suggestions...!');
      this.loader.stop();
      return;
    }

    const now = formatDateTime(new Date());
    this.api
      .saveOutPatientFeedback({
        encounterId: this.encounterId,
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
            this.getOutPatientFeedback();
            this.notify.success(response.message ?? '');
          } else if (response.status === 'error') {
            this.notify.error(response.message ?? '');
          }
        },
      });
  }

  private getOutPatientFeedback(): void {
    this.loader.start();
    this.api.getOutPatientFeedback(this.unitId, this.organizationId, this.patientId, this.visitType, this.visitTypeId, this.encounterId).subscribe({
      next: (response) => {
        if (response.status === 'success') {
          this.loader.stop();
          const feedbackList = response.listObject ?? [];
          this.encounterId = response.object ?? this.encounterId;
          if (feedbackList.length > 0) {
            this.isAlreadyFilledUp.set(true);
            feedbackList.forEach((item) => this.setOutPatientFeedbackList(item));
          }
        } else if (response.status === 'error') {
          this.loader.stop();
          this.notify.error(response.message ?? '');
        }
      },
    });
  }

  private setOutPatientFeedbackList(value: OpfFeedbackItem): void {
    if (value.feedbackKey === 0) {
      this.sectionOfHospital.set(value.feedbackValue);
    } else if (value.feedbackKey === 13) {
      this.suggestions.set(value.feedbackValue);
    } else if (value.feedbackKey === 11) {
      this.cleanlinessRating.set(Number(value.feedbackValue));
      this.ratingValues.update((v) => ({ ...v, 11: Number(value.feedbackValue) }));
    } else if (value.feedbackKey === 12) {
      this.experienceRating.set(Number(value.feedbackValue));
      this.ratingValues.update((v) => ({ ...v, 12: Number(value.feedbackValue) }));
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
