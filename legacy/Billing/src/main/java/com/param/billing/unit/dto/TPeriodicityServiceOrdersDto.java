package com.param.billing.unit.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import com.param.global.billing.dto.AdmissionListIpdChargesResDto;

public class TPeriodicityServiceOrdersDto {
	private Integer periodicityServiceOrderId;
	private Integer patientId;
	private Integer admissionId;
	private Integer visitTypeId;
	private Integer wardId;
	private Integer bedId;
	private Integer roomId;
	private Integer serviceId;
	private String fromDate;
	private String toDate;
	private Instant fromTime;
	private Instant toTime;
	private Integer noOfUnit;
	private Integer unitId;
	private Integer organisationId;
	private char status;
	private Integer orderDetailId;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private String lastUpdation;
	private Integer measurementUnitId;
	private Integer duration;
	private Integer specialityId;
	private Integer subSpecialityId;
	private Integer orderStatusId;
	private Integer defaultSelfTariffId;
	private String orderNo;
	private Integer selfPayeeId;
	private Integer serviceRendered;
	private Integer equipmentId;
	private AdmissionListIpdChargesResDto chargesResDto;
	
	public Integer getRoomId() {
		return roomId;
	}
	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	public Integer getServiceRendered() {
		return serviceRendered;
	}
	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}
	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}
	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}
	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}
	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}
	public AdmissionListIpdChargesResDto getChargesResDto() {
		return chargesResDto;
	}
	public void setChargesResDto(AdmissionListIpdChargesResDto chargesResDto) {
		this.chargesResDto = chargesResDto;
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
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		LocalTime modalityTime1 = LocalTime.parse(fromTime);
		LocalDate today = LocalDate.now();
		this.fromTime = modalityTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {

		LocalTime modalityTime1 = LocalTime.parse(toTime);
		LocalDate today = LocalDate.now();
		this.toTime = modalityTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

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
		this.status = status;
	}
	public Integer getOrderDetailId() {
		return orderDetailId;
	}
	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getLastUpdation() {
		return lastUpdation;
	}
	public void setLastUpdation(String lastUpdation) {
		this.lastUpdation = lastUpdation;
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
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public Integer getOrderStatusId() {
		return orderStatusId;
	}
	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
}
