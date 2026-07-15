package com.param.adt.admission.dao;

import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitMasterDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.PatientRegistrationDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAdmissionRequestDao extends IGenericDao<AdmissionNote, Integer>
{	
	
	Response getPatientListLike(PatientRegistrationDto patientRegistrationDto);
	
	Response getPatientVisitDetailsById(VisitMasterDto visitMasterDto);
	
	Response getDoctorListAganistSpeciality(DoctorSpecialityMapperDto doctorSpecialityMapperDto);
	
	Response saveAdmissionRequest(AdmissionNoteDto admissionNoteDto);

	Response saveAdmissionPatientMapper(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	Response saveWaitingListNumber(AdmissionNoteDto admissionNoteDto)throws ApplicationException;

	Response getMaxWaitingListNumber(AdmissionNoteDto admissionNoteDto) throws ApplicationException;

	Response getDoctorsListAganistSpecialitys(DoctorSpecialityMapperDto doctorSpecialityMapper)throws ApplicationException ;

	Long getNextValAdmissionNoteId()throws ApplicationException ;

	Response isAlreadyRequestExist(AdmissionNoteDto admissionNoteDto)throws ApplicationException ;

	public Response getNephrologyDoctorsList(DoctorSpecialityMapperDto doctorSpecialityMapper)throws ApplicationException ;

	Response getPastAdmissionDetails(AdmissionNoteDto admissionNoteDto)throws ApplicationException ;

	Response getPatientListLikeForERMapOfBed(PatientRegistrationDto patientRegistrationDto)throws ApplicationException ;
	
}
