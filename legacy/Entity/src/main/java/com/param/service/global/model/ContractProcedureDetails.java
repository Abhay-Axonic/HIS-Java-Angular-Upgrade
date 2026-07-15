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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.SpecialityMaster;

@Entity
@Table(name = "t_contract_procedure_details" , schema = "service")
@SequenceGenerator(name = "contract_procedure_details_seq" , sequenceName = "service.contract_procedure_details_seq" , allocationSize = 1)
public class ContractProcedureDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "contract_procedure_details_seq")
	@Column(name = "contract_procedure_details_id")
	private Integer contractProcedureDetailsId;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "contract_id")
	private Integer contractId;
	
	@Column(name = "department_id")
	private Integer departmentId;
	
	@Column(name = "procedure_id")
	private Integer procedureId;
	
	@Column(name = "cap_amount")
	private Double capAmount;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="status")
	private Character status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "contract_id" , insertable = false , updatable = false , nullable = false)
	private MCompanyContractMaster mCompanyContractMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name  = "department_id" , insertable = false , updatable = false , nullable = false)
	private SpecialityMaster specialityMaster;

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getContractProcedureDetailsId() {
		return contractProcedureDetailsId;
	}

	public void setContractProcedureDetailsId(Integer contractProcedureDetailsId) {
		this.contractProcedureDetailsId = contractProcedureDetailsId;
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

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public Double getCapAmount() {
		return capAmount;
	}

	public void setCapAmount(Double capAmount) {
		this.capAmount = capAmount;
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

	public SpecialityMaster getSpecialityMaster() {
		return specialityMaster;
	}

	public void setSpecialityMaster(SpecialityMaster specialityMaster) {
		this.specialityMaster = specialityMaster;
	}
	
	
	
}
