package com.param.otims.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name = "GET_ANESTHESIA_TYPE_LIST",
				query= "SELECT	ans.anesthesiaTypeId as anesthesiaTypeId, ans.anesthesiaTypeDesc as anesthesiaTypeDesc, ans.anesthesiaTypeName as anesthesiaTypeName, "
					 + "		ans.anesthesiaTypeCode as anesthesiaTypeCode, ans.status as status, ans.orgId as orgId, ans.unitId as unitId, ans.createdBy as createdBy, "
					 + "		to_char(ans.createdDate,'DD-MM-YYYY HH24:MI:SS') as createdDate, ans.updatedBy as updatedBy, to_char(ans.updatedDate,'DD-MM-YYYY HH24:MI:SS') as updatedDate "
					 + "FROM	AnesthesiaTypeMaster ans "
					 + "WHERE  ans.status = 'A' and ans.orgId =:orgId and ans.unitId =:unitId "
					 + "ORDER BY ans.anesthesiaTypeName"),
	
				@NamedQuery(name = "GET_LIST_ANESTHESIA_TYPE_MASTER",
				query= "SELECT ans.anesthesiaTypeId as anesthesiaTypeId,"
						+ " ans.anesthesiaTypeDesc as anesthesiaTypeDesc,"
						+ " ans.anesthesiaTypeName as anesthesiaTypeName,"
						+ " ans.anesthesiaTypeCode as anesthesiaTypeCode,"
						+ " ans.status as status,"
						+ " ans.orgId as orgId,"
						+ " ans.unitId as unitId "
						+ " FROM	AnesthesiaTypeMaster ans "
						+ " WHERE  ans.orgId =:orgId "
						+ " AND ans.unitId =:unitId "
						+ " ORDER BY ans.anesthesiaTypeId"),
				
				@NamedQuery(name = "GET_ANESTHESIA_TYPE_MASTER_BY_ID",
				query= "SELECT ans.anesthesiaTypeId as anesthesiaTypeId,"
						+ " ans.anesthesiaTypeDesc as anesthesiaTypeDesc,"
						+ " ans.anesthesiaTypeName as anesthesiaTypeName,"
						+ " ans.anesthesiaTypeCode as anesthesiaTypeCode,"
						+ " ans.status as status, ans.orgId as orgId,"
						+ " ans.unitId as unitId "
						+ " FROM	AnesthesiaTypeMaster ans "
						+ " WHERE  ans.orgId =:orgId "
						+ " AND ans.unitId =:unitId "
						+ " AND ans.anesthesiaTypeId =:anesthesiaTypeId"
						+ " ORDER BY ans.anesthesiaTypeId"),
				
				@NamedQuery(name = "GET_ANESTHESIA_TYPE_MASTER_BY_NAME",
				query= "SELECT ans.anesthesiaTypeId as anesthesiaTypeId,"
						+ " ans.anesthesiaTypeName as anesthesiaTypeName "
						+ " FROM AnesthesiaTypeMaster ans "
						+ " WHERE (LOWER(ans.anesthesiaTypeName) =:anesthesiaTypeName)" //dilip
						+ " AND  ans.orgId =:orgId  AND ans.unitId =:unitId" //dilip
						),
				
				@NamedQuery(name = "GET_ANESTHESIA_TYPE_MASTER_BY_NAME_NOT_ID",
				query= "SELECT ans.anesthesiaTypeId as anesthesiaTypeId,"
						+ " ans.anesthesiaTypeName as anesthesiaTypeName"
						+ " FROM AnesthesiaTypeMaster ans "
						+ " WHERE "
						+ " (LOWER(ans.anesthesiaTypeName) =:anesthesiaTypeName) " //dilip
						+ " AND ans.anesthesiaTypeId != :anesthesiaTypeId AND ans.orgId =:orgId  AND ans.unitId =:unitId" //dilip
						)
})
@Entity
@Table(name = "m_anesthesia_type_master" , schema = "otims")
@SequenceGenerator(name = "m_anesthesia_type_master_seq" , sequenceName = "otims.m_anesthesia_type_master_seq" , allocationSize = 1)
public class AnesthesiaTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_anesthesia_type_master_seq")
	@Column(name = "anesthesia_type_id")
	private int anesthesiaTypeId;
	
	@Column(name = "anesthesia_type_desc")
	private String anesthesiaTypeDesc;
	
	@Column(name = "anesthesia_type_name")
	private String anesthesiaTypeName;
	
	@Column(name = "anesthesia_type_code")
	private String anesthesiaTypeCode;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "anasthesiaTypeMaster")
	private List<ProcedureConfiguration> listProcedureConfiguration;

	public List<ProcedureConfiguration> getListProcedureConfiguration() {
		return listProcedureConfiguration;
	}

	public void setListProcedureConfiguration(List<ProcedureConfiguration> listProcedureConfiguration) {
		this.listProcedureConfiguration = listProcedureConfiguration;
	}

	public int getAnesthesiaTypeId() {
		return anesthesiaTypeId;
	}

	public void setAnesthesiaTypeId(int anesthesiaTypeId) {
		this.anesthesiaTypeId = anesthesiaTypeId;
	}

	public String getAnesthesiaTypeDesc() {
		return anesthesiaTypeDesc;
	}

	public void setAnesthesiaTypeDesc(String anesthesiaTypeDesc) {
		this.anesthesiaTypeDesc = anesthesiaTypeDesc;
	}

	public String getAnesthesiaTypeName() {
		return anesthesiaTypeName;
	}

	public void setAnesthesiaTypeName(String anesthesiaTypeName) {
		this.anesthesiaTypeName = anesthesiaTypeName;
	}

	public String getAnesthesiaTypeCode() {
		return anesthesiaTypeCode;
	}

	public void setAnesthesiaTypeCode(String anesthesiaTypeCode) {
		this.anesthesiaTypeCode = anesthesiaTypeCode;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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
	
}
