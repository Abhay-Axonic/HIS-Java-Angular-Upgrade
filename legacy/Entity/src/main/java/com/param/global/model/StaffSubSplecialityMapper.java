package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_staff_sub_speciality_mapper", schema = "doctor")
@SequenceGenerator(name="staff_sub_speciality_mapper_seq", sequenceName="doctor.staff_sub_speciality_mapper_seq", allocationSize=1)
public class StaffSubSplecialityMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_sub_speciality_mapper_seq")
	@Column(name="staff_sub_speciality_id")
	private Integer staffSubSpecialityId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name="sub_speciality_id")
	private Integer subSpecialityId;
	
	@Column(name="status")
	private Character status;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;



	public Integer getStaffSubSpecialityId() {
		return staffSubSpecialityId;
	}

	public void setStaffSubSpecialityId(Integer staffSubSpecialityId) {
		this.staffSubSpecialityId = staffSubSpecialityId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
	
	
}
