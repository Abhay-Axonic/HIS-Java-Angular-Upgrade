package com.param.billing.unit.dto;

import java.util.Date;

public class TPeriodicityServiceDetailsDto {
	private Integer periodicityServiceOrderId;
	private Integer orderDetailId;
	private String orderNo;
	private String orderDate;
	private String fromDate;
	private String fromTime;
	private String toDate;
	private String toTime;
	private Integer serviceId;
	private String serviceName;
	private Integer noOfUnit;
	private String wardName;
	private String bedNumber;
	private Integer orderStatusId;
	private Integer patientId;
	private Integer visitTypeId;
	private Integer wardId;
	private Integer bedId;
	private Integer unitId;
	private Integer organisationId;
	private Integer createdBy;
	private Integer admissionId;
	private char status;
	private String createdDate;
	private Integer duration;
	private String tatTimeName;
	private Integer measurementUnitId;
	private Integer serviceRendered;
	
	private Integer assetCategoryId;
	private Integer productCategoryId;
	private String equipmentName;
	private Integer equipmentId;
	private Date fromDateNew;
	private Date toDateNew;
	private Date createdDateNew;
	
	
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	public Integer getAssetCategoryId() {
		return assetCategoryId;
	}
	public void setAssetCategoryId(Integer assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}
	public Integer getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public Integer getServiceRendered() {
		return serviceRendered;
	}
	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}
	public Integer getMeasurementUnitId() {
		return measurementUnitId;
	}
	public void setMeasurementUnitId(Integer measurementUnitId) {
		this.measurementUnitId = measurementUnitId;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public String getTatTimeName() {
		return tatTimeName;
	}
	public void setTatTimeName(String tatTimeName) {
		this.tatTimeName = tatTimeName;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'A');
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getNoOfUnit() {
		return noOfUnit;
	}
	public void setNoOfUnit(Integer noOfUnit) {
		this.noOfUnit = noOfUnit;
	}
	public String getWardName() {
		return wardName;
	}
	public void setWardName(String wardName) {
		this.wardName = wardName;
	}
	public String getBedNumber() {
		return bedNumber;
	}
	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}
	public Integer getPeriodicityServiceOrderId() {
		return periodicityServiceOrderId;
	}
	public void setPeriodicityServiceOrderId(Integer periodicityServiceOrderId) {
		this.periodicityServiceOrderId = periodicityServiceOrderId;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Date getFromDateNew() {
		return fromDateNew;
	}
	public void setFromDateNew(Date fromDateNew) {
		this.fromDateNew = fromDateNew;
	}
	public Date getToDateNew() {
		return toDateNew;
	}
	public void setToDateNew(Date toDateNew) {
		this.toDateNew = toDateNew;
	}
	public Date getCreatedDateNew() {
		return createdDateNew;
	}
	public void setCreatedDateNew(Date createdDateNew) {
		this.createdDateNew = createdDateNew;
	}
	
	
}
