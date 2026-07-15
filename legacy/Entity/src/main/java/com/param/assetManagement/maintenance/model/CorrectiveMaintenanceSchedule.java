package com.param.assetManagement.maintenance.model;

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

import com.param.entity.model.master.Vendor;
import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({
	
	@NamedQuery(name="GET_CORRECTIVE_MAINTENANCE_SCHEDULING_LIST",
		query="SELECT mstr.correctiveMaintenanceScheduleId as correctiveMaintenanceScheduleId ,"
					+ " mstr.correctiveMaintenanceWorklistId as correctiveMaintenanceWorklistId , "
					+ " mstr.requestAllocationTypeId as requestAllocationTypeId, "
					+ " mstr.ticketNo as ticketNo, "
					+ " mstr.vendorId as vendorId, "
					+ " mstr.resolutionDetails as resolutionDetails, "
					+ " mstr.isCompleted as isCompleted, "
					+ " to_char(mstr.assetUpDate,'DD/MM/YYYY') as assetUpDate, "
					+ " to_char(mstr.assetUpTime,'hh24:mi') as assetUpTime, "
					+ " mstr.underObservation as underObservation, "
					+ " to_char(mstr.fromDate,'DD/MM/YYYY') as fromDate, "
					+ " to_char(mstr.toDate,'DD/MM/YYYY') as toDate, "
					+ " mstr.isStart as isStart, "
					+ " mstr.maintenanceScheduleStatusId as maintenanceScheduleStatusId, "
					+ " to_char(mstr.completionDate,'DD/MM/YYYY') as completionDate, "
					+ " mstr.remark as reSolutionRemark, "
					+ " mstr.createdBy as createdBy, "
					+ " mstr.status as status, "
					+ " mstr.updatedBy as updatedBy, "
					+ " to_char(mstr.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate, "
					+ " to_char(mstr.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate, "
					+ " mstr.unitId as unitId, "
					+ " mstr.organizationId as organizationId, "
					+ " correctiveMaintenanceWorklist.allocationRemark  as allocationRemark, "
					+ " correctiveMaintenanceWorklist.assignTo  as assignTo, "
					+ " to_char(correctiveMaintenanceWorklist.scheduleDate,'DD/MM/YYYY') as scheduleDate, "
					+ " to_char(correctiveMaintenanceWorklist.scheduleTime,'hh24:mi') as scheduleTime, "
					+ "correctiveMaintenance.correctiveMaintenanceId as correctiveMaintenanceId ,"
					+ " correctiveMaintenance.callLogNumber as callLogNumber , "
					+ " to_char(correctiveMaintenance.requestDateTime,'DD/MM/YYYY HH24:MI') as requestDateTime, "
					+ " to_char(correctiveMaintenance.downDateTime,'DD/MM/YYYY') as downDateTime, "
					+ " to_char(correctiveMaintenance.resolutionExpectedDateTime,'DD/MM/YYYY') as resolutionExpectedDateTime, "
					+ " to_char(correctiveMaintenance.resolutionExpectedTime,'hh24:mi') as resolutionExpectedTime, "
					+ " to_char(correctiveMaintenance.downTime,'hh24:mi') as downTime, "
					+ " correctiveMaintenance.requestTypeId as requestTypeId , "
					+ " correctiveMaintenance.serialNumber as serialNumber , "
					+ " correctiveMaintenance.isDown as isDown, "
					+ " correctiveMaintenance.requestDetails as requestDetails , "
					+ " correctiveMaintenance.isComplete as isComplete , "
					+ " correctiveMaintenance.completionRemak as completionRemak , "
					+ " to_char(correctiveMaintenance.completionTime,'hh24:mi') as completionTime, "
					+ " to_char(correctiveMaintenance.completionDate,'DD/MM/YYYY') as reqCompletionDate, "
					+ " correctiveMaintenance.remark as remark , "
					+ " assetCategory.category as assetcategory, "
					+ " correctiveMaintenance.requestCriticalityId as requestCriticalityId , "
					+ " correctiveMaintenance.maintenanceStatusId as maintenanceWorklistStatusId , "
					+ " concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as userName, "
					+ " concat(coalesce(prefx.prefixCode),'.',' ',coalesce(emp.firstName),' ',coalesce(emp.middleName),' ',coalesce(emp.lastName)) as requestedByName, "
					+ " itemMaster.name as assetName, "
					+ " correctiveMaintenance.batchId as batchId, "
					+ " correctiveMaintenance.itemId as itemId, "
					+ " correctiveMaintenance.storeId as storeId, "
					+ " correctiveMaintenance.referenceId as referenceId, "
					+ " correctiveMaintenance.referenceLogNumber as referenceLogNumber, "
					+ " correctiveMaintenance.reOpen as reOpen, "
					+ " itemMaster.code as assetCode  "
					+ " FROM CorrectiveMaintenanceSchedule mstr "
					+ " INNER JOIN mstr.correctiveMaintenanceWorklist correctiveMaintenanceWorklist "
					+ " INNER JOIN correctiveMaintenanceWorklist.correctiveMaintenance correctiveMaintenance "
					+ " INNER JOIN correctiveMaintenance.userMaster requestedByMstr "
					+ " INNER JOIN correctiveMaintenance.item itemMaster "
					+ " INNER JOIN itemMaster.productCategory  productCategory "
					+ " INNER JOIN productCategory.assetCategory  assetCategory "
					+ " INNER JOIN correctiveMaintenanceWorklist.userMaster userMsr "
					+ " LEFT JOIN userMsr.employeeMastersList employee "
					+ " LEFT JOIN employee.prefixMaster prefix "
					+ " LEFT JOIN requestedByMstr.employeeMastersList emp "
					+ " LEFT JOIN emp.prefixMaster prefx "
					+ " WHERE mstr.organizationId= :organizationId "
					+ " AND mstr.unitId= :unitId "
					),
	
	
	@NamedQuery(name="GET_CORRECTIVE_MAINTENANCE_SCHEDULING_LIST_BY_CMS_ID",
	query="SELECT mstr.correctiveMaintenanceScheduleId as correctiveMaintenanceScheduleId ,"
				+ " mstr.correctiveMaintenanceWorklistId as correctiveMaintenanceWorklistId , "
				+ " mstr.requestAllocationTypeId as requestAllocationTypeId, "
				+ " mstr.ticketNo as ticketNo, "
				+ " mstr.vendorId as vendorId, "
				+ " mstr.resolutionDetails as resolutionDetails, "
				+ " mstr.isCompleted as isCompleted, "
				+ " to_char(mstr.assetUpDate,'DD/MM/YYYY') as assetUpDate, "
				+ " to_char(mstr.assetUpTime,'hh24:mi') as assetUpTime, "
				+ " mstr.underObservation as underObservation, "
				+ " to_char(mstr.fromDate,'DD/MM/YYYY') as fromDate, "
				+ " to_char(mstr.toDate,'DD/MM/YYYY') as toDate, "
				+ " mstr.isStart as isStart, "
				+ " mstr.maintenanceScheduleStatusId as maintenanceScheduleStatusId, "
				+ " to_char(mstr.completionDate,'DD/MM/YYYY') as completionDate, "
				+ " mstr.remark as reSolutionRemark, "
				+ " mstr.createdBy as createdBy, "
				+ " mstr.status as status, "
				+ " mstr.updatedBy as updatedBy, "
				+ " to_char(mstr.createdDate,'dd-MM-yyyy HH:ss:mm') as createdDate, "
				+ " to_char(mstr.updatedDate,'dd-MM-yyyy HH:ss:mm') as updatedDate, "
				+ " mstr.unitId as unitId, "
				+ " mstr.organizationId as organizationId, "
				+ " correctiveMaintenanceWorklist.allocationRemark  as allocationRemark, "
				+ " correctiveMaintenanceWorklist.assignTo  as assignTo, "
				+ " to_char(correctiveMaintenanceWorklist.scheduleDate,'DD/MM/YYYY') as scheduleDate, "
				+ " to_char(correctiveMaintenanceWorklist.scheduleTime,'hh24:mi') as scheduleTime, "
				+ "correctiveMaintenance.correctiveMaintenanceId as correctiveMaintenanceId ,"
				+ " correctiveMaintenance.callLogNumber as callLogNumber , "
				+ " to_char(correctiveMaintenance.requestDateTime,'DD/MM/YYYY HH24:MI') as requestDateTime, "
				+ " to_char(correctiveMaintenance.downDateTime,'DD/MM/YYYY') as downDateTime, "
				+ " to_char(correctiveMaintenance.resolutionExpectedDateTime,'DD/MM/YYYY') as resolutionExpectedDateTime, "
				+ " to_char(correctiveMaintenance.resolutionExpectedTime,'hh24:mi') as resolutionExpectedTime, "
				+ " to_char(correctiveMaintenance.downTime,'hh24:mi') as downTime, "
				+ " correctiveMaintenance.requestTypeId as requestTypeId , "
				+ " correctiveMaintenance.serialNumber as serialNumber , "
				+ " correctiveMaintenance.isDown as isDown, "
				+ " correctiveMaintenance.requestDetails as requestDetails , "
				+ " correctiveMaintenance.isComplete as isComplete , "
				+ " correctiveMaintenance.completionRemak as completionRemak , "
				+ " to_char(correctiveMaintenance.completionTime,'hh24:mi') as completionTime, "
				+ " to_char(correctiveMaintenance.completionDate,'DD/MM/YYYY') as reqCompletionDate, "
				+ " correctiveMaintenance.remark as remark , "
				+ " assetCategory.category as assetcategory, "
				+ " correctiveMaintenance.requestCriticalityId as requestCriticalityId , "
				+ " correctiveMaintenance.maintenanceStatusId as maintenanceWorklistStatusId , "
				+ " concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as userName, "
				+ " concat(coalesce(prefx.prefixCode),'.',' ',coalesce(emp.firstName),' ',coalesce(emp.middleName),' ',coalesce(emp.lastName)) as requestedByName, "
				+ " itemMaster.name as assetName, "
				+ " correctiveMaintenance.batchId as batchId, "
				+ " correctiveMaintenance.itemId as itemId, "
				+ " correctiveMaintenance.storeId as storeId, "
				+ " correctiveMaintenance.referenceId as referenceId, "
				+ " correctiveMaintenance.referenceLogNumber as referenceLogNumber, "
				+ " correctiveMaintenance.reOpen as reOpen, "
				+ " itemMaster.code as assetCode ,"
				+ " vendor.name as vendorName "
				+ " FROM CorrectiveMaintenanceSchedule mstr "
				+ " INNER JOIN mstr.correctiveMaintenanceWorklist correctiveMaintenanceWorklist "
				+ " INNER JOIN correctiveMaintenanceWorklist.correctiveMaintenance correctiveMaintenance "
				+ " INNER JOIN correctiveMaintenance.userMaster requestedByMstr "
				+ " INNER JOIN correctiveMaintenance.item itemMaster "
				+ " INNER JOIN itemMaster.productCategory  productCategory "
				+ " INNER JOIN productCategory.assetCategory  assetCategory "
				+ " INNER JOIN correctiveMaintenanceWorklist.userMaster userMsr "
				+ " LEFT JOIN mstr.vendor vendor "
				+ " LEFT JOIN userMsr.employeeMastersList employee "
				+ " LEFT JOIN employee.prefixMaster prefix "
				+ " LEFT JOIN requestedByMstr.employeeMastersList emp "
				+ " LEFT JOIN emp.prefixMaster prefx "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.correctiveMaintenanceScheduleId =:correctiveMaintenanceScheId ")
})

@Entity
@Table(name = "t_corrective_maintenance_schedule", schema = "assets")
@SequenceGenerator(name = "seq_corrective_maintenance_schedule", sequenceName = "assets.seq_corrective_maintenance_schedule", allocationSize = 1)
public class CorrectiveMaintenanceSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_corrective_maintenance_schedule")
	@Column(name = "corrective_maintenance_schedule_id")
	private Integer correctiveMaintenanceScheduleId;
	
	@Column(name = "corrective_maintenance_worklist_id")
	private Integer correctiveMaintenanceWorklistId;
	
	@Column(name = "request_allocation_type_id")
	private Integer requestAllocationTypeId;
	
	@Column(name = "ticket_no")
	private String ticketNo;
	
	@Column(name = "resolution_details")
	private String resolutionDetails;
	
	@Column(name = "vendor_id")
	private Integer vendorId;

	@Column(name = "is_completed")
	private boolean isCompleted;
	
	@Column(name = "asset_up_date")
	private Date assetUpDate;
	
	@Column(name="asset_up_time")
	@Convert(converter = InstantConverter.class)
	private Instant assetUpTime;
	
	@Column(name = "under_observation")
	private boolean underObservation;
	
	@Column(name = "from_date")
	private Date fromDate;
	
	@Column(name = "to_date")
	private Date toDate;
	
	@Column(name = "is_start")
	private boolean isStart;
	
	@Column(name = "maintenance_schedule_status_id")
	private Integer maintenanceScheduleStatusId;
	
	@Column(name = "completion_date")
	private Date completionDate;
	
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "corrective_maintenance_worklist_id", insertable = false, updatable = false)
	private CorrectiveMaintenanceWorklist correctiveMaintenanceWorklist;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", insertable = false, updatable = false)
	private Vendor vendor;
	
	public Integer getCorrectiveMaintenanceScheduleId() {
		return correctiveMaintenanceScheduleId;
	}

	public void setCorrectiveMaintenanceScheduleId(Integer correctiveMaintenanceScheduleId) {
		this.correctiveMaintenanceScheduleId = correctiveMaintenanceScheduleId;
	}

	public Integer getCorrectiveMaintenanceWorklistId() {
		return correctiveMaintenanceWorklistId;
	}

	public void setCorrectiveMaintenanceWorklistId(Integer correctiveMaintenanceWorklistId) {
		this.correctiveMaintenanceWorklistId = correctiveMaintenanceWorklistId;
	}

	public Integer getRequestAllocationTypeId() {
		return requestAllocationTypeId;
	}

	public void setRequestAllocationTypeId(Integer requestAllocationTypeId) {
		this.requestAllocationTypeId = requestAllocationTypeId;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getResolutionDetails() {
		return resolutionDetails;
	}

	public void setResolutionDetails(String resolutionDetails) {
		this.resolutionDetails = resolutionDetails;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	public boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public Date getAssetUpDate() {
		return assetUpDate;
	}

	public void setAssetUpDate(Date assetUpDate) {
		this.assetUpDate = assetUpDate;
	}

	public Instant getAssetUpTime() {
		return assetUpTime;
	}

	public void setAssetUpTime(Instant assetUpTime) {
		this.assetUpTime = assetUpTime;
	}

	public boolean getIsUnderObservation() {
		return underObservation;
	}

	public void setIsUnderObservation(boolean underObservation) {
		this.underObservation = underObservation;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public boolean getIsStart() {
		return isStart;
	}

	public void setIsStart(boolean isStart) {
		this.isStart = isStart;
	}

	public Integer getMaintenanceScheduleStatusId() {
		return maintenanceScheduleStatusId;
	}

	public void setMaintenanceScheduleStatusId(Integer maintenanceScheduleStatusId) {
		this.maintenanceScheduleStatusId = maintenanceScheduleStatusId;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
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
}
