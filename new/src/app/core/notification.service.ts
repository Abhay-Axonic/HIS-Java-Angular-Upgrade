import { Injectable, signal } from '@angular/core';

export interface Notice {
  kind: 'success' | 'error' | 'warning';
  message: string;
  id: number;
}

/**
 * Rebuild-side equivalent of the legacy app's `growl` service
 * (angular-growl, `growl.success(...)`/`growl.error(...)`/`growl.warning(...)`
 * calls throughout every controller audited in docs/PHASE2_PILOT_AUDIT.md).
 * Exposes the same three severities as a signal so any host page (including
 * an eventual ngUpgrade shell) can render them with its own toast UI --
 * this service only tracks state, it doesn't render anything itself, so it
 * doesn't lock the rebuild into a specific toast library up front.
 */
@Injectable({ providedIn: 'root' })
export class NotificationService {
  private nextId = 0;
  readonly notices = signal<Notice[]>([]);

  success(message: string): void {
    this.push('success', message);
  }

  error(message: string): void {
    this.push('error', message);
  }

  warning(message: string): void {
    this.push('warning', message);
  }

  dismiss(id: number): void {
    this.notices.update((list) => list.filter((n) => n.id !== id));
  }

  private push(kind: Notice['kind'], message: string): void {
    const id = this.nextId++;
    this.notices.update((list) => [...list, { kind, message, id }]);
    setTimeout(() => this.dismiss(id), 6000);
  }
}
