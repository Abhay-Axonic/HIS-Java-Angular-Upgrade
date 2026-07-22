import { Component, computed, inject, signal } from '@angular/core';
import { KeyValuePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AiButtonMasterService } from './ai-button-master.service';
import { AiButtonConfigPayload, AiButtonMasterRecord } from './ai-button-master.models';

/**
 * Rebuild of `aIButtonMaster` (legacy state url `/aIButtonMaster` -- note
 * the capital "I", confirmed from the live `.state(...)` registration in
 * aiButtonMasterController.js, NOT the "aiButtonMaster" spelling the
 * screen is commonly referred to by). See docs/ANGULAR_PENDING_FINDINGS.md
 * entry N (this batch) for the full audit writeup -- several quirks below
 * are faithfully reproduced legacy bugs, not rebuild defects:
 *
 *  - No pagination (legacy has none either -- the whole `button_master`
 *    list loads at once, filtered purely client-side by label).
 *  - Rows with tabId exactly 42 or 43 render NO status toggle at all (the
 *    view's `tabId<42`/`tabId>43` branches both miss that gap).
 *  - `saveButtonMaster()`/`updateButtonMaster()` show no error toast on a
 *    failed save/update -- they just silently reload the list.
 *  - Clicking Edit on an inactive (isStatus=false) row is a silent no-op.
 *  - `updateButtonMaster()`, unlike `saveButtonMaster()`, never checks
 *    that an AI configuration was actually set before submitting.
 *  - The AI-configuration "info" icon and the "Edit"/"gear" config flow
 *    share the SAME in-memory url/model state -- viewing a different
 *    row's config via the info icon while mid-edit silently overwrites
 *    the config about to be saved/updated for the row actually being
 *    edited. Reproduced as-is; not fixed.
 */
@Component({
  selector: 'app-ai-button-master',
  imports: [FormsModule, StatusToggle, KeyValuePipe],
  templateUrl: './ai-button-master.html',
  styleUrl: './ai-button-master.scss',
})
export class AiButtonMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AiButtonMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  list = signal<AiButtonMasterRecord[]>([]);
  searchFilter = signal('');
  filteredList = computed(() => {
    const term = this.searchFilter().trim().toLowerCase();
    if (!term) return this.list();
    return this.list().filter((row) => row.label?.toLowerCase().includes(term));
  });

  // ---- create/edit form fields (shared by both flows, matching legacy's
  // single $scope-level field set) ----
  buttonLabel = signal('');
  systemPrompt = signal('');
  userPrompt = signal('');
  editFlag = signal(false);
  private editingButtonId = signal<number | null>(null);
  private editingTabId = signal<number | null>(null);

  // ---- AI-configuration modal state -- shared across the gear-icon
  // config flow AND the per-row "AI Configuration" info icon, exactly as
  // legacy's single $scope.url/$scope.model are (see class doc comment). ----
  configUrl = signal('');
  configModel = signal<Record<string, string> | null>(null);
  configModalOpen = signal(false);

  aiInfoModalOpen = signal(false);
  systemPromptInfo = signal('');
  systemPromptInfoModalOpen = signal(false);
  userPromptInfo = signal('');
  userPromptInfoModalOpen = signal(false);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getButtonMaster(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
    });
  }

  /** Matches legacy's checkValidation(): label -> systemPrompt -> userPrompt, first failure wins. */
  private checkValidation(): boolean {
    if (!this.buttonLabel().trim()) {
      this.notify.error('Please enter label');
      return false;
    }
    if (!this.systemPrompt().trim()) {
      this.notify.error('Please enter system prompt');
      return false;
    }
    if (!this.userPrompt().trim()) {
      this.notify.error('Please enter user prompt');
      return false;
    }
    return true;
  }

  // ---- gear icon: open/fetch AI configuration ----
  openConfig(): void {
    if (!this.checkValidation()) return;
    if (!this.editFlag() || !this.configModel()) {
      this.api.getAiButtonConfigMaster(this.unitId).subscribe({
        next: (res) => {
          if (res.status === 'success' && res.object) {
            const parsed = JSON.parse(res.object) as AiButtonConfigPayload;
            this.configUrl.set(parsed.urlObject?.URL ?? '');
            this.configModel.set(parsed.aiPayload ?? {});
            this.configModalOpen.set(true);
          }
        },
      });
    } else {
      this.configModalOpen.set(true);
    }
  }

  closeConfigModal(): void {
    this.configModalOpen.set(false);
  }

  updateConfigValue(key: string, value: string): void {
    this.configModel.update((model) => (model ? { ...model, [key]: value } : model));
  }

  // ---- Save (create) ----
  saveButtonMaster(): void {
    if (!this.checkValidation()) return;
    const model = this.configModel();
    if (!model) {
      this.notify.error('Please add AI configuration');
      return;
    }
    const jsonObject = JSON.stringify({ urlObject: { URL: this.configUrl() }, aiPayload: model }, null, 2);
    this.api
      .saveButtonMaster({
        label: this.buttonLabel(),
        systemPrompt: this.systemPrompt(),
        userPrompt: this.userPrompt(),
        jsonObject,
        hospitalId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          // Matches legacy exactly: no error toast on failure, just reload the list either way.
          if (res.status === 'success') {
            this.notify.success('Saved successfully');
          }
          this.resetForm();
          this.loadList();
        },
        error: () => {
          this.resetForm();
          this.loadList();
        },
      });
  }

  // ---- Edit: populate form from the clicked row, no separate fetch ----
  editButtonMaster(row: AiButtonMasterRecord): void {
    if (row.isStatus === false) {
      // Faithful to legacy: clicking Edit on an inactive row is a silent no-op.
      this.editFlag.set(false);
      return;
    }
    this.buttonLabel.set(row.label);
    this.systemPrompt.set(row.systemPrompt);
    this.userPrompt.set(row.userPrompt);
    this.editingTabId.set(row.tabId);
    this.editingButtonId.set(row.buttonId);
    this.editFlag.set(true);
    const parsed = JSON.parse(row.jsonObject) as AiButtonConfigPayload;
    this.configUrl.set(parsed.urlObject?.URL ?? '');
    this.configModel.set(parsed.aiPayload ?? {});
  }

  // ---- Update: no "config must be set" check, unlike Save (faithful asymmetry) ----
  updateButtonMaster(): void {
    if (!this.checkValidation()) return;
    const jsonObject = JSON.stringify(
      { urlObject: { URL: this.configUrl() }, aiPayload: this.configModel() },
      null,
      2,
    );
    this.api
      .updateButtonMaster({
        label: this.buttonLabel(),
        systemPrompt: this.systemPrompt(),
        userPrompt: this.userPrompt(),
        tabId: this.editingTabId() ?? 0,
        buttonId: this.editingButtonId() ?? 0,
        jsonObject,
      })
      .subscribe({
        next: (res) => {
          this.loadList();
          if (res.status === 'success') {
            this.notify.success('Updated successfully');
          }
          this.resetForm();
          this.editFlag.set(false);
        },
        error: () => {
          this.editFlag.set(false);
        },
      });
  }

  private resetForm(): void {
    this.systemPrompt.set('');
    this.userPrompt.set('');
    this.buttonLabel.set('');
    this.editingTabId.set(10);
    this.editingButtonId.set(null);
  }

  // ---- Clear button: matches legacy exactly -- only clears the label if
  // NOT currently editing; always clears systemPrompt/userPrompt; never
  // touches configUrl/configModel/editFlag. ----
  clearFields(): void {
    if (!this.editFlag()) {
      this.buttonLabel.set('');
    }
    this.systemPrompt.set('');
    this.userPrompt.set('');
  }

  // ---- read-only info popups ----
  showAiInfo(row: AiButtonMasterRecord): void {
    const parsed = JSON.parse(row.jsonObject) as AiButtonConfigPayload;
    this.configUrl.set(parsed.urlObject?.URL ?? '');
    this.configModel.set(parsed.aiPayload ?? {});
    this.aiInfoModalOpen.set(true);
  }

  showSystemPromptInfo(row: AiButtonMasterRecord): void {
    this.systemPromptInfo.set(row.systemPrompt);
    this.systemPromptInfoModalOpen.set(true);
  }

  showUserPromptInfo(row: AiButtonMasterRecord): void {
    this.userPromptInfo.set(row.userPrompt);
    this.userPromptInfoModalOpen.set(true);
  }

  // ---- status toggle: correct entry-12 pattern (.update() with a new
  // row object). Only rendered by the template for tabId<42 (disabled) or
  // tabId>43 (enabled) -- rows with tabId exactly 42/43 render none at
  // all, matching legacy's own gap. ----
  onToggleStatus(row: AiButtonMasterRecord): void {
    const next = !row.isStatus;
    this.api.updateButtonMasterStatus(row.buttonId, next).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) => rows.map((r) => (r.buttonId === row.buttonId ? { ...r, isStatus: next } : r)));
          this.notify.success('status changed successfully');
        }
      },
    });
  }
}
