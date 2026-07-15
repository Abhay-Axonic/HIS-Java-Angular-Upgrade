package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_unit_subscription_check", schema = "public")
@SequenceGenerator(name = "unit_subscription_check_seq", sequenceName = "public.unit_subscription_check_seq", allocationSize = 1)
public class UnitSubscriptionCheck {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_subscription_check_seq")
	@Column(name = "id")
	private Integer id;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="failed_count")
	private Integer failedCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}
	
	
}
