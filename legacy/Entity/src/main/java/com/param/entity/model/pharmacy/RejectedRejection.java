package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;

@Entity(name="RejectedRejection")
@Table(name="t_rejected_rejection",schema="pharmacy")
public class RejectedRejection extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;
	
	@Column(name = "reject_no")
	private String rejectNo;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="issue_reject_id", nullable=true)
	private Reject reject;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ward_id", nullable=true)
	private WardMasters ward;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="store_id", nullable=false)
	private Store store;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="issue_id", nullable=true)
	private Issue issue;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="status_id")
	private Status status;
	
	@OneToMany(mappedBy="rejectedRejection",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<RejectedRejectionDetail> rejectedRejectionDetail;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="return_id")
	private PatientIssueReturn patientIssueReturn;
	
	public PatientIssueReturn getPatientIssueReturn() {
		return patientIssueReturn;
	}

	public void setPatientIssueReturn(PatientIssueReturn patientIssueReturn) {
		this.patientIssueReturn = patientIssueReturn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRejectNo() {
		return rejectNo;
	}

	public void setRejectNo(String rejectNo) {
		this.rejectNo = rejectNo;
	}

	public Reject getReject() {
		return reject;
	}

	public void setReject(Reject reject) {
		this.reject = reject;
	}

	public WardMasters getWard() {
		return ward;
	}

	public void setWard(WardMasters ward) {
		this.ward = ward;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public List<RejectedRejectionDetail> getRejectedRejectionDetail() {
		return rejectedRejectionDetail;
	}

	public void setRejectedRejectionDetail(List<RejectedRejectionDetail> rejectedRejectionDetail) {
		this.rejectedRejectionDetail = rejectedRejectionDetail;
	}

	public RejectedRejection() {
		super();
	}

	public RejectedRejectionDetail addRejectedRejectionDetail(RejectedRejectionDetail rejectedRejectionDetail) {
		if (getRejectedRejectionDetail() == null)
			this.rejectedRejectionDetail = new ArrayList<RejectedRejectionDetail>();
		
		getRejectedRejectionDetail().add(rejectedRejectionDetail);
		rejectedRejectionDetail.setRejectedRejection(this);

		return rejectedRejectionDetail;
	}
	
	public RejectedRejection(Integer id, String rejectNo, Reject reject, WardMasters ward, Store store) {
		super();
		this.id = id;
		this.rejectNo = rejectNo;
		this.reject = reject;
		this.ward = ward;
		this.store = store;
	}
	
	public RejectedRejection(String rejectNo, Reject reject, Store store, Issue issue, WardMasters ward, Status status) {
		super();
		this.rejectNo = rejectNo;
		this.reject = reject;
		this.store = store;
		this.issue = issue;
		this.ward = ward;
		this.status = status;
	}
	public RejectedRejection(String rejectNo, Reject reject, Store store, Issue issue, Status status) {
		super();
		this.rejectNo = rejectNo;
		this.reject = reject;
		this.store = store;
		this.issue = issue;
		this.status = status;
	}
	public RejectedRejection(String rejectNo, Store store, Issue issue, Status status) {
		super();
		this.rejectNo = rejectNo;
		this.issue = issue;
		this.status = status;
		this.store=store;
	}
}