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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


import com.param.global.model.SpecialityMaster;
@NamedQueries({
	
	@NamedQuery(name = "CHECK_DEPARTMENT_NURSING_STATION_MAPPER_AVAILABLE",
	query = "SELECT departmentNursingStationMapper.departmentNursingStationMpprId as departmentNursingStationMpprId,"
		+ " departmentNursingStationMapper.specialityId as specialityId "
		+ " FROM DepartmentNursingStationMapper departmentNursingStationMapper"
		+ " WHERE departmentNursingStationMapper.organisationId= :organisationId "
		+ " AND departmentNursingStationMapper.unitId= :unitId "
		+ " AND departmentNursingStationMapper.specialityId= :specialityId"),

	
	@NamedQuery(name = "GET_DEPARTMENT_NURSING_STATION_MAPPER_BY_ID",
	query = "SELECT departmentNursingStationMapper.departmentNursingStationMpprId as departmentNursingStationMpprId,"
	    + " departmentNursingStationMapper.nursingStationId as nursingStationId,"
		+ " departmentNursingStationMapper.specialityId as specialityId,"
		+ " departmentNursingStationMapper.status as status,"
		+ " departmentNursingStationMapper.isDeleted as isDeleted,"
		+ " departmentNursingStationMapper.organisationId as organisationId, "
		+ " nursingStationMaster.nursingStationDesc as nursingStationDesc, "
		+ " departmentNursingStationMapper.unitId as unitId "
		+ " FROM DepartmentNursingStationMapper departmentNursingStationMapper"
		+ " INNER JOIN departmentNursingStationMapper.nursingStationMaster nursingStationMaster "
		+ " WHERE departmentNursingStationMapper.organisationId= :organisationId "
		+ " AND departmentNursingStationMapper.unitId= :unitId "
		+ " AND departmentNursingStationMapper.specialityId= :specialityId "
		+ " AND departmentNursingStationMapper.isDeleted!= 'Y' "

		),
	
	@NamedQuery(name = "GET_DEPARTMENT_NURSING_STATION_MAPPER_LIST",
	query = "SELECT  distinct(departmentNursingStationMapper.specialityId) as specialityId,"
		+ " departmentNursingStationMapper.status as status,"
		+ " departmentNursingStationMapper.isDeleted as isDeleted,"
		+ " departmentNursingStationMapper.organisationId as organisationId,"
		+ " departmentMaster.specialityName as specialityName,"
		+ " departmentNursingStationMapper.unitId as unitId"
		+ " FROM DepartmentNursingStationMapper departmentNursingStationMapper"
		+ " INNER JOIN departmentNursingStationMapper.departmentMaster departmentMaster"
		+ " WHERE departmentNursingStationMapper.organisationId= :organisationId "
		+ " AND departmentNursingStationMapper.unitId= :unitId "
		+ " AND departmentNursingStationMapper.isDeleted!= 'Y' "

		),

					
	@NamedQuery(name = "GET_ACTIVE_DEPARTMENT_NURSING_STATION_MAPPER_LIST",
	query = "SELECT departmentNursingStationMapper.departmentNursingStationMpprId as departmentNursingStationMpprId,"
	    + " departmentNursingStationMapper.nursingStationId as nursingStationId,"
		+ " departmentNursingStationMapper.specialityId as specialityId,"
		+ " departmentNursingStationMapper.status as status,"
		+ " departmentNursingStationMapper.isDeleted as isDeleted,"
		+ " departmentNursingStationMapper.organisationId as organisationId,"
		+ " departmentNursingStationMapper.unitId as unitId "
		+ " FROM DepartmentNursingStationMapper departmentNursingStationMapper"
		+ " WHERE departmentNursingStationMapper.status= 'A' "
		+ " AND departmentNursingStationMapper.organisationId= :organisationId "
		+ " AND departmentNursingStationMapper.unitId= :unitId "
		+ " AND departmentNursingStationMapper.isDeleted!= 'Y' "
		),

	
	@NamedQuery(name = "UPDATE_DEPARTMENT_NURSING_STATION_MAPPER_STATUS",
	query = " UPDATE DepartmentNursingStationMapper departmentNursingStationMapper "
			+ " SET departmentNursingStationMapper.status = :status"
			+ " WHERE departmentNursingStationMapper.organisationId= :organisationId "
			+ " AND departmentNursingStationMapper.unitId= :unitId "
			+ " AND departmentNursingStationMapper.specialityId = :specialityId"
		),
	
	@NamedQuery(name = "UPDATE_DEPARTMENT_NURSING_STATION_MAPPER",
	query = " UPDATE DepartmentNursingStationMapper departmentNursingStationMapper "
			+ " SET departmentNursingStationMapper.isDeleted = :isDeleted"
			+ " WHERE departmentNursingStationMapper.organisationId= :organisationId "
			+ " AND departmentNursingStationMapper.unitId = :unitId"
			+ " AND departmentNursingStationMapper.specialityId = :specialityId"
		),
	
	@NamedQuery(name = "GET_DEPT_LIST_BY_NURSING_STATION_ID",

	query = " SELECT specialityId as departmentId "

			+ " FROM  DepartmentNursingStationMapper departmentNursingStationMapper "
			+ " WHERE departmentNursingStationMapper.organisationId=:orgId "
			+ " AND departmentNursingStationMapper.unitId=:unitId"
			+ " AND departmentNursingStationMapper.nursingStationId=:nursingStationId "
			+ " AND departmentNursingStationMapper.isDeleted='N' "
		),
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_DEPARTMENT_NURSING_STATION_MAPPER_COUNT", 
			query=" select count(distinct(mppr.department_id)) as \"specialityId\" "
				+ " from adt.t_department_nursing_station_mapper mppr "
				
				+ " inner join doctor.m_speciality_master um " 
				+ " on mppr.department_id=um.speciality_id "
				+ " where mppr.organisation_id= :organisationId"
				+ " and mppr.unit_id= :unitId"
			)

})
@Entity
@Table(name="t_department_nursing_station_mapper",schema="adt")
@SequenceGenerator(name="t_department_nursing_station_mapper_seq",sequenceName="adt.t_department_nursing_station_mapper_seq",allocationSize=1)
public class DepartmentNursingStationMapper {
	@Id
	@Column(name="department_nursing_station_mppr_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_department_nursing_station_mapper_seq")
	private Integer departmentNursingStationMpprId;
	
	@Column(name="nursing_station_id")
	private Integer nursingStationId;
	
	@Column(name="department_id")
	private Integer specialityId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private Character status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;

	@Column(name="is_deleted")
	private Character isDeleted;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="nursing_station_id", insertable=false, updatable=false)
	private NursingStationMaster nursingStationMaster;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="department_id", insertable=false, updatable=false)
	private SpecialityMaster departmentMaster;
	
	public Integer getDepartmentNursingStationMpprId() {
		return departmentNursingStationMpprId;
	}

	public void setDepartmentNursingStationMpprId(Integer departmentNursingStationMpprId) {
		this.departmentNursingStationMpprId = departmentNursingStationMpprId;
	}

	public Integer getNursingStationId() {
		return nursingStationId;
	}

	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public NursingStationMaster getNursingStationMaster() {
		return nursingStationMaster;
	}

	public void setNursingStationMaster(NursingStationMaster nursingStationMaster) {
		this.nursingStationMaster = nursingStationMaster;
	}

	public SpecialityMaster getDepartmentMaster() {
		return departmentMaster;
	}

	public void setDepartmentMaster(SpecialityMaster departmentMaster) {
		this.departmentMaster = departmentMaster;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
}
