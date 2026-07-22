/**
 * Real field shape read directly from `TicketSystemMasterDto`
 * (legacy/Global/src/main/java/com/param/global/ticketofsystem/dto/TicketSystemMasterDto.java)
 * and ticketSystemMasterController.js -- not guessed.
 */
export interface TicketSystemMasterRecord {
  ticketMasterId: number;
  ticketReportedBy: string;
  ticketContactNumber: string;
  ticketEmail: string;
  ticketIssueDescription: string;
  ticketStatus: 'A' | 'I';
  ticketNumber?: string;
  ticketIssueType: string;
  ticketPriority: string;
}

export interface TicketAttachment {
  fileId: number;
  fileName: string;
  fileUrl: string;
}

export interface TicketFormState {
  reportedBy: string;
  contactNumber: string;
  email: string;
  issueDescription: string;
  issueType: string;
  priorityType: string;
}

export function emptyTicketForm(reportedBy: string): TicketFormState {
  return {
    reportedBy,
    contactNumber: '',
    email: '',
    issueDescription: '',
    issueType: '',
    priorityType: '',
  };
}

/**
 * Both option lists are read verbatim from the legacy controller's
 * hardcoded arrays -- High/Medium priority are commented out in legacy
 * (ticketSystemMasterController.js:56) and genuinely unavailable today,
 * not an oversight in this rebuild. See docs/ANGULAR_PENDING_FINDINGS.md
 * entry N (this batch).
 */
export const ISSUE_TYPE_OPTIONS = [{ type: 'Bug', id: '1' }];
export const PRIORITY_OPTIONS = [{ type: 'Low', id: '3' }];

export const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
