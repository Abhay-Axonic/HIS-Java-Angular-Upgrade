import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { EmployeeSurveyService } from './employee-survey.service';
import { SurveyPoint } from './employee-survey.models';

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/employee-survey.html +
 * resources/assets/js/controller/master/unit/EmployeeSurveyController.js
 * (`EmployeeSurvey.Controller`). Self-registered state (its OWN
 * `.config($stateProvider)` block, EmployeeSurveyController.js:186-194) --
 * confirmed the ONLY registration of state name "employeeSurvey" anywhere
 * in the codebase (grepped app.js and the whole controller tree), so this
 * is not an addItemPOCreation-style duplicate/shadow case. `$state` IS in
 * this controller's DI list and the missing-cookie guard runs before
 * anything else -- no instance of the recurring missing-`$state`-DI crash
 * bug here.
 *
 * **No `data.moduleId` -- confirmed by reading the self-registered state
 * config directly: it has no `resolve` block at all**, same class as
 * `billingGroupMaster`/`periodicityService`. A screen that persists data
 * tied to the logged-in user having zero module gate is a faithful match to
 * legacy, not a rebuild gap -- see docs/ANGULAR_PENDING_FINDINGS.md's
 * standing note on this gap class.
 *
 * **`controllerAs: 'vm'` is declared but NOT load-bearing** (entry-11-style
 * finding, confirmed via full-file grep): every single assignment in the
 * controller is `$scope.*`, and the view has zero `vm.*` bindings anywhere
 * -- classic `$scope` wiring despite the `controllerAs` declaration.
 *
 * Backend confirmed fully real for both endpoints this screen's controller
 * defines (Controller -> Service -> DAO, table `public.t_employee_survey`)
 * -- see employee-survey.service.ts's doc comment. Golden-trace capture
 * (`tools/golden-trace/golden-traces/employeeSurvey.json`) independently
 * confirms the real initial-load signature: exactly ONE screen-specific
 * call, a GET of the static question-list JSON file -- no backend API call
 * at all on load, matching source exactly (`getEmployeeSurveyById` is
 * commented out, see the service's doc comment).
 *
 * **Faithfully reproduced quirks/bugs, not fixed:**
 * - **No `<form>` element wraps any of this screen's markup** (confirmed:
 *   zero `<form>` tags anywhere in employee-survey.html). The rating
 *   input's `min="1" max="10" required` HTML5 attributes (view:163) are
 *   consequently **fully decorative** -- there is no enclosing form for the
 *   browser to apply constraint validation against, and no JS-side clamp
 *   either (see next point). A user can submit any rating value, including
 *   blank, 0, or a value outside 1-10. Reproduced by using a plain
 *   `<input type="number" min="1" max="10">` with no enclosing `<form>` and
 *   no numeric clamping in `saveSurvey()`.
 * - **The view's own inline `<script>` block (employee-survey.html:203-215)
 *   defines `$scope.validateRating`, but no element in this same view ever
 *   calls it** (`grep -n "validateRating"` against the view matches only
 *   the definition itself) -- dead regardless of whether the embedded
 *   `<script>` tag even executes successfully in that context (it likely
 *   throws `ReferenceError: $scope is not defined`, since `$scope` is a
 *   controller-closure variable, not a global, and this script sits outside
 *   any Angular directive/controller scope). Not reproduced -- there is
 *   nothing live to reproduce (no rebuild equivalent of an inert,
 *   unreachable inline script).
 * - **`saveSurvey()`'s validation only requires ONE of the 19 real
 *   questions to have a selected radio option** (ctrl:57-70 loops the full
 *   `ESSList`, including the always-blank 20th "rating" pseudo-item, and
 *   sets a flag true the moment ANY item's `selectedOption` is non-empty) --
 *   not "all answered". Reproduced via `hasAnyAnswer()` below.
 * - **On a successful save, the question list reloads blank from the static
 *   JSON (`geInitialJson()` re-called, ctrl:94) but the numeric rating
 *   value is NEVER reset** (`$scope.survey.rating` is untouched in the
 *   success branch) -- confirmed via full-file read, not assumed. A second
 *   save right after a successful one silently resubmits whatever rating
 *   was last typed. Reproduced: `resetQuestionsOnly()` never touches
 *   `rating`.
 * - **Every one of the 20 saved rows carries the SAME `rating` value**
 *   (`EmployeeSurveyDaoImpl.saveEmployeeSurvey` sets
 *   `employeeSurvey.setRating(employeeSurveyDto.getRating())` inside the
 *   per-item loop, ctrl-side `data.rating` is a single top-level field
 *   applied identically to every list item) -- not a per-question rating,
 *   one shared value duplicated onto every persisted row. Reproduced by
 *   sending `rating` once at the payload's top level, exactly as legacy
 *   does (no client-side fan-out needed; the backend does the duplication).
 * - **An `error` response only growls** -- no reset of the form/list either
 *   way on failure, matching ctrl:101-107 exactly.
 */
@Component({
  selector: 'app-employee-survey',
  imports: [FormsModule],
  templateUrl: './employee-survey.html',
  styleUrl: './employee-survey.scss',
})
export class EmployeeSurvey {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(EmployeeSurveyService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  points = signal<SurveyPoint[]>([]);
  // Separate from `points` on purpose -- mirrors legacy's genuinely separate
  // `$scope.survey.rating` variable, deliberately never reset by a
  // successful save (see class doc comment).
  rating = signal<number | null>(null);

  constructor() {
    this.loadQuestions();
  }

  private loadQuestions(): void {
    this.api.getSurveyQuestions().subscribe({
      next: (sets) => this.points.set(sets[0]?.ESSList ?? []),
    });
  }

  updateSelectedOption(row: SurveyPoint, value: string): void {
    this.points.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, selectedOption: value } : r)));
  }

  private hasAnyAnswer(): boolean {
    return this.points().some((p) => p.selectedOption !== '');
  }

  saveSurvey(): void {
    if (!this.hasAnyAnswer()) {
      this.notify.error('Please select at least one point patient');
      return;
    }

    const now = formatDateTime(new Date());
    this.api
      .saveEmployeeSurvey({
        satisfactionList: this.points(),
        createdBy: this.userId,
        updatedBy: this.userId,
        userId: this.userId,
        updatedDate: now,
        createdDate: now,
        organizationId: this.organizationId,
        rating: this.rating() != null ? String(this.rating()) : '',
        unitId: this.unitId,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:93-95 -- reload the blank question list only; `rating`
            // is deliberately left untouched, see class doc comment.
            this.loadQuestions();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(date).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:19/77-78. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
