package com.param.billing.global.transaction.config.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_unit_application_registration_configuration",schema="public")
@SequenceGenerator(name="unit_application_registration_field_configuration_seq",sequenceName="public.unit_application_registration_field_configuration_seq",allocationSize=1)
public class RegistrationFieldConfiguration {
	@Id
	@Column(name=" unit_appl_registration_config_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="unit_application_registration_field_configuration_seq")
	private Integer  unitApplRegistrationConfigId;
	
	@Column(name="prefix")
	private Character prefix;
	
	@Column(name="first_name")
	private Character firstName;

	@Column(name="middle_name")
	private Character middleName;

	@Column(name="last_name")
	private Character lastName;

	@Column(name="birth_date")
	private Character birthDate;

	@Column(name="sex")
	private Character sex;

	@Column(name="calling_code")
	private Character callingCode;

	@Column(name="code_digit")
	private Integer codeDigit;

	@Column(name="mobile_no")
	private Character mobileNo;	

	@Column(name="mobile_digit")
	private Integer mobileDigit;
	
	@Column(name="email")
	private Character email;
	
	@Column(name="nationality")
	private Character nationality;

	@Column(name="identification_doc")
	private Character identificationDoc;
	

	@Column(name="race")
	private Character race;

	@Column(name="marital_status")
	private Character maritalStatus;
	
	@Column(name="occupation")
	private Character occupation;
	
	@Column(name="patient_category")
	private Character patientCategory;
	
	@Column(name="photo")
	private Character photo;

	
	@Column(name="thumb_impression")
	private Character thumbImpression;

	@Column(name="unit_id")
	private Integer unitId;
	

	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	
	@Column(name="address")
	private Character address;
	
	
	@Column(name="kin_details")
	private Character kinDetails;
	
	@Column(name="source_id")
	private Character sourceId;
	
	@Column(name="kinprefix")
	private Character kinprefix;
	
	@Column(name="kin_name")
	private Character kinName;
	
	@Column(name="kin_mobile")
	private Character kinMobileNo;
	
	@Column(name="kin_address")
	private Character kinAddress;
	
	public Character getKinAddress() {
		return kinAddress;
	}


	public void setKinAddress(Character kinAddress) {
		this.kinAddress = kinAddress;
	}


	public Character getKinMobileNo() {
		return kinMobileNo;
	}


	public void setKinMobileNo(Character kinMobileNo) {
		this.kinMobileNo = kinMobileNo;
	}


	public Character getKinprefix() {
		return kinprefix;
	}


	public void setKinprefix(Character kinprefix) {
		this.kinprefix = kinprefix;
	}


	public Character getKinName() {
		return kinName;
	}


	public void setKinName(Character kinName) {
		this.kinName = kinName;
	}


	@Column(name="relation_id")
	private Character relationId;

	@Column(name="phone_no")
	private Character phoneNo;
	
	@Column(name="identification_id")
	private Character identificationId;
	
	@Column(name="identificationno_id")
	private Character identificationNo;
	
	@Column(name="country_id")
	private Character countryId;
	
	@Column(name="state_id")
	private Character stateId;
	
	@Column(name="district_id")
	private Character districtId;
	
	@Column(name="city_id")
	private Character cityId;
	
	@Column(name="area")
	private Character areaId;
	
	@Column(name="post_id")
	private Character postCode;

	public Character getRelationId() {
		return relationId;
	}


	public void setRelationId(Character relationId) {
		this.relationId = relationId;
	}


	public Character getSourceId() {
		return sourceId;
	}


	public void setSourceId(Character sourceId) {
		this.sourceId = sourceId;
	}


	public Character getAddress() {
		return address;
	}


	public void setAddress(Character address) {
		this.address = address;
	}


	public Character getKinDetails() {
		return kinDetails;
	}


	public void setKinDetails(Character kinDetails) {
		this.kinDetails = kinDetails;
	}


	public Integer getUnitApplRegistrationConfigId() {
		return unitApplRegistrationConfigId;
	}


	public void setUnitApplRegistrationConfigId(Integer unitApplRegistrationConfigId) {
		this.unitApplRegistrationConfigId = unitApplRegistrationConfigId;
	}


	public Character getPrefix() {
		return prefix;
	}


	public void setPrefix(Character prefix) {
		this.prefix = prefix;
	}


	public Character getFirstName() {
		return firstName;
	}


	public void setFirstName(Character firstName) {
		this.firstName = firstName;
	}


	public Character getMiddleName() {
		return middleName;
	}


	public void setMiddleName(Character middleName) {
		this.middleName = middleName;
	}


	public Character getLastName() {
		return lastName;
	}


	public void setLastName(Character lastName) {
		this.lastName = lastName;
	}


	public Character getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Character birthDate) {
		this.birthDate = birthDate;
	}


	public Character getSex() {
		return sex;
	}


	public void setSex(Character sex) {
		this.sex = sex;
	}


	public Character getCallingCode() {
		return callingCode;
	}


	public void setCallingCode(Character callingCode) {
		this.callingCode = callingCode;
	}	

	public Character getMobileNo() {
		return mobileNo;
	}


	public void setMobileNo(Character mobileNo) {
		this.mobileNo = mobileNo;
	}	

	public Character getNationality() {
		return nationality;
	}


	public void setNationality(Character nationality) {
		this.nationality = nationality;
	}


	public Character getIdentificationDoc() {
		return identificationDoc;
	}


	public void setIdentificationDoc(Character identificationDoc) {
		this.identificationDoc = identificationDoc;
	}


	public Character getRace() {
		return race;
	}


	public void setRace(Character race) {
		this.race = race;
	}


	public Character getMaritalStatus() {
		return maritalStatus;
	}


	public void setMaritalStatus(Character maritalStatus) {
		this.maritalStatus = maritalStatus;
	}


	public Character getOccupation() {
		return occupation;
	}


	public void setOccupation(Character occupation) {
		this.occupation = occupation;
	}


	public Character getPatientCategory() {
		return patientCategory;
	}


	public void setPatientCategory(Character patientCategory) {
		this.patientCategory = patientCategory;
	}


	public Character getPhoto() {
		return photo;
	}


	public void setPhoto(Character photo) {
		this.photo = photo;
	}


	public Character getThumbImpression() {
		return thumbImpression;
	}


	public void setThumbImpression(Character thumbImpression) {
		this.thumbImpression = thumbImpression;
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


	public Integer getCodeDigit() {
		return codeDigit;
	}


	public void setCodeDigit(Integer codeDigit) {
		this.codeDigit = codeDigit;
	}


	public Integer getMobileDigit() {
		return mobileDigit;
	}


	public void setMobileDigit(Integer mobileDigit) {
		this.mobileDigit = mobileDigit;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Date getUpdatedDate() {
		return updatedDate;
	}


	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	public Character getEmail() {
		return email;
	}


	public void setEmail(Character email) {
		this.email = email;
	}

	public Character getIdentificationId() {
		return identificationId;
	}


	public void setIdentificationId(Character identificationId) {
		this.identificationId = identificationId;
	}


	public Character getIdentificationNo() {
		return identificationNo;
	}


	public void setIdentificationNo(Character identificationNo) {
		this.identificationNo = identificationNo;
	}


	public Character getCountryId() {
		return countryId;
	}


	public void setCountryId(Character countryId) {
		this.countryId = countryId;
	}


	public Character getStateId() {
		return stateId;
	}


	public void setStateId(Character stateId) {
		this.stateId = stateId;
	}


	public Character getDistrictId() {
		return districtId;
	}


	public void setDistrictId(Character districtId) {
		this.districtId = districtId;
	}


	public Character getCityId() {
		return cityId;
	}


	public void setCityId(Character cityId) {
		this.cityId = cityId;
	}

	public Character getAreaId() {
		return areaId;
	}


	public void setAreaId(Character areaId) {
		this.areaId = areaId;
	}


	public Character getPostCode() {
		return postCode;
	}


	public void setPostCode(Character postCode) {
		this.postCode = postCode;
	}


	public Character getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(Character phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	
}
