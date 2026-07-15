package com.param.procurement.common.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
abstract public class AbstractEntity {

	@Column(name = "is_deleted")
	@JsonIgnore
	protected Boolean isDeleted;

	@Column(name = "added_by")
	@JsonIgnore
	protected Long addedBy;

	@Column(name = "added_date")
	@JsonIgnore
	protected Date addedDate;

	@Column(name = "updated_by")
	@JsonIgnore
	protected Long updatedBy;

	@Column(name = "updated_date")
	@JsonIgnore
	protected Date updatedDate;

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean deleted) {
		isDeleted = deleted;
	}

	public Long getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Long addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
