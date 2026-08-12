import { Component, inject, signal } from '@angular/core';
import { TransfusionTransmittedDiseasesService } from './transfusion-transmitted-diseases.service';
import { BloodBagRow } from './transfusion-transmitted-diseases.models';

/**
 * Parity rebuild of screen 722 `transfusionTransmittedDiseases` — HONEST
 * EMPTY (search/write-path, no list endpoint). Legacy state:
 * app.js:18234-18238 (controller transfusionTransmittedDiseasesController,
 * template views/master/bloodBankDonor/transfusion-transmitted-diseases.html).
 *
 * LIVE-VERIFIED CLASSIFICATION (DQ-2026-08-12-034): NOT a master list — a
 * search/entry form (transaction class). The controller's ONLY backend
 * constant is SEARCH_BAG_DETAILS = "/api/bloodbank/searchBloodBag"
 * (bloodBank.url.config.js:141) — a blood-bag SEARCH from a date-range /
 * bloodBagNo form (controller:57-100), not a list endpoint. There is no feed
 * to load on init; the signals are kept per the house pattern and rows stays
 * empty until the search write-path is wired under the transaction
 * methodology (DQ-025/032/034). rowCells documents the search-result row
 * contract for that wiring.
 */
@Component({
  selector: 'app-transfusion-transmitted-diseases',
  standalone: true,
  imports: [],
  templateUrl: './transfusion-transmitted-diseases.html',
  styleUrl: './transfusion-transmitted-diseases.scss',
})
export class TransfusionTransmittedDiseases {
  private readonly service = inject(TransfusionTransmittedDiseasesService);

  readonly rows = signal<BloodBagRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  // No ngOnInit load: the legacy controller has NO list endpoint — only the
  // SEARCH_BAG_DETAILS blood-bag search (write-path, deferred).

  rowCells(row: BloodBagRow): string[] {
    return [
      String(row.bloodBagNo ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
