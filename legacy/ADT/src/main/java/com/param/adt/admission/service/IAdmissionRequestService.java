package com.param.adt.admission.service;

import javax.transaction.Transactional;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitMasterDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.PatientRegistrationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAdmissionRequestService 
{
	
	@Transactional
	Response saveAdmissionRequest(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	@Transactional
	Response getPatientListLike(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;
	
	@Transactional
	Response getPatientVisitDetailsById(VisitMasterDto visitMasterDto) throws ApplicationException;

	@Transactional
	Response getDoctorListAganistSpeciality(DoctorSpecialityMapperDto doctorSpecialityMapper) throws ApplicationException;
	
	Response saveAdmissionPatientMapper(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

	Response saveWaitingListNumber(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

	@Transactional
	Response getDoctorsListAganistSpecialitys(DoctorSpecialityMapperDto doctorSpecialityMapper)throws ApplicationException;
	
	@Transactional
	public Response getNephrologyDoctorsList(DoctorSpecialityMapperDto doctorSpecialityMapper)throws ApplicationException;

	@Transactional
	public Response getPastAdmissionDetails(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

	@Transactional
	Response getPatientListLikeForERMapOfBed(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;


    Response getAdmissionForIPDCoversheetByDate(AdmissionNoteDto dto) throws ApplicationException;

	Response getUniqueDateForIPDCoversheet(AdmissionNoteDto dto) throws ApplicationException;
}
