import { Component, computed, inject, signal } from '@angular/core';
import { KeyValuePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AiButtonConfigurationService } from './ai-button-configuration.service';
import {
  AiConfigPayload,
  ButtonMasterOption,
  DoctorOption,
  SavedConfigRow,
  SpecialityOption,
} from './ai-button-configuration.models';

/** The 4 optional tag checkboxes in the config modal's "Input_from_application" row. */
type TagKey = 'patientName' | 'gender' | 'age' | 'location';
const TAG_STRINGS: Record<TagKey, string> = {
  patientName: '@PatientName',
  gender: '@Gender',
  age: '@Age',
  location: '@Location',
};

/**
 * Rebuild of `aiButtonConfiguration` (legacy state url `/aiButtonConfiguration`,
 * controller file `aiButtonConfugurationController.js` -- filename has a real
 * typo, missing the second "r" in "Configuration"; state name/title are
 * spelled correctly). This screen binds an AI-assist prompt/URL configuration
 * to a Doctor-or-Speciality + a pre-defined Button template
 * (`aIButtonMaster`, batch 10's sibling screen, same `button_master` table).
 *
 * See docs/ANGULAR_PENDING_FINDINGS.md (this batch's entry) for the full
 * audit writeup. Summary of what's below:
 *
 * **Faithfully reproduced legacy bugs/quirks (real, visible, not "invisible"):**
 *  - "Configuration Name" and "Button" columns both render the same
 *    `label` value (view lines 264/268 both interpolate `{{button.label}}`)
 *    -- a real redundant-column bug, not invented differently here.
 *  - Duplicate-name/duplicate-speciality guard on CREATE only (not Update)
 *    responds `{status:"success", code:"1", message:"Already exists"}` --
 *    an actual `growl.error` IS shown (not silent), but the create form
 *    still fully resets as if the save succeeded. Reproduced as-is.
 *  - Saving (create OR edit) a config unconditionally sets its DB status
 *    back to `'A'` server-side (`ButtonMasterDaoImpl.saveConfig`,
 *    `buttonMasterConfig.setStatus('A')` on both branches) -- editing a
 *    previously-deactivated config silently reactivates it. This is
 *    backend Java behavior, out of scope for this Angular-only rebuild to
 *    change; the rebuild sends the same payload shape legacy does (no
 *    status field), so the existing backend behavior applies unchanged.
 *  - Clicking the Edit (pencil) icon only *populates* form state -- it
 *    does NOT itself open the config modal (confirmed: `editButtonMaster`
 *    never calls `.modal('show')`). The modal only opens on the next click
 *    of the gear icon. Two-step edit flow, faithfully reproduced.
 *  - `tabId`/multi-tab gating (`$scope.tabId==1`) is dead vestigial code
 *    from a commented-out "Letter" tab (view lines 130-132, 194-238) --
 *    `tabId` is hardcoded to 1 and never changed, so the gate is always
 *    true in practice. Not reproduced as a real gate (there is nothing to
 *    gate); noted here only.
 *  - `statusChanged`'s success handler reloads the Button-template dropdown
 *    (`getButtonMaster`) rather than the saved-config table it actually
 *    lives in (`getSavedConfig`) -- legacy dead-code/copy-paste artifact.
 *    Harmless because the checkbox's own two-way binding already reflects
 *    the correct end state. Reproduced via an equivalent explicit optimistic
 *    row update (this rebuild has no automatic two-way-binding side effect
 *    to rely on) plus the same (otherwise pointless) button-list reload.
 *
 * **Corrected here, NOT faithfully reproduced (silent/invisible-bug carve-out
 * per CLAUDE.md -- each logged as its own BR-PENDING entry in
 * docs/BUSINESS_RULE_MANIFEST.md/BR_INDEX.md):**
 *
 *  1. **BR-PENDING-aibuttonconfiguration-stale-configid (most severe).**
 *     Legacy's `$scope.configId` is set once inside `editButtonMaster` and
 *     is **never reset anywhere else in the controller** (confirmed: it's
 *     assigned at exactly one line, ctrl:413, and read at ctrl:215/291).
 *     Live-verified: edit config A (configId becomes 555) -> save -> switch
 *     to a genuine brand-new Doctor+Button combination WITHOUT ever calling
 *     `editButtonMaster` again -> the outgoing `saveConfig` POST still
 *     carries `"configId":555`. Since the DAO branches purely on
 *     `configId != null`, this silently **UPDATES/overwrites the unrelated
 *     config A** instead of creating a new row for the new selection -- a
 *     real "Updated successfully" toast fires, masking that no new
 *     configuration was created and a previously-saved, unrelated one was
 *     just clobbered. Fixed here by explicitly passing `null` for
 *     `configId` whenever `editFlag()` is false at save time (see
 *     `saveConfig()` below), rather than trusting a leftover field.
 *  2. **BR-PENDING-aibuttonconfiguration-silent-save-failure.** Legacy's
 *     final `saveConfig()` (ctrl:274-346, the live definition -- see next
 *     paragraph on the dead duplicate) shows a `growl` toast ONLY on
 *     `response.data.status=="success"`; its `else` branch and its
 *     `.catch(function(error){})` are both completely empty -- live-verified
 *     (real POST against unmodified legacy, forced into its error path):
 *     zero DOM toast elements rendered, empty `growl-container`, no console
 *     surfacing beyond the network response itself. Fixed here: any
 *     non-success response OR HTTP error now shows a real error toast.
 *  3. **BR-PENDING-aibuttonconfiguration-tag-checkbox-desync.** The
 *     Gender/Age/Location/PatientName checkboxes in the config modal are
 *     visually driven by `$scope.gender`/`$scope.age`/`$scope.location`
 *     (set, but never reset, by `editButtonMaster` -- and `patientName` is
 *     never even read from the loaded record at all), while what's actually
 *     PERSISTED on Save is driven by a completely separate,
 *     controller-lifetime `$scope.inputString` array that's only ever
 *     mutated by the checkboxes' own `ng-click` handler -- never
 *     synchronized from `editButtonMaster`. Live-verified: editing a saved
 *     config whose `Input_from_application` includes `@Gender` shows the
 *     Gender checkbox checked, but clicking Save without touching any
 *     checkbox sends `Input_from_application` WITHOUT `@Gender` -- a real
 *     "Updated successfully" toast, with the tag silently dropped. The
 *     inverse leak is also real: a tag checked once during a session stays
 *     in `inputString` forever and can silently leak into a later,
 *     unrelated record's save. Fixed here: the 4 tag flags are derived
 *     fresh from the loaded record's `Input_from_application` on every
 *     edit-open (and reset to false when opening a fresh create), and the
 *     persisted string is built fresh from current flag state at save time
 *     -- no stale accumulator.
 *
 * **Dead code, not reproduced (behaviorally identical either way, so no
 * manifest entry):** legacy defines `$scope.saveConfig` TWICE (ctrl:201-270,
 * then ctrl:274-346) -- the second definition unconditionally overwrites the
 * first via normal JS hoisting/reassignment, so the first is provably
 * unreachable dead code. This rebuild implements only the live (second,
 * `isSaving`-guarded) behavior.
 */
@Component({
  selector: 'app-ai-button-configuration',
  imports: [FormsModule, StatusToggle, KeyValuePipe],
  templateUrl: './ai-button-configuration.html',
  styleUrl: './ai-button-configuration.scss',
})
export class AiButtonConfiguration {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AiButtonConfigurationService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  // ---- top selector row ----
  selectionMode = signal<'doctor' | 'speciality'>('doctor');
  doctorList = signal<DoctorOption[]>([]);
  specialityList = signal<SpecialityOption[]>([]);
  buttonOptions = signal<ButtonMasterOption[]>([]);

  selectedDoctorId: number | null = null;
  selectedDoctorName = '';
  selectedSpecialityId: number | null = null;
  selectedSpecialityName = '';
  selectedButtonId: number | null = null;
  selectedButtonName = '';

  // Bound directly to the <select> elements (mirrors legacy's $scope.doctor/
  // $scope.speciality/$scope.button object-valued ng-model bindings) so the
  // dropdowns visually show the right pre-selection when editing a saved row.
  selectedDoctorObj: DoctorOption | null = null;
  selectedSpecialityObj: SpecialityOption | null = null;
  selectedButtonObj: ButtonMasterOption | null = null;

  // ---- saved-config table ----
  savedConfigs = signal<SavedConfigRow[]>([]);
  searchFilter = signal('');
  filteredConfigs = computed(() => {
    const term = this.searchFilter().trim().toLowerCase();
    if (!term) return this.savedConfigs();
    return this.savedConfigs().filter((row) => row.label?.toLowerCase().includes(term));
  });

  // ---- edit vs create state ----
  editFlag = signal(false);
  private configId: number | null = null;

  // ---- config modal state ----
  configModalOpen = signal(false);
  configUrl = signal('');
  configModel = signal<Record<string, string> | null>(null);
  userPromptField = signal('');
  systemPromptField = signal('');
  tagFlags = signal<Record<TagKey, boolean>>({ patientName: false, gender: false, age: false, location: false });

  // ---- read-only info modal ----
  aiInfoModalOpen = signal(false);
  aiInfoUrl = signal('');
  aiInfoModel = signal<Record<string, string> | null>(null);

  private isSaving = false;

  constructor() {
    this.api.getDoctors(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.doctorList.set(res.listObject ?? []);
      },
    error: (err) => console.error('ai-button-configuration error', err),
    });
    this.api.getSpecialities(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.specialityList.set(res.listObject ?? []);
      },
    error: (err) => console.error('ai-button-configuration error', err),
    });
    this.loadButtonOptions();
    this.loadSavedConfigs();
  }

  private loadButtonOptions(): void {
    this.api.getButtonMaster(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.buttonOptions.set(res.listObject ?? []);
      },
    error: (err) => console.error('ai-button-configuration error', err),
    });
  }

  private loadSavedConfigs(): void {
    this.api.getSavedConfig(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.savedConfigs.set(res.listObject ?? []);
      },
    error: (err) => console.error('ai-button-configuration error', err),
    });
  }

  // ---- Doctor / Speciality radio (mirrors getDoctor()/getSpeciality()) ----
  selectDoctorMode(): void {
    this.selectionMode.set('doctor');
    this.selectedSpecialityId = null;
    this.selectedSpecialityName = '';
  }

  selectSpecialityMode(): void {
    this.selectionMode.set('speciality');
    this.selectedDoctorId = null;
    this.selectedDoctorName = '';
  }

  onDoctorChange(doctor: DoctorOption | null): void {
    this.selectedDoctorId = doctor?.doctorId ?? null;
    this.selectedDoctorName = doctor?.doctorName ?? '';
  }

  onSpecialityChange(speciality: SpecialityOption | null): void {
    this.selectedSpecialityId = speciality?.doctorId ?? null;
    this.selectedSpecialityName = speciality?.firstName ?? '';
  }

  onButtonChange(button: ButtonMasterOption | null): void {
    this.selectedButtonId = button?.buttonId ?? null;
    this.selectedButtonName = button?.label ?? '';
  }

  // ---- gear icon: open/fetch config (mirrors getConfig()) ----
  openConfig(): void {
    if (this.selectedDoctorId == null && this.selectedSpecialityId == null) {
      this.notify.error('Please select doctor or speciality');
      return;
    }
    if (this.selectedButtonId == null) {
      this.notify.error('Please select button');
      return;
    }

    if (!this.editFlag()) {
      this.api.getAiButtonConfigMasterForDocAndSpeciality(this.selectedButtonId, this.organizationId).subscribe({
        next: (res) => {
          if (res.object != null) {
            const parsed = JSON.parse(res.object) as AiConfigPayload;
            this.configUrl.set(parsed.urlObject?.URL ?? '');
            this.configModel.set(parsed.aiPayload ?? {});
            this.tagFlags.set(this.parseTags(parsed.aiPayload?.['Input_from_application']));

            // Seed the User/System prompt textareas from the selected button
            // template's own prompt fields (matches legacy's
            // assignUserPrompt('User_Prompt')/assignSystemPrompt('System_prompt')
            // inside this same branch).
            const button = this.buttonOptions().find((b) => b.buttonId === this.selectedButtonId);
            if (button) {
              this.onUserPromptInput(button.userPrompt ?? '');
              this.onSystemPromptInput(button.systemPrompt ?? '');
            }
            this.configModalOpen.set(true);
          } else {
            this.notify.error('Data is not configured');
          }
        },
      error: (err) => {
        console.error('ai-button-configuration error', err);
        this.notify.error('Something went wrong!!');
      },
      });
    } else if (this.configModel() != null) {
      this.configModalOpen.set(true);
    }
  }

  closeConfigModal(): void {
    this.configModalOpen.set(false);
  }

  onUserPromptInput(value: string): void {
    this.userPromptField.set(value);
    this.configModel.update((m) => (m ? { ...m, User_Prompt: value } : m));
  }

  onSystemPromptInput(value: string): void {
    this.systemPromptField.set(value);
    this.configModel.update((m) => (m ? { ...m, System_prompt: value } : m));
  }

  updateConfigValue(key: string, value: string): void {
    this.configModel.update((m) => (m ? { ...m, [key]: value } : m));
  }

  toggleTag(tag: TagKey): void {
    this.tagFlags.update((flags) => ({ ...flags, [tag]: !flags[tag] }));
  }

  /** Parses a stored `Input_from_application` string into the 4 tag flags -- the fix for
   * BR-PENDING-aibuttonconfiguration-tag-checkbox-desync (see class doc comment). */
  private parseTags(raw: string | undefined): Record<TagKey, boolean> {
    const value = raw ?? '';
    return {
      patientName: value.includes(TAG_STRINGS.patientName),
      gender: value.includes(TAG_STRINGS.gender),
      age: value.includes(TAG_STRINGS.age),
      location: value.includes(TAG_STRINGS.location),
    };
  }

  /** Builds the persisted `Input_from_application` string fresh from current tag-flag state. */
  private buildInputFromApplication(): string {
    const flags = this.tagFlags();
    let result = '@Input_from_application,';
    (Object.keys(TAG_STRINGS) as TagKey[]).forEach((tag) => {
      if (flags[tag]) result += `${TAG_STRINGS[tag]},`;
    });
    return result;
  }

  // ---- Save (create or update, mirrors the live saveConfig()) ----
  saveConfig(): void {
    if (this.isSaving) return;
    this.isSaving = true;

    const model = this.configModel();
    if (!model) {
      this.isSaving = false;
      return;
    }
    const updatedModel = { ...model, Input_from_application: this.buildInputFromApplication() };
    this.configModel.set(updatedModel);

    const jsonObject = JSON.stringify(
      { urlObject: { URL: this.configUrl() }, aiPayload: updatedModel },
      null,
      2,
    );

    this.api
      .saveConfig({
        doctorId: this.selectedDoctorId,
        specialityId: this.selectedSpecialityId,
        doctorName: this.selectedDoctorName || null,
        specialityName: this.selectedSpecialityName || null,
        label: this.selectedButtonName,
        buttonId: this.selectedButtonId as number,
        // BR-PENDING-aibuttonconfiguration-stale-configid fix: only ever send a
        // real configId when genuinely editing -- never trust a leftover value.
        configId: this.editFlag() ? this.configId : null,
        jsonObject,
        hospitalId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.resetSelection();
            this.loadSavedConfigs();
            if (res.code === '1') {
              this.notify.error(res.message ?? 'Already exists');
            } else {
              this.notify.success(res.message ?? 'Saved successfully');
            }
          } else {
            // BR-PENDING-aibuttonconfiguration-silent-save-failure fix: legacy shows
            // nothing at all here. Always tell the user.
            this.editFlag.set(false);
            this.notify.error(res.message ?? 'Something went wrong!!');
          }
          this.configModalOpen.set(false);
          this.isSaving = false;
        },
        error: () => {
          // BR-PENDING-aibuttonconfiguration-silent-save-failure fix: legacy's
          // `.catch(function(error){})` is completely empty.
          this.notify.error('Something went wrong!!');
          this.configModalOpen.set(false);
          this.isSaving = false;
        },
      });
  }

  private resetSelection(): void {
    this.editFlag.set(false);
    this.configId = null;
    this.selectedDoctorId = null;
    this.selectedDoctorName = '';
    this.selectedSpecialityId = null;
    this.selectedSpecialityName = '';
    this.selectedButtonId = null;
    this.selectedButtonName = '';
    this.selectedDoctorObj = null;
    this.selectedSpecialityObj = null;
    this.selectedButtonObj = null;
    this.selectionMode.set('doctor');
  }

  // ---- Edit (pencil icon): mirrors editButtonMaster() -- ONLY populates state,
  // does not itself open the modal (matches legacy exactly; the gear icon opens it). ----
  editButtonMaster(row: SavedConfigRow): void {
    if (row.doctorId != null) {
      this.selectionMode.set('doctor');
      this.selectedSpecialityId = null;
      this.selectedSpecialityName = '';
      this.selectedSpecialityObj = null;
      const doctor = this.doctorList().find((d) => d.doctorId === row.doctorId);
      this.selectedDoctorId = row.doctorId;
      this.selectedDoctorName = doctor?.doctorName ?? row.doctorName ?? '';
      this.selectedDoctorObj = doctor ?? null;
    } else if (row.specialityId != null) {
      this.selectionMode.set('speciality');
      this.selectedDoctorId = null;
      this.selectedDoctorName = '';
      this.selectedDoctorObj = null;
      const speciality = this.specialityList().find((s) => s.doctorId === row.specialityId);
      this.selectedSpecialityId = row.specialityId;
      this.selectedSpecialityName = speciality?.firstName ?? row.specialityName ?? '';
      this.selectedSpecialityObj = speciality ?? null;
    }
    this.selectedButtonId = row.buttonId;
    this.selectedButtonName = row.label;
    this.selectedButtonObj = this.buttonOptions().find((b) => b.buttonId === row.buttonId) ?? null;
    this.editFlag.set(true);
    this.configId = row.configId;

    const parsed = JSON.parse(row.jsonObject) as AiConfigPayload;
    this.configUrl.set(parsed.urlObject?.URL ?? '');
    this.configModel.set(parsed.aiPayload ?? {});
    this.userPromptField.set(parsed.aiPayload?.['User_Prompt'] ?? '');
    this.systemPromptField.set(parsed.aiPayload?.['System_prompt'] ?? '');
    this.tagFlags.set(this.parseTags(parsed.aiPayload?.['Input_from_application']));
  }

  // ---- read-only info popup (mirrors AIInfo()) ----
  showAiInfo(row: SavedConfigRow): void {
    const parsed = JSON.parse(row.jsonObject) as AiConfigPayload;
    this.aiInfoUrl.set(parsed.urlObject?.URL ?? '');
    this.aiInfoModel.set(parsed.aiPayload ?? {});
    this.aiInfoModalOpen.set(true);
  }

  // ---- status toggle (mirrors statusChanged()) ----
  onToggleStatus(row: SavedConfigRow): void {
    const next = !row.isStatus;
    this.api.updateButtonMasterConfigStatus(row.configId, next, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.savedConfigs.update((rows) =>
            rows.map((r) => (r.configId === row.configId ? { ...r, isStatus: next } : r)),
          );
          // Faithful to legacy's statusChanged success handler, which reloads the
          // Button-template dropdown (getAllData/getButtonMaster) rather than the
          // saved-config table this toggle actually lives in -- a real, harmless
          // dead-code/copy-paste quirk (harmless there only because ng-model's
          // two-way binding already updates the visible switch independently;
          // reproduced here via the explicit row .update() above instead).
          this.loadButtonOptions();
        }
      },
    error: (err) => {
      console.error('ai-button-configuration error', err);
      this.notify.error('Something went wrong!!');
    },
    });
  }
}
