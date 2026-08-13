import { __decorate } from "tslib";
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DonorRegistrationService } from './donor-registration.service';
/**
 * Parity rebuild of screen 719 `donorRegistration` — a donor-registration
 * FORM (transaction class). T0 write smoke PASS (BUILDABLE, DQ-056): the
 * backend chain is honest (log-and-throw, never masked).
 *
 * T3 build (2026-08-12) replaces the prefix-list shell with the registration
 * form, matching the legacy validation set (donorRegistrationController.js:
 * prefix, first/last name, dob, gender, calling code, 10-digit mobile,
 * nationality, blood group, identification type/number/expiry, donor type)
 * while keeping the payload literal to the DTO (blood group / donor type /
 * expiry are validated but NOT DTO fields — the backend drops them at
 * registration; the screening flow consumes them later).
 *
 * Honest states: the dead patientAutofillSearch stub is NOT wired; a null
 * donorNumber (empty t_prefix_running_number config, DQ-056) renders as
 * "donor number pending configuration", never as "Donor null".
 */
let DonorRegistration = class DonorRegistration {
    service = inject(DonorRegistrationService);
    prefixes = signal([]);
    genders = signal([]);
    nationalities = signal([]);
    bloodGroups = signal([]);
    donorTypes = signal([]);
    identifications = signal([]);
    loadingFeeds = signal(true);
    feedsError = signal('');
    form = signal({
        prefixId: null,
        firstName: '',
        middleName: '',
        lastName: '',
        genderId: null,
        dob: '', // <input type="date"> -> YYYY-MM-DD; converted on save
        countryCallingCode: '91',
        mobileNumber: '',
        email: '',
        nationalityId: null,
        bloodGroupId: null, // UI-validated only (not a DTO field)
        donoTypeId: null, // UI-validated only (not a DTO field)
        identificationTypeId: null,
        identificationNumber: '',
        documentExpiry: '', // UI-validated only (not a DTO field)
        address: '',
    });
    saving = signal(false);
    saveError = signal('');
    result = signal(null);
    savedMessage = signal('');
    ngOnInit() {
        this.loadFeeds();
    }
    loadFeeds() {
        this.loadingFeeds.set(true);
        this.feedsError.set('');
        this.service.getPrefixes().subscribe({
            next: (res) => {
                if (res.status === 'success') {
                    this.prefixes.set(res.listObject ?? []);
                }
            },
            error: () => this.feedsError.set('The prefix feed could not be loaded.'),
        });
        this.service.getGenders().subscribe({
            next: (res) => {
                if (res.status === 'success') {
                    this.genders.set(res.listObject ?? []);
                }
            },
            error: () => this.feedsError.set('The gender feed could not be loaded.'),
        });
        this.service.getNationalities().subscribe({
            next: (res) => {
                if (res.status === 'success') {
                    this.nationalities.set(res.listObject ?? []);
                }
            },
            error: () => this.feedsError.set('The nationality feed could not be loaded.'),
        });
        this.service.getBloodGroups().subscribe({
            next: (res) => {
                if (res.status === 'success') {
                    this.bloodGroups.set(res.listObject ?? []);
                }
            },
            error: () => this.feedsError.set('The blood-group feed could not be loaded.'),
        });
        this.service.getDonorTypes().subscribe({
            next: (res) => {
                if (res.status === 'success') {
                    this.donorTypes.set(res.listObject ?? []);
                }
            },
            error: () => this.feedsError.set('The donor-type feed could not be loaded.'),
        });
        this.service.getIdentifications().subscribe({
            next: (res) => {
                if (res.status === 'success') {
                    this.identifications.set(res.listObject ?? []);
                }
            },
            error: () => this.feedsError.set('The identification feed could not be loaded.'),
            complete: () => this.loadingFeeds.set(false),
        });
    }
    /** Legacy parity validation (donorRegistrationController.js:1194-1275). */
    validate() {
        const f = this.form();
        if (!f.prefixId) {
            return 'Please select a prefix.';
        }
        if (!f.firstName?.trim()) {
            return 'Please enter a first name.';
        }
        if (!f.lastName?.trim()) {
            return 'Please enter a last name.';
        }
        if (!f.dob) {
            return 'Please enter the date of birth.';
        }
        if (!f.genderId) {
            return 'Please select a gender.';
        }
        if (!f.countryCallingCode) {
            return 'Please enter a country calling code.';
        }
        if (!/^\d{10}$/.test(f.mobileNumber)) {
            return 'Please enter a valid 10-digit mobile number.';
        }
        // Nationality is legacy-required, but the dev master
        // (public.m_nationality_master) is empty — the feed returns [] and a
        // parity-required field would make the screen unsaveable in dev.
        // BR-2026-08-12-038 (candidate): required only when the feed has rows.
        if (this.nationalities().length > 0 && !f.nationalityId) {
            return 'Please select a nationality.';
        }
        if (!f.bloodGroupId) {
            return 'Please select a blood group.';
        }
        if (!f.identificationTypeId) {
            return 'Please select an identification type.';
        }
        if (!f.identificationNumber?.trim()) {
            return 'Please enter an identification number.';
        }
        if (!f.documentExpiry) {
            return 'Please select the document expiry date.';
        }
        if (!f.donoTypeId) {
            return 'Please select a donor type.';
        }
        return '';
    }
    /** "YYYY-MM-DD" (input[type=date]) -> "DD-MM-YYYY 00:00:00" (Dozer contract). */
    toDozerDate(iso) {
        const [y, m, d] = iso.split('-');
        return `${d}-${m}-${y} 00:00:00`;
    }
    nowStamp() {
        const n = new Date();
        const p = (x) => String(x).padStart(2, '0');
        return `${p(n.getDate())}-${p(n.getMonth() + 1)}-${n.getFullYear()} ${p(n.getHours())}:${p(n.getMinutes())}:${p(n.getSeconds())}`;
    }
    onSave() {
        this.saveError.set('');
        this.result.set(null);
        this.savedMessage.set('');
        const problem = this.validate();
        if (problem) {
            this.saveError.set(problem);
            return;
        }
        const f = this.form();
        const stamp = this.nowStamp();
        const payload = {
            organizationId: 1,
            unitId: 1,
            prefixId: f.prefixId,
            firstName: f.firstName.trim(),
            middleName: f.middleName?.trim() || null,
            lastName: f.lastName.trim(),
            genderId: f.genderId,
            dob: this.toDozerDate(f.dob),
            mobileNumber: f.mobileNumber.trim(),
            countryCallingCode: f.countryCallingCode,
            email: f.email?.trim() || null,
            nationalityId: f.nationalityId,
            identificationTypeId: f.identificationTypeId,
            identificationNumber: f.identificationNumber.trim(),
            address: f.address?.trim() || null,
            createdBy: 1,
            updatedBy: 1,
            createdDate: stamp,
            updatedDate: stamp,
        };
        this.saving.set(true);
        this.service.saveDonor(payload).subscribe({
            next: (res) => {
                this.saving.set(false);
                if (res.status === 'success') {
                    this.result.set(res.object ?? null);
                    this.savedMessage.set((res.object?.donorNumber
                        ? `Donor ${res.object.donorNumber} saved successfully.`
                        : 'Donor saved successfully — donor number pending configuration.') +
                        ' (dev data; cleaned after verification)');
                }
                else {
                    this.saveError.set(res.message ?? 'The donor could not be saved. (backend error)');
                }
            },
            error: (err) => {
                this.saving.set(false);
                const msg = err?.error?.message ?? err?.message ?? '';
                this.saveError.set(msg
                    ? `The donor could not be saved: ${msg}`
                    : 'The donor could not be saved. (request failed)');
            },
        });
    }
    reset() {
        this.form.set({
            prefixId: null,
            firstName: '',
            middleName: '',
            lastName: '',
            genderId: null,
            dob: '',
            countryCallingCode: '91',
            mobileNumber: '',
            email: '',
            nationalityId: null,
            bloodGroupId: null,
            donoTypeId: null,
            identificationTypeId: null,
            identificationNumber: '',
            documentExpiry: '',
            address: '',
        });
        this.saveError.set('');
        this.result.set(null);
        this.savedMessage.set('');
    }
};
DonorRegistration = __decorate([
    Component({
        selector: 'app-donor-registration',
        standalone: true,
        imports: [FormsModule],
        templateUrl: './donor-registration.html',
        styleUrl: './donor-registration.scss',
    })
], DonorRegistration);
export { DonorRegistration };
