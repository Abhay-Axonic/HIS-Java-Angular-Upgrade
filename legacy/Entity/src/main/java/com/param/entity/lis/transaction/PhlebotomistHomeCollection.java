package com.param.entity.lis.transaction;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@Entity
@Table(name = "t_phlebotomist_home_collection", schema = "lab")
@SequenceGenerator(name = "phlebotomist_home_collection_seq", sequenceName = "lab.phlebotomist_home_collection_seq", allocationSize = 1)
public class PhlebotomistHomeCollection {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phlebotomist_home_collection_seq")
	@Column(name = "phlebotomist_home_collection_id")
	private Integer phlebotomistHomeCollectionId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "phlebotomist_collection_date")
	private Date  phlebotomistCollectionDate;
	
	@Column(name="from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name="to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "status_id")
	private Integer statusId;

	public Integer getPhlebotomistHomeCollectionId() {
		return phlebotomistHomeCollectionId;
	}

	public void setPhlebotomistHomeCollectionId(Integer phlebotomistHomeCollectionId) {
		this.phlebotomistHomeCollectionId = phlebotomistHomeCollectionId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public Date getPhlebotomistCollectionDate() {
		return phlebotomistCollectionDate;
	}

	public void setPhlebotomistCollectionDate(Date phlebotomistCollectionDate) {
		this.phlebotomistCollectionDate = phlebotomistCollectionDate;
	}

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(Instant toTime) {
		this.toTime = toTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	
}
