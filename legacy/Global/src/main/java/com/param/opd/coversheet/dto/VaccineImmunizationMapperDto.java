package com.param.opd.coversheet.dto;

import java.util.List;

public class VaccineImmunizationMapperDto {

	private Integer vaccineImmunMapperId;
	
	private Integer vaccinationMasterId;
	
	private Integer patientId;
	
	private Integer encounterId;
	
	private Integer admissionId	;
	
	private Integer roleId	;
	
	private String vaccineName	;
	
	private Integer age;
	
	private String day;
	
	private String doseName;
	
	private String route;
	
	private String siteName;
	
	private String remarks	;
	private String genderDesc	;
	private String description	;
	private Integer series;
	private String units	;

	private Character isNoSignificantVaccination;
	private Character vaccineFlagStatus;
	
	private String plannedDate;
	
	private String plannedDateSave;
	
	private String actualDate;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	private Integer itemId;
	private String location	;
	private String name	;
	private Integer genericId;
	private String genericName	;
	private Integer maxAge;
	
	private String maxDay;
	
	private String minAge;
	
	private String minDay;
	
	private String remark;
	
	private String calculatedDate;
	private String calculatedPlannedDate;
	private String abbreviation;

	private boolean isVaccinationComplete;
	private boolean isForceClosed;
	private String vaccinationNote	;

	private String vaccineNameText;
	private boolean isMarkAllVaccineComplete;

	private List<VaccineImmunizationMapperDto> listVaccineImmunizationMapperDto;
	private List<VaccineImmunizationMapperDto> listOfGenericIdWiseList;
	
	private String itemCode;
	
	private String seriesName;

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

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

	public String getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(String plannedDate) {
		this.plannedDate = plannedDate;
	}

	public String getActualDate() {
		return actualDate;
	}

	public void setActualDate(String actualDate) {
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
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

	public List<VaccineImmunizationMapperDto> getListVaccineImmunizationMapperDto() {
		return listVaccineImmunizationMapperDto;
	}

	public void setListVaccineImmunizationMapperDto(List<VaccineImmunizationMapperDto> listVaccineImmunizationMapperDto) {
		this.listVaccineImmunizationMapperDto = listVaccineImmunizationMapperDto;
	}

	public String getVaccineName() {
		return vaccineName;
	}

	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDoseName() {
		return doseName;
	}

	public void setDoseName(String doseName) {
		this.doseName = doseName;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getPlannedDateSave() {
		return plannedDateSave;
	}

	public void setPlannedDateSave(String plannedDateSave) {
		this.plannedDateSave = plannedDateSave;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCalculatedDate() {
		return calculatedDate;
	}

	public void setCalculatedDate(String calculatedDate) {
		this.calculatedDate = calculatedDate;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public List<VaccineImmunizationMapperDto> getListOfGenericIdWiseList() {
		return listOfGenericIdWiseList;
	}

	public void setListOfGenericIdWiseList(List<VaccineImmunizationMapperDto> listOfGenericIdWiseList) {
		this.listOfGenericIdWiseList = listOfGenericIdWiseList;
	}

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public String getGenderDesc() {
		return genderDesc;
	}

	public void setGenderDesc(String genderDesc) {
		this.genderDesc = genderDesc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSeries() {
		return series;
	}

	public void setSeries(Integer series) {
		this.series = series;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Integer getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(Integer maxAge) {
		this.maxAge = maxAge;
	}

	public String getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(String maxDay) {
		this.maxDay = maxDay;
	}

	public String getMinAge() {
		return minAge;
	}

	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getMinDay() {
		return minDay;
	}

	public void setMinDay(String minDay) {
		this.minDay = minDay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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

	public String getCalculatedPlannedDate() {
		return calculatedPlannedDate;
	}

	public void setCalculatedPlannedDate(String calculatedPlannedDate) {
		this.calculatedPlannedDate = calculatedPlannedDate;
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

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
}
