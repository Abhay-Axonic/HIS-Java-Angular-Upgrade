import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CabinDisplayService } from './cabin-display.service';

@Component({
  selector: 'app-cabin-display',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cabin-display.html',
  styleUrls: ['./cabin-display.scss'],
})
export class CabinDisplay implements OnInit {
  private svc = inject(CabinDisplayService);

  protected loading = signal(false);
  protected loaded = signal(false);
  protected error = signal('');
  protected tokenList = signal<any[]>([]);

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.loading.set(true);
    this.error.set('');
    const today = this.fmt(new Date());
    this.svc.getDoctorTokenList({
      organizationId: 1,
      unitId: 1,
      doctorId: 1,
      currentDate: today,
    }).subscribe({
      next: (r: any) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.tokenList.set(r.listObject || []);
          this.loaded.set(true);
        } else {
          // the core feed is phantom-column-broken — surfaced honestly
          this.error.set(
            'Cabin token list returned an error envelope — the feed is broken by ' +
              'a phantom column (see the banner above).'
          );
        }
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Cabin token list request failed.');
      },
    });
  }

  private fmt(d: Date): string {
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
      d.getDate()
    ).padStart(2, '0')}`;
  }
}
