package com.param.billing.global.transaction.model;

import java.util.Date;


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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.model.AreaMaster;
import com.param.global.model.CityMaster;
import com.param.global.model.CountryMaster;
import com.param.global.model.DistrictMaster;
import com.param.global.model.StateMaster;
@NamedQueries({
				@NamedQuery(name="GET_INSURANCE_MASTER_BY_ID",
						query="SELECT"
								+ " insuranceMaster.insuranceId AS insuranceId, "
								+ " insuranceMaster.orgId AS orgId, "
								+ " insuranceMaster.unitId AS unitId, "
								+ " insuranceMaster.insuranceCode AS insuranceCode, "
								+ " insuranceMaster.insuranceDesc AS insuranceDesc, "
								+ " to_char(insuranceMaster.startDate,'MM/dd/yyyy') AS startDate, "
								+ " to_char(insuranceMaster.endDate,'MM/dd/yyyy') AS endDate, "
								+ " insuranceMaster.address AS address, "
								+ " insuranceMaster.countryId AS countryId, "
								+ " insuranceMaster.stateId AS stateId, "
								+ " insuranceMaster.districtId AS districtId, "
								+ " insuranceMaster.cityId AS cityId, "
								+ " insuranceMaster.areaId AS areaId, "
								+ " insuranceMaster.zipCode AS zipCode, "
								+ " insuranceMaster.contactNo AS contactNo, "
								+ " insuranceMaster.faxNo AS faxNo, "
								+ " insuranceMaster.email AS email, "
								+ " insuranceMaster.contactPerson AS contactPerson, "
								+ " insuranceMaster.status AS status,"
								+ " countryMaster.countryName AS countryName,"
								+ " stateMaster.stateName AS stateName, "
								+ " districtMaster.districtName AS districtName, "
								+ " cityMaster.cityName AS cityName, "
								+ " areaMaster.areaName AS areaName,"
								+ " insuranceMaster.blockReportPrint AS blockReportPrint,"
								+ " insuranceMaster.reportPath AS reportPath,  "
								+ " insuranceMaster.generalLedgerId AS generalLedgerId "
								+ " FROM InsuranceMaster insuranceMaster"
								+ " INNER JOIN insuranceMaster.countryMaster countryMaster"
								+ " INNER JOIN insuranceMaster.stateMaster stateMaster"
								+ " INNER JOIN insuranceMaster.districtMaster districtMaster"
								+ " INNER JOIN insuranceMaster.cityMaster cityMaster"
								+ " INNER JOIN insuranceMaster.areaMaster areaMaster"
								+ " WHERE insuranceMaster.insuranceId= :insuranceId"
								+ " AND insuranceMaster.orgId= :orgId"
								+ " AND insuranceMaster.unitId= :unitId"
								),
				
				@NamedQuery(name="GET_INSURANCE_MASTER_LIST",
						query="SELECT"
								+ " insuranceMaster.insuranceId AS insuranceId, "
								+ " insuranceMaster.orgId AS orgId, "
								+ " insuranceMaster.unitId AS unitId, "
								+ " insuranceMaster.insuranceCode AS insuranceCode, "
								+ " insuranceMaster.insuranceDesc AS insuranceDesc, "
								+ " to_char(insuranceMaster.startDate,'dd/MM/yyyy') AS startDate, "
								+ " to_char(insuranceMaster.endDate,'dd/MM/yyyy') AS endDate, "
								+ " insuranceMaster.address AS address, "
								+ " insuranceMaster.countryId AS countryId, "
								+ " insuranceMaster.stateId AS stateId, "
								+ " insuranceMaster.districtId AS districtId, "
								+ " insuranceMaster.cityId AS cityId, "
								+ " insuranceMaster.areaId AS areaId, "
								+ " insuranceMaster.zipCode AS zipCode, "
								+ " insuranceMaster.contactNo AS contactNo, "
								+ " insuranceMaster.faxNo AS faxNo, "
								+ " insuranceMaster.email AS email, "
								+ " insuranceMaster.contactPerson AS contactPerson, "
								+ " insuranceMaster.status AS status, "
								+ " countryMaster.countryName AS countryName,"
								+ " stateMaster.stateName AS stateName, "
								+ " districtMaster.districtName AS districtName, "
								+ " cityMaster.cityName AS cityName, "
								+ " areaMaster.areaName AS areaName,"
								+ " insuranceMaster.reportPath AS reportPath, "
								+ " insuranceMaster.generalLedgerId AS generalLedgerId "
								+ " FROM InsuranceMaster insuranceMaster"
								+ " INNER JOIN insuranceMaster.countryMaster countryMaster"
								+ " INNER JOIN insuranceMaster.stateMaster stateMaster"
								+ " INNER JOIN insuranceMaster.districtMaster districtMaster"
								+ " INNER JOIN insuranceMaster.cityMaster cityMaster"
								+ " INNER JOIN insuranceMaster.areaMaster areaMaster"
								+ " WHERE insuranceMaster.orgId= :orgId"
								+ " AND insuranceMaster.unitId= :unitId "
								+ " order by insuranceMaster.insuranceId desc "
								),
						
				@NamedQuery(name="GET_INSURANCE_MASTER_BY_NAME",
						query="SELECT"
								+ " insuranceMaster.insuranceId AS insuranceId, "
								+ " insuranceMaster.insuranceDesc AS insuranceDesc "
								+ " FROM InsuranceMaster insuranceMaster"
								+ " WHERE LOWER(insuranceMaster.insuranceDesc)= :insuranceDesc "
								+ " AND insuranceMaster.orgId= :orgId"
								+ " AND insuranceMaster.unitId= :unitId"
								),
						
				@NamedQuery(name="GET_INSURANCE_MASTER_BY_NAME_NOT_NAME",
						query="SELECT"
								+ " insuranceMaster.insuranceId AS insuranceId, "
								+ " insuranceMaster.insuranceDesc AS insuranceDesc "
								+ " FROM InsuranceMaster insuranceMaster"
								+ " WHERE insuranceMaster.insuranceId != :insuranceId"
								+ " AND LOWER(insuranceMaster.insuranceDesc)= :insuranceDesc "
								+ " AND insuranceMaster.orgId= :orgId"
								+ " AND insuranceMaster.unitId= :unitId"
							
								),
				
				@NamedQuery(name="GET_ACTIVE_INSURANCE_MASTER_LIST",
				query="SELECT"
						+ " insuranceMaster.insuranceId AS insuranceId, "
						+ " insuranceMaster.unitId AS unitId,"
						+ " insuranceMaster.status AS status "
						+ " FROM InsuranceMaster insuranceMaster"
						+ " WHERE insuranceMaster.unitId= :unitId"
						+ " AND insuranceMaster.status= 'A'"
					
						)
						
						
				
})
@NamedNativeQuery(name="GET_MAPPED_GRADE_TO_INSUERANCE",
			query="SELECT "
					+ " cmpMstr.company_desc AS \"companyDesc\" ,grdeMstr.grade_desc AS \"gradeDesc\",grdeMstr.code AS \"insuranceCode\"  "
				+" FROM "
				+ "		service.t_company_contarct_grade_mpper mpper" 
				+" INNER JOIN "
				+ "		public.t_company_master cmpMstr on cmpMstr.company_id=mpper.company_id "
				+" INNER JOIN "
				+ "		service.m_contract_grade_master grdeMstr on grdeMstr.contract_grade_master_id=mpper.contract_grade_master_id "
				+" INNER JOIN "
				+ "		service.t_insurance_tpa_mapper ins_tpa_mpper on cmpMstr.company_id=ins_tpa_mpper.company_id "
				+" WHERE "
				+ "		ins_tpa_mpper.insurance_id=:insuranceId "
				+ "		AND mpper.status='A' "
				+ " 	AND ins_tpa_mpper.status='A'")

@Entity
@Table(name = "t_insurance", schema = "adt")
@SequenceGenerator(name = "insurance_seq", sequenceName = "adt.insurance_seq", allocationSize = 1)
public class InsuranceMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "insurance_seq")
	@Column(name = "insurance_id")
	private Integer insuranceId;
	
	@Column(name = "organization_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "insurance_code")
	private String insuranceCode;
	
	@Column(name = "insurance_desc")
	private String insuranceDesc;
	
	@Column(name = "start_date")
	private Date startDate;
	
	@Column(name = "end_date")
	private Date endDate;
	
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
	
	@Column(name = "contact_no")
	private String contactNo;
	
	@Column(name = "fax_no")
	private String faxNo;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "contact_person")
	private String contactPerson;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "report_path")
	private String reportPath;
	
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
	private AreaMaster areaMaster;
	
	@Column(name = "general_ledger_id")
	private Integer generalLedgerId;
	
	public Integer getInsuranceId() {
		return insuranceId;
	}
	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getInsuranceCode() {
		return insuranceCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public String getInsuranceDesc() {
		return insuranceDesc;
	}
	public void setInsuranceDesc(String insuranceDesc) {
		this.insuranceDesc = insuranceDesc;
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
	public String getContactNo() {
		return contactNo;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public String getFaxNo() {
		return faxNo;
	}
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
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
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
	public DistrictMaster getDistrictMaster() {
		return districtMaster;
	}
	public void setDistrictMaster(DistrictMaster districtMaster) {
		this.districtMaster = districtMaster;
	}
	public CityMaster getCityMaster() {
		return cityMaster;
	}
	public void setCityMaster(CityMaster cityMaster) {
		this.cityMaster = cityMaster;
	}
	public AreaMaster getAreaMaster() {
		return areaMaster;
	}
	public void setAreaMaster(AreaMaster areaMaster) {
		this.areaMaster = areaMaster;
	}
	public String getReportPath() {
		return reportPath;
	}
	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
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
