package com.param.adt.admission.dao;

import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.admission.model.AdmissionPatientDocuments;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionNoteDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPendingRequestDao extends IGenericDao<AdmissionNote, Integer>
{
	
	Response getPendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	Response getAvtivePatientCategoryList(Integer orgId) throws ApplicationException;

	Response getAvtivePaymentEntitlementList() throws ApplicationException;

	Response updatePendingAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	Response updateAdmissionRequestMapperDetails(AdmissionNoteDto admissionPatientMapperDto);

	Response getPreviousData(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	Response updateReserveBedDetails(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	Response uploadAdmissionDocuments(AdmissionPatientDocuments admissionPatientDocumentsDto) throws ApplicationException;

	Response getCount(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	Response getOPDAdmissionRequestDetails(Integer encounterId, Integer patientId, Integer organizationid, Integer unitId) throws ApplicationException;

	Response cancelPendingAdmissionRequest(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

	Response getDayCareAdmissionRequestDetails(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

	Response getAdmissionRequestDetailsForBookView(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

    Response getPatientRegistrationTypes() throws ApplicationException;
}
