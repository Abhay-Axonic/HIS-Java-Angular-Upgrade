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

@NamedQueries({
	
	@NamedQuery(name="CHECK_ALREADY_EXIST_REG",
		query="SELECT deathRegistration.deathRegistrationId AS deathRegistrationId "
					+ "	FROM DeathRegistration deathRegistration "
					+ " WHERE (LOWER(deathRegistration.firstName)=:firstName OR deathRegistration.firstName=:firstName) "
					+ " AND (LOWER(deathRegistration.lastName)=:lastName OR deathRegistration.lastName=:lastName) "
					//+ " AND deathRegistration.unitId=:unitId "
					//+ " AND deathRegistration.organizationId=:organizationId "
					+ " AND deathRegistration.age=:age "
					+ " AND deathRegistration.genderId=:genderId "
					),
	})

@Entity
@Table(name="t_death_registration",schema="patient")
@SequenceGenerator(name = "deth_registration_seq", sequenceName = "patient.deth_registration_seq", allocationSize = 1)
public class DeathRegistration {

	@Id
	@Column( name="death_registration_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="deth_registration_seq")
	private int deathRegistrationId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="gender_id")
	private Integer genderId;
	
	@Column(name="mark_on_decease")
	private String markOnDecease;
	
	@Column(name="date_of_death")
	private Date dateOfDeath;
	
	@Column(name="is_medico_legal")
	private char isMedicoLegal;
	
	@Column(name="uhid_number")
	private String uhidNumber;
	
	@Column(name="kin_id")
	private Integer kinId;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "brought_from")
	private String broughtFrom;
	
	@Column(name = "death_reason")
	private String deathReason;
	
	@Column(name = "prefix_id")
	private Integer prefixId;
	
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
	
	@Column(name = "age")
	private String age;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, nullable = false, updatable = false)
	private GenderMaster genderMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kin_id", insertable = false, nullable = false, updatable = false)
	private KinDetails kinDetails;
	

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

	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(Date dateOfDeath) {
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

	

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	
	
}
/*CREATE TABLE patient.t_death_registration
(
  deth_registration_id integer NOT NULL,
  organization_id integer,
  unit_id integer,
  first_name character varying,
  last_name character varying,
  gender_id integer,
  mark_on_decease character varying,
  date_of_death timestamp without time zone,
  is_medico_legal character(1) DEFAULT 'N'::bpchar,
  uhid_number character varying,
  kin_id integer,
  created_by integer,
  created_date timestamp without time zone DEFAULT now(),
  updated_by integer,
  updated_date timestamp without time zone DEFAULT now(),
  status character(1) DEFAULT 'A'::bpchar,
  CONSTRAINT t_deth_registration_pkey PRIMARY KEY (deth_registration_id)
)*/