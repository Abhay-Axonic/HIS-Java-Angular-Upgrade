import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { StorewiseGenericMapperService } from './storewise-generic-mapper.service';
import { StorewiseGenericMapperRow } from './storewise-generic-mapper.models';

/**
 * Parity rebuild of screen 635 `storewiseGenericMapper`. Legacy state: the
 * original storewiseGenericMapper state is COMMENTED OUT (app.js:13755-13760,
 * template views/master/UnitMaster/pharmacy/storewise-generic-mapper.html,
 * MasterController); the active neighbor addstorewiseGenericMapper
 * (app.js:13761-13766) is also a MasterController shell. The real list logic
 * lives in storeWiseGenericMapperController.js (GetGenericMapperList, ctrl:67-84),
 * which consumes the endpoint's response as a RAW ARRAY
 * (self.model.genericMapperList = response.data — no envelope).
 *
 * LIVE-VERIFIED CLASSIFICATION (DQ-2026-08-12-034): NOT a master list — a
 * raw-array feed (non-envelope class). GET /api/unit/storewise/generic/mapper/1
 * (STORE_WISE_GENERIC_API, constants.js:244) returned HTTP 200 with a RAW
 * ARRAY body on 2026-08-12 (status key absent). The service returns the raw
 * array typed as the row shape; this component guards Array.isArray(res)
 * before rendering — REAL FEED (raw-array class).
 */
@Component({
  selector: 'app-storewise-generic-mapper',
  standalone: true,
  imports: [],
  templateUrl: './storewise-generic-mapper.html',
  styleUrl: './storewise-generic-mapper.scss',
})
export class StorewiseGenericMapper {
  private readonly service = inject(StorewiseGenericMapperService);
  private readonly session = inject(SessionService);

  readonly rows = signal<StorewiseGenericMapperRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getMapperList(this.session.unitId ?? 1).subscribe({
      next: (res) => {
        this.loading.set(false);
        // Raw-array feed (no envelope): guard before rendering.
        if (Array.isArray(res)) {
          this.rows.set(res);
        } else {
          this.error.set(
            'The storewise generic mapper feed returned a non-array body (expected a raw array).',
          );
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: StorewiseGenericMapperRow): string[] {
    return [
      String(row.genericCode ?? '—'),
      String(row.genericName ?? '—'),
      String(row.storeName ?? '—'),
      String(row.status ?? '—'),
    ];
  }
}
