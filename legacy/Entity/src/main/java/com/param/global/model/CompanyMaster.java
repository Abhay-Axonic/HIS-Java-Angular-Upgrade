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
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.model.DepositMaster;
import com.param.service.global.model.MCompanyContractMaster;

@NamedQueries({
	@NamedQuery(name="GET_ACTIVE_COMPANY_MASTER_LIST", 
			   query=" SELECT company.companyId as companyId, company.companyCode as companyCode, company.companyDesc as companyDesc,"
			   		+ " company.companyTypeId as companyTypeId, "
					+ " company.organizationId as organizationId, "
					+ " company.unitId as unitId "
			   		+ " FROM CompanyMaster company"
			   		+ " WHERE company.status='A' AND company.unitId=:unitId AND company.organizationId=:orgId AND company.isTrust='I' "
			   		+ " ORDER BY company.companyDesc ASC "),
	@NamedQuery(name="GET_COMPANY_MASTER_LIST", 
	   query=" SELECT company.companyId AS companyId,"
	   		+ " company.companyCode AS companyCode,"
	   		+ " company.companyDesc AS companyDesc,"
	   		+ " company.companyTypeId AS companyTypeId,"
	   		+ " coalesce(company.reportPath,'') AS reportPath,company.paymentTerms AS paymentTerms,company.creditLimit AS creditLimit,"
	   		+ " company.arCodeId AS arCodeId, arc.aRCodeDesc as aRCodeDesc, "
	   		+ " arc.aRCode as arCode, company.status AS status,"
	   		+ " to_char(company.startDate,'dd/MM/yyyy') AS startDate,to_char(company.endDate,'dd/MM/yyyy') AS endDate,"
	   		+ " company.faxNo AS faxNo,company.emailId AS emailId,company.contactPersonName AS contactPersonName,company.mobileNo AS mobileNo,"
	   		+ " company.address AS address,cntryMstr.countryName AS countryName,statemstr.stateName AS stateName ,"
	   		+ " districtMstr.districtName AS districtName,cityMstr.cityName AS cityName,"
	   		+ " areaMstr.areaName AS areaName,company.zipcode AS zipcode, company.districtId as districtId,company.generalLedgerId as generalLedgerId "
	   		+ " FROM CompanyMaster company "
	   		+ " INNER JOIN company.countryMaster cntryMstr "
	   		+ " LEFT JOIN company.stateMaster  statemstr "
	   		+ " LEFT JOIN company.districtMaster  districtMstr "
	   		+ " LEFT JOIN company.cityMaster  cityMstr "
	   		+ " LEFT JOIN company.areaMaster  areaMstr "
	   		+ " LEFT JOIN company.arCodeMaster arc "
	   		+ " WHERE company.unitId=:unitId AND company.organizationId=:orgId AND company.companyTypeId=:companyTypeId AND company.isTrust='I' "
	   		+ " ORDER BY company.companyDesc ASC "),
	@NamedQuery(name="GET_COMPANY_MASTER_LIST_BY_ID", 
	   query=" SELECT company.companyId AS companyId,"
	   		+ " company.companyCode AS companyCode,"
	   		+ " company.companyDesc AS companyDesc,"
	   		+ " company.companyTypeId AS companyTypeId,"
	   		+ " company.arCodeId AS arCodeId,"
	   		+ " arc.aRCode as arCode,company.paymentTerms AS paymentTerms,company.creditLimit AS creditLimit, "
	   		+ " to_char(company.startDate,'MM/dd/yyyy') AS startDate,to_char(company.endDate,'MM/dd/yyyy') AS endDate,"
	   		+ " company.faxNo AS faxNo,company.emailId AS emailId,company.contactPersonName AS contactPersonName,company.mobileNo AS mobileNo,"
	   		+ " company.address AS address,company.countryId AS countryId ,cntryMstr.countryName AS countryName,company.stateId AS stateId,statemstr.stateName AS stateName ,"
	   		+ " company.districtId AS districtId,districtMstr.districtName AS districtName,company.districtId AS districtId,company.cityId AS cityId,cityMstr.cityName AS cityName,"
	   		+ " company.areaId AS areaId, areaMstr.areaName AS areaName,company.zipcode AS zipcode,coalesce(company.reportPath,'') AS reportPath ,"
	   		+ " company.blockReportPrint as blockReportPrint,company.generalLedgerId as generalLedgerId "
	   		+ " FROM CompanyMaster company "
	   		+ " INNER JOIN company.countryMaster cntryMstr "
	   		+ " LEFT JOIN company.stateMaster  statemstr "
	   		+ " LEFT JOIN company.districtMaster  districtMstr "
	   		+ " LEFT JOIN company.cityMaster  cityMstr "
	   		+ " LEFT JOIN company.areaMaster  areaMstr "
	   		+ " LEFT JOIN company.arCodeMaster arc "
	   		+ " WHERE company.unitId=:unitId AND company.organizationId=:orgId AND company.companyId=:companyId AND company.isTrust='I'  "),
	@NamedQuery(name="GET_COMPANY_MASTER_LIST_BY_NAME", 
	   query=" SELECT company.companyId AS companyId "	   		
	   		+ " FROM CompanyMaster company "	   		
	   		+ " WHERE company.unitId=:unitId AND company.organizationId=:orgId "
	   		+ " AND LOWER(company.companyDesc)=:companyName AND LOWER(company.emailId)=:emailId "
	   		+ " AND company.faxNo=:faxNo  AND company.isTrust='I'  "),
	@NamedQuery(name="GET_COMPANY_MASTER_LIST_BY_NAME_NOT_BY_ID", 
	   query=" SELECT company.companyId AS companyId "	   		
	   		+ " FROM CompanyMaster company "	   		
	   		+ " WHERE company.unitId=:unitId AND company.organizationId=:orgId "
	   		+ " AND LOWER(company.companyDesc)=:companyName AND LOWER(company.emailId)=:emailId "
	   		+ " AND company.faxNo=:faxNo "
	   		+ " AND company.companyId!=:companyId AND company.isTrust='I'  "),
	@NamedQuery(name="GET_ACTIVE_TPA", 
	   query=" SELECT company.companyId as companyId, company.companyCode as companyCode, company.companyDesc as companyDesc,"
	   		+ " company.companyTypeId as companyTypeId"
	   		+ " FROM CompanyMaster company"
	   		+ " WHERE company.status='A' AND company.companyTypeId=2 AND company.unitId=:unitId AND company.organizationId=:orgId AND company.isTrust='I' "
	   		+ " ORDER BY company.companyDesc ASC "),
	@NamedQuery(name="UPDATE_COMPANY_IMAGE",query="UPDATE CompanyMaster com "
			+ " SET reportPath=:imagePath"
			+ " WHERE com.organizationId=:orgId"
			+ " AND com.unitId=:unitId"
			+ " AND com.companyId=:companyId"),
	
	@NamedQuery(name="GET_COMPANY_TRUST_MASTER_LIST", 
	   query=" SELECT company.companyId AS trustId,"
	   		+ " company.companyCode AS trustName,"
	   		+ " company.companyDesc AS trustName,"
	   		+ " company.companyTypeId AS companyTypeId,"
	   		+ " to_char(company.startDate,'MM/dd/yyyy') AS startDate,"
	   		+ " company.emailId AS contactPersonEmail,company.contactPersonName AS contactPersonName,company.mobileNo AS phoneNo,"
	   		+ " company.address AS trustAddress,company.countryId AS countryId ,cntryMstr.countryName AS countryName,company.stateId AS stateId,statemstr.stateName AS stateName ,"
	   		+ " company.districtId AS districtId,districtMstr.districtName AS districtName,company.districtId AS districtId,company.cityId AS cityId,cityMstr.cityName AS cityName,"
	   		+ " company.areaId AS areaId, areaMstr.areaName AS areaName,company.residencePhoneNo as cellNo, company.status as trustStatus ,company.zipcode AS zipCode "
	   		+ " FROM CompanyMaster company "
	   		+ " INNER JOIN company.countryMaster cntryMstr "
	   		+ " LEFT JOIN company.stateMaster  statemstr "
	   		+ " LEFT JOIN company.districtMaster  districtMstr "
	   		+ " LEFT JOIN company.cityMaster  cityMstr "
	   		+ " LEFT JOIN company.areaMaster  areaMstr "
	   		+ " LEFT JOIN company.arCodeMaster arc "
	   		+ " WHERE  company.organizationId=:orgId AND company.unitId=:unitId AND company.isTrust='A' order by  company.companyId  DESC"),
	
	@NamedQuery(name="GET_COMPANY_TRUST_ACTIVE_MASTER_LIST", 
	   query=" SELECT company.companyId AS trustId,"
	   		+ " company.companyCode AS trustName,"
	   		+ " company.companyDesc AS trustName,"
	   		+ " company.companyTypeId AS companyTypeId,"
	   		+ " to_char(company.startDate,'MM/dd/yyyy') AS startDate,"
	   		+ " company.emailId AS contactPersonEmail,company.contactPersonName AS contactPersonName,company.mobileNo AS phoneNo,"
	   		+ " company.address AS trustAddress,company.countryId AS countryId ,cntryMstr.countryName AS countryName,company.stateId AS stateId,statemstr.stateName AS stateName ,"
	   		+ " company.districtId AS districtId,districtMstr.districtName AS districtName,company.districtId AS districtId,company.cityId AS cityId,cityMstr.cityName AS cityName,"
	   		+ " company.areaId AS areaId, areaMstr.areaName AS areaName,company.residencePhoneNo as cellNo, company.status as trustStatus ,company.zipcode AS zipCode "
	   		+ " FROM CompanyMaster company "
	   		+ " INNER JOIN company.countryMaster cntryMstr "
	   		+ " LEFT JOIN company.stateMaster  statemstr "
	   		+ " LEFT JOIN company.districtMaster  districtMstr "
	   		+ " LEFT JOIN company.cityMaster  cityMstr "
	   		+ " LEFT JOIN company.areaMaster  areaMstr "
	   		+ " LEFT JOIN company.arCodeMaster arc "
	   		+ " WHERE  company.organizationId=:orgId AND company.unitId=:unitId AND company.isTrust='A' AND  company.status='A'   order by  company.companyId  DESC"),
	
	@NamedQuery(name="GET_COMPANY_TRUST_MASTER_BY_ID", 
	   query=" SELECT company.companyId AS trustId,"
	   		+ " company.companyCode AS trustName,"
	   		+ " company.companyDesc AS trustName,"
	   		+ " company.companyTypeId AS companyTypeId,"
	   		+ " to_char(company.startDate,'MM/dd/yyyy') AS startDate,"
	   		+ " company.emailId AS contactPersonEmail,company.contactPersonName AS contactPersonName,company.mobileNo AS phoneNo,"
	   		+ " company.address AS trustAddress,company.countryId AS countryId ,cntryMstr.countryName AS countryName,company.stateId AS stateId,statemstr.stateName AS stateName ,"
	   		+ " company.districtId AS districtId,districtMstr.districtName AS districtName,company.districtId AS districtId,company.cityId AS cityId,cityMstr.cityName AS cityName,"
	   		+ " company.areaId AS areaId, areaMstr.areaName AS areaName,company.residencePhoneNo as cellNo,company.status as trustStatus,company.zipcode AS zipCode "
	   		+ " FROM CompanyMaster company "
	   		+ " INNER JOIN company.countryMaster cntryMstr "
	   		+ " LEFT JOIN company.stateMaster  statemstr "
	   		+ " LEFT JOIN company.districtMaster  districtMstr "
	   		+ " LEFT JOIN company.cityMaster  cityMstr "
	   		+ " LEFT JOIN company.areaMaster  areaMstr "
	   		+ " LEFT JOIN company.arCodeMaster arc "
	   		+ " WHERE  company.organizationId=:orgId AND company.unitId=:unitId AND  company.companyId =:trustId AND company.status='A' AND company.isTrust='A' order by  company.companyId  DESC"),
})
@NamedNativeQuery(name="GET_COMPANY_CONTRACT_GRADE_BY_COMPANY_ID",
              query="SELECT mpper.contract_grade_master_id AS \"contractGradeMasterId\","
              		+ " gradeMstr.grade_desc AS \"gradeDesc\" "
              		+ " FROM service.t_company_contarct_grade_mpper mpper "
              		+ " INNER JOIN service.m_contract_grade_master gradeMstr on mpper.contract_grade_master_id=gradeMstr.contract_grade_master_id"
              		+ " WHERE mpper.company_id=:companyId AND mpper.status='A' ")
@Entity
@Table(name="t_company_master", schema="public")
@SequenceGenerator(name = "company_master_seq", sequenceName = "public.company_master_seq", allocationSize = 1)
public class CompanyMaster {
	  
	@Id
	@Column(name = "company_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_master_seq")
	private int companyId;
	
	@Column(name = "company_code")
	private String companyCode;
	
	@Column(name = "company_desc")
	private String companyDesc;
	
	@Column(name = "company_type_id")
	private Integer companyTypeId;
	
	@Column(name = "credit_limit")
	private Double creditLimit;
	
	
	@Column(name = "report_path")
	private String reportPath;
	
	@Column(name = "contact_person_name")
	private String contactPersonName;
	
	@Column(name = "credit_days")
	private String creditDays;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "country_id")
	private Integer countryId;
	
	@Column(name = "state_id")
	private Integer stateId;
	
	@Column(name = "city_id")
	private Integer cityId;
	
	@Column(name = "district_id") 
	private Integer  districtId;
	
	@Column(name = "area_id")
	private Integer  areaId;
	
	@Column(name = "zipcode")
	private String zipcode;
	
	@Column(name = "mobile_no")
	private String mobileNo;
	
	@Column(name = "residence_phone_no")
	private String residencePhoneNo;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="fax_no")
	private String faxNo;
	
	@Column(name="email_id")
	private String emailId;
	
	@Column(name="start_date")
	private Date startDate;
	
	@Column(name="end_date")
	private Date endDate;
	
	@Column(name="payment_terms")
	private String paymentTerms;
	
	@Column(name="ar_code_id")
	private Integer arCodeId;
	
	@Column(name="contract_no")
	private String contractNo;
	
	@Column(name="is_trust")
	private String isTrust;
	
    @Column(name="block_report_print")
    private Character blockReportPrint;
	
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
	private CityMaster cityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "area_id", insertable = false, updatable = false)
	private AreaMaster  areaMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ar_code_id", insertable = false, updatable = false)
	private ARCodeMaster arCodeMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "companyMaster")
	private List<DepositMaster> listDepositMaster;
	
	@OneToMany(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "companyMaster")
	private List<MCompanyContractMaster> listMCompanyContractMaster;

	@Column(name = "general_ledger_id")
	private Integer generalLedgerId;
	
	public List<MCompanyContractMaster> getListMCompanyContractMaster() {
		return listMCompanyContractMaster;
	}

	public void setListMCompanyContractMaster(List<MCompanyContractMaster> listMCompanyContractMaster) {
		this.listMCompanyContractMaster = listMCompanyContractMaster;
	}

	public List<DepositMaster> getListDepositMaster() {
		return listDepositMaster;
	}

	public void setListDepositMaster(List<DepositMaster> listDepositMaster) {
		this.listDepositMaster = listDepositMaster;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public Integer getCompanyTypeId() {
		return companyTypeId;
	}

	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}
	
	public String getCreditDays() {
		return creditDays;
	}

	public void setCreditDays(String creditDays) {
		this.creditDays = creditDays;
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

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}


	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getResidencePhoneNo() {
		return residencePhoneNo;
	}

	public void setResidencePhoneNo(String residencePhoneNo) {
		this.residencePhoneNo = residencePhoneNo;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Double getCreditLimit() {
		return creditLimit;
	}

	public void setCreditLimit(Double creditLimit) {
		this.creditLimit = creditLimit;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFaxNo() {
		return faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public Integer getArCodeId() {
		return arCodeId;
	}

	public void setArCodeId(Integer arCodeId) {
		this.arCodeId = arCodeId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public CountryMaster getCountryMaster() {
		return countryMaster;
	}

	public void setCountryMaster(CountryMaster countryMaster) {
		this.countryMaster = countryMaster;
	}

	public StateMaster getStateMaster() {
		return stateMaster;
	}

	public void setStateMaster(StateMaster stateMaster) {
		this.stateMaster = stateMaster;
	}

	public CityMaster getCityMaster() {
		return cityMaster;
	}

	public void setCityMaster(CityMaster cityMaster) {
		this.cityMaster = cityMaster;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getIsTrust() {
		return isTrust;
	}

	public void setIsTrust(String isTrust) {
		this.isTrust = isTrust;
	}

	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}

	public Integer getGeneralLedgerId() {
		return generalLedgerId;
	}

	public void setGeneralLedgerId(Integer generalLedgerId) {
		this.generalLedgerId = generalLedgerId;
	}
	
}
