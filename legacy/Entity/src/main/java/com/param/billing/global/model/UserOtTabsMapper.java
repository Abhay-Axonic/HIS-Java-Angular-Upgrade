package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "t_user_ot_tab_mapper", schema = "public")
@SequenceGenerator(name = "user_ot_tab_mapper_seq", sequenceName = "public.user_ot_tab_mapper_seq", allocationSize = 1)
public class UserOtTabsMapper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_ot_tab_mapper_seq")
	@Column(name = "user_ot_tab_mapper_id")
	private Integer userOtTabMapperId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "ot_details")
	private Boolean otDetails;
	
	@Column(name = "procedure_details")
	private Boolean procedureDetails;
	
	@Column(name = "service")
	private Boolean service;
	
	@Column(name = "inventry")
	private Boolean inventry;
	
	@Column(name = "equipment")
	private Boolean equipment;
	
	@Column(name = "cssd_kit")
	private Boolean cssdKit;
	
	@Column(name = "notes")
	private Boolean notes;
	
	@Column(name = "time_sheet")
	private Boolean timeSheet;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "medication")
	private Boolean medication;
	
	@Column(name = "implant")
	private Boolean implant;
	
	@Column(name = "medical_gas")
	private Boolean medicalGas;
	
	@Column(name = "anesthesia_notes")
	private Boolean anesthesiaNotes;
	
	@Column(name = "surgery_notes")
	private Boolean surgeryNotes;
	
	@Column(name = "discharge_notes")
	private Boolean dischargeNotes ;
	
	@Column(name = "follow_up_notes")
	private Boolean followUpNotes ;
	

	public Integer getUserOtTabMapperId() {
		return userOtTabMapperId;
	}

	public void setUserOtTabMapperId(Integer userOtTabMapperId) {
		this.userOtTabMapperId = userOtTabMapperId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Boolean getOtDetails() {
		return otDetails;
	}

	public void setOtDetails(Boolean otDetails) {
		this.otDetails = otDetails;
	}

	public Boolean getProcedureDetails() {
		return procedureDetails;
	}

	public void setProcedureDetails(Boolean procedureDetails) {
		this.procedureDetails = procedureDetails;
	}

	public Boolean getService() {
		return service;
	}

	public void setService(Boolean service) {
		this.service = service;
	}

	public Boolean getInventry() {
		return inventry;
	}

	public void setInventry(Boolean inventry) {
		this.inventry = inventry;
	}

	public Boolean getEquipment() {
		return equipment;
	}

	public void setEquipment(Boolean equipment) {
		this.equipment = equipment;
	}

	public Boolean getCssdKit() {
		return cssdKit;
	}

	public void setCssdKit(Boolean cssdKit) {
		this.cssdKit = cssdKit;
	}

	public Boolean getNotes() {
		return notes;
	}

	public void setNotes(Boolean notes) {
		this.notes = notes;
	}

	public Boolean getTimeSheet() {
		return timeSheet;
	}

	public void setTimeSheet(Boolean timeSheet) {
		this.timeSheet = timeSheet;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getMedication() {
		return medication;
	}

	public void setMedication(Boolean medication) {
		this.medication = medication;
	}

	public Boolean getImplant() {
		return implant;
	}

	public void setImplant(Boolean implant) {
		this.implant = implant;
	}

	public Boolean getMedicalGas() {
		return medicalGas;
	}

	public void setMedicalGas(Boolean medicalGas) {
		this.medicalGas = medicalGas;
	}

	public Boolean getAnesthesiaNotes() {
		return anesthesiaNotes;
	}

	public void setAnesthesiaNotes(Boolean anesthesiaNotes) {
		this.anesthesiaNotes = anesthesiaNotes;
	}

	public Boolean getSurgeryNotes() {
		return surgeryNotes;
	}

	public void setSurgeryNotes(Boolean surgeryNotes) {
		this.surgeryNotes = surgeryNotes;
	}

	public Boolean getDischargeNotes() {
		return dischargeNotes;
	}

	public void setDischargeNotes(Boolean dischargeNotes) {
		this.dischargeNotes = dischargeNotes;
	}

	public Boolean getFollowUpNotes() {
		return followUpNotes;
	}

	public void setFollowUpNotes(Boolean followUpNotes) {
		this.followUpNotes = followUpNotes;
	}

}



