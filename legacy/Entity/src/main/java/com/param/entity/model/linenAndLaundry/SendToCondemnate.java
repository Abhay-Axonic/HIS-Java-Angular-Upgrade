package com.param.entity.model.linenAndLaundry;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_send_to_condemnate database table.
 * 
 */
@Entity
@Table(name="t_send_to_condemnate", schema="linen_and_laundry")
public class SendToCondemnate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="accepted_by")
	private Integer acceptedBy;

	@Column(name="accepted_on")
	private Timestamp acceptedOn;

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Timestamp addedDate;

	@Column(name="approval_status_id")
	private Integer approvalStatusId;

	@Column(name="cancel_note")
	private String cancelNote;

	@Column(name="cancel_reason_id")
	private Integer cancelReasonId;

	@Column(name="from_store_id")
	private Integer fromStoreId;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_deleted")
	private Boolean isDeleted;

	@Column(name="organization_id")
	private Integer organizationId;

	@Column(name="priority_id")
	private Integer priorityId;

	private String remark;

	@Column(name="request_no")
	private String requestNo;

	@Column(name="status_id")
	private Integer statusId;

	@Column(name="to_store_id")
	private Integer toStoreId;

	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@OneToMany(mappedBy="sendToCondemnate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<SendToCondemnateDetail> sendToCondemnateDetails;

	public SendToCondemnate() {
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

	public Integer getApprovalStatusId() {
		return this.approvalStatusId;
	}

	public void setApprovalStatusId(Integer approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getCancelNote() {
		return this.cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public Integer getCancelReasonId() {
		return this.cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public Integer getFromStoreId() {
		return this.fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
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

	public Integer getPriorityId() {
		return this.priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequestNo() {
		return this.requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Integer getStatusId() {
		return this.statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getToStoreId() {
		return this.toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
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
	
	public List<SendToCondemnateDetail> getSendToCondemnateDetails() {
		return sendToCondemnateDetails;
	}

	public void setSendToCondemnateDetails(List<SendToCondemnateDetail> sendToCondemnateDetails) {
		this.sendToCondemnateDetails = sendToCondemnateDetails;
	}

	public SendToCondemnateDetail addSendToCondemnateDetail(SendToCondemnateDetail sendToCondemnateDetails) {
		getSendToCondemnateDetails().add(sendToCondemnateDetails);
		sendToCondemnateDetails.setSendToCondemnate(this);

		return sendToCondemnateDetails;
	}

	public SendToCondemnateDetail removeTSendToCondemnateDetail(SendToCondemnateDetail sendToCondemnateDetails) {
		getSendToCondemnateDetails().remove(sendToCondemnateDetails);
		sendToCondemnateDetails.setSendToCondemnate(null);

		return sendToCondemnateDetails;
	}

}