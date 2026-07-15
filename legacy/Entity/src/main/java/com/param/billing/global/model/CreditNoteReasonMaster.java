package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedQueries({
				@NamedQuery(name="GET_CREDIT_NOTE_REASON_MASTER_LIST",
						query=" SELECT "
								+ " creditNoteReasonMaster.creditNoteReasonId AS creditNoteReasonId, creditNoteReasonMaster.code AS code, "
								+ " creditNoteReasonMaster.reasonDesc AS reasonDesc, creditNoteReasonMaster.status AS status, "
								+ " creditNoteReasonMaster.organisationId AS organisationId, creditNoteReasonMaster.unitId AS unitId"
								+ " FROM CreditNoteReasonMaster creditNoteReasonMaster WHERE creditNoteReasonMaster.organisationId= :organisationId AND creditNoteReasonMaster.unitId= :unitId"
								+ " ORDER BY creditNoteReasonMaster.creditNoteReasonId asc"
								),
				@NamedQuery(name="GET_CREDIT_NOTE_REASON_MASTER_BY_ID",
						query=" SELECT "
								+ " creditNoteReasonMaster.creditNoteReasonId AS creditNoteReasonId, creditNoteReasonMaster.code AS code, "
								+ " creditNoteReasonMaster.reasonDesc AS reasonDesc, creditNoteReasonMaster.status AS status, "
								+ " creditNoteReasonMaster.organisationId AS organisationId, creditNoteReasonMaster.unitId AS unitId"
								+ " FROM CreditNoteReasonMaster creditNoteReasonMaster WHERE creditNoteReasonMaster.organisationId= :organisationId AND creditNoteReasonMaster.unitId= :unitId"
								+ " AND creditNoteReasonMaster.creditNoteReasonId= :creditNoteReasonId"
								+ " ORDER BY creditNoteReasonMaster.creditNoteReasonId asc"
								),
					
				@NamedQuery(name="GET_ACTIVE_CREDIT_NOTE_REASON_MASTER_LIST",
						query=" SELECT "
								+ " creditNoteReasonMaster.creditNoteReasonId AS creditNoteReasonId, creditNoteReasonMaster.code AS code, "
								+ " creditNoteReasonMaster.reasonDesc AS reasonDesc, creditNoteReasonMaster.status AS status, "
								+ " creditNoteReasonMaster.organisationId AS organisationId, creditNoteReasonMaster.unitId AS unitId"
								+ " FROM CreditNoteReasonMaster creditNoteReasonMaster WHERE creditNoteReasonMaster.organisationId= :organisationId AND creditNoteReasonMaster.unitId= :unitId"
								+ " AND creditNoteReasonMaster.status= 'A' ORDER BY creditNoteReasonMaster.creditNoteReasonId asc"
								),
						
				@NamedQuery(name="GET_CREDIT_NOTE_REASON_MASTER_BY_NAME",	
						query=" SELECT "
								+ " creditNoteReasonMaster.creditNoteReasonId AS creditNoteReasonId, creditNoteReasonMaster.reasonDesc AS reasonDesc "
								+ " FROM CreditNoteReasonMaster creditNoteReasonMaster WHERE LOWER(creditNoteReasonMaster.reasonDesc)= :reasonDesc"
								),
						
				@NamedQuery(name="GET_CREDIT_NOTE_REASON_MASTER_BY_NAME_NOT_ID",	
						query=" SELECT "
								+ " creditNoteReasonMaster.creditNoteReasonId AS creditNoteReasonId, creditNoteReasonMaster.code AS code, "
								+ " creditNoteReasonMaster.reasonDesc AS reasonDesc FROM CreditNoteReasonMaster creditNoteReasonMaster "
								+ " WHERE creditNoteReasonMaster.creditNoteReasonId != :creditNoteReasonId AND LOWER(creditNoteReasonMaster.reasonDesc)= :reasonDesc"
							
								)

})
@Entity
@Table(name = "m_credit_note_reason_master", schema = "billing")
@SequenceGenerator(name = "seq_m_credit_note_reason_master", sequenceName = "billing.seq_m_credit_note_reason_master", allocationSize = 1)

public class CreditNoteReasonMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_credit_note_reason_master")
	@Column(name = "credit_note_reason_id")
	private Integer creditNoteReasonId;
	
	@Column(name = "code ")
	private String code;
	
	@Column(name = "reason_desc")
	private String reasonDesc;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
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

	public Integer getCreditNoteReasonId() {
		return creditNoteReasonId;
	}

	public void setCreditNoteReasonId(Integer creditNoteReasonId) {
		this.creditNoteReasonId = creditNoteReasonId;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getReasonDesc() {
		return reasonDesc;
	}

	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
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
