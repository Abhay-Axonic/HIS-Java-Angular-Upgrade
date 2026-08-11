import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { ProfilePanelTestMasterService } from './profile-panel-test-master.service';
import { ProfilePanelTestRow } from './profile-panel-test-master.models';

/**
 * Parity rebuild of the legacy `profilepanelTestMaster` screen
 * (lis/unit/profilepanelTestMasterController.js + views/master/LIS/
 * profile-panel-test-master.html). Profile/panel test master list. Backend:
 * GET /LIS/unit/PanelList/{org}/{unit}/{off}/{size}. Module id 567.
 * Dev-empty: the backend answers status 'error' + "Panel Test Records Not
 * Found." when the dev DB has no rows -- the message is surfaced plainly in
 * the error state (no masked banner), matching the batch-1 house pattern.
 */
@Component({
  selector: 'app-profile-panel-test-master',
  standalone: true,
  templateUrl: './profile-panel-test-master.html',
  styleUrl: './profile-panel-test-master.scss',
})
export class ProfilePanelTestMaster {
  private readonly service = inject(ProfilePanelTestMasterService);
  private readonly session = inject(SessionService);

  private static readonly OFFSET = 0;
  private static readonly SIZE = 10;

  readonly rows = signal<ProfilePanelTestRow[]>([]);
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
    this.service.getList(this.orgId(), this.unitId(), ProfilePanelTestMaster.OFFSET, ProfilePanelTestMaster.SIZE).subscribe({
      next: (res) => {
        this.loading.set(false);
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
        } else {
          this.error.set(res.message ?? 'Could not load profile panel tests.');
        }
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set(`Request failed: ${err?.status ?? 'unknown'} ${err?.message ?? ''}`);
      },
    });
  }

  rowCells(row: ProfilePanelTestRow): string[] {
    return [
      String(row.panelCode ?? '—'),
      String(row.panelName ?? '—'),
      String(row.panelAlies ?? '—'),
    ];
  }
}
