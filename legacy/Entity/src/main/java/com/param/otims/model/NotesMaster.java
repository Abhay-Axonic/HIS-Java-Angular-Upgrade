package com.param.otims.model;

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
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_NOTE_MASTER",
		query="SELECT mstr.noteMasterId as noteMasterId "
					+ " FROM NotesMaster mstr"
					+ " WHERE  mstr.organizationId= :organizationId"
					+ " AND mstr.unitId= :unitId"
					+ " AND lower(mstr.code)= :code "
					+ " AND lower(mstr.description)= :description"
					),
	//added by minesh for update NotesMaster.
	@NamedQuery(name="CHECK_FOR_UNIQUE_NOTE_MASTER_NOT_ID",
	query="SELECT mstr.noteMasterId as noteMasterId "
			+ " FROM NotesMaster mstr"
			+ " WHERE  mstr.organizationId= :organizationId"
			+ " AND mstr.unitId= :unitId"
			+ " AND mstr.noteMasterId != :noteMasterId"
			+ " AND lower(mstr.code)= :code "
			+ " AND lower(mstr.description)= :description"
			),// end update NotesMaster.
	
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_NOTE_MASTER_SAVE",
	query="SELECT mstr.noteMasterId as noteMasterId "
			+ " FROM NotesMaster mstr"
			+ " WHERE  mstr.organizationId= :organizationId"
			+ " AND mstr.unitId= :unitId"
			+ " AND lower(mstr.code)= :code "
			+ " AND lower(mstr.description)= :description"
			),// end update NotesMaster.
	
	@NamedQuery(name="GET_NOTELIST_MASTER_LIST",
	query="SELECT mstr.noteMasterId as noteMasterId , " 
				+ " mstr.code as code , "
				+ " mstr.description as description, "
				+ " mstr.noteType as noteType, "
				+ " mstr.template as template, "
				+ " mstr.templateFormatId as templateFormatId, "
				+ " mstr.formTemplateId as formTemplateId, "
				+ " mstr.formTemplateKeywords as formTemplateKeywords, "
				+ " mstr.templateTypeId as templateTypeId, "
				+ " mstr.templateId as templateId, "
				+ " mstr.title as title, "
				+ " mstr.status as status "
				+ " FROM NotesMaster mstr "
				+ " WHERE  mstr.organizationId= :organizationId"
				+ " AND mstr.unitId= :unitId"
				),
	
	@NamedQuery(name="GET_NOTELIST_MASTER_LIST_BY_NOTE_TYPE_ID",
	query="SELECT mstr.noteMasterId as noteMasterId , " 
				+ " mstr.code as code , "
				+ " mstr.description as description, "
				+ " mstr.noteType as noteType, "
				+ " mstr.template as template, "
				+ " mstr.templateFormatId as templateFormatId, "
				+ " mstr.formTemplateId as formTemplateId, "
				+ " mstr.formTemplateKeywords as formTemplateKeywords, "
				+ " mstr.title as title, "
				+ " mstr.templateTypeId as templateTypeId, "
				+ " mstr.templateId as templateId, "
				+ " mstr.status as status "
				+ " FROM NotesMaster mstr "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.noteType= :noteType "
				+ " AND mstr.status = 'A' "
				)
})

@Entity
@Table(name = "m_note_master", schema = "otims")
@SequenceGenerator(name = "note_master_seq", sequenceName = "otims.note_master_seq", allocationSize = 1)
public class NotesMaster {

	@Id
	@Column(name = "note_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "note_master_seq")
	private Integer noteMasterId;

	@Column(name = "code")
	private String code;

	@Column(name = "description")
	private String description;

	@Column(name = "note_type")
	private Integer noteType;
	
	@Column(name = "template")
	private String template;
	
	@Column(name = "status")
	private Character status;

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
	
	@Column(name = "template_format_id")
	private Integer templateFormatId;
	
	
	@Column(name = "form_template_id")
	private String formTemplateId;
	
	@Column(name = "form_template_keywords")
	private String formTemplateKeywords;
	
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "template_type_id")
	private Integer templateTypeId;
	
	
	@Column(name = "template_id")
	private Integer templateId;
	

	public Integer getNoteMasterId() {
		return noteMasterId;
	}

	public void setNoteMasterId(Integer noteMasterId) {
		this.noteMasterId = noteMasterId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getNoteType() {
		return noteType;
	}

	public void setNoteType(Integer noteType) {
		this.noteType = noteType;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public String getFormTemplateKeywords() {
		return formTemplateKeywords;
	}

	public void setFormTemplateKeywords(String formTemplateKeywords) {
		this.formTemplateKeywords = formTemplateKeywords;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
