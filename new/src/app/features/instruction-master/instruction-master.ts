import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { InstructionMasterService } from './instruction-master.service';
import {
  AddedLanguageEntry,
  DeptSpecialityDetail,
  InstructionMasterRow,
  LanguageOption,
  RawSpecialityOption,
  SaveInstructionPayload,
  SpecialityOption,
  TranslatedInstructionRow,
  emptyInstructionForm,
} from './instruction-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/instruction-master.html +
 * resources/assets/js/controller/master/global/common/inspectionMasterController.js.
 *
 * **Naming quirk, re-verified this batch, confirmed real and deliberate --
 * not a dead/shadowed route.** Read directly from the live `.state(...)`
 * block in app.js:16615-16642: state name `instructionMaster`
 * (url `/instructionMaster`, the ONLY registration of this state name
 * anywhere in app.js -- grepped the whole file), `controller:
 * "inspectionMasterController"`, module id 501, no `requireSuperUser`
 * check. The view's every `vm.*`-would-be binding is actually classic
 * `$scope.*` (no `controllerAs` in the state config, none used in the
 * view either) and resolves against real functions in
 * `inspectionMasterController.js` -- confirmed by reading both files in
 * full and grepping every `ng-click`/`ng-model` target.
 *
 * **SEVERITY-1-ADJACENT, PROMINENTLY FLAGGED PER EXPLICIT INSTRUCTION: the
 * `apertium.org` reference is NOT simply dead/commented code -- it is very
 * likely a genuinely live, unconditional external network call on every
 * load of this screen, via a DIFFERENT code path than the one that looked
 * dead at first read.** `inspectionMasterController.js:123-140`'s own
 * `onclickAjax()` function (the one with the apertium.org URL literally
 * inside it) IS fully commented out there and never called from that
 * file -- but the view (`instruction-master.html:397`) unconditionally
 * `ng-include`s a SEPARATE shared partial,
 * `views/OPD/CoverSheet/customise-language.html`, on every page load (not
 * gated behind opening the "Customise Language" modal). That partial's own
 * trailing `<script>` block (customise-language.html:158-190) has a THIRD,
 * independent copy of this same jQuery/JSONP call to
 * `https://www.apertium.org/apy/translate?q=hello%20world&langpair=en|es`
 * -- and here, only the wrapping `//  function onclickAjax() {` /
 * `//  }` comment lines are commented out; the actual
 * `$('input:text').each(function() {...ajax...});` call body in between is
 * NOT commented out at all, and executes as a plain top-level statement
 * the instant this script block runs.
 *
 * Whether it actually runs turns on how AngularJS/jQuery deliver
 * `ng-include`d HTML into the live DOM. Confirmed via source:
 * `index.html:308` loads `jquery.min.js` BEFORE `index.html:345` loads
 * `angular.min.js` -- so `angular.element` is real jQuery for this whole
 * app, not jqLite. jQuery's own DOM-insertion methods (`.html()`/
 * `.append()`, which AngularJS's template-swap/`ng-include` machinery uses
 * under the hood when jQuery is present) DO evaluate embedded `<script>`
 * tags on insertion -- including `src`-based ones -- unlike plain
 * `.innerHTML=`. This is independently corroborated by an already-relied-
 * upon, confirmed-working pattern in THIS exact codebase: every view
 * template (including this one, instruction-master.html:404-409) ends
 * with a raw `<script>$(".selectedPageName").text(...)...</script>` block
 * that reliably updates the sidebar title on every real navigation --
 * something that could not work at all if `ng-include`/`templateUrl`-
 * delivered script tags were inert. The same Google Translate widget
 * script tag (`//translate.google.com/translate_a/element.js`) is ALSO
 * unconditionally present in this same ng-included partial.
 *
 * I attempted to settle this with an actual live network capture (the
 * legacy app is running locally on :8080) rather than rely on inference
 * alone, but was blocked by an unrelated, pre-existing crash:
 * `loginController.js:124` throws
 * `TypeError: Cannot read properties of undefined (reading
 * 'organizationLogo')` inside `getUnitByUserCredentials()`, which prevents
 * the Unit dropdown from ever populating for the documented
 * `admin`/`12345`/DEMO local test login in this environment -- so no
 * session could be established to capture real network traffic tonight.
 * This login-flow bug is unrelated to this screen and is called out
 * separately as a cross-cutting environment blocker, not fixed here.
 *
 * **Decision made for this rebuild, pending owner sign-off: NEITHER the
 * apertium.org call NOR the Google Translate widget script is wired into
 * this rebuild.** Per the explicit instruction governing this batch, an
 * external network dependency (a different risk class than a missing
 * internal backend) must be flagged, not silently ported. This is
 * flagged, not silently wired up. If the owner confirms live behavior
 * matches this analysis, legacy has an undisclosed, real dependency on a
 * third-party translation service on every load of this admin screen --
 * worth its own decision (drop it, keep it disclosed, or replace it)
 * entirely independent of this rebuild.
 *
 * **`$translate`-based `convertLan()` (ctrl:275-280) is dead/unreachable
 * code, confirmed separately from the above** -- the live `<select
 * ng-model="LanguObj">` in customise-language.html (line 73) has NO
 * `ng-change` wired to it (a commented-out sibling at lines 66-72 shows
 * the abandoned wiring); `convertLan()` (which references an undefined
 * `$translate` service and would throw if ever called) cannot be reached
 * from any live UI element. Not reproduced.
 *
 * **Shared `<modal>` directive close-button crash (entry 55 policy
 * applies).** `<modal visible="showModal"></modal>` (view:220) is the
 * same shared, prototypal-scope directive from entry 55 -- confirmed no
 * `cancel()` is defined anywhere in `inspectionMasterController.js`
 * (grep), so legacy's own × close button throws `cancel is not a
 * function` on click today. This screen's actual `showModal`/`cancel()`
 * combination is not otherwise used for anything (no confirm-before-save
 * flow exists here, unlike `systemObservationMaster`) -- so this rebuild
 * simply never renders that particular dead modal at all, per the
 * standing policy (no working equivalent needed since legacy's own
 * version does nothing functional either).
 *
 * **A confirmed, real, live crash bug: the search box's `ng-keyup`
 * handler calls a function that does not exist anywhere in this
 * controller.** `ng-keyup="serachTestmaster(searchKeyword);"`
 * (view:90) -- `serachTestmaster` is not defined anywhere in
 * `inspectionMasterController.js` (grep). This throws (caught/logged by
 * Angular's `$exceptionHandler`, not a blank page) on every keystroke in
 * the search box, in legacy, today. It has ZERO functional effect on
 * search itself, though: the actual filtering is done entirely by the
 * `ng-model="searchKeyword"` two-way binding feeding the
 * `| filter:searchKeyword` pipe already applied to `commonList` in the
 * table (view:180) -- independent of the keyup handler succeeding or
 * failing. This rebuild reproduces the WORKING half (client-side filter on
 * `searchKeyword`) and does not attempt to reproduce a redundant,
 * side-effect-free crash that a strongly-typed rebuild has no equivalent
 * broken call for -- there is no user-visible behavior difference either
 * way. Flagged, not silently normalized into "the bug never existed."
 *
 * **A real, confirmed status/filter-vocabulary drift vs. sibling
 * `systemObservationMaster` (both "Admin - Global Master (general)",
 * audited together this batch):** this screen's Department multiselect
 * shows EVERY active speciality from `getSpecialityList`
 * (ctrl:496-528) with no `isClinicalSpeciality` filter at all, while
 * `systemObservationMaster`'s otherwise-structurally-identical Speciality
 * multiselect filters to `isClinicalSpeciality=='Y'` only
 * (systemObservationMasterController.js:180-187). Confirmed by reading
 * both controllers in full -- not assumed from the shared "Admin" module
 * grouping. Preserved as two independently-correct behaviors, not
 * normalized to match each other.
 *
 * **The search box only filters the CURRENT PAGE's already-fetched rows**
 * -- this screen paginates server-side (`getInspectionMasterList` sends
 * `offset`/`noOfRecordsPerPage`; a separate `getInspectionMasterCount`
 * call drives the pager), and the `filter:searchKeyword` pipe only ever
 * sees whatever page is currently loaded into `commonList`. No
 * "search across all records" endpoint is wired to this screen's search
 * box in legacy (the several `getSearchedInstructionList`-family endpoints
 * on `InstructionMasterController.java` exist but are never called from
 * this controller -- they back a DIFFERENT consumer, an instruction-picker
 * autocomplete elsewhere, not this master-list screen). Reproduced
 * exactly: this rebuild's search also only filters the current page.
 *
 * **`defaultSet()`'s duplicate-entry bug, faithfully reproduced.**
 * `ng-blur="defaultSet()"` on the Description field (view:36) pushes a new
 * "English" entry into `addedLanguageList` EVERY time the field loses
 * focus, without the duplicate-language check `addLanguageDetails()` does
 * (ctrl:247-260) -- tabbing in and out of the field repeatedly stacks
 * duplicate English rows. Reproduced exactly, not deduplicated.
 *
 * **No edit path for Department mappings exists on update** -- `update()`
 * (ctrl:420-493) always resends whatever is currently in
 * `selectedSpecialityType`/`addedLanguageList`, matching `showUpdateBtn`'s
 * own population of those from the GET-by-id response
 * (ctrl:386-417) -- reproduced as a normal edit-then-resubmit flow, not a
 * partial-update.
 */
@Component({
  selector: 'app-instruction-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './instruction-master.html',
  styleUrl: './instruction-master.scss',
})
export class InstructionMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(InstructionMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- list + server-side pagination (mirrors initInspectionMasterList/
  // getInspectionList, ctrl:57-114) ----
  list = signal<InstructionMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Search filters only the CURRENT PAGE's already-fetched rows -- see
  // class comment. Not a rebuild simplification.
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.instructionCode?.toLowerCase().includes(keyword) || r.instructionDesc?.toLowerCase().includes(keyword),
    );
  });

  // ---- master data ----
  specialityMasterList = signal<SpecialityOption[]>([]);
  languageList = signal<LanguageOption[]>([]);

  // ---- create/edit form state (mirrors $scope.common, ctrl:36-48) ----
  form = signal(emptyInstructionForm());
  updateMode = signal(false);
  submitted = signal(false);
  private selectedSpecialityIds = signal<Set<number>>(new Set());
  showDeptDropdown = signal(false);
  addedLanguageList = signal<AddedLanguageEntry[]>([]);

  selectedSpecialityLabels = computed(() => {
    const ids = this.selectedSpecialityIds();
    return this.specialityMasterList()
      .filter((s) => ids.has(s.id))
      .map((s) => s.label);
  });
  isSpecialityChecked = (id: number) => this.selectedSpecialityIds().has(id);

  // ---- "Customise Language" modal (mirrors assignToLang/addLanguageDetails/
  // removeLanguage, ctrl:116-267) -- only reachable when isMedication=='Y',
  // matching view:66-70's ng-if. NOT wired to any external translate call --
  // see class comment (apertium.org / Google Translate widget flagged, not
  // implemented). ----
  showCustomiseLanguageModal = signal(false);
  languageSelection = signal<LanguageOption | null>(null);
  convertedTextDraft = signal('');

  // ---- "Department Details" read-only info popup (mirrors showFieldList,
  // ctrl:531-561) ----
  showDeptDetailsModal = signal(false);
  deptDetailsForModal = signal<DeptSpecialityDetail[]>([]);

  // ---- "Customise Language" read-only details popup (mirrors
  // showLanguageList, ctrl:563-590) ----
  showLanguageDetailsModal = signal(false);
  languageDetailsForModal = signal<TranslatedInstructionRow[]>([]);

  constructor() {
    // Order matches legacy's actual source order exactly (ctrl:305/529/641):
    // initInspectionMasterList() (list+count) fires first, then
    // getSpecialityMasterList(), then getLanguageMasterList() last. All
    // three are independent (no data dependency between them), so this is
    // source-fidelity, not a functional requirement -- confirmed via the
    // golden-trace capture too (same relative order observed there).
    this.loadList();
    this.loadSpecialityMasterList();
    this.loadLanguageMasterList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  private loadSpecialityMasterList(): void {
    this.api.getSpecialityList(this.organizationId, this.unitId).subscribe({
      next: (res: { status: string; listObject?: RawSpecialityOption[] }) => {
        if (res.status === 'success') {
          this.specialityMasterList.set(
            (res.listObject ?? []).map((v) => ({ id: v.specialityId, label: v.specialityName })),
          );
        }
      },
    });
  }

  // See docs/ANGULAR_PENDING_FINDINGS.md entry 114 -- known background-only
  // HttpClient-parsing risk on this exact shared endpoint, not fixed here.
  private loadLanguageMasterList(): void {
    this.api.getLanguageMasterList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.languageList.set(res.listObject ?? []);
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

  toggleDeptDropdown(): void {
    this.showDeptDropdown.update((v) => !v);
  }

  toggleDept(id: number): void {
    const next = new Set(this.selectedSpecialityIds());
    if (next.has(id)) next.delete(id);
    else next.add(id);
    this.selectedSpecialityIds.set(next);
  }

  /** Mirrors defaultSet(), ctrl:282-289 -- pushes a duplicate "English"
   * entry on every blur, with NO duplicate check (unlike addLanguageDetails
   * below). Faithfully reproduced, not deduplicated -- see class comment. */
  onDescriptionBlur(): void {
    this.addedLanguageList.update((list) => [
      ...list,
      { desc: 'English', languageId: '1', convertedText: this.form().instructionDesc },
    ]);
  }

  openCustomiseLanguageModal(): void {
    this.convertedTextDraft.set(this.form().instructionDesc);
    this.showCustomiseLanguageModal.set(true);
  }

  closeCustomiseLanguageModal(): void {
    this.showCustomiseLanguageModal.set(false);
  }

  /** Mirrors addLanguageDetails(), ctrl:198-267 -- DOES check for an
   * existing entry with the same languageId before pushing (unlike
   * defaultSet above), matching legacy's own asymmetry exactly. */
  addLanguageDetail(): void {
    const selection = this.languageSelection();
    const convertedText = this.convertedTextDraft().trim();
    if (!selection) {
      this.notify.error('Please select language');
      return;
    }
    if (!convertedText) {
      this.notify.error('Please fill-up text');
      return;
    }
    const entry: AddedLanguageEntry = {
      desc: selection.desc,
      languageId: String(selection.languageId),
      convertedText,
    };
    const exists = this.addedLanguageList().some((e) => e.languageId === entry.languageId);
    if (exists) {
      this.notify.error('Already Exist..');
      return;
    }
    this.addedLanguageList.update((list) => [...list, entry]);
    this.languageSelection.set(null);
    this.convertedTextDraft.set('');
  }

  removeLanguageDetail(index: number): void {
    this.addedLanguageList.update((list) => list.filter((_, i) => i !== index));
  }

  save(): void {
    this.submitted.set(true);
    const f = this.form();
    if (!f.instructionCode.trim()) {
      this.notify.warning('Please Enter Instruction Code');
      return;
    }
    if (!f.instructionDesc.trim()) {
      this.notify.warning('Please Enter Instruction Description');
      return;
    }

    const now = formatYmdHms(new Date());
    const payload: SaveInstructionPayload = {
      instructionCode: f.instructionCode,
      instructionDesc: f.instructionDesc,
      isMedication: f.isMedication,
      instructionId: f.instructionId,
      createdBy: this.userId,
      updatedBy: this.userId,
      createdDate: now,
      updatedDate: now,
      unitId: this.unitId,
      organizationId: this.organizationId,
      status: 'A',
      deptSpecilityId: [...this.selectedSpecialityIds()].map((id) => ({ id })),
      addedLanguageList: this.addedLanguageList(),
    };

    const call = this.updateMode() ? this.api.update(payload) : this.api.save(payload);
    call.subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.resetForm();
          this.loadList();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  private resetForm(): void {
    this.form.set(emptyInstructionForm());
    this.updateMode.set(false);
    this.submitted.set(false);
    this.selectedSpecialityIds.set(new Set());
    this.showDeptDropdown.set(false);
    this.addedLanguageList.set([]);
  }

  /** Mirrors showUpdateBtn(), ctrl:386-417. */
  edit(row: InstructionMasterRow): void {
    this.api.getById(row.instructionId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error('Something went wrong');
          return;
        }
        const obj = (res.object ?? {}) as Record<string, unknown>;
        this.form.set({
          instructionId: row.instructionId,
          instructionCode: String(obj['instructionCode'] ?? row.instructionCode),
          instructionDesc: String(obj['instructionDesc'] ?? row.instructionDesc),
          isMedication: (obj['isMedication'] as 'Y' | 'N') ?? row.isMedication,
        });
        this.selectedSpecialityIds.set(new Set((res.listObject ?? []).map((d) => d.id)));
        this.addedLanguageList.set(
          (res.resObject ?? []).map((t) => ({
            desc: t.desc,
            languageId: String(t.languageId ?? ''),
            convertedText: t.convertedText,
          })),
        );
        this.updateMode.set(true);
        this.submitted.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:593-627) ----
  toggleStatus(row: InstructionMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateStatus({
        instructionId: row.instructionId,
        status: nextStatus,
        updatedDate: formatDmyHms(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.update((rows) =>
              rows.map((r) => (r.instructionId === row.instructionId ? { ...r, status: nextStatus } : r)),
            );
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- "Department Details" info popup (mirrors showFieldList, ctrl:531-561) ----
  viewDeptDetails(row: InstructionMasterRow): void {
    this.api
      .getDeptSpecilityById({ organizationId: this.organizationId, unitId: this.unitId, instructionId: row.instructionId })
      .subscribe({
        next: (res) => {
          this.deptDetailsForModal.set(res.status === 'success' ? res.listObject ?? [] : []);
          this.showDeptDetailsModal.set(true);
        },
      });
  }

  closeDeptDetailsModal(): void {
    this.showDeptDetailsModal.set(false);
  }

  // ---- "Customise Language" read-only details popup (mirrors
  // showLanguageList, ctrl:563-590) ----
  viewLanguageDetails(row: InstructionMasterRow): void {
    this.api
      .getConvertedLanguageListById({
        organizationId: this.organizationId,
        unitId: this.unitId,
        instructionId: row.instructionId,
      })
      .subscribe({
        next: (res) => {
          this.languageDetailsForModal.set(res.status === 'success' ? res.listObject ?? [] : []);
          this.showLanguageDetailsModal.set(true);
        },
      });
  }

  closeLanguageDetailsModal(): void {
    this.showLanguageDetailsModal.set(false);
  }
}

/** `moment(new Date()).format('YYYY-MM-DD HH:mm:ss')`, matching ctrl:316-317/424-425. */
function formatYmdHms(d: Date): string {
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd} ${hh}:${min}:${ss}`;
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:595-596. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
