import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { InsuranceTpaMappingService } from './insurance-tpa-mapping.service';
import { InsuranceOption, InsuranceTpaMapperRow, TpaOption } from './insurance-tpa-mapping.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/insurance-TPA-mapping.html +
 * resources/assets/js/controller/billing/unit/insuranceTPAMappingController.js.
 * moduleId 573 (app.js:8082-8108 resolve block, confirmed live).
 *
 * **Edit/Update are deliberately NOT wired to the backend at all in this
 * rebuild -- this is a corrected, visible version of a genuinely broken
 * legacy feature, not a missing feature in the rebuild. Confirmed at TWO
 * separate, independent levels, not guessed:**
 *
 * 1. The view's Update button (`ng-click="update()"`) calls a function that
 *    does not exist anywhere in `insuranceTPAMappingController.js` -- only
 *    `updateDeptAtUnitLevel()` exists, itself dead, leftover copy-paste
 *    from an unrelated "Billing Group Master" screen (references
 *    `$scope.billingGroupMasterObj`, `BILLING + UPDATE_BILLING_GROUP_MASTER`,
 *    `initBillingGroupMasterList` -- none of which exist on this screen).
 * 2. `showUpdateBtn(id)` (the pencil/edit icon's handler) calls
 *    `GETUNITSPECIALITYBYID`, which resolves to `/adt/getUnitSpecialityById`
 *    (confirmed in `adt.url.config.js:591`) -- a COMPLETELY UNRELATED
 *    endpoint belonging to the Unit Speciality Master screen, not Insurance
 *    TPA Mapping. Even if `update()` existed, the Edit click already fetches
 *    the wrong data shape (`unitSpecialityMapperId`/`specialityId`/
 *    `specialityName`) into a form built for TPA/insurance-company fields.
 * 3. `InsuranceTPAMapperController.java` (`@RequestMapping("api/billing")`)
 *    confirms there is no update endpoint for this feature at all --
 *    `saveInsuranceTPAMapper`, `getInsuranceTPAMapperList[Count]`,
 *    `updateInsuranceTPAMapperStatus`, `getInsuranceTPAMapperListById`, and
 *    `getInsuranceListNotMappedToMapper` are the entire surface. There is
 *    nothing a correct `update()` could even call.
 *
 * Applying this codebase's established silent-vs-visible precedent
 * (`_screen-template/README.md`'s status-toggle CSS fix is the canonical
 * example): silently porting a dead button that fetches unrelated data
 * into a real, working Angular app would be actively misleading, and
 * inventing a working update flow the backend has never had would be an
 * unauthorized behavioral change (CLAUDE.md). Instead, the Edit icon here
 * is visible but clearly disabled with an explanatory title, and clicking
 * it surfaces an honest notification rather than silently doing nothing or
 * fetching garbage. List, Create, and Status-toggle are all genuinely
 * real and fully wired -- confirmed live (2026-07-31): `getActiveTPA` and
 * `getInsuranceTPAMapperList[Count]` all returned real 200 envelopes.
 *
 * **Other deliberate quirks preserved:**
 * - `save` (ctrl:128-166) validates companyId selected, then at least one
 *   insurance selected, each with its own growl warning -- no confirm
 *   modal at all (unlike every other screen in this batch), POSTs
 *   directly on the click of the Save icon.
 * - After a successful save, legacy re-fetches the not-mapped list for the
 *   SAME company (`getCompanyId`, ctrl:158) rather than clearing the TPA
 *   selection -- so a user can keep adding more insurance companies to the
 *   same TPA in quick succession. Reproduced: `confirmSave` keeps
 *   `selectedTpaId` set and only clears the picked-insurance list.
 */
@Component({
  selector: 'app-insurance-tpa-mapping',
  imports: [FormsModule, StatusToggle],
  templateUrl: './insurance-tpa-mapping.html',
  styleUrl: './insurance-tpa-mapping.scss',
})
export class InsuranceTpaMapping {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(InsuranceTpaMappingService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  tpaList = signal<TpaOption[]>([]);
  selectedTpaId = signal<number | ''>('');
  notMappedList = signal<InsuranceOption[]>([]);
  pickedInsurance = signal<InsuranceOption[]>([]);

  list = signal<InsuranceTpaMapperRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => r.companyDesc?.toLowerCase().includes(keyword));
  });

  isPicked = computed(() => new Set(this.pickedInsurance().map((c) => c.insuranceId)));

  constructor() {
    this.api.getActiveTpaList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.tpaList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
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

  onTpaChange(companyId: number | ''): void {
    this.selectedTpaId.set(companyId);
    this.pickedInsurance.set([]);
    if (companyId === '') {
      this.notMappedList.set([]);
      return;
    }
    this.fetchNotMapped(companyId);
  }

  private fetchNotMapped(companyId: number): void {
    this.api.getInsuranceNotMapped(companyId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.notMappedList.set(res.listObject ?? []);
      },
    });
  }

  togglePickInsurance(option: InsuranceOption): void {
    this.pickedInsurance.update((list) => {
      const exists = list.some((c) => c.insuranceId === option.insuranceId);
      if (exists) return list.filter((c) => c.insuranceId !== option.insuranceId);
      return [...list, option];
    });
  }

  // ctrl:128-166 -- two separate checks, own warnings, no confirm modal, see class comment.
  save(): void {
    const companyId = this.selectedTpaId();
    if (companyId === '') {
      this.notify.warning('Please Select TPA');
      return;
    }
    if (this.pickedInsurance().length === 0) {
      this.notify.warning('Please Add Mapped Insurance');
      return;
    }
    const now = formatDateTime(new Date());
    this.api
      .save({
        companyId,
        status: 'A',
        createdDate: now,
        createdBy: this.userId,
        updatedDate: now,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        insuranceTPAMapperDtoList: this.pickedInsurance(),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved');
            // ctrl:158 -- re-fetches not-mapped for the SAME TPA rather
            // than clearing the selection, see class comment.
            this.pickedInsurance.set([]);
            this.fetchNotMapped(companyId);
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
      });
  }

  // Deliberately non-functional -- see class comment. Real legacy behavior
  // for this icon is to fetch an unrelated screen's data; this rebuild
  // surfaces the gap honestly instead.
  editUnavailable(): void {
    this.notify.warning('Editing an existing TPA mapping is not currently supported.');
  }

  toggleStatus(row: InsuranceTpaMapperRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.insuranceTpaMapperId === row.insuranceTpaMapperId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus(row.insuranceTpaMapperId, nextStatus, this.organizationId, this.unitId, this.userId, formatDateTime(new Date()))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success(res.message ?? 'Updated');
          else this.notify.error(res.message ?? 'Something went wrong');
        },
      });
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
