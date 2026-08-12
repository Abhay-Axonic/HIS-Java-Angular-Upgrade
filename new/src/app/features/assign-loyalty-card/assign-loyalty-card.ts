import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { AssignLoyaltyCardService } from './assign-loyalty-card.service';
import {
  PatientSearchRow,
  CardTypeRow,
  LoyaltyCardRow,
  AssignPayload,
} from './assign-loyalty-card.models';

/**
 * Assign Loyalty Card (752) — first transaction-loop flow (T3 build per the
 * signed-off flow map DQ-037/038). The chain: picker (patient search, >=3
 * chars) -> feeds (cardTypeMaster uses the literal orgnisationId typo) ->
 * card check -> existing-card lookup -> THE WRITE (assign) -> status A.
 *
 * Known findings logged (BR-2026-08-12-033, NOT applied): the assign is not
 * idempotent by construction (no business key) and there is NO one-card-per-
 * patient guard server-side (the UI enforces by convention; Amendment 4
 * low-stakes classification — build proceeds).
 */
@Component({
  selector: 'app-assign-loyalty-card',
  standalone: true,
  imports: [],
  templateUrl: './assign-loyalty-card.html',
  styleUrl: './assign-loyalty-card.scss',
})
export class AssignLoyaltyCard {
  private readonly service = inject(AssignLoyaltyCardService);
  private readonly session = inject(SessionService);

  // Step 1 state
  readonly searchName = signal('');
  readonly patients = signal<PatientSearchRow[]>([]);
  readonly searching = signal(false);
  readonly searchError = signal('');

  // Step 2 state
  readonly cardTypes = signal<CardTypeRow[]>([]);
  readonly cardConfigurationId = signal('');
  readonly feedsError = signal('');

  // Step 3/4 state
  readonly cardNo = signal('');
  readonly checkResult = signal('');
  readonly existingCards = signal<LoyaltyCardRow[]>([]);

  // Step 5 state
  readonly assigning = signal(false);
  readonly assignResult = signal('');
  readonly assignError = signal('');
  readonly assigned = signal(false);

  readonly orgId = this.session.organizationId ?? 1;
  readonly unitId = this.session.unitId ?? 1;

  ngOnInit(): void {
    this.service.getCardTypes(this.orgId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.cardTypes.set(res.listObject ?? []);
        } else {
          this.feedsError.set(res.message ?? 'Card types could not be loaded.');
        }
      },
      error: () => this.feedsError.set('Card types feed failed.'),
    });
  }

  search(): void {
    const name = this.searchName().trim();
    if (name.length < 3) {
      this.searchError.set('Please enter at least 3 characters.');
      return;
    }
    this.searching.set(true);
    this.searchError.set('');
    this.service.searchPatients({
      uhIdNumber: '', patientName: name, genderId: '', mobileNo: '',
      birthDate: '', identificationNo: '', patientType: '1',
      unitId: this.unitId, organizationId: this.orgId,
    }).subscribe({
      next: (res) => {
        this.searching.set(false);
        if (res.status === 'success') {
          this.patients.set(res.listObject ?? []);
        } else {
          this.searchError.set(res.message ?? 'Search failed.');
        }
      },
      error: () => { this.searching.set(false); this.searchError.set('Search request failed.'); },
    });
  }

  selectPatient(p: PatientSearchRow): void {
    this.service.getExistingCards(p.patientId ?? 0, this.orgId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.existingCards.set(res.listObject ?? []);
        }
      },
    });
  }

  check(): void {
    const cardNo = this.cardNo().trim();
    if (cardNo.length !== 16) {
      this.checkResult.set('Card number must be 16 digits.');
      return;
    }
    this.checkResult.set('');
    this.service.checkCard(this.orgId, this.unitId, cardNo).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.checkResult.set('Card available.');
        } else {
          this.checkResult.set(res.message ?? 'Card check failed.');
        }
      },
      error: () => this.checkResult.set('Card check request failed.'),
    });
  }

  assign(patient: PatientSearchRow): void {
    const cardNo = this.cardNo().trim();
    if (cardNo.length !== 16 || !this.cardConfigurationId()) {
      this.assignError.set('A 16-digit card number and a card type are required.');
      return;
    }
    this.assignError.set('');
    this.assignResult.set('');
    this.assigning.set(true);
    const payload: AssignPayload = {
      patientLoyaltyCardMpprId: '',
      loyaltyCardId: '',
      patientId: String(patient?.patientId ?? ''),
      cardConfigurationId: this.cardConfigurationId(),
      organisationId: this.orgId,
      unitId: this.unitId,
      status: 'A',
      createdBy: this.session.userId ?? 1,
      updatedBy: this.session.userId ?? 1,
      isDeleted: 'N',
      isUserDefined: 'Y',
      cardNo,
    };
    this.service.assign(payload).subscribe({
      next: (res) => {
        this.assigning.set(false);
        if (res.status === 'success') {
          this.assigned.set(true);
          this.assignResult.set('Card assigned.');
        } else {
          this.assignError.set(res.message ?? 'Assignment failed.');
        }
      },
      error: () => { this.assigning.set(false); this.assignError.set('Assign request failed.'); },
    });
  }

  rowCells(row: PatientSearchRow): string[] {
    return [
      String(row.patientId ?? '—'),
      String(row.patientName ?? '—'),
      String(row.uhIdNumber ?? '—'),
    ];
  }
}
