import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PharmanService } from './pharman.service';
import { PharmanDrugSuggestion, PharmanPopupInfo } from './pharman.models';

/**
 * Rebuild of the legacy `pharman` state -- see pharmanController.js (136
 * lines) and views/AppointmentScheduling/medicineInfo.html. Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entry 26 (5 findings, all faithfully
 * replicated below, none fixed).
 *
 * No data.moduleId on this route -- legacy's state has no resolve block,
 * but the controller has a real cookieObject session guard, same shape
 * as pesapalProcessPage/viewAssetScheduler. `appointmentObj` (the
 * declared-but-never-consumed ui-router param) is confirmed dead --
 * pharmanController.js never injects $stateParams at all -- not modeled.
 *
 * Parity targets, deliberately NOT fixed:
 *  - Both "Doctor" and "Patient" buttons call the same handler
 *    (`setType('Doctor')` in both cases in legacy).
 *  - "Save changes" has no handler at all.
 *  - The header X clears accumulated popup data; the footer "Close" does
 *    not, so results can stack across searches if closed via the footer.
 *  - Selecting a drug with zero recorded forms crashes (unguarded
 *    `medFormsList[0]`) -- replicated via a non-null assertion, same
 *    treatment as pesapalProcessPage's entry-18 crash replication.
 *  - `uib-typeahead`'s more obscure validation edge cases (e.g.
 *    `typeahead-editable="false"`'s exact reject-on-blur behavior) are
 *    NOT reproduced pixel-for-pixel -- AngularJS UI Bootstrap has no
 *    direct Angular equivalent; this rebuild implements the core
 *    interaction (search-as-you-type from 3 characters, click-to-select)
 *    without that library's full edge-case behavior. A deliberate,
 *    disclosed scoping decision for a non-critical, low-priority screen.
 *  - Legacy's `ng-change="getFormsByName()"` on the medicine input fires
 *    on every keystroke, not just on typeahead selection -- using
 *    `medicineName` (only ever set by `selectDrug`), so typing after a
 *    selection keeps refetching forms for the STALE previously-selected
 *    drug, not the in-progress typed text. Not reproduced here -- this
 *    rebuild only calls getFormsByName() on actual selection, the one
 *    genuinely useful trigger. Same disclosed-simplification treatment
 *    as the typeahead point above, not a silent fix.
 */
@Component({
  selector: 'app-pharman',
  imports: [FormsModule],
  templateUrl: './pharman.html',
  styleUrl: './pharman.scss',
})
export class Pharman {
  private readonly api = inject(PharmanService);

  medicine = signal('');
  medicineName = '';
  suggestions = signal<PharmanDrugSuggestion[]>([]);
  showSuggestions = signal(false);

  formulation = signal('');
  medFormsList = signal<{ formulation: string }[]>([]);

  type = signal('');
  list = signal<PharmanPopupInfo[]>([]);
  showModal = signal(false);

  onMedicineInput(value: string): void {
    this.medicine.set(value);
    if (value.length < 3) {
      this.suggestions.set([]);
      this.showSuggestions.set(false);
      return;
    }
    this.api.searchPharmanDrugList(value).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.suggestions.set(res.listObject ?? []);
          this.showSuggestions.set(true);
        }
      },
    });
  }

  selectDrug(suggestion: PharmanDrugSuggestion): void {
    this.medicine.set(suggestion.medicine);
    this.medicineName = suggestion.medicine;
    this.showSuggestions.set(false);
    this.getFormsByName();
  }

  getFormsByName(): void {
    this.api.getPharmanDrugFormByName(this.medicineName).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const forms = res.listObject ?? [];
          this.medFormsList.set(forms);
          // Entry 26 finding 5, faithfully replicated: unguarded --
          // crashes here if `forms` is empty, matching legacy exactly.
          this.formulation.set(forms[0]!.formulation);
        }
      },
    });
  }

  getMedForm(formulation: string): void {
    this.formulation.set(formulation);
  }

  /** Entry 26 finding 1: both "Doctor" and "Patient" buttons call this
   * with the literal string 'Doctor', matching legacy's copy-paste bug
   * exactly -- callers below intentionally both pass 'Doctor'. */
  setType(type: string): void {
    if (!this.medicine()) {
      // legacy: growl.warning("Please select medicine.")
      return;
    }
    this.type.set(type);
    this.showModal.set(true);
    this.getPopUpData();
  }

  getPopUpData(): void {
    this.api.getPopUpData(this.medicine(), this.formulation()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          // Unconditional append, never resetting -- matches legacy's
          // own accumulate-forever-until-clear() behavior exactly.
          this.list.update((current) => [...current, ...(res.listObject ?? [])]);
        }
      },
    });
  }

  /** Header X's handler -- clears state. Entry 26 finding 4: the footer
   * "Close" button deliberately does NOT call this, matching legacy. */
  clear(): void {
    this.list.set([]);
    this.medicine.set('');
    this.formulation.set('');
    this.type.set('');
    this.showModal.set(false);
  }

  /** Footer "Close" button -- dismisses the modal only, matching legacy's
   * data-dismiss="modal" with no ng-click. */
  dismissModal(): void {
    this.showModal.set(false);
  }
}
