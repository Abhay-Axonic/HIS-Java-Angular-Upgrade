import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { debounceTime, Subject, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { RegistrationConfigService } from '../../core/registration-config.service';
import { InsuranceMasterService } from './insurance-master.service';
import {
  AddressDetails,
  GeoOption,
  InsuranceMasterRecord,
  emptyInsuranceMaster,
} from './insurance-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const EMAIL_PATTERN = /^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
const FAX_PATTERN = /^\+?[0-9]{6,}$/;
const ALLOWED_DOC_EXTENSIONS = ['doc', 'pdf', 'docx', 'txt'];

/**
 * Parity rebuild of views/master/UnitMaster/billingMaster/insurance-master.html
 * + resources/assets/js/controller/master/unit/insuranceMasterController.js.
 * Audited and golden-traced in docs/PHASE2_PILOT_AUDIT.md section 2 + 8.
 *
 * Deliberately preserves the legacy screen's two-layer validation
 * (HTML required-field state below + a second, stricter manual check in
 * `save()`/`update()`) rather than collapsing it -- that redundancy is
 * logged as its own cleanup candidate in docs/ANGULAR_PENDING_FINDINGS.md
 * entry 1, owner decision was explicitly "preserve both for this pilot."
 *
 * The "Grade Details" modal present in the legacy controller
 * (`getGreadDetails`) is NOT rebuilt here: its trigger icon is commented
 * out in the legacy view, so it's unreachable UI today -- parity means
 * matching observable behavior, and a user can never reach that modal in
 * the current app either.
 */
@Component({
  selector: 'app-insurance-master',
  imports: [FormsModule],
  templateUrl: './insurance-master.html',
  styleUrl: './insurance-master.scss',
})
export class InsuranceMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(InsuranceMasterService);
  private readonly registrationConfig = inject(RegistrationConfigService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  record = signal<InsuranceMasterRecord>(emptyInsuranceMaster());
  saveMode = signal(true);
  formSubmitted = signal(false);

  countryList = signal<GeoOption[]>([]);
  stateList = signal<GeoOption[]>([]);
  districtList = signal<GeoOption[]>([]);
  cityList = signal<GeoOption[]>([]);
  areaList = signal<GeoOption[]>([]);

  selectedFile = signal<File | null>(null);
  fileError = signal<string | null>(null);

  ledgerSearchTerm = signal('');
  ledgerResults = signal<{ generalLedgerId: number; generalLedgerName: string }[]>([]);
  private ledgerSearch$ = new Subject<string>();

  list = signal<InsuranceMasterRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  searchKeyword = signal('');
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((row) =>
      [row.insuranceCode, row.insuranceDesc, row.email, row.contactPerson]
        .filter(Boolean)
        .some((f) => f!.toLowerCase().includes(keyword)),
    );
  });

  showAddressModal = signal(false);
  addressDetails = signal<AddressDetails>({});

  constructor() {
    this.loadCountryList();
    this.loadList();

    this.ledgerSearch$.pipe(debounceTime(250), switchMap((term) => this.api.getGeneralLedgerList(this.organizationId, term))).subscribe({
      next: (res) => this.ledgerResults.set(res.status === 'success' ? res.listObject ?? [] : []),
    });
  }

  // ---- geo cascade ----
  private loadCountryList(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
  }

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
    this.api.getAreasByCity(cityId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.areaList.set(res.listObject ?? []);
      },
    });
  }

  onAreaChange(areaId: number): void {
    this.updateField('areaId', areaId);
    const area = this.areaList().find((a) => a.areaId === areaId);
    if (area?.postCode) {
      this.updateField('zipCode', area.postCode);
    }
  }

  // ---- ledger typeahead ----
  onLedgerSearchChange(term: string): void {
    this.ledgerSearchTerm.set(term);
    this.record.update((r) => ({ ...r, generalLedgerId: undefined }));
    if (term.length >= 1) this.ledgerSearch$.next(term);
  }

  selectLedger(ledger: { generalLedgerId: number; generalLedgerName: string }): void {
    this.record.update((r) => ({ ...r, generalLedgerId: ledger.generalLedgerId }));
    this.ledgerSearchTerm.set(ledger.generalLedgerName);
    this.ledgerResults.set([]);
  }

  clearLedgerIfUnselected(): void {
    if (this.record().generalLedgerId == null) {
      this.ledgerSearchTerm.set('');
    }
  }

  // ---- file ----
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    this.selectedFile.set(file);
    this.fileError.set(null);
  }

  private validateFileExtension(): boolean {
    const file = this.selectedFile();
    if (!file) return true;
    const ext = file.name.split('.').pop()?.toLowerCase() ?? '';
    if (!ALLOWED_DOC_EXTENSIONS.includes(ext)) {
      this.notify.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file');
      return false;
    }
    return true;
  }

  // ---- list ----
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

  updateField<K extends keyof InsuranceMasterRecord>(field: K, value: InsuranceMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- save / update -- deliberately duplicates the legacy app's
  // two-layer validation (HTML-required state above + this manual pass),
  // see the class-level doc comment. ----
  private manualValidate(): string | null {
    const r = this.record();
    if (!r.insuranceCode.trim()) return 'Please Enter Insurance Code';
    if (!r.insuranceDesc.trim()) return 'Please Enter Insurance Description';
    if (r.countryId === '' || Number.isNaN(Number(r.countryId))) return 'Please Select Country';
    if (r.stateId === '' || Number.isNaN(Number(r.stateId))) return 'Please Select State';
    if (r.districtId === '' || Number.isNaN(Number(r.districtId))) return 'Please Select District';
    if (r.cityId === '' || Number.isNaN(Number(r.cityId))) return 'Please Select City';
    if (r.areaId === '' || Number.isNaN(Number(r.areaId))) return 'Please Select Area';
    if (!r.zipCode.trim()) return 'Please Enter zipcode';
    if (!r.email.trim()) return 'Please Enter Email';
    return null;
  }

  save(): void {
    this.formSubmitted.set(true);
    const warning = this.manualValidate();
    if (warning) {
      this.notify.warning(warning);
      return;
    }

    const r = this.record();
    if (!EMAIL_PATTERN.test(r.email)) {
      this.notify.error('Please enter a valid email address');
      return;
    }
    if (r.faxNo && !FAX_PATTERN.test(r.faxNo)) {
      this.notify.error('Invalid Fax Number');
      return;
    }
    const mobileDigit = this.registrationConfig.fieldConfig().mobileDigit;
    if (r.contactNo && r.contactNo.length !== mobileDigit) {
      this.notify.error('Please enter valid mobile number');
      return;
    }
    if (!this.validateFileExtension()) return;

    const payload: InsuranceMasterRecord = { ...r, generalLedgerName: this.ledgerSearchTerm() };

    // save() and update() return differently-shaped responses on the real
    // backend (save's insuranceId is nested under `object.insuranceId`,
    // update's is `object` directly) -- a genuine legacy API asymmetry,
    // not a typo to normalize away. Handled as two branches rather than
    // forcing one shared type.
    if (this.saveMode()) {
      this.api.save(payload).subscribe({
        next: (res) => this.handleSaveResponse(res.status, res.message, res.object?.insuranceId),
        error: () => this.notify.error('Save failed'),
      });
    } else {
      this.api.update(payload).subscribe({
        next: (res) => this.handleSaveResponse(res.status, res.message, res.object),
        error: () => this.notify.error('Save failed'),
      });
    }
  }

  private handleSaveResponse(status: 'success' | 'error', message: string | undefined, insuranceId: number | undefined): void {
    if (status !== 'success') {
      this.notify.error(message ?? 'Save failed');
      return;
    }
    const file = this.selectedFile();
    if (file && insuranceId != null) {
      this.api.uploadDocument(this.organizationId, this.unitId, insuranceId, file).subscribe({
        next: () => this.afterSave(message),
      });
    } else {
      this.afterSave(message);
    }
  }

  private afterSave(message?: string): void {
    this.notify.success(message ?? 'Saved successfully');
    this.resetForm();
    this.loadList();
  }

  private resetForm(): void {
    this.record.set(emptyInsuranceMaster());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.selectedFile.set(null);
    this.ledgerSearchTerm.set('');
  }

  edit(insuranceId: number): void {
    this.api.getById(insuranceId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.object) return;
        const rec = res.object as unknown as InsuranceMasterRecord;
        this.record.set(rec);
        this.ledgerSearchTerm.set(rec.generalLedgerName ?? '');
        this.saveMode.set(false);
        if (rec.countryId) this.onCountryChange(rec.countryId as number);
        if (rec.stateId) this.onStateChange(rec.stateId as number);
        if (rec.districtId) this.onDistrictChange(rec.districtId as number);
        if (rec.cityId) this.onCityChange(rec.cityId as number);
      },
    });
  }

  toggleStatus(row: InsuranceMasterRecord): void {
    const next: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.updateStatus(row.insuranceId as number, next, this.userId, new Date().toISOString()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          row.status = next;
          this.notify.success(res.message ?? 'Updated successfully');
        }
      },
    });
  }

  viewAddress(row: InsuranceMasterRecord): void {
    this.addressDetails.set(row as unknown as AddressDetails);
    this.showAddressModal.set(true);
  }

  closeAddressModal(): void {
    this.showAddressModal.set(false);
  }
}
