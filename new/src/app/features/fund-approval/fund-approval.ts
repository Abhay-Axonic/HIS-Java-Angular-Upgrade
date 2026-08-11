import { Component, signal } from '@angular/core';

/**
 * Parity rebuild of the legacy `fundApproval` state.
 *
 * NO-ENDPOINT (noData class, DQ-2026-08-12-005/-020): no backend list
 * endpoint exists for this screen (MasterController shell or absent
 * controller). Honest noData state: names the missing backend path rather
 * than pretending to load. Module 809.
 */
@Component({
  selector: 'app-fund-approval',
  standalone: true,
  imports: [],
  templateUrl: './fund-approval.html',
  styleUrl: './fund-approval.scss',
})
export class FundApproval {
  readonly rows = signal<unknown[]>([]);
  readonly loading = signal(false);
  readonly error = signal('No list endpoint exists in the backend for this screen.');
  readonly noData = signal(true);

  ngOnInit(): void {
    // noData — never call a backend that has no endpoint.
  }
}
