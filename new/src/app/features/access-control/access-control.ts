import { Component, signal } from '@angular/core';

/**
 * Parity rebuild of the legacy `accessControl` / `accessControl1` states
 * (views/common/access-control.html + views/access-control.html share the
 * accessControlController STUB).
 *
 * NO-ENDPOINT (noData class, DQ-2026-08-12-005/-019): the stub controller has
 * zero serviceAction/api/URI references — the legacy renders an empty
 * angular-ui-tree editor with no data path. Honest noData state: the screen
 * names the missing backend list endpoint rather than pretending to load.
 * ONE component serves BOTH routes (moduleId 677 and 673) — the commonMaster
 * shared-controller precedent (DQ-2026-08-12-019).
 */
@Component({
  selector: 'app-access-control',
  standalone: true,
  imports: [],
  templateUrl: './access-control.html',
  styleUrl: './access-control.scss',
})
export class AccessControl {
  readonly rows = signal<unknown[]>([]);
  readonly loading = signal(false);
  readonly error = signal('No list endpoint exists in the backend for this screen.');
  readonly noData = signal(true);

  ngOnInit(): void {
    // noData — never call a backend that has no endpoint.
  }
}
