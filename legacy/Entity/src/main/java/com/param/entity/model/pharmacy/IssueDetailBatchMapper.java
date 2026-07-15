package com.param.entity.model.pharmacy;

import java.io.Serializable;
import java.sql.Timestamp;
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
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;

@Entity(name = "IssueDetailBatchMapper")
@Table(name = "t_issue_detail_batch_mapper", schema = "pharmacy")
public class IssueDetailBatchMapper extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issue_detail_id", nullable = false)
	private IssueDetail issueDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_unit_id")
	private UomUnit uomUnit;

	@Column(name = "issue_quantity")
	private Integer issueQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accepted_by")
	private User acceptedBy;

	@Column(name = "accepted_on", nullable = true)
	private Timestamp acceptedOn;

	@Column(name = "accepted_quantity")
	private Integer acceptedQuantity;
	
	@Column(name = "return_accepted_quantity")
	private Integer returnAcceptedQuantity;
	
	@Column(name = "return_rejected_quantity")
	private Integer returnRejectedQuantity;

	public Integer getReturnRejectedQuantity() {
		return returnRejectedQuantity;
	}

	public void setReturnRejectedQuantity(Integer returnRejectedQuantity) {
		this.returnRejectedQuantity = returnRejectedQuantity;
	}

	public Integer getReturnAcceptedQuantity() {
		return returnAcceptedQuantity;
	}

	public void setReturnAcceptedQuantity(Integer returnAcceptedQuantity) {
		this.returnAcceptedQuantity = returnAcceptedQuantity;
	}
	@Column(name = "rejected_quantity")
	private Integer rejectedQuantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reject_reason_id")
	private RejectReason rejectReason;

	@Column(name = "reject_note")
	private String rejectNote;

	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "is_return_acceptance")
	private Boolean isReturnAcceptance;
	
	public Boolean getIsReturnAcceptance() {
		return isReturnAcceptance;
	}

	public void setIsReturnAcceptance(Boolean isReturnAcceptance) {
		this.isReturnAcceptance = isReturnAcceptance;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "issueDetailBatchMapper", cascade = CascadeType.ALL)
	private List<PatientIssueReturnDetail> patientIssueReturnDetail;
	
	
	@Column(name = "rejection_accepted_quantity")
	private Integer rejectionAcceptedQuantity;
	
	@Column(name = "rejection_rejected_quantity")
	private Integer rejectionRejectedQuantity;
	
	@Column(name = "rejected_rejection_accepted_quantity")
	private Integer rejectedRejectionAcceptedQuantity;
	
	@Column(name = "rejected_rejection_rejected_quantity")
	private Integer rejectedRejectionRejectedQuantity;

	public Integer getRejectedRejectionAcceptedQuantity() {
		return rejectedRejectionAcceptedQuantity;
	}

	public void setRejectedRejectionAcceptedQuantity(Integer rejectedRejectionAcceptedQuantity) {
		this.rejectedRejectionAcceptedQuantity = rejectedRejectionAcceptedQuantity;
	}

	public Integer getRejectedRejectionRejectedQuantity() {
		return rejectedRejectionRejectedQuantity;
	}

	public void setRejectedRejectionRejectedQuantity(Integer rejectedRejectionRejectedQuantity) {
		this.rejectedRejectionRejectedQuantity = rejectedRejectionRejectedQuantity;
	}

	public Integer getRejectionAcceptedQuantity() {
		return rejectionAcceptedQuantity;
	}

	public void setRejectionAcceptedQuantity(Integer rejectionAcceptedQuantity) {
		this.rejectionAcceptedQuantity = rejectionAcceptedQuantity;
	}

	public Integer getRejectionRejectedQuantity() {
		return rejectionRejectedQuantity;
	}

	public void setRejectionRejectedQuantity(Integer rejectionRejectedQuantity) {
		this.rejectionRejectedQuantity = rejectionRejectedQuantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public IssueDetail getIssueDetail() {
		return issueDetail;
	}

	public void setIssueDetail(IssueDetail issueDetail) {
		this.issueDetail = issueDetail;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}

	public Integer getIssueQuantity() {
		return issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public User getAcceptedBy() {
		return acceptedBy;
	}

	public void setAcceptedBy(User acceptedBy) {
		this.acceptedBy = acceptedBy;
	}

	public Timestamp getAcceptedOn() {
		return acceptedOn;
	}

	public void setAcceptedOn(Timestamp acceptedOn) {
		this.acceptedOn = acceptedOn;
	}

	public Integer getAcceptedQuantity() {
		return acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getRejectedQuantity() {
		return rejectedQuantity==null?0:rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity==null?0:rejectedQuantity;
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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public IssueDetailBatchMapper() {
		super();
	}
	
	

	public List<PatientIssueReturnDetail> getPatientIssueReturnDetail() {
		return patientIssueReturnDetail;
	}

	public void setPatientIssueReturnDetail(List<PatientIssueReturnDetail> patientIssueReturnDetail) {
		this.patientIssueReturnDetail = patientIssueReturnDetail;
	}

	public IssueDetailBatchMapper(IssueDetail issueDetail, Batch batch, UomType uomType, UomUnit uomUnit,
			Integer issueQuantity, Integer acceptedQuantity, Double amount) {
		super();
		this.issueDetail = issueDetail;
		this.batch = batch;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.issueQuantity = issueQuantity;
		this.acceptedQuantity = acceptedQuantity;
		this.amount = amount;
	}

	public IssueDetailBatchMapper(Batch batch, UomType uomType, UomUnit uomUnit, Integer issueQuantity, Double amount) {
		super();
		this.batch = batch;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.issueQuantity = issueQuantity;
		this.amount = amount;
	}

	public IssueDetailBatchMapper(Batch batch,Integer issueQuantity) {
		this.batch = batch;
		this.issueQuantity = issueQuantity;
	}
	public IssueDetailBatchMapper(Integer id) {
		super();
		this.id = id;
	}

	public void updateIssueDetailBatchMapper(Integer id,Integer acceptedQuantity, String rejectNote,
			Integer rejectedQuantity, RejectReason rejectReason) {
		this.id = id;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectNote = rejectNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
	}
	public void updateIssueDetailBatchMapper1(Integer id,Integer acceptedQuantity, String rejectNote,
			Integer rejectedQuantity, RejectReason rejectReason) {
		this.id = id;
		this.rejectedRejectionAcceptedQuantity = acceptedQuantity;
		this.rejectNote = rejectNote;
		this.rejectedRejectionRejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
	}
	
	 
	public void updateIssueDetailBatchMapper(Integer id, String rejectNote, RejectReason rejectReason) {
		this.id = id;
		this.rejectNote = rejectNote;
		this.rejectReason = rejectReason;
	}
	public void updateIssueDetailBatchMapper(Integer acceptedQuantity, String rejectNote,
			Integer rejectedQuantity, RejectReason rejectReason) {

		this.acceptedQuantity = acceptedQuantity;
		this.rejectNote = rejectNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
	}
	
	public void updateIssueDetailBatchMapper1(Integer acceptedQuantity, String rejectNote,
			Integer rejectedQuantity, RejectReason rejectReason) {

		this.returnAcceptedQuantity = acceptedQuantity;
		this.rejectNote = rejectNote;
		this.returnRejectedQuantity = rejectedQuantity;
		this.rejectReason = rejectReason;
	}
	
	public void updateIssueDetailBatchMapper( String rejectNote, RejectReason rejectReason) {
 
		this.rejectNote = rejectNote; 
		this.rejectReason = rejectReason;
	}

}