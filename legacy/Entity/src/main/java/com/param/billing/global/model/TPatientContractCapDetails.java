package com.param.billing.global.model;

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
	@NamedNativeQuery(name = "GET_EXISITNG_CONTARCT_DETAILS_BY_ENCOUNTERID_VISITTYPE_ADMISSIONID",
					 query = "SELECT"
							 + "   psd.patient_sponsor_details_id AS \"patientSponsorDetailsId\","
							 + "   psd.contract_id AS \"contractId\"  "
							 + "   FROM  opd.t_patient_sponsor_details psd"
							 + " WHERE"
							 + "    psd.visit_type_id =:visitTypeId"
							 + "    AND psd.encounter_id =:encounterId"
						/*	 + "    OR psd.admission_id=:admissionId"*/
						     + " GROUP BY"
							 + "   psd.patient_sponsor_details_id,"
							 + "   psd.contract_id"),

/*	@NamedNativeQuery(name = "GET_CONTARCT_CONSUMPTION_SERVICE_DETAILS_BY_CONTARCTID_SERVICEID",
	 query = "SELECT"
			 + "   csd.contract_id AS \"contractId\","
			 + "   csd.final_price AS \"finalPrice\","
			 + "   false AS \"isfromCap\" "
			 + " FROM"
			 + "   service.t_contract_service_details csd"
			 + " WHERE"
			 + "   csd.contract_id =:contractId"
			 + "   AND csd.service_id =:serviceId")*/
})
@Entity
@Table(name = "t_patient_contract_cap_details", schema = "billing")
@SequenceGenerator(name = "t_seq_patient_contract_cap_details", sequenceName = "billing.t_seq_patient_contract_cap_details", allocationSize = 1)
public class TPatientContractCapDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_patient_contract_cap_details")
	@Column(name = "patient_cont_cap_detls_id")
	private int patientContCapDetlsId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "department_id")
	private Integer DepartmentId; 
	
	@Column(name = "department_cap_amount")
	private Double departmentCapAmount;
	
	@Column(name = "department_balance_cap_amount")
	private Double departmentBalanceCapAmount;
	
	@Column(name = "sub_department_id")
	private Integer subDepartmentId;
	
	@Column(name = "sub_department_cap_amount")
	private Double subDepartmentCapAmount;
	
	@Column(name = "sub_department_balance_cap_amount")
	private Double subDepartmentBalanceCapAmount;
	
	@Column(name = "product_category_cap_amount")
	private Double productCategoryCapAmount;
	
	@Column(name = "product_category_balance_cap_amount")
	private Double productCategoryBalanceCapAmount;

	@Column(name = "is_service_item")
	private Integer isServiceItem;
	
	@Column(name = "product_category_id")
	private Integer productCategoryId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "org_id")
	private Integer orgnisationId;
	
	@Column(name = "org_unit_id")
	private Integer unitId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public int getPatientContCapDetlsId() {
		return patientContCapDetlsId;
	}

	public void setPatientContCapDetlsId(int patientContCapDetlsId) {
		this.patientContCapDetlsId = patientContCapDetlsId;
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

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getDepartmentId() {
		return DepartmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		DepartmentId = departmentId;
	}

	public Double getDepartmentCapAmount() {
		return departmentCapAmount;
	}

	public void setDepartmentCapAmount(Double departmentCapAmount) {
		this.departmentCapAmount = departmentCapAmount;
	}

	public Double getDepartmentBalanceCapAmount() {
		return departmentBalanceCapAmount;
	}

	public void setDepartmentBalanceCapAmount(Double departmentBalanceCapAmount) {
		this.departmentBalanceCapAmount = departmentBalanceCapAmount;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public Double getSubDepartmentCapAmount() {
		return subDepartmentCapAmount;
	}

	public void setSubDepartmentCapAmount(Double subDepartmentCapAmount) {
		this.subDepartmentCapAmount = subDepartmentCapAmount;
	}

	public Double getSubDepartmentBalanceCapAmount() {
		return subDepartmentBalanceCapAmount;
	}

	public void setSubDepartmentBalanceCapAmount(Double subDepartmentBalanceCapAmount) {
		this.subDepartmentBalanceCapAmount = subDepartmentBalanceCapAmount;
	}

	public Double getProductCategoryCapAmount() {
		return productCategoryCapAmount;
	}

	public void setProductCategoryCapAmount(Double productCategoryCapAmount) {
		this.productCategoryCapAmount = productCategoryCapAmount;
	}

	public Double getProductCategoryBalanceCapAmount() {
		return productCategoryBalanceCapAmount;
	}

	public void setProductCategoryBalanceCapAmount(Double productCategoryBalanceCapAmount) {
		this.productCategoryBalanceCapAmount = productCategoryBalanceCapAmount;
	}

	public Integer getIsServiceItem() {
		return isServiceItem;
	}

	public void setIsServiceItem(Integer isServiceItem) {
		this.isServiceItem = isServiceItem;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
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

	
	
}
