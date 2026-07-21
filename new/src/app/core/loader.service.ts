import { Injectable, signal } from '@angular/core';

/**
 * Rebuild-side equivalent of legacy's `$rootScope.startLoader()`/`stopLoader()`
 * (app.js:429-437), which show/hide a single global `#preloader` overlay
 * element. Exposes visibility as a signal so the root shell (app.html) can
 * render one overlay for the whole app, matching legacy's single shared
 * element rather than a per-screen spinner.
 */
@Injectable({ providedIn: 'root' })
export class LoaderService {
  readonly visible = signal(false);

  start(): void {
    this.visible.set(true);
  }

  stop(): void {
    this.visible.set(false);
  }
}
