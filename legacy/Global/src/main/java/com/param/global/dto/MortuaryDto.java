package com.param.global.dto;

import java.time.*;
import java.util.List;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;

public class MortuaryDto {
	
	private int deathRegistrationId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String firstName;
	
	private String lastName;
	
	private Integer genderId;
	
	private String markOnDecease;
	
	private String dateOfDeath;
	
	private char isMedicoLegal;
	
	private String broughtFrom;
	
	private String deathReason;
	
	private Integer prefixId;
	
	private String prefixCode;
	
	private String address;

	private Integer countryId;

	private Integer stateId;

	private Integer districtId;

	private Integer cityId;

	private Integer areaId;

	private String zipCode;
	
	private String uhidNumber;
	
	private Integer kinId;
	
	private Character status;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer visitTypeId;
	
	private List<KinDetailsDto> kinArray;
	
	private Integer mortuaryRequestId;
	
	private Integer patientId;
	
	private Integer tPatientId;
	
	private Integer dPatientId;
	
	private Integer durationFormat;
	
	private Integer durationValue;
	
	private Integer mortuaryStatusId;
	
	private String outTime;
	
	private Integer organzationId;
	
	private String expectedArrivalDate;
	
	private Instant expectedArrivalTime;
	
	private String patientName;
	
	private String genderCode;
	
	private String age;
	
	private Integer admissionId;
	
	private Integer mortuaryBedId;
	
	private Integer releaseKinId;
	
	private String releaseNote;
	
	private String toeBandId;
	
	private String kinName;
	
	private String mobileNo;
	
	private String mortuaryBedNumber;
	
	private String admissionKinId;
	
	private Integer bedStatusId;
	
	private Integer rejectReasonId;
	
	private String rejectionNote;
	
	private Integer doctorId;
	
	private Integer documentTypeId;
	
	private String document;
	
	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectionNote() {
		return rejectionNote;
	}

	public void setRejectionNote(String rejectionNote) {
		this.rejectionNote = rejectionNote;
	}

	public Integer getBedStatusId() {
		return bedStatusId;
	}

	public void setBedStatusId(Integer bedStatusId) {
		this.bedStatusId = bedStatusId;
	}

	public String getAdmissionKinId() {
		return admissionKinId;
	}

	public void setAdmissionKinId(String admissionKinId) {
		this.admissionKinId = admissionKinId;
	}

	public String getMortuaryBedNumber() {
		return mortuaryBedNumber;
	}

	public void setMortuaryBedNumber(String mortuaryBedNumber) {
		this.mortuaryBedNumber = mortuaryBedNumber;
	}

	public String getKinName() {
		return kinName;
	}

	public void setKinName(String kinName) {
		this.kinName = kinName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getToeBandId() {
		return toeBandId;
	}

	public void setToeBandId(String toeBandId) {
		this.toeBandId = toeBandId;
	}

	public int getDeathRegistrationId() {
		return deathRegistrationId;
	}

	public void setDeathRegistrationId(int deathRegistrationId) {
		this.deathRegistrationId = deathRegistrationId;
	}

	public String getReleaseNote() {
		return releaseNote;
	}

	public void setReleaseNote(String releaseNote) {
		this.releaseNote = releaseNote;
	}

	public Instant getExpectedArrivalTime() {
		return expectedArrivalTime;
	}

	public void setExpectedArrivalTime(String expectedArrivalTime) {
		if (expectedArrivalTime!=null && !expectedArrivalTime.equals(""))
		{
			LocalTime time = LocalTime.parse(expectedArrivalTime);
			LocalDate today = LocalDate.now();
			this.expectedArrivalTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.expectedArrivalTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	
	
	public Integer getReleaseKinId() {
		return releaseKinId;
	}

	public void setReleaseKinId(Integer releaseKinId) {
		this.releaseKinId = releaseKinId;
	}

	public Integer getMortuaryBedId() {
		return mortuaryBedId;
	}

	public void setMortuaryBedId(Integer mortuaryBedId) {
		this.mortuaryBedId = mortuaryBedId;
	}

	public int getDethRegistrationId() {
		return deathRegistrationId;
	}

	public void setDethRegistrationId(int dethRegistrationId) {
		this.deathRegistrationId = dethRegistrationId;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getMarkOnDecease() {
		return markOnDecease;
	}

	public void setMarkOnDecease(String markOnDecease) {
		this.markOnDecease = markOnDecease;
	}

	public String getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(String dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public char getIsMedicoLegal() {
		return isMedicoLegal;
	}

	public void setIsMedicoLegal(char isMedicoLegal) {
		this.isMedicoLegal = isMedicoLegal;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getKinId() {
		return kinId;
	}

	public void setKinId(Integer kinId) {
		this.kinId = kinId;
	}

	

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public List<KinDetailsDto> getKinArray() {
		return kinArray;
	}

	public void setKinArray(List<KinDetailsDto> kinArray) {
		this.kinArray = kinArray;
	}

	public Integer getMortuaryRequestId() {
		return mortuaryRequestId;
	}

	public void setMortuaryRequestId(Integer mortuaryRequestId) {
		this.mortuaryRequestId = mortuaryRequestId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer gettPatientId() {
		return tPatientId;
	}

	public void settPatientId(Integer tPatientId) {
		this.tPatientId = tPatientId;
	}

	public Integer getdPatientId() {
		return dPatientId;
	}

	public void setdPatientId(Integer dPatientId) {
		this.dPatientId = dPatientId;
	}

	public Integer getDurationFormat() {
		return durationFormat;
	}

	public void setDurationFormat(Integer durationFormat) {
		this.durationFormat = durationFormat;
	}

	public Integer getDurationValue() {
		return durationValue;
	}

	public void setDurationValue(Integer durationValue) {
		this.durationValue = durationValue;
	}

	public Integer getMortuaryStatusId() {
		return mortuaryStatusId;
	}

	public void setMortuaryStatusId(Integer mortuaryStatusId) {
		this.mortuaryStatusId = mortuaryStatusId;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public Integer getOrganzationId() {
		return organzationId;
	}

	public void setOrganzationId(Integer organzationId) {
		this.organzationId = organzationId;
	}

	public String getExpectedArrivalDate() {
		return expectedArrivalDate;
	}

	public void setExpectedArrivalDate(String expectedArrivalDate) {
		this.expectedArrivalDate = expectedArrivalDate;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getBroughtFrom() {
		return broughtFrom;
	}

	public void setBroughtFrom(String broughtFrom) {
		this.broughtFrom = broughtFrom;
	}

	public String getDeathReason() {
		return deathReason;
	}

	public void setDeathReason(String deathReason) {
		this.deathReason = deathReason;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
	
	
}
