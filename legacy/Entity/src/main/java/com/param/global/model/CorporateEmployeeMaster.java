package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedQueries({
	@NamedQuery(name="GET_CORPORATE_EMPLOYEE_DETAILS",
			query="select mstr.corporateId as corporateId,mstr.corporateEmployeeId as corporateEmployeeId,cmpMstr.companyDesc as companyDesc,mstr.employeeNumber as employeeNumber,concat(coalesce(mstr.firstName,'') ||' '|| coalesce(mstr.middleName,'') ||' '|| coalesce(mstr.lastName,'')) as firstName,"
					+ " mstr.mobileNo as mobileNo,mstr.emailId as emailId,cntyMstr.countryName as countryName,mstr.countryId as countryId,stMstr.stateName as stateName, mstr.stateId as stateId,"
					+ " disMstr.districtName as districtName,mstr.districtId as districtId,ctyMstr.cityName as cityName,mstr.cityId as cityId,arMstr.areaName as areaName,mstr.areaId as areaId,"
					+ " mstr.zipCode as zipCode,mstr.address as address,mstr.status as status "
					+ " from CorporateEmployeeMaster mstr inner join mstr.companyMaster cmpMstr "
					+ " inner join mstr.countryMaster cntyMstr inner join mstr.stateMaster stMstr "
					+ " inner join mstr.districtMaster disMstr inner join mstr.cityMaster ctyMstr "
					+ " inner join mstr.areaMaster arMstr  "
					+ " where mstr.organizationId=:orgId "
					+ " and mstr.unitId=:unitId"),
	@NamedQuery(name="GET_CORPORATE_EMPLOYEE_MASTER_DETAILS_BY_ID",
		query="select mstr.corporateId as corporateId,mstr.corporateEmployeeId as corporateEmployeeId,cmpMstr.companyDesc as companyDesc,mstr.employeeNumber as employeeNumber,coalesce(mstr.firstName,'') as firstName,"
				+ " coalesce(mstr.middleName,'') as middleName, coalesce(mstr.lastName,'') as lastName, "
					+ " mstr.mobileNo as mobileNo,mstr.emailId as emailId,cntyMstr.countryName as countryName,mstr.countryId as countryId,stMstr.stateName as stateName, mstr.stateId as stateId,"
					+ " disMstr.districtName as districtName,mstr.districtId as districtId,ctyMstr.cityName as cityName,mstr.cityId as cityId,arMstr.areaName as areaName,mstr.areaId as areaId,"
					+ " mstr.zipCode as zipCode,mstr.address as address,mstr.status as status "
					+ " from CorporateEmployeeMaster mstr inner join mstr.companyMaster cmpMstr "
					+ " inner join mstr.countryMaster cntyMstr inner join mstr.stateMaster stMstr "
					+ " inner join mstr.districtMaster disMstr inner join mstr.cityMaster ctyMstr "
					+ " inner join mstr.areaMaster arMstr  "
					+ " where mstr.organizationId=:orgId "
					+ " and mstr.unitId=:unitId"
					+ " and mstr.corporateEmployeeId=:corporateEmployeeId"),
	@NamedQuery(name="VERIFY_CORPORATE_EMPLOYEE_DETAILS",
	query="select mstr.corporateId as corporateId,mstr.corporateEmployeeId as corporateEmployeeId,cmpMstr.companyDesc as companyDesc,mstr.employeeNumber as employeeNumber,coalesce(mstr.firstName,'') as firstName,"
			+ " coalesce(mstr.middleName,'') as middleName, coalesce(mstr.lastName,'') as lastName, "
				+ " mstr.mobileNo as mobileNo,mstr.emailId as emailId,cntyMstr.countryName as countryName,mstr.countryId as countryId,stMstr.stateName as stateName, mstr.stateId as stateId,"
				+ " disMstr.districtName as districtName,mstr.districtId as districtId,ctyMstr.cityName as cityName,mstr.cityId as cityId,arMstr.areaName as areaName,mstr.areaId as areaId,"
				+ " mstr.zipCode as zipCode,mstr.address as address,mstr.status as status "
				+ " from CorporateEmployeeMaster mstr inner join mstr.companyMaster cmpMstr "
				+ " inner join mstr.countryMaster cntyMstr inner join mstr.stateMaster stMstr "
				+ " inner join mstr.districtMaster disMstr inner join mstr.cityMaster ctyMstr "
				+ " inner join mstr.areaMaster arMstr  "
				+ " where mstr.organizationId=:orgId "
				+ " and mstr.unitId=:unitId"
				+ " and mstr.employeeNumber=:empNo "
				+ " and mstr.corporateId=:companyId ")
})

@Entity
@Table(name="m_corporate_employee",schema="public")
@SequenceGenerator(name="m_corporate_employee_seq",sequenceName="public.m_corporate_employee_seq",allocationSize=1)
public class CorporateEmployeeMaster {
	@Id
	@Column(name="corporate_employee_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_corporate_employee_seq")
	private Integer corporateEmployeeId;
	
	@Column(name="corporate_id")
	private Integer corporateId;
	
	@Column(name="employee_number")
	private String employeeNumber;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="middle_name")
	private String middleName;

	@Column(name="last_name")
	private String lastName;
	
	@Column(name="mobile_no")
	private String mobileNo;
	
	@Column(name="email_id")
	private String emailId;
	
	@Column(name="address")
	private String address;
	
	
	@Column(name="country_id")
	private Integer countryId;
	
	@Column(name="state_id")
	private Integer stateId;
	
	@Column(name="district_id")
	private Integer districtId;
	
	@Column(name="city_id")
	private Integer cityId;
	
	@Column(name="area_id")
	private Integer areaId;
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", insertable = false, updatable = false)
	private CountryMaster countryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id", insertable = false, updatable = false)
	private StateMaster stateMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "district_id", insertable = false, updatable = false)
	private DistrictMaster districtMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id", insertable = false, updatable = false)
	private CityMaster  cityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id", insertable = false, updatable = false)
	private AreaMaster  areaMaster;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gender_id", insertable = false, updatable = false)
	private GenderMaster genderMaster;*/	
	
/*	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_category_id", insertable = false, updatable = false)
	private EmployeeCategoryMaster employeeCategoryMaster;*/
/*	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "emp_designation_id", insertable = false, updatable = false)
	private EmployeeDesignationMaster employeeDesignationMaster;*/
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "identification_type_id", insertable = false, updatable = false)
	private IdentificationMaster identificationMaster;*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "corporate_id", insertable = false, updatable = false)
	private CompanyMaster companyMaster;
	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "corporateEmployeeMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CorporateEmpDependentDetails> employeeDetailsList;

	public Integer getCorporateEmployeeId() {
		return corporateEmployeeId;
	}

	public void setCorporateEmployeeId(Integer corporateEmployeeId) {
		this.corporateEmployeeId = corporateEmployeeId;
	}

	public Integer getCorporateId() {
		return corporateId;
	}

	public void setCorporateId(Integer corporateId) {
		this.corporateId = corporateId;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
		
}
