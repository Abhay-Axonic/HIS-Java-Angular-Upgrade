package com.param.global.dto;

import java.math.BigDecimal;

public class AmbulanceChargesDto {

	private int ambulanceChargesId;
	private int patientId;
	private String patientName;
	private String ambulanceTypeName ;
	private String billNo;
	private String uhidNumber;
	private String fromAddress;
	private String toAddress;
	private int ambulanceType;
	private String distance;
	private String ambulanceProvider;
	private String vehicleNo;
	private String outSourceVenderName;
	private String lpoNo;
	private String lpoDate;
	private String lpoVenderName;
	private String lpoVenderAddress;
	private String lpoAmbulanceProvider;
	private String lpoFromLocation;
	private String lpoToLocation;
	private String lpoDistance;
	private String lpoAmount;
	private char status;
	private int createdBy;
	private String createdDate;
	private int updatedBy;
	private String updatedDate;
	private int organizationId;
	private Integer noOfRecordsPerPage;
	private Integer  offset;
	private Integer unitId;
	
	private Integer admissionId;
	
	private Integer encounterId;
	
	private Integer serviceId;
	
	private Integer orderDetailsId;
	
	private String orderNo;
	
	private BigDecimal totalAmount;
	
	private BigDecimal paidAmount;
	
	private Integer billingMasterId;
	
	public int getAmbulanceChargesId() {
		return ambulanceChargesId;
	}
	public void setAmbulanceChargesId(int ambulanceChargesId) {
		this.ambulanceChargesId = ambulanceChargesId;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public int getAmbulanceType() {
		return ambulanceType;
	}
	public void setAmbulanceType(int ambulanceType) {
		this.ambulanceType = ambulanceType;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getAmbulanceProvider() {
		return ambulanceProvider;
	}
	public void setAmbulanceProvider(String ambulanceProvider) {
		this.ambulanceProvider = ambulanceProvider;
	}
	public String getVehicleNo() {
		return vehicleNo;
	}
	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	public String getOutSourceVenderName() {
		return outSourceVenderName;
	}
	public void setOutSourceVenderName(String outSourceVenderName) {
		this.outSourceVenderName = outSourceVenderName;
	}
	public String getLpoNo() {
		return lpoNo;
	}
	public void setLpoNo(String lpoNo) {
		this.lpoNo = lpoNo;
	}
	public String getLpoDate() {
		return lpoDate;
	}
	public void setLpoDate(String lpoDate) {
		this.lpoDate = lpoDate;
	}
	public String getLpoVenderName() {
		return lpoVenderName;
	}
	public void setLpoVenderName(String lpoVenderName) {
		this.lpoVenderName = lpoVenderName;
	}
	public String getLpoVenderAddress() {
		return lpoVenderAddress;
	}
	public void setLpoVenderAddress(String lpoVenderAddress) {
		this.lpoVenderAddress = lpoVenderAddress;
	}
	public String getLpoAmbulanceProvider() {
		return lpoAmbulanceProvider;
	}
	public void setLpoAmbulanceProvider(String lpoAmbulanceProvider) {
		this.lpoAmbulanceProvider = lpoAmbulanceProvider;
	}
	public String getLpoFromLocation() {
		return lpoFromLocation;
	}
	public void setLpoFromLocation(String lpoFromLocation) {
		this.lpoFromLocation = lpoFromLocation;
	}
	public String getLpoToLocation() {
		return lpoToLocation;
	}
	public String getAmbulanceTypeName() {
		return ambulanceTypeName;
	}
	public void setAmbulanceTypeName(String ambulanceTypeName) {
		this.ambulanceTypeName = ambulanceTypeName;
	}
	public void setLpoToLocation(String lpoToLocation) {
		this.lpoToLocation = lpoToLocation;
	}
	public String getLpoDistance() {
		return lpoDistance;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public void setLpoDistance(String lpoDistance) {
		this.lpoDistance = lpoDistance;
	}
	public String getLpoAmount() {
		return lpoAmount;
	}
	public void setLpoAmount(String lpoAmount) {
		this.lpoAmount = lpoAmount;
	}
	
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}
	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
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
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public BigDecimal getPaidAmount() {
		return paidAmount;
	}
	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}
	public Integer getBillingMasterId() {
		return billingMasterId;
	}
	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}
	

	
}
