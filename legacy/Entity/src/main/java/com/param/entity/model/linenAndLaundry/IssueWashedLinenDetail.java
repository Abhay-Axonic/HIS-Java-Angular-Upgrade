package com.param.entity.model.linenAndLaundry;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the t_issue_washed_linen_detail database table.
 * 
 */
@Entity
@Table(name="t_issue_washed_linen_detail", schema="linen_and_laundry")
public class IssueWashedLinenDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="accepted_quantity")
	private Integer acceptedQuantity;

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Timestamp addedDate;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_deleted")
	private Boolean isDeleted;

	@Column(name="issue_quantity")
	private Integer issueQuantity;

	@Column(name="item_id")
	private Integer itemId;

	@Column(name="organization_id")
	private Integer organizationId;

	@Column(name="reject_note")
	private String rejectNote;

	@Column(name="reject_reason_id")
	private Integer rejectReasonId;

	@Column(name="rejected_quantity")
	private Integer rejectedQuantity;

	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@ManyToOne
	@JoinColumn(name="issue_dirty_linen_detail_id")
	private IssueDirtyLinenDetail issueDirtyLinenDetail;

	@ManyToOne
	@JoinColumn(name="issue_washed_linen_id")
	private IssueWashedLinen issueWashedLinen;

	public IssueWashedLinenDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAcceptedQuantity() {
		return this.acceptedQuantity;
	}

	public void setAcceptedQuantity(Integer acceptedQuantity) {
		this.acceptedQuantity = acceptedQuantity;
	}

	public Integer getAddedBy() {
		return this.addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Timestamp getAddedDate() {
		return this.addedDate;
	}

	public void setAddedDate(Timestamp addedDate) {
		this.addedDate = addedDate;
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

	public Integer getItemId() {
		return this.itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrganizationId() {
		return this.organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getRejectNote() {
		return this.rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectReasonId() {
		return this.rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getRejectedQuantity() {
		return this.rejectedQuantity;
	}

	public void setRejectedQuantity(Integer rejectedQuantity) {
		this.rejectedQuantity = rejectedQuantity;
	}

	public Integer getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}

	public IssueDirtyLinenDetail getIssueDirtyLinenDetail() {
		return issueDirtyLinenDetail;
	}

	public void setIssueDirtyLinenDetail(IssueDirtyLinenDetail issueDirtyLinenDetail) {
		this.issueDirtyLinenDetail = issueDirtyLinenDetail;
	}

	public IssueWashedLinen getIssueWashedLinen() {
		return issueWashedLinen;
	}

	public void setIssueWashedLinen(IssueWashedLinen issueWashedLinen) {
		this.issueWashedLinen = issueWashedLinen;
	}

}