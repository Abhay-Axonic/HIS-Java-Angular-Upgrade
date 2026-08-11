import { Component, signal } from '@angular/core';

/**
 * Parity rebuild of the legacy `packageMaster` state (app.js:7838) -- `views/PharmacyInventory/package-master.html (state commented out in app.js)` has no dedicated controller implementing a list endpoint.
 *
 * NO-ENDPOINT (noData class, DQ-2026-08-12-023): no backend list endpoint
 * exists for this screen. Honest noData state: the screen names the missing
 * backend list endpoint rather than pretending to load.
 */
@Component({
  selector: 'app-package-master-global',
  standalone: true,
  imports: [],
  templateUrl: './package-master-global.html',
  styleUrl: './package-master-global.scss',
})
export class PackageMaster {
  readonly rows = signal<unknown[]>([]);
  readonly loading = signal(false);
  readonly error = signal('No list endpoint exists in the backend for this screen.');
  readonly noData = signal(true);

  ngOnInit(): void {
    // noData -- never call a backend that has no endpoint.
  }
}
