package com.param.global.unit.dto;

import io.swagger.models.auth.In;
import scala.collection.generic.BitOperations;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;


public class AllergyPatientMapperDto {

	private Integer allergyId;
	private Integer patientId;
	private Integer allergyTypeId;
	private String type;
	private String natureOfReaction;
	private String comments;
	private Integer severityId;
	private String remark;
	private Integer month;
	private Integer year;
	private char isNoKnownAllergies;
	private char status;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String updatedDate;
	private Integer unitId;
	private Integer organizationId;
	private String allergyName;
	private String severityDesc;
	private String allergyTypeName;
	private String allergyTypeDesc;
	private String allergen;
	private String monthYear;
	private Integer allergyPatientMapperId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer roleId;
	private char isReviewedFlag;
	private char isEnterInErrorStatus;
	
	private String genericSearchName;
	private String fromDate;
	private String toDate;
	private Integer genericId;
	private String genericCode;
	private String genericName;
	private String allergyDesc;
	private String allergyDescText;
	private String searchAllergen;

	private String userName;
	private String resolvedDate;
	private String resolvedRemark;
	private BigInteger allergyCount;
	private Integer covTemplateId;
	private String cimsCode;
	private String cimsType;
	private List<ListOfAllergyPatientMapperDto> listOfAllergyPatientMapperDto;

	private List<AllergyPatientMapperDto> allergyOldList;

	private List<AllergyPatientMapperDto> allergyCurrentList;

	private List<String> dates;

	private String addedDate;
	private Integer hospitalId;
	private Boolean isFromHisApiCall;

	private String uuid;
	private Integer applicationId;

	private List<AllergyPatientMapperDto> allergyPatientMapperDtoList;

	public String getGenericSearchName() {
		return genericSearchName;
	}
	public void setGenericSearchName(String genericSearchName) {
		this.genericSearchName = genericSearchName;
	}
	public Integer getGenericId() {
		return genericId;
	}
	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}
	public String getGenericCode() {
		return genericCode;
	}
	public void setGenericCode(String genericCode) {
		this.genericCode = genericCode;
	}
	public String getGenericName() {
		return genericName;
	}
	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}
	public List<ListOfAllergyPatientMapperDto> getListOfAllergyPatientMapperDto() {
		return listOfAllergyPatientMapperDto;
	}
	public void setListOfAllergyPatientMapperDto(
			List<ListOfAllergyPatientMapperDto> listOfAllergyPatientMapperDto) {
		this.listOfAllergyPatientMapperDto = listOfAllergyPatientMapperDto;
	}
	public Integer getAllergyPatientMapperId() {
		return allergyPatientMapperId;
	}
	public void setAllergyPatientMapperId(Integer allergyPatientMapperId) {
		this.allergyPatientMapperId = allergyPatientMapperId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}
	public char getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}
	public void setIsEnterInErrorStatus(char isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}
	public Integer getAllergyId() {
		return allergyId;
	}
	public void setAllergyId(Integer allergyId) {
		this.allergyId = allergyId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getAllergyTypeId() {
		return allergyTypeId;
	}
	public void setAllergyTypeId(Integer allergyTypeId) {
		this.allergyTypeId = allergyTypeId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNatureOfReaction() {
		return natureOfReaction;
	}
	public void setNatureOfReaction(String natureOfReaction) {
		this.natureOfReaction = natureOfReaction;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getSeverityId() {
		return severityId;
	}
	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public char getIsNoKnownAllergies() {
		return isNoKnownAllergies;
	}
	public void setIsNoKnownAllergies(char isNoKnownAllergies) {
		this.isNoKnownAllergies = isNoKnownAllergies;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public String getAllergyName() {
		return allergyName;
	}
	public void setAllergyName(String allergyName) {
		this.allergyName = allergyName;
	}
	public String getSeverityDesc() {
		return severityDesc;
	}
	public void setSeverityDesc(String severityDesc) {
		this.severityDesc = severityDesc;
	}
	public String getAllergyTypeName() {
		return allergyTypeName;
	}
	public void setAllergyTypeName(String allergyTypeName) {
		this.allergyTypeName = allergyTypeName;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getAllergyDesc() {
		return allergyDesc;
	}
	public void setAllergyDesc(String allergyDesc) {
		this.allergyDesc = allergyDesc;
	}
	public String getResolvedDate() {
		return resolvedDate;
	}
	public void setResolvedDate(String resolvedDate) {
		this.resolvedDate = resolvedDate;
	}
	public String getResolvedRemark() {
		return resolvedRemark;
	}
	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAllergyDescText() {
		return allergyDescText;
	}
	public void setAllergyDescText(String allergyDescText) {
		this.allergyDescText = allergyDescText;
	}
	public BigInteger getAllergyCount() {
		return allergyCount;
	}
	public void setAllergyCount(BigInteger allergyCount) {
		this.allergyCount = allergyCount;
	}
	public String getSearchAllergen() {
		return searchAllergen;
	}
	public void setSearchAllergen(String searchAllergen) {
		this.searchAllergen = searchAllergen;
	}
	public String getAllergyTypeDesc() {
		return allergyTypeDesc;
	}
	public void setAllergyTypeDesc(String allergyTypeDesc) {
		this.allergyTypeDesc = allergyTypeDesc;
	}
	public String getAllergen() {
		return allergen;
	}
	public void setAllergen(String allergen) {
		this.allergen = allergen;
	}
	public String getMonthYear() {
		return monthYear;
	}
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	public Integer getCovTemplateId() {
		return covTemplateId;
	}
	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	public String getCimsCode() {
		return cimsCode;
	}
	public void setCimsCode(String cimsCode) {
		this.cimsCode = cimsCode;
	}
	public String getCimsType() {
		return cimsType;
	}
	public void setCimsType(String cimsType) {
		this.cimsType = cimsType;
	}

	public List<AllergyPatientMapperDto> getAllergyOldList() {
		return allergyOldList;
	}

	public void setAllergyOldList(List<AllergyPatientMapperDto> allergyOldList) {
		this.allergyOldList = allergyOldList;
	}

	public List<AllergyPatientMapperDto> getAllergyCurrentList() {
		return allergyCurrentList;
	}

	public void setAllergyCurrentList(List<AllergyPatientMapperDto> allergyCurrentList) {
		this.allergyCurrentList = allergyCurrentList;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Boolean getIsFromHisApiCall() {
		return isFromHisApiCall;
	}

	public void setIsFromHisApiCall(Boolean isFromHisApiCall) {
		isFromHisApiCall = isFromHisApiCall;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List<AllergyPatientMapperDto> getAllergyPatientMapperDtoList() {
		return allergyPatientMapperDtoList;
	}

	public void setAllergyPatientMapperDtoList(List<AllergyPatientMapperDto> allergyPatientMapperDtoList) {
		this.allergyPatientMapperDtoList = allergyPatientMapperDtoList;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
}
