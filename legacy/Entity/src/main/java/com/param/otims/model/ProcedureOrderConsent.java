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
	
	@NamedQuery(name="GET_CONSENT_DETAILS_BY_PROCEDURE_ORDER_ID",
			query="SELECT ct.consentMasterName as consentMasterName, "
					+ "mstr.procedureOrderConsentId as procedureOrderConsentId, "
					+ "mstr.consentId as consentId, "
					+ "ct.filePath as filePath, "
					+ "ct.documentName as documentName, "
					+ "ct.template as template,"
					+ "ct.templateType as templateType, "
					+ "ctt.consentTypeMasterName as consentTypeMasterName, "
					+ " mstr.organizationId as organizationId, "
					+ " mstr.procedureRequestId as procedureRequestId , "
					+ " mstr.procedureOrderId as procedureOrderId , "
					+ " mstr.unitId as unitId, "
					+ " mstr.createdBy as createdBy, "
					+ " mstr.updatedBy as updatedBy, "
					+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
					+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
					+ " mstr.status as status "
					+ "FROM ProcedureOrderConsent  mstr "
					+ "INNER JOIN mstr.ConsentMaster  ct "
					+ "INNER JOIN ct.consentTypeMaster ctt "
					+ "WHERE mstr.organizationId=:organizationId "
					+ " and mstr.unitId =:unitId "
					+ " and mstr.procedureOrderId =:procedureOrderId ")
})



@Entity	
@Table(name = "t_procedure_order_consent", schema = "otims")
@SequenceGenerator(name = "procedure_order_consent_seq", sequenceName = "otims.procedure_order_consent_seq", allocationSize = 1)
public class ProcedureOrderConsent {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_order_consent_seq")
	@Column(name = "procedure_order_consent_id")
	private Integer procedureOrderConsentId;
	
	@Column(name = "procedure_request_id")
	private Integer procedureRequestId;
	
	@Column(name = "consent_id")
	private Integer consentId ;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId ;
	
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
	private ConsentMaster ConsentMaster;

	public Integer getProcedureOrderConsentId() {
		return procedureOrderConsentId;
	}

	public void setProcedureOrderConsentId(Integer procedureOrderConsentId) {
		this.procedureOrderConsentId = procedureOrderConsentId;
	}

	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
	}

	public Integer getConsentId() {
		return consentId;
	}

	public void setConsentId(Integer consentId) {
		this.consentId = consentId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
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
