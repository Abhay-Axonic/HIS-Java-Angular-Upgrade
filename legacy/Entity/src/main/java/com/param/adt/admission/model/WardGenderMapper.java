package com.param.adt.admission.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.unit.model.WardMaster;
import com.param.global.model.GenderMaster;

@Entity
@Table(name="t_ward_gender_mapper",schema="adt")
@SequenceGenerator(name="ward_gender_mapper_seq",sequenceName="adt.ward_gender_mapper_seq",allocationSize=1)
public class WardGenderMapper {

	@Id
	@Column(name="ward_gender_mapper_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="ward_gender_mapper_seq")
	private Integer wardGenderMapperId;
	
	@Column(name="ward_id")
	private Integer wardId;
	
	@Column(name="gender_id")
	private Integer genderId;
	
	@Column(name="status")
	private Character status;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="updated_date")
	private Date updatedDate;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster; 
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ward_id", insertable = false, updatable = false)
	private WardMaster wardMaster; 
	
	
	public Integer getWardGenderMapperId() {
		return wardGenderMapperId;
	}

	public void setWardGenderMapperId(Integer wardGenderMapperId) {
		this.wardGenderMapperId = wardGenderMapperId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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