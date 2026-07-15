package com.param.entity.lis.unit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.common.LocalTimeConverter;

@Entity
@Table(name = "m_equipment_id_test_master", schema = "lab")
@SequenceGenerator(name = "m_equipment_id_test_master_seq", sequenceName = "lab.m_equipment_id_test_master_seq", allocationSize = 1)
public class EquipmentIdTestMaster {
	
	
	@Id
	@Column(name = "equipment_id_test")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_equipment_id_test_master_seq")
	private Integer equipmentIdTest;

	@Column(name = "equipment_id")
	private Integer equipmentId;
	
	@Column(name = "test_id")
	private Integer testId;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;


	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "status")
	private Character status;
	

	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getEquipmentIdTest() {
		return equipmentIdTest;
	}

	public void setEquipmentIdTest(Integer equipmentIdTest) {
		this.equipmentIdTest = equipmentIdTest;
	}
	

}
