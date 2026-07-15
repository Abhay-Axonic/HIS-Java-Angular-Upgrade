package com.param.global.model;

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
@NamedQueries({
	
	@NamedQuery(name = "CHECK_DOCTOR_CABIN_MAPPER_AVAILABLE",
	query = "SELECT doctorCabinMapper.doctorCabinMpprId as doctorCabinMpprId,"
		+ " doctorCabinMapper.doctorId as doctorId "
		+ " FROM DoctorCabinMapper doctorCabinMapper"
		+ " WHERE doctorCabinMapper.organisationId= :organisationId "
		+ " AND doctorCabinMapper.unitId= :unitId "
		+ " AND doctorCabinMapper.doctorId= :doctorId"),

	
	@NamedQuery(name = "GET_DOCTOR_CABIN_MAPPER_BY_ID",
	query = "SELECT doctorCabinMapper.doctorCabinMpprId as doctorCabinMpprId,"
	    + " doctorCabinMapper.cabinId as cabinId,"
		+ " doctorCabinMapper.doctorId as doctorId,"
		+ " doctorCabinMapper.status as status,"
		+ " doctorCabinMapper.isDeleted as isDeleted,"
		+ " doctorCabinMapper.organisationId as organisationId,"
		+ " doctorCabinMapper.unitId as unitId, "
		+ " concat(doctorMaster.firstName, ' ', doctorMaster.middleName, ' ', doctorMaster.lastName ) AS doctorName, "
		+ " cabinMaster.cabinDesc AS cabinDesc"
		+ " FROM DoctorCabinMapper doctorCabinMapper"
		+ " INNER JOIN doctorCabinMapper.cabinMaster cabinMaster"
		+ " INNER JOIN doctorCabinMapper.doctorMaster doctorMaster"
		+ " WHERE doctorCabinMapper.organisationId= :organisationId "
		+ " AND doctorCabinMapper.unitId= :unitId "
		+ " AND doctorCabinMapper.doctorId= :doctorId "
		+ " AND doctorCabinMapper.isDeleted!= 'Y' "

		),
	
	@NamedQuery(name = "GET_DOCTOR_CABIN_MAPPER_LIST",
	query = "SELECT  distinct(doctorMaster.doctorId) as doctorId,"
		+ " doctorCabinMapper.status as status,"
		+ " doctorCabinMapper.isDeleted as isDeleted,"
		+ " doctorCabinMapper.organisationId as organisationId,"
		+ " doctorCabinMapper.unitId as unitId, "
		+ " concat(doctorMaster.firstName, ' ', doctorMaster.middleName, ' ', doctorMaster.lastName ) AS doctorName "
		+ " FROM DoctorCabinMapper doctorCabinMapper"
		+ " INNER JOIN doctorCabinMapper.doctorMaster doctorMaster"
		+ " WHERE doctorCabinMapper.organisationId= :organisationId "
		+ " AND doctorCabinMapper.unitId= :unitId "
		+ " AND doctorCabinMapper.isDeleted!= 'Y' "

		),
					
	@NamedQuery(name = "GET_ACTIVE_DOCTOR_CABIN_MAPPER_LIST",
	query = "SELECT doctorCabinMapper.doctorCabinMpprId as doctorCabinMpprId,"
	    + " doctorCabinMapper.cabinId as cabinId,"
		+ " doctorCabinMapper.doctorId as doctorId,"
		+ " doctorCabinMapper.status as status,"
		+ " doctorCabinMapper.isDeleted as isDeleted,"
		+ " doctorCabinMapper.organisationId as organisationId,"
		+ " doctorCabinMapper.unitId as unitId "
		+ " FROM DoctorCabinMapper doctorCabinMapper"
		+ " WHERE doctorCabinMapper.status= 'A' "
		+ " AND doctorCabinMapper.organisationId= :organisationId "
		+ " AND doctorCabinMapper.unitId= :unitId "
		+ " AND doctorCabinMapper.isDeleted!= 'Y' "
		),
	
	@NamedQuery(name = "GET_DETAILED_CABIN_LIST",
	query = "SELECT "
//			+ " doctorCabinMapper.doctorCabinMpprId as doctorCabinMpprId,"
	    + " distinct( doctorCabinMapper.cabinId) as cabinId,"
		+ " doctorCabinMapper.doctorId as doctorId,"
		+ " cabinMaster.cabinDesc AS cabinDesc"
		+ " FROM DoctorCabinMapper doctorCabinMapper"
		+ " INNER JOIN doctorCabinMapper.cabinMaster cabinMaster"
		+ " WHERE doctorCabinMapper.doctorId= :doctorId "
		+ " AND doctorCabinMapper.organisationId= :organisationId "
		+ " AND doctorCabinMapper.unitId= :unitId "
		+ " AND doctorCabinMapper.isDeleted!= 'Y' "
		),
	
	
	@NamedQuery(name = "UPDATE_DOCTOR_CABIN_MAPPER_STATUS",
	query = " UPDATE DoctorCabinMapper doctorCabinMapper "
			+ " SET doctorCabinMapper.status = :status"
			+ " WHERE doctorCabinMapper.organisationId= :organisationId "
			+ " AND doctorCabinMapper.unitId= :unitId "
			+ " AND doctorCabinMapper.doctorId = :doctorId"
		),
	
	@NamedQuery(name = "UPDATE_DOCTOR_CABIN_MAPPER",
	query = " UPDATE DoctorCabinMapper doctorCabinMapper "
			+ " SET doctorCabinMapper.isDeleted = :isDeleted"
			+ " WHERE doctorCabinMapper.organisationId= :organisationId "
			+ " AND doctorCabinMapper.unitId = :unitId"
			+ " AND doctorCabinMapper.doctorId = :doctorId"
		),
	

	@NamedQuery(name = "GET_CABIN_BY_DOC_ID",
	query = "SELECT "
//			+ "doctorCabinMapper.doctorCabinMpprId as doctorCabinMpprId,"
	    + " distinct (doctorCabinMapper.cabinId) as cabinId, "
		+ " cabinMaster.cabinDesc AS cabinDesc "
		+ " FROM DoctorCabinMapper doctorCabinMapper "
		+ " INNER JOIN doctorCabinMapper.cabinMaster cabinMaster "
		+ " WHERE doctorCabinMapper.doctorId= :doctorId "
		//+ " AND doctorCabinMapper.organisationId= :organisationId "
		+ " AND doctorCabinMapper.unitId = :unitId"
		+ " AND doctorCabinMapper.isDeleted!= 'Y' "
		),
})



@NamedNativeQueries({
	@NamedNativeQuery(name="GET_DOCTOR_CABIN_MAPPER_COUNT", 
			query=" select count(distinct(doc_mst.doctor_id)) as \"doctorId\" "
				+ " from doctor.m_doctor_master doc_mst "
				+ " inner join public.t_doctor_cabin_mapper cab_mppr " 
				+ " on doc_mst.doctor_id = cab_mppr.doctor_id "
				+ " where doc_mst.organization_id= :organisationId"
				+ " and cab_mppr.unit_id= :unitId"
			),
	
			@NamedNativeQuery(name="GET_LIST_DOCTOR", 
			query=" select distinct (doc_mst.doctor_id) as \"doctorId\", "
					+ " doc_mst.is_rmo as \"isRMO\", "
					+ " concat(doc_mst.first_name, ' ', doc_mst.middle_name, ' ', doc_mst.last_name ) AS  \"doctorName\" "
					+ " from doctor.m_doctor_master doc_mst "
					+ " inner join t_user_unit_mapper uum on "
					+ " uum.user_id = doc_mst.user_id "
					+ " where doc_mst.organization_id= :organisationId"
					+ " and uum.unit_id= :unitId "
					 +" and uum.status = 'A' "
					 + " ORDER BY \"doctorName\" ASC "
					)
})
			

@Entity
@Table(name="t_doctor_cabin_mapper",schema="public")
@SequenceGenerator(name="seq_doctor_cabin_mapper",sequenceName="public.seq_doctor_cabin_mapper",allocationSize=1)
public class DoctorCabinMapper {
	@Id
	@Column(name="doctor_cabin_mppr_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_doctor_cabin_mapper")
	private Integer doctorCabinMpprId;
	
	@Column(name="cabin_id")
	private Integer cabinId;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
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
	@JoinColumn(name="doctor_id", insertable=false, updatable=false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="cabin_id", insertable=false, updatable=false)
	private CabinMaster cabinMaster;

	

	public Integer getDoctorCabinMpprId() {
		return doctorCabinMpprId;
	}

	public void setDoctorCabinMpprId(Integer doctorCabinMpprId) {
		this.doctorCabinMpprId = doctorCabinMpprId;
	}

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

	public CabinMaster getCabinMaster() {
		return cabinMaster;
	}

	public void setCabinMaster(CabinMaster cabinMaster) {
		this.cabinMaster = cabinMaster;
	}

	
	
}
