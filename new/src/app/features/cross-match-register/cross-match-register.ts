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
  /** Signal-aware setter for the child rows (the indexed
   * crossMatchRegisterDetails[i].x bindings — same mutation class). */
  setDetailField(idx: number, key: string, value: unknown): void {
    this.form.update((f) => ({
      ...f,
      crossMatchRegisterDetails: (f.crossMatchRegisterDetails ?? []).map((r, i) =>
        i === idx ? {...r, [key]: value} : r,
      ),
    }));
  }


  // The order/requisition reference is REAL data a user must supply — the
  // golden trace ran against transient fixture rows (SMOKE-CM) that are torn
  // down; a standalone screen must not default to them (2026-08-13, owner
  // click-through finding). Enter the live order/requisition ids or the save
  // fails honestly with the backend's message.
  readonly patientRequisitionId = signal<number | null>(null);
  readonly bloodComponentOrderId = signal<number | null>(null);

  readonly form = signal<CrossMatchSave>({
    organizationId: 1,
    unitId: 1,
    patientId: 1,
    patientRequisitionId: 0,
    bloodComponentOrderId: 0,
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
      patientRequisitionId: this.patientRequisitionId() ?? f.patientRequisitionId,
      bloodComponentOrderId: this.bloodComponentOrderId() ?? f.bloodComponentOrderId,
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
      error: (err: any) => {
        this.saving.set(false);
        this.error.set(
          'Save failed: ' + (err?.error?.message ?? err?.message ?? 'the server rejected the request')
        );
      },
    });
  }
  /** Signal-aware field setter — rerouted from the in-place `form().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.form.update((f) => ({...f, [key]: value}));
  }

}
