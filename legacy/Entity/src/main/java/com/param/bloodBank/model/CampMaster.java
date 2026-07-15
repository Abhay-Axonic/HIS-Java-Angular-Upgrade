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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.model.PatientTypeMaster;
import com.param.global.model.AreaMaster;
import com.param.global.model.CityMaster;
import com.param.global.model.CountryMaster;
import com.param.global.model.DistrictMaster;
import com.param.global.model.StateMaster;

@NamedQueries({
	
	@NamedQuery(name="GET_CAMP_MASTER_LIST",
		query=" SELECT campMaster.campId AS campId, campMaster.campCode AS campCode,"
				+ " campMaster.campName AS campName, campMaster.address AS address ,  campMaster.organizationId AS organizationId,"
				+ " campMaster.unitId AS unitId, campMaster.status AS status, "
				+ " campMaster.countryId AS countryId, countryMaster.countryName AS countryName, "
				+ " campMaster.stateId AS stateId, stateMaster.stateName AS stateName, "
				+ " campMaster.cityId AS cityId, cityMaster.cityName AS cityName, "
				+ " campMaster.districtId AS districtId, districtMaster.districtName AS districtName, "
				+ " campMaster.areaId AS areaId, areaMaster.areaName AS areaName, patientTypeMaster.description AS patientTypeName, "
				+ " campMaster.patientTypeId AS patientTypeId, "
				+ " campMaster.pincode AS pincode "
				+ " FROM CampMaster campMaster "
				+ " INNER JOIN campMaster.countryMaster countryMaster "
				+ " INNER JOIN campMaster.stateMaster stateMaster "
				+ " INNER JOIN campMaster.areaMaster areaMaster "
				+ " INNER JOIN campMaster.stateMaster stateMaster "
				+ " INNER JOIN campMaster.districtMaster districtMaster "
				+ " INNER JOIN campMaster.cityMaster cityMaster "
				+ " LEFT JOIN  campMaster.patientTypeMaster patientTypeMaster "
				+ " WHERE campMaster.organizationId= :organizationId "
				+ " AND campMaster.unitId= :unitId "
				+ " ORDER BY campMaster.campId asc "
				)
	
	/*@NamedQuery(name="UPDATE_CAMP_MASTER_STATUS",
	query=" SELECT campMaster.campId AS campId, campMaster.campCode AS campCode "
			+ " FROM CampMaster campMaster "
			+ " INNER JOIN campMaster.stateMaster stateMaster "
			+ " INNER JOIN campMaster.districtMaster districtMaster "
			+ " WHERE campMaster.organizationId= :organizationId "
			+ " AND campMaster.unitId= :unitId "
			+ " ORDER BY campMaster.campId asc "
			)*/
	
})
	

@Entity
@Table(name="m_camp_master", schema="blood_bank")
@SequenceGenerator(name="camp_master_seq", sequenceName="blood_bank.camp_master_seq", allocationSize= 1)

public class CampMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "camp_master_seq")

	@Column(name = "camp_id")
	private Integer campId;
	
	@Column(name = "camp_code")
	private String campCode;
	
	@Column(name = "camp_name")
	private String campName;
	
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
	
	@Column(name = "pincode")
	private String pincode;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	
	@Column(name = "patient_type_id")
	private Integer patientTypeId;
	
	
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
	@JoinColumn(name = "patient_type_id", insertable = false, updatable = false)
	private PatientTypeMaster patientTypeMaster;

	public Integer getCampId() {
		return campId;
	}

	public void setCampId(Integer campId) {
		this.campId = campId;
	}

	public String getCampCode() {
		return campCode;
	}

	public void setCampCode(String campCode) {
		this.campCode = campCode;
	}

	public String getCampName() {
		return campName;
	}

	public void setCampName(String campName) {
		this.campName = campName;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}


	
	
	
}
