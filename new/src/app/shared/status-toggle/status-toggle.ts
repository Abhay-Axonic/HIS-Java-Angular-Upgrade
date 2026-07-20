import { ChangeDetectionStrategy, Component, input, output } from '@angular/core';

/**
 * Reusable status/active-inactive toggle switch.
 *
 * Bakes in the fix for docs/ANGULAR_PENDING_FINDINGS.md entry 12: the
 * corrected `input:checked + .slider` CSS ships inside this component's own
 * stylesheet, not the app-wide `styles.scss`. A screen using
 * `<app-status-toggle>` cannot regress into the "toggle works server-side
 * but looks visually static in every state" bug, because there is no
 * separate global `.switch`/`.slider` rule for a new screen to forget to
 * add or copy correctly.
 *
 * This component only emits `toggled` -- it never mutates any data itself.
 * The host component's own `toggled` handler decides how to update its
 * list, and must do so via a signal `.update()` that replaces the row (a
 * new object), not an in-place mutation of the same row reference. Entry
 * 12 found the in-place-mutation version doesn't corrupt data, but only
 * visibly re-renders when *some other* unrelated signal write happens to
 * trigger it -- fragile, not actually working. See
 * `new/src/app/features/_screen-template/README.md` for the correct
 * `.update()` usage example.
 */
@Component({
  selector: 'app-status-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <label class="switch">
      <input type="checkbox" [checked]="checked()" [disabled]="disabled()" (change)="toggled.emit()" />
      <span class="slider"></span>
    </label>
  `,
  styleUrl: './status-toggle.scss',
})
export class StatusToggle {
  checked = input.required<boolean>();
  disabled = input(false);
  toggled = output<void>();
}
