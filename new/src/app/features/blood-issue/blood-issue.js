import { __decorate } from "tslib";
import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { BloodIssueService } from './blood-issue.service';
let BloodIssue = class BloodIssue {
    svc = inject(BloodIssueService);
    session = inject(SessionService);
    patientId = signal(null);
    issuerId = signal(null);
    receivedId = signal(null);
    bloodUnitQty = signal(1);
    issueDate = signal('');
    remarks = signal('');
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
            issueDate: this.issueDate() || '13-08-2026 00:00:00',
            requisitionDate: '13-08-2026 00:00:00',
            createdDate: '13-08-2026 00:00:00',
            updatedDate: '13-08-2026 00:00:00',
            btoId: 1,
            issuerId: this.issuerId() ?? 1,
            receivedId: this.receivedId() ?? 1,
            bloodUnitQty: this.bloodUnitQty() || 1,
            bloodBagIssueNo: 'ISSUE-DEMO',
            remarks: this.remarks() || 'demo issue',
            crossMatchRegisterId: 1,
            bloodBagIssueList: [
                {
                    organizationId: 1,
                    unitId: 1,
                    issueBloodBagStatusId: 1,
                    bagNo: 'SMOKE-BAG1',
                    segmentNo: 'S1',
                    bloodGroupId: 1,
                    componentId: 5,
                    bloodBankStockId: 1,
                    crossMatchRegisterDetailsId: 1,
                    crossMatchReportResult: 'Compatible',
                    createdBy: userId,
                    updatedBy: userId,
                    status: 'A',
                    createdDate: '13-08-2026 00:00:00',
                    updatedDate: '13-08-2026 00:00:00',
                },
            ],
        };
        this.svc.saveIssue(payload).subscribe({
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
BloodIssue = __decorate([
    Component({
        selector: 'app-blood-issue',
        standalone: true,
        imports: [FormsModule],
        templateUrl: './blood-issue.html',
        styleUrls: ['./blood-issue.scss'],
    })
], BloodIssue);
export { BloodIssue };
