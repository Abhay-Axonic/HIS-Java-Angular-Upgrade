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
@Table(name = "t_doctor_sub_speciality_mapper", schema = "doctor")
@SequenceGenerator(name="doctor_sub_speciality_mapper_seq", sequenceName="doctor.doctor_sub_speciality_mapper_seq", allocationSize=1)
public class DoctorSubSplecialityMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_sub_speciality_mapper_seq")
	@Column(name="doctor_sub_speciality_id")
	private Integer doctorSubSpecialityId;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
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

	public Integer getDoctorSubSpecialityId() {
		return doctorSubSpecialityId;
	}

	public void setDoctorSubSpecialityId(Integer doctorSubSpecialityId) {
		this.doctorSubSpecialityId = doctorSubSpecialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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
