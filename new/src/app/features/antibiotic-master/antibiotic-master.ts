import { Component, signal } from '@angular/core';

/**
 * Parity rebuild of the legacy `antibiotic` state (app.js:5291) -- `views/master/LIS/antibiotic.html` driven by the shared `MasterController` shell in common-controller.js, zero endpoint calls.
 *
 * NO-ENDPOINT (noData class, DQ-2026-08-12-022): no backend list endpoint
 * exists for this screen. Honest noData state: the screen names the missing
 * backend list endpoint rather than pretending to load.
 */
@Component({
  selector: 'app-antibiotic-master',
  standalone: true,
  imports: [],
  templateUrl: './antibiotic-master.html',
  styleUrl: './antibiotic-master.scss',
})
export class Antibiotic {
  readonly rows = signal<unknown[]>([]);
  readonly loading = signal(false);
  readonly error = signal('No list endpoint exists in the backend for this screen.');
  readonly noData = signal(true);

  ngOnInit(): void {
    // noData -- never call a backend that has no endpoint.
  }
}
