package com.param.entity.model.linenAndLaundry;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_send_to_condemnate_detail database table.
 * 
 */
@Entity
@Table(name="t_send_to_condemnate_detail", schema="linen_and_laundry")
public class SendToCondemnateDetail implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="accepted_quantity")
	private Integer acceptedQuantity;

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Timestamp addedDate;

	@Column(name="batch_id")
	private Integer batchId;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_deleted")
	private Boolean isDeleted;

	@Column(name="organization_id")
	private Integer organizationId;

	private Integer quantity;

	@Column(name="reject_note")
	private String rejectNote;

	@Column(name="reject_quantity")
	private Integer rejectQuantity;

	@Column(name="reject_reason_id")
	private Integer rejectReasonId;

	@Column(name="scrap_quantity")
	private Integer scrapQuantity;

	@Column(name="status_id")
	private Integer statusId;

	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sendToCondemnateDetail", cascade = CascadeType.ALL)
	private List<ConvertedCondemnation> convertedCondemnations;

	@ManyToOne
	@JoinColumn(name="send_to_condemnate_id")
	private SendToCondemnate sendToCondemnate;

	public SendToCondemnateDetail() {
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

	public Integer getBatchId() {
		return this.batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
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

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getRejectNote() {
		return this.rejectNote;
	}

	public void setRejectNote(String rejectNote) {
		this.rejectNote = rejectNote;
	}

	public Integer getRejectQuantity() {
		return this.rejectQuantity;
	}

	public void setRejectQuantity(Integer rejectQuantity) {
		this.rejectQuantity = rejectQuantity;
	}

	public Integer getRejectReasonId() {
		return this.rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public Integer getScrapQuantity() {
		return this.scrapQuantity;
	}

	public void setScrapQuantity(Integer scrapQuantity) {
		this.scrapQuantity = scrapQuantity;
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

	public ConvertedCondemnation addTConvertedCondemnation(ConvertedCondemnation convertedCondemnation) {
		getConvertedCondemnations().add(convertedCondemnation);
		convertedCondemnation.setSendToCondemnateDetail(this);

		return convertedCondemnation;
	}

	public ConvertedCondemnation removeTConvertedCondemnation(ConvertedCondemnation convertedCondemnation) {
		getConvertedCondemnations().remove(convertedCondemnation);
		convertedCondemnation.setSendToCondemnateDetail(null);

		return convertedCondemnation;
	}

	public List<ConvertedCondemnation> getConvertedCondemnations() {
		return convertedCondemnations;
	}

	public void setConvertedCondemnations(List<ConvertedCondemnation> convertedCondemnations) {
		this.convertedCondemnations = convertedCondemnations;
	}

	public SendToCondemnate getSendToCondemnate() {
		return sendToCondemnate;
	}

	public void setSendToCondemnate(SendToCondemnate sendToCondemnate) {
		this.sendToCondemnate = sendToCondemnate;
	}

}