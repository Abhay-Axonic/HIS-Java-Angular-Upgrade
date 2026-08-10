/**
 * charge-conversion — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/charge-conversion.html
 *   (menu node 592 'Charge Conversion', parent 570 Billing -> 531 Unit
 *   Master, controller chargeConversionController loaded index.html:853).
 *
 * SCREEN 29 — LIVE but TRANSACTION-shaped (a patient-driven conversion flow,
 * NOT a master). The backend is real and live: ChargeConversionController
 * (billing/unit, @RequestMapping unit/ChargeConversion) with
 * /saveChargeConversion; every READ feed is context-gated (patient search ->
 * encounter -> payer selection: /adt/patientSearchByMultipleCriteria,
 * /api/global/patient/billing/{id}, /unit/CompanyContract/getVisitTypeList,
 * /unit/CompanyContract/getChargeConversionListByCurrentPayer, ...). There is
 * NO standalone list — the flow needs a patient context the rebuild cannot
 * fabricate.
 *
 * SCOPED BACKEND CHECK (billing write-path focus):
 * - FALSE-SUCCESS shape on the primary write: PASS at controller + service
 *   (both catch and RETHROW — controller :39-44 ServiceException, service
 *   :95-97 ApplicationException); DAO saveChargeConversionDetailsList
 *   (ChargeConversionDetailsDaoImpl:45-52) returns SUCCESS only after the
 *   real save.
 * - SILENT PARTIAL-FAILURE FINDING (the walk's masked-shape class, on a
 *   billing write path): ChargeConversionServiceImpl.saveChargeConversion-
 *   MasterList (lines 74-83) — when getOrderDetailsListByOrderId returns
 *   ERROR (its own catch-and-return at ChargeConversionDetailsDaoImpl:126-129),
 *   the `if(SUCCESS && listObject != null)` block silently SKIPS the details
 *   save AND the order-rate update, yet the service returns
 *   SUCCESS + "Charge conversion saved" (line 91). An inner failure reported
 *   as full success — HTTP 200 with a success body while the conversion
 *   details were never written. NOT FIXED (Rule 10) — backend BR candidate.
 * - The read path's catch-and-return (DAO:126-129) is the honest-error-
 *   masked-as-200 shape (screen-17 class), the mechanism feeding the
 *   partial-failure above.
 *
 * THE REBUILD: the honest transaction surface — the flow's structure with
 * the feeds that work without a patient context (the visit-type list),
 * and the partial-failure finding surfaced on the banner (the
 * screen-17/24/25 pattern). The full conversion flow (patient search ->
 * encounter -> payer) is context-gated and cannot be honestly exercised
 * without a patient; that is documented, not faked.
 */
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { ChargeConversionService } from './charge-conversion.service';

@Component({
  selector: 'app-charge-conversion',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './charge-conversion.html',
  styleUrl: './charge-conversion.scss',
})
export class ChargeConversion {
  private svc = inject(ChargeConversionService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected visitTypes = signal<{ id: number; name: string }[]>([]);
  protected backendFinding = signal(false);

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }

  constructor() {
    this.loadVisitTypes();
  }

  private loadVisitTypes(): void {
    this.loading.set(true);
    this.svc.visitTypes(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.visitTypes.set((r.listObject as { id: number; name: string }[]) ?? []);
        } else {
          this.notify.error(r?.message || 'Visit type list failed');
        }
      },
      error: () => {
        this.loading.set(false);
        this.notify.error('Visit type request failed');
      },
    });
  }
}
