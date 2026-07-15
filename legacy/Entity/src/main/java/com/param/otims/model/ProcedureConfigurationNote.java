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

@NamedQueries({
	
	@NamedQuery(name="GET_NOTE_BY_PROCEDURE_CONFIG_ID",
	query="SELECT mstr.procedureConfigNoteId as procedureConfigNoteId , " 
				+ " mstr.procedureConfigId as procedureConfigId , "
				+ " mstr.noteMasterId as noteMasterId, "
				+ " mstr.noteTypeId as noteType, "
				+ " mstr.template as template, "
				+ " notesMaster.description as description, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " procedureConfiguration.serviceId as serviceId, "
				+ " mstr.status as status "
				+ " FROM ProcedureConfigurationNote mstr "
				+ " INNER JOIN  mstr.notesMaster notesMaster  "
                + " INNER JOIN  mstr.procedureConfiguration procedureConfiguration  "
				+ " WHERE  mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				//+ " AND mstr.procedureConfigId= :procedureConfigId "
				+ " AND mstr.status = 'A' "
				),
	
	@NamedQuery(name="GET_NOTE_BY_PROCEDURE_ORDER_ID",
	query="SELECT  "
				+ " mstr.noteMasterId as noteMasterId, "
				+ " mstr.noteTypeId as noteTypeId, "
				+ " mstr.template as template, "
				+ " notesMaster.description as description, "
				+ " mstr.organizationId as organizationId, "
				+ " mstr.unitId as unitId, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.updatedBy as updatedBy, "
				+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
				+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
				+ " mstr.status as status "
				+ " FROM ProcedureConfigurationNote mstr "
				+ " INNER JOIN  mstr.notesMaster notesMaster  "
                + " INNER JOIN  mstr.procedureConfiguration procedureConfiguration  "
				+ " WHERE  mstr.status = 'A' "
				)
})


@Entity
@Table(name = "t_procedure_config_note" , schema = "otims")
@SequenceGenerator(name = "procedure_config_note_seq" , sequenceName = "otims.procedure_config_note_seq" , allocationSize = 1)
public class ProcedureConfigurationNote {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_config_note_seq")
	@Column(name = "procedure_config_note_id")
	private Integer procedureConfigNoteId;
	
	@Column(name = "procedure_config_id")
	private Integer procedureConfigId;

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
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "note_master_id", insertable = false, updatable = false)
	private NotesMaster notesMaster;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procedure_config_id", insertable = false, updatable = false)
    private ProcedureConfiguration procedureConfiguration;


	public Integer getProcedureConfigNoteId() {
		return procedureConfigNoteId;
	}

	public void setProcedureConfigNoteId(Integer procedureConfigNoteId) {
		this.procedureConfigNoteId = procedureConfigNoteId;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
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
	
}
