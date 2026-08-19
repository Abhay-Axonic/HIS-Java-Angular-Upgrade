import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { PhlebotomistHomeCollectionService } from './phlebotomist-home-collection.service';
import { PhlebotomistCollectionRow } from './phlebotomist-home-collection.models';

/**
 * Parity rebuild of the legacy `userAllocationForHomecollection` state
 * (app.js:7091, module 155 "Home Collection", LIS) — the phlebotomist
 * home-collection allocation + save.
 *
 * CLASSIFICATION (live-verified 2026-08-13): the write is BUILDABLE
 * (POST /LIS/PhlebotomistHomeCollection/save lands
 * lab.t_phlebotomist_home_collection — DQ-061). The legacy state has NO
 * resolve/module check — its access control is the MENU-level module
 * presence (module 155 in the role's permissions). This route therefore
 * uses the moduleAccessGuard with moduleId 155 — the guard reproduces the
 * menu gate exactly (deliberate parity, NOT an omission — see README).
 *
 * CONTRACT NOTES (T0 smoke): fromTime/toTime + createdDate/updatedDate are
 * REQUIRED (the DAO NPEs on nulls); the search feed
 * (getUserAllocationHomeCollectionList) is session-gated — the form keeps
 * the search honest-state and wires only the verified save.
 */
@Component({
  selector: 'app-phlebotomist-home-collection',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './phlebotomist-home-collection.html',
  styleUrl: './phlebotomist-home-collection.scss',
})
export class PhlebotomistHomeCollection {
  private readonly service = inject(PhlebotomistHomeCollectionService);
  private readonly session = inject(SessionService);

  readonly form = signal<{
    userId: number | null;
    collectionDate: string;
    fromTime: string;
    toTime: string;
  }>({
    userId: this.session.userId ?? 1,
    collectionDate: '13-08-2026 00:00:00',
    fromTime: '10:00:00',
    toTime: '11:00:00',
  });
  readonly saving = signal(false);
  readonly saveMessage = signal('');
  readonly saveError = signal('');
  readonly saved = signal<PhlebotomistCollectionRow[]>([]);

  save(): void {
    this.saving.set(true);
    this.saveMessage.set('');
    this.saveError.set('');
    this.saved.set([]);
    const entry = {
      organizationId: this.session.organizationId ?? 1,
      unitId: this.session.unitId ?? 1,
      userId: this.form().userId ?? 1,
      phlebotomistCollectionDate: this.form().collectionDate,
      fromTime: this.form().fromTime,
      toTime: this.form().toTime,
      createdBy: this.session.userId ?? 1,
      createdDate: this.form().collectionDate,
      status: 'A',
      updatedBy: this.session.userId ?? 1,
      updatedDate: this.form().collectionDate,
    };
    this.service.save([entry]).subscribe({
      next: (res) => {
        this.saving.set(false);
        if (res.status === 'success') {
          this.saved.set(res.object ?? []);
          const id = this.saved()[0]?.phlebotomistHomeCollectionId;
          this.saveMessage.set(
            `Home collection saved.${id ? ` collectionId: ${id}` : ''}`,
          );
        } else {
          this.saveError.set(`Backend rejected: ${res.message ?? 'unknown'}`);
        }
      },
      error: (err) => {
        this.saving.set(false);
        this.saveError.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });


  }
  /** Signal-aware field setter — rerouted from the in-place `form().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.form.update((f) => ({...f, [key]: value}));
  }

}
