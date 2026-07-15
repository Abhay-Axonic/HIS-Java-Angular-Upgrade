package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="m_service_profit_margin",schema="lab")
@SequenceGenerator(name="service_profit_margin_id_seq",sequenceName="lab.service_profit_margin_id_seq",allocationSize=1)
public class ServiceProfitMargin {
	@Id
	@Column(name="service_profit_margin_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="service_profit_margin_id_seq")
	private Integer serviceprofitmarginId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="patient_type_id")
	private Integer patientTypeId;
	
	@Column(name="billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name="price")
	private Double price;
	
	
	@Column(name="cost_of_service")
	private Double costofservice;
	
	@Column(name="profit")
	private Double  profit;

	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;

	public Integer getServiceprofitmarginId() {
		return serviceprofitmarginId;
	}

	public void setServiceprofitmarginId(Integer serviceprofitmarginId) {
		this.serviceprofitmarginId = serviceprofitmarginId;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}



	
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getCostofservice() {
		return costofservice;
	}

	public void setCostofservice(Double costofservice) {
		this.costofservice = costofservice;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}
	
	

	
	
	
	
}
