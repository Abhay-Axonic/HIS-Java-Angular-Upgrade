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
	@NamedNativeQuery(name = "UPDATE_PROCEDURE_REQ_STATUS_BY_ID",
					 query = "update otims.t_procedure_req_mst set procedure_status_id = :statusId "
					 		+ "where pro_req_id =:id "),
	
	@NamedNativeQuery(name = "UPDATE_PROCEDURE_SCHEDULE_STATUS_BY_ID",
	 query = "update otims.t_procedure_schedule set proceduer_status_id = :statusId "
	 		+ "where pro_req_id =:id "),
	
	@NamedNativeQuery(name = "UPDATE_ISSUE",
	 query = "update pharmacy.t_issue set sale_id=:saleId "
	 		+ " where id =:issueId "),
	
	@NamedNativeQuery(name = "UPDATE_PROCEDURE",
	 query = "update otims.t_procedure_order_master set exported_to_otims = false "
	 		+ "where procedure_order_id =:proId "),


	
	@NamedNativeQuery(name = "GET_ORDERED_PROCEDURE_FOR_OPD",
	 query = " select  "
			   + "	req_mst.pro_req_id  "
			   + "from  "
			   + "	otims.t_procedure_req_mst req_mst  "
			   + "inner join opd.t_encounter_master enc_mst on  "
			   + "	enc_mst.encounter_id = req_mst.encounter_id  "
			   + "where  "
			   + "	req_mst.patient_id = :patientId  "
			   + "	and req_mst.admission_id is null  "
			   + "	and enc_mst.doctor_id =:doctorId  "
			   + "	and req_mst.prefered_date_time >=:dateTime  "
			   + "	and req_mst.procedure_status_id in(1,3)  "),
	
	@NamedNativeQuery(name = "GET_ORDERED_PROCEDURE_FOR_IPD",
	 query = " select  "
			   + "	req_mst.pro_req_id  "
			   + "from  "
			   + "	otims.t_procedure_req_mst req_mst  "
			   + "where  "
			   + "	 req_mst.admission_id = :admissionId  "
			   + "	and req_mst.prefered_date_time >=:dateTime  "
			   + "	and req_mst.procedure_status_id in(1,3)  "),
	
	@NamedNativeQuery(name = "CONVERT_PROCEDURES",
	 query =  " update  "
			   + "	otims.t_procedure_req_mst  "
			   + "set  "
			   + "	admission_id = :admissionId, "
			   + "	visit_type_id = :visitTypeId,"
			   + "  encounter_id = NULL "
			   + "where  "
			   + "	pro_req_id = :proReqId  "),
	
	@NamedNativeQuery(name = "UPDATE_PROCEDURE_REQ_STATUS_AND_DATE_BY_ID",
	 query = "update otims.t_procedure_req_mst set procedure_status_id = :statusId ,"
	 		+ "prefered_date_time = :changeOfDate ,"
	 		+ " prefered_time =:changeOfTime "
	 		+ "where pro_req_id =:id "), 


	    
})
@Entity
@Table(name = "t_procedure_req_mst" , schema = "otims")
@SequenceGenerator(name = "t_procedure_req_mst_seq" , sequenceName = "otims.t_procedure_req_mst_seq" , allocationSize = 1)
public class ProcedureReqMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_procedure_req_mst_seq")
	@Column(name = "pro_req_id")
	private int proReqId;
	
	@Column(name = "primary_procedure_id")
	private Integer primaryProcedureId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "prefered_date_time")
	private Date preferedDateTime;
	
	@Column(name = "prefered_time")
	@Convert(converter = InstantConverter.class)
	private Instant preferedTime;
	
	@Column(name = "request_date")
	private Date requestDate;
	
	@Column(name = "request_by")
	private Integer requestBy;
	
	@Column(name = "procedure_status_id")
	private Integer procedureStatusId;
	
	@Column(name = "ot_completion_date_time")
	private Date otCompletionDateTime;
	
	@Column(name = "ot_completion_by")
	private Integer otCompletionBy;
	
	@Column(name = "ot_bill_completion_date_time")
	private Date otBillCompletionDateTime;
	
	@Column(name = "ot_bill_completion_by")
	private Integer otBillCompletionBy;
	
	@Column(name = "org_id")
	private Integer orgId;
	
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
	
	@Column(name = "emergency")
	private Boolean  emergency;

	public int getProReqId() {
		return proReqId;
	}

	public void setProReqId(int proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getPrimaryProcedureId() {
		return primaryProcedureId;
	}

	public void setPrimaryProcedureId(Integer primaryProcedureId) {
		this.primaryProcedureId = primaryProcedureId;
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

	public Date getPreferedDateTime() {
		return preferedDateTime;
	}

	public void setPreferedDateTime(Date preferedDateTime) {
		this.preferedDateTime = preferedDateTime;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Integer getRequestBy() {
		return requestBy;
	}

	public void setRequestBy(Integer requestBy) {
		this.requestBy = requestBy;
	}

	public Integer getProcedureStatusId() {
		return procedureStatusId;
	}

	public void setProcedureStatusId(Integer procedureStatusId) {
		this.procedureStatusId = procedureStatusId;
	}

	public Date getOtCompletionDateTime() {
		return otCompletionDateTime;
	}

	public void setOtCompletionDateTime(Date otCompletionDateTime) {
		this.otCompletionDateTime = otCompletionDateTime;
	}

	public Integer getOtCompletionBy() {
		return otCompletionBy;
	}

	public void setOtCompletionBy(Integer otCompletionBy) {
		this.otCompletionBy = otCompletionBy;
	}

	public Date getOtBillCompletionDateTime() {
		return otBillCompletionDateTime;
	}

	public void setOtBillCompletionDateTime(Date otBillCompletionDateTime) {
		this.otBillCompletionDateTime = otBillCompletionDateTime;
	}

	public Integer getOtBillCompletionBy() {
		return otBillCompletionBy;
	}

	public void setOtBillCompletionBy(Integer otBillCompletionBy) {
		this.otBillCompletionBy = otBillCompletionBy;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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

	public Instant getPreferedTime() {
		return preferedTime;
	}

	public void setPreferedTime(Instant preferedTime) {
		this.preferedTime = preferedTime;
	}

	public Boolean getEmergency() {
		return emergency;
	}

	public void setEmergency(Boolean emergency) {
		this.emergency = emergency;
	}
	
}
