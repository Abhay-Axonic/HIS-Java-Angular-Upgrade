package com.param.otims.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedNativeQueries({
	
	  @NamedNativeQuery(name="GET_SURGERIES_OF_THE_DAY_LIST",
		query="select " 
				+" distinct pr.uhid_number as \"uhidNumber\", " 
				+" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\", " 
			//	+" concat(coalesce(doc.first_name,''),' ',coalesce(doc.middle_name,''),' ',coalesce(doc.last_name,'')) as \"doctorName\", " 
				+"  (case when admDoc.doctor_id is not null then concat(coalesce(admDoc.first_name,''),' ',coalesce(admDoc.middle_name,''),' ',coalesce(admDoc.last_name,'')) " + 
				" else " + 
				" concat(coalesce(encounterDoc.first_name,''),' ',coalesce(encounterDoc.middle_name,''),' ',coalesce(encounterDoc.last_name,'')) " + 
				" end " + 
				" ) as \"doctorName\", "
				+"  (case when admDoc.doctor_id is not null then admDoc.doctor_id " + 
				" else " + 
				" encounterDoc.doctor_id " + 
				" end " + 
				" ) as \"doctorId\", "
				+"  (case when admDoc.doctor_id is not null then  admission.speciality_id " + 
				" else " + 
				" encounter.dept_id " + 
				" end " + 
				" ) as \"specialityId\", "
				+" pr.patient_id as \"patientId\", " 
				+"	pr.image_path as \"imagePath\", "
				+"	pr.gender_id as \"genderId\", "
				+ "to_char(pr.dob,'DD/MM/YYYY') as \"birthDate\", "
				+ "to_char(prm.created_date,'YYYY-MM-DD') as \"createdDate\", "
				+" schedule.total_duration as \"totalDuration\", " 
				+" prm.admission_id as \"admissionId\", " 
				+" prm.encounter_id as \"encounterId\", " 
				+" prm.pro_req_id as \"proReqId\", " 
				+" ot.theatre_name as \"theatreName\", "
				+" ot.operation_theatre_id as \"theatreId\", "
				+" schedule.ot_type as \"otType\", " 
				+" tbl.table_name as \"tableName\", "
				+" encounter.encounter_number as \"encounterNumber\", "
				+" admission.admission_number as \"admissionNumber\", "
				+ "  to_char(adtDtls.doa, 'dd/MM/yyyy') as \"doa\", "
				+ "  to_char(adtDtls.pdd, 'dd/MM/yyyy') as \"pdd\", "
				+" bm.bed_number as \"bedNumber\", "
				+" schedule.procedure_schedule_id as \"procedureScheduleId\", "
				+" ward.ward_name as \"wardName\", "
				+" prm.visit_type_id as \"visitTypeId\", "
				+" to_char(prm.prefered_time,'hh24:mi:ss') as \"changeTime\", "
				+" to_char(schedule.prefer_time,'hh24:mi:ss') as \"preferTime\", "
				+" gender.gender_code as \"genderCode\", "
				+" gender.gender_name as \"genderName\", "
				+" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ') as \"age\", "
				+" to_char(prm.request_date, 'YYYY-MM-DD')  as \"preferedDateTime\" , " 
				+" to_char(schedule.schedule_date, 'YYYY-MM-DD')  as \"scheduleDate\" , "
				+" schedule.procedure_type as \"procedureType\", "
				+" schedule.proceduer_status_id as \"proceduerStatusId\", "
				+" proConfig.anesthesia_type_id as \"anesthesiaTypeId\","
				+" proConfig.procedure_config_id as \"procedureConfigId\","
				+" schedule.procedure_name as \"procedureName\","
				+" adtDtls.billing_bed_category_id as \"bedBillingCategoryId\","
				+" schedule.ot_transfer_status_id as \"otTransferStatus\" " 
				+"from otims.t_procedure_req_mst prm " 
				+"inner join otims.t_procedure_schedule schedule on prm.pro_req_id = schedule.pro_req_id " 
				+" inner join otims.t_procedure_configuration proConfig on prm.primary_procedure_id = proConfig.service_id " 
				+"inner join otims.t_procedure_ot_schedule otDetails on schedule.procedure_schedule_id = otDetails.procedure_schedule_id " 
				+"inner join otims.m_operation_theatre ot on otDetails.theatre_id = ot.operation_theatre_id " 
				+"inner join otims.m_operation_table tbl on otDetails.table_id = tbl.operation_table_id "
				//+"left join doctor.m_doctor_master doc on doc.user_id = prm.created_by " 
				+"inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id " 
				+"inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id "
				+ "left join adt.t_admission admission on prm.admission_id = admission.admission_id "
				+ "left join adt.t_admission_details adtDtls on admission.admission_id = adtDtls.admission_id and adtDtls.status='A' "
				+ " left JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id "  
				+ " left JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "left join opd.t_encounter_master encounter on prm.encounter_id = encounter.encounter_id  and encounter.status_id in(5,6)" 
				+"inner join public.m_gender_master gender on pr.gender_id = gender.gender_id "
				+" left join doctor.m_doctor_master admDoc on admDoc.doctor_id = admission.doctor_id " 
				+" left join doctor.m_doctor_master encounterDoc on encounterDoc.doctor_id = encounter.doctor_id "
				+" where  prm.procedure_status_id = 3  and prm.procedure_status_id = 3 " 
				+" and schedule.organization_id = :organizationId "
				+" and schedule.unit_id = :unitId "
				+" and schedule.status = 'A' "
				+" and to_char(schedule.schedule_date,'YYYY-MM-DD') = :createdDate "
				+ " and otDetails.status='A' and (prm.admission_id is not null or prm.encounter_id is not null)"  
				),
	  
	  
	  @NamedNativeQuery(name="SEARCH_OT_CHECK_LIST",
			query="select " 
					+" distinct pr.uhid_number as \"uhidNumber\", " 
					+" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\", " 
					+" concat(coalesce(doc.first_name,''),' ',coalesce(doc.middle_name,''),' ',coalesce(doc.last_name,'')) as \"doctorName\", " 
					+" pr.patient_id as \"patientId\", " 
					+"	pr.image_path as \"imagePath\", "
					+"	pr.gender_id as \"genderId\", "
					+ "to_char(pr.dob,'DD/MM/YYYY') as \"birthDate\", "
					+ "to_char(prm.created_date,'YYYY-MM-DD') as \"createdDate\", "
					+" schedule.total_duration as \"totalDuration\", " 
					+" prm.admission_id as \"admissionId\", " 
					+" prm.encounter_id as \"encounterId\", " 
					+" prm.pro_req_id as \"proReqId\", " 
					+" ot.theatre_name as \"theatreName\", "
					+" ot.operation_theatre_id as \"theatreId\", "
					+" schedule.ot_type as \"otType\", " 
					+" tbl.table_name as \"tableName\", "
					+" encounter.encounter_number as \"encounterNumber\", "
					+" admission.admission_number as \"admissionNumber\", "
					+ "  to_char(adtDtls.doa, 'dd/MM/yyyy') as \"doa\", "
					+ "  to_char(adtDtls.pdd, 'dd/MM/yyyy') as \"pdd\", "
					+" bm.bed_number as \"bedNumber\", "
					+" schedule.procedure_schedule_id as \"procedureScheduleId\", " 
					+" ward.ward_name as \"wardName\", "
					+" prm.visit_type_id as \"visitTypeId\", "
					+" to_char(prm.prefered_time,'hh24:mi:ss') as \"changeTime\", "
					+" to_char(schedule.prefer_time,'hh24:mi:ss') as \"preferTime\", "
					+" gender.gender_code as \"genderCode\", "
					+" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ') as \"age\", "
					+" to_char(prm.request_date, 'YYYY-MM-DD')  as \"preferedDateTime\" , " 
					+" to_char(schedule.schedule_date, 'YYYY-MM-DD')  as \"scheduleDate\" , "
					+" schedule.procedure_type as \"procedureType\", "
					+" schedule.proceduer_status_id as \"proceduerStatusId\", "
					+" proConfig.anesthesia_type_id as \"anesthesiaTypeId\","
					+" schedule.procedure_name as \"procedureName\","
					+" schedule.ot_transfer_status_id as \"otTransferStatus\" " 
					+"from otims.t_procedure_req_mst prm " 
					+"inner join otims.t_procedure_schedule schedule on prm.pro_req_id = schedule.pro_req_id " 
					+" inner join otims.t_procedure_configuration proConfig on prm.primary_procedure_id = proConfig.service_id " 
					+"inner join otims.t_procedure_ot_schedule otDetails on schedule.procedure_schedule_id = otDetails.procedure_schedule_id " 
					+"inner join otims.m_operation_theatre ot on otDetails.theatre_id = ot.operation_theatre_id " 
					+"inner join otims.m_operation_table tbl on otDetails.table_id = tbl.operation_table_id "
					+"left join doctor.m_doctor_master doc on doc.user_id = prm.created_by " 
					+"inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id " 
					+"inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id "
					+ "left join adt.t_admission admission on prm.admission_id = admission.admission_id "
					+ "left join adt.t_admission_details adtDtls on admission.admission_id = adtDtls.admission_id and adtDtls.status='A' "
					+ " left JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id "  
					+ " left JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
					+ "left join opd.t_encounter_master encounter on prm.encounter_id = encounter.encounter_id " 
					+"inner join public.m_gender_master gender on pr.gender_id = gender.gender_id "
					+" where  prm.procedure_status_id = 3  and prm.procedure_status_id = 3 " 
					+" and schedule.organization_id = :organizationId "
					+" and schedule.unit_id = :unitId "
					+" and schedule.status = 'A' "
					+" and to_char(schedule.schedule_date,'YYYY-MM-DD') between :fromDate and :toDate "
					+ " and otDetails.status='A' and (prm.admission_id is not null or prm.encounter_id is not null)"  
					),
	  
	  @NamedNativeQuery(name="INIT_OT_REQUIEST_LIST",
			query="select " 
					+" distinct pr.uhid_number as \"uhidNumber\", " + 
					" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\", " + 
					" pr.patient_id as \"patientId\", " + 
					"	pr.image_path as \"imagePath\", " + 
					"	pr.gender_id as \"genderId\", " + 
					" admission.admission_id as \"admissionId\", " + 
					" prm.pro_req_id as \"proReqId\", " + 
					" otDetails.theatre_id as \"theatreId\", " + 
					" admission.admission_number as \"admissionNumber\", " + 
					" schedule.procedure_schedule_id as \"procedureScheduleId\", " + 
					" to_char(schedule.prefer_time,'hh24:mi:ss') as \"preferTime\", " + 
					" gender.gender_code as \"genderCode\", " + 
					" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ',  EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"age\", " + 
					" to_char(prm.request_date, 'YYYY-MM-DD')  as \"preferedDateTime\" , " + 
					" schedule.procedure_name as \"procedureName\"," + 
					" bm.bed_number as \"bedNumber\", "+ "ward.ward_name as \"wardName\", " +
					" transfer.ot_transfer_status as \"otTransferStatus\" " + 
					" from adt.t_admission admission " + 
					" inner join patient.t_patient_registration pr on admission.patient_id = pr.patient_id " + 
					" inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id " + 
					" inner join public.m_gender_master gender on pr.gender_id = gender.gender_id " + 
					" inner join adt.t_admission_details adtDtls on admission.admission_id = adtDtls.admission_id and adtDtls.status='A' " + 
					" left join otims.t_procedure_req_mst prm on prm.admission_id = admission.admission_id" + 
					" left join otims.t_procedure_schedule schedule on prm.pro_req_id = schedule.pro_req_id " + 
					" left join otims.t_procedure_ot_schedule otDetails on schedule.procedure_schedule_id = otDetails.procedure_schedule_id  " + 
					" left join otims.t_transfer_request  transfer on transfer.admission_id = admission.admission_id " +
					" LEFT JOIN adt.m_bed_master bm on adtDtls.bed_id=bm.bed_id " +
					" LEFT JOIN adt.m_ward_master ward on adtDtls.ward_id=ward.ward_id " +
					" where  admission.organization_id = :organizationId " + 
					" and admission.unit_id = :unitId " + 
					" and adtDtls.nursing_acceptance = 'Y'" + 
					" and admission.status = 'A' "
					+" and schedule.proceduer_status_id in (2,3,4) " 
					),
})


@Entity	
@Table(name = "t_procedure_schedule", schema = "otims")
@SequenceGenerator(name = "procedure_schedule_seq", sequenceName = "otims.procedure_schedule_seq", allocationSize = 1)
public class OTIMSDashboard {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_schedule_seq")
	@Column(name = "procedure_schedule_id")
	private Integer procedureScheduleId ;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "procedure_name")
	private String procedureName;
	
	@Column(name = "ot_type")
	private String otType;
	
	@Column(name = "procedure_type")
	private String procedureType ;
	
	@Column(name = "schedule_date")
	private Date scheduleDate;
	
	@Column(name = "prefer_time")
	@Convert(converter = InstantConverter.class)
	private Instant preferTime;
	
	@Column(name = "total_duration")
	private Integer totalDuration;
	
	@Column(name = "department")
	private String department ;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
	@Column(name = "proceduer_status_id")
	private Integer proceduerStatusId;
	
	@Column(name = "status")
	private char status;
			
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "ot_transfer_status_id")
	private Integer otTransferStatusId;

	public Integer getProcedureScheduleId() {
		return procedureScheduleId;
	}

	public void setProcedureScheduleId(Integer procedureScheduleId) {
		this.procedureScheduleId = procedureScheduleId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getOtType() {
		return otType;
	}

	public void setOtType(String otType) {
		this.otType = otType;
	}

	public String getProcedureType() {
		return procedureType;
	}

	public void setProcedureType(String procedureType) {
		this.procedureType = procedureType;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Instant getPreferTime() {
		return preferTime;
	}

	public void setPreferTime(Instant preferTime) {
		this.preferTime = preferTime;
	}

	public Integer getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Integer totalDuration) {
		this.totalDuration = totalDuration;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProceduerStatusId() {
		return proceduerStatusId;
	}

	public void setProceduerStatusId(Integer proceduerStatusId) {
		this.proceduerStatusId = proceduerStatusId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Integer getOtTransferStatusId() {
		return otTransferStatusId;
	}

	public void setOtTransferStatusId(Integer otTransferStatusId) {
		this.otTransferStatusId = otTransferStatusId;
	}
	
	
	
	
	
}
