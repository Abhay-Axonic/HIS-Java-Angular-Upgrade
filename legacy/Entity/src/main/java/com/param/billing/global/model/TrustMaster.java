package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.PatientCategoryMaster;

@NamedQueries({
	@NamedQuery(name = "GET_TRUST_BY_TRUST_ID", 
			query = "SELECT trustMaster.trustId as trustId,"
			+ " trustMaster.trustName as trustName," 
			+ " trustMaster.trustAddress as trustAddress,"
			+ " trustMaster.countryId as countryId,"
			+ " trustMaster.stateId as stateId,"
			+ " trustMaster.districtId as districtId,"
			+ " trustMaster.cityId as cityId,"
			+ " trustMaster.areaId as areaId,"
			+ " trustMaster.zipCode as zipCode ,"
			+ " trustMaster.contactPersonName as contactPersonName,"
			+ " trustMaster.contactPersonEmail as contactPersonEmail,"
			+ " trustMaster.phoneNo as phoneNo,"
			+ " trustMaster.cellNo as cellNo,"
			+ " trustMaster.validFrom as validFrom,"
			+ " trustMaster.patientCategoryId as patientCategoryId,"
			+ " trustMaster.patientTypeId as patientTypeId,"
			+ " trustMaster.disease as disease,"
			+ " trustMaster.trustCompanyId as trustCompanyId, "
			+ " trustMaster.maxLimitPerPerson as maxLimitPerPerson,"
			+ " trustMaster.isCompanyContract as isCompanyContract ,"
			+ " trustMaster.createdDate as createdDate,"
			+ " trustMaster.updatedDate as updatedDate,"
			+ " trustMaster.createdBy as createdBy,"
			+ " trustMaster.updatedBy as updatedBy,"
			+ " trustMaster.trustCompanyId as trustCompanyId,"
			+ " trustMaster.trustStatus as trustStatus ,"
			+ " trustMaster.unitId as unitId,"
			+ " trustMaster.orgId as orgId,"
			+ " patientCategoryMaster.patientCategory as patientCategory "
			+ " FROM TrustMaster trustMaster"
			+ " JOIN trustMaster.patientCategoryMaster patientCategoryMaster"
			+ " WHERE trustMaster.orgId=:orgId "
			+ " AND trustMaster.unitId=:unitId "
			+ " AND trustMaster.trustId=:trustId "),

	@NamedQuery(name = "GET_TRUST_BY_CODE", query = "SELECT trustMaster.trustId as trustId,"
			+ " trustMaster.trustName as trustName," 
			+ " trustMaster.trustAddress as trustAddress,"
			+ " trustMaster.countryId as countryId,"
			+ " trustMaster.stateId as stateId,"
			+ " trustMaster.districtId as districtId,"
			+ " trustMaster.cityId as cityId,"
			+ " trustMaster.areaId as areaId,"
			+ " trustMaster.zipCode as zipCode ,"
			+ " trustMaster.contactPersonName as contactPersonName,"
			+ " trustMaster.contactPersonEmail as contactPersonEmail,"
			+ " trustMaster.phoneNo as phoneNo,"
			+ " trustMaster.cellNo as cellNo,"
			+ " trustMaster.validFrom as validFrom,"
			+ " trustMaster.patientCategoryId as patientCategoryId,"
			+ " trustMaster.patientTypeId as patientTypeId,"
			+ " trustMaster.disease as disease,"
			+ " trustMaster.maxLimitPerPerson as maxLimitPerPerson,"
			+ " trustMaster.isCompanyContract as isCompanyContract ,"
			+ " trustMaster.trustCompanyId as trustCompanyId, "
			+ " trustMaster.createdDate as createdDate,"
			+ " trustMaster.updatedDate as updatedDate,"
			+ " trustMaster.createdBy as createdBy,"
			+ " trustMaster.updatedBy as updatedBy,"
			+ " trustMaster.trustCompanyId as trustCompanyId,"
			+ " trustMaster.trustStatus as trustStatus ,"
			+ " trustMaster.unitId as unitId,"
			+ " trustMaster.orgId as orgId ,"
			+ " patientCategoryMaster.patientCategory as patientCategory "
			+ " FROM TrustMaster trustMaster"
			+ " JOIN trustMaster.patientCategoryMaster patientCategoryMaster"
			+ " WHERE trustMaster.orgId=:orgId "
			+ " AND trustMaster.unitId=:unitId "),
		

	@NamedQuery(name = "UPDATE_GET_TRUST_BY_CODE", query = "SELECT trustMaster.trustId as trustId,"
			+ " trustMaster.trustName as trustName," 
			+ " trustMaster.trustAddress as trustAddress,"
			+ " trustMaster.countryId as countryId,"
			+ " trustMaster.stateId as stateId,"
			+ " trustMaster.districtId as districtId,"
			+ " trustMaster.cityId as cityId,"
			+ " trustMaster.areaId as areaId,"
			+ " trustMaster.zipCode as zipCode ,"
			+ " trustMaster.contactPersonName as contactPersonName,"
			+ " trustMaster.contactPersonEmail as contactPersonEmail,"
			+ " trustMaster.phoneNo as phoneNo,"
			+ " trustMaster.cellNo as cellNo,"
			+ " trustMaster.validFrom as validFrom,"
			+ " trustMaster.patientCategoryId as patientCategoryId,"
			+ " trustMaster.patientTypeId as patientTypeId,"
			+ " trustMaster.disease as disease,"
			+ " trustMaster.maxLimitPerPerson as maxLimitPerPerson,"
			+ " trustMaster.isCompanyContract as isCompanyContract ,"
			+ " trustMaster.trustCompanyId as trustCompanyId,"
			+ " trustMaster.createdDate as createdDate,"
			+ " trustMaster.updatedDate as updatedDate,"
			+ " trustMaster.createdBy as createdBy,"
			+ " trustMaster.updatedBy as updatedBy,"
			+ " trustMaster.trustCompanyId as trustCompanyId,"
			+ " trustMaster.trustStatus as trustStatus,"
			+ " trustMaster.unitId as unitId,"
			+ " trustMaster.orgId as orgId, "
			+ " patientCategoryMaster.patientCategory as patientCategory "
			+ " FROM TrustMaster trustMaster "
			+ " JOIN trustMaster.patientCategoryMaster patientCategoryMaster"
			+ " WHERE trustMaster.orgId=:orgId "
			+ " AND trustMaster.unitId=:unitId "
			+ " AND trustMaster.trustId <> :trustId "),

	@NamedQuery(name = "GET_PAGINATED_TRUST_MASTER_LIST", 
	query = "SELECT trustMaster.trustId as trustId,"
			+ " trustMaster.trustName as trustName," 
			+ " trustMaster.trustAddress as trustAddress,"
			+ " trustMaster.countryId as countryId,"
			+ " trustMaster.stateId as stateId,"
			+ " trustMaster.districtId as districtId,"
			+ " trustMaster.cityId as cityId,"
			+ " trustMaster.areaId as areaId,"
			+ " trustMaster.zipCode as zipCode ,"
			+ " trustMaster.contactPersonName as contactPersonName,"
			+ " trustMaster.contactPersonEmail as contactPersonEmail,"
			+ " trustMaster.phoneNo as phoneNo,"
			+ " trustMaster.cellNo as cellNo,"
			+ " trustMaster.validFrom as validFrom,"
			+ " trustMaster.patientCategoryId as patientCategoryId,"
	        + " trustMaster.patientTypeId as patientTypeId,"
			+ " trustMaster.disease as disease,"
			+ " trustMaster.maxLimitPerPerson as maxLimitPerPerson,"
			+ " trustMaster.trustCompanyId as trustCompanyId, "
			+ " trustMaster.isCompanyContract as isCompanyContract ,"
			+ " trustMaster.createdDate as createdDate,"
			+ " trustMaster.updatedDate as updatedDate,"
			+ " trustMaster.createdBy as createdBy,"
			+ " trustMaster.updatedBy as updatedBy,"
			+ " trustMaster.trustStatus as trustStatus ,"
			+ " trustMaster.trustCompanyId as trustCompanyId,"
			+ " trustMaster.unitId as unitId,"
			+ " trustMaster.orgId as orgId,"
			+ " patientCategoryMaster.patientCategory as patientCategory "
			+ " FROM TrustMaster trustMaster "
			+ " JOIN trustMaster.patientCategoryMaster patientCategoryMaster"
			+ " WHERE trustMaster.orgId=:orgId "
			+ " AND trustMaster.unitId=:unitId "
			+ " ORDER BY trustId DESC"),
	
	@NamedQuery(name = "GET_ACTIVE_TRUST_LIST", query = "SELECT trustMaster.trustId as trustId,"
			+ " trustMaster.trustName as trustName," 
			+ " trustMaster.trustAddress as trustAddress,"
			+ " trustMaster.countryId as countryId,"
			+ " trustMaster.stateId as stateId,"
			+ " trustMaster.districtId as districtId,"
			+ " trustMaster.cityId as cityId,"
			+ " trustMaster.areaId as areaId,"
			+ " trustMaster.zipCode as zipCode ,"
			+ " trustMaster.contactPersonName as contactPersonName,"
			+ " trustMaster.contactPersonEmail as contactPersonEmail,"
			+ " trustMaster.phoneNo as phoneNo,"
			+ " trustMaster.cellNo as cellNo,"
			+ " trustMaster.validFrom as validFrom,"
			+ " trustMaster.patientCategoryId as patientCategoryId,"
			+ " trustMaster.patientTypeId as patientTypeId,"
			+ " trustMaster.disease as disease,"
			+ " trustMaster.maxLimitPerPerson as maxLimitPerPerson,"
			+ " trustMaster.isCompanyContract as isCompanyContract ,"
			+ " trustMaster.trustCompanyId as trustCompanyId, "
			+ " trustMaster.createdDate as createdDate,"
			+ " trustMaster.updatedDate as updatedDate,"
			+ " trustMaster.createdBy as createdBy,"
			+ " trustMaster.updatedBy as updatedBy,"
			+ " trustMaster.trustStatus as trustStatus ,"
			+ " trustMaster.trustCompanyId as trustCompanyId,"
			+ " trustMaster.unitId as unitId,"
			+ " trustMaster.orgId as orgId," 
			+ " patientCategoryMaster.patientCategory as patientCategory "
			+ " FROM TrustMaster trustMaster"
			+ " JOIN trustMaster.patientCategoryMaster patientCategoryMaster"
			+ " WHERE trustMaster.orgId=:orgId "
			+ " AND trustMaster.unitId=:unitId "
			+ " AND trustMaster.trustStatus='A' "),
	
	@NamedQuery(name = "GET_ACTIVE_TRUST_LIST_BY_ID", query = "SELECT trustMaster.trustId as trustId,"
			+ " trustMaster.trustName as trustName," 
			+ " trustMaster.trustAddress as trustAddress,"
			+ " trustMaster.countryId as countryId,"
			+ " trustMaster.stateId as stateId,"
			+ " trustMaster.districtId as districtId,"
			+ " trustMaster.cityId as cityId,"
			+ " trustMaster.areaId as areaId,"
			+ " trustMaster.zipCode as zipCode ,"
			+ " trustMaster.contactPersonName as contactPersonName,"
			+ " trustMaster.contactPersonEmail as contactPersonEmail,"
			+ " trustMaster.phoneNo as phoneNo,"
			+ " trustMaster.cellNo as cellNo,"
			+ " trustMaster.validFrom as validFrom,"
			+ " trustMaster.patientCategoryId as patientCategoryId,"
			+ " trustMaster.patientTypeId as patientTypeId,"
			+ " trustMaster.disease as disease,"
			+ " trustMaster.maxLimitPerPerson as maxLimitPerPerson,"
			+ " trustMaster.isCompanyContract as isCompanyContract ,"
			+ " trustMaster.trustCompanyId as trustCompanyId, "
			+ " trustMaster.createdDate as createdDate,"
			+ " trustMaster.updatedDate as updatedDate,"
			+ " trustMaster.createdBy as createdBy,"
			+ " trustMaster.updatedBy as updatedBy,"
			+ " trustMaster.trustStatus as trustStatus ,"
			+ " trustMaster.trustCompanyId as trustCompanyId,"
			+ " trustMaster.unitId as unitId,"
			+ " trustMaster.orgId as orgId,"
			+ " patientCategoryMaster.patientCategory as patientCategory "
			+ " FROM TrustMaster trustMaster"
			+ " JOIN trustMaster.patientCategoryMaster patientCategoryMaster"
			+ " WHERE trustMaster.orgId=:orgId "
			+ " AND trustMaster.unitId=:unitId "
			+ " AND trustMaster.trustId=:trustId "),
			
})
@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_TOTAL_TRUST_RECORD", query = "select count(*) from billing.m_trust_master where "
			+ "org_id = :orgId and unit_id = :unitId ") })

@Entity
@Table(name = "m_trust_master", schema = "billing")
@SequenceGenerator(name = "m_trust_master_seq", sequenceName = "billing.m_trust_master_seq", allocationSize = 1)

public class TrustMaster {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_trust_master_seq")
	@Column(name = "trust_id")
	private Integer trustId;
	
	@Column(name="trust_name")
	private String trustName;
	
	@Column(name="trust_address")
	private String trustAddress;

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
	private Integer zipCode ;

	@Column(name="contactPersonName")
	private String contactPersonName;
	
	@Column(name="contact_person_email")
	private String  contactPersonEmail;
	
	@Column(name="phone_no")
	private String  phoneNo;
	
	@Column(name="cell_no")
	private String  cellNo;
	
	@Column(name="valid_from")
	private Date validFrom;
	
	@Column(name = "patient_category_id")
	private Integer patientCategoryId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "patient_type_id")
	private Integer  patientTypeId;
	
	@Column(name="disease")
	private String  disease;
	
	@Column(name="max_limit_per_person")
	private String maxLimitPerPerson;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="trust_status")
	private char trustStatus;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="is_company_contract")
	private char isCompanyContract;
	
	@Column(name = "trust_company_id")
	private Integer trustCompanyId;
	

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "trustMaster")
	private List<TrustVisitTypeMaster> listTrustVisitTypeMaster;	*/
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="patient_category_id", insertable=false, updatable=false)
	private PatientCategoryMaster patientCategoryMaster;


	public Integer getTrustId() {
		return trustId;
	}


	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}


	public String getTrustName() {
		return trustName;
	}


	public void setTrustName(String trustName) {
		this.trustName = trustName;
	}


	public String getTrustAddress() {
		return trustAddress;
	}


	public void setTrustAddress(String trustAddress) {
		this.trustAddress = trustAddress;
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


	public Integer getZipCode() {
		return zipCode;
	}


	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
	}


	public String getContactPersonName() {
		return contactPersonName;
	}


	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}


	public String getContactPersonEmail() {
		return contactPersonEmail;
	}


	public void setContactPersonEmail(String contactPersonEmail) {
		this.contactPersonEmail = contactPersonEmail;
	}


	public String getPhoneNo() {
		return phoneNo;
	}


	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public String getCellNo() {
		return cellNo;
	}


	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}


	public Date getValidFrom() {
		return validFrom;
	}


	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}


	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}


	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}


	public Integer getVisitTypeId() {
		return visitTypeId;
	}


	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}


	public Integer getPatientTypeId() {
		return patientTypeId;
	}


	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}


	public String getDisease() {
		return disease;
	}


	public void setDisease(String disease) {
		this.disease = disease;
	}


	public String getMaxLimitPerPerson() {
		return maxLimitPerPerson;
	}


	public void setMaxLimitPerPerson(String maxLimitPerPerson) {
		this.maxLimitPerPerson = maxLimitPerPerson;
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


	public char getTrustStatus() {
		return trustStatus;
	}


	public void setTrustStatus(char trustStatus) {
		this.trustStatus = trustStatus;
	}


	public Integer getUnitId() {
		return unitId;
	}


	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}


	public Integer getOrgId() {
		return orgId;
	}


	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}


	public Integer getTrustCompanyId() {
		return trustCompanyId;
	}


	public void setTrustCompanyId(Integer trustCompanyId) {
		this.trustCompanyId = trustCompanyId;
	}


	public PatientCategoryMaster getPatientCategoryMaster() {
		return patientCategoryMaster;
	}


	public void setPatientCategoryMaster(PatientCategoryMaster patientCategoryMaster) {
		this.patientCategoryMaster = patientCategoryMaster;
	}


	public char getIsCompanyContract() {
		return isCompanyContract;
	}


	public void setIsCompanyContract(char isCompanyContract) {
		this.isCompanyContract = isCompanyContract;
	}
	


}
