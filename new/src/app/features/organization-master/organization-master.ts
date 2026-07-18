import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpEventType } from '@angular/common/http';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { UploadProgressService } from '../../core/upload-progress.service';
import { OrganizationMasterService } from './organization-master.service';
import { ModuleTreeNodeComponent } from './module-tree-node';
import {
  GeoOption,
  ModuleNode,
  OrganizationRecord,
  TimezoneOption,
  emptyOrganization,
} from './organization-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/organizationMaster.html
 * + resources/assets/js/controller/master/organizationController.js.
 * Audited and golden-traced in docs/PHASE2_PILOT_AUDIT.md section 4 + 8.
 *
 * The legacy controller's missing-`$state`-injection bug (login-redirect
 * guard threw instead of redirecting) was already fixed and approved
 * separately (branch fix/organizationmaster-state-injection) -- this
 * rebuild naturally uses the correct, working guard behavior, not a
 * reproduction of the bug.
 *
 * The commented-out licence-details sub-table in the legacy view is NOT
 * rebuilt here -- it's unreachable UI in the current app, so it isn't
 * part of the screen's observable behavior. The Module Access modal's
 * BI/OTIMS tabs are likewise not rebuilt (only their HIS tab is reachable
 * in the legacy view -- its other two tab headers are commented out).
 */
@Component({
  selector: 'app-organization-master',
  imports: [FormsModule, ModuleTreeNodeComponent],
  templateUrl: './organization-master.html',
  styleUrl: './organization-master.scss',
})
export class OrganizationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(OrganizationMasterService);
  readonly uploadProgress = inject(UploadProgressService);

  record = signal<OrganizationRecord>(emptyOrganization());
  saveMode = signal(true);
  formSubmitted = signal(false);

  countryList = signal<GeoOption[]>([]);
  stateList = signal<GeoOption[]>([]);
  districtList = signal<GeoOption[]>([]);
  cityList = signal<GeoOption[]>([]);
  areaList = signal<GeoOption[]>([]);
  timezoneList = signal<TimezoneOption[]>([]);

  selectedLogo = signal<File | null>(null);

  list = signal<OrganizationRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  showModuleModal = signal(false);
  moduleTree = signal<ModuleNode[]>([]);

  constructor() {
    const organizationId = this.session.organizationId ?? 0;
    const unitId = this.session.unitId ?? 0;

    // Fired unconditionally on load, matching the legacy controller --
    // see the service's doc comments for why these two in particular are
    // "call but don't render."
    this.api.getLicenceTypeList(organizationId).subscribe();
    this.api.getModuleTree().subscribe({
      next: (res) => {
        if (res.status === 'success') this.moduleTree.set(this.buildTree(res.listObject ?? []));
      },
    });
    this.api.getPermissions(organizationId, unitId).subscribe();

    this.api.getTimezones().subscribe({
      next: (res) => {
        if (res.status === 'success') this.timezoneList.set(res.listObject ?? []);
      },
    });
    this.api.getCountryList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private buildTree(flat: { moduleId: number; moduleName: string; parentId: number; actionLink?: string }[]): ModuleNode[] {
    const byId = new Map<number, ModuleNode>();
    for (const m of flat) {
      byId.set(m.moduleId, { ...m, children: [] });
    }
    const roots: ModuleNode[] = [];
    for (const node of byId.values()) {
      if (node.parentId === 0) {
        roots.push(node);
      } else {
        byId.get(node.parentId)?.children.push(node);
      }
    }
    return roots;
  }

  // ---- geo cascade ----
  onCountryChange(countryId: number): void {
    this.updateField('countryId', countryId);
    this.stateList.set([]);
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
    if (!countryId) return;
    this.api.getStatesByCountry(countryId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateList.set(res.listObject ?? []);
      },
    });
  }

  onStateChange(stateId: number): void {
    this.updateField('stateId', stateId);
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
    if (!stateId) return;
    this.api.getDistrictsByState(stateId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.districtList.set(res.listObject ?? []);
      },
    });
  }

  onDistrictChange(districtId: number): void {
    this.updateField('districtId', districtId);
    this.cityList.set([]);
    this.areaList.set([]);
    if (!districtId) return;
    this.api.getCitiesByDistrict(districtId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cityList.set(res.listObject ?? []);
      },
    });
  }

  onCityChange(cityId: number): void {
    this.updateField('cityId', cityId);
    this.areaList.set([]);
    if (!cityId) return;
    this.api.getAreasByCity(cityId, this.session.organizationId ?? 0).subscribe({
      next: (res) => {
        if (res.status === 'success') this.areaList.set(res.listObject ?? []);
      },
    });
  }

  onAreaChange(areaId: number): void {
    this.updateField('areaId', areaId);
    const area = this.areaList().find((a) => a.areaId === areaId);
    if (area?.postCode) this.updateField('postCode', area.postCode);
  }

  // ---- logo upload -- reports progress through the shared
  // UploadProgressService, the rebuild-side equivalent of the legacy
  // `fileProgress` broadcast (see the service header comment). ----
  onLogoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.selectedLogo.set(input.files?.[0] ?? null);
  }

  private uploadLogoIfSelected(organizationId: number, onDone: () => void): void {
    const file = this.selectedLogo();
    if (!file) {
      onDone();
      return;
    }
    this.uploadProgress.reset();
    this.api.uploadLogo(organizationId, file).subscribe({
      next: (event) => {
        if (event.type === HttpEventType.UploadProgress && event.total) {
          this.uploadProgress.report(event.loaded, event.total);
        } else if (event.type === HttpEventType.Response) {
          this.uploadProgress.reset();
          onDone();
        }
      },
    });
  }

  // ---- list ----
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getOrganizationList(offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getOrganizationCount().subscribe({
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

  updateField<K extends keyof OrganizationRecord>(field: K, value: OrganizationRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- save / update ----
  save(): void {
    this.formSubmitted.set(true);
    const r = this.record();
    const requiredOk =
      r.organizationCode.trim() &&
      r.organizationName.trim() &&
      r.registrationNumber.trim() &&
      r.organizationAddress.trim() &&
      r.countryId !== '' &&
      r.stateId !== '' &&
      r.districtId !== '' &&
      r.cityId !== '' &&
      r.areaId !== '' &&
      r.postCode &&
      r.organizationContact.trim() &&
      r.organizationEmailId.trim() &&
      r.unitTimeZone;
    if (!requiredOk) return;

    // Legacy has an asymmetry here worth keeping visible rather than
    // silently harmonizing: updateOrganization() checks mobile-number
    // length == 10 explicitly; saveOrganization() does not.
    if (!this.saveMode() && r.organizationContact.length !== 10) {
      this.notify.error('Please Enter Valid Organization Contact');
      return;
    }

    const request = this.saveMode() ? this.api.save(r) : this.api.update(r);
    request.subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error(res.message ?? 'Save failed');
          return;
        }
        const organizationId = res.object ?? (r.organizationId as number);
        this.uploadLogoIfSelected(organizationId, () => {
          this.notify.success(res.message ?? 'Saved successfully');
          this.resetForm();
          this.loadList();
        });
      },
      error: () => this.notify.error('Save failed'),
    });
  }

  private resetForm(): void {
    this.record.set(emptyOrganization());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.selectedLogo.set(null);
  }

  edit(organizationId: number): void {
    this.api.getById(organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.object) return;
        this.record.set(res.object);
        this.saveMode.set(false);
        const rec = res.object;
        if (rec.countryId) this.onCountryChange(rec.countryId as number);
        if (rec.stateId) this.onStateChange(rec.stateId as number);
        if (rec.districtId) this.onDistrictChange(rec.districtId as number);
        if (rec.cityId) this.onCityChange(rec.cityId as number);
      },
    });
  }

  toggleStatus(row: OrganizationRecord): void {
    const next: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.updateStatus(row.organizationId as number, next).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          row.status = next;
          this.notify.success(res.message ?? 'Status updated');
        }
      },
    });
  }

  // ---- module access modal ----
  openModuleModal(): void {
    this.showModuleModal.set(true);
  }

  closeModuleModal(): void {
    this.showModuleModal.set(false);
  }

  saveModulePermissions(): void {
    const selected: { moduleId: number; moduleName: string; actionLink?: string; parentId: number; moduleType: number }[] = [];
    const collect = (nodes: ModuleNode[]) => {
      for (const n of nodes) {
        if (n.selected) {
          selected.push({ moduleId: n.moduleId, moduleName: n.moduleName, actionLink: n.actionLink, parentId: n.parentId, moduleType: 1 });
        }
        collect(n.children);
      }
    };
    collect(this.moduleTree());

    this.api
      .savePermissions({
        roleId: '',
        privilegeId: '',
        moduleType: 1,
        modules: selected,
        organisationId: this.session.organizationId ?? 0,
        unitId: this.session.unitId ?? 0,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Saved Successfully');
            this.closeModuleModal();
          }
        },
      });
  }
}
