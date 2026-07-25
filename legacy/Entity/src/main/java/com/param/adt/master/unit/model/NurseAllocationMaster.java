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
	@NamedQuery(name="GET_NURSE_ALLOCATION_MASTER_BY_ID",
			query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId,"
					+ " nurseAllocationMaster.visitTypeId AS visitTypeId,"
					+ " nurseAllocationMaster.roleId AS roleId,"
					+ " nurseAllocationMaster.userId AS userId,"
					+ " nurseAllocationMaster.specialityId AS specialityId,"
					+ " nurseAllocationMaster.doctorId AS doctorId,"
					+ " nurseAllocationMaster.wardId AS wardId,"
					+ " nurseAllocationMaster.nursingStationId AS nursingStationId,"
					+ " nurseAllocationMaster.cabinId as cabinId,"
					+ " nurseAllocationMaster.status AS status,"
					+ " specialityMaster.specialityName as specialityName,"
					+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as doctorName,"
					+ " cabinMaster.cabinDesc as cabinDesc,"
					+ " wardMaster.wardName as wardName,"
					+ " nursingStationMaster.nursingStationDesc as nursingStationDesc,"
					+ " roleMaster.roleDesc as roleName,"
					+ " CONCAT  (employeeMaster.firstName, ' ', employeeMaster.middleName, ' ',employeeMaster.lastName) as nurseName"
					+ " FROM NurseAllocationMaster nurseAllocationMaster "
					+ " LEFT JOIN nurseAllocationMaster.specialityMaster specialityMaster"
					+ " LEFT JOIN nurseAllocationMaster.doctorMaster doctorMaster"
					+ " LEFT JOIN nurseAllocationMaster.cabinMaster cabinMaster"
					+ " LEFT JOIN nurseAllocationMaster.wardMaster wardMaster"
					+ " LEFT JOIN nurseAllocationMaster.nursingStationMaster nursingStationMaster"
					+ " LEFT JOIN nurseAllocationMaster.roleMaster roleMaster"
					+ " LEFT JOIN nurseAllocationMaster.userMaster userMaster"
					+ " LEFT JOIN userMaster.employeeMastersList employeeMaster"
					+ " WHERE nurseAllocationMaster.organizationId=:orgId"
					+ " AND nurseAllocationMaster.unitId=:unitId "
					+ " AND nurseAllocationMaster.nurseAllocationId=:nurseAllocationId"),
	
	@NamedQuery(name="GET_NURSE_ALLOCATION_MASTER_LIST",
	query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId,"
			+ " nurseAllocationMaster.visitTypeId AS visitTypeId,"
			+ " nurseAllocationMaster.roleId AS roleId,"
			+ " nurseAllocationMaster.userId AS userId,"
			+ " nurseAllocationMaster.specialityId AS specialityId,"
			+ " nurseAllocationMaster.doctorId AS doctorId,"
			+ " nurseAllocationMaster.wardId AS wardId,"
			+ " nurseAllocationMaster.nursingStationId AS nursingStationId,"
			+ " nurseAllocationMaster.cabinId as cabinId,"
			+ " nurseAllocationMaster.status AS status,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as doctorName,"
			+ " cabinMaster.cabinDesc as cabinDesc,"
			+ " wardMaster.wardName as wardName,"
			+ " nursingStationMaster.nursingStationDesc as nursingStationDesc,"
			+ " roleMaster.roleDesc as roleName,"
			+ " CONCAT  (employeeMaster.firstName, ' ', employeeMaster.middleName, ' ',employeeMaster.lastName) as nurseName"
			+ " FROM NurseAllocationMaster nurseAllocationMaster "
			+ " LEFT JOIN nurseAllocationMaster.specialityMaster specialityMaster"
			+ " LEFT JOIN nurseAllocationMaster.doctorMaster doctorMaster"
			+ " LEFT JOIN nurseAllocationMaster.cabinMaster cabinMaster"
			+ " LEFT JOIN nurseAllocationMaster.wardMaster wardMaster"
			+ " LEFT JOIN nurseAllocationMaster.nursingStationMaster nursingStationMaster"
			+ " LEFT JOIN nurseAllocationMaster.roleMaster roleMaster"
			+ " LEFT JOIN nurseAllocationMaster.userMaster userMaster"
			+ " LEFT JOIN userMaster.employeeMastersList employeeMaster"
			+ " WHERE nurseAllocationMaster.organizationId=:orgId"
			+ " AND nurseAllocationMaster.unitId=:unitId "
			+ " AND nurseAllocationMaster.visitTypeId=:visitTypeId "
			+ " ORDER BY roleMaster.roleDesc ASC "),
	
	@NamedQuery(name="GET_ACTIVE_NURSE_ALLOCATION_MASTER_LIST_BY_WARD_ID",
	query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId,"
			+ " nurseAllocationMaster.visitTypeId AS visitTypeId,"
			+ " nurseAllocationMaster.roleId AS roleId,"
			+ " nurseAllocationMaster.userId AS userId,"
			+ " nurseAllocationMaster.specialityId AS specialityId,"
			+ " nurseAllocationMaster.doctorId AS doctorId,"
			+ " nurseAllocationMaster.wardId AS wardId,"
			+ " nurseAllocationMaster.nursingStationId AS nursingStationId,"
			+ " nurseAllocationMaster.cabinId as cabinId,"
			+ " nurseAllocationMaster.status AS status,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as doctorName,"
			+ " cabinMaster.cabinDesc as cabinDesc,"
			+ " wardMaster.wardName as wardName,"
			+ " nursingStationMaster.nursingStationDesc as nursingStationDesc,"
			+ " roleMaster.roleDesc as roleName,"
			+ " CONCAT  (employeeMaster.firstName, ' ', employeeMaster.middleName, ' ',employeeMaster.lastName) as nurseName"
			+ " FROM NurseAllocationMaster nurseAllocationMaster "
			+ " LEFT JOIN nurseAllocationMaster.specialityMaster specialityMaster"
			+ " LEFT JOIN nurseAllocationMaster.doctorMaster doctorMaster"
			+ " LEFT JOIN nurseAllocationMaster.cabinMaster cabinMaster"
			+ " LEFT JOIN nurseAllocationMaster.wardMaster wardMaster"
			+ " LEFT JOIN nurseAllocationMaster.nursingStationMaster nursingStationMaster"
			+ " LEFT JOIN nurseAllocationMaster.roleMaster roleMaster"
			+ " LEFT JOIN nurseAllocationMaster.userMaster userMaster"
			+ " LEFT JOIN userMaster.employeeMastersList employeeMaster"
			+ " WHERE nurseAllocationMaster.organizationId=:orgId"
			+ " AND nurseAllocationMaster.unitId=:unitId "
			+ " AND nurseAllocationMaster.wardId=:wardId AND nurseAllocationMaster.status='A' "),
	
	@NamedQuery(name="GET_ACTIVE_NURSE_ALLOCATION_MASTER_LIST_BY_NURSE_ID",
	query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId,"
			+ " nurseAllocationMaster.visitTypeId AS visitTypeId,"
			+ " nurseAllocationMaster.roleId AS roleId,"
			+ " nurseAllocationMaster.userId AS userId,"
			+ " nurseAllocationMaster.specialityId AS specialityId,"
			+ " nurseAllocationMaster.doctorId AS doctorId,"
			+ " nurseAllocationMaster.wardId AS wardId,"
			+ " nurseAllocationMaster.nursingStationId AS nursingStationId,"
			+ " nurseAllocationMaster.cabinId as cabinId,"
			+ " nurseAllocationMaster.status AS status,"
			+ " specialityMaster.specialityName as specialityName,"
			+ " concat('Dr. ',coalesce(doctorMaster.firstName),' ',coalesce(doctorMaster.middleName),' ',coalesce(doctorMaster.lastName)) as doctorName,"
			+ " cabinMaster.cabinDesc as cabinDesc,"
			+ " wardMaster.wardName as wardName,"
			+ " nursingStationMaster.nursingStationDesc as nursingStationDesc,"
			+ " roleMaster.roleDesc as roleName,"
			+ " CONCAT  (employeeMaster.firstName, ' ', employeeMaster.middleName, ' ',employeeMaster.lastName) as nurseName"
			+ " FROM NurseAllocationMaster nurseAllocationMaster "
			+ " LEFT JOIN nurseAllocationMaster.specialityMaster specialityMaster"
			+ " LEFT JOIN nurseAllocationMaster.doctorMaster doctorMaster"
			+ " LEFT JOIN nurseAllocationMaster.cabinMaster cabinMaster"
			+ " LEFT JOIN nurseAllocationMaster.wardMaster wardMaster"
			+ " LEFT JOIN nurseAllocationMaster.nursingStationMaster nursingStationMaster"
			+ " LEFT JOIN nurseAllocationMaster.roleMaster roleMaster"
			+ " LEFT JOIN nurseAllocationMaster.userMaster userMaster"
			+ " LEFT JOIN userMaster.employeeMastersList employeeMaster"
			+ " WHERE nurseAllocationMaster.organizationId=:orgId"
			+ " AND nurseAllocationMaster.unitId=:unitId "
			+ " AND nurseAllocationMaster.userId=:nurseId AND nurseAllocationMaster.visitTypeId=1 "),
	
	@NamedQuery(name="GET_ACTIVE_NURSE_ALLOCATION_MASTER_LIST",
	query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId,"
			+ " nurseAllocationMaster.visitTypeId AS visitTypeId,"
			+ " nurseAllocationMaster.roleId AS roleId,"
			+ " nurseAllocationMaster.userId AS userId,"
			+ " nurseAllocationMaster.specialityId AS specialityId,"
			+ " nurseAllocationMaster.doctorId AS doctorId,"
			+ " nurseAllocationMaster.wardId AS wardId,"
			+ " nurseAllocationMaster.nursingStationId AS nursingStationId,"
			+ " nurseAllocationMaster.cabinId as cabinId,"
			+ " nurseAllocationMaster.status AS status "
			+ " FROM NurseAllocationMaster nurseAllocationMaster "
			+ " WHERE nurseAllocationMaster.organizationId=:orgId"
			+ " AND nurseAllocationMaster.unitId=:unitId "
			+ " AND nurseAllocationMaster.status='A' "),
	
	
	@NamedQuery(name="GET_NURSE_ALLOCATION_MASTER_LIST_BY_USER_ID",
	query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId,"
			+ " nurseAllocationMaster.visitTypeId AS visitTypeId,"
			+ " nurseAllocationMaster.roleId AS roleId,"
			+ " nurseAllocationMaster.userId AS userId,"
			+ " nurseAllocationMaster.specialityId AS specialityId,"
			+ " nurseAllocationMaster.doctorId AS doctorId,"
			+ " nurseAllocationMaster.wardId AS wardId,"
			+ " nurseAllocationMaster.nursingStationId AS nursingStationId,"
			+ " nurseAllocationMaster.cabinId as cabinId,"
			+ " nurseAllocationMaster.status AS status "
			+ " FROM NurseAllocationMaster nurseAllocationMaster "
			+ " WHERE nurseAllocationMaster.organizationId=:orgId"
			+ " AND nurseAllocationMaster.unitId=:unitId "
			+ " AND nurseAllocationMaster.userId=:userId "
			+ " AND nurseAllocationMaster.visitTypeId=:visitTypeId "
			+ " AND nurseAllocationMaster.status='A' "),


	@NamedQuery(name="GET_NURSE_ALLOCATION_MASTER_BY_NAME_NOT_BY_ID",
	query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId,"
			+ " nurseAllocationMaster.visitTypeId AS visitTypeId,"
			+ " nurseAllocationMaster.roleId AS roleId,"
			+ " nurseAllocationMaster.userId AS userId,"
			+ " nurseAllocationMaster.specialityId AS specialityId,"
			+ " nurseAllocationMaster.doctorId AS doctorId,"
			+ " nurseAllocationMaster.wardId AS wardId,"
			+ " nurseAllocationMaster.nursingStationId AS nursingStationId,"
			+ " nurseAllocationMaster.cabinId as cabinId,"
			+ " nurseAllocationMaster.status AS status "
			+ " FROM NurseAllocationMaster nurseAllocationMaster "
			+ " WHERE nurseAllocationMaster.organizationId=:orgId"
			+ " AND nurseAllocationMaster.unitId=:unitId "
			+ " AND nurseAllocationMaster.userId=:userId "
			+ " AND nurseAllocationMaster.visitTypeId=:visitTypeId "
			+ " AND nurseAllocationMaster.status='A' "
			+ " AND nurseAllocationMaster.nurseAllocationId!=:nurseAllocationId")
})
@Entity
@Table(name = "m_nurse_allocation_master", schema = "public")
@SequenceGenerator(name = "m_nurse_allocation_seq", sequenceName = "public.m_nurse_allocation_seq", allocationSize = 1)
public class NurseAllocationMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_nurse_allocation_seq")
	@Column(name = "nurse_allocation_id")
	private Integer nurseAllocationId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "role_id")
	private Integer roleId; 

	@Column(name = "user_id")
	private Integer userId; 
	
	@Column(name = "speciality_id")
	private Integer specialityId; 
	
	@Column(name = "doctor_id")
	private Integer doctorId; 
	
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
	@JoinColumn(name="doctor_id", insertable=false, updatable=false)
	private DoctorMaster doctorMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="cabin_id", insertable=false, updatable=false)
	private CabinMaster cabinMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="ward_id", insertable=false, updatable=false)
	private WardMaster wardMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="nursing_station_id", insertable=false, updatable=false)
	private NursingStationMaster nursingStationMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="role_id", insertable=false, updatable=false)
	private RoleMaster roleMaster;
	
	@OneToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	public Integer getNurseAllocationId() {
		return nurseAllocationId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public Integer getUserId() {
		return userId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNurseAllocationId(Integer nurseAllocationId) {
		this.nurseAllocationId = nurseAllocationId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	} 
	
	
}
