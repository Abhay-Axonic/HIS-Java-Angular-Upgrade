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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_OT_NOTE_BY_PRO_REQ_ID", query = 
	"SELECT " + 
	" comNote.ot_completion_note_id as \"otCompletionNoteId\"," + 
	" notemster.description as \"description\"," + 
	" comNote.pro_req_id as \"proReqId\", " + 
	" comNote.procedure_order_id as \"procedureOrderId\"," + 
	" comNote.note_master_id as \"noteMasterId\", " + 
	" comNote.note_type_id as \"noteTypeId\", " + 
	" comNote.template_transaction_id as \"templateTransactionId\", " +
	" comNote.template_format_id as \"templateFormatId\", " +
	" comNote.template_type_id as \"templateTypeId\", " +
	" comNote.template_id as \"templateId\", " +
	" comNote.template as \"template\", " + 
	" to_char(comNote.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\"," + 
	" comNote.created_by as \"createdBy\", " + 
	" comNote.status as \"status\", " + 
	" comNote.updated_by as \"updatedBy\", " + 
	" to_char(comNote.updated_date, 'DD-MM-YYYY HH24:MI:SS') as \"updatedDate\"," + 
	" comNote.unit_id as \"unitId\", " + 
	" comNote.organization_id as \"organizationId\" " + 
	" FROM otims.t_ot_completion_note comNote " + 
	" inner join otims.m_note_master notemster on comNote.note_master_id = notemster.note_master_id " + 
	" where comNote.pro_req_id = :id") 
	})

@Entity
@Table(name = "t_ot_completion_note" , schema = "otims")
@SequenceGenerator(name = "ot_completion_note_seq" , sequenceName = "otims.ot_completion_note_seq" , allocationSize = 1)
public class OTCompletionNote {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "ot_completion_note_seq")
	@Column(name = "ot_completion_note_id")
	private Integer otCompletionNoteId;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "procedure_order_id")
	private Integer procedureOrderId;

	@Column(name = "note_master_id")
	private Integer noteMasterId;
	
	@Column(name = "note_type_id")
	private Integer noteTypeId;
	
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
	
	@Column(name = "template")
	private String template;
	
	@Column(name = "template_transaction_id")
	private Integer templateTransactionId;
	
	@Column(name = "template_format_id")
	private Integer templateFormatId;
	
	@Column(name = "template_type_id")
	private Integer templateTypeId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "note_master_id", insertable = false, updatable = false)
	private NotesMaster notesMaster;
	
	public Integer getOtCompletionNoteId() {
		return otCompletionNoteId;
	}

	public void setOtCompletionNoteId(Integer otCompletionNoteId) {
		this.otCompletionNoteId = otCompletionNoteId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public Integer getNoteMasterId() {
		return noteMasterId;
	}

	public void setNoteMasterId(Integer noteMasterId) {
		this.noteMasterId = noteMasterId;
	}

	public Integer getNoteTypeId() {
		return noteTypeId;
	}

	public void setNoteTypeId(Integer noteTypeId) {
		this.noteTypeId = noteTypeId;
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

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
    
    
}
