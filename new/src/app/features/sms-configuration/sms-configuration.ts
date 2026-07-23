import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { SmsConfigurationService } from './sms-configuration.service';
import { CategoryOption, MobileNumberEntry, RequiredDataOption, SmsUtilityConfiguration, TriggerOption, WhatsAppTemplateOption } from './sms-configuration.models';
import {
  createPaginationState,
  getPagesList,
  PaginationState,
  setFirstPage,
  setLastPage,
  setNextPage,
  setPage,
  setPreviousPage,
  setTotalPages,
} from '../../core/client-pagination.util';

/**
 * Rebuild of the legacy `smsUtilityConfiguration` state -- see
 * smsUtilityConfigurationController.js (705 lines) + view
 * sms-utility-configuration.html (689 lines). Two tabs ("Triggered SMS" /
 * "Scheduled SMS", `firstTabLevel` 1/2 in legacy) sharing one screen; each
 * tab has its own Trigger Point + Category dropdowns, a "Required Data"
 * picker that appends a token into a Template Description textarea, and a
 * grid below with an edit pencil + inline active/inactive status toggle.
 * Module id 774 gate is enforced entirely by `moduleAccessGuard` at the
 * route layer (`data: { moduleId: 774 }`), not in this component.
 *
 * **Two bugs faithfully reproduced, per the Step 0/1 audit -- NOT fixed:**
 *
 * 1. Dead "default SMS to Y" safety net (ctrl:244-246): legacy checks/writes
 *    `$scope.common.isSmsSend` (lowercase "ms") when the actually-bound
 *    checkbox model and save payload both use `common.isSMSSend` (uppercase
 *    "MS", ctrl:59, 323, view:59). The case-typo means this "if both
 *    channels are off, default SMS to Y" branch reads/writes a property
 *    that nothing else in the controller ever looks at -- it is dead code.
 *    Net effect: a user CAN save a config with BOTH `isSMSSend` and
 *    `isWhatsappSend` left `'N'`. This rebuild's `save()` validation
 *    deliberately does NOT add any such default -- see the comment at the
 *    validation site below.
 * 2. Duplicate `clearMobileTempFields` definition (ctrl:441-443 vs.
 *    ctrl:513-517): legacy defines this function twice under the same
 *    name; the SECOND definition silently wins for BOTH call sites, because
 *    both the Tab-1 "Clear" button next to the User-Defined-Mail mini-list
 *    (view:212, category 15 section) and the Tab-2 "Clear" button next to
 *    the Schedule Mobile Number mini-list (view:547, category 9 section)
 *    call the exact same function name. The winning definition only
 *    touches Tab-2/`commonSchedule` state -- so clicking Tab-1's Clear
 *    button clears TAB-2's mobile-number list/input, never Tab-1's own
 *    `mailList`. Reproduced verbatim below via `clearScheduleMobileList()`,
 *    which both buttons call -- see its doc comment.
 *
 * **Other real quirks, preserved (not bugs introduced by this rebuild):**
 *
 * - `clearFields()` (ctrl:486-492) is bound to BOTH tabs' Template
 *   Description eraser buttons (view:155 and view:483) and clears BOTH
 *   `common.templateDescription` AND
 *   `commonSchedule.scheduletemplateDescriptionForData` together, whichever
 *   tab's button was clicked. Reproduced via the shared `clearTemplateFields()`.
 * - `save()`'s payload (ctrl:305-326) always reads `isSMSSend`/
 *   `isWhatsappSend`/`whatsAppTemplateMasterId` off Tab-1's `common` object,
 *   even when saving a Tab-2 (schedule) row -- `commonSchedule` has no such
 *   fields at all. Reproduced in `confirmSave()` below.
 * - `mobileNoList`/`userDefinedMail` (ctrl:207-218, 294-303) are built from
 *   the SAME shared `mobileNumbers`/`mailList` arrays regardless of which
 *   tab is being saved -- not scoped per tab. Reproduced as-is.
 * - `showData()` (ctrl:446-471, Tab-1 edit) never clears `mailList` before
 *   re-populating it from the clicked row's `userDefinedMail` -- editing a
 *   second User-Defined-Mail row APPENDS onto whatever the mini-list
 *   already held, rather than replacing it. Reproduced via `editRow()`.
 *   `showDataForschedule()` (ctrl:530-552, Tab-2 edit)'s `getMobileNoList()`
 *   call, by contrast, DOES reset `mobileNumbers` on every call (ctrl:592)
 *   -- also reproduced, so the asymmetry is intentional, not an oversight.
 * - Switching TO Tab 2 always calls `scheduleSMS()` (ctrl:612-626, 633-644),
 *   which reinitializes the whole `commonSchedule` form to blank -- any
 *   unsaved Tab-2 edit in progress is silently discarded on every tab
 *   switch. Switching to Tab 1 has no equivalent reset. Reproduced in
 *   `setTab()`.
 * - Tab 1's grid uses real client-side pagination (`PaginationService` /
 *   this rebuild's `client-pagination.util`, view:227-234, 350-369). Tab
 *   2's grid (view:645, `smsScheduleConfigurationList`) is filtered
 *   in-browser but NEVER actually paginated -- its `<ul ng-if="pager.pages.length">`
 *   (view:663) references a `pager` scope variable this controller never
 *   populates, so that block is permanently dead and the full list always
 *   renders. Reproduced: Tab 2's grid below has a keyword filter but no
 *   pagination controls.
 * - `whatsAppOption` is read once from a DIFFERENT localStorage key,
 *   `cookieObjectAppConfigSecond` (ctrl:38, 48) -- NOT the
 *   `cookieObjectAppConfig` key `SessionService.appConfig` already exposes
 *   -- so it's read directly here via `readWhatsAppOption()` rather than
 *   through `SessionService`.
 * - Rows with `isEmail` truthy are dropped from BOTH tabs' grids entirely
 *   (ctrl:394-402, the push-into-either-list `if` is gated on
 *   `!isEmail`) -- reproduced in `loadGrids()`.
 * - `LoaderService` is wired around the list/save calls, matching the
 *   convention established in `corrective-maintenance-worklist` (a
 *   data-heavy grid screen), unlike the simpler "master" screens that
 *   omit it.
 */
@Component({
  selector: 'app-sms-configuration',
  imports: [FormsModule],
  templateUrl: './sms-configuration.html',
  styleUrl: './sms-configuration.scss',
})
export class SmsConfiguration {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(SmsConfigurationService);

  private orgId = 0;
  private unitId = 0;
  private userId = 0;
  /**
   * Read once from `cookieObjectAppConfigSecond` -- see class doc comment.
   * Not `private` -- the template reads it directly to gate the WhatsApp
   * Template dropdown/required-field check.
   */
  whatsAppOption = 1;

  // 1 = Triggered SMS, 2 = Scheduled SMS (mirrors `firstTabLevel`).
  activeTab = signal<1 | 2>(1);

  // ---- dropdown data ----
  triggerList = signal<TriggerOption[]>([]);
  scheduleTriggerList = signal<TriggerOption[]>([]);
  categoryList = signal<CategoryOption[]>([]);
  scheduleCategoryList = signal<CategoryOption[]>([]);
  whatsAppTemplateList = signal<WhatsAppTemplateOption[]>([]);

  // Shared "Required Data" picker state -- ONE instance for both tabs,
  // matching legacy's single `$scope.dataList`/`selectedDate`/`userDefinedString`.
  dataList = signal<RequiredDataOption[]>([]);
  selectedDate = signal('');
  userDefinedString = signal('');

  mobileDigit = signal(10);

  // ---- Tab 1 form ("common") ----
  smsUtilityId = signal<number | undefined>(undefined);
  triggerId = signal<number | ''>('');
  categoryId = signal<number | ''>('');
  templateDescription = signal('');
  isSMSSend = signal<'Y' | 'N'>('N');
  isWhatsappSend = signal<'Y' | 'N'>('N');
  whatsAppTemplateMasterId = signal<number | ''>('');

  // Tab 1's User-Defined-Mail mini-list (shown when categoryId === 15).
  mailId = signal('');
  mailList = signal<MobileNumberEntry[]>([]);
  showUserDefinedMailSection = computed(() => this.categoryId() === 15);

  // ---- Tab 2 form ("commonSchedule") ----
  scheduleSmsUtilityId = signal<number | undefined>(undefined);
  scheduleTriggerId = signal<number | ''>('');
  scheduleCategoryId = signal<number | ''>('');
  scheduleTemplateDescription = signal('');

  // Tab 2's Mobile Number mini-list (shown when scheduleCategoryId === 9).
  newMobileNumber = signal('');
  mobileNumbers = signal<MobileNumberEntry[]>([]);
  showScheduleMobileSection = computed(() => this.scheduleCategoryId() === 9);

  // ---- grids ----
  private tab1AllRows: SmsUtilityConfiguration[] = [];
  pagination = signal<PaginationState<SmsUtilityConfiguration>>(createPaginationState<SmsUtilityConfiguration>());
  scheduleRows = signal<SmsUtilityConfiguration[]>([]);
  searchKeyword = signal('');

  scheduleDisplayRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.scheduleRows();
    if (!keyword) return rows;
    return rows.filter(
      (r) => (r.triggerName ?? '').toLowerCase().includes(keyword) || (r.templateDescription ?? '').toLowerCase().includes(keyword) || (r.categoryName ?? '').toLowerCase().includes(keyword),
    );
  });

  showConfirmModal = signal(false);

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.orgId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;
    this.whatsAppOption = this.readWhatsAppOption();

    this.loadTriggerList();
    this.loadCategoryList();
    this.loadGrids();
    this.loadMobileDigitConfig();
    this.loadWhatsAppTemplateList();
  }

  // ---- tabs (mirrors setFirstTabLevel, ctrl:612-626) ----
  setTab(tab: 1 | 2): void {
    this.activeTab.set(tab);
    // Legacy quirk, preserved -- see class doc comment.
    if (tab === 2) {
      this.resetTab2Form();
    }
  }

  // ---- dropdown loads ----
  private loadTriggerList(): void {
    this.api.getTriggerList().subscribe({
      next: (res) => {
        const t1: TriggerOption[] = [];
        const t2: TriggerOption[] = [];
        for (const v of res.listObject ?? []) {
          if (v.isSms) {
            if (v.triggerType === 2) t2.push(v);
            else t1.push(v);
          }
        }
        this.triggerList.set(t1);
        this.scheduleTriggerList.set(t2);
      },
    });
  }

  private loadCategoryList(): void {
    this.api.getCategoryList().subscribe({
      next: (res) => {
        const c1: CategoryOption[] = [];
        const c2: CategoryOption[] = [];
        for (const v of res.listObject ?? []) {
          if (v.categoryType === 2) c2.push(v);
          else c1.push(v);
        }
        this.categoryList.set(c1);
        this.scheduleCategoryList.set(c2);
      },
    });
  }

  private loadMobileDigitConfig(): void {
    this.api.getMobileDigitConfig(this.orgId, this.unitId).subscribe({
      next: (res) => {
        const row = (res.listObject ?? [])[0];
        const digit = parseInt(row?.mobileDigit ?? '10', 10);
        this.mobileDigit.set(Number.isFinite(digit) ? digit : 10);
      },
      error: () => this.mobileDigit.set(10),
    });
  }

  private readWhatsAppOption(): number {
    try {
      const raw = localStorage.getItem('cookieObjectAppConfigSecond');
      if (!raw) return 1;
      const parsed = JSON.parse(raw) as { whatsAppOption?: number };
      return parsed?.whatsAppOption ?? 1;
    } catch {
      return 1;
    }
  }

  // ctrl:687-703 -- only fetches when whatsAppOption === 4, same guard kept here.
  onWhatsappSendClick(): void {
    this.loadWhatsAppTemplateList();
  }

  private loadWhatsAppTemplateList(): void {
    if (this.whatsAppOption !== 4) return;
    this.api.getWhatsAppTemplateList(this.orgId, this.unitId).subscribe({
      next: (res) => this.whatsAppTemplateList.set(res.listObject ?? []),
    });
  }

  // ---- required data (mirrors getReqiredData, ctrl:473-484) ----
  onTriggerChange(): void {
    if (this.triggerId() !== '') this.getRequiredData(this.triggerId() as number);
  }

  onScheduleTriggerChange(): void {
    if (this.scheduleTriggerId() !== '') this.getRequiredData(this.scheduleTriggerId() as number);
  }

  private getRequiredData(triggerId: number): void {
    this.api.getRequiredData(triggerId).subscribe({
      next: (res) => this.dataList.set(res.listObject ?? []),
    });
  }

  // ---- append token into Template Description (mirrors appendString/appendStringForSchedule) ----
  appendToTab1(): void {
    this.appendToken((token) => this.templateDescription.set(this.templateDescription() + ' ' + token));
  }

  appendToTab2(): void {
    this.appendToken((token) => this.scheduleTemplateDescription.set(this.scheduleTemplateDescription() + ' ' + token));
  }

  private appendToken(apply: (token: string) => void): void {
    const token = this.selectedDate() === '1' ? this.userDefinedString() : this.selectedDate();
    apply(token);
    this.userDefinedString.set('');
    this.selectedDate.set('');
  }

  /**
   * Mirrors `clearFields()` (ctrl:486-492) -- deliberately clears BOTH
   * tabs' Template Description at once, bound to BOTH tabs' eraser
   * buttons, exactly like legacy. See class doc comment.
   */
  clearTemplateFields(): void {
    this.userDefinedString.set('');
    this.selectedDate.set('');
    this.templateDescription.set('');
    this.scheduleTemplateDescription.set('');
  }

  // ---- Tab 1 User-Defined-Mail mini-list (mirrors addMailNumber/deleteMailId) ----
  addMailNumber(): void {
    if (!this.mailId().trim()) {
      this.notify.warning('Please Enter User Defined data');
      return;
    }
    if (this.mailId().length !== this.mobileDigit()) {
      this.notify.error('Please enter valid mobile number');
      return;
    }
    this.mailList.update((list) => [...list, { title: this.mailId(), done: false }]);
    this.mailId.set('');
  }

  deleteMailId(index: number): void {
    this.mailList.update((list) => list.filter((_, i) => i !== index));
  }

  // ---- Tab 2 Mobile Number mini-list (mirrors addMobileNumber/deleteMobileNumber) ----
  addMobileNumber(): void {
    if (this.newMobileNumber().length !== this.mobileDigit()) {
      this.notify.error('Please enter valid mobile number');
      return;
    }
    this.mobileNumbers.update((list) => [...list, { title: this.newMobileNumber(), done: false }]);
    this.newMobileNumber.set('');
  }

  deleteMobileNumber(index: number): void {
    this.mobileNumbers.update((list) => list.filter((_, i) => i !== index));
  }

  /**
   * BUG, faithfully reproduced -- see class doc comment (bug #2). Legacy
   * defines `clearMobileTempFields` TWICE; the SECOND definition
   * (ctrl:513-517) silently wins for BOTH Clear buttons and only touches
   * Tab-2/schedule mobile-number state. Both the Tab-1 mini-list's Clear
   * button AND Tab-2 mini-list's own Clear button call THIS SAME method
   * below -- Tab-1's button therefore wipes Tab-2's mobile list/input
   * instead of its own `mailList` (which is never cleared by either
   * button). Do not "fix" this by giving Tab-1 its own clear method.
   */
  clearScheduleMobileList(): void {
    this.newMobileNumber.set('');
    this.mobileNumbers.set([]);
  }

  // ---- grid loads (mirrors getSmsConfigurationList, ctrl:384-411) ----
  private loadGrids(): void {
    this.loader.start();
    this.api.getList(this.orgId, this.unitId).subscribe({
      next: (res) => {
        const tab1: SmsUtilityConfiguration[] = [];
        const tab2: SmsUtilityConfiguration[] = [];
        for (const row of res.listObject ?? []) {
          // Rows with isEmail truthy are dropped from BOTH grids -- ctrl:394-402.
          if (row.isEmail == null || !row.isEmail) {
            if (row.scheduleTypeId === 2) tab2.push(row);
            else tab1.push(row);
          }
        }
        this.tab1AllRows = tab1;
        this.applyTab1Filter();
        this.scheduleRows.set(tab2);
        this.loader.stop();
      },
      error: () => {
        this.notify.error('Error');
        this.loader.stop();
      },
    });
  }

  private applyTab1Filter(): void {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const filtered = keyword
      ? this.tab1AllRows.filter(
          (r) => (r.triggerName ?? '').toLowerCase().includes(keyword) || (r.templateDescription ?? '').toLowerCase().includes(keyword) || (r.categoryName ?? '').toLowerCase().includes(keyword),
        )
      : this.tab1AllRows;
    const p = this.pagination();
    p.filteredData = filtered;
    setTotalPages(p);
    this.pagination.set({ ...p });
  }

  onSearchChange(): void {
    this.applyTab1Filter();
    // scheduleDisplayRows recomputes automatically off the same searchKeyword signal.
  }

  setPageSize(): void {
    this.applyTab1Filter();
  }

  page(action: 'first' | 'prev' | 'next' | 'last' | number): void {
    const p = this.pagination();
    if (action === 'first') setFirstPage(p);
    else if (action === 'prev') setPreviousPage(p);
    else if (action === 'next') setNextPage(p);
    else if (action === 'last') setLastPage(p);
    else setPage(action, p);
    this.pagination.set({ ...p });
  }

  pagesList(): number[] {
    return getPagesList(this.pagination());
  }

  // ---- edit (mirrors showData, ctrl:446-471) ----
  editRow(row: SmsUtilityConfiguration): void {
    this.smsUtilityId.set(row.smsUtilityId);
    this.triggerId.set(row.triggerId ?? '');
    this.categoryId.set(row.categoryId ?? '');
    this.templateDescription.set(row.templateDescription ?? '');
    this.isSMSSend.set((row.isSMSSend as 'Y' | 'N') ?? 'N');
    this.isWhatsappSend.set((row.isWhatsappSend as 'Y' | 'N') ?? 'N');
    this.whatsAppTemplateMasterId.set(row.whatsAppTemplateMasterId ?? '');
    if (row.triggerId != null) this.getRequiredData(row.triggerId);
    // Real bug, preserved verbatim -- see class doc comment: mailList is
    // NEVER cleared here, so repeated edits accumulate onto it.
    if (row.userDefinedMail != null && row.userDefinedMail !== '') {
      const parts = row.userDefinedMail.split(',');
      this.mailList.update((list) => [...list, ...parts.map((title) => ({ title, done: false }))]);
    }
  }

  // ---- edit (mirrors showDataForschedule, ctrl:530-552) ----
  editScheduleRow(row: SmsUtilityConfiguration): void {
    this.scheduleSmsUtilityId.set(row.smsUtilityId);
    this.scheduleTriggerId.set(row.triggerId ?? '');
    this.scheduleCategoryId.set(row.categoryId ?? '');
    this.scheduleTemplateDescription.set(row.templateDescription ?? '');
    if (row.triggerId != null) this.getMobileNoList(row.triggerId);
  }

  // ctrl:591-611 -- resets/refetches mobileNumbers fully on every call.
  private getMobileNoList(triggerId: number): void {
    this.api.getMobileList(triggerId, this.orgId, this.unitId).subscribe({
      next: (list) => this.mobileNumbers.set((list ?? []).map((v) => ({ title: v.mobileNo, done: false }))),
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:554-589) ----
  toggleStatus(row: SmsUtilityConfiguration): void {
    row.status = row.status === 'A' ? 'I' : 'A';
    // Re-trigger change detection for whichever grid holds this row object.
    this.pagination.set({ ...this.pagination() });
    this.scheduleRows.set([...this.scheduleRows()]);
    this.api.updateStatus({ smsUtilityId: row.smsUtilityId, status: row.status, updatedBy: this.userId }).subscribe({
      next: (res) => {
        if (res.status === 'success') this.notify.success('Updated Successfully...!!');
        else this.notify.error('Something went wrong...!!');
      },
      error: () => this.notify.error('Something went wrong...!!'),
    });
  }

  // ---- save (mirrors save(), ctrl:205-376) ----
  save(): void {
    if (this.activeTab() === 1) {
      if (this.triggerId() === '') {
        this.notify.warning('Please Select Trigger Point');
        return;
      }
      if (this.categoryId() === '') {
        this.notify.warning('Please Select Category');
        return;
      }
      if (!this.templateDescription().trim()) {
        this.notify.warning('Please Add Template Description');
        return;
      }
      // BUG, faithfully reproduced -- NOT fixed here. See bug #1 in the
      // class doc comment: legacy's "default isSmsSend to Y when both
      // channels are off" safety net (ctrl:244-246) checks/writes the
      // wrong-cased property (`isSmsSend`, never read/sent anywhere else)
      // instead of the real `isSMSSend`, making it dead code. A config
      // with BOTH isSMSSend and isWhatsappSend left 'N' is therefore
      // allowed to save -- deliberately not adding any such default here.
      if (this.isWhatsappSend() === 'Y' && this.whatsAppOption === 4 && this.whatsAppTemplateMasterId() === '') {
        this.notify.warning('Please Select Whatsapp Template');
        return;
      }
    } else {
      if (this.scheduleTriggerId() === '') {
        this.notify.warning('Please Select Trigger Point');
        return;
      }
      if (this.scheduleCategoryId() === '') {
        this.notify.warning('Please Select Category');
        return;
      }
      if (!this.scheduleTemplateDescription().trim()) {
        this.notify.warning('Please Add Template Description');
        return;
      }
    }
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const isTab1 = this.activeTab() === 1;
    const smsUtilityId = (isTab1 ? this.smsUtilityId() : this.scheduleSmsUtilityId()) ?? null;

    // Faithful quirk -- see class doc comment: built from the SAME shared
    // lists regardless of which tab is being saved (ctrl:207-218, 294-303).
    const mobileNoList = this.mobileNumbers().map((m) => m.title);
    const userDefinedMail = this.mailList().length ? this.mailList().map((m) => m.title).join(',') : null;
    const now = formatDateTime(new Date());

    const payload = {
      smsUtilityId,
      triggerId: isTab1 ? this.triggerId() : this.scheduleTriggerId(),
      templateDescription: isTab1 ? this.templateDescription() : this.scheduleTemplateDescription(),
      categoryId: isTab1 ? this.categoryId() : this.scheduleCategoryId(),
      orgId: this.orgId,
      unitId: this.unitId,
      status: 'A',
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
      triggerName: '',
      categoryName: '',
      scheduleTypeId: this.activeTab(),
      mobileNoList,
      isEmail: false,
      userDefinedMail,
      // Faithful quirk -- see class doc comment: always Tab-1's form state,
      // even when saving a Tab-2 row.
      isSMSSend: this.isSMSSend(),
      isWhatsappSend: this.isWhatsappSend(),
      whatsAppTemplateMasterId: this.whatsAppTemplateMasterId() || null,
    };

    this.loader.start();
    this.api.save(payload).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Saved Successfully');
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
        // ctrl:364-368 -- these reset calls run unconditionally, regardless
        // of success/error and regardless of which tab was saved.
        this.resetTab1Form();
        this.resetTab2Form();
        this.loadGrids();
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Something went wrong');
      },
    });
  }

  // ---- form resets (mirrors initMaster, ctrl:68-93) ----
  private resetTab1Form(): void {
    this.smsUtilityId.set(undefined);
    this.triggerId.set('');
    this.categoryId.set('');
    this.templateDescription.set('');
    this.isSMSSend.set('N');
    this.isWhatsappSend.set('N');
    this.whatsAppTemplateMasterId.set('');
    this.mailList.set([]);
    this.selectedDate.set('');
    this.userDefinedString.set('');
  }

  // ---- form reset (mirrors scheduleSMS, ctrl:633-644, plus mobileNumbers=[] at ctrl:366) ----
  private resetTab2Form(): void {
    this.scheduleSmsUtilityId.set(undefined);
    this.scheduleTriggerId.set('');
    this.scheduleCategoryId.set('');
    this.scheduleTemplateDescription.set('');
    this.mobileNumbers.set([]);
    this.newMobileNumber.set('');
  }
}

// mirrors `moment(date).format('DD-MM-YYYY HH:mm:ss')` (ctrl:211-212)
function formatDateTime(d: Date): string {
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
