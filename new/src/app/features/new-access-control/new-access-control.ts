import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { NewAccessControlService } from './new-access-control.service';
import {
  ActionMasterForm,
  ActionMasterRow,
  ActionMasterSearchOption,
  emptyActionMasterForm,
} from './new-access-control.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/common/new-access-control.html +
 * resources/assets/js/controller/master/newAccessControlController.js
 * (405 lines, classic `$scope`, no `controllerAs`). Route wired in the
 * central `app.js` (~line 17156), moduleId 677, no `requireSuperUser`.
 * `$state`/`$cookies` ARE in this controller's own DI list but neither is
 * ever actually used anywhere in the file (confirmed via full-file grep)
 * -- there is no per-controller login guard here at all (unlike most
 * other screens' `cookieObject == undefined -> $state.go('login')`
 * pattern); the only access gate for this screen is the route-level
 * resolve block. This rebuild still applies the standard session guard
 * below for consistency with the rest of this codebase (matching where
 * legacy's OWN enforcement actually lives -- index.html's page-load-time
 * cookie check -- not inventing a new restriction).
 *
 * **Confirmed genuinely distinct from the OLD `accessControl` screen**
 * (`views/common/access-control.html`,
 * `resources/assets/accessControl/controller/accessControlController.js`)
 * -- different URL (`/accessControl` vs `/newAccessControl`), different
 * view, different controller, and a completely different feature: old
 * `accessControl` assigns per-role module permissions (`savePermissions`,
 * `accessFormObj.modules`, a role/privilege tree); `newAccessControl`
 * manages the underlying Action Master (menu/action hierarchy) rows
 * themselves via a completely separate `/api/global/*ActionMaster*`
 * endpoint family. Zero code or endpoint overlap. (Side-finding, out of
 * scope for this rebuild: the OLD `accessControl` state's own controller
 * name is registered TWICE in index.html, by two different files loaded
 * in sequence -- `resources/assets/accessControl/controller/
 * accessControlController.js` (real, `savePermissions`-based) then
 * `resources/assets/js/controller/access-control/access-control.js` (a
 * much simpler ui-tree demo controller) -- Angular's last-registration-
 * wins means the SECOND file is what actually backs the live
 * `accessControl` state today, an `addItemPOCreation`-shaped dead-code
 * shadow. Not this screen; flagged for whoever next touches `accessControl`.)
 *
 * All 6 endpoints confirmed real end-to-end (Controller -> Service ->
 * DAO) -- see new-access-control.service.ts's own doc comment.
 *
 * **Uses the shared `<modal>` directive** (view:143,
 * `<modal visible="showModal"></modal>`) -- per
 * `docs/ANGULAR_PENDING_FINDINGS.md` entry 55's already-resolved standing
 * policy, the directive's hardcoded close-button `ng-click="cancel();
 * stopLoader();"` would crash in legacy today (`newAccessControlController.js`
 * never defines `$scope.cancel` anywhere, confirmed via full-file grep) --
 * this is an accepted, disclosed standing improvement to fix in the
 * shared component, not a parity gap to reproduce. `closeConfirmModal()`
 * below is an ordinary, working close.
 *
 * **Findings, all reproduced faithfully, none fixed:**
 *
 * 1. **No validation at all before the Save confirm modal opens.** The
 *    Menu Name field has a visual `*` but no HTML5 `required`, no
 *    `<form>` wrapper, and no manual JS check anywhere in `save()`
 *    (ctrl:171-234) -- confirmed by reading the full function. Clicking
 *    Save with every field blank opens the confirm modal and, on
 *    confirm, POSTs an empty `actionName`; the backend DAO doesn't
 *    validate this either (`setActionName(actionMasterDto.getActionName())`,
 *    no null/blank check). Reproduced by not adding any validation gate
 *    here either.
 * 2. **`createdBy` is hardcoded to the literal `1`, not the real logged-in
 *    user** (`$scope.createdBy = 1`, ctrl:23, never reassigned anywhere)
 *    -- every row ever created via this screen is attributed to user id 1
 *    regardless of who's actually logged in. Reproduced verbatim below.
 * 3. **`actionLink`/`actionIcon` are always sent as `undefined`.** Both
 *    are read from bare `$scope.actionLink`/`$scope.actionIcon`
 *    (ctrl:186-187,294,296) -- never assigned anywhere in the controller,
 *    and the view has no input bound to either. Every row saved/updated
 *    via this screen always persists `action_link`/`action_icon` as
 *    null. Reproduced by simply never setting these in the save/update
 *    payloads.
 * 4. **`selectParentId`'s `parentActioName` assignment is always
 *    undefined, but harmless.** `SEARCH_ACTION_MASTER_BY_PARENT_ACTION_NAME`
 *    (the query backing the parent typeahead) only aliases `id`/`name`/
 *    `actionId` -- never `actionName` -- so `info.actionName` read by
 *    `selectParentId` (ctrl:78-84) is always `undefined`. Confirmed
 *    harmless: neither `saveActionMaster` nor `updateActionMaster`'s DAO
 *    ever reads `parentActioName` from the request DTO at all. Reproduced
 *    by simply never populating it in `selectParent()` below.
 * 5. **`showDetailsBtn`/`configDetails` (ctrl:238-240) are confirmed
 *    fully dead** -- no element anywhere in the view calls
 *    `showDetailsBtn`, and `configDetails` is never read anywhere either.
 *    Omitted entirely (non-observable).
 * 6. **The real search endpoint is commented out** (ctrl:61-74,
 *    `$scope.getSearchList`, a live-vs-commented-out guard per the
 *    playbook's sharpened checklist) -- the only search actually wired
 *    up is `ng-model="searchKeyword"` + `filter:searchKeyword` on the
 *    `ng-repeat` (view:72,97), which only ever filters the CURRENTLY
 *    LOADED PAGE's rows (`commonList`, capped at `noOfRecordsPerPage`) --
 *    the same "current-page-only search" quirk already catalogued for
 *    `doctorCategoryMaster`/`roleManagement` (entry 87) and
 *    `createTabularTemplates` (entry 94). Reproduced identically via a
 *    plain client-side filter over `list()`, not a real search call.
 * 7. **Status toggle never reverts, success or failure, and never
 *    reloads the list** -- `updateStatus` (ctrl:332-365) mutates
 *    `info.status` via the checkbox's own native two-way binding
 *    BEFORE the API call even starts (an unconditional optimistic
 *    flip), and its success/error handlers only show a toast + reset the
 *    add/edit form (`initMaster()`) -- neither branch reverts the flip
 *    or re-fetches the list. Same family as this codebase's
 *    `doctorCategory`/`generic-master` 406-revert precedent, but here
 *    there's no revert logic to begin with, in either direction.
 *    Reproduced via an unconditional local list update before the API
 *    call, no revert, no reload.
 * 8. **Delete ("x", `getRemovedAction`, ctrl:367-401) DOES reload the
 *    list on success** -- confirmed asymmetric with `updateStatus`
 *    despite calling the exact same `updateActionMasterStatus` endpoint
 *    (just with `status:'D'`); `GET_ACTION_MASTER_LIST`'s own query
 *    excludes `status != 'D'` rows, so the reload is what actually makes
 *    a deleted row disappear. Reproduced with a real list reload here,
 *    unlike the plain status toggle.
 * 9. **The "is Child" checkbox's show/hide of the parent-search field is
 *    raw jQuery in legacy** (`$("#checkMenu").click(...)`,
 *    `showUpdateBtn`'s own `$(".inputMenu").show()/.hide()`) -- rebuilt
 *    here as an ordinary reactive `showParentSearch` computed signal,
 *    same observable show/hide behaviour, no jQuery. Legacy never resets
 *    `parentId`/`name` when the checkbox is unchecked (confirmed: no such
 *    code exists) -- this rebuild doesn't either, so re-checking the box
 *    after unchecking still shows whatever parent was last selected,
 *    matching legacy exactly.
 * 10. **`showUpdateBtn` flips into edit mode synchronously, before the
 *     `getActionMasterById` response resolves** (ctrl:242-279) -- same
 *     family as `doctorCategory`'s own `showUpdateBtn` finding. Reproduced
 *     the same way: `saveMode.set(false)` fires immediately, the form
 *     itself only populates once the response arrives.
 */
@Component({
  selector: 'app-new-access-control',
  imports: [FormsModule, StatusToggle],
  templateUrl: './new-access-control.html',
  styleUrl: './new-access-control.scss',
})
export class NewAccessControl {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(NewAccessControlService);

  // ctrl:23 -- hardcoded, not the real session user. See class doc, finding 2.
  private readonly hardcodedCreatedBy = 1;

  list = signal<ActionMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  /** Client-side, current-page-only filter -- see class doc, finding 6. */
  visibleRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });

  form = signal<ActionMasterForm>(emptyActionMasterForm());
  saveMode = signal(true);
  showParentSearch = computed(() => this.form().isChild === 'Y');
  parentSuggestions = signal<ActionMasterSearchOption[]>([]);
  showConfirmModal = signal(false);

  constructor() {
    if (!this.session.current) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.loadListAndCount(this.currentPage(), this.pageSize());
  }

  // ---- list + pagination (mirrors initMasterList vs. masterList --
  // legacy only re-fetches the count on initial load / page-size change,
  // not on every page-navigation click; see class doc comment). ----
  private loadListAndCount(page: number, pageSize: number): void {
    const offset = (page - 1) * pageSize;
    this.api.getList(offset, pageSize).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount().subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  private loadListOnly(page: number, pageSize: number): void {
    const offset = (page - 1) * pageSize;
    this.api.getList(offset, pageSize).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount(1, this.pageSize());
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadListOnly(page, this.pageSize());
  }

  // ---- add/edit form ----
  updateForm<K extends keyof ActionMasterForm>(field: K, value: ActionMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  onIsChildChange(checked: boolean): void {
    this.updateForm('isChild', checked ? 'Y' : 'N');
  }

  // ---- parent-search typeahead (mirrors getListByName/selectParentId) ----
  onParentSearchInput(keyword: string): void {
    this.updateForm('name', keyword);
    if (!keyword.trim()) {
      this.parentSuggestions.set([]);
      return;
    }
    this.api.searchByParentActionName(keyword).subscribe({
      next: (res) => {
        if (res.status === 'success') this.parentSuggestions.set(res.listObject ?? []);
      },
    });
  }

  selectParent(option: ActionMasterSearchOption): void {
    this.updateForm('parentId', option.actionId);
    this.updateForm('name', option.name);
    // parentActioName intentionally left as-is -- see class doc, finding 4.
    this.parentSuggestions.set([]);
  }

  // typeahead-editable="false" -- reverts the typed text on blur if
  // nothing was actually selected (mirrors sub-speciality-master.ts's
  // onLedgerBlur()). Delayed so a click on a suggestion row registers first.
  onParentSearchBlur(): void {
    setTimeout(() => {
      if (!this.form().parentId) {
        this.updateForm('name', '');
      }
      this.parentSuggestions.set([]);
    }, 200);
  }

  // ---- save flow: Save validates nothing (see class doc, finding 1),
  // opens the confirm modal; only the modal's Ok actually POSTs. ----
  onSaveClick(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.create();
  }

  private create(): void {
    const f = this.form();
    const now = formatDmyHms(new Date());
    this.api
      .save({
        parentId: f.parentId,
        actionName: f.actionName,
        name: f.name,
        parentActioName: f.parentActioName,
        actionLink: undefined,
        actionIcon: undefined,
        createdBy: this.hardcodedCreatedBy,
        status: 'A',
        isChild: f.isChild,
        createdDate: now,
        grandParentActionName: f.grandParentActionName,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadListAndCount(this.currentPage(), this.pageSize());
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  // ---- update flow: no confirm modal at all, matching legacy's
  // update() being wired directly to the Update button. ----
  onUpdateClick(): void {
    this.update();
  }

  private update(): void {
    const f = this.form();
    this.api
      .update({
        actionId: f.actionId,
        parentId: f.parentId,
        actionName: f.actionName,
        parentActioName: f.parentActioName,
        grandParentActionName: f.grandParentActionName,
        actionLink: undefined,
        isChild: f.isChild,
        actionIcon: undefined,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadListAndCount(this.currentPage(), this.pageSize());
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyActionMasterForm());
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn -- flips into edit mode BEFORE the
  // getActionMasterById response resolves; see class doc, finding 10). ----
  edit(row: ActionMasterRow): void {
    this.saveMode.set(false);
    this.api.getById(row.actionId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          const isChild = (detail.parentId ?? 0) > 0;
          this.form.set({
            actionId: detail.actionId,
            actionName: detail.actionName ?? '',
            isChild: isChild ? 'Y' : 'N',
            name: isChild ? (detail.parentactioname ?? '') : '',
            parentActioName: isChild ? (detail.parentactioname ?? '') : '',
            parentId: detail.parentId,
            grandParentActionName: '',
          });
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- status toggle: unconditional optimistic flip, no revert, no
  // reload on success or failure -- see class doc, finding 7. ----
  toggleStatus(row: ActionMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.actionId === row.actionId ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus(row.actionId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.resetForm();
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- delete ("x") -- DOES reload the list on success; see class doc,
  // finding 8. ----
  remove(row: ActionMasterRow): void {
    this.api.updateStatus(row.actionId, 'D').subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Removed Successfully');
          this.resetForm();
          this.loadListAndCount(this.currentPage(), this.pageSize());
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }
}

function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
