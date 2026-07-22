import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { TicketSystemMasterService } from './ticket-system-master.service';
import {
  ISSUE_TYPE_OPTIONS,
  PAGE_SIZE_OPTIONS,
  PRIORITY_OPTIONS,
  TicketAttachment,
  TicketFormState,
  TicketSystemMasterRecord,
  emptyTicketForm,
} from './ticket-system-master.models';

const EMAIL_PATTERN = /^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;

/**
 * Rebuild of `ticketSystemMaster` (legacy state url `/ticketSystemMaster`,
 * self-registered in ticketSystemMasterController.js, no resolve block --
 * no module gate, matching legacy exactly). See
 * docs/ANGULAR_PENDING_FINDINGS.md entry N (this batch) for the full audit
 * writeup. Several quirks below are faithfully reproduced legacy bugs, not
 * rebuild defects -- see the inline comments at each one:
 *
 *  - Initial load always shows (at most) the latest 10 tickets, ignoring
 *    the page-size control, because the backend's own initial-list query
 *    hardcodes `LIMIT 10`. Real pagination only kicks in once the user
 *    changes the page size or page number (routes through `/search`).
 *  - The search box fires a real server request on every keystroke, and
 *    matches ONLY issue-description and contact-number -- not reported-by,
 *    email, or ticket number.
 *  - "Remove All" attachments genuinely deletes every file server-side,
 *    but the visible attachment list only removes the ONE row last
 *    targeted by an individual file's remove icon (or the first row, if
 *    none was ever clicked this session) -- a real legacy bug
 *    (`$scope.listIndex` reuse), not fixed here.
 *  - Priority/Issue Type dropdowns offer only "Low"/"Bug" -- High/Medium
 *    are commented out in legacy and genuinely unavailable today.
 *  - Save always attempts a file-upload follow-up call even when zero
 *    files were selected.
 */
@Component({
  selector: 'app-ticket-system-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ticket-system-master.html',
  styleUrl: './ticket-system-master.scss',
})
export class TicketSystemMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(TicketSystemMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;
  private readonly reportedUnitName = (this.session.current?.['unitNme'] as string | undefined) ?? '';
  private readonly organizationName = (this.session.current?.['orgNme'] as string | undefined) ?? '';
  private readonly loggedInUserName = (this.session.current?.['userName'] as string | undefined) ?? '';

  issueTypeOptions = ISSUE_TYPE_OPTIONS;
  priorityOptions = PRIORITY_OPTIONS;
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  list = signal<TicketSystemMasterRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  form = signal<TicketFormState>(emptyTicketForm(this.loggedInUserName));
  files = signal<File[]>([]);
  private editingIndex: number | null = null;
  private editingTicketId: number | null = null;
  editing = signal(false);

  // ---- attachments modal ----
  attachmentModalOpen = signal(false);
  attachments = signal<TicketAttachment[]>([]);
  private currentAttachmentTicketId: number | null = null;

  // ---- shared "Alert" confirm popup (single-file remove vs remove-all) ----
  confirmModalOpen = signal(false);
  confirmIsRemoveAll = signal(false);
  private pendingRemoveFileId: number | null = null;
  private pendingRemoveIndex: number | null = null;

  constructor() {
    this.loadInitial();
  }

  private loadInitial(): void {
    this.currentPage.set(1);
    this.api.getAll(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          this.totalCount.set(res.object ?? 0);
        }
      },
    });
  }

  private offset(): number {
    return (this.currentPage() - 1) * this.pageSize();
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.searchKeyword.set('');
    this.runSearch(false);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.runSearch(false);
  }

  /** Fires on every keystroke, matching legacy's `ng-keyup="setSearchKey(...)"`. */
  onSearchKeyup(value: string): void {
    this.searchKeyword.set(value);
    this.currentPage.set(1);
    if (value === '') {
      this.loadInitial();
    } else {
      this.runSearch(true);
    }
  }

  private runSearch(isKeywordSearch: boolean): void {
    this.api
      .search({
        unitId: this.unitId,
        organizationId: this.organizationId,
        offSet: this.offset(),
        noOfRecordsPerPage: this.pageSize(),
        searchKeyWord: this.searchKeyword() || null,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.set(res.listObject ?? []);
            if (isKeywordSearch) {
              // Faithful to a real legacy bug: the backend returns the
              // CURRENT PAGE's row count as `object` when searching, not a
              // true total match count -- pagination controls beyond page 1
              // are not reliable while a keyword is active. Reproduced as-is.
              this.totalCount.set(res.listObject?.length ?? 0);
            }
          } else {
            this.notify.error('Something went wrong.');
          }
        },
      });
  }

  private checkEmail(): boolean {
    const email = this.form().email;
    if (!email) return true;
    if (!EMAIL_PATTERN.test(email)) {
      this.notify.error('Please Enter Valid Email');
      return false;
    }
    return true;
  }

  onFilesSelected(fileList: FileList | null): void {
    if (!fileList) return;
    const existing = this.files();
    const toAdd: File[] = [];
    for (const file of Array.from(fileList)) {
      if (existing.some((f) => f.name === file.name) || toAdd.some((f) => f.name === file.name)) {
        this.notify.warning(`file is already available :: ${file.name}`);
      } else {
        toAdd.push(file);
      }
    }
    this.files.set([...existing, ...toAdd]);
  }

  addTicketDetails(): void {
    if (!this.checkEmail()) return;
    if (!this.form().issueDescription) {
      this.notify.warning('Please Enter Issue Description');
      return;
    }
    const f = this.form();
    const payload = {
      ticketReportedBy: f.reportedBy,
      ticketReportedUnitName: this.reportedUnitName,
      ticketContactNumber: f.contactNumber,
      ticketEmail: f.email,
      ticketIssueDescription: f.issueDescription,
      ticketStatus: 'A',
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
      ticketCreatedDate: new Date().toISOString(),
      ticketUpdatedDate: null,
      ticketCreatedBy: this.userId,
      ticketUpdatedBy: this.userId,
      ticketIssueType: f.issueType,
      ticketPriority: f.priorityType,
    };
    this.api.save(payload).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          // Faithful to legacy: this fires unconditionally, even with zero files selected.
          this.doUploadFiles(res.object.ticketMasterId, false, null);
          this.resetForm();
        } else {
          this.notify.error('Something went Wrong..Data Not Saved..');
        }
      },
    });
  }

  private doUploadFiles(ticketMasterId: number, updateFlag: boolean, updateAtIndex: number | null): void {
    this.api
      .uploadFiles(this.files(), ticketMasterId, updateFlag, this.userId, this.unitId, this.organizationId, this.organizationName)
      .subscribe({
        next: (res) => {
          if (res.status === 'success' || (res.status as string) === 'Not Found') {
            if (res.status === 'success') {
              this.notify.success(res.message ?? 'Saved successfully');
            } else {
              this.notify.warning('Data Saved With No Attached');
            }
            const row = res.object;
            if (row) {
              if (updateAtIndex !== null) {
                this.list.update((rows) => rows.map((r, i) => (i === updateAtIndex ? row : r)));
              } else {
                this.list.update((rows) => [row, ...rows]);
              }
            }
          } else {
            this.notify.error('Something went Wrong.. File Not Uploaded');
          }
          this.files.set([]);
        },
        error: () => this.notify.error('Something went Wrong.. File Not Uploaded'),
      });
  }

  showUpdateBtn(ticket: TicketSystemMasterRecord, index: number): void {
    this.editing.set(true);
    this.editingIndex = index;
    this.editingTicketId = ticket.ticketMasterId;
    this.form.set({
      reportedBy: ticket.ticketReportedBy,
      contactNumber: ticket.ticketContactNumber,
      email: ticket.ticketEmail,
      issueDescription: ticket.ticketIssueDescription,
      issueType: ticket.ticketIssueType,
      priorityType: ticket.ticketPriority,
    });
  }

  updateTicketSystem(): void {
    if (!this.checkEmail()) return;
    if (!this.form().issueDescription) {
      this.notify.warning('Please Enter Issue Description');
      return;
    }
    if (this.editingTicketId === null || this.editingIndex === null) return;
    const f = this.form();
    const payload = {
      ticketReportedBy: f.reportedBy,
      ticketReportedUnitName: this.reportedUnitName,
      ticketContactNumber: f.contactNumber,
      ticketEmail: f.email,
      ticketIssueDescription: f.issueDescription,
      ticketStatus: 'A',
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
      ticketMasterId: this.editingTicketId,
      ticketUpdatedDate: new Date().toISOString(),
      ticketCreatedBy: this.userId,
      ticketUpdatedBy: this.userId,
      ticketIssueType: f.issueType,
      ticketPriority: f.priorityType,
    };
    const index = this.editingIndex;
    const ticketMasterId = this.editingTicketId;
    this.api.update(payload).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          if (this.files().length > 0) {
            this.doUploadFiles(ticketMasterId, true, index);
          } else {
            this.notify.success(res.message ?? 'Ticket Updated Succesfully');
            const row = res.object;
            this.list.update((rows) => rows.map((r, i) => (i === index ? { ...r, ...row } : r)));
          }
          this.resetForm();
          this.editing.set(false);
        } else {
          this.notify.error('Something went Wrong');
        }
      },
    });
  }

  private resetForm(): void {
    this.form.set(emptyTicketForm(this.loggedInUserName));
    this.files.set([]);
    this.editingIndex = null;
    this.editingTicketId = null;
  }

  // ---- status toggle: correct entry-12 pattern (.update() with a new row object) ----
  onToggleStatus(row: TicketSystemMasterRecord): void {
    const next: 'A' | 'I' = row.ticketStatus === 'A' ? 'I' : 'A';
    this.api.changeStatus(row.ticketMasterId, this.unitId, this.organizationId, next).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) =>
            rows.map((r) => (r.ticketMasterId === row.ticketMasterId ? { ...r, ticketStatus: next } : r)),
          );
          this.notify.success(res.message ?? 'Status updated');
        }
      },
    });
  }

  // ---- attachments modal ----
  showAllAttachments(ticketMasterId: number): void {
    this.currentAttachmentTicketId = ticketMasterId;
    this.api.getAttachments(ticketMasterId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.attachments.set(res.listObject ?? []);
          this.attachmentModalOpen.set(true);
        } else if ((res.status as string) === 'No data found') {
          // Faithful to legacy: a "no data" result is shown via the SUCCESS
          // (green) toast style, not an error/warning -- an odd but real choice.
          this.notify.success(res.message ?? 'No data found');
        } else {
          this.notify.error('Something went Wrong');
        }
      },
    });
  }

  closeAttachmentModal(): void {
    this.attachmentModalOpen.set(false);
  }

  confirmRemove(attachment: TicketAttachment, index: number): void {
    this.confirmIsRemoveAll.set(false);
    this.pendingRemoveFileId = attachment.fileId;
    this.pendingRemoveIndex = index;
    this.confirmModalOpen.set(true);
  }

  confirmRemoveAll(): void {
    this.confirmIsRemoveAll.set(true);
    this.confirmModalOpen.set(true);
  }

  closeConfirmModal(): void {
    this.confirmModalOpen.set(false);
  }

  removeAttachment(): void {
    if (this.pendingRemoveFileId === null) return;
    this.api.removeFile(this.pendingRemoveFileId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const idx = this.pendingRemoveIndex;
          if (idx !== null) {
            this.attachments.update((rows) => rows.filter((_, i) => i !== idx));
          }
          this.notify.success(res.message ?? 'Removed');
        } else {
          this.notify.error(res.message ?? 'Error');
        }
        this.confirmModalOpen.set(false);
      },
    });
  }

  removeAllAttachment(): void {
    if (this.currentAttachmentTicketId === null) return;
    this.api.removeAllFiles(this.currentAttachmentTicketId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          // Faithful to a real legacy bug: the backend deletes every file
          // for this ticket, but the UI only removes the one row last
          // targeted by an individual remove click (index 0 if none was
          // ever clicked this session) -- NOT the whole visible list.
          const idx = this.pendingRemoveIndex ?? 0;
          this.attachments.update((rows) => rows.filter((_, i) => i !== idx));
          this.notify.success(res.message ?? 'Removed');
        } else {
          this.notify.error(res.message ?? 'Error');
        }
        this.confirmModalOpen.set(false);
      },
    });
  }
}
