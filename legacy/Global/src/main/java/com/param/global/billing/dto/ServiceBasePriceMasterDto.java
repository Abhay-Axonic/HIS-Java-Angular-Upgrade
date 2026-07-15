package com.param.global.billing.dto;



public class ServiceBasePriceMasterDto {
	
	private Integer serviceBasePriceId;
	  
	private Integer serviceMasterId;
	  
	private Integer visitTypeId;
	  
	private double basePrice;
	  
	private String  fromDate;
	  
	private String  toDate;
	  
	private Integer unitId;
	
	private Integer orgnisationId;
	
	private Character status;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private String serviceName;
	
	private String specialityName;
	
	private String subSpecialityMasterName;
	
	private Integer specialityId;
	
	private Integer subSpecialityMasterId;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String serviceStandardCode;
	
	private double costOfService;
	
	public double getCostOfService() {
		return costOfService;
	}

	public void setCostOfService(double costOfService) {
		this.costOfService = costOfService;
	}

	public Integer getServiceBasePriceId() {
		return serviceBasePriceId;
	}

	public void setServiceBasePriceId(Integer serviceBasePriceId) {
		this.serviceBasePriceId = serviceBasePriceId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityMasterName() {
		return subSpecialityMasterName;
	}

	public void setSubSpecialityMasterName(String subSpecialityMasterName) {
		this.subSpecialityMasterName = subSpecialityMasterName;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getSubSpecialityMasterId() {
		return subSpecialityMasterId;
	}

	public void setSubSpecialityMasterId(Integer subSpecialityMasterId) {
		this.subSpecialityMasterId = subSpecialityMasterId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}
	
	
}
