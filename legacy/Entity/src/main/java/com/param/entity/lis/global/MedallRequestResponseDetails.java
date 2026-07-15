package com.param.entity.lis.global;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_medall_request_response_details",schema="lab")
@SequenceGenerator(name="t_medall_request_response_details_seq",sequenceName="lab.t_medall_request_response_details_seq",allocationSize=1)
public class MedallRequestResponseDetails {
	@Id
	@Column(name="medall_request_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_medall_request_response_details_seq")
	private Integer  medallRequestId;
	
	@Column(name="medall_bill_id")
	private Integer medallBillId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="requested_payload")
	private String requestedPayload;
	
	@Column(name="returned_response")
	private String returnedResponse;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name="added_date")
	private Date addedDate;
	
	@Column(name="added_by")
	private Integer addedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="order_id")
	private Integer orderId;


	public Integer getMedallRequestId() {
		return medallRequestId;
	}

	public void setMedallRequestId(Integer medallRequestId) {
		this.medallRequestId = medallRequestId;
	}

	public Integer getMedallBillId() {
		return medallBillId;
	}

	public void setMedallBillId(Integer medallBillId) {
		this.medallBillId = medallBillId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getRequestedPayload() {
		return requestedPayload;
	}

	public void setRequestedPayload(String requestedPayload) {
		this.requestedPayload = requestedPayload;
	}

	public String getReturnedResponse() {
		return returnedResponse;
	}

	public void setReturnedResponse(String returnedResponse) {
		this.returnedResponse = returnedResponse;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
