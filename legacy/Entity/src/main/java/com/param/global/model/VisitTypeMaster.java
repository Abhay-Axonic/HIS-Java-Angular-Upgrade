package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.VisitorPassMapper;
import com.param.billing.global.model.DepositMaster;
import com.param.billing.global.transaction.model.TariffVisitTypeMapper;
import com.param.otims.model.SurgeryResourceConfig;
import com.param.service.global.model.MCompanyContractMaster;
import com.param.service.global.model.MPackageMaster;
@NamedQueries({
	@NamedQuery(name="GET_ACTIVE_VISIT_TYPE_LIST",
				query =" SELECT visitTypeMaster.visitTypeId as visitTypeId,"
						+ " visitTypeMaster.visitTypeCode as visitTypeName,"
						+ " visitTypeMaster.visitTypeCode as visitTypeCode "
						+ " FROM VisitTypeMaster visitTypeMaster"
						+ " WHERE visitTypeMaster.status = 'A'"
						+ " ORDER BY visitTypeMaster.visitTypeCode ASC "),
	
	@NamedQuery(name="GET_VISIT_TYPE_LIST",
				query =" SELECT visitTypeMaster.visitTypeId as id,"
					+ " visitTypeMaster.visitTypeName as desc,"
					+ " visitTypeMaster.visitTypeCode as code ,"
					+ " visitTypeMaster.status AS status "
					+ " FROM VisitTypeMaster visitTypeMaster"),

	@NamedQuery(name="GET_VISIT_TYPE_BY_ID",
				query =" SELECT visitTypeMaster.visitTypeId as id,"
					+ " visitTypeMaster.visitTypeName as desc,"
					+ " visitTypeMaster.visitTypeCode as code ,"
					+ " visitTypeMaster.status AS status "
					+ " FROM VisitTypeMaster visitTypeMaster"
					+ " WHERE visitTypeMaster.visitTypeId=:visitId "),
	
	@NamedQuery(name="GET_VISIT_TYPE_BY_NAME",
			query =" SELECT visitTypeMaster.visitTypeId as visitTypeId "
				+ " FROM VisitTypeMaster visitTypeMaster "
				+ " WHERE LOWER(visitTypeMaster.visitTypeName) =:name "),
	
	@NamedQuery(name="GET_VISIT_TYPE_BY_NAME_NOT_BY_ID",
			query =" SELECT visitTypeMaster.visitTypeId as visitTypeId "				
				+ " FROM VisitTypeMaster visitTypeMaster "
				+ " WHERE LOWER(visitTypeMaster.visitTypeName) =:name "
				+ " AND visitTypeMaster.visitTypeId!=:visitId ")
		
})

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "m_visit_type_master", schema = "public")
@SequenceGenerator(name="visit_type_master_seq" , sequenceName ="public.visit_type_master_seq", allocationSize = 1)
public class VisitTypeMaster 
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="visit_type_master_seq")
	@Column(name="visit_type_id")
	private int visitTypeId;
	
	@Column(name="visit_type_name")
	private String visitTypeName;
	
	@Column(name="visit_type_code")
	private String visitTypeCode;
	
	@Column(name="created_by")
	private int createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="status")
	private char status;
	
	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "visitTypeMaster")
	private List<DepositMaster> listDepositMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "visitTypeMaster")
	private List<MPackageMaster> listMPackageMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "visitTypeMaster")
	private List<MCompanyContractMaster> listMCompanyContractMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "visitTypeMaster")
	private List<TariffVisitTypeMapper> tariffVisitTypeMapperList;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "visitTypeMaster")
	private List<VisitorPassMapper> visitorPassMapperList;
	

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "visitTypeMaster")
	private List<SurgeryResourceConfig> listSurgeryResourceConfig;
	

	public List<MCompanyContractMaster> getListMCompanyContractMaster() {
		return listMCompanyContractMaster;
	}

	public void setListMCompanyContractMaster(List<MCompanyContractMaster> listMCompanyContractMaster) {
		this.listMCompanyContractMaster = listMCompanyContractMaster;
	}
	
	public List<MPackageMaster> getListMPackageMaster() {
		return listMPackageMaster;
	}

	public void setListMPackageMaster(List<MPackageMaster> listMPackageMaster) {
		this.listMPackageMaster = listMPackageMaster;
	}

	public List<DepositMaster> getListDepositMaster() {
		return listDepositMaster;
	}

	public void setListDepositMaster(List<DepositMaster> listDepositMaster) {
		this.listDepositMaster = listDepositMaster;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	
	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(int visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitTypeName() {
		return visitTypeName;
	}

	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}

	public String getVisitTypeCode() {
		return visitTypeCode;
	}

	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public List<TariffVisitTypeMapper> getTariffVisitTypeMapperList() {
		return tariffVisitTypeMapperList;
	}

	public void setTariffVisitTypeMapperList(List<TariffVisitTypeMapper> tariffVisitTypeMapperList) {
		this.tariffVisitTypeMapperList = tariffVisitTypeMapperList;
	}

	public List<SurgeryResourceConfig> getListSurgeryResourceConfig() {
		return listSurgeryResourceConfig;
	}

	public void setListSurgeryResourceConfig(List<SurgeryResourceConfig> listSurgeryResourceConfig) {
		this.listSurgeryResourceConfig = listSurgeryResourceConfig;
	}
	
	
}
