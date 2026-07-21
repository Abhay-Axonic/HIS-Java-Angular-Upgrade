import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CreateTabularTemplatesService } from './create-tabular-templates.service';
import { TabularTemplateFormState, TabularTemplateRow, emptyTabularTemplateForm } from './create-tabular-templates.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/template/create-tabular-templates.html +
 * resources/assets/js/controller/template/createTabularTemplatesController.js
 * ("createTabularTemplatesController", state `createTabularTemplates`,
 * app.js:16584-16611, moduleId 736, no requireSuperUser check). Genuinely
 * distinct from the already-built create-tabular-data-templates (the
 * row/column data-mapper screen, `getActiveTabularTemplateMaster` /
 * `saveTabularTemplateDataMapper`) -- this screen is the master CRUD for
 * template code/description that the mapper screen's dropdown reads from.
 * Both share moduleId 736 and the same TabularTemplateMasterController.
 *
 * **Step 0 note:** the legacy view/inline script is a literal, un-adapted
 * copy of `views/master/GlobalMaster/cabin-master.html` -- the page
 * `<legend>` (commented out) said "Cabin Master", the form's own `name`
 * attribute is `cabinMasterForm`, and the bottom `<script>` sets
 * `.selectedPageName` to "Cabin Master" and highlights the sidebar link
 * for `cabinMaster`, not this screen. None of that affects real behavior
 * (the actual fields/bindings -- `tabularTemplateCode`/`tabularTemplateDesc`
 * -- are correctly wired to the real Tabular Template endpoints, confirmed
 * against TabularTemplateMasterController.java), so this is NOT the
 * addItemPOCreation/OTBillingCompletion "wrong controller" shape -- just a
 * cosmetic copy-paste leftover. The real page title (used here) is the
 * state's own `title: 'Tabular Template'` (app.js:16588), not the leftover
 * "Cabin Master" text baked into the legacy HTML.
 *
 * **Deliberate quirks/bugs preserved, not fixed (see this screen's drafted
 * docs/ANGULAR_PENDING_FINDINGS.md entry):**
 * - Search (`ng-repeat="info in commonList|orderBy:column:reverse|filter:searchKeyword"`,
 *   view:123) filters ONLY the current page's already-fetched rows
 *   (`commonList`, the same array `getTabularTemplateMasterList`'s
 *   response overwrites on every page/size change) -- there is no
 *   server-side search endpoint call at all. A real template that exists
 *   in the database but isn't on the currently-displayed page will not be
 *   found. Same bug shape as this codebase's already-documented
 *   assetCategoryMaster finding (entry 10, bug 2). Reproduced via
 *   `filteredPageList`, a pure client-side filter over `list()` (the
 *   current page only), not a real search call.
 * - `column`/`reverse` (the `orderBy` clause) are never assigned anywhere
 *   in the controller -- confirmed by grep of the full file -- so the
 *   sort-caret icons in the legacy table header are purely decorative and
 *   have no `ng-click` wired to them either. No sort control implemented
 *   here; this is not a missing feature, legacy's own carets do nothing.
 * - Save (`save()`, ctrl:148-206) only opens the confirm modal when the
 *   form is ALREADY valid (`cabinMasterForm.$valid` checked before
 *   `showModal` flips) -- an empty required field is a silent no-op, no
 *   growl, no modal. Update (`update()`, ctrl:244-293) has NO confirm
 *   modal at all, saves immediately if valid. Same create/update
 *   asymmetry already documented in this codebase's speciality-master.
 * - The legacy view's `<modal visible="showModal">` is the SAME shared,
 *   prototypal-scope directive documented in
 *   docs/ANGULAR_PENDING_FINDINGS.md entry 55 -- confirmed this
 *   controller defines no `cancel()` anywhere (grep of the full file), so
 *   legacy's own × close button crashes on click today. Per entry 55's
 *   standing policy, this rebuild's close button is a normal, WORKING
 *   `closeConfirmModal()` -- a disclosed standing improvement, not
 *   silently smoothing over a screen-specific bug (this is the 13th
 *   confirmed instance of that shared-directive gap, extending entry 55's
 *   blast-radius count).
 * - **Disclosed, deliberate deviation (not silently fixed) --
 *   `updateStatus` (ctrl:295-331):** legacy mutates the SAME `$scope.common`
 *   object used by the create/edit form (`common.tabularTemplateId`,
 *   `.status`, `.updatedBy`, `.updatedDate` are all overwritten
 *   synchronously, before the HTTP call even fires) to build the status-
 *   change payload -- so toggling ANY row's status while a DIFFERENT
 *   row's edit form is open corrupts that in-progress edit (a real,
 *   confirmed-via-source race: a click on "Update" during the toggle's
 *   in-flight window would silently overwrite the wrong record). This
 *   rebuild's `onToggleStatus()` builds its own independent payload from
 *   the row being toggled and never touches the `form` signal mid-flight
 *   -- avoiding the corruption window entirely, per the same precedent
 *   already set by this codebase's insurance-master ledger-race deviation
 *   (entry 4): a real, narrow race nobody would want deliberately kept is
 *   disclosed, not re-built on purpose. The ONE part of legacy's behavior
 *   that IS reproduced: `initMaster()` unconditionally resets the
 *   create/edit form back to blank/create-mode after ANY row's status
 *   toggle succeeds, even one unrelated to whatever's open -- this part is
 *   simple, deterministic, and genuinely observable (not a race), so
 *   `onToggleStatus()` still calls `resetForm()` on success.
 */
@Component({
  selector: 'app-create-tabular-templates',
  imports: [FormsModule, StatusToggle],
  templateUrl: './create-tabular-templates.html',
  styleUrl: './create-tabular-templates.scss',
})
export class CreateTabularTemplates {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CreateTabularTemplatesService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- list + server-side pagination (mirrors initMasterList/PagerService, ctrl:56-136) ----
  list = signal<TabularTemplateRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ---- search: client-side, CURRENT PAGE ONLY -- see class doc ----
  searchKeyword = signal('');
  filteredPageList = computed(() => {
    const q = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!q) return rows;
    return rows.filter(
      (r) =>
        (r.tabularTemplateCode ?? '').toLowerCase().includes(q) ||
        (r.tabularTemplateDesc ?? '').toLowerCase().includes(q),
    );
  });

  // ---- form state (mirrors $scope.common, ctrl:43-50) ----
  form = signal<TabularTemplateFormState>(emptyTabularTemplateForm());
  saveMode = signal(true); // true = saveBtnFlag (create); false = updateBtnFlag (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  codeInvalid = computed(() => this.formSubmitted() && !this.form().tabularTemplateCode);
  descInvalid = computed(() => this.formSubmitted() && !this.form().tabularTemplateDesc);
  private isFormValid = computed(() => !!this.form().tabularTemplateCode && !!this.form().tabularTemplateDesc);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
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

  onSearchInput(term: string): void {
    this.searchKeyword.set(term);
  }

  private resetForm(): void {
    this.form.set(emptyTabularTemplateForm());
    this.saveMode.set(true);
    this.formSubmitted.set(false);
  }

  updateField<K extends keyof TabularTemplateFormState>(field: K, value: TabularTemplateFormState[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  /** Mirrors save() (ctrl:148-206): only opens the confirm modal when the
   * form is already valid -- an empty required field is a silent no-op. */
  save(): void {
    this.formSubmitted.set(true);
    if (!this.isFormValid()) return;
    this.showConfirmModal.set(true);
  }

  /** Working close button per entry 55's standing policy -- see class doc. */
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    const payload = {
      tabularTemplateCode: f.tabularTemplateCode,
      tabularTemplateDesc: f.tabularTemplateDesc,
      organizationId: this.organizationId,
      unitId: this.unitId,
      createdBy: this.userId,
      updatedBy: this.userId,
      status: 'A',
      createdDate: now,
      updatedDate: now,
    };
    this.api.create(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Saved successfully');
          this.resetForm();
          this.loadList();
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
    });
  }

  /** Mirrors update() (ctrl:244-293): no confirm modal, saves immediately
   * if valid -- the same create/update asymmetry already documented in
   * this codebase's speciality-master. */
  update(): void {
    this.formSubmitted.set(true);
    if (!this.isFormValid()) return;
    const f = this.form();
    const payload = {
      tabularTemplateId: f.tabularTemplateId,
      tabularTemplateDesc: f.tabularTemplateDesc,
      tabularTemplateCode: f.tabularTemplateCode,
      organizationId: this.organizationId,
      unitId: this.unitId,
      updatedBy: this.userId,
      updatedDate: formatDateTime(new Date()),
    };
    this.api.update(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Updated successfully');
          this.resetForm();
          this.loadList();
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
    });
  }

  /** Mirrors showUpdateBtn (ctrl:213-241). */
  edit(row: TabularTemplateRow): void {
    this.api.getById(row.tabularTemplateId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const details = res.listObject?.[0];
          if (details) {
            this.form.set({
              tabularTemplateId: details.tabularTemplateId,
              tabularTemplateCode: details.tabularTemplateCode,
              tabularTemplateDesc: details.tabularTemplateDesc,
            });
            this.saveMode.set(false);
            this.formSubmitted.set(false);
          }
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
    });
  }

  /** See class doc's "disclosed, deliberate deviation" note: builds an
   * independent payload from `row` rather than mutating the shared `form`
   * signal, avoiding legacy's real edit-form-corruption race -- but still
   * resets the form to blank/create-mode on success, matching legacy's
   * own unconditional `initMaster()` call. */
  onToggleStatus(row: TabularTemplateRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    const payload = {
      tabularTemplateId: row.tabularTemplateId,
      status: nextStatus,
      updatedBy: this.userId,
      updatedDate: formatDateTime(new Date()),
    };
    this.api.updateStatus(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) =>
            rows.map((r) => (r.tabularTemplateId === row.tabularTemplateId ? { ...r, status: nextStatus } : r)),
          );
          this.notify.success(res.message ?? 'Updated successfully');
          this.resetForm();
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:155-156/247-248/297-298. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
