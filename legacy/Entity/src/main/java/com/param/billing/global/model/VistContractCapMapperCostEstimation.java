package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_vist_contract_cap_mapper_cost_estimation",schema="service")
public class VistContractCapMapperCostEstimation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vist_contract_cap_mapper_id")
	private Integer vistContractCapMapperId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="unit_id")
	private Integer unitid;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="contract_id")
	private Integer contractId;
	
	@Column(name="department_id")
	private Integer departmentId;
	
	@Column(name="sub_department_id")
	private Integer subDepartmentId;
	
	@Column(name="department_cap_amount")
	private Double departmentCapAmount;
	
	@Column(name="balance_cap_amount")
	private Double balanceCapAmount;
	
	@Column(name="productCategoryId")
	private Integer productCategoryId;
	
	@Column(name="quantity")
	private Double quantity;
	
	@Column(name="balance_quantity")
	private Double balanceQuantity;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name="amount")
	private Double amount;//ganesh
	
	@Column(name="balance_amount")
	private Double balanceAmount;//ganesh

	@Column(name = "patient_id")
	private Integer patientId;//ganesh
	
	@Column(name="service_id")
	private Integer serviceId;

	@Column(name = "applicable_days")
	private Integer applicableDays;
	
	@Column(name = "applicable_date")
	private Date applicableDate;
	
	@Column(name = "till_discharge")
	private Boolean tillDischarge;
	
	public Boolean getTillDischarge() {
		return tillDischarge;
	}

	public void setTillDischarge(Boolean tillDischarge) {
		this.tillDischarge = tillDischarge;
	}

	public Date getApplicableDate() {
		return applicableDate;
	}

	public void setApplicableDate(Date applicableDate) {
		this.applicableDate = applicableDate;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getApplicableDays() {
		return applicableDays;
	}

	public void setApplicableDays(Integer applicableDays) {
		this.applicableDays = applicableDays;
	}

	public Double getBalanceCapAmount() {
		return balanceCapAmount;
	}

	public void setBalanceCapAmount(Double balanceCapAmount) {
		this.balanceCapAmount = balanceCapAmount;
	}

	public Integer getVistContractCapMapperId() {
		return vistContractCapMapperId;
	}

	public void setVistContractCapMapperId(Integer vistContractCapMapperId) {
		this.vistContractCapMapperId = vistContractCapMapperId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitid() {
		return unitid;
	}

	public void setUnitid(Integer unitid) {
		this.unitid = unitid;
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

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}


	public Double getDepartmentCapAmount() {
		return departmentCapAmount;
	}

	public void setDepartmentCapAmount(Double departmentCapAmount) {
		this.departmentCapAmount = departmentCapAmount;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getBalanceQuantity() {
		return balanceQuantity;
	}

	public void setBalanceQuantity(Double balanceQuantity) {
		this.balanceQuantity = balanceQuantity;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(Double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	
}
