package com.param.adt.master.unit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.RoleMaster;
import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.CabinMaster;
import com.param.global.model.DoctorMaster;
import com.param.global.model.EmployeeMaster;
import com.param.global.model.SpecialityMaster;
import com.param.global.org.common.model.DoseMaster;
         
@NamedQueries({
	@NamedQuery(name="GET_RMO_ALLOCATION_MASTER_BY_ID",
			query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
			+ " rmoAllocationMaster.visitTypeId AS visitTypeId,"
			+ " rmoAllocationMaster.specialityId AS specialityId,"
			+ " rmoAllocationMaster.rmoDoctorId AS rmoDoctorId,"
			+ " rmoAllocationMaster.wardId AS wardId,"
			+ " rmoAllocationMaster.status AS status,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as rmoDoctorName,"
			+ " wardMaster.wardName as wardName "
			+ " FROM RmoAllocationMaster rmoAllocationMaster "
			+ " LEFT JOIN rmoAllocationMaster.specialityMaster specialityMaster"
			+ " LEFT JOIN rmoAllocationMaster.doctorMaster doctorMaster"
			+ " LEFT JOIN rmoAllocationMaster.wardMaster wardMaster "
			+ " WHERE rmoAllocationMaster.organizationId=:orgId "
			+ " AND rmoAllocationMaster.unitId=:unitId "
			+ " AND rmoAllocationMaster.rmoAllocationId=:rmoAllocationId "),
	
	@NamedQuery(name="GET_RMO_ALLOCATION_MASTER_LIST",
	query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
			+ " rmoAllocationMaster.visitTypeId AS visitTypeId,"
			+ " rmoAllocationMaster.specialityId AS specialityId,"
			+ " rmoAllocationMaster.rmoDoctorId AS rmoDoctorId,"
			+ " rmoAllocationMaster.wardId AS wardId,"
			+ " rmoAllocationMaster.status AS status,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as rmoDoctorName,"
			+ " wardMaster.wardName as wardName "
			+ " FROM RmoAllocationMaster rmoAllocationMaster "
			+ " LEFT JOIN rmoAllocationMaster.specialityMaster specialityMaster"
			+ " LEFT JOIN rmoAllocationMaster.doctorMaster doctorMaster"
			+ " LEFT JOIN rmoAllocationMaster.wardMaster wardMaster "
			+ " WHERE rmoAllocationMaster.organizationId=:orgId "
			+ " AND rmoAllocationMaster.unitId=:unitId "
			+ " AND rmoAllocationMaster.visitTypeId=:visitTypeId "),
	
/*	@NamedQuery(name="GET_ACTIVE_RMO_ALLOCATION_MASTER_LIST_BY_WARD_ID",
	query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
			+ " rmoAllocationMaster.visitTypeId AS visitTypeId,"
			+ " rmoAllocationMaster.specialityId AS specialityId,"
			+ " rmoAllocationMaster.doctorId AS doctorId,"
			+ " rmoAllocationMaster.wardId AS wardId,"
			+ " rmoAllocationMaster.nursingStationId AS nursingStationId,"
			+ " rmoAllocationMaster.cabinId as cabinId,"
			+ " rmoAllocationMaster.status AS status,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as doctorName,"
			+ " cabinMaster.cabinDesc as cabinDesc,"
			+ " wardMaster.wardName as wardName,"
			+ " nursingStationMaster.nursingStationDesc as nursingStationDesc,"
			+ " roleMaster.roleDesc as roleName,"
			+ " CONCAT  (employeeMaster.firstName, ' ', employeeMaster.middleName, ' ',employeeMaster.lastName) as nurseName"
			+ " FROM RmoAllocationMaster nurseAllocationMaster "
			+ " LEFT JOIN rmoAllocationMaster.specialityMaster specialityMaster"
			+ " LEFT JOIN rmoAllocationMaster.doctorMaster doctorMaster"
			+ " LEFT JOIN rmoAllocationMaster.cabinMaster cabinMaster"
			+ " LEFT JOIN rmoAllocationMaster.wardMaster wardMaster"
			+ " LEFT JOIN rmoAllocationMaster.nursingStationMaster nursingStationMaster"
			+ " LEFT JOIN rmoAllocationMaster.roleMaster roleMaster"
			+ " LEFT JOIN rmoAllocationMaster.userMaster userMaster"
			+ " LEFT JOIN userMaster.employeeMastersList employeeMaster"
			+ " WHERE rmoAllocationMaster.organizationId=:orgId"
			+ " AND rmoAllocationMaster.unitId=:unitId "
			+ " AND rmoAllocationMaster.wardId=:wardId"),*/
	
/*	@NamedQuery(name="GET_ACTIVE_RMO_ALLOCATION_MASTER_LIST_BY_NURSE_ID",
	query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
			+ " rmoAllocationMaster.visitTypeId AS visitTypeId,"
			+ " rmoAllocationMaster.specialityId AS specialityId,"
			+ " rmoAllocationMaster.doctorId AS doctorId,"
			+ " rmoAllocationMaster.wardId AS wardId,"
			+ " rmoAllocationMaster.nursingStationId AS nursingStationId,"
			+ " rmoAllocationMaster.cabinId as cabinId,"
			+ " rmoAllocationMaster.status AS status,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as doctorName,"
			+ " cabinMaster.cabinDesc as cabinDesc,"
			+ " wardMaster.wardName as wardName,"
			+ " nursingStationMaster.nursingStationDesc as nursingStationDesc,"
			+ " roleMaster.roleDesc as roleName,"
			+ " CONCAT  (employeeMaster.firstName, ' ', employeeMaster.middleName, ' ',employeeMaster.lastName) as nurseName"
			+ " FROM RmoAllocationMaster rmoAllocationMaster "
			+ " LEFT JOIN rmoAllocationMaster.specialityMaster specialityMaster"
			+ " LEFT JOIN rmoAllocationMaster.doctorMaster doctorMaster"
			+ " LEFT JOIN rmoAllocationMaster.cabinMaster cabinMaster"
			+ " LEFT JOIN rmoAllocationMaster.wardMaster wardMaster"
			+ " LEFT JOIN rmoAllocationMaster.nursingStationMaster nursingStationMaster"
			+ " LEFT JOIN rmoAllocationMaster.roleMaster roleMaster"
			+ " LEFT JOIN rmoAllocationMaster.userMaster userMaster"
			+ " LEFT JOIN userMaster.employeeMastersList employeeMaster"
			+ " WHERE rmoAllocationMaster.organizationId=:orgId"
			+ " AND rmoAllocationMaster.unitId=:unitId "
			+ " AND rmoAllocationMaster.userId=:nurseId AND rmoAllocationMaster.visitTypeId=1 "),*/
	
	@NamedQuery(name="GET_ACTIVE_RMO_ALLOCATION_MASTER_LIST",
	query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
			+ " rmoAllocationMaster.visitTypeId AS visitTypeId,"
			+ " rmoAllocationMaster.specialityId AS specialityId,"
			+ " rmoAllocationMaster.rmoDoctorId AS rmoDoctorId,"
			+ " rmoAllocationMaster.wardId AS wardId,"
			+ " rmoAllocationMaster.status AS status "
			+ " FROM RmoAllocationMaster rmoAllocationMaster "
			+ " WHERE rmoAllocationMaster.organizationId=:orgId"
			+ " AND rmoAllocationMaster.unitId=:unitId "
			+ " AND rmoAllocationMaster.status='A' "),
	
	
	@NamedQuery(name="GET_RMO_ALLOCATION_MASTER_LIST_BY_RMODOCTOR_ID",
	query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
			+ " rmoAllocationMaster.visitTypeId AS visitTypeId,"
			+ " rmoAllocationMaster.specialityId AS specialityId,"
			+ " rmoAllocationMaster.rmoDoctorId AS rmoDoctorId,"
			+ " rmoAllocationMaster.wardId AS wardId,"
			+ " rmoAllocationMaster.status AS status "
			+ " FROM RmoAllocationMaster rmoAllocationMaster "
			+ " WHERE rmoAllocationMaster.organizationId=:orgId"
			+ " AND rmoAllocationMaster.unitId=:unitId "
			+ " AND rmoAllocationMaster.rmoDoctorId=:rmoDoctorId "
			+ " AND rmoAllocationMaster.status='A' "
			/*+ " AND rmoAllocationMaster.visitTypeId=:visitTypeId "*/),
	
	
	@NamedQuery(name="GET_RMO_ALLOCATION_MASTER_BY_NAME_NOT_BY_ID",
	query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
			+ " rmoAllocationMaster.visitTypeId AS visitTypeId,"
			+ " rmoAllocationMaster.specialityId AS specialityId,"
			+ " rmoAllocationMaster.rmoDoctorId AS rmoDoctorId,"
			+ " rmoAllocationMaster.wardId AS wardId,"
			+ " rmoAllocationMaster.status AS status "
			+ " FROM RmoAllocationMaster rmoAllocationMaster "
			+ " WHERE rmoAllocationMaster.organizationId=:orgId"
			+ " AND rmoAllocationMaster.rmoDoctorId=:rmoDoctorId"
			+ " AND rmoAllocationMaster.unitId=:unitId "
			+ " AND rmoAllocationMaster.visitTypeId=:visitTypeId "
			+ " AND rmoAllocationMaster.rmoAllocationId!=:rmoAllocationId "),
	
	
})
	
@Entity
@Table(name = "m_rmo_allocation_master", schema = "public")
@SequenceGenerator(name = "m_rmo_allocation_master_seq", sequenceName = "public.m_rmo_allocation_seq", allocationSize = 1)
public class RmoAllocationMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_rmo_allocation_master_seq")
	@Column(name = "rmo_allocation_id")
	private Integer rmoAllocationId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "speciality_id")
	private Integer specialityId; 
	
	@Column(name = "rmo_doctor_id")
	private Integer rmoDoctorId; 
	
	@Column(name = "cabin_id")
	private Integer cabinId;
	
	@Column(name = "ward_id")
	private Integer wardId; 
	
	@Column(name = "nursing_station_id")
	private Integer nursingStationId;

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
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="speciality_id", insertable=false, updatable=false)
	private SpecialityMaster specialityMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="rmo_doctor_id", insertable=false, updatable=false)
	private DoctorMaster doctorMaster;
	
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ward_id", insertable=false, updatable=false)
	private WardMaster wardMaster;
	
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="rmo_allocation_id", insertable=false, updatable=false)
	private RmoAllocationMapper rmoAllocationMapper;
	
	public Integer getRmoAllocationId() {
		return rmoAllocationId;
	}

	public void setRmoAllocationId(Integer rmoAllocationId) {
		this.rmoAllocationId = rmoAllocationId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getRmoDoctorId() {
		return rmoDoctorId;
	}

	public void setRmoDoctorId(Integer rmoDoctorId) {
		this.rmoDoctorId = rmoDoctorId;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
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

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

	public WardMaster getWardMaster() {
		return wardMaster;
	}

	public void setWardMaster(WardMaster wardMaster) {
		this.wardMaster = wardMaster;
	}

	
}
