package com.param.service.global.model;

import java.util.Date;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({ 
					@NamedQuery(name = "GET_CONTRACT_GRADE_MASTER_BY_ID",
							query = "SELECT  "
									+ " contractGradeMaster.contractGradeMasterId as contractGradeMasterId, "
									+ " contractGradeMaster.orgId AS orgId, "
									+ " contractGradeMaster.code AS code, "
									+ " contractGradeMaster.gradeDesc AS gradeDesc, "
									+ " contractGradeMaster.status AS status, "
									+ " contractGradeMaster.unitId AS unitId "
									+ " FROM ContractGradeMaster contractGradeMaster  "
									+ " WHERE contractGradeMaster.contractGradeMasterId= :contractGradeMasterId "
									+ " AND contractGradeMaster.unitId= :unitId "
									+ " AND contractGradeMaster.orgId= :orgId"
									),
					
					@NamedQuery(name = "GET_CONTRACT_GRADE_MASTER_List",
							query = "SELECT  "
									+ " contractGradeMaster.contractGradeMasterId as contractGradeMasterId, "
									+ " contractGradeMaster.orgId AS orgId, "
									+ " contractGradeMaster.code AS code, "
									+ " contractGradeMaster.gradeDesc AS gradeDesc, "
									+ " contractGradeMaster.status AS status, "
									+ " contractGradeMaster.unitId AS unitId "
									+ " FROM ContractGradeMaster contractGradeMaster "
									+ " WHERE contractGradeMaster.unitId=:unitId "
									+ " AND contractGradeMaster.orgId=:orgId"
									),
					
					@NamedQuery(name = "GET_ACTIVE_CONTRACT_GRADE_MASTER_LIST",
					query = "SELECT "
							+ " contractGradeMaster.contractGradeMasterId as contractGradeMasterId, "
							+ " contractGradeMaster.orgId AS orgId, "
							+ " contractGradeMaster.code AS code, "
							+ " contractGradeMaster.gradeDesc AS gradeDesc, "
							+ " contractGradeMaster.status AS status, "
							+ " contractGradeMaster.unitId AS unitId "
							+ " FROM ContractGradeMaster contractGradeMaster "
							+ " WHERE contractGradeMaster.unitId= :unitId  "
							+ " AND contractGradeMaster.orgId=:orgId "
							+ " AND contractGradeMaster.status='A' " 
							),
					

					@NamedQuery(name = "GET_CONTRACT_GRADE_MASTER_BY_NAME",
					query = "SELECT "
							+ " contractGradeMaster.contractGradeMasterId as contractGradeMasterId, "
							+ " contractGradeMaster.gradeDesc AS gradeDesc, "
							+ " contractGradeMaster.unitId AS unitId "
							+ " FROM ContractGradeMaster contractGradeMaster "
							+ " WHERE LOWER(contractGradeMaster.gradeDesc)= :gradeDesc "
							+ " AND contractGradeMaster.unitId= :unitId  "
							+ " AND contractGradeMaster.orgId=:orgId "
							),
					
					@NamedQuery(name = "GET_CONTRACT_GRADE_MASTER_BY_NAME_NOT_ID",
					query = "SELECT "
							+ " contractGradeMaster.contractGradeMasterId as contractGradeMasterId, "
							+ " contractGradeMaster.gradeDesc AS gradeDesc "
							+ " FROM ContractGradeMaster contractGradeMaster "
							+ " WHERE contractGradeMaster.contractGradeMasterId != :contractGradeMasterId"
							+ " AND LOWER(contractGradeMaster.gradeDesc)= :gradeDesc " 
							)
				
					
})
			@NamedNativeQuery(
					name = "GET_CONTRACT_GRADE_MAPPER_NOT_MAPPED_IN_MAPPER",
					query = "SELECT "
							+ " contractGradeMaster.contract_grade_master_id as \"contractGradeMasterId\", "
							+ " contractGradeMaster.code AS \"code\", "
							+ " contractGradeMaster.grade_desc AS \"gradeDesc\" "
							+ " FROM service.m_contract_grade_master contractGradeMaster "
							+ " WHERE contractGradeMaster.contract_grade_master_id "
							+ " NOT IN "
							+ " (select mpper.contract_grade_master_id AS \"contractGradeMasterMapperId\"  "
							+ "       from service.t_company_contarct_grade_mpper mpper "
							+ "        where mpper.status='A' )"
							+ " And  contractGradeMaster.org_id=:orgId and contractGradeMaster.unit_id= :unitId " 
							)
@Entity
@Table(name = "m_contract_grade_master" , schema = "service")
@SequenceGenerator(name = "m_contract_grade_master_seq" , sequenceName = "service.m_contract_grade_master_seq" , allocationSize = 1)
public class ContractGradeMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_contract_grade_master_seq")
	@Column(name = "contract_grade_master_id")
	private int contractGradeMasterId;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "grade_desc")
	private String gradeDesc;
	
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
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "associatedCompanyMaster")
	private List<MCompanyContractMaster> listMCompanyContractMaster;

	public List<MCompanyContractMaster> getListMCompanyContractMaster() {
		return listMCompanyContractMaster;
	}

	public void setListMCompanyContractMaster(List<MCompanyContractMaster> listMCompanyContractMaster) {
		this.listMCompanyContractMaster = listMCompanyContractMaster;
	}

	public int getContractGradeMasterId() {
		return contractGradeMasterId;
	}

	public void setContractGradeMasterId(int contractGradeMasterId) {
		this.contractGradeMasterId = contractGradeMasterId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGradeDesc() {
		return gradeDesc;
	}

	public void setGradeDesc(String gradeDesc) {
		this.gradeDesc = gradeDesc;
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
	
}