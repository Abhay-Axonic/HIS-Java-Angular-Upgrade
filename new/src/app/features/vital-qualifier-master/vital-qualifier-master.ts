import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { VitalQualifierMasterService } from './vital-qualifier-master.service';
import {
  VitalCategoryOption,
  VitalQualifierForm,
  VitalQualifierRow,
  emptyVitalQualifierForm,
} from './vital-qualifier-master.models';

/**
 * Parity rebuild of views/master/GlobalMaster/vital-qualifier-master.html +
 * resources/assets/js/controller/master/global/vitalQualifierMasterController.js
 * (`vitalQualifierMasterController`, classic `$scope`, no `controllerAs`).
 *
 * **Self-registered state, not in app.js's central registry** -- this
 * controller file registers its own ui-router state via a `.config()` block
 * at the bottom of the file (ctrl:352-368, `$stateProvider.state("vitalQualifierMaster", ...)`),
 * confirmed by direct read and cross-checked against
 * `tools/angular-audit/data/states_enriched.json` (`"line": 340,
 * "source_file": "...vitalQualifierMasterController.js"`). `has_resolve: false`
 * -- no module-access route guard, matching the already-established
 * `billingGroupMaster`/`periodicityService` precedent (module gating enforced
 * only by the sidebar's own `checkAccess(953)`, not the router). `data.moduleId`
 * correctly omitted from this rebuild's route.
 *
 * **Confirmed real, fully-functional CRUD -- no known findings.** Live
 * round trip confirmed 2026-07-28 (real auth session): a Vital Category row
 * had to be seeded directly (dev DB had zero rows and no UI screen exists to
 * add one -- `getVitalCategoryList`'s named query has no admin screen calling
 * its counterpart save endpoint anywhere in the app), then create/edit/
 * status-toggle all confirmed against the real backend, all HTTP 200, test
 * rows cleaned up after.
 *
 * **Real, confirmed-live quirks reproduced below:**
 * - **No client-side validation at all.** Both `save()` and `update()`
 *   (ctrl:149-207/245-295) have their `$scope.commonform.$valid`-equivalent
 *   checks commented out entirely (`// if ($scope.cabinMasterForm.$valid) {`)
 *   -- confirmed by direct read, not inferred. The view's `required`
 *   attributes are inert; there is no wrapping `<form>` element at all.
 *   Reproduced by NOT gating submission on field completeness (matching
 *   legacy exactly -- an empty Code/Description genuinely can be submitted).
 * - **The confirm modal does not auto-close after a successful Save.**
 *   `save()`'s real-persist branch (ctrl:176-202) never sets `$scope.showModal
 *   = false` -- confirmed live: after a successful save (toast shown, list
 *   refreshed behind it), the modal stays visibly open until the user
 *   manually clicks Cancel or the header X (both work, since both carry
 *   Bootstrap's own `data-dismiss="modal"` independent of the
 *   `cancel()`/`stopLoader()` functions the header X's `ng-click` calls --
 *   neither is defined anywhere in this controller, confirmed via grep, but
 *   both are harmless no-ops since `data-dismiss` closes the modal at the
 *   jQuery/Bootstrap layer regardless). This is a distinct quirk from the
 *   already-documented entry-55 "close button crashes" class -- the close
 *   buttons here already work; the gap is specifically that Save doesn't
 *   auto-dismiss on success. Faithfully reproduced: `confirmFromModal()`
 *   does not close `showConfirmModal` on success, only on explicit Cancel/X.
 * - `showUpdateBtn` (ctrl:214-242) assigns the fetched record onto
 *   `$scope.common` wholesale, then does one extra, inert line
 *   (`$scope.common.cabinId = response.data.listObject[0].cabinId`) -- a
 *   harmless copy-paste leftover from a sibling screen (this DTO has no
 *   `cabinId` field; both sides evaluate to `undefined`). Not reproduced --
 *   zero behavioral effect.
 * - `updateStatus` (ctrl:297-330) sends legacy's *entire* `$scope.common`
 *   object, not just id/status -- confirmed harmless via direct DAO read
 *   (`VitalQualifireMasterDaoImpl.updateVitalQualifireMasterStatus` does
 *   `findById()` first, then mutates only `status`/`updatedBy`/`updatedDate`
 *   on the real fetched entity, ignoring the rest of the payload). Modeled
 *   here as a minimal, correctly-scoped payload -- functionally identical,
 *   no wrong-record risk.
 * - Real client-side search (`filter:searchKeyword` over the currently
 *   fetched page) and real pagination, both reproduced as working (matching
 *   this codebase's `district-master`-style siblings, not a dead-search
 *   screen).
 */
@Component({
  selector: 'app-vital-qualifier-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './vital-qualifier-master.html',
  styleUrl: './vital-qualifier-master.scss',
})
export class VitalQualifierMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(VitalQualifierMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  categoryOptions = signal<VitalCategoryOption[]>([]);
  form = signal<VitalQualifierForm>(emptyVitalQualifierForm());
  saveMode = signal(true);
  showConfirmModal = signal(false);
  searchKeyword = signal('');

  rows = signal<VitalQualifierRow[]>([]);
  totalCount = signal(0);

  displayedRows = () => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  };

  constructor() {
    this.loadCategoryList();
    this.loadList();
  }

  private loadCategoryList(): void {
    this.api.getCategoryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.categoryOptions.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    this.api.getList(this.organizationId, 0, 10, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe();
  }

  updateField<K extends keyof VitalQualifierForm>(field: K, value: VitalQualifierForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // mirrors $scope.save (ctrl:149-207) -- no field validation (see class doc
  // comment), confirm-modal-gated once per page session? No -- popUpFlag IS
  // reset by initMaster() on success, matching every-save-confirms.
  onSaveClick(): void {
    this.showConfirmModal.set(true);
  }

  // Cancel/X -- both work in legacy via Bootstrap's own data-dismiss (see
  // class doc comment); modeled as a single explicit close here.
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn -> save() real-persist branch. Deliberately does NOT
  // close the modal on success -- see class doc comment for why this
  // faithfully matches legacy (the modal only closes via explicit Cancel/X).
  confirmFromModal(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        vitalCategoryId: f.vitalCategoryId,
        vitalQualifireCode: f.vitalQualifireCode,
        vitalQualifireDesc: f.vitalQualifireDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Added Successfully.');
            this.form.set(emptyVitalQualifierForm());
          } else {
            this.notify.error(res.message ?? 'Error');
          }
          this.loadList();
        },
      });
  }

  // mirrors $scope.update (ctrl:245-295) -- no confirm-modal gate, no
  // validation (see class doc comment).
  onUpdateClick(): void {
    const f = this.form();
    this.api
      .update({
        vitalQualifireId: f.vitalQualifireId as number,
        vitalCategoryId: f.vitalCategoryId,
        vitalQualifireCode: f.vitalQualifireCode,
        vitalQualifireDesc: f.vitalQualifireDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
            this.form.set(emptyVitalQualifierForm());
            this.saveMode.set(true);
          } else {
            this.notify.error(res.message ?? 'Error');
          }
          this.loadList();
        },
      });
  }

  // mirrors showUpdateBtn (ctrl:214-242).
  edit(row: VitalQualifierRow): void {
    this.saveMode.set(false);
    this.api.getById(row.vitalQualifireId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error('Error!!');
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          vitalQualifireId: d.vitalQualifireId,
          vitalCategoryId: String(d.vitalCategoryId),
          vitalQualifireCode: d.vitalQualifireCode,
          vitalQualifireDesc: d.vitalQualifireDesc,
        });
      },
    });
  }

  // mirrors updateStatus (ctrl:297-330) -- see class doc comment for the
  // whole-vs-minimal-payload note.
  toggleStatus(row: VitalQualifierRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) =>
      rows.map((r) => (r.vitalQualifireId === row.vitalQualifireId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        vitalQualifireId: row.vitalQualifireId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
          } else {
            this.notify.error(res.message ?? 'Something went wrong.');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:156-157/248-249/299-300. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
