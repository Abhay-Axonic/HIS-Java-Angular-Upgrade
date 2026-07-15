package com.param.otims.model;

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

import com.param.adt.master.global.model.ConsentMaster;


@NamedQueries({
	
	@NamedQuery(name="GET_CONSENT_LIST_BY_PROCEDURE_CONFIG_ID",
	query="SELECT mstr.procedureConfigConsentId as procedureConfigConsentId , " 
				+ " mstr.procedureConfigId as procedureConfigId , "
				+ " mstr.consentId as consentId, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " consentMaster.consentMasterName as consentMasterName, "
				+ " consentTypeMaster.consentTypeMasterName as consentTypeMasterName, "
				+ " consentMaster.filePath as filePath, "
				+ " consentMaster.documentName as documentName, "
				+ " consentMaster.templateType as templateType, "
				+ " consentMaster.template as template, "
				+ " mstr.status as status "
				+ " FROM ProcedureConfigurationConsent mstr "
				+ " INNER JOIN  mstr.consentMaster consentMaster  "
				+ " INNER JOIN  consentMaster.consentTypeMaster consentTypeMaster  "
                + " INNER JOIN  mstr.procedureConfiguration procedureConfiguration  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				//+ " AND mstr.procedureConfigId= :procedureConfigId "
				+ " AND mstr.status = 'A' "
				)
})
@Entity
@Table(name = "t_procedure_config_consent" , schema = "otims")
@SequenceGenerator(name = "procedure_config_consent_seq" , sequenceName = "otims.procedure_config_consent_seq" , allocationSize = 1)
public class ProcedureConfigurationConsent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_config_consent_seq")
	@Column(name = "procedure_config_consent_id")
	private Integer procedureConfigConsentId;
	
	@Column(name = "procedure_config_id")
	private Integer procedureConfigId;
	
	@Column(name = "consent_id")
	private Integer consentId ;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consent_id", insertable = false, updatable = false)
	private  ConsentMaster consentMaster;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_config_id", insertable = false, updatable = false)
    private ProcedureConfiguration procedureConfiguration;


	public Integer getProcedureConfigConsentId() {
		return procedureConfigConsentId;
	}

	public void setProcedureConfigConsentId(Integer procedureConfigConsentId) {
		this.procedureConfigConsentId = procedureConfigConsentId;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}

	public Integer getConsentId() {
		return consentId;
	}

	public void setConsentId(Integer consentId) {
		this.consentId = consentId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
