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


import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.global.model.SpecialityMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_UNIT_SPECIALITY_LIST",query="SELECT usm.unitSpecialityMapperId as unitSpecialityMapperId, "
			+ "usm.specialityId as specialityId, "
			+ "spl.specialityName as specialityName, "
			+ "spl.specialityCode as specialityCode, "
			+ "usm.isSurgicalCode as isSurgicalCode, "
			+ "usm.isClinicalSpeciality as isClinicalSpeciality, "
			+ "usm.isRenderingSpeciality as isRenderingSpeciality, "
			+ "usm.deptWiseAppointment as deptWiseAppointment, "
			+ "usm.isKioskDept as isKioskDept, "
			+ "usm.isNursingAssessmentRequired as isNursingAssessmentRequired, "
			+ "usm.allowedOverbookingSlots as allowedOverbookingSlots, "
			+ "usm.pddDays as pddDays, "
			+ "usm.status as status "
			+ "FROM UnitSpecialityMapper as usm "
			+ "INNER JOIN usm.specialityMaster as spl "
			+ "WHERE (usm.status='A' OR usm.status='I') "
			+ "AND usm.organizationId=:orgId "
			+ "AND usm.unitId=:unitId "
			+ "ORDER BY spl.specialityName "),
	
	@NamedQuery(name="GET_UNIT_SPECIALITY_BY_ID",query="SELECT usm.unitSpecialityMapperId as unitSpecialityMapperId,"
			+ "usm.specialityId as specialityId,"
			+ "spl.specialityName as specialityName, "
			+ "spl.specialityCode as specialityCode "
			+ "FROM UnitSpecialityMapper as usm "
			+ "INNER JOIN usm.specialityMaster as spl "
			+ "WHERE usm.unitSpecialityMapperId=:unitSpecialityMapperId "
			+ "AND usm.organizationId=:orgId "
			+ "AND usm.unitId=:unitId "),
	
	@NamedQuery(name="GET_SPECIALITY_LIST_BY_DOCTOR_ID",query="SELECT speciality.specialityId as specialityId, "
			+ "speciality.specialityName as specialityName, "
			+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,doc.doctorId as doctorId "
			+ "FROM UnitSpecialityMapper usm "
			+ "INNER JOIN usm.specialityMaster speciality "
			+ "INNER JOIN speciality.doctorSpecialityMappersList dsml "
			+ "INNER JOIN dsml.doctorMaster doc "
			+ "WHERE dsml.status='A' "
			+ "AND usm.status='A' "
			+ "AND speciality.status='A'" +
			" AND doc.status='A' "
//			+ "AND dsml.doctorId=:doctorId "
			+ "AND usm.organizationId=:orgId "
			+ "AND usm.unitId=:unitId "
//			+ "ORDER BY speciality.specialityName "
			),
	
	
	
	@NamedQuery(name="GET_ACTIVE_UNIT_SPECIALITY_LIST",
	query="SELECT usm.unitSpecialityMapperId as unitSpecialityMapperId, "
			+ "usm.specialityId as specialityId, "
			+ "spl.specialityName as specialityName, "
			+ "spl.specialityCode as specialityCode, "
			+ "usm.status as status "
			+ "FROM UnitSpecialityMapper as usm "
			+ "INNER JOIN usm.specialityMaster as spl "
			+ "WHERE usm.status='A' "
			+ " AND spl.status='A' "
			+ "AND usm.organizationId=:orgId "
			+ "AND usm.unitId=:unitId "
			+ "ORDER BY spl.specialityName ASC"),
	
	@NamedQuery(name="GET_PAGINATED_UNIT_DEPARTMENT_MASTER_DETAILS_LIST_SEARCH",
	query="SELECT usm.unitSpecialityMapperId as unitSpecialityMapperId, "
			+ "usm.specialityId as specialityId, "
			+ "spl.specialityName as specialityName, "
			+ "spl.specialityCode as specialityCode, "
			+ "usm.isSurgicalCode as isSurgicalCode, "
			+ "usm.isClinicalSpeciality as isClinicalSpeciality, "
			+ "usm.isRenderingSpeciality as isRenderingSpeciality, "
			+ "usm.deptWiseAppointment as deptWiseAppointment, "
			+ "usm.isKioskDept as isKioskDept, "
			+ "usm.isNursingAssessmentRequired as isNursingAssessmentRequired, "
			+ "usm.allowedOverbookingSlots as allowedOverbookingSlots, "
			+ "usm.pddDays as pddDays, "
			+ "usm.status as status "
			+ "FROM UnitSpecialityMapper as usm "
			+ "INNER JOIN usm.specialityMaster as spl "
			+ "WHERE (usm.status='A' OR usm.status='I') "
			+ "AND usm.organizationId=:orgId "
			+ "AND usm.unitId=:unitId "
			+ " AND (lower(spl.specialityName) like :specialityName "
			+ " OR lower(spl.specialityCode) like :specialityName )"
			+ "ORDER BY spl.specialityName "),
})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_SPECIALITY_LIST_BY_DOCTOR_ID_ORG",query=   " select " 
			 + " 	speciality1_.speciality_id as \"specialityId\", " 
			 + " 	speciality1_.speciality_name as \"specialityName\", " 
			 + " 	('Dr. ' || coalesce(doctormast3_.first_name)|| ' ' || coalesce(doctormast3_.middle_name)|| ' ' || coalesce(doctormast3_.last_name)) as \"doctorName\" " 
			 + " from " 
			 + " 	doctor.m_speciality_master speciality1_ " 
			 + " inner join doctor.t_doctor_speciality_mapper doctorspec2_ on " 
			 + " 	speciality1_.speciality_id = doctorspec2_.speciality_id " 
			 + " inner join doctor.m_doctor_master doctormast3_ on " 
			 + " 	doctorspec2_.doctor_id = doctormast3_.doctor_id " 
			 + " where " 
			 + " 	doctorspec2_.status = 'A' " 
			 + " 	and speciality1_.status = 'A' " 
			 + " 	and doctorspec2_.doctor_id =:doctorId " 
			 + " 	and doctormast3_.organization_id =:orgId " 
			 + " order by " 
			 + " 	speciality1_.speciality_name "),
	
	@NamedNativeQuery(name="GET_SUB_SPECIALITY_LIST_BY_DOCTOR_ID_ORG",query=   " select " 
			 + " 	speciality1_.sub_speciality_id as \"subSpecialityMasterId\", " 
			 + " 	speciality1_.sub_speciality_name as \"subSpecialityMasterName\", " 
			 + " 	('Dr. ' || coalesce(doctormast3_.first_name)|| ' ' || coalesce(doctormast3_.middle_name)|| ' ' || coalesce(doctormast3_.last_name)) as \"doctorName\" " 
			 + " from " 
			 + " 	doctor.m_sub_speciality_master speciality1_ " 
			 + " inner join doctor.t_doctor_sub_speciality_mapper doctorspec2_ on " 
			 + " 	speciality1_.sub_speciality_id = doctorspec2_.sub_speciality_id " 
			 + " inner join doctor.m_doctor_master doctormast3_ on " 
			 + " 	doctorspec2_.doctor_id = doctormast3_.doctor_id " 
			 + " where " 
			 + " 	doctorspec2_.status = 'A' " 
			 + " 	and speciality1_.status = 'A' " 
			 + " 	and doctorspec2_.doctor_id =:doctorId " 
			 + " 	and doctormast3_.organization_id =:orgId " 
			 + " order by " 
			 + " 	speciality1_.sub_speciality_name "),
	
	@NamedNativeQuery(name="GET_SPECIALITY_LIST_BY_USER_ID_UNIT",query=   " select " 
			 + " 	speciality1_.speciality_id as \"specialityId\", " 
			 + " 	speciality1_.speciality_name as \"specialityName\" " 
			 + " from " 
			 + " 	doctor.m_speciality_master speciality1_ " 
			 + " inner join doctor.t_staff_speciality_mapper staffspec2_ on " 
			 + " 	speciality1_.speciality_id = staffspec2_.speciality_id " 
			 + " where " 
			 + " 	 speciality1_.status = 'A' " 
			 + " 	and staffspec2_.status = 'A' " 
			 + " 	and staffspec2_.user_id =:userId " ),
	
	@NamedNativeQuery(name="GET_SUB_SPECIALITY_LIST_BY_USER_ID_UNIT",query=   " select " 
			 + " 	speciality1_.sub_speciality_id as \"subSpecialityMasterId\", " 
			 + " 	speciality1_.sub_speciality_name as \"subSpecialityMasterName\" " 
			 + " from " 
			 + " 	doctor.m_sub_speciality_master speciality1_ " 
			 + " inner join doctor.t_staff_sub_speciality_mapper staffspec2_ on " 
			 + " 	speciality1_.sub_speciality_id = staffspec2_.sub_speciality_id " 
			 + " where " 
			 + " 	 speciality1_.status = 'A' " 
			 + " 	and staffspec2_.status = 'A' " 
			 + " 	and staffspec2_.user_id =:userId " 
			 + " order by " 
			 + " 	speciality1_.sub_speciality_name "),
})

@Entity
@Table(name = "t_unit_speciality_mapper", schema = "public")
@SequenceGenerator(name="unit_speciality_mapper_seq", sequenceName="public.unit_speciality_mapper_seq", allocationSize=1)
public class UnitSpecialityMapper 
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_speciality_mapper_seq")
	@Column(name="unit_speciality_mapper_id")
	private int unitSpecialityMapperId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;

	@Column(name="speciality_id")
	private Integer specialityId;

	@Column(name="status")
	private char status;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	
	@Column(name = "is_surgical_code")
	private char isSurgicalCode;

	@Column(name = "is_clinical_speciality")
	private char isClinicalSpeciality;

	@Column(name = "is_rendering_speciality")
	private char isRenderingSpeciality;

	@Column(name = "dept_wise_appointment")
	private char deptWiseAppointment;
	
	@Column(name = "is_kiosk_dept")
	private Character isKioskDept;
	
	@Column(name = "is_nursing_assessment_required")
	private Character isNursingAssessmentRequired;
	
	@Column(name = "allowed_overbooking_slots")
	private Integer allowedOverbookingSlots;
	
	@Column(name = "pdd_days")
	private Integer pddDays;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;

	
	
	
	public char getIsSurgicalCode() {
		return isSurgicalCode;
	}

	public void setIsSurgicalCode(char isSurgicalCode) {
		this.isSurgicalCode = isSurgicalCode;
	}

	public char getIsClinicalSpeciality() {
		return isClinicalSpeciality;
	}

	public void setIsClinicalSpeciality(char isClinicalSpeciality) {
		this.isClinicalSpeciality = isClinicalSpeciality;
	}

	public char getIsRenderingSpeciality() {
		return isRenderingSpeciality;
	}

	public void setIsRenderingSpeciality(char isRenderingSpeciality) {
		this.isRenderingSpeciality = isRenderingSpeciality;
	}

	public char getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(char deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

	public Character getIsKioskDept() {
		return isKioskDept;
	}

	public void setIsKioskDept(Character isKioskDept) {
		this.isKioskDept = isKioskDept;
	}

	public Character getIsNursingAssessmentRequired() {
		return isNursingAssessmentRequired;
	}

	public void setIsNursingAssessmentRequired(Character isNursingAssessmentRequired) {
		this.isNursingAssessmentRequired = isNursingAssessmentRequired;
	}

	public Integer getAllowedOverbookingSlots() {
		return allowedOverbookingSlots;
	}

	public void setAllowedOverbookingSlots(Integer allowedOverbookingSlots) {
		this.allowedOverbookingSlots = allowedOverbookingSlots;
	}

	

	public Integer getPddDays() {
		return pddDays;
	}

	public void setPddDays(Integer pddDays) {
		this.pddDays = pddDays;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getUnitSpecialityMapperId() {
		return unitSpecialityMapperId;
	}

	public void setUnitSpecialityMapperId(int unitSpecialityMapperId) {
		this.unitSpecialityMapperId = unitSpecialityMapperId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	
	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}


	
	
	
}
