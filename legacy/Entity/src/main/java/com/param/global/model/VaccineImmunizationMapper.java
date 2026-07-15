package com.param.global.model;

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

import com.param.entity.model.master.Generic;
import com.param.entity.model.master.Item;

@NamedQueries({
	@NamedQuery(name="GET_VACCINE_IMMUNIZATION_MAPPER",
			query="SELECT VacImmMap.vaccineImmunMapperId as vaccineImmunMapperId, VacImmMap.location as location, "
					+ " VacImmMap.isVaccinationComplete as isVaccinationComplete, VacImmMap.isMarkAllVaccineComplete as isMarkAllVaccineComplete, VacImmMap.vaccineNameText as vaccineNameText,"
//						+ " VacImmMap.remarks AS remarks, to_char(VacImmMap.plannedDate, 'dd/mm/yyyy') AS plannedDate,"
//						+ " to_char(VacImmMap.plannedDate,'dd/mm/yyyy') AS plannedDateSave," 
						+ " VacImmMap.vaccinationNote as vaccinationNote, VacImmMap.remarks AS remarks, VacImmMap.isNoSignificantVaccination AS isNoSignificantVaccination, to_char(VacImmMap.plannedDate, 'dd/mm/yyyy') AS plannedDate, vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"
						+ " to_char(VacImmMap.calculatedDate, 'dd/mm/yyyy') AS calculatedDate, item.name as name, VacImmMap.itemId as itemId, "
					    + " VacImmMap.vaccineFlagStatus as vaccineFlagStatus, vaccinationMaster.genericId as genericId ,generic.name as genericName ,"
					    + " VacImmMap.vaccinationMasterId as vaccinationMasterId, to_char(VacImmMap.actualDate,'dd/mm/yyyy') AS actualDate, vaccinationMaster.series AS series,"
						+ " VacImmMap.organizationId AS organizationId,VacImmMap.unitId AS unitId,VacImmMap.status AS status,VacImmMap.patientId as patientId ,"
						+ " genderMaster.desc as genderDesc, vaccinationMaster.minDay AS minDay,vaccinationMaster.abbreviation as abbreviation, "
						+ " routeOfAdministration.description as description," 
						+ " siteOfAdministrationMaster.siteName as siteName,"
						+ " strengthUnit.units as units "
						+ " FROM VaccineImmunizationMapper VacImmMap "
						+ " LEFT JOIN VacImmMap.vaccinationMaster vaccinationMaster "
						+ " LEFT JOIN VacImmMap.item item "
						 + " left JOIN vaccinationMaster.doseMaster doseMaster"
						 + " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
						 + " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
						 + " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
//						 + " LEFT JOIN vaccinationMaster.item item"
						 + " LEFT JOIN vaccinationMaster.generic generic"
						 + " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
						+ " WHERE VacImmMap.organizationId=:organizationId "
//						+ " AND VacImmMap.unitId=:unitId"
						+ " AND VacImmMap.patientId=:patientId "
						+ " ORDER BY VacImmMap.vaccineImmunMapperId asc"
						),
	
	@NamedQuery(name="GET_VACCINE_IMMUNIZATION_MAPPER_ABDM",
	query="SELECT VacImmMap.vaccineImmunMapperId as vaccineImmunMapperId, VacImmMap.location as location, "
			+ " VacImmMap.isVaccinationComplete as isVaccinationComplete, VacImmMap.isMarkAllVaccineComplete as isMarkAllVaccineComplete, VacImmMap.vaccineNameText as vaccineNameText,"
            + " VacImmMap.vaccinationNote as vaccinationNote, VacImmMap.remarks AS remarks, VacImmMap.isNoSignificantVaccination AS isNoSignificantVaccination, to_char(VacImmMap.plannedDate, 'yyyy-mm-dd') AS plannedDate, vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"				+ " to_char(VacImmMap.calculatedDate, 'dd/mm/yyyy') AS calculatedDate, item.name as name, VacImmMap.itemId as itemId, item.code as itemCode, "
			    + " VacImmMap.vaccineFlagStatus as vaccineFlagStatus, vaccinationMaster.genericId as genericId ,generic.name as genericName ,"
			    + " VacImmMap.vaccinationMasterId as vaccinationMasterId, to_char(VacImmMap.actualDate,'dd-mm-yyyy') AS actualDate, vaccinationMaster.series AS series,"
				+ " VacImmMap.organizationId AS organizationId,VacImmMap.unitId AS unitId,VacImmMap.status AS status,VacImmMap.patientId as patientId ,"
				+ " genderMaster.desc as genderDesc, vaccinationMaster.minDay AS minDay,vaccinationMaster.abbreviation as abbreviation, "
				+ " routeOfAdministration.description as description," 
				+ " siteOfAdministrationMaster.siteName as siteName,"
				+ " strengthUnit.units as units "
				+ " FROM VaccineImmunizationMapper VacImmMap "
				+ " LEFT JOIN VacImmMap.vaccinationMaster vaccinationMaster "
				+ " LEFT JOIN VacImmMap.item item "
				 + " left JOIN vaccinationMaster.doseMaster doseMaster"
				 + " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
				 + " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
				 + " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
//				 + " LEFT JOIN vaccinationMaster.item item"
				 + " LEFT JOIN vaccinationMaster.generic generic"
				 + " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
				+ " WHERE VacImmMap.organizationId=:organizationId "
//				+ " AND VacImmMap.unitId=:unitId"
				+ " AND VacImmMap.patientId=:patientId "
				+ " AND VacImmMap.plannedDate is not NULL "
				+ " ORDER BY VacImmMap.vaccineImmunMapperId asc"
				),
	
	
	
	
	@NamedQuery(name="GET_VACCINE_IMMUNIZATION_MAPPER_ABDM_BYENCOUNTER",
	query="SELECT VacImmMap.vaccineImmunMapperId as vaccineImmunMapperId, VacImmMap.location as location, "
			+ " VacImmMap.isVaccinationComplete as isVaccinationComplete, VacImmMap.isMarkAllVaccineComplete as isMarkAllVaccineComplete, VacImmMap.vaccineNameText as vaccineNameText,"
            + " VacImmMap.vaccinationNote as vaccinationNote, VacImmMap.remarks AS remarks, VacImmMap.isNoSignificantVaccination AS isNoSignificantVaccination, to_char(VacImmMap.plannedDate, 'yyyy-mm-dd') AS plannedDate, vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"				+ " to_char(VacImmMap.calculatedDate, 'dd/mm/yyyy') AS calculatedDate, item.name as name, VacImmMap.itemId as itemId, item.code as itemCode, "
			    + " VacImmMap.vaccineFlagStatus as vaccineFlagStatus, vaccinationMaster.genericId as genericId ,generic.name as genericName ,"
			    + " VacImmMap.vaccinationMasterId as vaccinationMasterId, to_char(VacImmMap.actualDate,'dd-mm-yyyy') AS actualDate, vaccinationMaster.series AS series,"
				+ " VacImmMap.organizationId AS organizationId,VacImmMap.unitId AS unitId,VacImmMap.status AS status,VacImmMap.patientId as patientId ,"
				+ " genderMaster.desc as genderDesc, vaccinationMaster.minDay AS minDay,vaccinationMaster.abbreviation as abbreviation, "
				+ " routeOfAdministration.description as description," 
				+ " siteOfAdministrationMaster.siteName as siteName,"
				+ " strengthUnit.units as units "
				+ " FROM VaccineImmunizationMapper VacImmMap "
				+ " LEFT JOIN VacImmMap.vaccinationMaster vaccinationMaster "
				+ " LEFT JOIN VacImmMap.item item "
				 + " left JOIN vaccinationMaster.doseMaster doseMaster"
				 + " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
				 + " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
				 + " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
//				 + " LEFT JOIN vaccinationMaster.item item"
				 + " LEFT JOIN vaccinationMaster.generic generic"
				 + " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
				+ " WHERE VacImmMap.organizationId=:organizationId "
				+ " AND VacImmMap.encounterId=:encounterId"
				+ " AND VacImmMap.patientId=:patientId "
				+ " AND VacImmMap.plannedDate is not NULL "
				+ " ORDER BY VacImmMap.vaccineImmunMapperId asc"
				),
	
	
	@NamedQuery(name="GET_VACCINE_IMMUNIZATION_MAPPER_ABDM_BYADDMISSION",
	query="SELECT VacImmMap.vaccineImmunMapperId as vaccineImmunMapperId, VacImmMap.location as location, "
			+ " VacImmMap.isVaccinationComplete as isVaccinationComplete, VacImmMap.isMarkAllVaccineComplete as isMarkAllVaccineComplete, VacImmMap.vaccineNameText as vaccineNameText,"
            + " VacImmMap.vaccinationNote as vaccinationNote, VacImmMap.remarks AS remarks, VacImmMap.isNoSignificantVaccination AS isNoSignificantVaccination, to_char(VacImmMap.plannedDate, 'yyyy-mm-dd') AS plannedDate, vaccinationMaster.maxDay AS maxDay,vaccinationMaster.minAge AS minAge,"				+ " to_char(VacImmMap.calculatedDate, 'dd/mm/yyyy') AS calculatedDate, item.name as name, VacImmMap.itemId as itemId, item.code as itemCode, "
			    + " VacImmMap.vaccineFlagStatus as vaccineFlagStatus, vaccinationMaster.genericId as genericId ,generic.name as genericName ,"
			    + " VacImmMap.vaccinationMasterId as vaccinationMasterId, to_char(VacImmMap.actualDate,'dd-mm-yyyy') AS actualDate, vaccinationMaster.series AS series,"
				+ " VacImmMap.organizationId AS organizationId,VacImmMap.unitId AS unitId,VacImmMap.status AS status,VacImmMap.patientId as patientId ,"
				+ " genderMaster.desc as genderDesc, vaccinationMaster.minDay AS minDay,vaccinationMaster.abbreviation as abbreviation, "
				+ " routeOfAdministration.description as description," 
				+ " siteOfAdministrationMaster.siteName as siteName,"
				+ " strengthUnit.units as units "
				+ " FROM VaccineImmunizationMapper VacImmMap "
				+ " LEFT JOIN VacImmMap.vaccinationMaster vaccinationMaster "
				+ " LEFT JOIN VacImmMap.item item "
				 + " left JOIN vaccinationMaster.doseMaster doseMaster"
				 + " left JOIN vaccinationMaster.routeOfAdministration routeOfAdministration"
				 + " LEFT JOIN vaccinationMaster.siteOfAdministrationMaster siteOfAdministrationMaster"
				 + " LEFT JOIN vaccinationMaster.strengthUnit strengthUnit"
//				 + " LEFT JOIN vaccinationMaster.item item"
				 + " LEFT JOIN vaccinationMaster.generic generic"
				 + " LEFT JOIN vaccinationMaster.genderMaster genderMaster"
				+ " WHERE VacImmMap.organizationId=:organizationId "
				+ " AND VacImmMap.admissionId=:admissionId"
				+ " AND VacImmMap.patientId=:patientId "
				+ " AND VacImmMap.plannedDate is not NULL "
				+ " ORDER BY VacImmMap.vaccineImmunMapperId asc"
				),
	
	
	
	
	
	
	
	@NamedQuery(name="GET_VACCINE_MAPPER_BY_PATIENTID_VACCINATIONID",
	query="SELECT VacImmMap.vaccineImmunMapperId as vaccineImmunMapperId,VacImmMap.remarks AS remarks, VacImmMap.encounterId as encounterId , VacImmMap.admissionId as admissionId, "
			    + " VacImmMap.vaccineFlagStatus as vaccineFlagStatus,VacImmMap.vaccinationMasterId as vaccinationMasterId,"
				+ " VacImmMap.organizationId AS organizationId,VacImmMap.unitId AS unitId,VacImmMap.status AS status,VacImmMap.patientId as patientId "
				+ " FROM VaccineImmunizationMapper VacImmMap"
				+ " WHERE VacImmMap.patientId=:patientId"
				+ " AND VacImmMap.vaccinationMasterId=:vaccinationMasterId"
				)
			
	
})

@Entity
@Table(name = "t_vaccine_immunization_mapper", schema = "opd")
@SequenceGenerator(name = "seq_t_vaccine_immunization_mapper", sequenceName = "opd.t_seq_vaccine_immunization_mapper", allocationSize = 1)
public class VaccineImmunizationMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_vaccine_immunization_mapper")
	
	@Column(name = "vaccine_immunization_mapper_id")
	private Integer vaccineImmunMapperId;
	
	@Column(name = "vaccination_id")
	private Integer vaccinationMasterId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId	;
	
	@Column(name = "role_id")
	private Integer roleId	;
	
	@Column(name = "remarks")
	private String remarks	;
	
	@Column(name = "vaccine_flag")
	private Character vaccineFlagStatus;
	
	@Column(name = "planned_date")
	private Date plannedDate;
	
	@Column(name = "actual_date")
	private Date actualDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "location")
	private String location	;
	
	@Column(name = "calculated_date")
	private Date calculatedDate;
	
	@Column(name = "encounter_id")
	private Integer encounterId	;
	
	@Column(name = "item_id")
	private Integer itemId	;
	
	@Column(name="is_no_significant_vaccination")
	private Character isNoSignificantVaccination;
	
	@Column(name="is_vaccination_Complete")
	private boolean isVaccinationComplete;

	@Column(name="is_force_closed")
	private boolean isForceClosed;
	
	@Column(name = "vaccination_note")
	private String vaccinationNote	;

	@Column(name = "is_mark_all_vaccine_complete")
	private boolean isMarkAllVaccineComplete	; 

	@Column(name = "vaccine_name_text")
	private String vaccineNameText	;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="vaccination_id", insertable=false, updatable=false)
	private VaccinationMaster vaccinationMaster;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="item_id", insertable=false, updatable=false)
	private Item item;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="generic_id", insertable=false, updatable=false)
	private Generic generic;

	public Integer getVaccineImmunMapperId() {
		return vaccineImmunMapperId;
	}

	public void setVaccineImmunMapperId(Integer vaccineImmunMapperId) {
		this.vaccineImmunMapperId = vaccineImmunMapperId;
	}

	public Integer getVaccinationMasterId() {
		return vaccinationMasterId;
	}

	public void setVaccinationMasterId(Integer vaccinationMasterId) {
		this.vaccinationMasterId = vaccinationMasterId;
	}

	

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Character getVaccineFlagStatus() {
		return vaccineFlagStatus;
	}

	public void setVaccineFlagStatus(Character vaccineFlagStatus) {
		this.vaccineFlagStatus = vaccineFlagStatus;
	}

	public Date getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}

	public Date getActualDate() {
		return actualDate;
	}

	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCalculatedDate() {
		return calculatedDate;
	}

	public void setCalculatedDate(Date calculatedDate) {
		this.calculatedDate = calculatedDate;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Character getIsNoSignificantVaccination() {
		return isNoSignificantVaccination;
	}

	public void setIsNoSignificantVaccination(Character isNoSignificantVaccination) {
		this.isNoSignificantVaccination = isNoSignificantVaccination;
	}


	public boolean getIsVaccinationComplete() {
		return isVaccinationComplete;
	}

	public void setIsVaccinationComplete(boolean isVaccinationComplete) {
		this.isVaccinationComplete = isVaccinationComplete;
	}

	public String getVaccinationNote() {
		return vaccinationNote;
	}

	public void setVaccinationNote(String vaccinationNote) {
		this.vaccinationNote = vaccinationNote;
	}

	public boolean getIsForceClosed() {
		return isForceClosed;
	}

	public void setIsForceClosed(boolean isForceClosed) {
		this.isForceClosed = isForceClosed;
	}
	public String getVaccineNameText() {
		return vaccineNameText;
	}

	public void setVaccineNameText(String vaccineNameText) {
		this.vaccineNameText = vaccineNameText;

	}

	public boolean getIsMarkAllVaccineComplete() {
		return isMarkAllVaccineComplete;
	}

	public void setIsMarkAllVaccineComplete(boolean isMarkAllVaccineComplete) {
		this.isMarkAllVaccineComplete = isMarkAllVaccineComplete;
	}

	
	
	
}
