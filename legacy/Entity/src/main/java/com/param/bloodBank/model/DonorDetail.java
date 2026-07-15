package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.NationalityMaster;
import com.param.global.model.AreaMaster;
import com.param.global.model.CityMaster;
import com.param.global.model.CountryMaster;
import com.param.global.model.DistrictMaster;
import com.param.global.model.StateMaster;

@Entity
@Table(name="t_donor_details",schema="blood_bank")
@SequenceGenerator(name="donor_details_seq",sequenceName="blood_bank.donor_details_seq",allocationSize=1)
public class DonorDetail {

	@Id
	@Column(name = "patient_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donor_details_seq")
	private int donorDetailsId;

	@Column(name = "donor_id")
	private Integer donorId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "nationality_id")
	private Integer nationalityId;

	@Column(name = "race_id")
	private Integer raceId;

	@Column(name = "marital_status_id")
	private Integer maritalStatusId;

	@Column(name = "address")
	private String address;

	@Column(name = "country_id")
	private Integer countryId;

	@Column(name = "state_id")
	private Integer stateId;

	@Column(name = "district_id")
	private Integer districtId;

	@Column(name = "city_id")
	private Integer cityId;

	@Column(name = "area_id")
	private Integer areaId;

	@Column(name = "zip_code")
	private String zipCode;

	@Column(name = "phone_code")
	private String phoneCode;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "permanent_address")
	private String permanentAddress;

	@Column(name = "permanent_country_id")
	private Integer permanentCountryId;

	@Column(name = "permanent_state_id")
	private Integer permanentStateId;

	@Column(name = "permanent_district_id")
	private Integer permanentDistrictId;

	@Column(name = "permanent_city_id")
	private Integer permanentCityId;

	@Column(name = "permanent_area_id")
	private Integer permanentAreaId;

	@Column(name = "permanent_zip_code")
	private String permanentZipCode;

	@Column(name = "permanent_phone_code")
	private String permanentPhoneCode;

	@Column(name = "permanent_phone_number")
	private String permanentPhoneNumber;

	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donor_id", insertable = false, updatable = false)
	private DonorRegistrationMaster donorRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", insertable = false, updatable = false)
	private CountryMaster countryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id", insertable = false, updatable = false)
	private StateMaster stateMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id", insertable = false, updatable = false)
	private CityMaster cityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id", insertable = false, updatable = false)
	private AreaMaster areaMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id", insertable = false, updatable = false)
	private DistrictMaster districtMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nationality_id", insertable = false, updatable = false)
	private NationalityMaster nationalityMaster;

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public Integer getPermanentCountryId() {
		return permanentCountryId;
	}

	public void setPermanentCountryId(Integer permanentCountryId) {
		this.permanentCountryId = permanentCountryId;
	}

	public Integer getPermanentStateId() {
		return permanentStateId;
	}

	public void setPermanentStateId(Integer permanentStateId) {
		this.permanentStateId = permanentStateId;
	}

	public Integer getPermanentDistrictId() {
		return permanentDistrictId;
	}

	public void setPermanentDistrictId(Integer permanentDistrictId) {
		this.permanentDistrictId = permanentDistrictId;
	}

	public Integer getPermanentCityId() {
		return permanentCityId;
	}

	public void setPermanentCityId(Integer permanentCityId) {
		this.permanentCityId = permanentCityId;
	}

	public Integer getPermanentAreaId() {
		return permanentAreaId;
	}

	public void setPermanentAreaId(Integer permanentAreaId) {
		this.permanentAreaId = permanentAreaId;
	}

	public String getPermanentZipCode() {
		return permanentZipCode;
	}

	public void setPermanentZipCode(String permanentZipCode) {
		this.permanentZipCode = permanentZipCode;
	}

	public String getPermanentPhoneCode() {
		return permanentPhoneCode;
	}

	public void setPermanentPhoneCode(String permanentPhoneCode) {
		this.permanentPhoneCode = permanentPhoneCode;
	}

	public String getPermanentPhoneNumber() {
		return permanentPhoneNumber;
	}

	public void setPermanentPhoneNumber(String permanentPhoneNumber) {
		this.permanentPhoneNumber = permanentPhoneNumber;
	}

	public Integer getDonorId() {
		return donorId;
	}

	public void setDonorId(Integer donorId) {
		this.donorId = donorId;
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

	public Integer getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(Integer nationalityId) {
		this.nationalityId = nationalityId;
	}

	public Integer getRaceId() {
		return raceId;
	}

	public void setRaceId(Integer raceId) {
		this.raceId = raceId;
	}

	public Integer getMaritalStatusId() {
		return maritalStatusId;
	}

	public void setMaritalStatusId(Integer maritalStatusId) {
		this.maritalStatusId = maritalStatusId;
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

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public int getDonorDetailsId() {
		return donorDetailsId;
	}

	public void setDonorDetailsId(int donorDetailsId) {
		this.donorDetailsId = donorDetailsId;
	}

	
}
