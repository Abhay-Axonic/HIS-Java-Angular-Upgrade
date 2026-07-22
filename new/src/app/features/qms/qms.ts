import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { QmsService } from './qms.service';
import { QmsActiveDept, QmsAppConfigSecond, QmsKioskOption, QmsLabConfig } from './qms.models';

/**
 * Rebuild of the legacy `QMS` state -- the fullscreen kiosk token-issue
 * screen. See QMSMasterController.js (resources/assets/js/controller/qms/)
 * and views/master/QMSMaster/QMS.html.
 *
 * No `data.moduleId` on this screen's route (see app.routes.ts) -- legacy's
 * `QMS` state has no resolve block at all (no moduleId, no
 * requireSuperUser gate), confirmed by reading the state directly -- same
 * shape as `billingGroupMaster`/`periodicityService`.
 *
 * **Fullscreen/no-chrome divergence (legacy `$rootScope.loginpage = false`,
 * QMSMasterController.js:52):** this codebase's Shell architecture has no
 * per-route chrome-suppression mechanism -- confirmed by checking the
 * `display` screen's precedent (the other screen in this codebase with the
 * identical `loginpage = false` kiosk-fullscreen requirement in legacy):
 * `display`'s route is nested under the same `Shell` parent route as every
 * other screen in app.routes.ts, with no chrome-suppression flag of any
 * kind. This is a known, already-accepted simplification carried forward
 * unchanged here, not something introduced or newly deferred by this
 * screen -- see docs/ANGULAR_REBUILD_PLAYBOOK.md Step 3's Shell note and
 * `display`'s own route comment.
 *
 * **Real bug ported faithfully, not fixed:** `confirmQms()`'s guard below
 * mirrors legacy's `if($scope.common.qmsKioskMasterId!="" ||
 * $scope.common.qmsKioskMasterId!=undefined)` (QMSMasterController.js:112) --
 * a tautology (always true, since a value can't simultaneously be `""` AND
 * `undefined`) that never actually blocks an empty submission. Reproduced
 * exactly: the department-list call fires even with an empty kiosk id.
 *
 * **Other faithfully-preserved quirks, confirmed by reading the controller
 * in full:**
 *  - `kioskName` is set from `res.listObject[0].qmsDesc` with no length
 *    guard (ctrl:~128) -- if a kiosk genuinely has zero active departments,
 *    legacy throws here (same class of unguarded-array-access risk as the
 *    `assigningPickerDispenser` crash, entry 9). Not fixed, per standing
 *    policy -- ported as-is.
 *  - `getDeptDetails()`/`saveToken()` fire immediately on a department
 *    button click, with NO confirmation step -- the "Are you sure?" popup
 *    markup (`#tokenNumberPopup`) exists in QMS.html but its trigger
 *    (`data-toggle="modal" data-target="#tokenNumberPopup"`) is commented
 *    out on the department button (view:~106) -- confirmed dead, not
 *    reproduced.
 *  - `#tokenDisplayModal` (view markup, a "Print" button wired to
 *    `printTokenNumberReport()`) is never shown via any `.modal('show')`
 *    call anywhere in the controller -- confirmed dead, not reproduced.
 *    The actual print paths are `#tokenDisplayModalPRN` (PRN-printer
 *    branch) and `printTokenNumberReport()`'s own dynamically-opened
 *    popup window (browser-print branch) -- both reproduced below.
 *  - The kiosk-remembered-via-cookie fast path: if the
 *    `kioskIdForTokenGen` cookie is already set (from a previous visit),
 *    legacy skips the "Select Kiosk" modal entirely and auto-calls
 *    `confirmQms()` with the remembered id (ctrl:~87-92).
 */
@Component({
  selector: 'app-qms',
  imports: [FormsModule],
  templateUrl: './qms.html',
  styleUrl: './qms.scss',
})
export class Qms {
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(QmsService);

  private readonly orgId = this.session.organizationId ?? 0;
  private readonly orgUnitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  hospitalName = (this.session.current?.['unitNme'] as string | undefined) ?? '';

  firstImg = signal<string | null>(null);
  activeQmsList = signal<QmsKioskOption[]>([]);
  activeQmsDeptList = signal<QmsActiveDept[]>([]);
  kioskName = signal('');
  tokenNumber = signal('');
  selectedKioskId = signal<number | ''>('');
  showKioskModal = signal(false);
  showTokenPrnModal = signal(false);
  currentDate = '';
  currentTime = '';

  // ---- mirrors $scope.checkLabConfig()/prnUrl and the
  // cookieObjectAppConfigSecond/qmsPrnPrint read, ctrl:29-37 ----
  private readonly prnUrl: string | undefined;
  private readonly qmsPrnPrint: string | undefined;
  private qmsPrnPrintFormat = '';

  private readonly cookieskioskId: string | null;
  private deptDetails: QmsActiveDept | null = null;

  constructor() {
    const labConfig = this.readJsonLocalStorage<QmsLabConfig>('labConfig') ?? {};
    this.prnUrl = labConfig.prnUrl;

    const appConfigSecond = this.readJsonLocalStorage<QmsAppConfigSecond>('cookieObjectAppConfigSecond');
    this.qmsPrnPrint = appConfigSecond?.qmsPrnPrint;

    this.cookieskioskId = this.readCookie('kioskIdForTokenGen');

    this.loadScreenImage();
    this.loadQmsPrnFormat();
    this.loadQmsMasterList();
  }

  private loadScreenImage(): void {
    this.api.getScreenImage(this.orgId).subscribe({
      next: (res) => {
        if (res.object != null) {
          this.firstImg.set(res.object.imageURL);
        }
      },
    });
  }

  private loadQmsPrnFormat(): void {
    this.api.getQmsPrnFormat(this.orgUnitId).subscribe({
      next: (res) => {
        this.qmsPrnPrintFormat = res.listObject?.[0]?.qmsPrnPrintFormat ?? '';
      },
    });
  }

  private loadQmsMasterList(): void {
    this.api.getActiveQmsMasterList(this.orgId, this.orgUnitId).subscribe({
      next: (res) => {
        this.activeQmsList.set(res.listObject ?? []);
        if (!this.cookieskioskId) {
          this.showKioskModal.set(true);
        } else {
          this.selectedKioskId.set(Number(this.cookieskioskId));
          this.confirmQms();
        }
      },
    });
  }

  // ---- mirrors $scope.selectQMSKiosk(kioskId), ctrl:96-100 -- only
  // persists the cookie, does NOT call confirmQms() itself (unlike
  // QMSMasterCall's version of this same-named function) ----
  onKioskSelected(kioskId: number | ''): void {
    this.selectedKioskId.set(kioskId);
    this.writeCookie('kioskIdForTokenGen', String(kioskId));
  }

  // ---- mirrors $scope.confirmQMS(), ctrl:110-133. Tautology guard ported
  // faithfully -- see class doc comment. ----
  confirmQms(): void {
    const kioskId = this.selectedKioskId();
    // eslint-disable-next-line no-constant-condition -- faithful port of a
    // legacy tautology bug, see class doc comment. Never actually gates.
    if (kioskId !== '' || kioskId != null) {
      this.api.getActiveDeptListByKioskId(this.orgId, this.orgUnitId, kioskId).subscribe({
        next: (res) => {
          this.loader.stop();
          const list = (res.listObject ?? []).map((dept) => {
            let discripton = dept.discripton;
            if (dept.qmsDeptName === 'Unregistered') discripton = 'For Walk-in Patients';
            if (dept.qmsDeptName === 'Registered') discripton = 'For Already Registered Patients';
            return { ...dept, discripton };
          });
          this.activeQmsDeptList.set(list);
          // Unguarded [0] access -- faithful port, see class doc comment.
          this.kioskName.set(list[0].qmsDesc ?? '');
          // Modal dismissal itself happens synchronously on the "Ok" click
          // (template's `data-dismiss="modal"` equivalent), decoupled from
          // this async response completing -- matches legacy's Bootstrap
          // `data-dismiss="modal"` attribute on the same button.
        },
      });
    }
  }

  // ---- mirrors $scope.getDeptDetails(obj), ctrl:150-153 -- fires
  // saveToken() immediately, no confirmation step (see class doc comment) ----
  getDeptDetails(dept: QmsActiveDept): void {
    this.deptDetails = dept;
    this.saveToken();
  }

  // ---- mirrors $scope.saveToken(), ctrl:156-183 ----
  private saveToken(): void {
    if (!this.deptDetails) return;
    this.tokenNumber.set('wait...');
    this.loader.start();

    this.api
      .saveToken({
        qmsKioskMasterId: this.deptDetails.qmsKioskMasterId,
        startLetter: this.deptDetails.startLetter,
        startNumber: this.deptDetails.startNumber,
        qmsDeptMasterId: this.deptDetails.qmsDeptMasterId,
        createdBy: this.userId,
        updatedBy: this.userId,
        organisationId: this.orgId,
        unitId: this.orgUnitId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          this.tokenNumber.set(res.object ?? '');
          setTimeout(() => {
            if (this.prnUrl && this.qmsPrnPrint === 'Y' && this.qmsPrnPrintFormat) {
              this.showTokenPrnModal.set(true);
              this.printPrnPrint(this.qmsPrnPrintFormat);
            } else {
              this.printTokenNumberReport();
            }
          }, 2000);
        },
      });
  }

  // ---- mirrors $scope.printPRNPrint(prnObject), ctrl:192-211. Sends a
  // raw text POST straight to the configured local printer URL via native
  // fetch -- deliberately bypassing HttpClient/authInterceptor, matching
  // legacy's own raw XMLHttpRequest that never goes through GenericService
  // (this isn't one of our backend endpoints; it's an arbitrary
  // device-local print-server address from `labConfig.prnUrl`). ----
  private printPrnPrint(prnFormat: string): void {
    setTimeout(() => this.showTokenPrnModal.set(false), 4000);

    const now = new Date();
    const text = prnFormat
      .replace('@tokenNumber', this.tokenNumber())
      .replace('@date', now.toISOString().slice(0, 10))
      .replace('@time', now.toTimeString().slice(0, 5));

    fetch(this.prnUrl as string, {
      method: 'POST',
      headers: { 'Content-Type': 'text/plain' },
      body: text,
    }).catch(() => {
      // legacy's raw XHR only logs status/response in onreadystatechange;
      // no user-facing error handling on failure either -- matched here.
    });
  }

  // ---- mirrors $scope.printTokenNumberReport(), ctrl:216-227 ----
  private printTokenNumberReport(): void {
    const now = new Date();
    this.currentDate = this.formatDate(now);
    this.currentTime = now.toTimeString().slice(0, 8);

    const printContents = `
      <table style="font: 14px monospace !important;">
        <tbody>
          <tr><td><span><font size="4"><b>&nbsp;&nbsp;&nbsp;${this.hospitalName}</b></font></span></td></tr>
          <tr><td><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Your Token Number </span></td></tr>
          <tr><td><span><font size="6"><b>&nbsp;&nbsp;&nbsp;&nbsp;${this.tokenNumber()}</b></font></span></td></tr>
          <tr><td><p>Please wait for your turn</p></td></tr>
          <tr><td>${this.currentDate}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${this.currentTime}</td></tr>
        </tbody>
      </table>`;

    const popupWin = window.open('', '_blank', 'width=400,height=400');
    if (!popupWin) return;
    popupWin.document.open();
    popupWin.document.write(
      `<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">${printContents}</body></html>`,
    );
    popupWin.document.close();
  }

  private formatDate(d: Date): string {
    const dd = String(d.getDate()).padStart(2, '0');
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    return `${dd}-${mm}-${d.getFullYear()}`;
  }

  private readJsonLocalStorage<T>(key: string): T | null {
    try {
      const raw = localStorage.getItem(key);
      return raw ? (JSON.parse(raw) as T) : null;
    } catch {
      return null;
    }
  }

  private readCookie(name: string): string | null {
    const prefix = `${name}=`;
    const match = document.cookie.split('; ').find((row) => row.startsWith(prefix));
    return match ? decodeURIComponent(match.substring(prefix.length)) : null;
  }

  private writeCookie(name: string, value: string): void {
    document.cookie = `${name}=${encodeURIComponent(value)}; path=/`;
  }
}
