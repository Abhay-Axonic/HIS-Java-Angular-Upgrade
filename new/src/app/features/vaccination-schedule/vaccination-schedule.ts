import { Component, signal } from '@angular/core';

/**
 * Parity rebuild of the legacy `vaccinationSchedule` state (app.js:17056) -- `views/clinicalWorkStation/vaccination-schedule.html` driven by the shared `MasterController` shell in common-controller.js, zero endpoint calls.
 *
 * NO-ENDPOINT (noData class, DQ-2026-08-12-022): no backend list endpoint
 * exists for this screen. Honest noData state: the screen names the missing
 * backend list endpoint rather than pretending to load.
 */
@Component({
  selector: 'app-vaccination-schedule',
  standalone: true,
  imports: [],
  templateUrl: './vaccination-schedule.html',
  styleUrl: './vaccination-schedule.scss',
})
export class VaccinationSchedule {
  readonly rows = signal<unknown[]>([]);
  readonly loading = signal(false);
  readonly error = signal('No list endpoint exists in the backend for this screen.');
  readonly noData = signal(true);

  ngOnInit(): void {
    // noData -- never call a backend that has no endpoint.
  }
}
