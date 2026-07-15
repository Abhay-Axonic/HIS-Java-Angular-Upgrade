package com.param.billing.global.transaction.config.model;

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

import com.param.global.model.ServiceMaster;


@NamedQueries({
	
	@NamedQuery(name="GET_AUTO_POSTING_CONFIG_LIST",query="SELECT autoPos.id as id, "
			+ "autoPos.orgId as orgId, "
			+ "autoPos.unitId as unitId, "
			+ "autoPos.serviceId as serviceId,service.specialityId as specialityId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "autoPos.postingTime as postingTime, "
			+ "autoPos.isHalfDay as isHalfDay, "
			+ "autoPos.halfDayHrs as halfDayHrs, "
			+ "autoPos.bufferTimeApplicable as bufferTimeApplicable, "
			+ "autoPos.bufferTimeForAdmission as bufferTimeForAdmission, "
			+ "autoPos.bufferTimeForDischarge as bufferTimeForDischarge, "
			+ "autoPos.status as status "
			+ "FROM AutoPostingServicesCofiguration autoPos "
			+ "INNER JOIN autoPos.serviceMaster service "
			+ "WHERE autoPos.orgId=:orgId "
			+ "AND autoPos.unitId=:unitId "
			+ "AND autoPos.status='A' "
			+ "AND autoPos.isPeriodic='N'"),
	
	@NamedQuery(name="GET_PERIODIC_BILLING_LIST",query="SELECT autoPos.id as id, "
			+ "autoPos.orgId as orgId, "
			+ "autoPos.unitId as unitId, "
			+ "autoPos.serviceId as serviceId,"
			+ "service.specialityId as specialityId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "autoPos.status as status "
			+ "FROM AutoPostingServicesCofiguration autoPos "
			+ "INNER JOIN autoPos.serviceMaster service "
			+ "WHERE autoPos.orgId=:orgId "
			+ "AND autoPos.unitId=:unitId "
			+ "AND autoPos.status='A' "
			+ "AND autoPos.isPeriodic='Y'"),
	

	
	@NamedQuery(name="GET_AUTO_POSTING_CONFIG_LIST_FOR_ROOM_RENT_POSTING",query="SELECT autoPos.id as id, "
			+ "autoPos.orgId as orgId, "
			+ "autoPos.unitId as unitId, "
			+ "autoPos.serviceId as serviceId,service.specialityId as specialityId, "
			+ "service.serviceStandardName as serviceStandardName, "
			+ "autoPos.postingTime as postingTime, "
			+ "autoPos.isHalfDay as isHalfDay, "
			+ "autoPos.halfDayHrs as halfDayHrs, "
			+ "autoPos.bufferTimeApplicable as bufferTimeApplicable, "
			+ "autoPos.bufferTimeForAdmission as bufferTimeForAdmission, "
			+ "autoPos.bufferTimeForDischarge as bufferTimeForDischarge, "
			+ "autoPos.status as status "
			+ "FROM AutoPostingServicesCofiguration autoPos "
			+ "INNER JOIN autoPos.serviceMaster service "
			+ "WHERE autoPos.orgId=:orgId "
			+ "AND autoPos.unitId=:unitId "
			+ " AND autoPos.postingTime=:postingId "
			+ " AND autoPos.isPeriodic='N' "
			+ " AND autoPos.status='A' "),
	
	@NamedQuery(name="CHECK_EXISTANCE",query="SELECT autoPos.id as id "
			+ "FROM AutoPostingServicesCofiguration autoPos "
			+ "WHERE autoPos.orgId=:orgId "
			+ "AND autoPos.unitId=:unitId "
			+ "AND autoPos.serviceId=:serviceId ")
})




@Entity
@Table(name="t_auto_posting_services_cofiguration", schema="public")
@SequenceGenerator(name = "auto_posting_services_cofiguration_seq" , sequenceName = "public.auto_posting_services_cofiguration_seq" , allocationSize = 1)
public class AutoPostingServicesCofiguration {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "auto_posting_services_cofiguration_seq")
	private Integer id;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="posting_time")
	private Integer postingTime;
	
	@Column(name="is_half_day")
	private Character isHalfDay;
	
	@Column(name="half_day_hrs")
	private Integer halfDayHrs;
	
	@Column(name="is_buffer_time_applicable")
	private Character bufferTimeApplicable;
	
	@Column(name="buffer_time_for_admission")
	private Integer bufferTimeForAdmission;
	
	@Column(name="buffer_time_for_discharge")
	private Integer bufferTimeForDischarge;
	
	@Column(name="status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="is_periodic")
	private Character isPeriodic;
	
	@Column(name="is_bed_charges_service")
	private Character isBedChargesService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", insertable = false, updatable = false, nullable = false)
	private ServiceMaster serviceMaster ;

	public Character getIsBedChargesService() {
		return isBedChargesService;
	}

	public void setIsBedChargesService(Character isBedChargesService) {
		this.isBedChargesService = isBedChargesService;
	}

	public Character getIsPeriodic() {
		return isPeriodic;
	}

	public void setIsPeriodic(Character isPeriodic) {
		this.isPeriodic = isPeriodic;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPostingTime() {
		return postingTime;
	}

	public void setPostingTime(Integer postingTime) {
		this.postingTime = postingTime;
	}

	public Character getIsHalfDay() {
		return isHalfDay;
	}

	public void setIsHalfDay(Character isFalfDay) {
		this.isHalfDay = isFalfDay;
	}

	public Integer getHalfDayHrs() {
		return halfDayHrs;
	}

	public void setHalfDayHrs(Integer halfDayHrs) {
		this.halfDayHrs = halfDayHrs;
	}

	public Character getBufferTimeApplicable() {
		return bufferTimeApplicable;
	}

	public void setBufferTimeApplicable(Character bufferTimeApplicable) {
		this.bufferTimeApplicable = bufferTimeApplicable;
	}

	public Integer getBufferTimeForAdmission() {
		return bufferTimeForAdmission;
	}

	public void setBufferTimeForAdmission(Integer bufferTimeForAdmission) {
		this.bufferTimeForAdmission = bufferTimeForAdmission;
	}

	public Integer getBufferTimeForDischarge() {
		return bufferTimeForDischarge;
	}

	public void setBufferTimeForDischarge(Integer bufferTimeForDischarge) {
		this.bufferTimeForDischarge = bufferTimeForDischarge;
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
	
}
