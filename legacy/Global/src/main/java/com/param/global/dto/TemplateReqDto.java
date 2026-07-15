package com.param.global.dto;

import java.math.BigInteger;
import java.util.List;

public class TemplateReqDto {

	private int orgId;
	private int unitId;
	private int doctorId;
	private int specialityId;
	private int subSpecialityId;
	private int templateTypeId;
	private int patientId;
	private int visitTypeId;
	private int admissionId;
	private int encounterId;
	private int diagnosisId;
	private int orderDetailsId;
	private int serviceId;
	private int mIcpcSystemId;
	private int mIcpcId;
	private int appointmentId;
	private int admNoteId;
	private BigInteger templateCount;
	private List<Integer> templateTypeIds;
	private Character isTemplateTypeReq;
	private String templateHtml;
	private String searchKeyword;
	private Integer templateId;
	private String createdDate1;
	private String conversationId;

	public String getCreatedDate1() {
		return createdDate1;
	}

	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}

	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public int getUnitId() {
		return unitId;
	}
	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	public int getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(int specialityId) {
		this.specialityId = specialityId;
	}
	public int getTemplateTypeId() {
		return templateTypeId;
	}
	public void setTemplateTypeId(int templateTypeId) {
		this.templateTypeId = templateTypeId;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public int getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(int visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public int getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(int admissionId) {
		this.admissionId = admissionId;
	}
	public int getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(int encounterId) {
		this.encounterId = encounterId;
	}
	public int getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(int diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public int getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(int orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getmIcpcSystemId() {
		return mIcpcSystemId;
	}
	public void setmIcpcSystemId(int mIcpcSystemId) {
		this.mIcpcSystemId = mIcpcSystemId;
	}
	public int getmIcpcId() {
		return mIcpcId;
	}
	public void setmIcpcId(int mIcpcId) {
		this.mIcpcId = mIcpcId;
	}
	public int getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(int subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public BigInteger getTemplateCount() {
		return templateCount;
	}
	public void setTemplateCount(BigInteger templateCount) {
		this.templateCount = templateCount;
	}
	public List<Integer> getTemplateTypeIds() {
		return templateTypeIds;
	}
	public void setTemplateTypeIds(List<Integer> templateTypeIds) {
		this.templateTypeIds = templateTypeIds;
	}
	public Character getIsTemplateTypeReq() {
		return isTemplateTypeReq;
	}
	public void setIsTemplateTypeReq(Character isTemplateTypeReq) {
		this.isTemplateTypeReq = isTemplateTypeReq;
	}
	public int getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	public int getAdmNoteId() {
		return admNoteId;
	}
	public void setAdmNoteId(int admNoteId) {
		this.admNoteId = admNoteId;
	}
	public String getTemplateHtml() {
		return templateHtml;
	}
	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	
	
}
