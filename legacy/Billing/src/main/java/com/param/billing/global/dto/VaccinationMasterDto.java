package com.param.billing.global.dto;

import java.util.List;

import javax.persistence.Column;

public class VaccinationMasterDto {
	
	private Integer vaccinationId;
	
	private Integer vaccinationCode;
	
	private String vaccinationDesc;
	
	private String name;
	
	private Integer administrationId;
	
	private Integer siteId;
	
	private Integer doseId;
	
	private String doseDesc;
	
	private Double strengthDesc;
	
	private Integer  uomId;
	
	private String  units;
	
	private Integer  age;
	
	private Integer  dayid;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String day;
	
	private Integer organizationId;
	
	private Integer roaId;
	
	private String description;
	
	private String siteName;
	
	private Integer unitId;
	
    private Integer offset;
    
	private String days;

    private Integer month;

	private String roaName;
	
    private Integer unit;

	private String vaccineName;
	
	private String weeks;
	
	private String years;
	
	private Integer interval;
	
	private String chooseInterval;
	
	private Integer genericId;
	
	private Integer maxAge;
	
	private String maxDay;
	
	private String minAge;
	
	private String minDay;
	
	private String remark;
	
	private Integer series;
	
	private Integer genderId;
	
	private Integer id;
	
	private String abbreviation;

	private List<Integer> selectedGenderList;
	private List<VaccinationMasterDto> listOfGenericIdList;

	private String genericName;
	
	private String genderDesc;
	private Integer itemId;


	public Integer getOffset() {
		return offset;
	}


	public void setOffset(Integer offset) {
		this.offset = offset;
	}


	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}


	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}


	private Integer noOfRecordsPerPage;


	public Integer getVaccinationId() {
		return vaccinationId;
	}


	public void setVaccinationId(Integer vaccinationId) {
		this.vaccinationId = vaccinationId;
	}


	public Integer getVaccinationCode() {
		return vaccinationCode;
	}


	public void setVaccinationCode(Integer vaccinationCode) {
		this.vaccinationCode = vaccinationCode;
	}


	public String getVaccinationDesc() {
		return vaccinationDesc;
	}


	public void setVaccinationDesc(String vaccinationDesc) {
		this.vaccinationDesc = vaccinationDesc;
	}


	public Integer getAdministrationId() {
		return administrationId;
	}


	public void setAdministrationId(Integer administrationId) {
		this.administrationId = administrationId;
	}


	public Integer getSiteId() {
		return siteId;
	}


	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}


	public Integer getDoseId() {
		return doseId;
	}


	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}


	public Integer getUomId() {
		return uomId;
	}


	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}


	public Integer getAge() {
		return age;
	}


	public void setAge(Integer age) {
		this.age = age;
	}


	public Integer getDayid() {
		return dayid;
	}


	public void setDayid(Integer dayid) {
		this.dayid = dayid;
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


	public String getDay() {
		return day;
	}


	public void setDay(String day) {
		this.day = day;
	}


	public String getDoseDesc() {
		return doseDesc;
	}


	public void setDoseDesc(String doseDesc) {
		this.doseDesc = doseDesc;
	}


	public Integer getRoaId() {
		return roaId;
	}


	public void setRoaId(Integer roaId) {
		this.roaId = roaId;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getSiteName() {
		return siteName;
	}


	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}


	public String getUnits() {
		return units;
	}


	public void setUnits(String units) {
		this.units = units;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDays() {
		return days;
	}


	public void setDays(String days) {
		this.days = days;
	}


	public Integer getMonth() {
		return month;
	}


	public void setMonth(Integer month) {
		this.month = month;
	}


	public String getRoaName() {
		return roaName;
	}


	public void setRoaName(String roaName) {
		this.roaName = roaName;
	}


	public Integer getUnit() {
		return unit;
	}


	public void setUnit(Integer unit) {
		this.unit = unit;
	}


	public String getVaccineName() {
		return vaccineName;
	}


	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}


	public String getWeeks() {
		return weeks;
	}


	public void setWeeks(String weeks) {
		this.weeks = weeks;
	}


	public String getYears() {
		return years;
	}


	public void setYears(String years) {
		this.years = years;
	}


	public Integer getInterval() {
		return interval;
	}


	public void setInterval(Integer interval) {
		this.interval = interval;
	}


	public String getChooseInterval() {
		return chooseInterval;
	}


	public void setChooseInterval(String chooseInterval) {
		this.chooseInterval = chooseInterval;
	}


	public Integer getGenericId() {
		return genericId;
	}


	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
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


	public Integer getSeries() {
		return series;
	}


	public void setSeries(Integer series) {
		this.series = series;
	}


	public Integer getGenderId() {
		return genderId;
	}


	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}


	public List<Integer> getSelectedGenderList() {
		return selectedGenderList;
	}


	public void setSelectedGenderList(List<Integer> selectedGenderList) {
		this.selectedGenderList = selectedGenderList;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
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


	public String getAbbreviation() {
		return abbreviation;
	}


	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}


	public Integer getItemId() {
		return itemId;
	}


	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}


	public List<VaccinationMasterDto> getListOfGenericIdList() {
		return listOfGenericIdList;
	}


	public void setListOfGenericIdList(List<VaccinationMasterDto> listOfGenericIdList) {
		this.listOfGenericIdList = listOfGenericIdList;
	}


	public Double getStrengthDesc() {
		return strengthDesc;
	}


	public void setStrengthDesc(Double strengthDesc) {
		this.strengthDesc = strengthDesc;
	}


	
	
}
