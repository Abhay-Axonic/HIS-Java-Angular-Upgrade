import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { DisplayCallService } from './display-call.service';
import { QmsKioskOption } from './display-call.models';

/**
 * Parity rebuild of views/master/QMSMaster/QMS-display.html +
 * resources/assets/js/controller/qms/QMSTokenDisplayMasterCallController.js
 * (`QMSTokenDisplayMasterCallController`, `$scope`-style, no
 * `controllerAs`). Route `/displayCall`, moduleId 794 (app.js:20176-20203
 * resolve block, confirmed live), no `requireSuperUser`.
 *
 * **This is genuinely a thin, 1-field/1-real-API screen -- not a sign of a
 * stripped-down/broken rebuild.** It is a small kiosk-picker LAUNCHER, not
 * the actual QMS queue-display board. The real, full-featured, actively-
 * polling display board lives at a SEPARATE state (`display`,
 * `/display/:orgId/:unitId/:cookieskioskId/:userId`, template
 * `display.html`, controller `QMSTokenDisplayMasterController` -- note:
 * no "Call" in that name, a different file) which this screen's "Ok"
 * button opens in a new browser tab/window. That separate screen is
 * confirmed real (5 live backend calls, 2 of them polled every 2s) but is
 * NOT part of this batch -- out of scope here, left unbuilt.
 *
 * **Step 0 backend-existence check: CONFIRMED REAL.** The one real backend
 * call this screen makes, `POST /api/pharmacy/getActiveQMSMasterList`,
 * resolves to a genuine Controller -> Service -> DAO chain (see
 * display-call.service.ts's doc comment for citations). NOT a
 * `modalityTypeMaster`-style dead-backend screen.
 *
 * **Deliberate quirks/gaps preserved, not bugs in this rebuild:**
 * - Legacy auto-opens the kiosk-selection modal the instant
 *   `getActiveQMSMasterList` resolves (`angular.element('#qmsKioskDetailsModal').modal('show')`,
 *   ctrl:66-69) -- there is no button that opens it; it just appears on
 *   page load. Reproduced by opening the modal signal as soon as the list
 *   loads, not gating it behind any click.
 * - `$scope.kioskName` (view heading "Kiosk : {{kioskName}}") is set to
 *   `""` at init (ctrl:46) and never reassigned anywhere else in the
 *   controller (confirmed via grep) -- the heading is always blank in
 *   legacy today. Reproduced by never populating it.
 * - `imageBackground()` (ctrl:74-82, would call
 *   `GET /api/unit/applicationConfiguration/screen-images/{orgId}/3`) is
 *   defined but its only call site is commented out (ctrl:83:
 *   `// $scope.imageBackground();`) -- confirmed dead code, and the view
 *   this controller renders (QMS-display.html) has no `{{firstImg}}`
 *   binding at all either. Not implemented -- there is nothing live to
 *   port (the endpoint itself is real and used by the separate `display`
 *   screen above, just never reached from this one).
 * - `SetCookies()`/`getCookie('kioskIdForDisplay')` (ctrl:86-93) write/read
 *   a cookie that has no other consumer anywhere in this controller or
 *   view (only ever `console.log`'d) -- and the real `display` state
 *   receives its kiosk id via URL route param, not this cookie. Confirmed
 *   inert; not reproduced.
 * - "Issue Token URL" (view:129, `href="#QMS"`) links to the separate,
 *   unrelated `QMS` state/screen -- rendered as a plain anchor, matching
 *   legacy's own plain (non-`ui-sref`) `href`; that screen isn't part of
 *   this rebuild.
 * - `confirmQMS()` (ctrl:104-109) does `window.open(".../#/display/" + orgId + "/" + unitId + "/" + kioskId + "/" + userId)`
 *   -- reproduced as a same-origin relative-hash `window.open`, matching
 *   this codebase's pesapal-process-page precedent for linking to an
 *   out-of-scope route not yet built in this rebuild (it will not resolve
 *   to a working page today -- an expected, disclosed gap, not a bug in
 *   this screen).
 * - `$state` IS correctly injected in this controller's DI list (ctrl:7-8)
 *   and used correctly (ctrl:21-24) -- no missing-`$state`-DI crash bug
 *   here either.
 */
@Component({
  selector: 'app-display-call',
  imports: [FormsModule],
  templateUrl: './display-call.html',
  styleUrl: './display-call.scss',
})
export class DisplayCall {
  private readonly session = inject(SessionService);
  private readonly api = inject(DisplayCallService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- mirrors $scope.kioskName -- always blank, see class comment ----
  kioskName = signal('');

  activeQmsList = signal<QmsKioskOption[]>([]);
  selectedKioskId = signal<number | ''>('');
  showKioskModal = signal(false);

  constructor() {
    this.init();
  }

  // ---- mirrors $scope.initQMSMaster() + $scope.getQMSMasterList(), ctrl:18-71 ----
  private init(): void {
    this.api.getActiveQmsMasterList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.activeQmsList.set(res.status === 'success' ? (res.listObject ?? []) : []);
        // Legacy auto-opens the picker modal the instant this call
        // resolves (ctrl:68) -- not gated behind any button click.
        this.showKioskModal.set(true);
      },
    });
  }

  // ---- mirrors $scope.selectQMSKiosk(kioskId), ctrl:96-100 (the
  // SetCookies() round-trip it performs is confirmed inert, see class
  // comment -- not reproduced) ----
  onKioskSelected(kioskId: number | ''): void {
    this.selectedKioskId.set(kioskId);
  }

  // ---- mirrors $scope.confirmQMS(), ctrl:104-109. Legacy's modal
  // (`#qmsKioskDetailsModal`, view:139-173) has `data-keyboard="false"
  // data-backdrop="static"`, and its × close button is literally commented
  // out in the source (view:147: `<!-- <button ... data-dismiss="modal">
  // &times;</button> -->`) -- the only live footer control is "Ok"
  // (`data-dismiss="modal"` + `ng-click="confirmQMS()"`). Once this modal
  // auto-opens, legacy gives the user no way to dismiss it without picking
  // a kiosk and clicking Ok. Reproduced exactly: no `closeKioskModal()`/
  // cancel path here either -- not "improved" with an escape hatch legacy
  // deliberately (per the commented-out markup) doesn't offer. ----
  confirmQms(): void {
    const kioskId = this.selectedKioskId();
    const url = `${location.protocol}//${location.hostname}${location.port ? ':' + location.port : ''}/#/display/${this.organizationId}/${this.unitId}/${kioskId}/${this.userId}`;
    // Opens the separate, real `display` kiosk-board screen -- out of
    // scope for this rebuild batch, so this route will not resolve to a
    // working page yet. Expected, disclosed gap, see class comment.
    window.open(url);
  }
}
