import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { BookViewHandoffService } from '../../core/book-view-handoff.service';
import { BookViewPatientDetails } from './book-view.models';

/**
 * SCOPED REBUILD, disclosed: `bookViewMasterController.js` is 4,027 lines
 * with 81 distinct `$scope.X = function` assignments -- personal/family/
 * surgical/medical/travel history, diagnosis, investigations, group
 * services, template transactions, package-order tabs, discharge-summary
 * printing, a checklist modal, and more. This rebuild covers the
 * session/app-config guards, the CORRECTED patient-context safety check
 * (entry 28, the reason this screen was called out for extra care), and
 * the patient demographic header those checks gate. It does NOT cover
 * the full 81-function tab surface -- that is a substantial, separate
 * effort and building it superficially in the time available for this
 * batch would not meet this project's own verification bar. Held out
 * explicitly rather than shipped as if complete; see the batch 1c
 * consolidated report for the recommendation to treat the rest of this
 * screen as its own dedicated follow-up.
 *
 * Of the audited findings that DO fall inside this scoped surface:
 * - Entry 28 (SEVERITY 1, RESOLVED 2026-07-20): the fix that was found
 *   resolved during the whole-project retroactive audit. This component
 *   is built directly against the CURRENT, corrected
 *   `checkAppointment()` (bookViewMasterController.js:130-179, read
 *   fresh for this rebuild, not from memory of the original bug) --
 *   cross-checking `sessionStorage.cookieEncounterObject` against the
 *   `bookViewObj` handoff's `encounterId`/`patientId` before trusting
 *   either, redirecting instead of rendering on any mismatch. The old
 *   "trust sessionStorage unconditionally" bug is deliberately NOT
 *   reproduced -- reproducing a fixed patient-safety bug as this
 *   screen's "parity target" would be indefensible regardless of the
 *   project's usual faithful-replication default.
 * - Entry 17 (RESOLVED): `cookieObjectAppConfig` guard here already has
 *   a real `== undefined` check (confirmed at
 *   bookViewMasterController.js:44-48) -- this screen was never part of
 *   entry 17's original bug list, and its redirect target
 *   (`applicationConfiguration`) is entry 34's fallback screen, itself
 *   confirmed fixed.
 *
 * Redirect targets `bedManagement` and `encounterQueueManagement` are
 * real legacy states, not yet built in this rebuild (out of the current
 * 14-screen batch) -- same placeholder-target treatment as other
 * out-of-batch redirects this project has already used (e.g.
 * `viewAssetScheduler`'s guards).
 */
@Component({
  selector: 'app-book-view',
  templateUrl: './book-view.html',
  styleUrl: './book-view.scss',
})
export class BookView {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly handoff = inject(BookViewHandoffService);

  contextVerified = signal(false);
  patientDetails = signal<BookViewPatientDetails>({});

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }

    const appConfig = this.session.appConfig;
    if (appConfig == null) {
      this.router.navigate(['/applicationConfiguration']);
      return;
    }

    this.checkAppointment();
  }

  /** Faithful port of the CORRECTED checkAppointment()
   * (bookViewMasterController.js:130-179) -- see class doc comment. */
  private checkAppointment(): void {
    const bookViewObj = this.handoff.takeObject();
    const raw = sessionStorage.getItem('cookieEncounterObject');
    const encounterDetailsObj = raw ? JSON.parse(raw) : {};

    const hasStateContext = Object.keys(bookViewObj).length > 0;
    const contextMatches =
      hasStateContext &&
      Object.keys(encounterDetailsObj).length > 0 &&
      String(encounterDetailsObj.encounterId) === String(bookViewObj.encounterId) &&
      String(encounterDetailsObj.patientId) === String(bookViewObj.patientId);

    if (contextMatches) {
      this.patientDetails.set({
        patientName: encounterDetailsObj.patientName,
        uhIdNumber: encounterDetailsObj.uhIdNumber,
        genderCode: encounterDetailsObj.genderCode,
        age: encounterDetailsObj.age,
        birthDate: encounterDetailsObj.birthDate,
        patientId: encounterDetailsObj.patientId,
        encounterId: encounterDetailsObj.encounterId,
        admissionId: encounterDetailsObj.admissionId,
        genderId: encounterDetailsObj.genderId,
        visitTypeId: encounterDetailsObj.visitTypeId ?? 1,
        patientCategoryId: encounterDetailsObj.patientCategoryId,
      });
      this.contextVerified.set(true);
    } else {
      // No growl/notification here -- legacy's corrected checkAppointment()
      // redirects silently on a mismatch too (confirmed: no growl call in
      // that branch), not inventing user-facing messaging legacy doesn't have.
      this.router.navigate([hasStateContext ? '/bedManagement' : '/encounterQueueManagement']);
    }
  }
}
