package com.param.adt.discharge.dto;

import java.math.BigDecimal;
import java.util.List;

import com.param.global.dto.OrderDetailsPackageMapperDto;

public class InvestigationOrderDto {

	private Integer orderDetailsId;

	private Integer orderId;

	private Integer serviceMasterId;
	
	private Integer sampleStatusId;

	private String serviceStandardName;

	private String serviceStandardCode;

	private String specialityName;

	private String createdDate;

	private Integer admissionId;

	private Integer encounterId;

	private Integer labSampleDtlsId;

	private Integer labTestResId;

	private Integer panelId;

	private Integer subDeptId;
	
	private Integer specialityId;
	
	private Integer subSpecialityId;

	private Character isPanel;

	private String statusName;

	private Integer patientId;
	
	private Integer serviceRendered;
	
	private Integer templateTransactionId;
	
	private Integer serviceIsBilled;

	private Character orderStatus;
	
	private Integer appointmentId;
	
	private Integer slotId;
	private Integer organizationId;
	private String createdDateBookView;
	private String createdDateNew;

	private Character isPackageService;
	private Character isAutoRender;
	
	private Integer orderSetId;


	private Integer proReqId;
	
	private Integer procedureReqId;
	private Integer packageId;
	private Integer priorityId;
	
	private String orderSchDate;
	
	private String userName;
	
	private String ordRemarks;
	
	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;

	private String templateObject;
	
	private Integer microTemplateStatusId;
	
	private Double parameterMinValue;
	
	private Double parameterMaxValue;
	
	private String parameterUnit;
	
	private String resultValue;
	
	private Integer typeId;
	
	private String resultRemark;
	
	private String parameterName;
	
	private Integer testType;
	
	private String headerName;
	
	private Integer headerId;
	
	private String footNote;
	
	private Character isAbnormal;
	
	private Integer refrangeTypeId;
	
	public Character getIsAbnormal() {
		return isAbnormal;
	}

	public void setIsAbnormal(Character isAbnormal) {
		this.isAbnormal = isAbnormal;
	}

	public Integer getRefrangeTypeId() {
		return refrangeTypeId;
	}

	public void setRefrangeTypeId(Integer refrangeTypeId) {
		this.refrangeTypeId = refrangeTypeId;
	}

	
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getPanelId() {
		return panelId;
	}

	public void setPanelId(Integer panelId) {
		this.panelId = panelId;
	}

	public Character getIsPanel() {
		return isPanel;
	}

	public void setIsPanel(Character isPanel) {
		this.isPanel = isPanel;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getLabTestResId() {
		return labTestResId;
	}

	public void setLabTestResId(Integer labTestResId) {
		this.labTestResId = labTestResId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public Integer getServiceIsBilled() {
		return serviceIsBilled;
	}

	public void setServiceIsBilled(Integer serviceIsBilled) {
		this.serviceIsBilled = serviceIsBilled;
	}

	public Character getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getCreatedDateBookView() {
		return createdDateBookView;
	}

	public void setCreatedDateBookView(String createdDateBookView) {
		this.createdDateBookView = createdDateBookView;
	}

	public Character getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(Character isPackageService) {
		this.isPackageService = isPackageService;
	}

	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		return orderDetailsPackageMapperDtosList;
	}

	public void setOrderDetailsPackageMapperDtosList(List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Integer getOrderSetId() {
		return orderSetId;
	}

	public void setOrderSetId(Integer orderSetId) {
		this.orderSetId = orderSetId;
	}

	public String getOrderSchDate() {
		return orderSchDate;
	}

	public void setOrderSchDate(String orderSchDate) {
		this.orderSchDate = orderSchDate;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureReqId() {
		return procedureReqId;
	}

	public void setProcedureReqId(Integer procedureReqId) {
		this.procedureReqId = procedureReqId;
	}

	public String getCreatedDateNew() {
		return createdDateNew;
	}

	public void setCreatedDateNew(String createdDateNew) {
		this.createdDateNew = createdDateNew;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrdRemarks() {
		return ordRemarks;
	}

	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
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

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
	}

	public Integer getMicroTemplateStatusId() {
		return microTemplateStatusId;
	}

	public void setMicroTemplateStatusId(Integer microTemplateStatusId) {
		this.microTemplateStatusId = microTemplateStatusId;
	}


	public String getParameterUnit() {
		return parameterUnit;
	}

	public void setParameterUnit(String parameterUnit) {
		this.parameterUnit = parameterUnit;
	}

	public String getResultValue() {
		return resultValue;
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

	public Double getParameterMinValue() {
		return parameterMinValue;
	}

	public void setParameterMinValue(Double parameterMinValue) {
		this.parameterMinValue = parameterMinValue;
	}

	public Double getParameterMaxValue() {
		return parameterMaxValue;
	}

	public void setParameterMaxValue(Double parameterMaxValue) {
		this.parameterMaxValue = parameterMaxValue;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getResultRemark() {
		return resultRemark;
	}

	public void setResultRemark(String resultRemark) {
		this.resultRemark = resultRemark;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	public String getFootNote() {
		return footNote;
	}

	public void setFootNote(String footNote) {
		this.footNote = footNote;
	}
	
	
	
}
