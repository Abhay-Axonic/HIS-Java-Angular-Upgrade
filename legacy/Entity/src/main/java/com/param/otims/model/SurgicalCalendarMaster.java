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
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedNativeQueries;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedNativeQueries({

	
	@NamedNativeQuery(name="GET_SURGICAL_CALENDER_LIST", 
			query="select "
					+ "   distinct(to_char(surgical_cal.procedure_date,'yyyy/MM/dd'))  as \"procedureDate\", "
					+ "	specility_mst.speciality_name as \"speciality\", "
					+ "	surgical_cal.speciality_id as \"specialityId\", "
					+ "	surgical_cal.doctor_id as \"doctorId\", "
					+ "	to_char(min(surgical_cal.from_time),'HH:MI') as \"fromTime\", "
					+ "	to_char(max(surgical_cal.to_time),'HH:MI') as \"toTime\", "
					+ "	'Dr. ' || doct_mst.first_name || ' ' || doct_mst.last_name as \"doctorName\", "
					+ "	to_char(surgical_cal.from_date,'dd/MM/yyyy') as \"fromDate\", "
					+ "	to_char(surgical_cal.to_date,'dd/MM/yyyy') as \"toDate\", "
					+ "	day_mst.\"day\" as \"day\", "
					+ "	day_mst.day_id as \"dayId\" "
					+ "from "
					+ "	public.m_surgical_calender surgical_cal "
					+ "inner join public.m_day_master day_mst on "
					+ "	surgical_cal.day_id = day_mst.day_id "
					+ "inner join doctor.m_doctor_master doct_mst on "
					+ "	surgical_cal.doctor_id = doct_mst.doctor_id "
					+ "inner join doctor.m_speciality_master specility_mst on "
					+ "	surgical_cal.speciality_id = specility_mst.speciality_id "
					+ "where "
					+ "	surgical_cal.organisation_id = :orgId "
					+ "	and surgical_cal.unit_id = :unitId "
					+ "group by \"procedureDate\",\"specialityId\",\"speciality\",\"doctorName\",\"fromDate\",\"toDate\",\"day\",\"dayId\",surgical_cal.status,\"doctorId\" "
					+ " order by \"procedureDate\""
			),
			
			@NamedNativeQuery(name="GET_SURGICAL_CALENDER_COUNT", 
			query="select "
					+ "   count(distinct(surgical_cal.procedure_date)) "
					+ "from "
					+ "	public.m_surgical_calender surgical_cal "
					+ "where "
					+ "	surgical_cal.organisation_id = :orgId "
					+ "	and surgical_cal.unit_id = :unitId "
			),
			
			@NamedNativeQuery(name="GET_SURGICAL_CALENDER_DETAILS", 
			query="select "
					+ "    surgical_cal.surgical_calender_id as \"surgicalCalenderId\", "
					+ "    to_char(surgical_cal.procedure_date,'dd/MM/yyyy')  as \"procedureDate\", "
					+ "    to_char(surgical_cal.from_time,'HH:MI')  as \"fromTime\", "
					+ "    to_char(surgical_cal.to_time,'HH:MI')  as \"toTime\", "
					+ "	surgical_cal.status as \"status\" "
					+ "from "
					+ "	public.m_surgical_calender surgical_cal "
					+ "where "
					+ "	surgical_cal.organisation_id = :orgId "
					+ "	and surgical_cal.unit_id = :unitId "
					+ "	and surgical_cal.procedure_date = :procedureDate "
					+ "	and surgical_cal.day_id = :dayId "
					+ "	and doctor_id = :doctorId "
					+ "    and speciality_id = :specialityId "
					+ " order by \"procedureDate\""
			),
			
			@NamedNativeQuery(name="UPDATE_SURGICAL_CALENDER_BY_ID", 
			query="update "
					+ "public.m_surgical_calender "
					+ "set status = :status "
					+ "where surgical_calender_id = :surgicalCalenderId"
			),
			
			@NamedNativeQuery(name="UPDATE_SURGICAL_CALENDER_STATUS_ID", 
			query="update "
					+ "public.m_surgical_calender "
					+ "set calender_status_id = :calenderStatusId "
					+ "where surgical_calender_id = :surgicalCalenderId"
			),
			
			@NamedNativeQuery(name="UPDATE_SURGICAL_CALENDER_BY_PROCEDURE_DATE", 
			query="update "
					+ "public.m_surgical_calender "
					+ "set status = :status "
					+ "where procedure_date = :procedureDate "
					+ "and organisation_id = :orgId "
					+ "and unit_id = :unitId "
					+ "and doctor_id = :doctorId "
					+ "and speciality_id = :specialityId "
			),
			
			
			@NamedNativeQuery(name="GET_WEEKLY_CALENDER_SLOTS", 
			query="select "
					+ "	patient.uhid_number as \"uhid\", "
					+ "	prefix_mst.prefix_code || ' ' || patient.first_name || ' ' || patient.last_name as \"patientDetails\", "
					+ "	to_char(calender_mst.procedure_date,'DD-MM-YYYY HH24:MI:SS')  as \"procedureDate\", "
					//+ " to_date(to_char(calender_mst.procedure_date, 'YYYY/MM/DD'), 'YYYY/MM/DD') as \"procedureInDate\", "
					+ "	service_mst.service_standard_name as  \"procedureName\", "
					+ "	calender_mst.surgical_calender_id as \"surgicalCalenderId\", "
					+ "	calender_mst.calender_status_id as \"calenderStatusId\", "
					+ "	calender_mst.doctor_id as \"doctorId\", "
					+ "	calender_mst.speciality_id as \"specialityId\", "
					+ "	concat(to_char(calender_mst.from_time, 'HH:MI')) as \"fromTime\", "
					+ "	concat(to_char(calender_mst.to_time, 'HH:MI')) as \"toTime\" "
					+ "from "
					+ "	public.m_surgical_calender calender_mst "
					+ "left join public.t_surgical_calender_mppr calender_mppr on "
					+ "	calender_mst.surgical_calender_id = calender_mppr.surgical_calender_id "
					+ "left join doctor.m_doctor_master doc on "
					+ "	calender_mst.doctor_id = doc.doctor_id "
					+ "left join doctor.m_speciality_master spl on "
					+ "	calender_mst.speciality_id = spl.speciality_id "
					+ "left join patient.t_patient_registration patient on "
					+ "	calender_mppr.patient_id = patient.patient_id "
					+ "left join public.m_gender_master gender on "
					+ "	gender.gender_id = patient.gender_id "
					+ "left join public.m_prefix_master prefix_mst on "
					+ "	patient.prefix_id = prefix_mst.prefix_id "
					+ "left join otims.t_procedure_order_master proc_order_mst on "
					+ "	calender_mppr.procedure_order_id = proc_order_mst.procedure_order_id "
					+ "left join service.m_service_master service_mst on "
					+ "	proc_order_mst.service_id = service_mst.service_master_id "
					+ "where "
					+ "	calender_mst.organisation_id =:orgId "
					+ "	and calender_mst.unit_id =:unitId "
					+ "	and calender_mst.status = 'A' "
					+ "	and calender_mst.procedure_date between :fromDate and :toDate "
					+ "	and calender_mst.doctor_id = :doctorId"
					+ "	order by calender_mst.procedure_date "
			),
			
			

})

@Entity
@Table(name = "m_surgical_calender", schema = "public")
@SequenceGenerator(name = "m_seq_surgical_calender", sequenceName = "public.m_seq_surgical_calender", allocationSize = 1)
public class SurgicalCalendarMaster {
	@Id
	@Column(name = "surgical_calender_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_surgical_calender")
	private Integer surgicalCalenderId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "from_date")
	private Date fromDate;
	
	@Column(name = "to_date")
	private Date toDate;
	
	@Column(name = "day_id")
	private Integer dayId;
	
	@Column(name = "from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name = "to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@Column(name = "calender_status_id")
	private Integer calenderStatusId;
	
	@Column(name = "procedure_date")
	private Date procedureDate;

	public Integer getSurgicalCalenderId() {
		return surgicalCalenderId;
	}

	public void setSurgicalCalenderId(Integer surgicalCalenderId) {
		this.surgicalCalenderId = surgicalCalenderId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(Instant toTime) {
		this.toTime = toTime;
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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getCalenderStatusId() {
		return calenderStatusId;
	}

	public void setCalenderStatusId(Integer calenderStatusId) {
		this.calenderStatusId = calenderStatusId;
	}

	public Date getProcedureDate() {
		return procedureDate;
	}

	public void setProcedureDate(Date procedureDate) {
		this.procedureDate = procedureDate;
	}
	
	
}
