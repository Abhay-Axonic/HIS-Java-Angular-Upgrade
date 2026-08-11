import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TokenDisplayService } from './token-display.service';

@Component({
  selector: 'app-token-display',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './token-display.html',
  styleUrls: ['./token-display.scss'],
})
export class TokenDisplay implements OnInit {
  private svc = inject(TokenDisplayService);

  protected loading = signal(false);
  protected loaded = signal(false);
  protected error = signal('');
  protected tokenList = signal<any[]>([]);
  protected broadcastMsgs = signal<any[]>([]);

  ngOnInit(): void {
    this.refresh();
  }

  refresh(): void {
    this.loading.set(true);
    this.error.set('');
    const today = this.fmt(new Date());
    this.svc.getTokenList({ organizationId: 1, unitId: 1, currentDate: today }).subscribe({
      next: (r: any) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.tokenList.set(r.listObject || []);
          this.loaded.set(true);
        } else {
          this.error.set('Token list feed returned an error envelope.');
        }
      },
      error: () => {
        this.loading.set(false);
        this.error.set('Token list request failed.');
      },
    });
    this.svc.getTokenMsgList({ organizationId: 1, unitId: 1, appointmentTypeId: 1 }).subscribe({
      next: (r: any) => {
        if (r?.status === 'success') this.broadcastMsgs.set(r.listObject || []);
      },
      error: () => {},
    });
  }

  private fmt(d: Date): string {
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
      d.getDate()
    ).padStart(2, '0')}`;
  }
}
