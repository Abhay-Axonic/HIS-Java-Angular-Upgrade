package com.param.entity.model.linenAndLaundry;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_washing_request_detail database table.
 * 
 */
@Entity
@Table(name="t_washing_request_detail", schema="linen_and_laundry")
public class WashingRequestDetail implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Timestamp addedDate;

	@Column(name="approved_contaminated_quantity")
	private Integer approvedContaminatedQuantity;

	@Column(name="approved_dirty_linen_quantity")
	private Integer approvedDirtyLinenQuantity;

	@Column(name="batch_id")
	private Integer batchId;

	@Column(name="contaminated_linen_quantity")
	private Integer contaminatedLinenQuantity;

	@Column(name="dirt_linen_quantity")
	private Integer dirtLinenQuantity;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_deleted")
	private Boolean isDeleted;

	@Column(name="organization_id")
	private Integer organizationId;

	@Column(name="reject_note")
	private String rejectNote;

	@Column(name="reject_reason_id")
	private Integer rejectReasonId;

	@Column(name="rejected_contaminated_quantity")
	private Integer rejectedContaminatedQuantity;

	@Column(name="rejected_dirty_linen_quantity")
	private Integer rejectedDirtyLinenQuantity;

	@Column(name="status_id")
	private Integer statusId;

	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@OneToMany(mappedBy="washingRequestDetail")
	private List<IssueDirtyLinenDetail> TIssueDirtyLinenDetails;

	@ManyToOne
	@JoinColumn(name="washing_request_id")
	private WashingRequest washingRequest;

	public WashingRequestDetail() {
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

	public Integer getApprovedContaminatedQuantity() {
		return this.approvedContaminatedQuantity;
	}

	public void setApprovedContaminatedQuantity(Integer approvedContaminatedQuantity) {
		this.approvedContaminatedQuantity = approvedContaminatedQuantity;
	}

	public Integer getApprovedDirtyLinenQuantity() {
		return this.approvedDirtyLinenQuantity;
	}

	public void setApprovedDirtyLinenQuantity(Integer approvedDirtyLinenQuantity) {
		this.approvedDirtyLinenQuantity = approvedDirtyLinenQuantity;
	}

	public Integer getBatchId() {
		return this.batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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

	public Integer getRejectedContaminatedQuantity() {
		return this.rejectedContaminatedQuantity;
	}

	public void setRejectedContaminatedQuantity(Integer rejectedContaminatedQuantity) {
		this.rejectedContaminatedQuantity = rejectedContaminatedQuantity;
	}

	public Integer getRejectedDirtyLinenQuantity() {
		return this.rejectedDirtyLinenQuantity;
	}

	public void setRejectedDirtyLinenQuantity(Integer rejectedDirtyLinenQuantity) {
		this.rejectedDirtyLinenQuantity = rejectedDirtyLinenQuantity;
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

	public List<IssueDirtyLinenDetail> getTIssueDirtyLinenDetails() {
		return this.TIssueDirtyLinenDetails;
	}

	public void setTIssueDirtyLinenDetails(List<IssueDirtyLinenDetail> TIssueDirtyLinenDetails) {
		this.TIssueDirtyLinenDetails = TIssueDirtyLinenDetails;
	}

	public IssueDirtyLinenDetail addIssueDirtyLinenDetail(IssueDirtyLinenDetail issueDirtyLinenDetail) {
		getTIssueDirtyLinenDetails().add(issueDirtyLinenDetail);
		issueDirtyLinenDetail.setWashingRequestDetail(this);

		return issueDirtyLinenDetail;
	}

	public IssueDirtyLinenDetail removeIssueDirtyLinenDetail(IssueDirtyLinenDetail issueDirtyLinenDetail) {
		getTIssueDirtyLinenDetails().remove(issueDirtyLinenDetail);
		issueDirtyLinenDetail.setWashingRequestDetail(null);

		return issueDirtyLinenDetail;
	}

	public WashingRequest getWashingRequest() {
		return washingRequest;
	}

	public void setWashingRequest(WashingRequest washingRequest) {
		this.washingRequest = washingRequest;
	}

}