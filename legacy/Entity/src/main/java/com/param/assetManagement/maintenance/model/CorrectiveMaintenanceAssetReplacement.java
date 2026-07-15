package com.param.assetManagement.maintenance.model;

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

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;

@NamedQueries({
	@NamedQuery(name="GET_REPLACE_ITEM_LIST_BY_SCHEDULE_ID",
		query="SELECT replace.assetReplacementId as assetReplacementId , " 
				+ "replace.correctiveMaintenanceSchedulingId as correctiveMaintenanceSchedulingId, "
				+ "replace.itemId as itemId, "
				+ "replace.requestedSerialNoId as requestedSerialNoId, "
				+ "replace.requestedStoreId as requestedStoreId, "
				+ "replace.newSerialNoId as newSerialNoId, "
				+ "replace.newStoreId as newStoreId, "
				+ "replace.createdBy as createdBy, "
				+ "replace.status as status, "
				+ "replace.updatedBy as updatedBy, "
				+ " to_char(replace.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate, "
				+ " to_char(replace.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate, "
				+ "item.name as itemName, "
				+ "item.code as itemCode, "
				+ "replace.unitId as unitId, "
				+ " newBatch.batchNo as newSerialNo , "
				+ " requestedBatch.batchNo as requestedSerialNo , "
				+ "replace.organizationId as organizationId "
				+" FROM CorrectiveMaintenanceAssetReplacement replace " 
				+" INNER JOIN replace.item item "
				+" INNER JOIN replace.requestedBatch requestedBatch "
				+" INNER JOIN replace.newBatch newBatch "
				+" where replace.correctiveMaintenanceSchedulingId = :correctiveMaintenanceScheduleId  "
				+" AND replace.status = 'A'"
				+" AND replace.unitId = :unitId"
				+" AND replace.organizationId = :organizationId " 
			),
	
	
	@NamedQuery(name="GET_REPLACE_ITEM_LIST",
	query="SELECT replace.assetReplacementId as assetReplacementId , " 
			+ "replace.correctiveMaintenanceSchedulingId as correctiveMaintenanceSchedulingId, "
			+ "replace.itemId as itemId, "
			+ "replace.requestedSerialNoId as requestedSerialNoId, "
			+ "replace.requestedStoreId as requestedStoreId, "
			+ "replace.newSerialNoId as newSerialNoId, "
			+ "replace.newStoreId as newStoreId, "
			+ "replace.createdBy as createdBy, "
			+ "replace.status as status, "
			+ "replace.updatedBy as updatedBy, "
			+ " to_char(replace.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate, "
			+ " to_char(replace.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate, "
			+ "item.name as itemName, "
			+ "item.code as itemCode, "
			+ "replace.unitId as unitId, "
			+ " newBatch.batchNo as newSerialNo , "
			+ " requestedBatch.batchNo as requestedSerialNo , "
			+ "replace.organizationId as organizationId ,"
			+ "correctiveMaintenance.callLogNumber as callLogNumber, "
			+ "to_char(correctiveMaintenance.requestDateTime,'dd-MM-yyyy HH:ss:mm') as requestDateTime, "
			+ " to_char(correctiveMaintenance.resolutionExpectedDateTime,'DD/MM/YYYY') as resolutionExpectedDateTime, "
			+ "to_char(cmSche.completionDate,'dd-MM-yyyy HH:ss:mm') as completionDate ,"
			+ " replace.replaceStatusId as replaceStatusId, "
			+ " assetCategory.category as assetName "
			+" FROM CorrectiveMaintenanceAssetReplacement replace " 
			+" INNER JOIN replace.item item "
			+" LEFT JOIN item.productCategory as productCategory "
			+" LEFT JOIN productCategory.assetCategory as assetCategory "
			+" INNER JOIN replace.requestedBatch requestedBatch "
			+" INNER JOIN replace.newBatch newBatch "
			+ "INNER JOIN replace.CM cmSche "
			+ "INNER JOIN cmSche.correctiveMaintenanceWorklist correctiveMaintenanceWorklist "
			+ "INNER JOIN correctiveMaintenanceWorklist.correctiveMaintenance correctiveMaintenance " 
			+" where replace.unitId = :unitId "
			+" AND replace.organizationId = :organizationId " 
		)
	
})
@Entity
@Table(name = "t_corrective_maintenance_asset_replacement", schema = "assets")
@SequenceGenerator(name = "seq_corrective_maintenance_asset_replacement", sequenceName = "assets.seq_corrective_maintenance_asset_replacement", allocationSize = 1)
public class CorrectiveMaintenanceAssetReplacement {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_corrective_maintenance_asset_replacement")
	@Column(name = "asset_replacement_id")
	private Integer assetReplacementId;
	
	@Column(name = "corrective_maintenance_id")
	private Integer correctiveMaintenanceId;
	
	@Column(name = "corrective_maintenance_scheduling_id")
	private Integer correctiveMaintenanceSchedulingId;
	
	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "requested_store_id")
	private Integer requestedStoreId;
	
	@Column(name = "requested_serial_no_id")
	private Integer requestedSerialNoId;
	
	@Column(name = "new_serial_no_id")
	private Integer newSerialNoId;
	
	@Column(name = "new_store_id")
	private Integer newStoreId;
	
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
	
	@Column(name = "org_unit_id")
	private Integer unitId ;

	@Column(name = "org_id")
	private Integer organizationId;
	
	@Column(name = "reason_note")
	private String reasonNote;
	
	@Column(name = "reason_id")
	private Integer reasonId ;

	@Column(name = "replace_status_id")
	private Integer replaceStatusId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", insertable = false, updatable = false)
	private Item item;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requested_serial_no_id", insertable = false, updatable = false)
	private Batch requestedBatch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "new_serial_no_id", insertable = false, updatable = false)
	private Batch newBatch;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "corrective_maintenance_scheduling_id", insertable = false, updatable = false)
	private CorrectiveMaintenanceSchedule CM;
	
	public Integer getAssetReplacementId() {
		return assetReplacementId;
	}

	public void setAssetReplacementId(Integer assetReplacementId) {
		this.assetReplacementId = assetReplacementId;
	}

	public Integer getCorrectiveMaintenanceId() {
		return correctiveMaintenanceId;
	}

	public void setCorrectiveMaintenanceId(Integer correctiveMaintenanceId) {
		this.correctiveMaintenanceId = correctiveMaintenanceId;
	}

	public Integer getCorrectiveMaintenanceSchedulingId() {
		return correctiveMaintenanceSchedulingId;
	}

	public void setCorrectiveMaintenanceSchedulingId(Integer correctiveMaintenanceSchedulingId) {
		this.correctiveMaintenanceSchedulingId = correctiveMaintenanceSchedulingId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getRequestedStoreId() {
		return requestedStoreId;
	}

	public void setRequestedStoreId(Integer requestedStoreId) {
		this.requestedStoreId = requestedStoreId;
	}

	public Integer getRequestedSerialNoId() {
		return requestedSerialNoId;
	}

	public void setRequestedSerialNoId(Integer requestedSerialNoId) {
		this.requestedSerialNoId = requestedSerialNoId;
	}

	public Integer getNewSerialNoId() {
		return newSerialNoId;
	}

	public void setNewSerialNoId(Integer newSerialNoId) {
		this.newSerialNoId = newSerialNoId;
	}

	public Integer getNewStoreId() {
		return newStoreId;
	}

	public void setNewStoreId(Integer newStoreId) {
		this.newStoreId = newStoreId;
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

	public String getReasonNote() {
		return reasonNote;
	}

	public void setReasonNote(String reasonNote) {
		this.reasonNote = reasonNote;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Integer getReplaceStatusId() {
		return replaceStatusId;
	}

	public void setReplaceStatusId(Integer replaceStatusId) {
		this.replaceStatusId = replaceStatusId;
	}

}
