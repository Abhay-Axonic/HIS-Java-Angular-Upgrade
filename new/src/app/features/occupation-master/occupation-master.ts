import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { OccupationMasterService } from './occupation-master.service';
import { OccupationForm, OccupationRow, emptyOccupationForm } from './occupation-master.models';

/**
 * Parity rebuild of views/master/GlobalMaster/occupation-master.html +
 * resources/assets/js/controller/adt/global/occupationMasterController.js
 * (`occupationMasterController`, classic `$scope`, no `controllerAs`).
 * moduleId 393 (app.js:3633-3654 resolve block, confirmed live). Backend
 * confirmed real and working (all 6 endpoints return HTTP 200 with a valid
 * token, checked 2026-07-21).
 *
 * **THE SCREEN'S CORE FUNCTIONALITY IS NON-FUNCTIONAL IN LEGACY TODAY, as
 * currently wired -- identical finding to this batch's sibling
 * `marital-status-master` (see that component's doc comment for the full
 * writeup); summarized here:**
 *
 * 1. `ng-click="saveOccupation()"` / `ng-click="updateOccupation()"`
 *    (view:48-49) call functions never defined anywhere in
 *    occupationMasterController.js. The controller's real `$scope.save`/
 *    `$scope.update` (ctrl:196-256, 333-384) are fully-implemented, working
 *    functions -- but unreachable from this view's buttons.
 * 2. `ng-repeat="info in occupationList"` (view:79) -- `$scope.occupationList`
 *    is never assigned; only `$scope.commonList` is (ctrl:110-116/156-162,
 *    populated by a real, successful `GETOCCUPATIONLIST` call that fires on
 *    every load). The table is permanently empty regardless of real
 *    server-side data.
 * 3. Form inputs bind to `occupation.occupationCode`/`occupation.occupationName`
 *    (view:16/30), but `save()`/`update()` build their payload from
 *    `$scope.common.code`/`$scope.common.desc` (ctrl:209-210, 343-344) -- a
 *    completely different, never-written-to scope object.
 *
 * Confirmed via a full read of this controller and view (not inferred).
 * Same 3-axis mismatch independently confirmed in this batch's
 * `maritalstatusMasterController.js`+marital-status-master.html and the
 * `relationMasterController.js`+relation-master.html sibling (batch-8,
 * built in parallel) -- same apparent copy-paste ancestor, same class of
 * bug as `bedBook` (docs/ANGULAR_PENDING_FINDINGS.md entry 53). Flagged for
 * docs/ANGULAR_PENDING_FINDINGS.md; not fixed here per standing parity
 * discipline -- faithfully reproduced below, same technique as
 * marital-status-master.
 *
 * **Confirmed divergence from the marital-status-master sibling:**
 * `update()`'s payload here (ctrl:341-347) omits a `status` field entirely;
 * marital's equivalent (ctrl:343-351) hardcodes `status:'A'`. Preserved in
 * `OccupationUpdatePayload` (models.ts) for documentation fidelity even
 * though `update()` is unreachable in both siblings.
 *
 * The controller's login-redirect guard (ctrl:30-34) correctly injects
 * `$state` -- no missing-`$state`-DI crash class here (entries 8/10). No
 * search box exists in this view at all.
 *
 * **Separate copy-paste residue bug, also faithfully reproduced:** the
 * legacy view's `<legend>` literally reads "Religion Master"
 * (occupation-master.html:5), not "Occupation Master" -- confirmed by a
 * direct read of the file, not a typo introduced here. Reproduced verbatim
 * in the template below.
 */
@Component({
  selector: 'app-occupation-master',
  imports: [FormsModule],
  templateUrl: './occupation-master.html',
  styleUrl: './occupation-master.scss',
})
export class OccupationMaster {
  private readonly session = inject(SessionService);
  private readonly api = inject(OccupationMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;

  // ---- form state (mirrors the view's own `occupation.*` bindings, view:16/30 --
  // NOT `$scope.common`, which is what the dead save()/update() actually read) ----
  form = signal<OccupationForm>(emptyOccupationForm());
  saveMode = signal(true);

  // ---- list + pagination plumbing (mirrors initOccupationMasterList/getOccupationMasterList) ----
  private readonly noOfRecordsPerPage = 10;
  private offset = 0;

  /** Mirrors `$scope.commonList` (ctrl:110-116) -- real data, really
   * fetched. Deliberately NOT read by the template; see class doc comment. */
  private readonly fetchedList = signal<OccupationRow[]>([]);

  /** Mirrors the legacy view's `occupationList` (view:79) -- never assigned
   * anywhere in occupationMasterController.js. Faithful bug reproduction:
   * this stays empty forever regardless of `fetchedList`. */
  renderList = signal<OccupationRow[]>([]);

  totalCount = signal(0);

  constructor() {
    this.loadList();
    this.loadCount();
  }

  private loadList(): void {
    this.api.getOccupationList(this.organizationId, this.offset, this.noOfRecordsPerPage).subscribe({
      next: (res) => {
        if (res.status === 'success') this.fetchedList.set(res.listObject ?? []);
      },
    });
  }

  private loadCount(): void {
    this.api.getOccupationCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  updateField<K extends keyof OccupationForm>(field: K, value: OccupationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  /** Confirmed-live silent no-op -- see doc comment below. Legacy's
   * occupation-master.html:48 wires `ng-click="saveOccupation()"` to a
   * function never defined anywhere in occupationMasterController.js. */
  onSaveClick(): void {
    // Confirmed live against the real legacy app (2026-07-21): clicking
    // Save/Update produces ZERO console output, not a thrown TypeError --
    // AngularJS's expression compiler guards function-call expressions
    // against an undefined target rather than invoking `undefined()`
    // directly, so `ng-click="saveOccupation()"` calling a scope method
    // that doesn't exist is a genuine silent no-op, not a crash. An
    // earlier version of this file reproduced this as a thrown TypeError,
    // which was verified WRONG by testing the real app directly -- true
    // no-op is the faithful reproduction, matching sibling relationMaster
    // (docs/ANGULAR_PENDING_FINDINGS.md, the adt/global disconnected-CRUD
    // cluster finding).
  }

  /** Confirmed-live silent no-op -- see onSaveClick doc comment. Legacy's
   * occupation-master.html:49 wires `ng-click="updateOccupation()"` to a
   * function never defined anywhere in occupationMasterController.js, and
   * AngularJS silently no-ops the call rather than throwing. */
  onUpdateClick(): void {
    // Intentionally empty -- see onSaveClick doc comment.
  }
}
