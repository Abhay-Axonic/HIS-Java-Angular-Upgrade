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

	
	@NamedQuery(name="GET_ACTIVE_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER",
			query="SELECT doctorIPConsultationServiceMapper.doctorIPConsultationServiceMapperId as doctorIPConsultationServiceMapperId, "
					+ "doctorIPConsultationServiceMapper.doctorId as doctorId, "
					+ "doctorIPConsultationServiceMapper.firstConsultationServiceId as firstConsultationServiceId, "
					+ "doctorIPConsultationServiceMapper.routineConsultationServiceId as routineConsultationServiceId, "
					+ "doctorIPConsultationServiceMapper.noOfRoutineServices as noOfRoutineServices, "
					+ "doctorIPConsultationServiceMapper.followupRoutineServiceId as followupRoutineServiceId, "
					+ "doctorIPConsultationServiceMapper.icuPrimaryConsultationServiceId as icuPrimaryConsultationServiceId, "
					+ "doctorIPConsultationServiceMapper.status as status "
					+ "FROM DoctorIPConsultationServiceMapper doctorIPConsultationServiceMapper "
					+ "WHERE doctorIPConsultationServiceMapper.status='A' "
					),
			
	@NamedQuery(name="CHECK_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_AVAILABLE",
			query="SELECT doctorIPConsultationServiceMapper.doctorIPConsultationServiceMapperId as doctorIPConsultationServiceMapperId "
					+ "FROM DoctorIPConsultationServiceMapper doctorIPConsultationServiceMapper "
					+ "WHERE doctorIPConsultationServiceMapper.organizationId=:organizationId "
					+ "AND doctorIPConsultationServiceMapper.unitId=:unitId "
					+ "AND doctorIPConsultationServiceMapper.doctorId=:doctorId "
					//+ "AND doctorIPConsultationServiceMapper.firstConsultationServiceId=:firstConsultationServiceId "
					//+ "AND doctorIPConsultationServiceMapper.routineConsultationServiceId=:routineConsultationServiceId "
					//+ "AND doctorIPConsultationServiceMapper.icuPrimaryConsultationServiceId=:icuPrimaryConsultationServiceId "
					//+ "AND doctorIPConsultationServiceMapper.noOfRoutineServices=:noOfRoutineServices "
					//+ "AND doctorIPConsultationServiceMapper.followupRoutineServiceId=:followupRoutineServiceId"
					),
	
	@NamedQuery(name="GET_DOCTOR_IP_CONSULTATION_BY_DOCTOR_ID",query="SELECT doctorIPConsultationServiceMapperId as doctorIPConsultationServiceMapperId, "
					+ "doctorId as doctorId, "
					+ "firstConsultationServiceId as firstConsultationServiceId, "
					+ "followupRoutineServiceId as followupRoutineServiceId "
					+ "FROM DoctorIPConsultationServiceMapper "
					+ "WHERE organizationId=:orgId "
					+ "AND unitId=:unitId "
					+ "AND doctorId=:docId ")
	
})

@NamedNativeQueries({
					@NamedNativeQuery(name="GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST",
					query="SELECT dipc.doctor_id AS \"doctorId\" ,"
							+ " dipc.doctor_ip_consultation_service_mapper_id AS \"doctorIPConsultationServiceMapperId\" ," 
							+ " dipc.organization_id AS \"organizationId\" , "
							+ " dipc.unit_id AS \"unitId\" ," 
							+ " dipc.first_consultation_service_id AS \"firstConsultationServiceId\" , "
							+ " dipc.routine_consultation_service_id AS \"routineConsultationServiceId\" ," 
							+ " dipc.no_of_routine_services AS \"noOfRoutineServices\" ,"
							+ " dipc.followup_routine_service_id AS \"followupRoutineServiceId\" ," 
							+ " dipc.icu_primary_consultation_service_id AS \"icuPrimaryConsultationServiceId\" ,"
							+ " dipc.status AS \"status\" ,"
							+ " concat(dm.first_name, ' ' ,dm.last_name) AS \"doctorName\" , "							
							+ " spm.speciality_id AS \"specialityId\" ," 
							+ " spm.speciality_name AS \"specialityName\", "
							+ " fcs.service_standard_name AS \"firstConsultationServiceName\" ," 
							+ " rcs.service_standard_name AS \"routineConsultationServiceName\" ,"
							+ " frs.service_standard_name AS \"followupRoutineServiceName\" ," 
							+ " ipcs.service_standard_name AS \"icuPrimaryConsultationServiceName\" "
							+ " from doctor.m_doctor_ip_consultation_service_mapper dipc"
							+ " inner join doctor.t_doctor_speciality_mapper dsm on dipc.doctor_id=dsm.doctor_id"
							+ " inner join doctor.m_speciality_master spm on dsm.speciality_id=spm.speciality_id "
							+ " inner join service.m_service_master fcs on dipc.first_consultation_service_id=fcs.service_master_id "
							+ " inner join service.m_service_master rcs on dipc.routine_consultation_service_id=rcs.service_master_id "
							+ " inner join service.m_service_master frs on dipc.followup_routine_service_id=frs.service_master_id "
							+ " inner join service.m_service_master ipcs on dipc.icu_primary_consultation_service_id=ipcs.service_master_id "
							+ " inner join doctor.m_doctor_master dm on dipc.doctor_id=dm.doctor_id "
							+ " where dipc.organization_id=:organizationId"
							+ " and dipc.unit_id=:unitId and dipc.status='A'" 
						 
							),
					
					@NamedNativeQuery(name="GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_LIST_COUNT",
					query="SELECT count(*) "
							+ " from doctor.m_doctor_ip_consultation_service_mapper dipc"
							+ " where dipc.organization_id=:organizationId"
							+ " and dipc.unit_id=:unitId and dipc.status='A'" 
							),
					
					@NamedNativeQuery(name="GET_DOCTOR_IP_CONSULTATION_SERVICE_MAPPER_BY_ID",
					query="SELECT dipc.doctor_id AS \"doctorId\" ,"
							+ " dipc.doctor_ip_consultation_service_mapper_id AS \"doctorIPConsultationServiceMapperId\" ," 
							+ " dipc.organization_id AS \"organizationId\" , "
							+ " dipc.unit_id AS \"unitId\" ," 
							+ " dipc.first_consultation_service_id AS \"firstConsultationServiceId\" , "
							+ " dipc.routine_consultation_service_id AS \"routineConsultationServiceId\" ," 
							+ " dipc.no_of_routine_services AS \"noOfRoutineServices\" ,"
							+ " dipc.followup_routine_service_id AS \"followupRoutineServiceId\" ," 
							+ " dipc.icu_primary_consultation_service_id AS \"icuPrimaryConsultationServiceId\" ,"
							+ " dipc.status AS \"status\" ,"
							+ " concat(dm.first_name, ' ' ,dm.last_name) AS \"doctorName\" , "							
							+ " spm.speciality_id AS \"specialityId\" ," 
							+ " spm.speciality_name AS \"specialityName\", "
							+ " fcs.service_standard_name AS \"firstConsultationServiceName\" ," 
							+ " rcs.service_standard_name AS \"routineConsultationServiceName\" ,"
							+ " frs.service_standard_name AS \"followupRoutineServiceName\" ," 
							+ " ipcs.service_standard_name AS \"icuPrimaryConsultationServiceName\" "
							+ " from doctor.m_doctor_ip_consultation_service_mapper dipc"
							+ " inner join doctor.t_doctor_speciality_mapper dsm on dipc.doctor_id=dsm.doctor_id"
							+ " inner join doctor.m_speciality_master spm on dsm.speciality_id=spm.speciality_id "
							+ " inner join service.m_service_master fcs on dipc.first_consultation_service_id=fcs.service_master_id "
							+ " inner join service.m_service_master rcs on dipc.routine_consultation_service_id=rcs.service_master_id "
							+ " inner join service.m_service_master frs on dipc.followup_routine_service_id=frs.service_master_id "
							+ " inner join service.m_service_master ipcs on dipc.icu_primary_consultation_service_id=ipcs.service_master_id "
							+ " inner join doctor.m_doctor_master dm on dipc.doctor_id=dm.doctor_id "
							+ " where dipc.organization_id=:organizationId"
							+ " and dipc.unit_id=:unitId" 
							+ " and dipc.doctor_ip_consultation_service_mapper_id=:doctorIPConsultationServiceMapperId" 

							)
})

@Entity
@Table(name = "m_doctor_ip_consultation_service_mapper", schema = "doctor")
@SequenceGenerator(name = "seq_m_doctor_ip_consultation_service_mapper", sequenceName = "doctor.seq_m_doctor_ip_consultation_service_mapper", allocationSize = 1)
public class DoctorIPConsultationServiceMapper {
	@Id
	@Column(name = "doctor_ip_consultation_service_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_doctor_ip_consultation_service_mapper")
	private Integer doctorIPConsultationServiceMapperId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "first_consultation_service_id")
	private Integer firstConsultationServiceId;
	
	@Column(name = "routine_consultation_service_id")
	private Integer routineConsultationServiceId;
	
	@Column(name = "no_of_routine_services")
	private Integer noOfRoutineServices;
	
	@Column(name = "followup_routine_service_id")
	private Integer followupRoutineServiceId;
	
	@Column(name = "icu_primary_consultation_service_id")
	private Integer icuPrimaryConsultationServiceId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_id")
	private Integer createdId;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_id")
	private Integer updatedId;
	
	@Column(name = "status")
	private Character status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "first_consultation_service_id", insertable = false, updatable = false)
	private ServiceMaster firstConsultationService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "routine_consultation_service_id", insertable = false, updatable = false)
	private ServiceMaster routineConsultationService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "followup_routine_service_id", insertable = false, updatable = false)
	private ServiceMaster followupRoutineService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "icu_primary_consultation_service_id", insertable = false, updatable = false)
	private ServiceMaster icuPrimaryConsultationService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private DoctorMaster specialityMaster;
	
		
	public Integer getDoctorIPConsultationServiceMapperId() {
		return doctorIPConsultationServiceMapperId;
	}

	public void setDoctorIPConsultationServiceMapperId(Integer doctorIPConsultationServiceMapperId) {
		this.doctorIPConsultationServiceMapperId = doctorIPConsultationServiceMapperId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getFirstConsultationServiceId() {
		return firstConsultationServiceId;
	}

	public void setFirstConsultationServiceId(Integer firstConsultationServiceId) {
		this.firstConsultationServiceId = firstConsultationServiceId;
	}

	public Integer getRoutineConsultationServiceId() {
		return routineConsultationServiceId;
	}

	public void setRoutineConsultationServiceId(Integer routineConsultationServiceId) {
		this.routineConsultationServiceId = routineConsultationServiceId;
	}

	public Integer getNoOfRoutineServices() {
		return noOfRoutineServices;
	}

	public void setNoOfRoutineServices(Integer noOfRoutineServices) {
		this.noOfRoutineServices = noOfRoutineServices;
	}

	public Integer getFollowupRoutineServiceId() {
		return followupRoutineServiceId;
	}

	public void setFollowupRoutineServiceId(Integer followupRoutineServiceId) {
		this.followupRoutineServiceId = followupRoutineServiceId;
	}

	public Integer getIcuPrimaryConsultationServiceId() {
		return icuPrimaryConsultationServiceId;
	}

	public void setIcuPrimaryConsultationServiceId(Integer icuPrimaryConsultationServiceId) {
		this.icuPrimaryConsultationServiceId = icuPrimaryConsultationServiceId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedId() {
		return createdId;
	}

	public void setCreatedId(Integer createdId) {
		this.createdId = createdId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedId() {
		return updatedId;
	}

	public void setUpdatedId(Integer updatedId) {
		this.updatedId = updatedId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public DoctorMaster getDoctorMaster() {
		return doctorMaster;
	}

	public void setDoctorMaster(DoctorMaster doctorMaster) {
		this.doctorMaster = doctorMaster;
	}

		
}
