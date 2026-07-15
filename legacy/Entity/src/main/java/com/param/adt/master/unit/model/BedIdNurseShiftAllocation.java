package com.param.adt.master.unit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_bed_id_nurse_shift_allocation", schema = "public")
@SequenceGenerator(name = "m_bed_id_nurse_shift_allocation_seq", sequenceName = "public.m_bed_id_nurse_shift_allocation_seq", allocationSize = 1)
public class BedIdNurseShiftAllocation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_bed_id_nurse_shift_allocation_seq")
	@Column(name = "bed_nurse_shift_allocation_id")
	private Integer bedNurseShiftAllocationId;
	
	@Column(name = "nurse_shift_allocation_details_id")
	private Integer nurseShiftAllocationDetailsId;
	
	@Column(name = "bed_id")
	private Integer bedId; 

	@Column(name = "org_id")
	private Integer orgId; 
	
	@Column(name = "unit_id")
	private Integer unitId; 

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
	
	@Column(name = "is_bed_assigned")
	private Character isBedAssigned;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@Column(name = "nurse_id")
	private Integer nurseId;
	
	@Column(name = "shift_date")
	private Date shiftDate;

	public Integer getBedNurseShiftAllocationId() {
		return bedNurseShiftAllocationId;
	}

	public Integer getNurseShiftAllocationDetailsId() {
		return nurseShiftAllocationDetailsId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}


	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setBedNurseShiftAllocationId(Integer bedNurseShiftAllocationId) {
		this.bedNurseShiftAllocationId = bedNurseShiftAllocationId;
	}

	public void setNurseShiftAllocationDetailsId(Integer nurseShiftAllocationDetailsId) {
		this.nurseShiftAllocationDetailsId = nurseShiftAllocationDetailsId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getIsBedAssigned() {
		return isBedAssigned;
	}


	public void setIsBedAssigned(Character isBedAssigned) {
		this.isBedAssigned = isBedAssigned;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	

	public Date getShiftDate() {
		return shiftDate;
	}


	public void setShiftDate(Date shiftDate) {
		this.shiftDate = shiftDate;
	}

	public Integer getNurseId() {
		return nurseId;
	}

	public void setNurseId(Integer nurseId) {
		this.nurseId = nurseId;
	}

	
	
}
