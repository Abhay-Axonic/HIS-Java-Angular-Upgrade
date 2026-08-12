import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { TransferService } from './transfer.service';
import {
  AcceptTransferPayload,
  AdmittedPatientRow,
  BedRow,
  LEGAL_TRANSITIONS,
  RejectTransferPayload,
  SaveTransferRequestPayload,
  TransferRow,
  TRANSFER_STATUS,
  WardRow,
} from './transfer.models';

interface Envelope {
  status?: string;
  message?: string;
  object?: unknown;
}

/**
 * Direct transfer (65) — the ADMISSION chain, second full flow-map sign-off.
 * Flow: picker (admitted-patient search) -> ward/bed selection -> W1 request
 * -> W2 accept -> W3 final (the executing move); reject path parallel to W2.
 * State machine rendered from TRANSFER_STATUS; buttons only show in their
 * legal state (BR-036: the server enforces none of these — the screen is the
 * convention). The W1->W2 contract: W2's payload carries the transferRequestId
 * created by W1's response (DQ-046).
 */
@Component({
  selector: 'app-transfer-screen',
  templateUrl: './transfer.html',
  styleUrls: ['./transfer.scss'],
  standalone: true,
})
export class TransferScreen {
  private session = inject(SessionService);
  private svc = inject(TransferService);

  readonly statusMap = TRANSFER_STATUS;
  readonly legal = LEGAL_TRANSITIONS;

  // picker
  searchTerm = signal('');
  patients = signal<AdmittedPatientRow[]>([]);
  selectedPatient = signal<AdmittedPatientRow | null>(null);

  // destination feeds
  wards = signal<WardRow[]>([]);
  beds = signal<BedRow[]>([]);
  selectedWard = signal<number>(0);
  selectedBed = signal<number>(0);

  // pending list (the W2/W3 rows)
  pending = signal<TransferRow[]>([]);
  selectedTransfer = signal<TransferRow | null>(null);

  // phase + write state
  phase = signal<'pick' | 'request' | 'accept' | 'final'>('pick');
  writeMsg = signal('');
  writeOk = signal<boolean>(false);
  busy = signal(false);

  private org(): number {
    return Number(this.session.organizationId ?? 1);
  }
  private unit(): number {
    return Number(this.session.unitId ?? 1);
  }
  private user(): number {
    return Number(this.session.userId ?? 1);
  }

  private unwrap(res: unknown): Envelope {
    const r = res as Envelope;
    return {
      status: r?.status,
      message: r?.message,
      object: r?.object,
    };
  }

  // ---------- picker ----------
  searchPatients(): void {
    const term = this.searchTerm().trim();
    if (term.length < 3) {
      this.writeMsg.set('Enter at least 3 characters to search.');
      this.writeOk.set(false);
      return;
    }
    this.busy.set(true);
    this.svc.searchAdmittedPatients(term, this.org(), this.unit()).subscribe({
      next: (res) => {
        const e = this.unwrap(res);
        const list = (e?.object as { listObject?: AdmittedPatientRow[] }) ?? res as { listObject?: AdmittedPatientRow[] };
        const rows = list?.listObject ?? [];
        this.patients.set(Array.isArray(rows) ? rows : []);
        this.writeMsg.set(this.patients().length ? `${this.patients().length} patient(s) found.` : 'No patients found.');
        this.writeOk.set(true);
        this.busy.set(false);
      },
      error: (err) => {
        this.writeMsg.set(this.errText(err));
        this.writeOk.set(false);
        this.busy.set(false);
      },
    });
  }

  selectPatient(p: AdmittedPatientRow): void {
    this.selectedPatient.set(p);
    this.phase.set('request');
    this.writeMsg.set(`Patient selected: ${p.patientName ?? p.patientId}. Choose destination ward/bed and request the transfer.`);
    this.writeOk.set(true);
    this.loadWards();
  }

  // ---------- destination feeds ----------
  loadWards(): void {
    const p = this.selectedPatient();
    if (!p) return;
    this.svc.getWardsForDirectTransfer(p.genderId ?? 2, p.visitTypeId ?? 2, this.unit(), this.org()).subscribe({
      next: (res) => {
        const list = (res as { listObject?: WardRow[] })?.listObject ?? [];
        this.wards.set(Array.isArray(list) ? list : []);
      },
      error: () => this.wards.set([]),
    });
  }

  onWardChange(raw: string): void {
    const wardId = Number(raw);
    this.selectedWard.set(wardId);
    this.svc.getBedsByWard(wardId, this.org(), this.unit()).subscribe({
      next: (res) => {
        const list = (res as { listObject?: BedRow[] })?.listObject ?? [];
        this.beds.set(Array.isArray(list) ? list : []);
      },
      error: () => this.beds.set([]),
    });
  }

  onBedChange(raw: string): void {
    this.selectedBed.set(Number(raw));
  }

  // ---------- W1 ----------
  requestTransfer(): void {
    const p = this.selectedPatient();
    if (!p) {
      this.writeMsg.set('Select a patient first.');
      this.writeOk.set(false);
      return;
    }
    if (!this.selectedWard() || !this.selectedBed()) {
      this.writeMsg.set('Choose destination ward and bed.');
      this.writeOk.set(false);
      return;
    }
    const payload: SaveTransferRequestPayload = {
      transferId: 0,
      transferTypeId: 2,
      transferStatusId: 2,
      toWardId: this.selectedWard(),
      toBedId: this.selectedBed(),
      toBedCategoryId: 1,
      toBillingBedCategoryId: 1,
      admissionId: p.admissionId,
      organizationId: this.org(),
      unitId: this.unit(),
      createdBy: this.user(),
      updatedBy: this.user(),
      note: 'Requested from rebuild transfer screen',
    };
    this.busy.set(true);
    this.svc.saveTransferRequest(payload).subscribe({
      next: (res) => {
        const e = this.unwrap(res);
        if (e?.status === 'success') {
          this.writeMsg.set('Transfer request created (W1 ok). Ready for acceptance.');
          this.writeOk.set(true);
          this.phase.set('accept');
          this.loadPending();
        } else {
          this.writeMsg.set(`W1 failed: ${e?.message ?? 'unknown error'}`);
          this.writeOk.set(false);
        }
        this.busy.set(false);
      },
      error: (err) => {
        this.writeMsg.set(this.errText(err));
        this.writeOk.set(false);
        this.busy.set(false);
      },
    });
  }

  // ---------- pending list (W2/W3 rows) ----------
  loadPending(): void {
    this.svc.getPendingTransfers(this.org(), this.unit()).subscribe({
      next: (res) => {
        const list = (res as { listObject?: TransferRow[] })?.listObject ?? [];
        this.pending.set(Array.isArray(list) ? list : []);
      },
      error: () => this.pending.set([]),
    });
  }

  selectPendingRow(row: TransferRow): void {
    this.selectedTransfer.set(row);
    this.writeMsg.set(
      `Selected transfer ${row.transferId} (status ${this.statusLabel(row.transferStatusId)}). ` +
      'Accept to book the destination bed, or reject with a reason.',
    );
    this.writeOk.set(true);
  }

  statusLabel(id?: number): string {
    return id != null && this.statusMap[id] ? this.statusMap[id].label : `unknown (${id})`;
  }

  // ---------- W2 ----------
  acceptTransfer(): void {
    const row = this.selectedTransfer();
    if (!row) {
      this.writeMsg.set('Select a pending transfer first.');
      this.writeOk.set(false);
      return;
    }
    const payload: AcceptTransferPayload = {
      organizationId: this.org(),
      unitId: this.unit(),
      transferId: row.transferId,
      transferRequestId: row.transferRequestId,
      updatedBy: this.user(),
      updatedDate: Date.now(),
      toBedCategoryId: row.toBedCategoryId,
      toBillingBedCategoryId: row.toBillingBedCategoryId,
      toWardId: row.toWardId,
      toRoomId: row.toRoomId,
      toBedId: row.toBedId,
      admissionId: row.admissionId,
      workstationNote: 'Accepted from rebuild transfer screen',
    };
    this.busy.set(true);
    this.svc.acceptTransferRequest(payload).subscribe({
      next: (res) => {
        const e = this.unwrap(res);
        if (e?.status === 'success') {
          this.writeMsg.set('Transfer accepted (W2 ok) — destination bed booked, Ready For Transfer. Execute the final transfer.');
          this.writeOk.set(true);
          this.phase.set('final');
          this.loadPending();
        } else {
          this.writeMsg.set(`W2 failed: ${e?.message ?? 'unknown error'}`);
          this.writeOk.set(false);
        }
        this.busy.set(false);
      },
      error: (err) => {
        this.writeMsg.set(this.errText(err));
        this.writeOk.set(false);
        this.busy.set(false);
      },
    });
  }

  // ---------- W3 ----------
  finalTransfer(): void {
    const row = this.selectedTransfer();
    if (!row) {
      this.writeMsg.set('Select the accepted transfer first.');
      this.writeOk.set(false);
      return;
    }
    const payload: AcceptTransferPayload = {
      organizationId: this.org(),
      unitId: this.unit(),
      transferId: row.transferId,
      transferRequestId: row.transferRequestId,
      updatedBy: this.user(),
      updatedDate: Date.now(),
      toBedCategoryId: row.toBedCategoryId,
      toWardId: row.toWardId,
      toRoomId: row.toRoomId,
      toBedId: row.toBedId,
      admissionId: row.admissionId,
    };
    this.busy.set(true);
    this.svc.acceptFinalTransferRequest(payload).subscribe({
      next: (res) => {
        const e = this.unwrap(res);
        if (e?.status === 'success') {
          this.writeMsg.set('Transfer completed (W3 ok) — patient moved. Status: Transferd.');
          this.writeOk.set(true);
          this.phase.set('final');
          this.loadPending();
        } else {
          this.writeMsg.set(`W3 failed: ${e?.message ?? 'unknown error'}`);
          this.writeOk.set(false);
        }
        this.busy.set(false);
      },
      error: (err) => {
        this.writeMsg.set(this.errText(err));
        this.writeOk.set(false);
        this.busy.set(false);
      },
    });
  }

  // ---------- reject ----------
  rejectTransfer(): void {
    const row = this.selectedTransfer();
    if (!row) {
      this.writeMsg.set('Select a pending transfer first.');
      this.writeOk.set(false);
      return;
    }
    const payload: RejectTransferPayload = {
      organizationId: this.org(),
      unitId: this.unit(),
      transferId: row.transferId,
      transferRequestId: row.transferRequestId,
      updatedBy: this.user(),
      updatedDate: Date.now(),
      adtRejectReason: 'Rejected from rebuild transfer screen',
      adtReasonId: 1,
      admissionId: row.admissionId,
    };
    this.busy.set(true);
    this.svc.rejectTransferRequest(payload).subscribe({
      next: (res) => {
        const e = this.unwrap(res);
        if (e?.status === 'success') {
          this.writeMsg.set('Transfer rejected (status: Rejected).');
          this.writeOk.set(true);
          this.loadPending();
        } else {
          this.writeMsg.set(`Reject failed: ${e?.message ?? 'unknown error'}`);
          this.writeOk.set(false);
        }
        this.busy.set(false);
      },
      error: (err) => {
        this.writeMsg.set(this.errText(err));
        this.writeOk.set(false);
        this.busy.set(false);
      },
    });
  }

  // ---------- helpers ----------
  private errText(err: unknown): string {
    const e = err as { status?: number; message?: string; error?: { message?: string } };
    if (e?.error?.message) return `Error: ${e.error.message}`;
    if (e?.message) return `Error: ${e.message}`;
    return 'Request failed.';
  }

  can(phase: 'request' | 'accept' | 'final', row?: TransferRow): boolean {
    if (!row || row.transferStatusId == null) return false;
    const from = this.legal[row.transferStatusId];
    if (!from) return false;
    const target = phase === 'request' ? 2 : phase === 'accept' ? 6 : 9;
    return from.includes(target);
  }
}
