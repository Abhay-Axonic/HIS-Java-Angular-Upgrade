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

import com.param.adt.master.global.model.UserMaster;
import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;


@NamedQueries({
	
	@NamedQuery(name="GET_CORRECTIVE_MAINTENANCE_WORKLIST_LIST",
		query="SELECT  mstr.correctiveMaintenanceWorklistId as correctiveMaintenanceWorklistId,"
					+ " mstr.allocationRemark  as allocationRemark, "
					+ " mstr.assignTo  as assignTo, "
					+ " to_char(mstr.scheduleDate,'DD/MM/YYYY') as scheduleDate, "
					+ " to_char(mstr.scheduleTime,'hh24:mi') as scheduleTime, "
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
					+ " correctiveMaintenance.remark as remark , "
					+ " assetCategory.category as assetcategory, "
					+ " correctiveMaintenance.isComplete as isComplete , "
					+ " correctiveMaintenance.completionRemak as completionRemak , "
					+ " to_char(correctiveMaintenance.completionTime,'hh24:mi') as completionTime, "
					+ " to_char(correctiveMaintenance.completionDate,'DD/MM/YYYY') as completionDate, "
					+ " correctiveMaintenance.requestCriticalityId as requestCriticalityId , "
					+ " correctiveMaintenance.maintenanceStatusId as maintenanceWorklistStatusId , "
					+ " concat(coalesce(prefix.prefixCode),'.',' ',coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as userName, "
					+ " concat(coalesce(prefx.prefixCode),'.',' ',coalesce(emp.firstName),' ',coalesce(emp.middleName),' ',coalesce(emp.lastName)) as requestedByName, "
					+ " itemMaster.name as assetName, "
					+ " correctiveMaintenance.batchId as batchId, "
					+ " correctiveMaintenance.itemId as itemId, "
					+ " correctiveMaintenance.referenceId as referenceId, "
					+ " correctiveMaintenance.referenceLogNumber as referenceLogNumber, "
					+ " correctiveMaintenance.reOpen as reOpen, "
					+ " itemMaster.code as assetCode  "
					+ " FROM CorrectiveMaintenanceWorklist mstr "
					+ " INNER JOIN mstr.correctiveMaintenance correctiveMaintenance "
					+ " INNER JOIN correctiveMaintenance.userMaster requestedByMstr "
					+ " INNER JOIN correctiveMaintenance.item itemMaster "
					+ " INNER JOIN itemMaster.productCategory  productCategory "
					+ " INNER JOIN productCategory.assetCategory  assetCategory "
					+ " INNER JOIN mstr.userMaster userMsr "
					+ " LEFT JOIN userMsr.employeeMastersList employee "
					+ " LEFT JOIN employee.prefixMaster prefix "
					+ " LEFT JOIN requestedByMstr.employeeMastersList emp "
					+ " LEFT JOIN emp.prefixMaster prefx "
					+ " WHERE mstr.organizationId= :organizationId "
					+ " AND mstr.unitId= :unitId "
					)
})
@Entity
@Table(name = "t_corrective_maintenance_worklist", schema = "assets")
@SequenceGenerator(name = "seq_corrective_maintenance_worklist", sequenceName = "assets.seq_corrective_maintenance_worklist", allocationSize = 1)
public class CorrectiveMaintenanceWorklist {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_corrective_maintenance_worklist")
	@Column(name = "corrective_maintenance_worklist_id")
	private Integer correctiveMaintenanceWorklistId;
	
	@Column(name = "corrective_maintenance_id")
	private Integer correctiveMaintenanceId;

	@Column(name = "schedule_date")
	private Date scheduleDate;
	
	@Column(name="schedule_time")
	@Convert(converter = InstantConverter.class)
	private Instant scheduleTime;
	
	@Column(name = "maintenance_worklist_status_id")
	private Integer maintenanceWorklistStatusId;
	
	@Column(name = "assign_to")
	private Integer assignTo;
	
	@Column(name = "remark")
	private String allocationRemark;
	
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
	
	@Column(name = "exported_to_scheduling")
	private boolean exportedToScheduling;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "corrective_maintenance_id", insertable = false, updatable = false)
	private CorrectiveMaintenance correctiveMaintenance;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assign_to", insertable = false, updatable = false)
	private UserMaster userMaster;

	public Integer getCorrectiveMaintenanceWorklistId() {
		return correctiveMaintenanceWorklistId;
	}

	public void setCorrectiveMaintenanceWorklistId(Integer correctiveMaintenanceWorklistId) {
		this.correctiveMaintenanceWorklistId = correctiveMaintenanceWorklistId;
	}

	public Integer getCorrectiveMaintenanceId() {
		return correctiveMaintenanceId;
	}

	public void setCorrectiveMaintenanceId(Integer correctiveMaintenanceId) {
		this.correctiveMaintenanceId = correctiveMaintenanceId;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Instant getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Instant scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public Integer getMaintenanceWorklistStatusId() {
		return maintenanceWorklistStatusId;
	}

	public void setMaintenanceWorklistStatusId(Integer maintenanceWorklistStatusId) {
		this.maintenanceWorklistStatusId = maintenanceWorklistStatusId;
	}

	public Integer getAssignTo() {
		return assignTo;
	}

	public void setAssignTo(Integer assignTo) {
		this.assignTo = assignTo;
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

	public String getAllocationRemark() {
		return allocationRemark;
	}

	public void setAllocationRemark(String allocationRemark) {
		this.allocationRemark = allocationRemark;
	}

	public boolean isExportedToScheduling() {
		return exportedToScheduling;
	}

	public void setExportedToScheduling(boolean exportedToScheduling) {
		this.exportedToScheduling = exportedToScheduling;
	}
	
	
}
