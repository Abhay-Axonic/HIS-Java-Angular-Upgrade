import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NotificationService } from './core/notification.service';
import { LoaderService } from './core/loader.service';
import { StoreSelectionService } from './core/store-selection.service';

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
}
