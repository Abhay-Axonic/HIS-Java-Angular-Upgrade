import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CurrencydenominationService } from './currencydenomination.service';
import {
  CurrencyDenominationForm,
  CurrencyDenominationRow,
  CurrencyOption,
  emptyCurrencyDenominationForm,
} from './currencydenomination.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/GlobalMaster/currency-denomination.html`
 * (260 lines) + `resources/assets/js/controller/master/global/billing/
 * currencyDenominationController.js` (400 lines, `currencyDenominationController`,
 * no `controllerAs`). State `currencydenomination` (all-lowercase, no
 * camelCase -- kept as the literal legacy identifier for this feature's
 * folder/file/class names, same precedent as this codebase's `qmslogo`)
 * is registered in `app.js:7946-7972`: `url: '/currencydenomination'`, a
 * REAL `resolve` block gating on `moduleId 452` (via
 * `localStorage.permissions.modules`, `$state.go('accessDenied')` on
 * miss) -- confirmed directly from the state definition, not assumed
 * gate-less. Real, reachable sidebar entries exist in
 * `new-sidebar-16-10.html:924`, `side-bar.html:374`,
 * `new-side-bar.html:3589`, and `dashboard-quick-links.html:13086`
 * (`href="#/currencydenomination"`, `checkAccess(452)`), so this is a
 * genuine top-level menu screen, not a decoy/dead route.
 *
 * Backend confirmed real end to end: `CurrencyDenominationMasterController`
 * (`@RequestMapping("api/billing")`, exact prefix taken directly from the
 * class-level annotation -- NOT assumed; see service file header for why
 * that matters here specifically) -> `CurrencyDenominationMasterServiceImpl`
 * -> `CurrencyDenominationMasterDaoImpl` -> entity
 * `com.param.billing.global.model.CurrencyDenominationMaster`
 * (`t_currency_denomination_master`). A DIFFERENT, unrelated entity class
 * (`com.param.entity.model.master.CurrencyDenomination`) also exists in the
 * codebase under a similar name but has zero references anywhere in
 * `legacy/` -- confirmed dead/decoy, not this screen's backing table.
 *
 * ================================================================
 * `ratePerDoller` -- confirmed dormant, exactly as
 * docs/ANGULAR_PENDING_FINDINGS.md entry 148 describes
 * ================================================================
 * The entity has real `rate_per_doller`/`date` columns
 * (`@Column(name="rate_per_doller") int ratePerDoller`, `@Column(name="date")
 * Date date`) and the DTO carries a matching `ratePerDoller` field -- but
 * `CurrencyDenominationMasterDaoImpl.saveCurrencyDenominationMaster`
 * (DAO:41-69) and `.updateCurrencyDenominationMaster` (DAO:110-134) never
 * call `getRatePerDoller()`/`setRatePerDoller(...)` on either the DTO or
 * the entity, and no named query (`GET_CURRENCY_DENOMINATIONMASTER_BY_ID`/
 * `_LIST`) ever selects it either. Confirmed directly against the DAO's
 * actual save/update flow (not taken on the prior note's word) -- it is
 * genuinely rate-shaped data sitting unused, not a calculation this screen
 * performs today; correctly NOT modeled anywhere in this rebuild. The
 * `date` column is likewise dormant (also never set/read by this flow) --
 * a second, previously-uncatalogued unused column alongside `ratePerDoller`.
 * `unitId` is a third, related-but-distinct case: it IS wired into the save
 * DAO call (`.setUnitId(dto.getUnitId())`, DAO:55), but the controller's
 * own `data` object never includes a `unitId` field (ctrl:216-228) --so it
 * is always persisted as `null` on every save from this screen. None of
 * these are severity-1 (no silent loss of anything the user actually
 * entered) -- just unused/always-null columns, reproduced by omission
 * rather than modeled.
 *
 * ================================================================
 * Two-click confirm modal -- Save ONLY, matching legacy's actual asymmetry
 * ================================================================
 * `saveCurrencyDenomination()` (ctrl:210-271) is gated by `$scope.popUpFlag`
 * (`init()` sets it `true`, ctrl:57): the FIRST click, with `popUpFlag`
 * true, only toggles `showModal` open (ctrl:233-235) -- no POST yet. The
 * shared `<modal visible="showModal">` directive's Ok button
 * (`commonPopUpDirectives.js`) calls `PopupOkBtn()` (ctrl:204-207), which
 * sets `popUpFlag = false` and calls `saveCurrencyDenomination()` again,
 * which this time (else-branch, ctrl:236-268) fires the actual POST.
 * `updateCurrencyDenomination()` (ctrl:348-399) has NO popUpFlag/showModal
 * handling at all -- Update always POSTs immediately, no confirm step,
 * same asymmetry this codebase's country-master already has for the
 * identical reason (legacy genuinely has no popup wired to Update here).
 *
 * **RESOLVED, 2026-07-24 (see docs/ANGULAR_PENDING_FINDINGS.md entry 152):**
 * this screen's own build correctly flagged a genuine ambiguity here rather
 * than silently picking a side -- reading `popUpFlag` as reset back to
 * `true` ONLY inside `init()` (ctrl:29-84, which runs on a SUCCESSFUL save,
 * ctrl:245) with no reset in the failure branch (ctrl:252-259, e.g.
 * "Already Exists.") suggests the same "stuck after failure" shape found
 * elsewhere in this codebase. But this screen's confirm modal is the SAME
 * shared `<modal>` directive (`commonPopUpDirectives.js`) used by every
 * other affected screen, and that directive's own hardcoded Ok-button
 * handler resets `popUpFlag` back to `true` on every click, unconditionally
 * -- confirmed live against real, unmodified legacy (duplicate-then-retry
 * on `cardTypeMaster`/`raceMaster`): the confirm modal correctly reappears
 * after a failure, every time. The "stuck" bug does NOT exist in real
 * legacy for any screen sharing this directive. `popupArmed` below is now
 * re-armed on both success AND failure to match -- see `doSave()`'s
 * failure branch.
 *
 * ================================================================
 * Status toggle -- list itself never refreshes, but the whole form does
 * (both confirmed, and worth separating -- they're not the same thing)
 * ================================================================
 * `updateStatus()` (ctrl:307-345) calls `$scope.init()` on a SUCCESSFUL
 * toggle (ctrl:328) -- which reloads the currency dropdown and fully
 * resets the edit form/save-vs-update mode/`popUpFlag` (ctrl:29-84) --
 * but it does NOT call `initCurrencyDenominatoinMaster(...)`, so the
 * on-screen denomination table itself is never re-fetched from the server
 * either way (success or failure). The checkbox is two-way bound
 * (`ng-model="info.status"`, view:214), so the visual flip is already
 * correct regardless. The surprising part: a successful status toggle
 * silently wipes out whatever the user had open in the Save/Update form
 * at that moment (e.g. mid-edit on a DIFFERENT row) as a side effect of
 * `init()` running -- reproduced verbatim via `resetForm()` in the success
 * branch below, since it's a real (if odd) legacy behavior, not a rebuild
 * bug, and is fully visible from the frontend (not a silent invisible
 * defect).
 *
 * ================================================================
 * Dead/unwired search box
 * ================================================================
 * The "Search" `<input class="text-search-box">` (view:119-121) has no
 * `ng-model`/`ng-change` at all -- it is decorative only, wired to
 * nothing. Reproduced as a visual-only input here too (no `searchKeyword`
 * signal, no client-side filter) rather than "completing" functionality
 * legacy never had.
 *
 * ================================================================
 * Success/error toasts use the SERVER's own message text, not a
 * hardcoded client string
 * ================================================================
 * Unlike card-type-master/country-master (which hardcode their own
 * "X added Successfully...!!" strings), this controller calls
 * `growl.success(response.data.message, ...)` /
 * `growl.error(response.data.message, ...)` verbatim (ctrl:243-259,
 * 373-389, 326-334) -- i.e. it always displays whatever message the
 * backend's `Response` object returns (`"Added Successfully."` /
 * `"Already Exists."` / `"Updated Successfully."`, from
 * `ICommonConstants`). Reproduced by passing `res.message` straight
 * through to `NotificationService` rather than hardcoding a client-side
 * string.
 *
 * ================================================================
 * Duplicate-name check is NOT scoped by currency
 * ================================================================
 * `saveCurrencyDenominationMaster`/`updateCurrencyDenominationMaster`
 * (Service:25-41, 64-80) reject a save/update as "Already Exists." purely
 * by `denominationDesc` (case-insensitive) + `orgnisationId` -- NOT also
 * scoped by `currencyId` (`GET_CURRENCY_DENOMINATIONMASTER_BY_NAME`,
 * Entity:21-25). So two different currencies cannot each have a
 * denomination named e.g. "Cent" in the same organisation. This is a
 * legacy business-rule quirk, reproduced as-is (the uniqueness check is
 * entirely server-side; nothing to model client-side beyond displaying
 * whatever message comes back).
 */
@Component({
  selector: 'app-currencydenomination',
  imports: [FormsModule, StatusToggle],
  templateUrl: './currencydenomination.html',
  styleUrl: './currencydenomination.scss',
})
export class Currencydenomination {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(CurrencydenominationService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form/edit-mode state (mirrors $scope.currencyDenominationMasterObj
  // + saveBtnFlag/updateBtnFlag) ----
  form = signal<CurrencyDenominationForm>(emptyCurrencyDenominationForm());
  saveMode = signal(true);
  submitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY,
  // see class doc's "Two-click confirm modal" section for the flagged
  // ambiguity around whether the "stuck after failure" shape actually
  // manifests here) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true);

  // ---- currency dropdown (mirrors $scope.currencyList) ----
  currencyOptions = signal<CurrencyOption[]>([]);

  // ---- list + server-side pagination (mirrors
  // initCurrencyDenominatoinMaster/setPage/commonListCount) ----
  list = signal<CurrencyDenominationRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadCurrencyOptions();
    this.loadList();
  }

  private loadCurrencyOptions(): void {
    this.api.getCurrencyOptions(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.currencyOptions.set(res.listObject ?? []);
      },
    });
  }

  // Mirrors initCurrencyDenominatoinMaster -- fetches the current page via
  // offset/noOfRecordsPerPage AND the total count in parallel (legacy's
  // own `setPage(1,false)` call after the count response only recomputes
  // the pager locally, it does not re-fetch the list a second time; using
  // one combined reload here for both initial load and page navigation is
  // a harmless simplification -- see country-master precedent -- it does
  // not change what the user ever sees, only how many times the read-only
  // count endpoint is called).
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  // ctrl:89-93 (setNoOfRecords) -- changing page size reloads at (legacy's
  // own stale, never-updated) offset 0, i.e. page 1.
  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof CurrencyDenominationForm>(field: K, value: CurrencyDenominationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 4 required fields backing `currencyDenominationform.$valid`
  // (view:18,36,50,66): Currency (select), Code, Description, Unit.
  // `lowestDenomination` is never required (defaults to 'N', always has a
  // value from the checkbox).
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return f.currencyId !== '' && !!f.denominationCode.trim() && !!f.denominationDesc.trim() && !!f.denominatonUnit.trim();
  }

  isCurrencyInvalid(): boolean {
    return this.submitted() && this.form().currencyId === '';
  }

  isCodeInvalid(): boolean {
    return this.submitted() && !this.form().denominationCode.trim();
  }

  isDescriptionInvalid(): boolean {
    return this.submitted() && !this.form().denominationDesc.trim();
  }

  isUnitInvalid(): boolean {
    return this.submitted() && !this.form().denominatonUnit.trim();
  }

  // ---- save / confirm-modal (mirrors saveCurrencyDenomination/PopupOkBtn,
  // ctrl:204-271) ----
  save(): void {
    this.submitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the currencyDenominationform.$valid gate
    if (this.popupArmed()) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
    } else {
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.popupArmed.set(false); // mirrors PopupOkBtn's `$scope.popUpFlag = false;`
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        currencyId: f.currencyId as number,
        denominationCode: f.denominationCode,
        denominationDesc: f.denominationDesc,
        denominatonUnit: f.denominatonUnit,
        lowestDenomination: f.lowestDenomination,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        orgnisationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          // Server's own message text either way -- see class doc.
          if (res.status === 'success') {
            this.notify.success(res.message ?? '');
            this.resetForm(); // ctrl:245 -- init() runs on success, re-arms popupArmed too
          } else {
            this.notify.error(res.message ?? '');
            // Fixed, diverges from legacy (see class doc comment): re-arm
            // the confirm gate on failure too.
            this.popupArmed.set(true);
          }
          // ctrl:261-266 -- list reload happens unconditionally on ANY
          // resolved response, success or business failure alike.
          this.loadList();
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyCurrencyDenominationForm());
    this.submitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors init()'s popUpFlag = true
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:274-304 -- flips into update
  // mode BEFORE the GET resolves, and uses a raw alert() on failure, both
  // preserved verbatim; same precedent as country-master's `edit()`) ----
  edit(row: CurrencyDenominationRow): void {
    this.saveMode.set(false);
    this.api.getById(row.currencyDenominationId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            currencyDenominationId: detail.currencyDenominationId,
            currencyId: detail.currencyId,
            denominationCode: detail.denominationCode,
            denominationDesc: detail.denominationDesc,
            denominatonUnit: String(detail.denominatonUnit),
            lowestDenomination: detail.lowestDenomination,
          });
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- update (mirrors updateCurrencyDenomination, ctrl:348-399 -- no
  // confirm modal at all) ----
  update(): void {
    this.submitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the currencyDenominationform.$valid gate
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        currencyDenominationId: f.currencyDenominationId as number,
        denominationDesc: f.denominationDesc,
        denominatonUnit: f.denominatonUnit,
        lowestDenomination: f.lowestDenomination,
        denominationCode: f.denominationCode,
        currencyId: f.currencyId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        orgnisationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? '');
            this.resetForm(); // ctrl:375 -- init() runs on a successful update too
          } else {
            this.notify.error(res.message ?? '');
          }
          // ctrl:390-395 -- list reload happens unconditionally either way,
          // same as save.
          this.loadList();
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:307-345) ----
  toggleStatus(row: CurrencyDenominationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- the view's checkbox is two-way bound in legacy, so
    // the visual change happens immediately regardless of the response.
    this.list.update((rows) =>
      rows.map((r) => (r.currencyDenominationId === row.currencyDenominationId ? { ...r, status: nextStatus } : r)),
    );

    this.loader.start();
    this.api
      .updateStatus({
        currencyDenominationId: row.currencyDenominationId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? '');
            // ctrl:328 -- init() genuinely runs on a successful status
            // toggle in this controller, resetting the currency dropdown
            // AND wiping any in-progress Save/Update form -- see class
            // doc's "Status toggle" section. It does NOT reload the
            // denomination list/count either way (that part IS the
            // "list never refreshes" shape, matching card-type-master's
            // own status-toggle finding).
            this.resetForm();
            this.loadCurrencyOptions();
          } else {
            this.notify.error(res.message ?? '');
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:211-213/308-310/349-351. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
