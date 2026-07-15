package com.param.otims.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.PatientRegistration;

@NamedNativeQueries({
	
			@NamedNativeQuery(name="GET_OT_RESERVATION_LIST_BY_CRITERIA", 
			query="select distinct " 
				+"	otreservat.ot_reservation_id as \"otReservationId\", " 
				+"	otreservat.ot_reservation_no as \"otReservationNo\", " 
				+"	otreservat.pro_req_id as \"proReqId\", " 
				+"	otreservat.patient_id as \"patientId\", " 
				+"  (case when proReqMst.admission_id is not null then " 
				+"   admission.admission_number " 
				+"    else " 
				+"    encounter.encounter_number" 
				+"  end) as \"visitNumber\", "
				+"	otreservat.admission_id as \"admissionId\", " 
				+"	otreservat.encounter_id as \"encounterId\", " 
				+"	otreservat.visit_type_id as \"visitTypeId\", " 
				+"	otreservat.schedule_id as \"scheduleId\", " 
				+"	otreservat.reservation_status_id as \"reservationStatusId\", " 
				+"	otreservat.reason_note as \"reasonNote\", " 
				+"	otreservat.reason_id as \"reasonId\", " 
				+"	otreservat.organization_id as \"organizationId\", " 
				+"	otreservat.unit_id as \"unitId\", " 
				+"	otreservat.created_by as \"createdBy\", " 
				+"	otreservat.updated_by as \"updatedBy\", " 
				+"	to_char(otreservat.created_date,'dd-MM-yyyy HH:ss:mm') as \"createdDate\", " 
				+"	to_char(otreservat.updated_date,'dd-MM-yyyy HH:ss:mm') as \"updatedDate\", " 
				+"	otreservat.status as \"status\", " 
				+"	patient.uhid_number as \"uhidNumber\", "
				+ " prf.prefix_code as \"prefixCode\", "
				+"	patient.prefix_id as \"prefixId\",(coalesce(patient.first_name,'')|| ' ' || coalesce(patient.middle_name,	'')|| ' ' || coalesce(patient.last_name,'')) as \"patientName\", " 
				+"	patient.gender_id as \"genderId\", "
				+"	patient.image_path as \"imagePath\", "
				+ " patient.email as \"emailId\", " 
				+"	to_char(patient.dob,'DD-MM-YYYY') as \"dob\", " 
				+"	patient.patient_category_id as \"patientCategoryId\", " 
				+"	patient.registration_type_id as \"registrationTypeId\", "
				+"	(extract(year from age(current_timestamp,	patient.dob))|| 'Y ' || extract(month from age(current_timestamp,patient.dob))|| 'M ') as \"age\", "
				+"  gender.gender_code as \"genderCode\", " 
				+"	schedule.procedure_name as \"procedureName\", "
				+ " 	proConfig.operation_theatre_type_id as \"operationTheatreTypeId\", " 
				+"	schedule.ot_type as \"otType\", " 
				+"	schedule.procedure_type as \"procedureType\", " 
				+"  to_char(schedule.schedule_date,'YYYY-MM-DD') as \"scheduleDate\", "
				+ " otTable.table_name as \"tableName\", "
				+ " to_char(schedule.prefer_time,'HH24:MI:SS') as \"preferTime\","
				+ " (coalesce( doc.first_name,	'')|| ' ' || coalesce(doc.middle_name, '')|| ' ' || coalesce(doc.last_name,'')) as \"doctorName\", "
				+ " to_char(adl.doa,'dd/MM/yyyy') as \"doa\", "
				+ " to_char(adl.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ " bm.bed_number as \"bedNumber\" "
				+"  from otims.t_ot_reservation otreservat " 
				+"  inner join otims.t_procedure_req_mst proReqMst on otreservat.pro_req_id = proReqMst.pro_req_id " 
				+" left join adt.t_admission admission on proReqMst.admission_id = admission.admission_id "
				+" left join adt.t_admission_details adl on adl.admission_id = admission.admission_id  and adl.status='A' "
				+" left join doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+"  left join adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+" left join opd.t_encounter_master encounter on proReqMst.encounter_id = encounter.encounter_id  "
				+"  inner join patient.t_patient_registration patient on otreservat.patient_id = patient.patient_id " 
				+"  inner join public.m_gender_master gender on patient.gender_id = gender.gender_id " 
				+"  inner join otims.t_procedure_schedule schedule on otreservat.schedule_id = schedule.procedure_schedule_id " 
				+"  inner join otims.t_procedure_ot_schedule ot on 	ot.procedure_schedule_id = schedule.procedure_schedule_id " 
				+"  inner join otims.m_operation_table otTable on 	ot.table_id = otTable.operation_table_id "
				+" inner join otims.t_procedure_order_master proOrdrMast on proReqMst.pro_req_id = proOrdrMast.pro_req_id "
				+ " inner join otims.t_procedure_configuration proConfig on proReqMst.primary_procedure_id = proConfig.service_id "
				+ " left join public.m_prefix_master prf  on prf.prefix_id = patient.prefix_id "
				+"  where otreservat.organization_id = :organizationId " 
				+"  and otreservat.unit_id = :unitId " 
				+"	and schedule.status = 'A' " 
				+"	and otTable.status = 'A' "
				+ " and ot.status = 'A' " 
				+"	and(to_char(otreservat.created_date,'YYYY-MM-DD') between :fromDate and :toDate) "),
			
			@NamedNativeQuery(name="GET_OT_RESERVATION_LIST_BY_CRITERIA_FOR_CALENDER", 
			query="select distinct " 
				+"	otreservat.ot_reservation_id as \"otReservationId\", " 
				+"	otreservat.ot_reservation_no as \"otReservationNo\", " 
				+"	otreservat.pro_req_id as \"proReqId\", " 
				+"	otreservat.patient_id as \"patientId\", " 
				+"  (case when proReqMst.admission_id is not null then " 
				+"   admission.admission_number " 
				+"    else " 
				+"    encounter.encounter_number" 
				+"  end) as \"visitNumber\", "
				+"	otreservat.admission_id as \"admissionId\", " 
				+"	otreservat.encounter_id as \"encounterId\", " 
				+"	otreservat.visit_type_id as \"visitTypeId\", " 
				+"	otreservat.schedule_id as \"scheduleId\", " 
				+"	otreservat.reservation_status_id as \"reservationStatusId\", " 
				+"	otreservat.reason_note as \"reasonNote\", " 
				+"	otreservat.reason_id as \"reasonId\", " 
				+"	otreservat.organization_id as \"organizationId\", " 
				+"	otreservat.unit_id as \"unitId\", " 
				+"	otreservat.created_by as \"createdBy\", " 
				+"	otreservat.updated_by as \"updatedBy\", " 
				+"	to_char(otreservat.created_date,'dd-MM-yyyy HH:ss:mm') as \"createdDate\", " 
				+"	to_char(otreservat.updated_date,'dd-MM-yyyy HH:ss:mm') as \"updatedDate\", " 
				+"	otreservat.status as \"status\", " 
				+"	patient.uhid_number as \"uhidNumber\", "
				+ " prf.prefix_code as \"prefixCode\", "
				+"	patient.prefix_id as \"prefixId\",(coalesce(patient.first_name,'')|| ' ' || coalesce(patient.middle_name,	'')|| ' ' || coalesce(patient.last_name,'')) as \"patientName\", " 
				+"	patient.gender_id as \"genderId\", "
				+"	patient.image_path as \"imagePath\", "
				+ " patient.email as \"emailId\", " 
				+"	to_char(patient.dob,'DD-MM-YYYY') as \"dob\", " 
				+"	patient.patient_category_id as \"patientCategoryId\", " 
				+"	patient.registration_type_id as \"registrationTypeId\", "
				+"	(extract(year from age(current_timestamp,	patient.dob))|| 'Y ' || extract(month from age(current_timestamp,patient.dob))|| 'M ') as \"age\", "
				+"  gender.gender_code as \"genderCode\", " 
				+"	schedule.procedure_name as \"procedureName\", "
				+ " 	proConfig.operation_theatre_type_id as \"operationTheatreTypeId\", " 
				+"	schedule.ot_type as \"otType\", " 
				+"	schedule.procedure_type as \"procedureType\", " 
				+"  to_char(schedule.schedule_date,'YYYY-MM-DD') as \"scheduleDate\", "
				+ " to_char(schedule.prefer_time,'HH24:MI') as \"preferTime\","
				+ " (coalesce( doc.first_name,	'')|| ' ' || coalesce(doc.middle_name, '')|| ' ' || coalesce(doc.last_name,'')) as \"doctorName\", "
				+ " to_char(adl.doa,'dd/MM/yyyy') as \"doa\", "
				+ " to_char(adl.pdd,'dd/MM/yyyy') as \"pdd\", "
				+ " otTable.operation_table_id as \"tableId\", "
				+ " otpTable.operation_theatre_id  as \"theatreId\" , "
				+ " otTable.table_name as \"tableName\" , "
				+ " otpTable.theatre_name as \"theatreName\", "
				+ " schedule.procedure_schedule_id as \"procedureScheduleId\", "
				+ " bm.bed_number as \"bedNumber\","
				+ "  admission.ot_status as \"otStatus\", "
				+ " to_char(ot.t_time,'HH24:MI') as \"toTime\" "
				+"  from otims.t_ot_reservation otreservat " 
				+"  inner join otims.t_procedure_req_mst proReqMst on otreservat.pro_req_id = proReqMst.pro_req_id " 
				+" left join adt.t_admission admission on proReqMst.admission_id = admission.admission_id "
				+" left join adt.t_admission_details adl on adl.admission_id = admission.admission_id  and adl.status='A' "
				+" left join doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+"  left join adt.m_bed_master bm on adl.bed_id = bm.bed_id "
				+" left join opd.t_encounter_master encounter on proReqMst.encounter_id = encounter.encounter_id  "
				+"  inner join patient.t_patient_registration patient on otreservat.patient_id = patient.patient_id " 
				+"  inner join public.m_gender_master gender on patient.gender_id = gender.gender_id " 
				+"  inner join otims.t_procedure_schedule schedule on otreservat.schedule_id = schedule.procedure_schedule_id " 
				+"  inner join otims.t_procedure_ot_schedule ot on 	ot.procedure_schedule_id = schedule.procedure_schedule_id " 
				+"  inner join otims.m_operation_table otTable on 	ot.table_id = otTable.operation_table_id "
				+"  inner join otims.m_operation_theatre otpTable on 	ot.theatre_id = otpTable.operation_theatre_id "
				+" inner join otims.t_procedure_order_master proOrdrMast on proReqMst.pro_req_id = proOrdrMast.pro_req_id "
				+ " inner join otims.t_procedure_configuration proConfig on proReqMst.primary_procedure_id = proConfig.service_id "
				+ " left join public.m_prefix_master prf  on prf.prefix_id = patient.prefix_id "
				+" left join otims.t_procedure_staff_schedule proStaffSed on proStaffSed.pro_req_id = otreservat.pro_req_id "
				+"  where otreservat.organization_id = :organizationId " 
				+"  and otreservat.unit_id = :unitId " 
				//+"	and schedule.status = 'A' " 
				//+"	and otTable.status = 'A' "
				//+ " and ot.status = 'A' " 
				+"	and(to_char(schedule.schedule_date,'YYYY-MM-DD') between :fromDate and :toDate) "
				),
			
	
	@NamedNativeQuery(name="SEARCH_RESERVED_PROCEDURES", 
			query="select distinct service.service_master_id as \"serviceMasterId\", "
                    + "service.service_standard_name  || ' | ' || service.service_standard_code as \"serviceStandardName\" "+
					" from 	service.t_unit_service_mapper mapper " + 
					"	inner join service.m_service_master service on " + 
					"	service.service_master_id = mapper.service_id " + 
					"	inner join service.t_unit_service_master_details details on " + 
					"	details.service_master_id = service.service_master_id " + 
					"	where  " + 
					"	mapper.unit_id = :unitId " + 
					"	and mapper.orgnisation_id = :organizationId " + 
					"	and service.status = 'A'"
                    + " AND details.is_procedure = 'Y' "
					+ " AND(lower(service.service_standard_name )like :searchKeyword OR lower(service.service_standard_code) like :searchKeyword)"),
	
	@NamedNativeQuery(name="GET_RESERVED_PROCEDURES_BY_PATIENT_ID", 
	query="select " 
			+"	otreservat.ot_reservation_id as \"otReservationId\", " 
			+"	otreservat.ot_reservation_no as \"otReservationNo\", " 
			+"	otreservat.pro_req_id as \"proReqId\", " 
			+"	otreservat.patient_id as \"patientId\", " 
			+"	otreservat.admission_id as \"admissionId\", " 
			+"	otreservat.encounter_id as \"encounterId\", " 
			+"	otreservat.visit_type_id as \"visitTypeId\", " 
			+"	otreservat.schedule_id as \"scheduleId\", " 
			+"	otreservat.reservation_status_id as \"reservationStatusId\", " 
			+"	otreservat.reason_note as \"reasonNote\", " 
			+"	otreservat.reason_id as \"reasonId\", " 
			+"	otreservat.organization_id as \"organizationId\", " 
			+"	otreservat.unit_id as \"unitId\", " 
			+"	otreservat.created_by as \"createdBy\", " 
			+"	otreservat.updated_by as \"updatedBy\", " 
			+"	to_char(otreservat.created_date,'YYYY-MM-DD HH:ss:mm') as \"createdDate\", " 
			+"	to_char(otreservat.updated_date,'YYYY-MM-DD HH:ss:mm') as \"updatedDate\", " 
			+"	otreservat.status as \"status\", " 
			+"	patient.uhid_number as \"uhidNumber\", "
			+ " prf.prefix_code as \"prefixCode\", "
			+"	patient.prefix_id as \"prefixId\",(coalesce(patient.first_name,'')|| ' ' || coalesce(patient.middle_name,	'')|| ' ' || coalesce(patient.last_name,'')) as \"patientName\", " 
			+"	patient.gender_id as \"genderId\", "
			+ " patient.email as \"emailId\", " 
			+"	to_char(patient.dob,'DD-MM-YYYY') as \"dob\", " 
			+"	patient.patient_category_id as \"patientCategoryId\", " 
			+"	patient.registration_type_id as \"registrationTypeId\", "
			+"	(extract(year from age(current_timestamp,	patient.dob))|| 'Y ' || extract(month from age(current_timestamp,patient.dob))|| 'M ') as \"age\", "
			+"  gender.gender_code as \"genderCode\", " 
			+"	schedule.procedure_name as \"procedureName\", " 
			+"	schedule.ot_type as \"otType\", " 
			+"	schedule.procedure_type as \"procedureType\", " 
			+"  to_char(schedule.schedule_date,'dd/MM/yyyy') as \"scheduleDate\", "
			+ " otTable.table_name as \"tableName\", "
			+ " to_char(schedule.prefer_time,'HH24:MI:SS') as \"preferTime\" "  
			+"  from otims.t_ot_reservation otreservat " 
			+"  inner join otims.t_procedure_req_mst proReqMst on otreservat.pro_req_id = proReqMst.pro_req_id " 
			+"  inner join patient.t_patient_registration patient on otreservat.patient_id = patient.patient_id " 
			+"  inner join public.m_gender_master gender on patient.gender_id = gender.gender_id " 
			+"  inner join otims.t_procedure_schedule schedule on otreservat.schedule_id = schedule.procedure_schedule_id " 
			+"  inner join otims.t_procedure_ot_schedule ot on 	ot.procedure_schedule_id = schedule.procedure_schedule_id " 
			+"  inner join otims.m_operation_table otTable on 	ot.table_id = otTable.operation_table_id "
			+" inner join otims.t_procedure_order_master proOrdrMast on proReqMst.pro_req_id = proOrdrMast.pro_req_id "
			+ " left join public.m_prefix_master prf  on prf.prefix_id = patient.prefix_id "
            +"  where otreservat.patient_id = :patientId and otreservat.reservation_status_id = 1 "
         //   + " and to_char(schedule.schedule_date,'YYYY-MM-DD') <= :currentDate "
            +"	and schedule.status = 'A' " 
			+"	and otTable.status = 'A' " ),
	
	@NamedNativeQuery(name="GET_VISIT_TYPE_LIST_BY_PATIENT_ID", 
	query="select "
		+"	adm.admission_id as \"admissionId\"," 
		+"	adm.visit_type_id as \"visitTypeId\", " 
		+"	adm.admission_number as \"visitNumber\" " 
		+" 	from adt.t_admission adm " 
		+" 	inner join adt.t_admission_details adls on adm.admission_id = adls.admission_id " 
		+" 	where " 
		+"	adm.patient_id = :patientId " 
		+"	and adm.status = 'A' " 
		+"	and adls.nursing_acceptance = 'Y' " 
		+" 	union " 
		+" 	select " 
		+"	encounter.encounter_id as \"admissionId\", " 
		+"	1 as \"visitTypeId\", " 
		+"	encounter.encounter_number as \"visitNumber\" " 
		+" 	from opd.t_encounter_master encounter " 
		+" 	where " 
		+"	encounter.patient_id = :patientId " 
		+"	and encounter.status_id in (5,6) " 
		+" 	order by \"visitTypeId\" "),
	/*@NamedNativeQuery(name="GET_RESERVED_PROCEDURES_BY_PATIENT_ID", 
	query="select  otres.ot_reservation_id as \"otReservationId\", "
			+ " otres.ot_reservation_no as  \"otReservationNo\", "
            + " otres.pro_req_id as  \"proReqId\", "
            + " otres.patient_id as  \"patientId\"  "
            + " from otims.t_ot_reservation otres "
            + " inner join otims.t_procedure_schedule schedule on otres.schedule_id = schedule.procedure_schedule_id "
            +"  where otres.patient_id = :patientId and otres.reservation_status_id = 1 and to_char(schedule.schedule_date,'YYYY-MM-DD') <= :currentDate"),*/
	@NamedNativeQuery(name="GET_RESERVED_PROCEDURES_BY_PATIENT_ID_FOR_DEPOSITE", 
	query="select " 
			+"	otreservat.ot_reservation_id as \"otReservationId\", " 
			+"	otreservat.ot_reservation_no as \"otReservationNo\" " 
			+"  from otims.t_ot_reservation otreservat " 
			+"  inner join otims.t_procedure_req_mst proReqMst on otreservat.pro_req_id = proReqMst.pro_req_id " 
			+"  inner join patient.t_patient_registration patient on otreservat.patient_id = patient.patient_id " 
			+"  inner join public.m_gender_master gender on patient.gender_id = gender.gender_id " 
			+"  inner join otims.t_procedure_schedule schedule on otreservat.schedule_id = schedule.procedure_schedule_id " 
			+"  inner join otims.t_procedure_ot_schedule ot on 	ot.procedure_schedule_id = schedule.procedure_schedule_id " 
			+"  inner join otims.m_operation_table otTable on 	ot.table_id = otTable.operation_table_id "
			+" inner join otims.t_procedure_order_master proOrdrMast on proReqMst.pro_req_id = proOrdrMast.pro_req_id "
			+ " left join public.m_prefix_master prf  on prf.prefix_id = patient.prefix_id "
            +"  where otreservat.patient_id = :patientId and otreservat.reservation_status_id in(1,3) and to_char(schedule.schedule_date,'YYYY-MM-DD') >= :currentDate"
            +"	and schedule.status = 'A' " 
			+"	and otTable.status = 'A' " ),
			
})
@Entity
@Table(name = "t_ot_reservation", schema = "otims")
@SequenceGenerator(name = "ot_reservation_seq", sequenceName = "otims.ot_reservation_seq", allocationSize = 1)
public class OTReservation {

	@Id
	@Column(name = "ot_reservation_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ot_reservation_seq")
	private Integer otReservationId;
	
	@Column(name="ot_reservation_no")
	private String otReservationNo;
	
	@Column(name="pro_req_id")
	private Integer proReqId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name="encounter_id")
	private Integer encounterId;

	@Column(name="visit_type_id")
	private Integer visitTypeId;

	@Column(name="schedule_id")
	private Integer scheduleId;

	@Column(name="reservation_status_id")
	private Integer reservationStatusId;
	
	@Column(name="reason_note")
	private String reasonNote;
	
	@Column(name="reason_id")
	private Integer reasonId;
	
	@Column(name="status")
	private Character status;
	
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pro_req_id", insertable = false, updatable = false)
	private ProcedureReqMaster procedureReqMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id", insertable = false, updatable = false)
	private OTIMSDashboard procedureSchedule;
	
	public Integer getOtReservationId() {
		return otReservationId;
	}

	public void setOtReservationId(Integer otReservationId) {
		this.otReservationId = otReservationId;
	}

	public String getOtReservationNo() {
		return otReservationNo;
	}

	public void setOtReservationNo(String otReservationNo) {
		this.otReservationNo = otReservationNo;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getReservationStatusId() {
		return reservationStatusId;
	}

	public void setReservationStatusId(Integer reservationStatusId) {
		this.reservationStatusId = reservationStatusId;
	}

	public String getReasonNote() {
		return reasonNote;
	}

	public void setReasonNote(String reasonNote) {
		this.reasonNote = reasonNote;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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
	
	
}
