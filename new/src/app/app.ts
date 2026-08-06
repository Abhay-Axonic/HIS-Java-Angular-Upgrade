import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NotificationService } from './core/notification.service';
import { LoaderService } from './core/loader.service';
import { StoreSelectionService } from './core/store-selection.service';
import { SessionService } from './core/session.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly notifications = inject(NotificationService);
  protected readonly loader = inject(LoaderService);
  protected readonly storeSelection = inject(StoreSelectionService);
  private readonly session = inject(SessionService);

  constructor() {
    // Track A #65: warm the permissions bootstrap as early as possible so the
    // first module-gated route's guard resolves from the warmed cache instead
    // of bearing the fetch latency. Idempotent (one fetch per session); a
    // failure surfaces as reason=permissionsUnavailable at the guard.
    void this.session.ensurePermissions();
  }
}
