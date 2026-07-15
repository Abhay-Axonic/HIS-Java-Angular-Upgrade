package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_blood_component_separation_deatils", schema = "blood_bank")
@SequenceGenerator(name = "blood_component_separation_deatils_seq", sequenceName = "blood_bank.blood_component_separation_deatils_seq", allocationSize = 1)
public class BloodComponentSeparationDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blood_component_separation_deatils_seq")
	@Column(name = "blood_component_separation_deatils_id")
	private Integer bloodComponentSeparationDeatilsId;
	
	@Column(name = "blood_component_separation_id")
	private Integer bloodComponentSeparationId;
	
	@Column(name = "component_id")
	private Integer componentId;
	
	@Column(name = "blood_bag_no")
	private String bloodBagNo;
	
	@Column(name = "bag_weight")
	private Integer bagWeight;
	
	@Column(name = "remark")
	private String remark;
	
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

	public Integer getBloodComponentSeparationDeatilsId() {
		return bloodComponentSeparationDeatilsId;
	}

	public void setBloodComponentSeparationDeatilsId(Integer bloodComponentSeparationDeatilsId) {
		this.bloodComponentSeparationDeatilsId = bloodComponentSeparationDeatilsId;
	}

	public Integer getBloodComponentSeparationId() {
		return bloodComponentSeparationId;
	}

	public void setBloodComponentSeparationId(Integer bloodComponentSeparationId) {
		this.bloodComponentSeparationId = bloodComponentSeparationId;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public String getBloodBagNo() {
		return bloodBagNo;
	}

	public void setBloodBagNo(String bloodBagNo) {
		this.bloodBagNo = bloodBagNo;
	}

	public Integer getBagWeight() {
		return bagWeight;
	}

	public void setBagWeight(Integer bagWeight) {
		this.bagWeight = bagWeight;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}
