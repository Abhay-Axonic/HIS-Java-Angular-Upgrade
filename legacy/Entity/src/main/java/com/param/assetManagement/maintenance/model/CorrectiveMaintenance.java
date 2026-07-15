package com.param.assetManagement.maintenance.model;


import java.math.BigInteger;
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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Item;
import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	
	@NamedQuery(name="GET_CORRECTIVE_MAINTENANCE_LIST",
		query="SELECT mstr.correctiveMaintenanceId as correctiveMaintenanceId ,"
					+ " mstr.callLogNumber as callLogNumber , "
					+ " to_char(mstr.requestDateTime,'DD/MM/YYYY HH24:MI') as requestDateTime, "
					+ " to_char(mstr.downDateTime,'DD/MM/YYYY') as downDateTime, "
					+ " to_char(mstr.resolutionExpectedDateTime,'DD/MM/YYYY') as resolutionExpectedDateTime, "
					+ " to_char(mstr.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate, "
					+ " to_char(mstr.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate, "
					+ " mstr.updatedBy as updatedBy , "
					+ " mstr.createdBy as createdBy , "
					+ " mstr.status as status , "
					+ " mstr.itemId as itemId , "
					+ " mstr.batchId as batchId , "
					+ " mstr.isComplete as isComplete , "
					+ " mstr.completionRemak as completionRemak , "
					+ " to_char(mstr.completionTime,'hh24:mi') as completionTime, "
					+ " to_char(mstr.completionDate,'DD/MM/YYYY') as completionDate, "
					+ " mstr.unitId as unitId , "
					+ " mstr.organizationId as organizationId , "
					+ " to_char(mstr.resolutionExpectedTime,'hh24:mi') as resolutionExpectedTime, "
					+ " to_char(mstr.downTime,'hh24:mi') as downTime, "
					+ " mstr.requestTypeId as requestTypeId , "
					+ " mstr.serialNumber as serialNumber , "
					+ " mstr.isDown as isDown, "
					+ " mstr.requestDetails as requestDetails , "
					+ " mstr.remark as remark , "
					+ " assetCategory.category as assetcategory, "
					+ " mstr.requestCriticalityId as requestCriticalityId , "
					+ " mstr.storeId as storeId , "
					+ " mstr.maintenanceStatusId as maintenanceStatusId , "
					+ " concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as userName, "
					+ " itemMaster.name as assetName, "
					+ " mstr.referenceId as referenceId, "
					+ " mstr.referenceLogNumber as referenceLogNumber, "
					+ " mstr.reOpen as reOpen, "
					+ " itemMaster.code as assetCode  "
					+ " FROM CorrectiveMaintenance mstr "
					+ " INNER JOIN mstr.item itemMaster "
					+ " INNER JOIN itemMaster.productCategory  productCategory "
					+ " INNER JOIN productCategory.assetCategory  assetCategory "
					+ " INNER JOIN mstr.userMaster userMsr "
					+ " LEFT JOIN userMsr.employeeMastersList employee "
					+ " LEFT JOIN employee.prefixMaster prefix "
					+ " WHERE mstr.organizationId= :organizationId "
					+ " AND mstr.unitId= :unitId "
					),
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_ITEM_REQUEST",
	query="SELECT mstr.correctiveMaintenanceId as correctiveMaintenanceId "
				+ " FROM CorrectiveMaintenance mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.batchId= :batchId "
				+ " AND mstr.itemId= :itemId "
				+ " AND mstr.maintenanceStatusId !=2 " //2 for completed
				),
	@NamedQuery(name="TO_CHECK_EQUIPMENT_MAINTENANCE_STATUS",
	query="SELECT mstr.correctiveMaintenanceId as correctiveMaintenanceId "
				+ " FROM CorrectiveMaintenance mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.batchId= :batchId "
				+ " AND mstr.itemId= :itemId "
		//		+ " AND to_char(mstr.requestDateTime,'yyyy-mm-dd') = :equDate "
				+ " AND  mstr.maintenanceStatusId not in(2,6) "
				),
	
	@NamedQuery(name="GET_MANUFACTURER_DETAILS_BY_ITEM_ID",
	query="SELECT distinct manufacturer.name as name,"
				+ " manufacturer.address as address "
				+ " FROM Item itm "
				+ " INNER JOIN itm.manufacturer manufacturer "
				+ " WHERE itm.id= :itemId "
				),
	
	@NamedQuery(name="GET_VENDOR_LIST_BY_ITEM_ID",
	query="SELECT distinct vendors.name as name,"
				+ " vendors.emailId as emailId,"
				+ " country.countryName as country, "
				+ " state.stateName as state, "
				+ " city.cityName as city, "
				+ " area.areaName as area, "
				+ " vendors.phoneNumber as mobileNumber, "
				+ " vendors.contactPersonName as contactPersonName,"
				+ " vendors.phoneNumber as phoneNumber, "
				+ " vendors.address as address, "
				+ " vendors.zipcode as zipcode "
				+ " FROM OpeningBalanceDetail opeBal "
				+ " INNER JOIN opeBal.vendor vendors "
				+ " INNER JOIN vendors.country country "
				+ " INNER JOIN vendors.state state "
				+ " INNER JOIN vendors.city city "
				+ " INNER JOIN vendors.area area "
				+ " INNER JOIN opeBal.item itm "
				+ " INNER JOIN itm.batchList batch "
				+ " WHERE  opeBal.item.id= :itemId and batch.id = :batchId "
				),
	
	@NamedQuery(name="GET_ACCESSORY_DETAILS_BY_ITEM_ID",
	query="SELECT distinct item.name as assetName,"
				+ " item.code as assetCode, "
				+ " opeBalAcc.serialNumber as serialNumber, "
				+ " opeBalAcc.quantity as quantity "
				+ " FROM OpeningBalanceAccessories opeBalAcc "
				+ " INNER JOIN opeBalAcc.openingBalanceDetail openingBalanceDetail "
				+ " INNER JOIN openingBalanceDetail.item item "
				+ " INNER JOIN item.batchList batch "
				+ " WHERE openingBalanceDetail.item.id= :itemId and batch.id = :batchId "
				)
	
})
@NamedNativeQueries({
	@NamedNativeQuery(
		name = "GET_INSURANCE_DETAILS_BY_ITEM_ID",
				query ="select distinct insurance.policy_number as \"policyNumber\", " 
						+" company.company_desc as \"insuranceName\", " 
						+" company.mobile_no as \"contactNo\", " 
						+" company.email_id as \"email\", " 
						+" company.contact_person_name as \"contactPersonName\", " 
						+" to_char(insurance.policy_from_date,'yyyy-mm-dd') as \"policyFromDate\", " 
						+" to_char(insurance.policy_to_date ,'yyyy-mm-dd') as \"policyToDate\", " 
						+" insurance.amount_insured as \"amountInsured\", "  
						+" company.address as \"address\", " 
						+" area.area_name as \"area\", " 
						+" country.country_name as \"country\"," 
						+" state.state_name as \"state\", " 
						+" city.city_name as \"city\", "
						+" company.zipcode as \"zipCode\" "
						+" from inventory.t_opening_balance_insurance insurance " 
						+" inner join inventory.t_opening_balance_detail opebalDat on  insurance.opening_balance_detail_id = opebalDat.id " 
						+" inner join  public.t_company_master company on insurance.insurance_company_id = company.company_id " 
						+" inner join public.m_country_master country on company.country_id = country.country_id " 
						+" inner join public.m_state_master state on company.state_id = state.state_id " 
						+" inner join public.m_city_master city on company.city_id = city.city_id " 
						+" inner join public.m_area_master area on company.area_id = area.area_id "
						+ " inner join public.m_item itm on itm.id = opebalDat.item_id "
						+ " inner join inventory.t_batch batch on itm.id = batch.item_id "
						+" where opebalDat.item_id = :itemId and batch.id = :batchId "),
})


@Entity
@Table(name = "t_corrective_maintenance", schema = "assets")
@SequenceGenerator(name = "seq_corrective_maintenance", sequenceName = "assets.seq_corrective_maintenance", allocationSize = 1)
public class CorrectiveMaintenance {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_corrective_maintenance")
	
	@Column(name = "corrective_maintenance_id")
	private Integer correctiveMaintenanceId;
	
	@Column(name = "call_log_number")
	private String callLogNumber;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "request_type_id")
	private Integer requestTypeId;
	
	@Column(name = "request_criticality_id")
	private Integer requestCriticalityId;
	
	@Column(name = "serial_number")
	private String serialNumber;
	
	@Column(name = "is_down")
	private boolean isDown;
	
	@Column(name = "request_date_time")
	private Date requestDateTime;
	
	@Column(name = "down_date_time")
	private Date downDateTime;
	
	@Column(name = "resolution_expected_date_time")
	private Date resolutionExpectedDateTime;
	
	@Column(name = "request_details")
	private String requestDetails;
	
	@Column(name = "maintenance_status_id")
	private Integer maintenanceStatusId;
	
	@Column(name = "remark")
	private String remark;
	
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
	
	@Column(name = "org_unit_id ")
	private Integer unitId ;

	@Column(name = "org_id")
	private Integer organizationId;
	
	@Column(name = "exported_to_worklist")
	private boolean exportedToWorklist;
	
	@Column(name = "completion_date")
	private Date completionDate ;
	
	@Column(name = "completion_remak")
	private String completionRemak ;
	
	@Column(name = "is_complete")
	private boolean isComplete;
	
	@Column(name = "re_open")
	private boolean reOpen;
	
	
	@Column(name = "reference_id")
	private Integer referenceId ;
	
	@Column(name = "batch_id")
	private BigInteger batchId ;
	
	@Column(name = "reference_log_number")
	private String referenceLogNumber ;
	
	@Column(name="completion_time")
	@Convert(converter = InstantConverter.class)
	private Instant completionTime;
	
	@Column(name="resolution_expected_time")
	@Convert(converter = InstantConverter.class)
	private Instant resolutionExpectedTime;
	
	@Column(name="down_time")
	@Convert(converter = InstantConverter.class)
	private Instant downTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	private UserMaster userMaster;

	public Integer getCorrectiveMaintenanceId() {
		return correctiveMaintenanceId;
	}

	public void setCorrectiveMaintenanceId(Integer correctiveMaintenanceId) {
		this.correctiveMaintenanceId = correctiveMaintenanceId;
	}

	public String getCallLogNumber() {
		return callLogNumber;
	}

	public void setCallLogNumber(String callLogNumber) {
		this.callLogNumber = callLogNumber;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(Integer requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public Integer getRequestCriticalityId() {
		return requestCriticalityId;
	}

	public void setRequestCriticalityId(Integer requestCriticalityId) {
		this.requestCriticalityId = requestCriticalityId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public boolean getIsDown() {
		return isDown;
	}

	public void setIsDown(boolean isDown) {
		this.isDown = isDown;
	}

	public Date getRequestDateTime() {
		return requestDateTime;
	}

	public void setRequestDateTime(Date requestDateTime) {
		this.requestDateTime = requestDateTime;
	}

	public Date getDownDateTime() {
		return downDateTime;
	}

	public void setDownDateTime(Date downDateTime) {
		this.downDateTime = downDateTime;
	}

	public Date getResolutionExpectedDateTime() {
		return resolutionExpectedDateTime;
	}

	public void setResolutionExpectedDateTime(Date resolutionExpectedDateTime) {
		this.resolutionExpectedDateTime = resolutionExpectedDateTime;
	}

	public String getRequestDetails() {
		return requestDetails;
	}

	public void setRequestDetails(String requestDetails) {
		this.requestDetails = requestDetails;
	}

	public Integer getMaintenanceStatusId() {
		return maintenanceStatusId;
	}

	public void setMaintenanceStatusId(Integer maintenanceStatusId) {
		this.maintenanceStatusId = maintenanceStatusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Instant getResolutionExpectedTime() {
		return resolutionExpectedTime;
	}

	public void setResolutionExpectedTime(Instant resolutionExpectedTime) {
		this.resolutionExpectedTime = resolutionExpectedTime;
	}

	public Instant getDownTime() {
		return downTime;
	}

	public void setDownTime(Instant downTime) {
		this.downTime = downTime;
	}

	public boolean isExportedToWorklist() {
		return exportedToWorklist;
	}

	public void setExportedToWorklist(boolean exportedToWorklist) {
		this.exportedToWorklist = exportedToWorklist;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getCompletionRemak() {
		return completionRemak;
	}

	public void setCompletionRemak(String completionRemak) {
		this.completionRemak = completionRemak;
	}

	public boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public Instant getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(Instant completionTime) {
		this.completionTime = completionTime;
	}

	public BigInteger getBatchId() {
		return batchId;
	}

	public void setBatchId(BigInteger batchId) {
		this.batchId = batchId;
	}

	public String getReferenceLogNumber() {
		return referenceLogNumber;
	}

	public void setReferenceLogNumber(String referenceLogNumber) {
		this.referenceLogNumber = referenceLogNumber;
	}

	public boolean isReOpen() {
		return reOpen;
	}

	public void setReOpen(boolean reOpen) {
		this.reOpen = reOpen;
	}

	
}
