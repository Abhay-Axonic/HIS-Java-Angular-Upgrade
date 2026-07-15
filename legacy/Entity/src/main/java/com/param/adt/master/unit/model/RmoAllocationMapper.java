package com.param.adt.master.unit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.DoctorMaster;
  @NamedQueries({      
@NamedQuery(name="GET_DOCTOR_LIST_BY_RMO_ALLOCATION_ID",
query="SELECT rmoAllocationMapper.doctorId AS doctorId,rmoAllocationMapper.rmoAllocationMapperId AS rmoAllocationMapperId,"
		+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as doctorName "
		+ " FROM RmoAllocationMapper rmoAllocationMapper "
		+ " INNER JOIN rmoAllocationMapper.doctorMaster doctorMaster"
		+ " WHERE rmoAllocationMapper.organizationId=:orgId"
		+ " AND rmoAllocationMapper.unitId=:unitId "
		+ " AND rmoAllocationMapper.rmoAllocationId =:rmoAllocationId and rmoAllocationMapper.isDeleted='N' "),

@NamedQuery(name="GET_WARD_LIST_BY_RMO_ALLOCATION_ID",
query="SELECT rmoAllocationMapper.wardId AS wardId,rmoAllocationMapper.rmoAllocationMapperId AS rmoAllocationMapperId,"
		+ " wardMaster.wardName as wardName "
		+ " FROM RmoAllocationMapper rmoAllocationMapper "
		+ " INNER JOIN rmoAllocationMapper.wardMaster wardMaster"
		+ " WHERE rmoAllocationMapper.organizationId=:orgId"
		+ " AND rmoAllocationMapper.unitId=:unitId "
		+ " AND rmoAllocationMapper.rmoAllocationId =:rmoAllocationId and rmoAllocationMapper.isDeleted='N' "
		+ " ORDER BY wardMaster.wardName ASC ")


  })
  
@Entity
@Table(name = "m_rmo_allocation_mapper", schema = "public")
@SequenceGenerator(name = "m_rmo_allocation_mapper_seq", sequenceName = "public.m_rmo_allocation_mapper_seq", allocationSize = 1)
public class RmoAllocationMapper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_rmo_allocation_mapper_seq")
	@Column(name = "rmo_allocation_mapper_id")
	private Integer rmoAllocationMapperId;
	
	@Column(name = "rmo_allocation_id")
	private Integer rmoAllocationId;
	
	@Column(name = "doctor_id")
	private Integer doctorId; 
	
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
	
	@Column(name = "is_deleted")
	private Character isDeleted;

	
	@Column(name = "ward_id")
	private Integer wardId;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="doctor_id", insertable=false, updatable=false)
	private DoctorMaster doctorMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ward_id", insertable=false, updatable=false)
	private WardMaster wardMaster;
	
	public Integer getRmoAllocationMapperId() {
		return rmoAllocationMapperId;
	}

	public void setRmoAllocationMapperId(Integer rmoAllocationMapperId) {
		this.rmoAllocationMapperId = rmoAllocationMapperId;
	}

	public Integer getRmoAllocationId() {
		return rmoAllocationId;
	}

	public void setRmoAllocationId(Integer rmoAllocationId) {
		this.rmoAllocationId = rmoAllocationId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public WardMaster getWardMaster() {
		return wardMaster;
	}

	public void setWardMaster(WardMaster wardMaster) {
		this.wardMaster = wardMaster;
	}
	
	
	
}
