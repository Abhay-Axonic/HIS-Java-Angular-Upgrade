package com.param.entity.model.linenAndLaundry;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.RejectReason;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_linen_issue_batch_mapper database table.
 * 
 */
@Entity
@Table(name="t_linen_issue_batch_mapper", schema="linen_and_laundry")
public class LinenIssueBatchMapper extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@Column(name="accepted_by")
	private Integer acceptedBy;

	@Column(name="accepted_on")
	private Timestamp acceptedOn;

	@Column(name="accepted_quantity")
	private Integer acceptedQuantity;

	@ManyToOne
	@JoinColumn(name="batch_id")
	private Batch batch;

	@Column(name="issue_quantity")
	private Integer issueQuantity;

	@Column(name="rejected_note")
	private String rejectedNote;

	@Column(name="rejected_quantity")
	private Integer rejectedQuantity;

	@ManyToOne
	@JoinColumn(name="rejected_reason_id")
	private RejectReason rejectedReason;

	@ManyToOne
	@JoinColumn(name="linen_issue_detail_id")
	private LinenIssueDetail linenIssueDetail;

	@OneToMany(mappedBy="linenIssueBatchMapper", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RejectedAcceptanceDetail> rejectedAcceptanceDetails;

	public LinenIssueBatchMapper() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAcceptedBy() {
		return this.acceptedBy;
	}

	public void setAcceptedBy(Integer acceptedBy) {
		this.acceptedBy = acceptedBy;
	}

	public Timestamp getAcceptedOn() {
		return this.acceptedOn;
	}

	public void setAcceptedOn(Timestamp acceptedOn) {
		this.acceptedOn = acceptedOn;
	}

	public Integer getAcceptedQuantity() {
		return this.acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public Boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getIssueQuantity() {
		return this.issueQuantity;
	}

	public void setIssueQuantity(Integer issueQuantity) {
		this.issueQuantity = issueQuantity;
	}

	public String getRejectedNote() {
		return this.rejectedNote;
	}

	public void setRejectedNote(String rejectedNote) {
		this.rejectedNote = rejectedNote;
	}

	public Integer getRejectedQuantity() {
		return this.rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public RejectReason getRejectedReason() {
		return rejectedReason;
	}

	public void setRejectedReason(RejectReason rejectedReason) {
		this.rejectedReason = rejectedReason;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public LinenIssueDetail getLinenIssueDetail() {
		return linenIssueDetail;
	}

	public void setLinenIssueDetail(LinenIssueDetail linenIssueDetail) {
		this.linenIssueDetail = linenIssueDetail;
	}

	public List<RejectedAcceptanceDetail> getRejectedAcceptanceDetails() {
		return rejectedAcceptanceDetails;
	}

	public void setRejectedAcceptanceDetails(List<RejectedAcceptanceDetail> rejectedAcceptanceDetails) {
		this.rejectedAcceptanceDetails = rejectedAcceptanceDetails;
	}

	public LinenIssueBatchMapper(Batch batch, Integer issueQuantity) {
		super();
		this.batch = batch;
		this.issueQuantity = issueQuantity;
	}

	public RejectedAcceptanceDetail addRejectedAcceptanceDetail(RejectedAcceptanceDetail rejectedAcceptanceDetail) {
		getRejectedAcceptanceDetails().add(rejectedAcceptanceDetail);
		rejectedAcceptanceDetail.setLinenIssueBatchMapper(this);

		return rejectedAcceptanceDetail;
	}

	public RejectedAcceptanceDetail removeRejectedAcceptanceDetail(RejectedAcceptanceDetail rejectedAcceptanceDetail) {
		getRejectedAcceptanceDetails().remove(rejectedAcceptanceDetail);
		rejectedAcceptanceDetail.setLinenIssueBatchMapper(null);

		return rejectedAcceptanceDetail;
	}

	public void updateLinenIssueBatchMapper(Integer id, Integer acceptedQuantity, String rejectedNote, Integer rejectedQuantity,
			RejectReason rejectedReason) {
		this.id = id;
		this.acceptedQuantity = acceptedQuantity;
		this.rejectedNote = rejectedNote;
		this.rejectedQuantity = rejectedQuantity;
		this.rejectedReason = rejectedReason;
	}

	public LinenIssueBatchMapper(Integer id) {
		super();
		this.id = id;
	}
	
}