package com.param.billing.global.transaction.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.DateConverter;

@Entity
@Table(name = "t_intrim_bill", schema = "billing")
@SequenceGenerator(name = "t_intrim_bill_seq", sequenceName = "billing.t_intrim_bill_seq", allocationSize = 1)
public class IntrimBill {

	@Id
	@Column(name = "intrim_bill_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_intrim_bill_seq")
	private Integer intrimBillId;
	
	@Column(name = "order_id")
	private Integer orderId;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "org_unit_id")
	private Integer orgUnitId;

	@Column(name = "intrim_bill_no")
	private String intrimBillNo;

	@Column(name = "order_date")
	@Convert(converter=DateConverter.class)
	private Long orderDate;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "ord_doctor_id")
	private Integer doctorId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "priority_id")
	private Integer priorityId;
	
	@Column(name = "dr_cr_id")
	private Integer drCrId ;
	
	@Column(name = "ord_remarks")
	private String ordRemarks;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	@Convert(converter=DateConverter.class)
	private Long createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	@Convert(converter=DateConverter.class)
	private Long updatedDate;
	
	@Column(name = "order_status")
	private Character orderStatus;
	
	@Column(name = "order_status_id")
	private Integer orderStatusId;
	
	@Column(name = "is_pharmacy")
	private Character isPharmacy;

	public Integer getIntrimBillId() {
		return intrimBillId;
	}

	public void setIntrimBillId(Integer intrimBillId) {
		this.intrimBillId = intrimBillId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public String getIntrimBillNo() {
		return intrimBillNo;
	}

	public void setIntrimBillNo(String intrimBillNo) {
		this.intrimBillNo = intrimBillNo;
	}

	public Long getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public Integer getDrCrId() {
		return drCrId;
	}

	public void setDrCrId(Integer drCrId) {
		this.drCrId = drCrId;
	}

	public String getOrdRemarks() {
		return ordRemarks;
	}

	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Character getIsPharmacy() {
		return isPharmacy;
	}

	public void setIsPharmacy(Character isPharmacy) {
		this.isPharmacy = isPharmacy;
	}
	
}
