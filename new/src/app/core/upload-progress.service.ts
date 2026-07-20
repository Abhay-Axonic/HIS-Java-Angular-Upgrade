import { Injectable, signal } from '@angular/core';

/**
 * Rebuild-side equivalent of the legacy shared factory
 * resources/assets/js/common/imageUploadFactory.js, which `$broadcast`s a
 * `fileProgress` event that BOTH `organizationController.js` and
 * `hospitalMasterController.js` listen for (confirmed by grep -- the only
 * two consumers app-wide, see docs/PHASE2_PILOT_AUDIT.md section 4).
 *
 * This pilot only rebuilds organizationMaster, not hospitalMaster, but the
 * coupling point itself is preserved architecturally: any future rebuild
 * of hospitalMaster's upload can subscribe to the same signal instead of
 * re-inventing it, matching the original app's "one shared upload
 * progress source, multiple listeners" shape.
 */
@Injectable({ providedIn: 'root' })
export class UploadProgressService {
  readonly progress = signal<number | null>(null);

  report(loaded: number, total: number): void {
    this.progress.set(total > 0 ? loaded / total : null);
  }

  reset(): void {
    this.progress.set(null);
  }
}
