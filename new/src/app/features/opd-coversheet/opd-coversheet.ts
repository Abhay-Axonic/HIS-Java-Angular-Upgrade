/**
 * opd-coversheet — parity rebuild of the legacy OPD coversheet (menu node 9
 *   'OPD Coversheet', parent 2 Opd; state OPDCoverSheet registered
 *   app.js:13412; controller OPDCoverSheetController loaded index.html:906;
 *   template views/OPD/CoverSheet/opd-coversheet.html — a 26,660-line
 *   clinical transaction surface).
 *
 * SCREEN 45 — LIVE class (the OPD subtree's sixth and final candidate; C5
 *   PASS — registered + menu node 9 chain 9 -> 2 Opd; C7 genuinely new).
 *   NOTE the menu's actionLink for node 9 is MIS-COPIED (#/encounterQueueManagement
 *   — node 6's route), but the state is genuinely reachable: the legacy
 *   navigates INTO it via $state.go("OPDCoverSheet") from patientList / the
 *   encounter queue (the drill-down pattern), so the node advertises the
 *   screen under a wrong link — the MENU-BROKEN shape, recorded not rebuilt
 *   differently.
 *
 * BACKEND BY ENDPOINT TRUTH (all CALLED with the cached token, body status
 *   field inspected):
 * - /api/global/coversheet/templates (orgId, unitId, doctorId, specialityId,
 *   templateTypeId) — LIVE: 200 success listObject [] for template types
 *   1..7 (dev-empty, WORKING);
 * - /api/global/templates/complaints + /api/global/templates/diagnosis —
 *   LIVE: 200 success [] (dev-empty, WORKING);
 * - /api/global/getActiveButtonMaster — BROKEN: the DAO's second SQL
 *   (`select disclaimer from public.m_unit_master ...`,
 *   UserRoleMasterDaoImpl.getActiveButtonMaster) references a column the
 *   dev schema does NOT have (0 columns match 'disclaimer' in any spelling);
 *   the HibernateException is caught and the controller returns the
 *   ERROR/null-code envelope — the MASKED-ERROR class with a phantom
 *   COLUMN (the visit_type_id / role_desc family; this one on a
 *   button/action-bar feed). Banner finding.
 *
 * SCOPED BACKEND CHECK: the coversheet's 83 clinical tabs (vitals,
 *   complaints, diagnosis, medication, orders, investigations, allergies,
 *   histories, immunization) are ALL patient/encounter-scoped — the legacy
 *   enters with params.appointmentObj from patientList / the queue. The
 *   rebuild renders the surface + the feeds that work WITHOUT a patient
 *   context (the template feeds above), and documents the patient-gated
 *   paths. No write-path false-success shape in the feeds surfaced here.
 */
import { Component, inject, signal } from '@angular/core';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { OpdCoversheetService } from './opd-coversheet.service';

@Component({
  selector: 'app-opd-coversheet',
  standalone: true,
  imports: [],
  templateUrl: './opd-coversheet.html',
  styleUrl: './opd-coversheet.scss',
})
export class OpdCoversheet {
  private svc = inject(OpdCoversheetService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected templateTypes = [1, 2, 3, 4, 5, 6, 7];
  protected templatesByType = signal<Record<number, number>>({});
  protected complaintsCount = signal<number | null>(null);
  protected diagnosisCount = signal<number | null>(null);
  protected buttonFeedError = signal(false);

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }

  constructor() {
    this.load();
  }

  protected load(): void {
    this.loading.set(true);
    const org = this.orgId();
    const unit = this.unitId();
    const perType: Record<number, number> = {};
    let pending = this.templateTypes.length;

    // the working no-patient feeds: coversheet templates by type
    for (const t of this.templateTypes) {
      this.svc.templates(org, unit, t).subscribe({
        next: (r) => {
          pending -= 1;
          if (r?.status === 'success') {
            perType[t] = (r.listObject as unknown[])?.length ?? 0;
          } else {
            perType[t] = -1; // honest: feed error for this type
          }
          if (pending === 0) {
            this.templatesByType.set({ ...perType });
            this.loading.set(false);
          }
        },
        error: () => {
          pending -= 1;
          perType[t] = -1;
          if (pending === 0) {
            this.templatesByType.set({ ...perType });
            this.loading.set(false);
          }
        },
      });
    }

    // complaints + diagnosis template feeds
    this.svc.complaints(org, unit).subscribe({
      next: (r) => this.complaintsCount.set((r?.listObject as unknown[])?.length ?? 0),
      error: () => this.complaintsCount.set(-1),
    });
    this.svc.diagnosis(org, unit).subscribe({
      next: (r) => this.diagnosisCount.set((r?.listObject as unknown[])?.length ?? 0),
      error: () => this.diagnosisCount.set(-1),
    });

    // the broken button-master feed — surface it honestly, never fake success
    this.svc.buttonMaster({ hospitalId: org, unitId: unit }).subscribe({
      next: (r) => {
        if (r?.status !== 'success') {
          this.buttonFeedError.set(true);
          this.notify.error('Button master feed failed (known backend defect)');
        }
      },
      error: () => this.buttonFeedError.set(true),
    });
  }
}
