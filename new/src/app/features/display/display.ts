import { Component, DestroyRef, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DisplayService } from './display.service';
import { QmsActiveDept, QmsTokenDisplayItem, UnitBillingAppConfig } from './display.models';

const POLL_INTERVAL_MS = 2000;

/**
 * Rebuild of the legacy `display` state (QMS Token Display kiosk screen) --
 * see QMSTokenDisplayMasterController.js and views/master/QMSMaster/display.html.
 * Full audit: docs/ANGULAR_PENDING_FINDINGS.md entry 33.
 *
 * This screen is deliberately unauthenticated (legacy uses
 * `GenericServiceForWithoutLogin`, and the route has no resolve block at
 * all -- confirmed at app.js:20149) -- it's a public kiosk display, not a
 * logged-in-user screen. No `moduleAccessGuard` on its route (see
 * app.routes.ts) -- adding one would invent a login requirement legacy
 * never had.
 *
 * Parity targets replicated deliberately, not fixed (owner has not
 * approved fixing either, per entry 33):
 *  - Only 3 department slots are ever rendered (a 4th active department is
 *    silently dropped -- entry 33's primary finding).
 *  - `getTokenList`/`getTokenMessage` start polling only after the first
 *    2s interval tick, not eagerly on load (a real, reproducible blank gap).
 *  - `getTokenMessage`'s TTS announcement has no de-duplication and will
 *    repeat every poll cycle the backend keeps returning the same message
 *    (entry 33's second finding) -- NOT deduped here, on purpose.
 *  - A poll cycle where the department/token list endpoint returns a null
 *    `listObject` leaves the previously rendered department panels frozen
 *    on stale data rather than clearing them (see loadTokenList below).
 *
 * One deliberate NON-parity addition: legacy's own two `$interval` polls
 * are never cancelled on scope destroy (no `$destroy` handler found in the
 * 269-line controller). That's harmless for a screen that in practice is
 * never navigated away from (a physical kiosk), but leaving both intervals
 * running forever in an SPA that can route elsewhere is a real resource
 * leak with no user-visible parity implication either way -- cleaned up
 * via DestroyRef here as ordinary Angular hygiene, not a behavior change.
 */
@Component({
  selector: 'app-display',
  imports: [],
  templateUrl: './display.html',
  styleUrl: './display.scss',
})
export class Display {
  private readonly route = inject(ActivatedRoute);
  private readonly api = inject(DisplayService);
  private readonly destroyRef = inject(DestroyRef);

  private readonly orgId: string;
  private readonly orgUnitId: string;
  private readonly cookieskioskId: string;

  private deptList: number[] = [];
  private callCounterToDisplay: QmsTokenDisplayItem[] = [];

  firstImg = signal<string | null>(null);
  unitBillingAppConfig = signal<UnitBillingAppConfig>({});
  activeQMSDeptList = signal<QmsActiveDept[]>([]);

  tokenDisplayList = signal<QmsTokenDisplayItem[]>([]);
  deptNumberFirstD = signal<number | null>(null);
  deptNumberSecondD = signal<number | null>(null);
  deptNumberThirdD = signal<number | null>(null);
  deptNumberFirstListD = signal<QmsTokenDisplayItem[]>([]);
  deptNumberSecondListD = signal<QmsTokenDisplayItem[]>([]);
  deptNumberThirdListD = signal<QmsTokenDisplayItem[]>([]);

  constructor() {
    const params = this.route.snapshot.paramMap;
    this.orgId = params.get('orgId') ?? '';
    this.orgUnitId = params.get('unitId') ?? '';
    this.cookieskioskId = params.get('cookieskioskId') ?? '';

    this.loadBillingAppConfig();
    this.loadScreenImage();
    this.loadActiveDeptList();

    const tokenListHandle = setInterval(() => this.loadTokenList(), POLL_INTERVAL_MS);
    const tokenMessageHandle = setInterval(() => this.loadTokenMessage(), POLL_INTERVAL_MS);
    this.destroyRef.onDestroy(() => {
      clearInterval(tokenListHandle);
      clearInterval(tokenMessageHandle);
    });
  }

  private loadBillingAppConfig(): void {
    this.api.getBillingAppConfig(this.orgId, this.orgUnitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.unitBillingAppConfig.set(res.object ?? {});
        }
      },
    });
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

  private loadActiveDeptList(): void {
    if (!this.cookieskioskId) return;
    this.api.getActiveDeptListByKioskId(this.orgId, this.orgUnitId, this.cookieskioskId).subscribe({
      next: (res) => {
        const list = res.listObject ?? [];
        this.activeQMSDeptList.set(list);
        this.deptList = list.map((dept) => dept.qmsDeptMasterId);
        // legacy also sets $scope.kioskName = list[0].qmsDesc here -- confirmed
        // dead, never bound anywhere in display.html, omitted.
      },
    });
  }

  private loadTokenList(): void {
    if (!this.cookieskioskId) return;
    this.api.getTokenListForDisplay(this.orgId, this.orgUnitId, this.cookieskioskId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;

        const newTokenDisplayList: QmsTokenDisplayItem[] = [];
        const listObject = res.listObject;

        if (listObject != null) {
          for (const value of listObject) {
            if (value.isCall == null || value.isCall !== 'A') {
              newTokenDisplayList.push(value);
            }
          }
          for (const value of listObject) {
            if (value.isCall != null && value.isCall === 'A') {
              newTokenDisplayList.push(value);
              const alreadyTracked = this.callCounterToDisplay.some(
                (e) => e.tokenNumberWithStartLetter === value.tokenNumberWithStartLetter,
              );
              if (!alreadyTracked) {
                this.speak(
                  `Token number, ${value.tokenNumberWithStartLetter}, please visit ${value.qmsCounterDesc} counter, thank you .`,
                );
                this.callCounterToDisplay.push(value);
              }
            }
            if (value.isCall !== 'A') {
              const idx = this.callCounterToDisplay.findIndex(
                (e) => e.tokenNumberWithStartLetter === value.tokenNumberWithStartLetter,
              );
              if (idx > -1) this.callCounterToDisplay.splice(idx, 1);
            }
          }
        }

        // Unconditional -- reflects newTokenDisplayList whether or not
        // listObject was present this cycle (empty array either way if it
        // was null), matching QMSTokenDisplayMasterController.js:132-167.
        this.tokenDisplayList.set(newTokenDisplayList);

        if (listObject == null) return;

        // Dept-slot computation only runs when listObject was present. A
        // null listObject leaves the previously rendered department panels
        // frozen on stale data rather than clearing -- see entry 33
        // addendum; preserved deliberately, not a bug this rebuild adds.
        let deptNumberFirst: number | null = null;
        let deptNumberSecond: number | null = null;
        let deptNumberThird: number | null = null;
        const deptNumberFirstList: QmsTokenDisplayItem[] = [];
        const deptNumberSecondList: QmsTokenDisplayItem[] = [];
        const deptNumberThirdList: QmsTokenDisplayItem[] = [];

        this.deptList.forEach((deptId, index) => {
          for (const token of newTokenDisplayList) {
            if (deptId !== token.assignedDeptId) continue;
            if (index === 0) {
              deptNumberFirst = deptId;
              if (token.isCall == null || token.isCall === 'N') deptNumberFirstList.push(token);
            } else if (index === 1) {
              deptNumberSecond = deptId;
              if (token.isCall == null || token.isCall === 'N') deptNumberSecondList.push(token);
            } else if (index === 2) {
              deptNumberThird = deptId;
              if (token.isCall == null || token.isCall === 'N') deptNumberThirdList.push(token);
            }
            // index 3 ("Four" slot) intentionally not modeled -- confirmed
            // dead in the view (never bound anywhere), see entry 33.
          }
        });

        this.deptNumberFirstD.set(deptNumberFirst);
        this.deptNumberSecondD.set(deptNumberSecond);
        this.deptNumberThirdD.set(deptNumberThird);
        this.deptNumberFirstListD.set(deptNumberFirstList);
        this.deptNumberSecondListD.set(deptNumberSecondList);
        this.deptNumberThirdListD.set(deptNumberThirdList);
      },
    });
  }

  private loadTokenMessage(): void {
    if (!this.cookieskioskId) return;
    this.api.getTokenDisplayMessage(this.orgId, this.orgUnitId, this.cookieskioskId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const listObject = res.listObject;
        if (listObject && listObject.length > 0) {
          // Deliberately unconditional, no de-duplication -- matches
          // legacy's real behavior exactly (entry 33 addendum). Not this
          // rebuild's place to silently fix a likely-repeated announcement.
          this.speak(listObject[0].qmsCallMassage);
        }
      },
    });
  }

  private speak(text: string): void {
    window.speechSynthesis.speak(new SpeechSynthesisUtterance(text));
  }
}
