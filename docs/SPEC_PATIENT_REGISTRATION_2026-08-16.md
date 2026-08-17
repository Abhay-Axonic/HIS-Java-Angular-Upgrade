# SPEC — OPD PATIENT REGISTRATION (modern Angular rebuild)

SERVED-BRANCH: `fix/patient-type-details-status` @ ac229c50. Source of truth:
legacy controller `controller/opd/transection/patientRegistrationController.js`
(7989 lines) + templates `views/AdmissionDischarge/patient-registration.html`
(main form) + `personal-registration.html` (second view). This is the spec —
NO code until it is reviewed.

## 1. Sections and fields (the legacy's model)

The legacy uses ONE controller scope object `patientObj` plus `kinDetails`; the
permanent/company blocks are fields ON `patientObj` (not separate objects).

| Section | Fields (ng-model, all on patientObj unless noted) | Validations (legacy) |
|---|---|---|
| **1. Patient search (UHID)** | datepickerSearch, dependentSearchObj{typeId, identificationTypeId, identificationNumber, employeeName, empCode, dependentName}, employeeSearchObj{typeId, specialityId, identificationTypeId, identificationNumber, employeeName, empCode} | — |
| **2. Personal** | prefixId (select, req), firstName (req), middleName (req), lastName (req), genderId (select, req), datepicker→dob (dd/MM/yyyy, req), ageYear/ageMonth/ageDay (computed from DOB), maritalStatusId, nationalityId, raceId, occupationId, patientCategoryId, bloodGroup, aliseName (req when VIP), isVip, healthId, mobileNo (req, maxlength 10), countryCallingCode, phoneCode/phoneNumber, whatsappCallingCode/whatsappNo, email, skypeId, fileSequenceNumber | ~445 `required` attributes across the form; maxlength 3/10; patterns |
| **3. Identification** | identificationId (select, req), identificationNo (req), datepickerExpiry (identification expiry, dd/MM/yyyy) | required + date |
| **4. Kin** (kinDetails) | prefixId, kinName (req), relationId, mobileNo (req), phoneNo, countryId/stateId/districtId/cityId/areaId (kin's own geo cascade), address, postCode, identificationId, identificationNo, expiry, isGuarantor | prefix/kinName/mobile/area required |
| **5. Address (geography cascade)** | countryId→stateId→districtId→cityId→areaId, areaName, zipCode, address, areaNew/areaNotExist (add-new-area toggles) | required |
| **6. Company details** | companyName, companyAddress, companyCountryId→companyStateId→companyDistrictId→companyCityId→companyAreaId, companyZipCode, companyMobileNumber | — |
| **7. Permanent address** | permanentCountryId→permanentStateId→permanentDistrictId→permanentCityId→permanentAreaId, permanentZipCode, permanentPhoneCode/PhoneNumber, permanentAddress, permanentArea, areaNew1/areaNotExistPermanent | required |
| **8. Photo upload** | image (file), deletePatientImage | file upload |
| **9. Payment entitlement** | paymentEntitlementId (Self/Insurance/Corporate — GETACTIVEPAYMENTENTITLEMENT), insurance contracts (addContracts/removeContract — company + associated-company lists), insurance policies (addInsurances) | — |

## 2. API endpoints (the full inventory)

**Writes:** POST `/api/global/patientRegistration` (ADDPATIENTREGISTRATION) ·
POST `/api/global/updatePatient` (UPDATEPATIENTREGISTRATION) · CONVERTPATIENT
(unknown→known).

**Search:** POST `/api/global/patientSearch` (GETTEMPORYPATIENTFORPREREGISTRATION
— the UHID/patient lookup) · SEARCHDOCTORDETAILSBYCRITERIA ·
SEARCHPLAINDOCTORDETAILSBYCRITERIA · SEARCHDEPENDENTDETAILSBYCRITERIA ·
GETPLAINEMPLOYEEDETAILS.

**Master lists:** GETACTIVEGENDERLIST · GETACTIVEPREFIXLIST ·
GETACTIVEMARITALSTATUSLIST · GETACTIVENATIONALITYLIST ·
GETACTIVEOCCUPATIONLIST · GETACTIVERELATIONLIST · GETACTIVEPATIENTCATEGORY ·
GETACTIVEPRIORITYLIST · GETRACELIST · GETACTIVEIDENTIFICATION
(`/adt/getActiveIdentificationList`) · GETACTIVESPECIALITY ·
GETACTIVEPAYMENTENTITLEMENT · company/associated-company lists.

**Geography cascade:** GETSINGLECOUNTRY · GETSTATELISTBYCOUNTRYID ·
GETDISTRICTLISTBYSTATEID · GETCITYLISTBYDISTRICTID ·
GETAREALISTBYCITY (`/adt/getAreaByCityId`) · `/adt/getDataByPinCode/` (pincode
→ address) · `/global/serviceAreaBysearchKeyword` (area search).

**Photo/docs:** GET `/api/global/getPatientDocumentDetailList` ·
`/api/global/patient/deletePatientImage/`.

**Config/OTP/ABDM:** GET
`/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/`
(the DYNAMIC required-field config — the per-unit source of which fields are
mandatory) · `/autoMail/sendOTP` + `/autoMail/sendOTPonMobile` (mobile OTP) ·
`/api/abdm/v3/link/patient/links/sms/notify2` (health-id) ·
`/api/global/checkSqlConnectionForUpdate` · `/api/global/getDatabaseLink`.

## 3. Cascades

- **Geography ×3** (the patient's address, the permanent address, the kin's
  address) + **company ×1**: country → state → district → city → area, each
  level's list loaded on the parent's selection (GETSTATE...BYCOUNTRYID etc.),
  the area via GETAREALISTBYCITY. The add-new-area toggles (areaNew,
  areaNotExist, areaNew1, areaNotExistPermanent).
- **Identification → expiry**: the identification type's selection enables the
  expiry datepicker.
- **Payment entitlement → insurance/company contracts** (the add/remove rows).
- **Search → populate**: the UHID search (patientSearch) populates the whole
  form; the doctor/employee/dependent searches populate their fields.
- **DOB ↔ age**: datepicker → ageYear/Month/Day (calculateAge on blur).

## 4. Rebuilt conventions to follow (from insurance-master + bed-master)

- Feature dir: `registration/` with `.ts` (component), `.html`, `.service.ts`,
  `.models.ts`, `.scss`.
- Signals (`signal()`), `@if`/`@for` — no legacy syntax (project rule).
- Services: `this.base` + typed http calls; the response-key guard
  (`res.listObject?.[0] ?? res.object` — the DQ-127 class) on the getById-style
  reads; the `ApiResponse` envelope types in `.models.ts`.
- Cascade lists as signals (countryList/stateList/districtList/cityList pattern
  from insurance-master.ts:137-176), each loaded on the parent's change.
- Audit fields on save: `formatLegacyNow()` for updatedDate/createdDate (the
  DQ-128 convention; the backend's getDate parses the payload dates).
- The moduleAccessGuard + `data.moduleId`/title in the route (the legacy's
  patientRegistration resolve requires the same module id the state gates).
- The field-config endpoint decides the required set — the spec's requireds are
  the legacy template's static set; the config may add/remove per unit (the
  rebuild must apply the config's requireds the same way).

## 5. Scope notes / risks

- The largest form in the codebase (445+ requireds, 3 geo cascades + company,
  photo upload, OTP, ABDM, dynamic field-config).
- The legacy's save posts the WHOLE patientObj (including the permanent +
  company blocks); the rebuild's payload must match the DTO exactly (the
  backend parses by field name — the audit dates, the null-safe util).
- The save is drift-adjacent (the t_patient_details family_id phantom was the
  local's registration blocker; the corrected backend's master-data gaps
  blocked the drive — the build itself is not blocked by that, but live
  verification of a save needs the masters seeded).
- This spec is the contract: fields, types, endpoints, cascades, validations.
  The build starts only after your review.

## 6. OWNER REVIEW — three binding changes (2026-08-16)

1. **The field config DRIVES requirements.** The build MUST read
   `/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/`
   via the rebuild's existing `RegistrationConfigService` (the same mechanism
   insurance-master uses) and derive the required set from it — the template's
   static requireds are the fallback baseline, the config overrides per unit.
   If the corrected backend says mobile is optional, the rebuild marks it
   optional.
2. **The save payload uses the DTO field names** (PatientRegistrationDto —
   the @RequestBody of POST /api/global/patientRegistration): patientId,
   organizationId, unitId, uhidNumber, prefixId, firstName, middleName,
   lastName, genderId, dob (String, dd/MM/yyyy), mobileNumber,
   countryCallingCode, email, identificationTypeId, identificationNumber,
   identificationExpiryDate (String), patientCategoryId, address, countryId,
   stateId, districtId, cityId, areaId, zipCode, status (char), createdBy,
   updatedBy, createdDate (String), updatedDate (String); the kin is the NESTED
   KinDetailsDto (kinDetailsDto): kinDetailsId, organizationId, unitId,
   prefixId, kinName, relationId, mobileNo, phoneNo, identificationId,
   identificationNo, expiry, isGuarantor, countryId..areaId, address, postCode.
   NOT the $scope names. The payment entitlement is NOT in this DTO (the
   sponsor/payment is a separate mechanism — the form carries the selector;
   the sponsor write is deferred).
3. **Scope cut — FIRST BUILD (core only):** Personal (prefix, name, gender,
   DOB/age, mobile, email) + Identification (type, number, expiry) + Kin
   (name, relation, mobile) + Address geography cascade (country->state->
   district->city->area, zip) + Payment Entitlement selector + Save + audit
   fields. DEFERRED to pass 2: ABDM, OTP, photo, company, permanent address,
   add-new-area, employee/dependent search, VIP/alias, barcode.
