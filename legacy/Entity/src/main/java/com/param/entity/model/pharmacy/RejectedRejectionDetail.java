package com.param.entity.model.pharmacy;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;

@Entity(name = "RejectedRejectionDetail")
@Table(name = "t_rejected_rejection_detail", schema = "pharmacy")
public class RejectedRejectionDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rejected_rejection_id", nullable = false)
	private RejectedRejection rejectedRejection;
	
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "return_detail_id")
	private PatientIssueReturnDetail patientIssueReturnDetail;

	/*
	 * @Column(name = "reject_no") private String rejectNo;
	 */

	public PatientIssueReturnDetail getPatientIssueReturnDetail() {
		return patientIssueReturnDetail;
	}

	public void setPatientIssueReturnDetail(PatientIssueReturnDetail patientIssueReturnDetail) {
		this.patientIssueReturnDetail = patientIssueReturnDetail;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_reject_detail_id")
	private RejectDetail rejectDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_batch_mapper", nullable = false)
	private IssueDetailBatchMapper issueDetailBatchMapper;

	@Column(name = "accepted_quantity")
	private Integer acceptedQuantity;

	@Column(name = "reject_quantity")
	private Integer rejectQuantity;

	@Column(name = "rejected_reject_quantity")
	private Integer rejectedRejectQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public RejectedRejection getRejectedRejection() {
		return rejectedRejection;
	}

	public void setRejectedRejection(RejectedRejection rejectedRejection) {
		this.rejectedRejection = rejectedRejection;
	}

	public RejectDetail getRejectDetail() {
		return rejectDetail;
	}

	public void setRejectDetail(RejectDetail rejectDetail) {
		this.rejectDetail = rejectDetail;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public RejectReason getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(RejectReason rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getRejectNote() {
		return rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public IssueDetailBatchMapper getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(IssueDetailBatchMapper issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getRejectedRejectQuantity() {
		return rejectedRejectQuantity;
	}

	public void setRejectedRejectQuantity(Integer rejectedRejectQuantity) {
		this.rejectedRejectQuantity = rejectedRejectQuantity;
	}

	public RejectedRejectionDetail() {
		super();
	}

	public RejectedRejectionDetail(Integer id, RejectedRejection rejectedRejection, String rejectNo,
			RejectDetail rejectDetail, Integer acceptedQuantity, Integer rejectQuantity, RejectReason rejectReason,
			String rejectNote) {
		super();
		this.id = id;
		this.rejectedRejection = rejectedRejection;
		// this.rejectNo = rejectNo;
		this.rejectDetail = rejectDetail;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
	}

	public RejectedRejectionDetail(String rejectNo, RejectDetail rejectDetail, Integer acceptedQuantity,
			Integer rejectQuantity, RejectReason rejectReason, String rejectNote,
			IssueDetailBatchMapper issueDetailBatchMapper, Status status) {
		super();
		// this.rejectNo = rejectNo;
		this.rejectDetail = rejectDetail;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectQuantity = rejectQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.status = status;
	}

	public RejectedRejectionDetail(RejectDetail rejectDetail, IssueDetailBatchMapper issueDetailBatchMapper,
			Integer acceptedQuantity, Integer rejectedRejectQuantity, RejectReason rejectReason, String rejectNote,
			Status status) {
		super();
		this.rejectDetail = rejectDetail;
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
		this.status = status;
	}
	
	public RejectedRejectionDetail(PatientIssueReturnDetail patientIssueReturnDetail, IssueDetailBatchMapper issueDetailBatchMapper,
			Integer acceptedQuantity, Integer rejectedRejectQuantity, RejectReason rejectReason, String rejectNote,
			Status status) {
		super();
		this.patientIssueReturnDetail = patientIssueReturnDetail;
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectedRejectQuantity = rejectedRejectQuantity;
		this.rejectReason = rejectReason;
		this.rejectNote = rejectNote;
		this.status = status;
	}

}