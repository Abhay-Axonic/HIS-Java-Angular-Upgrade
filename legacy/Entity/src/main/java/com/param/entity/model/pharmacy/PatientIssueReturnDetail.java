package com.param.entity.model.pharmacy;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.ReturnReason;
import com.param.entity.model.master.Status;

@Entity(name="PatientIssueReturnDetail")
@Table(name="t_patient_issue_return_detail",schema="pharmacy")
public class PatientIssueReturnDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(unique=true, nullable=false)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="patient_issue_return_id")
	private PatientIssueReturn patientIssueReturn;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="issue_batch_mapper_id")
	private IssueDetailBatchMapper issueDetailBatchMapper;
	
	@Column(name="return_qty")
	private Integer returnQuantity;
	
    @Column(name="sale_details_id")
    private Integer saleDetailsId;
	
	@Column(name="accept_qty")
	private Integer acceptQuantity;
	
	public Integer getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Integer saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}

	public Integer getAcceptQuantity() {
		return acceptQuantity;
	}

	public void setAcceptQuantity(Integer acceptQuantity) {
		this.acceptQuantity = acceptQuantity;
	}

	public Integer getRejectQuantity() {
		return rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}
	@Column(name="reject_qty")
	private Integer rejectQuantity;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="return_reason_id")
	private ReasonMaster returnReason;
	
	 @ManyToOne(fetch = FetchType.LAZY)
	//@ManyToOne(fetch=FetchType.LAZY , cascade = {CascadeType.ALL})
	@JoinColumn(name = "status_id")
	private Status status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PatientIssueReturn getPatientIssueReturn() {
		return patientIssueReturn;
	}

	public void setPatientIssueReturn(PatientIssueReturn patientIssueReturn) {
		this.patientIssueReturn = patientIssueReturn;
	}

	public IssueDetailBatchMapper getIssueDetailBatchMapper() {
		return issueDetailBatchMapper;
	}

	public void setIssueDetailBatchMapper(IssueDetailBatchMapper issueDetailBatchMapper) {
		this.issueDetailBatchMapper = issueDetailBatchMapper;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	
	
	public ReasonMaster getReturnReason() {
		return returnReason;
	}

	public void setReturnReason(ReasonMaster returnReason) {
		this.returnReason = returnReason;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public PatientIssueReturnDetail() {
		super();
	}

	public PatientIssueReturnDetail(Integer id) {
		super();
		this.id = id;
	}

	public PatientIssueReturnDetail(Integer id, PatientIssueReturn patientIssueReturn,
			IssueDetailBatchMapper issueDetailBatchMapper, Integer returnQuantity, ReasonMaster returnReason) {
		super();
		this.id = id;
		this.patientIssueReturn = patientIssueReturn;
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
	}
	
	public PatientIssueReturnDetail(IssueDetailBatchMapper issueDetailBatchMapper, Integer returnQuantity, ReasonMaster returnReason,Status status) {
		super();
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		this.status=status;
	}

	public PatientIssueReturnDetail(IssueDetailBatchMapper issueDetailBatchMapper, Integer returnQuantity, ReasonMaster returnReason) {
		super();
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
		
	}


	public PatientIssueReturnDetail(IssueDetailBatchMapper issueDetailBatchMapper, Status staus,Integer returnQuantity, ReasonMaster returnReason) {
		super();
		this.issueDetailBatchMapper = issueDetailBatchMapper;
		this.status=staus;
		this.returnQuantity = returnQuantity;
		this.returnReason = returnReason;
	}
	public PatientIssueReturnDetail(Status status) {
		super();
		this.status = status;
	}
	
}
