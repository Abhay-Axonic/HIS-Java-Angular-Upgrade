import { Component, signal } from '@angular/core';

/**
 * Parity rebuild of the legacy `OPConsulationConfigurationMaster` state.
 *
 * NO-ENDPOINT (noData class, DQ-2026-08-12-005/-020): no backend list
 * endpoint exists for this screen (MasterController shell or absent
 * controller). Honest noData state: names the missing backend path rather
 * than pretending to load. Module 611.
 */
@Component({
  selector: 'app-op-consultation-configuration-master',
  standalone: true,
  imports: [],
  templateUrl: './op-consultation-configuration-master.html',
  styleUrl: './op-consultation-configuration-master.scss',
})
export class OPConsulationConfigurationMaster {
  readonly rows = signal<unknown[]>([]);
  readonly loading = signal(false);
  readonly error = signal('No list endpoint exists in the backend for this screen.');
  readonly noData = signal(true);

  ngOnInit(): void {
    // noData — never call a backend that has no endpoint.
  }
}
