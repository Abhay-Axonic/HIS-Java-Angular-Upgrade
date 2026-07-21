import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { DetailScheduleWorklistHandoffService } from '../../core/detail-schedule-worklist-handoff.service';
import * as pg from '../../core/client-pagination.util';
import { ScheduleWorklistService } from './schedule-worklist.service';
import { ScheduleWorklistUser, ScheduleWorklistStore, AssetScheduleRow, ScheduleWorklistCriteria } from './schedule-worklist.models';

/**
 * Rebuild of the legacy `scheduleWorklist` state -- see
 * preventiveScheduleWorklistController.js (205 lines). Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entries 17 (RESOLVED, re-verified),
 * 21, 23 (addendum), 37 (recurs here via shared StoreSelectionService).
 *
 * No data.moduleId on this route -- no resolve block in legacy's state,
 * but the controller has 2 real guards in sequence: cookieObject (session)
 * and cookieObjectAppConfig. moduleAccessGuard covers the first; the
 * second is checked here in the constructor, matching legacy's own
 * controller-level (not resolve-block) placement. Legacy redirects a
 * missing cookieObjectAppConfig to `applicationConfiguration` -- not one
 * of this batch's 14 screens and not yet rebuilt (and per entry 34, that
 * screen has its own unrelated severity-1 crash bug). Redirects to
 * `/accessDenied?reason=appConfigMissing` instead, reusing the existing
 * redirect convention rather than inventing a link to an unbuilt,
 * separately-broken screen -- same reasoning as the 401-handling
 * deviation already established for pesapalProcessPage.
 *
 * Parity targets, deliberately NOT fixed:
 *  - Entry 21's OR-instead-of-AND search filter, including the
 *    lexicographic (not chronological) date-string comparison.
 *  - Entry 23's THREE-WAY processStatus code mismatch (search dropdown
 *    vs. table display vs. PMSchedule) -- replicated exactly, including
 *    the blank-cell-for-status-7 gap.
 *  - getEmployeeListBySpecialityAndSubSpeciality only fires when
 *    assetMaintenanceSpecialityId is non-null, matching legacy's
 *    `if($scope.assetMaintenanceSpecialityId!=null)` guard exactly.
 */
@Component({
  selector: 'app-schedule-worklist',
  imports: [FormsModule, DatePipe],
  templateUrl: './schedule-worklist.html',
  styleUrl: './schedule-worklist.scss',
})
export class ScheduleWorklist {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly storeSelection = inject(StoreSelectionService);
  private readonly detailHandoff = inject(DetailScheduleWorklistHandoffService);
  private readonly api = inject(ScheduleWorklistService);

  private readonly unitId: number;
  private readonly organizationId: number;
  private readonly assetMaintenanceSpecialityId: number | undefined;

  usersList = signal<ScheduleWorklistUser[]>([]);
  storeList = signal<ScheduleWorklistStore[]>([]);
  assetDetailsByScheduleCriteria = signal<AssetScheduleRow[]>([]);
  pagination = signal(pg.createPaginationState<AssetScheduleRow>('10'));

  criteria: ScheduleWorklistCriteria;

  constructor() {
    if (this.session.appConfig == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'appConfigMissing' } });
      // matches legacy's early `return;` -- nothing below should run.
      this.unitId = 0;
      this.organizationId = 0;
      this.criteria = {
        fromDate: new Date(),
        toDate: new Date(),
        storeId: 0,
        pmsType: '',
        processStatus: '',
        assignTo: 0,
        unitId: 0,
        organizationId: 0,
      };
      return;
    }

    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
      this.storeSelection.show();
    }

    this.assetMaintenanceSpecialityId = this.session.appConfig.assetMaintenanceSpecialityId;
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;

    this.criteria = {
      fromDate: new Date(),
      toDate: new Date(),
      storeId: 0,
      pmsType: '',
      processStatus: '',
      assignTo: 0,
      unitId: this.unitId,
      organizationId: this.organizationId,
    };

    this.loader.start();
    if (this.assetMaintenanceSpecialityId != null) {
      this.api
        .getEmployeeListBySpecialityAndSubSpeciality(this.organizationId, this.unitId, this.assetMaintenanceSpecialityId)
        .subscribe({
          next: (res) => {
            this.usersList.set(res.status === 'success' ? (res.listObject ?? []) : []);
          },
        });
    }

    const userId = this.session.userId ?? 0;
    this.api.getDetailedStoreList(userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.storeList.set(res.listObject ?? []);
      },
    });

    this.getAssetDetailsByCriteria();
  }

  getAssetDetailsByCriteria(): void {
    this.loader.start();
    this.api.getAssetDetailsByCriteria(this.criteria).subscribe({
      next: (res) => {
        this.loader.stop();
        const list = res.listObject;
        if (list != null) {
          const fDate = formatDate(this.criteria.fromDate);
          const tDate = formatDate(this.criteria.toDate);
          const store = this.criteria.storeId;
          const pmstype = this.criteria.pmsType;
          const proStatus = this.criteria.processStatus;
          const empId = this.criteria.assignTo;

          const matched: AssetScheduleRow[] = [];
          for (const row of list) {
            const newDate = formatDate(row.nextDate);
            // Entry 21, faithfully replicated: OR, not AND. Also
            // lexicographic (not chronological) date-string comparison.
            if (
              fDate >= newDate ||
              tDate <= newDate ||
              store === row.storeId ||
              pmstype === row.pmsType ||
              String(proStatus) === String(row.processStatus) ||
              empId == row.assignTo
            ) {
              matched.push(row);
            }
          }

          this.assetDetailsByScheduleCriteria.set(matched);
          if (matched.length > 0) {
            const p = this.pagination();
            pg.searchData(matched, p);
            this.pagination.set({ ...p });
          }
        } else {
          // legacy: growl.error('No List to Display...!!')
        }
      },
      error: () => this.loader.stop(),
    });
  }

  onSearchChange(term: string): void {
    const p = this.pagination();
    p.search = term;
    pg.searchData(this.assetDetailsByScheduleCriteria(), p);
    this.pagination.set({ ...p });
  }

  onPageSizeChange(size: string): void {
    const p = this.pagination();
    p.pageSize = size;
    pg.setTotalPages(p);
    this.pagination.set({ ...p });
  }

  goToPage(page: number): void {
    const p = this.pagination();
    pg.setPage(page, p);
    this.pagination.set({ ...p });
  }
  goToFirstPage(): void {
    const p = this.pagination();
    pg.setFirstPage(p);
    this.pagination.set({ ...p });
  }
  goToPreviousPage(): void {
    const p = this.pagination();
    pg.setPreviousPage(p);
    this.pagination.set({ ...p });
  }
  goToNextPage(): void {
    const p = this.pagination();
    pg.setNextPage(p);
    this.pagination.set({ ...p });
  }
  goToLastPage(): void {
    const p = this.pagination();
    pg.setLastPage(p);
    this.pagination.set({ ...p });
  }
  pagesList(): number[] {
    return pg.getPagesList(this.pagination());
  }

  redirectToDetailScheduleWorklist(asset: AssetScheduleRow): void {
    if (asset != null) {
      this.detailHandoff.setObject({ ...asset });
      this.router.navigate(['/detailScheduleWorklist']);
    }
  }

  getAssignNameById(id: number | undefined): string {
    if (id == null || id === 0) return '';
    const user = this.usersList().find((u) => u.userId === id);
    return user?.name ?? '';
  }
}

function formatDate(d: Date | string | undefined): string {
  if (!d) return '';
  const date = new Date(d);
  const dd = String(date.getDate()).padStart(2, '0');
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const yyyy = date.getFullYear();
  return `${dd}-${mm}-${yyyy}`;
}
