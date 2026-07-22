import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { QmsMasterCallService } from './qms-master-call.service';
import { QmsActiveDept, QmsKioskOption } from './qms-master-call.models';

/**
 * Rebuild of the legacy `QMSMasterCall` state -- see
 * QMSMasterTokenCallController.js (resources/assets/js/controller/qms/)
 * and views/master/QMSMaster/QMS-master-call.html. Route `/QMSMasterCall`
 * (path TBD by whoever wires app.routes.ts centrally), moduleId 794,
 * confirmed live from the state's own resolve block. No `requireSuperUser`.
 *
 * Renders with normal admin nav chrome in legacy
 * (`$rootScope.loginpage = true`, ctrl:39) -- the opposite of `QMS`'s
 * fullscreen kiosk mode. This codebase's Shell has no per-route chrome
 * toggle either way (see `display`/`displayCall`'s precedent: both nest
 * under the same `Shell` parent route regardless of their own
 * `loginpage` value) -- this screen simply renders under Shell normally,
 * which happens to already match its own `loginpage = true` intent (unlike
 * `QMS`, which does NOT match its `loginpage = false` intent -- a known,
 * already-accepted gap, not something this screen's rebuild needs to fix).
 *
 * **Real bug ported faithfully, not fixed:** `confirmQms()` below has the
 * exact same tautology guard as `QMS`'s version -- see qms.ts's class doc
 * comment for the full explanation. It never actually blocks an empty
 * kiosk selection.
 *
 * **Other faithfully-preserved behavior:**
 *  - `onKioskSelected()` writes the SAME `kioskIdForTokenGen` cookie `QMS`
 *    reads on load (ctrl:73-76/86-91) -- picking a kiosk here is a genuine,
 *    intentional hand-off: `QMS`'s own cold-start logic will skip its
 *    kiosk-picker modal and auto-confirm using this remembered value.
 *  - `kioskName` set from `res.listObject[0].qmsDesc` with no length guard
 *    (ctrl:~108), same unguarded-array-access risk as `QMS` -- ported
 *    as-is, not fixed.
 *  - The auto-show-modal call legacy's `getQMSMasterList()` has for `QMS`
 *    is commented out in THIS controller (ctrl:~68: `//angular.element(
 *    '#qmsKioskDetailsModal').modal('show');`) -- confirmed dead, and the
 *    view itself (QMS-master-call.html) has no modal markup at all, just a
 *    plain inline "Select Kiosk" dropdown. Not reproduced.
 *  - `confirmQms()`'s success handler does `window.open(BASE_URL+ROOT_URL+
 *    '/#/QMS')` (ctrl:~104-108) to open the real QMS kiosk screen in a NEW
 *    TAB -- reproduced as `window.open('/QMS', '_blank')`, matching this
 *    exact "opens in new tab" behavior. Legacy's commented-out
 *    `$state.go('QMS',{})` alternative (ctrl:109) is NOT live -- in-app
 *    navigation deliberately not used here.
 */
@Component({
  selector: 'app-qms-master-call',
  imports: [FormsModule],
  templateUrl: './qms-master-call.html',
  styleUrl: './qms-master-call.scss',
})
export class QmsMasterCall {
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(QmsMasterCallService);

  private readonly orgId = this.session.organizationId ?? 0;
  private readonly orgUnitId = this.session.unitId ?? 0;

  activeQmsList = signal<QmsKioskOption[]>([]);
  activeQmsDeptList = signal<QmsActiveDept[]>([]);
  kioskName = signal('');
  selectedKioskId = signal<number | ''>('');

  constructor() {
    this.init();
  }

  // ---- mirrors $scope.initQMSMaster() + $scope.getQMSMasterList(), ctrl:18-70 ----
  private init(): void {
    this.api.getActiveQmsMasterList(this.orgId, this.orgUnitId).subscribe({
      next: (res) => {
        this.activeQmsList.set(res.listObject ?? []);
        // Legacy's auto-show-modal call is commented out here -- confirmed
        // dead, not reproduced (see class doc comment).
      },
    });
  }

  // ---- mirrors $scope.selectQMSKiosk(kioskId), ctrl:92-96 -- persists the
  // SAME cookie `QMS` reads, then immediately calls confirmQms() (unlike
  // `QMS`'s own selectQMSKiosk, which only persists the cookie) ----
  onKioskSelected(kioskId: number | ''): void {
    this.selectedKioskId.set(kioskId);
    this.writeCookie('kioskIdForTokenGen', String(kioskId));
    this.confirmQms();
  }

  // ---- mirrors $scope.confirmQMS(), ctrl:98-118. Tautology guard ported
  // faithfully -- see class doc comment. ----
  confirmQms(): void {
    const kioskId = this.selectedKioskId();
    // eslint-disable-next-line no-constant-condition -- faithful port of a
    // legacy tautology bug, see class doc comment. Never actually gates.
    if (kioskId !== '' || kioskId != null) {
      this.api.getActiveDeptListByKioskId(this.orgId, this.orgUnitId, kioskId).subscribe({
        next: (res) => {
          this.loader.stop();
          const list = res.listObject ?? [];
          this.activeQmsDeptList.set(list);
          // Unguarded [0] access -- faithful port, see class doc comment.
          this.kioskName.set(list[0].qmsDesc ?? '');
          // Opens the real QMS kiosk screen in a new tab -- matches
          // legacy's window.open(...'#/QMS') exactly; the commented-out
          // $state.go('QMS') in-app-navigation alternative is not live.
          window.open('/QMS', '_blank');
        },
      });
    }
  }

  private writeCookie(name: string, value: string): void {
    document.cookie = `${name}=${encodeURIComponent(value)}; path=/`;
  }
}
