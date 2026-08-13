import { __decorate } from "tslib";
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NursingOrderService } from './nursing-order.service';
let NursingOrder = class NursingOrder {
    svc = inject(NursingOrderService);
    session = inject(SessionService);
    patientId = signal(null);
    nursingOrderId = signal(null);
    frequencyId = signal(null);
    instruction = signal('');
    startDate = signal('');
    stopDate = signal('');
    saving = signal(false);
    result = signal('');
    ok = signal(false);
    save() {
        const userId = this.session.userId ?? 1;
        this.saving.set(true);
        this.result.set('');
        this.ok.set(false);
        const payload = {
            organizationId: 1,
            unitId: 1,
            patientId: this.patientId() ?? 0,
            createdBy: userId,
            updatedBy: userId,
            status: 'A',
            createdDate: '13-08-2026 00:00:00',
            updatedDate: '13-08-2026 00:00:00',
            nursingOrderMapperDtoList: [
                {
                    nursingOrderId: this.nursingOrderId() ?? 1,
                    frequencyId: this.frequencyId() ?? 1,
                    instruction: this.instruction() || 'smoke',
                    startDate: this.startDate() || '13-08-2026 00:00:00',
                    stopDate: this.stopDate() || '13-08-2026 00:00:00',
                    createdBy: userId,
                    updatedBy: userId,
                    status: 'A',
                    createdDate: '13-08-2026 00:00:00',
                    updatedDate: '13-08-2026 00:00:00',
                },
            ],
        };
        this.svc.save(payload).subscribe({
            next: (res) => {
                this.saving.set(false);
                this.ok.set(res?.status === 'success');
                this.result.set(res?.message ?? JSON.stringify(res));
            },
            error: (err) => {
                this.saving.set(false);
                this.ok.set(false);
                this.result.set(err?.error?.message ?? 'Request failed');
            },
        });
    }
};
NursingOrder = __decorate([
    Component({
        selector: 'app-nursing-order',
        standalone: true,
        imports: [FormsModule],
        templateUrl: './nursing-order.html',
        styleUrls: ['./nursing-order.scss'],
    })
], NursingOrder);
export { NursingOrder };
