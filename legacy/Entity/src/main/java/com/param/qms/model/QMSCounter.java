package com.param.qms.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="m_qms_counter",schema="pharmacy")
@SequenceGenerator(name="m_qms_counter_seq",sequenceName="pharmacy.m_qms_counter_seq",allocationSize=1)
public class QMSCounter {

	@Id
	@Column(name="qms_counter_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_qms_counter_seq")
	private Integer qmsCounterId;
	
	@Column(name="qms_counter_code")
	private String qmsCounterCode;
	
	@Column(name="qms_counter_desc")
	private String qmsCounterDesc;
	
	@Column(name="status")
	private Character status ;
	
	@Column(name="unit_id")
	private Integer unitId ;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_by")
	private Integer createdBy ;
	
	@Column(name="updated_by")
	private Integer updatedBy ;
	
	@Column(name="created_date")
	private Date createdDate ;
	
	@Column(name="updated_date")
	private Date updatedDate;

	public Integer getQmsCounterId() {
		return qmsCounterId;
	}

	public void setQmsCounterId(Integer qmsCounterId) {
		this.qmsCounterId = qmsCounterId;
	}

	public String getQmsCounterCode() {
		return qmsCounterCode;
	}

	public void setQmsCounterCode(String qmsCounterCode) {
		this.qmsCounterCode = qmsCounterCode;
	}

	public String getQmsCounterDesc() {
		return qmsCounterDesc;
	}

	public void setQmsCounterDesc(String qmsCounterDesc) {
		this.qmsCounterDesc = qmsCounterDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
