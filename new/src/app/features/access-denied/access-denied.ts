import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

/**
 * Parity rebuild of legacy's `accessDenied` state (app.js:17258), reached
 * the same way legacy reaches it -- a failed module/role permission check
 * on route entry (see core/module-access.guard.ts). Same message legacy
 * shows verbatim: "Access Denied" / "You do not have permission to access
 * this page."
 */
@Component({
  selector: 'app-access-denied',
  template: `
    <section class="access-denied">
      <h1>Access Denied</h1>
      <p>
        @if (reason === 'notLoggedIn') {
          You must be logged in to access this page.
        } @else {
          You do not have permission to access this page.
        }
      </p>
    </section>
  `,
  styles: `
    .access-denied {
      text-align: center;
      margin-top: 25vh;
      color: #00001e;
    }
    h1 {
      font-weight: 400;
      font-size: 2rem;
    }
    p {
      font-size: 1.5rem;
    }
  `,
})
export class AccessDenied {
  private readonly route = inject(ActivatedRoute);
  reason = this.route.snapshot.queryParamMap.get('reason');
}
