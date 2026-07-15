package com.param.global.common;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_cc_avenue_log",schema="billing")
@SequenceGenerator(name="cc_avenue_log_seq",sequenceName="billing.cc_avenue_log_seq",allocationSize=1)
public class CCAvenueLog {

	@Id
	@Column(name="cc_avenue_log_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="cc_avenue_log_seq")
	private Integer ccCvenueLogId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="mobile_no")
	private String mobileNo;
	
	@Column(name="order_no")
	private String orderNo;
	
	@Column(name="request_amount")
	private Double requestAmount;
	
	@Column(name="paid_amount")
	private Double paidAmount;
	
	@Column(name="pay_link")
	private String payLink;
	
	@Column(name="is_used")
	private Boolean isUsed;
	
	@Column(name="result_code")
	private String resultCode;
	
	@Column(name="result_description")
	private String resultDescription;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private char status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="bill_id")
	private Integer billId;

	public Integer getCcCvenueLogId() {
		return ccCvenueLogId;
	}

	public void setCcCvenueLogId(Integer ccCvenueLogId) {
		this.ccCvenueLogId = ccCvenueLogId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Double getRequestAmount() {
		return requestAmount;
	}

	public void setRequestAmount(Double requestAmount) {
		this.requestAmount = requestAmount;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getPayLink() {
		return payLink;
	}

	public void setPayLink(String payLink) {
		this.payLink = payLink;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDescription() {
		return resultDescription;
	}

	public void setResultDescription(String resultDescription) {
		this.resultDescription = resultDescription;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	
	
}
