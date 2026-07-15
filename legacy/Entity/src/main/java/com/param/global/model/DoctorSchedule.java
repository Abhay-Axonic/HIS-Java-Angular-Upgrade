package com.param.global.model;

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
	@NamedNativeQuery(name = "GET_SCHEDULE_TO_SYNC_BY_ID",
					 
			query = " select  " 
							 + "  	docsch.schedule_id as \"scheduleId\",  " 
							 + "  	doc.global_doc_id as \"doctorId\",  " 
							 + "  	docsch.doctor_id as \"doctorLocalId\",  " 
							 + "  	docsch.speciality_id as \"departmentId\",  " 
							 + "  	to_char(docsch.schedule_date, 'DD-MM-YYYY') as \"scheduleDate\",  " 
							 + "  	to_char(docsch.from_time, 'HH24:MI') as \"fromTime\",  " 
							 + "  	to_char(docsch.to_time, 'HH24:MI') as \"toTime\",  " 
							 + "  	docsch.status as \"availableStatus\",  " 
							 + "  	docsch.created_by as \"createdBy\",  " 
							 + "  	to_char(docsch.created_date, 'DD-MM-YYYY') as \"createdDate\",  " 
							 + "  	docsch.unit_id as \"unitId\",  " 
							 + "  	docsch.org_id as \"hospitalId\",  " 
							 + "  	org.global_org_id as \"globalOrgId\",  " 
							 + "  	unit.global_unit_id as \"globalUnitId\",  " 
							 + "  	2 as \"applicationId\", " 
							 + "  	1 as \"type\", " 
							 + "  	to_char(docsch.from_date, 'DD-MM-YYYY') as \"fromDate\", " 
							 + "  	to_char(docsch.to_date, 'DD-MM-YYYY') as \"toDate\", "
							 + "  	delete_all_slots as \"deleteAllSlots\" "
							 + "  from  " 
							 + "  	doctor.m_doctor_schedule docsch  " 
							 + "  inner join doctor.m_doctor_master doc on  " 
							 + "  	doc.doctor_id = docsch.doctor_id  " 
							 + "  inner join m_organization_master org on  " 
							 + "  	org.organization_id = docsch.org_id  " 
							 + "  inner join m_unit_master unit on  " 
							 + "  	unit.unit_id = docsch.unit_id  " 
							 + "  where  " 
							 + "  	docsch.schedule_id =:id  " 
							 + "  	and docsch.doctor_id is not null  " 
							 + " 	and org.global_org_id is not null  " 
							 + " 	and doc.global_doc_id is not null  "
							 + "	and docsch.add_remove=0 " 
							 + "  	and to_char(docsch.schedule_date, 'YYYY-MM-DD') >= :currentDate  " 
							 + "  union  " 
							 + "  select  " 
							 + "  	docsch.schedule_id as \"scheduleId\",  " 
							 + "  	doc.global_doc_id as \"doctorId\",  " 
							 + "  	docsch.doctor_id as \"doctorLocalId\",  " 
							 + "  	docsch.speciality_id as \"departmentId\",  " 
							 + "  	to_char(docsch.schedule_date, 'DD-MM-YYYY') as \"scheduleDate\",  " 
							 + "  	to_char(docsch.from_time, 'HH24:MI') as \"fromTime\",  " 
							 + "  	to_char(docsch.to_time, 'HH24:MI') as \"toTime\",  " 
							 + "  	docsch.status as \"availableStatus\",  " 
							 + "  	docsch.created_by as \"createdBy\",  " 
							 + "  	to_char(docsch.created_date, 'DD-MM-YYYY') as \"createdDate\",  " 
							 + "  	docsch.unit_id as \"unitId\",  " 
							 + "  	docsch.org_id as \"hospitalId\",  " 
							 + "  	org.global_org_id as \"globalOrgId\",  " 
							 + "  	unit.global_unit_id as \"globalUnitId\",  " 
							 + "  	2 as \"applicationId\", " 
							 + "  	2 as \"type\",	 " 
							 + "  	to_char(docsch.from_date, 'DD-MM-YYYY') as \"fromDate\", " 
							 + "  	to_char(docsch.to_date, 'DD-MM-YYYY') as \"toDate\", "
							 + "  	delete_all_slots as \"deleteAllSlots\" "
							 + "  from  " 
							 + "  	doctor.m_doctor_schedule docsch  " 
							 + "  inner join doctor.m_doctor_master doc on  " 
							 + "  	doc.doctor_id = docsch.doctor_id  " 
							 + "  inner join m_organization_master org on  " 
							 + "  	org.organization_id = docsch.org_id  " 
							 + "  inner join m_unit_master unit on  " 
							 + "  	unit.unit_id = docsch.unit_id  " 
							 + "  where  " 
							 + "  	docsch.schedule_id =:id  " 
							 + "  	and docsch.doctor_id is not null  " 
							 + " 	and org.global_org_id is not null  " 
							 + " 	and doc.global_doc_id is not null  "
							 + "	and docsch.add_remove in (1,2) "),
	
	@NamedNativeQuery(name = "GET_DOCTOR_CHARGES_TO_SYNC_BY_DOCTOR_ID",
	 query =   " select " 
			 + " 	mpr.doctor_id as \"localDoctorId\", " 
			 + " 	mpr.organization_id as \"hospitalId\", " 
			 + " 	org.global_org_id as \"globalOrgId\", " 
			 + " 	doc.global_doc_id as \"doctorId\", "
			 + " 	mpr.unit_id as \"unitId\", " 
			 + " 	unit.global_unit_id as \"globalUnitId\", " 
			 + " 	2 as \"applicationId\", " 
			 + " 	(case " 
			 + " 		when new_visit_rate is not null then new_visit_rate " 
			 + " 		else ( " 
			 + " 		select " 
			 + " 			rate " 
			 + " 		from " 
			 + " 			service.m_unit_service_tariff_master " 
			 + " 		where " 
			 + " 			service_id = new_visit_service_id " 
			 + " 			and unit_id = mpr.unit_id " 
			 + " 			and billing_bed_category_id =( " 
			 + " 			select " 
			 + " 				opd_billing_cat_id " 
			 + " 			from " 
			 + " 				t_unit_application_billing_configuration " 
			 + " 			where " 
			 + " 				status = 'A' " 
			 + " 				and unit_id = mpr.unit_id ) " 
			 + " 			and visit_type_id = 1 " 
			 + " 			and payment_entitlement_id = 1 " 
			 + " 			and effective_date = ( " 
			 + " 			select " 
			 + " 				max(effective_date) " 
			 + " 			from " 
			 + " 				service.m_unit_service_tariff_master " 
			 + " 			where " 
			 + " 				service_id = new_visit_service_id " 
			 + " 				and unit_id = mpr.unit_id " 
			 + " 				and billing_bed_category_id =( " 
			 + " 				select " 
			 + " 					opd_billing_cat_id " 
			 + " 				from " 
			 + " 					t_unit_application_billing_configuration " 
			 + " 				where " 
			 + " 					status = 'A' " 
			 + " 					and unit_id = mpr.unit_id ) " 
			 + " 				and visit_type_id = 1 " 
			 + " 				and payment_entitlement_id = 1 " 
			 + " 				and effective_date <= now() ) " 
			 + " 		limit 1) end ) as \"newVisitRate\", "
			 + " 	(case " 
			 + " 		when follow_up_visit_rate is not null then follow_up_visit_rate " 
			 + " 		else ( " 
			 + " 		select " 
			 + " 			rate " 
			 + " 		from " 
			 + " 			service.m_unit_service_tariff_master " 
			 + " 		where " 
			 + " 			service_id = followup_visit_service_id " 
			 + " 			and unit_id = mpr.unit_id " 
			 + " 			and billing_bed_category_id =( " 
			 + " 			select " 
			 + " 				opd_billing_cat_id " 
			 + " 			from " 
			 + " 				t_unit_application_billing_configuration " 
			 + " 			where " 
			 + " 				status = 'A' " 
			 + " 				and unit_id = mpr.unit_id ) " 
			 + " 			and visit_type_id = 1 " 
			 + " 			and payment_entitlement_id = 1 " 
			 + " 			and effective_date = ( " 
			 + " 			select " 
			 + " 				max(effective_date) " 
			 + " 			from " 
			 + " 				service.m_unit_service_tariff_master " 
			 + " 			where " 
			 + " 				service_id = followup_visit_service_id " 
			 + " 				and unit_id = mpr.unit_id " 
			 + " 				and billing_bed_category_id =( " 
			 + " 				select " 
			 + " 					opd_billing_cat_id " 
			 + " 				from " 
			 + " 					t_unit_application_billing_configuration " 
			 + " 				where " 
			 + " 					status = 'A' " 
			 + " 					and unit_id = mpr.unit_id ) " 
			 + " 				and visit_type_id = 1 " 
			 + " 				and payment_entitlement_id = 1 " 
			 + " 				and effective_date <= now() ) " 
			 + " 		limit 1) end )as \"followUpVisitRate\",mpr.speciality_id as \"classificationId\","
			 + "  spec.speciality_name as \"classificationDesc\" " 
			 + " from " 
			 + " 	doctor.m_doctor_consultation_service_mapper mpr " 
			 + " inner join doctor.m_doctor_master doc on " 
			 + " 	doc.doctor_id = mpr.doctor_id " 
			 + " inner join m_organization_master org on " 
			 + " 	org.organization_id = mpr.organization_id " 
			 + " inner join m_unit_master unit on " 
			 + " 	unit.unit_id = mpr.unit_id "
			 + " inner join doctor.m_speciality_master spec on " 
			 +"   spec.speciality_id =mpr.speciality_id " 
			 + " where " 
			 + " 	mpr.status = 'A' " 
			 + " 	and mpr.payment_entitlement_id = 1 " 
			 + " 	and mpr.doctor_id=:id "
			 + "	and org.global_org_id is not null "
			 + "    and doc.global_doc_id is not null " 
			 )

})
@Entity
@Table(name = "m_doctor_schedule" , schema = "doctor")
@SequenceGenerator(name = "m_doctor_schedule_seq" , sequenceName = "doctor.m_doctor_schedule_seq" , allocationSize = 1)
public class DoctorSchedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_doctor_schedule_seq")
	@Column(name = "schedule_id")
	private int scheduleId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "speciality_id")
	private Integer specialityId;
	
	@Column(name = "schedule_date")
	private Date scheduleDate;
	
	@Column(name = "from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name = "to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "status")
	private char status;
	
	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "from_date")
	private Date fromDate;
	
	@Column(name = "to_date")
	private Date toDate;
	
	@Column(name="delete_all_slots")
	private Boolean deleteAllSlots;
	
	@Column(name="add_remove") // 0 - Add	1 - Remove
	private Integer addRemove;
	
	public Integer getAddRemove() {
		return addRemove;
	}

	public void setAddRemove(Integer addRemove) {
		this.addRemove = addRemove;
	}

	public Boolean getDeleteAllSlots() {
		return deleteAllSlots;
	}

	public void setDeleteAllSlots(Boolean deleteAllSlots) {
		this.deleteAllSlots = deleteAllSlots;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'A');
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
