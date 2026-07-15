package com.param.global.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
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

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	@NamedQuery(name="GET_ACCOUNT_PAY_LIST",
			query="SELECT mstr.accPayableMstId as accPayableMstId, mstr.accCode as accCode ,"
					+ "	mstr.accDesc as accDesc ,mstr.accPayableCode as accPayableCode ,"
					+ " mstr.accDay as accDay , mstr.accEmail as accEmail , mstr.accMobile as accMobile , "
					+ " to_char(mstr.accValidityFromDate,'DD-MM-YYYY') as accValidityFromDate ,  to_char(mstr.accValidityToDate,'DD-MM-YYYY')  as accValidityToDate , mstr.accCreditLimit as accCreditLimit,"
					+ "	to_char(mstr.accWorkFromTime,'HH:mm:ss') as accWorkFromTime ,to_char(mstr.accWorkToTime,'HH:mm:ss') as accWorkToTime,mstr.accPaymentTerms as accPaymentTerms , mstr.accAddress as accAddress,"
					+ " mstr.accCountryId as accCountryId ,mstr.accStateId as accStateId  ,mstr.accCityId as accCityId , mstr.accAreaId as accAreaId ,  mstr.accDistrictId as accDistrictId ,"
					+ "  mstr.accZipcode as accZipcode ,  mstr.accContactNumber as accContactNumber ,  mstr.status as status,"
					+ " countryMaster.countryName as countryName, "
					+ " stateMaster.stateName as stateName, "
					+ " cityMaster.cityName as cityName, "
					+ " districtMaster.districtName as districtName, "
					+ " areaMaster.areaName as areaName  "
					+ " FROM AccountPayableMaster mstr "
					+ " left join mstr.countryMaster countryMaster "
					+ " left join mstr.stateMaster stateMaster "
					+ " left join mstr.cityMaster cityMaster "
					+ " left join mstr.districtMaster districtMaster "
					+ " left join mstr.areaMaster areaMaster "
					+ " WHERE mstr.orgId= :organizationId "
					+ " AND mstr.orgUnitId= :unitId "),
	
	@NamedQuery(name="GET_ACTIVE_ACCOUNT_PAY_LIST",
	query="SELECT mstr.accPayableMstId as accPayableMstId, mstr.accCode as accCode ,"
			+ "	mstr.accDesc as accDesc ,mstr.accPayableCode as accPayableCode ,"
			+ " mstr.accDay as accDay , mstr.accEmail as accEmail , mstr.accMobile as accMobile , "
			+ " to_char(mstr.accValidityFromDate,'DD-MM-YYYY') as accValidityFromDate ,  to_char(mstr.accValidityToDate,'DD-MM-YYYY')  as accValidityToDate , mstr.accCreditLimit as accCreditLimit,"
			+ "	to_char(mstr.accWorkFromTime,'HH:mm:ss') as accWorkFromTime ,to_char(mstr.accWorkToTime,'HH:mm:ss') as accWorkToTime,mstr.accPaymentTerms as accPaymentTerms , mstr.accAddress as accAddress,"
			+ " mstr.accCountryId as accCountryId ,mstr.accStateId as accStateId  ,mstr.accCityId as accCityId , mstr.accAreaId as accAreaId ,  mstr.accDistrictId as accDistrictId ,"
			+ "  mstr.accZipcode as accZipcode ,  mstr.accContactNumber as accContactNumber ,  mstr.status as status "
			+ " FROM AccountPayableMaster mstr "
			+ " WHERE mstr.orgId= :organizationId "
			+ " AND mstr.orgUnitId= :unitId "
			+ " AND mstr.status='A'"),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CODE",
	query="SELECT mstr.accPayableMstId as accPayableMstId, mstr.accCode as accCode "
			+ " FROM AccountPayableMaster mstr "
			+ " WHERE mstr.orgId= :orgId "
			+ " AND mstr.orgUnitId= :unitId "
			+ " AND LOWER(mstr.accCode)= :code "
			+ " AND LOWER(mstr.accPayableCode)= :payCode "),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_CODE_WITH_ID",
	query="SELECT mstr.accPayableMstId as accPayableMstId, mstr.accCode as accCode "
			+ " FROM AccountPayableMaster mstr "
			+ " WHERE mstr.orgId= :orgId "
			+ " AND mstr.orgUnitId= :unitId "
			+ " AND LOWER(mstr.accCode)= :code "
			+ " AND LOWER(mstr.accPayableCode)= :payCode "
			+ " AND mstr.accPayableMstId <> :accPayableMstId")
})
@Entity
@Table(name = "m_account_payable_master", schema = "public")
@SequenceGenerator(name = "m_account_payable_master_seq", sequenceName = "public.m_account_payable_master_seq", allocationSize = 1)
public class AccountPayableMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_account_payable_master_seq")
	@Column(name = "acc_payable_mst_id")
	private int accPayableMstId;

	@Column(name = "acc_code")
	private String accCode;

	@Column(name = "acc_desc")
	private String accDesc;
	
	@Column(name = "acc_payable_code")
	private String accPayableCode;
	
	@Column(name = "acc_day")
	private String accDay;
	
	@Column(name = "acc_email")
	private String accEmail;
	
	@Column(name = "acc_mobile ")
	private String accMobile;
	
	@Column(name = "acc_work_from_time")
	@Convert(converter = InstantConverter.class)
	private Instant accWorkFromTime;
	
	@Column(name = "acc_work_to_time")
	@Convert(converter = InstantConverter.class)
	private Instant accWorkToTime;
	
	@Column(name = "acc_validity_from_date")
	private Date accValidityFromDate;
	
	@Column(name = "acc_validity_to_date")
	private Date accValidityToDate;
	
	@Column(name = "acc_credit_limit")
	private double accCreditLimit;
	
	@Column(name = "acc_payment_terms ")
	private String accPaymentTerms;
	
	@Column(name = "acc_address ")
	private String accAddress;
	
	@Column(name = "acc_country_id ")
	private Integer accCountryId;
	
	@Column(name = "acc_state_id ")
	private Integer accStateId;
	
	@Column(name = "acc_city_id ")
	private Integer accCityId;
	
	@Column(name = "acc_area_id ")
	private Integer accAreaId;
	
	@Column(name = "acc_district_id ")
	private Integer accDistrictId;
	
	@Column(name = "acc_zipcode ")
	private String accZipcode;
	
	@Column(name = "acc_contact_number")
	private String accContactNumber;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="org_unit_id")
	private Integer orgUnitId;
	
	@Column(name="dept_id")
	private Integer deptId;
	
	@Column(name="sub_dept_id")
	private Integer subDeptId;
	
	@Column(name="order_id")
	private Integer orderId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acc_city_id", insertable = false, updatable = false)
	private CityMaster cityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acc_country_id", insertable = false, updatable = false)
	private CountryMaster countryMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acc_state_id", insertable = false, updatable = false)
	private StateMaster stateMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acc_district_id", insertable = false, updatable = false)
	private DistrictMaster districtMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "acc_area_id", insertable = false, updatable = false)
	private AreaMaster areaMaster;

	public int getAccPayableMstId() {
		return accPayableMstId;
	}

	public void setAccPayableMstId(int accPayableMstId) {
		this.accPayableMstId = accPayableMstId;
	}

	public String getAccCode() {
		return accCode;
	}

	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}

	public String getAccDesc() {
		return accDesc;
	}

	public void setAccDesc(String accDesc) {
		this.accDesc = accDesc;
	}

	public String getAccPayableCode() {
		return accPayableCode;
	}

	public void setAccPayableCode(String accPayableCode) {
		this.accPayableCode = accPayableCode;
	}

	public String getAccEmail() {
		return accEmail;
	}

	public void setAccEmail(String accEmail) {
		this.accEmail = accEmail;
	}

	public String getAccMobile() {
		return accMobile;
	}

	public void setAccMobile(String accMobile) {
		this.accMobile = accMobile;
	}


	public Date getAccValidityFromDate() {
		return accValidityFromDate;
	}

	public void setAccValidityFromDate(Date accValidityFromDate) {
		this.accValidityFromDate = accValidityFromDate;
	}

	public Date getAccValidityToDate() {
		return accValidityToDate;
	}

	public void setAccValidityToDate(Date accValidityToDate) {
		this.accValidityToDate = accValidityToDate;
	}

	public double getAccCreditLimit() {
		return accCreditLimit;
	}

	public void setAccCreditLimit(double accCreditLimit) {
		this.accCreditLimit = accCreditLimit;
	}

	public String getAccPaymentTerms() {
		return accPaymentTerms;
	}

	public void setAccPaymentTerms(String accPaymentTerms) {
		this.accPaymentTerms = accPaymentTerms;
	}

	public String getAccAddress() {
		return accAddress;
	}

	public void setAccAddress(String accAddress) {
		this.accAddress = accAddress;
	}

	public String getAccZipcode() {
		return accZipcode;
	}

	public void setAccZipcode(String accZipcode) {
		this.accZipcode = accZipcode;
	}

	
	public Character getStatus() {
		return status!=null?status:'A';
	}

	public void setStatus(Character status) {
		this.status = status!=null?status:'A';
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getAccCountryId() {
		return accCountryId;
	}

	public void setAccCountryId(Integer accCountryId) {
		this.accCountryId = accCountryId;
	}

	public Integer getAccStateId() {
		return accStateId;
	}

	public void setAccStateId(Integer accStateId) {
		this.accStateId = accStateId;
	}

	public Integer getAccCityId() {
		return accCityId;
	}

	public void setAccCityId(Integer accCityId) {
		this.accCityId = accCityId;
	}

	public Integer getAccAreaId() {
		return accAreaId;
	}

	public void setAccAreaId(Integer accAreaId) {
		this.accAreaId = accAreaId;
	}

	public Integer getAccDistrictId() {
		return accDistrictId;
	}

	public void setAccDistrictId(Integer accDistrictId) {
		this.accDistrictId = accDistrictId;
	}

	public String getAccDay() {
		return accDay;
	}

	public void setAccDay(String accDay) {
		this.accDay = accDay;
	}

	public Instant getAccWorkFromTime() {
		return accWorkFromTime;
	}

	public void setAccWorkFromTime(Instant accWorkFromTime) {
		this.accWorkFromTime = accWorkFromTime;
	}

	public Instant getAccWorkToTime() {
		return accWorkToTime;
	}

	public void setAccWorkToTime(Instant accWorkToTime) {
		this.accWorkToTime = accWorkToTime;
	}

	public String getAccContactNumber() {
		return accContactNumber;
	}

	public void setAccContactNumber(String accContactNumber) {
		this.accContactNumber = accContactNumber;
	}
	
	
	
	
	
}
