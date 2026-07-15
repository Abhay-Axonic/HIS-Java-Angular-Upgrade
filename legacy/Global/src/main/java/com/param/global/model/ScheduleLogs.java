package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	/*@NamedQuery(name="GET_PENDING_SCHEDULED_LOGS",
				query="SELECT 	schedule.scheduleLogsId as scheduleLogsId ,schedule.tableName as tableName , schedule.status as status,"
						+ "	  	to_char(schedule.dateTime , 'HH24:MI:SS') as dateTime , schedule.recordId as recordId , schedule.priority as priority,"
						+ "		schedule.errorCount as errorCount , schedule.addedBy as addedBy , to_char(schedule.addedDate, 'HH24:MI:SS')as addedDate , "
						+ "		schedule.action as action  , schedule.application as application,schedule.appointmentStatusId as appointmentStatusId "
						+ "FROM ScheduleLogs schedule "
						+ "WHERE TRIM(schedule.status) = 'PENDING' "
						//+ " AND schedule.tableName!='AppointmentDetails' "
						+ "ORDER BY schedule.scheduleLogsId ASC "),*/
	
	@NamedQuery(name="GET_PENDING_SCHEDULED_LOGS_FOR_APPOINTMENTDETAIL",
	query="SELECT 	schedule.scheduleLogsId as scheduleLogsId ,schedule.tableName as tableName , schedule.status as status,"
			+ "	  	to_char(schedule.dateTime , 'HH24:MI:SS') as dateTime , schedule.recordId as recordId , schedule.priority as priority,"
			+ "		schedule.errorCount as errorCount , schedule.addedBy as addedBy , to_char(schedule.addedDate, 'HH24:MI:SS')as addedDate , "
			+ "		schedule.action as action  , schedule.application as application "
			+ "FROM ScheduleLogs schedule "
			+ "WHERE TRIM(schedule.status) = 'PENDING' "
			+ " AND schedule.tableName='AppointmentDetails' "
			+ "ORDER BY schedule.scheduleLogsId ASC "),
						
						
	@NamedQuery(name = "MARK_SCHEDULED_LOGS_COMPLETED",
				query= "UPDATE	ScheduleLogs schlog "
					 + "SET 	schlog.status = 'COMPLETED' "
					 + "WHERE	schlog.scheduleLogsId =:scheduleLogsId "),
		   
    @NamedQuery(name = "MARK_SCHEDULED_LOGS_COMPLETED_WITH_PATIENT_ID",
    query= "UPDATE    ScheduleLogs schlog "
         + "SET     schlog.status = 'COMPLETED' "
         + "WHERE    schlog.recordId =:recordId "),
    
	@NamedQuery(name = "MARK_SCHEDULED_LOGS_ERROR",
				query= "UPDATE	ScheduleLogs schlog "
					 + "SET 	schlog.status = 'ERROR' "
					 + "WHERE	schlog.scheduleLogsId =:scheduleLogsId ")
})


@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_PENDING_SCHEDULED_LOGS",query=" with scheduledata as ( " 
				 + " select " 
				 + " 	logs.schedule_logs_id as \"scheduleLogsId\", " 
				 + " 	logs.table_name as \"tableName\", " 
				 + " 	logs.status as \"status\", " 
				 + " 	to_char(logs.date_time , 'HH24:MI:SS') as \"dateTime\", " 
				 + " 	logs.record_id as \"recordId\", " 
				 + " 	logs.priority as \"priority\", " 
				 + " 	logs.error_count as \"errorCount\", " 
				 + " 	logs.added_by as \"addedBy\", " 
				 + " 	to_char(logs.added_date, 'HH24:MI:SS') as \"addedDate\", " 
				 + " 	logs.action as \"action\", " 
				 + " 	logs.application as \"application\", " 
				 + " 	logs.appointment_status_id as \"appointmentStatusId\", " 
				 + " 	org.global_org_id as \"globalOrgId\", " 
				 + " 	(case " 
				 + " 		when logs.table_name = 'AppointmentMaster' " 
				 + " 		or logs.table_name = 'AppointmentDetails' then ( " 
				 + " 		select " 
				 + " 			global_doc_id " 
				 + " 		from " 
				 + " 			doctor.m_doctor_master mdm " 
				 + " 		where " 
				 + " 			doctor_id = ( " 
				 + " 			select " 
				 + " 				doctor_id " 
				 + " 			from " 
				 + " 				opd.m_slot_master msm " 
				 + " 			where " 
				 + " 				slot_id = ( " 
				 + " 				select " 
				 + " 					slot_id " 
				 + " 				from " 
				 + " 					opd.m_appointment_master " 
				 + " 				where " 
				 + " 					appointment_id = logs.record_id))) " 
				 + " 		when logs.table_name = 'DoctorSchedule' then ( " 
				 + " 		select " 
				 + " 			global_doc_id " 
				 + " 		from " 
				 + " 			doctor.m_doctor_master mdm " 
				 + " 		where " 
				 + " 			doctor_id = ( " 
				 + " 			select " 
				 + " 				doctor_id " 
				 + " 			from " 
				 + " 				doctor.m_doctor_schedule mds " 
				 + " 			where " 
				 + " 				schedule_id = logs.record_id)) " 
				 + " 		when logs.table_name = 'DoctorChargesMaster' then ( " 
				 + " 		select " 
				 + " 			global_doc_id " 
				 + " 		from " 
				 + " 			doctor.m_doctor_master mdm " 
				 + " 		where " 
				 + " 			doctor_id = logs.record_id) " 
				 + " 	end) as \"globalDocId\", " 
				 + " 	(case " 
				 + " 		when logs.table_name = 'AppointmentMaster' " 
				 + " 		or logs.table_name = 'AppointmentDetails' then ( " 
				 + " 		select " 
				 + " 			uuid " 
				 + " 		from " 
				 + " 			patient.t_patient_registration tpr " 
				 + " 		where " 
				 + " 			patient_id = ( " 
				 + " 			select " 
				 + " 				patient_id " 
				 + " 			from " 
				 + " 				opd.m_appointment_master mam " 
				 + " 			where " 
				 + " 				appointment_id = logs.record_id)) " 
				 + " 		when logs.table_name = 'AdmissionDetails' then ( " 
				 + " 		select " 
				 + " 			uuid " 
				 + " 		from " 
				 + " 			patient.t_patient_registration tpr " 
				 + " 		where " 
				 + " 			patient_id = ( " 
				 + " 			select " 
				 + " 				adm.patient_id " 
				 + " 			from " 
				 + " 				adt.t_admission adm "
				 + "            inner join adt.m_discharge_summary  mds on mds.admission_id =logs.record_id	 " 
				 + " 			where " 
				 + " 				adm.admission_id = logs.record_id and mds.status='A' )) "
				 + "       when logs.table_name = 'PatientDocumentMapper' then ( " 
				 +" 		   select "  
				 +" 			uuid " 
				 +" 		  from " 
				 +" 			patient.t_patient_registration tpr " 
				 +" 		  where "  
				 +" 			patient_id = ( "  
				 +" 			select " 
				 +" 				patient_id " 
				 +" 			from " 
				 +" 				opd.t_encounter_doc_details tedd " 
				 +" 			where " 
				 +" 				encounter_doc_id = logs.record_id)) " 
				 + "       when logs.table_name = 'PatientKinDetail' then ( " 
				 +" 		   select "  
				 +" 			uuid " 
				 +" 		  from " 
				 +" 			patient.t_patient_registration tpr " 
				 +" 		  where "  
				 +" 			patient_id = ( "  
				 +" 			select " 
				 +" 				patient_id " 
				 +" 			from " 
				 +" 				public.m_kin_details  " 
				 +" 			where " 
				 +" 				kin_id = logs.record_id)) " 
				 + " 	end) as \"uuid\" ,"
				 + " 	org.channel_id as \"channelId\" " 
				 + " from " 
				 + " 	public.t_schedule_logs logs " 
				 + " inner join m_organization_master org on " 
				 + " 	org.organization_id = logs.org_id " 
				 + " where " 
				 + " 	trim(logs.status) = 'PENDING' "
				 + " 	and (org.global_org_id is not null " 
				 + " 	or logs.table_name = 'OrganizationMaster') ) " 
				 + " select " 
				 + " 	* " 
				 + " from " 
				 + " 	scheduledata s " 
				 + " where " 
				 + " 	(case " 
				 + " 		when s.\"tableName\" = 'AppointmentMaster' or s.\"tableName\" = 'AppointmentDetails'  " 
				 + " 			then s.\"globalDocId\" is not null and s.\"uuid\" is not null " 
				 + " 		when s.\"tableName\" = 'DoctorSchedule' or s.\"tableName\" = 'DoctorChargesMaster'  " 
				 + " 			then s.\"globalDocId\" is not null " 
				 + " 		when s.\"tableName\" = 'DoctorMaster'	or s.\"tableName\" = 'PatientRegistration' or s.\"tableName\" = 'UnitMaster'    or  s.\"tableName\" = 'PatientDocumentMapper' " 
				 + " 			then s.\"globalOrgId\" is not null " 
				 + " 		when s.\"tableName\" = 'AdmissionDetails' or  s.\"tableName\" = 'PatientDocumentMapper' or  s.\"tableName\" = 'PatientKinDetail' "
				 + "			then s.\"uuid\" is not null "
				 + " 		when s.\"tableName\" = 'OrganizationMaster' "
				 + "			then trim(s.status) = 'PENDING' "
				 + " 	end) " 
				 + " order by " 
				 + " 	s.\"scheduleLogsId\" asc  ")
})

@Entity
@Table(name="t_schedule_logs" , schema="public")
@SequenceGenerator(name="schedule_logs_seq",sequenceName="public.schedule_logs_seq",allocationSize= 1)
public class ScheduleLogs {

	@Id
	@GeneratedValue(generator="schedule_logs_seq",strategy=GenerationType.SEQUENCE)
	@Column(name="schedule_logs_id")
	private int scheduleLogsId;
	
	@Column(name="table_name")
	private String tableName;
	
	@Column(name="status")
	private String status;
	
	@Column(name="date_time")
	private Date dateTime;
	
	@Column(name="record_id")
	private Integer recordId;
	
	@Column(name="priority")
	private String priority;
	
	@Column(name="error_count")
	private Integer errorCount;
	
	@Column(name="added_by")
	private Integer addedBy;
	
	@Column(name="added_date")
	private Date addedDate;
	
	@Column(name="action")
	private String action;
	
	@Column(name="application")
	private String application;

	@Column(name="appointment_status_id")
	private Integer appointmentStatusId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public int getScheduleLogsId() {
		return scheduleLogsId;
	}

	public void setScheduleLogsId(int scheduleLogsId) {
		this.scheduleLogsId = scheduleLogsId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Integer getErrorCount() {
		return errorCount;
	}

	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	
}
