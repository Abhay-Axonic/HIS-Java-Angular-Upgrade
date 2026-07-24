import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CurrencyMasterService } from './currency-master.service';
import { CountryOption, CurrencyForm, CurrencyRow, emptyCurrencyForm } from './currency-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const VALID_FILE_EXTENSIONS = ['jpg', 'txt', 'pdf', 'docx', 'doc', 'jpeg', 'png', 'gif', 'csv'];
const MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024; // 5MB -- resources/assets/js/service/fileUploadDirective.js:19

/**
 * Parity rebuild of views/master/GlobalMaster/currency-master.html (265
 * lines) + resources/assets/js/controller/master/global/billing/
 * currencyMasterController.js (419 lines, `currencyMasterController`, no
 * `controllerAs`).
 *
 * State `currencyMaster` is registered in `app.js:7918-7935` with a REAL
 * `resolve` block gating on permission module id **451** (`app.js`'s
 * `arr.filter(item => item.id == 451)`, confirmed by reading the exact
 * block, not assumed) -- confirmed live and reachable via the sidebar
 * (`new-side-bar.html:3588`, `new-sidebar-16-10.html:922-923`, both
 * `ng-if="checkAccess(451)"`) and via `adminPriviledges.json`'s id-451 entry
 * (`"name": "Currency Master", "actionLink": "#/currencyMaster"`).
 *
 * `CurrencyMasterController.java`'s class-level `@RequestMapping` is
 * `"/billing"` (legacy/Global/.../controller/CurrencyMasterController.java:20)
 * -- NO `/api` prefix (see currency-master.service.ts's doc comment).
 *
 * ================================================================
 * *** SEVERITY-1 FINDING, escalated, not silently reproduced or fixed ***
 * ================================================================
 * `CurrencyMasterDaoImpl.updateCurrencyMaster` (legacy/Global/.../dao/
 * CurrencyMasterDaoImpl.java:120-145) never calls `setCountryId` on the
 * fetched entity, even though the real update payload (ctrl:373-381)
 * includes `countryId` and the Country dropdown is fully editable while
 * updating. Changing a currency's Country and clicking Update silently has
 * NO effect on the persisted row -- no error, a normal success toast, the
 * list "reloads" showing the OLD country unchanged. See
 * docs/DECISION_NEEDED_CurrencyMasterUpdateCountryIdDropped.md -- awaiting
 * an owner decision on whether to fix legacy, fix only the rebuild, or
 * faithfully reproduce the current (broken) behavior. This rebuild
 * currently sends the real wire payload (matching legacy's actual request
 * shape) while this decision is pending -- see CurrencyUpdatePayload's own
 * doc comment in currency-master.models.ts.
 *
 * ================================================================
 * Two-click confirm flow on Save ONLY, "stuck after failure" bug --
 * FAITHFULLY REPRODUCED here (NOT fixed, unlike this codebase's
 * card-type-master, where the owner explicitly approved a fix on
 * 2026-07-23) -- flagging for the orchestrating session's cross-codebase
 * tracking rather than fixing without instruction.
 * ================================================================
 * `saveCurrency()` (ctrl:234-293) is gated by `$scope.popUpFlag` (`init()`
 * sets it `true`, ctrl:58): the FIRST click, with `popUpFlag` true, just
 * toggles `showModal` open (ctrl:255-257) -- no API call yet. The modal's Ok
 * button (`PopupOkBtn`, ctrl:227-230) sets `popUpFlag = false` and calls
 * `saveCurrency()` again, which this time (else-branch, ctrl:258-290) fires
 * the actual POST. `popUpFlag` is only ever reset back to `true` by
 * `init()` -- called on page load and inside `saveCurrency`'s OWN success
 * branch (ctrl:267) -- there is NO reset anywhere in its error branch
 * (ctrl:274-281, e.g. duplicate-name case). Legacy's real net effect: after
 * any FAILED save, `popUpFlag` stays permanently `false`, so every
 * subsequent Save click silently skips the confirm modal and fires the POST
 * immediately, until the page is reloaded. Modeled via `popupArmed` below;
 * `save()` only shows the modal while `popupArmed()` is true, and it is
 * ONLY re-armed by a successful save (`resetForm()`), never by a failed one
 * -- see `doSave()`'s failure branch.
 *
 * ================================================================
 * List reload semantics -- DIFFERENT shape from card-type-master, verified
 * line by line, not assumed identical
 * ================================================================
 * Both `saveCurrency()` (ctrl:283-289) and `updateCurrency()` (ctrl:409-413)
 * call `initCurrencyMasterList(...)` UNCONDITIONALLY, outside the
 * success/failure branch -- the list reloads after every save/update
 * attempt, success OR failure. `updateStatus()` (ctrl:327-365), by
 * contrast, never calls it in either branch -- the status checkbox's
 * visible flip comes purely from its own two-way `ng-model` binding on
 * `info.status` (view:217-221), matching this codebase's usual
 * "status toggle never reloads the list" bug shape. `updateStatus()`
 * DOES call `$scope.init()` on success (ctrl:348) though, which resets the
 * create/edit form fields and re-fetches the country dropdown -- a real,
 * if minor, side effect: toggling any row's status silently wipes whatever
 * the user might be mid-typing into the form above. All three reproduced
 * exactly as observed, not smoothed into one shared shape.
 *
 * ================================================================
 * Country-code auto-fill is one click behind the actual selection
 * ================================================================
 * `getCountryCode` (ctrl:197-224) is wired via `ng-click` on the Country
 * `<select>` itself (view:19), NOT `ng-change`. A `<select>`'s click event
 * fires when the dropdown opens, BEFORE the browser applies whatever option
 * the user is about to pick -- so this always fetches the country code for
 * the PREVIOUSLY selected country, one click behind whatever the user just
 * chose. The Country Code field is a plain, always-editable text input
 * though (view:36-42), so a user can simply type/correct it -- this is a
 * convenience-autofill lag, not a hard block, and not fixed here.
 *
 * ================================================================
 * "Symbol" file upload -- real client-side validation, but wired to
 * nothing; the whole Symbol concept is dead on this screen
 * ================================================================
 * The file input (view:86-91, `validfile` directive) DOES run real
 * extension/size validation and show a real error message on an invalid
 * file (resources/assets/js/service/fileUploadDirective.js:17-48) -- but
 * the selected file is never read, stored, or submitted anywhere by this
 * controller. The table's own Symbol column is hardcoded `-` (view:214),
 * never bound to any field either. `currencySymbol` is always sent as `""`
 * on save (ctrl:243) and isn't even persisted by the DAO (see
 * CurrencySavePayload's doc comment). Reproduced here as inert
 * validation-only feedback, matching the real (non-)behavior.
 *
 * ================================================================
 * Search box is purely decorative -- no `ng-model`, no filtering anywhere
 * ================================================================
 * The search `<input>` (view:130-134) has no `ng-model` and is never read
 * by the controller at all -- unlike this codebase's card-type-master
 * (client-side filter) or billing-group-master (same), this screen's
 * search box does nothing. Not wired to any filtering logic here, matching
 * legacy's real (lack of) behavior exactly.
 */
@Component({
  selector: 'app-currency-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './currency-master.html',
  styleUrl: './currency-master.scss',
})
export class CurrencyMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(CurrencyMasterService);

  private orgId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.currencyMaster + saveBtnFlag/updateBtnFlag) ----
  form = signal<CurrencyForm>(emptyCurrencyForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- country dropdown (ctrl:60-78) ----
  countryList = signal<CountryOption[]>([]);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag; stays false forever after a failed save -- see class doc, NOT fixed

  // ---- "Symbol" file input -- real validation, wired to nothing, see class doc ----
  fileError = signal<string | null>(null);

  // ---- list + real server-side offset/limit pagination (mirrors initCurrencyMasterList/getCurrencyCount) ----
  list = signal<CurrencyRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.orgId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadCountryList();
    this.loadList();
  }

  // ---- country dropdown + auto-fill (ctrl:60-78, 197-224) ----
  private loadCountryList(): void {
    this.api.getActiveCountryList(this.orgId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
  }

  // Bound to the Country <select>'s (click), not (change) -- see class doc
  // comment on the one-click-behind auto-fill quirk. Reads the CURRENTLY
  // committed countryId, before whatever the user is about to pick applies.
  onCountryDropdownClick(): void {
    const countryId = this.form().countryId;
    if (countryId === '' || countryId == null) return;
    this.api.getCountryCode(Number(countryId), this.orgId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.[0]) {
          this.updateField('countryCode', res.listObject[0].countryCode);
        } else {
          window.alert('Error!!'); // ctrl:219, verbatim
        }
      },
    });
  }

  // ---- "Symbol" file input -- validation only, never submitted, see class doc ----
  onSymbolFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    if (!file) return;
    const ext = file.name.split('.').pop()?.toLowerCase() ?? '';
    if (!VALID_FILE_EXTENSIONS.includes(ext)) {
      input.value = '';
      this.fileError.set(`Invalid file format. Allowed file formats are : ${VALID_FILE_EXTENSIONS.join(', ')}.`);
      return;
    }
    if (file.size > MAX_FILE_SIZE_BYTES) {
      input.value = '';
      this.fileError.set('File is too large. Upload a file less than 5MB.');
      return;
    }
    this.fileError.set('');
  }

  // ---- list (ctrl:90-140) ----
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.orgId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.set(res.status === 'success' ? (res.listObject ?? []) : []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.orgId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof CurrencyForm>(field: K, value: CurrencyForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 4 required fields (Country/Country Code/Currency Code/
  // Currency Name, view:19,39,62,79) that back `$scope.currencyform.$valid`.
  // No manual JS validation beyond this in legacy.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return f.countryId !== '' && !!(f.countryCode ?? '').trim() && !!(f.currencyCode ?? '').trim() && !!(f.currencyName ?? '').trim();
  }

  isCountryInvalid(): boolean {
    return this.formSubmitted() && this.form().countryId === '';
  }

  isCountryCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().countryCode ?? '').trim();
  }

  isCurrencyCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().currencyCode ?? '').trim();
  }

  isCurrencyNameInvalid(): boolean {
    return this.formSubmitted() && !(this.form().currencyName ?? '').trim();
  }

  // ---- save / confirm-modal (mirrors saveCurrency/PopupOkBtn, ctrl:227-293) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the currencyform.$valid gate (ctrl:253)
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
        currencyName: f.currencyName,
        countryId: Number(f.countryId),
        currencySymbol: '',
        currencyCode: f.currencyCode,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        orgnisationId: this.orgId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:268-273, verbatim shape
            this.resetForm(); // ctrl:267 -- init() runs on success only; re-arms popupArmed
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:275-280, verbatim shape
            // Legacy's real bug, faithfully reproduced (NOT fixed) -- see
            // class doc comment. popupArmed is deliberately NOT re-armed
            // here, matching ctrl:274-281 having no reset in this branch.
          }
          // ctrl:283-289 -- fires UNCONDITIONALLY, success or failure, see class doc.
          this.loadList();
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyCurrencyForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors init()'s popUpFlag = true
    this.loadCountryList(); // mirrors init() re-fetching the country dropdown
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:296-324 -- always a fresh GET, see
  // CurrencyRow's doc comment on why the already-loaded row can't be reused) ----
  edit(row: CurrencyRow): void {
    this.saveMode.set(false);
    this.api.getById(row.currencyId, this.orgId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.[0]) {
          window.alert('Error!!'); // ctrl:319-321, verbatim
          return;
        }
        const r = res.listObject[0];
        this.form.set({
          currencyId: r.currencyId,
          countryId: r.countryId,
          countryCode: r.countryCode,
          currencyCode: r.currencyCode,
          currencyName: r.currencyName,
        });
      },
    });
  }

  // ---- update (mirrors updateCurrency, ctrl:368-418 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the currencyform.$valid gate
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        currencyName: f.currencyName,
        currencyCode: f.currencyCode,
        currencyId: f.currencyId as number,
        countryId: Number(f.countryId), // see CurrencyUpdatePayload's doc comment -- backend silently drops this
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        orgnisationId: this.orgId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:395-400, verbatim shape
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:401-407, verbatim shape
          }
          // ctrl:409-413 -- fires UNCONDITIONALLY, success or failure, see class doc.
          this.loadList();
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:327-365 -- never reloads the list) ----
  toggleStatus(row: CurrencyRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- the view's checkbox is two-way bound in legacy, so
    // the visual change happens immediately regardless of the response
    // either way. See class doc comment: no reload on either branch.
    this.list.update((rows) => rows.map((r) => (r.currencyId === row.currencyId ? { ...r, status: nextStatus } : r)));

    this.loader.start();
    this.api
      .updateStatus({
        currencyId: row.currencyId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:349-354, verbatim shape
            // ctrl:348 -- a successful toggle calls $scope.init(), which
            // resets the create/edit form and re-fetches the country
            // dropdown, even though the toggle target is unrelated to
            // whatever's currently in the form. See class doc comment.
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:355-361, verbatim shape
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:235-237/328-330/369-371. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
