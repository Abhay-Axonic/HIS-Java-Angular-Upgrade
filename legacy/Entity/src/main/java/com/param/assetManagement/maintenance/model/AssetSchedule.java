package com.param.assetManagement.maintenance.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	/*@NamedQuery(name="GET_PREVENTIVE_MAINTENANCE_LIST",
		query="select distinct ON(item.id)item.id as assetNameId, assc.id as assetCategoryId,assc.category as assetCategory," +
				" pc.id as productCategoryId,pc.category as productCategory, assc.code as assetCode, item.name as assetName," +
				" batch.batch_no as serialNo, vndr.name as vendor from public.m_item item " + 
				" inner join inventory.t_batch batch on item.id = batch.item_id " + 
				" inner join public.m_product_category  pc on item.product_category_id = pc.id " + 
				" inner join public.m_asset_category assc on pc.asset_category_id = assc.id  " + 
				" inner join public.m_asset_type asst on asst.id = assc.asset_type_id " + 
				" inner join public.m_vendor vndr on vndr.id = batch.vendor_id " + 
				" where item.is_active=true and item.unit_id=:unitId and item.organization_id =:organizationId")*/
 	
	
	@NamedQuery(name = "GET_ASSET_SCHEDULED_LIST", 
			query = "SELECT asset.assetScheduleId as assetScheduleId, asset.itemNameId as itemNameId, "
			+ "asset.itemName as itemName, asset.itemCode as itemCode, asset.nextDate as nextDate, "
			+ "asset.serialNo as serialNo, asset.pmsType as pmsType, asset.vendor as vendor, "
			+ "asset.pmsFrequency as pmsFrequency, asset.remark as remark, asset.processStatus as processStatus,  "
			+ "asset.status as status FROM AssetSchedule as asset "
			+ "WHERE asset.itemNameId=:itemNameId AND asset.serialNo=:serialNo "
			+ "AND asset.unitId=:unitId AND asset.organizationId=:organizationId "
			+ "ORDER BY asset.assetScheduleId ASC "),
	
	@NamedQuery(name = "GET_SCHEDULE_AND_COMPLETION", 
			query = "SELECT asset.scheduledDate as scheduledDate, asset.scheduledTime as scheduledTime, "
			+ "asset.scheduledRemark as scheduledRemark, asset.completionDate as completionDate, asset.completionTime as completionTime, "
			+ "asset.completionRemark as completionRemark, asset.assignOn as assignOn, asset.assignTo as assignTo, "
			+ "asset.assignTime as assignTime, asset.assignRemark as assignRemark FROM AssetSchedule as asset "
			+ "WHERE asset.assetScheduleId=:assetScheduleId AND asset.unitId=:unitId AND asset.organizationId=:organizationId"),
	
//	@NamedQuery(name = "GET_ALL_ASSET_SCHEDULED_LIST", 
//			query = "SELECT asset.assetScheduleId as assetScheduleId, asset.itemNameId as itemNameId, "
//			+ "asset.itemName as itemName, asset.itemCode as itemCode, asset.nextDate as nextDate, "
//			+ "asset.serialNo as serialNo, asset.pmsType as pmsType, asset.vendor as vendor, asset.vendorId as vendorId, "
//			+ "asset.pmsFrequency as pmsFrequency, asset.remark as remark, asset.processStatus as processStatus,  "
//			+ "asset.scheduledDate as scheduledDate, asset.scheduledTime as scheduledTime, asset.scheduledRemark as scheduledRemark, "
//			+ "asset.completionDate as completionDate, asset.completionTime as completionTime,asset.completionRemark as completionRemark, "
//			+ "asset.status as status FROM AssetSchedule as asset "
//			+ "WHERE asset.unitId=:unitId AND asset.organizationId=:organizationId "
//			+ "ORDER BY asset.assetScheduleId ASC "),
	

})

@Entity
@Table(name = "m_asset_schedule", schema = "equipment")
@SequenceGenerator(name = "seq_m_asset_schedule", sequenceName = "equipment.seq_m_asset_schedule", allocationSize = 1)
public class AssetSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_asset_schedule")
	
	@Column(name = "asset_schedule_id")
	private Integer assetScheduleId;
	
	@Column(name = "item_name_id")
	private Integer itemNameId;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "item_code")
	private String itemCode;
	
	@Column(name = "next_date")
	private Date nextDate;
	
	@Column(name = "serial_no")
	private String serialNo;

	@Column(name = "pms_type")
	private String pmsType;
	
	@Column(name = "vendor")
	private String vendor;
	
	@Column(name = "freq")
	private String pmsFrequency;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "manufacturer_id")
	private Integer manufacturerId;
	
	@Column(name = "vendor_id")
	private Integer vendorId;
	
	@Column(name = "scheduled_date")
	private Date scheduledDate;
	
	@Column(name = "scheduled_time")
	private String scheduledTime;
	
	@Column(name = "scheduled_remark")
	private String scheduledRemark;
	
	@Column(name = "completion_date")
	private Date completionDate;
	
	@Column(name = "completion_time")
	private String completionTime;
	
	@Column(name = "completion_remark")
	private String completionRemark;
	
	@Column(name = "process_status")
	private Integer processStatus;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="assign_to")
	private Integer assignTo;
	
	@Column(name="assign_on")
	private Date assignOn;
	
	@Column(name="assign_time")
	private String assignTime;
	
	@Column(name="assign_remark")
	private String assignRemark;
	
	public Integer getAssetScheduleId() {
		return assetScheduleId;
	}

	public void setAssetScheduleId(Integer assetScheduleId) {
		this.assetScheduleId = assetScheduleId;
	}

	public Integer getItemNameId() {
		return itemNameId;
	}

	public void setItemNameId(Integer itemNameId) {
		this.itemNameId = itemNameId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Date getNextDate() {
		return nextDate;
	}

	public void setNextDate(Date nextDate) {
		this.nextDate = nextDate;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getPmsType() {
		return pmsType;
	}

	public void setPmsType(String pmsType) {
		this.pmsType = pmsType;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getPmsFrequency() {
		return pmsFrequency;
	}

	public void setPmsFrequency(String pmsFrequency) {
		this.pmsFrequency = pmsFrequency;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getScheduledRemark() {
		return scheduledRemark;
	}

	public void setScheduledRemark(String scheduledRemark) {
		this.scheduledRemark = scheduledRemark;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public String getCompletionRemark() {
		return completionRemark;
	}

	public void setCompletionRemark(String completionRemark) {
		this.completionRemark = completionRemark;
	}

	public Integer getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Integer assignTo) {
		this.assignTo = assignTo;
	}

	public Date getAssignOn() {
		return assignOn;
	}

	public void setAssignOn(Date assignOn) {
		this.assignOn = assignOn;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public String getAssignRemark() {
		return assignRemark;
	}

	public void setAssignRemark(String assignRemark) {
		this.assignRemark = assignRemark;
	}
	
	
}
