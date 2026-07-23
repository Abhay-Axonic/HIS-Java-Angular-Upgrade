import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CorrectiveMaintenanceWorklistHandoffService, CorrectiveMaintenanceWorklistDetailObject } from '../../core/corrective-maintenance-worklist-handoff.service';
import { DetailsCorrectiveMaintenanceWorklistService } from './details-corrective-maintenance-worklist.service';
import { DcmwEmployee, DcmwSaveWorklistPayload } from './details-corrective-maintenance-worklist.models';

/**
 * Rebuild of the legacy `detailsCorrectiveMaintenanceWorklist` state -- see
 * detailsCorrectiveMaintenanceWorklistController.js (194 lines) +
 * details-corrective-maintenance-worklist.html (383 lines). No data.moduleId
 * (no resolve block), real cookieObject guard only -- same convention as
 * its parent `correctiveMaintenanceWorklist` route.
 *
 * Row data arrives via `CorrectiveMaintenanceWorklistHandoffService.takeObject()`,
 * the rebuild's equivalent of legacy's `$stateParams.detailsObj` (ctrl:58) --
 * this state is reached only via ui-router `params`, never a URL/route
 * param, so there is nothing to read from ActivatedRoute here.
 *
 * DEAD MARKUP omitted (confirmed inert copy-paste residue from a sibling
 * screen, zero trigger buttons and zero controller support in THIS screen's
 * own view/controller): the PMS Details modal, Accessories Details modal,
 * and Checklist/View-Checklist modals present in the legacy template
 * (view:196-336) are not rebuilt here.
 *
 * Status vocabulary for `maintenanceWorklistStatusId` (shared with the
 * parent worklist screen): 1=Requested, 2=Completed, 3=Assigned,
 * 4=Work In Progress, 5=Pending, 6=Cancelled, 7=Reopen.
 *
 * Lock rule (ctrl:59-61): the whole form -- including every Allocation
 * Details field and the Save button -- is read-only when
 * `maintenanceWorklistStatusId` is 4 (Work In Progress) or 2 (Completed).
 * Cancelled(6)/Reopen(7) rows remain editable; this is legacy's real
 * behavior (no `isDisabled` branch covers those two ids), faithfully
 * reproduced below rather than "fixed" to also lock them.
 *
 * Save/"assign" bug, faithfully reproduced (ctrl:129): `saveDetailsWorklist()`
 * ALWAYS hardcodes `maintenanceWorklistStatusId: 3` (Assigned) in the save
 * payload, regardless of the row's actual current status -- so saving a
 * Pending/Reopen/Cancelled row silently resets it to Assigned. Not fixed;
 * see `saveDetailsWorklist()` below.
 *
 * Validation ordering, faithfully reproduced (ctrl:123-192): unlike a
 * typical "validate before opening the confirm modal" flow, legacy's Save
 * button only opens the confirm modal (`data-toggle="modal"`, gated by
 * `ng-disabled="isDisabled"`) -- the "Please Select Assign To" / "Please
 * select scheduleDate" / "Please select scheduleTime" warnings all live
 * INSIDE `saveDetailsWorklist()`, which only runs after the modal's Ok
 * button is clicked (and the modal has already closed via
 * `data-dismiss="modal"` on that same button). So an invalid form pops the
 * confirm modal, closes it again on Ok, and only then shows the warning
 * growl. Reproduced the same way: `openSaveConfirm()` does no validation,
 * `confirmSave()` closes the modal and calls the validating save method.
 */
@Component({
  selector: 'app-details-corrective-maintenance-worklist',
  imports: [FormsModule, RouterLink],
  templateUrl: './details-corrective-maintenance-worklist.html',
  styleUrl: './details-corrective-maintenance-worklist.scss',
})
export class DetailsCorrectiveMaintenanceWorklist {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notification = inject(NotificationService);
  private readonly handoff = inject(CorrectiveMaintenanceWorklistHandoffService);
  private readonly api = inject(DetailsCorrectiveMaintenanceWorklistService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;
  private specialityId: number | undefined;

  detail = signal<CorrectiveMaintenanceWorklistDetailObject>({});
  isLocked = signal(false);

  usersList = signal<DcmwEmployee[]>([]);

  assignTo = signal<number | string>('');
  scheduleDateIso = signal('');
  scheduleTime = signal('');
  allocationRemark = signal('');

  showConfirmModal = signal(false);

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;
    // Read defensively -- see CookieObjectAppConfig in session.service.ts,
    // not guaranteed present the same way legacy's own read isn't guarded.
    this.specialityId = this.session.appConfig?.assetMaintenanceSpecialityId;

    this.detail.set(this.handoff.takeObject());
    this.initAllocationFields();
    this.getEmployeeList();
  }

  /**
   * Mirrors legacy's `$scope.init()` (ctrl:56-91) -- called once on load
   * and again (see `saveDetailsWorklist()`) after a successful save,
   * re-deriving the Allocation Details fields from `detailsObj`/defaults.
   * `detailsObj` itself is never mutated by a save in legacy (only local
   * scope vars are), so the re-run after save resets the visible form back
   * to its pre-edit state for the few seconds before the redirect fires.
   */
  private initAllocationFields(): void {
    const d = this.detail();
    this.isLocked.set(d.maintenanceWorklistStatusId === 4 || d.maintenanceWorklistStatusId === 2);

    if (d.correctiveMaintenanceWorklistId != null) {
      this.scheduleTime.set(d.scheduleTime ?? '');
      this.scheduleDateIso.set(parseLegacyDateToIso(d.scheduleDate));
      this.assignTo.set(d.assignTo ?? '');
      this.allocationRemark.set(d.allocationRemark ?? '');
    } else {
      const now = new Date();
      // Legacy's default schedule-time-of-day (ctrl:69-85) has an off-by-one
      // zero-pad bug -- see legacyPad() below. Reproduced exactly.
      this.scheduleTime.set(`${legacyPad(now.getHours())}:${legacyPad(now.getMinutes())}`);
      this.scheduleDateIso.set(formatYmd(now));
      this.assignTo.set('');
      this.allocationRemark.set('');
    }
  }

  private getEmployeeList(): void {
    this.loader.start();
    this.api.getEmployeeListBySpecialityAndSubSpeciality(this.organizationId, this.unitId, this.specialityId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.usersList.set(res.listObject ?? []);
        }
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  openSaveConfirm(): void {
    this.showConfirmModal.set(true);
  }

  closeSaveConfirm(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.saveDetailsWorklist();
  }

  /** ctrl:123-192 -- see class doc comment for the validation-ordering and
   *  always-status-3 quirks faithfully reproduced here. */
  private saveDetailsWorklist(): void {
    const assignTo = this.assignTo();
    const scheduleDateIso = this.scheduleDateIso();
    const scheduleTime = this.scheduleTime();

    if (assignTo === '') {
      this.notification.warning('Please Select Assign To');
      return;
    }
    if (!scheduleDateIso) {
      this.notification.warning('Please select scheduleDate');
      return;
    }
    if (scheduleTime === '') {
      this.notification.warning('Please select scheduleTime');
      return;
    }

    const now = formatLegacyNow();
    const d = this.detail();
    const payload: DcmwSaveWorklistPayload = {
      correctiveMaintenanceWorklistId: d.correctiveMaintenanceWorklistId ?? null,
      correctiveMaintenanceId: d.correctiveMaintenanceId,
      scheduleDate: isoToLegacyDateTime(scheduleDateIso),
      scheduleTime,
      // BUG faithfully reproduced (ctrl:129): always "Assigned", regardless
      // of `d.maintenanceWorklistStatusId`'s real prior value -- see class
      // doc comment.
      maintenanceWorklistStatusId: 3,
      assignTo,
      allocationRemark: this.allocationRemark(),
      createdBy: this.userId,
      createdDate: now,
      status: 'A',
      updatedBy: this.userId,
      updatedDate: now,
      unitId: this.unitId,
      organizationId: this.organizationId,
    };

    this.loader.start();
    this.api.saveWorklist(payload).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.initAllocationFields();
          this.notification.success(res.message ?? '');
          // Legacy: `$timeout(function () { $state.go('correctiveMaintenanceWorklist'); }, 3000)`.
          setTimeout(() => this.router.navigate(['/correctiveMaintenanceWorklist']), 3000);
        } else if (res.status === 'error') {
          this.notification.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}

/**
 * Legacy parses `scheduleDate` with `moment(value, "DD-MM-YYYY")` (ctrl:64) --
 * lenient parsing reading only the leading `DD-MM-YYYY` portion. Ported
 * here as a plain `yyyy-MM-dd` string for `<input type="date">` binding.
 */
function parseLegacyDateToIso(value: string | undefined | null): string {
  if (!value) return '';
  const match = /^(\d{2})-(\d{2})-(\d{4})/.exec(value);
  if (!match) return '';
  const [, dd, mm, yyyy] = match;
  return `${yyyy}-${mm}-${dd}`;
}

/** Legacy's save-time format, `moment($scope.scheduleDate).format('DD-MM-YYYY HH:mm:ss')`
 *  (ctrl:127) -- `scheduleDate` always carries a midnight time-of-day when
 *  built from a plain date input, so the time component here is always
 *  `00:00:00`. */
function isoToLegacyDateTime(iso: string): string {
  if (!iso) return '';
  const [yyyy, mm, dd] = iso.split('-');
  if (!yyyy || !mm || !dd) return '';
  return `${dd}-${mm}-${yyyy} 00:00:00`;
}

/** Legacy: `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')` (ctrl:133,136). */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

/** Plain `yyyy-MM-dd` for `<input type="date">`, matching this project's
 *  known Date-object-vs-date-input convention. */
function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}

/**
 * Legacy's default schedule-time-of-day builder (ctrl:69-85) has an
 * off-by-one zero-pad bug: it pads hours/minutes `< 9` (not `< 10`), so the
 * value 9 is left unpadded ("9") while 0-8 are zero-padded ("00".."08") and
 * 10+ need no padding anyway. Net effect: only the 9th hour/minute of any
 * hour renders unpadded. Reproduced exactly, not "fixed" to a uniform
 * `padStart(2, '0')`.
 */
function legacyPad(n: number): string {
  return n < 9 ? `0${n}` : `${n}`;
}
