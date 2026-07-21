import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CreateTabularDataTemplatesService } from './create-tabular-data-templates.service';
import { CtdtComponent, CtdtRowCol, CtdtSavedEntry } from './create-tabular-data-templates.models';

/**
 * Rebuild of the legacy `createTabularDataTemplates` state -- see
 * createTabularDataTemplatesController.js (388 lines). No data.moduleId
 * (no resolve block), real cookieObject guard only.
 *
 * SEVERITY 1 finding, logged (docs/ANGULAR_PENDING_FINDINGS.md), NOT
 * fixed, faithfully reproduced as this screen's parity target:
 * `saveTabularDataOfTemplate()` calls `$scope.getBloodGroupList()` in
 * BOTH its success and error branches
 * (createTabularDataTemplatesController.js:274,283) -- a function never
 * defined anywhere in this controller. The comment directly above it
 * ("save blood group matching list") confirms this is copy-paste
 * residue from an unrelated Blood Group Matching screen. Clicking Save
 * throws `TypeError: $scope.getBloodGroupList is not a function`
 * unconditionally, regardless of whether the save itself succeeded.
 * Reproduced via a direct throw in `saveTabularDataOfTemplate()` below,
 * same "can't express literally, replicate the observable crash"
 * treatment as the sibling screen's `itemId` bug in this same batch.
 */
@Component({
  selector: 'app-create-tabular-data-templates',
  imports: [FormsModule],
  templateUrl: './create-tabular-data-templates.html',
  styleUrl: './create-tabular-data-templates.scss',
})
export class CreateTabularDataTemplates {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CreateTabularDataTemplatesService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  componentList = signal<CtdtComponent[]>([]);
  tabularTemplateId: number | undefined;
  numberOfRow = 0;
  numberOfCoulmn = 0;
  tableName = '';
  forTableName = signal(false);

  rowList = signal<CtdtRowCol[]>([]);
  columnList = signal<CtdtRowCol[]>([]);
  columnHeaderList = signal<CtdtRowCol[]>([]);
  savedList = signal<CtdtSavedEntry[]>([]);

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.getComponentList();
  }

  private getComponentList(): void {
    this.loader.start();
    this.api.getActiveTabularTemplateMaster(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.componentList.set(res.listObject ?? []);
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  addRow(): void {
    const rows: CtdtRowCol[] = [];
    for (let i = 0; i < this.numberOfRow; i++) {
      rows.push({ rowId: i, coulmnId: i, rowName: '', columnName: '', inputbox: false, tabularTemplateId: this.tabularTemplateId });
    }
    this.rowList.set(rows);
  }

  addColumn(): void {
    const cols: CtdtRowCol[] = [];
    for (let i = 0; i < this.numberOfCoulmn; i++) {
      cols.push({ coulmnId: i, rowId: i, rowName: '', columnName: '', inputbox: false, tabularTemplateId: this.tabularTemplateId });
    }
    this.columnList.set(cols);
    this.columnHeaderList.set(cols.map((c) => ({ ...c })));

    const saved: CtdtSavedEntry[] = [];
    for (const row of this.rowList()) {
      for (const col of this.columnList()) {
        saved.push({
          inputValue: '',
          coulmnId: col.coulmnId,
          tableName: this.tableName,
          columnName: col.columnName,
          rowId: row.rowId,
          rowName: row.rowName,
          tabularTemplateId: this.tabularTemplateId,
          numberOfRow: this.numberOfRow,
          numberOfCoulmn: this.numberOfCoulmn,
          organizationId: this.organizationId,
          unitId: this.unitId,
          createdBy: this.userId,
          updatedBy: this.userId,
          status: 'A',
          createdDate: formatDmyHms(new Date()),
          updatedDate: formatDmyHms(new Date()),
        });
      }
    }
    this.savedList.set(saved);
  }

  openInputBoxForTableName(): void {
    this.forTableName.set(true);
    this.columnHeaderList().forEach((c) => (c.inputbox = false));
    this.rowList().forEach((r) => (r.inputbox = false));
  }

  closeTableName(): void {
    this.forTableName.set(false);
  }

  closeRowAndColumn(): void {
    this.columnHeaderList().forEach((c) => (c.inputbox = false));
    this.rowList().forEach((r) => (r.inputbox = false));
  }

  openInputBox(info: CtdtRowCol): void {
    this.forTableName.set(false);
    this.columnHeaderList().forEach((c) => (c.inputbox = c.coulmnId === info.coulmnId));
  }

  openInputBoxForRow(info: CtdtRowCol): void {
    this.forTableName.set(false);
    this.rowList().forEach((r) => (r.inputbox = r.rowId === info.rowId));
  }

  pushRowName(id: number, text: string): void {
    this.rowList().forEach((r) => {
      if (r.rowId === id) r.rowName = text;
    });
    this.savedList().forEach((s) => {
      if (s.rowId === id) s.rowName = text;
    });
  }

  pushColumnName(id: number, text: string): void {
    this.columnList().forEach((c) => {
      if (c.coulmnId === id) c.columnName = text;
    });
    this.savedList().forEach((s) => {
      if (s.coulmnId === id) s.columnName = text;
    });
  }

  getCurrentStatus(inputValue: string, coulmnId: number, columnName: string, rowId: number, rowName: string): void {
    if (inputValue === undefined) return;
    this.savedList().forEach((s) => {
      if (s.coulmnId === coulmnId && s.rowId === rowId) {
        s.inputValue = inputValue;
        s.tableName = this.tableName;
        s.columnName = columnName;
        s.rowName = rowName;
      }
    });
  }

  loadTemplateList(id: number): void {
    if (!id) return;
    this.loader.start();
    this.rowList.set([]);
    this.columnList.set([]);
    this.columnHeaderList.set([]);
    this.savedList.set([]);

    this.api.getTemplateDataMapperById(id, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success') {
          if (res.status === 'error') this.notify.error(res.message ?? '');
          return;
        }
        const list = res.listObject ?? [];
        if (list.length === 0) return;

        list.forEach((entry) => {
          entry.organizationId = this.organizationId;
          entry.unitId = this.unitId;
          entry.createdBy = this.userId;
          entry.updatedBy = this.userId;
          entry.status = 'A';
          entry.createdDate = formatDmyHms(new Date());
          entry.updatedDate = formatDmyHms(new Date());
        });
        this.savedList.set(list);
        this.numberOfRow = list[0].numberOfRow ?? 0;
        this.numberOfCoulmn = list[0].numberOfCoulmn ?? 0;
        this.tableName = list[0].tableName ?? '';

        const rows: CtdtRowCol[] = [];
        for (let i = 0; i < this.numberOfRow; i++) {
          rows.push({ rowId: i, coulmnId: i, rowName: '', columnName: '', inputbox: false, tabularTemplateId: this.tabularTemplateId });
        }
        const cols: CtdtRowCol[] = [];
        for (let i = 0; i < this.numberOfCoulmn; i++) {
          cols.push({ coulmnId: i, rowId: i, rowName: '', columnName: '', inputbox: false, tabularTemplateId: this.tabularTemplateId });
        }

        rows.forEach((r) => {
          const match = list.find((s) => s.rowId === r.rowId);
          if (match) r.rowName = match.rowName;
        });
        cols.forEach((c) => {
          const match = list.find((s) => s.coulmnId === c.coulmnId);
          if (match) c.columnName = match.columnName;
        });

        this.rowList.set(rows);
        this.columnList.set(cols);
        this.columnHeaderList.set(cols.map((c) => ({ ...c })));
      },
      error: () => this.loader.stop(),
    });
  }

  /** Faithful crash replication -- see class doc comment. */
  saveTabularDataOfTemplate(): void {
    this.savedList().forEach((s) => (s.tableName = this.tableName));

    this.loader.start();
    this.api
      .saveTabularTemplateDataMapper({
        toSaveList: this.savedList(),
        tabularTemplateId: this.tabularTemplateId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.tabularTemplateId = undefined;
            this.notify.success(res.message ?? '');
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? '');
          }
          throw new TypeError('$scope.getBloodGroupList is not a function');
        },
        error: () => {
          this.loader.stop();
          throw new TypeError('$scope.getBloodGroupList is not a function');
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
