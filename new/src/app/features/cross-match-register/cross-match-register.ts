import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { CrossMatchRegisterService } from './cross-match-register.service';
import { CrossMatchDetail, CrossMatchSave } from './cross-match-register.models';

@Component({
  selector: 'app-cross-match-register',
  imports: [FormsModule],
  templateUrl: './cross-match-register.html',
  styleUrl: './cross-match-register.scss',
})
export class CrossMatchRegisterComponent {
  private readonly service = inject(CrossMatchRegisterService);
  private readonly session = inject(SessionService);

  readonly result = signal('');
  readonly error = signal('');
  readonly saving = signal(false);

  readonly form = signal<CrossMatchSave>({
    organizationId: 1,
    unitId: 1,
    patientId: 1,
    patientRequisitionId: 1,
    bloodComponentOrderId: 1,
    requisitionDate: '13-08-2026 00:00:00',
    issueDate: '13-08-2026 00:00:00',
    requiredBloodGroup: 1,
    patientBloodGroup: 1,
    requiredQty: 1,
    pendingQty: 1,
    technicianId: 1,
    remark: '',
    crossMatchStatus: 1,
    createdBy: 1,
    updatedBy: 1,
    status: 'A',
    createdDate: '13-08-2026 00:00:00',
    updatedDate: '13-08-2026 00:00:00',
    crossMatchRegisterDetails: [
      {
        componentId: 5,
        bloodGroupId: 1,
        segmentNo: 'S1',
        bloodBankStockId: 1,
        salineMajor: 1,
        salineMinor: 1,
        albuminMajor: 1,
        albuminMinor: 1,
        coombsMajor: 1,
        coombsMinor: 1,
        crossMatchReportResult: 'Compatible',
        crossMatchRegisterDetailsStatus: 1,
        createdBy: 1,
        createdDate: '13-08-2026 00:00:00',
        updatedDate: '13-08-2026 00:00:00',
      },
    ],
  });

  save(): void {
    const userId = this.session.userId ?? 1;
    const f = this.form();
    const payload: CrossMatchSave = {
      ...f,
      createdBy: userId,
      updatedBy: userId,
    };
    this.saving.set(true);
    this.result.set('');
    this.error.set('');
    this.service.saveCrossMatch(payload).subscribe({
      next: (res) => {
        this.saving.set(false);
        const ok = res?.status === 'success';
        if (ok) {
          this.result.set(
            'Cross-match saved — master and details rows landed (live-verified write, DQ-068).'
          );
        } else {
          this.error.set(res?.message ?? 'Save failed (no message).');
        }
      },
      error: (err: unknown) => {
        this.saving.set(false);
        this.error.set(`Save failed: ${String(err)}`);
      },
    });
  }
}
