package com.param.adt.admission.service;

import javax.transaction.Transactional;

import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionNoteDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPendingRequestService 
{
	@Transactional
	Response getPendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	@Transactional
	Response getAvtivePatientCategoryList(Integer orgId) throws ApplicationException;

	@Transactional
	Response getAvtivePaymentEntitlementList() throws ApplicationException;

	@Transactional
	Response updatePendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto);

	@Transactional
	Response updateAdmissionRequestMapperDetails(AdmissionNoteDto admissionNoteDto, AdmissionNote admissionNoteObiject);

	@Transactional
	Response updateReserveBedDetails(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

	@Transactional
	Response uploadAdmissionDocuments(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	@Transactional
	Response getPendingAdmissionCount(AdmissionNoteDto admissionNoteDto) throws ApplicationException;
	
	@Transactional
	Response getOPDAdmissionRequestDetails(Integer encounterId, Integer patientId, Integer organizationid, Integer unitId) throws ApplicationException;

	@Transactional
	Response cancelPendingAdmissionRequest(AdmissionNoteDto admissionNoteDto);
	
	@Transactional
	Response getDayCareAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto)throws ApplicationException;
	
	@Transactional
	Response getAdmissionRequestDetailsForBookView(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

    Response getOpAdmissionListForIPDCoversheetByDate(AdmissionNoteDto dto) throws ApplicationException;

	Response getUniqueDateForIPDCoversheet(AdmissionNoteDto dto) throws ApplicationException;

    Response getPatientRegistrationTypes() throws ApplicationException;
}
