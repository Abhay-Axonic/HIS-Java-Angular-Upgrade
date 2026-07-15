package com.param.entity.model.linenAndLaundry;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the t_converted_condemnation database table.
 * 
 */
@Entity
@Table(name="t_converted_condemnation", schema="linen_and_laundry")
public class ConvertedCondemnation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="added_by")
	private Integer addedBy;

	@Column(name="added_date")
	private Timestamp addedDate;

	@Column(name="is_active")
	private Boolean isActive;

	@Column(name="is_deleted")
	private Boolean isDeleted;

	@Column(name="item_id")
	private Integer itemId;

	@Column(name="organization_id")
	private Integer organizationId;

	private Integer quantity;

	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Timestamp updatedDate;

	@ManyToOne
	@JoinColumn(name="send_to_condemnation_detail_id")
	private SendToCondemnateDetail sendToCondemnateDetail;

	public ConvertedCondemnation() {
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

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public SendToCondemnateDetail getSendToCondemnateDetail() {
		return sendToCondemnateDetail;
	}

	public void setSendToCondemnateDetail(SendToCondemnateDetail sendToCondemnateDetail) {
		this.sendToCondemnateDetail = sendToCondemnateDetail;
	}

}