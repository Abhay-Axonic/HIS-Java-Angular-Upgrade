import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { StorageLocationMasterSlideBlocksService } from './storage-location-master-slide-blocks.service';
import { StorageRackRow } from './storage-location-master-slide-blocks.models';

/**
 * Parity rebuild of the legacy `storageLocationMasterForSlideBlocks` screen
 * (lis/unit/storageLocationMstForSlideBlocksController.js +
 * views/PharmacyInventory/InPatient/storage-location-master-for-slide-blocks.html).
 * Slide-block storage rack master list. Backend:
 * GET /LIS/unit/listRackMaster/{org}/{unit}/{off}/{size}. Module id 568.
 * Dev-empty: the backend answers status 'error' + "Rack Records not found."
 * when the dev DB has no rows -- the message is surfaced plainly in the error
 * state (no masked banner), matching the batch-1 house pattern.
 */
@Component({
  selector: 'app-storage-location-master-slide-blocks',
  standalone: true,
  templateUrl: './storage-location-master-slide-blocks.html',
  styleUrl: './storage-location-master-slide-blocks.scss',
})
export class StorageLocationMasterSlideBlocks {
  private readonly service = inject(StorageLocationMasterSlideBlocksService);
  private readonly session = inject(SessionService);

  private static readonly OFFSET = 0;
  private static readonly SIZE = 10;

  readonly rows = signal<StorageRackRow[]>([]);
  readonly loading = signal(false);
  readonly error = signal('');

  private orgId(): number {
    return this.session.organizationId ?? 1;
  }
  private unitId(): number {
    return this.session.unitId ?? 1;
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading.set(true);
    this.error.set('');
    this.service.getList(this.orgId(), this.unitId(), StorageLocationMasterSlideBlocks.OFFSET, StorageLocationMasterSlideBlocks.SIZE).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load slide-block storage locations.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: StorageRackRow): string[] {
    return [
      String(row.rackCode ?? '—'),
      String(row.rackName ?? '—'),
    ];
  }
}
