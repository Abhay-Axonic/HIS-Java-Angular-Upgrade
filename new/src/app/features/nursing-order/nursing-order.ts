import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NursingOrderService } from './nursing-order.service';

@Component({
  selector: 'app-nursing-order',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './nursing-order.html',
  styleUrls: ['./nursing-order.scss'],
})
export class NursingOrder {
  private svc = inject(NursingOrderService);
  private session = inject(SessionService);

  patientId = signal<number | null>(null);
  nursingOrderId = signal<number | null>(null);
  frequencyId = signal<number | null>(null);
  instruction = signal<string>('');
  startDate = signal<string>('');
  stopDate = signal<string>('');
  saving = signal(false);
  result = signal<string>('');
  ok = signal(false);

  save(): void {
    const userId = this.session.userId ?? 1;
    this.saving.set(true);
    this.result.set('');
    this.ok.set(false);
    const payload = {
      organizationId: 1,
      unitId: 1,
      patientId: this.patientId() ?? 0,
      createdBy: userId,
      updatedBy: userId,
      status: 'A',
      createdDate: '13-08-2026 00:00:00',
      updatedDate: '13-08-2026 00:00:00',
      nursingOrderMapperDtoList: [
        {
          nursingOrderId: this.nursingOrderId() ?? 1,
          frequencyId: this.frequencyId() ?? 1,
          instruction: this.instruction() || 'smoke',
          startDate: this.startDate() || '13-08-2026 00:00:00',
          stopDate: this.stopDate() || '13-08-2026 00:00:00',
          createdBy: userId,
          updatedBy: userId,
          status: 'A',
          createdDate: '13-08-2026 00:00:00',
          updatedDate: '13-08-2026 00:00:00',
        },
      ],
    };
    this.svc.save(payload).subscribe({
      next: (res: any) => {
        this.saving.set(false);
        this.ok.set(res?.status === 'success');
        this.result.set(res?.message ?? JSON.stringify(res));
      },
      error: (err: any) => {
        this.saving.set(false);
        this.ok.set(false);
        this.result.set(err?.error?.message ?? 'Request failed');
      },
    });
  }
}
