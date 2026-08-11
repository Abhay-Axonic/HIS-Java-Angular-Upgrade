import { Component, signal } from '@angular/core';

/**
 * Parity rebuild of the legacy `daywiseDietTemplate` state (app.js:11735) -- `views/master/GlobalMaster/DietNutrition/daywise-diet-template.html` has no dedicated controller implementing a list endpoint.
 *
 * NO-ENDPOINT (noData class, DQ-2026-08-12-023): no backend list endpoint
 * exists for this screen. Honest noData state: the screen names the missing
 * backend list endpoint rather than pretending to load.
 */
@Component({
  selector: 'app-day-wise-diet-template',
  standalone: true,
  imports: [],
  templateUrl: './day-wise-diet-template.html',
  styleUrl: './day-wise-diet-template.scss',
})
export class DaywiseDietTemplate {
  readonly rows = signal<unknown[]>([]);
  readonly loading = signal(false);
  readonly error = signal('No list endpoint exists in the backend for this screen.');
  readonly noData = signal(true);

  ngOnInit(): void {
    // noData -- never call a backend that has no endpoint.
  }
}
