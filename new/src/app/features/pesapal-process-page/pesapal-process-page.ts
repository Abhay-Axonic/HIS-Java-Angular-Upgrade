import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { PesapalProcessPageService } from './pesapal-process-page.service';
import { TempAppointmentObject } from './pesapal-process-page.models';

/**
 * Rebuild of the legacy `pesapalProcessPage` state -- see
 * pesapalSuccessPageController.js (114 lines). Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entry 18 (SEVERITY 1).
 *
 * No `data.moduleId` on this screen's route (see app.routes.ts) -- legacy's
 * state has no resolve block, matching `moduleAccessGuard`'s behavior for
 * "must be logged in, no specific module gate" (same shape as
 * billingGroupMaster/periodicityService). Unlike `display`, this
 * controller has a REAL, working session check
 * (pesapalSuccessPageController.js:22-27, `$state.go('login')` on missing
 * `cookieObject`) -- `moduleAccessGuard` replicates the same effect at the
 * route level (redirect to `/accessDenied` instead of a literal `/login`,
 * per the same reasoning as auth.interceptor.ts's 401 handling).
 *
 * Parity target, owner-approved (not fixed): entry 18's crash is
 * faithfully replicated below -- a missing `pesapal_merchant_reference`
 * query param throws synchronously during construction, before the
 * payment-status call or final redirect ever run, leaving the user stuck
 * on "Wait your payment is in process...." indefinitely.
 */
@Component({
  selector: 'app-pesapal-process-page',
  imports: [],
  templateUrl: './pesapal-process-page.html',
  styleUrl: './pesapal-process-page.scss',
})
export class PesapalProcessPage {
  private readonly route = inject(ActivatedRoute);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PesapalProcessPageService);

  transactionId = signal<string | null>(null);

  constructor() {
    const queryParams = this.route.snapshot.queryParamMap;
    const transactionTrackingId = queryParams.get('pesapal_transaction_tracking_id');
    const merchantReference = queryParams.get('pesapal_merchant_reference');

    // Entry 18's crash, faithfully replicated: no null-check, matching
    // pesapalSuccessPageController.js:47 exactly (`param1.split("#")`).
    // queryParamMap.get() returns null (legacy's equivalent returns
    // undefined) when the param is absent -- either way this throws a
    // synchronous TypeError, the same observable crash class as legacy.
    const res = merchantReference!.split('#');
    const merchantId = res[0];

    this.transactionId.set(transactionTrackingId);
    this.loader.start();

    const unitId = this.session.unitId ?? 0;
    const organizationId = this.session.organizationId ?? 0;
    const callbackUrl = `${location.protocol}//${location.hostname}${location.port ? ':' + location.port : ''}/#/pesapalProcessPage`;

    this.api
      .getPesapayPaymentStatus(merchantId, transactionTrackingId ?? '', unitId, organizationId, callbackUrl)
      .subscribe({
        next: (response) => {
          const status = response.object?.transactionStatus;
          if (status === 'COMPLETED') {
            // no-op, matches legacy
          } else if (status === 'PENDING') {
            console.log('payment is pending');
          } else if (status === 'FAILED') {
            console.log('payment is failed');
          } else if (status === 'CONSUMED') {
            this.notify.warning('Already used ');
          } else {
            console.log('Invalid Transaction Id');
          }

          // sessionStorage key (including the "Objet" typo) matches
          // legacy exactly -- shared with selfRegistrationController.js,
          // not free to "correct". JSON.parse(null) legitimately returns
          // null in JS (no throw), matching legacy's own
          // `JSON.parse(sessionStorage.getItem(...))` call when the key
          // is absent.
          const tempAppointmentObj: TempAppointmentObject | null = JSON.parse(
            sessionStorage.getItem('tempAppointmentObjet') as string,
          );
          if (tempAppointmentObj != null) {
            tempAppointmentObj.isFromPesaPalSuccessPage = 'Y';
            tempAppointmentObj.pesapalPaymentStatus = status;
            tempAppointmentObj.pesaPalAmount = response.object?.amount;
            tempAppointmentObj.resTransactionId = response.object?.transactionId;
            tempAppointmentObj.transactionType = response.object?.transactionType;
            sessionStorage.setItem('tempAppointmentObjet', JSON.stringify(tempAppointmentObj));
          }

          // Unconditional regardless of transactionStatus, matching
          // legacy exactly -- targets `paymentInformation`, a screen
          // outside this 14-screen batch and not yet rebuilt. In a
          // co-located hybrid deployment this reaches legacy's real
          // screen; in this dev sandbox (separate origin/port) it will
          // not resolve to a working page yet -- an expected, out-of-scope
          // gap, not a bug in this rebuild.
          const redirectUrl = `${location.protocol}//${location.hostname}${location.port ? ':' + location.port : ''}/#/paymentInformation`;
          window.open(redirectUrl, '_self');
          this.loader.stop();
        },
      });
  }
}
