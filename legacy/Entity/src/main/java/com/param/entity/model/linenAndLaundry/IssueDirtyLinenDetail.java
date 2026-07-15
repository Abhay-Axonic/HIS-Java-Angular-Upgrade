package com.param.entity.model.linenAndLaundry;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_issue_dirty_linen_detail database table.
 * 
 */
@Entity
@Table(name="t_issue_dirty_linen_detail", schema="linen_and_laundry")
public class IssueDirtyLinenDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Timestamp addedDate;

	@Column(name="approved_contaminated_linen_quantity")
	private Integer approvedContaminatedLinenQuantity;

	@Column(name="approved_dirt_linen_quantity")
	private Integer approvedDirtLinenQuantity;

	@Column(name="contaminated_linen_quantity")
	private Integer contaminatedLinenQuantity;

	@Column(name="dirt_linen_quantity")
	private Integer dirtLinenQuantity;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_deleted")
	private Boolean isDeleted;

	@Column(name="item_id")
	private Integer itemId;

	@Column(name="organization_id")
	private Integer organizationId;

	@Column(name="reject_note")
	private String rejectNote;

	@Column(name="reject_reason_id")
	private Integer rejectReasonId;

	@Column(name="rejected_contaminated_linen_quantity")
	private Integer rejectedContaminatedLinenQuantity;

	@Column(name="rejected_dirt_linen_quantity")
	private Integer rejectedDirtLinenQuantity;

	@Column(name="status_id")
	private Integer statusId;

	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@ManyToOne
	@JoinColumn(name="issue_dirty_linen_id")
	private IssueDirtyLinen issueDirtyLinen;

	@ManyToOne
	@JoinColumn(name="washing_request_detail_id")
	private WashingRequestDetail washingRequestDetail;

	@OneToMany(mappedBy="issueDirtyLinenDetail")
	private List<IssueWashedLinenDetail> issueWashedLinenDetails;

	public IssueDirtyLinenDetail() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getApprovedContaminatedLinenQuantity() {
		return this.approvedContaminatedLinenQuantity;
	}

	public void setApprovedContaminatedLinenQuantity(Integer approvedContaminatedLinenQuantity) {
		this.approvedContaminatedLinenQuantity = approvedContaminatedLinenQuantity;
	}

	public Integer getApprovedDirtLinenQuantity() {
		return this.approvedDirtLinenQuantity;
	}

	public void setApprovedDirtLinenQuantity(Integer approvedDirtLinenQuantity) {
		this.approvedDirtLinenQuantity = approvedDirtLinenQuantity;
	}

	public Integer getContaminatedLinenQuantity() {
		return this.contaminatedLinenQuantity;
	}

	public void setContaminatedLinenQuantity(Integer contaminatedLinenQuantity) {
		this.contaminatedLinenQuantity = contaminatedLinenQuantity;
	}

	public Integer getDirtLinenQuantity() {
		return this.dirtLinenQuantity;
	}

	public void setDirtLinenQuantity(Integer dirtLinenQuantity) {
		this.dirtLinenQuantity = dirtLinenQuantity;
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

	public Integer getRejectedContaminatedLinenQuantity() {
		return this.rejectedContaminatedLinenQuantity;
	}

	public void setRejectedContaminatedLinenQuantity(Integer rejectedContaminatedLinenQuantity) {
		this.rejectedContaminatedLinenQuantity = rejectedContaminatedLinenQuantity;
	}

	public Integer getRejectedDirtLinenQuantity() {
		return this.rejectedDirtLinenQuantity;
	}

	public void setRejectedDirtLinenQuantity(Integer rejectedDirtLinenQuantity) {
		this.rejectedDirtLinenQuantity = rejectedDirtLinenQuantity;
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
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

	public IssueDirtyLinen getIssueDirtyLinen() {
		return issueDirtyLinen;
	}

	public void setIssueDirtyLinen(IssueDirtyLinen issueDirtyLinen) {
		this.issueDirtyLinen = issueDirtyLinen;
	}

	public WashingRequestDetail getWashingRequestDetail() {
		return washingRequestDetail;
	}

	public void setWashingRequestDetail(WashingRequestDetail washingRequestDetail) {
		this.washingRequestDetail = washingRequestDetail;
	}

	public List<IssueWashedLinenDetail> getIssueWashedLinenDetails() {
		return issueWashedLinenDetails;
	}

	public void setIssueWashedLinenDetails(List<IssueWashedLinenDetail> issueWashedLinenDetails) {
		this.issueWashedLinenDetails = issueWashedLinenDetails;
	}

	public IssueWashedLinenDetail addIssueWashedLinenDetail(IssueWashedLinenDetail issueWashedLinenDetail) {
		getIssueWashedLinenDetails().add(issueWashedLinenDetail);
		issueWashedLinenDetail.setIssueDirtyLinenDetail(this);

		return issueWashedLinenDetail;
	}

	public IssueWashedLinenDetail removeIssueWashedLinenDetail(IssueWashedLinenDetail issueWashedLinenDetail) {
		getIssueWashedLinenDetails().remove(issueWashedLinenDetail);
		issueWashedLinenDetail.setIssueDirtyLinenDetail(null);

		return issueWashedLinenDetail;
	}

}