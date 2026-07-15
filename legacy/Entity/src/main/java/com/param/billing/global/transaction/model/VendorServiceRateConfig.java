package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedNativeQueries({
	@NamedNativeQuery(name="GET_VENDOR_SERVICE_RATE_CONFIG_LIST",query=" select " 
			 + " 	vsrc.vendor_service_rate_config_id as \"vendorServiceRateConfigId\", " 
			 + " 	vsrc.service_id as \"serviceId\", " 
			 + " 	vsrc.package_id as \"packageId\", " 
			 + " 	msm.service_standard_name as \"serviceDesc\", " 
			 + " 	coalesce(service.service_standard_name,'-') as \"packageDesc\", " 
			 + " 	vsrc.vendor_id as \"vendorId\", " 
			 + " 	mv.\"name\" as \"vendorName\", " 
			 + " 	vsrc.payment_entitlement_id as \"paymentEntitlementId\", " 
			 + " 	mpem.payment_entitlement_desc as \"paymentEntitlementDesc\", " 
			 + " 	vsrc.visit_type_id as \"visitTypeId\", " 
			 + " 	mvtm.visit_type_code as \"visitTypeDesc\", " 
			 + " 	vsrc.patient_type_id as \"patientTypeId\", " 
			 + " 	mpcm.patient_category as \"patientTypeDesc\", " 
			 + " 	vsrc.billing_bed_category_id as \"billingBedCatId\", " 
			 + " 	mbbcm.billing_bed_desc as \"billingBedCatDesc\", " 
			 + " 	vsrc.rate as \"rate\", " 
			 + " 	to_char(vsrc.effective_date, 'dd/mm/yyyy') as \"effectiveDate\", " 
			 + " 	vsrc.status as \"status\" " 
			 + " from " 
			 + " 	service.t_vendor_service_rate_config vsrc " 
			 + " inner join service.m_service_master msm on " 
			 + " 	vsrc.service_id = msm.service_master_id " 
			 + " inner join service.t_unit_service_master_details tusmd on " 
			 + " 	vsrc.service_id = tusmd.service_master_id " 
			 + " inner join m_vendor mv on " 
			 + " 	mv.id = vsrc.vendor_id " 
			 + " inner join m_payment_entitlement_master mpem on " 
			 + " 	vsrc.payment_entitlement_id = mpem.payment_entitlement_id " 
			 + " inner join m_visit_type_master mvtm on " 
			 + " 	vsrc.visit_type_id = mvtm.visit_type_id " 
			 + " inner join m_patient_category_master mpcm on " 
			 + " 	vsrc.patient_type_id = mpcm.patient_category_id " 
			 + " inner join adt.m_billing_bed_category_master mbbcm on " 
			 + " 	vsrc.billing_bed_category_id = mbbcm.billing_bed_category_id " 
			 + " left join service.m_package_master mpm on " 
			 + " 	mpm.package_master_id = (case when vsrc.package_id > 0 then vsrc.package_id else null end) " 
			 + " left join service.m_parent_package_master parentMstr on " 
			 + " 	parentMstr.parent_package_id = mpm.parent_package_id " 
			 + " left join service.m_service_master service on " 
			 + " 	service.service_master_id = parentMstr.service_id " 
			 + " where " 
			 + " 	vsrc.status = 'A' " 
			 + " 	and tusmd.status = 'A' " 
			 + " 	and mv.is_active = true "
			 + "	and tusmd.unit_id=:unitId " 
			 + " 	and vsrc.unit_id =:unitId " )})

@Entity
@Table(name="t_vendor_service_rate_config",schema="service")
@SequenceGenerator(name="vendor_service_rate_config_seq",sequenceName="service.vendor_service_rate_config_seq",allocationSize=1)
public class VendorServiceRateConfig {

	@Id
	@Column(name="vendor_service_rate_config_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="vendor_service_rate_config_seq")
	private Integer vendorServiceRateConfigId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="vendor_id")
	private Integer vendorId;
	
	@Column(name="payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="patient_type_id")
	private Integer patientTypeId;
	
	@Column(name="billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name="rate")
	private Double rate;
	
	@Column(name="effective_date")
	private Date effectiveDate;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private Character status;

	public Integer getVendorServiceRateConfigId() {
		return vendorServiceRateConfigId;
	}

	public void setVendorServiceRateConfigId(Integer vendorServiceRateConfigId) {
		this.vendorServiceRateConfigId = vendorServiceRateConfigId;
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

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
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

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
}
