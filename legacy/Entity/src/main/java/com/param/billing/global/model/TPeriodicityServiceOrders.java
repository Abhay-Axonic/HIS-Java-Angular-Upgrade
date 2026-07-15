package com.param.billing.global.model;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.Admission;
import com.param.adt.master.global.model.BedMaster;
import com.param.adt.master.unit.model.WardMaster;
import com.param.global.common.LocalTimeConverter;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.ServiceMaster;
import com.param.global.common.InstantConverter;

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_PERIODICITY_SERVICE_ORDERS_BY_ADMISSIONID",
			query =" select " 
					 + " 	pso.periodicity_service_order_id as \"periodicityServiceOrderId\", " 
					 + " 	pso.order_detail_id as \"orderDetailId\", " 
					 + " 	om.order_no as \"orderNo\", " 
					 + " 	pso.service_id as \"serviceId\", " 
					 + " 	pso.patient_id as \"patientId\", " 
					 + " 	pso.visit_type_id as \"visitTypeId\", " 
					 + " 	pso.ward_id as \"wardId\", " 
					 + " 	pso.bed_id as \"bedId\", " 
					 + " 	psc.measurement_unit_id as \"measurementUnitId\", " 
					 + " 	pso.unit_id as \"unitId\", " 
					 + " 	pso.organisation_id as \"organisationId\", " 
					 + " 	pso.created_by as \"createdBy\", " 
					 + " 	psc.duration as \"duration\", " 
					 + " 	ttm.tat_time_name as \"tatTimeName\", " 
					 + " 	sm.service_standard_name as \"serviceName\", " 
					 + " 	to_char(pso.from_date, 'DD-MM-YYYY') as \"fromDate\", "
					 + " 	pso.from_date as \"fromDateNew\", "
					 + " 	pso.to_date as \"toDateNew\", " 
					 + " 	pso.created_date as \"createdDateNew\", " 
					 + " 	to_char(pso.from_time, 'HH24:MI') as \"fromTime\", " 
					 + " 	to_char(pso.to_date, 'DD-MM-YYYY') as \"toDate\", " 
					 + " 	to_char(pso.to_time, 'HH24:MI') as \"toTime\", " 
					 + " 	pso.no_of_unit as \"noOfUnit\", " 
					 + " 	pso.order_status_id as \"orderStatusId\", " 
					 + " 	pso.admission_id as \"admissionId\", " 
					 + " 	pso.status as \"status\", " 
					 + " 	item.name as \"equipmentName\", " 
					 + " 	pso.equipment_id as \"equipmentId\", " 
					 + " 	psc.asset_category_id as \"assetCategoryId\", " 
					 + " 	psc.product_category_id as \"productCategoryId\", " 
					 + " 	od.service_rendered as \"serviceRendered\", " 
					 + " 	to_char(pso.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" " 
					 + " from " 
					 + " 	billing.t_periodicity_service_orders pso " 
					 + " inner join service.m_service_master sm on " 
					 + " 	sm.service_master_id = pso.service_id " 
					 + " left join public.t_order_details od on " 
					 + " 	od.order_details_id = pso.order_detail_id " 
					 + " left join public.t_order_master om on " 
					 + " 	om.order_id = od.order_id " 
					 + " inner join public.m_item item on " 
					 + " 	item.id = pso.equipment_id " 
					 + " inner join service.m_periodicity_service_config psc on " 
					 + " 	psc.service_id = pso.service_id " 
					 + " 	and psc.equipment_id = item.id "
					 + " 	and psc.visit_type_id = om.visit_type_id " 
					 + " 	and psc.unit_id = pso.unit_id " 
					 + " 	and psc.status = 'A' " 
					 + " inner join lab.m_tat_time_master ttm on " 
					 + " 	ttm.tat_time_id = psc.measurement_unit_id " 
					 + " 	and ttm.org_id = pso.organisation_id " 
					 + " where " 
					 + " 	pso.admission_id =:admissionId " 
					 + " union "
					 + " select " 
					 + " 	pso.periodicity_service_order_id as \"periodicityServiceOrderId\", " 
					 + " 	pso.order_detail_id as \"orderDetailId\", " 
					 + " 	om.order_no as \"orderNo\", " 
					 + " 	pso.service_id as \"serviceId\", " 
					 + " 	pso.patient_id as \"patientId\", " 
					 + " 	pso.visit_type_id as \"visitTypeId\", " 
					 + " 	pso.ward_id as \"wardId\", " 
					 + " 	pso.bed_id as \"bedId\", " 
					 + " 	psc.measurement_unit_id as \"measurementUnitId\", " 
					 + " 	pso.unit_id as \"unitId\", " 
					 + " 	pso.organisation_id as \"organisationId\", " 
					 + " 	pso.created_by as \"createdBy\", " 
					 + " 	psc.duration as \"duration\", " 
					 + " 	ttm.tat_time_name as \"tatTimeName\", " 
					 + " 	sm.service_standard_name as \"serviceName\", " 
					 + " 	to_char(pso.from_date, 'DD-MM-YYYY') as \"fromDate\", "
					 + " 	pso.from_date as \"fromDateNew\", "
					 + " 	pso.to_date as \"toDateNew\", " 
					 + " 	pso.created_date as \"createdDateNew\", "
					 + " 	to_char(pso.from_time, 'HH24:MI') as \"fromTime\", " 
					 + " 	to_char(pso.to_date, 'DD-MM-YYYY') as \"toDate\", " 
					 + " 	to_char(pso.to_time, 'HH24:MI') as \"toTime\", " 
					 + " 	pso.no_of_unit as \"noOfUnit\", " 
					 + " 	pso.order_status_id as \"orderStatusId\", " 
					 + " 	pso.admission_id as \"admissionId\", " 
					 + " 	pso.status as \"status\", " 
					 + " 	item.name as \"equipmentName\", " 
					 + " 	pso.equipment_id as \"equipmentId\", " 
					 + " 	psc.asset_category_id as \"assetCategoryId\", " 
					 + " 	psc.product_category_id as \"productCategoryId\", " 
					 + " 	od.service_rendered as \"serviceRendered\", " 
					 + " 	to_char(pso.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" " 
					 + " from " 
					 + " 	billing.t_periodicity_service_orders pso " 
					 + " inner join service.m_service_master sm on " 
					 + " 	sm.service_master_id = pso.service_id " 
					 + " left join public.t_order_details od on " 
					 + " 	od.order_details_id = pso.order_detail_id " 
					 + " left join public.t_order_master om on " 
					 + " 	om.order_id = od.order_id " 
					 + " left join public.m_item item on " 
					 + " 	item.id = pso.equipment_id " 
					 + " inner join service.m_periodicity_service_config psc on " 
					 + " 	psc.service_id = pso.service_id " 
					 + " 	and psc.visit_type_id = om.visit_type_id " 
					 + " 	and psc.unit_id = pso.unit_id " 
					 + " 	and psc.status = 'A' "
					 + "    AND psc.equipment_id = item.id "
					 + " inner join lab.m_tat_time_master ttm on "
					 + " 	ttm.tat_time_id = psc.measurement_unit_id " 
					 //+ " 	and ttm.org_id = pso.organisation_id "
					 + " where " 
					 + " 	pso.admission_id =:admissionId " 
					 ),
	
	@NamedNativeQuery(name="GET_PERIODICITY_SERVICE_ORDERS_BY_ORDERDETAILS_ID",
	query ="select "
			+ " 	pso.periodicity_service_order_id as \"periodicityServiceOrderId\", "
			+ " 	pso.order_detail_id as \"orderDetailId\", "
			+ " 	om.order_no as \"orderNo\", "
			+ " 	pso.service_id as \"serviceId\", "
			+ " 	pso.patient_id as \"patientId\", "
			+ " 	pso.visit_type_id as \"visitTypeId\", "
			+ " 	pso.ward_id as \"wardId\", "
			+ " 	pso.bed_id as \"bedId\", "
			+ " 	psc.measurement_unit_id as \"measurementUnitId\", "
			+ " 	pso.unit_id as \"unitId\", "
			+ " 	pso.organisation_id as \"organisationId\", "
			+ " 	psc.duration as \"duration\", "
			+ " 	to_char(pso.from_date, 'DD-MM-YYYY') as \"fromDate\", "
			+ " 	to_char(pso.from_time, 'HH24:MI') as \"fromTime\", "
			+ " 	to_char(pso.to_date, 'DD-MM-YYYY') as \"toDate\", "
			+ " 	to_char(pso.to_time, 'HH24:MI') as \"toTime\", "
			+ " 	pso.no_of_unit as \"noOfUnit\", "
			+ " 	pso.order_status_id as \"orderStatusId\", "
			+ " 	pso.admission_id as \"admissionId\", "
			+ " 	pso.status as \"status\", "
			+ " 	od.service_rendered as \"serviceRendered\", "
			+ " 	to_char(pso.created_date, 'DD-MM-YYYY HH24:MI:SS') as \"createdDate\" "
			+ " from "
			+ " 	billing.t_periodicity_service_orders pso "
			+ " inner join service.m_service_master sm on "
			+ " 	sm.service_master_id = pso.service_id "
			+ " left join public.t_order_details od on "
			+ " 	od.order_details_id = pso.order_detail_id "
			+ " left join public.t_order_master om on "
			+ " 	om.order_id = od.order_id "
			+ " inner join service.m_periodicity_service_config psc on "
			+ " 	psc.service_id = pso.service_id "
			+ " 	and psc.visit_type_id = om.visit_type_id "
			+ " 	and psc.unit_id = pso.unit_id "
			+ " left join lab.m_tat_time_master ttm on "
			+ " 	ttm.tat_time_id = psc.measurement_unit_id "
			+ " 	and ttm.org_id = pso.organisation_id "
			+ " where "
			+ " 	pso.order_detail_id =:orderDetailId "
			+ "		and pso.status='A' "),

	@NamedNativeQuery(name = "GET_PERIODICITY_FOR_BATCH_PROCESSING",
					 query = "select pservo.periodicity_service_order_id as \"periodicityServiceOrderId\", pservo.patient_id as \"patientId\", pservo.admission_id as \"admissionId\",  "
							 + " pservo.visit_type_id as \"visitTypeId\", pservo.ward_id as \"wardId\", pservo.bed_id as \"bedId\", pservo.service_id as \"serviceId\",  "
							 + " to_char(pservo.from_date,'DD-MM-YYYY') as \"fromDate\", to_char(pservo.from_time,'HH24:MI') as \"fromTime\", pservo.no_of_unit as \"noOfUnit\", pservo.unit_id as \"unitId\", "
							 + " pservo.organisation_id as \"organisationId\", pservo.status as \"status\", to_char(pservo.last_updation,'DD-MM-YYYY HH24:MI:SS') as \"lastUpdation\", "
							 + " config.measurement_unit_id as \"measurementUnitId\", config.duration as \"duration\", srv.speciality_id as \"specialityId\", srv.sub_speciality_id as \"subSpecialityId\" "
							 + " from billing.t_periodicity_service_orders pservo "
							 + " inner join service.m_periodicity_service_config config "
							 + " on config.service_id = pservo.service_id and pservo.organisation_id = config.organisation_id and pservo.unit_id = config.unit_id "
							 + " inner join service.m_service_master srv "
							 + " on srv.service_master_id = pservo.service_id "
							 + " where pservo.status = 'A' and config.status = 'A' "
							 + " and (pservo.to_date is null or pservo.to_date >= :toDate ) "
							 + " order by pservo.admission_id, pservo.periodicity_service_order_id asc "),
	
	@NamedNativeQuery(name = "UPDATE_PERIODICITY_NO_OF_UNITS_BY_ID",
					 query =  " update billing.t_periodicity_service_orders set no_of_unit = (no_of_unit + :noOfUnits) , last_updation = now() , "
					 		+ " where periodicity_service_order_id = :id ")
})

@Entity
@Table(name = "t_periodicity_service_orders" , schema = "billing")
@SequenceGenerator(name = "t_periodicity_service_orders_seq" , sequenceName = "billing.t_periodicity_service_orders_seq" , allocationSize = 1)
public class TPeriodicityServiceOrders {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_periodicity_service_orders_seq")
	@Column(name = "periodicity_service_order_id")
	private Integer periodicityServiceOrderId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "visit_type_id")
	private Integer visitTypeId;
	
	@Column(name = "ward_id")
	private Integer wardId;
	
	@Column(name = "bed_id")
	private Integer bedId;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "from_date")
	private Date fromDate;
	
	@Column(name = "to_date")
	private Date toDate;
	
	@Column(name = "from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name = "to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "no_of_unit")
	private Integer noOfUnit;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "order_detail_id")
	private Integer orderDetailId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "last_updation")
	private Date lastUpdation;
	
	@Column(name = "order_status_id")
	private Integer orderStatusId;
	
	@Column(name = "equipment_id")
	private Integer equipmentId;
	
	
	
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id" , insertable = false , updatable = false , nullable = false)
	private Admission admission;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bed_id" , insertable = false , updatable = false , nullable = false)
	private BedMaster bedMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_detail_id" , insertable = false , updatable = false , nullable = false)
	private OrderDetailsMaster orderDetailsMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id" , insertable = false , updatable = false , nullable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id" , insertable = false , updatable = false , nullable = false)
	private ServiceMaster serviceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ward_id" , insertable = false , updatable = false , nullable = false)
	private WardMaster wardMaster;

	
	public Integer getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	
	public Integer getPeriodicityServiceOrderId() {
		return periodicityServiceOrderId;
	}

	public void setPeriodicityServiceOrderId(Integer periodicityServiceOrderId) {
		this.periodicityServiceOrderId = periodicityServiceOrderId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(Instant toTime) {
		this.toTime = toTime;
	}

	public Integer getNoOfUnit() {
		return noOfUnit;
	}

	public void setNoOfUnit(Integer noOfUnit) {
		this.noOfUnit = noOfUnit;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Admission getAdmission() {
		return admission;
	}

	public void setAdmission(Admission admission) {
		this.admission = admission;
	}

	public BedMaster getBedMaster() {
		return bedMaster;
	}

	public void setBedMaster(BedMaster bedMaster) {
		this.bedMaster = bedMaster;
	}

	public OrderDetailsMaster getOrderDetailsMaster() {
		return orderDetailsMaster;
	}

	public void setOrderDetailsMaster(OrderDetailsMaster orderDetailsMaster) {
		this.orderDetailsMaster = orderDetailsMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public ServiceMaster getServiceMaster() {
		return serviceMaster;
	}

	public void setServiceMaster(ServiceMaster serviceMaster) {
		this.serviceMaster = serviceMaster;
	}

	public WardMaster getWardMaster() {
		return wardMaster;
	}

	public void setWardMaster(WardMaster wardMaster) {
		this.wardMaster = wardMaster;
	}

	public Date getLastUpdation() {
		return lastUpdation;
	}

	public void setLastUpdation(Date lastUpdation) {
		this.lastUpdation = lastUpdation;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
}
