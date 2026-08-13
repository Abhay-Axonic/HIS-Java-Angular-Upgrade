import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { BloodIssueService } from './blood-issue.service';

@Component({
  selector: 'app-blood-issue',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './blood-issue.html',
  styleUrls: ['./blood-issue.scss'],
})
export class BloodIssue {
  private svc = inject(BloodIssueService);
  private session = inject(SessionService);

  patientId = signal<number | null>(null);
  issuerId = signal<number | null>(null);
  receivedId = signal<number | null>(null);
  bloodUnitQty = signal<number>(1);
  issueDate = signal<string>('');
  remarks = signal<string>('');
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
      issueDate: this.issueDate() || '13-08-2026 00:00:00',
      requisitionDate: '13-08-2026 00:00:00',
      createdDate: '13-08-2026 00:00:00',
      updatedDate: '13-08-2026 00:00:00',
      btoId: 1,
      issuerId: this.issuerId() ?? 1,
      receivedId: this.receivedId() ?? 1,
      bloodUnitQty: this.bloodUnitQty() || 1,
      bloodBagIssueNo: 'ISSUE-DEMO',
      remarks: this.remarks() || 'demo issue',
      crossMatchRegisterId: 1,
      bloodBagIssueList: [
        {
          organizationId: 1,
          unitId: 1,
          issueBloodBagStatusId: 1,
          bagNo: 'SMOKE-BAG1',
          segmentNo: 'S1',
          bloodGroupId: 1,
          componentId: 5,
          bloodBankStockId: 1,
          crossMatchRegisterDetailsId: 1,
          crossMatchReportResult: 'Compatible',
          createdBy: userId,
          updatedBy: userId,
          status: 'A',
          createdDate: '13-08-2026 00:00:00',
          updatedDate: '13-08-2026 00:00:00',
        },
      ],
    };
    this.svc.saveIssue(payload).subscribe({
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
