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
	@NamedNativeQuery(name = "GET_OT_MEDICAL_GAS_BY_PRO_REQ_ID", query = 
	" SELECT  medicalGas.ot_completion_medical_gas_id as \"otCompletionMedicalGasId\", " + 
	" medicalGas.procedure_order_id as \"procedureOrderId\"," + 
	" medicalGas.service_id as \"serviceId\"," + 
	" to_char(medicalGas.from_time,'HH24:MI:SS') as \"fromTime\", " + 
	" to_char(medicalGas.to_time,'HH24:MI:SS') as \"toTime\"," + 
	" medicalGas.mrp as \"mrp\"," + 
	" medicalGas.status as \"status\"," + 
	" medicalGas.org_id as \"orgId\", " + 
	" medicalGas.unit_id as \"unitId\", " + 
	" medicalGas.created_by as \"createdBy\"," + 
	" to_char(medicalGas.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\", " + 
	" medicalGas.updated_by as \"updatedBy\", " + 
	" to_char(medicalGas.updated_date, 'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\", " + 
	" medicalGas.pro_req_id as \"proReqId\"," + 
	" to_char(medicalGas.from_date, 'DD-MM-YYYY HH24:MI:SS') as \"fromDate\", " + 
	" to_char(medicalGas.to_date, 'DD-MM-YYYY HH24:MI:SS') as \"toDate\"," + 
	" service.service_standard_name as \"serviceName\" " + 
	" FROM otims.t_ot_completion_medical_gas medicalGas " + 
	" inner join service.m_service_master service on service.service_master_id = medicalGas.service_id " + 
	" where medicalGas.pro_req_id= :id") 
	})
@Entity
@Table(name = "t_ot_completion_medical_gas" , schema = "otims")
@SequenceGenerator(name = "t_ot_completion_medical_gas_seq" , sequenceName = "otims.t_ot_completion_medical_gas_seq" , allocationSize = 1)
public class OtCompletionMedicalGas {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_ot_completion_medical_gas_seq")
	@Column(name = "ot_completion_medical_gas_id")
	private Integer otCompletionMedicalGasId;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	//private Date fromTime;
	
	@Column(name = "to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "from_date")
	private Date fromDate;
	
	@Column(name = "to_date")
	private Date toDate;
	
	@Column(name = "mrp")
	private double mrp;
	
	@Column(name = "status")
	private char status;
	
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

	@Column(name = "pro_req_id")
	private Integer proReqId;

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public double getMrp() {
		return mrp;
	}

	public void setMrp(double mrp) {
		this.mrp = mrp;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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

	public Integer getOtCompletionMedicalGasId() {
		return otCompletionMedicalGasId;
	}

	public void setOtCompletionMedicalGasId(Integer otCompletionMedicalGasId) {
		this.otCompletionMedicalGasId = otCompletionMedicalGasId;
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

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
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
	
}
