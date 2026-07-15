package com.param.global.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDetailsDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicalHistoryDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;

public class CoversheetTemplateMasterDto {
	private Integer covTemplateId;

	private String templateName;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;
	
	private String updatedDate;
	private String ceratedDate;
	private String patientRemark;
	private Integer unitId;

	private Integer organizationId;
	
	private Character status;
	private Integer patientId;
	private Integer roleId;

	private Integer admissionId;
	private Integer encounterId;
	private Integer doctorId;
	private Integer typeId;
	private BigInteger id;
	private Integer routeId;
	private Integer visitTypeId;
	private Integer quantityForDoseCal;
	private Integer strengthForDoseCal;
	private boolean instructionTextFlag;
	private Integer doctorFavoriteId;
	private String isInstructionText;

	private Integer parentTemplateId;
	private List<CoversheetTemplateMasterDto> templateTypeList;
	private List<ComplaintAppointmentDto> complaintsPatientList;
	private List<ComplaintAppointmentDetailsDto> listComplaintAppointmentDetailsDto;
	private List<PatientFamilyHistoryDto> familyHistoryCurrentList;

	private List<ComplaintAppointmentMapperDto> listComplaintData;
	private List<ComplaintAppointmentMapperDto> listComplaintFavoriteData;
	private List<DiagnosisPatientAppointmentMapperDto> listDiagnosisData;
	private List<DiagnosisPatientAppointmentMapperDto> listDiagnosisFavoriteData;
	private List<InvestigationOrderMasterNewDto> listLabOrderData;
	private List<InvestigationOrderMasterNewDto> listLabOrderFavoriteData;
	private List<CoversheetInstructionDto> listAdviceData;
	private List<CoversheetInstructionDto> listAdviceFavoriteData;
	private List<MedicationDTO> listPrescriptionData;
	private List<MedicationDTO> listPrescriptionFavoriteData;
	private List<ComplaintAppointmentDetailsDto> listComplaintDataSave;
	private List<PatientRegistrationDto> listPatientRemark;
	private List<QuickNotesMapperDto> listQuickNotes;
	private List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;

	private List<PatientHabitDetailsDto> patientHabitList;

	private List<MedicationHistoryDto> detailedMedicationsHistory;

	private List<MedicationDTO> detailedMedications;

	private List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisList;

	private List<AppointmentVitalMapperDto> patientViatalList;

	private List<PatientSurgicalHistoryDto> surgicalHistoryCurrentList;

	private List<PatientMedicalHistoryDto> medicalHistoryCurrentList;

	private List<AllergyPatientMapperDto> allergyCurrentList;

	private List<PatientTreatmentHistoryDetailsDto> treatmentHistoryCurrentList;

	private List<PatientPersonalHistoryDetailsDto> personalHistoryCurrentList;

	private List<PatientTravelHistoryDetailsDto> travelHistoryCurrentList;

	private List<OrderDetailsMasterDto> currentEncounterOrderLists;

	private List<ConsultDoctorDto> consultOrdersList;

	private List<OrderDetailsMasterDto> orderedProcedureList;

	private List<TemplateMasterDto> letterTemplateList;

	private List<TemplateMasterDto> certTemplateList;
	private boolean isCovtemplate;
	private List<ObservationDto> observationList;
	
    private Date startDateNew;
	
	private Date endDateNew;
	
	private Integer billingBedCategoryId;
	private Integer paymentEntitlementId;
	private Integer subDepartmentId;
	private Integer paymentEntitlementTypeId;
	private Integer serviceRenderingDeptId;
	private Integer orderStatusId;
	private Integer payeeId;
	private Integer ordDocSplId;
	private Integer patientTypeId;
	
	
	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
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

	
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	
	public List<ComplaintAppointmentDto> getComplaintsPatientList() {
		return complaintsPatientList;
	}

	public void setComplaintsPatientList(List<ComplaintAppointmentDto> complaintsPatientList) {
		this.complaintsPatientList = complaintsPatientList;
	}

	public List<PatientFamilyHistoryDto> getFamilyHistoryCurrentList() {
		return familyHistoryCurrentList;
	}

	public void setFamilyHistoryCurrentList(List<PatientFamilyHistoryDto> familyHistoryCurrentList) {
		this.familyHistoryCurrentList = familyHistoryCurrentList;
	}

	public List<PatientHabitDetailsDto> getPatientHabitList() {
		return patientHabitList;
	}

	public void setPatientHabitList(List<PatientHabitDetailsDto> patientHabitList) {
		this.patientHabitList = patientHabitList;
	}

	public List<DiagnosisPatientAppointmentMapperDto> getPatientDiagnosisList() {
		return patientDiagnosisList;
	}

	public void setPatientDiagnosisList(List<DiagnosisPatientAppointmentMapperDto> patientDiagnosisList) {
		this.patientDiagnosisList = patientDiagnosisList;
	}

	public List<AppointmentVitalMapperDto> getPatientViatalList() {
		return patientViatalList;
	}

	public void setPatientViatalList(List<AppointmentVitalMapperDto> patientViatalList) {
		this.patientViatalList = patientViatalList;
	}

	public List<PatientSurgicalHistoryDto> getSurgicalHistoryCurrentList() {
		return surgicalHistoryCurrentList;
	}

	public void setSurgicalHistoryCurrentList(List<PatientSurgicalHistoryDto> surgicalHistoryCurrentList) {
		this.surgicalHistoryCurrentList = surgicalHistoryCurrentList;
	}

	public List<PatientMedicalHistoryDto> getMedicalHistoryCurrentList() {
		return medicalHistoryCurrentList;
	}

	public void setMedicalHistoryCurrentList(List<PatientMedicalHistoryDto> medicalHistoryCurrentList) {
		this.medicalHistoryCurrentList = medicalHistoryCurrentList;
	}

	public List<AllergyPatientMapperDto> getAllergyCurrentList() {
		return allergyCurrentList;
	}

	public void setAllergyCurrentList(List<AllergyPatientMapperDto> allergyCurrentList) {
		this.allergyCurrentList = allergyCurrentList;
	}

	public List<PatientTreatmentHistoryDetailsDto> getTreatmentHistoryCurrentList() {
		return treatmentHistoryCurrentList;
	}

	public void setTreatmentHistoryCurrentList(List<PatientTreatmentHistoryDetailsDto> treatmentHistoryCurrentList) {
		this.treatmentHistoryCurrentList = treatmentHistoryCurrentList;
	}

	public List<PatientPersonalHistoryDetailsDto> getPersonalHistoryCurrentList() {
		return personalHistoryCurrentList;
	}

	public void setPersonalHistoryCurrentList(List<PatientPersonalHistoryDetailsDto> personalHistoryCurrentList) {
		this.personalHistoryCurrentList = personalHistoryCurrentList;
	}

	public List<PatientTravelHistoryDetailsDto> getTravelHistoryCurrentList() {
		return travelHistoryCurrentList;
	}

	public void setTravelHistoryCurrentList(List<PatientTravelHistoryDetailsDto> travelHistoryCurrentList) {
		this.travelHistoryCurrentList = travelHistoryCurrentList;
	}

	public List<OrderDetailsMasterDto> getCurrentEncounterOrderLists() {
		return currentEncounterOrderLists;
	}

	public void setCurrentEncounterOrderLists(List<OrderDetailsMasterDto> currentEncounterOrderLists) {
		this.currentEncounterOrderLists = currentEncounterOrderLists;
	}

	public List<ConsultDoctorDto> getConsultOrdersList() {
		return consultOrdersList;
	}

	public void setConsultOrdersList(List<ConsultDoctorDto> consultOrdersList) {
		this.consultOrdersList = consultOrdersList;
	}

	public List<OrderDetailsMasterDto> getOrderedProcedureList() {
		return orderedProcedureList;
	}

	public void setOrderedProcedureList(List<OrderDetailsMasterDto> orderedProcedureList) {
		this.orderedProcedureList = orderedProcedureList;
	}

	public List<TemplateMasterDto> getLetterTemplateList() {
		return letterTemplateList;
	}

	public void setLetterTemplateList(List<TemplateMasterDto> letterTemplateList) {
		this.letterTemplateList = letterTemplateList;
	}

	public List<TemplateMasterDto> getCertTemplateList() {
		return certTemplateList;
	}

	public void setCertTemplateList(List<TemplateMasterDto> certTemplateList) {
		this.certTemplateList = certTemplateList;
	}

	public List<ObservationDto> getObservationList() {
		return observationList;
	}

	public void setObservationList(List<ObservationDto> observationList) {
		this.observationList = observationList;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public List<ComplaintAppointmentDetailsDto> getListComplaintAppointmentDetailsDto() {
		return listComplaintAppointmentDetailsDto;
	}

	public void setListComplaintAppointmentDetailsDto(List<ComplaintAppointmentDetailsDto> listComplaintAppointmentDetailsDto) {
		this.listComplaintAppointmentDetailsDto = listComplaintAppointmentDetailsDto;
	}

	public String getCeratedDate() {
		return ceratedDate;
	}

	public void setCeratedDate(String ceratedDate) {
		this.ceratedDate = ceratedDate;
	}

	public List<MedicationHistoryDto> getDetailedMedicationsHistory() {
		return detailedMedicationsHistory;
	}

	public void setDetailedMedicationsHistory(List<MedicationHistoryDto> detailedMedicationsHistory) {
		this.detailedMedicationsHistory = detailedMedicationsHistory;
	}

	public List<MedicationDTO> getDetailedMedications() {
		return detailedMedications;
	}

	public void setDetailedMedications(List<MedicationDTO> detailedMedications) {
		this.detailedMedications = detailedMedications;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public List<CoversheetTemplateMasterDto> getTemplateTypeList() {
		return templateTypeList;
	}

	public void setTemplateTypeList(List<CoversheetTemplateMasterDto> templateTypeList) {
		this.templateTypeList = templateTypeList;
	}

	public Integer getParentTemplateId() {
		return parentTemplateId;
	}

	public void setParentTemplateId(Integer parentTemplateId) {
		this.parentTemplateId = parentTemplateId;
	}



	public List<ComplaintAppointmentMapperDto> getListComplaintData() {
		return listComplaintData;
	}

	public void setListComplaintData(List<ComplaintAppointmentMapperDto> listComplaintData) {
		this.listComplaintData = listComplaintData;
	}

	public List<DiagnosisPatientAppointmentMapperDto> getListDiagnosisData() {
		return listDiagnosisData;
	}

	public void setListDiagnosisData(List<DiagnosisPatientAppointmentMapperDto> listDiagnosisData) {
		this.listDiagnosisData = listDiagnosisData;
	}

	public List<InvestigationOrderMasterNewDto> getListLabOrderData() {
		return listLabOrderData;
	}

	public void setListLabOrderData(List<InvestigationOrderMasterNewDto> listLabOrderData) {
		this.listLabOrderData = listLabOrderData;
	}

	public List<CoversheetInstructionDto> getListAdviceData() {
		return listAdviceData;
	}

	public void setListAdviceData(List<CoversheetInstructionDto> listAdviceData) {
		this.listAdviceData = listAdviceData;
	}

	public List<MedicationDTO> getListPrescriptionData() {
		return listPrescriptionData;
	}

	public void setListPrescriptionData(List<MedicationDTO> listPrescriptionData) {
		this.listPrescriptionData = listPrescriptionData;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public List<ComplaintAppointmentMapperDto> getListComplaintFavoriteData() {
		return listComplaintFavoriteData;
	}

	public void setListComplaintFavoriteData(List<ComplaintAppointmentMapperDto> listComplaintFavoriteData) {
		this.listComplaintFavoriteData = listComplaintFavoriteData;
	}

	public List<DiagnosisPatientAppointmentMapperDto> getListDiagnosisFavoriteData() {
		return listDiagnosisFavoriteData;
	}

	public void setListDiagnosisFavoriteData(List<DiagnosisPatientAppointmentMapperDto> listDiagnosisFavoriteData) {
		this.listDiagnosisFavoriteData = listDiagnosisFavoriteData;
	}

	public List<InvestigationOrderMasterNewDto> getListLabOrderFavoriteData() {
		return listLabOrderFavoriteData;
	}

	public void setListLabOrderFavoriteData(List<InvestigationOrderMasterNewDto> listLabOrderFavoriteData) {
		this.listLabOrderFavoriteData = listLabOrderFavoriteData;
	}

	public List<CoversheetInstructionDto> getListAdviceFavoriteData() {
		return listAdviceFavoriteData;
	}

	public void setListAdviceFavoriteData(List<CoversheetInstructionDto> listAdviceFavoriteData) {
		this.listAdviceFavoriteData = listAdviceFavoriteData;
	}

	public List<MedicationDTO> getListPrescriptionFavoriteData() {
		return listPrescriptionFavoriteData;
	}

	public void setListPrescriptionFavoriteData(List<MedicationDTO> listPrescriptionFavoriteData) {
		this.listPrescriptionFavoriteData = listPrescriptionFavoriteData;
	}

	public List<ComplaintAppointmentDetailsDto> getListComplaintDataSave() {
		return listComplaintDataSave;
	}

	public void setListComplaintDataSave(List<ComplaintAppointmentDetailsDto> listComplaintDataSave) {
		this.listComplaintDataSave = listComplaintDataSave;
	}
	
	public List<PatientRegistrationDto> getListPatientRemark() {
		return listPatientRemark;
	}

	public void setListPatientRemark(List<PatientRegistrationDto> listPatientRemark) {
		this.listPatientRemark = listPatientRemark;
	}
	
	public List<QuickNotesMapperDto> getListQuickNotes() {
		return listQuickNotes;
	}

	public void setListQuickNotes(List<QuickNotesMapperDto> listQuickNotes) {
		this.listQuickNotes = listQuickNotes;
	}

	public List<AppointmentVitalMapperDto> getListAppointmentVitalMapperDto() {
		return listAppointmentVitalMapperDto;
	}

	public void setListAppointmentVitalMapperDto(List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto) {
		this.listAppointmentVitalMapperDto = listAppointmentVitalMapperDto;
	}

	public String getPatientRemark() {
		return patientRemark;
	}

	public void setPatientRemark(String patientRemark) {
		this.patientRemark = patientRemark;
	}

	public Integer getRouteId() {
		return routeId;
	}

	public void setRouteId(Integer routeId) {
		this.routeId = routeId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getQuantityForDoseCal() {
		return quantityForDoseCal;
	}

	public void setQuantityForDoseCal(Integer quantityForDoseCal) {
		this.quantityForDoseCal = quantityForDoseCal;
	}

	public Integer getStrengthForDoseCal() {
		return strengthForDoseCal;
	}

	public void setStrengthForDoseCal(Integer strengthForDoseCal) {
		this.strengthForDoseCal = strengthForDoseCal;
	}

	public boolean isInstructionTextFlag() {
		return instructionTextFlag;
	}

	public void setInstructionTextFlag(boolean instructionTextFlag) {
		this.instructionTextFlag = instructionTextFlag;
	}

	public Integer getDoctorFavoriteId() {
		return doctorFavoriteId;
	}

	public void setDoctorFavoriteId(Integer doctorFavoriteId) {
		this.doctorFavoriteId = doctorFavoriteId;
	}

	public String getIsInstructionText() {
		return isInstructionText;
	}

	public void setIsInstructionText(String isInstructionText) {
		this.isInstructionText = isInstructionText;
	}
	
	public boolean getIsCovtemplate() {
		return isCovtemplate;
	}

	public void setIsCovtemplate(boolean isCovtemplate) {
		this.isCovtemplate = isCovtemplate;
	}

	public Date getStartDateNew() {
		return startDateNew;
	}

	public void setStartDateNew(Date startDateNew) {
		this.startDateNew = startDateNew;
	}

	public Date getEndDateNew() {
		return endDateNew;
	}

	public void setEndDateNew(Date endDateNew) {
		this.endDateNew = endDateNew;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
	}

	public Integer getServiceRenderingDeptId() {
		return serviceRenderingDeptId;
	}

	public void setServiceRenderingDeptId(Integer serviceRenderingDeptId) {
		this.serviceRenderingDeptId = serviceRenderingDeptId;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}
 
	
}
