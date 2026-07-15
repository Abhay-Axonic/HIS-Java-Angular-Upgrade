package com.param.opd.coversheet.service;

import java.util.List;
import javax.transaction.Transactional;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IConsultDoctorService {

	@Transactional
	Response saveConsultDoctor(List<ConsultDoctorDto> consultDoctorDto)throws ApplicationException;

	@Transactional
	Response consultDoctorListByEncounter(Integer encounterId)throws ApplicationException;

	@Transactional
	Response inactiveConsultDoctor(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	@Transactional
	Response getConsultationPatientListForDocDesk(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	@Transactional
	Response consultDoctorListByAdmission(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	@Transactional
	Response updateReferredStatus(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	@Transactional
	Response consultDoctorListForBookView(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	@Transactional
	Response updateConsultDoctorNotes(ConsultDoctorDto consultDoctorDto)throws ApplicationException;

	
}
