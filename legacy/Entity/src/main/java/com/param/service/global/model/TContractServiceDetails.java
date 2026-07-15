package com.param.service.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.ServiceMaster;

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_SERVICE_DETAILS_BY_CONTRACT_ID",query="SELECT"
			 + "   csd.contract_id AS \"contractId\","
			 + "   csd.final_price AS \"finalPrice\", " 
			 + "   FROM service.t_contract_service_details csd " 
			 + "   INNER JOIN service.m_company_contract_master ccm on csd.contract_id = ccm.contract_id "
			 + "   WHERE csd.contract_id=:contractId "
			 + "   AND csd.service_id=:serviceId "
			 + "   and :currentDate between ccm.validity_from and ccm.validity_to ")
	
})




@Entity
@Table(name = "t_contract_service_details" , schema = "service")
@SequenceGenerator(name = "t_contract_service_details_seq" , sequenceName = "service.t_contract_service_details_seq" , allocationSize = 1)
public class TContractServiceDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_contract_service_details_seq")
	@Column(name = "contract_service_detail_id")
	private Integer contractServiceDetailId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name="service_price")
	private Double servicePrice;
	
	@Column(name="final_price")
	private Double finalPrice;
	
	@Column(name="apportioned_price")
	private Double apportionedPrice;
	
	@Column(name="credit_payable")
	private Double creditPayable;
	
	@Column(name="self_payable")
	private Double selfPayable;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name = "patient_category_id ")
	private Integer patientCategoryId ;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "contract_id" , insertable = false , updatable = false , nullable = false)
	private MCompanyContractMaster mCompanyContractMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "service_id" , insertable = false , updatable = false , nullable = false)
	private ServiceMaster serviceMaster;
	
	
	
	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Double getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(Double apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Integer getContractServiceDetailId() {
		return contractServiceDetailId;
	}

	public void setContractServiceDetailId(Integer contractServiceDetailId) {
		this.contractServiceDetailId = contractServiceDetailId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getServicePrice() {
		return servicePrice;
	}

	public void setServicePrice(Double servicePrice) {
		this.servicePrice = servicePrice;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
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

	public MCompanyContractMaster getmCompanyContractMaster() {
		return mCompanyContractMaster;
	}

	public void setmCompanyContractMaster(MCompanyContractMaster mCompanyContractMaster) {
		this.mCompanyContractMaster = mCompanyContractMaster;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

}