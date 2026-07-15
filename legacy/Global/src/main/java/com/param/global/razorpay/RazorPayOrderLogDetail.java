package com.param.global.razorpay;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="m_razorpay_order_log_details")
@SequenceGenerator(name = "razorpay_order_log_details_seq", sequenceName = "public.razorpay_order_log_details_seq", allocationSize = 1)
public class RazorPayOrderLogDetail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "razorpay_order_log_details_seq")
	@Column(name="razorpay_order_det_id")
	private Integer razorPayOrderDetId;

	@Column(name="appointment_id")
	private Integer appointmentId;
	
	@Column(name="order_id")
	private String orderId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name="mobile_no")
	private String mobileNo;
	
	@Column(name="unique_id")
	private String uniqueId;
	
	@Column(name="is_used")
	private Boolean isUsed;
	
	@Column(name="paid")
	private String paid;
	
	@Column(name="short_url")
	private String shortUrl;
	
	@Column(name="created_date")
	private Date createdDate;
	
	public Integer getRazorPayOrderDetId() {
		return razorPayOrderDetId;
	}

	public void setRazorPayOrderDetId(Integer razorPayOrderDetId) {
		this.razorPayOrderDetId = razorPayOrderDetId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	
	
}
