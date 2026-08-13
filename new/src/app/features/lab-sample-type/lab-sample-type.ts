import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LabSampleTypeService } from './lab-sample-type.service';

@Component({
  selector: 'app-lab-sample-type',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './lab-sample-type.html',
  styleUrls: ['./lab-sample-type.scss'],
})
export class LabSampleType {
  private svc = inject(LabSampleTypeService);
  private session = inject(SessionService);

  sampleCode = signal<string>('');
  sampleDesc = signal<string>('');
  sampleStatus = signal<string>('A');
  saving = signal(false);
  result = signal<string>('');
  ok = signal(false);

  save(): void {
    const userId = this.session.userId ?? 1;
    const now = Date.now();
    this.saving.set(true);
    this.result.set('');
    this.ok.set(false);
    const payload = {
      orgId: 1,
      code: this.sampleCode() || 'SAMPLE-NEW',
      desc: this.sampleDesc() || 'demo sample type',
      status: this.sampleStatus() || 'A',
      createdBy: userId,
      createdDate: now,
      updatedBy: userId,
      updatedDate: now,
    };
    this.svc.addSample(payload).subscribe({
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
