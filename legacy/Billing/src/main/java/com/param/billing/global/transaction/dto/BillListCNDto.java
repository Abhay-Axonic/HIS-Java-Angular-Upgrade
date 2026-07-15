package com.param.billing.global.transaction.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.LocalTime;

public class BillListCNDto {

	private Integer billingMasterId;
	private String billDate;
	private String billNumber;
	private String uhidNumber;
	private String patientName;
	private Integer visitTypeId;
	private Integer encounterId;
	private Integer admissionId;
	private String visitNo;
	private String visitDate;
	private Character isOtcReg;
	private Double totalBillAmount;
	private Double netAmount;
	private Double amountPaid;
	private Double outstanding;
	private Integer billStatusId;
	private Integer orgId;
	private Integer unitId;
	private Integer patientId;
	private Integer visitId;
	private Integer appointmentId;
	private String userName;
	private Integer isInterimOrFinalBill;
	private Integer doctorId;
	private Double totalSelfPayable;
	private Double totalCreditPayable;
	private Integer referalDocId;
	private Double intermediateSelfDue;
	private Character status;
	private Character encounterStatus;
	private Character isFinalBill;
	private String appointmentDate;
	private String encounterTime;
	private String doctorName;
	private String unitName;
	private String hospitalName;
	private String currency;
	private Double paidAmount;
	private Integer amountReceived;
	private Instant timeSlot;
	private Integer totalActiveDeposit;
	private String uhid;
	private String billStatusDesc;
	private Integer hospitalId;
	private Double outstandingAmount;

	

	public Character getIsFinalBill() {
		return isFinalBill;
	}

	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitId() {
		return visitId;
	}

	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Character getIsOtcReg() {
		return isOtcReg;
	}

	public void setIsOtcReg(Character isOtcReg) {
		this.isOtcReg = isOtcReg;
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

	public Integer getIsInterimOrFinalBill() {
		return isInterimOrFinalBill;
	}

	public void setIsInterimOrFinalBill(Integer isInterimOrFinalBill) {
		this.isInterimOrFinalBill = isInterimOrFinalBill;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Double getTotalSelfPayable() {
		return totalSelfPayable;
	}

	public void setTotalSelfPayable(Double totalSelfPayable) {
		this.totalSelfPayable = totalSelfPayable;
	}

	public Double getTotalCreditPayable() {
		return totalCreditPayable;
	}

	public void setTotalCreditPayable(Double totalCreditPayable) {
		this.totalCreditPayable = totalCreditPayable;
	}

	public Integer getReferalDocId() {
		return referalDocId;
	}

	public void setReferalDocId(Integer referalDocId) {
		this.referalDocId = referalDocId;
	}

	public Double getIntermediateSelfDue() {
		return intermediateSelfDue;
	}

	public void setIntermediateSelfDue(Double intermediateSelfDue) {
		this.intermediateSelfDue = intermediateSelfDue;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Character getEncounterStatus() {
		return encounterStatus;
	}

	public void setEncounterStatus(Character encounterStatus) {
		this.encounterStatus = encounterStatus;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getEncounterTime() {
		return encounterTime;
	}

	public void setEncounterTime(String encounterTime) {
		this.encounterTime = encounterTime;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(Integer amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Instant getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(Instant timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Integer getTotalActiveDeposit() {
		return totalActiveDeposit;
	}

	public void setTotalActiveDeposit(Integer totalActiveDeposit) {
		this.totalActiveDeposit = totalActiveDeposit;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getBillStatusDesc() {
		return billStatusDesc;
	}

	public void setBillStatusDesc(String billStatusDesc) {
		this.billStatusDesc = billStatusDesc;
	}
}
