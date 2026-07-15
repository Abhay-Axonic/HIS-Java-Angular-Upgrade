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
				@NamedQuery(name = "GET_PROCEDURE_TYPE_LIST",
							query= "SELECT	prc.procedureTypeId as procedureTypeId, prc.procedureTypeName as procedureTypeName, prc.procedureTypeDesc as procedureTypeDesc, "
								+ "			prc.procedureTypeCode as procedureTypeCode, prc.status as status, prc.orgId as orgId, prc.unitId as unitId, prc.createdBy as createdBy, "
								+ "			to_char(prc.createdDate, 'DD-MM-YYYY HH24:MI:SS') as createdDate, prc.updatedBy as updatedBy, to_char(prc.updatedDate,'DD-MM-YYYY HH24:MI:SS') as updatedDate "
								+ "FROM		ProcedureTypeMaster prc "
								+ "WHERE	prc.status ='A' and prc.orgId =:orgId and prc.unitId =:unitId "
								+ "ORDER BY prc.procedureTypeName "),
				
				@NamedQuery(name = "GET_LIST_PROCEDURE_TYPE_MASTER",
						query= "SELECT "
								+ " prc.procedureTypeId AS procedureTypeId, "
								+ " prc.procedureTypeName AS procedureTypeName,"
								+ " prc.procedureTypeDesc AS procedureTypeDesc,"
								+ " prc.procedureTypeCode AS procedureTypeCode,"
								+ " prc.status AS status,"
								+ " prc.orgId AS orgId,"
								+ " prc.unitId AS unitId"
								+ " FROM ProcedureTypeMaster prc "
								+ " WHERE prc.orgId= :orgId"
								+ " AND prc.unitId= :unitId"
								+ " ORDER BY prc.procedureTypeId asc"),
				
				@NamedQuery(name = "GET_PROCEDURE_TYPE_MASTER_BY_ID",
						query= "SELECT "
								+ " prc.procedureTypeId AS procedureTypeId, "
								+ " prc.procedureTypeName AS procedureTypeName,"
								+ " prc.procedureTypeDesc AS procedureTypeDesc,"
								+ " prc.procedureTypeCode AS procedureTypeCode,"
								+ " prc.status AS status,"
								+ " prc.orgId AS orgId,"
								+ " prc.unitId AS unitId"
								+ " FROM ProcedureTypeMaster prc "
								+ " WHERE prc.orgId= :orgId"
								+ " AND prc.unitId= :unitId"
								+ " AND prc.procedureTypeId= :procedureTypeId"
								+ " ORDER BY prc.procedureTypeId asc"),
				
				@NamedQuery(name = "GET_PROCEDURE_TYPE_MASTER_BY_NAME",
						query= "SELECT "
								+ " prc.procedureTypeId AS procedureTypeId, "
								+ " prc.procedureTypeName AS procedureTypeName"
								+ " FROM ProcedureTypeMaster prc "
								+ " WHERE LOWER(prc.procedureTypeName)= :procedureTypeName AND prc.orgId= :orgId "
								+ " AND prc.unitId= :unitId "
								+ " ORDER BY prc.procedureTypeId asc"),
				
				@NamedQuery(name = "GET_PROCEDURE_TYPE_MASTER_BY_NAME_NOT_ID",
						query= "SELECT "
								+ " prc.procedureTypeId AS procedureTypeId, "
								+ " prc.procedureTypeName AS procedureTypeName"
								+ " FROM ProcedureTypeMaster prc "
								+ " WHERE prc.procedureTypeId != :procedureTypeId"
								+ " AND LOWER(prc.procedureTypeName) = :procedureTypeName AND prc.orgId= :orgId "
								+ " AND prc.unitId= :unitId"
								+ " ORDER BY prc.procedureTypeId asc")
})
@Entity
@Table(name = "m_procedure_type_master" , schema = "otims")
@SequenceGenerator(name = "m_procedure_type_master_seq" , sequenceName = "otims.m_procedure_type_master_seq" , allocationSize = 1)
public class ProcedureTypeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_procedure_type_master_seq")
	@Column(name = "procedure_type_id")
	private int procedureTypeId;
	
	@Column(name = "procedure_type_name")
	private String procedureTypeName;
	
	@Column(name = "procedure_type_desc")
	private String procedureTypeDesc;
	
	@Column(name = "procedure_type_code")
	private String procedureTypeCode;
	
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
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "procedureTypeMaster")
	private List<ProcedureConfiguration> listProcedureConfiguration;

	public List<ProcedureConfiguration> getListProcedureConfiguration() {
		return listProcedureConfiguration;
	}

	public void setListProcedureConfiguration(List<ProcedureConfiguration> listProcedureConfiguration) {
		this.listProcedureConfiguration = listProcedureConfiguration;
	}

	public int getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(int procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public String getProcedureTypeName() {
		return procedureTypeName;
	}

	public void setProcedureTypeName(String procedureTypeName) {
		this.procedureTypeName = procedureTypeName;
	}

	public String getProcedureTypeDesc() {
		return procedureTypeDesc;
	}

	public void setProcedureTypeDesc(String procedureTypeDesc) {
		this.procedureTypeDesc = procedureTypeDesc;
	}

	public String getProcedureTypeCode() {
		return procedureTypeCode;
	}

	public void setProcedureTypeCode(String procedureTypeCode) {
		this.procedureTypeCode = procedureTypeCode;
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
