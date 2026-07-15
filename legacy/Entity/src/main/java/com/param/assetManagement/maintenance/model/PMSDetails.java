package com.param.assetManagement.maintenance.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@NamedQueries({
	@NamedQuery(name = "GET_PMS_DETAILS_BY_PMS_ID", 
			query = "SELECT pms.pmsDetailsId as pmsDetailsId, pms.assetScheduleId as assetScheduleId , pms.assetScheduleId as assetScheduleId , "
					+ " pms.ticketNo as ticketNo , pms.pmsRemarks as pmsRemarks , pms.pmsDetails as pmsDetails ,"
					+ " pms.workOrderStatus as workOrderStatus , "
					+ " pms.completionTime as completionTime , pms.underObservationTime as underObservationTime , "
					+ " pms.completionDate as completionDate, pms.underObservationDate as underObservationDate  "
					+ " FROM PMSDetails as pms where pms.status='A' AND "
					+ " pms.pmsDetailsId=:pmsDetailsId AND pms.unitId=:unitId AND pms.organizationId=:organizationId"),
	
})

@Entity
@Table(name = "m_pms_details", schema = "equipment")
@SequenceGenerator(name = "seq_pms_details", sequenceName = "equipment.seq_pms_details", allocationSize = 1)
public class PMSDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pms_details")
	
	@Column(name = "pms_details_id")
	private Integer pmsDetailsId;
	
	@Column(name = "asset_schedule_id")
	private Integer assetScheduleId;
	
	@Column(name = "pms_type")
	private String pmsType;

	@Column(name = "ticket_no")
	private String ticketNo;

	@Column(name = "pms_remarks")
	private String pmsRemarks;
	
	@Column(name = "pms_details")
	private String pmsDetails;

	@Column(name = "completion_time")
	private String completionTime;
	
	@Column(name = "under_observation_time")
	private String underObservationTime;
	
	@Column(name = "completion_date")
	private Date completionDate;
	
	@Column(name = "under_observation_date")
	private Date underObservationDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by", updatable = false)
	private Integer createdBy;

	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name="unit_id", updatable = false)
	private Integer unitId;
	
	@Column(name="organization_id", updatable = false)
	private Integer organizationId;

	@Column(name = "work_order_status")
	private Character workOrderStatus;

	@Transient
	private List<SpareConsumption> getSpareConsumptionList;

	public Integer getPmsDetailsId() {
		return pmsDetailsId;
	}

	public void setPmsDetailsId(Integer pmsDetailsId) {
		this.pmsDetailsId = pmsDetailsId;
	}

	public Integer getAssetScheduleId() {
		return assetScheduleId;
	}

	public void setAssetScheduleId(Integer assetScheduleId) {
		this.assetScheduleId = assetScheduleId;
	}

	public String getPmsType() {
		return pmsType;
	}

	public void setPmsType(String pmsType) {
		this.pmsType = pmsType;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getPmsRemarks() {
		return pmsRemarks;
	}

	public void setPmsRemarks(String pmsRemarks) {
		this.pmsRemarks = pmsRemarks;
	}

	public String getPmsDetails() {
		return pmsDetails;
	}

	public void setPmsDetails(String pmsDetails) {
		this.pmsDetails = pmsDetails;
	}

	public String getCompletionTime() {
		return completionTime;
	}

	public void setCompletionTime(String completionTime) {
		this.completionTime = completionTime;
	}

	public String getUnderObservationTime() {
		return underObservationTime;
	}

	public void setUnderObservationTime(String underObservationTime) {
		this.underObservationTime = underObservationTime;
	}

	public Date getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Date completionDate) {
		this.completionDate = completionDate;
	}

	public Date getUnderObservationDate() {
		return underObservationDate;
	}

	public void setUnderObservationDate(Date underObservationDate) {
		this.underObservationDate = underObservationDate;
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

	public List<SpareConsumption> getGetSpareConsumptionList() {
		return getSpareConsumptionList;
	}

	public void setGetSpareConsumptionList(List<SpareConsumption> getSpareConsumptionList) {
		this.getSpareConsumptionList = getSpareConsumptionList;
	}

	public Character getWorkOrderStatus() {
		return workOrderStatus;
	}

	public void setWorkOrderStatus(Character workOrderStatus) {
		this.workOrderStatus = workOrderStatus;
	}
}
