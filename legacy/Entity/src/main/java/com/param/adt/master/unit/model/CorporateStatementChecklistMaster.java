package com.param.adt.master.unit.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.AdmissionDetails;
import com.param.adt.master.global.model.BedMaster;
import com.param.adt.master.global.model.HierarchyMaster;
import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.transfer.model.Transfer;
import com.param.billing.global.transaction.model.TariffBillingBedCategoryMapper;
import com.param.service.global.model.MCompanyContractMaster;
import com.param.service.global.model.MPackageMaster;

@NamedQueries({
	
	
	
	@NamedQuery(name="GET_CORPORATE_STATEMENT_CHECKLIST_BY_NAME",
	query="SELECT cscm.corporateStatementChecklistId as corporateStatementChecklistId, "
			+ "cscm.checkListDesc as checkListDesc "
			+ "FROM CorporateStatementChecklistMaster cscm "
			+ "WHERE cscm.organizationId=:orgId "
			+ "AND cscm.unitId=:unitId "
			+ "AND LOWER(cscm.checkListDesc)=:desc"),
	
	
	@NamedQuery(name="GET_CORPORATE_STATEMENT_CHECKLIST_MASTER_LIST",
	query="SELECT bbc.corporateStatementChecklistId as corporateStatementChecklistId, "
			+ " bbc.checkListDesc as checkListDesc ,"
			+ " bbc.checkListCode as checkListCode ,"
			+ " bbc.status as status "
			+ " FROM CorporateStatementChecklistMaster bbc "
			+ " WHERE bbc.organizationId=:orgId "
			+ " AND bbc.unitId=:unitId "),	
	
	@NamedQuery(name="GET_ACTIVE_CORPORATE_STATEMENT_CHECKLIST_LIST",
	     query="SELECT bbc.corporateStatementChecklistId as corporateStatementChecklistId, "
			+ "bbc.checkListDesc as checkListDesc "
			+ "FROM CorporateStatementChecklistMaster bbc "
			+ "WHERE bbc.status='A' "
			+ "AND bbc.organizationId=:orgId "
			+ "AND bbc.unitId=:unitId order by bbc.corporateStatementChecklistId "),
	
	@NamedQuery(name="GET_CORPORATE_STATEMENT_CHECKLIST_BY_ID",
	query="SELECT bbc.corporateStatementChecklistId as corporateStatementChecklistId, "
			+ "bbc.checkListDesc as checkListDesc ,"
			+ "bbc.checkListCode as checkListCode "
			+ "FROM CorporateStatementChecklistMaster bbc "
			+ "WHERE bbc.unitId=:unitId "
			+ "AND bbc.organizationId=:orgId "
			+ "AND bbc.corporateStatementChecklistId=:corporateStatementChecklistId"),
	
	@NamedQuery(name="GET_CORPORATE_STATEMENT_CHECKLIST_BY_NAME_NOT_BY_ID",
	query="SELECT bbc.corporateStatementChecklistId as corporateStatementChecklistId, "
			+ "bbc.checkListDesc as checkListDesc "
			+ "FROM CorporateStatementChecklistMaster bbc "
			+ "WHERE bbc.corporateStatementChecklistId !=:corporateStatementChecklistId "
			+ "AND LOWER(bbc.checkListDesc)=:desc "
			+ "AND bbc.organizationId=:orgId "
			+ "AND bbc.unitId=:unitId "),
	
})

@Entity
@Table(name="m_corporate_statement_checklist_master",schema="adt")
@SequenceGenerator(name="corporate_statement_checklist_master_seq",sequenceName="adt.corporate_statement_checklist_master_seq",allocationSize=1)
public class CorporateStatementChecklistMaster 
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="corporate_statement_checklist_master_seq")
	@Column(name="corporate_statement_checklist_id")
	private Integer corporateStatementChecklistId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="check_list_code")
	private String checkListCode;
	
	@Column(name="check_list_desc")
	private String checkListDesc;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billingBedCategoryMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillingBedVisitTypeMapper> bedVisitTypeMappersList;
	

	public Integer getCorporateStatementChecklistId() {
		return corporateStatementChecklistId;
	}

	public void setCorporateStatementChecklistId(Integer corporateStatementChecklistId) {
		this.corporateStatementChecklistId = corporateStatementChecklistId;
	}

	public String getCheckListCode() {
		return checkListCode;
	}

	public void setCheckListCode(String checkListCode) {
		this.checkListCode = checkListCode;
	}

	public String getCheckListDesc() {
		return checkListDesc;
	}

	public void setCheckListDesc(String checkListDesc) {
		this.checkListDesc = checkListDesc;
	}

	public List<BillingBedVisitTypeMapper> getBedVisitTypeMappersList() {
		return bedVisitTypeMappersList;
	}

	public void setBedVisitTypeMappersList(List<BillingBedVisitTypeMapper> bedVisitTypeMappersList) {
		this.bedVisitTypeMappersList = bedVisitTypeMappersList;
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


	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}

}
