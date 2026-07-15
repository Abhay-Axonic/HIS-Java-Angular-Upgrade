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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.SpecialityMaster;
import com.param.global.model.SubSpecialityMaster;

@NamedQueries({
	@NamedQuery(name="GET_CAP_AMOUNTS_BY_CONTRACT_ID",query="SELECT departmentId as departmentId, "
			+ "subDepartmentId as subDepartmentId, "
			+ "departmentCapAmount as departmentCapAmount, "
			+ "productCategoryId as productCategoryId, "
			+ "quantity as quantity, "
			+ "amount as amount, "
			+ "orgId as organizationId, "
			+ "unitId as unitId, "
			+ "contractId as contractId, "
			+ "serviceId as serviceId, "
			+ "applicableDays as applicableDays, "
			+ "tillDischarge as tillDischarge ,"
			+ " contractId as contractId "
			+ "FROM TContractCapDetails "
			+ "WHERE contractId IN (:contractId) "
			+ "AND (departmentCapAmount > 0 OR serviceId is not null "
			+ "OR productCategoryId is not null)"),
	
	
	@NamedQuery(name="GET_CAP_AMOUNTS_BY_CONTRACT_ID_FOR_PROCEDURE",query="SELECT departmentId as departmentId, "
			+ "capAmount as amount, "
			+ "orgId as organizationId, "
			+ "unitId as unitId, "
			+ "contractId as contractId, "
			+ "procedureId as serviceId  "
			+ "FROM  ContractProcedureDetails "
			+ "WHERE contractId IN (:contractId) "
			+ "AND (capAmount > 0 OR procedureId is not null) ")
})


@Entity
@Table(name = "t_contract_cap_details" , schema = "service")
@SequenceGenerator(name = "t_contract_cap_details_seq" , sequenceName = "service.t_contract_cap_details_seq" , allocationSize = 1)
public class TContractCapDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_contract_cap_details_seq")
	@Column(name = "contract_cap_details_id")
	private Integer contractCapDetailsId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
	@Column(name = "sub_department_id")
	private Integer subDepartmentId;
	
	@Column(name = "department_cap_amount")
	private Double departmentCapAmount;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "product_category_id")
	private Integer productCategoryId;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "prod_cat_co_share")
	private Double prodCatCoShare;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "applicable_days")
	private Integer applicableDays;
	
	@Column(name = "till_discharge")
	private Boolean tillDischarge;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "contract_id" , insertable = false , updatable = false , nullable = false)
	private MCompanyContractMaster mCompanyContractMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "department_id" , insertable = false , updatable = false , nullable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "sub_department_id" , insertable = false , updatable = false , nullable = false)
	private SubSpecialityMaster subSpecialityMaster;

	public Boolean getTillDischarge() {
		return tillDischarge;
	}

	public void setTillDischarge(Boolean tillDischarge) {
		this.tillDischarge = tillDischarge;
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

	public Double getProdCatCoShare() {
		return prodCatCoShare;
	}

	public void setProdCatCoShare(Double prodCatCoShare) {
		this.prodCatCoShare = prodCatCoShare;
	}

	public Integer getContractCapDetailsId() {
		return contractCapDetailsId;
	}

	public void setContractCapDetailsId(Integer contractCapDetailsId) {
		this.contractCapDetailsId = contractCapDetailsId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public MCompanyContractMaster getmCompanyContractMaster() {
		return mCompanyContractMaster;
	}

	public void setmCompanyContractMaster(MCompanyContractMaster mCompanyContractMaster) {
		this.mCompanyContractMaster = mCompanyContractMaster;
	}

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}

	public SubSpecialityMaster getSubSpecialityMaster() {
		return subSpecialityMaster;
	}

	public void setSubSpecialityMaster(SubSpecialityMaster subSpecialityMaster) {
		this.subSpecialityMaster = subSpecialityMaster;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getDepartmentCapAmount() {
		return departmentCapAmount;
	}

	public void setDepartmentCapAmount(Double departmentCapAmount) {
		this.departmentCapAmount = departmentCapAmount;
	}
	
}
