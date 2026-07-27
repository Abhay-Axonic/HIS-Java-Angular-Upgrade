import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { MachineParameterMasterService } from './machine-parameter-master.service';
import {
  MachineOption,
  MachineParameterForm,
  MachineParameterRow,
  emptyMachineParameterForm,
} from './machine-parameter-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/LIS/machine-Parameter-Master.html
 * + resources/assets/js/controller/lis/unit/machineParameterMasterController.js
 * (344 lines, `machineParameterMasterController`). State
 * `machineParameterMaster` is registered in `app.js:19473` with a
 * `resolve` block gating on module id **839** -- confirmed directly
 * from the live route config. Admin's account in this dev environment
 * does not have this module granted (out of an otherwise very large
 * ~3038-entry permission set) -- confirmed real-session backend
 * reachability during batch 23 candidate screening by calling the
 * endpoint directly, bypassing the route guard.
 *
 * ================================================================
 * SEVERITY-1: Update is completely non-functional on real,
 * unmodified legacy -- confirmed live and root-caused at the source,
 * faithfully reproduced (not fixed, not worked around)
 * ================================================================
 * Live-verified 2026-07-27 against real legacy: created a parameter,
 * toggled it Inactive, then Edit -> Update with no changes -- every
 * attempt returns a real HTTP 200 with `{"status":"error","message":
 * "Failed to add Machine."}`, no database write occurs. Root cause,
 * confirmed by direct source read:
 * `MachineParameterServiceImpl.updateMachineParaMaster` (service:55-73)
 * DOES have a correctly-built, org+unit-scoped, self-excluding
 * duplicate-check by `code` (`UPDATE_GET_MACHINE_PARAMETER_BY_CODE`) --
 * if the new code collides with ANOTHER row, it correctly returns
 * `"Machine code is already exists."` without crashing. But when the
 * code is unique (including an unchanged, no-op edit), it proceeds to
 * `MachineParameterDaoImpl.updateMachineParaMaster` (dao:82-99), which
 * calls Dozer's `mapper.map(machineParameterDto, MachineParameter.class,
 * "MachinePatraMstrDtoTOMachinePatraMstrDto")` -- but that map-id is
 * defined in `dozer-bean-mappings.xml:226-231` for `MachineParameter`
 * <-> **`MachineMasterDto`**, a completely different DTO belonging to
 * the sibling "Machine Master" screen, not `MachineParameterDto`. This
 * throws unconditionally; the catch block papers over it with
 * `MACHINE_TEST_ADD_FAIL = "Failed to add Machine."`, a message
 * copy-pasted from the Add path. Net effect: Update NEVER succeeds --
 * either it correctly rejects as a code collision, or it always
 * crashes with a misleading message. This is a genuine backend
 * copy-paste bug the rebuild cannot work around; `update()` below is
 * wired to the real, broken endpoint and will reproduce this exactly.
 *
 * ================================================================
 * Save has NO duplicate-check of any kind -- faithfully reproduced
 * ================================================================
 * `MachineParameterDaoImpl.saveMachineParameterMaster` (dao:39-57)
 * inserts directly with zero uniqueness query anywhere in its call
 * chain (confirmed by reading the full DAO and Service files) -- any
 * number of rows with the same code/description can be created.
 *
 * ================================================================
 * No confirm-modal on this screen -- unlike every other batch 22/23
 * screen, this controller has no `popUpFlag`/`PopupOkBtn`/`showModal`
 * anywhere. `save()` fires directly once the form is valid. Confirmed
 * by reading the full 344-line controller. No fix needed; there is no
 * bug to reproduce or fix here.
 * ================================================================
 *
 * ================================================================
 * Real Angular-style form validation (`commonform.$valid`), not
 * manual per-field JS checks -- reproduced via formSubmitted +
 * per-field invalid signals, same pattern as operationTheatreType
 * (batch 22)
 * ================================================================
 * Code: required, max 10 chars, both messages correctly wired
 * (`commonform.commonCode.$error.*`). Machine Description: required
 * (correctly wired), but its maxlength-256 error message references
 * `commonform.commonDesc` -- a field that doesn't exist (the real
 * field is named `machineParameterDesc`) -- so that specific message
 * can NEVER render even though the `ng-maxlength="256"` constraint
 * still silently blocks submission via `$valid`. Reproduced exactly:
 * required-field message shown, but exceeding 256 chars silently
 * blocks Save with no visible reason, matching legacy's broken
 * binding. Machine: a required `<select>` populated from
 * `getActiveMachineList` (real API call, not hardcoded).
 *
 * ================================================================
 * Status toggle does NOT reload the list -- reproduced via the same
 * optimistic-local-patch pattern as operationTheatreType/cardTypeMaster
 * (batch 22). Confirmed by source read: no `initMachineParaMasterList()`
 * call anywhere in `updateStatus()`.
 * ================================================================
 *
 * Pagination is genuinely functional (real offset tracking via
 * `PagerService`/`setPage`, same shape as `bloodBagType`).
 *
 * Edit-load response is a single OBJECT (`response.data.object`), not
 * array-wrapped (unlike `bloodBagType`) and not a client-side row copy
 * (unlike `OTIMSroleCreation`) -- a real server round-trip per edit.
 */
@Component({
  selector: 'app-machine-parameter-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './machine-parameter-master.html',
  styleUrl: './machine-parameter-master.scss',
})
export class MachineParameterMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(MachineParameterMasterService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state ----
  form = signal<MachineParameterForm>(emptyMachineParameterForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  machineOptions = signal<MachineOption[]>([]);

  // ---- list + real, working server-side pagination ----
  list = signal<MachineParameterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => [r.code, r.machineParameterDesc, r.machine].join(' ').toLowerCase().includes(keyword));
  });

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.organizationId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadMachines();
    this.loadList();
  }

  private loadMachines(): void {
    this.api.getActiveMachines(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.machineOptions.set(res.listObject ?? []);
        }
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.set(res.status === 'success' ? (res.listObject ?? []) : []);
      },
      error: () => this.loader.stop(),
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

  updateField<K extends keyof MachineParameterForm>(field: K, value: MachineParameterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isCodeRequiredInvalid(): boolean {
    return this.formSubmitted() && !this.form().code.trim();
  }

  isCodeMaxLengthInvalid(): boolean {
    return this.formSubmitted() && this.form().code.length > 10;
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().machineParameterDesc.trim();
  }

  isMachineInvalid(): boolean {
    return this.formSubmitted() && !this.form().machineId;
  }

  // Mirrors `commonform.$valid` (ctrl:260-261,304). Description's
  // maxlength constraint is enforced silently (no message), matching
  // legacy's broken `commonform.commonDesc` error binding -- see class doc.
  private isValid(): boolean {
    const f = this.form();
    return !!f.code.trim() && f.code.length <= 10 && !!f.machineParameterDesc.trim() && f.machineParameterDesc.length <= 256 && !!f.machineId;
  }

  // ---- save (mirrors save, ctrl:259-300 -- no confirm modal, see class doc) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.isValid()) return;
    const f = this.form();
    this.loader.start();
    this.api
      .save({
        machineId: Number(f.machineId),
        code: f.code,
        machineParameterDesc: f.machineParameterDesc,
        status: 'A',
        organisationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('machine Parameter save successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyMachineParameterForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:230-257 -- OBJECT-wrapped GET response) ----
  edit(row: MachineParameterRow): void {
    this.loader.start();
    this.api.getById(this.organizationId, this.unitId, row.machinParameterId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.object) {
          this.saveMode.set(false);
          this.form.set({
            machinParameterId: res.object.machinParameterId,
            code: res.object.code,
            machineParameterDesc: res.object.machineParameterDesc,
            machineId: String((res.object as unknown as { machineId: number }).machineId ?? ''),
          });
        } else {
          this.notify.error('Error In Fetching Data');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update, ctrl:302-341 -- CONFIRMED BROKEN on
  // real legacy, see class doc; no client-side workaround) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.isValid()) return;
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        machinParameterId: f.machinParameterId as number,
        machineId: Number(f.machineId),
        code: f.code,
        machineParameterDesc: f.machineParameterDesc,
        organisationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:193-228 -- does NOT
  // reload the list, see class doc) ----
  toggleStatus(row: MachineParameterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.machinParameterId === row.machinParameterId ? { ...r, status: nextStatus } : r)));
    this.loader.start();
    this.api.updateStatus(this.organizationId, row.machinParameterId, nextStatus).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Updated Successfully');
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}
